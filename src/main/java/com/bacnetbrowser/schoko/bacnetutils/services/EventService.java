package com.bacnetbrowser.schoko.bacnetutils.services;


import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.databaseconfig.EventRepository;
import com.bacnetbrowser.schoko.datahandler.EventHandler;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetEvent;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetObject;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetTypes;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.GetAlarmSummaryAck;
import com.serotonin.bacnet4j.service.acknowledgement.GetEnrollmentSummaryAck;
import com.serotonin.bacnet4j.service.confirmed.*;
import com.serotonin.bacnet4j.type.constructed.*;
import com.serotonin.bacnet4j.type.enumerated.*;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.*;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class is for manage the events out of the BACnet network
 * Communication to Network in here
 *
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */

public class EventService extends DeviceEventAdapter  {

    static EventHandler eventHandler;
    static EventRepository eventRepository;
    private final HashMap<String, BACnetEvent> activeEvents = new HashMap<>();
    static final Logger LOG = LoggerFactory.getLogger(EventService.class);

    public EventService(EventHandler eventHandler, EventRepository eventRepository) {
        EventService.eventHandler = eventHandler;
        EventService.eventRepository = eventRepository;
    }


    @Override
    public void eventNotificationReceived(UnsignedInteger processIdentifier, ObjectIdentifier initiatingDeviceIdentifier,
                                          ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp,
                                          UnsignedInteger notificationClass, UnsignedInteger priority,
                                          EventType eventType, CharacterString messageText, NotifyType notifyType,
                                          Boolean ackRequired, EventState fromState, EventState toState,
                                          NotificationParameters eventValues) {
        LOG.info("Event message received");
        if(Character.isDigit(eventObjectIdentifier.toString().charAt(0)) ||
                initiatingDeviceIdentifier.getObjectType().equals(ObjectType.trendLog)) {
            LOG.info("Event with oid: {} ignored", eventObjectIdentifier.toString());
        } else {
            EventProcessing eventProcessing = new EventProcessing(processIdentifier, initiatingDeviceIdentifier,
                    eventObjectIdentifier, timeStamp,
                    notificationClass, priority,
                    eventType, messageText, notifyType,
                    ackRequired, fromState, toState,
                    eventValues,this);
            Thread thread = new Thread(eventProcessing);
            thread.start();
        }
    }



    /**
     * Catch's all event changes on the network and update the db and event list
     */

    boolean isAckNeeded(EventTransitionBits bits){
        return !bits.isToFault() || !bits.isToOffnormal();
    }

    boolean isResetNeeded(EventTransitionBits bits){
        return !bits.isToNormal();
    }

    void closeEventIfPossible(BACnetEvent existingEvent){
        if(existingEvent.getAckState() && existingEvent.getResetState() && existingEvent.getToState().equals("Normal")) {
            activeEvents.remove(existingEvent.getObjectName());
        }
    }

    public HashMap<String, BACnetEvent> getActiveEvents() {
        return activeEvents;
    }

