package com.bacnetbrowser.schoko.bacnetutils.structure;

import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;

import java.util.ArrayList;
import java.util.List;

public abstract class Structure {

    private String name;
    private String description;
    private StructureNode parent;
    private final List<StructureNode> children = new ArrayList<>();

    public Structure(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StructureNode> getChildren() {
        return children;
    }

    public boolean containsChild(String objectName){
        for (StructureNode child : children) {
            if (child.getName().equals(objectName)) {
                return true;
            }
        }
        return false;
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
                + objectName + " in structure node: " + name);
    }

    public StructureNode getParent() {
        return parent;
    }

    public void setParent(StructureNode parent) {
        this.parent = parent;
    }
}
