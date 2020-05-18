package com.bacnetbrowser.schoko.webcontroller;

import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.models.WaitingRoomDeviceFrontend;
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
    private final DeviceService deviceService = new DeviceService();
    static final Logger LOG = LoggerFactory.getLogger(HTTPController.class);


    @Autowired
    public HTTPController(HierarchyHandler hierarchyHandler, SettingsHandler settingsHandler,
                          EventHandler eventHandler) {
        this.hierarchyHandler = hierarchyHandler;
        this.settingsHandler = settingsHandler;
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
    @GetMapping("/logicalview")
    public BACnetNode devcieStructure (){
        return hierarchyHandler.getDeviceStructure();
    }


    @GetMapping("/preload/devices")
    public ArrayList<WaitingRoomDeviceFrontend> getImportedDevices(){
        ArrayList<WaitingRoomDeviceFrontend> list = new ArrayList<>();
        for(BACnetDevice device : DeviceService.bacnetDevices){
            WaitingRoomDeviceFrontend deviceFD = new WaitingRoomDeviceFrontend(
                    true,
                    device.getName(),
                    device.getModelName(),
                    device.getAddress().getMacAddress().getDescription(),
                    device.getInstanceNumber());
            list.add(deviceFD);
        }
        return list;
    }

    @GetMapping("/devices")
    public ArrayList<WaitingRoomDeviceFrontend> getWaitingRoomList(){
        ArrayList<WaitingRoomDeviceFrontend> list = new ArrayList<>();
        deviceService.createLocalDevice();
        for(BACnetDevice device : DeviceService.waitingRoomBacnetDevices.values()){
            WaitingRoomDeviceFrontend deviceFD = new WaitingRoomDeviceFrontend(
                    DeviceService.getBACnetDevice(device.getObjectIdentifier()) != null,
                    device.getName(),
                    device.getModelName(),
                    device.getAddress().getMacAddress().getDescription(),
                    device.getInstanceNumber());
            list.add(deviceFD);
        }
        return list;
    }

    @PostMapping(value = "/devices")
    public ResponseEntity<SettingsHandler> setFinalDevices(@RequestBody ArrayList<WaitingRoomDeviceFrontend> bacnetDevices){
        LOG.info("Received desired list from frontend with: {} devices",bacnetDevices.size());
        DeviceService.bacnetDevices.clear();
        bacnetDevices.forEach(device -> {
                DeviceService.bacnetDevices.add(DeviceService.waitingRoomBacnetDevices.get(device.getInstanceNumber));
        });
        LOG.info("{} BACnet devices finally registered at local device", DeviceService.bacnetDevices.size());
        deviceService.readFinalAddedDevices();
        eventHandler.createEventStream();
        LOG.info("BACnet devices ready");
        hierarchyHandler.createStructure();
        LOG.info("Create or refresh hierarchy, notify frontend with: {}", HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * update Settings in settingsHandler and the settings store
     * @return OK if success
     */
    @PostMapping(value = "/settings")
    public ResponseEntity<SettingsHandler> updateSettingsAndBuildProcess(@RequestBody SettingsHandler settings) {
        settingsHandler.setPort(settings.getPort());
        settingsHandler.setBacnetSeparator(settings.getBacnetSeparator());
        settingsHandler.setLocalDeviceID(settings.getLocalDeviceID());
        settingsHandler.setPrecisionRealValue(settings.getPrecisionRealValue());
        settingsHandler.setScanSeconds(settings.getScanSeconds());
        LOG.info("New settings saved, notify frontend with: {}", HttpStatus.OK);
        return new ResponseEntity<>(settings, HttpStatus.OK);
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
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "404 The resource you seeking for does not exist") //404
    private static class FoundException extends RuntimeException {
            }


}
