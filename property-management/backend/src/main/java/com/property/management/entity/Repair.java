package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_repair")
public class Repair extends BaseEntity {
    private String repairNo;
    private Long userId;
    private String userName;
    private String userPhone;
    private Long projectId;
    private String projectName;
    private Long houseId;
    private String houseInfo;
    private String repairType;
    private String description;
    private String images;
    private String status;
    private String assigneeId;
    private String assigneeName;
    private String assignDate;
    private String completeDate;
    private String result;
    private String feedback;
    private Integer rating;
}
