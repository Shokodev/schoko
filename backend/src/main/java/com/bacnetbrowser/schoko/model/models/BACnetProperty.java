package com.bacnetbrowser.schoko.model.models;


/**
 * Is used to generate Objects in needed format for frontend
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
public class BACnetProperty {
        private String value;
        private String propertyIdentifier;

    public BACnetProperty(String value, String propertyIdentifier) {
        this.value = value;
        this.propertyIdentifier = propertyIdentifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public void setPropertyIdentifier(String propertyIdentifier) {
        this.propertyIdentifier = propertyIdentifier;
    }



}
