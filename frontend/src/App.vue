<template>
  <div id="app">
    <header>
      <section class="hero has-background-grey-dark">
        <div class="container">
          <h1 class="title has-text-white-ter">
        <span class="icon is-large" v-on:click="ackAlarm()" v-if="getHasNewAlarm">
          <router-link class="far fa-bell button is-danger" to="/alarmList"></router-link>
        </span>
            Schoko BACnet Browser
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

        // This Function creates the websocket with stomp on startup
        // The stompclient gets passed in the store for it being available in other components and views like the alarmview
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        connect: function () {
            const socket = new WebSocket('ws://localhost:8098/ws/events');
            this.newStompClient(Stomp.over(socket));
            this.getStompClient.connect({}, this.callbackStomp);
        },
        // This Function subscribe to the eventSub channel on the backend server
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        callbackStomp: function (frame) {
          if (frame.command === "CONNECTED") {
            this.getStompClient.subscribe('/broker/eventSub', this.callback, {});
          }
          this.getStompClient.send("/app/eventSub", {}, "init")
        },
        // This Function gets called by the stompclient and the message body gets passed in the store as a JSON
        // If new events have been created the alarm bell will light up
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        callback: function (message) {
          let events = ((JSON).parse(message.body));
          if (events.length > this.getEvents.length) {
            this.newEvent(true)
          }
          this.newEventList(JSON.parse(message.body));
          this.isConnectedEvents(true);
        },
        // This Function closes the websocket
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        connectClose: function () {
          this.getStompClient.send("/app/endEvents", {}, "WebSocket Closed");
          this.getStompClient.unsubscribe('/broker/eventSub');
          this.getStompClient.disconnect();
          this.isConnectedEvents(false);
        },
        // This Function acknolage with the alarmicon
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        ackAlarm: function () {
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
    padding-left: 2em;
    padding-top: 3em;
  }
  #content{
    padding-left: 1.5em;
    padding-top: 3em;
  }
  .container{
    height: 3em;
  }
</style>
