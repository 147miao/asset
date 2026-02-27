package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.entity.AssetCategory;

import java.util.List;
import java.util.Map;

public interface AssetCategoryService extends IService<AssetCategory> {
    List<AssetCategory> getCategoryTree();
    List<AssetCategory> getCategoriesByParentId(Long parentId);
    List<AssetCategory> getCategoriesByAssetType(String assetType);
    boolean saveCategory(AssetCategory category);
    boolean updateCategory(AssetCategory category);
    boolean deleteCategory(Long id);
    boolean updateSortOrder(Map<Long, Integer> sortMap);
    boolean fixEncoding();
}