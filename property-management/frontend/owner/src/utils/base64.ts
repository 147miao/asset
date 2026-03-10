export const encodeBase64 = (str: string): string => {
  try {
    return btoa(unescape(encodeURIComponent(str)))
  } catch {
    console.error('Base64 编码失败:')
    return ''
  }
}

export const decodeBase64 = (str: string): string => {
  try {
    return decodeURIComponent(escape(atob(str)))
  } catch {
    console.error('Base64 解码失败:')
    return ''
  }
}

export interface TokenData {
  userId: string
  username: string
}

export const encodeToken = (data: TokenData): string => {
  try {
    const jsonStr = JSON.stringify(data)
    return encodeBase64(jsonStr)
  } catch {
    console.error('Token 编码失败:')
    return ''
  }
}

export const decodeToken = (token: string): TokenData | null => {
  try {
    const jsonStr = decodeBase64(token)
    return JSON.parse(jsonStr)
  } catch {
    console.error('Token 解码失败:')
    return null
  }
}
