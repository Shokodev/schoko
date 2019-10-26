package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.datahandler.ObjectHandler;
import com.bacnetbrowser.schoko.model.models.BACnetProperty;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;


@Component
public class ObjectService extends DeviceEventAdapter {

    @Autowired
    private ObjectHandler objectHandler;

    private final LinkedList<BACnetProperty> properties = new LinkedList<>();
    private  RemoteDevice remoteDevice;
    private  ObjectIdentifier objectIdentifier;


    /**
     * gets All properties from a DataPoint by elementTyp
     */
    public void readDataPointProperties(String elementName) {
        RemoteDevice remoteDevice = HierarchyService.obejctNamesToRemoteDevice.get(elementName);
        String elementType = HierarchyService.objectNamesToOids.get(elementName);
        ObjectIdentifier oid = HierarchyService.oidStringToOid.get(elementType);
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
    private void creatProperties(LinkedList<BACnetProperty> properties, RemoteDevice remoteDevice, ObjectIdentifier oid, List<PropertyTypeDefinition> poids)  {

        for (PropertyTypeDefinition op : poids) {
            try {
            ConfirmedRequestService request = new ReadPropertyRequest(oid, op.getPropertyIdentifier());
            ReadPropertyAck result = (ReadPropertyAck) DeviceHandler.localDevice.send(remoteDevice, request);
            BACnetProperty property = new BACnetProperty(result.getValue().toString(),result.getPropertyIdentifier().toString());
            properties.add(property);
            } catch (BACnetException bac){
                System.err.println("Cant read property " + op.getPropertyIdentifier().toString() + " of Object: " + oid.toString());;
            }
        }
    }

    public LinkedList<BACnetProperty> getProperties() {
        return properties;
    }

    /**
     * Start a subscription at remote device with the current object
     */
    public void subscribeToCovRequest() {
        try {
            DeviceHandler.localDevice.send(remoteDevice, new SubscribeCOVRequest(new UnsignedInteger(0), objectIdentifier, new Boolean(true), new UnsignedInteger(0)));
            System.out.println("Subscription @: '" + objectIdentifier + "' on: " + remoteDevice.getObjectIdentifier());
    } catch (BACnetException bac){
        System.err.println("Cant subscribed data point: " + objectIdentifier.getObjectType());
            objectHandler.updateStream();

    }}

    /**
     * Unsubscribe the current subscription at remote device of current object
     */
    public void unsubscribeToCovRequest(){

        try {
            DeviceHandler.localDevice.send(remoteDevice, new SubscribeCOVRequest(new UnsignedInteger(0), objectIdentifier, null, null));
        System.out.println("Unsubscription @: '" + objectIdentifier + "' on: " + remoteDevice.getObjectIdentifier());
    } catch (BACnetException bac){
        System.err.println("Cant unsubscribed data point: " + objectIdentifier.getObjectType());

    }}

    public void clearPropertyList(){
       properties.clear();
    }

    /**
     * Write new value for given object
     * @param poid witch property to write
     * @param value new value for property
     */
    public void writeValue(PropertyIdentifier poid, Encodable value){
        // 8 is default priority for process level commands
        WritePropertyRequest request = new WritePropertyRequest(objectIdentifier, poid, null, value, new UnsignedInteger(8));
        System.out.println("Write on :" + poid.toString() + " with: " + value.toString());
        try {
            DeviceHandler.localDevice.send(remoteDevice, request);
        }catch(BACnetException bac){
            System.err.println("Cant write " + poid.toString() + " at " + objectIdentifier.toString() + " you fool!");
        }
    }

    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    /**
     * Listen at change of value at the given remote device
     */
    @Override
    public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, RemoteDevice initiatingDevice, ObjectIdentifier monitoredObjectIdentifier, UnsignedInteger timeRemaining, SequenceOf<PropertyValue> listOfValues) {


        for (PropertyValue pv : listOfValues){
            for(BACnetProperty pid : getProperties()){
               if(pv.getPropertyIdentifier().toString().equals(pid.getPropertyIdentifier())){
                   pid.setValue(pv.getValue().toString());
                   System.out.println("Device: " + initiatingDevice.getObjectIdentifier() + " has sent new " +pid.getPropertyIdentifier() + ": " + pid.getValue());

               }

            }
        }
    objectHandler.updateStream();
    }

}

