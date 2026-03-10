import { useState, useEffect } from 'react'
import { Card, Table, Tag, Button, message, Modal, Form, Input, Rate, Select } from 'antd'
import { useUserStore } from '../../stores/user'
import { getRepairsByUser, createRepair, cancelRepair, rateRepair } from '../../api'
import type { Repair } from '../../types'

function Repair() {
  const userInfo = useUserStore((state) => state.userInfo)
  const [repairs, setRepairs] = useState<Repair[]>([])
  const [loading, setLoading] = useState(true)
  const [createModalVisible, setCreateModalVisible] = useState(false)
  const [rateModalVisible, setRateModalVisible] = useState(false)
  const [selectedRepair, setSelectedRepair] = useState<Repair | null>(null)
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
      const res = await getRepairsByUser(userInfo.id)
      setRepairs(res.data || [])
    } catch {
      message.error('加载报修数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handleCreate = async (values: { title: string; description: string; repairType: string }) => {
    if (!userInfo?.id) {
      message.error('用户未登录')
      return
    }
    try {
      await createRepair({
        title: values.title,
        description: values.description,
        repairType: values.repairType,
        userId: Number(userInfo.id)
      })
      message.success('提交成功')
      setCreateModalVisible(false)
      form.resetFields()
      loadData()
    } catch {
      message.error('提交失败')
    }
  }

  const handleCancel = async (id: string) => {
    try {
      await cancelRepair(id)
      message.success('取消成功')
      loadData()
    } catch {
      message.error('取消失败')
    }
  }

  const handleRate = async (values: { rating: number; feedback: string }) => {
    if (!selectedRepair) return
    try {
      await rateRepair(selectedRepair.id, values.rating, values.feedback)
      message.success('评价成功')
      setRateModalVisible(false)
      setSelectedRepair(null)
      rateForm.resetFields()
      loadData()
    } catch {
      message.error('评价失败')
    }
  }

  const columns = [
    { title: '标题', dataIndex: 'title', key: 'title' },
    { title: '描述', dataIndex: 'description', key: 'description', ellipsis: true },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => {
        const colorMap: Record<string, 'orange' | 'blue' | 'green' | 'red'> = {
          pending: 'orange',
          processing: 'blue',
          completed: 'green',
          cancelled: 'red'
        }
        const textMap: Record<string, string> = {
          pending: '待处理',
          processing: '处理中',
          completed: '已完成',
          cancelled: '已取消'
        }
        return <Tag color={colorMap[status] || 'default'}>{textMap[status] || status}</Tag>
      }
    },
    { title: '创建时间', dataIndex: 'createTime', key: 'createTime' },
    {
      title: '操作',
      key: 'action',
      render: (_: unknown, record: Repair) => (
        <div className="flex gap-2">
          {record.status === 'pending' && (
            <Button size="small" danger onClick={() => handleCancel(record.id)}>取消</Button>
          )}
          {record.status === 'completed' && (
            <Button size="small" type="primary" ghost onClick={() => {
              setSelectedRepair(record)
              setRateModalVisible(true)
            }}>评价</Button>
          )}
        </div>
      )
    }
  ]

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-text-primary">报修服务</h1>
        <Button type="primary" onClick={() => setCreateModalVisible(true)}>提交报修</Button>
      </div>
      <Card>
        <Table
          columns={columns}
          dataSource={repairs}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      </Card>

      <Modal
        title="提交报修"
        open={createModalVisible}
        onCancel={() => setCreateModalVisible(false)}
        footer={null}
      >
        <Form form={form} layout="vertical" onFinish={handleCreate}>
          <Form.Item name="repairType" label="报修类型" rules={[{ required: true, message: '请选择报修类型' }]}>
            <Select placeholder="请选择报修类型">
              <Select.Option value="plumbing">水管</Select.Option>
              <Select.Option value="electrical">电路</Select.Option>
              <Select.Option value="appliance">家电</Select.Option>
              <Select.Option value="other">其他</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="title" label="标题" rules={[{ required: true, message: '请输入标题' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="描述" rules={[{ required: true, message: '请输入描述' }]}>
            <Input.TextArea rows={4} />
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

export default Repair
