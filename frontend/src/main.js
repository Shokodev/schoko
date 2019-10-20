import Vue from 'vue';
import App from './App.vue';
import VueRouter from "vue-router";
import routes from './router/index';
import "bulma/css/bulma.css";
import Store from './store';

Vue.config.productionTip = false;

Vue.use(VueRouter);
const router = new VueRouter({
  routes
});

new Vue({
  Store,
  router,
  render: h => h(App),
}).$mount('#app');
