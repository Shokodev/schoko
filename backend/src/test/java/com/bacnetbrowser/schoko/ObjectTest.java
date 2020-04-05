package com.bacnetbrowser.schoko;


import com.bacnetbrowser.schoko.model.models.BACnetProperty;
import com.bacnetbrowser.schoko.model.services.DeviceService;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.CalendarEntry;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.ReadListener;
import com.serotonin.bacnet4j.util.RequestUtils;


import java.util.List;
public class ObjectTest {
    static LocalDevice localDevice;

    public static void main(String[] args) throws Exception {
        int localDevice_ID = 10001;
        IpNetworkBuilder ipNetworkBuilder = new IpNetworkBuilder();
        ipNetworkBuilder.withLocalBindAddress(IpNetwork.DEFAULT_BIND_IP);
        ipNetworkBuilder.withBroadcast("255.255.255.255", IpNetwork.BVLC_TYPE);
        ipNetworkBuilder.withPort(47808);
        DefaultTransport transport = new DefaultTransport(ipNetworkBuilder.build());
        localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(new Listener());
        localDevice.initialize();

        localDevice.sendGlobalBroadcast(new WhoIsRequest());

        // Wait a bit for responses to come in.
        Thread.sleep(2000);

    }

    static class Listener extends DeviceEventAdapter implements ReadListener {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("IAm received" + d.getVendorName() + ": " + d.getObjectIdentifier().toString());
            List<ObjectIdentifier> oids = null;
            List<ObjectPropertyReference> refs = null;
            try {
                oids = ((SequenceOf<ObjectIdentifier>)
                        RequestUtils.sendReadPropertyAllowNull(
                                localDevice, d, d.getObjectIdentifier(),
                                PropertyIdentifier.objectList)).getValues();
            } catch (BACnetException e) {
                e.printStackTrace();
            }

            oids.forEach(val -> {
                ObjectProperties.getRequiredObjectPropertyTypeDefinitions(val.getObjectType()).forEach(definition -> {
                    try {
                       System.out.println(RequestUtils.readProperty(localDevice,d,val,definition.getPropertyTypeDefinition().getPropertyIdentifier(),null).toString());
                    } catch (BACnetException bac) {
                        System.out.println("Can't read properties of " + val);
                    }
                });
            });
        }

        @Override
        public boolean progress(double v, int i, ObjectIdentifier objectIdentifier, PropertyIdentifier propertyIdentifier, UnsignedInteger unsignedInteger, Encodable encodable) {
            return false;
        }
    }

}
