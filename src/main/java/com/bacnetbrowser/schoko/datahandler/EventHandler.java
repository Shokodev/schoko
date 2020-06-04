package com.bacnetbrowser.schoko.datahandler;

import com.bacnetbrowser.schoko.databaseconfig.EventRepository;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetEvent;
import com.bacnetbrowser.schoko.bacnetutils.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * Handles the events between the BACnet and client
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class EventHandler {

    private final SimpMessagingTemplate template;
    private final EventRepository eventRepository;
    private EventService eventService;
    private static final Logger LOG = LoggerFactory.getLogger(EventHandler.class);

    @Autowired
    public EventHandler(SimpMessagingTemplate template, EventRepository eventRepository) {
        this.template = template;
        this.eventRepository = eventRepository;
    }

    public void createEventStream(){
       EventService eventService = new EventService(this, eventRepository);
       this.eventService = eventService;
        Thread thread = new Thread(){
            public void run(){
                eventService.getEventInformation();
            }
        };
        thread.start();
    }

    /**
     * by changes sent from the remote device the new list will be sent to the client
     */
    public void  updateStream(){
        try {
            template.convertAndSend("/broker/eventSub", eventService.getActiveEvents().values());
            LOG.info("Send updated eventList with: " + eventService.getActiveEvents().size() + " events");
        } catch (NullPointerException e){
        LOG.warn("Can't fetch events before at least one device is imported");
        }
    }

    public void ackAllEvents(){
        for(BACnetEvent event : eventService.getActiveEvents().values()){
           eventService.acknowledgeEvent(event.getObjectName());
        }
    }

    public void ackEvent(String objectName) {
        eventService.acknowledgeEvent(objectName);
    }

}

