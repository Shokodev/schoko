package com.bacnetbrowser.schoko.model.datahandler;


import com.bacnetbrowser.schoko.model.services.EventService;
import com.bacnetbrowser.schoko.model.services.ObjectService;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.*;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;



/**
 * This class used to generate local device an get remote devices from bacnet network
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class DeviceHandler {

    public static LocalDevice localDevice;

    private ObjectService objectService;
    private EventService eventService;

    @Autowired
    public DeviceHandler(ObjectService objectService, EventService eventService) {
        this.objectService = objectService;
        this.eventService = eventService;
    }

    /**
     * Create Local Device and add listener
     *
     * @param port port to read Network
     */
    public void createLocalDevice(Integer port)  {
        rebaseLocalDevice();
        IpNetwork ipNetwork = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, port);
        Transport transport = new Transport(ipNetwork);
        int localDevice_ID = 10001;
        localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(eventService);
        localDevice.getEventHandler().addListener(objectService);
        try {
            localDevice.initialize();
        } catch(Exception e){
            System.err.println("LocalDevice initialize failed, restart the application may solve this problem");
        }
        System.out.println("Successfully created LocalDevice " + localDevice.getConfiguration().getInstanceId());
        scanForRemoteDevices();
        getRemoteDeviceInformation();
        setLocalDeviceAsAlarmReceiver();

    }

    /**
     *Send WhoIs request to the network
     */
    private void scanForRemoteDevices()  {
        System.out.println("Scan for remote devices.........");
        try {
            WhoIsRequest request = new WhoIsRequest();
            localDevice.sendGlobalBroadcast(request);
            Thread.sleep(1000 * 5);
            //End scan after 5s if no device is found
            if(!alertNoDeviceFound()){
                localDevice.terminate();
            }

        }catch(BACnetException | InterruptedException bac){
            System.err.println("Network scan failure, restart the application may solve this problem");
        }
    }

    /**
     * checks list for remote devices at LocalDevice after network scan
     * @return massage and boolean
     */
    private boolean alertNoDeviceFound(){
        if (localDevice.getRemoteDevices().isEmpty()){
            System.err.println("No remote devices found");
            return false;
        }
        return true;
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
     *
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
                System.err.println("No objects in " + remoteDevice + "found");
            }

            for (ObjectIdentifier oid : oids) {
                if (oid.getObjectType().equals(ObjectType.notificationClass)){
                WritePropertyRequest request = new WritePropertyRequest(oid, PropertyIdentifier.recipientList, null, creatAlarmDestination(), new UnsignedInteger(16));

               try {
                   localDevice.send(remoteDevice, request);
               }catch(BACnetException bac2){
                   System.err.println("No notification classes found at remote " + remoteDevice);
               }
                System.out.println("LocalDevice " + localDevice.getConfiguration().getInstanceId() + " as receiver registered to: " + oid.toString() + " @ " + remoteDevice.getObjectIdentifier());

            }}
        }
    } else {
            System.out.println("No destinations added");
        }

    }

    /**
     * read and save more information about each remote device
     */
    private void getRemoteDeviceInformation() {

        for (RemoteDevice remoteDevice : localDevice.getRemoteDevices()) {

            try {
                ObjectIdentifier oid = remoteDevice.getObjectIdentifier();

                ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.protocolServicesSupported));
                remoteDevice.setServicesSupported((ServicesSupported) ack.getValue());

                ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.objectName));
                remoteDevice.setName(ack.getValue().toString());

                ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.protocolVersion));
                remoteDevice.setProtocolVersion((UnsignedInteger) ack.getValue());

                ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.protocolRevision));
                remoteDevice.setProtocolRevision((UnsignedInteger) ack.getValue());

                ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.description));
                remoteDevice.setVendorName(ack.getValue().toString());
            } catch (BACnetException bac){
                System.err.println("Cant read remote device information");
            }
        }
    }

    /**
     * Reset local device if creating is called again in runtime
     */
    private void rebaseLocalDevice(){
        if(localDevice != null){
            localDevice.terminate();
            System.out.println("*********************Reset*********************");

        }
    }

}
