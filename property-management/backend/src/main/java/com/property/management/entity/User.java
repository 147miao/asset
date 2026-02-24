package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_user")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private String idCard;
    private String userType;
    private Long projectId;
    private String projectName;
    private Long houseId;
    private String houseInfo;
    private String leaseStartDate;
    private String leaseEndDate;
    private String status;
    private String avatar;
    private String address;
    private String remark;
}
