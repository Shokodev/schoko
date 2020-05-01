package com.bacnetbrowser.schoko.bacnetutils.services;

import com.bacnetbrowser.schoko.bacnetutils.models.BACnetDevice;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetEvent;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetObject;
import com.bacnetbrowser.schoko.bacnetutils.models.BACnetTypes;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.*;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

public class EventProcessing implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(EventProcessing.class);
    private final UnsignedInteger processIdentifier;
    private final ObjectIdentifier initiatingDeviceIdentifier;
    private final ObjectIdentifier eventObjectIdentifier;
    private final TimeStamp timeStamp;
    private final UnsignedInteger notificationClass;
    private final UnsignedInteger priority;
    private final EventType eventType;
    private final CharacterString messageText;
    private final NotifyType notifyType;
    private final Boolean ackRequired;
    private final EventState fromState;
    private final EventState toState;
    private final NotificationParameters eventValues;
    private final EventService eventService;

    EventProcessing(UnsignedInteger processIdentifier, ObjectIdentifier initiatingDeviceIdentifier,
                    ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp,
                    UnsignedInteger notificationClass, UnsignedInteger priority,
                    EventType eventType, CharacterString messageText,
                    NotifyType notifyType, Boolean ackRequired, EventState fromState,
                    EventState toState, NotificationParameters eventValues, EventService eventService) {

        this.processIdentifier = processIdentifier;
        this.initiatingDeviceIdentifier = initiatingDeviceIdentifier;
        this.eventObjectIdentifier = eventObjectIdentifier;
        this.timeStamp = timeStamp;
        this.notificationClass = notificationClass;
        this.priority = priority;
        this.eventType = eventType;
        this.messageText = messageText;
        this.notifyType = notifyType;
        this.ackRequired = ackRequired;
        this.fromState = fromState;
        this.toState = toState;
        this.eventValues = eventValues;
        this.eventService = eventService;
    }

    @Override
    public void run() {
        // Events with type "Event" will be ignored
        try {
            BACnetDevice bacnetDevice = DeviceService.getBacnetDevice(initiatingDeviceIdentifier);
            BACnetObject bacnetObject = bacnetDevice.getBACnetObject(eventObjectIdentifier);
            BACnetObject notiClass = bacnetDevice.getBACnetObject(new ObjectIdentifier(ObjectType.notificationClass,notificationClass.intValue()));
            EventTransitionBits eventTransitionBits = notiClass.getEventTransitionBits();
            BACnetEvent bacnetEvent;

            // Edit ack notification
            if (notifyType.equals(NotifyType.ackNotification)) {
                LOG.info("Acknowledge massage from: {}" ,bacnetObject.getObjectName());
                BACnetEvent existingEvent = eventService.getActiveEvents().get(bacnetObject.getObjectName());
                if (!existingEvent.getAckState()) {
                    existingEvent.setAckState(true);
                } else if (!existingEvent.getResetState()) {
                    existingEvent.setResetState(true);
                }
                existingEvent.setNotifyType(BACnetTypes.getNotifyTypeAsText(notifyType));
                existingEvent.setTimeStamp(BACnetTypes.parseToSQLTimeStamp(timeStamp));
                existingEvent.setPresentValue(bacnetObject.getPresentValueAsText());
                existingEvent.setFromState(BACnetTypes.getGermanEventStateText(fromState));
                existingEvent.setToState(BACnetTypes.getGermanEventStateText(toState));
                EventService.eventRepository.save(new BACnetEvent(existingEvent));
                eventService.closeEventIfPossible(existingEvent);
            }
            // Edit back to normal
            else if (toState.equals(EventState.normal)) {
                LOG.info("Back to normal massage from: {}" ,bacnetObject.getObjectName());
                BACnetEvent existingEvent = eventService.getActiveEvents().get(bacnetObject.getObjectName());
                existingEvent.setPresentValue(bacnetObject.getPresentValueAsText());
                existingEvent.setNotifyType(BACnetTypes.getNotifyTypeAsText(notifyType));
                existingEvent.setTimeStamp(BACnetTypes.parseToSQLTimeStamp(timeStamp));
                existingEvent.setFromState(BACnetTypes.getGermanEventStateText(fromState));
                existingEvent.setToState(BACnetTypes.getGermanEventStateText(toState));
                EventService.eventRepository.save(new BACnetEvent(existingEvent));
                eventService.closeEventIfPossible(existingEvent);
            }
            // New Event
            else {
                LOG.info("New event massage from: {}" ,bacnetObject.getObjectName());
                bacnetEvent = new BACnetEvent(processIdentifier.toString(), bacnetDevice.getVendorName(), eventObjectIdentifier.toString(),
                        BACnetTypes.parseToSQLTimeStamp(timeStamp), notificationClass.toString(), priority.toString(), eventType.toString(),
                        messageText.toString(), BACnetTypes.getNotifyTypeAsText(notifyType), eventTransitionBits.toString(),
                        BACnetTypes.getGermanEventStateText(fromState),
                        BACnetTypes.getGermanEventStateText(toState), eventValues.toString(),
                        bacnetObject.getDescription(), bacnetObject.getPresentValueAsText(),
                        bacnetObject.getObjectName(), eventService.isAckNeeded(eventTransitionBits), eventService.isResetNeeded(eventTransitionBits), UUID.randomUUID().toString());
                eventService.getActiveEvents().put(bacnetObject.getObjectName(), bacnetEvent);
                EventService.eventRepository.save(bacnetEvent);
            }
            EventService.eventHandler.updateStream();
        } catch (NullPointerException e){
            LOG.warn("Event from " + eventObjectIdentifier + " @ " + initiatingDeviceIdentifier + " could not be processed or will be ignored");
        }

    }


}
