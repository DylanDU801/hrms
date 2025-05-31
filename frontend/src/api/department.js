import request from '@/utils/request'

export function fetchDepartments() {
  return request({
    url: '/departments',
    method: 'get'
  })
}

export function fetchDepartment(id) {
  return request({
    url: `/departments/${id}`,
    method: 'get'
  })
}

export function createDepartment(data) {
  return request({
    url: '/departments',
    method: 'post',
    data
  })
}

export function updateDepartment(id, data) {
  return request({
    url: `/departments/${id}`,
    method: 'put',
    data
  })
}

export function deleteDepartment(id) {
  return request({
    url: `/departments/${id}`,
    method: 'delete'
  })
}
