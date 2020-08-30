package com.bacnetbrowser.schoko.virtualdevice;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.BinaryValueObject;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Enumerated;


public class VirtualBinaryValueObject extends BinaryValueObject {

    public VirtualBinaryValueObject(LocalDevice localDevice, int instanceNumber,
                                    String name, BinaryPV presentValue,
                                    boolean outOfService, CharacterString description) throws BACnetServiceException {
        super(localDevice, instanceNumber, name, presentValue, outOfService);
        this.properties.put(PropertyIdentifier.description, description);
    }

}
