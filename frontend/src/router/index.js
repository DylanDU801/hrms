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
    path: '/outsourcing-tests',
    component: Layout,
    redirect: '/outsourcing-tests/index',
    name: 'OutsourcingTest',
    meta: { title: '外包测试', icon: 'el-icon-cpu' },
    children: [
      {
        path: 'index',
        name: 'OutsourcingTestList',
        component: () => import('@/views/outsourcing-tests/index'),
        meta: { title: '测试任务', icon: 'el-icon-s-order' }
      },
      {
        path: 'my-tests',
        name: 'MyTests',
        component: () => import('@/views/outsourcing-tests/my-tests'),
        meta: { title: '我的任务', icon: 'el-icon-user' }
      },
      {
        path: 'performance',
        name: 'TestPerformance',
        component: () => import('@/views/outsourcing-tests/performance'),
        meta: { title: '绩效统计', icon: 'el-icon-data-analysis' }
      },
      {
        path: ':id',
        name: 'TestDetail',
        component: () => import('@/views/outsourcing-tests/detail'),
        meta: { title: '测试详情' },
        hidden: true
      }
    ]
  },

  {
    path: '/applications',
    component: Layout,
    redirect: '/applications/index',
    name: 'Application',
    meta: { title: '申请审批', icon: 'el-icon-document' },
    children: [
      {
        path: 'index',
        name: 'ApplicationList',
        component: () => import('@/views/applications/index'),
        meta: { title: '申请管理', icon: 'el-icon-s-order' }
      },
      {
        path: 'my-applications',
        name: 'MyApplications',
        component: () => import('@/views/applications/my-applications'),
        meta: { title: '我的申请', icon: 'el-icon-user' }
      },
      {
        path: 'pending-approvals',
        name: 'PendingApprovals',
        component: () => import('@/views/applications/pending-approvals'),
        meta: { title: '待我审批', icon: 'el-icon-s-check' }
      },
      {
        path: ':id',
        name: 'ApplicationDetail',
        component: () => import('@/views/applications/detail'),
        meta: { title: '申请详情' },
        hidden: true
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

  {
    path: '/system',
    component: Layout,
    redirect: '/system/users',
    name: 'System',
    meta: { title: '系统管理', icon: 'el-icon-setting' },
    children: [
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/system/users'),
        meta: { title: '用户管理', icon: 'el-icon-user-solid' }
      },
      {
        path: 'roles',
        name: 'Roles',
        component: () => import('@/views/system/roles'),
        meta: { title: '角色管理', icon: 'el-icon-s-custom' }
      },
      {
        path: 'permissions',
        name: 'Permissions',
        component: () => import('@/views/system/permissions'),
        meta: { title: '权限管理', icon: 'el-icon-key' }
      },
      {
        path: 'logs',
        name: 'OperationLogs',
        component: () => import('@/views/system/logs'),
        meta: { title: '操作日志', icon: 'el-icon-document-copy' }
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