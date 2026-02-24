package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_fee_standard")
public class FeeStandard extends BaseEntity {
    private Long projectId;
    private String projectName;
    private String feeType;
    private String feeName;
    private BigDecimal unitPrice;
    private String unit;
    private String calculationMethod;
    private String billingCycle;
    private String startDate;
    private String endDate;
    private String status;
    private String remark;
}
