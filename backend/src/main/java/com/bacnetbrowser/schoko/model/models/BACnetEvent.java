package com.bacnetbrowser.schoko.model.models;


import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Is used to generate BACnet event Objects with needed properties for the client
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */

@Entity
public class BACnetEvent {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    private int eventID;

    // Event Properties
    private String processIdentifier;
    private String remoteDeviceName;
    private String oid;
    private java.sql.Timestamp timeStamp;
    private String notificationClass;
    private String priority;
    private String eventType;
    private String messageText;
    private String notifyType;
    private String ackRequired;
    private String fromState;
    private String toState;
    private String eventValues;

    // Object Properties
    private String description;
    private String presentValue;
    private String objectName;


    public BACnetEvent() {
    }

    public BACnetEvent(String processIdentifier, String remoteDeviceName, String oid, Timestamp timeStamp,
                       String notificationClass, String priority, String eventType, String messageText, String notifyType,
                       String ackRequired, String fromState, String toState, String eventValues, String description,
                       String presentValue, String objectName) {
        this.processIdentifier = processIdentifier;
        this.remoteDeviceName = remoteDeviceName;
        this.oid = oid;
        this.timeStamp = timeStamp;
        this.notificationClass = notificationClass;
        this.priority = priority;
        this.eventType = eventType;
        this.messageText = messageText;
        this.notifyType = notifyType;
        this.ackRequired = ackRequired;
        this.fromState = fromState;
        this.toState = toState;
        this.eventValues = eventValues;
        this.description = description;
        this.presentValue = presentValue;
        this.objectName = objectName;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {

        this.eventID = eventID;
    }

    public String getProcessIdentifier() {
        return processIdentifier;
    }

    public void setProcessIdentifier(String processIdentifier) {
        this.processIdentifier = processIdentifier;
    }

    public String getRemoteDeviceName() {
        return remoteDeviceName;
    }

    public void setRemoteDeviceName(String remoteDeviceName) {
        this.remoteDeviceName = remoteDeviceName;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNotificationClass() {
        return notificationClass;
    }

    public void setNotificationClass(String notificationClass) {
        this.notificationClass = notificationClass;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getAckRequired() {
        return ackRequired;
    }

    public void setAckRequired(String ackRequired) {
        this.ackRequired = ackRequired;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public String getEventValues() {
        return eventValues;
    }

    public void setEventValues(String eventValues) {
        this.eventValues = eventValues;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPresentValue() {
        return presentValue;
    }

    public void setPresentValue(String presentValue) {
        this.presentValue = presentValue;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

}







