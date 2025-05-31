<template>
  <div class="my-tests-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>我的测试任务</span>
      </div>
      
      <!-- 统计卡片 -->
      <div class="stats-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ stats.total }}</div>
              <div class="stat-label">总任务数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card pending">
              <div class="stat-number">{{ stats.inProgress }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card completed">
              <div class="stat-number">{{ stats.completed }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card score">
              <div class="stat-number">{{ stats.avgScore }}</div>
              <div class="stat-label">平均得分</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 筛选区域 -->
      <div class="filter-container">
        <el-select v-model="listQuery.status" placeholder="状态" clearable style="width: 120px" class="filter-item">
          <el-option label="进行中" value="IN_PROGRESS"></el-option>
          <el-option label="已提交" value="SUBMITTED"></el-option>
          <el-option label="已完成" value="COMPLETED"></el-option>
        </el-select>
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      </div>

      <!-- 表格 -->
      <el-table :data="list" v-loading="listLoading" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="testName" label="测试名称" width="200"></el-table-column>
        <el-table-column prop="description" label="描述" width="250" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityType(scope.row.priority)">{{ scope.row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="estimatedHours" label="预计工时" width="100"></el-table-column>
        <el-table-column prop="actualHours" label="实际工时" width="100"></el-table-column>
        <el-table-column prop="score" label="得分" width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.score">{{ scope.row.score }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">详情</el-button>
            <el-button 
              size="mini" 
              type="success" 
              @click="handleSubmit(scope.row)" 
              v-if="scope.row.status === 'IN_PROGRESS'"
            >
              提交结果
            </el-button>
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

    <!-- 提交结果对话框 -->
    <el-dialog title="提交测试结果" :visible.sync="submitDialogVisible" width="600px">
      <el-form ref="submitForm" :model="submitTemp" label-width="100px">
        <el-form-item label="测试任务">
          <span>{{ submitTemp.testName }}</span>
        </el-form-item>
        <el-form-item label="测试结果" prop="result">
          <el-input type="textarea" :rows="5" v-model="submitTemp.result" placeholder="请输入测试结果" />
        </el-form-item>
        <el-form-item label="实际工时" prop="actualHours">
          <el-input-number v-model="submitTemp.actualHours" :min="0" :max="200" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="submitDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSubmit">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'MyTests',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 0,
        limit: 20,
        status: undefined
      },
      stats: {
        total: 0,
        inProgress: 0,
        completed: 0,
        avgScore: 0
      },
      submitTemp: {
        id: undefined,
        testName: '',
        result: '',
        actualHours: 0
      },
      submitDialogVisible: false
    }
  },
  created() {
    this.getList()
    this.getStats()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.list = [
          {
            id: 1,
            testName: '用户登录功能测试',
            description: '测试用户登录、注册、密码重置等核心功能',
            status: 'IN_PROGRESS',
            priority: 'HIGH',
            estimatedHours: 16,
            actualHours: null,
            score: null,
            startTime: '2025-05-15 10:30:00'
          },
          {
            id: 2,
            testName: '员工管理模块测试',
            description: '测试员工增删改查、部门分配等功能',
            status: 'COMPLETED',
            priority: 'MEDIUM',
            estimatedHours: 24,
            actualHours: 20,
            score: 85,
            startTime: '2025-05-10 09:00:00'
          }
        ]
        this.total = 2
        this.listLoading = false
      }, 1000)
    },
    getStats() {
      // 模拟统计数据
      this.stats = {
        total: 5,
        inProgress: 1,
        completed: 4,
        avgScore: 88
      }
    },
    handleFilter() {
      this.listQuery.page = 0
      this.getList()
    },
    handleDetail(row) {
      this.$router.push({ path: `/outsourcing-tests/${row.id}` })
    },
    handleSubmit(row) {
      this.submitTemp = {
        id: row.id,
        testName: row.testName,
        result: '',
        actualHours: row.estimatedHours
      }
      this.submitDialogVisible = true
    },
    confirmSubmit() {
      if (!this.submitTemp.result.trim()) {
        this.$message.error('请输入测试结果')
        return
      }
      this.$message({
        message: '提交成功',
        type: 'success'
      })
      this.submitDialogVisible = false
      this.getList()
    },
    getStatusType(status) {
      const statusMap = {
        'IN_PROGRESS': 'warning',
        'SUBMITTED': 'primary',
        'COMPLETED': 'success'
      }
      return statusMap[status]
    },
    getStatusText(status) {
      const statusMap = {
        'IN_PROGRESS': '进行中',
        'SUBMITTED': '已提交',
        'COMPLETED': '已完成'
      }
      return statusMap[status]
    },
    getPriorityType(priority) {
      const priorityMap = {
        'LOW': 'info',
        'MEDIUM': 'primary',
        'HIGH': 'warning',
        'URGENT': 'danger'
      }
      return priorityMap[priority]
    },
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString()
    }
  }
}
</script>

<style scoped>
.my-tests-container {
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

.stat-card.pending {
  border-left-color: #E6A23C;
}

.stat-card.completed {
  border-left-color: #67C23A;
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