<template>
  <div class="pending-approvals-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>待我审批</span>
      </div>
      
      <el-table :data="pendingApplications" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="申请标题" width="200"></el-table-column>
        <el-table-column prop="applicant" label="申请人" width="120"></el-table-column>
        <el-table-column prop="type" label="申请类型" width="120"></el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="160"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary">详情</el-button>
            <el-button size="mini" type="success">批准</el-button>
            <el-button size="mini" type="danger">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'PendingApprovals',
  data() {
    return {
      pendingApplications: [
        {
          id: 1,
          title: '年假申请',
          applicant: '张三',
          type: '请假申请',
          applyTime: '2025-05-15 10:30:00'
        },
        {
          id: 2,
          title: '设备申请',
          applicant: '李四',
          type: '设备申请',
          applyTime: '2025-05-14 14:20:00'
        }
      ]
    }
  }
}
</script>

<style scoped>
.pending-approvals-container {
  padding: 20px;
}
</style>