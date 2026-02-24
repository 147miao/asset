package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.common.PageResult;
import com.property.management.entity.Message;

import java.util.List;

public interface MessageService extends IService<Message> {
    PageResult<Message> selectPage(Integer pageNum, Integer pageSize, Long userId, String messageType, String status);

    Message selectById(Long id);

    boolean addMessage(Message message);

    boolean sendMessage(String title, String content, String messageType, Long senderId, Long receiverId, Long projectId, String relatedType, Long relatedId);

    boolean sendBroadcastMessage(String title, String content, String messageType, Long projectId);

    boolean markAsRead(Long id);

    boolean markAllAsRead(Long userId);

    List<Message> selectUnreadByUserId(Long userId);

    Integer countUnreadByUserId(Long userId);

    boolean deleteMessage(Long id);
}
