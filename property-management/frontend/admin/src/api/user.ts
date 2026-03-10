import request from '@/utils/request'
import type { User, LoginParams, PageParams, PageResult, ApiResponse } from '@/types'

export function login(data: LoginParams): Promise<ApiResponse<User>> {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function register(data: Partial<User>): Promise<ApiResponse<User>> {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function getUserPage(params: PageParams): Promise<ApiResponse<PageResult<User>>> {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

export function getUserById(id: string): Promise<ApiResponse<User>> {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function addUser(data: Partial<User>): Promise<ApiResponse<User>> {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateUser(data: Partial<User>): Promise<ApiResponse<User>> {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

export function deleteUser(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

export function resetPassword(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/user/resetPassword/${id}`,
    method: 'put'
  })
}

export function getUserStatistics(): Promise<ApiResponse<Record<string, number>>> {
  return request({
    url: '/user/statistics',
    method: 'get'
  })
}

export function updateStatus(id: string, status: string): Promise<ApiResponse<void>> {
  return request({
    url: `/user/status/${id}`,
    method: 'put',
    params: { status }
  })
}
