package com.bacnetbrowser.schoko.bacnetutils.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WaitingRoomDeviceFrontend {
    @JsonProperty("name")
    public String name;
    @JsonProperty("modelName")
    public String modelName;
    @JsonProperty("ipAddress")
    public String ipAddress;
    @JsonProperty("instanceNumber")
    public int instanceNumber;

    public WaitingRoomDeviceFrontend(String name, String modelName, String ipAddress, int instanceNumber) {
        this.name = name;
        this.modelName = modelName;
        this.ipAddress = ipAddress;
        this.instanceNumber = instanceNumber;
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
