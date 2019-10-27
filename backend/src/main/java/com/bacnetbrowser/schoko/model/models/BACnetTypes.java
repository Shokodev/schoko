package com.bacnetbrowser.schoko.model.models;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO this class should be able to get the right PropertyValue type
public class BACnetTypes {

    public static Encodable getPropertyValuesByObjectType(ObjectType objectType, String value) {
        if ((objectType.equals(ObjectType.binaryOutput)) ||(objectType.equals(ObjectType.binaryValue))) {
            return new BinaryPV(Integer.parseInt(value));
        } else if ((objectType.equals(ObjectType.analogOutput)) ||(objectType.equals(ObjectType.analogValue))) {
            return new Real((float) Double.parseDouble(value));
        } else if ((objectType.equals(ObjectType.multiStateOutput)) ||(objectType.equals(ObjectType.multiStateValue))) {
            return new UnsignedInteger(Integer.parseInt(value));
        }
        return new UnsignedInteger(Integer.parseInt(value));
    }

    /**
     * Used to get Text Texts mapped to the present value
     * @param oid needs the oid of the given object
     * @param remoteDevice remote device of the given object
     * @return
     */
    public static String getPresentValueAsText(ObjectIdentifier oid, RemoteDevice remoteDevice){
        if((oid.getObjectType().equals(ObjectType.binaryValue)) || (oid.getObjectType().equals(ObjectType.binaryOutput)) || (oid.getObjectType().equals(ObjectType.binaryInput)))
        {
            ConfirmedRequestService requestValue = new ReadPropertyRequest(oid, PropertyIdentifier.presentValue);
            ConfirmedRequestService requestActive = new ReadPropertyRequest(oid, PropertyIdentifier.activeText);
            ConfirmedRequestService requestInactive = new ReadPropertyRequest(oid, PropertyIdentifier.inactiveText);
            try{
                ReadPropertyAck resultValue = (ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, requestValue);
                ReadPropertyAck resultActive = (ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, requestActive);
                ReadPropertyAck resultInactive = (ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, requestInactive);
                if(resultValue.getValue().equals(BinaryPV.active)){
                    return resultActive.getValue().toString();
                } else {
                    return resultInactive.getValue().toString();
                }

            } catch (BACnetException bac){
                System.out.println("Cant read present value of: " + oid + " @ " + remoteDevice);
            }
            return null;
        } else if ((oid.getObjectType().equals(ObjectType.multiStateValue)) || (oid.getObjectType().equals(ObjectType.multiStateOutput)) || (oid.getObjectType().equals(ObjectType.multiStateInput))) {
            ConfirmedRequestService requestValue = new ReadPropertyRequest(oid, PropertyIdentifier.presentValue);
            try{
                ReadPropertyAck resultValue = (ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, requestValue);
                List<CharacterString> texts = ((SequenceOf<CharacterString>) RequestUtils.sendReadPropertyAllowNull(
                        DeviceHandler.localDevice, remoteDevice, oid,
                        PropertyIdentifier.stateText)).getValues();
                return (texts.get((Integer.parseInt(resultValue.getValue().toString()))-1).getValue());

            } catch (BACnetException bac){
                System.out.println("Cant read present value of: " + oid + " @ " + remoteDevice);
            }
        } else if ((oid.getObjectType().equals(ObjectType.analogValue)) || (oid.getObjectType().equals(ObjectType.analogOutput)) || (oid.getObjectType().equals(ObjectType.analogInput))) {
            ConfirmedRequestService requestValue = new ReadPropertyRequest(oid, PropertyIdentifier.presentValue);
            ConfirmedRequestService requestUnit = new ReadPropertyRequest(oid, PropertyIdentifier.units);
            try{
                ReadPropertyAck resultValue = (ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, requestValue);
                ReadPropertyAck resultUnit = (ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, requestUnit);
                return resultValue.getValue().toString() + " " + resultUnit.getValue().toString();

            } catch (BACnetException bac){
                System.out.println("Cant read present value of: " + oid + " @ " + remoteDevice);
            }
        }
        return null;
    }

    public static String getGermanEventStateText(String eventState){
        HashMap<String,String> germanEventStates = new HashMap<>();
        germanEventStates.put("fault","Störung");
        germanEventStates.put("off normal","Nicht Normal");
        germanEventStates.put("high limit","Obere Grenze");
        germanEventStates.put("low limit","Untere Grenze");
        germanEventStates.put("normal","Normal");
        return germanEventStates.get(eventState);

    }

}

