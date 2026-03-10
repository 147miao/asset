import request from '@/utils/request'
import type { AssetCategory, ApiResponse } from '@/types'

export function getCategoryTree(): Promise<ApiResponse<AssetCategory[]>> {
  return request({
    url: '/asset-category/tree',
    method: 'get'
  })
}

export function getCategoriesByParentId(parentId: string): Promise<ApiResponse<AssetCategory[]>> {
  return request({
    url: `/asset-category/parent/${parentId}`,
    method: 'get'
  })
}

export function getCategoriesByAssetType(assetType: string): Promise<ApiResponse<AssetCategory[]>> {
  return request({
    url: `/asset-category/type/${assetType}`,
    method: 'get'
  })
}

export function saveCategory(data: Partial<AssetCategory>): Promise<ApiResponse<AssetCategory>> {
  return request({
    url: '/asset-category',
    method: 'post',
    data
  })
}

export function updateCategory(data: Partial<AssetCategory>): Promise<ApiResponse<AssetCategory>> {
  return request({
    url: '/asset-category',
    method: 'put',
    data
  })
}

export function deleteCategory(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/asset-category/${id}`,
    method: 'delete'
  })
}

export function updateSortOrder(data: { id: string; sort: number }[]): Promise<ApiResponse<void>> {
  return request({
    url: '/asset-category/sort',
    method: 'put',
    data
  })
}
