<template>
  <div class="test-detail-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>测试任务详情</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回</el-button>
      </div>
      
      <div v-if="testDetail">
        <!-- 基本信息 -->
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务ID">{{ testDetail.id }}</el-descriptions-item>
          <el-descriptions-item label="任务名称">{{ testDetail.testName }}</el-descriptions-item>
          <el-descriptions-item label="创建人">{{ testDetail.creatorName }}</el-descriptions-item>
          <el-descriptions-item label="测试人员">{{ testDetail.testerName }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(testDetail.status)">
              {{ getStatusText(testDetail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="优先级">
            <el-tag :type="getPriorityType(testDetail.priority)">
              {{ testDetail.priority }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="预计工时">{{ testDetail.estimatedHours }}小时</el-descriptions-item>
          <el-descriptions-item label="实际工时">{{ testDetail.actualHours || '-' }}小时</el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ formatDate(testDetail.startTime) }}</el-descriptions-item>
          <el-descriptions-item label="结束时间">{{ formatDate(testDetail.endTime) }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(testDetail.createdTime) }}</el-descriptions-item>
          <el-descriptions-item label="得分">
            <span v-if="testDetail.score">
              <el-tag :type="getScoreType(testDetail.score)">{{ testDetail.score }}分</el-tag>
            </span>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="任务描述" :span="2">
            <div style="max-height: 150px; overflow-y: auto;">{{ testDetail.description }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="测试结果" :span="2" v-if="testDetail.result">
            <div style="max-height: 150px; overflow-y: auto;">{{ testDetail.result }}</div>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 操作按钮 -->
        <div style="margin-top: 20px; text-align: center;" v-if="showActions">
          <el-button 
            type="primary" 
            @click="handleEdit" 
            v-if="canEdit"
          >
            编辑任务
          </el-button>
          <el-button 
            type="success" 
            @click="handleAssign" 
            v-if="canAssign"
          >
            分配测试人员
          </el-button>
          <el-button 
            type="warning" 
            @click="handleSubmit" 
            v-if="canSubmit"
          >
            提交结果
          </el-button>
          <el-button 
            type="info" 
            @click="handleReview" 
            v-if="canReview"
          >
            审核结果
          </el-button>
        </div>

        <!-- 时间线 -->
        <div style="margin-top: 30px;">
          <h3>任务流程</h3>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in timeline"
              :key="index"
              :icon="activity.icon"
              :type="activity.type"
              :color="activity.color"
              :timestamp="activity.timestamp">
              {{ activity.content }}
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

    <!-- 编辑对话框 -->
    <el-dialog title="编辑测试任务" :visible.sync="editDialogVisible" width="600px">
      <el-form ref="editForm" :rules="rules" :model="editTemp" label-position="left" label-width="100px">
        <el-form-item label="测试名称" prop="testName">
          <el-input v-model="editTemp.testName" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" :rows="3" v-model="editTemp.description" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="editTemp.priority" placeholder="请选择优先级">
            <el-option label="低" value="LOW"></el-option>
            <el-option label="中" value="MEDIUM"></el-option>
            <el-option label="高" value="HIGH"></el-option>
            <el-option label="紧急" value="URGENT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预计工时" prop="estimatedHours">
          <el-input-number v-model="editTemp.estimatedHours" :min="1" :max="200" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmEdit">确认</el-button>
      </div>
    </el-dialog>

    <!-- 分配对话框 -->
    <el-dialog title="分配测试人员" :visible.sync="assignDialogVisible" width="400px">
      <el-form ref="assignForm" :model="assignTemp" label-width="100px">
        <el-form-item label="测试人员">
          <el-select v-model="assignTemp.testerId" placeholder="请选择测试人员">
            <el-option
              v-for="tester in testers"
              :key="tester.id"
              :label="tester.name"
              :value="tester.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAssign">确认分配</el-button>
      </div>
    </el-dialog>

    <!-- 提交结果对话框 -->
    <el-dialog title="提交测试结果" :visible.sync="submitDialogVisible" width="600px">
      <el-form ref="submitForm" :model="submitTemp" label-width="100px">
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

    <!-- 审核对话框 -->
    <el-dialog title="审核测试结果" :visible.sync="reviewDialogVisible" width="500px">
      <el-form ref="reviewForm" :model="reviewTemp" label-width="100px">
        <el-form-item label="测试结果">
          <el-input type="textarea" :rows="4" v-model="reviewTemp.result" readonly />
        </el-form-item>
        <el-form-item label="得分">
          <el-input-number v-model="reviewTemp.score" :min="0" :max="100" />
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input type="textarea" :rows="3" v-model="reviewTemp.comment" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReview">确认审核</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'TestDetail',
  data() {
    return {
      testDetail: null,
      loading: true,
      timeline: [],
      testers: [
        { id: 1, name: '张测试' },
        { id: 2, name: '李测试' }
      ],
      editTemp: {
        testName: '',
        description: '',
        priority: '',
        estimatedHours: 0
      },
      assignTemp: {
        testerId: undefined
      },
      submitTemp: {
        result: '',
        actualHours: 0
      },
      reviewTemp: {
        result: '',
        score: 0,
        comment: ''
      },
      editDialogVisible: false,
      assignDialogVisible: false,
      submitDialogVisible: false,
      reviewDialogVisible: false,
      rules: {
        testName: [{ required: true, message: '测试名称是必填项', trigger: 'blur' }],
        description: [{ required: true, message: '描述是必填项', trigger: 'blur' }],
        priority: [{ required: true, message: '优先级是必填项', trigger: 'change' }],
        estimatedHours: [{ required: true, message: '预计工时是必填项', trigger: 'blur' }]
      }
    }
  },
  computed: {
    showActions() {
      return this.canEdit || this.canAssign || this.canSubmit || this.canReview
    },
    canEdit() {
      return this.testDetail && this.testDetail.status === 'PENDING'
    },
    canAssign() {
      return this.testDetail && this.testDetail.status === 'PENDING'
    },
    canSubmit() {
      return this.testDetail && this.testDetail.status === 'IN_PROGRESS'
    },
    canReview() {
      return this.testDetail && this.testDetail.status === 'SUBMITTED'
    }
  },
  created() {
    this.getTestDetail()
  },
  methods: {
    getTestDetail() {
      this.loading = true
      const id = this.$route.params.id
      
      // 模拟API调用
      setTimeout(() => {
        this.testDetail = {
          id: id,
          testName: '用户登录功能测试',
          description: '测试用户登录、注册、密码重置等核心功能，包括正常流程和异常流程的测试。',
          creatorName: '项目经理',
          testerName: '张测试',
          status: 'IN_PROGRESS',
          priority: 'HIGH',
          estimatedHours: 16,
          actualHours: null,
          score: null,
          startTime: '2025-05-15 10:30:00',
          endTime: '2025-05-20 18:00:00',
          createdTime: '2025-05-14 14:20:00',
          result: null
        }
        
        this.generateTimeline()
        this.loading = false
      }, 1000)
    },
    generateTimeline() {
      this.timeline = [
        {
          content: '任务创建',
          timestamp: this.testDetail.createdTime,
          icon: 'el-icon-plus',
          type: 'primary'
        },
        {
          content: '分配测试人员: ' + this.testDetail.testerName,
          timestamp: '2025-05-15 09:00:00',
          icon: 'el-icon-user',
          type: 'success'
        }
      ]
      
      if (this.testDetail.status === 'IN_PROGRESS') {
        this.timeline.push({
          content: '开始测试',
          timestamp: this.testDetail.startTime,
          icon: 'el-icon-video-play',
          type: 'warning'
        })
      }
      
      if (this.testDetail.status === 'COMPLETED' && this.testDetail.result) {
        this.timeline.push({
          content: '提交测试结果',
          timestamp: '2025-05-18 16:30:00',
          icon: 'el-icon-upload',
          type: 'info'
        })
        
        if (this.testDetail.score) {
          this.timeline.push({
            content: `审核完成，得分: ${this.testDetail.score}分`,
            timestamp: '2025-05-19 10:00:00',
            icon: 'el-icon-check',
            type: 'success'
          })
        }
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    handleEdit() {
      this.editTemp = {
        testName: this.testDetail.testName,
        description: this.testDetail.description,
        priority: this.testDetail.priority,
        estimatedHours: this.testDetail.estimatedHours
      }
      this.editDialogVisible = true
    },
    handleAssign() {
      this.assignTemp.testerId = undefined
      this.assignDialogVisible = true
    },
    handleSubmit() {
      this.submitTemp = {
        result: '',
        actualHours: this.testDetail.estimatedHours
      }
      this.submitDialogVisible = true
    },
    handleReview() {
      this.reviewTemp = {
        result: this.testDetail.result || '',
        score: 0,
        comment: ''
      }
      this.reviewDialogVisible = true
    },
    confirmEdit() {
      this.$refs['editForm'].validate(valid => {
        if (valid) {
          this.$message({
            message: '更新成功',
            type: 'success'
          })
          this.editDialogVisible = false
          this.getTestDetail()
        }
      })
    },
    confirmAssign() {
      if (!this.assignTemp.testerId) {
        this.$message.error('请选择测试人员')
        return
      }
      this.$message({
        message: '分配成功',
        type: 'success'
      })
      this.assignDialogVisible = false
      this.getTestDetail()
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
      this.getTestDetail()
    },
    confirmReview() {
      if (this.reviewTemp.score === 0) {
        this.$message.error('请输入得分')
        return
      }
      this.$message({
        message: '审核成功',
        type: 'success'
      })
      this.reviewDialogVisible = false
      this.getTestDetail()
    },
    getStatusType(status) {
      const statusMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'SUBMITTED': 'primary',
        'COMPLETED': 'success'
      }
      return statusMap[status]
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待分配',
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
    getScoreType(score) {
      if (score >= 90) return 'success'
      if (score >= 80) return 'primary'
      if (score >= 70) return 'warning'
      return 'danger'
    },
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString()
    }
  }
}
</script>

<style scoped>
.test-detail-container {
  padding: 20px;
}

.el-timeline {
  padding-left: 20px;
}
</style>