import request from '@/utils/request'
import type { Service, PageParams, PageResult, ApiResponse } from '@/types'

export function getServicePage(params: PageParams): Promise<ApiResponse<PageResult<Service>>> {
  return request({
    url: '/service/page',
    method: 'get',
    params
  })
}

export function getServiceById(id: string): Promise<ApiResponse<Service>> {
  return request({
    url: `/service/${id}`,
    method: 'get'
  })
}

export function updateService(data: Partial<Service>): Promise<ApiResponse<Service>> {
  return request({
    url: '/service',
    method: 'put',
    data
  })
}

export function cancelService(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/service/cancel/${id}`,
    method: 'put'
  })
}

export function completeService(id: string, remark?: string): Promise<ApiResponse<void>> {
  return request({
    url: `/service/complete/${id}`,
    method: 'put',
    params: { remark }
  })
}

export function assignService(id: string, assigneeId: string): Promise<ApiResponse<void>> {
  return request({
    url: `/service/assign/${id}`,
    method: 'put',
    params: { assigneeId }
  })
}

export function getServiceStatistics(projectId?: string): Promise<ApiResponse<Record<string, number>>> {
  return request({
    url: '/service/statistics',
    method: 'get',
    params: { projectId }
  })
}
