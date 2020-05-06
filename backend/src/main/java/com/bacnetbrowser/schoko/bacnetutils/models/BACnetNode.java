package com.bacnetbrowser.schoko.bacnetutils.models;

import java.util.ArrayList;
import java.util.List;


/**
 * Is used to generate Objects in needed format for frontend
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
public class BACnetNode {

    private String name;
    private String objectName;
    private String objectIdentifier;
    private String description;
    private List<BACnetNode> children;

    // BACnet Object
    public BACnetNode(String objectName, String objectIdentifier, String description, String name) {
        this.objectName = objectName;
        this.objectIdentifier = objectIdentifier;
        this.description = description;
        this.name = name;
    }
    // Structure Object
    public BACnetNode(String name, String objectIdentifier, String description,List<BACnetNode> children){
        this.children = children;
        this.description = description;
        this.objectIdentifier = objectIdentifier;
        this.name = name;
    }

    public String getName() { return name; }

    public String getObjectName() {
        return objectName;
    }

    public String getObjectIdentifier() {
        return objectIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public List<BACnetNode> getChildren() {
        return children;
    }

    public BACnetNode addChild(BACnetNode child){
        this.children.add(child);
        return child;
    }

    /**
     * Check if parent has specific child by a string equals object name of the child
     * @param objectName element name to check
     * @return boolean
     */
    public boolean checkIfChildNodeAlreadyExists(String objectName){
        List<BACnetNode> children = getChildren();
        for (BACnetNode child : children) {
            if (child.getName().equals(objectName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return a specific child of the current node by element name
     * @param splittedObjectName
     * @return BACnetStructure<Structure> child
     */
    public BACnetNode getChildBySplittedObjectName(String splittedObjectName) {
        List<BACnetNode> children = getChildren();
        for(BACnetNode child : children) {
            if (child.getName().equals(splittedObjectName)) {
                return child;
            }
        }
        return null;
    }
}
