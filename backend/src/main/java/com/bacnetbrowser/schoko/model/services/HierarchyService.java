package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.models.BACnetStructure;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class used to generate a BACnet structure by the BACnet structure view objects
 * Communication to Network in here
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class HierarchyService {
    // This lists have main information about structure an objects
    static HashMap<String, ObjectIdentifier> oidStringToOid = new HashMap<>();
    static   HashMap<String, String> objectNamesToOids = new HashMap<>();
    static HashMap<String, String> objectNamesToDescription = new HashMap<>();
    static HashMap<String, RemoteDevice> obejctNamesToRemoteDevice = new HashMap<>();
    private HashMap<String, String> structureElements;
    private BACnetStructure bacnetStructure;
    private String siteName;
    private String siteDescription;
    private String structureSeparator;


    /**
     * Initialize the needed properties
     * @param siteName Name of the Site or Building
     * @param siteDescription Description of site
     * @param structureSeparator BacNet separator in ObjectName
     */
    public void create (String siteName, String siteDescription, String structureSeparator){
        this.siteName = siteName;
        this.siteDescription = siteDescription;
        this.structureSeparator = structureSeparator;
        readallBACnetObjects();
        this.structureElements = getAllStructureElements();
        this.bacnetStructure = buildStructure();


    }

    /**
     *Create a BACnet Structure with object names
     * This method need support from getParentNode() and createNode()
     * @return BACnetStructure bacnetStructure
     */
    private BACnetStructure buildStructure() {

        BACnetStructure bacnetStructure = new BACnetStructure(siteName, "Top Element",siteDescription, null);
        int nodeCounter = 0;
        for ( String key : objectNamesToOids.keySet() ) {
            String[] splittedObjectName = key.split(structureSeparator);
            for (int i = 0; i < splittedObjectName.length; i++) {
                    BACnetStructure parent = getParentNode(bacnetStructure ,i,splittedObjectName);
                    BACnetStructure node = createNode(splittedObjectName[i], oidStringToOid.get(objectNamesToOids.get(key)).getObjectType().toString(), objectNamesToDescription.get(key), obejctNamesToRemoteDevice.get(key).getInstanceNumber(), parent);
                    if (node != null) {
                        parent.addChild(node);
                        nodeCounter++;
                    }
                }
            }
        if (bacnetStructure.getChildren().isEmpty()){
            System.err.println("No Nodes were created, check settings!");
            return null;
        }
        System.out.println("Structure successfully created with " + nodeCounter + " nodes");
        return bacnetStructure;
    }

    /**
     * This method is used to get the direct parent of a node before the node is added to the Structure
     * 
     * @param bacnetStructure current structure
     * @param counter counter of the loop of the createStructure() method
     * @param splittedObjectName current splitted object name
     * @return BACnetStructure
     */
    private BACnetStructure getParentNode(BACnetStructure bacnetStructure, int counter, String[] splittedObjectName){
        BACnetStructure parent = bacnetStructure;
        for (int i = 0;i < counter; i++) {
           parent = parent.getChildByElementName(splittedObjectName[i]);
        }
        if (parent == null){
            return bacnetStructure;
        }
        return parent;
    }

    /**
     * Creates a new node on dependent of its type
     *  Check if its is structure node and if node exists else dont create node
     * @param name current name of object name
     * @param type current object identifier
     * @param parent parent node of current object
     * @return new BACnetStructure<>
     */
    private BACnetStructure createNode(String name, String type, String description, Integer deviceID, BACnetStructure parent){
         if (structureElements.containsKey(name) && (parent.checkIfNodeExists(parent, name))) {
            return new BACnetStructure (name, "Structure Element",structureElements.get(name),null);
        }
        else if (parent.checkIfNodeExists(parent, name) && (!type.startsWith("Structure"))) {
            return new BACnetStructure (name, type,description,deviceID);
        }
        return null;
    }

    /**
     *
     * @return all structure elements and their description
     */
    private HashMap<String, String> getAllStructureElements () {
        HashMap<String, String> allStructureElements = new HashMap<>();
        for (ObjectIdentifier oid : oidStringToOid.values()){
           if (oid.getObjectType().equals(ObjectType.structuredView)) {
              String obejctName = getKey(objectNamesToOids,oid.toString());
              String[] splitted = obejctName.split(structureSeparator);
              String name = splitted[splitted.length -1];
              allStructureElements.put(name,objectNamesToDescription.get(obejctName));
           }}
        return allStructureElements;
    }

    /**
     * General method to read a key of a hash map
     * this method is copied from the internet
     * @param map witch map you want to read the key
     * @param value value witch you need the key
     * @return key of hash map
     */
    static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public BACnetStructure getBacnetStructure() {
        return bacnetStructure;
    }

    public void deleteStructure(){
        bacnetStructure = null;
        oidStringToOid.clear();
        objectNamesToOids.clear();
        obejctNamesToRemoteDevice.clear();
        objectNamesToDescription.clear();

    }

    /**
     * Reads all BACnet Objects of all Remote Devises
     */
    private void readallBACnetObjects(){
        try {

            for (RemoteDevice remoteDevice : DeviceHandler.localDevice.getRemoteDevices()) {
                List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>)
                        RequestUtils.sendReadPropertyAllowNull(
                                DeviceHandler.localDevice, remoteDevice, remoteDevice.getObjectIdentifier(),
                                PropertyIdentifier.objectList)).getValues();
                for (ObjectIdentifier oid : oids) {
                    if (checkIfNecessaryForStructure(oid)) {
                    String tempObjectName = ((ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.objectName))).getValue().toString();
                    String tempDescription = ((ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.description))).getValue().toString();
                        objectNamesToOids.put(tempObjectName,oid.toString());
                        objectNamesToDescription.put(tempObjectName,tempDescription);
                        obejctNamesToRemoteDevice.put(tempObjectName,remoteDevice);
                        oidStringToOid.put(oid.toString(),oid);
                    }}}} catch (BACnetException e) {
            System.out.println("Failed to read objects");

        }
    }

    /**
     * checks if the given object identifier is necessary for building the structure
     * @param oid object identifier
     * @return true or false
     */
    private Boolean checkIfNecessaryForStructure(ObjectIdentifier oid){
        ObjectType type = oid.getObjectType();
        return !type.equals(ObjectType.file) && !type.equals(ObjectType.device) && !type.equals(ObjectType.program) && !oid.toString().startsWith("Vendor");
    }


}

