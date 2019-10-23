<template>
    <div class="modal-mask">
        <div class="modal-wrapper">
            <div class="modal-container">
                <article class="media">
                    <div class="media-content">
                        <div class="content">
                              <binary-output>

                              </binary-output>
                        </div>
                    </div>
                </article>
                <button class="button" @click="$emit('close')">
                    OK
                </button>
                <button class="button" v-on:click="connect">
                    Connect
                </button>
            </div>
        </div>
    </div>
</template>

<script>
    let stompClient=null;
    import Stomp from 'stompjs';
    import BinaryOutput from "./bacnetobject/BinaryOutput";
    export default {
        name: "modal",
        props: ['node'],
        components:{BinaryOutput},
        data() {
            return{
                status: "disconnected",

        }
        },
        mounted() {

        },
        methods: {
                connect: function(){
                    const socket = new WebSocket('ws://localhost:8098/ws/Object/websocket');
                    stompClient = Stomp.over(socket);
                    stompClient.connect({}, function (frame) {
                        console.log('Connected: ' + frame);
                        stompClient.subscribe('/topic/user', console.log(String.body))

                    })
    }
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