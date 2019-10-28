package com.bacnetbrowser.schoko.model.models;

/**
 * is used to generate BACnet event Objects with needed properties for the client
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


    public BACnetEvent(String oid, String RemoteDeviceName, String timeStamp, String fromState, String toState, String description, String presentValue) {
        this.oid = oid;
        this.RemoteDeviceName = RemoteDeviceName;
        this.timeStamp = timeStamp;
        this.fromState = fromState;
        this.toState = toState;
        this.description = description;
        this.presentValue = presentValue;
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
}




