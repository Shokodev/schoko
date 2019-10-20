package com.bacnetbrowser.schoko.model.models;


public class BACnetEvent {

    // Event Properties
    private Integer eventID = 0;
    private String oid;
    private Integer oidRemoteDevice;
    private String timeStamp;
    private String fromState;
    private String toState;


    // Object Properties
    private String description;
    private String presentValue;


    public BACnetEvent(Integer eventID, String oid, Integer oidRemoteDevice, String timeStamp, String fromState, String toState, String description, String presentValue) {
        this.eventID = eventID;
        this.oid = oid;
        this.oidRemoteDevice = oidRemoteDevice;
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

    public Integer getOidRemoteDevice() {
        return oidRemoteDevice;
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
}




