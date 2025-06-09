<template>
  <div class="logs-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>操作日志</span>
        <el-dropdown style="float: right;">
          <el-button type="text">
            更多操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="handleExport">导出日志</el-dropdown-item>
            <el-dropdown-item @click.native="handleCleanup">清理日志</el-dropdown-item>
            <el-dropdown-item @click.native="handleSecurityEvents">安全事件</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-container">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ stats.total }}</div>
              <div class="stat-label">总日志数</div>
            </div>
          </el-col>
<!--          <el-col :span="6">-->
<!--            <div class="stat-card today">-->
<!--              <div class="stat-number">{{ stats.today }}</div>-->
<!--              <div class="stat-label">今日操作</div>-->
<!--            </div>-->
<!--          </el-col>-->
          <el-col :span="6">
            <div class="stat-card success">
              <div class="stat-number">{{ stats.success }}</div>
              <div class="stat-label">成功操作</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card failed">
              <div class="stat-number">{{ stats.failed }}</div>
              <div class="stat-label">失败操作</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 表格 -->
      <el-table :data="list" v-loading="listLoading" border style="width: 100%" @row-click="handleRowClick">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="user.username" label="用户" width="120"></el-table-column>
        <el-table-column prop="operation" label="操作" width="120">
          <template slot-scope="scope">
            <el-tag :type="getOperationType(scope.row.operation)">{{ scope.row.operation }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="method" label="方法" width="80">
          <template slot-scope="scope">
            <el-tag :type="getMethodType(scope.row.method)" size="mini">{{ scope.row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="IP地址" width="140"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionTime" label="耗时" width="80">
          <template slot-scope="scope">
            <span :class="{ 'slow-operation': scope.row.executionTime > 1000 }">
              {{ scope.row.executionTime }}ms
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="操作时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">详情</el-button>
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
    <el-dialog title="操作日志详情" :visible.sync="detailDialogVisible" width="800px">
      <div v-if="currentLog">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="日志ID">{{ currentLog.id }}</el-descriptions-item>
          <el-descriptions-item label="用户">{{ currentLog.username }}</el-descriptions-item>
          <el-descriptions-item label="操作">{{ currentLog.operation }}</el-descriptions-item>
          <el-descriptions-item label="方法">{{ currentLog.method }}</el-descriptions-item>
          <el-descriptions-item label="IP地址">{{ currentLog.ipAddress }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentLog.status)">{{ getStatusText(currentLog.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="执行时间">{{ currentLog.executionTime }}ms</el-descriptions-item>
          <el-descriptions-item label="操作时间">{{ formatDate(currentLog.createdTime) }}</el-descriptions-item>
          <el-descriptions-item label="用户代理" :span="2">
            <div style="max-height: 100px; overflow-y: auto; word-break: break-all;">
              {{ currentLog.userAgent }}
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="请求参数" :span="2">
            <div style="max-height: 200px; overflow-y: auto;">
              <pre>{{ formatJson(currentLog.params) }}</pre>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="响应结果" :span="2">
            <div style="max-height: 200px; overflow-y: auto;">
              <pre>{{ formatJson(currentLog.result) }}</pre>
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 清理日志对话框 -->
    <el-dialog title="清理历史日志" :visible.sync="cleanupDialogVisible" width="400px">
      <el-form ref="cleanupForm" :model="cleanupTemp" label-width="120px">
        <el-form-item label="保留天数">
          <el-input-number v-model="cleanupTemp.retentionDays" :min="1" :max="365" />
          <div style="margin-top: 5px; color: #909399; font-size: 12px;">
            将删除 {{ cleanupTemp.retentionDays }} 天前的所有日志
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cleanupDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmCleanup">确认清理</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { fetchLogs, fetchLogStats, exportLogs, cleanupLogs} from '@/api/log'

export default {
  name: 'OperationLogs',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      dateRange: [],
      listQuery: {
        page: 1,
        limit: 20,
        userId: undefined,
        operation: undefined,
        status: undefined,
        startDate: undefined,
        endDate: undefined
      },
      stats: {
        total: 0,
        today: 0,
        success: 0,
        failed: 0
      },
      users: [
        { id: 1, name: 'admin' },
        { id: 2, name: 'hr001' },
        { id: 3, name: 'pm001' }
      ],
      currentLog: null,
      cleanupTemp: {
        retentionDays: 30
      },
      detailDialogVisible: false,
      cleanupDialogVisible: false
    }
  },
  watch: {
    dateRange(val) {
      if (val && val.length === 2) {
        this.listQuery.startDate = val[0]
        this.listQuery.endDate = val[1]
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
    }
  },
  created() {
    this.getList()
    // this.getStats()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await fetchLogs(this.listQuery)
        this.list = response.data.content
        this.total = response.data.totalElements
        this.stats.total = response.data.totalElements
      } catch (error) {
        console.error(error)
        this.$message.error('获取日志列表失败')
      } finally {
        this.listLoading = false
      }
    },
    async getStats() {
      try {
        const response = await fetchLogStats()
        this.stats = response.data
      } catch (error) {
        console.error(error)
      }
    },
    // async handleExport() {
    //   try {
    //     const response = await exportLogs(this.listQuery)
    //     const url = window.URL.createObjectURL(new Blob([response]))
    //     const link = document.createElement('a')
    //     link.href = url
    //     link.setAttribute('download', `操作日志_${new Date().toISOString()}.xlsx`)
    //     document.body.appendChild(link)
    //     link.click()
    //     this.$message.success('导出成功')
    //   } catch (error) {
    //     console.error(error)
    //     this.$message.error('导出失败')
    //   }
    // },
    // async confirmCleanup() {
    //   try {
    //     await this.$confirm(`确认删除 ${this.cleanupTemp.retentionDays} 天前的所有日志?`, '警告', {
    //       confirmButtonText: '确定',
    //       cancelButtonText: '取消',
    //       type: 'warning'
    //     })
    //     await cleanupLogs(this.cleanupTemp.retentionDays)
    //     this.$message.success('清理完成')
    //     this.cleanupDialogVisible = false
    //     this.getList()
    //     this.getStats()
    //   } catch (error) {
    //     if (error !== 'cancel') {
    //       console.error(error)
    //       this.$message.error('清理失败')
    //     }
    //   }
    // },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        userId: undefined,
        operation: undefined,
        status: undefined,
        startDate: undefined,
        endDate: undefined
      }
      this.dateRange = []
      this.getList()
    },
    handleRowClick(row) {
      this.handleDetail(row)
    },
    handleDetail(row) {
      this.currentLog = row
      this.detailDialogVisible = true
    },
    handleExport() {
      this.$message({
        message: '导出功能开发中...',
        type: 'info'
      })
    },
    handleCleanup() {
      this.cleanupDialogVisible = true
    },
    handleSecurityEvents() {
      this.$message({
        message: '安全事件分析功能开发中...',
        type: 'info'
      })
    },
    confirmCleanup() {
      this.$confirm(`确认删除 ${this.cleanupTemp.retentionDays} 天前的所有日志?`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        try {
          cleanupLogs(this.cleanupTemp.retentionDays)
          this.getList()
        } catch (error) {
          this.getList()
          console.error(error)
          this.$message.error('获取日志列表失败')
        } finally {
          this.$message({
            type: 'success',
            message: '清理完成!'
          })
          this.cleanupDialogVisible = false
          this.getList()
        }
      })
    },
    getOperationType(operation) {
      const typeMap = {
        'LOGIN': 'success',
        'LOGOUT': 'info',
        'CREATE': 'primary',
        'UPDATE': 'warning',
        'DELETE': 'danger',
        'SELECT': ''
      }
      return typeMap[operation] || ''
    },
    getMethodType(method) {
      const typeMap = {
        'GET': 'success',
        'POST': 'primary',
        'PUT': 'warning',
        'DELETE': 'danger'
      }
      return typeMap[method] || ''
    },
    getStatusType(status) {
      const statusMap = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'ERROR': 'warning'
      }
      return statusMap[status]
    },
    getStatusText(status) {
      const statusMap = {
        'SUCCESS': '成功',
        'FAILED': '失败',
        'ERROR': '错误'
      }
      return statusMap[status]
    },
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString()
    },
    formatJson(jsonString) {
      if (!jsonString) return ''
      try {
        return JSON.stringify(JSON.parse(jsonString), null, 2)
      } catch (e) {
        return jsonString
      }
    }
  }
}
</script>

<style scoped>
.logs-container {
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

.stat-card.today {
  border-left-color: #67C23A;
}

.stat-card.success {
  border-left-color: #67C23A;
}

.stat-card.failed {
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

.slow-operation {
  color: #F56C6C;
  font-weight: bold;
}

pre {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.el-table tbody tr {
  cursor: pointer;
}

.el-table tbody tr:hover {
  background-color: #f5f7fa;
}
</style>
