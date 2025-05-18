<template>
  <div class="salaries-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>薪资管理</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAdd">添加薪资记录</el-button>
      </div>
      
      <el-table :data="salaryList" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="employeeName" label="员工姓名" width="120"></el-table-column>
        <el-table-column prop="payDate" label="发放日期" width="120"></el-table-column>
        <el-table-column prop="amount" label="金额">
          <template slot-scope="scope">
            ¥ {{ scope.row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 薪资对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="40%">
      <el-form :model="salary" :rules="rules" ref="salaryForm" label-width="100px">
        <el-form-item label="员工" prop="employeeId">
          <el-select v-model="salary.employeeId" placeholder="请选择员工">
            <el-option
              v-for="item in employees"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发放日期" prop="payDate">
          <el-date-picker
            v-model="salary.payDate"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="salary.amount" :precision="2" :step="100" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="salary.remark"></el-input>
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
  name: 'Salaries',
  data() {
    return {
      salaryList: [
        { id: 1, employeeId: 1, employeeName: '张三', payDate: '2025-05-10', amount: 10000, remark: '5月工资' },
        { id: 2, employeeId: 2, employeeName: '李四', payDate: '2025-05-10', amount: 12000, remark: '5月工资' }
      ],
      employees: [
        { id: 1, name: '张三' },
        { id: 2, name: '李四' }
      ],
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      salary: {
        id: null,
        employeeId: '',
        payDate: '',
        amount: 0,
        remark: ''
      },
      rules: {
        employeeId: [
          { required: true, message: '请选择员工', trigger: 'change' }
        ],
        payDate: [
          { required: true, message: '请选择发放日期', trigger: 'change' }
        ],
        amount: [
          { required: true, message: '请输入金额', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    // 实际项目中应该通过API获取薪资列表和员工列表
    // fetchSalaries().then(response => {
    //   this.salaryList = response.data
    // })
    // fetchEmployees().then(response => {
    //   this.employees = response.data
    // })
  },
  methods: {
    handleAdd() {
      this.dialogTitle = '添加薪资记录'
      this.salary = { id: null, employeeId: '', payDate: '', amount: 0, remark: '' }
      this.isEdit = false
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.salaryForm.clearValidate()
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑薪资记录'
      this.salary = Object.assign({}, {
        id: row.id,
        employeeId: row.employeeId,
        payDate: row.payDate,
        amount: row.amount,
        remark: row.remark
      })
      this.isEdit = true
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该薪资记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 实际项目中应该调用API删除薪资记录
        // deleteSalary(row.id).then(() => {
        //   this.getSalaries()
        //   this.$message({
        //     type: 'success',
        //     message: '删除成功!'
        //   })
        // })
        
        // 模拟删除效果
        this.salaryList = this.salaryList.filter(s => s.id !== row.id)
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
      this.$refs.salaryForm.validate(valid => {
        if (valid) {
          // 找到员工名称
          const employee = this.employees.find(e => e.id === this.salary.employeeId)
          const employeeName = employee ? employee.name : ''
          
          if (this.isEdit) {
            // 编辑薪资记录
            // updateSalary(this.salary.id, this.salary).then(() => {
            //   this.getSalaries()
            //   this.dialogVisible = false
            //   this.$message({
            //     type: 'success',
            //     message: '更新成功!'
            //   })
            // })
            
            // 模拟编辑效果
            const index = this.salaryList.findIndex(s => s.id === this.salary.id)
            this.salaryList.splice(index, 1, {
              ...this.salary,
              employeeName
            })
          } else {
            // 添加薪资记录
            // createSalary(this.salary).then(response => {
            //   this.getSalaries()
            //   this.dialogVisible = false
            //   this.$message({
            //     type: 'success',
            //     message: '添加成功!'
            //   })
            // })
            
            // 模拟添加效果
            const newSalary = Object.assign({}, this.salary)
            newSalary.id = this.salaryList.length + 1
            newSalary.employeeName = employeeName
            this.salaryList.push(newSalary)
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
.salaries-container {
  padding: 20px;
}
</style>