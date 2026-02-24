package com.property.management.controller;

import com.property.management.common.PageResult;
import com.property.management.common.Result;
import com.property.management.entity.User;
import com.property.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> loginData) {
        try {
            String phone = loginData.get("phone");
            String password = loginData.get("password");
            User user = userService.login(phone, password);
            return Result.success("登录成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        try {
            return Result.success("注册成功", userService.register(user));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/page")
    public Result<PageResult<User>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) String status) {
        return Result.success(userService.selectPage(pageNum, pageSize, userName, phone, userType, status));
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.selectById(id));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody User user) {
        return Result.success(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(userService.deleteUser(id));
    }

    @PutMapping("/password")
    public Result<Boolean> updatePassword(@RequestParam Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        try {
            return Result.success(userService.updatePassword(id, oldPassword, newPassword));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/resetPassword/{id}")
    public Result<Boolean> resetPassword(@PathVariable Long id) {
        return Result.success(userService.resetPassword(id));
    }

    @GetMapping("/phone/{phone}")
    public Result<User> getByPhone(@PathVariable String phone) {
        return Result.success(userService.selectByPhone(phone));
    }

    @GetMapping("/statistics")
    public Result<List<Map<String, Object>>> statistics() {
        return Result.success(userService.getUserStatistics());
    }

    @GetMapping("/feeStatistics/{userId}")
    public Result<List<Map<String, Object>>> feeStatistics(@PathVariable Long userId) {
        return Result.success(userService.getUserFeeStatistics(userId));
    }

    @PutMapping("/status/{id}")
    public Result<Boolean> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(userService.updateStatus(id, status));
    }
}
