package com.bacnetbrowser.schoko.webcontroller;

import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.services.DeviceService;
import com.bacnetbrowser.schoko.datahandler.EventHandler;
import com.bacnetbrowser.schoko.datahandler.HierarchyHandler;
import com.bacnetbrowser.schoko.datahandler.SettingsHandler;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


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

    private final HierarchyHandler hierarchyHandler;
    private final SettingsHandler settingsHandler;
    private final EventHandler eventHandler;
    private final DeviceService deviceService;
    static final Logger LOG = LoggerFactory.getLogger(HTTPController.class);


    @Autowired
    public HTTPController(HierarchyHandler hierarchyHandler, SettingsHandler settingsHandler, EventHandler eventHandler) {
        this.hierarchyHandler = hierarchyHandler;
        this.settingsHandler = settingsHandler;
        this.eventHandler = eventHandler;
        this.deviceService = new DeviceService();
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
    @GetMapping("/logicalview")
    public BACnetNode devcieStructure (){
        return hierarchyHandler.getDeviceStructure();
    }

    @GetMapping("/devices")
    public ArrayList<BACnetDevice> getWaitingRoomList(){
        deviceService.createLocalDevice();
        return deviceService.getWaitingRoomBacnetDevices();
    }

    @PostMapping("/devices")
    public ResponseEntity<SettingsHandler> setFinalDevices(@RequestBody ArrayList<BACnetDevice> baCnetDevices){
        DeviceService.getBacnetDevices().addAll(baCnetDevices);
        LOG.info("{} BACnet devices finally registered at local device", DeviceService.getBacnetDevices().size());
        deviceService.readFinalAddedDevices();
        LOG.info("Build structure with new settings.....");
        hierarchyHandler.createStructure(settingsHandler.getSiteName(),settingsHandler.getSiteDescription(),settingsHandler.getBacnetSeparator());
        eventHandler.createEventStream();
        return new ResponseEntity<>(HttpStatus.OK);
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
