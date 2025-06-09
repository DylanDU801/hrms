<template>
  <div class="pending-approvals-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>待我审批</span>
      </div>

      <el-table
        :data="pendingApplications"
        style="width: 100%"
        border
        v-loading="listLoading"
      >
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="申请标题" width="200"></el-table-column>
        <el-table-column prop="applicant.name" label="申请人" width="120"></el-table-column>
        <el-table-column prop="applicationType" label="申请类型" width="120">
          <template slot-scope="scope">
            {{ getTypeText(scope.row.applicationType) }}
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.applyTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{getStatusText(scope.row.status)}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityType(scope.row.priority)" size="small">{{ getWarningText(scope.row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="success" @click="handleApprove(scope.row, true)">批准</el-button>
            <el-button size="mini" type="danger" @click="handleApprove(scope.row, false)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>


    <el-dialog title="审批意见" :visible.sync="approvalDialogVisible" width="500px">
      <el-form :model="approvalForm" label-width="80px">
        <el-form-item label="审批意见">
          <el-input type="textarea" v-model="approvalForm.reason" :rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmApproval">确定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="申请详情" :visible.sync="detailDialogVisible" width="600px">
      <div v-if="currentApplication">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请ID">{{ currentApplication.id }}</el-descriptions-item>
          <el-descriptions-item label="申请类型">{{ getTypeText(currentApplication.applicationType) }}</el-descriptions-item>
          <el-descriptions-item label="申请标题" :span="2">{{ currentApplication.title }}</el-descriptions-item>
          <el-descriptions-item label="申请人">{{ currentApplication.applicant.name }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentApplication.status)">
              {{ getStatusText(currentApplication.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="优先级">
            <el-tag :type="getPriorityType(currentApplication.priority)" size="small">
              {{ getWarningText(currentApplication.priority) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ formatDate(currentApplication.applyTime) }}</el-descriptions-item>
          <el-descriptions-item label="当前审批人">{{ currentApplication.currentApprover!=null ? currentApplication.currentApprover.name : '无'}}</el-descriptions-item>
          <el-descriptions-item label="审批时间">{{ formatDate(currentApplication.approveTime) || '未审批' }}</el-descriptions-item>
          <el-descriptions-item label="申请内容" :span="2">
            <div style="max-height: 150px; overflow-y: auto;">{{ currentApplication.content }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="审批意见" :span="2" v-if="currentApplication.approveReason">
            {{ currentApplication.approveReason }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { getPendingApprovals, approveApplication } from '@/api/application'

export default {
  name: 'PendingApprovals',
  data() {
    return {
      pendingApplications: [],
      listLoading: false,
      approvalDialogVisible: false,
      detailDialogVisible: false,
      approvalForm: {
        id: null,
        approved: false,
        reason: ''
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getPendingApprovals().then(response => {
        this.pendingApplications = response.data
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleDetail(row) {
      this.currentApplication = row
      console.log("this.currentApplication",this.currentApplication)
      this.detailDialogVisible = true
    },
    handleApprove(row, isApprove) {
      this.approvalForm = {
        id: row.id,
        approved: isApprove,
        reason: ''
      }
      this.approvalDialogVisible = true
    },
    confirmApproval() {
      if (!this.approvalForm.reason.trim()) {
        this.$message.error('请输入审批意见')
        return
      }
      approveApplication(this.approvalForm.id,this.approvalForm.approved,this.approvalForm.reason).then(() => {
        this.$message.success('操作成功')
        this.approvalDialogVisible = false
        this.fetchData()
      }).catch(error => {
        console.error(error)
        this.$message.error('操作失败')
      })
    },
    getWarningText(type) {
      const typeMap = {
        'LOW': '低',
        'NORMAL': '普通',
        'HIGH': '高',
        'URGENT': '紧急'
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
    getStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'IN_REVIEW': 'primary',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'CANCELLED': 'info'
      }
      return statusMap[status]
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待审批',
        'IN_REVIEW': '审批中',
        'APPROVED': '已批准',
        'REJECTED': '已拒绝',
        'CANCELLED': '已撤销'
      }
      return statusMap[status]
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
      return typeMap[type] || type
    },
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.pending-approvals-container {
  padding: 20px;
}
</style>

