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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class used to generate a BACnet structure by the BACnet standard property "Object Name"
 * Communication to BacStack in here
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class HierarchyService {
    private HashMap<String, ObjectIdentifier> oidStringToOid = new HashMap<>();
    private HashMap<String, String> objectNamesToOids = new HashMap<>();
    private HashMap<String, String> objectNamesToDescription = new HashMap<>();
    private HashMap<String, RemoteDevice> obejctNamesToRemoteDevice = new HashMap<>();
    private HashMap<String, String> structureElements;
    private BACnetStructure bacnetStructure;
    private String siteName;
    private String siteDescription;
    private String structureSeparator;

    @Autowired
    DeviceHandler deviceHandler;



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
        this.bacnetStructure = createStructure();


    }

    /**
     *Create a BACnet Structure with object names
     * This method need support from getParentNode() and createNode()
     * @return BACnetStructure bacnetStructure
     */
    private BACnetStructure createStructure() {
        BACnetStructure bacnetStructure = new BACnetStructure(siteName, "Top Element",siteDescription, null);
        Integer nodeCounter = 0;
        for ( String key : objectNamesToOids.keySet() ) {
            String[] splittedObjectName = key.split(structureSeparator);
            for (int i = 0; i < splittedObjectName.length; i++) {
                    BACnetStructure parent = getParentNode(bacnetStructure ,i,splittedObjectName);
                    BACnetStructure node = createNode(splittedObjectName[i], objectNamesToOids.get(key), objectNamesToDescription.get(key), obejctNamesToRemoteDevice.get(key).getInstanceNumber(), parent);
                    if (node != null) {
                        parent.addChild(node);
                        nodeCounter++;
                    }
                }
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
              String[] splitted = obejctName.split(getStructureSeparator());
              String name = splitted[splitted.length -1];
              allStructureElements.put(name,objectNamesToDescription.get(obejctName));
           }}
        return allStructureElements;
    }

   private <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

   public String getStructureSeparator() {
        return structureSeparator;
    }

   public BACnetStructure getBacnetStructure() {
        return bacnetStructure;
    }

   public void deleteStructure(){
        bacnetStructure = null;

    }

    /**
     * Reads all BACnet Objects of all Remote Devises
     * @return HashMap<ObjectNames, ObjectsIdentifier>
     */
    private void readallBACnetObjects(){
        try {

            for (RemoteDevice remoteDevice : deviceHandler.getLocalDevice().getRemoteDevices()) {
                List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>)
                        RequestUtils.sendReadPropertyAllowNull(
                                deviceHandler.getLocalDevice(), remoteDevice, remoteDevice.getObjectIdentifier(),
                                PropertyIdentifier.objectList)).getValues();
                for (ObjectIdentifier oid : oids) {
                    String tempObjectName = ((ReadPropertyAck) deviceHandler.getLocalDevice().send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.objectName))).getValue().toString();
                    String tempDescription = ((ReadPropertyAck) deviceHandler.getLocalDevice().send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.description))).getValue().toString();
                    if (!oid.toString().startsWith("Vendor")){
                        objectNamesToOids.put(tempObjectName,oid.toString());
                        objectNamesToDescription.put(tempObjectName,tempDescription);
                        obejctNamesToRemoteDevice.put(tempObjectName,remoteDevice);
                        oidStringToOid.put(oid.toString(),oid);
                    }}}} catch (BACnetException e) {
            System.out.println("Failed to read objects");

        }
    }

    public HashMap<String, ObjectIdentifier> getOidStringToOid() {
        return oidStringToOid;
    }

    public HashMap<String, String> getObjectNamesToOids() {
        return objectNamesToOids;
    }

    public HashMap<String, String> getObjectNamesToDescription() {
        return objectNamesToDescription;
    }

    public HashMap<String, RemoteDevice> getObejctNamesToRemoteDevice() {
        return obejctNamesToRemoteDevice;
    }

    //TODO get description for Events
    public String getDescription (){
        return "nee";
    }

}

