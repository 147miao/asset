import request from '@/utils/request'
import type { Repair, PageParams, PageResult, ApiResponse } from '@/types'

export function getRepairPage(params: PageParams): Promise<ApiResponse<PageResult<Repair>>> {
  return request({
    url: '/repair/page',
    method: 'get',
    params
  })
}

export function getRepairById(id: string): Promise<ApiResponse<Repair>> {
  return request({
    url: `/repair/${id}`,
    method: 'get'
  })
}

export function updateRepair(data: Partial<Repair>): Promise<ApiResponse<Repair>> {
  return request({
    url: '/repair',
    method: 'put',
    data
  })
}

export function cancelRepair(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/repair/cancel/${id}`,
    method: 'put'
  })
}

export function assignRepair(id: string, assigneeId: string, assigneeName: string): Promise<ApiResponse<void>> {
  return request({
    url: `/repair/assign/${id}`,
    method: 'put',
    params: { assigneeId, assigneeName }
  })
}

export function completeRepair(id: string, result: string): Promise<ApiResponse<void>> {
  return request({
    url: `/repair/complete/${id}`,
    method: 'put',
    params: { result }
  })
}

export function getRepairStatistics(projectId?: string): Promise<ApiResponse<Record<string, number>>> {
  return request({
    url: '/repair/statistics',
    method: 'get',
    params: { projectId }
  })
}
