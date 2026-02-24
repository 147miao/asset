import { useState, useEffect } from 'react'
import { Card, Table, Tag, Button, Modal, Form, Input, Select, Rate, message } from 'antd'
import { PlusOutlined, StarOutlined } from '@ant-design/icons'
import { useUserStore } from '../../stores/user'
import { getServicesByUser, createService, cancelService, rateService } from '../../api'
import { logger } from '../../utils/request'

function Service() {
  const { userInfo } = useUserStore()
  const [services, setServices] = useState([])
  const [loading, setLoading] = useState(false)
  const [addModalVisible, setAddModalVisible] = useState(false)
  const [rateModalVisible, setRateModalVisible] = useState(false)
  const [currentService, setCurrentService] = useState(null)
  const [form] = Form.useForm()
  const [rateForm] = Form.useForm()

  useEffect(() => {
    if (userInfo?.id) {
      loadData()
    }
  }, [userInfo?.id])

  const loadData = async () => {
    if (!userInfo?.id) {
      logger.warn('用户信息不存在，无法加载服务数据')
      return
    }

    setLoading(true)
    logger.info('开始加载服务记录', { userId: userInfo.id })

    try {
      const res = await getServicesByUser(userInfo.id)
      setServices(res.data || [])
      logger.info('服务记录加载成功', { count: res.data?.length || 0 })
    } catch (error) {
      logger.error('加载服务记录失败:', error)
      message.error('加载服务记录失败，请稍后重试')
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = () => {
    form.resetFields()
    setAddModalVisible(true)
  }

  const handleSubmitAdd = async () => {
    try {
      const values = await form.validateFields()
      const data = { ...values, userId: userInfo.id, userName: userInfo.realName }
      logger.info('预约服务', data)
      await createService(data)
      message.success('服务预约成功')
      logger.info('服务预约成功')
      setAddModalVisible(false)
      loadData()
    } catch (error) {
      logger.error('预约服务失败', error)
      message.error('预约失败')
    }
  }

  const handleCancel = async (record) => {
    try {
      logger.info('取消服务预约', { serviceId: record.id })
      await cancelService(record.id)
      message.success('已取消预约')
      logger.info('取消服务预约成功')
      loadData()
    } catch (error) {
      logger.error('取消服务预约失败', error)
      message.error('取消失败')
    }
  }

  const handleRate = (record) => {
    setCurrentService(record)
    rateForm.resetFields()
    setRateModalVisible(true)
  }

  const handleSubmitRate = async () => {
    try {
      const values = await rateForm.validateFields()
      logger.info('评价服务', { serviceId: currentService.id, ...values })
      await rateService(currentService.id, values.rating, values.feedback)
      message.success('评价成功')
      logger.info('评价成功')
      setRateModalVisible(false)
      loadData()
    } catch (error) {
      logger.error('评价失败', error)
      message.error('评价失败')
    }
  }

  const getStatusColor = (status) => {
    const colorMap = {
      pending: 'orange',
      confirmed: 'blue',
      in_progress: 'processing',
      completed: 'green',
      cancelled: 'red'
    }
    return colorMap[status] || 'default'
  }

  const getStatusText = (status) => {
    const textMap = {
      pending: '待确认',
      confirmed: '已确认',
      in_progress: '进行中',
      completed: '已完成',
      cancelled: '已取消'
    }
    return textMap[status] || status
  }

  const columns = [
    {
      title: '服务类型',
      dataIndex: 'serviceType',
      key: 'serviceType',
      render: (type) => {
        const typeMap = {
          cleaning: '保洁服务',
          security: '安保服务',
          venue: '场地租赁',
          other: '其他'
        }
        return typeMap[type] || type || '-'
      },
      width: 140
    },
    {
      title: '预约时间',
      dataIndex: 'appointmentTime',
      key: 'appointmentTime',
      render: (text) => text || '-',
      width: 180
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => <Tag color={getStatusColor(status)}>{getStatusText(status) || '-'}</Tag>,
      width: 100
    },
    {
      title: '提交时间',
      dataIndex: 'createTime',
      key: 'createTime',
      render: (text) => text || '-',
      width: 180
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      fixed: 'right',
      render: (_, record) => (
        <div>
          {record.status === 'pending' && (
            <Button type="primary" danger size="small" onClick={() => handleCancel(record)}>
              取消预约
            </Button>
          )}
          {record.status === 'completed' && !record.rating && (
            <Button type="primary" size="small" icon={<StarOutlined />} onClick={() => handleRate(record)}>
              评价
            </Button>
          )}
          {record.rating && (
            <Tag color="gold">已评价 {record.rating}星</Tag>
          )}
        </div>
      )
    }
  ]

  return (
    <div style={{ padding: '24px', background: '#f5f7fa', minHeight: 'calc(100vh - 64px)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <div>
          <h2 style={{ fontSize: '20px', fontWeight: 600, margin: 0, color: '#1f2937' }}>
            增值服务
          </h2>
          <p style={{ fontSize: '14px', color: '#6b7280', marginTop: '4px', margin: 0 }}>
            预约和管理您的增值服务
          </p>
        </div>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          预约服务
        </Button>
      </div>

      <Card 
        style={{ 
          borderRadius: '8px', 
          boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
          border: 'none'
        }}
      >
        <Table
          columns={columns}
          dataSource={services}
          rowKey="id"
          loading={loading}
          rowStyle={{ height: '60px' }}
          scroll={{ x: 800 }}
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `共 ${total} 条记录`
          }}
        />
      </Card>

      <Modal
        title="预约服务"
        open={addModalVisible}
        onOk={handleSubmitAdd}
        onCancel={() => setAddModalVisible(false)}
        okText="提交预约"
        cancelText="取消"
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="serviceType"
            label="服务类型"
            rules={[{ required: true, message: '请选择服务类型' }]}
          >
            <Select placeholder="请选择服务类型">
              <Select.Option value="cleaning">保洁服务</Select.Option>
              <Select.Option value="security">安保服务</Select.Option>
              <Select.Option value="venue">场地租赁</Select.Option>
              <Select.Option value="other">其他</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="appointmentTime"
            label="预约时间"
            rules={[{ required: true, message: '请选择预约时间' }]}
          >
            <Input type="datetime-local" />
          </Form.Item>
          <Form.Item
            name="description"
            label="服务需求"
            rules={[{ required: true, message: '请输入服务需求' }]}
          >
            <Input.TextArea rows={4} placeholder="请详细描述服务需求" />
          </Form.Item>
          <Form.Item
            name="contactPhone"
            label="联系电话"
            rules={[{ required: true, message: '请输入联系电话' }]}
          >
            <Input placeholder="请输入联系电话" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="评价服务"
        open={rateModalVisible}
        onOk={handleSubmitRate}
        onCancel={() => setRateModalVisible(false)}
        okText="提交评价"
        cancelText="取消"
      >
        <Form form={rateForm} layout="vertical">
          <Form.Item
            name="rating"
            label="评分"
            rules={[{ required: true, message: '请选择评分' }]}
          >
            <Rate />
          </Form.Item>
          <Form.Item
            name="feedback"
            label="评价内容"
          >
            <Input.TextArea rows={4} placeholder="请输入评价内容" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default Service
