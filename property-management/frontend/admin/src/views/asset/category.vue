<template>
  <div class="page-container">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span class="title">资产分类管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon style="margin-right: 4px"><Plus /></el-icon>
            新增分类
          </el-button>
        </div>
      </template>

      <div class="category-container">
        <!-- 资产类型选择 -->
        <el-form :inline="true" class="type-selector">
          <el-form-item label="资产类型">
            <el-radio-group v-model="activeAssetType" @change="loadCategoryTree">
              <el-radio label="fixed">固定资产</el-radio>
              <el-radio label="current">流动资产</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>

        <!-- 分类树 -->
        <div class="category-tree">
          <el-tree
            v-loading="loading"
            :data="categoryTree"
            :props="treeProps"
            :expand-on-click-node="false"
            @node-click="handleNodeClick"
            node-key="id"
            default-expand-all
          >
            <template #default="{ node, data }">
              <div class="tree-node">
                <span class="node-name">{{ data.categoryName }}</span>
                <div class="node-actions">
                  <el-button type="primary" link size="small" @click.stop="handleEdit(data)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button type="success" link size="small" @click.stop="handleAddChild(data.id)">
                    <el-icon><Plus /></el-icon>
                  </el-button>
                  <el-button type="danger" link size="small" @click.stop="handleDelete(data.id, data.categoryName)">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </div>
            </template>
          </el-tree>
        </div>
      </div>
    </el-card>

    <!-- 分类编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      destroy-on-close
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-form-item label="分类编码" prop="categoryCode">
          <el-input v-model="formData.categoryCode" placeholder="请输入分类编码" />
        </el-form-item>
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="formData.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="父分类" prop="parentId">
          <el-select v-model="formData.parentId" placeholder="请选择父分类">
            <el-option label="无（一级分类）" value="" />
            <el-option
              v-for="item in parentCategories"
              :key="item.id"
              :label="item.categoryName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="资产类型" prop="assetType">
          <el-select v-model="formData.assetType" placeholder="请选择资产类型">
            <el-option label="固定资产" value="fixed" />
            <el-option label="流动资产" value="current" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类描述">
          <el-input
            v-model="formData.description"
            type="textarea"
            placeholder="请输入分类描述"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="排序序号">
          <el-input-number v-model="formData.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status">
            <el-option label="启用" value="active" />
            <el-option label="禁用" value="inactive" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getCategoryTree, saveCategory, updateCategory, deleteCategory, getCategoriesByAssetType } from '@/api/assetCategory'

const loading = ref(false)
const activeAssetType = ref('fixed')
const categoryTree = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formData = ref({})
const formRef = ref(null)

const treeProps = {
  children: 'children',
  label: 'categoryName'
}

const parentCategories = computed(() => {
  return categoryTree.value.filter(item => item.level < 3)
})

const loadCategoryTree = async () => {
  loading.value = true
  try {
    const res = await getCategoriesByAssetType(activeAssetType.value)
    if (res.code === 200) {
      categoryTree.value = buildTree(res.data || [])
    }
  } catch (error) {
    console.error('加载分类树失败:', error)
    ElMessage.error('加载分类树失败')
  } finally {
    loading.value = false
  }
}

const buildTree = (data, parentId = null) => {
  return data
    .filter(item => item.parentId === parentId)
    .map(item => ({
      ...item,
      children: buildTree(data, item.id)
    }))
}

const handleAdd = () => {
  dialogTitle.value = '新增分类'
  formData.value = {
    assetType: activeAssetType.value,
    sortOrder: 0,
    status: 'active'
  }
  dialogVisible.value = true
}

const handleAddChild = (parentId) => {
  dialogTitle.value = '新增子分类'
  formData.value = {
    parentId,
    assetType: activeAssetType.value,
    sortOrder: 0,
    status: 'active'
  }
  dialogVisible.value = true
}

const handleEdit = (data) => {
  dialogTitle.value = '编辑分类'
  formData.value = { ...data }
  dialogVisible.value = true
}

const handleDelete = async (id, name) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除分类 "${name}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const res = await deleteCategory(id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadCategoryTree()
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

const formRules = {
  categoryCode: [
    { required: true, message: '请输入分类编码', trigger: 'blur' }
  ],
  categoryName: [
    { required: true, message: '请输入分类名称', trigger: 'blur' }
  ],
  assetType: [
    { required: true, message: '请选择资产类型', trigger: 'change' }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    
    let res
    if (formData.value.id) {
      res = await updateCategory(formData.value)
    } else {
      res = await saveCategory(formData.value)
    }
    
    if (res.code === 200) {
      ElMessage.success(formData.value.id ? '更新成功' : '新增成功')
      dialogVisible.value = false
      loadCategoryTree()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
  }
}

const handleNodeClick = (data) => {
  console.log('点击分类:', data)
}

onMounted(() => {
  loadCategoryTree()
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
  }
  
  .type-selector {
    padding: 16px;
    border-bottom: 1px solid #f0f0f0;
    margin-bottom: 16px;
    background-color: #fafafa;
    border-radius: 8px;
  }
  
  .category-tree {
    min-height: 500px;
    
    .tree-node {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      padding: 4px 0;
      
      .node-name {
        flex: 1;
        padding: 4px 8px;
        border-radius: 4px;
        transition: all 0.3s;
        
        &:hover {
          background-color: #f5f7fa;
        }
      }
      
      .node-actions {
        display: flex;
        gap: 4px;
        opacity: 0;
        transition: opacity 0.3s;
      }
      
      &:hover .node-actions {
        opacity: 1;
      }
    }
  }
  
  // 响应式设计
  @media (max-width: 1199px) {
    padding: 12px;
    
    .card-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 12px;
    }
    
    .type-selector {
      padding: 12px;
    }
    
    .category-tree {
      min-height: 400px;
    }
  }
  
  @media (max-width: 767px) {
    padding: 8px;
    
    .card-header {
      .title {
        font-size: 16px;
      }
    }
    
    .type-selector {
      .el-form-item {
        margin-bottom: 8px;
        
        .el-form-item__label {
          font-size: 12px;
        }
      }
    }
    
    .category-tree {
      min-height: 300px;
      
      .tree-node {
        flex-direction: column;
        align-items: flex-start;
        gap: 8px;
        padding: 8px 0;
        
        .node-actions {
          opacity: 1;
          width: 100%;
          justify-content: flex-start;
          gap: 8px;
        }
      }
    }
  }
}
</style>