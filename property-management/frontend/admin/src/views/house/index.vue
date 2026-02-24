<template>
  <div class="page-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">房屋管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon style="margin-right: 4px"><Plus /></el-icon>
            新增
          </el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="项目名称">
          <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="楼栋">
          <el-input v-model="queryParams.building" placeholder="请输入楼栋" clearable style="width: 140px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 140px">
            <el-option label="自住" value="occupied" />
            <el-option label="空置" value="vacant" />
            <el-option label="出租" value="rented" />
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
        <el-table-column prop="projectName" label="项目名称" width="160" show-overflow-tooltip />
        <el-table-column prop="building" label="楼栋" width="80" />
        <el-table-column prop="unit" label="单元" width="80" />
        <el-table-column prop="roomNumber" label="房号" width="90" />
        <el-table-column prop="area" label="面积(㎡)" width="100">
          <template #default="{ row }">
            {{ row.area ? `${row.area}㎡` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'occupied'" type="success" size="small">自住</el-tag>
            <el-tag v-else-if="row.status === 'vacant'" type="warning" size="small">空置</el-tag>
            <el-tag v-else-if="row.status === 'rented'" type="primary" size="small">出租</el-tag>
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
import { getHousePage } from '@/api/house'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  projectName: '',
  building: '',
  status: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getHousePage(queryParams.value)
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
    projectName: '',
    building: '',
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
