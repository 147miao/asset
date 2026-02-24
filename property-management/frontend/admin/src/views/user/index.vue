<template>
  <div class="user-page">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">用户列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon style="margin-right: 4px"><Plus /></el-icon>
            新增用户
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.userName" placeholder="请输入姓名" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="用户类型">
          <el-select v-model="searchForm.userType" placeholder="请选择" clearable style="width: 140px">
            <el-option label="业主" value="owner" />
            <el-option label="租户" value="tenant" />
            <el-option label="员工" value="employee" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable style="width: 120px">
            <el-option label="正常" value="active" />
            <el-option label="禁用" value="disabled" />
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
        <el-table-column prop="realName" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="userType" label="用户类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getUserTypeColor(row.userType)" size="small">{{ getUserTypeText(row.userType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="projectName" label="所属项目" min-width="150" show-overflow-tooltip />
        <el-table-column prop="houseInfo" label="房屋信息" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-switch v-model="row.status" active-value="active" inactive-value="disabled" @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link size="small" @click="handleResetPwd(row)">重置</el-button>
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
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <el-select v-model="form.userType" placeholder="请选择" style="width: 100%">
            <el-option label="业主" value="owner" />
            <el-option label="租户" value="tenant" />
            <el-option label="员工" value="employee" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属项目">
          <el-select v-model="form.projectId" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in projectList" :key="item.id" :label="item.projectName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshLeft, Plus } from '@element-plus/icons-vue'
import { getUserPage, addUser, updateUser, deleteUser, resetPassword, updateStatus } from '@/api/user'
import { getAllProjects } from '@/api/project'
import { logger } from '@/utils/request'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const formRef = ref(null)
const projectList = ref([])

const searchForm = reactive({
  userName: '',
  phone: '',
  userType: '',
  status: ''
})

const form = reactive({
  id: null,
  realName: '',
  phone: '',
  userType: 'owner',
  projectId: null,
  email: '',
  idCard: '',
  address: '',
  remark: ''
})

const rules = {
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  userType: [{ required: true, message: '请选择用户类型', trigger: 'change' }]
}

const userTypeMap = {
  owner: { text: '业主', color: 'primary' },
  tenant: { text: '租户', color: 'success' },
  employee: { text: '员工', color: 'warning' }
}

const getUserTypeText = (type) => userTypeMap[type]?.text || type
const getUserTypeColor = (type) => userTypeMap[type]?.color || 'info'

const loadData = async () => {
  loading.value = true
  logger.info('加载用户列表', { pageNum: pageNum.value, pageSize: pageSize.value, searchForm })
  
  try {
    const res = await getUserPage({ ...searchForm, pageNum: pageNum.value, pageSize: pageSize.value })
    
    if (!res.data) {
      logger.warn('用户列表响应数据为空')
      tableData.value = []
      total.value = 0
      return
    }
    
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
    logger.info('用户列表加载成功', { total: total.value, count: tableData.value.length })
  } catch (error) {
    logger.error('加载用户列表失败', error)
    ElMessage.error(error.message || '加载用户列表失败')
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const loadProjects = async () => {
  logger.info('加载项目列表')
  
  try {
    const res = await getAllProjects()
    projectList.value = res.data || []
    logger.info('项目列表加载成功', { count: projectList.value.length })
  } catch (error) {
    logger.error('加载项目列表失败', error)
    ElMessage.error('加载项目列表失败')
    projectList.value = []
  }
}

const handleSearch = () => {
  logger.info('搜索用户', searchForm)
  pageNum.value = 1
  loadData()
}

const handleReset = () => {
  logger.info('重置搜索条件')
  Object.assign(searchForm, { userName: '', phone: '', userType: '', status: '' })
  handleSearch()
}

const handleAdd = () => {
  logger.info('打开新增用户对话框')
  dialogTitle.value = '新增用户'
  Object.assign(form, { id: null, realName: '', phone: '', userType: 'owner', projectId: null, email: '', idCard: '', address: '', remark: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  logger.info('打开编辑用户对话框', { userId: row.id, userName: row.realName })
  dialogTitle.value = '编辑用户'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', { type: 'warning' })
  } catch {
    logger.info('取消删除用户', { userId: row.id })
    return
  }
  
  logger.info('删除用户', { userId: row.id, userName: row.realName })
  
  try {
    await deleteUser(row.id)
    logger.info('用户删除成功', { userId: row.id })
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    logger.error('删除用户失败', { userId: row.id, error })
    ElMessage.error(error.message || '删除失败')
  }
}

const handleResetPwd = async (row) => {
  try {
    await ElMessageBox.confirm('确定要重置该用户的密码吗？', '提示', { type: 'warning' })
  } catch {
    logger.info('取消重置密码', { userId: row.id })
    return
  }
  
  logger.info('重置用户密码', { userId: row.id, userName: row.realName })
  
  try {
    await resetPassword(row.id)
    logger.info('密码重置成功', { userId: row.id })
    ElMessage.success('密码已重置为123456')
  } catch (error) {
    logger.error('重置密码失败', { userId: row.id, error })
    ElMessage.error(error.message || '重置密码失败')
  }
}

const handleStatusChange = async (row) => {
  logger.info('更新用户状态', { userId: row.id, newStatus: row.status })
  
  try {
    await updateStatus(row.id, row.status)
    logger.info('状态更新成功', { userId: row.id, status: row.status })
    ElMessage.success('状态更新成功')
  } catch (error) {
    logger.error('状态更新失败', { userId: row.id, error })
    row.status = row.status === 'active' ? 'disabled' : 'active'
    ElMessage.error(error.message || '状态更新失败')
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
  } catch (error) {
    logger.warn('用户表单验证失败', error)
    return
  }
  
  submitLoading.value = true
  
  try {
    if (form.id) {
      logger.info('更新用户', { userId: form.id, userName: form.realName })
      await updateUser(form)
      logger.info('用户更新成功', { userId: form.id })
      ElMessage.success('修改成功')
    } else {
      logger.info('新增用户', { userName: form.realName, phone: form.phone })
      await addUser({ ...form, password: '123456' })
      logger.info('用户新增成功')
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    logger.error('保存用户失败', { form, error })
    ElMessage.error(error.message || '保存失败')
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  loadData()
  loadProjects()
})
</script>

<style lang="scss" scoped>
.user-page {
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
