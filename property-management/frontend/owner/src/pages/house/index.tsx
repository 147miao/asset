import { useState, useEffect } from 'react'
import { Card, Table, Tag, message } from 'antd'
import { useUserStore } from '../../stores/user'
import { getHousesByOwner } from '../../api'
import type { House } from '../../types'

function House() {
  const userInfo = useUserStore((state) => state.userInfo)
  const [houses, setHouses] = useState<House[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadData()
  }, [userInfo?.id])

  const loadData = async () => {
    if (!userInfo?.id) {
      setHouses([])
      setLoading(false)
      return
    }
    setLoading(true)
    try {
      const res = await getHousesByOwner(userInfo.id)
      setHouses(res.data || [])
    } catch {
      message.error('加载房屋数据失败')
    } finally {
      setLoading(false)
    }
  }

  const columns = [
    { title: '楼栋', dataIndex: 'building', key: 'building' },
    { title: '单元', dataIndex: 'unit', key: 'unit' },
    { title: '房号', dataIndex: 'number', key: 'number' },
    { title: '面积(㎡)', dataIndex: 'area', key: 'area' },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => {
        const colorMap: Record<string, 'green' | 'red'> = {
          normal: 'green',
          abnormal: 'red'
        }
        const textMap: Record<string, string> = {
          normal: '正常',
          abnormal: '异常'
        }
        return <Tag color={colorMap[status] || 'default'}>{textMap[status] || status}</Tag>
      }
    }
  ]

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-text-primary">我的房屋</h1>
      </div>
      <Card>
        <Table
          columns={columns}
          dataSource={houses}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      </Card>
    </div>
  )
}

export default House
