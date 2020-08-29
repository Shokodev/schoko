package com.bacnetbrowser.schoko.bacnetutils.devices;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * This class is used to parse the BACnetDevice class in usable look for the frontend
 * -> LOMBOCK for easy creations
 *
 */


public class WaitingRoomDeviceFrontend {

    private boolean alreadyImported;
    @JsonProperty("name")
    private String name;
    @JsonProperty("modelName")
    private String modelName;
    @JsonProperty("description")
    private String ipAddress;
    @JsonProperty("instanceNumber")
    private int instanceNumber;

    public WaitingRoomDeviceFrontend(boolean alreadyImported, String name, String modelName, String ipAddress, int instanceNumber) {
        this.alreadyImported = alreadyImported;
        this.name = name;
        this.modelName = modelName;
        this.ipAddress = ipAddress;
        this.instanceNumber = instanceNumber;
    }

    public boolean isAlreadyImported() {
        return alreadyImported;
    }

    public void setAlreadyImported(boolean alreadyImported) {
        this.alreadyImported = alreadyImported;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getInstanceNumber() {
        return instanceNumber;
    }

    public void setInstanceNumber(int instanceNumber) {
        this.instanceNumber = instanceNumber;
    }
}
    //Example
    /*
    {
        "address": {
        "networkNumber": {
            "typeId": 2,
                    "smallValue": true
        },
        "macAddress": {
            "typeId": 6,
                    "length": 6,
                    "bytes": "wKgBsbrA",
                    "description": "192.168.1.177:47808"
        },
        "description": "192.168.1.177:47808",
                "global": false
    },
        "userData": null,
            "maxReadMultipleReferences": 200,
            "bacnetObjects":[],
        "name": "Site01'AS01",
            "servicesSupported": {
        "typeId": 8,
                "value": [
        true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                false,
                true,
                true,
                true,
                true,
                true,
                false,
                true,
                false,
                false,
                false,
                false,
                false,
                true,
                true,
                true,
                true,
                true,
                false,
                true,
                true,
                true,
                true,
                true,
                false,
                true,
                true
            ],
        "readPropertyMultiple": true,
                "getEnrollmentSummary": true,
                "confirmedPrivateTransfer": true,
                "unconfirmedPrivateTransfer": true,
                "confirmedTextMessage": false,
                "reinitializeDevice": true,
                "unconfirmedTextMessage": false,
                "confirmedCovNotification": true,
                "confirmedEventNotification": true,
                "removeListElement": true,
                "unconfirmedEventNotification": true,
                "unconfirmedCovNotification": true,
                "writePropertyMultiple": true,
                "deviceCommunicationControl": true,
                "lifeSafetyOperation": false,
                "subscribeCovPropertyMultiple": false,
                "timeSynchronization": true,
                "getEventInformation": true,
                "subscribeCovProperty": true,
                "utcTimeSynchronization": true,
                "confirmedCovNotificationMultiple": false,
                "acknowledgeAlarm": true,
                "getAlarmSummary": true,
                "atomicWriteFile": true,
                "vtOpen": false,
                "createObject": true,
                "atomicReadFile": true,
                "vtClose": false,
                "subscribeCov": true,
                "vtData": false,
                "readProperty": true,
                "writeProperty": true,
                "addListElement": true,
                "deleteObject": true,
                "whoIs": true,
                "whoHas": true,
                "ihave": true,
                "readRange": true,
                "iam": true,
                "writeGroup": false,
                "unconfirmedCovNotificationMultiple": false
    },
        "objectIdentifier": {
        "typeId": 12,
                "objectType": {
            "typeId": 9
        },
        "instanceNumber": 2098177,
                "uninitialized": false
    },
        "modelName": "PXC100-E.D / HW=V3.00",
            "instanceNumber": 2098177,
            "segmentationSupported": {
        "typeId": 9
    },
        "vendorIdentifier": 7,
            "maxAPDULengthAccepted": 1476,
            "vendorName": null
    }*/

