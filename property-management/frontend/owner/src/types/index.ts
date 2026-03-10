export interface User {
  id: string
  username: string
  nickname?: string
  realName?: string
  phone: string
  avatar?: string
  token?: string
}

export interface LoginParams {
  phone: string
  password: string
}

export interface ApiResponse<T = unknown> {
  code: number
  data: T
  message: string
}

export interface House {
  id: string
  building: string
  unit: string
  number: string
  area: number
  status: 'normal' | 'abnormal'
}

export interface Fee {
  id: string
  feeName?: string
  type: string
  amount: number
  status: 'pending' | 'paid' | 'overdue'
  dueDate: string
}

export interface Repair {
  id: string
  title: string
  description?: string
  status: 'pending' | 'processing' | 'completed' | 'cancelled'
  createTime: string
}

export interface Service {
  id: string
  serviceName: string
  description: string
  fee: number
  status: string
  createTime: string
  userId?: string
  userName?: string
  userPhone?: string
  projectId?: string
  projectName?: string
  houseId?: string
  houseInfo?: string
  appointmentDate?: string
  appointmentTime?: string
  address?: string
  completeDate?: string
  remark?: string
  feedback?: string
  rating?: number
}

export interface Message {
  id: string
  title: string
  content: string
  createTime: string
  read: boolean
}
