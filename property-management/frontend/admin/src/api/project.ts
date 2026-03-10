import request from '@/utils/request'
import type { Project, PageParams, PageResult, ApiResponse } from '@/types'

export function getProjectPage(params: PageParams): Promise<ApiResponse<PageResult<Project>>> {
  return request({
    url: '/project/page',
    method: 'get',
    params
  })
}

export function getProjectById(id: string): Promise<ApiResponse<Project>> {
  return request({
    url: `/project/${id}`,
    method: 'get'
  })
}

export function addProject(data: Partial<Project>): Promise<ApiResponse<Project>> {
  return request({
    url: '/project',
    method: 'post',
    data
  })
}

export function updateProject(data: Partial<Project>): Promise<ApiResponse<Project>> {
  return request({
    url: '/project',
    method: 'put',
    data
  })
}

export function deleteProject(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/project/${id}`,
    method: 'delete'
  })
}

export function getAllProjects(): Promise<ApiResponse<Project[]>> {
  return request({
    url: '/project/all',
    method: 'get'
  })
}

export function getProjectStatistics(projectId?: string): Promise<ApiResponse<Record<string, number>>> {
  const url = projectId ? `/project/statistics/${projectId}` : '/project/statistics/0'
  return request({
    url,
    method: 'get'
  })
}
