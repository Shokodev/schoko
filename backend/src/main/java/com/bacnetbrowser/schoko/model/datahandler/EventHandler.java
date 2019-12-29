package com.bacnetbrowser.schoko.model.datahandler;

import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import java.util.LinkedList;



/**
 * Handles the events between the BACnet and client
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class EventHandler {

    private EventService eventService;
    private SimpMessagingTemplate template;

    @Autowired
    public EventHandler(EventService eventService, SimpMessagingTemplate template) {
        this.eventService = eventService;
        this.template = template;

    }

    public LinkedList<BACnetEvent> getEvents() {
        return eventService.getEvents();
    }

    /**
     * by changes sent from the remote device the new list will be sent to the client
     */
    public void  updateStream(){
        template.convertAndSend("/broker/eventSub", eventService.getEvents());
        System.out.println("Send updated eventList");
    }

}

