import { useState, useEffect } from 'react'
import { Card, Form, Input, Button, Modal, message, Avatar, Divider } from 'antd'
import { UserOutlined, LockOutlined, PhoneOutlined, MailOutlined } from '@ant-design/icons'
import { useUserStore } from '../../stores/user'
import { updateUserInfo, updatePassword } from '../../api'
import { logger } from '../../utils/request'

function Profile() {
  const { userInfo, setUserInfo } = useUserStore()
  const [form] = Form.useForm()
  const [pwdForm] = Form.useForm()
  const [pwdModalVisible, setPwdModalVisible] = useState(false)

  useEffect(() => {
    if (userInfo) {
      form.setFieldsValue({
        realName: userInfo.realName,
        phone: userInfo.phone,
        email: userInfo.email,
        idCard: userInfo.idCard,
        address: userInfo.address
      })
    }
  }, [userInfo])

  const handleUpdate = async () => {
    try {
      const values = await form.validateFields()
      logger.info('更新用户信息', { userId: userInfo.id, ...values })
      await updateUserInfo(values)
      message.success('更新成功')
      setUserInfo({ ...userInfo, ...values })
      logger.info('用户信息更新成功')
    } catch (error) {
      logger.error('更新用户信息失败', error)
      message.error('更新失败')
    }
  }

  const handleChangePassword = async () => {
    try {
      const values = await pwdForm.validateFields()
      if (values.newPassword !== values.confirmPassword) {
        message.error('两次输入的密码不一致')
        return
      }
      logger.info('修改密码', { userId: userInfo.id })
      await updatePassword({
        oldPassword: values.oldPassword,
        newPassword: values.newPassword
      })
      message.success('密码修改成功')
      logger.info('密码修改成功')
      setPwdModalVisible(false)
      pwdForm.resetFields()
    } catch (error) {
      logger.error('密码修改失败', error)
      message.error('密码修改失败')
    }
  }

  return (
    <div style={{ padding: '24px', background: '#f5f7fa', minHeight: 'calc(100vh - 64px)' }}>
      <div style={{ marginBottom: '24px' }}>
        <h2 style={{ fontSize: '20px', fontWeight: 600, margin: 0, color: '#1f2937' }}>
          个人中心
        </h2>
        <p style={{ fontSize: '14px', color: '#6b7280', marginTop: '4px', margin: 0 }}>
          管理您的个人信息和账户设置
        </p>
      </div>
      
      <Card 
        style={{ 
          borderRadius: '8px', 
          boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
          border: 'none'
        }}
      >
        <div style={{ textAlign: 'center', marginBottom: '30px' }}>
          <Avatar size={100} icon={<UserOutlined />} style={{ background: '#1890ff' }} />
          <h3 style={{ marginTop: '16px', fontSize: '20px', fontWeight: 600, color: '#1f2937' }}>
            {userInfo?.realName}
          </h3>
          <p style={{ color: '#6b7280', marginTop: '8px', fontSize: '14px' }}>
            {userInfo?.userType === 'owner' ? '业主' : 
             userInfo?.userType === 'tenant' ? '租户' : '员工'}
          </p>
        </div>

        <Divider style={{ margin: '24px 0' }} />

        <Form
          form={form}
          layout="vertical"
          style={{ maxWidth: '500px', margin: '0 auto' }}
        >
          <Form.Item
            name="realName"
            label="姓名"
            rules={[{ required: true, message: '请输入姓名' }]}
          >
            <Input prefix={<UserOutlined />} placeholder="请输入姓名" size="large" />
          </Form.Item>
          <Form.Item
            name="phone"
            label="手机号"
            rules={[
              { required: true, message: '请输入手机号' },
              { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
            ]}
          >
            <Input prefix={<PhoneOutlined />} placeholder="请输入手机号" size="large" />
          </Form.Item>
          <Form.Item
            name="email"
            label="邮箱"
            rules={[
              { type: 'email', message: '请输入正确的邮箱地址' }
            ]}
          >
            <Input prefix={<MailOutlined />} placeholder="请输入邮箱" size="large" />
          </Form.Item>
          <Form.Item
            name="idCard"
            label="身份证号"
          >
            <Input placeholder="请输入身份证号" size="large" />
          </Form.Item>
          <Form.Item
            name="address"
            label="地址"
          >
            <Input placeholder="请输入地址" size="large" />
          </Form.Item>
          <Form.Item style={{ marginTop: '24px' }}>
            <Button type="primary" onClick={handleUpdate} block size="large">
              更新信息
            </Button>
          </Form.Item>
        </Form>

        <Divider style={{ margin: '24px 0' }} />

        <div style={{ textAlign: 'center' }}>
          <Button type="default" icon={<LockOutlined />} onClick={() => setPwdModalVisible(true)} size="large">
            修改密码
          </Button>
        </div>
      </Card>

      <Modal
        title="修改密码"
        open={pwdModalVisible}
        onOk={handleChangePassword}
        onCancel={() => setPwdModalVisible(false)}
        okText="确认修改"
        cancelText="取消"
      >
        <Form form={pwdForm} layout="vertical">
          <Form.Item
            name="oldPassword"
            label="原密码"
            rules={[{ required: true, message: '请输入原密码' }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="请输入原密码" />
          </Form.Item>
          <Form.Item
            name="newPassword"
            label="新密码"
            rules={[
              { required: true, message: '请输入新密码' },
              { min: 6, message: '密码至少6位' }
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="请输入新密码" />
          </Form.Item>
          <Form.Item
            name="confirmPassword"
            label="确认密码"
            rules={[
              { required: true, message: '请再次输入新密码' }
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="请再次输入新密码" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default Profile
