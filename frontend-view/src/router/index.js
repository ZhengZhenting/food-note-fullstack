import Vue from "vue";
import VueRouter from "vue-router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import { getToken } from "@/utils/storage.js";
import echarts from 'echarts';
Vue.prototype.$echarts = echarts;
Vue.use(ElementUI);
Vue.use(VueRouter);

const routes = [
  {
    path: "*",
    redirect: "/login"
  },
  {
    path: "/login",
    component: () => import(`@/views/login/Login.vue`)
  },
  {
    path: "/register",
    component: () => import(`@/views/register/Register.vue`)
  },
  {
    path: "/admin", // 管理员主路由
    component: () => import(`@/views/admin/Home.vue`),
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "/adminLayout",
        name: 'Navigation',
        icon: 'el-icon-pie-chart',
        component: () => import(`@/views/admin/Main.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/userManage",
        name: 'User',
        icon: 'el-icon-user',
        component: () => import(`@/views/admin/UserManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/categoryManage",
        name: 'Category',
        icon: 'el-icon-paperclip',
        component: () => import(`@/views/admin/CategoryManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/evaluationsManage",
        name: 'Comment',
        icon: 'el-icon-chat-dot-round',
        component: () => import(`@/views/admin/EvaluationsManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/gourmetManage",
        name: 'Gourmet',
        icon: 'el-icon-tableware',
        component: () => import(`@/views/admin/GourmetManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/contentNetManage",
        name: 'Content',
        icon: 'el-icon-document-add',
        component: () => import(`@/views/admin/ContentNetManage.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/cookbookNetManage",
        name: 'CookBook',
        icon: 'el-icon-tickets',
        component: () => import(`@/views/admin/CookbookManage.vue`),
        meta: { requireAuth: true },
      },
    ]
  },
  {
    path: "/user",
    component: () => import(`@/views/user/Main.vue`),
    meta: {
      requireAuth: true,
    },
    children: [
      {
        path: "/gourmet",
        name: '美食做法',
        component: () => import(`@/views/user/Gourmet.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/cookbook",
        name: '食谱大全',
        component: () => import(`@/views/user/Cookbook.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/dietHistory",
        name: '饮食记录',
        component: () => import(`@/views/user/DietHistory.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/nutriment",
        name: '营养摄入',
        component: () => import(`@/views/user/Nutriment.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/service",
        name: '服务中心',
        component: () => import(`@/views/user/Service.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/self",
        name: '个人中心',
        component: () => import(`@/views/user/Self.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/resetPwd",
        name: '重置密码',
        component: () => import(`@/views/user/ResetPwd.vue`),
        meta: { requireAuth: true },
      },
      {
        path: "/save",
        name: '我的收藏',
        component: () => import(`@/views/user/Save.vue`),
        meta: { requireAuth: true },
      },
    ]
  }
];
const router = new VueRouter({
  routes,
  mode: 'history'
});
router.beforeEach((to, from, next) => {
  // 放行登录页和注册页
  if (to.path === '/login' || to.path === '/register') {
    return next();
  }

  // 检查需要认证的路由
  if (to.matched.some(record => record.meta.requireAuth)) {
    const token = getToken();

    // 未登录情况处理
    if (!token) {
      return next({
        path: '/login',
        query: { redirect: to.fullPath } // 记录目标路由
      });
    }

    // 已登录时的权限检查
    try {
      const role = parseInt(sessionStorage.getItem('role'));
      
      // 管理员路径检查
      if (to.matched[0].path === '/admin' && role !== 1) {
        clearToken();
        return next("/login"); //返回登录页
      }

      // 用户路径检查
      if (to.matched[0].path === '/user' && role !== 2) {
        clearToken();
        return next("/login"); //返回登录页
      }

      return next();
    } catch (error) {
      console.error('权限检查失败:', error);
      return next('/login');
    }
  }

  // 普通页面直接放行
  next();
});
export default router;
