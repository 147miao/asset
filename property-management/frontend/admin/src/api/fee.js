import request from '@/utils/request'

export function getFeePage(params) {
  return request({
    url: '/fee/page',
    method: 'get',
    params
  })
}

export function getFeeById(id) {
  return request({
    url: `/fee/${id}`,
    method: 'get'
  })
}

export function addFee(data) {
  return request({
    url: '/fee',
    method: 'post',
    data
  })
}

export function updateFee(data) {
  return request({
    url: '/fee',
    method: 'put',
    data
  })
}

export function deleteFee(id) {
  return request({
    url: `/fee/${id}`,
    method: 'delete'
  })
}

export function payFee(id, payMethod) {
  return request({
    url: `/fee/pay/${id}`,
    method: 'post',
    params: { payMethod }
  })
}

export function getIncomeStatistics(params) {
  return request({
    url: '/fee/incomeStatistics',
    method: 'get',
    params
  })
}

export function getArrearsStatistics(projectId) {
  return request({
    url: '/fee/arrearsStatistics',
    method: 'get',
    params: { projectId }
  })
}

export function getTotalArrears(projectId) {
  return request({
    url: '/fee/totalArrears',
    method: 'get',
    params: { projectId }
  })
}

export function getFeeTypeStatistics(params) {
  return request({
    url: '/fee/feeTypeStatistics',
    method: 'get',
    params
  })
}

export function autoGenerateFee(projectId, feeType, billingPeriod) {
  return request({
    url: '/fee/autoGenerate',
    method: 'post',
    params: { projectId, feeType, billingPeriod }
  })
}

export function getReport(params) {
  return request({
    url: '/fee/report',
    method: 'get',
    params
  })
}

export function getFeeStandardPage(params) {
  return request({
    url: '/feeStandard/page',
    method: 'get',
    params
  })
}

export function addFeeStandard(data) {
  return request({
    url: '/feeStandard',
    method: 'post',
    data
  })
}

export function updateFeeStandard(data) {
  return request({
    url: '/feeStandard',
    method: 'put',
    data
  })
}

export function deleteFeeStandard(id) {
  return request({
    url: `/feeStandard/${id}`,
    method: 'delete'
  })
}

export function getFeeStandardByProject(projectId) {
  return request({
    url: `/feeStandard/project/${projectId}`,
    method: 'get'
  })
}
