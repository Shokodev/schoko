package com.bacnetbrowser.schoko.model.models;

import com.bacnetbrowser.schoko.model.services.DeviceService;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.ObjectPropertyTypeDefinition;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.*;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.PropertyValues;
import com.serotonin.bacnet4j.util.ReadListener;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;



public class BACnetObject extends RemoteObject implements ReadListener {

    private RemoteDevice bacnetDevice;
    private ObjectType objectType;
    static final Logger LOG = LoggerFactory.getLogger(BACnetObject.class);


    public BACnetObject(ObjectIdentifier oid, BACnetDevice bacnetDevice) {
        super(DeviceService.localDevice,oid);
        this.bacnetDevice = bacnetDevice.getBacnetDeviceInfo();
        this.objectType = oid.getObjectType();

    }

    public ObjectType getObjectType() {
        return objectType;
    }

    @Override
    public String getObjectName() {
        try {
            return RequestUtils.readProperty(DeviceService.localDevice, bacnetDevice, super.getObjectIdentifier(), PropertyIdentifier.objectName, null).toString();
        } catch (BACnetException e) {
            System.err.println("Could not read objectName of:" + super.getObjectIdentifier());
        }
        return "COM";
    }

    public String getDescription() {
        try {
            return RequestUtils.readProperty(DeviceService.localDevice,bacnetDevice,super.getObjectIdentifier(), PropertyIdentifier.description, null).toString();
        } catch (BACnetException ignored) {
        }
        return null;
    }

    public Encodable readProperty(PropertyIdentifier propertyIdentifier) {
        try {
            return RequestUtils.readProperty(DeviceService.localDevice,bacnetDevice,super.getObjectIdentifier(), propertyIdentifier,null);
        } catch (BACnetException bac) {
            System.err.println("Can't read " + propertyIdentifier + " of " + getObjectName());
        }
        return new CharacterString("COM");
    }

    public List<BACnetProperty> readProperties() {

    }

    public String getPresentValueAsText() {
        if ((objectType.equals(ObjectType.binaryValue)) || (objectType.equals(ObjectType.binaryOutput)) || (objectType.equals(ObjectType.binaryInput))) {
                BinaryPV resultValue = (BinaryPV) readProperty(PropertyIdentifier.presentValue);
                String resultActive = readProperty(PropertyIdentifier.activeText).toString();
                String resultInactive = readProperty(PropertyIdentifier.inactiveText).toString();
                if (resultValue.equals(BinaryPV.active)) {
                    return resultActive;
                } else {
                    return resultInactive;
                }
        } else if ((objectType.equals(ObjectType.multiStateValue)) || (objectType.equals(ObjectType.multiStateOutput)) || (objectType.equals(ObjectType.multiStateInput))) {
            try {
                Integer resultValue = Integer.parseInt(readProperty(PropertyIdentifier.presentValue).toString());
                List<CharacterString> texts = ((SequenceOf<CharacterString>) RequestUtils.sendReadPropertyAllowNull(
                        DeviceService.localDevice, bacnetDevice, super.getObjectIdentifier(),
                        PropertyIdentifier.stateText)).getValues();
                return texts.get(resultValue - 1).toString();

            } catch (BACnetException bac) {
                System.out.println("Cant read present value of: " + super.getObjectIdentifier() + " @ " + bacnetDevice);
            }
        } else if ((objectType.equals(ObjectType.analogValue)) || (objectType.equals(ObjectType.analogOutput)) || (objectType.equals(ObjectType.analogInput))) {
                String resultValue = readProperty(PropertyIdentifier.presentValue).toString();
                String resultUnit =  readProperty(PropertyIdentifier.units).toString();
                return resultValue + " " + resultUnit;
        }
        return "#";
    }

    public String getPriorityByStatus(EventState eventState){
        try {
            List<UnsignedInteger> priorities = ((SequenceOf<UnsignedInteger>) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, bacnetDevice, super.getObjectIdentifier(),
                    PropertyIdentifier.priority)).getValues();
            if(eventState.equals(EventState.normal)){
                return priorities.get(2).toString();
            } else if (eventState.equals(EventState.fault)){
                return priorities.get(1).toString();
            } else {
                return priorities.get(0).toString();
            }
        } catch (BACnetException bac) {
            System.err.println("Can't read " + PropertyIdentifier.priority  + " of " + getObjectName());
        }
        return "COM";
    }

    public String getAckTransitBitsByStatus(EventState eventState){
        try {
            EventTransitionBits priorities = ((EventTransitionBits) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, bacnetDevice, super.getObjectIdentifier(),
                    PropertyIdentifier.ackRequired));
            if(eventState.equals(EventState.normal)){
                return String.valueOf(priorities.isToNormal());
            } else if (eventState.equals(EventState.fault)){
                return String.valueOf(priorities.isToFault());
            } else {
                return String.valueOf(priorities.isToOffnormal());
            }
        } catch (BACnetException bac) {
            System.err.println("Can't read " + PropertyIdentifier.priority  + " of " + getObjectName());
        }
        return "COM";
    }

    public EventTransitionBits getAckTransitBits() {
        try {
            return  ((EventTransitionBits) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, bacnetDevice, super.getObjectIdentifier(),
                    PropertyIdentifier.ackRequired));
        } catch (BACnetException bac) {
            System.err.println("Cant read time stamp of: " + super.getObjectIdentifier() + " from: " + bacnetDevice.getVendorName());
        }
        return null;
    }

    public List<TimeStamp> getTimeStamps() {
        try {
            return ((SequenceOf<TimeStamp>) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, bacnetDevice, super.getObjectIdentifier(),
                    PropertyIdentifier.eventTimeStamps)).getValues();
        } catch (BACnetException bac) {
            System.err.println("Cant read time stamp of: " + super.getObjectIdentifier() + " from: " + bacnetDevice.getVendorName());
        }
        return null;
    }

    @Override
    public boolean progress(double v, int i, ObjectIdentifier objectIdentifier, PropertyIdentifier propertyIdentifier, UnsignedInteger unsignedInteger, Encodable encodable) {
        return false;
    }
}
