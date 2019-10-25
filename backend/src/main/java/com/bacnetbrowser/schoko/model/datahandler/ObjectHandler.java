package com.bacnetbrowser.schoko.model.datahandler;


import com.bacnetbrowser.schoko.model.models.BACnetTypes;
import com.bacnetbrowser.schoko.model.services.ObjectService;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


/**
 * This class used to handle the object functions for the object properties between stack and REST
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class ObjectHandler {


   private SimpMessagingTemplate template;
   private ObjectService objectService;

   @Autowired
    public ObjectHandler(SimpMessagingTemplate template, ObjectService objectService) {
        this.template = template;
        this.objectService = objectService;
    }

    /**
     * Is used to start a websocket stream for data point properties by their object names
     * it also triggers the subscription for this object on the remote device
     * @param elementName object name
     */
    public void getNewPropertyStream(String elementName)  {
        objectService.readDataPointProperties(elementName);
        objectService.subscribeToCovRequest();
    }

    /**
     * after closing the websocket, the subscription have to end as well and the propertyList have to be empty for next websocket stream
     */
    public void disconnectPropertyStream(){
        objectService.unsubscribeToCovRequest();
        objectService.clearPropertyList();
    }

    /**
     * This method does update the properties by changes sent from the remote device
     */
    public void  updateStream(){
        template.convertAndSend("/topic/user", objectService.getProperties());
    }

    /**
     * This method is use to write the command request of the client
     * @param propertyIdentifier witch property
     * @param newValue new value for property
     */
    public void setNewValue(String propertyIdentifier, String newValue){
        BACnetTypes baCnetTypes = new  BACnetTypes();
        for (PropertyIdentifier oid : PropertyIdentifier.ALL)
            if (oid.toString().equals(propertyIdentifier)){
                objectService.writeValue(oid, baCnetTypes.getPropertyValuesByObjectType(objectService.getObjectIdentifier().getObjectType(), newValue));
        }}

    }










