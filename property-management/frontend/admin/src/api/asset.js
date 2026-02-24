import request from '@/utils/request'

export function getAssetPage(params) {
  return request({
    url: '/asset/page',
    method: 'get',
    params
  })
}

export function getAssetById(id) {
  return request({
    url: `/asset/${id}`,
    method: 'get'
  })
}

export function addAsset(data) {
  return request({
    url: '/asset',
    method: 'post',
    data
  })
}

export function updateAsset(data) {
  return request({
    url: '/asset',
    method: 'put',
    data
  })
}

export function deleteAsset(id) {
  return request({
    url: `/asset/${id}`,
    method: 'delete'
  })
}

export function exportAssets(projectId) {
  return request({
    url: '/asset/export',
    method: 'get',
    params: { projectId },
    responseType: 'blob'
  })
}

export function getAssetsByProjectId(projectId) {
  return request({
    url: `/asset/project/${projectId}`,
    method: 'get'
  })
}

export function updateAssetStatus(id, status) {
  return request({
    url: `/asset/status/${id}`,
    method: 'put',
    params: { status }
  })
}
