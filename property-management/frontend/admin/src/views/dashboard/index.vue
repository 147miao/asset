<template>
  <div class="dashboard">
    <div class="welcome-section">
      <h1 class="welcome-title">欢迎回来，{{ userStore.userInfo?.realName || '管理员' }}</h1>
      <p class="welcome-desc">今天是 {{ today }}，祝您工作顺利！</p>
    </div>

    <el-row :gutter="[16, 16]" class="stat-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="item in statisticsCards" :key="item.title">
        <el-card 
          class="stat-card" 
          :body-style="{ padding: '20px' }"
          shadow="hover"
        >
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: item.color }">
              <el-icon :size="24"><component :is="item.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="[16, 16]" class="content-row">
      <el-col :xs="24" :lg="16">
        <el-card class="chart-card" :body-style="{ padding: '20px' }" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">收支趋势</span>
            </div>
          </template>
          <div ref="incomeChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card class="chart-card" :body-style="{ padding: '20px' }" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">费用类型分布</span>
            </div>
          </template>
          <div ref="feeTypeChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="[16, 16]" class="content-row">
      <el-col :xs="24" :lg="12">
        <el-card class="table-card" :body-style="{ padding: '0' }" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">待处理报修</span>
              <el-button type="primary" link @click="router.push('/repair')">
                查看全部 <el-icon><ArrowRight /></el-icon>
              </el-button>
            </div>
          </template>
          <el-table :data="pendingRepairs" style="width: 100%">
            <el-table-column prop="repairNo" label="报修编号" width="120" />
            <el-table-column prop="userName" label="报修人" />
            <el-table-column prop="repairType" label="报修类型" />
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="提交时间" width="160" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="table-card" :body-style="{ padding: '0' }" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-title">欠费统计</span>
              <el-button type="primary" link @click="router.push('/fee')">
                查看全部 <el-icon><ArrowRight /></el-icon>
              </el-button>
            </div>
          </template>
          <el-table :data="arrearsList" style="width: 100%">
            <el-table-column prop="userName" label="用户名" />
            <el-table-column prop="houseInfo" label="房屋信息" />
            <el-table-column prop="feeType" label="费用类型" />
            <el-table-column prop="unpaidAmount" label="欠费金额">
              <template #default="{ row }">
                <span style="color: #cf1322; font-weight: 600">¥{{ row.unpaidAmount }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import * as echarts from 'echarts'
import { getProjectStatistics } from '@/api/project'
import { getIncomeStatistics, getArrearsStatistics } from '@/api/fee'
import { getRepairPage } from '@/api/repair'
import type { Repair } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const incomeChart = ref<HTMLDivElement>()
const feeTypeChart = ref<HTMLDivElement>()

const today = computed(() => {
  const now = new Date()
  const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' }
  return now.toLocaleDateString('zh-CN', options)
})

interface StatCard {
  title: string
  value: string | number
  icon: string
  color: string
}

const statisticsCards = ref<StatCard[]>([
  { title: '项目总数', value: 0, icon: 'OfficeBuilding', color: '#e6f7ff' },
  { title: '用户总数', value: 0, icon: 'User', color: '#f6ffed' },
  { title: '待处理报修', value: 0, icon: 'Tools', color: '#fff7e6' },
  { title: '欠费总额', value: '¥0', icon: 'Money', color: '#fff1f0' }
])

const pendingRepairs = ref<Repair[]>([])
const arrearsList = ref<{ userName: string; houseInfo: string; feeType: string; unpaidAmount: number }[]>([])

const statusMap: Record<string, { text: string; type: string }> = {
  pending: { text: '待处理', type: 'warning' },
  assigned: { text: '已分配', type: 'info' },
  in_progress: { text: '维修中', type: 'primary' },
  completed: { text: '已完成', type: 'success' }
}

const getStatusType = (status: string): string => statusMap[status]?.type || 'info'
const getStatusText = (status: string): string => statusMap[status]?.text || status

const initIncomeChart = async () => {
  if (!incomeChart.value) return
  const chart = echarts.init(incomeChart.value)
  const endDate = new Date()
  const startDate = new Date()
  startDate.setDate(startDate.getDate() - 30)
  
  try {
    const res = await getIncomeStatistics({
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0]
    })
    
    const incomeData = (res as unknown as { data: { income: { date: string; amount: number }[] } })?.data?.income || []
    
    const option = {
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: incomeData.map((item: { date: string }) => item.date),
        axisLine: { lineStyle: { color: '#e5e7eb' } },
        axisLabel: { color: '#6b7280' }
      },
      yAxis: { 
        type: 'value',
        axisLine: { show: false },
        axisLabel: { color: '#6b7280' },
        splitLine: { lineStyle: { color: '#f0f0f0' } }
      },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      series: [{
        name: '收入',
        type: 'line',
        smooth: true,
        data: incomeData.map((item: { amount: number }) => item.amount),
        areaStyle: { color: 'rgba(24, 144, 255, 0.15)' },
        lineStyle: { color: '#1890ff', width: 2 },
        itemStyle: { color: '#1890ff' }
      }]
    }
    chart.setOption(option)
  } catch (error) {
    console.error('加载收支趋势图表失败:', error)
  }
}

