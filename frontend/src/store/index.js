import Vue from 'vue'
import Vuex from 'vuex'
import structure from "./modules/structure";
import settings from "./modules/settings";
import readAndWrite from "./modules/readAndWrite";
import events from "./modules/events";

Vue.use(Vuex);

export default new Vuex.Store({
modules: {
  structure,
  settings,
  readAndWrite,
  events
}
});
