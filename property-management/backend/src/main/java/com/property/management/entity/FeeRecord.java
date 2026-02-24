package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_fee_record")
public class FeeRecord extends BaseEntity {
    private String feeNo;
    private Long userId;
    private String userName;
    private Long projectId;
    private String projectName;
    private Long houseId;
    private String houseInfo;
    private String feeType;
    private String feeName;
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private BigDecimal unpaidAmount;
    private String billingPeriod;
    private String dueDate;
    private String payDate;
    private String payMethod;
    private String status;
    private String remark;
}
