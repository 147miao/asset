package com.property.management.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer deleted;
    private String createTime;
    private String updateTime;
}
