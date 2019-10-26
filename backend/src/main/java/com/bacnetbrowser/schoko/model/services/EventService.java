package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.LinkedList;

@Component
public class EventService extends DeviceEventAdapter {

    @Autowired
    private EventHandler eventHandler;

    final private LinkedList<BACnetEvent> events = new LinkedList<>();

    @Override
    public void iAmReceived(RemoteDevice d)  {
        int ID = d.getInstanceNumber();
        System.out.println("Remote device " + ID + " registered at LocalDevice");
    }

    /**
     * Catchs all event changes on the network an update the alarm list
     */
    @Override
    public void eventNotificationReceived(UnsignedInteger processIdentifier, RemoteDevice initiatingDevice, ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp, UnsignedInteger notificationClass, UnsignedInteger priority, EventType eventType, CharacterString messageText, NotifyType notifyType, Boolean ackRequired, EventState fromState, EventState toState, NotificationParameters eventValues) {
        BACnetEvent existingEvent;
        try {
            existingEvent = getEventByOid(eventObjectIdentifier.toString());
            update(existingEvent,fromState.toString(),toState.toString());
            //TODO, event handling with acknowledgement
            //Temporary remove event by toState = normal
            if (toState.toString().equals("normal")){
                removeEventByID(existingEvent.getEventID());}
                eventHandler.updateStream();
                System.out.println("Update or finishing event with ID: " + existingEvent.getEventID() + " from object: " + existingEvent.getOid());

        } catch (NullPointerException e){
            if (toState.toString().equals("off normal")){
               DateTime date  = timeStamp.getDateTime();
            BACnetEvent baCnetEvent = new BACnetEvent(eventObjectIdentifier.toString(),initiatingDevice.getVendorName(),date.getDate().toString() + " "+ date.getTime().toString(),
                    fromState.toString(),toState.toString(),getDescriptionOfObject(eventObjectIdentifier.toString()),getPresentValue(eventObjectIdentifier,initiatingDevice));
            baCnetEvent.setEventID(baCnetEvent.hashCode());
            addEvent(baCnetEvent);
            eventHandler.updateStream();
            System.out.println("New event: " + baCnetEvent.getEventID() + " from object: " + baCnetEvent.getOid());
        }

    }

    }

    /**
     * This private method is used to get the description of a object by object-identifier for the event list
     * @param oid object-identifier
     * @return description
     */
    private String getDescriptionOfObject (String oid){
        return HierarchyService.getKey(HierarchyService.objectNamesToDescription,oid);

    }

    private String getPresentValue(ObjectIdentifier oid, RemoteDevice remoteDevice){
        ConfirmedRequestService request = new ReadPropertyRequest(oid, PropertyIdentifier.presentValue);
        try{
            ReadPropertyAck result = (ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, request);
            return result.getValue().toString();
        } catch (BACnetException bac){
            System.out.println("Cant read present value of: " + oid + " @ " + remoteDevice);
        }
        return null;
    }

    private BACnetEvent getEventByOid(String oid){
        for (BACnetEvent event : events) {
            if (event.getOid().contains(oid)){
                return event;
            }
        }
        return null;
    }

    private void removeEventByID (int eventID){
        for (BACnetEvent event : events) {
            if (event.getEventID().equals(eventID)){
                events.remove(event);
            }
        }
    }

    private void addEvent(BACnetEvent baCnetEvent){
        events.add(baCnetEvent);
    }

    public LinkedList<BACnetEvent> getEvents() {
        return events;
    }

    public void update(BACnetEvent event, String fromState, String toState) {
        event.setFromState(fromState);
        event.setToState(toState);
    }

}
