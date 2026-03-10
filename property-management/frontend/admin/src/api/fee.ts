import request from '@/utils/request'
import type { Fee, PageParams, PageResult, ApiResponse } from '@/types'

export function getFeePage(params: PageParams): Promise<ApiResponse<PageResult<Fee>>> {
  return request({
    url: '/fee/page',
    method: 'get',
    params
  })
}

export function getFeeById(id: string): Promise<ApiResponse<Fee>> {
  return request({
    url: `/fee/${id}`,
    method: 'get'
  })
}

export function addFee(data: Partial<Fee>): Promise<ApiResponse<Fee>> {
  return request({
    url: '/fee',
    method: 'post',
    data
  })
}

export function updateFee(data: Partial<Fee>): Promise<ApiResponse<Fee>> {
  return request({
    url: '/fee',
    method: 'put',
    data
  })
}

export function deleteFee(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/fee/${id}`,
    method: 'delete'
  })
}

export function payFee(id: string, payMethod: string): Promise<ApiResponse<void>> {
  return request({
    url: `/fee/pay/${id}`,
    method: 'post',
    params: { payMethod }
  })
}

export function getIncomeStatistics(params: { startDate?: string; endDate?: string }): Promise<ApiResponse<Record<string, number>>> {
  return request({
    url: '/fee/incomeStatistics',
    method: 'get',
    params
  })
}

export function getArrearsStatistics(projectId?: string): Promise<ApiResponse<Record<string, number>>> {
  return request({
    url: '/fee/arrearsStatistics',
    method: 'get',
    params: { projectId }
  })
}
