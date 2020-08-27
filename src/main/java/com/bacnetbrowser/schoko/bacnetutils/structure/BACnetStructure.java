package com.bacnetbrowser.schoko.bacnetutils.structure;

import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;
import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
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
                }else {
                    parent = getParentNode(this,i,splittedObjectName);
                }
                StructureNode node = createNode((i == (splittedObjectName.length -1)),splittedObjectName[i],
                        StructureService.objectNamesToObjectIdentifier.get(objectName).getObjectType().toString(),
                        StructureService.objectNamesToDescription.get(objectName), parent,objectName);
                if (node != null) {
                    parent.getChildren().add(node);
                    nodeCounter++;
                }
            }
        }
        if (this.getChildren().isEmpty()) {
            throw new StructureBuildException("No bacNet nodes were created, check settings!");
        }
        LOG.info("BacNet structure successfully created with " + nodeCounter + " nodes");
    }

    private Structure getParentNode(Structure bacnetStructure, int counter, String[] splittedObjectName) throws StructureBuildException {
        Structure parent = bacnetStructure;
        for (int i = 0; i < counter; i++) {
            parent = parent.getChild(splittedObjectName[i]);
        }
        return parent;
    }

    private StructureNode createNode(boolean lastInLoop,String splittedObjectName, String type, String description, Structure parent,String objectName) {
            if (StructureService.structureElements.containsKey(splittedObjectName) && !(parent.containsChild(splittedObjectName))) {
                return new StructureNode(splittedObjectName,StructureService.structureElements.get(splittedObjectName), "Structure Element", " ");
            } else if (parent.containsChild(splittedObjectName) && (!type.equals("Structure Element") && lastInLoop)) {
                return new StructureNode(splittedObjectName, description, type, objectName);
            } else if (parent.containsChild(splittedObjectName)) {
                return new StructureNode(splittedObjectName, "No structure view objects found",type,objectName);
            }
        return null;
    }
}
