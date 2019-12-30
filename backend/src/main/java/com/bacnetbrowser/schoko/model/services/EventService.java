package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.databaseConfig.EventRepository;
import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.models.BACnetProperty;
import com.bacnetbrowser.schoko.model.models.BACnetTypes;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
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
import java.util.Map;

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

    final private LinkedList<BACnetEvent> events = new LinkedList<>();

    @Override
    public void iAmReceived(RemoteDevice d) {
        int ID = d.getInstanceNumber();
        System.out.println("Remote device " + ID + " registered at LocalDevice");
    }

    /**
     * Catchs all event changes on the network and update the alarm list
     */
    @Override
    public void eventNotificationReceived(UnsignedInteger processIdentifier, RemoteDevice remoteDevice,
                                          ObjectIdentifier oid, TimeStamp timeStamp, UnsignedInteger notificationClass,
                                          UnsignedInteger priority, EventType eventType, CharacterString messageText,
                                          NotifyType notifyType, Boolean ackRequired, EventState fromState,
                                          EventState toState, NotificationParameters eventValues) {
        DateTime date = timeStamp.getDateTime();
        BACnetEvent bacnetEvent = new BACnetEvent(processIdentifier.toString(), remoteDevice.getVendorName(), oid.toString(),
                BACnetTypes.parseDateTime(date), notificationClass.toString(), priority.toString(), eventType.toString(),
                messageText.toString(), notifyType.toString(), ackRequired.toString(),
                BACnetTypes.getGermanEventStateText(fromState.toString()),
                BACnetTypes.getGermanEventStateText(toState.toString()), eventValues.toString(),
                getDescriptionOfObject(oid, remoteDevice), BACnetTypes.getPresentValueAsText(oid, remoteDevice),
                getObjectName(oid, remoteDevice));

        eventRepository.save(bacnetEvent);
        if (!removeExpiertEvent(oid, remoteDevice)){
            events.add(bacnetEvent);
            System.out.println("New event added from object: " + bacnetEvent.getObjectName());
        } else {
            System.out.println("Event removed from object: " + bacnetEvent.getObjectName());
        }
        eventHandler.updateStream();

    }


    /**
     * This method is used to get the description of a object by object-identifier for the event list
     *
     * @param oid          object-identifier
     * @param remoteDevice remote device of object
     * @return description of object
     */
    private String getDescriptionOfObject(ObjectIdentifier oid, RemoteDevice remoteDevice) {
        try {
            return ((ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.description))).getValue().toString();
        } catch (BACnetException ignored) {
        }
        return null;
    }

    /**
     * Read the right time stamp dependent on the event state of the object
     *
     * @param oid          object identifier
     * @param remoteDevice remote device of the object
     * @param eventState   event state of the object
     * @return time stamp as dateTime
     */
    private DateTime getTimeStampofObject(ObjectIdentifier oid, RemoteDevice remoteDevice, String eventState) {
        try {
            Encodable a = ((ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.eventTimeStamps))).getValue();
            List<TimeStamp> stamps = ((SequenceOf<TimeStamp>) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, remoteDevice, oid,
                    PropertyIdentifier.eventTimeStamps)).getValues();
            if (eventState.equals(EventState.fault.toString())) {
                return stamps.get(1).getDateTime();

            } else {
                return stamps.get(0).getDateTime();

            }
        } catch (BACnetException ignored) {
        }
        return new DateTime();
    }

    /**
     * Removes a new event depending on event state
     */
    private boolean removeExpiertEvent(ObjectIdentifier oid, RemoteDevice remoteDevice) {
        return events.removeIf(event -> event.getOid().equals(oid.toString()) && event.getRemoteDeviceName().equals(remoteDevice.getVendorName()));
    }

    public LinkedList<BACnetEvent> getEvents() {
        return events;
    }

    /**
     * Update event state on a BACnet event
     * TODO this method will be used after the  acknowledgement is implemented
     *
     * @param event     BACnet event
     * @param fromState event state before change of caught event
     * @param toState   new event state of caught event
     */
    public void update(BACnetEvent event, String fromState, String toState) {
        event.setFromState(fromState);
        event.setToState(toState);
    }

    /**
     * Gos trough all objects and checks their event state
     */
    public void getEventInformation() {
        events.clear();
        for (RemoteDevice remoteDevice : DeviceService.localDevice.getRemoteDevices()) {
            List<ObjectIdentifier> oids = null;
            try {
                oids = ((SequenceOf<ObjectIdentifier>)
                        RequestUtils.sendReadPropertyAllowNull(
                                DeviceService.localDevice, remoteDevice, remoteDevice.getObjectIdentifier(),
                                PropertyIdentifier.objectList)).getValues();
            } catch (BACnetException bac) {
                System.err.println("Cant read object identifier for events");
            }
            for (ObjectIdentifier oid : oids) {
                ConfirmedRequestService request = new ReadPropertyRequest(oid, PropertyIdentifier.eventState);
                try {
                    ReadPropertyAck result = (ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, request);
                    if ((!result.getValue().equals(EventState.normal)) && (!oid.toString().startsWith("Vendor"))) {
                        addExistingEvents(oid, remoteDevice, result.getValue().toString());
                    }
                } catch (BACnetException ignored) {

                }
            }

        }
        eventHandler.updateStream();
    }

    /**
     * Is in the sequence of getEventInformation, has a object not a normal event state a new event object will be created
     *
     * @param oid          object identifier
     * @param remoteDevice remote device of object
     * @param eventState   event state of the object
     */
    private void addExistingEvents(ObjectIdentifier oid, RemoteDevice remoteDevice, String eventState) {
        BACnetEvent event = getEventProperties(oid, remoteDevice, eventState);
        eventRepository.save(event);
        events.add(event);
    }

    private String getObjectName(ObjectIdentifier oid, RemoteDevice remoteDevice) {
        try {
            return ((ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.objectName))).getValue().toString();
        } catch (BACnetException ignored) {
        }
        return null;
    }

    private BACnetEvent getEventProperties(ObjectIdentifier oid, RemoteDevice remoteDevice, String eventState) {
        DateTime date = getTimeStampofObject(oid, remoteDevice, eventState);
            return new BACnetEvent("1", remoteDevice.getVendorName(), oid.toString()
                    ,BACnetTypes.parseDateTime(date), "ERROR_notificationClass",
                    "ERROR_priority", "ERROR_eventType", "ERROR_actionText", "ERROR_notifyType",
                    "ERROR_ackRequired", EventState.normal.toString(), BACnetTypes.getGermanEventStateText(eventState),
                    "ERROR_eventParameters", getDescriptionOfObject(oid, remoteDevice),
                    BACnetTypes.getPresentValueAsText(oid, remoteDevice), getObjectName(oid, remoteDevice));
        }



}



