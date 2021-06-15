import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import AdminEbook from '../views/admin/Admin-Ebook.vue'
import AdminCategory from '../views/admin/Admin-Category.vue'
import AdminDoc from '../views/admin/Admin-Doc.vue'
import Doc from '../views/doc.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/about',
    name: 'About',
    component:About
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component:AdminEbook
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component:AdminCategory
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component:AdminDoc
  },
  {
    path: '/doc',
    name: 'Doc',
    component:Doc
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
