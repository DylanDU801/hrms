import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'el-icon-s-home' }
    }]
  },

  {
    path: '/employees',
    component: Layout,
    redirect: '/employees/list',
    name: 'Employee',
    meta: { title: '员工管理', icon: 'el-icon-user' },
    children: [
      {
        path: 'list',
        name: 'EmployeeList',
        component: () => import('@/views/employees/list'),
        meta: { title: '员工列表', icon: 'el-icon-s-order' }
      },
      {
        path: 'add',
        name: 'EmployeeAdd',
        component: () => import('@/views/employees/create'),
        meta: { title: '添加员工', icon: 'el-icon-plus' }
      },
      {
        path: ':id',
        name: 'EmployeeDetail',
        component: () => import('@/views/employees/detail'),
        meta: { title: '员工详情' },
        hidden: true
      }
    ]
  },

  {
    path: '/departments',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Department',
        component: () => import('@/views/departments/index'),
        meta: { title: '部门管理', icon: 'el-icon-office-building' }
      }
    ]
  },

  {
    path: '/salaries',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Salary',
        component: () => import('@/views/salaries/index'),
        meta: { title: '薪资管理', icon: 'el-icon-money' }
      }
    ]
  },

  // 404页面必须放在最后
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // 需要服务支持
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// 详情
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // 重置路由
}

export default router