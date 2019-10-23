package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.eventParameter.EventParameter;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import static com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier.localDate;


@Component
public class EventService extends DeviceEventAdapter {

    @Autowired
    DeviceHandler deviceHandler;

    @Autowired
    HierarchyService hierarchyService;

    @Autowired
    EventHandler eventHandler;

    @Autowired
    ObjectService objectService;

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
            existingEvent = eventHandler.getEventByOid(eventObjectIdentifier.toString());
            eventHandler.update(existingEvent,fromState.toString(),toState.toString());
            //TODO, event handling with acknowledgement
            //Temporary remove event by toState = normal
            if (toState.toString().equals("normal")){
                eventHandler.removeEvent(existingEvent.getEventID());}
                System.out.println("Update or finishing event with ID: " + existingEvent.getEventID() + " from object: " + existingEvent.getOid());

        } catch (NullPointerException e){
            if (toState.toString().equals("off normal")){
               DateTime date  = timeStamp.getDateTime();
            BACnetEvent baCnetEvent = new BACnetEvent(eventHandler.getEvents().size() + 1,eventObjectIdentifier.toString(),initiatingDevice.getInstanceNumber(),date.getDate().toString() + " "+ date.getTime().toString(),
                    fromState.toString(),toState.toString(),getDescriptionOfObject(eventObjectIdentifier.toString()),objectService.getPresentValue(eventObjectIdentifier,initiatingDevice));
            eventHandler.addEvent(baCnetEvent);
            System.out.println("New event: " + baCnetEvent.getEventID() + " from object: " + baCnetEvent.getOid());
        }
    }}

    /**
     * This private method is used to get the description of a object by object-identifier for the event list
     * @param oid object-identifier
     * @return
     */
    private String getDescriptionOfObject (String oid){
       return hierarchyService.getDescriptionByOid(oid);
    }



}
