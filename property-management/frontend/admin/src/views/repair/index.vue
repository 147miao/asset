<template>
  <div class="page-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">报修管理</span>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="用户姓名">
          <el-input v-model="queryParams.userName" placeholder="请输入用户姓名" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="报修类型">
          <el-select v-model="queryParams.repairType" placeholder="请选择报修类型" clearable style="width: 160px">
            <el-option label="水管" value="water" />
            <el-option label="电路" value="electricity" />
            <el-option label="家电" value="appliance" />
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
        <el-table-column prop="userPhone" label="电话" width="130" />
        <el-table-column prop="projectName" label="项目" width="150" show-overflow-tooltip />
        <el-table-column prop="repairType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.repairType === 'water'" type="info" size="small">水管</el-tag>
            <el-tag v-else-if="row.repairType === 'electricity'" type="warning" size="small">电路</el-tag>
            <el-tag v-else-if="row.repairType === 'appliance'" type="success" size="small">家电</el-tag>
            <el-tag v-else size="small">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
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
            <el-button type="primary" link size="small" @click="handleView(row)">查看</el-button>
            <el-button type="success" link size="small" v-if="row.status === 'pending'" @click="handleAssign(row)">派单</el-button>
            <el-button type="warning" link size="small" v-if="row.status === 'processing'" @click="handleComplete(row)">完成</el-button>
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

    <el-dialog v-model="viewDialogVisible" title="报修详情" width="600px">
      <el-descriptions :column="1" border v-if="currentRow">
        <el-descriptions-item label="报修编号">{{ currentRow.repairNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="用户姓名">{{ currentRow.userName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentRow.userPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="项目名称">{{ currentRow.projectName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="房屋信息">{{ currentRow.houseInfo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="报修类型">
          <el-tag v-if="currentRow.repairType === 'water'" type="info">水管</el-tag>
          <el-tag v-else-if="currentRow.repairType === 'electricity'" type="warning">电路</el-tag>
          <el-tag v-else-if="currentRow.repairType === 'appliance'" type="success">家电</el-tag>
          <el-tag v-else>其他</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="报修标题">{{ currentRow.title || '-' }}</el-descriptions-item>
        <el-descriptions-item label="详细描述">{{ currentRow.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentRow.status === 'pending'" type="warning">待处理</el-tag>
          <el-tag v-else-if="currentRow.status === 'processing'" type="primary">处理中</el-tag>
          <el-tag v-else-if="currentRow.status === 'completed'" type="success">已完成</el-tag>
          <el-tag v-else-if="currentRow.status === 'cancelled'" type="danger">已取消</el-tag>
          <el-tag v-else>{{ currentRow.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理人">{{ currentRow.assigneeName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理结果">{{ currentRow.result || '-' }}</el-descriptions-item>
        <el-descriptions-item label="用户评价" v-if="currentRow.rating">
          <el-rate v-model="currentRow.rating" disabled />
          <span style="margin-left: 10px">{{ currentRow.feedback || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ currentRow.createTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="assignDialogVisible" title="派单" width="500px">
      <el-form :model="assignForm" label-width="80px">
        <el-form-item label="处理人">
          <el-select v-model="assignForm.assigneeName" placeholder="请选择处理人">
            <el-option label="系统管理员" value="系统管理员" />
            <el-option label="维修人员A" value="维修人员A" />
            <el-option label="维修人员B" value="维修人员B" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssign">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="completeDialogVisible" title="完成报修" width="500px">
      <el-form :model="completeForm" label-width="80px">
        <el-form-item label="处理结果">
          <el-input type="textarea" v-model="completeForm.result" :rows="4" placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComplete">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshLeft } from '@element-plus/icons-vue'
import { getRepairPage, assignRepair, completeRepair } from '@/api/repair'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  userName: '',
  repairType: '',
  status: ''
})

const viewDialogVisible = ref(false)
const assignDialogVisible = ref(false)
const completeDialogVisible = ref(false)
const currentRow = ref(null)
const assignForm = ref({
  assigneeId: 1,
  assigneeName: ''
})
const completeForm = ref({
  result: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getRepairPage(queryParams.value)
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
    userName: '',
    repairType: '',
    status: ''
  }
  loadData()
}

const handleView = (row) => {
  currentRow.value = row
  viewDialogVisible.value = true
}

const handleAssign = (row) => {
  currentRow.value = row
  assignForm.value = {
    assigneeId: 1,
    assigneeName: ''
  }
  assignDialogVisible.value = true
}

const submitAssign = async () => {
  if (!assignForm.value.assigneeName) {
    ElMessage.warning('请选择处理人')
    return
  }
  try {
    await assignRepair(currentRow.value.id, assignForm.value.assigneeId, assignForm.value.assigneeName)
    ElMessage.success('派单成功')
    assignDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('派单失败:', error)
    ElMessage.error('派单失败')
  }
}

const handleComplete = (row) => {
  currentRow.value = row
  completeForm.value = {
    result: ''
  }
  completeDialogVisible.value = true
}

const submitComplete = async () => {
  if (!completeForm.value.result) {
    ElMessage.warning('请输入处理结果')
    return
  }
  try {
    await completeRepair(currentRow.value.id, completeForm.value.result)
    ElMessage.success('操作成功')
    completeDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
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
