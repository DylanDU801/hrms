<template>
  <div class="outsourcing-tests-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>外包测试管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">创建测试任务</el-button>
      </div>

      <!-- 搜索区域 -->
      <div class="filter-container">
        <el-input
          v-model="listQuery.keyword"
          placeholder="搜索测试任务"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select v-model="listQuery.status" placeholder="状态" clearable style="width: 120px" class="filter-item">
          <el-option label="待分配" value="PENDING"></el-option>
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
        <el-table-column prop="tester.name" label="测试人员" width="120"></el-table-column>
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
        <el-table-column prop="score" label="得分" width="80"></el-table-column>
        <el-table-column label="操作" width="250" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="success" @click="handleEdit(scope.row)" v-if="scope.row.status === 'PENDING'">编辑</el-button>
<!--            <el-button size="mini" type="warning" @click="handleAssign(scope.row)" v-if="scope.row.status === 'PENDING'">分配</el-button>-->
            <el-button size="mini" type="info" @click="handleReview(scope.row)" v-if="scope.row.status === 'SUBMITTED'">审核</el-button>
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

    <!-- 创建/编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px">
        <el-form-item label="测试名称" prop="testName">
          <el-input v-model="temp.testName" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" :rows="3" v-model="temp.description" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="temp.priority" placeholder="请选择优先级">
            <el-option label="低" value="LOW"></el-option>
            <el-option label="中" value="MEDIUM"></el-option>
            <el-option label="高" value="HIGH"></el-option>
            <el-option label="紧急" value="URGENT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="测试人员">
          <el-select v-model="temp.tester.id" class="filter-item" placeholder="请选择">
            <el-option
                v-for="item in employeesOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预计工时" prop="estimatedHours">
          <el-input-number v-model="temp.estimatedHours" :min="1" :max="200" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="temp.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="temp.endTime"
            type="datetime"
            placeholder="选择结束时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确认</el-button>
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
import Pagination from '@/components/Pagination'
import { fetchTests, fetchTest, createTest, updateTest, deleteTest, assignTest, submitTestResult, reviewTest, getMyTests, getTestStatistics, getTesterPerformance } from '@/api/outsourcing-test'
import {fetchList} from "@/api/employee";

export default {
  name: 'OutsourcingTests',
  components: { Pagination },
  data() {
    return {
      employeesOptions:[],
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        size: 20,
        keyword: undefined,
        status: undefined
      },
      temp: {
        id: '',
        testName: '',
        description: '',
        priority: 'MEDIUM',
        estimatedHours: 8,
        startTime: '',
        endTime: '',
        tester:{
          id:''
        }
      },
      assignTemp: {
        testId: undefined,
        testerId: undefined
      },
      reviewTemp: {
        testId: undefined,
        result: '',
        score: 0,
        comment: ''
      },
      testers: [
        { id: 1, name: '张测试' },
        { id: 2, name: '李测试' }
      ],
      dialogFormVisible: false,
      assignDialogVisible: false,
      reviewDialogVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑测试任务',
        create: '创建测试任务'
      },
      rules: {
        testName: [{ required: true, message: '测试名称是必填项', trigger: 'blur' }],
        description: [{ required: true, message: '描述是必填项', trigger: 'blur' }],
        priority: [{ required: true, message: '优先级是必填项', trigger: 'change' }],
        estimatedHours: [{ required: true, message: '预计工时是必填项', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getfetchListList()
  },
  methods: {
    getfetchListList(){
      fetchList({page: 1, size: 2000}).then(response => {
        console.log(response)

        this.employeesOptions = response.content
      }).catch((error) => {
        console.log(error)
      })

    },
    getList() {
      this.listLoading = true
      fetchTests(this.listQuery).then(response => {
        console.log("response",response)
        if (Array.isArray(response.content)) {
          this.list = response.content
          this.total = response.totalElements
        } else {
          this.list = response.content || response || []
          this.total = response.totalElements
        }
        this.listLoading = false
      }).catch((error) => {
        console.log(error)
        this.listLoading = false
        // 如果API失败，使用模拟数据
        this.list = [
          { id: 1, name: '张三', email: 'zhangsan@example.com', position: '前端工程师', departmentName: '研发部' },
          { id: 2, name: '李四', email: 'lisi@example.com', position: '后端工程师', departmentName: '研发部' }
        ]
        this.total = 2
      })
    },
    handleFilter() {
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: '',
        testName: '',
        description: '',
        priority: 'MEDIUM',
        estimatedHours: 8,
        startTime: '',
        endTime: '',
        tester:{
          id:''
        }
      }
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
      this.$router.push({ path: `/outsourcing-tests/${row.id}` })
    },
    handleAssign(row) {
      this.assignTemp.testId = row.id
      this.assignTemp.testerId = undefined
      this.assignDialogVisible = true
    },
    handleReview(row) {
      this.reviewTemp.testId = row.id
      this.reviewTemp.result = row.result || ''
      this.reviewTemp.score = 0
      this.reviewTemp.comment = ''
      this.reviewDialogVisible = true
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {

          createTest(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.dialogFormVisible = false
            this.getList()
          }).catch((e) => {
            console.log(e)
            this.$notify({
              title: '错误',
              message: '创建失败',
              type: 'error',
              duration: 2000
            })
            this.dialogFormVisible = false
            this.getList()
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateTest(tempData.id, tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.getList()
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          }).catch(() => {
            this.$notify({
              title: '错误',
              message: '更新失败',
              type: 'error',
              duration: 2000
            })
            this.dialogFormVisible = false
            this.getList()
          })
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
      this.getList()
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
      this.getList()
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
    }
  }
}
</script>

<style scoped>
.outsourcing-tests-container {
  padding: 20px;
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
