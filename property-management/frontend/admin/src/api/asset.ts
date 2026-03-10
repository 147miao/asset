import request from '@/utils/request'
import type { Asset, PageParams, PageResult, ApiResponse } from '@/types'

export function getAssetPage(params: PageParams): Promise<ApiResponse<PageResult<Asset>>> {
  return request({
    url: '/asset/page',
    method: 'get',
    params
  })
}

export function getAssetById(id: string): Promise<ApiResponse<Asset>> {
  return request({
    url: `/asset/${id}`,
    method: 'get'
  })
}

export function addAsset(data: Partial<Asset>): Promise<ApiResponse<Asset>> {
  return request({
    url: '/asset',
    method: 'post',
    data
  })
}

export function updateAsset(data: Partial<Asset>): Promise<ApiResponse<Asset>> {
  return request({
    url: '/asset',
    method: 'put',
    data
  })
}

export function deleteAsset(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/asset/${id}`,
    method: 'delete'
  })
}

export function deleteAssets(ids: string[]): Promise<ApiResponse<void>> {
  return request({
    url: '/asset/batch',
    method: 'delete',
    data: ids
  })
}

export function exportAssets(projectId?: string): Promise<Blob> {
  return request({
    url: '/asset/export',
    method: 'get',
    params: { projectId },
    responseType: 'blob'
  })
}

export function getAssetsByProjectId(projectId: string): Promise<ApiResponse<Asset[]>> {
  return request({
    url: `/asset/project/${projectId}`,
    method: 'get'
  })
}
