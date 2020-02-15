package com.bacnetbrowser.schoko.model.models;

import com.bacnetbrowser.schoko.model.services.DeviceService;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;

import java.util.List;

public class BACnetObject extends RemoteObject {

    private RemoteDevice remoteDevice;
    private ObjectType objectType;


    public BACnetObject(ObjectIdentifier oid, RemoteDevice remoteDevice) {
        super(oid);
        this.remoteDevice = remoteDevice;
        this.objectType = oid.getObjectType();
    }

    public RemoteDevice getRemoteDevice() {
        return remoteDevice;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public String getObjectName() {
        try {
            return ((ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, new ReadPropertyRequest(super.getObjectIdentifier(), PropertyIdentifier.objectName))).getValue().toString();
        } catch (BACnetException ignored) {
        }
        return null;
    }

    public String getDescription() {
        try {
            return ((ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, new ReadPropertyRequest(super.getObjectIdentifier(), PropertyIdentifier.description))).getValue().toString();
        } catch (BACnetException ignored) {
        }
        return null;
    }

    public Encodable getProperty(PropertyIdentifier propertyIdentifier) {
        try {
            return ((ReadPropertyAck)DeviceService.localDevice.send(remoteDevice, new ReadPropertyRequest(super.getObjectIdentifier(), propertyIdentifier))).getValue();
        } catch (BACnetException bac) {
            System.err.println("Can't read " + propertyIdentifier + " of " + getObjectName());
        }
        return new CharacterString("COM");
    }

    public String getPresentValueAsText() {
        if ((objectType.equals(ObjectType.binaryValue)) || (objectType.equals(ObjectType.binaryOutput)) || (objectType.equals(ObjectType.binaryInput))) {
            ConfirmedRequestService requestValue = new ReadPropertyRequest(super.getObjectIdentifier(), PropertyIdentifier.presentValue);
            ConfirmedRequestService requestActive = new ReadPropertyRequest(super.getObjectIdentifier(), PropertyIdentifier.activeText);
            ConfirmedRequestService requestInactive = new ReadPropertyRequest(super.getObjectIdentifier(), PropertyIdentifier.inactiveText);
            try {
                ReadPropertyAck resultValue = (ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, requestValue);
                ReadPropertyAck resultActive = (ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, requestActive);
                ReadPropertyAck resultInactive = (ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, requestInactive);
                if (resultValue.getValue().equals(BinaryPV.active)) {
                    return resultActive.getValue().toString();
                } else {
                    return resultInactive.getValue().toString();
                }

            } catch (BACnetException bac) {
                System.out.println("Cant read present value of: " + super.getObjectIdentifier() + " @ " + remoteDevice);
            }
        } else if ((objectType.equals(ObjectType.multiStateValue)) || (objectType.equals(ObjectType.multiStateOutput)) || (objectType.equals(ObjectType.multiStateInput))) {
            ConfirmedRequestService requestValue = new ReadPropertyRequest(super.getObjectIdentifier(), PropertyIdentifier.presentValue);
            try {
                ReadPropertyAck resultValue = (ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, requestValue);
                List<CharacterString> texts = ((SequenceOf<CharacterString>) RequestUtils.sendReadPropertyAllowNull(
                        DeviceService.localDevice, remoteDevice, super.getObjectIdentifier(),
                        PropertyIdentifier.stateText)).getValues();
                return (texts.get((Integer.parseInt(resultValue.getValue().toString())) - 1).getValue());

            } catch (BACnetException bac) {
                System.out.println("Cant read present value of: " + super.getObjectIdentifier() + " @ " + remoteDevice);
            }
        } else if ((objectType.equals(ObjectType.analogValue)) || (objectType.equals(ObjectType.analogOutput)) || (objectType.equals(ObjectType.analogInput))) {
            ConfirmedRequestService requestValue = new ReadPropertyRequest(super.getObjectIdentifier(), PropertyIdentifier.presentValue);
            ConfirmedRequestService requestUnit = new ReadPropertyRequest(super.getObjectIdentifier(), PropertyIdentifier.units);
            try {
                ReadPropertyAck resultValue = (ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, requestValue);
                ReadPropertyAck resultUnit = (ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, requestUnit);
                return resultValue.getValue().toString() + " " + resultUnit.getValue().toString();

            } catch (BACnetException bac) {
                System.out.println("Cant read present value of: " + super.getObjectIdentifier() + " @ " + remoteDevice);
            }
        }
        return "#";
    }

    public String getPriorityByStatus(EventState eventState){
        try {
            List<UnsignedInteger> priorities = ((SequenceOf<UnsignedInteger>) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, remoteDevice, super.getObjectIdentifier(),
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
                    DeviceService.localDevice, remoteDevice, super.getObjectIdentifier(),
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
                    DeviceService.localDevice, remoteDevice, super.getObjectIdentifier(),
                    PropertyIdentifier.ackRequired));
        } catch (BACnetException bac) {
            System.err.println("Cant read time stamp of: " + super.getObjectIdentifier() + " from: " + remoteDevice.getVendorName());
        }
        return null;
    }

    public List<TimeStamp> getTimeStamps() {
        try {
            return ((SequenceOf<TimeStamp>) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, remoteDevice, super.getObjectIdentifier(),
                    PropertyIdentifier.eventTimeStamps)).getValues();
        } catch (BACnetException bac) {
            System.err.println("Cant read time stamp of: " + super.getObjectIdentifier() + " from: " + remoteDevice.getVendorName());
        }
        return null;
    }

}
