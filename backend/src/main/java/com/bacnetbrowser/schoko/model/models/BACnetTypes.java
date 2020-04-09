package com.bacnetbrowser.schoko.model.models;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.*;
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
     * @param objectType type of object
     * @param value value to write
     * @return right property write format
     */
    public static Encodable getPropertyValuesByObjectType(ObjectType objectType, String value) {
        if ((objectType.equals(ObjectType.binaryOutput)) ||(objectType.equals(ObjectType.binaryValue))) {
            return BinaryPV.forId(Integer.parseInt(value));
        } else if ((objectType.equals(ObjectType.analogOutput)) ||(objectType.equals(ObjectType.analogValue))) {
            return new Real((float) Double.parseDouble(value));
        } else if ((objectType.equals(ObjectType.multiStateOutput)) ||(objectType.equals(ObjectType.multiStateValue))) {
            return new UnsignedInteger(Integer.parseInt(value));
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
        BigDecimal bd = new BigDecimal(Double.parseDouble(value.toString()));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public static boolean checkIfTypeIsAnalog(ObjectType objectType){
        return objectType.equals((ObjectType.analogInput)) ||
                (objectType.equals(ObjectType.analogOutput)) ||
                (objectType.equals(ObjectType.analogValue));
    }




}

