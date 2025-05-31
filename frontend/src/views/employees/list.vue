<template>
  <div class="employees-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>员工列表</span>
        <el-button 
          style="float: right; padding: 3px 0" 
          type="text" 
          @click="handleAdd"
        >
          添加员工
        </el-button>
      </div>
      
      <!-- 搜索区域 -->
      <div class="filter-container">
        <el-input
          v-model="listQuery.keyword"
          placeholder="搜索员工姓名"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-button 
          class="filter-item" 
          type="primary" 
          icon="el-icon-search" 
          @click="handleFilter"
        >
          搜索
        </el-button>
        <el-button 
          class="filter-item" 
          style="margin-left: 10px;" 
          type="primary" 
          icon="el-icon-edit" 
          @click="handleCreate"
        >
          添加
        </el-button>
      </div>

      <!-- 表格 -->
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
      >
        <el-table-column 
          label="ID" 
          prop="id" 
          sortable="custom" 
          align="center" 
          width="80"
        />
        <el-table-column label="姓名" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleDetail(row)">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="邮箱" align="center">
          <template slot-scope="{row}">
            <span>{{ row.email }}</span>
          </template>
        </el-table-column>
        <el-table-column label="职位" align="center">
          <template slot-scope="{row}">
            <span>{{ row.position }}</span>
          </template>
        </el-table-column>
        <el-table-column label="部门" align="center">
          <template slot-scope="{row}">
            <span>{{ row.departmentName }}</span>
          </template>
        </el-table-column>
        <el-table-column 
          label="操作" 
          align="center" 
          width="230" 
          class-name="small-padding fixed-width"
        >
          <template slot-scope="{row, $index}">
            <el-button type="primary" size="mini" @click="handleDetail(row)">
              详情
            </el-button>
            <el-button type="success" size="mini" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button 
              v-if="row.status !== 'deleted'" 
              size="mini" 
              type="danger" 
              @click="handleDelete(row, $index)"
            >
              删除
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

    <!-- 编辑对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form 
        ref="dataForm" 
        :rules="rules" 
        :model="temp" 
        label-position="left" 
        label-width="70px" 
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="temp.email" />
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="temp.position" />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="temp.departmentId" class="filter-item" placeholder="请选择">
            <el-option 
              v-for="item in departmentOptions" 
              :key="item.id" 
              :label="item.name" 
              :value="item.id" 
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button 
          type="primary" 
          @click="dialogStatus === 'create' ? createData() : updateData()"
        >
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, createEmployee, updateEmployee, deleteEmployee } from '@/api/employee'
import Pagination from '@/components/Pagination'

export default {
  name: 'EmployeeList',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        keyword: undefined
      },
      departmentOptions: [
        { id: 1, name: '研发部' },
        { id: 2, name: '人事部' },
        { id: 3, name: '测试部' },
        { id: 4, name: '产品部' }
      ],
      temp: {
        id: undefined,
        name: '',
        email: '',
        position: '',
        departmentId: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        name: [{ required: true, message: '姓名是必填项', trigger: 'blur' }],
        email: [
          { required: true, message: '邮箱是必填项', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        // 兼容不同的响应格式
        if (Array.isArray(response.data)) {
          this.list = response.data
          this.total = response.data.length
        } else {
          this.list = response.data.content || response.data || []
          this.total = response.data.totalElements || response.data.total || this.list.length
        }
        this.listLoading = false
      }).catch(() => {
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
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        email: '',
        position: '',
        departmentId: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createEmployee(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          }).catch(() => {
            this.$notify({
              title: '错误',
              message: '创建失败',
              type: 'error',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateEmployee(tempData.id, tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
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
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该员工, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteEmployee(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.list.splice(index, 1)
        }).catch(() => {
          this.$notify({
            title: '错误',
            message: '删除失败',
            type: 'error',
            duration: 2000
          })
        })
      })
    },
    handleDetail(row) {
      this.$router.push({ path: `/employees/${row.id}` })
    },
    handleAdd() {
      this.$router.push({ path: '/employees/add' })
    }
  }
}
</script>

<style scoped>
.employees-container {
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
.link-type {
  color: #409EFF;
  cursor: pointer;
}
.link-type:hover {
  color: #66b1ff;
}
</style>