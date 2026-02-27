package com.property.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.management.entity.AssetCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssetCategoryMapper extends BaseMapper<AssetCategory> {
    List<AssetCategory> selectByParentId(Long parentId);
    List<AssetCategory> selectByAssetType(String assetType);
    List<AssetCategory> selectAllCategoryTree();
    void fixEncoding();
    void insertFixedCategories();
}