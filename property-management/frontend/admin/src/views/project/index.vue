<template>
  <div class="project-page">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">项目列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon style="margin-right: 4px"><Plus /></el-icon>
            新增项目
          </el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="项目名称">
          <el-input v-model="searchForm.projectName" placeholder="请输入项目名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="项目类型">
          <el-select v-model="searchForm.projectType" placeholder="请选择" clearable style="width: 140px">
            <el-option label="住宅" value="residential" />
            <el-option label="商业" value="commercial" />
            <el-option label="工业园区" value="industrial" />
          </el-select>
        </el-form-item>
        <el-form-item label="运营状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 120px">
            <el-option label="在营" value="operating" />
            <el-option label="待交付" value="pending" />
            <el-option label="维修中" value="maintenance" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon style="margin-right: 4px"><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon style="margin-right: 4px"><RefreshLeft /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" style="width: 100%" v-loading="loading" :row-style="{ height: '60px' }">
        <el-table-column prop="projectName" label="项目名称" width="180" show-overflow-tooltip />
        <el-table-column prop="projectType" label="项目类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ getProjectTypeText(row.projectType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="地址" min-width="180" show-overflow-tooltip />
        <el-table-column prop="buildingArea" label="建筑面积(㎡)" width="110" />
        <el-table-column prop="buildingCount" label="楼栋数量" width="90" />
        <el-table-column prop="status" label="运营状态" width="90">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目类型" prop="projectType">
          <el-select v-model="form.projectType" placeholder="请选择项目类型" style="width: 100%">
            <el-option label="住宅" value="residential" />
            <el-option label="商业" value="commercial" />
            <el-option label="工业园区" value="industrial" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入项目地址" />
        </el-form-item>
        <el-form-item label="建筑面积" prop="buildingArea">
          <el-input-number v-model="form.buildingArea" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="楼栋数量" prop="buildingCount">
          <el-input-number v-model="form.buildingCount" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="业态分布" prop="businessDistribution">
          <el-input v-model="form.businessDistribution" type="textarea" placeholder="请输入业态分布" />
        </el-form-item>
        <el-form-item label="运营状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择运营状态" style="width: 100%">
            <el-option label="在营" value="operating" />
            <el-option label="待交付" value="pending" />
            <el-option label="维修中" value="maintenance" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入项目描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshLeft, Plus } from '@element-plus/icons-vue'
import { getProjectPage, addProject, updateProject, deleteProject } from '@/api/project'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('新增项目')
const formRef = ref(null)

const searchForm = reactive({
  projectName: '',
  projectType: '',
  status: ''
})

const form = reactive({
  id: null,
  projectName: '',
  projectType: '',
  address: '',
  buildingArea: null,
  buildingCount: null,
  businessDistribution: '',
  status: 'operating',
  description: ''
})

const rules = {
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  projectType: [{ required: true, message: '请选择项目类型', trigger: 'change' }]
}

const projectTypeMap = {
  residential: '住宅',
  commercial: '商业',
  industrial: '工业园区'
}

const statusMap = {
  operating: { text: '在营', type: 'success' },
  pending: { text: '待交付', type: 'warning' },
  maintenance: { text: '维修中', type: 'danger' }
}

const getProjectTypeText = (type) => projectTypeMap[type] || type
const getStatusType = (status) => statusMap[status]?.type || 'info'
const getStatusText = (status) => statusMap[status]?.text || status

const loadData = async () => {
  loading.value = true
  try {
    const res = await getProjectPage({ ...searchForm, pageNum: pageNum.value, pageSize: pageSize.value })
    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pageNum.value = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, { projectName: '', projectType: '', status: '' })
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增项目'
  Object.assign(form, { id: null, projectName: '', projectType: '', address: '', buildingArea: null, buildingCount: null, businessDistribution: '', status: 'operating', description: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑项目'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定要删除该项目吗？', '提示', { type: 'warning' })
  await deleteProject(row.id)
  ElMessage.success('删除成功')
  loadData()
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (form.id) {
    await updateProject(form)
    ElMessage.success('修改成功')
  } else {
    await addProject(form)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.project-page {
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
