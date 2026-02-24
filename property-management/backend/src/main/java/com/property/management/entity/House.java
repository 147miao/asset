package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_house")
public class House extends BaseEntity {
    private Long projectId;
    private String projectName;
    private String buildingNo;
    private String unitNo;
    private String roomNo;
    private String houseType;
    private Double buildingArea;
    private Double usableArea;
    private String ownerName;
    private Long ownerId;
    private String ownerPhone;
    private String status;
    private String remark;
}
