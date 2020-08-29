package com.bacnetbrowser.schoko.bacnetutils.objects;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.*;
import com.serotonin.bacnet4j.type.enumerated.*;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.type.primitive.Boolean;

import java.lang.Double;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.HashMap;


/**
 * This class extents the stack for our purpose
 */
//TODO this class will be extended to manage more type parsing for further commanding
public class BACnetTypes {

    /**
     * get the right writable format for the given object
     * @param value value to write
     * @param bacnetObject current bacnetObject
     * @return right property write format
     */
    public static Encodable getPropertyValuesByObjectType(String value, BACnetObject bacnetObject) {
        ObjectType objectType = bacnetObject.getObjectType();
        if ((objectType.equals(ObjectType.binaryOutput)) ||(objectType.equals(ObjectType.binaryValue))) {
            CharacterString activeTexts = (CharacterString) bacnetObject.readProperty(PropertyIdentifier.activeText);
            if (activeTexts.getValue().equals(value)){
                return BinaryPV.active;
            } else
                return BinaryPV.inactive;
        } else if ((objectType.equals(ObjectType.analogOutput)) ||(objectType.equals(ObjectType.analogValue))) {
            return new Real((float) Double.parseDouble(value));
        } else if ((objectType.equals(ObjectType.multiStateOutput)) ||(objectType.equals(ObjectType.multiStateValue))) {
            SequenceOf<CharacterString> texts =(SequenceOf<CharacterString> ) bacnetObject.readProperty(PropertyIdentifier.stateText);
            int index = 0;
            for(CharacterString text : texts){
                if(text.getValue().equals(value)){
                   index = texts.indexOf(text);
                }
            }
            return new UnsignedInteger(index);
        }
        return new UnsignedInteger(Integer.parseInt(value));
    }

    /**
     *Translate event state to german
     * @param eventState event state as string english
     * @return event state as string german
     */
    public static String getGermanEventStateText(EventState eventState){
        HashMap<EventState,String> germanEventStates = new HashMap<>();
        germanEventStates.put(EventState.fault,"Störung");
        germanEventStates.put(EventState.offnormal,"Nicht Normal");
        germanEventStates.put(EventState.highLimit,"Obere Grenze");
        germanEventStates.put(EventState.lowLimit,"Untere Grenze");
        germanEventStates.put(EventState.normal,"Normal");
        return germanEventStates.get(eventState);

    }

    /**
     * Parse the BACnet TimeStamp into SQL format
     * @param timeStamp Date and Time property of BACnet
     * @return SQL time stamp format
     */
    public static java.sql.Timestamp parseToSQLTimeStamp(TimeStamp timeStamp){
        return new java.sql.Timestamp(timeStamp.getDateTime().getGC().getTimeInMillis());
    }

    /**
     * Parse the BACnet TimeStamp into SQL format
     * @param timeStamp Date and Time property of BACnet
     * @return SQL time stamp format
     */
    public static TimeStamp parseToBACnetTimeStamp(Timestamp timeStamp){
        DateTime dateTime = new DateTime(timeStamp.getTime());
        return new TimeStamp(dateTime);

    }

    public static String getNotifyTypeAsText(NotifyType notifyType){
        if(notifyType.equals(NotifyType.alarm)){
            return "Alarm";
        } else if(notifyType.equals(NotifyType.event)){
            return "Event";
        } else return "AckNotification";
    }

    /**
     * Used to round BACnet values to (places) digits after comma
     * @param value Encodable BACnet value
     * @param places how many places after comma
     * @return string to send to client
     */
    public static String round(Encodable value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(value.toString()));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public static boolean checkIfTypeIsAnalog(ObjectType objectType){
        return objectType.equals((ObjectType.analogInput)) ||
                (objectType.equals(ObjectType.analogOutput)) ||
                (objectType.equals(ObjectType.analogValue));
    }

    public static String getOutOfServiceAsText(Encodable encodable){
        if(encodable.equals(Boolean.TRUE)) {
            return "Ein";
        }
        return "Aus";
    }

    public static String getPolarityAsText(Encodable encodable){
        if(encodable.equals(Polarity.normal)) {
            return "Normal";
        }
        return "Invertiert";
    }

    public static String  getPriorityArrayAsText(BACnetObject bacnetObject, HashMap<PropertyIdentifier,Encodable>
            propertiesRaw, int precisionRealValue){
        HashMap<String, String> result = new HashMap<>();
        PriorityArray priorityArray = (PriorityArray) propertiesRaw.get(PropertyIdentifier.priorityArray);
        for (int i = 0; i < priorityArray.size(); i++) {
            if(!priorityArray.get(i).getConstructedValue().equals(Null.instance)){
                result.put(String.valueOf(i + 1), bacnetObject.getPresentValueAsText(propertiesRaw,
                        precisionRealValue,priorityArray.get(i).getValue()));
            }
        }
        return result.toString();
    }
}

