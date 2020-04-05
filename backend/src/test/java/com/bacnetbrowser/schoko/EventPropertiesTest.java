package com.bacnetbrowser.schoko;

import com.bacnetbrowser.schoko.model.services.DeviceService;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.service.acknowledgement.GetAlarmSummaryAck;
import com.serotonin.bacnet4j.service.acknowledgement.GetEnrollmentSummaryAck;
import com.serotonin.bacnet4j.service.acknowledgement.GetEventInformationAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.*;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;

import java.beans.Encoder;
import java.util.List;
import java.util.Objects;


public class EventPropertiesTest {
    public static void main(String[] args) throws Exception {
        int localDevice_ID = 10001;
        IpNetworkBuilder ipNetworkBuilder = new IpNetworkBuilder();
        ipNetworkBuilder.withLocalBindAddress(IpNetwork.DEFAULT_BIND_IP);
        ipNetworkBuilder.withBroadcast("255.255.255.255",IpNetwork.BVLC_TYPE);
        ipNetworkBuilder.withPort(47808);
        DefaultTransport transport = new DefaultTransport(ipNetworkBuilder.build());
        LocalDevice localDevice = new LocalDevice(localDevice_ID, transport);
        localDevice.getEventHandler().addListener(new EventPropertiesTest.Listener());
        localDevice.initialize();
        localDevice.sendGlobalBroadcast(new WhoIsRequest());
        // Wait a bit for responses to come in.
        Thread.sleep(2000);
        RemoteDevice remoteDevice = localDevice.getRemoteDevices().get(0);
        System.out.println(remoteDevice.getInstanceNumber());
        /*try {
            GetAlarmSummaryRequest request = new GetAlarmSummaryRequest();
            GetAlarmSummaryAck events = (GetAlarmSummaryAck) localDevice.send(remoteDevice, request);
            try {
            for (GetAlarmSummaryAck.AlarmSummary event : events.getValues()){
                System.err.println(event.getObjectIdentifier());
                }}catch (NullPointerException ignored){}
        } catch (BACnetException bac) {
            System.err.println("Read Fail");
        }
        System.out.println("---------------------------------");
        try {
            GetEventInformation request = new GetEventInformation(null);
            GetEventInformationAck events = (GetEventInformationAck) localDevice.send(remoteDevice, request);
            try {
                for (GetEventInformationAck.EventSummary event : events.getListOfEventSummaries()){
                    System.err.println(event.getObjectIdentifier());
                }}catch (NullPointerException ignored){}
        } catch (BACnetException bac) {
            System.err.println("Read Fail");
        }*/

    }


    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("IAm received" + d);
        }
    }
}

