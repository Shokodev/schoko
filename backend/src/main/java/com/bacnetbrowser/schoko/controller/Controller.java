package com.bacnetbrowser.schoko.controller;

import com.bacnetbrowser.schoko.model.datahandler.*;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.models.BACnetNode;
import com.bacnetbrowser.schoko.model.models.BACnetProperties;
import com.bacnetbrowser.schoko.model.models.BACnetStructure;
import com.serotonin.bacnet4j.exception.BACnetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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
public class Controller {

    private HierarchyHandler hierarchyHandler;
    private ObjectHandler objectHandler;
    private SettingsHandler settingsHandler;
    private DeviceHandler deviceHandler;
    private EventHandler eventHandler;


    @Autowired
    public Controller(HierarchyHandler hierarchyHandler, ObjectHandler objectHandler, DeviceHandler deviceHandler, SettingsHandler settingsHandler, EventHandler eventHandler) throws Exception {
        this.hierarchyHandler = hierarchyHandler;
        this.objectHandler = objectHandler;
        this.deviceHandler = deviceHandler;
        this.settingsHandler = settingsHandler;
        this.eventHandler = eventHandler;

        //TODO This has to be executed after settings button "create" is press
        settingsHandler.readXMLSettings();
        try {
            deviceHandler.createLocalDevice(Integer.parseInt(settingsHandler.getPort(), 16));
        }
        catch (NumberFormatException err){
            System.err.println("Warning wrong port "+settingsHandler.getPort());
        }
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
     *
     *
     * @return eventlist
     */
    @GetMapping("/eventlist")
    public LinkedList<BACnetEvent> getEvents (){
       return eventHandler.getEvents();
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
     * Gets a List of all properties of a datapoint
     * @param objectName get object properties by objectName
     */
    @MessageMapping("/user")
    @SendTo("/topic/user")
    public void getProperties (String objectName) {
        System.out.println("Read: " + objectName);
        objectHandler.getNewPropertyStream(objectName);
    }

    /**
     * Gets a List of all properties of a datapoint
     * @param closed  get object properties by objectName
     */
    @MessageMapping("/end")
    public void closed (String closed) {
        System.out.println("Message from Client: " + closed);
        objectHandler.disconnectPropertyStream();
    }



    /**
     * Exception if wrong URL tipped
     */
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Node not found") //404
    private class NodeNotFoundException extends RuntimeException {
            }
}