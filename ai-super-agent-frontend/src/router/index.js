import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView,
  },
  {
    path: '/love-app',
    name: 'LoveApp',
    component: () => import('../views/LoveAppView.vue'),
  },
  {
    path: '/manus-app',
    name: 'ManusApp',
    component: () => import('../views/ManusAppView.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
