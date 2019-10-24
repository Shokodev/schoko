package com.bacnetbrowser.schoko.model.models;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.Real;

//TODO this class should be able to get the right PropertyValue type
public class BACnetTypes {

    public Encodable getPropertyValuesByObjectType(ObjectType objectType, String value) {
        if ((objectType.equals(ObjectType.binaryOutput)) ||(objectType.equals(ObjectType.binaryValue))) {
            return new BinaryPV(Integer.parseInt(value));
        } else if ((objectType.equals(ObjectType.analogOutput)) ||(objectType.equals(ObjectType.analogValue))) {
            return new Real(Integer.parseInt(value));
        } else if ((objectType.equals(ObjectType.multiStateOutput)) ||(objectType.equals(ObjectType.multiStateValue))) {
            return new Enumerated(Integer.parseInt(value));
        }
        return null;
    }

}

