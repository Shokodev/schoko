package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.models.BACnetNode;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;


/**
 * This class used to generate a BACnet structure by the BACnet "Structure View" objects
 * Communication to Network in here
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class HierarchyService {
    //This lists have main information about structure an objects
    static HashMap<String, ObjectIdentifier> objectNamesToOids = new HashMap<>();
    static HashMap<String, String> objectNamesToDescription = new HashMap<>();
    static HashMap<String, RemoteDevice> obejctNamesToRemoteDevice = new HashMap<>();

    private BACnetNode bacnetStructure;
    private BACnetNode deviceStructure;
    private HashMap<String, String> structureElements = new HashMap<>();
    private String siteName;
    private String siteDescription;
    private String structureSeparator;


    /**
     * Initialize the needed properties
     *
     * @param siteName           Name of the Site or Building
     * @param siteDescription    Description of site
     * @param structureSeparator BacNet separator in ObjectName
     */
    public void create(String siteName, String siteDescription, String structureSeparator) {
        this.siteName = siteName;
        this.siteDescription = siteDescription;
        this.structureSeparator = structureSeparator;
        createStaticRemoteObjectLists();
        this.bacnetStructure = buildBacNetStructure();
        this.deviceStructure = buildDeviceStructure();

    }

    /**
     * Reads all BACnet Objects of all remote devises
     */
    private void createStaticRemoteObjectLists() {
        for (RemoteDevice remoteDevice : DeviceService.localDevice.getRemoteDevices()) {
            try {
                for (RemoteObject remoteObject : remoteDevice.getObjects()) {
                    if (checkIfNecessaryForStructure(remoteObject.getObjectIdentifier())) {
                        String tempDescription = ((ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, new ReadPropertyRequest(remoteObject.getObjectIdentifier(), PropertyIdentifier.description))).getValue().toString();
                        objectNamesToOids.put(remoteObject.getObjectName(), remoteObject.getObjectIdentifier());
                        objectNamesToDescription.put(remoteObject.getObjectName(), tempDescription);
                        obejctNamesToRemoteDevice.put(remoteObject.getObjectName(), remoteDevice);
                        getAllStructureElements(remoteObject.getObjectIdentifier(), remoteObject.getObjectName(), tempDescription);
                    }
                }
            } catch (BACnetException | NullPointerException e) {
                System.out.println("Failed to read objects");
            }
        }
    }

    public void deleteStructure() {
        bacnetStructure = null;
        deviceStructure = null;
        objectNamesToOids.clear();
        obejctNamesToRemoteDevice.clear();
        objectNamesToDescription.clear();

    }


    // Methods for BACnet structure

    /**
     * Create a BACnet Structure with object names
     * This method need support from getParentNode() and createNode()
     *
     * @return BACnetStructure bacnetStructure
     */
    private BACnetNode buildBacNetStructure() {

        BACnetNode bacnetStructure = new BACnetNode(siteName, "Top node bacnet structure", siteDescription, null);
        int nodeCounter = 0;
        for (String key : objectNamesToOids.keySet()) {
            String[] splittedObjectName = key.split(structureSeparator);
            for (int i = 0; i < splittedObjectName.length; i++) {
                BACnetNode parent = getParentNode(bacnetStructure, i, splittedObjectName);
                BACnetNode node = createNode(splittedObjectName[i], objectNamesToOids.get(key).getObjectType().toString(), objectNamesToDescription.get(key), obejctNamesToRemoteDevice.get(key).getInstanceNumber(), parent);
                if (node != null) {
                    parent.addChild(node);
                    nodeCounter++;
                }
            }
        }
        if (bacnetStructure.getChildren().isEmpty()) {
            System.err.println("No bacNet nodes were created, check settings!");
            return null;
        }
        System.out.println("BacNet structure successfully created with " + nodeCounter + " nodes");
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
            parent = parent.getChildByObjectName(splittedObjectName[i]);
        }
        if (parent == null) {
            return bacnetStructure;
        }
        return parent;
    }

    /**
     * Creates a new node on dependent of its type
     * Check if its is structure node and if node exists else dont create node
     *
     * @param name   current name of object name
     * @param type   current object identifier
     * @param parent parent node of current object
     * @return new BACnetStructure<>
     */
    private BACnetNode createNode(String name, String type, String description, Integer deviceID, BACnetNode parent) {
        if (structureElements.containsKey(name) && (parent.checkIfNodeExists(parent, name))) {
            return new BACnetNode(name, "Structure Element", structureElements.get(name), null);
        } else if (parent.checkIfNodeExists(parent, name) && (!type.startsWith("Structure"))) {
            return new BACnetNode(name, type, description, deviceID);
        }
        return null;
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
     *
     * @param oid object identifier
     * @return true or false
     */
    private Boolean checkIfNecessaryForStructure(ObjectIdentifier oid) {
        ObjectType type = oid.getObjectType();
        return !type.equals(ObjectType.file) && !type.equals(ObjectType.device) && !type.equals(ObjectType.program) && !oid.toString().startsWith("Vendor");
    }

    public BACnetNode getBacnetStructure() {
        return bacnetStructure;
    }


    // Methods for device structure

    /**
     * This method creates a structure with all remote devices and their objects
     */
    private BACnetNode buildDeviceStructure() {
        BACnetNode deviceStructure = new BACnetNode("Gerätesicht", "Top node devices", "Alle BACnet Geräte und ihre Objekte", null);
        int nodeCounter = 0;
        for (RemoteDevice remoteDevice : DeviceService.localDevice.getRemoteDevices()) {
            BACnetNode device = new BACnetNode(remoteDevice.getName(), remoteDevice.getObjectIdentifier().toString(), remoteDevice.getVendorName(), remoteDevice.getInstanceNumber());
            deviceStructure.addChild(device);
            for (RemoteObject remoteObject : remoteDevice.getObjects()) {
                addObjectToPropertyGroup(remoteObject, device);
                nodeCounter++;
            }
        }
        if (deviceStructure.getChildren().isEmpty()) {
            System.err.println("No device nodes were created, check settings!");
            return null;
        }
        System.out.println("Device structure successfully created with " + nodeCounter + " nodes");
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
        BACnetNode object = new BACnetNode(remoteObject.getObjectName(), remoteObject.getObjectIdentifier().toString(), objectNamesToDescription.get(remoteObject.getObjectName()), device.getDeviceInstanceNumber());
        BACnetNode propertyGroup = new BACnetNode(pid.toString(), pid.bigIntegerValue().toString(), pid.toString(), device.getDeviceInstanceNumber());
        if (device.getChildByObjectName(propertyGroup.getObjectName()) == null) {
            device.addChild(propertyGroup);
            propertyGroup.addChild(object);
        } else {
            device.getChildByObjectName(propertyGroup.getObjectName()).addChild(object);
        }

    }

    public BACnetNode getDeviceStructure() {
        return deviceStructure;
    }


}

