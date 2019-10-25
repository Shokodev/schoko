import Home from '../views/Home.vue'
import Structure from '../views/Structure.vue'
import Settings from '../views/Settings.vue'
import AlarmList from "../views/AlarmList";


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
    path: '/alarmlist',
    name: 'alarmlist',
    component: AlarmList,
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
