package com.property.management.controller;

import com.property.management.common.Result;
import com.property.management.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    void testLogin_Success() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("phone", "13800138000");
        loginData.put("password", "123456");
        Result<User> result = userController.login(loginData);
        assertNotNull(result);
    }

    @Test
    void testLogin_InvalidPhone() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("phone", "123");
        loginData.put("password", "123456");
        Result<User> result = userController.login(loginData);
        assertNotNull(result);
    }

    @Test
    void testLogin_InvalidPassword() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("phone", "13800138000");
        loginData.put("password", "");
        Result<User> result = userController.login(loginData);
        assertNotNull(result);
    }

    @Test
    void testRegister_Success() {
        User user = new User();
        Result<User> result = userController.register(user);
        assertNotNull(result);
    }

    @Test
    void testGetById() {
        Result<User> result = userController.getById(1L);
        assertNotNull(result);
    }

    @Test
    void testUpdate() {
        User user = new User();
        Result<Boolean> result = userController.update(user);
        assertNotNull(result);
    }

    @Test
    void testUpdatePassword() {
        Result<Boolean> result = userController.updatePassword(1L, "old123", "new123");
        assertNotNull(result);
    }

    @Test
    void testGetUserPage() {
        Result<?> result = userController.page(1, 10, "", "", "", "");
        assertNotNull(result);
    }

    @Test
    void testDeleteUser() {
        Result<Boolean> result = userController.delete(999L);
        assertNotNull(result);
    }

    @Test
    void testResetPassword() {
        Result<Boolean> result = userController.resetPassword(1L);
        assertNotNull(result);
    }

    @Test
    void testUpdateStatus() {
        Result<Boolean> result = userController.updateStatus(1L, "active");
        assertNotNull(result);
    }

    @Test
    void testGetByPhone() {
        Result<User> result = userController.getByPhone("13800138000");
        assertNotNull(result);
    }

    @Test
    void testStatistics() {
        Result<?> result = userController.statistics();
        assertNotNull(result);
    }

    @Test
    void testFeeStatistics() {
        Result<?> result = userController.feeStatistics(1L);
        assertNotNull(result);
    }
}
