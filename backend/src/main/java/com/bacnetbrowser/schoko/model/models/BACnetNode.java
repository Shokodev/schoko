package com.bacnetbrowser.schoko.model.models;

import java.util.ArrayList;
import java.util.List;


/**
 * Is used to generate Objects in needed format for frontend
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
public class BACnetNode {

    private String elementName;
    private String elementType;
    private String elementDescription;
    private Integer device;
    private List<BACnetNode> children = new ArrayList<>();


    public BACnetNode(String elementName, String elementType, String elementDescription, Integer device) {
        this.elementName = elementName;
        this.elementType = elementType;
        this.elementDescription = elementDescription;
        this.device = device;
    }

    public String getElementName() {
        return elementName;
    }

    public String getElementType() {
        return elementType;
    }

    public String getElementDescription() {
        return elementDescription;
    }

    public List<BACnetNode> getChildren() {
        return children;
    }

    public Integer getDevice() {
        return device;
    }

    public BACnetNode addChild(BACnetNode child){
        this.children.add(child);
        return child;
    }

}