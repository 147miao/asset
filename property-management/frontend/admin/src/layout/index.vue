<template>
  <div class="layout">
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '220px'">
        <div class="logo">
          <el-icon v-if="isCollapse" :size="24"><HomeFilled /></el-icon>
          <template v-else>
            <el-icon :size="24"><HomeFilled /></el-icon>
            <span>智慧物业</span>
          </template>
        </div>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          class="sidebar-menu"
        >
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/project">
            <el-icon><OfficeBuilding /></el-icon>
            <span>项目管理</span>
          </el-menu-item>
          <el-menu-item index="/asset">
            <el-icon><Box /></el-icon>
            <span>资产管理</span>
          </el-menu-item>
          <el-menu-item index="/user">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/house">
            <el-icon><House /></el-icon>
            <span>房屋管理</span>
          </el-menu-item>
          <el-menu-item index="/fee">
            <el-icon><Money /></el-icon>
            <span>费用管理</span>
          </el-menu-item>
          <el-menu-item index="/service">
            <el-icon><Service /></el-icon>
            <span>服务管理</span>
          </el-menu-item>
          <el-menu-item index="/repair">
            <el-icon><Tools /></el-icon>
            <span>报修管理</span>
          </el-menu-item>
          <el-menu-item index="/message">
            <el-icon><Message /></el-icon>
            <span>消息管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="header-left">
            <el-icon class="collapse-btn" @click="toggleCollapse">
              <Fold v-if="!isCollapse" />
              <Expand v-else />
            </el-icon>
            <span class="page-title">{{ pageTitle }}</span>
          </div>
          <div class="header-right">
            <el-badge :value="unreadCount" :hidden="unreadCount === 0" :max="99">
              <el-icon :size="20" class="header-icon" @click="navigateTo('/message')">
                <Bell />
              </el-icon>
            </el-badge>
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                  {{ userStore.userInfo?.realName?.charAt(0) }}
                </el-avatar>
                <span class="user-name">{{ userStore.userInfo?.realName || '管理员' }}</span>
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleProfile">个人中心</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getUnreadMessages } from '@/api/message'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)
const unreadCount = ref(0)
const activeMenu = computed(() => route.path)

const menuTitleMap: Record<string, string> = {
  '/dashboard': '首页',
  '/project': '项目管理',
  '/asset': '资产管理',
  '/user': '用户管理',
  '/house': '房屋管理',
  '/fee': '费用管理',
  '/service': '服务管理',
  '/repair': '报修管理',
  '/message': '消息管理'
}

const pageTitle = computed(() => menuTitleMap[route.path] || '首页')

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const navigateTo = (path: string) => {
  router.push(path)
}

const handleProfile = () => {
  ElMessage.info('个人中心功能开发中')
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

const fetchUnreadCount = async () => {
  try {
    if (userStore.userInfo?.id) {
      const res = await getUnreadMessages(userStore.userInfo.id)
      const data = res.data as unknown[]
      unreadCount.value = Array.isArray(data) ? data.length : 0
    }
  } catch {
    unreadCount.value = 0
  }
}

onMounted(() => {
  fetchUnreadCount()
})
</script>

<style lang="scss" scoped>
.layout {
  width: 100%;
  height: 100vh;
  
  .el-container {
    height: 100%;
  }
  
  .el-aside {
    background: #fff;
    box-shadow: 1px 0 3px rgba(0, 0, 0, 0.08);
    transition: width 0.3s;
    
    .logo {
      height: 64px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      color: #1890ff;
      font-size: 18px;
      font-weight: 600;
      border-bottom: 1px solid #f0f0f0;
      
      .el-icon {
        color: #1890ff;
      }
    }
    
    .sidebar-menu {
      border-right: none;
      
      :deep(.el-menu-item) {
        height: 50px;
        line-height: 50px;
        margin: 4px 8px;
        border-radius: 8px;
        
        &.is-active {
          background: #e6f7ff !important;
          color: #1890ff !important;
          
          .el-icon {
            color: #1890ff;
          }
        }
        
        &:hover {
          background: #f5f7fa !important;
        }
        
        .el-icon {
          margin-right: 8px;
        }
      }
    }
  }
  
  .el-header {
    background: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 16px;
      
      .collapse-btn {
        font-size: 18px;
        cursor: pointer;
        color: #666;
        
        &:hover {
          color: #1890ff;
        }
      }
      
      .page-title {
        font-size: 16px;
        font-weight: 500;
        color: #1f2937;
      }
    }
    
    .header-right {
      display: flex;
      align-items: center;
      gap: 20px;
      
      .header-icon {
        cursor: pointer;
        color: #666;
        
        &:hover {
          color: #1890ff;
        }
      }
      
      .user-info {
        display: flex;
        align-items: center;
        gap: 8px;
        cursor: pointer;
        padding: 4px 8px;
        border-radius: 8px;
        
        &:hover {
          background: #f5f7fa;
        }
        
        .user-name {
          color: #1f2937;
          font-weight: 500;
        }
        
        .el-icon {
          color: #999;
          font-size: 12px;
        }
      }
    }
  }
  
  .el-main {
    background: #f5f7fa;
    padding: 20px;
    min-height: calc(100vh - 64px);
  }
}
</style>
