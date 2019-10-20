package com.bacnetbrowser.schoko.model.services;

import com.bacnetbrowser.schoko.model.datahandler.DeviceHandler;
import com.bacnetbrowser.schoko.model.models.BACnetProperties;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ObjectService {
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private DeviceHandler deviceHandler;
    @Autowired
    private HierarchyService hierarchyService;

    private LinkedList<BACnetProperties> properties;


    /**
     * gets All properties from a DataPoint by elementType
     *
     * @throws BACnetException from Network
     */
    public void readDataPointProperties(String elementName) throws BACnetException {
        LinkedList<BACnetProperties> properties = new LinkedList<>();
        RemoteDevice remoteDevice = hierarchyService.getObejctNamesToRemoteDevice().get(elementName);
        String elementType = hierarchyService.getObjectNamesToOids().get(elementName);
        ObjectIdentifier oid = hierarchyService.getOidStringToOid().get(elementType);
        try {
            subscriptionService.subscribeToCovRequest(remoteDevice, oid);
        } catch (Exception e){
            System.out.println("Cant subscribed data point: " + elementType);
        }
        ObjectType[] objectType = ObjectType.ALL;
        for (int i = 0; i < objectType.length ; i++) {
            if (elementType.contains(objectType[i].toString())) {
                try {
                    List<PropertyTypeDefinition> poids = ObjectProperties.getPropertyTypeDefinitions(objectType[i]);
                    creatPropertyList(properties, remoteDevice, oid, poids);
                } catch (Exception e) {
                    List<PropertyTypeDefinition> poids = ObjectProperties.getRequiredPropertyTypeDefinitions(objectType[i]);
                    creatPropertyList(properties, remoteDevice, oid, poids);
                }
                this.properties = properties;

            }}


    }

    /**
     * Used to create children for sending to the client
     * @param properties child
     * @param remoteDevice remote device
     * @param oid object identifier
     * @param poids property identifier
     * @throws BACnetException from Network
     */
    private void creatPropertyList(LinkedList<BACnetProperties> properties, RemoteDevice remoteDevice, ObjectIdentifier oid, List<PropertyTypeDefinition> poids) throws BACnetException {
        for (PropertyTypeDefinition op : poids) {
            ConfirmedRequestService request = new ReadPropertyRequest(oid, op.getPropertyIdentifier());
            ReadPropertyAck result = (ReadPropertyAck) deviceHandler.getLocalDevice().send(remoteDevice, request);
            BACnetProperties property = new BACnetProperties(result.getValue().toString(),result.getPropertyIdentifier().toString());
            properties.add(property);
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

}
