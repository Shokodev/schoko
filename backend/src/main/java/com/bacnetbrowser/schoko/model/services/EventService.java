package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.models.BACnetTypes;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.*;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.List;


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
            if (toState.equals(EventState.normal)){
                removeEventByID(existingEvent.getEventID());}
                eventHandler.updateStream();
                System.out.println("Update or finishing event with ID: " + existingEvent.getEventID() + " from object: " + existingEvent.getOid());

        } catch (NullPointerException e){
            if (!toState.equals(EventState.normal)){
               DateTime date  = timeStamp.getDateTime();
            BACnetEvent baCnetEvent = new BACnetEvent(eventObjectIdentifier.toString(),initiatingDevice.getVendorName(),parseDateTime(date),
                    BACnetTypes.getGermanEventStateText(fromState.toString()),BACnetTypes.getGermanEventStateText(toState.toString()),
                    getDescriptionOfObject(eventObjectIdentifier,initiatingDevice),
                    BACnetTypes.getPresentValueAsText(eventObjectIdentifier,initiatingDevice));
            //TODO As soon as the event history will be implemented, the eventID will be the ID of the Database. We are going to use this: https://docs.objectbox.io/getting-started
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
     * @param remoteDevice remote device of object
     * @return description of object
     */
    private String getDescriptionOfObject (ObjectIdentifier oid,RemoteDevice remoteDevice){
        try {
            return ((ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.description))).getValue().toString();
        } catch (BACnetException ignored){}
        return null;
    }

    private DateTime getTimeStampofObject(ObjectIdentifier oid,RemoteDevice remoteDevice, String eventState){
        try {
            Encodable a =  ((ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.eventTimeStamps))).getValue();
            List<TimeStamp> stamps = ((SequenceOf<TimeStamp>) RequestUtils.sendReadPropertyAllowNull(
                    DeviceHandler.localDevice, remoteDevice, oid,
                    PropertyIdentifier.eventTimeStamps)).getValues();
            if (eventState.equals(EventState.fault.toString())) {
                return stamps.get(1).getDateTime();

            } else {
                return stamps.get(0).getDateTime();

            }
        } catch (BACnetException ignored){}
        return new DateTime();
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

    public void getEventInformation() {

        for (RemoteDevice remoteDevice : DeviceHandler.localDevice.getRemoteDevices()) {
            List<ObjectIdentifier> oids = null;
            try {
                oids = ((SequenceOf<ObjectIdentifier>)
                        RequestUtils.sendReadPropertyAllowNull(
                                DeviceHandler.localDevice, remoteDevice, remoteDevice.getObjectIdentifier(),
                                PropertyIdentifier.objectList)).getValues();
                } catch (BACnetException bac) {
                System.err.println("Cant read object identifier for events");
                }
                for (ObjectIdentifier oid : oids) {
                    ConfirmedRequestService request = new ReadPropertyRequest(oid, PropertyIdentifier.eventState);
                    try {
                    ReadPropertyAck result = (ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, request);
                    if(!result.getValue().equals(EventState.normal)){
                        addExistingEvents(oid,remoteDevice,result.getValue().toString());
                    }}catch (BACnetException ignored){

                    }
                }


        }
        eventHandler.updateStream();
    }

    private void addExistingEvents(ObjectIdentifier oid, RemoteDevice remoteDevice, String eventState){
        if(!oid.toString().startsWith("Vendor")){
        DateTime date = getTimeStampofObject(oid,remoteDevice,eventState);
        BACnetEvent event = new BACnetEvent(oid.toString(),remoteDevice.getVendorName(),parseDateTime(date),EventState.normal.toString(),BACnetTypes.getGermanEventStateText(eventState),getDescriptionOfObject(oid,remoteDevice),BACnetTypes.getPresentValueAsText(oid,remoteDevice));
        event.setEventID(event.hashCode());
        addEvent(event);
    }}

    private String parseDateTime(DateTime dateTime){
        String finalDate = dateTime.getDate().toString() + " | " + dateTime.getTime().toString();
        return finalDate.replaceAll(", 119","");
    }

}
