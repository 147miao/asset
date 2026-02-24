<template>
  <div class="page-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">消息管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon style="margin-right: 4px"><Plus /></el-icon>
            发送消息
          </el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="消息类型">
          <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 160px">
            <el-option label="通知" value="notification" />
            <el-option label="报修" value="repair" />
            <el-option label="费用" value="fee" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.isRead" placeholder="请选择状态" clearable style="width: 140px">
            <el-option label="未读" :value="false" />
            <el-option label="已读" :value="true" />
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
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.type === 'notification'" type="info" size="small">通知</el-tag>
            <el-tag v-else-if="row.type === 'repair'" type="warning" size="small">报修</el-tag>
            <el-tag v-else-if="row.type === 'fee'" type="success" size="small">费用</el-tag>
            <el-tag v-else size="small">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isRead" label="状态" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.isRead" type="info" size="small">已读</el-tag>
            <el-tag v-else type="danger" size="small">未读</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发送时间" width="160" />
        <el-table-column label="操作" width="130" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small">查看</el-button>
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
import { getMessagePage } from '@/api/message'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  type: '',
  isRead: null
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getMessagePage(queryParams.value)
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
    type: '',
    isRead: null
  }
  loadData()
}

const handleAdd = () => {
  ElMessage.info('发送消息功能')
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
