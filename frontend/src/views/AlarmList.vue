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
    import Stomp from 'stompjs';
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


                connect: function () {
                    const socket = new WebSocket('ws://localhost:8098/ws/events');
                    this.stompClient = Stomp.over(socket);
                    this.stompClient.connect({}, this.callbackStomp);
                },
                connectClose: function () {
                    this.stompClient.send("/app/endEvents", {}, "WebSocket Closed");
                    this.stompClient.unsubscribe('/eventList/events');
                    this.stompClient.disconnect();
                    this.connected = false;
                },
                callbackStomp: function (frame) {
                    if (frame.command === "CONNECTED") {
                        this.stompClient.subscribe('/eventList/events', this.callback, {});
                    } else {
                        console.log("failed")
                    }
                    this.stompClient.send("/app/events", {}, this.bacnetEvents)

                },

                callback: function (message) {
                    this.$store.commit('setBACnetObject', JSON.parse(message.body));
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



            }
    };
</script>

<style scoped>

</style>