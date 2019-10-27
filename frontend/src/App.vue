<template>

  <div id="app">
    <header>
      <section class="hero is-primary">
        <div class="container">
          <h1>
        <span class="icon is-large" v-on:click="ackAlarm()" v-if="getHasNewAlarm">
          <router-link class="far fa-bell button is-danger" to="/alarmList"></router-link>
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
  import { mapActions, mapMutations, mapGetters } from "vuex"
  import Stomp from 'stompjs';

  export default {
    name: 'app',
    components: {
      Menu
    },
    mounted() {
      this.readSettings();
      this.connect()
    },
      methods: {
      ...mapActions(["readSettings", "newEvent"]),
      ...mapMutations(['isConnectedEvents', 'newStompClient','newEventList', 'getIsConnectedEvents']),

        connect: function () {
            const socket = new WebSocket('ws://localhost:8098/ws/events');
            this.newStompClient(Stomp.over(socket));
            this.getStompClient.connect({}, this.callbackStomp);
        },
        callbackStomp: function (frame) {
          if (frame.command === "CONNECTED") {
            this.getStompClient.subscribe('/broker/eventSub', this.callback, {});
          } else {
            console.log("failed")
          }
        },
        callback: function (message) {
          let events = ((JSON).parse(message.body));
          if (events.length > this.getEvents.length) {
            this.newEvent(true)
          }
          this.newEventList(JSON.parse(message.body));
          this.isConnectedEvents(true);
        },
        connectClose: function () {
          this.getStompClient.send("/app/endEvents", {}, "WebSocket Closed");
          this.getStompClient.unsubscribe('/broker/eventSub');
          this.getStompClient.disconnect();
          this.isConnectedEvents(false);
        },
        ackAlarm: function () {
        console.log("click")
          this.newEvent(false)
        }
    },
    computed: {
      ...mapGetters([
        'getStompClient','getEvents','getHasNewAlarm'
      ]),
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
  .container{
    height: 3em;
  }
</style>
