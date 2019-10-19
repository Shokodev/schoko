package com.bacnetbrowser.schoko.model.models;


public class BACnetEvents {

    // Event Properties
    private Integer eventID = 0;
    private Integer processIdentifier;
    private String oid;
    private Integer oidRemotDevice;
    private String timeStamp;
    private Integer notificationClass;
    private Integer priority;
    private String eventType;
    private String messageText;
    private String notifyType;
    private String ackRequired;
    private String fromState;
    private String toState;
    private String eventValue;

    // Object Properties
    private String description;
    private String presentValue;


    public BACnetEvents(Integer processIdentifier, String oid, Integer oidRemotDevice, String timeStamp, Integer notificationClass, Integer priority, String eventType, String messageText, String notifyType, String ackRequired, String fromState, String toState, String eventValue, String description, String presentValue) {
        eventID++;
        this.processIdentifier = processIdentifier;
        this.oid = oid;
        this.oidRemotDevice = oidRemotDevice;
        this.timeStamp = timeStamp;
        this.notificationClass = notificationClass;
        this.priority = priority;
        this.eventType = eventType;
        this.messageText = messageText;
        this.notifyType = notifyType;
        this.ackRequired = ackRequired;
        this.fromState = fromState;
        this.toState = toState;
        this.eventValue = eventValue;
        this.description = description;
        this.presentValue = presentValue;
    }
}
