# schoko

## Project setup
```
mvn clean install
```

### Run the Project 
```
mvn --projects backend spring-boot:run
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
Get the saved settings in the application.properties recources

_Respon Example (Settings objects)_

```
{
    "port": "BAC0",
    "precisionRealValue": 2,
    "siteDescription": "Site",
    "siteName": "Anlage",
    "bacnetSeparator": "'",
    "localDeviceID": "1001",
    "scanSeconds": 5
}
```

----------------------

```
/devices
```

Careful! This creats a BACnet network and scans for devices.
If there is a existing network with devices, it will be overwritten.
An be aware of the time you set in settings for "scanSeconds"... 
you will only receive the response afterwards! 

_Respon Example (ArrayList with device objects)_
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
    "siteDescription": "Site",
    "siteName": "Anlage",
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
With the new device list following steps are going to be taken:

    -> Create BACnet structure and logical view structure
    -> Create event DB if it does not exist yet
    -> Create event stream
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
```
There is one Socket implemented on the endpoint > /ws < with the following config

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
coming soon
```
