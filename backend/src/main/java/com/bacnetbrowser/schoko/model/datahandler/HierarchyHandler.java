package com.bacnetbrowser.schoko.model.datahandler;

import com.bacnetbrowser.schoko.model.models.BACnetNode;
import com.bacnetbrowser.schoko.model.models.BACnetStructure;
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

    @Autowired
    private HierarchyService hierarchyService;
    @Autowired
    SettingsHandler settingsHandler;

    /**
     * Returns Node with his children without children of the children
     * @param structure as said
     * @return Object to Rest
     */
    public BACnetNode getChildrenByNodeElementName(String structure) {
        BACnetStructure tempNode = hierarchyService.getBacnetStructure();
        String[] splitted = structure.split(hierarchyService.getStructureSeparator());
        if ((splitted.length == 1) && (settingsHandler.getSiteName().equals(structure))) {
            return createAndAddChildren(tempNode);

        }else{
        BACnetStructure node = null;
            for (int i = 1; i < splitted.length; i++) {
            node = tempNode.getChildByElementName(splitted[i]);
            tempNode = node;
        }
            return createAndAddChildren(node);
        }}

    /**
     * Needed for the method getChildrenByNodeElementName for create the nodes
     * @param parent as said
     * @return the Object to send to REST
     */
    private BACnetNode createAndAddChildren(BACnetStructure parent) {
        BACnetNode nodeTopElement = new BACnetNode(parent.getElementName(),parent.getElementType(),parent.getElementDescription(),parent.getDevice());
        List<BACnetStructure> children = parent.getChildren();
        for (BACnetStructure child : children ) {
            BACnetNode childForJSON = new BACnetNode(child.getElementName(),child.getElementType(),child.getElementDescription(),child.getDevice());
            nodeTopElement.addChild(childForJSON);
        }
        return nodeTopElement;
    }

    /**
     * delete structure if exists, read BacNet network and create new structure
     * @param siteName defined in defaultSettings
     * @param siteDescription defined in defaultSettings
     * @param structureSeparator defined in defaultSettings
     */
    public void createStructure(String siteName, String siteDescription, String structureSeparator){
        hierarchyService.deleteStructure();
        hierarchyService.create(siteName,siteDescription,structureSeparator);
    }

    public BACnetStructure getStructure() {
        return hierarchyService.getBacnetStructure();
    }



}


