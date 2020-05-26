package com.bacnetbrowser.schoko;


import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.service.unconfirmed.IAmRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;



public class DeviceTest {
    static LocalDevice localDevice;

    public static void main(String[] args) throws Exception {
        int localDevice_ID = 10001;
        IpNetworkBuilder ipNetworkBuilder = new IpNetworkBuilder();
        ipNetworkBuilder.withLocalBindAddress(IpNetwork.DEFAULT_BIND_IP);
        ipNetworkBuilder.withBroadcast("255.255.255.255", IpNetwork.BVLC_TYPE);
        ipNetworkBuilder.withPort(47808);
        IpNetwork ipNetwork = ipNetworkBuilder.build();
        DefaultTransport transport = new DefaultTransport(ipNetwork);
        localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(new Listener());
        localDevice.initialize();
        localDevice.startRemoteDeviceDiscovery();
    }

    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            Device device = new Device(localDevice, d.getInstanceNumber(), d.getAddress(), d.getSegmentationSupported(), d.getVendorIdentifier(),
                    d.getMaxAPDULengthAccepted());
            try {
                SequenceOf<ObjectIdentifier> oids = RequestUtils.getObjectList(localDevice,device);
                for(ObjectIdentifier oid : oids.getValues()){
                    System.out.println(oid);
                }
            } catch (BACnetException e) {
                e.printStackTrace();
            }
        }
    }

    static class Device extends RemoteDevice{

        public Device(LocalDevice localDevice, int instanceNumber, Address address, Segmentation segmentation,
                      int vendorIdentifier,int maxAPDULengthAccepted) {
            super(localDevice, instanceNumber, address);
            this.setDeviceProperty(PropertyIdentifier.maxApduLengthAccepted, new UnsignedInteger(maxAPDULengthAccepted));
            this.setDeviceProperty(PropertyIdentifier.segmentationSupported, segmentation);
            this.setDeviceProperty(PropertyIdentifier.vendorIdentifier, new UnsignedInteger(vendorIdentifier));
        }

    }

}


