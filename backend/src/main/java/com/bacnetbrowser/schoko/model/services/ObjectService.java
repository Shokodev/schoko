package com.bacnetbrowser.schoko.model.services;


import com.bacnetbrowser.schoko.model.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.model.models.BACnetProperty;
import com.bacnetbrowser.schoko.model.models.BACnetTypes;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.PriorityValue;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * This class used to manage communication to BACnet objects
 * Communication to Network in here
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class ObjectService extends DeviceEventAdapter {

    private final LinkedList<BACnetProperty> properties = new LinkedList<>();
    @Autowired
    private ObjectHandler objectHandler;
    private List<PropertyTypeDefinition> propertyTypeDefinitions = new LinkedList<>();
    private RemoteDevice remoteDevice;
    private ObjectIdentifier objectIdentifier;
    //TODO this value should be changeable by the client (User)
    private Integer roundPlacesAfterComma = 2;


    //TODO get direct from remote device
    /**
     * Gets all properties from a data point by objectName
     */
    public void readDataPointProperties(String objectName) {
        RemoteDevice remoteDevice = HierarchyService.obejctNamesToRemoteDevice.get(objectName);
        ObjectIdentifier oid = HierarchyService.objectNamesToOids.get(objectName);
        this.objectIdentifier = oid;
        this.remoteDevice = remoteDevice;
                try {
                    this.propertyTypeDefinitions = ObjectProperties.getPropertyTypeDefinitions(oid.getObjectType());
                    creatProperties(properties, remoteDevice, oid, propertyTypeDefinitions);

                } catch (Exception e) {
                    this.propertyTypeDefinitions = ObjectProperties.getRequiredPropertyTypeDefinitions(oid.getObjectType());
                    creatProperties(properties, remoteDevice, oid, propertyTypeDefinitions);
                }

        }

    /**
     * Used to create properties for sending to the client
     *
     * @param properties   child
     * @param remoteDevice remote device
     * @param oid          object identifier
     * @param poids        property identifier
     */
    private void creatProperties(LinkedList<BACnetProperty> properties, RemoteDevice remoteDevice, ObjectIdentifier oid, List<PropertyTypeDefinition> poids) {
        for (PropertyTypeDefinition op : poids) {
            try {
                ConfirmedRequestService request = new ReadPropertyRequest(oid, op.getPropertyIdentifier());
                ReadPropertyAck result = (ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, request);
                BACnetProperty property = new BACnetProperty(result.getValue().toString(), result.getPropertyIdentifier().toString());
                properties.add(property);
            } catch (BACnetException bac) {
                System.err.println("Cant read property " + op.getPropertyIdentifier().toString() + " of Object: " + oid.toString());
            }
        }
    }

    private void updateProperties() {
        for (PropertyTypeDefinition op : propertyTypeDefinitions) {
            try {
                ConfirmedRequestService request = new ReadPropertyRequest(objectIdentifier, op.getPropertyIdentifier());
                ReadPropertyAck result = (ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, request);
                for (BACnetProperty baCnetProperty : properties) {
                    if (baCnetProperty.getPropertyIdentifier().equals(op.getPropertyIdentifier().toString())) {
                        baCnetProperty.setValue(result.getValue().toString());
                    }
                }

            } catch (BACnetException bac) {
                System.err.println("Cant update property " + op.getPropertyIdentifier().toString() + " of Object: " + objectIdentifier.toString());
            }
        }

    }

    public LinkedList<BACnetProperty> getProperties() {
        return properties;
    }

    /**
     * Start a subscription at remote device with the current object
     */
    public void subscribeToCovRequest() {
        try {
            DeviceService.localDevice.send(remoteDevice, new SubscribeCOVRequest(new UnsignedInteger(0), objectIdentifier, new Boolean(true), new UnsignedInteger(0)));
            System.out.println("Subscription @: '" + objectIdentifier + "' on: " + remoteDevice.getObjectIdentifier());
        } catch (BACnetException bac) {
            System.err.println("Cant subscribed data point: " + objectIdentifier.getObjectType());
            objectHandler.updateStream();

        }
    }

    /**
     * Unsubscribe the current subscription at remote device of current object
     */
    public void unsubscribeToCovRequest() {

        try {
            DeviceService.localDevice.send(remoteDevice, new SubscribeCOVRequest(new UnsignedInteger(0), objectIdentifier, null, null));
            System.out.println("Unsubscription @: '" + objectIdentifier + "' on: " + remoteDevice.getObjectIdentifier());
        } catch (BACnetException bac) {
            System.err.println("Cant unsubscribed data point: " + objectIdentifier.getObjectType());

        }
    }

    public void clearPropertyList() {
        properties.clear();
    }

    /**
     * Write new value for given object
     *
     * @param poid  witch property to write
     * @param value new value for property
     */
    public void writeValue(PropertyIdentifier poid, Encodable value) {
        // 8 is default priority for manual operations
        WritePropertyRequest request = new WritePropertyRequest(objectIdentifier, poid, null, value, new UnsignedInteger(8));
        System.out.println("Write on :" + poid.toString() + " with: " + value.toString());
        try {
            DeviceService.localDevice.send(remoteDevice, request);
        } catch (BACnetException bac) {
            System.err.println("Cant write " + poid.toString() + " at " + objectIdentifier.toString());
        }

    }

    /**
     * Release a manual written property of the priority 8
     */
    public void releaseManualCommand() {
        WritePropertyRequest request = new WritePropertyRequest(objectIdentifier, PropertyIdentifier.presentValue, null, new PriorityValue(new Null()), new UnsignedInteger(8));
        System.out.println("Release :" + objectIdentifier);
        try {
            DeviceService.localDevice.send(remoteDevice, request);
        } catch (BACnetException bac) {
            System.err.println("Cant release " + objectIdentifier.toString());
        }
    }

    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    /**
     * Listen at change of value at the given remote device
     */
    @Override
    public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, RemoteDevice remoteDevice, ObjectIdentifier oid, UnsignedInteger timeRemaining, SequenceOf<PropertyValue> listOfValues) {
        for (PropertyValue pv : listOfValues) {
            for (BACnetProperty baCnetProperty : getProperties()) {
                if (pv.getPropertyIdentifier().toString().equals(baCnetProperty.getPropertyIdentifier())) {
                    if ((pv.getPropertyIdentifier().equals(PropertyIdentifier.presentValue)) && (oid.getObjectType().equals((ObjectType.analogInput)))) {
                        baCnetProperty.setValue(BACnetTypes.round(pv.getValue(), roundPlacesAfterComma));
                    } else {
                        baCnetProperty.setValue(pv.getValue().toString());
                    }
                    System.out.println("Device: " + remoteDevice.getObjectIdentifier() + " has sent new " + baCnetProperty.getPropertyIdentifier() + ": " + baCnetProperty.getValue());
                }

            }
        }
        //TODO this method should replace the code above
        updateProperties();
        objectHandler.updateStream();
    }

}