    //TODO whole acknowledge process -- Problem [What if firstEvent does not exist?]
    // Problem is temporarily solved by setting the same time as ack time
    public void acknowledgeEvent(String objectName){
        ObjectIdentifier oid = HierarchyService.objectNamesToOids.get(objectName);
        BACnetDevice bacnetDevice = HierarchyService.obejctNamesToBACnetDevice.get(objectName);
        BACnetObject object = bacnetDevice.getBACnetObject(oid);
        TimeStamp timeStamp;
        try {
            timeStamp = BACnetTypes.parseToBACnetTimeStamp((activeEvents.get(objectName)).getTimeStamp());
        } catch (NullPointerException e){
            timeStamp = new TimeStamp(new DateTime(DeviceService.localDevice));
        }
        TimeStamp timeStampOfAck = new TimeStamp(new DateTime(DeviceService.localDevice));
        CharacterString ack = new CharacterString("1");
        AcknowledgeAlarmRequest request = new AcknowledgeAlarmRequest(new UnsignedInteger(1),oid,
                (EventState) object.readProperty(PropertyIdentifier.eventState),timeStamp,ack,timeStampOfAck);
                try{
                    DeviceService.localDevice.send(bacnetDevice,request).get();
                } catch (BACnetException bac){
                    LOG.warn("Cant ack {} on device {} -> cause: ", oid.toString(),bacnetDevice.getName(), bac.fillInStackTrace());
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
    private TimeStamp getTimeStampOfObject(ObjectIdentifier oid, RemoteDevice remoteDevice, String eventState) {
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
       LOG.warn("Cant read time stamp of: " + oid.toString() + " from: " + remoteDevice.getVendorName() + " Local Datetime has been set");

       return new TimeStamp(new Time(DeviceService.localDevice));
    }
    }

    /**
     * Get all not acked active events
     */
    public void getEventInformation()  {
        LOG.info("Ask for event information");
        for (BACnetDevice bacnetDevice : DeviceService.bacnetDevices) {
            GetAlarmSummaryRequest request = new GetAlarmSummaryRequest();
            GetAlarmSummaryAck events = null;
            try {
                events = DeviceService.localDevice.send(bacnetDevice, request).get();
            } catch (BACnetException e) {
                LOG.warn("Can't get event information");
            }
                LOG.info("Received {} events from device: {} ", events.getValues().size() , bacnetDevice.getName());
            for (GetAlarmSummaryAck.AlarmSummary event : events.getValues()){
                    if(Character.isDigit(event.getObjectIdentifier().toString().charAt(0)) ||
                            event.getObjectIdentifier().getObjectType().equals(ObjectType.trendLog)) {
                        LOG.info("Event with oid: {} ignored", event.getObjectIdentifier().toString());
                    } else {
                        getFurtherEventInformation(GetEnrollmentSummaryRequest.AcknowledgmentFilter.acked, bacnetDevice);
                        createSQLEvent(event, bacnetDevice);
                    }
            }
        }
        eventHandler.updateStream();
    }

    //TODO events which are already exist with state: (Source normal but not acked or reset) before a device is imported will be ignored
    private List<ObjectIdentifier> getFurtherEventInformation(GetEnrollmentSummaryRequest.AcknowledgmentFilter
                                                                      acknowledgmentFilter, BACnetDevice bacnetDevice){
        GetEnrollmentSummaryRequest request = new GetEnrollmentSummaryRequest(acknowledgmentFilter,null,
                null,null,null,null);
        try{
            return ((GetEnrollmentSummaryAck) DeviceService.localDevice.send(bacnetDevice, request).get()).getValues()
                    .getValues().stream()
                    .map(GetEnrollmentSummaryAck.EnrollmentSummary::getObjectIdentifier)
                    .collect(Collectors.toList());
        }catch (BACnetException bac){
            LOG.warn("Can't get event information ack filter set to: {} ", acknowledgmentFilter);
            return null;
        }
    }
    /**
     * Used to create SQL Entries by getEventInformation
     * @param event alarm summary objects
     * @param bacnetDevice remote device of this objects
     */
    private void createSQLEvent(GetAlarmSummaryAck.AlarmSummary event, BACnetDevice bacnetDevice) {
        TimeStamp timeStamp = getTimeStampOfObject(event.getObjectIdentifier(),bacnetDevice, event.getAlarmState().toString());
        try {
           BACnetEvent eventSQL = eventRepository.findBACnetEventByRemoteDeviceNameAndOidAndAndTimeStamp(
                   bacnetDevice.getName(),event.getObjectIdentifier().toString(),
                   BACnetTypes.parseToSQLTimeStamp(timeStamp));
            LOG.info("Event already exists in database: " + eventSQL.getObjectName() + " ID: " + eventSQL.getEventID());
            activeEvents.put(eventSQL.getObjectName(),eventSQL);
        }catch (NullPointerException n) {
            BACnetObject object = bacnetDevice.getBACnetObject(event.getObjectIdentifier());
            BACnetObject notiClass = bacnetDevice.getBACnetObject(new ObjectIdentifier(
                    ObjectType.notificationClass, Integer.parseInt(object.readProperty(
                            PropertyIdentifier.notificationClass).toString())));
            EventTransitionBits eventTransitionBits = notiClass.getEventTransitionBits();
            boolean isAckNeeded = getFurtherEventInformation(GetEnrollmentSummaryRequest.AcknowledgmentFilter.acked, bacnetDevice).contains(event.getObjectIdentifier());
            BACnetEvent bacnetEvent = new BACnetEvent("1", bacnetDevice.getName(),
                    object.getObjectIdentifier().toString(),
                    BACnetTypes.parseToSQLTimeStamp(timeStamp),
                    BACnetTypes.parseToSQLTimeStamp(timeStamp),
                    object.readProperty(PropertyIdentifier.notificationClass).toString(),
                    notiClass.getPriorityByStatus(event.getAlarmState())
                    , event.getAlarmState().toString(), " ", "Alarm",
                    eventTransitionBits.toString(), BACnetTypes.getGermanEventStateText(EventState.normal),
                    BACnetTypes.getGermanEventStateText(event.getAlarmState()),
                    "EventSummaryLogEntry", object.getDescription(),
                    object.getPresentValueAsText(), object.getObjectName(),isAckNeeded,
                    isResetNeeded(eventTransitionBits),UUID.randomUUID().toString());
            activeEvents.put(object.getObjectName(), bacnetEvent);
            eventRepository.save(bacnetEvent);
        }
    }
}



