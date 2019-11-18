package com.bacnetbrowser.schoko.model.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Objects of this class are the BACnet structure
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
public class BACnetStructure {
    private String elementName;
    private String elementType;
    private String elementDescription;
    private Integer device;

   private List<BACnetStructure> children = new ArrayList<>();

   public BACnetStructure(String elementName, String elementType, String elementDescription, Integer device) {
        this.elementName = elementName;
        this.elementType = elementType;
        this.elementDescription = elementDescription;
        this.device = device;
    }

   public void addChild(BACnetStructure child) {
        this.children.add(child);
    }

   public List<BACnetStructure> getChildren() {
        return children;
    }

    /**
     * Check if parent has specific child by a string equals element name of the child
     * @param parent current parent
     * @param elementName element name to check
     * @return boolean
     */
   public boolean checkIfNodeExists(BACnetStructure parent, String elementName){
        List<BACnetStructure> children = parent.getChildren();
        for (BACnetStructure child : children) {
            if (child.getElementName().equals(elementName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return a specific child of the current node by element name
     * @param elementName object identifier
     * @return BACnetStructure<Structure> child
     */
   public BACnetStructure getChildByElementName(String elementName) {
        List<BACnetStructure> children = getChildren();
        for(BACnetStructure child : children) {
            if (child.getElementName().equals(elementName)) {
                return child;
            }
        }
        return null;
    }

   public String getElementName() {
        return elementName;
    }

   public String getElementType() { return elementType; }

   public String getElementDescription() {
        return elementDescription;
    }

   public Integer getDevice() {
        return device;
    }
}

