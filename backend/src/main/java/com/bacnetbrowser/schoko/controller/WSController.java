package com.bacnetbrowser.schoko.controller;


import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.model.models.BACnetNode;
import com.bacnetbrowser.schoko.model.models.BACnetProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for WebSockets
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@CrossOrigin
@RestController
@Component
@Configuration
public class WSController {

    private ObjectHandler objectHandler;
    private EventHandler eventHandler;

    @Autowired
    public WSController(ObjectHandler objectHandler, EventHandler eventHandler) {
        this.objectHandler = objectHandler;
        this.eventHandler = eventHandler;
    }

    /**
     * Gets a List of all properties of a data point
     * @param objectName get object properties by objectName
     */
    @MessageMapping("/objectSub")
    @SendTo("/broker/objectSub")
    public void subscribeProperties (String objectName) {
        System.out.println("Read object: " + objectName);
        objectHandler.getNewPropertyStream(objectName);
    }

    /**
     * Client informs server to close property stream
     * @param closed message for logging
     */
    @MessageMapping("/end")
    public void closed (String closed) {
        System.out.println("Message from Client: " + closed);
        objectHandler.disconnectPropertyStream();
    }

    /**
     * Client want to write a new value on a specific object
     * @param bacnetProperty is the object the client wants to write, this also have
     *                       the new value as a property
     */
    @MessageMapping("/setValue")
    public void setValue (BACnetProperty bacnetProperty) {
        objectHandler.setNewValue(bacnetProperty.getPropertyIdentifier(),bacnetProperty.getValue());

    }

    /**
     * Use to release manual operation
     */
    @MessageMapping("/releaseValue")
    public void releaseValue (String releaseMessage) {
        objectHandler.releaseValue();
    }

    /**
     * Used to get the existing events in the BACnet network by opening of the websocket
     */
    @MessageMapping("/eventSub")
    @SendTo("/broker/eventSub")
    public void subscribeEvents (String message) {
        System.out.println(message +" event channel");

    }

}
