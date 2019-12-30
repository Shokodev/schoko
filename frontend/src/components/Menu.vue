<template>
    <div>
        <p  class="menu-label">Menu</p>
            <ul  class="menu-list ">
                <li><router-link to="/home">Home</router-link></li>
                <li><router-link to="/alarmList">Alarmliste</router-link></li>
                <li><router-link to="/structure">Struktur</router-link></li>
                <li><router-link to="/settings">Einstellungen</router-link></li>
                <li><router-link  to="/about">Schoko</router-link></li>
            </ul>
    </div>
</template>

<script>
    export default {
        name: "Menu",
    }
</script>

<style >

    .sticky{
        position: sticky;
        top: 100px;
    }


</style>