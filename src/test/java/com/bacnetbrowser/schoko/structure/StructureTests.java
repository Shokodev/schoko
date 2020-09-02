package com.bacnetbrowser.schoko.structure;

import com.bacnetbrowser.schoko.bacnetutils.structure.StructureService;
import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;
import com.bacnetbrowser.schoko.bacnetutils.structure.pattern.BACnetStructure;
import com.bacnetbrowser.schoko.bacnetutils.structure.pattern.StructureObjectNode;
import com.bacnetbrowser.schoko.bacnetutils.structure.pattern.StructureViewNode;
import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StructureTests {
    private static final Logger LOG = LoggerFactory.getLogger(StructureTests.class);
    private StructureService structureService = new StructureService();

    @Test
    void BACnetStructureTest(){

        BACnetStructure expectedStructure = createDataForTestStructure();
        BACnetStructure structure = new BACnetStructure();
        try {
            structure.build();
        } catch (StructureBuildException e){
            LOG.error(e.getMessage());
        }
        assertEquals(expectedStructure,structure);
    }

    private BACnetStructure createDataForTestStructure(){
        BACnetStructure expectedStructure = new BACnetStructure();
        StructureService.structureElements.put("B","Gebäude");
        StructureService.structureElements.put("A","Lüftung");
        StructureService.structureElements.put("Ahu","Lüftungsanlage");
        StructureService.structureElements.put("FanEh","Abluftventilator");
        StructureService.objectNamesToObjectIdentifier.put("B'A'Ahu'FanEh'Cmd", new ObjectIdentifier(ObjectType.binaryValue,1));
        StructureService.objectNamesToObjectIdentifier.put("B'A'Ahu'FanEh'Alm", new ObjectIdentifier(ObjectType.binaryValue,2));
        StructureService.objectNamesToDescription.put("B'A'Ahu'FanEh'Cmd","Befehl");
        StructureService.objectNamesToDescription.put("B'A'Ahu'FanEh'Alm","Alarm");
        SettingsHandler.bacnetSeparator = "'";
        try{
        expectedStructure.getChildren().add(new StructureViewNode("B","Gebäude"));
        expectedStructure.getChild("B").getChildren().add(new StructureViewNode("A","Lüftung"));
        expectedStructure.getChild("B").getChild("A").getChildren().add(new StructureViewNode("Ahu","Lüftungsanlage"));
        expectedStructure.getChild("B").getChild("A").getChild("Ahu").getChildren().add(new StructureViewNode("FanEh","Abluftventilator"));
        expectedStructure.getChild("B").getChild("A").getChild("Ahu").getChild("FanEh").getChildren().add(new StructureObjectNode("Cmd","Befehl","binary-value","B'A'Ahu'FanEh'Cmd"));
        expectedStructure.getChild("B").getChild("A").getChild("Ahu").getChild("FanEh").getChildren().add(new StructureObjectNode("Alm","Alarm","binary-value","B'A'Ahu'FanEh'Alm"));
    } catch (StructureBuildException e){
        LOG.error("Test failed by creating test data: {}", e.getMessage());
        }
        return expectedStructure;
    }
}
