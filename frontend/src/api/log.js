import request from '@/utils/request'

export function fetchLogs(params) {
  return request({
    url: '/api/operation-logs',
    method: 'get',
    params
  })
}

export function fetchLogStats() {
  return request({
    url: '/api/logs/stats',
    method: 'get'
  })
}

export function exportLogs(params) {
  return request({
    url: '/api/logs/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export function cleanupLogs(retentionDays) {
  return request({
    url: '/api/operation-logs/cleanup',
    method: 'Delete',
    params: { "retentionDays":retentionDays }
  })
}
