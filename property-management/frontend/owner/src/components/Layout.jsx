import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout as AntLayout, Menu, Badge, Dropdown, Avatar, message } from 'antd'
import { HomeOutlined, HomeFilled, DollarOutlined, ToolOutlined, CustomerServiceOutlined, BellOutlined, UserOutlined, LogoutOutlined } from '@ant-design/icons'
import { useUserStore } from '../stores/user'
import { getUnreadMessages } from '../api'
import { useState, useEffect } from 'react'
import { logger } from '../utils/request'

const { Header, Content, Sider } = AntLayout

const menuItems = [
  { key: '/', icon: <HomeOutlined />, label: '首页' },
  { key: '/house', icon: <HomeFilled />, label: '我的房屋' },
  { key: '/fee', icon: <DollarOutlined />, label: '费用缴纳' },
  { key: '/repair', icon: <ToolOutlined />, label: '报修服务' },
  { key: '/service', icon: <CustomerServiceOutlined />, label: '增值服务' },
  { key: '/message', icon: <BellOutlined />, label: '消息通知' },
  { key: '/profile', icon: <UserOutlined />, label: '个人中心' }
]

function Layout() {
  const navigate = useNavigate()
  const location = useLocation()
  const { userInfo, logout } = useUserStore()
  const [unreadCount, setUnreadCount] = useState(0)

  useEffect(() => {
    const fetchUnread = async () => {
      if (!userInfo?.id) {
        return
      }

      logger.info('加载未读消息', { userId: userInfo.id })

      try {
        const res = await getUnreadMessages(userInfo.id)
        setUnreadCount(res.data?.length || 0)
        logger.info('未读消息加载成功', { count: res.data?.length || 0 })
      } catch (e) {
        logger.error('加载未读消息失败', e)
        setUnreadCount(0)
      }
    }

    if (userInfo.id) {
      fetchUnread()
    }
  }, [userInfo.id])

  const handleLogout = () => {
    logger.info('用户退出登录', { userId: userInfo?.id, userName: userInfo?.realName })
    logout()
    navigate('/login')
  }

  const userMenuItems = [
    { key: 'profile', icon: <UserOutlined />, label: '个人中心' },
    { key: 'logout', icon: <LogoutOutlined />, label: '退出登录' }
  ]

  const handleUserMenuClick = ({ key }) => {
    if (key === 'logout') {
      handleLogout()
    } else if (key === 'profile') {
      navigate('/profile')
    }
  }

  return (
    <AntLayout style={{ minHeight: '100vh' }}>
      <Sider width={200} style={{ background: '#fff' }}>
        <div style={{ height: 64, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18, fontWeight: 'bold', color: '#1890ff' }}>
          智慧物业
        </div>
        <Menu
          mode="inline"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={({ key }) => navigate(key)}
          style={{ borderRight: 0 }}
        />
      </Sider>
      <AntLayout>
        <Header style={{ background: '#fff', padding: '0 20px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <div style={{ fontSize: 16, fontWeight: 500 }}>{menuItems.find(item => item.key === location.pathname)?.label || '首页'}</div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 20 }}>
            <Badge count={unreadCount} size="small">
              <BellOutlined style={{ fontSize: 18, cursor: 'pointer' }} onClick={() => navigate('/message')} />
            </Badge>
            <Dropdown menu={{ items: userMenuItems, onClick: handleUserMenuClick }} placement="bottomRight">
              <div style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: 8 }}>
                <Avatar icon={<UserOutlined />} />
                <span>{userInfo?.realName || '业主'}</span>
              </div>
            </Dropdown>
          </div>
        </Header>
        <Content style={{ margin: 16, padding: 24, background: '#fff', borderRadius: 8, minHeight: 280, overflow: 'auto' }}>
          <Outlet />
        </Content>
      </AntLayout>
    </AntLayout>
  )
}

export default Layout
