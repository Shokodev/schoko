<template>
    <div class="modal-mask">
        <div class="modal-wrapper">
            <div class="modal-container">
                <article class="media">
                    <div class="media-content" >
                        <div class="content" v-if="this.getIsConnected">
                            <BinaryOutput @event="sendValue" v-if=isBinary
                            :node="this.getBACnetObject"
                            ></BinaryOutput>
                            <Analog @event="sendValue" v-if=isAnalog
                            :node="this.getBACnetObject"
                            ></Analog>
                            <Multistate @event="sendValue" v-if=isMultiState
                            :node="this.getBACnetObject"
                            ></Multistate>
                        </div>
                        <pulse-loader :loading=!this.getIsConnected :color="color" :size="size"></pulse-loader>
                    </div>
                </article>
                <button class="button" v-on:click="connectClose" @click="$emit('close')">
                    OK
                </button>
            </div>
        </div>
    </div>
</template>
<script>
    import Stomp from 'stompjs';
    import BinaryOutput from "./bacnetobject/BinaryOutput";
    import Analog from "./bacnetobject/Analog";
    import { mapGetters, mapMutations, mapActions} from "vuex";
    import PulseLoader from "vue-spinner/src/PulseLoader.vue";
    import Multistate from "./bacnetobject/Multistate";

    export default {
        name: "BACnetObjectModal",
        data() {
            return {
                stompClient: Object,
                connected: false,
                color: '#2c3e50',
                size: '15px',
                myObject: null,
            };
        },
        components: {Multistate, Analog, BinaryOutput, PulseLoader},

        mounted() {
            this.connect();
        },
        methods: {
            ...mapActions(["connect"]),
            ...mapMutations(["setIsConnected"]),

            // This Function creates the websocket when the component gets mounted
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            connect: function () {
                const socket = new WebSocket('ws://localhost:8098/ws/objects');
                this.stompClient = Stomp.over(socket);
                this.stompClient.connect({}, this.callbackStomp);
            },
            // This Function closes the websocket
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            connectClose: function () {
                this.stompClient.send("/app/end", {}, "WebSocket Closed");
                this.stompClient.unsubscribe('/broker/objectSub');
                this.stompClient.disconnect();
                this.setIsConnected(false);
            },
            // This Function gets called by the stompclient and passes the frame, if the websocket is connected the application subscribes to the
            // opened bacnet object.
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            callbackStomp: function (frame) {
                if (frame.command === "CONNECTED") {
                    this.stompClient.subscribe('/broker/objectSub', this.callback, {});
                }
                this.stompClient.send("/app/objectSub", {}, this.$store.getters.getObjectName)

            },
            // This Function gets called by the sompclient and passes the massage that was send by the server.
            // The body over the message gets parsed as a JSON for easier transformation.
            // The value setIsConnected is changed to true and the information gets displayed in the web
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            callback: function (message) {
                this.$store.commit('setBACnetObject', JSON.parse(message.body));
                this.setIsConnected(true);

            },
            // This Function gets called from the child components with an $emit
            // A new value gets send over the stompclient to the backend server
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            sendValue: function (value) {
                if(value !== "release") {
                    this.stompClient.send("/app/setValue", {}, JSON.stringify(this.getBACnetProperty));
                }else{
                this.stompClient.send("/app/releaseValue", {}, JSON.stringify("object is released"));
            }},

        },
        computed: {
            ...mapGetters(["getBACnetObject", "getBACnetProperty","getObjectType", "getIsConnected"]),
            // This computed method if the bacnet object type is from type Binary
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            isBinary: function () {
                return ['Binary Output', 'Binary Value', 'Binary Input'].indexOf(this.getObjectType) >= 0;

            },
            // This computed method if the bacnet object type is from type Analog
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            isAnalog: function () {
                return ['Analog Output', 'Analog Value', 'Analog Input'].indexOf(this.getObjectType) >= 0;
                // This computed method if the bacnet object type is from type Multi-state
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            },
            isMultiState: function () {
                return ['Multi-state Value', 'Multi-state Output', 'Multi-state Input'].indexOf(this.getObjectType) >= 0;

            },

        }
    }
</script>

<style>
    .modal-mask {
        position: fixed;
        z-index: 9998;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, .5);
        display: table;
        transition: opacity .3s ease;
    }

    .modal-wrapper {
        display: table-cell;
        vertical-align: middle;
    }

    .modal-container {
        width:700px;
        margin: 0px auto;
        padding: 20px 30px;
        background-color: #fff;
        border-radius: 2px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
        transition: all .3s ease;
        font-family: Helvetica, Arial, sans-serif;
    }
    .modal-header h3 {
    }
    .modal-enter .modal-container,
    .modal-leave-active .modal-container {
        -webkit-transform: scale(1.1);
        transform: scale(1.1);
    }
</style>