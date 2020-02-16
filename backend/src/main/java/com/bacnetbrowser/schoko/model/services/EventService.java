package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.databaseConfig.EventRepository;
import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.models.BACnetObject;
import com.bacnetbrowser.schoko.model.models.BACnetTypes;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.GetEnrollmentSummaryAck;
import com.serotonin.bacnet4j.service.confirmed.*;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.*;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;


/**
 * This class is for manage the events out of the BACnet network
 * Communication to Network in here
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */

@Component
public class EventService extends DeviceEventAdapter {

    @Autowired
    private EventHandler eventHandler;
    @Autowired
    private EventRepository eventRepository;

    private final HashMap<String, BACnetEvent> activeEvents = new HashMap<>();

    @Override
    public void iAmReceived(RemoteDevice d) {
        int ID = d.getInstanceNumber();
        System.out.println("Remote device " + ID + " registered at LocalDevice");
    }

    /**
     * Catchs all event changes on the network and update the db and event list
     */
    @Override
    public void eventNotificationReceived(UnsignedInteger processIdentifier, RemoteDevice remoteDevice,
                                          ObjectIdentifier oid, TimeStamp timeStamp, UnsignedInteger notificationClass,
                                          UnsignedInteger priority, EventType eventType, CharacterString messageText,
                                          NotifyType notifyType, Boolean ackRequired, EventState fromState,
                                          EventState toState, NotificationParameters eventValues) {
        System.out.println("Event message received");
        // Events with type "Event" will be ignored
        try {
            BACnetObject object = (BACnetObject) remoteDevice.getObject(oid);
            BACnetObject notiClass = (BACnetObject) remoteDevice.getObject(new ObjectIdentifier(ObjectType.notificationClass, Integer.parseInt(object.getProperty(PropertyIdentifier.notificationClass).toString())));
            EventTransitionBits eventTransitionBits = notiClass.getAckTransitBits();
            BACnetEvent bacnetEvent;

            // Edit ack notification
            if (notifyType.equals(NotifyType.ackNotification)) {
                BACnetEvent existingEvent = activeEvents.get(object.getObjectName());
                String actualEventState = object.getProperty(PropertyIdentifier.eventState).toString();
                if (!existingEvent.getAckState()) {
                    existingEvent.setAckState(true);
                } else if (!existingEvent.getResetState()) {
                    existingEvent.setResetState(true);
                }
                existingEvent.setNotifyType(BACnetTypes.getNotifyTypeAsText(notifyType));
                existingEvent.setTimeStamp(BACnetTypes.parseToSQLTimeStamp(timeStamp));
                existingEvent.setPresentValue(object.getPresentValueAsText());
                existingEvent.setFromState(BACnetTypes.getGermanEventStateText(actualEventState));
                existingEvent.setToState(BACnetTypes.getGermanEventStateText(actualEventState));
                eventRepository.save(new BACnetEvent(existingEvent));
                closeEventIfPossible(existingEvent);
            }
            // Edit back to normal
            else if (toState.equals(EventState.normal)) {
                BACnetEvent existingEvent = activeEvents.get(object.getObjectName());
                existingEvent.setPresentValue(object.getPresentValueAsText());
                existingEvent.setNotifyType(BACnetTypes.getNotifyTypeAsText(notifyType));
                existingEvent.setTimeStamp(BACnetTypes.parseToSQLTimeStamp(timeStamp));
                existingEvent.setFromState(BACnetTypes.getGermanEventStateText(fromState.toString()));
                existingEvent.setToState(BACnetTypes.getGermanEventStateText(toState.toString()));
                eventRepository.save(new BACnetEvent(existingEvent));
                closeEventIfPossible(existingEvent);
            }
            // New Event
            else {
                bacnetEvent = new BACnetEvent(processIdentifier.toString(), remoteDevice.getVendorName(), oid.toString(),
                        BACnetTypes.parseToSQLTimeStamp(timeStamp), notificationClass.toString(), priority.toString(), eventType.toString(),
                        messageText.toString(), BACnetTypes.getNotifyTypeAsText(notifyType), eventTransitionBits.toString(),
                        BACnetTypes.getGermanEventStateText(fromState.toString()),
                        BACnetTypes.getGermanEventStateText(toState.toString()), eventValues.toString(),
                        object.getDescription(), object.getPresentValueAsText(),
                        object.getObjectName(), isAckNeeded(eventTransitionBits), isResetNeeded(eventTransitionBits), UUID.randomUUID().toString());
                activeEvents.put(object.getObjectName(), bacnetEvent);
                eventRepository.save(bacnetEvent);
            }
            eventHandler.updateStream();
        } catch (NullPointerException e){
            System.err.println("Event from " + oid + " @ " + remoteDevice + " could not be processed");
        }
        System.out.println("Active events: " + activeEvents.size());


    }

    public boolean isAckNeeded(EventTransitionBits bits){
        return !bits.isToFault() || !bits.isToOffnormal();
    }

    public boolean isResetNeeded(EventTransitionBits bits){
        return !bits.isToNormal();
    }

    public void closeEventIfPossible(BACnetEvent existingEvent){
        if(existingEvent.getAckState() && existingEvent.getResetState() && existingEvent.getToState().equals("Normal")) {
            activeEvents.remove(existingEvent.getObjectName());
        }
    }

    public HashMap<String, BACnetEvent> getActiveEvents() {
        return activeEvents;
    }

