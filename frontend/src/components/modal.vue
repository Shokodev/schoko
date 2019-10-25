<template>
    <div class="modal-mask">
        <div class="modal-wrapper">
            <div class="modal-container">
                <article class="media">
                    <div class="media-content">
                        <div class="content">
                            <BinaryOutput v-if="this.connected"
                            :node="getBACnetObject"
                            ></BinaryOutput>
                        </div>
                        <pulse-loader :loading=!this.connected :color="color" :size="size"></pulse-loader>
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
    import { mapGetters } from "vuex";
    import PulseLoader from "vue-spinner/src/PulseLoader.vue";

    export default {
        name: "modal",
        data() {
            return{
                stompClient: Object,
                connected: false,
                color: '#2c3e50',
                size: '15px',
               // loading:true
            };
        },
        components: {BinaryOutput, PulseLoader},

        mounted() {
        this.connect();
        },
        methods: {
            connect: function(){
                const socket = new WebSocket('ws://localhost:8098/ws/Object');
                this.stompClient = Stomp.over(socket);
                this.stompClient.connect({},this.callbackStomp);
            },
            connectClose: function(){
                this.stompClient.send("/app/end",{},"WebSocket Closed");
                this.stompClient.unsubscribe('/topic/user');
                this.stompClient.disconnect();
                this.connected = false;
            },
            callbackStomp: function(frame) {
                if(frame.command ==="CONNECTED"){
                    this.stompClient.subscribe('/topic/user', this.callback,{});
                }
                else{ console.log("failed")}
                this.stompClient.send("/app/user",{}, this.$store.getters.getObjectName)
            },
            callback: function(message){
                this.$store.commit('setBACnetObject', JSON.parse(message.body));
                this.connected = true;
            }
        },
        computed: mapGetters (["getBACnetObject"])
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
        width: 600px;
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