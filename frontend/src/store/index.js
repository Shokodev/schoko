import Vue from 'vue'
import Vuex from 'vuex'
import structure from "./modules/structure";
import settings from "./modules/settings";
import Websocket from "./modules/WebSocket";

Vue.use(Vuex);

export default new Vuex.Store({
modules: {
  structure,
  settings,
  Websocket
}
});
