package com.bacnetbrowser.schoko.controller;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.datahandler.HierarchyHandler;
import com.bacnetbrowser.schoko.model.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.model.datahandler.SettingsHandler;
import com.bacnetbrowser.schoko.model.models.BACnetNodes;
import com.bacnetbrowser.schoko.model.models.BACnetProperties;
import com.bacnetbrowser.schoko.model.models.BACnetStructure;
import com.serotonin.bacnet4j.exception.BACnetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
public class Controller {

    private HierarchyHandler hierarchyHandler;
    private ObjectHandler objectHandler;
    private SettingsHandler settingsHandler;
    private DeviceHandler deviceHandler;


    @Autowired
    public Controller(HierarchyHandler hierarchyHandler, ObjectHandler objectHandler, DeviceHandler deviceHandler, SettingsHandler settingsHandler) throws Exception {
        this.hierarchyHandler = hierarchyHandler;
        this.objectHandler = objectHandler;
        this.deviceHandler = deviceHandler;
        this.settingsHandler = settingsHandler;

        //TODO This has to be executed after settings button "create" is press
        deviceHandler.createLocalDevice(settingsHandler.getPort());
        //TODO this has to be deleted as soon as settings ready an no more tests needed
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

    @PostMapping(value = "/settings")
    public ResponseEntity<SettingsHandler> update(@RequestBody SettingsHandler settings)
    {
        settingsHandler.setSiteName(settings.getSiteName());
        settingsHandler.setPort(settings.getPort());
        settingsHandler.setBacnetSeparator(settings.getBacnetSeparator());
        settingsHandler.setSiteDescription(settings.getSiteDescription());
        return new ResponseEntity<SettingsHandler>(settings, HttpStatus.OK);
    }

    /**
     * Site01'B'A'Ahu01'
     *
     * @return hierarchy structure with the children
     */
    @GetMapping("/structure/{elementName}")
    public BACnetNodes getBacnetStructure(@PathVariable String elementName){
        System.out.println("Get node: " + elementName);
        BACnetNodes BACnetNodes = hierarchyHandler.getChildrenByNodeElementName(elementName);
        if (BACnetNodes == null) throw  new NodeNotFoundException();
        return BACnetNodes;
    }

    /**
     * Used to rebase reload data from BacNet Network
     * @param home reload command
     * @return Top element of the new structure
     */
    @GetMapping("/home/{home}")
    public BACnetNodes getHome(@PathVariable String home)  {
        System.out.println("Rebuild structure");
        hierarchyHandler.createStructure(settingsHandler.getSiteName(),settingsHandler.getSiteDescription(),settingsHandler.getBacnetSeparator());
        BACnetNodes BACnetNodes = hierarchyHandler.getChildrenByNodeElementName(home);
        if (BACnetNodes == null) throw  new NodeNotFoundException();
        return BACnetNodes;
    }

    /**
     * Gets a List of all properties of a datapoint

     * @return List with properties values and identifiers
     * @throws BACnetException from Network
     */

    @GetMapping("/Datapoint/{elementName}")
    public LinkedList<BACnetProperties> getProperties(@PathVariable String elementName) throws BACnetException {
        System.out.println("Read: " + elementName);
        return  objectHandler.update(elementName);
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        return "Hello";
    }

    /**
     * Exception if wrong URL tipped
     */
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Node not found") //404
    private
    class NodeNotFoundException extends RuntimeException {
            }
}