import request from '@/utils/request'

export function fetchRoles() {
  return request({
    url: '/api/roles',
    method: 'get'
  })
}

export function fetchRole(id) {
  return request({
    url: `/api/roles/${id}`,
    method: 'get'
  })
}

export function createRole(data) {
  return request({
    url: '/api/roles',
    method: 'post',
    data
  })
}

export function updateRole(id, data) {
  return request({
    url: `/api/roles/${id}`,
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/api/roles/${id}`,
    method: 'delete'
  })
}

export function assignPermissions(roleId, permissionIds) {
  return request({
    url: `/api/roles/${roleId}/permissions`,
    method: 'post',
    data: permissionIds
  })
}

export function removePermission(roleId, permissionId) {
  return request({
    url: `/api/roles/${roleId}/permissions/${permissionId}`,
    method: 'delete'
  })
}

export function getRolePermissions(roleId) {
  return request({
    url: `/api/roles/${roleId}/permissions`,
    method: 'get'
  })
}

export function assignRolesToUser(userId, roleIds) {
  return request({
    url: `/api/users/${userId}/roles`,
    method: 'post',
    data: roleIds
  })
}

export function removeRoleFromUser(userId, roleId) {
  return request({
    url: `/api/users/${userId}/roles/${roleId}`,
    method: 'delete'
  })
}

export function getUserRoles(userId) {
  return request({
    url: `/api/users/${userId}/roles`,
    method: 'get'
  })
}

export function getUserPermissions(userId) {
  return request({
    url: `/api/users/${userId}/permissions`,
    method: 'get'
  })
}
