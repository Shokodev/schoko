package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.datahandler.EventHandler;
import com.bacnetbrowser.schoko.model.models.BACnetEvents;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest;
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
import org.springframework.stereotype.Component;


@Component
public class EventService extends DeviceEventAdapter {

    @Autowired
    DeviceHandler deviceHandler;

    @Autowired
    HierarchyService hierarchyService;

    @Autowired
    EventHandler eventHandler;

    @Override
    public void iAmReceived(RemoteDevice d)  {
        int ID = d.getInstanceNumber();
        System.out.println("Remote device " + ID + " registered at LocalDevice");
    }

    @Override
    public void eventNotificationReceived(UnsignedInteger processIdentifier, RemoteDevice initiatingDevice, ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp, UnsignedInteger notificationClass, UnsignedInteger priority, EventType eventType, CharacterString messageText, NotifyType notifyType, Boolean ackRequired, EventState fromState, EventState toState, NotificationParameters eventValues) throws BACnetException {

        System.out.println("Event notification received!!");
        System.out.println("***************************************************************************************************************");
        System.out.println("initiatingDevice: " + initiatingDevice);
        System.out.println("processIdentifier: " + processIdentifier);
        System.out.println("eventObjectIdentifier: " + eventObjectIdentifier);
        System.out.println("timeStamp: " + timeStamp.getDateTime());
        System.out.println("notificationClass: " + notificationClass);
        System.out.println("priority: " + priority);
        System.out.println("eventType: " + eventType);
        System.out.println("messageText: " + messageText);
        System.out.println("notifyType: " + notifyType);
        System.out.println("ackRequired: " + ackRequired);
        System.out.println("fromState: " + fromState);
        System.out.println("toState: " + toState);
        System.out.println("eventValues: " + eventValues.toString());
        System.out.println("***************************************************************************************************************");

        BACnetEvents baCnetEvent = new BACnetEvents(processIdentifier.intValue(),eventObjectIdentifier.toString(),initiatingDevice.getObjectIdentifier().getInstanceNumber(),timeStamp.toString(),notificationClass.intValue(),priority.intValue(),getEventTyp(eventType),messageText.toString(),notifyType.toString(),ackRequired.toString(),fromState.toString(),toState.toString(),"NA","dsd","sdsd"
        );
        eventHandler.addEvent(baCnetEvent);

    }


    private String getEventTyp (EventType eventType){



        return "Unknown Typ";
    }


    private String getEventValue (EventParameter eventValues){


        return "Unknown Typ";
    }




    @Override
    public void reinitializeDevice(ReinitializeDeviceRequest.ReinitializedStateOfDevice reinitializedStateOfDevice) {
        System.out.println("Reinitialize Device:" + reinitializedStateOfDevice);
    }




}
