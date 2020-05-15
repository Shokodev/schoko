package com.bacnetbrowser.schoko.bacnetutils.services;


import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetObject;
import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.*;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.DiscoveryUtils;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DeviceService extends DeviceEventAdapter {
    public static LocalDevice localDevice;
    public static Set<BACnetDevice> bacnetDevices = new HashSet<>();
    public static HashMap<Integer, BACnetDevice> waitingRoomBacnetDevices = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(DeviceService.class);


    @Override
    public void iAmReceived(RemoteDevice d) {
        BACnetDevice bacnetDevice = new BACnetDevice(localDevice, d.getInstanceNumber(), d.getAddress(),
                d.getSegmentationSupported(), d.getVendorIdentifier(), d.getMaxAPDULengthAccepted());
        waitingRoomBacnetDevices.put(bacnetDevice.getInstanceNumber(),bacnetDevice);
        LOG.info("Remote device " + d.getInstanceNumber() + " registered in waiting room of LocalDevice");
    }

    /**
     * Create Local Device and add listener
     */
    public void createLocalDevice()  {
        rebaseLocalDeviceIfExists();
        IpNetworkBuilder ipNetworkBuilder = new IpNetworkBuilder();
        ipNetworkBuilder.withLocalBindAddress(IpNetwork.DEFAULT_BIND_IP);
        ipNetworkBuilder.withBroadcast("255.255.255.255",IpNetwork.BVLC_TYPE);
        ipNetworkBuilder.withPort(Integer.parseInt(SettingsHandler.port,16));
        DefaultTransport transport = new DefaultTransport(ipNetworkBuilder.build());
        localDevice = new LocalDevice(Integer.parseInt(SettingsHandler.localDeviceID), transport);
        localDevice.getEventHandler().addListener(this);
        LOG.info("Try to initialize localdevice " + SettingsHandler.localDeviceID);
        try {
            localDevice.initialize();
        } catch(Exception e){
            LOG.error("LocalDevice initialize failed, restart the application may solve this problem");
        }
        LOG.info("Successfully created LocalDevice " + localDevice.getInstanceNumber());
        scanForRemoteDevices(SettingsHandler.scanSeconds);
    }

    public void readFinalAddedDevices(){
        setLocalDeviceAsAlarmReceiver();
        scanAndAddAllObjects();
    }

    /**
     *Send WhoIs request to the BACnet network
     */
    private void scanForRemoteDevices(int scanSeconds)  {
        LOG.info("Scan for remote devices.........");
        try {
            localDevice.startRemoteDeviceDiscovery();
            Thread.sleep(scanSeconds * 1000);
            //End scan after 5s if no device is found
            if(waitingRoomBacnetDevices.isEmpty()){
                localDevice.terminate();
                LOG.warn("No remote devices found");
            } else {
                localDevice.getEventHandler().removeListener(this);
                getRemoteDeviceInformation();
            }
        }catch(InterruptedException bac){
            LOG.warn("Network scan failure, restart the application may solve this problem");
        }
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
        if (!bacnetDevices.isEmpty()){
            for (BACnetDevice bacnetDevice : bacnetDevices) {
                SequenceOf<ObjectIdentifier> oids = null;
                try {
                    oids = RequestUtils.getObjectList(localDevice,bacnetDevice);
                } catch (BACnetException bac1){
                    System.err.println("No objects in " + bacnetDevice + "found");
                }
                if (oids != null ) {
                    for (ObjectIdentifier oid : oids) {
                        if (oid.getObjectType().equals(ObjectType.notificationClass)){
                            try {
                            RequestUtils.addListElement(localDevice,bacnetDevice,oid,PropertyIdentifier.recipientList,creatAlarmDestination());
                            }catch(BACnetException bac){
                               LOG.warn("No notification classes found at remote " + bacnetDevice);
                            }
                            LOG.info("LocalDevice " + localDevice.getInstanceNumber() + " as receiver registered to: " + oid.toString() + " @ " + bacnetDevice.getObjectIdentifier());
                        }}}
            }
        } else {
            LOG.warn("No destinations added");
        }

    }

    /**
     * Reads and save more information about each remote device
     */
    private void getRemoteDeviceInformation() {
        for (BACnetDevice bacnetDevice : waitingRoomBacnetDevices.values()) {
            try {
                DiscoveryUtils.getExtendedDeviceInformation(localDevice, bacnetDevice);
            } catch (BACnetException e) {
                LOG.error("Can't read further information from device {}", bacnetDevice);
            }
        }
    }

    /**
     * Reset local device if creating is called again in runtime
     */
    private void rebaseLocalDeviceIfExists(){
        if(localDevice != null){
            localDevice.terminate();
            LOG.info("*********************Reset*********************");

        }
    }

    /**
     * Reads all BACnet Objects of all remote devises
     */
    private void scanAndAddAllObjects(){
        for (BACnetDevice bacnetDevice: bacnetDevices) {
            try {
                SequenceOf<ObjectIdentifier> oids = RequestUtils.getObjectList(localDevice,bacnetDevice);
                for (ObjectIdentifier oid : oids) {
                    BACnetObject bacnetObject = new BACnetObject(oid,bacnetDevice);
                    bacnetDevice.getBacnetObjects().add(bacnetObject);
                    }} catch (BACnetException | NullPointerException e) {
                LOG.warn("Failed to read objects");
            }
        }
    }

    public static BACnetDevice getBACnetDevice(ObjectIdentifier oid){
        for(BACnetDevice device: bacnetDevices){
            if(device.getObjectIdentifier().equals(oid)){
                return device;
            }
        }
        return null;
    }


}
