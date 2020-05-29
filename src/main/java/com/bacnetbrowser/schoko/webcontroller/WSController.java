package com.bacnetbrowser.schoko.webcontroller;


import com.bacnetbrowser.schoko.datahandler.EventHandler;
import com.bacnetbrowser.schoko.datahandler.ObjectHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    private final ObjectHandler objectHandler;
    private final EventHandler eventHandler;
    private static final Logger LOG = LoggerFactory.getLogger(WSController.class);

    @Autowired
    public WSController(ObjectHandler objectHandler, EventHandler eventHandler) {
        this.objectHandler = objectHandler;
        this.eventHandler = eventHandler;
    }

    /**
     * Gets a List of all properties of a data point
     * @param objectName get object properties by objectName
     */
    @MessageMapping("/{objectName}")
    public void subscribeProperties (@DestinationVariable String objectName) {
        LOG.info("Subscribe object: " + objectName);
        objectHandler.getNewPropertyStream(objectName);
    }

    /**
     * Client informs server to close property stream
     */
    @MessageMapping("/end/{objectName}")
    public void closed (@DestinationVariable String objectName) {
        LOG.info("Disconnect stream for: " + objectName);
        objectHandler.disconnectPropertyStream(objectName);
    }

    /*
     * Client want to write a new value on a specific object
     * @param bacnetProperty is the object the client wants to write, this also have
     *                       the new value as a property
     */
    @MessageMapping("/setValue/{objectName}")
    public void setValue (BacNetProperty bacnetProperty, @DestinationVariable String objectName) {
        objectHandler.setNewValue(bacnetProperty.objectIdentifier,bacnetProperty.value,objectName);
    }

    /**
     * Use to release manual operation
     */
    @MessageMapping("/releaseValue")
    public void releaseValue (String objectName) {
        objectHandler.releaseValue(objectName);
    }

    @GetMapping("/ackAll")
    public void ackEvents(){
        eventHandler.ackAllEvents();
        LOG.info("Ack all events");
    }

    @MessageMapping("/ack")
    public void ackEvent (String objectName) {
        LOG.info("Acknowledge object: " + objectName);
        eventHandler.ackEvent(objectName);
    }

    @MessageMapping("/getEvents")
    public void getEvent () {
        eventHandler.updateStream();
    }

    public static class BacNetProperty{
        String objectIdentifier;
        String value;
    }

}
