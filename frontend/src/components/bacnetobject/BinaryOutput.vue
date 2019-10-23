<template>
<span>

            <span class="level box">
                    <span class="level-left">
                    <span>Aktueller Wert: {{this.presentValueValue}}</span>
                    </span>
                    <span class="level-right">
                  <span class="select">
                    <select>
                        <option>{{this.inactiveValue}}</option>
                        <option>{{this.activeValue}}</option>
                    </select>
                  </span>
                    <a class="button is-primary">
                         Senden
                     </a>
                    </span>
            </span>
            <div class="box">
                    <span>
                        Polarität:
                    </span>
                    <span>
                        {{this.polarityValue}}
                    </span>
            </div>
            <div class="box">
                    <span>
                        Beschreibung:
                    </span>
                    <span>
                        {{this.descriptionValue}}
                    </span>
            </div>
            <div class="box">
                    <span>
                        Ausser Betrieb:
                    </span>
                    <span>
                        {{this.outOfServiceValue}}
                    </span>
            </div>
            <div class="box">
                    <span>
                        Objekt Name:
                    </span>
                    <span>
                        {{this.objectNameValue}}
                    </span>
            </div>
</span>


</template>

<script>
    export default {
        name: "BinaryOutput",
        data(){
            return{
                bacnetObject:[{"value":"Binary Output 5","propertyIdentifier":"Object identifier"},{"value":"B'A'Ahu'FanSu'Cmd","propertyIdentifier":"Object name"},{"value":"Binary Output","propertyIdentifier":"Object type"},{"value":"0","propertyIdentifier":"Present value"},{"value":"Befehl","propertyIdentifier":"Description"},{"value":"","propertyIdentifier":"Device type"},{"value":"[true, true, false, false]","propertyIdentifier":"Status flags"},{"value":"fault","propertyIdentifier":"Event state"},{"value":"66","propertyIdentifier":"Reliability"},{"value":"false","propertyIdentifier":"Out of service"},{"value":"0","propertyIdentifier":"Polarity"},{"value":"Aus","propertyIdentifier":"Inactive text"},{"value":"Ein","propertyIdentifier":"Active text"},{"value":"Encodable(com.serotonin.bacnet4j.type.constructed.DateTime)","propertyIdentifier":"Change of state time"},{"value":"0","propertyIdentifier":"Change of state count"},{"value":"Encodable(com.serotonin.bacnet4j.type.constructed.DateTime)","propertyIdentifier":"Time of state count reset"},{"value":"0","propertyIdentifier":"Elapsed active time"},{"value":"Encodable(com.serotonin.bacnet4j.type.constructed.DateTime)","propertyIdentifier":"Time of active time reset"},{"value":"0","propertyIdentifier":"Minimum off time"},{"value":"0","propertyIdentifier":"Minimum on time"},{"value":"[PriorityValue(binaryValue=0), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(binaryValue=0), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(binaryValue=0)]","propertyIdentifier":"Priority array"},{"value":"0","propertyIdentifier":"Relinquish default"},{"value":"5","propertyIdentifier":"Time delay"},{"value":"23","propertyIdentifier":"Notification class"},{"value":"0","propertyIdentifier":"Feedback value"},{"value":"[true, true, true]","propertyIdentifier":"Event enable"},{"value":"[true, false, true]","propertyIdentifier":"Acked transitions"},{"value":"0","propertyIdentifier":"Notify type"},{"value":"[Encodable(com.serotonin.bacnet4j.type.constructed.TimeStamp), Encodable(com.serotonin.bacnet4j.type.constructed.TimeStamp), Encodable(com.serotonin.bacnet4j.type.constructed.TimeStamp)]","propertyIdentifier":"Event time stamps"},{"value":"7-BA-PX-BO-SBCv11.01","propertyIdentifier":"Profile name"}]
                ,inactiveValue:"",
                activeValue:"",
                objectNameValue:"",
                presentValueValue:"",
                objectTypeValue:"",
                descriptionValue:"",
                outOfServiceValue:"",
                polarityValue:"",
            };
        },
        mounted(){

                this.presentValue(),
                this.outOfService(),
                    this.description(),
            this.objectName()
        },
        methods:{
            objectName: function () {
                this.objectNameValue = this.searchPropertyIdentifierValue("Object name")
            },
            presentValue: function () {
                this.presentValueValue = this.searchPropertyIdentifierValue("Present value")
                this.inactiveValue = this.searchPropertyIdentifierValue("Inactive text")
                this.activeValue = this.searchPropertyIdentifierValue("Active text")
                this.polarityValue = this.searchPropertyIdentifierValue("Polarity")
                if(this.presentValueValue==="0"){
                    return this.presentValueValue = this.inactiveValue
                }else
                    return this.presentValueValue = this.activeValue
            },
            description: function () {
                this.descriptionValue = this.searchPropertyIdentifierValue("Description")

            },
            outOfService: function () {
                this.outOfServiceValue = this.searchPropertyIdentifierValue("Out of service")
            },
            searchPropertyIdentifierValue: function (search) {
                for (let i = 0; i < this.bacnetObject.length; i++) {
                    if (this.bacnetObject[i].propertyIdentifier === (search)) {
                        return this.bacnetObject[i].value;
                    } else {
                        console.log("Not Found")
                    }
                }
            }

        }

    }
</script>

<style scoped>

</style>