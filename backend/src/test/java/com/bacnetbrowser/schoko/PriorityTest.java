package com.bacnetbrowser.schoko;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.PriorityValue;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;

public class PriorityTest {
    public static void main(String[] args) throws Exception {
        IpNetwork ipNetwork = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, 47808);
        Transport transport = new Transport(ipNetwork);
        int localDevice_ID = 10001;
        LocalDevice localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(new PriorityTest.Listener());
        localDevice.initialize();
        localDevice.sendGlobalBroadcast(new WhoIsRequest());
        // Wait a bit for responses to come in.
        Thread.sleep(2000);
        System.out.println(localDevice.getRemoteDevices().get(0).getInstanceNumber());
        RemoteDevice remoteDevice = localDevice.getRemoteDevices().get(0);

        PriorityValue pv = new PriorityValue(new BinaryPV(1));
        //System.out.println(pv.getNullValue().toString());

        WritePropertyRequest req = new WritePropertyRequest(new ObjectIdentifier(ObjectType.binaryValue, 39),PropertyIdentifier.presentValue,null,new PriorityValue(new Null()),new UnsignedInteger(8));
        localDevice.send(remoteDevice,req);


        ConfirmedRequestService request = new ReadPropertyRequest(new ObjectIdentifier(ObjectType.binaryValue, 39), PropertyIdentifier.priorityArray);
        ReadPropertyAck result = (ReadPropertyAck) localDevice.send(remoteDevice, request);
        System.out.println(result.getValue().toString());





    }


    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("IAm received" + d);
        }
    }

}
