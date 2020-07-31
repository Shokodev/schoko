package com.bacnetbrowser.schoko.bacnetutils.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class PermanentDevices {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    private int deviceId;

    public PermanentDevices(int deviceId) {
        this.deviceId = deviceId;
    }

    public PermanentDevices() {
        //Initial
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
