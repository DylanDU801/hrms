<template>
  <div class="permissions-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>权限管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">添加权限</el-button>
      </div>
      
      <!-- 筛选区域 -->
      <div class="filter-container">
        <el-select v-model="listQuery.resource" placeholder="资源类型" clearable style="width: 150px" class="filter-item">
          <el-option
            v-for="resource in resources"
            :key="resource"
            :label="resource"
            :value="resource">
          </el-option>
        </el-select>
        <el-input
          v-model="listQuery.keyword"
          placeholder="搜索权限名称"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
        <el-button class="filter-item" type="success" @click="handleGroupView">分组视图</el-button>
      </div>

      <!-- 分组视图 -->
      <div v-if="showGroupView" class="group-view">
        <el-collapse v-model="activeGroups" accordion>
          <el-collapse-item 
            v-for="(permissions, resource) in groupedPermissions" 
            :key="resource"
            :title="`${resource} (${permissions.length}个权限)`"
            :name="resource"
          >
            <el-table :data="permissions" border style="width: 100%">
              <el-table-column prop="id" label="ID" width="80"></el-table-column>
              <el-table-column prop="name" label="权限名称" width="200"></el-table-column>
              <el-table-column prop="action" label="操作" width="120"></el-table-column>
              <el-table-column prop="description" label="描述"></el-table-column>
              <el-table-column prop="createdTime" label="创建时间" width="160">
                <template slot-scope="scope">
                  {{ formatDate(scope.row.createdTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
        </el-collapse>
      </div>

      <!-- 列表视图 -->
      <div v-else>
        <el-table :data="list" v-loading="listLoading" border style="width: 100%">
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="name" label="权限名称" width="200"></el-table-column>
          <el-table-column prop="resource" label="资源" width="120">
            <template slot-scope="scope">
              <el-tag>{{ scope.row.resource }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="action" label="操作" width="120"></el-table-column>
          <el-table-column prop="description" label="描述"></el-table-column>
          <el-table-column prop="createdTime" label="创建时间" width="160">
            <template slot-scope="scope">
              {{ formatDate(scope.row.createdTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
      </div>
    </el-card>

    <!-- 权限对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="500px">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px">
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="temp.name" placeholder="如：USER_READ" />
        </el-form-item>
        <el-form-item label="资源类型" prop="resource">
          <el-select v-model="temp.resource" placeholder="请选择资源类型" filterable allow-create>
            <el-option
              v-for="resource in resources"
              :key="resource"
              :label="resource"
              :value="resource">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="操作类型" prop="action">
          <el-select v-model="temp.action" placeholder="请选择操作类型">
            <el-option label="查看" value="查看"></el-option>
            <el-option label="编辑" value="编辑"></el-option>
            <el-option label="删除" value="删除"></el-option>
            <el-option label="创建" value="创建"></el-option>
            <el-option label="审批" value="审批"></el-option>
            <el-option label="分配" value="分配"></el-option>
            <el-option label="导出" value="导出"></el-option>
            <el-option label="导入" value="导入"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" :rows="3" v-model="temp.description" />
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
  name: 'Permissions',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      showGroupView: false,
      activeGroups: [],
      listQuery: {
        page: 0,
        limit: 20,
        resource: undefined,
        keyword: undefined
      },
      resources: ['用户', '员工', '部门', '测试', '申请', '薪资', '角色', '权限', '日志', '系统'],
      temp: {
        id: undefined,
        name: '',
        resource: '',
        action: '',
        description: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑权限',
        create: '创建权限'
      },
      rules: {
        name: [{ required: true, message: '权限名称是必填项', trigger: 'blur' }],
        resource: [{ required: true, message: '资源类型是必填项', trigger: 'change' }],
        action: [{ required: true, message: '操作类型是必填项', trigger: 'change' }],
        description: [{ required: true, message: '描述是必填项', trigger: 'blur' }]
      }
    }
  },
  computed: {
    groupedPermissions() {
      return this.list.reduce((groups, permission) => {
        const { resource } = permission
        if (!groups[resource]) {
          groups[resource] = []
        }
        groups[resource].push(permission)
        return groups
      }, {})
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      // 模拟数据
      setTimeout(() => {
        this.list = [
          {
            id: 1,
            name: 'USER_READ',
            resource: '用户',
            action: '查看',
            description: '查看用户信息',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 2,
            name: 'USER_WRITE',
            resource: '用户',
            action: '编辑',
            description: '编辑用户信息',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 3,
            name: 'USER_DELETE',
            resource: '用户',
            action: '删除',
            description: '删除用户',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 4,
            name: 'EMPLOYEE_READ',
            resource: '员工',
            action: '查看',
            description: '查看员工信息',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 5,
            name: 'EMPLOYEE_WRITE',
            resource: '员工',
            action: '编辑',
            description: '编辑员工信息',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 6,
            name: 'TEST_READ',
            resource: '测试',
            action: '查看',
            description: '查看测试任务',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 7,
            name: 'TEST_WRITE',
            resource: '测试',
            action: '编辑',
            description: '创建编辑测试任务',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 8,
            name: 'APPLICATION_READ',
            resource: '申请',
            action: '查看',
            description: '查看申请',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 9,
            name: 'APPLICATION_APPROVE',
            resource: '申请',
            action: '审批',
            description: '审批申请',
            createdTime: '2025-01-01 00:00:00'
          },
          {
            id: 10,
            name: 'SALARY_READ',
            resource: '薪资',
            action: '查看',
            description: '查看薪资',
            createdTime: '2025-01-01 00:00:00'
          }
        ]
        this.total = 10
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.page = 0
      this.getList()
    },
    handleGroupView() {
      this.showGroupView = !this.showGroupView
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        resource: '',
        action: '',
        description: ''
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
    handleDelete(row) {
      this.$confirm(`确认删除权限 ${row.name}?`, '提示', {
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
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleString()
    }
  }
}
</script>

<style scoped>
.permissions-container {
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

.group-view {
  margin-top: 20px;
}
</style>