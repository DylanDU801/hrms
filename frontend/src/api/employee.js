import request from '@/utils/request'

export function fetchUserList(query) {
  return request({
    url: '/employees/unbound-users',
    method: 'get',
    params:query
  })
}

export function fetchList(query) {
  return request({
    url: '/employees/page',
    method: 'get',
    params: query
  })
}

export function fetchEmployee(id) {
  return request({
    url: `/employees/${id}`,
    method: 'get'
  })
}

export function createEmployee(data) {
  return request({
    url: '/employees',
    method: 'post',
    data
  })
}

export function updateEmployee(id, data) {
  return request({
    url: `/employees/${id}`,
    method: 'put',
    data
  })
}

export function deleteEmployee(id) {
  return request({
    url: `/employees/${id}`,
    method: 'delete'
  })
}
