<template>
  <div class="application-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>申请详情</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回</el-button>
      </div>
      
      <div v-if="applicationDetail">
        <!-- 基本信息 -->
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请ID">{{ applicationDetail.id }}</el-descriptions-item>
          <el-descriptions-item label="申请类型">{{ getTypeText(applicationDetail.applicationType) }}</el-descriptions-item>
          <el-descriptions-item label="申请标题" :span="2">{{ applicationDetail.title }}</el-descriptions-item>
          <el-descriptions-item label="申请人">{{ applicationDetail.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(applicationDetail.status)">
              {{ getStatusText(applicationDetail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="优先级">
            <el-tag :type="getPriorityType(applicationDetail.priority)" size="small">
              {{ applicationDetail.priority }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ formatDate(applicationDetail.applyTime) }}</el-descriptions-item>
          <el-descriptions-item label="当前审批人">{{ applicationDetail.currentApproverName || '无' }}</el-descriptions-item>
          <el-descriptions-item label="审批时间">{{ formatDate(applicationDetail.approveTime) || '未审批' }}</el-descriptions-item>
          <el-descriptions-item label="申请内容" :span="2">
            <div style="max-height: 150px; overflow-y: auto;">{{ applicationDetail.content }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="审批意见" :span="2" v-if="applicationDetail.approveReason">
            {{ applicationDetail.approveReason }}
          </el-descriptions-item>
          <el-descriptions-item label="附件" :span="2" v-if="applicationDetail.attachmentPath">
            <el-link type="primary" @click="downloadAttachment">下载附件</el-link>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 操作按钮 -->
        <div style="margin-top: 20px; text-align: center;" v-if="showActions">
          <el-button 
            type="primary" 
            @click="handleEdit" 
            v-if="canEdit"
          >
            编辑申请
          </el-button>
          <el-button 
            type="success" 
            @click="handleApprove(true)" 
            v-if="canApprove"
          >
            批准申请
          </el-button>
          <el-button 
            type="danger" 
            @click="handleApprove(false)" 
            v-if="canApprove"
          >
            拒绝申请
          </el-button>
          <el-button 
            type="warning" 
            @click="handleCancel" 
            v-if="canCancel"
          >
            撤回申请
          </el-button>
          <el-button 
            type="info" 
            @click="handleForward" 
            v-if="canForward"
          >
            转发申请
          </el-button>
        </div>

        <!-- 审批流程 -->
        <div style="margin-top: 30px;">
          <h3>审批流程</h3>
          <el-timeline>
            <el-timeline-item
              v-for="(step, index) in approvalFlow"
              :key="index"
              :icon="step.icon"
              :type="step.type"
              :color="step.color"
              :timestamp="step.timestamp">
              <h4>{{ step.title }}</h4>
              <p>{{ step.content }}</p>
              <p v-if="step.comment" class="approval-comment">{{ step.comment }}</p>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      
      <div v-else v-loading="loading" style="height: 200px;">
        <div style="display: flex; align-items: center; justify-content: center; height: 100%;">
          加载中...
        </div>
      </div>
    </el-card>

    <!-- 审批对话框 -->
    <el-dialog :title="approveDialogTitle" :visible.sync="approveDialogVisible" width="500px">
      <el-form ref="approveForm" :model="approveTemp" label-width="100px">
        <el-form-item label="申请标题">
          <span>{{ approveTemp.title }}</span>
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
  </div>
</template>

<script>
export default {
  name: 'ApplicationDetail',
  data() {
    return {
      applicationDetail: null,
      loading: true,
      approvalFlow: [],
      approvers: [
        { id: 1, name: '张主管' },
        { id: 2, name: '李经理' },
        { id: 3, name: '王总监' }
      ],
      approveTemp: {
        id: undefined,
        title: '',
        approved: true,
        reason: ''
      },
      forwardTemp: {
        id: undefined,
        newApproverId: undefined,
        reason: ''
      },
      approveDialogVisible: false,
      forwardDialogVisible: false
    }
  },
  computed: {
    approveDialogTitle() {
      return this.approveTemp.approved ? '批准申请' : '拒绝申请'
    },
    showActions() {
      return this.canEdit || this.canApprove || this.canCancel || this.canForward
    },
    canEdit() {
      return this.applicationDetail && 
             this.applicationDetail.status === 'PENDING' && 
             this.isOwner()
    },
    canApprove() {
      return this.applicationDetail && 
             (this.applicationDetail.status === 'PENDING' || this.applicationDetail.status === 'IN_REVIEW') &&
             this.isApprover()
    },
    canCancel() {
      return this.applicationDetail && 
             this.applicationDetail.status === 'PENDING' && 
             this.isOwner()
    },
    canForward() {
      return this.applicationDetail && 
             (this.applicationDetail.status === 'PENDING' || this.applicationDetail.status === 'IN_REVIEW') &&
             this.isApprover()
    }
  },
  created() {
    this.getApplicationDetail()
  },
  methods: {
    getApplicationDetail() {
      this.loading = true
      const id = this.$route.params.id
      
      // 模拟API调用
      setTimeout(() => {
        this.applicationDetail = {
          id: id,
          applicationType: 'LEAVE',
          title: '年假申请',
          content: '申请5月20日-5月24日年假，共5天。原因：家庭聚会，需要陪伴家人。请批准，谢谢！',
          applicantName: '张三',
          status: 'PENDING',
          priority: 'NORMAL',
          applyTime: '2025-05-15 10:30:00',
          currentApproverName: '李经理',
          approveTime: null,
          approveReason: null,
          attachmentPath: '/uploads/leave_application.pdf'
        }
        
        this.generateApprovalFlow()
        this.loading = false
      }, 1000)
    },
    generateApprovalFlow() {
      this.approvalFlow = [
        {
          title: '申请提交',
          content: `${this.applicationDetail.applicantName} 提交了${this.getTypeText(this.applicationDetail.applicationType)}`,
          timestamp: this.applicationDetail.applyTime,
          icon: 'el-icon-edit',
          type: 'primary'
        }
      ]
      
      if (this.applicationDetail.status !== 'PENDING') {
        this.approvalFlow.push({
          title: '开始审批',
          content: `${this.applicationDetail.currentApproverName} 开始审批`,
          timestamp: '2025-05-15 11:00:00',
          icon: 'el-icon-view',
          type: 'warning'
        })
      }
      
      if (this.applicationDetail.status === 'APPROVED') {
        this.approvalFlow.push({
          title: '审批通过',
          content: `${this.applicationDetail.currentApproverName} 批准了申请`,
          comment: this.applicationDetail.approveReason,
          timestamp: this.applicationDetail.approveTime,
          icon: 'el-icon-check',
          type: 'success'
        })
      } else if (this.applicationDetail.status === 'REJECTED') {
        this.approvalFlow.push({
          title: '审批拒绝',
          content: `${this.applicationDetail.currentApproverName} 拒绝了申请`,
          comment: this.applicationDetail.approveReason,
          timestamp: this.applicationDetail.approveTime,
          icon: 'el-icon-close',
          type: 'danger'
        })
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    handleEdit() {
      // 跳转到编辑页面或打开编辑对话框
      this.$message({
        message: '编辑功能开发中...',
        type: 'info'
      })
    },
    handleApprove(approved) {
      this.approveTemp = {
        id: this.applicationDetail.id,
        title: this.applicationDetail.title,
        approved: approved,
        reason: ''
      }
      this.approveDialogVisible = true
    },
    handleCancel() {
      this.$confirm('确认撤回该申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '撤回成功!'
        })
        this.getApplicationDetail()
      })
    },
    handleForward() {
      this.forwardTemp = {
        id: this.applicationDetail.id,
        newApproverId: undefined,
        reason: ''
      }
      this.forwardDialogVisible = true
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
      this.getApplicationDetail()
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
      this.getApplicationDetail()
    },
    downloadAttachment() {
      this.$message({
        message: '下载功能开发中...',
        type: 'info'
      })
    },
    isOwner() {
      // 模拟判断当前用户是否是申请人
      return this.applicationDetail.applicantName === '当前用户'
    },
    isApprover() {
      // 模拟判断当前用户是否是审批人
      return true // 简化处理，实际应该检查当前用户权限
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
.application-detail-container {
  padding: 20px;
}

.el-timeline {
  padding-left: 20px;
}

.approval-comment {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  margin-top: 5px;
  font-style: italic;
  color: #606266;
}
</style>