import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'project',
        name: 'Project',
        component: () => import('@/views/project/index.vue'),
        meta: { title: '项目管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'asset',
        name: 'Asset',
        redirect: '/asset/list',
        meta: { title: '资产管理', icon: 'Box' },
        children: [
          {
            path: 'list',
            name: 'AssetList',
            component: () => import('@/views/asset/index.vue'),
            meta: { title: '资产列表' }
          },
          {
            path: 'category',
            name: 'AssetCategory',
            component: () => import('@/views/asset/category.vue'),
            meta: { title: '资产分类' }
          }
        ]
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/index.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'house',
        name: 'House',
        component: () => import('@/views/house/index.vue'),
        meta: { title: '房屋管理', icon: 'House' }
      },
      {
        path: 'fee',
        name: 'Fee',
        component: () => import('@/views/fee/index.vue'),
        meta: { title: '费用管理', icon: 'Money' }
      },
      {
        path: 'service',
        name: 'Service',
        component: () => import('@/views/service/index.vue'),
        meta: { title: '服务管理', icon: 'Service' }
      },
      {
        path: 'repair',
        name: 'Repair',
        component: () => import('@/views/repair/index.vue'),
        meta: { title: '报修管理', icon: 'Tools' }
      },
      {
        path: 'message',
        name: 'Message',
        component: () => import('@/views/message/index.vue'),
        meta: { title: '消息管理', icon: 'Message' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 物业管理系统` : '物业管理系统'
  const userStore = useUserStore()
  if (to.path !== '/login' && !userStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
