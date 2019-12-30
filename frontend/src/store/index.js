import Vue from 'vue'
import Vuex from 'vuex'
import structure from "./modules/structure";
import settings from "./modules/settings";
import readAndWrite from "./modules/readAndWrite";
import events from "./modules/events";
import devices from "./modules/devices";

Vue.use(Vuex);
// Store modules for vuex, also passing the vuex to vue
// @author Vogt Andreas,Daniel Reiter, Rafael Grimm
// @version 1.0

export default new Vuex.Store({
modules: {
  structure,
  settings,
  readAndWrite,
  events,
  devices
  }
});
