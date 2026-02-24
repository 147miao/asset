import { useState, useEffect } from 'react'
import { Card, Table, Tag, Button, Modal, Radio, message } from 'antd'
import { PayCircleOutlined } from '@ant-design/icons'
import { useUserStore } from '../../stores/user'
import { getFeesByUser, payFee } from '../../api'
import { logger } from '../../utils/request'

function Fee() {
  const { userInfo } = useUserStore()
  const [fees, setFees] = useState([])
  const [loading, setLoading] = useState(false)
  const [payModalVisible, setPayModalVisible] = useState(false)
  const [currentFee, setCurrentFee] = useState(null)
  const [payMethod, setPayMethod] = useState('wechat')

  useEffect(() => {
    if (userInfo?.id) {
      loadData()
    }
  }, [userInfo?.id])

  const loadData = async () => {
    if (!userInfo?.id) {
      logger.warn('用户信息不存在，无法加载费用数据')
      return
    }

    setLoading(true)
    logger.info('开始加载费用信息', { userId: userInfo.id })

    try {
      const res = await getFeesByUser(userInfo.id)
      setFees(res.data || [])
      logger.info('费用信息加载成功', { count: res.data?.length || 0 })
    } catch (error) {
      logger.error('加载费用信息失败:', error)
      message.error('加载费用信息失败，请稍后重试')
    } finally {
      setLoading(false)
    }
  }

  const handlePay = (fee) => {
    setCurrentFee(fee)
    setPayModalVisible(true)
  }

  const handleConfirmPay = async () => {
    try {
      logger.info('开始缴费', { feeId: currentFee.id, payMethod })
      await payFee(currentFee.id, payMethod)
      message.success('缴费成功')
      logger.info('缴费成功', { feeId: currentFee.id })
      setPayModalVisible(false)
      loadData()
    } catch (error) {
      logger.error('缴费失败', error)
      message.error('缴费失败')
    }
  }

  const columns = [
    {
      title: '费用名称',
      dataIndex: 'feeName',
      key: 'feeName',
      render: (text) => text || '-',
      width: 180
    },
    {
      title: '费用类型',
      dataIndex: 'feeType',
      key: 'feeType',
      render: (type) => {
        const typeMap = {
          property: '物业费',
          water: '水费',
          electricity: '电费',
          parking: '停车费'
        }
        return typeMap[type] || type || '-'
      },
      width: 100
    },
    {
      title: '金额',
      dataIndex: 'amount',
      key: 'amount',
      render: (amount) => amount !== undefined ? `¥${amount}` : '-',
      width: 120
    },
    {
      title: '计费周期',
      dataIndex: 'billingPeriod',
      key: 'billingPeriod',
      render: (text) => text || '-',
      width: 140
    },
    {
      title: '截止日期',
      dataIndex: 'dueDate',
      key: 'dueDate',
      render: (text) => text || '-',
      width: 140
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const colorMap = {
          unpaid: 'red',
          paid: 'green',
          overdue: 'orange'
        }
        const textMap = {
          unpaid: '未支付',
          paid: '已支付',
          overdue: '已逾期'
        }
        return <Tag color={colorMap[status]}>{textMap[status] || status || '-'}</Tag>
      },
      width: 100
    },
    {
      title: '操作',
      key: 'action',
      width: 140,
      fixed: 'right',
      render: (_, record) => (
        record.status === 'unpaid' || record.status === 'overdue' ? (
          <Button type="primary" size="small" icon={<PayCircleOutlined />} onClick={() => handlePay(record)}>
            立即缴费
          </Button>
        ) : (
          <Button type="default" size="small" disabled>
            已支付
          </Button>
        )
      )
    }
  ]

  return (
    <div style={{ padding: '24px', background: '#f5f7fa', minHeight: 'calc(100vh - 64px)' }}>
      <div style={{ marginBottom: '24px' }}>
        <h2 style={{ fontSize: '20px', fontWeight: 600, margin: 0, color: '#1f2937' }}>
          费用缴纳
        </h2>
        <p style={{ fontSize: '14px', color: '#6b7280', marginTop: '4px', margin: 0 }}>
          查看并缴纳您的各项物业费用
        </p>
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
          dataSource={fees}
          rowKey="id"
          loading={loading}
          rowStyle={{ height: '60px' }}
          scroll={{ x: 920 }}
          pagination={{
            pageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `共 ${total} 条记录`
          }}
        />
      </Card>

      <Modal
        title="在线缴费"
        open={payModalVisible}
        onOk={handleConfirmPay}
        onCancel={() => setPayModalVisible(false)}
        okText="确认支付"
        cancelText="取消"
      >
        {currentFee && (
          <div>
            <p><strong>费用名称：</strong>{currentFee.feeName}</p>
            <p><strong>费用金额：</strong>¥{currentFee.amount}</p>
            <p><strong>支付方式：</strong></p>
            <Radio.Group value={payMethod} onChange={(e) => setPayMethod(e.target.value)}>
              <Radio value="wechat">微信支付</Radio>
              <Radio value="alipay">支付宝</Radio>
              <Radio value="bank">银行卡</Radio>
            </Radio.Group>
          </div>
        )}
      </Modal>
    </div>
  )
}

export default Fee
