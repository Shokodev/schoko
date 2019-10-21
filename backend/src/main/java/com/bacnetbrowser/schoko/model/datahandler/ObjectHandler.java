package com.bacnetbrowser.schoko.model.datahandler;

import com.bacnetbrowser.schoko.model.models.BACnetProperties;
import com.bacnetbrowser.schoko.model.services.ObjectService;
import com.serotonin.bacnet4j.exception.BACnetException;
import org.springframework.beans.factory.annotation.Autowired;
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

    //TODO Update JSON, websocket needed
    public LinkedList<BACnetProperties> update (String elementName) throws BACnetException {
        objectService.readDataPointProperties(elementName);
        return objectService.getProperties();
    }



}



