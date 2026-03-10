import { useState, useEffect } from 'react'
import { Card, Table, Button, message, Modal, Form, Input, InputNumber, Rate, Select } from 'antd'
import { useUserStore } from '../../stores/user'
import { getServicesByUser, createService, cancelService, rateService } from '../../api'
import type { Service } from '../../types'

function Service() {
  const userInfo = useUserStore((state) => state.userInfo)
  const [services, setServices] = useState<Service[]>([])
  const [loading, setLoading] = useState(true)
  const [createModalVisible, setCreateModalVisible] = useState(false)
  const [rateModalVisible, setRateModalVisible] = useState(false)
  const [selectedService, setSelectedService] = useState<Service | null>(null)
  const [form] = Form.useForm()
  const [rateForm] = Form.useForm()

  useEffect(() => {
    if (userInfo?.id) {
      loadData()
    } else {
      setLoading(false)
    }
  }, [userInfo?.id])

  const loadData = async () => {
    if (!userInfo?.id) return
    setLoading(true)
    try {
      const res = await getServicesByUser(userInfo.id)
      setServices(res.data || [])
    } catch {
      message.error('加载服务数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleCreate = async (values: { name: string; description: string; price: number; serviceType: string }) => {
    if (!userInfo?.id) {
      message.error('用户未登录')
      return
    }
    try {
      await createService({
        serviceName: values.name,
        description: values.description,
        fee: values.price,
        serviceType: values.serviceType,
        userId: Number(userInfo.id)
      })
      message.success('申请成功')
      setCreateModalVisible(false)
      form.resetFields()
      loadData()
    } catch {
      message.error('申请失败')
    }
  }

  const handleCancel = async (id: string) => {
    try {
      await cancelService(id)
      message.success('取消成功')
      loadData()
    } catch {
      message.error('取消失败')
    }
  }

  const handleRate = async (values: { rating: number; feedback: string }) => {
    if (!selectedService) return
    try {
      await rateService(selectedService.id, values.rating, values.feedback)
      message.success('评价成功')
      setRateModalVisible(false)
      setSelectedService(null)
      rateForm.resetFields()
      loadData()
    } catch {
      message.error('评价失败')
    }
  }

  const columns = [
    { title: '服务名称', dataIndex: 'serviceName', key: 'serviceName' },
    { title: '描述', dataIndex: 'description', key: 'description', ellipsis: true },
    { title: '价格(¥)', dataIndex: 'fee', key: 'fee', render: (fee: number) => `¥${fee}` },
    {
      title: '操作',
      key: 'action',
      render: (_: unknown, record: Service) => (
        <div className="flex gap-2">
          <Button size="small" danger onClick={() => handleCancel(record.id)}>取消</Button>
          <Button size="small" type="primary" ghost onClick={() => {
            setSelectedService(record)
            setRateModalVisible(true)
          }}>评价</Button>
        </div>
      )
    }
  ]

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-text-primary">增值服务</h1>
        <Button type="primary" onClick={() => setCreateModalVisible(true)}>申请服务</Button>
      </div>
      <Card>
        <Table
          columns={columns}
          dataSource={services}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      </Card>

      <Modal
        title="申请服务"
        open={createModalVisible}
        onCancel={() => setCreateModalVisible(false)}
        footer={null}
      >
        <Form form={form} layout="vertical" onFinish={handleCreate}>
          <Form.Item name="serviceType" label="服务类型" rules={[{ required: true, message: '请选择服务类型' }]}>
            <Select placeholder="请选择服务类型">
              <Select.Option value="cleaning">保洁服务</Select.Option>
              <Select.Option value="security">安保服务</Select.Option>
              <Select.Option value="venue">场地租赁</Select.Option>
              <Select.Option value="other">其他</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="name" label="服务名称" rules={[{ required: true, message: '请输入服务名称' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="描述" rules={[{ required: true, message: '请输入描述' }]}>
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item name="price" label="价格" rules={[{ required: true, message: '请输入价格' }]}>
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block>提交</Button>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="评价服务"
        open={rateModalVisible}
        onCancel={() => setRateModalVisible(false)}
        footer={null}
      >
        <Form form={rateForm} layout="vertical" onFinish={handleRate}>
          <Form.Item name="rating" label="评分" rules={[{ required: true, message: '请评分' }]}>
            <Rate />
          </Form.Item>
          <Form.Item name="feedback" label="反馈">
            <Input.TextArea rows={3} />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block>提交</Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default Service
