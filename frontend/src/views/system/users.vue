<!-- frontend/src/views/system/users.vue -->
<template>
  <div class="users-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>用户管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">添加用户</el-button>
      </div>
      
      <!-- 搜索区域 -->
      <div class="filter-container">
        <el-input
          v-model="listQuery.keyword"
          placeholder="搜索用户名或邮箱"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select v-model="listQuery.enabled" placeholder="状态" clearable style="width: 120px" class="filter-item">
          <el-option label="启用" :value="true"></el-option>
          <el-option label="禁用" :value="false"></el-option>
        </el-select>
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      </div>

      <!-- 表格 -->
      <el-table :data="list" v-loading="listLoading" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="150"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
        <el-table-column prop="enabled" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.enabled ? 'success' : 'danger'">
              {{ scope.row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roles" label="角色" width="200">
          <template slot-scope="scope">
            <el-tag v-for="role in scope.row.roles" :key="role.id" size="small" style="margin-right: 5px;">
              {{ role.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.lastLoginTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="handleRoles(scope.row)">角色</el-button>
            <el-button 
              size="mini" 
              :type="scope.row.enabled ? 'danger' : 'success'" 
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.enabled ? '禁用' : '启用' }}
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

    <!-- 用户对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="500px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="temp.username" :disabled="dialogStatus === 'update'" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="temp.email" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="dialogStatus === 'create'">
          <el-input v-model="temp.password" type="password" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword" v-if="dialogStatus === 'create'">
          <el-input v-model="temp.confirmPassword" type="password" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="temp.enabled" active-text="启用" inactive-text="禁用"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确认</el-button>
      </div>
    </el-dialog>

    <!-- 角色分配对话框 -->
    <el-dialog title="分配角色" :visible.sync="roleDialogVisible" width="500px">
      <el-form label-width="100px">
        <el-form-item label="用户">
          <span>{{ currentUser.username }}</span>
        </el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="selectedRoles">
            <el-checkbox 
              v-for="role in allRoles" 
              :key="role.id" 
              :label="role.id"
              style="display: block; margin-bottom: 10px;"
            >
              {{ role.name }} - {{ role.description }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="assignRoles">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  name: 'Users',
  components: { Pagination },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (this.dialogStatus === 'create' && (!value || value.length < 6)) {
        callback(new Error('密码不能少于6位'))
      } else {
        callback()
      }
    }
    const validateConfirmPassword = (rule, value, callback) => {
      if (this.dialogStatus === 'create' && value !== this.temp.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 0,
        limit: 20,
        keyword: undefined,
        enabled: undefined
      },
      temp: {
        id: undefined,
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        enabled: true
      },
      currentUser: {},
      selectedRoles: [],
      allRoles: [],
      dialogFormVisible: false,
      roleDialogVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑用户',
        create: '创建用户'
      },
      rules: {
        username: [{ required: true, message: '用户名是必填项', trigger: 'blur' }],
        email: [
          { required: true, message: '邮箱是必填项', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        password: [{ validator: validatePassword, trigger: 'blur' }],
        confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getRoles()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.list = [
          {
            id: 1,
            username: 'admin',
            email: 'admin@hrms.com',
            enabled: true,
            roles: [{ id: 1, name: 'ADMIN' }],
            lastLoginTime: '2025-05-15 10:30:00',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 2,
            username: 'hr001',
            email: 'hr001@hrms.com',
            enabled: true,
            roles: [{ id: 2, name: 'HR' }],
            lastLoginTime: '2025-05-14 16:20:00',
            createdTime: '2025-01-02 00:00:00'
          }
        ]
        this.total = 2
        this.listLoading = false
      }, 1000)
    },
    getRoles() {
      // 模拟角色数据
      this.allRoles = [
        { id: 1, name: 'ADMIN', description: '系统管理员' },
        { id: 2, name: 'HR', description: '人事专员' },
        { id: 3, name: 'PROJECT_MANAGER', description: '项目经理' },
        { id: 4, name: 'TESTER', description: '测试人员' },
        { id: 5, name: 'EMPLOYEE', description: '普通员工' }
      ]
    },
    handleFilter() {
      this.listQuery.page = 0
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        enabled: true
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
      this.temp.password = ''
      this.temp.confirmPassword = ''
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleRoles(row) {
      this.currentUser = row
      this.selectedRoles = row.roles.map(role => role.id)
      this.roleDialogVisible = true
    },
    handleToggleStatus(row) {
      const action = row.enabled ? '禁用' : '启用'
      this.$confirm(`确认${action}用户 ${row.username}?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.enabled = !row.enabled
        this.$message({
          type: 'success',
          message: `${action}成功!`
        })
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.$message({
            message: '创建成功',
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
    assignRoles() {
      this.$message({
        message: '角色分配成功',
        type: 'success'
      })
      this.roleDialogVisible = false
      this.getList()
    },
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString()
    }
  }
}
</script>

<style scoped>
.users-container {
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
