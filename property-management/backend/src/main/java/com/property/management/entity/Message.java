package com.property.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.property.management.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pm_message")
public class Message extends BaseEntity {
    private String title;
    private String content;
    private String messageType;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private Long projectId;
    private String projectName;
    private String relatedType;
    private Long relatedId;
    private String status;
    private String readTime;
}
