# schoko

## Project setup
```
mvn clean install
```

### Run the Project 
```
mvn spring-boot:run
```
### Create production build
```
mvn clean package
```
-----------------------------------------------------------------------------
## Description of the API

###### The Applications is running on port: **http://127.0.0.1:8098**

###### All traffic restricted to JSON format!

**URL GETTERS** 
----------------------
```
/settings
```
Get the saved settings in the application.properties resources

_Respon Example (Settings objects)_

```
{
    "bacnetSeparator":"'",
    "localDeviceID":"1001",
    "precisionRealValue":2,
    "scanSeconds":5,
    "port":"BAC0"
}
```

----------------------

```
/devices
```

Careful! This creates a BACnet network config and will overwrite it if one exits.
After that it scans for devices.. each received device will be checked if its already 
in the final view, to set the "alreadyImported" property. Then you will receive a list 
with all imported and not imported devices of the given BACnet network.  

And be aware of the time you set in settings for "scanSeconds"... 
you will only receive the response afterwards! 

_Respon Example (ArrayList with device objects)_
```
[
    {
        "alreadyImported":true,
        "name":"Site01'AS01",
        "modelName":"PXC100-E.D / HW=V3.00",
        "description":"192.168.1.177:47808",
        "instanceNumber":2098177
    }
]
```

----------------------

```
/hierarchy
```

Get the whole BACnet structure but you will only receive somthing 
if a BACnet network exists!

_Respon Example(Nested object)_
```
{
    "name": "Site01",
    "objectIdentifier": "Top node bacnet structure",
    "description": "Site",
    "children": [
        {
            "name": "B",
            "objectIdentifier": "Structure Element",
            "description": "Gebäude",
            "children": [
                {
                    "name": "C",
                    "objectIdentifier": "Structure Element",
                    "description": "Kühlung & Kälte",
                    "children": [
                        {
                            "name": "CGrp",
                            "objectIdentifier": "Structure Element",
                            "description": "Kältekreis",
                            "children": [
                                {
                                    "name": "MxCrt",
                                    "objectIdentifier": "Structure Element",
                                    "description": "Mischkreis",
                                    "children": [
                                        {
                                            "name": "Vlv",
                                            "objectIdentifier": "analog-output",
                                            "description": "Ventil",
                                            "children": null,
                                            "objectName": "B'C'CGrp'MxCrt'Vlv"
                                        },           


            ...
          
```

----------------------

```
/logicalview
```

Get all objects sorted by devices and object types but you will only receive somthing 
if a BACnet network exists!

_Respone Example(Nested object)_

```
{
    "name": "Site01",
    "objectIdentifier": "Top node devices",
    "description": "Alle BACnet Geräte und ihre Objekte",
    "children": [
        {
            "name": "Site01-AS01",
            "objectIdentifier": "Structure Element",
            "description": "Device",
            "children": [
                {
                    "name": "analog-input",
                    "objectIdentifier": "Structure Element",
                    "description": "analog-input",
                    "children": [
                        {
                            "name": "Objekt",
                            "objectIdentifier": "analog-input 1",
                            "description": "Aussentemp. Wetterstation",
                            "children": null,
                            "objectName": "B'A'Ahu'TOaWs"
                        },
                        {
                            "name": "Objekt",
                            "objectIdentifier": "analog-input 2",
                            "description": "Vorlauftemperatur",
                            "children": null,
                            "objectName": "B'C'CGrp'MxCrt'TFl"
                        },

        ...
               
```

----------------------

```
/structure/{objectName}
```

Get just one "Node" object with his children of the nested tree object "BACnet structure".

- > TODO, function is not satisfiy!
Object name has to be like: Anlage'B'H'HGrp'Hcrv.SpTFl
That means that the object name has to start with the costum sitenam

_Respon Example (Node object with his children)_

```
{
    "name": "HGrp",
    "objectIdentifier": "Structure Element",
    "description": "Heizkreis",
    "children": [
        {
            "name": "Hra.TOaEf",
            "objectIdentifier": "analog-value",
            "description": "Wirksame Aussentemperatur",
            "children": null,
            "objectName": "B'H'HGrp'Hra.TOaEf"
        },
        {
            "name": "Hra.ShHLmWds",
            "objectIdentifier": "analog-value",
            "description": "Hebg.H-Grenze nom.Windge.",
            "children": null,
            "objectName": "B'H'HGrp'Hra.ShHLmWds"
        },
        {
            "name": "Hcrv.SpTFlDs",
            "objectIdentifier": "analog-value",
            "description": "VLT Sollw.Auslegungs-AT",
            "children": null,
            "objectName": "B'H'HGrp'Hcrv.SpTFlDs"
        },
        {
            "name": "Hcrv.SpTFl",
            "objectIdentifier": "analog-value",
            "description": "Vorlauftemperatursollwert",
            "children": null,
            "objectName": "B'H'HGrp'Hcrv.SpTFl"
        },
        {
            "name": "Hra.HLmCmf",
            "objectIdentifier": "analog-value",
            "description": "Heizgrenze Comfort",
            "children": null,
            "objectName": "B'H'HGrp'Hra.HLmCmf"
        },
```

--------------

**URL POSTS**
```
/settings
```

Save settings in the application.properties recources

_Post Example (Settings object)_
```
{
    "port": "BAC0",
    "precisionRealValue": 2,
    "bacnetSeparator": "'",
    "localDeviceID": "1001",
    "scanSeconds": 5
}
```

--------------

```
/devices
```

The received devices will be send to the final "bacnet devices list" (existing devices will be overwritten) 
A function is going to delete the consumer(localDevice) out  of all device which are no longer in 
the "final bacnet devices list".
With the new device list following steps are going to be taken:

    -> Create BACnet structure and logical view structure
    -> Create event DB if it does not exist yet
    -> Create event stream (register localdevice as alarm receiver)
    -> Ask all devcies for current event information  

_Post Example (ArrayList with device objects)_
```
[
    {
        "name": "Site01'AS01",
        "modelName": "PXC100-E.D / HW=V3.00",
        "ipAddress": "[c0,a8,1,b1,ba,c0]",
        "instanceNumber": 2098177
    }
]
```
## WEBSOCKET
There is one Socket implemented on the endpoint: `/ws` with the following config.

```


@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/broker");
        config.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }
```
You can open different channels, see below:

**Open a new object channel**
```
/broker/{objectName}
```
The objectName must be as destination variable in the URL!

    -> Imidiantly receive a list with properties (current state)
    -> Creates Subscription on BACnet for that object 
    -> You will be imformed about changes of 'present-value' 
    -> Multible channels can be opened 
    
*Send values to opened object channel*
```
/broker/setValue/{objectName}
```

    -> Parameter: {prpertyidentifier: xy, value: xy}

*Release set value in opened object channel*
```
/broker/release/{objectName}
```

 *close object channel*
 ```
 /broker/end/{objectName}
 ```

**Create event channel(Listener)**
```
/broker/getEvents
```

*Acknowledge an event*
```
/broker/ack/{objectName}
```

The objectName must be as destination variable in the URL!
