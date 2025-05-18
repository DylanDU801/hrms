<template>
  <div v-if="!item.hidden">
    <template v-if="!item.children || item.children.length === 0">
      <el-menu-item :index="resolvePath(item.path)">
        <i v-if="item.meta && item.meta.icon" :class="item.meta.icon" style="margin-right: 8px;"></i>
        <span>{{ item.meta && item.meta.title }}</span>
      </el-menu-item>
    </template>
    <el-submenu v-else :index="resolvePath(item.path)">
      <template slot="title">
        <i v-if="item.meta && item.meta.icon" :class="item.meta.icon" style="margin-right: 8px;"></i>
        <span>{{ item.meta && item.meta.title }}</span>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :item="child"
        :base-path="resolvePath(item.path)"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path-browserify'

export default {
  name: 'SidebarItem',
  props: {
    item: {
      type: Object,
      required: true
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  methods: {
    resolvePath(routePath) {
      if (routePath.startsWith('/')) {
        return routePath
      }
      // 用 path-browserify 兼容路径拼接
      return path.resolve(this.basePath, routePath)
    }
  }
}
</script>
