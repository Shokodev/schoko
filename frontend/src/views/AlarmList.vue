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
                <th title="Gerät">Gerät</th>
            </tr>
            </thead>
            <tbody>
            <tr id="alarmlist" :key="child.eventID" v-for="child in getEvents">
                <td>{{child.timeStamp}}</td>
                <td>{{child.oid}}</td>
                <td>{{child.description}}</td>
                <td>{{child.presentValue}}</td>
                <td>{{child.toState}}</td>
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
                ...mapActions(["newEvent"])
            },
            computed:{
            ...mapGetters(["getEvents"])
            }
    };
</script>

<style scoped>

</style>