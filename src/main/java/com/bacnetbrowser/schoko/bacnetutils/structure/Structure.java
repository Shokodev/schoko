package com.bacnetbrowser.schoko.bacnetutils.structure;

import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction of minimum what a structure node has to have
 */
public abstract class Structure {

    private String name;
    private String description;
    private List<Structure> children = new ArrayList<>();

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

    public List<Structure> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Structure> children){
        this.children = children;
    }

    public boolean containsChild(String name){
        for (Structure child : children) {
            if (child.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Structure getChild(String name) throws StructureBuildException {
        if (containsChild(name)) {
            for (Structure child : children) {
                if (child.getName().equals(name)) {
                    return child;
                }
            }
            return null;
        } else throw new StructureBuildException("No child with name: "
                + name + " in structure node: " + this.name);
    }



}
