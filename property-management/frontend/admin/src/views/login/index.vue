<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">物业管理系统</h2>
      <el-form :model="loginForm" :rules="rules" ref="formRef">
        <el-form-item prop="phone">
          <el-input v-model="loginForm.phone" placeholder="请输入手机号" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { logger } from '@/utils/request'
import { encodeToken } from '@/utils/base64'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  phone: '',
  password: ''
})

const rules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  try {
    await formRef.value.validate()
  } catch (error) {
    logger.warn('登录表单验证失败', error)
    return
  }
  
  loading.value = true
  logger.info('开始登录请求', { phone: loginForm.phone })
  
  try {
    const res = await login(loginForm)
    
    if (!res.data) {
      logger.error('登录响应数据为空')
      ElMessage.error('登录失败：服务器响应异常')
      return
    }
    
    const token = generateToken(res.data)
    userStore.setToken(token)
    userStore.setUserInfo(res.data)
    
    logger.info('登录成功', { userId: res.data.id, userName: res.data.realName, userType: res.data.userType })
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    logger.error('登录失败', error)
    
    if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('登录失败，请稍后重试')
    }
  } finally {
    loading.value = false
  }
}

const generateToken = (user) => {
  if (!user || !user.id) {
    logger.error('生成Token失败：用户信息无效', user)
    throw new Error('用户信息无效')
  }
  return encodeToken({ userId: user.id, username: user.realName, userType: user.userType })
}
</script>

<style lang="scss" scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .login-box {
    width: 400px;
    padding: 40px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

    .title {
      text-align: center;
      margin-bottom: 30px;
      color: #303133;
    }
  }
}
</style>
