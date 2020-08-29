package com.bacnetbrowser.schoko.bacnetutils.structure.pattern;

import com.bacnetbrowser.schoko.bacnetutils.structure.Structure;

public class StructureViewNode extends Structure {

    private final String type = "Structure Element";

    public StructureViewNode(String name, String description) {
        super(name, description);
    }

    public String getType() {
        return type;
    }
}
