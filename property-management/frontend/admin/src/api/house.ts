import request from '@/utils/request'
import type { House, PageParams, PageResult, ApiResponse } from '@/types'

export function getHousePage(params: PageParams): Promise<ApiResponse<PageResult<House>>> {
  return request({
    url: '/house/page',
    method: 'get',
    params
  })
}

export function getHouseById(id: string): Promise<ApiResponse<House>> {
  return request({
    url: `/house/${id}`,
    method: 'get'
  })
}

export function addHouse(data: Partial<House>): Promise<ApiResponse<House>> {
  return request({
    url: '/house',
    method: 'post',
    data
  })
}

export function updateHouse(data: Partial<House>): Promise<ApiResponse<House>> {
  return request({
    url: '/house',
    method: 'put',
    data
  })
}

export function deleteHouse(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/house/${id}`,
    method: 'delete'
  })
}

export function getHousesByProjectId(projectId: string): Promise<ApiResponse<House[]>> {
  return request({
    url: `/house/project/${projectId}`,
    method: 'get'
  })
}
