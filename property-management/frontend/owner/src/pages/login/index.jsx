import { Form, Input, Button, Card, message } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { login } from '../../api'
import { useUserStore } from '../../stores/user'
import { logger } from '../../utils/request'
import { encodeToken } from '../../utils/base64'
import './login.css'

function Login() {
  const navigate = useNavigate()
  const { setToken, setUserInfo } = useUserStore()
  const [form] = Form.useForm()

  const handleSubmit = async (values) => {
    logger.info('开始登录请求', { phone: values.phone })
    
    try {
      const res = await login(values)
      
      if (!res.data) {
        logger.error('登录响应数据为空')
        message.error('登录失败：服务器响应异常')
        return
      }
      
      const token = encodeToken({ userId: res.data.id, username: res.data.realName })
      setToken(token)
      setUserInfo(res.data)
      
      logger.info('登录成功', { userId: res.data.id, userName: res.data.realName })
      message.success('登录成功')
      navigate('/')
    } catch (error) {
      logger.error('登录失败', error)
      
      if (error.message) {
        message.error(error.message)
      } else {
        message.error('登录失败，请稍后重试')
      }
    }
  }

  const handleFinishFailed = (errorInfo) => {
    logger.warn('登录表单验证失败', errorInfo)
  }

  return (
    <div className="login-container">
      <Card className="login-card" title="智慧物业 - 业主端">
        <Form 
          form={form} 
          onFinish={handleSubmit}
          onFinishFailed={handleFinishFailed}
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
