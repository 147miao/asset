import request from '@/utils/request'

export function getProjectPage(params) {
  return request({
    url: '/project/page',
    method: 'get',
    params
  })
}

export function getProjectById(id) {
  return request({
    url: `/project/${id}`,
    method: 'get'
  })
}

export function addProject(data) {
  return request({
    url: '/project',
    method: 'post',
    data
  })
}

export function updateProject(data) {
  return request({
    url: '/project',
    method: 'put',
    data
  })
}

export function deleteProject(id) {
  return request({
    url: `/project/${id}`,
    method: 'delete'
  })
}

export function getAllProjects() {
  return request({
    url: '/project/all',
    method: 'get'
  })
}

export function getProjectStatistics(projectId) {
  if (projectId == null) {
    return request({
      url: '/project/statistics/0',
      method: 'get'
    })
  }
  return request({
    url: `/project/statistics/${projectId}`,
    method: 'get'
  })
}
