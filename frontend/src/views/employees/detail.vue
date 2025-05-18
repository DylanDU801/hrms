<template>
  <div class="employee-detail">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>员工详情</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">返回</el-button>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID">{{ employee.id }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ employee.name }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ employee.email }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ employee.position }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ employee.departmentName }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>
import { fetchEmployee } from '@/api/employee'
export default {
  name: 'EmployeeDetail',
  data() {
    return {
      employee: {
        id: '',
        name: '',
        email: '',
        position: '',
        departmentName: ''
      }
    }
  },
  created() {
    const id = this.$route.params.id
    fetchEmployee(id).then(response => {
      this.employee = response.data
    })
  },
  methods: {
    goBack() {
      this.$router.push('/employees/list')
    }
  }
}
</script>

<style scoped>
.employee-detail {
  padding: 20px;
}
</style>
