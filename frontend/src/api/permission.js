import request from '@/utils/request'

export function fetchPermissions() {
  return request({
    url: '/api/permissions',
    method: 'get'
  })
}

export function fetchPermission(id) {
  return request({
    url: `/api/permissions/${id}`,
    method: 'get'
  })
}

export function createPermission(data) {
  return request({
    url: '/api/permissions',
    method: 'post',
    data
  })
}

export function updatePermission(id, data) {
  return request({
    url: `/api/permissions/${id}`,
    method: 'put',
    data
  })
}

export function deletePermission(id) {
  return request({
    url: `/api/permissions/${id}`,
    method: 'delete'
  })
}

export function getPermissionsByResource() {
  return request({
    url: '/api/permissions/by-resource',
    method: 'get'
  })
}

export function updateRolePermissions(roleId, permissionIds) {
  return request({
    url: `/api/roles/${roleId}/permissions`,
    method: 'post',
    data: permissionIds
  })
}
