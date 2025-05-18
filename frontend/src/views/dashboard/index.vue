<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <!-- 统计数据 -->
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover">
            <div class="stat-card">
              <div class="icon" style="color: #409EFF;">
                <i class="el-icon-s-custom"></i>
              </div>
              <div class="content">
                <div class="title">员工总数</div>
                <div class="value">{{ stats.employeeCount || 0 }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover">
            <div class="stat-card">
              <div class="icon" style="color: #67C23A;">
                <i class="el-icon-office-building"></i>
              </div>
              <div class="content">
                <div class="title">部门数量</div>
                <div class="value">{{ stats.departmentCount || 0 }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover">
            <div class="stat-card">
              <div class="icon" style="color: #E6A23C;">
                <i class="el-icon-money"></i>
              </div>
              <div class="content">
                <div class="title">本月薪资</div>
                <div class="value">¥ {{ stats.monthlySalary || 0 }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="6">
          <el-card shadow="hover">
            <div class="stat-card">
              <div class="icon" style="color: #F56C6C;">
                <i class="el-icon-document"></i>
              </div>
              <div class="content">
                <div class="title">待处理事项</div>
                <div class="value">{{ stats.pendingTasks || 0 }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 快捷功能 -->
      <el-card class="box-card" style="margin-top: 20px;">
        <div slot="header" class="clearfix">
          <span>快捷功能</span>
        </div>
        <el-row :gutter="20" class="shortcut-container">
          <el-col :xs="12" :sm="6" :md="4" v-for="(item, index) in shortcuts" :key="index">
            <div class="shortcut-item" @click="handleShortcut(item)">
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 业务风险 -->
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :xs="24" :sm="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>业务风险提示</span>
            </div>
            <div class="risk-list">
              <div v-for="(risk, index) in risks" :key="index" class="risk-item">
                <div class="risk-title">
                  <el-tag :type="risk.level">{{ risk.levelText }}</el-tag>
                  <span>{{ risk.title }}</span>
                </div>
                <div class="risk-actions">
                  <el-button type="text" size="small">查看详情</el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :xs="24" :sm="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>待办事项</span>
              <el-button style="float: right; padding: 3px 0" type="text">查看全部</el-button>
            </div>
            <el-table :data="todoList" style="width: 100%">
              <el-table-column prop="title" label="事项内容"></el-table-column>
              <el-table-column prop="date" label="截止日期" width="100"></el-table-column>
              <el-table-column prop="status" label="状态" width="80">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.status === '紧急' ? 'danger' : 'warning'">{{ scope.row.status }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
// import { fetchDashboardData } from '@/api/dashboard'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        employeeCount: 28,
        departmentCount: 2,
        monthlySalary: 36000,
        pendingTasks: 15
      },
      shortcuts: [
        { title: '公司设置', icon: 'el-icon-s-tools', path: '/settings' },
        { title: '员工入职', icon: 'el-icon-user-solid', path: '/employees/add' },
        { title: '招聘管理', icon: 'el-icon-s-opportunity', path: '/recruitment' },
        { title: '考勤打卡', icon: 'el-icon-time', path: '/attendance' },
        { title: '薪资管理', icon: 'el-icon-money', path: '/salaries' },
        { title: '文档下载', icon: 'el-icon-document', path: '/documents' },
        { title: '工作审批', icon: 'el-icon-s-check', path: '/approvals' },
        { title: '报表分析', icon: 'el-icon-data-analysis', path: '/reports' }
      ],
      risks: [
        { title: '未确认员工工时 20 人', level: 'danger', levelText: '高风险' },
        { title: '员工证件即将到期 6 人', level: 'warning', levelText: '中风险' },
        { title: '服务合同即将到期 15 份', level: 'warning', levelText: '中风险' },
        { title: '未确认薪资单 8 人', level: 'info', levelText: '低风险' }
      ],
      todoList: [
        { title: '员工李四身份证即将过期', date: '2025-06-10', status: '紧急' },
        { title: '核对5月份考勤数据', date: '2025-05-25', status: '紧急' },
        { title: '更新员工信息', date: '2025-05-30', status: '一般' },
        { title: '确认工资单', date: '2025-06-05', status: '一般' }
      ]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      // 实际项目中应该调用API
      // fetchDashboardData().then(response => {
      //   this.stats = response.data.stats
      //   this.todoList = response.data.todoList
      //   this.risks = response.data.risks
      // })
    },
    handleShortcut(item) {
      this.$router.push(item.path)
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  
  &-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;
  }
}

.shortcut {
  &-container {
    display: flex;
    flex-wrap: wrap;
  }
  
  &-item {
    height: 100px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    border-radius: 4px;
    transition: all 0.3s;
    
    i {
      font-size: 30px;
      margin-bottom: 10px;
      color: #409EFF;
    }
    
    &:hover {
      background: #f0f2f5;
    }
  }
}

.risk {
  &-list {
    max-height: 300px;
    overflow-y: auto;
  }
  
  &-item {
    padding: 10px 0;
    border-bottom: 1px solid #ebeef5;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    &:last-child {
      border-bottom: none;
    }
  }
  
  &-title {
    display: flex;
    align-items: center;
    
    .el-tag {
      margin-right: 10px;
    }
  }
}
</style>