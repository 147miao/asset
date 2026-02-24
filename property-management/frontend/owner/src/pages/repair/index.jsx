import { useState, useEffect } from 'react'
import { Card, Table, Tag, Button, Modal, Form, Input, Select, Rate, message } from 'antd'
import { ToolOutlined, PlusOutlined, StarOutlined } from '@ant-design/icons'
import { useUserStore } from '../../stores/user'
import { getRepairsByUser, createRepair, cancelRepair, rateRepair } from '../../api'
import { logger } from '../../utils/request'

function Repair() {
  const { userInfo } = useUserStore()
  const [repairs, setRepairs] = useState([])
  const [loading, setLoading] = useState(false)
  const [addModalVisible, setAddModalVisible] = useState(false)
  const [rateModalVisible, setRateModalVisible] = useState(false)
  const [currentRepair, setCurrentRepair] = useState(null)
  const [form] = Form.useForm()
  const [rateForm] = Form.useForm()

  useEffect(() => {
    if (userInfo?.id) {
      loadData()
    }
  }, [userInfo?.id])

  const loadData = async () => {
    if (!userInfo?.id) {
      logger.warn('用户信息不存在，无法加载报修数据')
      return
    }

    setLoading(true)
    logger.info('开始加载报修记录', { userId: userInfo.id })

    try {
      const res = await getRepairsByUser(userInfo.id)
      setRepairs(res.data || [])
      logger.info('报修记录加载成功', { count: res.data?.length || 0 })
    } catch (error) {
      logger.error('加载报修记录失败:', error)
      message.error('加载报修记录失败，请稍后重试')
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
      logger.info('提交报修申请', data)
      await createRepair(data)
      message.success('报修申请提交成功')
      logger.info('报修申请提交成功')
      setAddModalVisible(false)
      loadData()
    } catch (error) {
      logger.error('提交报修失败', error)
      message.error('提交失败')
    }
  }

  const handleCancel = async (record) => {
    try {
      logger.info('取消报修', { repairId: record.id })
      await cancelRepair(record.id)
      message.success('已取消报修')
      logger.info('取消报修成功')
      loadData()
    } catch (error) {
      logger.error('取消报修失败', error)
      message.error('取消失败')
    }
  }

  const handleRate = (record) => {
    setCurrentRepair(record)
    rateForm.resetFields()
    setRateModalVisible(true)
  }

  const handleSubmitRate = async () => {
    try {
      const values = await rateForm.validateFields()
      logger.info('评价报修服务', { repairId: currentRepair.id, ...values })
      await rateRepair(currentRepair.id, values.rating, values.feedback)
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
      processing: 'blue',
      completed: 'green',
      cancelled: 'red'
    }
    return colorMap[status] || 'default'
  }

  const getStatusText = (status) => {
    const textMap = {
      pending: '待处理',
      processing: '处理中',
      completed: '已完成',
      cancelled: '已取消'
    }
    return textMap[status] || status
  }

  const columns = [
    {
      title: '报修标题',
      dataIndex: 'title',
      key: 'title',
      render: (text) => text || '-',
      width: 200
    },
    {
      title: '报修类型',
      dataIndex: 'repairType',
      key: 'repairType',
      render: (type) => {
        const typeMap = {
          water: '水管',
          electricity: '电路',
          appliance: '家电',
          other: '其他'
        }
        return typeMap[type] || type || '-'
      },
      width: 100
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
              取消报修
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
            报修服务
          </h2>
          <p style={{ fontSize: '14px', color: '#6b7280', marginTop: '4px', margin: 0 }}>
            提交和管理您的报修申请
          </p>
        </div>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          申请报修
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
          dataSource={repairs}
          rowKey="id"
          loading={loading}
          rowStyle={{ height: '60px' }}
          scroll={{ x: 780 }}
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `共 ${total} 条记录`
          }}
        />
      </Card>

      <Modal
        title="申请报修"
        open={addModalVisible}
        onOk={handleSubmitAdd}
        onCancel={() => setAddModalVisible(false)}
        okText="提交"
        cancelText="取消"
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="title"
            label="报修标题"
            rules={[{ required: true, message: '请输入报修标题' }]}
          >
            <Input placeholder="请输入报修标题" />
          </Form.Item>
          <Form.Item
            name="repairType"
            label="报修类型"
            rules={[{ required: true, message: '请选择报修类型' }]}
          >
            <Select placeholder="请选择报修类型">
              <Select.Option value="water">水管</Select.Option>
              <Select.Option value="electricity">电路</Select.Option>
              <Select.Option value="appliance">家电</Select.Option>
              <Select.Option value="other">其他</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item
            name="description"
            label="详细描述"
            rules={[{ required: true, message: '请输入详细描述' }]}
          >
            <Input.TextArea rows={4} placeholder="请详细描述报修内容" />
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

export default Repair
