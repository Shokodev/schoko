package com.bacnetbrowser.schoko.model.models;

/**
 * Is used to generate BACnet event Objects with needed properties for the client
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
public class BACnetEvent {

    // Event Properties
    private Integer eventID;
    private String oid;
    private String RemoteDeviceName;
    private String timeStamp;
    private String fromState;
    private String toState;

    // Object Properties
    private String description;
    private String presentValue;
    private String objectName;


    public BACnetEvent(String oid, String remoteDeviceName, String timeStamp, String fromState, String toState, String description, String presentValue, String objectName) {
        this.oid = oid;
        RemoteDeviceName = remoteDeviceName;
        this.timeStamp = timeStamp;
        this.fromState = fromState;
        this.toState = toState;
        this.description = description;
        this.presentValue = presentValue;
        this.objectName = objectName;
    }

    public Integer getEventID() {
        return eventID;
    }

    public String getOid() {
        return oid;
    }

    public String getRemoteDeviceName() {
        return RemoteDeviceName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getFromState() {
        return fromState;
    }

    public String getToState() {
        return toState;
    }

    public String getDescription() {
        return description;
    }

    public String getPresentValue() {
        return presentValue;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setRemoteDeviceName(String remoteDeviceName) {
        RemoteDeviceName = remoteDeviceName;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPresentValue(String presentValue) {
        this.presentValue = presentValue;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}




