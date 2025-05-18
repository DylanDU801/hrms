<template>
  <div class="employee-create">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>添加员工</span>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="form.position"></el-input>
        </el-form-item>
        <el-form-item label="部门" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="请选择部门">
            <el-option
              v-for="item in departments"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button @click="onCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'EmployeeCreate',
  data() {
    return {
      form: {
        name: '',
        email: '',
        position: '',
        departmentId: ''
      },
      departments: [
        { id: 1, name: '研发部' },
        { id: 2, name: '人事部' }
      ],
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        departmentId: [
          { required: true, message: '请选择部门', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    onSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message({
            message: '提交成功',
            type: 'success'
          })
          // 实际项目中这里应该调用API保存数据
           createEmployee(this.form).then(() => {
             this.$router.push('/employees/list')
           })
        } else {
          return false
        }
      })
    },
    onCancel() {
      this.$router.push('/employees/list')
    }
  }
}
</script>

<style scoped>
.employee-create {
  padding: 20px;
}
</style>