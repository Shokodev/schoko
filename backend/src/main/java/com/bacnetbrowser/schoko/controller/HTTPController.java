package com.bacnetbrowser.schoko.controller;

import com.bacnetbrowser.schoko.model.datahandler.*;
import com.bacnetbrowser.schoko.model.models.BACnetNode;
import com.bacnetbrowser.schoko.model.models.BACnetStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;



/**
 * Controller for REST server, communication between server and client
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@CrossOrigin
@RestController
@Component
@Configuration
public class HTTPController {

    private HierarchyHandler hierarchyHandler;
    private SettingsHandler settingsHandler;
    private DeviceHandler deviceHandler;


    @Autowired
    public HTTPController(HierarchyHandler hierarchyHandler, SettingsHandler settingsHandler, DeviceHandler deviceHandler) {
        this.hierarchyHandler = hierarchyHandler;
        this.settingsHandler = settingsHandler;
        this.deviceHandler = deviceHandler;
    }
    /**
     * @return complete hierarchy structure
     */
    @GetMapping("/hierarchy")
    public BACnetStructure all (){
        return hierarchyHandler.getStructure();
    }

    /**
     * update Settings in settingsHandler and the settings store
     * @param settings from Client
     * @return new settings
     */
    @PostMapping(value = "/settings")
    public ResponseEntity<SettingsHandler> updateSettings(@RequestBody SettingsHandler settings) {
        settingsHandler.setSiteName(settings.getSiteName());
        settingsHandler.setPort(settings.getPort());
        settingsHandler.setBacnetSeparator(settings.getBacnetSeparator());
        settingsHandler.setSiteDescription(settings.getSiteDescription());
        deviceHandler.createLocalDevice(Integer.parseInt(settingsHandler.getPort(), 16));
        System.out.println("Build structure with new settings.....");
        hierarchyHandler.createStructure(settingsHandler.getSiteName(),settingsHandler.getSiteDescription(),settingsHandler.getBacnetSeparator());
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
     *
     * @param elementName is equals to object name like "Site01'B'A'Ahu01"
     * @return hierarchy structure node with his children
     */
    @GetMapping("/structure/{elementName}")
    public BACnetNode getBacnetStructure(@PathVariable String elementName){
        System.out.println("Get node: " + elementName);
        return hierarchyHandler.getChildrenByNodeElementName(elementName);
        }

    /**
     * Exception if wrong URL tipped
     */
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Node not found") //404
    private static class NodeNotFoundException extends RuntimeException {
            }
}