package com.bacnetbrowser.schoko.model.datahandler;

import com.bacnetbrowser.schoko.model.models.BACnetProperties;
import com.bacnetbrowser.schoko.model.services.ObjectService;
import com.serotonin.bacnet4j.exception.BACnetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * This class used to get properties from bacnet Datapoints
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class ObjectHandler {

    @Autowired
    ObjectService objectService;

    @Autowired
    SimpMessagingTemplate template;


    public LinkedList<BACnetProperties> getNewPropertyStream(String elementName)  {
        objectService.readDataPointProperties(elementName);
        objectService.subscribeToCovRequest();
        return objectService.getProperties();
    }

    public void disconnectPropertyStream(){
        objectService.unsubscribeToCovRequest();
        objectService.clearPropertyList();
    }

    public void updateStream(){
        template.convertAndSend("/topic/user", objectService.getProperties());
    }


}



