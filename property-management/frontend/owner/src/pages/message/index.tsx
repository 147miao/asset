import { useState, useEffect } from 'react'
import { Card, List, Badge, Button, message, Empty } from 'antd'
import { useUserStore } from '../../stores/user'
import { getMessages, markAsRead, markAllAsRead } from '../../api'
import type { Message } from '../../types'

function Message() {
  const userInfo = useUserStore((state) => state.userInfo)
  const [messages, setMessages] = useState<Message[]>([])
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
      const res = await getMessages(userInfo.id)
      const data = res.data as { records?: Message[] } | undefined
      setMessages(data?.records || [])
    } catch {
      message.error('加载消息失败')
    } finally {
      setLoading(false)
    }
  }

  const handleRead = async (id: string) => {
    try {
      await markAsRead(id)
      loadData()
    } catch {
      message.error('标记已读失败')
    }
  }

  const handleReadAll = async () => {
    if (!userInfo?.id) return
    try {
      await markAllAsRead(userInfo.id)
      message.success('全部已读')
      loadData()
    } catch {
      message.error('操作失败')
    }
  }

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-text-primary">消息通知</h1>
        <Button onClick={handleReadAll}>全部标记已读</Button>
      </div>
      <Card>
        {messages.length === 0 ? (
          <Empty description="暂无消息" />
        ) : (
          <List
            loading={loading}
            dataSource={messages}
            renderItem={(item) => (
              <List.Item
                className={`cursor-pointer p-4 hover:bg-gray-50 rounded-lg transition-colors ${!item.read ? 'bg-blue-50' : ''}`}
                onClick={() => !item.read && handleRead(item.id)}
              >
                <List.Item.Meta
                  title={
                    <div className="flex items-center gap-2">
                      {!item.read && <Badge status="processing" />}
                      <span className={!item.read ? 'font-semibold' : ''}>{item.title}</span>
                    </div>
                  }
                  description={
                    <div>
                      <p className="text-text-secondary">{item.content}</p>
                      <p className="text-text-muted text-sm mt-1">{item.createTime}</p>
                    </div>
                  }
                />
              </List.Item>
            )}
          />
        )}
      </Card>
    </div>
  )
}

export default Message
