import request from '@/utils/request'

export function getMessagePage(params) {
  return request({
    url: '/message/page',
    method: 'get',
    params
  })
}

export function sendMessage(data) {
  return request({
    url: '/message/send',
    method: 'post',
    params: data
  })
}

export function sendBroadcast(data) {
  return request({
    url: '/message/broadcast',
    method: 'post',
    params: data
  })
}

export function deleteMessage(id) {
  return request({
    url: `/message/${id}`,
    method: 'delete'
  })
}
