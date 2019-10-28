package com.bacnetbrowser.schoko.controller;


import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.models.BACnetProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
/**
 * Controller for WebSockets
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@CrossOrigin
@RestController
@Component
@Configuration
@EnableScheduling
public class WSController {

    private ObjectHandler objectHandler;
    private EventHandler eventHandler;

    @Autowired
    public WSController(ObjectHandler objectHandler, EventHandler eventHandler) {
        this.objectHandler = objectHandler;
        this.eventHandler = eventHandler;
    }

    /**
     * Gets a List of all properties of a datapoint
     * @param objectName get object properties by objectName
     */
    @MessageMapping("/objectSub")
    @SendTo("/broker/objectSub")
    public void subscribeProperties (String objectName) {
        System.out.println("Read object: " + objectName);
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

    @MessageMapping("/setValue")
    public void setValue (BACnetProperty baCnetProperty) {
        objectHandler.setNewValue(baCnetProperty.getPropertyIdentifier(),baCnetProperty.getValue());
    }

    /**
     * Gets a List of all properties of a datapoint
     */
    @MessageMapping("/eventSub")
    @SendTo("/broker/eventSub")
    public void subscribeEvents (String message) {
        System.out.println(message +" events");
       eventHandler.lookForExistingEvents();
    }


}
