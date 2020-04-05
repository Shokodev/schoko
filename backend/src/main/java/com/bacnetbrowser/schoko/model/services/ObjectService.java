package com.bacnetbrowser.schoko.model.services;


import com.bacnetbrowser.schoko.model.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.model.models.BACnetDevice;
import com.bacnetbrowser.schoko.model.models.BACnetObject;
import com.bacnetbrowser.schoko.model.models.BACnetProperty;
import com.bacnetbrowser.schoko.model.models.BACnetTypes;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.ObjectPropertyTypeDefinition;
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
import com.serotonin.bacnet4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * This class used to manage communication to BACnet objects
 * Communication to Network in here
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class ObjectService extends DeviceEventAdapter {

    @Autowired
    private ObjectHandler objectHandler;
    static final Logger LOG = LoggerFactory.getLogger(ObjectService.class);
    private final LinkedList<BACnetProperty> properties = new LinkedList<>();
    private BACnetDevice bacnetDevice;
    private BACnetObject bacnetObject;
    private ObjectIdentifier objectIdentifier;
    //TODO this value should be changeable by the client (User)
    private Integer roundPlacesAfterComma = 2;


    //TODO get direct from remote device
    /**
     * Gets all properties from a data point by objectName
     */
    public void readDataPointProperties(String objectName) {
        BACnetDevice bacnetDevice = HierarchyService.obejctNamesToBACnetDevice.get(objectName);
        ObjectIdentifier oid = HierarchyService.objectNamesToOids.get(objectName);
        this.objectIdentifier = oid;
        this.bacnetDevice = bacnetDevice;
        this.bacnetObject = bacnetDevice.getBACnetObject(oid);
        bacnetObject.readProperties();
        }

    public LinkedList<BACnetProperty> getProperties() {
        return properties;
    }

    /**
     * Start a subscription at remote device with the current object
     */
    public void subscribeToCovRequest() {
        try {
            DeviceService.localDevice.send(bacnetDevice.getBacnetDeviceInfo(), new SubscribeCOVRequest(new UnsignedInteger(1), objectIdentifier, Boolean.TRUE, new UnsignedInteger(0))).get();
        } catch (BACnetException e) {
            LOG.warn("Can't subscribe : '" + objectIdentifier + "' on: " + bacnetDevice.getObjectIdentifier());
        }
        LOG.info("Subscription @: '" + objectIdentifier + "' on: " + bacnetDevice.getObjectIdentifier());
    }

    /**
     * Unsubscribe the current subscription at remote device of current object
     */
    public void unsubscribeToCovRequest() {
        try {
        DeviceService.localDevice.send(bacnetDevice.getBacnetDeviceInfo(), new SubscribeCOVRequest(new UnsignedInteger(1), objectIdentifier, null, null)).get();
        } catch (BACnetException e) {
        LOG.warn("Can't unsubscribe : '" + objectIdentifier + "' on: " + bacnetDevice.getObjectIdentifier());
    }
        LOG.info("Unsubscription @: '" + objectIdentifier + "' on: " + bacnetDevice.getObjectIdentifier());
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
        LOG.info("Write on :" + poid.toString() + " with: " + value.toString());
        try {
            RequestUtils.writeProperty(DeviceService.localDevice,bacnetDevice,objectIdentifier,poid,value);
        } catch (BACnetException bac) {
            LOG.warn("Cant write " + poid.toString() + " at " + objectIdentifier.toString());
        }

    }

    /**
     * Release a manual written property of the priority 8
     */
    public void releaseManualCommand() {
        LOG.info("Release :" + objectIdentifier);
        try {
            RequestUtils.writeProperty(DeviceService.localDevice,bacnetDevice,objectIdentifier,PropertyIdentifier.presentValue,null,new PriorityValue(new Null()),new UnsignedInteger(8));
        } catch (BACnetException bac) {
            LOG.warn("Cant release " + objectIdentifier.toString());
        }
    }

    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    /**
     * Listen at change of value at the given remote device
     */
    @Override
    public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, ObjectIdentifier initiatingDeviceIdentifier,
                                        ObjectIdentifier monitoredObjectIdentifier, UnsignedInteger timeRemaining,
                                        SequenceOf<PropertyValue> listOfValues) {
        for (PropertyValue pv : listOfValues) {
            for (BACnetProperty baCnetProperty : getProperties()) {
                if (pv.getPropertyIdentifier().toString().equals(baCnetProperty.getPropertyIdentifier())) {
                    if ((pv.getPropertyIdentifier().equals(PropertyIdentifier.presentValue)) && (initiatingDeviceIdentifier.getObjectType().equals((ObjectType.analogInput)))) {
                        baCnetProperty.setValue(BACnetTypes.round(pv.getValue(), roundPlacesAfterComma));
                    } else {
                        baCnetProperty.setValue(pv.getValue().toString());
                    }
                    LOG.info("Device: " + bacnetDevice.getObjectIdentifier() + " has sent new " + baCnetProperty.getPropertyIdentifier() + ": " + baCnetProperty.getValue());
                }

            }
        }
        //TODO this method should replace the code above
        objectHandler.updateStream();
    }

}

