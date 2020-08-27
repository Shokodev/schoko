package com.bacnetbrowser.schoko.bacnetutils.structure;

import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetObject;
import com.bacnetbrowser.schoko.bacnetutils.services.DeviceService;
import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;
import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class StructureService implements Structurable {

    //This lists have main information about structure an objects
    //TODO make private
    public static HashMap<String, ObjectIdentifier> objectNamesToObjectIdentifier = new HashMap<>();
    public static HashMap<String, String> objectNamesToDescription = new HashMap<>();
    public static HashMap<String, BACnetDevice> objectNamesToBACnetDevice = new HashMap<>();
    public static HashMap<String, String> structureElements = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(StructureService.class);

    private final BACnetStructure bacnetStructure = new BACnetStructure();
    private final LogicalStructure logicalStructure = new LogicalStructure();

    public StructureService() {

        }

    public void collectStructureData(){
        for (BACnetDevice bacnetDevice : DeviceService.bacnetDevices) {
            for (BACnetObject bacnetObject : bacnetDevice.getBacnetObjects()) {
                if (checkIfNecessaryForStructure(bacnetObject.getObjectIdentifier().getObjectType())) {
                    objectNamesToObjectIdentifier.put(bacnetObject.getObjectName(), bacnetObject.getObjectIdentifier());
                    objectNamesToDescription.put(bacnetObject.getObjectName(), bacnetObject.getDescription());
                    objectNamesToBACnetDevice.put(bacnetObject.getObjectName(), bacnetDevice);
                    getAllStructureElements(bacnetObject.getObjectIdentifier(), bacnetObject.getObjectName(), bacnetObject.getDescription());
                }
            }
        }
        if(structureElements.isEmpty()){
            LOG.info("No structure-view elements found -> try to build structure with object names");
        }
        LOG.info("Structure data collected of {} objects", objectNamesToObjectIdentifier.size());
    }

    @Override
    public void build() throws StructureBuildException {
            collectStructureData();
            bacnetStructure.build();
            logicalStructure.build();
    }

    public static Boolean checkIfNecessaryForStructure(ObjectType objectType) {
        if (objectType.equals(ObjectType.file)){
            return false;
        } else if (objectType.equals(ObjectType.program)){
            return false;
        } else return !Character.isDigit(objectType.toString().charAt(0));
    }

    private void getAllStructureElements(ObjectIdentifier oid, String objectName, String description) {
        if (oid.getObjectType().equals(ObjectType.structuredView)) {
            String[] splitted = objectName.split(SettingsHandler.bacnetSeparator);
            String name = splitted[splitted.length - 1];
            structureElements.put(name, description);
        }
    }

    public void deleteLists(){
        objectNamesToObjectIdentifier.clear();
        objectNamesToBACnetDevice.clear();
        objectNamesToDescription.clear();
    }



    public BACnetStructure getBacnetStructure() {
        return bacnetStructure;
    }

    public LogicalStructure getLogicalStructure() {
        return logicalStructure;
    }
}
