package com.bacnetbrowser.schoko.controller;

import com.bacnetbrowser.schoko.model.datahandler.*;
import com.bacnetbrowser.schoko.model.models.BACnetNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private EventHandler eventHandler;
    static final Logger LOG = LoggerFactory.getLogger(HTTPController.class);


    @Autowired
    public HTTPController(HierarchyHandler hierarchyHandler, SettingsHandler settingsHandler, DeviceHandler deviceHandler, EventHandler eventHandler) {
        this.hierarchyHandler = hierarchyHandler;
        this.settingsHandler = settingsHandler;
        this.deviceHandler = deviceHandler;
        this.eventHandler = eventHandler;
    }

    /**
     * @return complete hierarchy structure
     */
    @GetMapping("/hierarchy")
    public BACnetNode all (){
        return hierarchyHandler.getBacnetStructure();
    }

    /**
     * @return complete device structure
     */
    @GetMapping("/devices")
    public BACnetNode devcieStructure (){
        return hierarchyHandler.getDeviceStructure();
    }

    /**
     * update Settings in settingsHandler and the settings store
     * @param settings from Client
     * @return new settings
     */
    @PostMapping(value = "/settings")
    public ResponseEntity<SettingsHandler> updateSettingsAndBuildProcess(@RequestBody SettingsHandler settings) {
        settingsHandler.setSiteName(settings.getSiteName());
        settingsHandler.setPort(settings.getPort());
        settingsHandler.setBacnetSeparator(settings.getBacnetSeparator());
        settingsHandler.setSiteDescription(settings.getSiteDescription());
        settingsHandler.setLocalDeviceID(settings.getLocalDeviceID());
        deviceHandler.createNetwork(Integer.parseInt(settingsHandler.getPort(), 16),Integer.parseInt(settingsHandler.getLocalDeviceID()));
        LOG.info("Build structure with new settings.....");
        hierarchyHandler.createStructure(settingsHandler.getSiteName(),settingsHandler.getSiteDescription(),settingsHandler.getBacnetSeparator());
        eventHandler.createEventStream();
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
     * @param objectName is equals to object name like "Site01'B'A'Ahu01"
     * @return hierarchy structure node with his children
     */
    @GetMapping("/structure/{objectName}")
    public BACnetNode getBacnetStructure(@PathVariable String objectName){
        LOG.info("Get node: " + objectName);
        return hierarchyHandler.getNodeByObjectName(objectName);
        }

    /**
     * Exception if wrong URL tipped
     */
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Node not found") //404
    private static class NodeNotFoundException extends RuntimeException {
            }
}
