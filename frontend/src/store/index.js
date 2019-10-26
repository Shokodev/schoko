import Vue from 'vue'
import Vuex from 'vuex'
import structure from "./modules/structure";
import settings from "./modules/settings";
import readAndWrite from "./modules/readAndWrite";

Vue.use(Vuex);

export default new Vuex.Store({
modules: {
  structure,
  settings,
  readAndWrite
}
});
