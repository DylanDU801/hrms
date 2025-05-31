export function fetchOperationLogs(query) {
  return request({
    url: '/api/operation-logs',
    method: 'get',
    params: query
  })
}

export function fetchOperationLog(id) {
  return request({
    url: `/api/operation-logs/${id}`,
    method: 'get'
  })
}

export function getUserOperationStatistics(userId, days) {
  return request({
    url: '/api/operation-logs/user-statistics',
    method: 'get',
    params: { userId, days }
  })
}

export function getOperationTypeStatistics(startDate, endDate) {
  return request({
    url: '/api/operation-logs/operation-statistics',
    method: 'get',
    params: { startDate, endDate }
  })
}

export function getAnomalousOperations(days, anomalyType) {
  return request({
    url: '/api/operation-logs/anomalies',
    method: 'get',
    params: { days, anomalyType }
  })
}

export function getLoginLogs(query) {
  return request({
    url: '/api/operation-logs/logins',
    method: 'get',
    params: query
  })
}

export function exportOperationLogs(startDate, endDate, userId, operation) {
  return request({
    url: '/api/operation-logs/export',
    method: 'get',
    params: { startDate, endDate, userId, operation }
  })
}

export function cleanupOldLogs(retentionDays) {
  return request({
    url: '/api/operation-logs/cleanup',
    method: 'delete',
    params: { retentionDays }
  })
}

export function getSecurityEvents(days, severity) {
  return request({
    url: '/api/operation-logs/security-events',
    method: 'get',
    params: { days, severity }
  })
}
