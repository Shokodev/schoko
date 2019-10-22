package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.model.models.BACnetProperties;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVRequest;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.LinkedList;
import java.util.List;

@Component
public class ObjectService extends DeviceEventAdapter {

    @Autowired
    private DeviceHandler deviceHandler;
    @Autowired
    private HierarchyService hierarchyService;
    @Autowired
    private ObjectHandler objectHandler;

    private LinkedList<BACnetProperties> properties;

    private RemoteDevice remoteDevice;
    private ObjectIdentifier objectIdentifier;



    /**
     * gets All properties from a DataPoint by elementTyp
     */
    public void readDataPointProperties(String elementName) {
        LinkedList<BACnetProperties> properties = new LinkedList<>();
        RemoteDevice remoteDevice = hierarchyService.getObejctNamesToRemoteDevice().get(elementName);
        String elementType = hierarchyService.getObjectNamesToOids().get(elementName);
        ObjectIdentifier oid = hierarchyService.getOidStringToOid().get(elementType);
        this.objectIdentifier = oid;
        this.remoteDevice = remoteDevice;
        ObjectType[] objectType = ObjectType.ALL;
        for (ObjectType type : objectType) {
            if (oid.getObjectType().equals(type)) {
                try {
                    List<PropertyTypeDefinition> poids = ObjectProperties.getPropertyTypeDefinitions(type);
                    creatProperties(properties, remoteDevice, oid, poids);
                } catch (Exception e) {
                    List<PropertyTypeDefinition> poids = ObjectProperties.getRequiredPropertyTypeDefinitions(type);
                    creatProperties(properties, remoteDevice, oid, poids);
                }
                this.properties = properties;

            }
        }

    }

    /**
     * Used to create properties for sending to the client
     * @param properties child
     * @param remoteDevice remote device
     * @param oid object identifier
     * @param poids property identifier
     *
     */
    private void creatProperties(LinkedList<BACnetProperties> properties, RemoteDevice remoteDevice, ObjectIdentifier oid, List<PropertyTypeDefinition> poids)  {


        for (PropertyTypeDefinition op : poids) {
            try {
            ConfirmedRequestService request = new ReadPropertyRequest(oid, op.getPropertyIdentifier());
            ReadPropertyAck result = (ReadPropertyAck) deviceHandler.getLocalDevice().send(remoteDevice, request);
            BACnetProperties property = new BACnetProperties(result.getValue().toString(),result.getPropertyIdentifier().toString());
            properties.add(property);
            } catch (BACnetException bac){
                System.err.println("Cant read properties of: " + op.getObjectType());
            }

        }
    }

    public LinkedList<BACnetProperties> getProperties() {
        return properties;
    }

    public String getPresentValue(ObjectIdentifier oid, RemoteDevice remoteDevice){
        ConfirmedRequestService request = new ReadPropertyRequest(oid, PropertyIdentifier.presentValue);
        try{
        ReadPropertyAck result = (ReadPropertyAck) deviceHandler.getLocalDevice().send(remoteDevice, request);
            return result.getValue().toString();
    } catch (BACnetException bac){
        System.out.println("Cant read present value of: " + oid + " @ " + remoteDevice);
        }
      return null;
    }

    public void subscribeToCovRequest() {


        try {
            deviceHandler.getLocalDevice().send(remoteDevice, new SubscribeCOVRequest(new UnsignedInteger(0), objectIdentifier, new Boolean(true), new UnsignedInteger(0)));
            System.out.println("Subscription @: '" + objectIdentifier + "' on: " + remoteDevice.getObjectIdentifier());
    } catch (BACnetException bac){
        System.err.println("Cant subscribed data point: " + objectIdentifier.getObjectType());
    }}

    public void unsubscribeToCovRequest(){

        try {
        deviceHandler.getLocalDevice().send(remoteDevice, new SubscribeCOVRequest(new UnsignedInteger(0), objectIdentifier, null, null));
        System.out.println("Unsubscription @: '" + objectIdentifier + "' on: " + remoteDevice.getObjectIdentifier());
    } catch (BACnetException bac){
        System.err.println("Cant unsubscribed data point: " + objectIdentifier.getObjectType());
    }}

    public void clearPropertyList(){
        properties.clear();
    }

    @Override
    public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, RemoteDevice initiatingDevice, ObjectIdentifier monitoredObjectIdentifier, UnsignedInteger timeRemaining, SequenceOf<PropertyValue> listOfValues) {
        for (PropertyValue pv : listOfValues){
            for(BACnetProperties pid : properties){
               if(pv.getPropertyIdentifier().toString().equals(pid.getPropertyIdentifier())){
                   pid.setValue(pv.getValue().toString());
                   System.out.println("Device: " + initiatingDevice.getObjectIdentifier() + " has sent new " +pid.getPropertyIdentifier() + ": " + pid.getValue());

               }

            }
        }
        System.out.println("Try to update stream... ");
        objectHandler.updateStream();
    }


}

