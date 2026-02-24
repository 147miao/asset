import request from '@/utils/request'

export function getServicePage(params) {
  return request({
    url: '/service/page',
    method: 'get',
    params
  })
}

export function getServiceById(id) {
  return request({
    url: `/service/${id}`,
    method: 'get'
  })
}

export function updateService(data) {
  return request({
    url: '/service',
    method: 'put',
    data
  })
}

export function cancelService(id) {
  return request({
    url: `/service/cancel/${id}`,
    method: 'put'
  })
}

export function completeService(id, remark) {
  return request({
    url: `/service/complete/${id}`,
    method: 'put',
    params: { remark }
  })
}

export function assignService(id, assigneeId) {
  return request({
    url: `/service/assign/${id}`,
    method: 'put',
    params: { assigneeId }
  })
}

export function getServiceStatistics(projectId) {
  return request({
    url: '/service/statistics',
    method: 'get',
    params: { projectId }
  })
}
