package com.property.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.entity.AssetCategory;
import com.property.management.mapper.AssetCategoryMapper;
import com.property.management.service.AssetCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssetCategoryServiceImpl extends ServiceImpl<AssetCategoryMapper, AssetCategory> implements AssetCategoryService {

    @Override
    public List<AssetCategory> getCategoryTree() {
        List<AssetCategory> allCategories = baseMapper.selectAllCategoryTree();
        // 构建分类树结构
        return buildCategoryTree(allCategories, null);
    }

    @Override
    public List<AssetCategory> getCategoriesByParentId(Long parentId) {
        return baseMapper.selectByParentId(parentId);
    }

    @Override
    public List<AssetCategory> getCategoriesByAssetType(String assetType) {
        return baseMapper.selectByAssetType(assetType);
    }

    @Override
    public boolean saveCategory(AssetCategory category) {
        // 计算分类级别
        if (category.getParentId() == null) {
            category.setLevel(1);
        } else {
            AssetCategory parent = getById(category.getParentId());
            if (parent != null) {
                category.setLevel(parent.getLevel() + 1);
            }
        }
        return save(category);
    }

    @Override
    public boolean updateCategory(AssetCategory category) {
        // 重新计算分类级别
        if (category.getParentId() == null) {
            category.setLevel(1);
        } else {
            AssetCategory parent = getById(category.getParentId());
            if (parent != null) {
                category.setLevel(parent.getLevel() + 1);
            }
        }
        return updateById(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        // 检查是否有子分类
        QueryWrapper<AssetCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        wrapper.eq("deleted", 0);
        List<AssetCategory> children = list(wrapper);
        if (!children.isEmpty()) {
            return false; // 有子分类，不能删除
        }
        // 逻辑删除
        AssetCategory category = getById(id);
        if (category != null) {
            category.setDeleted(1);
            return updateById(category);
        }
        return false;
    }

    @Override
    public boolean updateSortOrder(Map<Long, Integer> sortMap) {
        sortMap.forEach((id, sortOrder) -> {
            AssetCategory category = new AssetCategory();
            category.setId(id);
            category.setSortOrder(sortOrder);
            updateById(category);
        });
        return true;
    }

    @Override
    public boolean fixEncoding() {
        try {
            baseMapper.fixEncoding();
            baseMapper.insertFixedCategories();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<AssetCategory> buildCategoryTree(List<AssetCategory> allCategories, Long parentId) {
        return allCategories.stream()
                .filter(category -> parentId == null ? category.getParentId() == null : parentId.equals(category.getParentId()))
                .peek(category -> {
                    List<AssetCategory> children = buildCategoryTree(allCategories, category.getId());
                })
                .collect(Collectors.toList());
    }
}