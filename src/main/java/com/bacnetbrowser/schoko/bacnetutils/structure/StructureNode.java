package com.bacnetbrowser.schoko.bacnetutils.structure;

import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;

import java.util.List;

public class StructureNode {

    private String name;
    private String objectIdentifier;
    private String description;
    private String objectName;
    private StructureNode parent;
    private List<StructureNode> children;

    public StructureNode(String name, String objectIdentifier, String description,
                         String objectName, StructureNode parent) {
        this.name = name;
        this.objectIdentifier = objectIdentifier;
        this.description = description;
        this.objectName = objectName;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectIdentifier() {
        return objectIdentifier;
    }

    public void setObjectIdentifier(String objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public StructureNode getParent() {
        return parent;
    }

    public void setParent(StructureNode parent) {
        this.parent = parent;
    }

    public List<StructureNode> getChildren() {
        return children;
    }

    public void addChild(StructureNode child){
        this.children.add(child);
    }

    public boolean containsChild(String objectName){
        for (StructureNode child : children) {
            if (child.getName().equals(objectName)) {
                return false;
            }
        }
        return true;
    }

    public StructureNode getChild(String objectName) throws StructureBuildException {
        if (containsChild(objectName)) {
            for (StructureNode child : children) {
                if (child.getName().equals(objectName)) {
                    return child;
                }
            }
            return null;
        } else throw new StructureBuildException("No child with object name: "
                    + objectName + " in structure node: " + getParent().name);
        }

}
