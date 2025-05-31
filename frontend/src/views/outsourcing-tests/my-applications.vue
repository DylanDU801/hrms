<template>
  <div class="my-applications-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>我的申请</span>
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
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
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
        <el-table-column prop="currentApproverName" label="当前审批人" width="120"></el-table-column>
        <el-table-column prop="approveTime" label="审批时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.approveTime) || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">详情</el-button>
            <el-button 
              size="mini" 
              type="warning" 
              @click="handleCancel(scope.row)" 
              v-if="scope.row.status === 'PENDING'"
            >
              撤回
            </el-button>
            <el-button 
              size="mini" 
              type="success" 
              @click="handleEdit(scope.row)" 
              v-if="scope.row.status === 'PENDING'"
            >
              编辑
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
        <el-form-item label="附件">
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :file-list="fileList"
            :on-change="handleFileChange"
            :limit="1">
            <el-button size="small" type="primary">选择文件</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png/pdf文件，且不超过2MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'MyApplications',
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
      fileList: [],
      dialogFormVisible: false,
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
            status: 'PENDING',
            priority: 'NORMAL',
            applyTime: '2025-05-15 10:30:00',
            currentApproverName: '李经理',
            approveTime: null
          },
          {
            id: 2,
            applicationType: 'OVERTIME',
            title: '项目加班申请',
            content: '因项目紧急需求，申请本周末加班完成测试任务。',
            status: 'APPROVED',
            priority: 'HIGH',
            applyTime: '2025-05-14 14:20:00',
            currentApproverName: '李经理',
            approveTime: '2025-05-14 16:30:00'
          },
          {
            id: 3,
            applicationType: 'EQUIPMENT',
            title: '笔记本电脑申请',
            content: '当前电脑配置较低，影响开发效率，申请更换高配置笔记本。',
            status: 'IN_REVIEW',
            priority: 'NORMAL',
            applyTime: '2025-05-13 09:15:00',
            currentApproverName: '张主管',
            approveTime: null
          }
        ]
        this.total = 3
        this.listLoading = false
      }, 1000)
    },
    getStats() {
      // 模拟统计数据
      this.stats = {
        total: 8,
        pending: 2,
        approved: 5,
        rejected: 1
      }
    },
    handleFilter() {
      this.listQuery.page = 0
      this.getList()
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
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDetail(row) {
      this.$router.push({ path: `/applications/${row.id}` })
    },
    handleCancel(row) {
      this.$confirm('确认撤回该申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '撤回成功!'
        })
        this.getList()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.$message({
            message: '提交成功',
            type: 'success'
          })
          this.dialogFormVisible = false
          this.getList()
        }
      })
    },
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
    handleFileChange(file, fileList) {
      this.fileList = fileList
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
.my-applications-container {
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