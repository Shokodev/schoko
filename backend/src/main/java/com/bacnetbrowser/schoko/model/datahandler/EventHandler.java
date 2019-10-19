package com.bacnetbrowser.schoko.model.datahandler;

import com.bacnetbrowser.schoko.model.models.BACnetEvents;
import com.bacnetbrowser.schoko.model.services.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;


/**
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class EventHandler {

    @Autowired
    DeviceHandler deviceHandler;
    @Autowired
    ObjectService objectService;

    private LinkedList<BACnetEvents> events = new LinkedList<>();

    public void addEvent(BACnetEvents baCnetEvents){
        events.add(baCnetEvents);
    }





}

