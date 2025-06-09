<template>
  <div class="my-applications-container">
    <el-card class="box-card">
      <el-table
        :data="applications"
        style="width: 100%"
        border
        v-loading="listLoading"
      >
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="申请标题" width="200"></el-table-column>
        <el-table-column prop="type" label="申请类型" width="120">
          <template slot-scope="scope">
            {{ getTypeText(scope.row.applicationType) }}
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
        <el-table-column prop="applyTime" label="申请时间" width="160"></el-table-column>
        <el-table-column prop="currentApprover.name" label="当前审批人" width="120"></el-table-column>
        <el-table-column prop="applyTime" label="审批时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.approveTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="warning" v-if="scope.row.status === '待审批'">撤回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

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
              {{ currentApplication.priority }}
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
import { getMyApplications, cancelApplication } from '@/api/application'

export default {
  name: 'MyApplications',
  data() {
    return {
      detailDialogVisible: false,
      currentApplication: null,
      applications: [],
      listLoading: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleDetail(row) {
      console.log(row)
      this.currentApplication = row
      this.detailDialogVisible = true
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
    getTypeText(type) {
      console.log(type)
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
    getPriorityType(priority) {
      const priorityMap = {
        'LOW': 'info',
        'NORMAL': 'primary',
        'HIGH': 'warning',
        'URGENT': 'danger'
      }
      return priorityMap[priority]
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
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString()
    },
    getStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return statusMap[status]
    },
    async fetchData() {
      this.listLoading = true
      try {
        const { data } = await getMyApplications()
        this.applications = data
      } catch (error) {
        console.error(error)
        this.$message.error('获取申请列表失败')
      } finally {
        this.listLoading = false
      }
    },
    handleAdd() {
      this.$router.push('/applications/create')
    },
    // handleDetail(row) {
    //   this.$router.push(`/applications/detail/${row.id}`)
    // },
    async handleCancel(row) {
      try {
        await this.$confirm('确定要撤回该申请吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await cancelApplication(row.id)
        this.$message.success('撤回成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('撤回失败')
        }
      }
    }
  }
}
</script>

<style scoped>
.my-applications-container {
  padding: 20px;
}
</style>
