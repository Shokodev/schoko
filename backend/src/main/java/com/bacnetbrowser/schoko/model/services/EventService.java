package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.databaseConfig.EventRepository;
import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.models.BACnetTypes;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.GetEventInformationAck;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.*;
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
import java.util.List;


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

        BACnetEvent bacnetEvent = new BACnetEvent(processIdentifier.toString(), remoteDevice.getVendorName(), oid.toString(),
                BACnetTypes.parseToSQLTimeStamp(timeStamp), notificationClass.toString(), priority.toString(), eventType.toString(),
                messageText.toString(), notifyType.toString(), ackRequired.toString(),
                BACnetTypes.getGermanEventStateText(fromState.toString()),
                BACnetTypes.getGermanEventStateText(toState.toString()), eventValues.toString(),
                getDescriptionOfObject(oid, remoteDevice), BACnetTypes.getPresentValueAsText(oid, remoteDevice),
                getObjectName(oid, remoteDevice));
        bacnetEvent.setEventID(createNewEventID(remoteDevice.getVendorName(), oid.toString()));
        if (toState.equals(EventState.normal)){
            bacnetEvent.setVisableInFrontend(false);
        }else {
            bacnetEvent.setVisableInFrontend(true);
        }
        eventRepository.save(bacnetEvent);
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
    private TimeStamp getTimeStampofObject(ObjectIdentifier oid, RemoteDevice remoteDevice, String eventState) {
        try {
            List<TimeStamp> stamps = ((SequenceOf<TimeStamp>) RequestUtils.sendReadPropertyAllowNull(
                    DeviceService.localDevice, remoteDevice, oid,
                    PropertyIdentifier.eventTimeStamps)).getValues();
            if (eventState.equals(EventState.fault.toString())) {
                return stamps.get(1);

            } else {
                return stamps.get(0);
            }
        } catch (BACnetException | NullPointerException ignored) {
       System.err.println("Cant read time stamp of: " + oid.toString() + " from: " + remoteDevice.getVendorName() + " Local Datetime has been set");
       return new TimeStamp(new DateTime());
    }
    }

    /**
     * Get all active events
     */
    public void getEventInformation() {

            try {
                for (RemoteDevice remotedevice : DeviceService.localDevice.getRemoteDevices()) {
                    GetEventInformation eventInfos = new GetEventInformation(null);
                    GetEventInformationAck events = (GetEventInformationAck) DeviceService.localDevice.send(remotedevice, eventInfos);
                    for (GetEventInformationAck.EventSummary event : events.getListOfEventSummaries()){
                       eventRepository.save(createSQLEvent(event,remotedevice));
                    }
                }
            }catch (BACnetException | NullPointerException bac) {
                System.err.println("Faild to read Event information");
            }
        eventHandler.updateStream();

    }

    private BACnetEvent createSQLEvent(GetEventInformationAck.EventSummary event, RemoteDevice remoteDevice) {
        ObjectIdentifier oid = event.getObjectIdentifier();
        TimeStamp timeStamp = getTimeStampofObject(event.getObjectIdentifier(),remoteDevice, event.getEventState().toString());
        BACnetEvent bacnetEvent =  new BACnetEvent("1", remoteDevice.getVendorName(), oid.toString()
                ,BACnetTypes.parseToSQLTimeStamp(timeStamp), "ERROR_notificationClass",
                "ERROR_priority", " ", " ", event.getNotifyType().toString(),
                "ERROR_ackRequired", BACnetTypes.getGermanEventStateText(EventState.normal.toString()), BACnetTypes.getGermanEventStateText(event.getEventState().toString()),
                "ERROR_eventParameters", getDescriptionOfObject(oid, remoteDevice),
                BACnetTypes.getPresentValueAsText(oid, remoteDevice), getObjectName(oid, remoteDevice));
        bacnetEvent.setEventID(createNewEventID(remoteDevice.getVendorName(), oid.toString()));
        if (event.getEventState().equals(EventState.normal)){
            bacnetEvent.setVisableInFrontend(false);
        }else {
            bacnetEvent.setVisableInFrontend(true);
        }

        return bacnetEvent;


    }

    private String getObjectName(ObjectIdentifier oid, RemoteDevice remoteDevice) {
        try {
            return ((ReadPropertyAck) DeviceService.localDevice.send(remoteDevice, new ReadPropertyRequest(oid, PropertyIdentifier.objectName))).getValue().toString();
        } catch (BACnetException ignored) {
        }
        return null;
    }

    private int createNewEventID(String remoteDevice, String oid){
       BACnetEvent bacnetEvent = eventRepository.findTopBACnetEventByRemoteDeviceNameAndOid(remoteDevice, oid);
       try{
       return bacnetEvent.getEventID() + 1;
       } catch (NullPointerException e){
       return 0;
       }
    }



}



