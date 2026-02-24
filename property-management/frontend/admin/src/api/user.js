import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function getUserPage(params) {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

export function getUserById(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function addUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

export function resetPassword(id) {
  return request({
    url: `/user/resetPassword/${id}`,
    method: 'put'
  })
}

export function getUserStatistics() {
  return request({
    url: '/user/statistics',
    method: 'get'
  })
}

export function updateStatus(id, status) {
  return request({
    url: `/user/status/${id}`,
    method: 'put',
    params: { status }
  })
}
