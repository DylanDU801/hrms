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
<!--              <el-button size="mini">权限</el-button>-->
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
import {fetchRoles,updateRole,createRole,deleteRole} from "@/api/role";
import {createEmployee, updateEmployee} from "@/api/employee";
import {fetchPermissions,updateRolePermissions} from "@/api/permission";

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
        name: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在2到20个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '角色描述不能为空', trigger: 'blur' },
          { max: 100, message: '不能超过100个字符', trigger: 'blur' }
        ]
      },
      listQuery: {
        page: 1,
        size: 10,
        name: undefined
      },
      total: 0,
    }
  },
  created() {
    this.getList()
    this.getPermissions()
  },
  methods: {
    async getList() {
      this.listLoading = false
      try {
        const response = await fetchRoles()
        this.list = response.data
      } catch (error) {
        console.error(error)
        this.$message.error('获取角色列表失败')
      }
    },
    getPermissions() {
      // 模拟权限数据并按资源分组
      // const permissions = [
      //   { id: 1, name: 'USER_READ', resource: '用户', action: '查看', description: '查看用户信息' },
      //   { id: 2, name: 'USER_WRITE', resource: '用户', action: '编辑', description: '编辑用户信息' },
      //   { id: 3, name: 'EMPLOYEE_READ', resource: '员工', action: '查看', description: '查看员工信息' },
      //   { id: 4, name: 'EMPLOYEE_WRITE', resource: '员工', action: '编辑', description: '编辑员工信息' },
      //   { id: 5, name: 'TEST_READ', resource: '测试', action: '查看', description: '查看测试任务' },
      //   { id: 6, name: 'TEST_WRITE', resource: '测试', action: '编辑', description: '创建编辑测试任务' }
      // ]

      fetchPermissions().then((re) => {
        console.log(re.data)
        this.allPermissions = re.data
        this.groupedPermissions = this.allPermissions.reduce((groups, permission) => {
          const { resource } = permission
          if (!groups[resource]) {
            groups[resource] = []
          }
          groups[resource].push(permission)
          return groups
        }, {})

      }).catch((e) => {
        console.error(e)
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createRole(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          }).catch((e) => {
            console.log(e)
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
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateRole(tempData.id, tempData).then(() => {
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
          })
        }
      })
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
      // this.$nextTick(() => {
      //   this.$refs['dataForm'].clearValidate()
      // })
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
        this.permissionLoading = true
        this.permissionDialogVisible = true

        // 如果权限数据未加载则重新获取
        if (this.allPermissions.length === 0) {
            this.getPermissions().finally(() => {
                this.permissionLoading = false
            })
        } else {
            this.permissionLoading = false
        }
    },
    async handleDelete(row) {
      this.$confirm(`确认删除角色 ${row.name}?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRole(row.id).then(() => {
          this.getList()
          this.$message.success('删除成功')
        }).catch((e) => {
          console.error(e)
          this.getList()
          this.$message.error('删除失败')
        })
      })
    },
    async assignPermissions() {
        try {
            await updateRolePermissions(
                this.currentRole.id,
                this.selectedPermissions
            )
          this.currentRole.permissions = this.allPermissions.filter(p =>
              this.selectedPermissions.includes(p.id)
          )
          this.$message.success('权限分配成功')
          this.permissionDialogVisible = false
          this.getList()
        } catch (error) {
        console.error('权限分配失败:', error)
        this.$message.error('权限分配失败')
      }
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
