import request from '../utils/request'

export const login = (data) => request({ url: '/user/login', method: 'post', data })

export const getHouseList = () => request({ url: '/house/list', method: 'get' })

export const getHousesByOwner = (ownerId) => request({ url: `/house/owner/${ownerId}`, method: 'get' })

export const getFeeList = (params) => request({ url: '/fee/page', method: 'get', params })

export const getFeesByUser = (userId) => request({ url: `/fee/user/${userId}`, method: 'get' })

export const getUnpaidFees = (userId) => request({ url: `/fee/unpaid/${userId}`, method: 'get' })

export const payFee = (id, payMethod) => request({ url: `/fee/pay/${id}`, method: 'post', params: { payMethod } })

export const getRepairList = (params) => request({ url: '/repair/page', method: 'get', params })

export const getRepairsByUser = (userId) => request({ url: `/repair/user/${userId}`, method: 'get' })

export const createRepair = (data) => request({ url: '/repair', method: 'post', data })

export const addRepair = (data) => request({ url: '/repair', method: 'post', data })

export const cancelRepair = (id) => request({ url: `/repair/cancel/${id}`, method: 'put' })

export const rateRepair = (id, rating, feedback) => request({ url: `/repair/rate/${id}`, method: 'put', params: { rating, feedback } })

export const getServiceList = (params) => request({ url: '/service/page', method: 'get', params })

export const getServicesByUser = (userId) => request({ url: `/service/user/${userId}`, method: 'get' })

export const createService = (data) => request({ url: '/service', method: 'post', data })

export const addService = (data) => request({ url: '/service', method: 'post', data })

export const cancelService = (id) => request({ url: `/service/cancel/${id}`, method: 'put' })

export const rateService = (id, rating, feedback) => request({ url: `/service/rate/${id}`, method: 'put', params: { rating, feedback } })

export const getMessageList = (params) => request({ url: '/message/page', method: 'get', params })

export const getMessages = (userId, params) => request({ url: '/message/page', method: 'get', params: { userId, ...params } })

export const getUnreadMessages = (userId) => request({ url: `/message/unread/${userId}`, method: 'get' })

export const markAsRead = (id) => request({ url: `/message/read/${id}`, method: 'put' })

export const markAllAsRead = (userId) => request({ url: `/message/readAll/${userId}`, method: 'put' })

export const updateUserInfo = (data) => request({ url: '/user', method: 'put', data })

export const updatePassword = (data) => request({ url: '/user/password', method: 'put', data })

export const getProjectList = () => request({ url: '/project/list', method: 'get' })
