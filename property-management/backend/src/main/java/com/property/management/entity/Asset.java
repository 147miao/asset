package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_asset")
public class Asset extends BaseEntity {
    private String assetCode;
    private String assetName;
    private String assetType;
    private Long categoryId;
    private String categoryName;
    private Long projectId;
    private String projectName;
    private BigDecimal originalValue;
    private BigDecimal currentValue;
    private String purchaseDate;
    private String storageDate;
    private String allocateDate;
    private String status;
    private String location;
    private String department;
    private String responsiblePerson;
    private String lastMaintenanceDate;
    private String scrapDate;
    private String scrapReason;
    // 固定资产特有字段
    private String depreciationMethod;
    private Integer expectedLife;
    private BigDecimal residualValue;
    // 流动资产特有字段
    private String supplier;
    private String warrantyPeriod;
    private String unit;
    private Integer quantity;
    private String remark;
}
