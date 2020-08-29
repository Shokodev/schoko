package com.bacnetbrowser.schoko.bacnetutils.structure.pattern;

import com.bacnetbrowser.schoko.bacnetutils.devices.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.objects.BACnetObject;
import com.bacnetbrowser.schoko.bacnetutils.devices.DeviceService;
import com.bacnetbrowser.schoko.bacnetutils.structure.Structurable;
import com.bacnetbrowser.schoko.bacnetutils.structure.Structure;
import com.bacnetbrowser.schoko.bacnetutils.structure.StructureService;
import com.bacnetbrowser.schoko.bacnetutils.structure.StructureTypes;
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
            StructureViewNode device = new StructureViewNode(bacnetDevice.getName(),bacnetDevice.getModelName());
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
        LOG.info("Logical structure successfully created with " + nodeCounter + " nodes");
    }

    @Override
    public void delete() {
        this.getChildren().clear();
    }

    private void addObjectToPropertyGroup(BACnetObject bacnetObject, Structure device) throws StructureBuildException {
        ObjectType objectType = bacnetObject.getObjectIdentifier().getObjectType();
        StructureObjectNode object = new StructureObjectNode(bacnetObject.getObjectIdentifier().toString(),
                StructureService.objectNamesToDescription.get(bacnetObject.getObjectName()),
                bacnetObject.getObjectIdentifier().toString(),
                bacnetObject.getObjectName());
        StructureViewNode propertyGroup = new StructureViewNode(objectType.toString(),objectType.toString());
        if (!device.containsChild(propertyGroup.getName())) {
            device.getChildren().add(propertyGroup);
            propertyGroup.getChildren().add(object);
        } else {
            device.getChild(propertyGroup.getName()).getChildren().add(object);
        }

    }

}
