<template>
    <ul class="menu-list">
        <li><router-link to="/home" v-on:click="getToHome">Home</router-link></li>
        <li><router-link to="/alarmList" v-on:click="AlarmList">Alarmliste</router-link></li>
        <li><router-link to="/structure" v-on:click="getHierarchy">Struktur</router-link></li>
        <li><router-link to="/settings" v-on:click="settings">Einstellungen</router-link></li>
    </ul>
</template>

<script>
    export default {
        name: "Menu",
        methods: {
            getToHome: function () {
                console.log("getToHome")
            },
            AlarmList: function () {
                console.log("AlarmList")
            },
            getHierarchy: function () {
                console.log("getHierarchy")
            },
            device: function () {
                console.log("device")
            },
            settings: function () {
                console.log("settings")
            }
        }
    }
</script>

<style >

</style>