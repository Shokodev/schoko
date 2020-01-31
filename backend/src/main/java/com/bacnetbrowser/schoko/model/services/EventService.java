package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.databaseConfig.EventRepository;
import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvent;
import com.bacnetbrowser.schoko.model.models.BACnetObject;
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

        BACnetObject object = (BACnetObject) remoteDevice.getObject(oid);

        BACnetEvent bacnetEvent = new BACnetEvent(processIdentifier.toString(), remoteDevice.getVendorName(), oid.toString(),
                BACnetTypes.parseToSQLTimeStamp(timeStamp), notificationClass.toString(), priority.toString(), eventType.toString(),
                messageText.toString(), BACnetTypes.getNotifyTypeAsText(notifyType), ackRequired.toString(),
                BACnetTypes.getGermanEventStateText(fromState.toString()),
                BACnetTypes.getGermanEventStateText(toState.toString()), eventValues.toString(),
                object.getDescription(), object.getPresentValueAsText(),
                object.getObjectName());
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

    public void acknowledgeEvent(BACnetEvent bacnetEvent){
        ObjectIdentifier oid = HierarchyService.objectNamesToOids.get(bacnetEvent.getOid());
        RemoteDevice remoteDevice = HierarchyService.obejctNamesToRemoteDevice.get(bacnetEvent.getOid());
        EventState eventState = null;
        EventState[] eventStates = EventState.ALL;
        for(EventState eventStateLook : eventStates){
            if(eventStateLook.equals(bacnetEvent.getToState())) {
                eventState = eventStateLook;
            }
        }
        TimeStamp timeStamp = getTimeStampofObject(oid,remoteDevice,bacnetEvent.getToState());
        TimeStamp timeStampOfAck = new TimeStamp(new DateTime());
        CharacterString ack = new CharacterString("1");
        AcknowledgeAlarmRequest acknowledgeAlarmRequest = new AcknowledgeAlarmRequest(new
                UnsignedInteger(Integer.parseInt(bacnetEvent.getProcessIdentifier())),oid,eventState,timeStamp,ack,timeStampOfAck
                );
        try{
        DeviceService.localDevice.send(remoteDevice,acknowledgeAlarmRequest);
    } catch (BACnetException bac){
        System.err.println("Cant ack " + oid.toString() + " on " + remoteDevice.getVendorName());
        }
    }

    private BACnetEvent createSQLEvent(GetEventInformationAck.EventSummary event, RemoteDevice remoteDevice) {
        BACnetObject object = (BACnetObject) remoteDevice.getObject(event.getObjectIdentifier());
        BACnetObject notiClass = (BACnetObject) remoteDevice.getObject(new ObjectIdentifier(ObjectType.notificationClass, Integer.parseInt(object.getProperty(PropertyIdentifier.notificationClass).toString())));
        TimeStamp timeStamp = getTimeStampofObject(event.getObjectIdentifier(),remoteDevice, event.getEventState().toString());
        BACnetEvent bacnetEvent =  new BACnetEvent("1", remoteDevice.getVendorName(), object.getObjectIdentifier().toString()
                ,BACnetTypes.parseToSQLTimeStamp(timeStamp), object.getProperty(PropertyIdentifier.notificationClass).toString(),notiClass.getPriorityByStatus(event.getEventState())
                , event.getNotifyType().toString(), " ", BACnetTypes.getNotifyTypeAsText(event.getNotifyType()),
                notiClass.getProperty(PropertyIdentifier.ackRequired).toString(), BACnetTypes.getGermanEventStateText(EventState.normal.toString()), BACnetTypes.getGermanEventStateText(event.getEventState().toString()),event.getNotifyType().toString() ,object.getDescription(),
                object.getPresentValueAsText(),object.getObjectName());
        bacnetEvent.setEventID(createNewEventID(remoteDevice.getVendorName(), object.getObjectIdentifier().toString()));
        if (event.getEventState().equals(EventState.normal)){
            bacnetEvent.setVisableInFrontend(false);
        }else {
            bacnetEvent.setVisableInFrontend(true);
        }

        return bacnetEvent;



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



