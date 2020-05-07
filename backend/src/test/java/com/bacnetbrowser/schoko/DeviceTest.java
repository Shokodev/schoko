package com.bacnetbrowser.schoko;


import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
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
        ipNetwork.enableBBMD();
        localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(new Listener());
        localDevice.initialize();
    }

    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            Device device = new Device(localDevice, d.getInstanceNumber(), d.getAddress(), d.getSegmentationSupported());
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
        Segmentation segmentation;
        int maxAPDULengthAccepted = 1476;

        public Device(LocalDevice localDevice, int instanceNumber, Address address, Segmentation segmentation) {
            super(localDevice, instanceNumber, address);
            this.segmentation = segmentation;
        }

        @Override
        public Segmentation getSegmentationSupported() {
            return this.segmentation;
        }

        @Override
        public int getMaxAPDULengthAccepted() {
            return maxAPDULengthAccepted;
        }
    }

}


