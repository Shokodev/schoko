package com.bacnetbrowser.schoko.bacnetutils.models;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class BACnetDevice extends RemoteDevice {
    //Default Max APDU length accepted
    int maxAPDULengthAccepted = 1476;
    private final ArrayList<BACnetObject> bacnetObjects = new ArrayList<>();
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


}
