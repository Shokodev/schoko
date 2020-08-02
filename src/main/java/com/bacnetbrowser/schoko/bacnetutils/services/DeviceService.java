package com.bacnetbrowser.schoko.bacnetutils.services;


import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetObject;
import com.bacnetbrowser.schoko.bacnetutils.models.PermanentDevices;
import com.bacnetbrowser.schoko.bacnetutils.models.WaitingRoomDeviceFrontend;
import com.bacnetbrowser.schoko.databaseconfig.DeviceRepository;
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
    public static Set<BACnetDevice> bacnetDevices = new HashSet<>(); //final device list
    public static HashMap<Integer, BACnetDevice> waitingRoomBacnetDevices = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository){
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void iAmReceived(RemoteDevice d) {
        BACnetDevice bacnetDevice = new BACnetDevice(localDevice, d.getInstanceNumber(), d.getAddress(),
                d.getSegmentationSupported(), d.getVendorIdentifier(), d.getMaxAPDULengthAccepted());
        getRemoteDeviceInformation(bacnetDevice);
        if(deviceRepository.findByDeviceId(d.getInstanceNumber()) != null){
            waitingRoomBacnetDevices.put(bacnetDevice.getInstanceNumber(), bacnetDevice);
            LOG.info("Remote device instance: " + bacnetDevice.getInstanceNumber()
                    + " found in DB -> marked for automatic reimport");
        } else {
            waitingRoomBacnetDevices.put(bacnetDevice.getInstanceNumber(), bacnetDevice);
            LOG.info("Remote device " + d.getInstanceNumber() + " registered in waiting room of LocalDevice");
        }
    }

    //Methods for LocalDevice and Network


    /**
     * Create Local Device and add listener
     */
    public void createLocalDevice() {
        rebaseLocalDeviceIfExists();
        IpNetworkBuilder ipNetworkBuilder = new IpNetworkBuilder();
        ipNetworkBuilder.withLocalBindAddress(IpNetwork.DEFAULT_BIND_IP);
        ipNetworkBuilder.withBroadcast("255.255.255.255", IpNetwork.BVLC_TYPE);
        ipNetworkBuilder.withPort(Integer.parseInt(SettingsHandler.port, 16));
        DefaultTransport transport = new DefaultTransport(ipNetworkBuilder.build());
        localDevice = new LocalDevice(Integer.parseInt(SettingsHandler.localDeviceID), transport);
        localDevice.getEventHandler().addListener(this);
        LOG.info("Try to initialize localdevice " + SettingsHandler.localDeviceID);
        try {
            localDevice.initialize();
        } catch (Exception e) {
            LOG.error("LocalDevice initialize failed, restart the application may solve this problem");
        }
        LOG.info("Successfully created LocalDevice " + localDevice.getInstanceNumber());
        scanForRemoteDevices(SettingsHandler.scanSeconds);
    }

    /**
     * Reset local device if creating is called again in runtime
     */
    private void rebaseLocalDeviceIfExists() {
        waitingRoomBacnetDevices.clear();
        if (localDevice != null) {
            localDevice.terminate();
            LOG.info("*********************Reset*********************");

        }
    }

    /**
     * Send WhoIs request to the BACnet network
     */
    private void scanForRemoteDevices(int scanSeconds) {
        LOG.info("Scan for remote devices.........");
        try {
            localDevice.startRemoteDeviceDiscovery();
            Thread.sleep(scanSeconds * 1000);
            //End scan after scanSeconds if no device is found
            if (waitingRoomBacnetDevices.isEmpty() && bacnetDevices.isEmpty()) {
                LOG.warn("No remote devices found");
            }
            localDevice.getEventHandler().removeListener(this);
        } catch (InterruptedException bac) {
            LOG.warn("Network scan failure, restart the application may solve this problem");
        }
    }

    /**
     * Reads and save more information about each remote device
     */
    private void getRemoteDeviceInformation(BACnetDevice bacnetDevice) {
            try {
                DiscoveryUtils.getExtendedDeviceInformation(localDevice, bacnetDevice);
            } catch (BACnetException e) {
                LOG.error("Can't read further information from device {}", bacnetDevice);
            }
    }

    // Methods for final devices

    /**
     * Used to update final device list
     * -> no longer needed devices will be removed and the local device will be removed as receiver
     */
    public void updateFinalDeviceList(ArrayList<Integer> instanceNumbers){
        removeLocalDeviceAsAlarmReceiver();
        DeviceService.bacnetDevices.clear();
        deviceRepository.deleteAll();
        instanceNumbers.forEach(number -> {
            BACnetDevice device = DeviceService.waitingRoomBacnetDevices.get(number);
            if(device != null) {
                DeviceService.bacnetDevices.add(device);
                deviceRepository.save(new PermanentDevices(number));
            }else {
                LOG.warn("Device with instance number: {} does not exist in waiting room ->" +
                        "flag removed from DB!", number);
            }
        });
        LOG.info("New amount of devices in DB list = {}", (long) deviceRepository.findAll().size());
        LOG.info("{} BACnet devices finally registered at local device -> Read objects of all devices ...", DeviceService.bacnetDevices.size());
        scanAndAddAllObjectsOfFinalDeviceList();
        DeviceService.waitingRoomBacnetDevices.clear();
    }

    /**
     * Remove local device as alarm receiver on all nc objects in the given list
     */
    private void removeLocalDeviceAsAlarmReceiver(){
        for (BACnetDevice bacnetDevice : DeviceService.bacnetDevices) {
            for (BACnetObject bacnetObject : bacnetDevice.getBacnetObjects()) {
                if (bacnetObject.getObjectType().equals(ObjectType.notificationClass)) {
                    try {
                        RequestUtils.removeListElement(localDevice,bacnetDevice,bacnetObject.getObjectIdentifier(),
                                PropertyIdentifier.recipientList,creatAlarmDestination());
                    } catch (BACnetException e) {
                        LOG.warn("Could not delete destination  @ {} ", bacnetObject.getObjectName());
                    }
                }
            }
        }
    }

    /**
     * Reads all BACnet Objects of all devices
     *  -> Register localDevice to al NC obejects
     */
    public void scanAndAddAllObjectsOfFinalDeviceList() {
        for (BACnetDevice bacnetDevice : DeviceService.bacnetDevices) {
            try {
                SequenceOf<ObjectIdentifier> oids = RequestUtils.getObjectList(localDevice, bacnetDevice);
                for (ObjectIdentifier oid : oids) {
                    BACnetObject bacnetObject = new BACnetObject(oid, bacnetDevice);
                    bacnetDevice.getBacnetObjects().add(bacnetObject);
                    if(oid.getObjectType().equals(ObjectType.notificationClass)){
                        try {
                            RequestUtils.addListElement(localDevice, bacnetDevice, oid, PropertyIdentifier.recipientList, creatAlarmDestination());
                        } catch (BACnetException bac) {
                            LOG.warn("Could not set localDevice as receiver to: {}  @ {} ",oid, bacnetDevice.getName());
                        }
                    }
                }
            } catch (BACnetException | NullPointerException e) {
                LOG.warn("Failed to read objects");
            }
        }
    }

    /**
     * Create a destination object to send to notificationClass objects in remote devices
     *
     * @return destination as required
     */
    private Destination creatAlarmDestination() {
        Recipient recipient = new Recipient(localDevice.getId());
        EventTransitionBits eventTransitionBits = new EventTransitionBits(true, true, true);
        return new Destination(recipient, new UnsignedInteger(1), Boolean.TRUE, eventTransitionBits);
    }

    //Getters
    public static BACnetDevice getBACnetDevice(ObjectIdentifier oid) {
        for (BACnetDevice device : bacnetDevices) {
            if (device.getObjectIdentifier().equals(oid)) {
                return device;
            }
        }
        return null;
    }
}

