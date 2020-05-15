package com.bacnetbrowser.schoko.bacnetutils.services;

import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetNode;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetObject;
import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * This class used to generate a BACnet structure by the BACnet "Structure View" objects
 * Communication to Network in here
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */

public class HierarchyService {
    //This lists have main information about structure an objects
    static HashMap<String, ObjectIdentifier> objectNamesToOids = new HashMap<>();
    static HashMap<String, String> objectNamesToDescription = new HashMap<>();
    static HashMap<String, BACnetDevice> obejctNamesToBACnetDevice = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(HierarchyService.class);

    private BACnetNode bacnetStructure;
    private BACnetNode deviceStructure;
    private final HashMap<String, String> structureElements = new HashMap<>();
    private String structureSeparator;
    private final String structureElement = "Structure Element";
    public static final String structureTypeBACnet = "BAC";
    public static final String structureTypeLogic = "LOGIC";


    /**
     * Initialize the needed properties
     */
    public void createStructure() {
        this.structureSeparator = SettingsHandler.bacnetSeparator;
        createStaticBACnetObjectLists();
        this.bacnetStructure = buildBacNetStructure();
        this.deviceStructure = buildDeviceStructure();
    }

    /**
     * Reads all BACnet Objects of all remote devises
     */
    private void createStaticBACnetObjectLists() {
        for (BACnetDevice bacnetDevice : DeviceService.bacnetDevices) {
                for (BACnetObject bacnetObject : bacnetDevice.getBacnetObjects()) {
                    if (checkIfNecessaryForStructure(bacnetObject.getObjectIdentifier().getObjectType())) {
                        objectNamesToOids.put(bacnetObject.getObjectName(), bacnetObject.getObjectIdentifier());
                        objectNamesToDescription.put(bacnetObject.getObjectName(), bacnetObject.getDescription());
                        obejctNamesToBACnetDevice.put(bacnetObject.getObjectName(), bacnetDevice);
                        getAllStructureElements(bacnetObject.getObjectIdentifier(), bacnetObject.getObjectName(), bacnetObject.getDescription());
                    }
                }
        }
        if(structureElements.isEmpty()){
            LOG.info("No structure-view elements found -> try to build structure with object names");
        }
    }

    /**
     * Add all structured view objects in list with their description
     */
    private void getAllStructureElements(ObjectIdentifier oid, String objectName, String description) {
        if (oid.getObjectType().equals(ObjectType.structuredView)) {
            String[] splitted = objectName.split(structureSeparator);
            String name = splitted[splitted.length - 1];
            structureElements.put(name, description);
        }
    }

    /**
     * Checks if the given object identifier is necessary for building the structure
     * @return true or false
     */
    private Boolean checkIfNecessaryForStructure(ObjectType objectType) {
        if (objectType.equals(ObjectType.file)){
            return false;
        } else if (objectType.equals(ObjectType.device)){
            return false;
        } else if (objectType.equals(ObjectType.program)){
            return false;
        } else return !Character.isDigit(objectType.toString().charAt(0));
    }

    public void deleteStructure() {
        bacnetStructure = null;
        deviceStructure = null;
        objectNamesToOids.clear();
        obejctNamesToBACnetDevice.clear();
        objectNamesToDescription.clear();

    }

    // Methods for BACnet structure with structure elements

    /**
     * Create a BACnet Structure with object names
     * This method need support from getParentNode() and createNode()
     *
     * @return BACnetStructure bacnetStructure
     */
    private BACnetNode buildBacNetStructure() {

        BACnetNode bacnetStructure = new BACnetNode(structureTypeBACnet, "Top node bacnet structure", "BACnet structure", new ArrayList<>());
        int nodeCounter = 0;
        for (String objectName : objectNamesToOids.keySet()) {
            String[] splittedObjectName = objectName.split(structureSeparator);
            for (int i = 0; i < splittedObjectName.length; i++) {
                BACnetNode parent;
                if (bacnetStructure.getChildren().isEmpty()) {
                    parent = bacnetStructure;
                }else {
                    parent = getParentNode(bacnetStructure,i,splittedObjectName);
                }
                BACnetNode node = createNode((i == (splittedObjectName.length -1)),splittedObjectName[i],
                        objectNamesToOids.get(objectName).getObjectType().toString(), objectNamesToDescription.get(objectName), parent,objectName);
                if (node != null) {
                    parent.addChild(node);
                    nodeCounter++;
                }
            }
        }
        if (bacnetStructure.getChildren().isEmpty()) {
            LOG.warn("No bacNet nodes were created, check settings!");
            return null;
        }
        LOG.info("BacNet structure successfully created with " + nodeCounter + " nodes");
        return bacnetStructure;
    }

