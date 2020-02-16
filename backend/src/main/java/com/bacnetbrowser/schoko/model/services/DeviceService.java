package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.models.BACnetObject;
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


@Component
public class DeviceService  {
    public static LocalDevice localDevice;

    private ObjectService objectService;
    private EventService eventService;



    @Autowired
    public DeviceService(ObjectService objectService, EventService eventService) {
        this.objectService = objectService;
        this.eventService = eventService;
    }

    /**
     * Create Local Device and add listener
     *
     * @param port port to read BACnet Network
     */
    public void createLocalDevice(Integer port, Integer localDevice_ID)  {
        rebaseLocalDeviceIfExists();
        IpNetwork ipNetwork = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, port);
        Transport transport = new Transport(ipNetwork);
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
        scanAndAddAllObjects();
        eventService.getEventInformation(true);
        eventService.getEventInformation(false);
    }

    /**
     *Send WhoIs request to the BACnet network
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
     * Checks list for remote devices at LocalDevice after network scan
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
     * Create a destination object to send to notificationClass objects in remote devices
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

                if (oids != null ) {
                    for (ObjectIdentifier oid : oids) {
                        if (oid.getObjectType().equals(ObjectType.notificationClass)){
                            WritePropertyRequest request = new WritePropertyRequest(oid, PropertyIdentifier.recipientList, null, creatAlarmDestination(), new UnsignedInteger(16));

                            try {
                                localDevice.send(remoteDevice, request);
                            }catch(BACnetException bac2){
                                System.err.println("No notification classes found at remote " + remoteDevice);
                            }
                            System.out.println("LocalDevice " + localDevice.getConfiguration().getInstanceId() + " as receiver registered to: " + oid.toString() + " @ " + remoteDevice.getObjectIdentifier());

                        }}}
            }
        } else {
            System.err.println("No destinations added");
        }

    }

    /**
     * Reads and save more information about each remote device
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

                ack = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.applicationSoftwareVersion));
                remoteDevice.setApplicationSoftwareVersion(ack.getValue().toString());

            } catch (BACnetException bac){
                System.err.println("Cant read remote device information of" + remoteDevice.getVendorName());
            }
        }
    }

    /**
     * Reset local device if creating is called again in runtime
     */
    private void rebaseLocalDeviceIfExists(){
        if(localDevice != null){
            localDevice.terminate();
            System.out.println("*********************Reset*********************");

        }
    }

    /**
     * Reads all BACnet Objects of all remote devises
     */
    private void scanAndAddAllObjects(){

        for (RemoteDevice remoteDevice : DeviceService.localDevice.getRemoteDevices()) {
            try {
                List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>)
                        RequestUtils.sendReadPropertyAllowNull(
                                DeviceService.localDevice, remoteDevice, remoteDevice.getObjectIdentifier(),
                                PropertyIdentifier.objectList)).getValues();
                for (ObjectIdentifier oid : oids) {
                    BACnetObject bacnetObject = new BACnetObject(oid,remoteDevice);
                    String tempObjectName = ((ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.objectName))).getValue().toString();
                    bacnetObject.setObjectName(tempObjectName);
                    remoteDevice.setObject(bacnetObject);
                    }} catch (BACnetException | NullPointerException e) {
                System.out.println("Failed to read objects");
            }
        }
    }





}
