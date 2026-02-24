export const encodeBase64 = (str) => {
  try {
    return btoa(unescape(encodeURIComponent(str)))
  } catch (error) {
    console.error('Base64 编码失败:', error)
    return ''
  }
}

export const decodeBase64 = (str) => {
  try {
    return decodeURIComponent(escape(atob(str)))
  } catch (error) {
    console.error('Base64 解码失败:', error)
    return ''
  }
}

export const encodeToken = (data) => {
  try {
    const jsonStr = JSON.stringify(data)
    return encodeBase64(jsonStr)
  } catch (error) {
    console.error('Token 编码失败:', error)
    return ''
  }
}

export const decodeToken = (token) => {
  try {
    const jsonStr = decodeBase64(token)
    return JSON.parse(jsonStr)
  } catch (error) {
    console.error('Token 解码失败:', error)
    return null
  }
}
