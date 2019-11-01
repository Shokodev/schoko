package com.bacnetbrowser.schoko;


import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;

import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;



public class DeviceTest {
    public static void main(String[] args) throws Exception {
        IpNetwork ipNetwork = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, 47808);
        Transport transport = new Transport(ipNetwork);
        int localDevice_ID = 10001;
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
                System.out.println("Device with ID: " +remoteDevice.getInstanceNumber());
            try {
                ObjectIdentifier oid = remoteDevice.getObjectIdentifier();
                ReadPropertyAck ack0 = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.objectName));
                System.out.println("Object name:" + ack0.getValue());
                ReadPropertyAck ack1 = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.description));
                System.out.println("Description: " + ack1.getValue());
                ReadPropertyAck ack2 = (ReadPropertyAck) localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.protocolVersion));
                System.out.println("Protocol version:" + ack2.getValue());
            } catch (BACnetException bac){
                System.err.println("Cant read remote device information");
            }
        }
    }

    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("IAm received" + d);
        }
    }
}
