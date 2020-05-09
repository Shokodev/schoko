package com.bacnetbrowser.schoko.datahandler;


import com.bacnetbrowser.schoko.bacnetutils.models.BACnetNode;
import com.bacnetbrowser.schoko.bacnetutils.services.HierarchyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class used to handel the hierarchy
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class HierarchyHandler {

    private final HierarchyService hierarchyService;
    private static final Logger LOG = LoggerFactory.getLogger(HierarchyService.class);


    public HierarchyHandler() {
        this.hierarchyService = new HierarchyService();
    }

    /**
     * Returns Node with his children without children of the children
     * @param objectName as said
     * @return Object to Rest
     */
    public BACnetNode getNodeByObjectName(String objectName) {
            BACnetNode structure = getBacnetStructure();
            String[] splitted = objectName.split(SettingsHandler.siteName);
        try {
            //Check if its the top element
            if ((splitted.length == 1) && (SettingsHandler.siteName.equals(objectName))) {
                return structure;
            } else {
                BACnetNode node = null;
                for (int i = 1; i < splitted.length; i++) {
                    node = structure.getChildBySplittedObjectName(splitted[i]);
                    structure = node;
                }
                return node;
            }
                } catch (NullPointerException e){
                    LOG.info("No structure yet, check settings!");
                    return new BACnetNode("Achtung!"," ","Bitte zuerst Einstellungen vornehmen","");
                }
        }

    /**
     * delete structure if exists, read BacNet network and create new structure
     */
    public void createStructure(){
        hierarchyService.deleteStructure();
        hierarchyService.create(SettingsHandler.siteName, SettingsHandler.siteDescription,
                SettingsHandler.bacnetSeparator);
    }

    public BACnetNode getBacnetStructure() {
        return hierarchyService.getBacnetStructure();
    }

    public BACnetNode getDeviceStructure(){
        return hierarchyService.getDeviceStructure();
    }
}


