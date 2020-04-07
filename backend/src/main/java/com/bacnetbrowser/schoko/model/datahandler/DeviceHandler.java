package com.bacnetbrowser.schoko.model.datahandler;



import com.bacnetbrowser.schoko.model.services.DeviceService;
import com.serotonin.bacnet4j.RemoteDevice;
import org.springframework.beans.factory.annotation.Autowired;
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
