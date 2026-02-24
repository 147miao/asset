package com.property.management.controller;

import com.property.management.common.PageResult;
import com.property.management.common.Result;
import com.property.management.entity.Message;
import com.property.management.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/page")
    public Result<PageResult<Message>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String messageType,
            @RequestParam(required = false) String status) {
        return Result.success(messageService.selectPage(pageNum, pageSize, userId, messageType, status));
    }

    @GetMapping("/{id}")
    public Result<Message> getById(@PathVariable Long id) {
        return Result.success(messageService.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Message message) {
        return Result.success(messageService.addMessage(message));
    }

    @PostMapping("/send")
    public Result<Boolean> send(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String messageType,
            @RequestParam(required = false) Long senderId,
            @RequestParam Long receiverId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String relatedType,
            @RequestParam(required = false) Long relatedId) {
        return Result.success(messageService.sendMessage(title, content, messageType, senderId, receiverId, projectId, relatedType, relatedId));
    }

    @PostMapping("/broadcast")
    public Result<Boolean> broadcast(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String messageType,
            @RequestParam(required = false) Long projectId) {
        return Result.success(messageService.sendBroadcastMessage(title, content, messageType, projectId));
    }

    @PutMapping("/read/{id}")
    public Result<Boolean> markAsRead(@PathVariable Long id) {
        return Result.success(messageService.markAsRead(id));
    }

    @PutMapping("/readAll/{userId}")
    public Result<Boolean> markAllAsRead(@PathVariable Long userId) {
        return Result.success(messageService.markAllAsRead(userId));
    }

    @GetMapping("/unread/{userId}")
    public Result<List<Message>> getUnreadByUserId(@PathVariable Long userId) {
        return Result.success(messageService.selectUnreadByUserId(userId));
    }

    @GetMapping("/count/{userId}")
    public Result<Integer> countUnread(@PathVariable Long userId) {
        return Result.success(messageService.countUnreadByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(messageService.deleteMessage(id));
    }
}
