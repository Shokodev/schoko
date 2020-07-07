package com.bacnetbrowser.schoko.bacnetutils.models;

import com.bacnetbrowser.schoko.bacnetutils.services.DeviceService;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.service.acknowledgement.ReadRangeAck;
import com.serotonin.bacnet4j.service.confirmed.ReadRangeRequest;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.*;
import com.serotonin.bacnet4j.type.enumerated.*;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.util.ReadListener;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BACnetObject extends RemoteObject {

    private final RemoteDevice bacnetDevice;
    private final ObjectType objectType;
    private String objectName;
    private final List<ObjectPropertyReference> propertyReferences = new ArrayList<>();
    private static final Logger LOG = LoggerFactory.getLogger(BACnetObject.class);

    public BACnetObject(ObjectIdentifier oid, BACnetDevice bacnetDevice) {
        super(DeviceService.localDevice,oid);
        this.bacnetDevice = bacnetDevice;
        this.objectType = oid.getObjectType();
        readObjectName();
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    @Override
    public String getObjectName() {
        return this.objectName;
    }

    public void readObjectName() {
            try {
               this.objectName = RequestUtils.readProperty(DeviceService.localDevice, bacnetDevice, super.getObjectIdentifier(), PropertyIdentifier.objectName, null).toString();
            } catch (BACnetException e) {
                LOG.warn("Could not read objectName of:" + super.getObjectIdentifier());
            }
    }

    public String getDescription(){
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
            LOG.warn("Can't read " + propertyIdentifier + " of " + getObjectName());
        }
        return new CharacterString("COM");
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
                LOG.warn("Cant read present value of: " + super.getObjectIdentifier() + " @ " + bacnetDevice);
            }
        } else if ((objectType.equals(ObjectType.analogValue)) || (objectType.equals(ObjectType.analogOutput)) || (objectType.equals(ObjectType.analogInput))) {
                String resultValue = readProperty(PropertyIdentifier.presentValue).toString();
                EngineeringUnits resultUnit = (com.serotonin.bacnet4j.type.enumerated.EngineeringUnits) readProperty(PropertyIdentifier.units);
                return resultValue + " " + EngineeringUnitsParser.UnitToString(resultUnit.intValue());
        }
        return " ";
    }


    public String getPresentValueAsText(HashMap<PropertyIdentifier,Encodable> propertiesRaw, int precisionRealValue) {
        if ((objectType.equals(ObjectType.binaryValue)) || (objectType.equals(ObjectType.binaryOutput)) || (objectType.equals(ObjectType.binaryInput))) {
            Enumerated resultValue = (Enumerated) propertiesRaw.get(PropertyIdentifier.presentValue);
            String resultActive = propertiesRaw.get(PropertyIdentifier.activeText).toString();
            String resultInactive = propertiesRaw.get(PropertyIdentifier.inactiveText).toString();
            if (resultValue.intValue() == 1) {
                return resultActive;
            } else {
                return resultInactive;
            }
        } else if ((objectType.equals(ObjectType.multiStateValue)) || (objectType.equals(ObjectType.multiStateOutput)) || (objectType.equals(ObjectType.multiStateInput))) {
                int resultValue = Integer.parseInt(propertiesRaw.get(PropertyIdentifier.presentValue).toString());
                List<CharacterString> texts = ((SequenceOf<CharacterString>) propertiesRaw.get(PropertyIdentifier.stateText)).getValues();
                return texts.get(resultValue -1).getValue();

        } else if ((objectType.equals(ObjectType.analogValue)) || (objectType.equals(ObjectType.analogOutput)) || (objectType.equals(ObjectType.analogInput))) {
            String resultValue = BACnetTypes.round(propertiesRaw.get(PropertyIdentifier.presentValue),precisionRealValue);
            EngineeringUnits resultUnit = (EngineeringUnits) propertiesRaw.get(PropertyIdentifier.units);
            return resultValue + " " + EngineeringUnitsParser.UnitToString(resultUnit.intValue());
        }
        return " ";
    }

    public String getPresentValueAsText(HashMap<PropertyIdentifier,Encodable> propertiesRaw, int precisionRealValue, Encodable newPresentValue) {
        if ((objectType.equals(ObjectType.binaryValue)) || (objectType.equals(ObjectType.binaryOutput)) || (objectType.equals(ObjectType.binaryInput))) {
            Enumerated resultValue = (Enumerated) newPresentValue;
            String resultActive = propertiesRaw.get(PropertyIdentifier.activeText).toString();
            String resultInactive = propertiesRaw.get(PropertyIdentifier.inactiveText).toString();
            if (resultValue.intValue() == 1) {
                return resultActive;
            } else {
                return resultInactive;
            }
        } else if ((objectType.equals(ObjectType.multiStateValue)) || (objectType.equals(ObjectType.multiStateOutput)) || (objectType.equals(ObjectType.multiStateInput))) {
            int resultValue = Integer.parseInt(newPresentValue.toString());
            List<CharacterString> texts = ((SequenceOf<CharacterString>) propertiesRaw.get(PropertyIdentifier.stateText)).getValues();
            return texts.get(resultValue -1).getValue();

        } else if ((objectType.equals(ObjectType.analogValue)) || (objectType.equals(ObjectType.analogOutput)) || (objectType.equals(ObjectType.analogInput))) {
            String resultValue = BACnetTypes.round(newPresentValue,precisionRealValue);
            EngineeringUnits resultUnit = (EngineeringUnits) propertiesRaw.get(PropertyIdentifier.units);
            return resultValue + " " + EngineeringUnitsParser.UnitToString(resultUnit.intValue());
        }
        return " ";
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
            LOG.warn("Can't read " + PropertyIdentifier.priority  + " of " + getObjectName());
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
            LOG.warn("Can't read " + PropertyIdentifier.priority  + " of " + getObjectName());
        }
        return "COM";
    }

    public EventTransitionBits getEventTransitionBits() {
        try {
            return  (EventTransitionBits) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, bacnetDevice, getObjectIdentifier(),
                    PropertyIdentifier.ackRequired);
        } catch (BACnetException bac) {

            LOG.warn("Cant read time stamp of: " + getObjectIdentifier() + " from: " + bacnetDevice.getName());
        }
        return null;
    }

    public List<TimeStamp> readTimeStamps() {
        try {
            return ((SequenceOf<TimeStamp>) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, bacnetDevice, super.getObjectIdentifier(),
                    PropertyIdentifier.eventTimeStamps)).getValues();
        } catch (BACnetException bac) {
            LOG.warn("Cant read time stamp of: " + super.getObjectIdentifier() + " from: " + bacnetDevice.getVendorName());
        }
        return null;
    }

    //Used for property stream
    public void readProperties(ReadListener listener) {
        creatPropertyReferenceList();
        try {
            RequestUtils.readProperties(DeviceService.localDevice, bacnetDevice, propertyReferences, true, listener);
        } catch (BACnetException bac) {
            LOG.warn("Can't read properties of " + getObjectIdentifier());
        }
    }

    private void creatPropertyReferenceList(){
        propertyReferences.clear();
        try{
            ObjectProperties.getObjectPropertyTypeDefinitions(objectType).forEach(definition -> {
                propertyReferences.add(new ObjectPropertyReference(getObjectIdentifier(),
                        definition.getPropertyTypeDefinition().getPropertyIdentifier()));
            });

        }catch(NullPointerException e){
        ObjectProperties.getRequiredObjectPropertyTypeDefinitions(objectType).forEach(definition -> {
            propertyReferences.add(new ObjectPropertyReference(getObjectIdentifier(),
                    definition.getPropertyTypeDefinition().getPropertyIdentifier()));
        });}
    }

    public List<ObjectPropertyReference> getPropertyReferences() {
        return propertyReferences;
    }

    /**
     * Write new value for given object
     *
     * @param poid  witch property to write
     * @param value new value for property
     */
    public void writeValue(PropertyIdentifier poid, Encodable value) {
        // 8 is default priority for manual operations
        LOG.info("Write on: " + poid.toString() + " @ " + getObjectName() +" with: " + value.toString());
        try {
          RequestUtils.writeProperty(DeviceService.localDevice,bacnetDevice,getObjectIdentifier(),poid,value,8);
        } catch (BACnetException bac) {
            LOG.warn("Cant write: " + poid.toString() + " at " + getObjectIdentifier().toString());
        }

    }

    /**
     * Release a manual written property of the priority 8
     */
    public void releaseManualCommand() {
        try {
            RequestUtils.writeProperty(DeviceService.localDevice,bacnetDevice,getObjectIdentifier(),
                    PropertyIdentifier.presentValue,null,new PriorityValue(new Null()),
                    new UnsignedInteger(8));
            LOG.info("Release: " + getObjectIdentifier());
        } catch (BACnetException bac) {
            LOG.warn("Cant release: " + getObjectIdentifier().toString());
        }
    }

    /**
     * Start a subscription at remote device with the current object
     */
    public void subscribeToCovRequest() {
        try {
            DeviceService.localDevice.send(bacnetDevice, new SubscribeCOVRequest(new UnsignedInteger(1), getObjectIdentifier(), Boolean.TRUE, new UnsignedInteger(0))).get();
            LOG.info("Subscription @: '" + getObjectIdentifier() + "' on: " + bacnetDevice.getObjectIdentifier());
        } catch (BACnetException e) {
            LOG.warn("Can't subscribe : '" + getObjectIdentifier() + "' on: " + bacnetDevice.getObjectIdentifier());
        }

    }

    /**
     * Unsubscribe the current subscription at remote device of current object
     */
    public void unsubscribeToCovRequest() {
        try {
            DeviceService.localDevice.send(bacnetDevice, new SubscribeCOVRequest(new UnsignedInteger(1), getObjectIdentifier(), null, null)).get();
            LOG.info("Unsubscription @: '" + getObjectIdentifier() + "' on: " + bacnetDevice.getObjectIdentifier());
        } catch (BACnetException e) {
            LOG.warn("Can't unsubscribe : '" + getObjectIdentifier() + "' on: " + bacnetDevice.getObjectIdentifier());
        }

    }

    //TODO replace "Object" with needed class
    public ArrayList<Object> readTrendBuffer(){
        try {
            ReadRangeAck ack = DeviceService.localDevice.send(bacnetDevice, new ReadRangeRequest(getObjectIdentifier(),
                    PropertyIdentifier.logBuffer, null,
                    new ReadRangeRequest.ByPosition(0,1000))).get();
            return new ArrayList<>(ack.getItemData().getValues());
        } catch (BACnetException e) {
            LOG.warn("Can't read {} of: {}",PropertyIdentifier.logBuffer.toString(),getObjectName());
        }
        return null;
    }

}



