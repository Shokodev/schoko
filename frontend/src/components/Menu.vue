<template>
    <ul  class="menu-list">
        <li><router-link to="/home">Home</router-link></li>
        <li><router-link to="/alarmList">Alarmliste</router-link></li>
        <li><router-link to="/structure">Struktur</router-link></li>
        <li><router-link to="/settings">Einstellungen</router-link></li>
    </ul>
</template>

<script>
    export default {
        name: "Menu",
    }
</script>

<style >

</style>