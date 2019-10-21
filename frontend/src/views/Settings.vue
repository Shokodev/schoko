<template>
    <div>
        <p class="subtitle is 3">Einstellungen</p>
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
                <label class="label">BACnet Seperator</label>
                <div class="control">
                    <input v-model="settings.bacnetSeperator"  class="input" type="text"  placeholder="'" >
                </div>
                <p class="help">BACnet Seperator eingeben</p>
            </div>
            <div class="control">
            <label class="label">BACnet Port</label>
                <div class="select">
                    <select v-model="settings.port">
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
    //TODO change static settings to vuex settings!!!
    import { mapGetters, mapActions} from "vuex"

    export default {
        name: "Settings",
        data() {
            return {
                settings: {
                    siteName: "DefaultSite",
                    port: Number,
                    siteDescription: "Desciption",
                    bacnetSeperator: "'",
                }
            }
        },
        computed: {
            ...mapGetters(["getSettings"]),
        },methods: {
            ...mapActions(["newSettings"]),

            SetSettings() {
                this.newSettings(this.settings)
                console.log("send")
            }
        },
        mounted() {
            this.settings.siteDescription = this.getSettings.siteDescription;
            this.settings.siteName = this.getSettings.siteName;
            this.settings.port = this.getSettings.port;
            this.settings.bacnetSeperator = this.getSettings.bacnetSeperator;
        }

    }
</script>

<style scoped>

</style>