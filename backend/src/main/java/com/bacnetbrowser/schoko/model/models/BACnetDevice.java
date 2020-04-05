package com.bacnetbrowser.schoko.model.models;

import com.bacnetbrowser.schoko.model.services.DeviceService;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.ArrayList;


public class BACnetDevice extends RemoteDevice {

    private ArrayList<BACnetObject> bacnetObjects = new ArrayList<>();
    private RemoteDevice remoteDevice;
    private static final Logger LOG = LoggerFactory.getLogger(BACnetDevice.class);

    public BACnetDevice(LocalDevice localDevice, int instanceNumber, Address address,RemoteDevice remoteDevice) {
        super(localDevice, instanceNumber, address);
        this.remoteDevice = remoteDevice;
    }

    @Override
    public ServicesSupported getServicesSupported() {
        return super.getServicesSupported();
    }

    public ArrayList<BACnetObject> getBacnetObjects() {
        return bacnetObjects;
    }

    public Encodable readProperty(PropertyIdentifier propertyIdentifier) {
        try {
            return RequestUtils.readProperty(DeviceService.localDevice,remoteDevice,remoteDevice.getObjectIdentifier(), propertyIdentifier,null);
        } catch (BACnetException bac) {
            LOG.warn("Can't read " + propertyIdentifier + " of " + this.getName());
        }
        return new CharacterString("COM");
    }

    public BACnetObject getBACnetObject(ObjectIdentifier oid){
        for(BACnetObject ob : bacnetObjects){
            if(ob.getObjectIdentifier().equals(oid)){
                return ob;
            }
        }
        return null;
    }

    @Override
    public int getInstanceNumber() {
        return remoteDevice.getInstanceNumber();
    }

    @Override
    public ObjectIdentifier getObjectIdentifier() {
        return remoteDevice.getObjectIdentifier();
    }

    @Override
    public String getVendorName() {
        return remoteDevice.getVendorName();
    }

    public RemoteDevice getBacnetDeviceInfo(){
        return this.remoteDevice;
    }
}
