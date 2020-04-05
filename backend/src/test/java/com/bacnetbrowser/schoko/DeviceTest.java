package com.bacnetbrowser.schoko;


import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;

import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;



public class DeviceTest {
    public static void main(String[] args) throws Exception {
        int localDevice_ID = 10001;
        IpNetworkBuilder ipNetworkBuilder = new IpNetworkBuilder();
        ipNetworkBuilder.withLocalBindAddress(IpNetwork.DEFAULT_BIND_IP);
        ipNetworkBuilder.withBroadcast("255.255.255.255",IpNetwork.BVLC_TYPE);
        ipNetworkBuilder.withPort(47808);
        DefaultTransport transport = new DefaultTransport(ipNetworkBuilder.build());
        LocalDevice localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(new Listener());
        localDevice.initialize();
        localDevice.sendGlobalBroadcast(new WhoIsRequest());
        // Wait a bit for responses to come in.
        Thread.sleep(1000);
        getRemoteDeviceInformation(localDevice);
    }

    private static void getRemoteDeviceInformation(LocalDevice localDevice) {

        for (RemoteDevice remoteDevice : localDevice.getRemoteDevices()) {
            System.out.println("Device with ID: " + remoteDevice.getInstanceNumber());

        }
    }

    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("IAm received" + d);
        }
    }
}
