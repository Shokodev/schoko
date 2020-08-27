package com.bacnetbrowser.schoko.bacnetutils.structure;


public class StructureNode extends Structure {

    private String objectIdentifier;
    private String objectName;


    public StructureNode(String name, String description,
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
