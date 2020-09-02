package com.bacnetbrowser.schoko.bacnetutils.devices;

import com.bacnetbrowser.schoko.bacnetutils.objects.BACnetObject;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;


public class BACnetDevice extends RemoteDevice {
    //Default Max APDU length accepted
    private final ArrayList<BACnetObject> bacnetObjects = new ArrayList<>();
    private Boolean isAlreadyImported = false;
    private static final Logger LOG = LoggerFactory.getLogger(BACnetDevice.class);

    public BACnetDevice(LocalDevice localDevice, int instanceNumber, Address address, Segmentation segmentation,
                  int vendorIdentifier,int maxAPDULengthAccepted) {
        super(localDevice, instanceNumber, address);
        this.setDeviceProperty(PropertyIdentifier.maxApduLengthAccepted, new UnsignedInteger(maxAPDULengthAccepted));
        this.setDeviceProperty(PropertyIdentifier.segmentationSupported, segmentation);
        this.setDeviceProperty(PropertyIdentifier.vendorIdentifier, new UnsignedInteger(vendorIdentifier));
    }

    public ArrayList<BACnetObject> getBacnetObjects() {
        return bacnetObjects;
    }

    public BACnetObject getBACnetObject(ObjectIdentifier oid){
        try{
            for(BACnetObject ob : bacnetObjects){
                if(ob.getObjectIdentifier().equals(oid)){
                    return ob;
                }
            }
        }catch (NullPointerException e){
            LOG.info("Object: {} does not exist on {}",this.getName(),oid);
        }
        return null;
    }

    public Boolean getAlreadyImported() {
        return isAlreadyImported;
    }

    public void setAlreadyImported(Boolean alreadyImported) {
        isAlreadyImported = alreadyImported;
    }
}

