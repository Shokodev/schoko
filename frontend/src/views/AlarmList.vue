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
        <div>

        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';
        export default {
        name: "AlarmList",
            data() {
                return {
                    connected: false,
                    stompClient: Object,
                    bacnetEvents: null,
                };
            },
            mounted() {
            },
            methods:{
            },
            computed:{
            ...mapGetters(["getEvents"])
            }
    };
</script>

<style scoped>

</style>