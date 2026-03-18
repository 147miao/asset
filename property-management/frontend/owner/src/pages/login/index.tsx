import { Form, Input, Button, Card, message } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { login } from '../../api'
import { useUserStore } from '../../stores/user'
import { encodeToken } from '../../utils/base64'
import type { User } from '../../types'
import './login.css'

interface LoginFormValues {
  phone: string
  password: string
}

function Login() {
  const navigate = useNavigate()
  const setToken = useUserStore((state) => state.setToken)
  const setUserInfo = useUserStore((state) => state.setUserInfo)
  const [form] = Form.useForm()

  const handleSubmit = async (values: LoginFormValues) => {
    try {
      const res = await login(values) as unknown as { data: User }
      
      if (!res || !res.data) {
        message.error('登录失败：服务器响应异常')
        return
      }
      
      const userInfo = res.data
      const token = encodeToken({ userId: userInfo.id, username: userInfo.realName || userInfo.username, userType: userInfo.userType })
      setToken(token)
      setUserInfo(userInfo)
      
      message.success('登录成功')
      navigate('/')
    } catch (error) {
      if (error instanceof Error) {
        message.error(error.message)
      } else {
        message.error('登录失败，请稍后重试')
      }
    }
  }

  return (
    <div className="login-container">
      <Card className="login-card" title="智慧物业 - 业主端">
        <Form 
          form={form} 
          onFinish={handleSubmit}
          size="large"
        >
          <Form.Item 
            name="phone" 
            rules={[
              { required: true, message: '请输入手机号' },
              { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
            ]}
          >
            <Input prefix={<UserOutlined />} placeholder="请输入手机号" />
          </Form.Item>
          <Form.Item 
            name="password" 
            rules={[
              { required: true, message: '请输入密码' },
              { min: 6, message: '密码长度不能少于6位' }
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="请输入密码" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block>登录</Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}

export default Login
