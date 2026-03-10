import axios, { AxiosResponse, AxiosError } from 'axios'
import { message, notification } from 'antd'
import { useUserStore } from '../stores/user'

const LOG_ENABLED = true

const logger = {
  debug: (...args: unknown[]) => LOG_ENABLED && console.log('[DEBUG]', new Date().toISOString(), ...args),
  info: (...args: unknown[]) => LOG_ENABLED && console.info('[INFO]', new Date().toISOString(), ...args),
  warn: (...args: unknown[]) => LOG_ENABLED && console.warn('[WARN]', new Date().toISOString(), ...args),
  error: (...args: unknown[]) => LOG_ENABLED && console.error('[ERROR]', new Date().toISOString(), ...args),
}

const ERROR_MESSAGES: Record<number, string> = {
  400: '请求参数错误',
  401: '登录已过期，请重新登录',
  403: '没有权限访问该资源',
  404: '请求的资源不存在',
  405: '请求方法不允许',
  408: '请求超时',
  500: '服务器内部错误',
  502: '网关错误',
  503: '服务暂时不可用',
  504: '网关超时'
}

const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

request.interceptors.request.use(
  (config) => {
    const token = useUserStore.getState().token
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error: AxiosError) => {
    message.error('请求发送失败')
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data as { code: number; message?: string }
    
    if (res.code !== 200) {
      const errorMessage = res.message || ERROR_MESSAGES[res.code] || '请求失败'
      
      if (res.code === 401) {
        useUserStore.getState().logout()
        
        notification.warning({
          message: '登录过期',
          description: '您的登录已过期，请重新登录',
          duration: 3
        })
        
        window.location.href = '/login'
        return Promise.reject(new Error('登录已过期'))
      }
      
      if (res.code === 403) {
        notification.warning({
          message: '权限不足',
          description: errorMessage,
          duration: 3
        })
        return Promise.reject(new Error(errorMessage))
      }
      
      if (res.code >= 500) {
        notification.error({
          message: '服务器错误',
          description: errorMessage,
          duration: 5
        })
        return Promise.reject(new Error(errorMessage))
      }
      
      message.error(errorMessage)
      return Promise.reject(new Error(errorMessage))
    }
    
    return response.data
  },
  (error: AxiosError) => {
    if (!error.response) {
      const networkMessage = error.code === 'ECONNABORTED' 
        ? '请求超时，请检查网络连接' 
        : '网络连接异常，请检查网络设置'
      
      if (error.code === 'ECONNABORTED') {
        notification.warning({
          message: '请求超时',
          description: '请求超时，请稍后重试',
          duration: 3
        })
      } else {
        notification.error({
          message: '网络错误',
          description: networkMessage,
          duration: 5
        })
      }
      
      return Promise.reject(new Error(networkMessage))
    }
    
    const status = error.response.status
    const errorData = error.response.data as { message?: string }
    const errorMessage = errorData?.message || ERROR_MESSAGES[status] || `请求失败 (${status})`
    
    if (status === 401) {
      useUserStore.getState().logout()
      
      notification.warning({
        message: '登录过期',
        description: '您的登录已过期，请重新登录',
        duration: 3
      })
      
      window.location.href = '/login'
      return Promise.reject(new Error('登录已过期'))
    }
    
    if (status === 403) {
      notification.warning({
        message: '权限不足',
        description: errorMessage,
        duration: 3
      })
      return Promise.reject(new Error(errorMessage))
    }
    
    if (status >= 500) {
      notification.error({
        message: '服务器错误',
        description: errorMessage,
        duration: 5
      })
      return Promise.reject(new Error(errorMessage))
    }
    
    message.error(errorMessage)
    return Promise.reject(new Error(errorMessage))
  }
)

export default request
export { logger }
