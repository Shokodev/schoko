package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.models.BACnetDevice;
import com.bacnetbrowser.schoko.model.models.BACnetObject;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.*;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DeviceService extends DeviceEventAdapter {
    public static LocalDevice localDevice;
    public static ArrayList<BACnetDevice> bacnetDevices = new ArrayList<>();
    static final Logger LOG = LoggerFactory.getLogger(DeviceService.class);

    private ObjectService objectService;
    private EventService eventService;



    @Autowired
    public DeviceService(ObjectService objectService, EventService eventService) {
        this.objectService = objectService;
        this.eventService = eventService;
    }

    @Override
    public void iAmReceived(RemoteDevice d) {
        BACnetDevice bacnetDevice = new BACnetDevice(localDevice, d.getInstanceNumber(), d.getAddress(),d);
        bacnetDevices.add(bacnetDevice);
        LOG.info("Remote device " + d.getInstanceNumber() + " registered at LocalDevice");
    }

    /**
     * Create Local Device and add listener
     *
     * @param port port to read BACnet Network
     */
    public void createLocalDevice(Integer port, Integer localDevice_ID)  {
        rebaseLocalDeviceIfExists();
        IpNetworkBuilder ipNetworkBuilder = new IpNetworkBuilder();
        ipNetworkBuilder.withLocalBindAddress(IpNetwork.DEFAULT_BIND_IP);
        ipNetworkBuilder.withBroadcast("255.255.255.255",IpNetwork.BVLC_TYPE);
        ipNetworkBuilder.withPort(port);
        DefaultTransport transport = new DefaultTransport(ipNetworkBuilder.build());
        localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(eventService);
        localDevice.getEventHandler().addListener(objectService);
        localDevice.getEventHandler().addListener(this);
        LOG.info("Try to initialize localdevice " + localDevice_ID);
        try {
            localDevice.initialize();
        } catch(Exception e){
            LOG.warn("LocalDevice initialize failed, restart the application may solve this problem");
        }
        LOG.info("Successfully created LocalDevice " + localDevice.getInstanceNumber());
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
        LOG.info("Scan for remote devices.........");
        try {
            WhoIsRequest request = new WhoIsRequest();
            localDevice.sendGlobalBroadcast(request);
            Thread.sleep(1000 * 5);
            //End scan after 5s if no device is found
            if(!alertNoDeviceFound()){
                localDevice.terminate();
            }

        }catch(InterruptedException bac){
            LOG.info("Network scan failure, restart the application may solve this problem");
        }
    }

    /**
     * Checks list for remote devices at LocalDevice after network scan
     * @return massage and boolean
     */
    private boolean alertNoDeviceFound(){
        if (bacnetDevices.isEmpty()){
            LOG.warn("No remote devices found");
            return false;
        }
        return true;
    }

    /**
     * Create a destination object to send to notificationClass objects in remote devices
     * @return destination as required
     */
    private Destination creatAlarmDestination()  {
        Recipient recipient = new Recipient(localDevice.getId());
        EventTransitionBits eventTransitionBits = new EventTransitionBits(true,true,true);
        return new Destination(recipient,new UnsignedInteger(1),Boolean.TRUE,eventTransitionBits);
    }

    /**
     * Looks in all remote devices for notificationsClass objects ad set the LocalDevice as receiver destination
     *
     */
    private void setLocalDeviceAsAlarmReceiver() {
        if (bacnetDevices.size() > 0){
            for (BACnetDevice bacnetDevice : bacnetDevices) {
                List<ObjectIdentifier> oids = null;
                try {
                    oids = ((SequenceOf<ObjectIdentifier>)
                            RequestUtils.sendReadPropertyAllowNull(
                                    localDevice, bacnetDevice.getBacnetDeviceInfo(), bacnetDevice.getObjectIdentifier(),
                                    PropertyIdentifier.objectList)).getValues();
                } catch (BACnetException bac1){
                    System.err.println("No objects in " + bacnetDevice + "found");
                }
                if (oids != null ) {
                    for (ObjectIdentifier oid : oids) {
                        if (oid.getObjectType().equals(ObjectType.notificationClass)){
                            try {
                            RequestUtils.addListElement(localDevice,bacnetDevice.getBacnetDeviceInfo(),oid,PropertyIdentifier.recipientList,creatAlarmDestination());
                            }catch(BACnetException bac){
                                System.err.println("No notification classes found at remote " + bacnetDevice);
                            }
                            System.out.println("LocalDevice " + localDevice.getInstanceNumber() + " as receiver registered to: " + oid.toString() + " @ " + bacnetDevice.getObjectIdentifier());
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

        for (BACnetDevice bacnetDevice : bacnetDevices) {
            bacnetDevice.setDeviceProperty(PropertyIdentifier.objectName, bacnetDevice.readProperty(PropertyIdentifier.objectName));
            bacnetDevice.setDeviceProperty(PropertyIdentifier.vendorName, bacnetDevice.readProperty(PropertyIdentifier.vendorName));
            bacnetDevice.setDeviceProperty(PropertyIdentifier.protocolServicesSupported, bacnetDevice.readProperty(PropertyIdentifier.protocolServicesSupported));
            bacnetDevice.setDeviceProperty(PropertyIdentifier.protocolVersion, bacnetDevice.readProperty(PropertyIdentifier.protocolVersion));
            bacnetDevice.setDeviceProperty(PropertyIdentifier.protocolRevision, bacnetDevice.readProperty(PropertyIdentifier.protocolRevision));
            bacnetDevice.setDeviceProperty(PropertyIdentifier.description, bacnetDevice.readProperty(PropertyIdentifier.description));
            bacnetDevice.setDeviceProperty(PropertyIdentifier.applicationSoftwareVersion, bacnetDevice.readProperty(PropertyIdentifier.applicationSoftwareVersion));
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

        for (BACnetDevice bacnetDevice: bacnetDevices) {
            try {
                List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>)
                        RequestUtils.sendReadPropertyAllowNull(
                                DeviceService.localDevice, bacnetDevice.getBacnetDeviceInfo(), bacnetDevice.getObjectIdentifier(),
                                PropertyIdentifier.objectList)).getValues();
                for (ObjectIdentifier oid : oids) {
                    BACnetObject bacnetObject = new BACnetObject(oid,bacnetDevice);
                    bacnetDevice.getBacnetObjects().add(bacnetObject);
                    }} catch (BACnetException | NullPointerException e) {
                System.out.println("Failed to read objects");
            }
        }
    }

    public static ArrayList<BACnetDevice> getBacnetDevices() {
        return bacnetDevices;
    }

    public static BACnetDevice getBacnetDevice(ObjectIdentifier oid){
        for(BACnetDevice dv : bacnetDevices){
            if(dv.getObjectIdentifier().equals(oid)){
                return dv;
            }
        }
        return null;
    }

}
