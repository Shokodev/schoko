<template>
    <div>
    <h1 class="subtitle is 3">
        Alarmliste
    </h1>
    <div>
            <span>
                <span  class="columns box" :key="child.elementName" v-for="child in bacnetEvents">
                    <div Class="column">Datum: {{child.timeStamp}} </div>
                      <div Class="column">Beschreibung: {{child.description}} </div>
                        <div Class="column">  Aktuller Wert: {{child.presentValue}} </div>
                         <div Class="column"> {{child.fromState}};</div>
                            <div Class="column"> {{child.toState}} </div>
                </span>
            </span>

    </div>
    </div>
</template>

<script>
        export default {
        name: "AlarmList",
            data() {
                return {
                    bacnetEvents: [{
                        "eventID": "5",
                        "oid": "532",
                        "oidRemoteDevice": "Controller Test",
                        "timeStamp": "08:00 25.10.2019",
                        "fromState": "Normal",
                        "toState": "to Fault",
                        "description": "Alles Defekt",
                        "presentValue": "Störung"
                    },
                        {"eventID": "5",
                    "oid": "532",
                    "oidRemoteDevice": "Controller Test",
                    "timeStamp": "07:00 25.10.2019",
                    "fromState": "Normal",
                    "toState": "to Fault",
                    "description": "Alles Defekt",
                    "presentValue": "Störung"
                        }]
                };
            },
            methods:{
            searchPropertyIdentifierValue: function () {
                    for (let i = 0; i < this.bacnetEvents.length; i++) {
                        if (this.bacnetEvents[i].propertyIdentifier) {
                            return this.bacnetEvents[i].value;
                        } else {
                            console.log("Not Found")
                        }
                    }
                }



            }
    };
</script>

<style scoped>

</style>