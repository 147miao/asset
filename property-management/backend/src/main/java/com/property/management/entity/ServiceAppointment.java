package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_service_appointment")
public class ServiceAppointment extends BaseEntity {
    private String appointmentNo;
    private Long userId;
    private String userName;
    private String userPhone;
    private Long projectId;
    private String projectName;
    private Long houseId;
    private String houseInfo;
    private String serviceType;
    private String serviceName;
    private String appointmentDate;
    private String appointmentTime;
    private String address;
    private String description;
    private String status;
    private BigDecimal fee;
    private String completeDate;
    private String remark;
    private String feedback;
    private Integer rating;
}
