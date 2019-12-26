package com.bacnetbrowser.schoko;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.models.BACnetTypes;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.PriorityValue;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;

import java.util.Map;

public class EventPropertiesTest {
    public static void main(String[] args) throws Exception {
        IpNetwork ipNetwork = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, 47808);
        Transport transport = new Transport(ipNetwork);
        int localDevice_ID = 10001;
        LocalDevice localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(new EventPropertiesTest.Listener());
        localDevice.initialize();
        localDevice.sendGlobalBroadcast(new WhoIsRequest());
        // Wait a bit for responses to come in.
        Thread.sleep(2000);
        System.out.println(localDevice.getRemoteDevices().get(0).getInstanceNumber());
        RemoteDevice remoteDevice = localDevice.getRemoteDevices().get(0);
        ObjectIdentifier oid = new ObjectIdentifier(ObjectType.binaryValue, 39);
        PropertyIdentifier[] properties = {PropertyIdentifier.priority,PropertyIdentifier.eventType,
                    PropertyIdentifier.actionText,PropertyIdentifier.notifyType,PropertyIdentifier.ackRequired,
                    PropertyIdentifier.eventParameters};

            try {
                Map<PropertyIdentifier, Encodable>  values = RequestUtils.getProperties(localDevice,remoteDevice,oid,null,
                        properties);
                System.out.println(values);
            } catch (BACnetException bac) {
                System.err.println("Cant read event properties");
            }
        }










static class Listener extends DeviceEventAdapter {
    @Override
    public void iAmReceived(RemoteDevice d) {
        System.out.println("IAm received" + d);
    }
}

}
