<template>
    <div>
    <h1 class="subtitle is 3">
        Alarmliste
    </h1>
    <div>
            <span>
                <span  class="columns box is-three-fifths" :key="child.eventID" v-for="child in getEvents">
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
    import Stomp from 'stompjs';
    import {mapGetters,mapMutations} from 'vuex';
        export default {
        name: "AlarmList",
            data() {
                return {
                    connected: false,
                    stompClient: Object,

                    bacnetEvents: null,
                };
            },
            mounted(){
                this.connect();
            },
            methods:{

            ...mapMutations(["newEventList"]),

                connect: function () {
                    const socket = new WebSocket('ws://localhost:8098/ws/events');
                    this.stompClient = Stomp.over(socket);
                    this.stompClient.connect({}, this.callbackStomp);
                },
                connectClose: function () {
                    this.stompClient.send("/app/endEvents", {}, "WebSocket Closed");
                    this.stompClient.unsubscribe('/broker/eventSub');
                    this.stompClient.disconnect();
                    this.connected = false;
                },
                callbackStomp: function (frame) {
                    if (frame.command === "CONNECTED") {
                        this.stompClient.subscribe('/broker/eventSub', this.callback, {});
                    } else {
                        console.log("failed")
                    }
                },

                callback: function (message) {
                  this.newEventList(JSON.parse(message.body));
                    this.connected = true;
                },
                //ToDo Alarm Handling
                sendValue: function () {
                    this.stompClient.send("", {}, JSON.stringify());
                },


            searchPropertyIdentifierValue: function () {
                    for (let i = 0; i < this.bacnetEvents.length; i++) {
                        if (this.bacnetEvents[i].propertyIdentifier) {
                            return this.bacnetEvents[i].value;
                        } else {
                            console.log("Not Found")
                        }
                    }
                }
            },

            computed:{
            ...mapGetters(["getEvents"])
            }
    };
</script>

<style scoped>






</style>