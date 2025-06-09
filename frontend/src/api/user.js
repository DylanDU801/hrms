import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function fetchUsers(params) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

export function createUser(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

export function updateUser(id,data) {
  return request({
    url: `/users/${id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/users/${id}`,
    method: 'Delete'
  })
}

export function toggleUserStatus(id, enabled) {
  return request({
    url: '/users/toggle-status',
    method: 'put',
    data: { "id":id, "enabled":enabled }
  })
}

export function assignUserRoles(userId, roleIds) {
  return request({
    url: '/api/users/assign-roles',
    method: 'POST',
    data: { userId, roleIds }
  })
}
