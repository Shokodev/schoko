package com.bacnetbrowser.schoko.model.datahandler;


import com.bacnetbrowser.schoko.model.models.BACnetNode;
import com.bacnetbrowser.schoko.model.services.HierarchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @Autowired
    public HierarchyHandler(HierarchyService hierarchyService, SettingsHandler settingsHandler) {
        this.hierarchyService = hierarchyService;
        this.settingsHandler = settingsHandler;
    }

    /**
     * Returns Node with his children without children of the children
     * @param objectName as said
     * @return Object to Rest
     */
    public BACnetNode getChildrenByObjectName(String objectName) {
            BACnetNode tempNode = hierarchyService.getBacnetStructure();
            String[] splitted = objectName.split(settingsHandler.getBacnetSeparator());
        try {
            //Check if its the top element
            if ((splitted.length == 1) && (settingsHandler.getSiteName().equals(objectName))) {
                return createAndAddChildren(tempNode);
            } else {
                BACnetNode node = null;
                for (int i = 1; i < splitted.length; i++) {
                    node = tempNode.getChildByObjectName(splitted[i]);
                    tempNode = node;
                }
                return createAndAddChildren(node);
            }
                } catch (NullPointerException e){
                    return new BACnetNode("Achtung!"," ","Bitte zuerst Einstellungen vornehmen",0);
                }
        }

    /**
     * Needed for the method getChildrenByNodeElementName for create the nodes of the given parent
     * @param parent as said
     * @return the Object to send to REST
     */
    private BACnetNode createAndAddChildren(BACnetNode parent) {
        BACnetNode node = new BACnetNode(parent.getObjectName(),parent.getObjectIdentifier(),parent.getDescription(),parent.getDeviceInstanceNumber());
        List<BACnetNode> children = parent.getChildren();
        for (BACnetNode child : children ) {
            BACnetNode childForJSON = new BACnetNode(child.getObjectName(),child.getObjectIdentifier(),child.getDescription(),child.getDeviceInstanceNumber());
            node.addChild(childForJSON);
        }
        return node;
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


