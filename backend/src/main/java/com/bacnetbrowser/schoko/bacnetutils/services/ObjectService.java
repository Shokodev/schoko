package com.bacnetbrowser.schoko.bacnetutils.services;


import com.bacnetbrowser.schoko.bacnetutils.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.bacnetutils.datahandler.SettingsHandler;
import com.bacnetbrowser.schoko.bacnetutils.models.*;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.ReadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * This class used to manage communication to BACnet objects
 * Communication to Network in here
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
public class ObjectService extends DeviceEventAdapter implements ReadListener {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectService.class);
    private final ObjectHandler objectHandler;
    private final LinkedList<BACnetProperty> properties = new LinkedList<>();
    private final HashMap<PropertyIdentifier,Encodable> propertiesRaw = new HashMap<>();
    private BACnetDevice bacnetDevice;
    private BACnetObject bacnetObject;
    private ObjectIdentifier objectIdentifier;
    private final Integer precisionRealValue;

    public ObjectService(ObjectHandler objectHandler){
        this.objectHandler = objectHandler;
        DeviceService.localDevice.getEventHandler().addListener(this);
        this.precisionRealValue = SettingsHandler.precisionRealValue;
    }

    public void removeFromLocalDevice(){
        DeviceService.localDevice.getEventHandler().removeListener(this);
    }

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
        propertiesRaw.clear();
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
        LOG.info("COV message received from: " + initiatingDeviceIdentifier + " of: " + monitoredObjectIdentifier);
        for (PropertyValue pv : listOfValues) {
            propertiesRaw.replace(pv.getPropertyIdentifier(),pv.getValue());
            LOG.info("Device: " + bacnetDevice.getObjectIdentifier() + " has sent new " +
                    pv.getPropertyIdentifier() + ": " + pv.getValue());
        }
        Runnable runnable = () -> {
            propertiesRaw.replace(PropertyIdentifier.priorityArray,bacnetObject.readProperty(PropertyIdentifier.priorityArray));
            parseProperties();
            objectHandler.updateStream();
        };
        Thread newThread = new Thread(runnable);
        newThread.start();
    }

    @Override
    public boolean progress(double v, int i, ObjectIdentifier objectIdentifier, PropertyIdentifier propertyIdentifier, UnsignedInteger unsignedInteger, Encodable encodable) {
        propertiesRaw.put(propertyIdentifier,encodable);
        if(propertiesRaw.size() == bacnetObject.getPropertyReferences().size()){
            LOG.info("Property list of: " + objectIdentifier + " ready with: " +propertiesRaw.size() + " properties for parsing" );
            parseProperties();
            LOG.info("Final property list of: " + objectIdentifier + " ready with: " +properties.size() + " properties" );
            objectHandler.updateStream();
        }
        return false;
    }

    public void parseProperties() {
        //Ignore if one property does not exist
        properties.clear();
        try {
            properties.add(new BACnetProperty(bacnetObject.getPresentValueAsText(propertiesRaw, precisionRealValue), PropertyIdentifier.presentValue.toString()));
            properties.add(new BACnetProperty(objectIdentifier.toString(), PropertyIdentifier.objectIdentifier.toString()));
            properties.add(new BACnetProperty(propertiesRaw.get(PropertyIdentifier.objectName).toString(), PropertyIdentifier.objectName.toString()));
            properties.add(new BACnetProperty(propertiesRaw.get(PropertyIdentifier.description).toString(), PropertyIdentifier.description.toString()));
        } catch (NullPointerException ignored){}
        try{
            properties.add(new BACnetProperty(propertiesRaw.get(PropertyIdentifier.stateText).toString(), PropertyIdentifier.stateText.toString()));
        } catch (NullPointerException ignored){}
        try{
            properties.add(new BACnetProperty(propertiesRaw.get(PropertyIdentifier.activeText).toString(), PropertyIdentifier.activeText.toString()));
            properties.add(new BACnetProperty(propertiesRaw.get(PropertyIdentifier.inactiveText).toString(), PropertyIdentifier.inactiveText.toString()));
        } catch (NullPointerException ignored){}
        try{
            properties.add(new BACnetProperty(BACnetTypes.getOutOfServiceAsText(propertiesRaw.get(PropertyIdentifier.outOfService)), PropertyIdentifier.outOfService.toString()));
        } catch (NullPointerException ignored){}
        try{
            properties.add(new BACnetProperty(BACnetTypes.getPolarityAsText(propertiesRaw.get(PropertyIdentifier.polarity)), PropertyIdentifier.polarity.toString()));
        } catch (Exception e){
            LOG.warn("Object: {} does not have {}",objectIdentifier,PropertyIdentifier.polarity.toString());
        }
        try{
            properties.add(new BACnetProperty(BACnetTypes.getPriorityArrayAsText(bacnetObject,propertiesRaw,precisionRealValue),PropertyIdentifier.priorityArray.toString()));
        } catch (Exception e){
            LOG.warn("Object: {} does not have {}",objectIdentifier,PropertyIdentifier.priorityArray.toString());
        }

    }

    public void updatePropertyRelease() {
        Runnable runnable = () -> {
            propertiesRaw.replace(PropertyIdentifier.priorityArray,bacnetObject.readProperty(PropertyIdentifier.priorityArray));
            parseProperties();
            objectHandler.updateStream();
        };
        Thread newThread = new Thread(runnable);
        newThread.start();

    }
}

