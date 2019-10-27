<template>
    <div>
        <p class="subtitle is 3">Einstellungen</p>
        <div class="modal-mask" v-if="this.getSyncSettings">
            <div class="modal-wrapper">
                <div class="modal-container">
            <pulse-loader :loading=this.getSyncSettings :color="color" :size="size"></pulse-loader>
                </div>
            </div>
        </div>
        <div class="control">
            <div class="field">
                <label class="label">Site Name</label>
                <div class="control">
                    <input v-model="settings.siteName"  class="input" type="text">
                </div>
                <p class="help">Site Namen eingeben</p>
            </div>
            <div class="field">
                <label class="label">Site Beschreibung</label>
                <div class="control">
                    <input v-model="settings.siteDescription"  class="input" type="text">
                </div>
                <p class="help">Site Description eingeben</p>
            </div>
            <div class="field">
                <label class="label">BACnet Separator</label>
                <div class="control">
                    <input v-model="settings.bacnetSeparator"  class="input" type="text" >
                </div>
                <p class="help">BACnet Separator eingeben</p>
            </div>
            <div class="control">
            <label class="label">BACnet Port</label>
                <div class="select">
                    <select v-model="settings.port">
                        <option>BAC0</option>
                        <option>BAC1</option>
                        <option>BAC2</option>
                        <option>BAC3</option>
                        <option>BAC4</option>
                        <option>BAC5</option>
                        <option>BAC6</option>
                        <option>BAC7</option>
                        <option>BAC8</option>
                        <option>BAC9</option>
                        <option>BACA</option>
                        <option>BACB</option>
                        <option>BACC</option>
                        <option>BACD</option>
                        <option>BACE</option>
                        <option>BACF</option>
                    </select>
                </div>
                <p class="help">BACnet port auswählen</p>
            </div>
            <div class="field is-grouped">
                <div class="control">
                    <button @click="SetSettings()" class="button is-link">Speichern</button>
                </div>
                <div class="control">
                    <button  class="button is-text">Abbrechen</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { mapGetters, mapActions} from "vuex";
    import PulseLoader from "vue-spinner/src/PacmanLoader";

    export default {
        name: "Settings",
        data() {
            return {
                settings: {
                    siteName: "DefaultSite",
                    port: Number,
                    siteDescription: "Description",
                    bacnetSeparator: "'",
                },
                color: '#2c3e50',
                size: '25px',
            }
        },
        components: {PulseLoader},
        computed: {
            ...mapGetters(["getSettings", "getSyncSettings"]),
        },methods: {
            ...mapActions(["newSettings"]),


            SetSettings() {
                this.newSettings(this.settings);
                console.log(this.settings);
                console.log("send")
            }
        },
        mounted() {
            this.settings.siteDescription = this.getSettings.siteDescription;
            this.settings.siteName = this.getSettings.siteName;
            this.settings.port = this.getSettings.port;
            this.settings.bacnetSeparator = this.getSettings.bacnetSeparator;
        }
    }
</script>

<style scoped>
    .input {
        background-color: white;
        border-color: #dbdbdb;
        border-radius: 4px;
        color: #363636;
        box-shadow: inset 0 1px 2px rgba(10, 10, 10, 0.1);
        max-width: 100%;
        width: 100%;
    }
    .input{

    }
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
        width: 100px;
        margin: 0px auto;
        padding: 20px 30px;
        background-color: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, .33);
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