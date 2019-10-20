<template>
    <div class="modal-mask">
        <div class="modal-wrapper">
            <div class="modal-container">
                <article class="media">
                    <div class="media-content">
                        <div class="content">
                            <strong>Datapoint</strong>
                            <ul>
                                <li v-for="object in bacnetObject" :key="object.elementName">
                                   {{ object.propertyIdentifier }} {{object.value}}
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
            },

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