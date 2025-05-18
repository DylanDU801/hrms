<template>
  <div class="app-wrapper">
    <sidebar class="sidebar-container" />
    <div class="main-container">
      <navbar class="fixed-header" />
      <div class="main-content">
        <div class="page-header">
          <h1 class="main-title">外包人员管理系统</h1>
        </div>
        <app-main />
      </div>
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from './components'
import ResizeHandler from './mixin/ResizeHandler'

export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  mixins: [ResizeHandler]
}
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";

.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  display: flex;
}

.sidebar-container {
  width: $sideBarWidth;
  position: fixed;
  font-size: 0;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 1001;
  overflow: hidden;
  transition: width 0.28s;
  background-color: $menuBg;
}

.main-container {
  margin-left: $sideBarWidth;
  position: relative;
  width: calc(100% - #{$sideBarWidth});
  min-height: 100%;
  transition: margin-left 0.28s;
}

.fixed-header {
  position: sticky;
  top: 0;
  right: 0;
  z-index: 9;
  width: 100%;
  transition: width 0.28s;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.main-content {
  padding: 0;
  position: relative;
  min-height: calc(100vh - 50px);
}

.page-header {
  padding: 15px 20px;
  margin-bottom: 15px;
  background-color: white;
  display: flex;
  align-items: center;
}

.main-title {
  margin: 0;
  padding: 0;
  font-size: 24px;
  font-weight: 600;
  color: #409EFF;
}

@media (max-width: 992px) {
  .main-container {
    margin-left: 64px;
    width: calc(100% - 64px);
  }
  
  .sidebar-container {
    width: 64px !important;
  }
  
  .main-title {
    font-size: 20px;
  }
}
</style>