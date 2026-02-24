package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_project")
public class Project extends BaseEntity {
    private String projectName;
    private String projectType;
    private String address;
    private Double buildingArea;
    private Integer buildingCount;
    private String businessDistribution;
    private String status;
    private Double equipmentOnlineRate;
    private Double complaintHandlingRate;
    private String description;
}
