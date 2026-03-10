import request from '../utils/request'
import type { ApiResponse } from '../types'

interface LoginData {
  phone: string
  password: string
}

interface UserInfo {
  id: string
  username: string
  realName: string
  phone: string
  [key: string]: unknown
}

export const login = (data: LoginData) => 
  request<ApiResponse<UserInfo>>({ url: '/user/login', method: 'post', data })

export const getHouseList = () => 
  request({ url: '/house/list', method: 'get' })

export const getHousesByOwner = (ownerId: string) => 
  request({ url: `/house/owner/${ownerId}`, method: 'get' })

export const getFeeList = (params?: Record<string, unknown>) => 
  request({ url: '/fee/page', method: 'get', params })

export const getFeesByUser = (userId: string) => 
  request({ url: `/fee/user/${userId}`, method: 'get' })

export const getUnpaidFees = (userId: string) => 
  request({ url: `/fee/unpaid/${userId}`, method: 'get' })

export const payFee = (id: string, payMethod: string) => 
  request({ url: `/fee/pay/${id}`, method: 'post', params: { payMethod } })

export const getRepairList = (params?: Record<string, unknown>) => 
  request({ url: '/repair/page', method: 'get', params })

export const getRepairsByUser = (userId: string) => 
  request({ url: `/repair/user/${userId}`, method: 'get' })

export const createRepair = (data: Record<string, unknown>) => 
  request({ url: '/repair', method: 'post', data })

export const addRepair = (data: Record<string, unknown>) => 
  request({ url: '/repair', method: 'post', data })

export const cancelRepair = (id: string) => 
  request({ url: `/repair/cancel/${id}`, method: 'put' })

export const rateRepair = (id: string, rating: number, feedback: string) => 
  request({ url: `/repair/rate/${id}`, method: 'put', params: { rating, feedback } })

export const getServiceList = (params?: Record<string, unknown>) => 
  request({ url: '/service/page', method: 'get', params })

export const getServicesByUser = (userId: string) => 
  request({ url: `/service/user/${userId}`, method: 'get' })

export const createService = (data: Record<string, unknown>) => 
  request({ url: '/service', method: 'post', data })

export const addService = (data: Record<string, unknown>) => 
  request({ url: '/service', method: 'post', data })

export const cancelService = (id: string) => 
  request({ url: `/service/cancel/${id}`, method: 'put' })

export const rateService = (id: string, rating: number, feedback: string) => 
  request({ url: `/service/rate/${id}`, method: 'put', params: { rating, feedback } })

export const getMessageList = (params?: Record<string, unknown>) => 
  request({ url: '/message/page', method: 'get', params })

export const getMessages = (userId: string, params?: Record<string, unknown>) => 
  request({ url: '/message/page', method: 'get', params: { userId, ...params } })

export const getUnreadMessages = (userId: string) => 
  request({ url: `/message/unread/${userId}`, method: 'get' })

export const markAsRead = (id: string) => 
  request({ url: `/message/read/${id}`, method: 'put' })

export const markAllAsRead = (userId: string) => 
  request({ url: `/message/readAll/${userId}`, method: 'put' })

export const updateUserInfo = (data: Record<string, unknown>) => 
  request({ url: '/user', method: 'put', data })

export const updatePassword = (data: Record<string, unknown>) => 
  request({ url: '/user/password', method: 'put', data })

export const getProjectList = () => 
  request({ url: '/project/list', method: 'get' })
