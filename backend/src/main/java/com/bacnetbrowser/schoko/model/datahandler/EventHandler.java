package com.bacnetbrowser.schoko.model.datahandler;

import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.services.ObjectService;
import com.serotonin.bacnet4j.type.enumerated.EventState;
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

    final private LinkedList<BACnetEvent> events = new LinkedList<>();

    public void addEvent(BACnetEvent baCnetEvent){
        events.add(baCnetEvent);
    }

    public LinkedList<BACnetEvent> getEvents() {
        return events;
    }

    public void update(BACnetEvent event, String fromState, String toState) {
        event.setFromState(fromState);
        event.setToState(toState);
    }

    public BACnetEvent getEventByOid(String oid){
        for (BACnetEvent event : events) {
            if (event.getOid().contains(oid)){
                return event;
            }
        }
        return null;
    }

    public void removeEvent (int ID){
        for (BACnetEvent event : events) {
            if (event.getEventID().equals(ID)){
                events.remove(event);
            }
    }
}
}

