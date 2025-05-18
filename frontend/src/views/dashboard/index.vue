<template>
  <div class="dashboard-container">
    <div class="dashboard-text">欢迎使用外包人员管理系统</div>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {}
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
    color: #409EFF;
  }
}
</style>