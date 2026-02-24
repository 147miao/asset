import { create } from 'zustand'

export const useUserStore = create((set) => ({
  token: localStorage.getItem('token') || '',
  userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
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
    set({ token: '', userInfo: {} })
  }
}))
