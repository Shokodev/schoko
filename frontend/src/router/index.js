import Home from '../views/Home.vue'
import Structure from '../views/Structure.vue'
import Settings from '../views/Settings.vue'
import AlarmList from "../views/AlarmList"
import Devices from "../views/Devices";

// routes[] with all the different views.
// @author Vogt Andreas,Daniel Reiter, Rafael Grimm
// @version 1.0
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
  },
  {
    path: '/devices',
    name: 'devices',
    component: Devices
  }
];

export default routes
