package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_asset_category")
public class AssetCategory extends BaseEntity {
    private String categoryCode;
    private String categoryName;
    private Long parentId;
    private Integer level;
    private String assetType;
    private String description;
    private Integer sortOrder;
    private String status;
}