import Home from '../views/Home.vue'
import Structure from '../views/Structure.vue'
import Settings from '../views/Settings.vue'


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

export default routes
