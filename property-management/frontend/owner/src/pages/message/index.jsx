import { useState, useEffect } from 'react'
import { Card, List, Tag, Badge, Empty, message } from 'antd'
import { BellOutlined } from '@ant-design/icons'
import { useUserStore } from '../../stores/user'
import { getMessages, markAsRead } from '../../api'
import { logger } from '../../utils/request'

function Message() {
  const { userInfo } = useUserStore()
  const [messages, setMessages] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (userInfo?.id) {
      loadData()
    }
  }, [userInfo?.id])

  const loadData = async () => {
    if (!userInfo?.id) {
      logger.warn('用户信息不存在，无法加载消息数据')
      return
    }

    setLoading(true)
    logger.info('开始加载消息', { userId: userInfo.id })

    try {
      const res = await getMessages(userInfo.id, { pageNum: 1, pageSize: 100 })
      setMessages(res.data?.list || res.data || [])
      logger.info('消息加载成功', { count: messages.length })
    } catch (error) {
      logger.error('加载消息失败:', error)
      message.error('加载消息失败，请稍后重试')
    } finally {
      setLoading(false)
    }
  }

  const handleRead = async (item) => {
    if (!item.isRead) {
      try {
        logger.info('标记消息为已读', { messageId: item.id })
        await markAsRead(item.id)
        item.isRead = true
        setMessages([...messages])
        logger.info('消息已标记为已读', { messageId: item.id })
      } catch (error) {
        logger.error('标记消息失败', error)
        message.error('标记失败')
      }
    }
  }

  const getTypeColor = (type) => {
    const colorMap = {
      fee: 'orange',
      repair: 'blue',
      announcement: 'green',
      system: 'red'
    }
    return colorMap[type] || 'default'
  }

  const getTypeText = (type) => {
    const textMap = {
      fee: '缴费提醒',
      repair: '报修通知',
      announcement: '公告',
      system: '系统消息'
    }
    return textMap[type] || type
  }

  return (
    <div style={{ padding: '24px', background: '#f5f7fa', minHeight: 'calc(100vh - 64px)' }}>
      <div style={{ marginBottom: '24px' }}>
        <h2 style={{ fontSize: '20px', fontWeight: 600, margin: 0, color: '#1f2937' }}>
          消息通知
        </h2>
        <p style={{ fontSize: '14px', color: '#6b7280', marginTop: '4px', margin: 0 }}>
          查看您的系统消息和通知
        </p>
      </div>
      
      <Card 
        loading={loading}
        style={{ 
          borderRadius: '8px', 
          boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
          border: 'none'
        }}
      >
        {messages.length > 0 ? (
          <List
            dataSource={messages}
            itemLayout="horizontal"
            renderItem={item => (
              <List.Item
                key={item.id}
                style={{ 
                  cursor: 'pointer', 
                  backgroundColor: item.isRead ? 'transparent' : '#f0f9ff',
                  padding: '16px 0',
                  borderBottom: '1px solid #f0f0f0'
                }}
                onClick={() => handleRead(item)}
              >
                <List.Item.Meta
                  avatar={
                    <Badge dot={!item.isRead}>
                      <BellOutlined style={{ fontSize: '24px', color: '#1890ff' }} />
                    </Badge>
                  }
                  title={
                    <div>
                      <Tag color={getTypeColor(item.messageType || item.type)}>
                        {getTypeText(item.messageType || item.type)}
                      </Tag>
                      <span style={{ marginLeft: '10px', fontWeight: item.isRead ? 400 : 600, color: '#1f2937' }}>
                        {item.title}
                      </span>
                    </div>
                  }
                  description={
                    <div style={{ marginTop: '8px' }}>
                      <p style={{ margin: 0, color: '#4b5563' }}>{item.content}</p>
                      <p style={{ color: '#9ca3af', fontSize: '13px', margin: '8px 0 0 0' }}>
                        {item.createTime}
                      </p>
                    </div>
                  }
                />
              </List.Item>
            )}
          />
        ) : (
          <Empty 
            image={Empty.PRESENTED_IMAGE_SIMPLE}
            description={
              <span style={{ color: '#9ca3af' }}>
                暂无消息
              </span>
            }
          />
        )}
      </Card>
    </div>
  )
}

export default Message
