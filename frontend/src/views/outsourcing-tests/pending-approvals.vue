<template>
  <div class="pending-approvals-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>待我审批</span>
        <el-badge :value="stats.urgent" class="item">
          <el-button style="float: right; padding: 3px 0" type="text" @click="handleBatchApprove">批量审批</el-button>
        </el-badge>
      </div>
      
      <!-- 统计卡片 -->
      <div class="stats-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ stats.total }}</div>
              <div class="stat-label">待审批总数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card urgent">
              <div class="stat-number">{{ stats.urgent }}</div>
              <div class="stat-label">紧急申请</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card overdue">
              <div class="stat-number">{{ stats.overdue }}</div>
              <div class="stat-label">超时申请</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card today">
              <div class="stat-number">{{ stats.today }}</div>
              <div class="stat-label">今日新增</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 筛选区域 -->
      <div class="filter-container">
        <el-select v-model="listQuery.type" placeholder="申请类型" clearable style="width: 150px" class="filter-item">
          <el-option label="请假申请" value="LEAVE"></el-option>
          <el-option label="加班申请" value="OVERTIME"></el-option>
          <el-option label="调岗申请" value="TRANSFER"></el-option>
          <el-option label="设备申请" value="EQUIPMENT"></el-option>
          <el-option label="报销申请" value="REIMBURSEMENT"></el-option>
        </el-select>
        <el-select v-model="listQuery.priority" placeholder="优先级" clearable style="width: 120px" class="filter-item">
          <el-option label="低" value="LOW"></el-option>
          <el-option label="普通" value="NORMAL"></el-option>
          <el-option label="高" value="HIGH"></el-option>
          <el-option label="紧急" value="URGENT"></el-option>
        </el-select>
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
        <el-button class="filter-item" type="success" @click="handleQuickApprove">快速审批</el-button>
      </div>

      <!-- 表格 -->
      <el-table 
        :data="list" 
        v-loading="listLoading" 
        border 
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="申请标题" width="200">
          <template slot-scope="scope">
            <span class="application-title" @click="handleDetail(scope.row)">{{ scope.row.title }}</span>
            <el-tag v-if="isOverdue(scope.row)" type="danger" size="mini" style="margin-left: 5px;">超时</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicationType" label="申请类型" width="120">
          <template slot-scope="scope">
            <el-tag>{{ getTypeText(scope.row.applicationType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicantName" label="申请人" width="120"></el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityType(scope.row.priority)" size="small">{{ scope.row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.applyTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="waitingDays" label="等待天数" width="100">
          <template slot-scope="scope">
            <span :class="{ 'overdue-text': scope.row.waitingDays > 3 }">
              {{ scope.row.waitingDays }}天
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="success" @click="handleApprove(scope.row, true)">批准</el-button>
            <el-button size="mini" type="danger" @click="handleApprove(scope.row, false)">拒绝</el-button>
            <el-button size="mini" type="warning" @click="handleForward(scope.row)">转发</el-button>
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

    <!-- 审批对话框 -->
    <el-dialog :title="approveDialogTitle" :visible.sync="approveDialogVisible" width="500px">
      <el-form ref="approveForm" :model="approveTemp" label-width="100px">
        <el-form-item label="申请标题">
          <span>{{ approveTemp.title }}</span>
        </el-form-item>
        <el-form-item label="申请内容">
          <div style="max-height: 100px; overflow-y: auto; border: 1px solid #dcdfe6; padding: 10px; border-radius: 4px;">
            {{ approveTemp.content }}
          </div>
        </el-form-item>
        <el-form-item label="审批结果">
          <el-radio-group v-model="approveTemp.approved">
            <el-radio :label="true">批准</el-radio>
            <el-radio :label="false">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见">
          <el-input type="textarea" :rows="3" v-model="approveTemp.reason" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmApprove">确认审批</el-button>
      </div>
    </el-dialog>

    <!-- 转发对话框 -->
    <el-dialog title="转发申请" :visible.sync="forwardDialogVisible" width="400px">
      <el-form ref="forwardForm" :model="forwardTemp" label-width="100px">
        <el-form-item label="转发给">
          <el-select v-model="forwardTemp.newApproverId" placeholder="请选择审批人">
            <el-option
              v-for="approver in approvers"
              :key="approver.id"
              :label="approver.name"
              :value="approver.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="转发原因">
          <el-input type="textarea" :rows="3" v-model="forwardTemp.reason" placeholder="请输入转发原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="forwardDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmForward">确认转发</el-button>
      </div>
    </el-dialog>

    <!-- 批量审批对话框 -->
    <el-dialog title="批量审批" :visible.sync="batchDialogVisible" width="500px">
      <el-form ref="batchForm" :model="batchTemp" label-width="100px">
        <el-form-item label="选中申请">
          <span>已选择 {{ selectedApplications.length }} 个申请</span>
        </el-form-item>
        <el-form-item label="审批结果">
          <el-radio-group v-model="batchTemp.approved">
            <el-radio :label="true">批准</el-radio>
            <el-radio :label="false">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见">
          <el-input type="textarea" :rows="3" v-model="batchTemp.reason" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBatchApprove">确认批量审批</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'PendingApprovals',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      selectedApplications: [],
      listQuery: {
        page: 0,
        limit: 20,
        type: undefined,
        priority: undefined
      },
      stats: {
        total: 0,
        urgent: 0,
        overdue: 0,
        today: 0
      },
      approveTemp: {
        id: undefined,
        title: '',
        content: '',
        approved: true,
        reason: ''
      },
      forwardTemp: {
        id: undefined,
        newApproverId: undefined,
        reason: ''
      },
      batchTemp: {
        approved: true,
        reason: ''
      },
      approvers: [
        { id: 1, name: '张主管' },
        { id: 2, name: '李经理' },
        { id: 3, name: '王总监' }
      ],
      approveDialogVisible: false,
      forwardDialogVisible: false,
      batchDialogVisible: false
    }
  },
  computed: {
    approveDialogTitle() {
      return this.approveTemp.approved ? '批准申请' : '拒绝申请'
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
            applicationType: 'LEAVE',
            title: '年假申请',
            content: '申请5月20日-5月24日年假，共5天。原因：家庭聚会。',
            applicantName: '张三',
            priority: 'NORMAL',
            applyTime: '2025-05-15 10:30:00',
            waitingDays: 1
          },
          {
            id: 2,
            applicationType: 'EQUIPMENT',
            title: '笔记本电脑申请',
            content: '当前电脑配置较低，影响开发效率，申请更换高配置笔记本。',
            applicantName: '李四',
            priority: 'HIGH',
            applyTime: '2025-05-12 09:15:00',
            waitingDays: 4
          },
          {
            id: 3,
            applicationType: 'OVERTIME',
            title: '紧急加班申请',
            content: '系统出现重大bug，需要紧急修复，申请今晚加班。',
            applicantName: '王五',
            priority: 'URGENT',
            applyTime: '2025-05-16 16:00:00',
            waitingDays: 0
          }
        ]
        this.total = 3
        this.listLoading = false
      }, 1000)
    },
    getStats() {
      // 模拟统计数据
      this.stats = {
        total: 12,
        urgent: 2,
        overdue: 3,
        today: 5
      }
    },
    handleFilter() {
      this.listQuery.page = 0
      this.getList()
    },
    handleSelectionChange(selection) {
      this.selectedApplications = selection
    },
    handleDetail(row) {
      this.$router.push({ path: `/applications/${row.id}` })
    },
    handleApprove(row, approved) {
      this.approveTemp = {
        id: row.id,
        title: row.title,
        content: row.content,
        approved: approved,
        reason: ''
      }
      this.approveDialogVisible = true
    },
    handleForward(row) {
      this.forwardTemp = {
        id: row.id,
        newApproverId: undefined,
        reason: ''
      }
      this.forwardDialogVisible = true
    },
    handleBatchApprove() {
      if (this.selectedApplications.length === 0) {
        this.$message.warning('请选择要批量审批的申请')
        return
      }
      this.batchTemp = {
        approved: true,
        reason: ''
      }
      this.batchDialogVisible = true
    },
    handleQuickApprove() {
      this.$message({
        message: '快速审批功能开发中...',
        type: 'info'
      })
    },
    confirmApprove() {
      if (!this.approveTemp.reason.trim()) {
        this.$message.error('请输入审批意见')
        return
      }
      this.$message({
        message: '审批成功',
        type: 'success'
      })
      this.approveDialogVisible = false
      this.getList()
    },
    confirmForward() {
      if (!this.forwardTemp.newApproverId) {
        this.$message.error('请选择转发对象')
        return
      }
      this.$message({
        message: '转发成功',
        type: 'success'
      })
      this.forwardDialogVisible = false
      this.getList()
    },
    confirmBatchApprove() {
      if (!this.batchTemp.reason.trim()) {
        this.$message.error('请输入审批意见')
        return
      }
      this.$message({
        message: `批量审批成功，处理了 ${this.selectedApplications.length} 个申请`,
        type: 'success'
      })
      this.batchDialogVisible = false
      this.getList()
    },
    isOverdue(row) {
      return row.waitingDays > 3
    },
    getTypeText(type) {
      const typeMap = {
        'LEAVE': '请假申请',
        'OVERTIME': '加班申请',
        'TRANSFER': '调岗申请',
        'RESIGNATION': '离职申请',
        'EQUIPMENT': '设备申请',
        'REIMBURSEMENT': '报销申请',
        'OTHER': '其他申请'
      }
      return typeMap[type]
    },
    getPriorityType(priority) {
      const priorityMap = {
        'LOW': 'info',
        'NORMAL': 'primary',
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
.pending-approvals-container {
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

.stat-card.urgent {
  border-left-color: #F56C6C;
}

.stat-card.overdue {
  border-left-color: #E6A23C;
}

.stat-card.today {
  border-left-color: #67C23A;
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

.application-title {
  color: #409EFF;
  cursor: pointer;
}

.application-title:hover {
  color: #66b1ff;
}

.overdue-text {
  color: #F56C6C;
  font-weight: bold;
}
</style>