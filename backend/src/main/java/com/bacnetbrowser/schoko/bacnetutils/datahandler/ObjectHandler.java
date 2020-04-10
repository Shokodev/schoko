package com.bacnetbrowser.schoko.bacnetutils.datahandler;


import com.bacnetbrowser.schoko.bacnetutils.models.BACnetTypes;
import com.bacnetbrowser.schoko.bacnetutils.services.ObjectService;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


/**
 * This class used to handle the BACnet object properties between BACnet and REST
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class ObjectHandler {


   private SimpMessagingTemplate template;
   private ObjectService objectService;
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
        this.objectService = objectService;
        objectService.clearPropertyList();
        objectService.readDataPointProperties(objectName);
        objectService.getBacnetObject().subscribeToCovRequest();
    }

    /**
     * After closing the websocket, the subscription at the remote device have to end as well and the propertyList have to be empty for next websocket stream
     */
    public void disconnectPropertyStream(){
        objectService.removeFromLocalDevice();
        objectService.getBacnetObject().unsubscribeToCovRequest();
        objectService.clearPropertyList();
    }

    /**
     * This method does update the properties by changes sent from the remote device
     */
    public void  updateStream(){
        template.convertAndSend("/broker/objectSub", objectService.getProperties());
        LOG.info("Send updated properties");
    }

    /**
     * This method is use to write the command request of the client
     * @param propertyIdentifier witch property
     * @param newValue new value for property
     */
    public void setNewValue(String propertyIdentifier, String newValue){
        objectService.getBacnetObject().writeValue(PropertyIdentifier.forName(propertyIdentifier),
                BACnetTypes.getPropertyValuesByObjectType(objectService.getObjectIdentifier().getObjectType(), newValue));
    }

    /**
     * Use to release manual operation
     */
    public void releaseValue(){
        objectService.getBacnetObject().releaseManualCommand();
    }

    }










