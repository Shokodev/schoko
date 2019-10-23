<template>
<span>
    <span>
        <span v-for="child in bacnetObject" :key="child.propertyIdentifier">
            <span class="box level" v-if="isPresentValue(child)">
                <span class="level-left">
                    <span>
                        Aktueller Wert:
                    </span>
                    <span>
                        {{child.value}}
                    </span>
                </span>
                <span class="level-right">
                <span class="select">
                    <select>
                        <option>{{inactiveValue}}</option>
                        <option>{{activeValue}}</option>
                    </select>
                </span>
                 <a class="button is-primary" v-on:click="objectName">
                    Senden
                 </a>
                </span>
            </span>
            <span class="box" v-if="isObjectName(child)">
                <span>
                    Objekt Name :
                </span>
                <span>
                    {{child.value}}
                </span>
            </span>
            <span class="box" v-if="isObjectType(child)">
                <span>
                    Objekt Typ :
                </span>
                <span>
                    {{child.value}}
                </span>
            </span>
            <span class="box" v-if="isDescription(child)">
                <span>
                    Beschreibung :
                </span>
                <span>
                    {{child.value}}
                </span>
            </span>
            <span class="box" v-if="isOutOfService(child)">
                <span>
                    Ausser Betrieb :
                </span>
                <span>
                    {{child.value}}
                </span>
            </span>
            <span class="box" v-if="isPolarity(child)">
                <span>
                    Polarität :
                </span>
                <span>
                    {{child.value}}
                </span>
            </span>
        </span>
    </span>


</span>
</template>

<script>
    export default {
        name: "BinaryOutput",
        data(){
            return{
                bacnetObject:[{"value":"Binary Output 5","propertyIdentifier":"Object identifier"},{"value":"B'A'Ahu'FanSu'Cmd","propertyIdentifier":"Object name"},{"value":"Binary Output","propertyIdentifier":"Object type"},{"value":"0","propertyIdentifier":"Present value"},{"value":"Befehl","propertyIdentifier":"Description"},{"value":"","propertyIdentifier":"Device type"},{"value":"[true, true, false, false]","propertyIdentifier":"Status flags"},{"value":"fault","propertyIdentifier":"Event state"},{"value":"66","propertyIdentifier":"Reliability"},{"value":"false","propertyIdentifier":"Out of service"},{"value":"0","propertyIdentifier":"Polarity"},{"value":"Aus","propertyIdentifier":"Inactive text"},{"value":"Ein","propertyIdentifier":"Active text"},{"value":"Encodable(com.serotonin.bacnet4j.type.constructed.DateTime)","propertyIdentifier":"Change of state time"},{"value":"0","propertyIdentifier":"Change of state count"},{"value":"Encodable(com.serotonin.bacnet4j.type.constructed.DateTime)","propertyIdentifier":"Time of state count reset"},{"value":"0","propertyIdentifier":"Elapsed active time"},{"value":"Encodable(com.serotonin.bacnet4j.type.constructed.DateTime)","propertyIdentifier":"Time of active time reset"},{"value":"0","propertyIdentifier":"Minimum off time"},{"value":"0","propertyIdentifier":"Minimum on time"},{"value":"[PriorityValue(binaryValue=0), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(binaryValue=0), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(nullValue=Null), PriorityValue(binaryValue=0)]","propertyIdentifier":"Priority array"},{"value":"0","propertyIdentifier":"Relinquish default"},{"value":"5","propertyIdentifier":"Time delay"},{"value":"23","propertyIdentifier":"Notification class"},{"value":"0","propertyIdentifier":"Feedback value"},{"value":"[true, true, true]","propertyIdentifier":"Event enable"},{"value":"[true, false, true]","propertyIdentifier":"Acked transitions"},{"value":"0","propertyIdentifier":"Notify type"},{"value":"[Encodable(com.serotonin.bacnet4j.type.constructed.TimeStamp), Encodable(com.serotonin.bacnet4j.type.constructed.TimeStamp), Encodable(com.serotonin.bacnet4j.type.constructed.TimeStamp)]","propertyIdentifier":"Event time stamps"},{"value":"7-BA-PX-BO-SBCv11.01","propertyIdentifier":"Profile name"}]
                ,inactiveValue:"",
                activeValue:""
            };
        },
        mounted(){
            this.objectName()
        },
        methods:{
            objectName: function () {
                for (let i = 0; i < this.bacnetObject.length; i++) {
                   if(this.bacnetObject[i].propertyIdentifier===("Inactive text")){
                       console.log(this.inactiveValue);
                       this.inactiveValue = this.bacnetObject[i].value
                   } else if(this.bacnetObject[i].propertyIdentifier===("Active text")){
                       console.log(this.activeValue);
                       this.activeValue = this.bacnetObject[i].value
                   }
                }
            },
            isPresentValue(mychild) {
                return mychild["propertyIdentifier"] === "Present value";
            },
            isObjectName(mychild) {
                return mychild["propertyIdentifier"] === "Object name";
            },
            isObjectType(mychild) {
                return mychild["propertyIdentifier"] === "Object type";
            },
            isDescription(mychild) {
                return mychild["propertyIdentifier"] === "Description";
            },
            isOutOfService(mychild) {
                return mychild["propertyIdentifier"] === "Out of service";
            },
            isPolarity(mychild) {
                return mychild["propertyIdentifier"] === "Polarity";
            }

        }

    }
</script>

<style scoped>
#value{
    padding-left: 10em;
}
</style>