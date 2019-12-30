<template>
    <div>
        <p  class="menu-label">Menu</p>
            <ul  class="menu-list ">
                <li><router-link class="button" to="/home">Home</router-link></li>
                <li><router-link class="button" :class="{'button is-danger' : getHasNewAlarm}" to="/alarmList">Alarmliste</router-link></li>
                <li><router-link class="button" to="/structure">Struktur</router-link></li>
                <li><router-link class="button" to="/settings">Einstellungen</router-link></li>
                <li><router-link class="button" to="/about">Schoko</router-link></li>
            </ul>
    </div>
</template>

<script>
    import {mapGetters } from "vuex";
    
    export default {
        name: "Menu",
        data() {
            return {
                }
            },
        computed: {
            ...mapGetters([
               'getHasNewAlarm'
           ])
        }
    }
</script>

<style >

</style>