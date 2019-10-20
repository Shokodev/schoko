<template>
    <div>
        <p class="title">Einstellungen</p>
        <div class="control">
            <div class="field">
                <label class="label">Site Name</label>
                <div class="control">
                    <input v-model="siteName"  class="input" type="text"  placeholder="Site Name" >
                </div>
                <p class="help">Site Namen eingeben</p>
            </div>
            <div class="field">
                <label class="label">Site Beschreibung</label>
                <div class="control">
                    <input v-model="siteDescription"  class="input" type="text"  placeholder="Site Description" >
                </div>
                <p class="help">Site Description eingeben</p>
            </div>
            <div class="field">
                <label class="label">BACnet Seperator</label>
                <div class="control">
                    <input v-model="bacnetSeperator"  class="input" type="text"  placeholder="'" >
                </div>
                <p class="help">BACnet Seperator eingeben</p>
            </div>

            <div class="control">
            <label class="label">BACnet Port</label>
                <div class="select">
                    <select v-model="port"  >
                        <option>0xBAC0</option>
                        <option>0xBAC1</option>
                        <option>0xBAC2</option>
                        <option>0xBAC3</option>
                        <option>0xBAC4</option>
                        <option>0xBAC5</option>
                        <option>0xBAC6</option>
                        <option>0xBAC7</option>
                        <option>0xBAC8</option>
                        <option>0xBAC9</option>
                        <option>0xBACA</option>
                        <option>0xBACB</option>
                        <option>0xBACC</option>
                        <option>0xBACD</option>
                        <option>0xBACE</option>
                        <option>0xBACF</option>
                    </select>
                </div>
                <p class="help">BACnet port auswählen {{portSelected}}</p>
            </div>
            <div class="field is-grouped">
                <div class="control">
                    <button v-on:click="save"  class="button is-link" :disabled="siteName.length<=0">Speichern</button>
                </div>
                <div class="control">
                    <button class="button is-text">Abbrechen</button>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    //TODO change static settings to vuex settings!!!
    import axios from 'axios'
    import { mapGetters, mapActions} from "vuex"

    export default {
        name: "Settings",
        data() {
            return {
                siteName: "",
                port: 0xBAC0,
                portSelected: "",
                siteDescription: "",
                bacnetSeperator: "'"
            }

            },
        methods: {
            save: function () {
                axios.post("http://localhost:8080/settings/", {
                    siteName: this.siteName, port: parseInt(this.port), siteDescription: this.siteDescription,bacnetSeperator: this.bacnetSeperator
                })

               return this.portSelected=this.port;

            },
            ...mapActions(["settings", "readSettings"]),

        },
        computed: mapGetters(["getSettings"])
        ,
        created() {
            console.log(this.readSettings)
        }
    }
</script>

<style scoped>

</style>