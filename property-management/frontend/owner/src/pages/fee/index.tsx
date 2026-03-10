import { useState, useEffect } from 'react'
import { Card, Table, Tag, Button, message, Modal } from 'antd'
import { useUserStore } from '../../stores/user'
import { getFeesByUser, payFee } from '../../api'
import type { Fee } from '../../types'

function Fee() {
  const userInfo = useUserStore((state) => state.userInfo)
  const [fees, setFees] = useState<Fee[]>([])
  const [loading, setLoading] = useState(true)
  const [payModalVisible, setPayModalVisible] = useState(false)
  const [selectedFee, setSelectedFee] = useState<Fee | null>(null)

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
      const res = await getFeesByUser(userInfo.id)
      setFees(res.data || [])
    } catch {
      message.error('加载费用数据失败')
    } finally {
      setLoading(false)
    }
  }

  const handlePay = async () => {
    if (!selectedFee) return
    try {
      await payFee(selectedFee.id, 'wechat')
      message.success('缴费成功')
      setPayModalVisible(false)
      loadData()
    } catch {
      message.error('缴费失败')
    }
  }

  const columns = [
    { title: '费用类型', dataIndex: 'type', key: 'type' },
    { title: '金额(¥)', dataIndex: 'amount', key: 'amount', render: (amount: number) => `¥${amount}` },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => {
        const colorMap: Record<string, 'orange' | 'green' | 'red'> = {
          pending: 'orange',
          paid: 'green',
          overdue: 'red'
        }
        const textMap: Record<string, string> = {
          pending: '待支付',
          paid: '已支付',
          overdue: '已逾期'
        }
        return <Tag color={colorMap[status] || 'default'}>{textMap[status] || status}</Tag>
      }
    },
    { title: '截止日期', dataIndex: 'dueDate', key: 'dueDate' },
    {
      title: '操作',
      key: 'action',
      render: (_: unknown, record: Fee) => (
        record.status === 'pending' && (
          <Button type="primary" size="small" onClick={() => {
            setSelectedFee(record)
            setPayModalVisible(true)
          }}>
            立即缴费
          </Button>
        )
      )
    }
  ]

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-text-primary">费用缴纳</h1>
      </div>
      <Card>
        <Table
          columns={columns}
          dataSource={fees}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      </Card>
      <Modal
        title="确认缴费"
        open={payModalVisible}
        onOk={handlePay}
        onCancel={() => setPayModalVisible(false)}
      >
        <p>确认缴纳费用 <strong>¥{selectedFee?.amount}</strong> 吗？</p>
      </Modal>
    </div>
  )
}

export default Fee
