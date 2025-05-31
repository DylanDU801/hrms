const myApplicationsVue = `<template>
  <div class="my-applications-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>我的申请</span>
        <el-button style="float: right; padding: 3px 0" type="text">提交申请</el-button>
      </div>
      
      <el-table :data="applications" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="申请标题" width="200"></el-table-column>
        <el-table-column prop="type" label="申请类型" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="160"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary">详情</el-button>
            <el-button size="mini" type="warning" v-if="scope.row.status === '待审批'">撤回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'MyApplications',
  data() {
    return {
      applications: [
        {
          id: 1,
          title: '年假申请',
          type: '请假申请',
          status: '待审批',
          applyTime: '2025-05-15 10:30:00'
        },
        {
          id: 2,
          title: '加班申请',
          type: '加班申请', 
          status: '已批准',
          applyTime: '2025-05-14 14:20:00'
        }
      ]
    }
  },
  methods: {
    getStatusType(status) {
      const statusMap = {
        '待审批': 'warning',
        '已批准': 'success',
        '已拒绝': 'danger'
      }
      return statusMap[status]
    }
  }
}
</script>

<style scoped>
.my-applications-container {
  padding: 20px;
}
</style>