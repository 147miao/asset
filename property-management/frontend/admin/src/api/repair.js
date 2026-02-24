import request from '@/utils/request'

export function getRepairPage(params) {
  return request({
    url: '/repair/page',
    method: 'get',
    params
  })
}

export function getRepairById(id) {
  return request({
    url: `/repair/${id}`,
    method: 'get'
  })
}

export function updateRepair(data) {
  return request({
    url: '/repair',
    method: 'put',
    data
  })
}

export function cancelRepair(id) {
  return request({
    url: `/repair/cancel/${id}`,
    method: 'put'
  })
}

export function assignRepair(id, assigneeId, assigneeName) {
  return request({
    url: `/repair/assign/${id}`,
    method: 'put',
    params: { assigneeId, assigneeName }
  })
}

export function completeRepair(id, result) {
  return request({
    url: `/repair/complete/${id}`,
    method: 'put',
    params: { result }
  })
}

export function getRepairStatistics(projectId) {
  return request({
    url: '/repair/statistics',
    method: 'get',
    params: { projectId }
  })
}