    /**
     * This method is used to get the direct parent of a node before the node is added to the Structure
     *
     * @param bacnetStructure    current structure
     * @param counter            counter of the loop of the createStructure() method
     * @param splittedObjectName current splitted object name
     * @return BACnetStructure
     */
    private BACnetNode getParentNode(BACnetNode bacnetStructure, int counter, String[] splittedObjectName) {
        BACnetNode parent = bacnetStructure;
        for (int i = 0; i < counter; i++) {
            parent = parent.getChildBySplittedObjectName(splittedObjectName[i]);
        }
        return parent;
    }

    /**
     * Creates a new node on dependent of its type
     * Check if its is structure node and if node exists else dont create node
     *
     * @param splittedObjectName   current splittedObjectName of object splittedObjectName
     * @param type   current object identifier
     * @param parent parent node of current object
     * @return new BACnetStructure<>
     */
    private BACnetNode createNode(boolean lastInLoop,String splittedObjectName, String type, String description, BACnetNode parent,String objectName) {
        if (structureElements.containsKey(splittedObjectName) && (parent.checkIfChildNodeAlreadyExists(splittedObjectName))) {
            return new BACnetNode(splittedObjectName, structureElement, structureElements.get(splittedObjectName), new ArrayList<>());
        } else if (parent.checkIfChildNodeAlreadyExists(splittedObjectName) && (!type.equals(structureElement) && lastInLoop)) {
            return new BACnetNode(objectName, type, description, splittedObjectName);
        } else if(parent.checkIfChildNodeAlreadyExists(splittedObjectName)) {
            return new BACnetNode(splittedObjectName, structureElement, "No structure view objects found", new ArrayList<>());
        }
        return null;
    }

    public BACnetNode getBacnetStructure() {
        return bacnetStructure;
    }

    // Methods for device(Logical view) structure

    /**
     * This method creates a structure with all remote devices and their objects
     */
    private BACnetNode buildDeviceStructure() {
        BACnetNode deviceStructure = new BACnetNode(structureTypeLogic, "Top node devices", "Logical view", new ArrayList<>());
        int nodeCounter = 0;
        for (BACnetDevice bacnetDevice : DeviceService.bacnetDevices) {
            BACnetNode device = new BACnetNode(bacnetDevice.getName(), structureElement, "Device", new ArrayList<>());
            deviceStructure.addChild(device);
            for (BACnetObject bacnetObject : bacnetDevice.getBacnetObjects()) {
                if(checkIfNecessaryForStructure(bacnetObject.getObjectType())){
                addObjectToPropertyGroup(bacnetObject, device);
                }
                nodeCounter++;
            }
        }
        if (deviceStructure.getChildren().isEmpty()) {
            LOG.warn("No device nodes were created, check settings!");
            return null;
        }
        LOG.info("Device structure successfully created with " + nodeCounter + " nodes");
        return deviceStructure;
    }

    /**
     * adds a object to the right group. If no group exits, one will be created
     *
     * @param remoteObject the object to add to the property group
     * @param device       device of the object
     */
    private void addObjectToPropertyGroup(RemoteObject remoteObject, BACnetNode device) {
        ObjectType pid = remoteObject.getObjectIdentifier().getObjectType();
        BACnetNode object = new BACnetNode(remoteObject.getObjectName(),remoteObject.getObjectIdentifier().toString(),
                objectNamesToDescription.get(remoteObject.getObjectName()), "Object");
        BACnetNode propertyGroup = new BACnetNode(pid.toString(), structureElement, pid.toString(), new ArrayList<>());
        if (device.getChildBySplittedObjectName(propertyGroup.getName()) == null) {
            device.addChild(propertyGroup);
            propertyGroup.addChild(object);
        } else {
            device.getChildBySplittedObjectName(propertyGroup.getName()).addChild(object);
        }

    }

    public BACnetNode getDeviceStructure() {
        return deviceStructure;
    }
}

