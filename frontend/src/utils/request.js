import axios  from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const request = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  timeout: 5000 // 请求超时
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['Authorization'] = `Bearer ${getToken()}`
    }
    return config
  },
  error => {
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// 响应拦截器

request.interceptors.response.use(
    response => {
        // 获取响应数据
        let res = response.data;

        // 兼容处理服务端返回的字符串数据，将其解析为JSON对象
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }

        // 检查响应中的状态码，如果为401则表示未授权
        if(res.code === '401'){
            // 如果未授权，则重定向到登录页面
            router.push('/login');
        }

        // 返回处理后的响应数据
        return res;
    },
    error => {
        // 如果响应发生错误，打印错误信息以便调试
        console.error('response error: ' + error);
        // 返回Promise.reject以通知响应发生错误
        return Promise.reject(error);
    }
)
// request.interceptors.response.use(
//   response => {
//     const res = response.data
//
//     // 如果返回的状态码不是20000，则判断为错误
//     if (res.code !== 20000) {
//       Message({
//         message: res.message || 'Error',
//         type: 'error',
//         duration: 5 * 1000
//       })
//
//       // 50008: 非法的token; 50012: 其他客户端登录; 50014: Token过期;
//       if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
//         // 重新登录
//         MessageBox.confirm(
//           '您已经登出，您可以取消停留在此页面，或者再次登录',
//           '确认登出',
//           {
//             confirmButtonText: '重新登录',
//             cancelButtonText: '取消',
//             type: 'warning'
//           }
//         ).then(() => {
//           store.dispatch('user/resetToken').then(() => {
//             location.reload()
//           })
//         })
//       }
//       return Promise.reject(new Error(res.message || 'Error'))
//     } else {
//       return res
//     }
//   },
//   error => {
//     console.log('err' + error) // for debug
//     Message({
//       message: error.message,
//       type: 'error',
//       duration: 5 * 1000
//     })
//     return Promise.reject(error)
//   }
// )

export default request
