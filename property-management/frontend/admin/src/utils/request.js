import axios from 'axios'
import { ElMessage, ElNotification } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const LOG_ENABLED = true
const LOG_LEVEL = 'debug'

const logger = {
  debug: (...args) => LOG_ENABLED && ['debug', 'info', 'warn', 'error'].includes(LOG_LEVEL) && console.log('[DEBUG]', new Date().toISOString(), ...args),
  info: (...args) => LOG_ENABLED && ['info', 'warn', 'error'].includes(LOG_LEVEL) && console.info('[INFO]', new Date().toISOString(), ...args),
  warn: (...args) => LOG_ENABLED && ['warn', 'error'].includes(LOG_LEVEL) && console.warn('[WARN]', new Date().toISOString(), ...args),
  error: (...args) => LOG_ENABLED && ['error'].includes(LOG_LEVEL) && console.error('[ERROR]', new Date().toISOString(), ...args),
  logRequest: (config) => {
    if (!LOG_ENABLED) return
    const requestId = Date.now().toString(36) + Math.random().toString(36).substr(2, 5)
    config._requestId = requestId
    config._startTime = Date.now()
    logger.info(`[请求开始] ID: ${requestId}`, {
      method: config.method?.toUpperCase(),
      url: config.url,
      params: config.params,
      data: config.data
    })
  },
  logResponse: (response) => {
    if (!LOG_ENABLED || !response.config) return
    const config = response.config
    const duration = Date.now() - (config._startTime || 0)
    logger.info(`[请求成功] ID: ${config._requestId}`, {
      status: response.status,
      duration: `${duration}ms`,
      data: response.data
    })
  },
  logError: (err) => {
    if (!LOG_ENABLED) return
    const config = err.config || {}
    const duration = Date.now() - (config._startTime || 0)
    logger.error(`[请求失败] ID: ${config._requestId}`, {
      url: config.url,
      duration: `${duration}ms`,
      error: err.message,
      response: err.response?.data
    })
  }
}

const ERROR_MESSAGES = {
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

const NETWORK_ERROR_MESSAGES = {
  ECONNABORTED: '请求超时，请检查网络连接',
  ERR_NETWORK: '网络连接失败，请检查网络设置',
  ERR_CANCELED: '请求已取消',
  ETIMEDOUT: '请求超时'
}

const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

request.interceptors.request.use(
  config => {
    logger.logRequest(config)
    
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    
    return config
  },
  error => {
    logger.logError(error)
    ElMessage.error('请求发送失败')
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    logger.logResponse(response)
    
    const res = response.data
    
    if (res.code !== 200) {
      const errorMessage = res.message || ERROR_MESSAGES[res.code] || '请求失败'
      
      if (res.code === 401) {
        logger.warn('用户未授权，跳转登录页')
        const userStore = useUserStore()
        userStore.logout()
        
        ElNotification({
          title: '登录过期',
          message: '您的登录已过期，请重新登录',
          type: 'warning',
          duration: 3000
        })
        
        router.push('/login')
        return Promise.reject(new Error('登录已过期'))
      }
      
      if (res.code === 403) {
        ElNotification({
          title: '权限不足',
          message: errorMessage,
          type: 'warning',
          duration: 3000
        })
        return Promise.reject(new Error(errorMessage))
      }
      
      if (res.code >= 500) {
        ElNotification({
          title: '服务器错误',
          message: errorMessage,
          type: 'error',
          duration: 5000
        })
        return Promise.reject(new Error(errorMessage))
      }
      
      ElMessage.error(errorMessage)
      return Promise.reject(new Error(errorMessage))
    }
    
    return res
  },
  error => {
    logger.logError(error)
    
    if (!error.response) {
      const networkMessage = NETWORK_ERROR_MESSAGES[error.code] || '网络连接异常，请检查网络设置'
      
      if (error.code === 'ECONNABORTED') {
        ElNotification({
          title: '请求超时',
          message: '请求超时，请稍后重试',
          type: 'warning',
          duration: 3000
        })
      } else {
        ElNotification({
          title: '网络错误',
          message: networkMessage,
          type: 'error',
          duration: 5000
        })
      }
      
      return Promise.reject(new Error(networkMessage))
    }
    
    const status = error.response.status
    const errorData = error.response.data
    const errorMessage = errorData?.message || ERROR_MESSAGES[status] || `请求失败 (${status})`
    
    if (status === 401) {
      logger.warn('用户未授权，跳转登录页')
      const userStore = useUserStore()
      userStore.logout()
      
      ElNotification({
        title: '登录过期',
        message: '您的登录已过期，请重新登录',
        type: 'warning',
        duration: 3000
      })
      
      router.push('/login')
      return Promise.reject(new Error('登录已过期'))
    }
    
    if (status === 403) {
      ElNotification({
        title: '权限不足',
        message: errorMessage,
        type: 'warning',
        duration: 3000
      })
      return Promise.reject(new Error(errorMessage))
    }
    
    if (status >= 500) {
      ElNotification({
        title: '服务器错误',
        message: errorMessage,
        type: 'error',
        duration: 5000
      })
      return Promise.reject(new Error(errorMessage))
    }
    
    ElMessage.error(errorMessage)
    return Promise.reject(new Error(errorMessage))
  }
)

export default request
export { logger }
