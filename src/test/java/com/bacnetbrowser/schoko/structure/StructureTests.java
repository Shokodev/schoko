package com.bacnetbrowser.schoko.structure;

import com.bacnetbrowser.schoko.bacnetutils.devices.DeviceService;
import com.bacnetbrowser.schoko.bacnetutils.structure.StructureService;
import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;
import com.bacnetbrowser.schoko.bacnetutils.structure.pattern.BACnetStructure;
import com.bacnetbrowser.schoko.bacnetutils.structure.pattern.LogicalStructure;
import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.fail;


public class StructureTests {
    private StructureService structureService = new StructureService();

    @Test
    void BACnetStructureTest(){
        StructureService.structureElements.put("B","Gebäude");
        StructureService.structureElements.put("A","Lüftung");
        StructureService.structureElements.put("Ahu","Lüftungsanlage");
        StructureService.structureElements.put("FanEh","Abluftventilator");
        StructureService.objectNamesToObjectIdentifier.put("B'A'Ahu'FanEh'Cmd", new ObjectIdentifier(ObjectType.binaryValue,1));
        StructureService.objectNamesToObjectIdentifier.put("B'A'Ahu'FanEh'Alm", new ObjectIdentifier(ObjectType.binaryValue,2));
        StructureService.objectNamesToDescription.put("B'A'Ahu'FanEh'Cmd","Befehl");
        StructureService.objectNamesToDescription.put("B'A'Ahu'FanEh'Alm","Alarm");
        SettingsHandler.bacnetSeparator = "'";
        BACnetStructure structure = new BACnetStructure();
        try {
            structure.build();
        } catch (StructureBuildException e){
            fail("Test Faield:" + e.getMessage());
        }
    }

    @Test
    void LogicStructureTest(){
        StructureService.structureElements.put("B","Gebäude");
        StructureService.structureElements.put("A","Lüftung");
        StructureService.structureElements.put("Ahu","Lüftungsanlage");
        StructureService.structureElements.put("FanEh","Abluftventilator");
        StructureService.objectNamesToObjectIdentifier.put("B'A'Ahu'FanEh'Cmd", new ObjectIdentifier(ObjectType.binaryValue,1));
        StructureService.objectNamesToObjectIdentifier.put("B'A'Ahu'FanEh'Alm", new ObjectIdentifier(ObjectType.binaryValue,2));
        StructureService.objectNamesToDescription.put("B'A'Ahu'FanEh'Cmd","Befehl");
        StructureService.objectNamesToDescription.put("B'A'Ahu'FanEh'Alm","Alarm");
        SettingsHandler.bacnetSeparator = "'";

        LogicalStructure structure = new LogicalStructure();
        try {
            structure.build();
        } catch (StructureBuildException e){
            fail("Test Faield:" + e.getMessage());
        }
    }

}
