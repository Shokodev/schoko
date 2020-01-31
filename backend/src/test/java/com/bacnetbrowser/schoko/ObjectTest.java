package com.bacnetbrowser.schoko;


import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;


import java.util.List;
public class ObjectTest {
    public static void main(String[] args) throws Exception {
        IpNetwork ipNetwork = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, 47808);
        Transport transport = new Transport(ipNetwork);
        int localDevice_ID = 10001;
        LocalDevice localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(new Listener());
        localDevice.initialize();

        localDevice.sendGlobalBroadcast(new WhoIsRequest());

        // Wait a bit for responses to come in.
        Thread.sleep(2000);

        for (RemoteDevice remoteDevice : localDevice.getRemoteDevices()) {
            RequestUtils.getExtendedDeviceInformation(localDevice, remoteDevice);
            List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>) RequestUtils.sendReadPropertyAllowNull(
                    localDevice, remoteDevice, remoteDevice.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();
            for (ObjectIdentifier oid : oids){
                System.out.println(oid.toString());
                List<PropertyTypeDefinition> poids = ObjectProperties.getPropertyTypeDefinitions(oid.getObjectType());
                for (PropertyTypeDefinition op : poids) {
                    try {
                        ConfirmedRequestService request = new ReadPropertyRequest(oid, op.getPropertyIdentifier());
                        ReadPropertyAck result = (ReadPropertyAck) localDevice.send(remoteDevice, request);
                        System.out.println(op.getPropertyIdentifier().toString() + ": " + result.getValue().toString());
                    } catch (BACnetException | NullPointerException ignored){}
                }
            }
        }}

    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("IAm received" + d.getVendorName() + ": " + d.getObjectIdentifier().toString());
        }
    }
}
