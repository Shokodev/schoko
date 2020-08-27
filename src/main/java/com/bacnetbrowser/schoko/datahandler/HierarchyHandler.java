package com.bacnetbrowser.schoko.datahandler;


import com.bacnetbrowser.schoko.bacnetutils.structure.*;
import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This class used to handel the hierarchy
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class HierarchyHandler {

    private final StructureService structureService;
    private static final Logger LOG = LoggerFactory.getLogger(StructureService.class);

    public HierarchyHandler() {
        this.structureService = new StructureService();
    }

    /**
     * Returns Node with his children without children of the children
     * @param objectName as said
     * @return Object to Rest
     */
    public Structure getNodeByObjectName(String objectName) {
            Structure structure = getBacnetStructure();
            String[] splitted = objectName.split(SettingsHandler.bacnetSeparator);
        try {
            //Check if its the top element
            //TODO Type confirmation
            if ((splitted.length == 1) && (StructureTypes.BACNET.toString().equals(objectName))) {
                return structure;
            } else {
                Structure node = null;
                for (int i = 1; i < splitted.length; i++) {
                    node = structure.getChild(splitted[i]);
                    structure = node;
                }
                return node;
            }
                } catch (StructureBuildException e){
                    LOG.info("No structure yet, check settings!");
                    return null;
                }
        }

    public Structure getBacnetStructure() {
        return structureService.getBacnetStructure();
    }

    public Structure getLogicalStructure(){
        return structureService.getLogicalStructure();
    }

    public void createStructure(){
        try {
            structureService.build();
        } catch (StructureBuildException e){
            LOG.error("Structure build failed " + e.fillInStackTrace());
        }
    }

}


