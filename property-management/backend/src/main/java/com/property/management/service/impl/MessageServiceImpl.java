package com.property.management.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.common.PageResult;
import com.property.management.entity.Message;
import com.property.management.mapper.MessageMapper;
import com.property.management.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public PageResult<Message> selectPage(Integer pageNum, Integer pageSize, Long userId, String messageType, String status) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, Message::getReceiverId, userId)
                .eq(StrUtil.isNotBlank(messageType), Message::getMessageType, messageType)
                .eq(StrUtil.isNotBlank(status), Message::getStatus, status)
                .eq(Message::getDeleted, 0)
                .orderByDesc(Message::getCreateTime);
        Page<Message> page = page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public Message selectById(Long id) {
        return getOne(new LambdaQueryWrapper<Message>()
                .eq(Message::getId, id)
                .eq(Message::getDeleted, 0));
    }

    @Override
    public boolean addMessage(Message message) {
        if (StrUtil.isBlank(message.getStatus())) {
            message.setStatus("unread");
        }
        return save(message);
    }

    @Override
    public boolean sendMessage(String title, String content, String messageType, Long senderId, Long receiverId, Long projectId, String relatedType, Long relatedId) {
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setProjectId(projectId);
        message.setRelatedType(relatedType);
        message.setRelatedId(relatedId);
        message.setStatus("unread");
        return save(message);
    }

    @Override
    public boolean sendBroadcastMessage(String title, String content, String messageType, Long projectId) {
        return true;
    }

    @Override
    public boolean markAsRead(Long id) {
        LambdaUpdateWrapper<Message> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Message::getId, id)
                .set(Message::getStatus, "read")
                .set(Message::getReadTime, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return update(wrapper);
    }

    @Override
    public boolean markAllAsRead(Long userId) {
        LambdaUpdateWrapper<Message> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Message::getReceiverId, userId)
                .eq(Message::getStatus, "unread")
                .set(Message::getStatus, "read")
                .set(Message::getReadTime, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return update(wrapper);
    }

    @Override
    public List<Message> selectUnreadByUserId(Long userId) {
        return baseMapper.selectUnreadByUserId(userId);
    }

    @Override
    public Integer countUnreadByUserId(Long userId) {
        return baseMapper.countUnreadByUserId(userId);
    }

    @Override
    public boolean deleteMessage(Long id) {
        Message message = new Message();
        message.setId(id);
        message.setDeleted(1);
        return updateById(message);
    }
}
