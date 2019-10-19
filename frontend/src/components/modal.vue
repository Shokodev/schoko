<template>
    <div class="modal-mask">
        <div class="modal-wrapper">
            <div class="modal-container">
                <article class="media">
                    <div class="media-content">
                        <div class="content">
                                <strong>Datapoint</strong>
                            <ul>
                                <li v-for="test in bacnetObject">
                                   {{ test.propertyIdentifier }} {{test.value}}
                                </li>
                            </ul>
                        </div>
                        <div class="field has-addons has-addons-centered box">
                            <div class="level-left" >
                                <strong>Aktueller Wert</strong>
                            </div>
                            <p class="control level-right">
                                <span class="select">
                                  <select>
                                    <option>Ein</option>
                                    <option>Aus</option>
                                  </select>
                                </span>
                            </p>
                            <p class="control">
                                <a class="button is-primary">
                                    Senden
                                </a>
                            </p>
                        </div>
                    </div>
                </article>
                <button class="button" @click="$emit('close')">
                    OK
                </button>
                <button class="button" @click=load>
                    Manuell laden
                </button>
                <button class="button" @click=connect v-if="status === 'disconnected'">
                    Connect
                </button>
                <button class="button" @click=disconnect v-if="status === 'connected'">
                    {{status}}
                    Disconnect
                </button>
                <button class="button" @click=sendMessage>
                    SendMessage
                </button>


            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "modal",
        props: ['node'],
        data() {
            return{
                bacnetObject: '',
                status: "disconnected"
            }
        },
        mounted() {
            this.bacnetObject = this.node;
        },
        methods:{
        load() {

            console.log(this.node)
        },
        connect() {
            this.socket = new WebSocket("ws://localhost:8080/ws/Object");
            this.socket.onopen = () => {
                this.status = "connected";
                this.logs.push({ event: "Connected to", data: 'ws://localhost:8080/topics/hello'});


                this.socket.onmessage = ({data}) => {
                    this.logs.push({ event: "Recieved message", data });
                };
            };
        },
        disconnect() {
            this.socket.close();
            this.status = "disconnected";
            this.logs = [];
        },
        sendMessage(e) {
            this.socket.send(this.message);
            this.logs.push({ event: "Sent message", data: this.message });
            this.message = "";
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