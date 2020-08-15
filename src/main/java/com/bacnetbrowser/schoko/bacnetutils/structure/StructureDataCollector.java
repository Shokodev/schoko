package com.bacnetbrowser.schoko.bacnetutils.structure;

import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetObject;
import com.bacnetbrowser.schoko.bacnetutils.services.DeviceService;
import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class StructureDataCollector {

    //This lists have main information about structure an objects
    static HashMap<String, ObjectIdentifier> objectNamesToObjectIdentifier = new HashMap<>();
    static HashMap<String, String> objectNamesToDescription = new HashMap<>();
    static HashMap<String, BACnetDevice> objectNamesToBACnetDevice = new HashMap<>();
    static HashMap<String, String> structureElements = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(StructureDataCollector.class);

    public StructureDataCollector() {
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
        }

    private Boolean checkIfNecessaryForStructure(ObjectType objectType) {
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



}
