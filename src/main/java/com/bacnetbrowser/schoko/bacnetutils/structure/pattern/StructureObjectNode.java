package com.bacnetbrowser.schoko.bacnetutils.structure.pattern;


import com.bacnetbrowser.schoko.bacnetutils.structure.Structure;

public class StructureObjectNode extends Structure {

    private String objectIdentifier;
    private String objectName;


    public StructureObjectNode(String name, String description,
                               String objectIdentifier, String objectName) {
        super(name, description);
        this.objectIdentifier = objectIdentifier;
        this.objectName = objectName;

    }

    public String getObjectIdentifier() {
        return objectIdentifier;
    }

    public void setObjectIdentifier(String objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }


}
