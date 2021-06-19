import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import About from '../views/About.vue'
import AdminUser from '../views/admin/Admin-User.vue'
import AdminEbook from '../views/admin/Admin-Ebook.vue'
import AdminCategory from '../views/admin/Admin-Category.vue'
import AdminDoc from '../views/admin/Admin-Doc.vue'
import Doc from '../views/doc.vue'
import AdminStatistics from '../views/admin/Admin-Statistics.vue'
import {Tool} from "@/util/tool";
import store from "@/store";

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
    path: '/admin/user',
    name: 'AdminUser',
    component:AdminUser,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component:AdminEbook,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component:AdminCategory,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component:AdminDoc,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/doc',
    name: 'Doc',
    component:Doc
  },
  {
    path: '/admin/statistics',
    name: 'AdminStatistics',
    component:AdminStatistics
  }
]
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// Route login interception
router.beforeEach((to, from, next) => {
  if (to.matched.some(function (item) {
    console.log(item, "login verification：", item.meta.loginRequire);
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      console.log("User didn't login！");
      next('/');
    } else {
      if(loginUser.role === 'ROLE_ADMIN'){
        next();
      }else{
        next('/');
      }
    }
  } else {
    next();
  }
});


export default router;