import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, Row, Col, Statistic, List, Badge, Button, message } from 'antd'
import { HomeOutlined, PayCircleOutlined, ToolOutlined, MessageOutlined, ArrowRightOutlined } from '@ant-design/icons'
import { useUserStore } from '../../stores/user'
import { getHousesByOwner, getUnpaidFees, getRepairsByUser, getUnreadMessages } from '../../api'
import type { House, Fee, Repair } from '../../types'

interface RepairItem extends Repair {
  title: string
  createTime: string
}

interface FeeItem extends Fee {
  feeName: string
  amount: number
  dueDate: string
}

function Home() {
  const navigate = useNavigate()
  const userInfo = useUserStore((state) => state.userInfo)
  const [houses, setHouses] = useState<House[]>([])
  const [unpaidFees, setUnpaidFees] = useState<FeeItem[]>([])
  const [repairs, setRepairs] = useState<RepairItem[]>([])
  const [unreadCount, setUnreadCount] = useState(0)
  const [loading, setLoading] = useState(true)

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
      const [houseRes, feeRes, repairRes, msgRes] = await Promise.all([
        getHousesByOwner(userInfo.id),
        getUnpaidFees(userInfo.id),
        getRepairsByUser(userInfo.id),
        getUnreadMessages(userInfo.id)
      ])

      setHouses(houseRes.data || [])
      setUnpaidFees(feeRes.data || [])
      setRepairs((repairRes.data || []).slice(0, 5) as RepairItem[])
      setUnreadCount(msgRes.data?.length || 0)
    } catch {
      message.error('加载数据失败，请稍后重试')
    } finally {
      setLoading(false)
    }
  }

  const getRepairStatusColor = (status: string) => {
    const colorMap: Record<string, 'warning' | 'processing' | 'success' | 'error' | 'default'> = {
      pending: 'warning',
      processing: 'processing',
      completed: 'success',
      cancelled: 'error'
    }
    return colorMap[status] || 'default'
  }

  const getRepairStatusText = (status: string) => {
    const textMap: Record<string, string> = {
      pending: '待处理',
      processing: '处理中',
      completed: '已完成',
      cancelled: '已取消'
    }
    return textMap[status] || status
  }

  return (
    <div style={{ padding: '24px', background: '#f5f7fa', minHeight: 'calc(100vh - 64px)' }}>
      <div style={{ marginBottom: '24px' }}>
        <h1 style={{ fontSize: '24px', fontWeight: 600, margin: 0, color: '#1f2937' }}>
          欢迎，{userInfo?.realName || '业主'}
        </h1>
        <p style={{ fontSize: '14px', color: '#6b7280', marginTop: '8px', margin: 0 }}>
          今天是个好日子，祝您生活愉快！
        </p>
      </div>
      
      <Row gutter={[16, 16]} style={{ marginBottom: '24px' }}>
        <Col xs={24} sm={12} md={12} lg={6}>
          <Card 
            loading={loading} 
            style={{ borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', border: 'none' }}
            hoverable
          >
            <Statistic
              title={<span style={{ fontSize: '14px', color: '#6b7280' }}>我的房屋</span>}
              value={houses.length}
              prefix={<HomeOutlined style={{ fontSize: '20px' }} />}
              valueStyle={{ color: '#3f8600', fontWeight: 600, fontSize: '28px' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={12} lg={6}>
          <Card 
            loading={loading} 
            style={{ borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', border: 'none' }}
            hoverable
          >
            <Statistic
              title={<span style={{ fontSize: '14px', color: '#6b7280' }}>待缴费</span>}
              value={unpaidFees.length}
              prefix={<PayCircleOutlined style={{ fontSize: '20px' }} />}
              valueStyle={{ color: '#cf1322', fontWeight: 600, fontSize: '28px' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={12} lg={6}>
          <Card 
            loading={loading} 
            style={{ borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', border: 'none' }}
            hoverable
          >
            <Statistic
              title={<span style={{ fontSize: '14px', color: '#6b7280' }}>报修记录</span>}
              value={repairs.length}
              prefix={<ToolOutlined style={{ fontSize: '20px' }} />}
              valueStyle={{ color: '#1890ff', fontWeight: 600, fontSize: '28px' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={12} lg={6}>
          <Card 
            loading={loading} 
            style={{ borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', border: 'none' }}
            hoverable
          >
            <Statistic
              title={<span style={{ fontSize: '14px', color: '#6b7280' }}>未读消息</span>}
              value={unreadCount}
              prefix={<MessageOutlined style={{ fontSize: '20px' }} />}
              valueStyle={{ color: '#1890ff', fontWeight: 600, fontSize: '28px' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]}>
        <Col xs={24} lg={12}>
          <Card 
            title={<span style={{ fontSize: '16px', fontWeight: 600 }}>最近报修</span>} 
            extra={
              <Button type="link" onClick={() => navigate('/repair')}>
                查看全部 <ArrowRightOutlined />
              </Button>
            }
            style={{ borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', border: 'none' }}
            loading={loading}
          >
            <List
              dataSource={repairs}
              itemLayout="horizontal"
              renderItem={(item) => (
                <List.Item style={{ padding: '12px 0', borderBottom: '1px solid #f0f0f0' }}>
                  <List.Item.Meta
                    avatar={<ToolOutlined style={{ fontSize: '24px', color: '#1890ff' }} />}
                    title={<span style={{ fontWeight: 500, color: '#1f2937' }}>{item.title || '-'}</span>}
                    description={
                      <div style={{ marginTop: '4px' }}>
                        <Badge status={getRepairStatusColor(item.status)} text={getRepairStatusText(item.status)} />
                        <span style={{ marginLeft: '12px', color: '#9ca3af', fontSize: '13px' }}>
                          {item.createTime || '-'}
                        </span>
                      </div>
                    }
                  />
                </List.Item>
              )}
            />
            {!loading && repairs.length === 0 && (
              <div style={{ textAlign: 'center', padding: '40px 0', color: '#9ca3af' }}>
                <ToolOutlined style={{ fontSize: '48px', marginBottom: '12px', color: '#d1d5db' }} />
                <p>暂无报修记录</p>
              </div>
            )}
          </Card>
        </Col>

        <Col xs={24} lg={12}>
          <Card 
            title={<span style={{ fontSize: '16px', fontWeight: 600 }}>待缴费</span>}
            extra={
              <Button type="link" onClick={() => navigate('/fee')}>
                查看全部 <ArrowRightOutlined />
              </Button>
            }
            style={{ borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', border: 'none' }}
            loading={loading}
          >
            <List
              dataSource={unpaidFees.slice(0, 5)}
              itemLayout="horizontal"
              renderItem={(item) => (
                <List.Item 
                  style={{ padding: '12px 0', borderBottom: '1px solid #f0f0f0' }}
                  actions={[
                    <Button type="primary" size="small" onClick={() => navigate('/fee')}>
                      去缴费
                    </Button>
                  ]}
                >
                  <List.Item.Meta
                    avatar={<PayCircleOutlined style={{ fontSize: '24px', color: '#3f8600' }} />}
                    title={<span style={{ fontWeight: 500, color: '#1f2937' }}>{item.feeName || '-'}</span>}
                    description={
                      <div style={{ marginTop: '4px' }}>
                        <span style={{ color: '#cf1322', fontWeight: 600, fontSize: '16px' }}>
                          ¥{item.amount || 0}
                        </span>
                        <span style={{ marginLeft: '12px', color: '#9ca3af', fontSize: '13px' }}>
                          截止: {item.dueDate || '-'}
                        </span>
                      </div>
                    }
                  />
                </List.Item>
              )}
            />
            {!loading && unpaidFees.length === 0 && (
              <div style={{ textAlign: 'center', padding: '40px 0', color: '#9ca3af' }}>
                <PayCircleOutlined style={{ fontSize: '48px', marginBottom: '12px', color: '#d1d5db' }} />
                <p>暂无待缴费记录</p>
              </div>
            )}
          </Card>
        </Col>
      </Row>
    </div>
  )
}

export default Home
