<template>
    <div>
    <h1 class="subtitle is 3">
        Alarmliste
    </h1>
        <table class="table is-hoverable is-striped">
            <thead>
            <tr>
                <th title="DatumZeit">Datum / Zeit</th>
                <th title="Objekt Name">Objekt Name</th>
                <th title="Beschreibung">Beschreibung</th>
                <th title="Aktueller Wert">Aktueller Wert</th>
                <th title="Zustand">Zustand</th>
                <th title="Reset">Reset</th>
                <th title="Quit">Quit</th>
                <th title="Gerät">Gerät</th>
            </tr>
            </thead>
            <tbody>
            <tr id="alarmlist" :key="child.eventID" v-for="child in getEvents">
                <td>{{child.timeStamp}}</td>
                <td>{{child.objectName}}</td>
                <td>{{child.description}}</td>
                <td>{{child.presentValue}}</td>
                <td>{{child.toState}}</td>
                <td><button class="button" v-if="!child.ackState" v-on:click="ackState(child.objectName)">
                    <span class="icon is-small">
                    <i class="fas fa-check"></i>
                    </span>
                </button></td>
                <td><button class="button" v-if="!child.resetState">
                    <span class="icon is-small">
                    <i class="fas fa-undo"></i>
                    </span>
                </button></td>
                <td>{{child.remoteDeviceName}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    import {mapGetters, mapActions} from 'vuex';
        export default {
        name: "AlarmList",
            data() {
                return {
                    bacnetEvents: null,
                };
            },
            mounted() {
                // This Function acknolage with the alarmicon
                // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
                // @version 1.0
                    this.newEvent(false)
            },
            methods: {
                ...mapActions(["newEvent","setAckObjectName"]),

            ackState: function (objectName){
                this.setAckObjectName(objectName);
                this.$emit('event',this.getAckObjectName);

            },



            },
            computed: {
                ...mapGetters(["getEvents","getAckObjectName"]),
            },
    };
</script>

<style scoped>

</style>