package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVRequest;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionService extends DeviceEventAdapter {

    @Autowired
    DeviceHandler deviceHandler;

    private SequenceOf<PropertyValue> listOfValues;

    /**
     * Subscribe for a Change of Value
     * @param remoteDevice specific remote device
     * @param oid object identifier
     * @throws BACnetException from network
     */
   public void subscribeToCovRequest(RemoteDevice remoteDevice, ObjectIdentifier oid) throws BACnetException {

            SubscribeCOVRequest request = new SubscribeCOVRequest(new UnsignedInteger(0), oid, new Boolean(true), new UnsignedInteger(0));
            deviceHandler.getLocalDevice().send(remoteDevice, request);
            System.out.println("Subscription @: '" + oid + "' on: " + remoteDevice.getObjectIdentifier() + " with id:" + request.getChoiceId());

    }

    @Override
    public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, RemoteDevice initiatingDevice, ObjectIdentifier monitoredObjectIdentifier, UnsignedInteger timeRemaining, SequenceOf<PropertyValue> listOfValues) {
        this.listOfValues = listOfValues;
    }


}
