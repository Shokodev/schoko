package com.bacnetbrowser.schoko.model.models;

import java.util.ArrayList;
import java.util.List;


/**
 * Is used to generate Objects in needed format for frontend
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
public class BACnetNode {

    private String objectName;
    private String objectIdentifier;
    private String description;
    private Integer deviceInstanceNumber;
    private List<BACnetNode> children = new ArrayList<>();


    public BACnetNode(String ObjectName, String objectIdentifier, String description, Integer deviceInstanceNumber) {
        this.objectName = ObjectName;
        this.objectIdentifier = objectIdentifier;
        this.description = description;
        this.deviceInstanceNumber = deviceInstanceNumber;
    }

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

    public Integer getDeviceInstanceNumber() {
        return deviceInstanceNumber;
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
            if (child.getObjectName().equals(objectName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return a specific child of the current node by element name
     * @param objectName object identifier
     * @return BACnetStructure<Structure> child
     */
    public BACnetNode getChildByObjectName(String objectName) {
        List<BACnetNode> children = getChildren();
        for(BACnetNode child : children) {
            if (child.getObjectName().equals(objectName)) {
                return child;
            }
        }
        return null;
    }

}
