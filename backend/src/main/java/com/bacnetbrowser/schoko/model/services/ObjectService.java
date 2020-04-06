package com.bacnetbrowser.schoko.model.services;


import com.bacnetbrowser.schoko.model.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.model.models.BACnetDevice;
import com.bacnetbrowser.schoko.model.models.BACnetObject;
import com.bacnetbrowser.schoko.model.models.BACnetProperty;
import com.bacnetbrowser.schoko.model.models.BACnetTypes;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.ReadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;


/**
 * This class used to manage communication to BACnet objects
 * Communication to Network in here
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */

public class ObjectService extends DeviceEventAdapter implements ReadListener {



    static final Logger LOG = LoggerFactory.getLogger(ObjectService.class);
    private ObjectHandler objectHandler;
    private final LinkedList<BACnetProperty> properties = new LinkedList<>();
    private BACnetDevice bacnetDevice;
    private BACnetObject bacnetObject;
    private ObjectIdentifier objectIdentifier;
    //TODO this value should be changeable by the client (User)
    private Integer roundPlacesAfterComma = 2;

    public ObjectService(){
        DeviceService.localDevice.getEventHandler().addListener(this);
    }

    public ObjectService(ObjectHandler objectHandler){
        this.objectHandler = objectHandler;
        DeviceService.localDevice.getEventHandler().addListener(this);
    }



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
        bacnetObject.readProperties(this);
        }

    public LinkedList<BACnetProperty> getProperties() {
        return properties;
    }

    public BACnetProperty getBACnetProperty(PropertyIdentifier propertyIdentifier){
        return properties.stream().filter(property -> property.getPropertyIdentifier().equals(propertyIdentifier.toString())).findFirst().orElse(null);
    }

    public void clearPropertyList() {
        properties.clear();
    }

    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    public BACnetObject getBacnetObject() {
        return bacnetObject;
    }

    /**
     * Listen at change of value at the given remote device
     */
    @Override
    public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, ObjectIdentifier initiatingDeviceIdentifier,
                                        ObjectIdentifier monitoredObjectIdentifier, UnsignedInteger timeRemaining,
                                        SequenceOf<PropertyValue> listOfValues) {
        LOG.info("COV message received from: " + initiatingDeviceIdentifier + " of: " +monitoredObjectIdentifier);
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
        objectHandler.updateStream();
    }

    @Override
    public boolean progress(double v, int i, ObjectIdentifier objectIdentifier, PropertyIdentifier propertyIdentifier, UnsignedInteger unsignedInteger, Encodable encodable) {
        properties.add(new BACnetProperty(encodable.toString(),propertyIdentifier.toString()));
        if(properties.size() == bacnetObject.getPropertyReferences().size()){
            LOG.info("Property list for: " + objectIdentifier + " ready with: " +properties.size() + " properties" );
            objectHandler.updateStream();
        }
        return false;
    }
}

