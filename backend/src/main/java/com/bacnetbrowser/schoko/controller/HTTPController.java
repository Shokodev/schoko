package com.bacnetbrowser.schoko.controller;

import com.bacnetbrowser.schoko.model.datahandler.*;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.models.BACnetNode;
import com.bacnetbrowser.schoko.model.models.BACnetProperty;
import com.bacnetbrowser.schoko.model.models.BACnetStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;


/**
 * Controller for REST server
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@CrossOrigin
@RestController
@Component
@Configuration
@EnableScheduling
public class HTTPController {

    private HierarchyHandler hierarchyHandler;
    private SettingsHandler settingsHandler;


    @Autowired
    public HTTPController(HierarchyHandler hierarchyHandler, SettingsHandler settingsHandler, DeviceHandler deviceHandler) {
        this.hierarchyHandler = hierarchyHandler;
        this.settingsHandler = settingsHandler;




        //TODO This has to be executed after settings button "create" is press
        settingsHandler.readXMLSettings();
        try {
            //parse HEX BACx port to Integer
            deviceHandler.createLocalDevice(Integer.parseInt(settingsHandler.getPort(), 16));
        }
        catch (NumberFormatException err){
            System.err.println("Warning wrong port "+settingsHandler.getPort());
        }
        //TODO this has to be moved in (updateSettings) method as soon as settings ready and no more tests needed
        System.out.println("Build structure.......");
        hierarchyHandler.createStructure(settingsHandler.getSiteName(),settingsHandler.getSiteDescription(),settingsHandler.getBacnetSeparator());

    }


    /**
     *
     *
     * @return complete hierarchy structure
     */
    @GetMapping("/hierarchy")
    public BACnetStructure all (){
        return hierarchyHandler.getStructure();
    }

    /**
     * update Settings in settingsHandler and the xml file
     * @param settings from Client
     * @return new settings
     */
    @PostMapping(value = "/settings")
    public ResponseEntity<SettingsHandler> updateSettings(@RequestBody SettingsHandler settings) {
        settingsHandler.setSiteName(settings.getSiteName());
        settingsHandler.setPort(settings.getPort());
        settingsHandler.setBacnetSeparator(settings.getBacnetSeparator());
        settingsHandler.setSiteDescription(settings.getSiteDescription());
        settingsHandler.writeXMLSettings();
        return new ResponseEntity<SettingsHandler>(settings, HttpStatus.OK);
    }

    /**
     * get the current settings of settings handler
     * @return settings
     */
    @GetMapping(value = "/settings")
    public SettingsHandler allSettings ()
    {
        return settingsHandler;
    }

    /**
     * Site01'B'A'Ahu01'
     *
     * @return hierarchy structure with the children
     */
    @GetMapping("/structure/{elementName}")
    public BACnetNode getBacnetStructure(@PathVariable String elementName){
        System.out.println("Get node: " + elementName);
        BACnetNode BACnetNodes = hierarchyHandler.getChildrenByNodeElementName(elementName);
        if (BACnetNodes == null) throw  new NodeNotFoundException();
        return BACnetNodes;
    }

    /**
     * Used to rebase reload data from BacNet Network
     * @param home reload command
     * @return Top element of the new structure
     */
    @GetMapping("/home/{home}")
    public BACnetNode getHome(@PathVariable String home)  {
        System.out.println("Rebuild structure");
        hierarchyHandler.createStructure(settingsHandler.getSiteName(),settingsHandler.getSiteDescription(),settingsHandler.getBacnetSeparator());
        BACnetNode BACnetNodes = hierarchyHandler.getChildrenByNodeElementName(home);
        if (BACnetNodes == null) throw  new NodeNotFoundException();
        return BACnetNodes;
    }


    /**
     * Exception if wrong URL tipped
     */
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Node not found") //404
    private class NodeNotFoundException extends RuntimeException {
            }
}