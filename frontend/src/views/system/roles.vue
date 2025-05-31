<template>
  <div class="roles-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>角色管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">添加角色</el-button>
      </div>
      
      <el-table :data="list" v-loading="listLoading" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="角色名称" width="150"></el-table-column>
        <el-table-column prop="description" label="描述" width="200"></el-table-column>
        <el-table-column prop="enabled" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.enabled ? 'success' : 'danger'">
              {{ scope.row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="permissions" label="权限数量" width="120">
          <template slot-scope="scope">
            <el-badge :value="scope.row.permissions ? scope.row.permissions.length : 0" class="item">
              <el-button size="mini">权限</el-button>
            </el-badge>
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
            <el-button size="mini" type="warning" @click="handlePermissions(scope.row)">权限</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 角色对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="500px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" :rows="3" v-model="temp.description" />
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

    <!-- 权限分配对话框 -->
    <el-dialog title="分配权限" :visible.sync="permissionDialogVisible" width="800px">
      <el-form label-width="100px">
        <el-form-item label="角色">
          <span>{{ currentRole.name }} - {{ currentRole.description }}</span>
        </el-form-item>
        <el-form-item label="权限">
          <div v-for="(group, resource) in groupedPermissions" :key="resource" style="margin-bottom: 20px;">
            <h4>{{ resource }}</h4>
            <el-checkbox-group v-model="selectedPermissions">
              <el-checkbox 
                v-for="permission in group" 
                :key="permission.id" 
                :label="permission.id"
                style="margin-right: 20px; margin-bottom: 10px;"
              >
                {{ permission.action }} - {{ permission.description }}
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="assignPermissions">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Roles',
  data() {
    return {
      list: [],
      listLoading: true,
      temp: {
        id: undefined,
        name: '',
        description: '',
        enabled: true
      },
      currentRole: {},
      selectedPermissions: [],
      allPermissions: [],
      groupedPermissions: {},
      dialogFormVisible: false,
      permissionDialogVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑角色',
        create: '创建角色'
      },
      rules: {
        name: [{ required: true, message: '角色名称是必填项', trigger: 'blur' }],
        description: [{ required: true, message: '描述是必填项', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getPermissions()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.list = [
          {
            id: 1,
            name: 'ADMIN',
            description: '系统管理员',
            enabled: true,
            permissions: Array(15).fill(0).map((_, i) => ({ id: i + 1 })),
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 2,
            name: 'HR',
            description: '人事专员',
            enabled: true,
            permissions: Array(8).fill(0).map((_, i) => ({ id: i + 1 })),
            createdTime: '2025-01-01 00:00:00'
          }
        ]
        this.listLoading = false
      }, 1000)
    },
    getPermissions() {
      // 模拟权限数据并按资源分组
      const permissions = [
        { id: 1, name: 'USER_READ', resource: '用户', action: '查看', description: '查看用户信息' },
        { id: 2, name: 'USER_WRITE', resource: '用户', action: '编辑', description: '编辑用户信息' },
        { id: 3, name: 'EMPLOYEE_READ', resource: '员工', action: '查看', description: '查看员工信息' },
        { id: 4, name: 'EMPLOYEE_WRITE', resource: '员工', action: '编辑', description: '编辑员工信息' },
        { id: 5, name: 'TEST_READ', resource: '测试', action: '查看', description: '查看测试任务' },
        { id: 6, name: 'TEST_WRITE', resource: '测试', action: '编辑', description: '创建编辑测试任务' }
      ]
      
      this.allPermissions = permissions
      this.groupedPermissions = permissions.reduce((groups, permission) => {
        const { resource } = permission
        if (!groups[resource]) {
          groups[resource] = []
        }
        groups[resource].push(permission)
        return groups
      }, {})
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        description: '',
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
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handlePermissions(row) {
      this.currentRole = row
      this.selectedPermissions = row.permissions ? row.permissions.map(p => p.id) : []
      this.permissionDialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确认删除角色 ${row.name}?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
        this.getList()
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
    assignPermissions() {
      this.$message({
        message: '权限分配成功',
        type: 'success'
      })
      this.permissionDialogVisible = false
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
.roles-container {
  padding: 20px;
}
</style>