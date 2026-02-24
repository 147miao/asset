<template>
  <div class="page-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">服务预约</span>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="服务类型">
          <el-select v-model="queryParams.serviceType" placeholder="请选择服务类型" clearable style="width: 160px">
            <el-option label="保洁服务" value="cleaning" />
            <el-option label="安保服务" value="security" />
            <el-option label="场地租赁" value="venue" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 140px">
            <el-option label="待处理" value="pending" />
            <el-option label="处理中" value="processing" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
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
        <el-table-column prop="userName" label="用户" width="100" />
        <el-table-column prop="serviceType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.serviceType === 'cleaning'" type="info" size="small">保洁服务</el-tag>
            <el-tag v-else-if="row.serviceType === 'security'" type="primary" size="small">安保服务</el-tag>
            <el-tag v-else-if="row.serviceType === 'venue'" type="success" size="small">场地租赁</el-tag>
            <el-tag v-else size="small">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="appointmentTime" label="预约时间" width="150" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'pending'" type="warning" size="small">待处理</el-tag>
            <el-tag v-else-if="row.status === 'processing'" type="primary" size="small">处理中</el-tag>
            <el-tag v-else-if="row.status === 'completed'" type="success" size="small">已完成</el-tag>
            <el-tag v-else-if="row.status === 'cancelled'" type="danger" size="small">已取消</el-tag>
            <el-tag v-else size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small">查看</el-button>
            <el-button type="success" link size="small" v-if="row.status === 'pending'">处理</el-button>
            <el-button type="warning" link size="small" v-if="row.status === 'processing'">完成</el-button>
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
import { Search, RefreshLeft } from '@element-plus/icons-vue'
import { getServicePage } from '@/api/service'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  serviceType: '',
  status: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getServicePage(queryParams.value)
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
    serviceType: '',
    status: ''
  }
  loadData()
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
