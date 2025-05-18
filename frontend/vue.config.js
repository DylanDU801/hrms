'use strict'
const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

// 所有配置项说明可以在 https://cli.vuejs.org/config/ 中查看
module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'development',
  productionSourceMap: false,
  devServer: {
    port: 8080,
    open: true,
    overlay: {
      warnings: false,
      errors: true
    },
    proxy: {
      // 代理所有/api开头的请求
      '/api': {
        target: 'http://localhost:8000', // 后端API地址
        changeOrigin: true, // 需要跨域
        pathRewrite: {
          '^/api': '' // 重写请求路径
        }
      }
    }
  },
  configureWebpack: {
    // 提供别名便于引用
    resolve: {
      alias: {
        '@': resolve('src')
      }
    }
  }
}