<template>

  <div id="app">
    <header>
      <section class="hero is-primary">
        <div class="container">
          <h1>
        <span class="icon is-large">
          <router-link v-if="!newAlarm" class="far fa-bell button" to="/alarmList" v-on:click="AlarmList"></router-link>

          <router-link v-if="newAlarm" class="far fa-bell button is-danger" to="/alarmList" v-on:click="AlarmList"></router-link>
        </span>
            BACnet Browser
          </h1>
        </div>
      </section>
    </header>
    <section class="columns is-fullheight">
      <aside id="menu" class="column is-narrow is-fullheight">
        <p  class="menu-label">Menu</p>
        <Menu></Menu>
      </aside>
      <router-view id="content" class="column"></router-view>
    </section>
  </div>
</template>

<script>
  import Menu from './components/Menu'
  import { mapActions } from "vuex"

  export default {
    name: 'app',
    components: {
      Menu
    },
    mounted() {
      this.readSettings()
    },
      methods: {
      ...mapActions(["readSettings"]),

        AlarmList: function () {
          console.log("AlarmList")
        },
    },
    computed: {
      newAlarm: function () {
        return false;
      }
    }
  }

</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;

  }
  #menu{
    padding-left: 1.5em;
  }
  #content{
    padding-left: 1.5em;
  }
</style>
