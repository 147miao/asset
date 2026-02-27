import request from '@/utils/request'

export function getCategoryTree() {
  return request({
    url: '/asset-category/tree',
    method: 'get'
  })
}

export function getCategoriesByParentId(parentId) {
  return request({
    url: `/asset-category/parent/${parentId}`,
    method: 'get'
  })
}

export function getCategoriesByAssetType(assetType) {
  return request({
    url: `/asset-category/type/${assetType}`,
    method: 'get'
  })
}

export function saveCategory(data) {
  return request({
    url: '/asset-category',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/asset-category',
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/asset-category/${id}`,
    method: 'delete'
  })
}

export function updateSortOrder(data) {
  return request({
    url: '/asset-category/sort',
    method: 'put',
    data
  })
}