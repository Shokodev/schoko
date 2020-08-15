package com.bacnetbrowser.schoko.bacnetutils.structure;

import java.util.List;

public abstract class Structure {

    private String name;
    private String type;
    private String description;
    private List<StructureNode> children;

    public Structure(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setChildren(List<StructureNode> children) {
        this.children = children;
    }
}
