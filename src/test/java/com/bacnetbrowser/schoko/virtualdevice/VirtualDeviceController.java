package com.bacnetbrowser.schoko.virtualdevice;

import com.bacnetbrowser.schoko.bacnetutils.devices.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.devices.DeviceService;

import com.serotonin.bacnet4j.exception.BACnetServiceException;

import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;

import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VirtualDeviceController {
    private BACnetDevice bacnetDevice;
    private static final Logger LOG = LoggerFactory.getLogger(VirtualDeviceController.class);

    public void createVirtualDevice(){
        this.bacnetDevice = new BACnetDevice(DeviceService.localDevice,
                1234,DeviceService.localDevice.getLoopbackAddress(),
                Segmentation.segmentedBoth,1212,1476);
        createBinaryValueObject();
    }

    private void createBinaryValueObject(){
        try {

            LOG.debug("Create Binary Value Object");
            VirtualBinaryValueObject binaryValue = new VirtualBinaryValueObject(DeviceService.localDevice,1,
                    "B1'E'BV01",BinaryPV.inactive,false,new CharacterString("Test Alarm 1"));
            binaryValue.supportStateText("Normal","Alarm");
            binaryValue.supportCovReporting();
            binaryValue.supportActiveTime();
            binaryValue.supportWritable();
            binaryValue.supportIntrinsicReporting(
                    0,
                    12,
                    BinaryPV.active,
                    new EventTransitionBits(true,true,true),
                    NotifyType.alarm,
                    0);
        } catch (BACnetServiceException e) {
            e.printStackTrace();
        }
    }
}
