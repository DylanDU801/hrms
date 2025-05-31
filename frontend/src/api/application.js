import request from '@/utils/request'

export function fetchApplications(query) {
  return request({
    url: '/api/applications',
    method: 'get',
    params: query
  })
}

export function fetchApplication(id) {
  return request({
    url: `/api/applications/${id}`,
    method: 'get'
  })
}

export function submitApplication(data, attachment) {
  const formData = new FormData()
  formData.append('application', new Blob([JSON.stringify(data)], { type: 'application/json' }))
  if (attachment) {
    formData.append('attachment', attachment)
  }
  
  return request({
    url: '/api/applications',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function approveApplication(id, approved, reason) {
  return request({
    url: `/api/applications/${id}/approve`,
    method: 'put',
    params: { approved, reason }
  })
}

export function cancelApplication(id) {
  return request({
    url: `/api/applications/${id}/cancel`,
    method: 'put'
  })
}

export function forwardApplication(id, newApproverId, reason) {
  return request({
    url: `/api/applications/${id}/forward`,
    method: 'put',
    params: { newApproverId, reason }
  })
}

export function getMyApplications(status) {
  return request({
    url: '/api/applications/my-applications',
    method: 'get',
    params: { status }
  })
}

export function getPendingApprovals() {
  return request({
    url: '/api/applications/pending-approvals',
    method: 'get'
  })
}

export function getApplicationStatistics(startDate, endDate, type) {
  return request({
    url: '/api/applications/statistics',
    method: 'get',
    params: { startDate, endDate, type }
  })
}

export function batchApproveApplications(applicationIds, approved, reason) {
  return request({
    url: '/api/applications/batch-approve',
    method: 'put',
    params: { applicationIds: applicationIds.join(','), approved, reason }
  })
}

export function downloadAttachment(id) {
  return request({
    url: `/api/applications/${id}/attachment`,
    method: 'get'
  })
}
