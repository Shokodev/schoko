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

All traffic restricted to JSON format! 

URL GETTERS 
```
/settings
--------------
Get the saved settings in the application.properties recources

Respon Example (Settings objects)
--------------
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
```
/devices
--------------
Careful! This creats a BACnet network and scans for devices.
If there is a existing network with devices, it will be overwritten.
An be aware of the time you set in settings for "scanSeconds"... 
you will only receive the response afterwards! 

Respon Example (ArrayList with device objects)
--------------
[
    {
        "name": "Site01'AS01",
        "modelName": "PXC100-E.D / HW=V3.00",
        "ipAddress": "[c0,a8,1,b1,ba,c0]",
        "instanceNumber": 2098177
    }
]
```

```
/hierarchy
-------------- 
Gets the whole BACnet structure but you will only receive somthing 
if a BACnet network exists!

Respon Example(Nested object)
--------------
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
```
/logicalview
-------------- 
Gets the all objects sorted by devices and object types but you will only receive somthing 
if a BACnet network exists!

Respon Example(Nested object)
--------------
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
```
/structure/{objectName}
--------------
Get just one "Node" object with his children of the nested tree object "BACnet structure".

-> TODO, function is not satisfiy!
Object name has to be like: Anlage'B'H'HGrp'Hcrv.SpTFl
That means that the object name has to start with the costum sitenam

Respon Example (Node object with his children)
--------------
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
