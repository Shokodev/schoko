package com.bacnetbrowser.schoko.bacnetutils.structure;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;

public class BACnetStructure extends Structure {
    private static final String name = "LOGIC";
    private static final String type = "logical-structure";
    private static final String description = "Logical View";

    public BACnetStructure() {
        super(name, type, description);
    }
}
