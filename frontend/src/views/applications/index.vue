<template>
  <div class="applications-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>申请审批管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">提交申请</el-button>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ stats.total }}</div>
              <div class="stat-label">总申请数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card pending">
              <div class="stat-number">{{ stats.pending }}</div>
              <div class="stat-label">待审批</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card approved">
              <div class="stat-number">{{ stats.approved }}</div>
              <div class="stat-label">已批准</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card rejected">
              <div class="stat-number">{{ stats.rejected }}</div>
              <div class="stat-label">已拒绝</div>
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
        <el-select v-model="listQuery.status" placeholder="状态" clearable style="width: 120px" class="filter-item">
          <el-option label="待审批" value="PENDING"></el-option>
          <el-option label="审批中" value="IN_REVIEW"></el-option>
          <el-option label="已批准" value="APPROVED"></el-option>
          <el-option label="已拒绝" value="REJECTED"></el-option>
        </el-select>
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
<!--        <el-button class="filter-item" type="success" @click="handleMyApplications">我的申请</el-button>-->
<!--        <el-button class="filter-item" type="warning" @click="handlePendingApprovals">待我审批</el-button>-->
      </div>

      <!-- 表格 -->
      <el-table :data="list" v-loading="listLoading" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="申请标题" width="200"></el-table-column>
        <el-table-column prop="applicationType" label="申请类型" width="120">
          <template slot-scope="scope">
            <el-tag>{{ getTypeText(scope.row.applicationType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicant.name" label="申请人" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityType(scope.row.priority)" size="small">{{ getWarningText(scope.row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.applyTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="currentApprover.name" label="当前审批人" width="120"></el-table-column>
        <el-table-column prop="applyTime" label="审批时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.approveTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">详情</el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleApprove(scope.row)"
              v-if="canApprove(scope.row)"
            >
              审批
            </el-button>
            <el-button
              size="mini"
              type="warning"
              @click="handleCancel(scope.row)"
              v-if="canCancel(scope.row)"
            >
              撤回
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.size"
        @pagination="getList"
      />
    </el-card>

    <!-- 申请对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px">
        <el-form-item label="申请类型" prop="applicationType">
          <el-select v-model="temp.applicationType" placeholder="请选择申请类型">
            <el-option label="请假申请" value="LEAVE"></el-option>
            <el-option label="加班申请" value="OVERTIME"></el-option>
            <el-option label="调岗申请" value="TRANSFER"></el-option>
            <el-option label="设备申请" value="EQUIPMENT"></el-option>
            <el-option label="报销申请" value="REIMBURSEMENT"></el-option>
            <el-option label="其他申请" value="OTHER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="申请标题" prop="title">
          <el-input v-model="temp.title" />
        </el-form-item>
        <el-form-item label="申请内容" prop="content">
          <el-input type="textarea" :rows="5" v-model="temp.content" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="temp.priority" placeholder="请选择优先级">
            <el-option label="低" value="LOW"></el-option>
            <el-option label="普通" value="NORMAL"></el-option>
            <el-option label="高" value="HIGH"></el-option>
            <el-option label="紧急" value="URGENT"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确认</el-button>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog title="审批申请" :visible.sync="approveDialogVisible" width="500px">
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
import Pagination from '@/components/Pagination'
import {fetchList} from "@/api/employee";
import {fetchApplications, fetchApplication, submitApplication, approveApplication, cancelApplication,forwardApplication,getMyApplications,getPendingApprovals,getApplicationStatistics,batchApproveApplications,downloadAttachment} from '@/api/application'

export default {
  name: 'Applications',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        size: 20,
        type: undefined,
        status: undefined
      },
      stats: {
        total: 0,
        pending: 0,
        approved: 0,
        rejected: 0
      },
      temp: {
        id: undefined,
        applicationType: '',
        title: '',
        content: '',
        priority: 'NORMAL'
      },
      approveTemp: {
        id: undefined,
        title: '',
        content: '',
        approved: true,
        reason: ''
      },
      currentApplication: null,
      fileList: [],
      dialogFormVisible: false,
      approveDialogVisible: false,
      detailDialogVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑申请',
        create: '提交申请'
      },
      rules: {
        applicationType: [{ required: true, message: '申请类型是必填项', trigger: 'change' }],
        title: [{ required: true, message: '申请标题是必填项', trigger: 'blur' }],
        content: [{ required: true, message: '申请内容是必填项', trigger: 'blur' }],
        priority: [{ required: true, message: '优先级是必填项', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getStats() // 确保调用统计数据
  },
  methods: {
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    getList() {
      this.listLoading = true
      fetchApplications(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = response.data.totalElements || 0
        this.listLoading = false
      }).catch(error => {
        console.error(error)
        this.listLoading = false
        this.$message.error('获取申请列表失败')
      })
    },
    getStats() {
      getApplicationStatistics().then(response => {
        console.log(response)

        this.stats = {
          total: response.data?.total || 0,
          pending: response.data?.pending || 0,
          approved: response.data?.approved || 0,
          rejected: response.data?.rejected || 0
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
        this.stats = {
          total: 0,
          pending: 0,
          approved: 0,
          rejected: 0
        }
        this.$message.error('获取统计数据失败')
      })
    },
    handleMyApplications() {
      getMyApplications(this.listQuery).then(response => {
        this.list = response.content || response || []
        this.total = response.totalElements || response.total || 0
      }).catch(error => {
        console.error(error)
        this.$message.error('获取我的申请失败')
      })
    },
    handlePendingApprovals() {
      getPendingApprovals(this.listQuery).then(response => {
        this.list = response.content || response || []
        this.total = response.totalElements || response.total || 0
      }).catch(error => {
        console.error(error)
        this.$message.error('获取待审批申请失败')
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          submitApplication(this.temp).then(() => {
            this.$message.success('提交成功')
            this.dialogFormVisible = false
            this.getList()
            this.getStats()
          }).catch(error => {
            console.error(error)
            this.getList()
            this.getStats()
            this.$message.error('提交申请失败')
          })
        }
      })
    },
    confirmApprove() {
      if (!this.approveTemp.reason.trim()) {
        this.$message.error('请输入审批意见')
        return
      }
      approveApplication(this.approveTemp.id,this.approveTemp.approved,this.approveTemp.reason).then(() => {
        this.$message.success('审批成功')
        this.approveDialogVisible = false
        this.getList()
        this.getStats()
      }).catch(error => {
        console.error(error)
        this.$message.error('审批操作失败')
      })
    },
    handleCancel(row) {
      this.$confirm('确认撤回该申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelApplication(row.id).then(() => {
          this.$message.success('撤回成功!')
          this.getList()
        }).catch(error => {
          console.error(error)
          this.$message.error('撤回申请失败')
        })
      })
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        applicationType: '',
        title: '',
        content: '',
        priority: 'NORMAL'
      }
      this.fileList = []
    },
    handleAdd() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDetail(row) {
      this.currentApplication = row
      console.log("this.currentApplication",this.currentApplication)
      this.detailDialogVisible = true
    },
    handleApprove(row) {
      this.approveTemp = {
        id: row.id,
        title: row.title,
        content: row.content,
        approved: true,
        reason: ''
      }
      this.approveDialogVisible = true
    },
    // handleCancel(row) {
    //   this.$confirm('确认撤回该申请?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(() => {
    //     this.$message({
    //       type: 'success',
    //       message: '撤回成功!'
    //     })
    //     this.getList()
    //   })
    // },
    // createData() {
    //   this.$refs['dataForm'].validate(valid => {
    //     if (valid) {
    //       this.$message({
    //         message: '提交成功',
    //         type: 'success'
    //       })
    //       this.dialogFormVisible = false
    //       this.getList()
    //     }
    //   })
    // },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.$message({
            message: '更新成功',
            type: 'success'
          })
          this.dialogFormVisible = false
          this.getList()
        }
      })
    },
    // confirmApprove() {
    //   if (!this.approveTemp.reason.trim()) {
    //     this.$message.error('请输入审批意见')
    //     return
    //   }
    //   this.$message({
    //     message: '审批成功',
    //     type: 'success'
    //   })
    //   this.approveDialogVisible = false
    //   this.getList()
    // },
    handleFileChange(file, fileList) {
      this.fileList = fileList
    },
    canApprove(row) {
      return row.status === 'PENDING' || row.status === 'IN_REVIEW'
    },
    canCancel(row) {
      return row.status === 'PENDING' && row.applicantName === '当前用户'
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
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString()
    }
  }
}
</script>

<style scoped>
.applications-container {
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

.stat-card.approved {
  border-left-color: #67C23A;
}

.stat-card.rejected {
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
