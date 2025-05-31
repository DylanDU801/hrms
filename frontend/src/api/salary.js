import request from '@/utils/request'

export function fetchSalaries() {
  return request({
    url: '/salaries',
    method: 'get'
  })
}

export function fetchSalariesByEmployee(employeeId) {
  return request({
    url: `/salaries/employee/${employeeId}`,
    method: 'get'
  })
}

export function createSalary(data) {
  return request({
    url: '/salaries',
    method: 'post',
    data
  })
}

export function updateSalary(id, data) {
  return request({
    url: `/salaries/${id}`,
    method: 'put',
    data
  })
}

export function deleteSalary(id) {
  return request({
    url: `/salaries/${id}`,
    method: 'delete'
  })
}
