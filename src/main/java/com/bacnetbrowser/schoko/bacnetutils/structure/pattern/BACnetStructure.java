package com.bacnetbrowser.schoko.bacnetutils.structure.pattern;

import com.bacnetbrowser.schoko.bacnetutils.structure.Structurable;
import com.bacnetbrowser.schoko.bacnetutils.structure.Structure;
import com.bacnetbrowser.schoko.bacnetutils.structure.StructureService;
import com.bacnetbrowser.schoko.bacnetutils.structure.StructureTypes;
import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;
import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BACnetStructure extends Structure implements Structurable {
    private static final Logger LOG = LoggerFactory.getLogger(BACnetStructure.class);

    public BACnetStructure() {
        super(StructureTypes.BACNET.toString(), "BACnet View");
    }

    @Override
    public void build() throws StructureBuildException {
        int nodeCounter = 0;
        for (String objectName : StructureService.objectNamesToObjectIdentifier.keySet()) {
            String[] splittedObjectName = objectName.split(SettingsHandler.bacnetSeparator);
            for (int i = 0; i < splittedObjectName.length; i++) {
                Structure parent;
                if (this.getChildren().isEmpty()) {
                    parent = this;
                } else {
                    parent = getParentNode(this,i,splittedObjectName);
                }
                if (!parent.containsChild(splittedObjectName[i])) {
                    parent.getChildren().add(
                            createNode((i == (splittedObjectName.length -1)),splittedObjectName[i],
                                    StructureService.objectNamesToObjectIdentifier.get(objectName).getObjectType(),
                                    StructureService.objectNamesToDescription.get(objectName),objectName)
                    );
                    nodeCounter++;
                }
            }
        }
        if (this.getChildren().isEmpty()) {
            throw new StructureBuildException("No BACnet nodes were created, check settings!");
        }
        LOG.info("BACnet structure successfully created with " + nodeCounter + " nodes");
    }

    @Override
    public void delete() {
        this.getChildren().clear();
    }

    private Structure getParentNode(Structure bacnetStructure, int counter, String[] splittedObjectName)
            throws StructureBuildException {
        Structure parent = bacnetStructure;
        for (int i = 0; i < counter; i++) {
            parent = parent.getChild(splittedObjectName[i]);
        }
        return parent;
    }

    private Structure createNode(boolean lastInLoop, String name, ObjectType type, String description, String objectName) {
        if (lastInLoop) {
            return new StructureObjectNode(name, description, type.toString(), objectName);
        } else {
            if (StructureService.structureElements.isEmpty()) {
                return new StructureViewNode(name, "No structure view objects found");
            } else {
                return new StructureViewNode(name, StructureService.structureElements.get(name));
            }
        }
    }
}

