import request from '@/utils/request'
import type { Message, PageParams, PageResult, ApiResponse } from '@/types'

export function getMessagePage(params: PageParams): Promise<ApiResponse<PageResult<Message>>> {
  return request({
    url: '/message/page',
    method: 'get',
    params
  })
}

export function sendMessage(data: Partial<Message>): Promise<ApiResponse<Message>> {
  return request({
    url: '/message/send',
    method: 'post',
    params: data
  })
}

export function sendBroadcast(data: Partial<Message>): Promise<ApiResponse<Message>> {
  return request({
    url: '/message/broadcast',
    method: 'post',
    params: data
  })
}

export function deleteMessage(id: string): Promise<ApiResponse<void>> {
  return request({
    url: `/message/${id}`,
    method: 'delete'
  })
}

export function getUnreadMessages(userId: string): Promise<ApiResponse<Message[]>> {
  return request({
    url: `/message/unread/${userId}`,
    method: 'get'
  })
}
