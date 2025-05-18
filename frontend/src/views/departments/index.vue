<template>
  <div class="departments-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>部门列表</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">添加部门</el-button>
      </div>
      
      <el-table :data="departmentList" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="部门名称"></el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 部门对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%">
      <el-form :model="department" :rules="rules" ref="departmentForm" label-width="100px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="department.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Departments',
  data() {
    return {
      departmentList: [
        { id: 1, name: '研发部' },
        { id: 2, name: '人事部' }
      ],
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      department: {
        id: null,
        name: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入部门名称', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    // 实际项目中应该通过API获取部门列表
    // fetchDepartments().then(response => {
    //   this.departmentList = response.data
    // })
  },
  methods: {
    handleAdd() {
      this.dialogTitle = '添加部门'
      this.department = { id: null, name: '' }
      this.isEdit = false
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.departmentForm.clearValidate()
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑部门'
      this.department = Object.assign({}, row)
      this.isEdit = true
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该部门?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 实际项目中应该调用API删除部门
        // deleteDepartment(row.id).then(() => {
        //   this.getDepartments()
        //   this.$message({
        //     type: 'success',
        //     message: '删除成功!'
        //   })
        // })
        
        // 模拟删除效果
        this.departmentList = this.departmentList.filter(dept => dept.id !== row.id)
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })          
      })
    },
    submitForm() {
      this.$refs.departmentForm.validate(valid => {
        if (valid) {
          if (this.isEdit) {
            // 编辑部门
            // updateDepartment(this.department.id, this.department).then(() => {
            //   this.getDepartments()
            //   this.dialogVisible = false
            //   this.$message({
            //     type: 'success',
            //     message: '更新成功!'
            //   })
            // })
            
            // 模拟编辑效果
            const index = this.departmentList.findIndex(dept => dept.id === this.department.id)
            this.departmentList.splice(index, 1, this.department)
          } else {
            // 添加部门
            // createDepartment(this.department).then(response => {
            //   this.getDepartments()
            //   this.dialogVisible = false
            //   this.$message({
            //     type: 'success',
            //     message: '添加成功!'
            //   })
            // })
            
            // 模拟添加效果
            const newDept = Object.assign({}, this.department)
            newDept.id = this.departmentList.length + 1
            this.departmentList.push(newDept)
          }
          this.dialogVisible = false
          this.$message({
            type: 'success',
            message: this.isEdit ? '更新成功!' : '添加成功!'
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.departments-container {
  padding: 20px;
}
</style>