const initFeeTypeChart = async () => {
  if (!feeTypeChart.value) return
  const chart = echarts.init(feeTypeChart.value)
  
  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: ¥{c} ({d}%)' },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      textStyle: { color: '#6b7280' }
    },
    series: [{
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['40%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          fontWeight: 'bold'
        }
      },
      data: [
        { name: '物业费', value: 35000, itemStyle: { color: '#1890ff' } },
        { name: '水电费', value: 28000, itemStyle: { color: '#52c41a' } },
        { name: '停车费', value: 15000, itemStyle: { color: '#faad14' } },
        { name: '其他', value: 5000, itemStyle: { color: '#722ed1' } }
      ]
    }]
  }
  chart.setOption(option)
}

const loadData = async () => {
  try {
    const projectRes = await getProjectStatistics()
    statisticsCards.value[0].value = (projectRes as unknown as { data: { totalProjects: number } })?.data?.totalProjects || 0
    
    const repairRes = await getRepairPage({ page: 1, size: 5, status: 'pending' })
    pendingRepairs.value = (repairRes as unknown as { data: { list: Repair[]; total: number } })?.data?.list || []
    statisticsCards.value[2].value = (repairRes as unknown as { data: { total: number } })?.data?.total || 0
    
    const arrearsRes = await getArrearsStatistics()
    const arrearsData = (arrearsRes as unknown as { data: { userName: string; houseInfo: string; feeType: string; unpaidAmount: number }[] })?.data || []
    arrearsList.value = arrearsData.slice(0, 5)
    const totalArrears = arrearsData.reduce((sum: number, item: { unpaidAmount: number }) => sum + Number(item.unpaidAmount || 0), 0)
    statisticsCards.value[3].value = `¥${totalArrears.toFixed(2)}`
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

onMounted(() => {
  loadData()
  initIncomeChart()
  initFeeTypeChart()
})
</script>

<style lang="scss" scoped>
.dashboard {
  .welcome-section {
    margin-bottom: 24px;
    
    .welcome-title {
      font-size: 24px;
      font-weight: 600;
      color: #1f2937;
      margin: 0 0 8px 0;
    }
    
    .welcome-desc {
      font-size: 14px;
      color: #6b7280;
      margin: 0;
    }
  }
  
  .stat-row {
    margin-bottom: 16px;
  }
  
  .content-row {
    margin-bottom: 16px;
  }
  
  .stat-card {
    border: none;
    border-radius: 8px;
    
    &:hover {
      transform: translateY(-2px);
      transition: all 0.3s ease;
    }
    
    .stat-content {
      display: flex;
      align-items: center;
      
      .stat-icon {
        width: 56px;
        height: 56px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        
        .el-icon {
          color: #1890ff;
        }
      }
      
      .stat-info {
        flex: 1;
        
        .stat-value {
          font-size: 24px;
          font-weight: 600;
          color: #1f2937;
          margin-bottom: 4px;
        }
        
        .stat-title {
          font-size: 14px;
          color: #6b7280;
        }
      }
    }
  }
  
  .chart-card, .table-card {
    border: none;
    border-radius: 8px;
    
    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      .card-title {
        font-size: 16px;
        font-weight: 600;
        color: #1f2937;
      }
    }
  }
  
  :deep(.el-card) {
    --el-card-border-radius: 8px;
  }
  
  :deep(.el-table) {
    .el-table__header-wrapper {
      th {
        background: #fafafa;
        color: #606266;
        font-weight: 600;
      }
    }
    
    .el-table__body-wrapper {
      .el-table__row {
        &:hover > td {
          background: #fafafa !important;
        }
      }
    }
  }
}
</style>
