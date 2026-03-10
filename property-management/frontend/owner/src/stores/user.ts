import { create } from 'zustand'
import type { User } from '../types'

interface UserState {
  token: string
  userInfo: User | null
  setToken: (token: string) => void
  setUserInfo: (info: User) => void
  logout: () => void
}

export const useUserStore = create<UserState>((set) => ({
  token: localStorage.getItem('token') || '',
  userInfo: JSON.parse(localStorage.getItem('userInfo') || 'null'),
  setToken: (token) => {
    localStorage.setItem('token', token)
    set({ token })
  },
  setUserInfo: (info) => {
    localStorage.setItem('userInfo', JSON.stringify(info))
    set({ userInfo: info })
  },
  logout: () => {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    set({ token: '', userInfo: null })
  }
}))
