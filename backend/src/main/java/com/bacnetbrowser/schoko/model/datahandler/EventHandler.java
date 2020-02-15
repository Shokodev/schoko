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

    private SimpMessagingTemplate template;

    @Autowired
    private EventService eventService;


    final private LinkedList<BACnetEvent> events = new LinkedList<>();

    @Autowired
    public EventHandler(SimpMessagingTemplate template) {
        this.template = template;
    }

    public LinkedList<BACnetEvent> getEvents() {
        return events;
    }

    /**
     * by changes sent from the remote device the new list will be sent to the client
     */
    public void  updateStream(){
        events.clear();
        events.addAll(eventService.getActiveEvents().values());
        template.convertAndSend("/broker/eventSub", events);
        System.out.println("Send updated eventList");
    }


}

