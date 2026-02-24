<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in statisticsCards" :key="item.title">
        <el-card class="stat-card">
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

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card>
          <template #header>
            <span>收支趋势</span>
          </template>
          <div ref="incomeChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <span>费用类型分布</span>
          </template>
          <div ref="feeTypeChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>待处理报修</span>
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
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>欠费统计</span>
          </template>
          <el-table :data="arrearsList" style="width: 100%">
            <el-table-column prop="userName" label="用户名" />
            <el-table-column prop="houseInfo" label="房屋信息" />
            <el-table-column prop="feeType" label="费用类型" />
            <el-table-column prop="unpaidAmount" label="欠费金额">
              <template #default="{ row }">
                <span style="color: #f56c6c">¥{{ row.unpaidAmount }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getProjectStatistics } from '@/api/project'
import { getIncomeStatistics, getArrearsStatistics, getFeeTypeStatistics } from '@/api/fee'
import { getRepairPage } from '@/api/repair'

const incomeChart = ref(null)
const feeTypeChart = ref(null)

const statisticsCards = ref([
  { title: '项目总数', value: 0, icon: 'OfficeBuilding', color: '#409EFF' },
  { title: '用户总数', value: 0, icon: 'User', color: '#67C23A' },
  { title: '待处理报修', value: 0, icon: 'Tools', color: '#E6A23C' },
  { title: '欠费总额', value: '¥0', icon: 'Money', color: '#F56C6C' }
])

const pendingRepairs = ref([])
const arrearsList = ref([])

const statusMap = {
  pending: { text: '待处理', type: 'warning' },
  assigned: { text: '已分配', type: 'info' },
  in_progress: { text: '维修中', type: 'primary' },
  completed: { text: '已完成', type: 'success' }
}

const getStatusType = (status) => statusMap[status]?.type || 'info'
const getStatusText = (status) => statusMap[status]?.text || status

const initIncomeChart = async () => {
  const chart = echarts.init(incomeChart.value)
  const endDate = new Date()
  const startDate = new Date()
  startDate.setDate(startDate.getDate() - 30)
  
  try {
    const res = await getIncomeStatistics({
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0]
    })
    
    const incomeData = res?.data?.income || []
    
    const option = {
      tooltip: { trigger: 'axis' },
      xAxis: {
        type: 'category',
        data: incomeData.map(item => item.date)
      },
      yAxis: { type: 'value' },
      series: [{
        name: '收入',
        type: 'line',
        smooth: true,
        data: incomeData.map(item => item.amount),
        areaStyle: { color: 'rgba(64, 158, 255, 0.3)' },
        lineStyle: { color: '#409EFF' }
      }]
    }
    chart.setOption(option)
  } catch (error) {
    console.error('加载收支趋势图表失败:', error)
  }
}

const initFeeTypeChart = async () => {
  const chart = echarts.init(feeTypeChart.value)
  const endDate = new Date()
  const startDate = new Date()
  startDate.setMonth(startDate.getMonth() - 1)
  
  try {
    const res = await getFeeTypeStatistics({
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0]
    })
    
    const feeTypeData = res?.data || []
    
    const option = {
      tooltip: { trigger: 'item' },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        data: feeTypeData.map(item => ({
          name: item.feeType,
          value: item.totalAmount
        }))
      }]
    }
    chart.setOption(option)
  } catch (error) {
    console.error('加载费用类型分布图表失败:', error)
  }
}

const loadData = async () => {
  try {
    const projectRes = await getProjectStatistics(null)
    statisticsCards.value[0].value = projectRes?.data?.totalProjects || 0
    
    const repairRes = await getRepairPage({ pageNum: 1, pageSize: 5, status: 'pending' })
    pendingRepairs.value = repairRes?.data?.records || []
    statisticsCards.value[2].value = repairRes?.data?.total || 0
    
    const arrearsRes = await getArrearsStatistics(null)
    const arrearsData = arrearsRes?.data || []
    arrearsList.value = arrearsData.slice(0, 5)
    const totalArrears = arrearsData.reduce((sum, item) => sum + Number(item.unpaidAmount || 0), 0)
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
  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
      }
      
      .stat-info {
        flex: 1;
        
        .stat-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 8px;
        }
        
        .stat-title {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }
}
</style>
