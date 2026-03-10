export interface User {
  id: string
  username: string
  realName?: string
  phone: string
  email?: string
  avatar?: string
  role: 'admin' | 'staff'
  status: 'active' | 'inactive'
  createTime?: string
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
  ownerId?: string
  ownerName?: string
  status: 'vacant' | 'occupied' | 'rented'
  createTime?: string
}

export interface Fee {
  id: string
  houseId: string
  houseNumber?: string
  ownerName?: string
  type: string
  amount: number
  status: 'pending' | 'paid' | 'overdue'
  dueDate: string
  payTime?: string
  createTime?: string
}

export interface Repair {
  id: string
  title: string
  description?: string
  houseId?: string
  houseNumber?: string
  ownerName?: string
  ownerPhone?: string
  status: 'pending' | 'processing' | 'completed' | 'cancelled'
  handlerId?: string
  handlerName?: string
  images?: string[]
  createTime: string
  updateTime?: string
}

export interface Service {
  id: string
  name: string
  description: string
  price: number
  category: string
  status: 'active' | 'inactive'
  image?: string
  createTime?: string
}

export interface Message {
  id: string
  title: string
  content: string
  type: 'notice' | 'announcement' | 'reminder'
  target: 'all' | 'specific'
  targetUsers?: string[]
  readCount?: number
  createTime: string
}

export interface Asset {
  id: string
  name: string
  categoryId: string
  categoryName?: string
  quantity: number
  unit: string
  location?: string
  status: 'normal' | 'maintenance' | 'scrapped'
  purchaseDate?: string
  createTime?: string
}

export interface AssetCategory {
  id: string
  name: string
  parentId?: string
  sort: number
  children?: AssetCategory[]
}

export interface Project {
  id: string
  name: string
  description?: string
  budget: number
  status: 'planning' | 'ongoing' | 'completed' | 'cancelled'
  startDate?: string
  endDate?: string
  managerId?: string
  managerName?: string
  createTime?: string
}

export interface DashboardStats {
  userCount: number
  houseCount: number
  feeTotal: number
  repairCount: number
  pendingRepairCount: number
  pendingFeeCount: number
}

export interface LoginParams {
  username: string
  password: string
}

export interface PageParams {
  page?: number
  size?: number
  keyword?: string
}

export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  size: number
}
