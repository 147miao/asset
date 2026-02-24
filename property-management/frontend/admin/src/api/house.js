import request from '@/utils/request'

export function getHousePage(params) {
  return request({
    url: '/house/page',
    method: 'get',
    params
  })
}

export function getHouseById(id) {
  return request({
    url: `/house/${id}`,
    method: 'get'
  })
}

export function addHouse(data) {
  return request({
    url: '/house',
    method: 'post',
    data
  })
}

export function updateHouse(data) {
  return request({
    url: '/house',
    method: 'put',
    data
  })
}

export function deleteHouse(id) {
  return request({
    url: `/house/${id}`,
    method: 'delete'
  })
}

export function getHousesByProjectId(projectId) {
  return request({
    url: `/house/project/${projectId}`,
    method: 'get'
  })
}
