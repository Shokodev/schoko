<template>
    <div>
    <h1 class="subtitle is 3">
        Alarmliste
    </h1>
    <div>
        <span>
            <span  class="columns box is-three-fifths" :key="child.eventID" v-for="child in getEvents">
                <div Class="column">Datum: {{child.timeStamp}} </div>
                  <div Class="column">Beschreibung: {{child.description}} </div>
                    <div Class="column">  Aktuller Wert: {{child.presentValue}} </div>
                     <div Class="column"> {{child.fromState}};</div>
                        <div Class="column"> {{child.toState}} </div>
            </span>
        </span>
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