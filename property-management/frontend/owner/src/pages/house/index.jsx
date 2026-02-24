import { useState, useEffect } from 'react'
import { Card, Table, Tag, message, Empty } from 'antd'
import { HomeOutlined } from '@ant-design/icons'
import { useUserStore } from '../../stores/user'
import { getHousesByOwner } from '../../api'
import { logger } from '../../utils/request'

function House() {
  const { userInfo } = useUserStore()
  const [houses, setHouses] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (userInfo?.id) {
      loadData()
    }
  }, [userInfo?.id])

  const loadData = async () => {
    if (!userInfo?.id) {
      logger.warn('用户信息不存在，无法加载房屋数据')
      return
    }

    setLoading(true)
    logger.info('开始加载房屋信息', { userId: userInfo.id })

    try {
      const res = await getHousesByOwner(userInfo.id)
      setHouses(res.data || [])
      logger.info('房屋信息加载成功', { count: res.data?.length || 0 })
    } catch (error) {
      logger.error('加载房屋信息失败:', error)
      message.error('加载房屋信息失败，请稍后重试')
    } finally {
      setLoading(false)
    }
  }

  const columns = [
    {
      title: '项目名称',
      dataIndex: 'projectName',
      key: 'projectName',
      render: (text) => text || '-',
      width: 200
    },
    {
      title: '楼栋',
      dataIndex: 'building',
      key: 'building',
      render: (text) => text || '-',
      width: 100
    },
    {
      title: '单元',
      dataIndex: 'unit',
      key: 'unit',
      render: (text) => text || '-',
      width: 100
    },
    {
      title: '房号',
      dataIndex: 'roomNumber',
      key: 'roomNumber',
      render: (text) => text || '-',
      width: 100
    },
    {
      title: '面积',
      dataIndex: 'area',
      key: 'area',
      render: (text) => text ? `${text}㎡` : '-',
      width: 120
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const colorMap = {
          occupied: 'green',
          vacant: 'orange',
          rented: 'blue'
        }
        const textMap = {
          occupied: '自住',
          vacant: '空置',
          rented: '出租'
        }
        return <Tag color={colorMap[status]}>{textMap[status] || status || '-'}</Tag>
      },
      width: 100
    }
  ]

  return (
    <div style={{ padding: '24px', background: '#f5f7fa', minHeight: 'calc(100vh - 64px)' }}>
      <div style={{ marginBottom: '24px' }}>
        <h2 style={{ fontSize: '20px', fontWeight: 600, margin: 0, color: '#1f2937' }}>
          我的房屋
        </h2>
        <p style={{ fontSize: '14px', color: '#6b7280', marginTop: '4px', margin: 0 }}>
          查看您名下的所有房产信息
        </p>
      </div>
      
      <Card 
        style={{ 
          borderRadius: '8px', 
          boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
          border: 'none'
        }}
      >
        {houses.length > 0 ? (
          <Table
            columns={columns}
            dataSource={houses}
            rowKey="id"
            loading={loading}
            pagination={false}
            rowStyle={{ height: '60px' }}
            scroll={{ x: 720 }}
          />
        ) : (
          <Empty
            image={Empty.PRESENTED_IMAGE_SIMPLE}
            description={
              <span style={{ color: '#9ca3af' }}>
                {loading ? '加载中...' : '暂无房屋信息'}
              </span>
            }
          />
        )}
      </Card>
    </div>
  )
}

export default House
