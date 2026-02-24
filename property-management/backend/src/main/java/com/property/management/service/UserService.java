package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.common.PageResult;
import com.property.management.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    User login(String phone, String password);

    User register(User user);

    PageResult<User> selectPage(Integer pageNum, Integer pageSize, String userName, String phone, String userType, String status);

    User selectById(Long id);

    boolean updateUser(User user);

    boolean deleteUser(Long id);

    boolean updatePassword(Long id, String oldPassword, String newPassword);

    boolean resetPassword(Long id);

    User selectByPhone(String phone);

    List<Map<String, Object>> getUserStatistics();

    List<Map<String, Object>> getUserFeeStatistics(Long userId);

    boolean updateStatus(Long id, String status);

    default boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return phone.matches("^1[3-9]\\d{9}$");
    }

    default boolean isValidIdCard(String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            return false;
        }
        return idCard.matches("^\\d{17}[\\dXx]$");
    }
}
