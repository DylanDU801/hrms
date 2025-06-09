<template>
  <div class="performance-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>测试绩效统计</span>
      </div>

      <!-- 筛选区域 -->
      <div class="filter-container">
<!--        <el-select v-model="listQuery.testerId" placeholder="选择测试人员" clearable style="width: 200px" class="filter-item">-->
<!--          <el-option-->
<!--            v-for="tester in testers"-->
<!--            :key="tester.id"-->
<!--            :label="tester.name"-->
<!--            :value="tester.id">-->
<!--          </el-option>-->
<!--        </el-select>-->
        <el-date-picker
          v-model="listQuery.month"
          type="month"
          placeholder="选择月份"
          value-format="yyyy-MM"
          class="filter-item">
        </el-date-picker>
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">统计</el-button>
<!--        <el-button class="filter-item" type="success" icon="el-icon-download" @click="handleExport">导出</el-button>-->
      </div>

      <!-- 统计卡片 -->
      <div class="stats-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ overallStats.totalTests }}</div>
              <div class="stat-label">总任务数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card completed">
              <div class="stat-number">{{ overallStats.completedTests }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card rate">
              <div class="stat-number">{{ overallStats.completionRate }}%</div>
              <div class="stat-label">完成率</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card score">
              <div class="stat-number">{{ overallStats.avgScore }}</div>
              <div class="stat-label">平均得分</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 绩效表格 -->
      <el-table :data="list" v-loading="listLoading" border style="width: 100%">
        <el-table-column prop="employeeName" label="测试人员" width="120"></el-table-column>
        <el-table-column prop="department" label="部门" width="100"></el-table-column>
        <el-table-column prop="totalTasks" label="总任务数" width="100" align="center"></el-table-column>
        <el-table-column prop="completedTasks" label="已完成" width="100" align="center"></el-table-column>
        <el-table-column prop="completionRate" label="完成率" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.completionRate"
              :color="getProgressColor(scope.row.completionRate)"
              :show-text="false">
            </el-progress>
            <span style="margin-left: 10px;">{{ scope.row.completionRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="averageScore" label="平均得分" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getScoreType(scope.row.averageScore)">{{ scope.row.averageScore }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalHours" label="总工时" width="100" align="center"></el-table-column>
        <el-table-column prop="efficiency" label="效率" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.efficiency }}%
          </template>
        </el-table-column>
        <el-table-column prop="grade" label="等级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getGradeType(scope.row.grade)">{{ scope.row.grade }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">详情</el-button>
<!--            <el-button size="mini" type="success" @click="handleHistory(scope.row)">历史</el-button>-->
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.limit"
        @pagination="getList"
      />
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog title="绩效详情" :visible.sync="detailDialogVisible" width="800px">
      <div v-if="currentPerformance">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="测试人员">{{ currentPerformance.employeeName }}</el-descriptions-item>
          <el-descriptions-item label="部门">{{ currentPerformance.department }}</el-descriptions-item>
          <el-descriptions-item label="统计月份">{{ listQuery.month }}</el-descriptions-item>
          <el-descriptions-item label="总任务数">{{ currentPerformance.totalTasks }}</el-descriptions-item>
          <el-descriptions-item label="已完成">{{ currentPerformance.completedTasks }}</el-descriptions-item>
          <el-descriptions-item label="完成率">{{ currentPerformance.completionRate }}%</el-descriptions-item>
          <el-descriptions-item label="平均得分">{{ currentPerformance.averageScore }}</el-descriptions-item>
          <el-descriptions-item label="总工时">{{ currentPerformance.totalHours }}h</el-descriptions-item>
          <el-descriptions-item label="效率">{{ currentPerformance.efficiency }}%</el-descriptions-item>
        </el-descriptions>

        <!-- 任务列表 -->
        <div style="margin-top: 20px;">
          <h4>本月任务详情</h4>
          <el-table :data="tasks" border style="width: 100%">
            <el-table-column prop="testName" label="任务名称" width="200"></el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="得分" width="80"></el-table-column>
            <el-table-column prop="estimatedHours" label="预计工时" width="100"></el-table-column>
            <el-table-column prop="actualHours" label="实际工时" width="100"></el-table-column>
          </el-table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  fetchTests,
  fetchTest,
  createTest,
  updateTest,
  deleteTest,
  assignTest,
  submitTestResult,
  reviewTest,
  getMyTests,
  getTestStatistics,
  getTesterPerformance,
  getTestsById
} from '@/api/outsourcing-test'

export default {
  name: 'TestPerformance',
  components: { Pagination },
  data() {
    return {
      tasks:[],
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 0,
        limit: 20,
        testerId: undefined,
        month: new Date().toISOString().slice(0, 7) // 当前月份
      },
      testers: [
        { id: 1, name: '张测试' },
        { id: 2, name: '李测试' },
        { id: 3, name: '王测试' }
      ],
      overallStats: {
        totalTests: 0,
        completedTests: 0,
        completionRate: 0,
        avgScore: 0
      },
      currentPerformance: null,
      detailDialogVisible: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getTesterPerformance(this.listQuery).then(response => {
        this.list = response.data
        this.total = 6

        // 计算总体统计
        this.calculateOverallStats()
        this.listLoading = false
      }).catch(error => {
        console.error(error)
        this.listLoading = false
      })
    },
    calculateOverallStats() {
      const totalTests = this.list.reduce((sum, item) => sum + item.totalTasks, 0)
      const completedTests = this.list.reduce((sum, item) => sum + item.completedTasks, 0)
      const avgScoreSum = this.list.filter(item => item && item.averageScore != null)
          .reduce((sum, item) => {
            const score = parseFloat(item.averageScore);
            return !isNaN(score) ? sum + score : sum;
          }, 0);



      console.log(this.list.length)
      console.log(avgScoreSum)
      this.list.length > 0 ? Math.round(avgScoreSum / this.list.length) : 0
      this.overallStats = {
        totalTests,
        completedTests,
        completionRate: totalTests > 0 ? Math.round((completedTests / totalTests) * 100) : 0,
        avgScore: this.list.length > 0 ? Math.round(avgScoreSum / this.list.length) : 0
      }
    },
    handleFilter() {
      this.listQuery.page = 0
      this.getList()
    },
    handleExport() {
      this.$message({
        message: '导出功能开发中...',
        type: 'info'
      })
    },
    handleDetail(row) {
      this.currentPerformance = row
      this.detailDialogVisible = true
      let id = row.employeeId
      getTestsById({id}).then(response => {
        console.log(response)
        this.tasks = response
      }).catch((error) => {
        console.log(error)
      })
    },
    handleHistory(row) {
      this.$message({
        message: '历史记录功能开发中...',
        type: 'info'
      })
    },
    getProgressColor(percentage) {
      if (percentage >= 90) return '#67c23a'
      if (percentage >= 70) return '#e6a23c'
      return '#f56c6c'
    },
    getScoreType(score) {
      if (score >= 90) return 'success'
      if (score >= 80) return 'primary'
      if (score >= 70) return 'warning'
      return 'danger'
    },
    getGradeType(grade) {
      const gradeMap = {
        'A': 'success',
        'B': 'primary',
        'C': 'warning',
        'D': 'danger'
      }
      return gradeMap[grade] || 'info'
    },
    getStatusType(status) {
      const statusMap = {
        'COMPLETED': 'success',
        'IN_PROGRESS': 'warning',
        'PENDING': 'info'
      }
      return statusMap[status]
    },
    getStatusText(status) {
      const statusMap = {
        'REVIEWED': '已完成',
        'IN_PROGRESS': '进行中',
        'PENDING': '待分配'
      }
      return statusMap[status]
    },
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString()
    }
  }
}
</script>

<style scoped>
.performance-container {
  padding: 20px;
}

.stats-container {
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-left: 4px solid #409EFF;
}

.stat-card.completed {
  border-left-color: #67C23A;
}

.stat-card.rate {
  border-left-color: #E6A23C;
}

.stat-card.score {
  border-left-color: #F56C6C;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.filter-container {
  padding-bottom: 10px;
}

.filter-item {
  display: inline-block;
  vertical-align: middle;
  margin-bottom: 10px;
  margin-right: 10px;
}
</style>
