<template>
  <div class="layout">
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '220px'">
        <div class="logo">
          <img src="@/assets/logo.png" alt="logo" v-if="!isCollapse" />
          <span v-if="!isCollapse">物业管理系统</span>
        </div>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/dashboard">
            <el-icon><HomeFilled /></el-icon>
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
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32" :src="userStore.userInfo.avatar">
                  {{ userStore.userInfo.realName?.charAt(0) }}
                </el-avatar>
                <span class="user-name">{{ userStore.userInfo.realName }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
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

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.layout {
  width: 100%;
  height: 100vh;
  
  .el-container {
    height: 100%;
  }
  
  .el-aside {
    background-color: #304156;
    transition: width 0.3s;
    
    .logo {
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 18px;
      font-weight: bold;
      
      img {
        width: 32px;
        height: 32px;
        margin-right: 10px;
      }
    }
    
    .el-menu {
      border-right: none;
    }
  }
  
  .el-header {
    background-color: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    
    .collapse-btn {
      font-size: 20px;
      cursor: pointer;
    }
    
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;
      
      .user-name {
        margin-left: 10px;
      }
    }
  }
  
  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
}
</style>
