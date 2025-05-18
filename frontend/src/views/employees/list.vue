import { fetchList } from '@/api/employee'

export default {
  // ...
  methods: {
    getList() {
      this.listLoading = true
      fetchList().then(response => {
        this.list = response.data // 按实际返回结构
        this.listLoading = false
      })
    }
  }
}
