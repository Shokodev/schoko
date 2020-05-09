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

URL GETTERS 
```
/settings
--------------
Respon Example
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
Careful! This creats a BACnet network 
Respon Example
--------------
{
   
}
```
```
/hierarchy
-------------- 
Whole BACnet structure
Respon Example
--------------
{
}                     
```

