package com.bacnetbrowser.schoko.model.datahandler;


import com.bacnetbrowser.schoko.model.services.EventService;
import com.bacnetbrowser.schoko.model.services.SubscriptionService;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Destination;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.Recipient;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

;

/**
 * This class used to generate local device an get remote devices from bacnet network
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class DeviceHandler {

    private LocalDevice localDevice;
    @Autowired
    private EventHandler eventHandler;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private EventService eventService;


    /**
     * Create Local Device and add listener
     *
     * @param port port to read Network
     * @throws Exception local Device
     */
    public void createLocalDevice(int port) throws Exception {
        IpNetwork ipNetwork = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP,port);
        Transport transport = new Transport(ipNetwork);
        int localDevice_ID = 10001;
        localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(subscriptionService);
        localDevice.getEventHandler().addListener(eventService);
        localDevice.initialize();
        System.out.println("Successfully created LocalDevice " + localDevice.getConfiguration().getInstanceId());
        scanForRemoteDevices();
        Thread.sleep(2000);
        setLocalDeviceAsAlarmReceiver();

    }

    private void scanForRemoteDevices() throws BACnetException {
        System.out.println("Scan for remote devices.........");
        localDevice.sendGlobalBroadcast(new WhoIsRequest());
    }

    public LocalDevice getLocalDevice() {
        return localDevice;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    /**
     * Create a destination objects to send to notificationClass objects in remote devices
     * @return destination as required
     */
    private Destination creatAlarmDestination(){
        Recipient recipient = new Recipient(localDevice.getConfiguration().getId());
        EventTransitionBits eventTransitionBits = new EventTransitionBits(true,true,true);
     return new Destination(recipient,new UnsignedInteger(1),new Boolean(true),eventTransitionBits);
    }

    /**
     * Looks in all remote devices for notificationsClass objects ad set the LocalDevice as receiver destination
     * @throws BACnetException from network
     */
    private void setLocalDeviceAsAlarmReceiver() {
        if (localDevice.getRemoteDevices().size() > 0){
        for (RemoteDevice remoteDevice : localDevice.getRemoteDevices()) {
            List<ObjectIdentifier> oids = null;
            try {
                 oids = ((SequenceOf<ObjectIdentifier>)
                        RequestUtils.sendReadPropertyAllowNull(
                                localDevice, remoteDevice, remoteDevice.getObjectIdentifier(),
                                PropertyIdentifier.objectList)).getValues();
            } catch (BACnetException bac1){
                System.out.println("No objects in " + remoteDevice + "found");
            }

            for (ObjectIdentifier oid : oids) {
                if (oid.getObjectType().equals(ObjectType.notificationClass)){
                WritePropertyRequest request = new WritePropertyRequest(oid, PropertyIdentifier.recipientList, null, creatAlarmDestination(), new UnsignedInteger(16));

               try {
                   localDevice.send(remoteDevice, request);
               }catch(BACnetException bac2){
                   System.out.println("No notification classes found");
               }
                System.out.println("LocalDevice " + localDevice.getConfiguration().getInstanceId() + " as receiver registered to: " + oid.toString() + " @ " + remoteDevice.getObjectIdentifier());

            }}
        }
    } else {
            System.out.println("No remote devices found");
        }

    }

}
