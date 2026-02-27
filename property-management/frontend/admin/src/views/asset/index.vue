<template>
  <div class="page-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">资产管理</span>
          <div class="header-actions">
            <el-button type="primary" @click="handleAdd" :disabled="!canAdd">
              <el-icon style="margin-right: 4px"><Plus /></el-icon>
              新增
            </el-button>
            <el-button @click="handleBatchEdit" :disabled="!canEdit || selectedRows.length === 0">
              <el-icon style="margin-right: 4px"><Edit /></el-icon>
              批量编辑
            </el-button>
            <el-button type="danger" @click="handleBatchDelete" :disabled="!canDelete || selectedRows.length === 0">
              <el-icon style="margin-right: 4px"><Delete /></el-icon>
              批量删除
            </el-button>
          </div>
        </div>
      </template>
      
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="资产名称">
          <el-input v-model="queryParams.assetName" placeholder="请输入资产名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="资产类型">
          <el-select v-model="queryParams.assetType" placeholder="请选择类型" clearable style="width: 160px">
            <el-option label="固定资产" value="fixed" />
            <el-option label="流动资产" value="current" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 140px">
            <el-option label="在库" value="in_storage" />
            <el-option label="使用中" value="in_use" />
            <el-option label="维修中" value="maintenance" />
            <el-option label="已报废" value="scrapped" />
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

      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        :row-style="{ height: '60px' }"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="assetCode" label="资产编号" width="120" />
        <el-table-column prop="assetName" label="资产名称" width="180">
          <template #default="{ row, $index }">
            <div v-if="editingRow === row.id">
              <el-input
                v-model="editForm.assetName"
                placeholder="请输入资产名称"
                @blur="handleBlur(row)"
                @keyup.enter="handleEnter(row)"
                ref="editInput"
                class="edit-input"
              />
            </div>
            <div v-else @dblclick="canEdit && handleEdit(row)" class="editable-cell" :class="{ 'non-editable': !canEdit }">
              {{ row.assetName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="assetType" label="资产类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.assetType === 'fixed'" type="primary" size="small">固定资产</el-tag>
            <el-tag v-else-if="row.assetType === 'current'" type="success" size="small">流动资产</el-tag>
            <el-tag v-else size="small">{{ row.assetType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="资产分类" width="120" show-overflow-tooltip />
        <el-table-column prop="projectName" label="所属项目" width="150" show-overflow-tooltip />
        <el-table-column prop="originalValue" label="原值" width="100" align="right">
          <template #default="{ row, $index }">
            <div v-if="editingRow === row.id">
              <el-input-number
                v-model="editForm.originalValue"
                :min="0"
                :precision="2"
                @blur="handleBlur(row)"
                @change="handleEnter(row)"
                class="edit-input"
              />
            </div>
            <div v-else @dblclick="canEdit && handleEdit(row)" class="editable-cell" :class="{ 'non-editable': !canEdit }">
              {{ row.originalValue || 0 }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getStatusType(row.status)"
              size="small"
            >
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="存放位置" min-width="160" show-overflow-tooltip />
        <el-table-column prop="responsiblePerson" label="负责人" width="120" />
        <el-table-column prop="purchaseDate" label="购买日期" width="130" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canEdit" type="primary" link size="small" @click="handleDetailEdit(row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button v-if="canDelete" type="danger" link size="small" @click="handleDelete(row.id, row.assetName)">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
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

    <!-- 资产编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      destroy-on-close
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-form-item label="资产编号" prop="assetCode">
          <el-input v-model="formData.assetCode" placeholder="请输入资产编号" />
        </el-form-item>
        <el-form-item label="资产名称" prop="assetName">
          <el-input v-model="formData.assetName" placeholder="请输入资产名称" />
        </el-form-item>
        <el-form-item label="资产类型" prop="assetType">
          <el-select v-model="formData.assetType" placeholder="请选择资产类型" @change="handleAssetTypeChange">
            <el-option label="固定资产" value="fixed" />
            <el-option label="流动资产" value="current" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产分类" prop="categoryId">
          <el-select v-model="formData.categoryId" placeholder="请选择资产分类">
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.categoryName"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属项目" prop="projectId">
          <el-select v-model="formData.projectId" placeholder="请选择所属项目">
            <el-option
              v-for="project in projects"
              :key="project.id"
              :label="project.projectName"
              :value="project.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="原值" prop="originalValue">
          <el-input-number v-model="formData.originalValue" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="现值" prop="currentValue">
          <el-input-number v-model="formData.currentValue" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="购买日期" prop="purchaseDate">
          <el-date-picker v-model="formData.purchaseDate" type="date" placeholder="请选择购买日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="入库日期" prop="storageDate">
          <el-date-picker v-model="formData.storageDate" type="date" placeholder="请选择入库日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择状态">
            <el-option label="在库" value="in_storage" />
            <el-option label="使用中" value="in_use" />
            <el-option label="维修中" value="maintenance" />
            <el-option label="已报废" value="scrapped" />
          </el-select>
        </el-form-item>
        <el-form-item label="存放位置" prop="location">
          <el-input v-model="formData.location" placeholder="请输入存放位置" />
        </el-form-item>
        <el-form-item label="使用部门" prop="department">
          <el-input v-model="formData.department" placeholder="请输入使用部门" />
        </el-form-item>
        <el-form-item label="负责人" prop="responsiblePerson">
          <el-input v-model="formData.responsiblePerson" placeholder="请输入负责人" />
        </el-form-item>

        <!-- 固定资产特有字段 -->
        <template v-if="formData.assetType === 'fixed'">
          <el-form-item label="折旧方法" prop="depreciationMethod">
            <el-select v-model="formData.depreciationMethod" placeholder="请选择折旧方法">
              <el-option label="直线法" value="straight_line" />
              <el-option label="加速折旧" value="accelerated" />
            </el-select>
          </el-form-item>
          <el-form-item label="预计使用年限" prop="expectedLife">
            <el-input-number v-model="formData.expectedLife" :min="1" style="width: 100%" />
          </el-form-item>
          <el-form-item label="残值" prop="residualValue">
            <el-input-number v-model="formData.residualValue" :min="0" :precision="2" style="width: 100%" />
          </el-form-item>
        </template>

        <!-- 流动资产特有字段 -->
        <template v-if="formData.assetType === 'current'">
          <el-form-item label="供应商" prop="supplier">
            <el-input v-model="formData.supplier" placeholder="请输入供应商" />
          </el-form-item>
          <el-form-item label="质保期" prop="warrantyPeriod">
            <el-input v-model="formData.warrantyPeriod" placeholder="请输入质保期" />
          </el-form-item>
          <el-form-item label="单位" prop="unit">
            <el-input v-model="formData.unit" placeholder="请输入单位" />
          </el-form-item>
          <el-form-item label="数量" prop="quantity">
            <el-input-number v-model="formData.quantity" :min="1" style="width: 100%" />
          </el-form-item>
        </template>

        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 批量编辑弹窗 -->
    <el-dialog
      v-model="batchEditVisible"
      title="批量编辑"
      width="500px"
      destroy-on-close
    >
      <el-form :model="batchEditForm" label-width="120px">
        <el-form-item label="状态">
          <el-select v-model="batchEditForm.status" placeholder="请选择状态">
            <el-option label="在库" value="in_storage" />
            <el-option label="使用中" value="in_use" />
            <el-option label="维修中" value="maintenance" />
            <el-option label="已报废" value="scrapped" />
          </el-select>
        </el-form-item>
        <el-form-item label="使用部门">
          <el-input v-model="batchEditForm.department" placeholder="请输入使用部门" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="batchEditForm.responsiblePerson" placeholder="请输入负责人" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="batchEditVisible = false">取消</el-button>
          <el-button type="primary" @click="handleBatchEditSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshLeft, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getAssetPage, addAsset, updateAsset, deleteAsset, deleteAssets } from '@/api/asset'
import { getCategoriesByAssetType } from '@/api/assetCategory'
import { useUserStore } from '@/stores/user'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  assetName: '',
  assetType: '',
  status: ''
})
const selectedRows = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formData = ref({})
const formRef = ref(null)
const editingRow = ref(null)
const editForm = ref({})
const categories = ref([])
const projects = ref([])

const userStore = useUserStore()

const hasPermission = computed(() => {
  const userType = userStore.userInfo?.userType
  // 只有员工类型的用户有管理权限
  return userType === 'employee'
})

const canEdit = computed(() => {
  return hasPermission.value
})

const canDelete = computed(() => {
  return hasPermission.value
})

const canAdd = computed(() => {
  return hasPermission.value
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAssetPage(queryParams.value)
    if (res.code === 200) {
      // 去重处理，根据ID确保每个资产的唯一性
      const uniqueRecords = []
      const idSet = new Set()
      res.data?.records?.forEach(record => {
        if (!idSet.has(record.id)) {
          idSet.add(record.id)
          uniqueRecords.push(record)
        }
      })
      tableData.value = uniqueRecords
      total.value = uniqueRecords.length
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
    assetName: '',
    assetType: '',
    status: ''
  }
  loadData()
}

const handleAdd = () => {
  dialogTitle.value = '新增资产'
  formData.value = {
    assetType: 'fixed',
    status: 'in_storage'
  }
  loadCategories('fixed')
  loadProjects()
  dialogVisible.value = true
}

const handleDetailEdit = (row) => {
  dialogTitle.value = '编辑资产'
  formData.value = { ...row }
  loadCategories(row.assetType)
  loadProjects()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  editingRow.value = row.id
  editForm.value = { ...row }
  nextTick(() => {
    const input = document.querySelector('.edit-input')
    if (input) {
      input.focus()
    }
  })
}

const handleBlur = (row) => {
  handleSave(row)
}

const handleEnter = (row) => {
  handleSave(row)
}

const handleSave = async (row) => {
  try {
    const res = await updateAsset(editForm.value)
    if (res.code === 200) {
      ElMessage.success('更新成功')
      editingRow.value = null
      loadData()
    } else {
      ElMessage.error(res.message || '更新失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  }
}

const handleDelete = async (id, name) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除资产 "${name}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const res = await deleteAsset(id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的资产')
    return
  }
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个资产吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const ids = selectedRows.value.map(row => row.id)
    const res = await deleteAssets(ids)
    if (res.code === 200) {
      ElMessage.success('批量删除成功')
      selectedRows.value = []
      loadData()
    } else {
      ElMessage.error(res.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

const batchEditVisible = ref(false)
const batchEditForm = ref({
  status: '',
  department: '',
  responsiblePerson: ''
})

const handleBatchEdit = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要编辑的资产')
    return
  }
  batchEditForm.value = {
    status: '',
    department: '',
    responsiblePerson: ''
  }
  batchEditVisible.value = true
}

const handleBatchEditSubmit = async () => {
  try {
    const updateData = { ...batchEditForm.value }
    // 过滤空值
    Object.keys(updateData).forEach(key => {
      if (!updateData[key]) {
        delete updateData[key]
      }
    })
    
    if (Object.keys(updateData).length === 0) {
      ElMessage.warning('请至少选择一个字段进行编辑')
      return
    }
    
    const ids = selectedRows.value.map(row => row.id)
    for (const id of ids) {
      const asset = { id, ...updateData }
      await updateAsset(asset)
    }
    
    ElMessage.success('批量编辑成功')
    batchEditVisible.value = false
    selectedRows.value = []
    loadData()
  } catch (error) {
    console.error('批量编辑失败:', error)
    ElMessage.error('批量编辑失败')
  }
}

const handleSelectionChange = (val) => {
  selectedRows.value = val
}

const handleAssetTypeChange = (assetType) => {
  loadCategories(assetType)
}

const loadCategories = async (assetType) => {
  try {
    const res = await getCategoriesByAssetType(assetType)
    if (res.code === 200) {
      categories.value = res.data || []
    }
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const loadProjects = async () => {
  // 这里应该调用项目API获取项目列表
  projects.value = [
    { id: 1, projectName: '阳光花园' },
    { id: 2, projectName: '创业大厦' },
    { id: 3, projectName: '科技产业园' }
  ]
}

const getStatusType = (status) => {
  const statusMap = {
    'in_storage': 'info',
    'in_use': 'success',
    'maintenance': 'warning',
    'scrapped': 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status) => {
  const statusMap = {
    'in_storage': '在库',
    'in_use': '使用中',
    'maintenance': '维修中',
    'scrapped': '已报废'
  }
  return statusMap[status] || status
}

const formRules = {
  assetCode: [
    { required: true, message: '请输入资产编号', trigger: 'blur' }
  ],
  assetName: [
    { required: true, message: '请输入资产名称', trigger: 'blur' }
  ],
  assetType: [
    { required: true, message: '请选择资产类型', trigger: 'change' }
  ],
  categoryId: [
    { required: true, message: '请选择资产分类', trigger: 'change' }
  ],
  originalValue: [
    { required: true, message: '请输入原值', trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    
    let res
    if (formData.value.id) {
      res = await updateAsset(formData.value)
    } else {
      res = await addAsset(formData.value)
    }
    
    if (res.code === 200) {
      ElMessage.success(formData.value.id ? '更新成功' : '新增成功')
      dialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.page-container {
  padding: 16px;
  
  .main-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 12px;
    padding-bottom: 16px;
    
    .title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
    
    .header-actions {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }
  }
  
  .search-form {
    padding: 16px;
    border-bottom: 1px solid #f0f0f0;
    margin-bottom: 16px;
    background-color: #fafafa;
    border-radius: 8px;
  }
  
  .editable-cell {
    cursor: pointer;
    padding: 4px 8px;
    border-radius: 4px;
    transition: all 0.3s;
    
    &:hover {
      background-color: #f5f7fa;
    }
  }
  
  .non-editable {
    cursor: default;
    
    &:hover {
      background-color: transparent;
    }
  }
  
  .edit-input {
    width: 100%;
  }
  
  // 响应式设计
  @media (max-width: 1199px) {
    padding: 12px;
    
    .card-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 12px;
      
      .header-actions {
        width: 100%;
        justify-content: space-between;
      }
    }
    
    .search-form {
      padding: 12px;
    }
    
    .el-table {
      font-size: 13px;
      
      th, td {
        padding: 8px;
      }
    }
  }
  
  @media (max-width: 767px) {
    padding: 8px;
    
    .card-header {
      .title {
        font-size: 16px;
      }
    }
    
    .search-form {
      .el-form-item {
        margin-bottom: 8px;
        
        .el-form-item__label {
          font-size: 12px;
        }
        
        .el-input,
        .el-select {
          width: 100% !important;
        }
      }
    }
    
    .el-table {
      font-size: 12px;
      
      th, td {
        padding: 6px;
      }
      
      .el-table__column {
        width: auto !important;
        min-width: 80px;
      }
    }
    
    .el-pagination {
      font-size: 12px;
      
      .el-pagination__sizes {
        display: none;
      }
    }
  }
}
</style>