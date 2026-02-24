<template>
  <div class="page-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">费用管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon style="margin-right: 4px"><Plus /></el-icon>
            新增
          </el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="费用类型">
          <el-select v-model="queryParams.feeType" placeholder="请选择费用类型" clearable style="width: 160px">
            <el-option label="物业费" value="property" />
            <el-option label="水费" value="water" />
            <el-option label="电费" value="electricity" />
            <el-option label="停车费" value="parking" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 140px">
            <el-option label="未支付" value="unpaid" />
            <el-option label="已支付" value="paid" />
            <el-option label="已逾期" value="overdue" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">
            <el-icon style="margin-right: 4px"><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="resetQuery">
            <el-icon style="margin-right: 4px"><RefreshLeft /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" v-loading="loading" style="width: 100%" :row-style="{ height: '60px' }">
        <el-table-column prop="feeName" label="费用名称" width="140" show-overflow-tooltip />
        <el-table-column prop="feeType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.feeType === 'property'" type="info" size="small">物业费</el-tag>
            <el-tag v-else-if="row.feeType === 'water'" type="primary" size="small">水费</el-tag>
            <el-tag v-else-if="row.feeType === 'electricity'" type="warning" size="small">电费</el-tag>
            <el-tag v-else-if="row.feeType === 'parking'" type="success" size="small">停车费</el-tag>
            <el-tag v-else size="small">{{ row.feeType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="100">
          <template #default="{ row }">
            ¥{{ row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="billingPeriod" label="计费周期" width="140" show-overflow-tooltip />
        <el-table-column prop="dueDate" label="截止日期" width="120" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'unpaid'" type="danger" size="small">未支付</el-tag>
            <el-tag v-else-if="row.status === 'paid'" type="success" size="small">已支付</el-tag>
            <el-tag v-else-if="row.status === 'overdue'" type="warning" size="small">已逾期</el-tag>
            <el-tag v-else size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small">编辑</el-button>
            <el-button type="danger" link size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, RefreshLeft, Plus } from '@element-plus/icons-vue'
import { getFeePage } from '@/api/fee'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  feeType: '',
  status: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getFeePage(queryParams.value)
    if (res.code === 200) {
      tableData.value = res.data?.records || []
      total.value = res.data?.total || 0
    }
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    feeType: '',
    status: ''
  }
  loadData()
}

const handleAdd = () => {
  ElMessage.info('新增功能')
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.page-container {
  padding: 24px;
  
  .main-card {
    border-radius: 8px;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
  }
  
  .search-form {
    padding: 20px 0;
    border-bottom: 1px solid #f0f0f0;
    margin-bottom: 24px;
  }
}
</style>