    //TODO whole acknowledge process
    public void acknowledgeEvent(String objectName){
        BACnetEvent editEvent = activeEvents.get(objectName);
        BACnetEvent firstEvent = eventRepository.findTopByEventIDIs(editEvent.getEventID());
        ObjectIdentifier oid = HierarchyService.objectNamesToOids.get(objectName);
        RemoteDevice remoteDevice = HierarchyService.obejctNamesToRemoteDevice.get(objectName);
        BACnetObject object = (BACnetObject) remoteDevice.getObject(oid);
        TimeStamp timeStamp = BACnetTypes.parseToBACnetTimeStamp(firstEvent.getTimeStamp());
        TimeStamp timeStampOfAck = new TimeStamp(new DateTime());
        CharacterString ack = new CharacterString("1");
        AcknowledgeAlarmRequest request = new AcknowledgeAlarmRequest(new UnsignedInteger(1),oid,
                (EventState) object.getProperty(PropertyIdentifier.eventState),timeStamp,ack,timeStampOfAck);
        try{
           DeviceService.localDevice.send(remoteDevice,request);
        } catch (BACnetException bac){
           System.err.println("Cant ack " + oid.toString() + " on " + remoteDevice.getVendorName());
        }
    }

    //Look for existing events

    //TODO Events which are found and are already acknowledged cant be properly processed -> repair
    /**
     * Read the right time stamp dependent on the event state of the object
     *
     * @param oid          object identifier
     * @param remoteDevice remote device of the object
     * @param eventState   event state of the object
     * @return time stamp as dateTime
     */
    private TimeStamp getTimeStampofObject(ObjectIdentifier oid, RemoteDevice remoteDevice, String eventState) {
        try {
            List<TimeStamp> stamps = ((SequenceOf<TimeStamp>) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, remoteDevice, oid,
                    PropertyIdentifier.eventTimeStamps)).getValues();
            if (eventState.equals(EventState.fault.toString())) {
                return stamps.get(1);

            } else if (eventState.equals(EventState.normal.toString())) {
                return stamps.get(2);
            }
            else return stamps.get(0);
        } catch (BACnetException | NullPointerException ignored) {
       System.err.println("Cant read time stamp of: " + oid.toString() + " from: " + remoteDevice.getVendorName() + " Local Datetime has been set");

       return new TimeStamp(new Time());
    }
    }

    /**
     * Get all not acked active events
     */
    public void getEventInformation(boolean acked) {
        for (RemoteDevice remoteDevice : DeviceService.localDevice.getRemoteDevices()) {
            Enumerated ack;
            if(acked){
                //acked
                ack = new Enumerated(1);
            }else {
                //not acked
                ack = new Enumerated(2);
            }
                    try {
                        GetEnrollmentSummaryRequest request = new GetEnrollmentSummaryRequest(ack,null,null,null,null,null);
                        GetEnrollmentSummaryAck events = (GetEnrollmentSummaryAck) DeviceService.localDevice.send(remoteDevice, request);
                        for (GetEnrollmentSummaryAck.EnrollmentSummary event : events.getValues()){
                            try {
                                if(!event.getObjectIdentifier().toString().startsWith("Vendor") && !event.getObjectIdentifier().equals(ObjectType.trendLog)) {
                                    eventRepository.save(Objects.requireNonNull(createSQLEvent(event, remoteDevice,acked)));
                                }
                            } catch (NullPointerException ignored){

                            }
                        }
                    }catch (BACnetException bac) {
                        System.err.println("Faild to read Event information of device " + remoteDevice.getVendorName());
                    }
                }
        eventHandler.updateStream();
    }

    /**
     * Used to create SQL Entries by getEventInformation
     * @param event alarm summary objects
     * @param remoteDevice remote device of this objects
     * @return event entry
     */
    private BACnetEvent createSQLEvent(GetEnrollmentSummaryAck.EnrollmentSummary event, RemoteDevice remoteDevice,boolean ack) {
        TimeStamp timeStamp = getTimeStampofObject(event.getObjectIdentifier(),remoteDevice, event.getEventState().toString());
        try {
           BACnetEvent eventSQL = eventRepository.findBACnetEventByRemoteDeviceNameAndOidAndAndTimeStamp(remoteDevice.getVendorName(),event.getObjectIdentifier().toString(),BACnetTypes.parseToSQLTimeStamp(timeStamp));
           System.out.println("Event already exists in database: " + eventSQL.getObjectName() + " ID: " + eventSQL.getEventID());
            activeEvents.put(eventSQL.getObjectName(),eventSQL);
           return null;
        }catch (NullPointerException n) {
            BACnetObject object = (BACnetObject) remoteDevice.getObject(event.getObjectIdentifier());
            BACnetObject notiClass = (BACnetObject) remoteDevice.getObject(new ObjectIdentifier(ObjectType.notificationClass, Integer.parseInt(object.getProperty(PropertyIdentifier.notificationClass).toString())));
            EventTransitionBits eventTransitionBits = notiClass.getAckTransitBits();
            boolean isAckNeeded;
            if(ack){
                isAckNeeded = true;
            }else {
                isAckNeeded = isAckNeeded(eventTransitionBits);
            }
            BACnetEvent bacnetEvent = new BACnetEvent("1", remoteDevice.getVendorName(), object.getObjectIdentifier().toString()
                    , BACnetTypes.parseToSQLTimeStamp(timeStamp), object.getProperty(PropertyIdentifier.notificationClass).toString(), notiClass.getPriorityByStatus(event.getEventState())
                    , event.getEventType().toString(), " ", "Alarm",
                    eventTransitionBits.toString(), BACnetTypes.getGermanEventStateText(EventState.normal.toString()), BACnetTypes.getGermanEventStateText(event.getEventState().toString()), "EventSummaryLogEntry", object.getDescription(),
                    object.getPresentValueAsText(), object.getObjectName(),isAckNeeded,isResetNeeded(eventTransitionBits),UUID.randomUUID().toString());
            activeEvents.put(object.getObjectName(), bacnetEvent);
            return bacnetEvent;
        }
    }

}



