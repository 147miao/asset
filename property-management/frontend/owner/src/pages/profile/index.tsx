import { useState, useEffect } from 'react'
import { Card, Form, Input, Button, message, Avatar } from 'antd'
import { UserOutlined } from '@ant-design/icons'
import { useUserStore } from '../../stores/user'
import { updateUserInfo, updatePassword } from '../../api'

function Profile() {
  const userInfo = useUserStore((state) => state.userInfo)
  const setUserInfo = useUserStore((state) => state.setUserInfo)
  const [form] = Form.useForm()
  const [passwordForm] = Form.useForm()
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (userInfo) {
      form.setFieldsValue({
        username: userInfo.username,
        realName: userInfo.realName,
        phone: userInfo.phone
      })
    }
  }, [userInfo, form])

  const handleUpdate = async (values: { username: string; realName: string; phone: string }) => {
    setLoading(true)
    try {
      await updateUserInfo({ ...values, id: userInfo?.id })
      setUserInfo({ ...userInfo, ...values } as never)
      message.success('更新成功')
    } catch {
      message.error('更新失败')
    } finally {
      setLoading(false)
    }
  }

  const handlePasswordChange = async (values: { oldPassword: string; newPassword: string }) => {
    setLoading(true)
    try {
      await updatePassword({ ...values, userId: userInfo?.id })
      message.success('密码修改成功')
      passwordForm.resetFields()
    } catch {
      message.error('密码修改失败')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="p-6">
      <h1 className="text-2xl font-semibold text-text-primary mb-6">个人中心</h1>
      
      <div className="max-w-2xl">
        <Card title="基本信息" className="mb-6">
          <div className="flex items-center gap-6 mb-6">
            <Avatar size={80} icon={<UserOutlined />} />
            <div>
              <p className="text-lg font-semibold">{userInfo?.realName || userInfo?.username}</p>
              <p className="text-text-secondary">{userInfo?.phone}</p>
            </div>
          </div>
          
          <Form form={form} layout="vertical" onFinish={handleUpdate}>
            <Form.Item name="username" label="用户名">
              <Input disabled />
            </Form.Item>
            <Form.Item name="realName" label="姓名" rules={[{ required: true, message: '请输入姓名' }]}>
              <Input />
            </Form.Item>
            <Form.Item name="phone" label="手机号" rules={[{ required: true, message: '请输入手机号' }]}>
              <Input />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit" loading={loading}>保存修改</Button>
            </Form.Item>
          </Form>
        </Card>

        <Card title="修改密码">
          <Form form={passwordForm} layout="vertical" onFinish={handlePasswordChange}>
            <Form.Item name="oldPassword" label="旧密码" rules={[{ required: true, message: '请输入旧密码' }]}>
              <Input.Password />
            </Form.Item>
            <Form.Item name="newPassword" label="新密码" rules={[{ required: true, message: '请输入新密码' }, { min: 6, message: '密码至少6位' }]}>
              <Input.Password />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit" loading={loading}>修改密码</Button>
            </Form.Item>
          </Form>
        </Card>
      </div>
    </div>
  )
}

export default Profile
