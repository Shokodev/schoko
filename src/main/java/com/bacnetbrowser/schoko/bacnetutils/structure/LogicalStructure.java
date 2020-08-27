package com.bacnetbrowser.schoko.bacnetutils.structure;

import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetNode;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetObject;
import com.bacnetbrowser.schoko.bacnetutils.services.DeviceService;
import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogicalStructure extends Structure implements Structurable {
    private static final Logger LOG = LoggerFactory.getLogger(LogicalStructure.class);

    public LogicalStructure() {
        super(StructureTypes.LOGIC.toString(), "Logical View");
    }

    @Override
    public void build() throws StructureBuildException {
        int nodeCounter = 0;
        for (BACnetDevice bacnetDevice : DeviceService.bacnetDevices) {
            StructureNode device = new StructureNode(bacnetDevice.getVendorName(),bacnetDevice.getModelName(),
                    "Structure Element",bacnetDevice.getName());
            this.getChildren().add(device);
            for (BACnetObject bacnetObject : bacnetDevice.getBacnetObjects()) {
                if(StructureService.checkIfNecessaryForStructure(bacnetObject.getObjectType())){
                    addObjectToPropertyGroup(bacnetObject, device);
                }
                nodeCounter++;
            }
        }
        if (this.getChildren().isEmpty()) {
            throw new StructureBuildException("No device nodes were created, check settings!");
        }
        LOG.info("Device structure successfully created with " + nodeCounter + " nodes");
    }

    private void addObjectToPropertyGroup(BACnetObject bacnetObject, StructureNode device) throws StructureBuildException {
        ObjectType objectType = bacnetObject.getObjectIdentifier().getObjectType();
        StructureNode object = new StructureNode(bacnetObject.getObjectIdentifier().toString(),
                StructureService.objectNamesToDescription.get(bacnetObject.getObjectName()),
                bacnetObject.getObjectIdentifier().toString(),
                bacnetObject.getObjectName());
        StructureNode propertyGroup = new StructureNode(objectType.toString(),objectType.toString(),"Structure Element"," ");
        if (device.getChild(propertyGroup.getName()) == null) {
            device.getChildren().add(propertyGroup);
            propertyGroup.getChildren().add(object);
        } else {
            device.getChild(propertyGroup.getName()).getChildren().add(object);
        }

    }

}
