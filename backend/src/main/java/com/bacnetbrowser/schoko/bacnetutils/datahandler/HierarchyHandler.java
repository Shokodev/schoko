package com.bacnetbrowser.schoko.bacnetutils.datahandler;


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

    private HierarchyService hierarchyService;
    private SettingsHandler settingsHandler;
    private static final Logger LOG = LoggerFactory.getLogger(HierarchyService.class);

    @Autowired
    public HierarchyHandler(SettingsHandler settingsHandler) {
        this.hierarchyService = new HierarchyService();
        this.settingsHandler = settingsHandler;
    }

    /**
     * Returns Node with his children without children of the children
     * @param objectName as said
     * @return Object to Rest
     */
    public BACnetNode getNodeByObjectName(String objectName) {
            BACnetNode structure = getBacnetStructure();
            String[] splitted = objectName.split(settingsHandler.getBacnetSeparator());
        try {
            //Check if its the top element
            if ((splitted.length == 1) && (settingsHandler.getSiteName().equals(objectName))) {
                return structure;
            } else {
                BACnetNode node = null;
                for (int i = 1; i < splitted.length; i++) {
                    node = structure.getChildByObjectName(splitted[i]);
                    structure = node;
                }
                return node;
            }
                } catch (NullPointerException e){
                    LOG.info("No structure yet, check settings!");
                    return new BACnetNode("Achtung!"," ","Bitte zuerst Einstellungen vornehmen",0);
                }
        }

    /**
     * delete structure if exists, read BacNet network and create new structure
     * @param siteName defined in defaultSettings
     * @param siteDescription defined in defaultSettings
     * @param structureSeparator defined in defaultSettings
     */
    public void createStructure(String siteName, String siteDescription, String structureSeparator){
        hierarchyService.deleteStructure();
        hierarchyService.create(siteName, siteDescription, structureSeparator);

    }

    public BACnetNode getBacnetStructure() {
        return hierarchyService.getBacnetStructure();
    }

    public BACnetNode getDeviceStructure(){
        return hierarchyService.getDeviceStructure();
    }
}


