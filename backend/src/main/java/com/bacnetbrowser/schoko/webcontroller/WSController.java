package com.bacnetbrowser.schoko.webcontroller;


import com.bacnetbrowser.schoko.bacnetutils.datahandler.EventHandler;
import com.bacnetbrowser.schoko.bacnetutils.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
    @MessageMapping("/objectSub")
    @SendTo("/broker/objectSub")
    public void subscribeProperties (String objectName) {
        LOG.info("Read object: " + objectName);
        objectHandler.getNewPropertyStream(objectName);
    }

    /**
     * Client informs server to close property stream
     * @param closed message for logging
     */
    @MessageMapping("/end")
    public void closed (String closed) {
        LOG.info("Message from Client: " + closed);
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

}
