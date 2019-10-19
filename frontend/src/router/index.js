import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Structure from '../views/Structure.vue'
import Settings from '../views/Settings.vue'


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'home',
    component: Home,
  },
  {
    path: '/settings',
    name: 'settings',
    component: Settings,
  },
  {
    path: '/structure',
    name: 'structure',
    component: Structure
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
