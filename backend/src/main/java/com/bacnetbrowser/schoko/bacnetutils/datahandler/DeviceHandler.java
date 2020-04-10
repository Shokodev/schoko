package com.bacnetbrowser.schoko.bacnetutils.datahandler;



import com.bacnetbrowser.schoko.bacnetutils.services.DeviceService;
import org.springframework.stereotype.Component;


/**
 * This class used to generate a bacnet network
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class DeviceHandler {



    /**
     * Start build process
     * @param port BACnet port form settings
     * @param bacNetID BACnet_ID for local device from settings
     */
    public void createNetwork(Integer port, Integer bacNetID){
        DeviceService deviceService = new DeviceService();
        deviceService.createLocalDevice(port,bacNetID);
    }

}
