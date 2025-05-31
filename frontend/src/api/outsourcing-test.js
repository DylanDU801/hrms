import request from '@/utils/request'

export function fetchTests(query) {
  return request({
    url: '/api/outsourcing-tests',
    method: 'get',
    params: query
  })
}

export function fetchTest(id) {
  return request({
    url: `/api/outsourcing-tests/${id}`,
    method: 'get'
  })
}

export function createTest(data) {
  return request({
    url: '/api/outsourcing-tests',
    method: 'post',
    data
  })
}

export function updateTest(id, data) {
  return request({
    url: `/api/outsourcing-tests/${id}`,
    method: 'put',
    data
  })
}

export function deleteTest(id) {
  return request({
    url: `/api/outsourcing-tests/${id}`,
    method: 'delete'
  })
}

export function assignTest(id, testerId) {
  return request({
    url: `/api/outsourcing-tests/${id}/assign`,
    method: 'put',
    params: { testerId }
  })
}

export function submitTestResult(id, result, actualHours) {
  return request({
    url: `/api/outsourcing-tests/${id}/submit`,
    method: 'put',
    params: { result, actualHours }
  })
}

export function reviewTest(id, score, comment) {
  return request({
    url: `/api/outsourcing-tests/${id}/review`,
    method: 'put',
    params: { score, comment }
  })
}

export function getMyTests() {
  return request({
    url: '/api/outsourcing-tests/my-tests',
    method: 'get'
  })
}

export function getTestStatistics(startDate, endDate) {
  return request({
    url: '/api/outsourcing-tests/statistics',
    method: 'get',
    params: { startDate, endDate }
  })
}

export function getTesterPerformance(testerId, month) {
  return request({
    url: '/api/outsourcing-tests/performance',
    method: 'get',
    params: { testerId, month }
  })
}
