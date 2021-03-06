package com.bacnetbrowser.schoko.datahandler;


import com.bacnetbrowser.schoko.bacnetutils.models.BACnetTypes;
import com.bacnetbrowser.schoko.bacnetutils.services.ObjectService;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


/**
 * This class used to handle the BACnet object properties between BACnet and REST
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class ObjectHandler {


   private final SimpMessagingTemplate template;
   private final HashMap<String,ObjectService> objectService = new HashMap<>();
   private static final Logger LOG = LoggerFactory.getLogger(ObjectHandler.class);

   @Autowired
    public ObjectHandler(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Is used to start a websocket stream for data point properties by their object names
     * it also triggers the subscription for this object on the remote device
     * @param objectName object name
     */
    public void getNewPropertyStream(String objectName)  {
        ObjectService objectService = new ObjectService(this);
        this.objectService.put(objectName, objectService);
        objectService.clearPropertyList();
        objectService.readDataPointProperties(objectName);
        objectService.getBacnetObject().subscribeToCovRequest();
    }

    /**
     * After closing the websocket, the subscription at the remote device have to end as well and the propertyList have to be empty for next websocket stream
     */
    public void disconnectPropertyStream(String objectName){
        objectService.get(objectName).removeFromLocalDevice();
        objectService.get(objectName).getBacnetObject().unsubscribeToCovRequest();
        objectService.get(objectName).clearPropertyList();
    }

    /**
     * This method does update the properties by changes sent from the remote device
     */
    public void  updateStream(String objectName){
        template.convertAndSend("/broker/" + objectName, objectService.get(objectName).getProperties());
        LOG.info("Send updated properties");
    }

    /**
     * This method is use to write the command request of the client
     * @param propertyIdentifier witch property
     * @param newValue new value for property
     */
    public void setNewValue(String propertyIdentifier, String newValue, String objectName){
        ObjectService ob = objectService.get(objectName);
        try {
            ob.getBacnetObject().writeValue(PropertyIdentifier.forName(propertyIdentifier),
                    BACnetTypes.getPropertyValuesByObjectType(newValue, ob.getBacnetObject()));
        } catch (Exception e){
            LOG.warn("Can't send value: {}   -> to {}", e.getLocalizedMessage(), ob.getObjectIdentifier());
        }
    }

    /**
     * Use to release manual operation
     */
    public void releaseValue(String objectName){
        objectService.get(objectName).getBacnetObject().releaseManualCommand();
        objectService.get(objectName).updatePropertyRelease();
    }

    }










