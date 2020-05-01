package com.bacnetbrowser.schoko.bacnetutils.models;

import com.bacnetbrowser.schoko.bacnetutils.services.DeviceService;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class BACnetDevice extends RemoteDevice {
    //Default Max APDU length accepted
    int maxAPDULengthAccepted = 1476;
    private ArrayList<BACnetObject> bacnetObjects = new ArrayList<>();
    private final Segmentation segmentation;
    private static final Logger LOG = LoggerFactory.getLogger(BACnetDevice.class);

    public BACnetDevice(LocalDevice localDevice, int instanceNumber, Address address,Segmentation segmentation) {
        super(localDevice, instanceNumber, address);
        this.segmentation = segmentation;
    }

    @Override
    public Segmentation getSegmentationSupported() {
        return this.segmentation;
    }

    @Override
    public int getMaxAPDULengthAccepted() {
        return maxAPDULengthAccepted;
    }

    public ArrayList<BACnetObject> getBacnetObjects() {
        return bacnetObjects;
    }

    public BACnetObject getBACnetObject(ObjectIdentifier oid){
        for(BACnetObject ob : bacnetObjects){
            if(ob.getObjectIdentifier().equals(oid)){
                return ob;
            }
        }
        return null;
    }


}
