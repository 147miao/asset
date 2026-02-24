package com.property.management.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.common.PageResult;
import com.property.management.entity.User;
import com.property.management.mapper.UserMapper;
import com.property.management.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String phone, String password) {
        User user = baseMapper.selectByPhone(phone);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!DigestUtil.md5Hex(password).equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        if ("disabled".equals(user.getStatus())) {
            throw new RuntimeException("用户已被禁用");
        }
        return user;
    }

    @Override
    public User register(User user) {
        User existUser = baseMapper.selectByPhone(user.getPhone());
        if (existUser != null) {
            throw new RuntimeException("手机号已存在");
        }
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        user.setStatus("active");
        save(user);
        return user;
    }

    @Override
    public PageResult<User> selectPage(Integer pageNum, Integer pageSize, String userName, String phone, String userType, String status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(userName), User::getRealName, userName)
                .like(StrUtil.isNotBlank(phone), User::getPhone, phone)
                .eq(StrUtil.isNotBlank(userType), User::getUserType, userType)
                .eq(StrUtil.isNotBlank(status), User::getStatus, status)
                .eq(User::getDeleted, 0)
                .orderByDesc(User::getCreateTime);
        Page<User> page = page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public User selectById(Long id) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, id)
                .eq(User::getDeleted, 0));
    }

    @Override
    public boolean updateUser(User user) {
        return updateById(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setDeleted(1);
        return updateById(user);
    }

    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        User user = selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!DigestUtil.md5Hex(oldPassword).equals(user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setPassword(DigestUtil.md5Hex(newPassword));
        return updateById(updateUser);
    }

    @Override
    public boolean resetPassword(Long id) {
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setPassword(DigestUtil.md5Hex("123456"));
        return updateById(updateUser);
    }

    @Override
    public User selectByPhone(String phone) {
        return baseMapper.selectByPhone(phone);
    }

    @Override
    public List<Map<String, Object>> getUserStatistics() {
        return baseMapper.selectUserStatistics();
    }

    @Override
    public List<Map<String, Object>> getUserFeeStatistics(Long userId) {
        return baseMapper.selectUserFeeStatistics(userId);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        return updateById(user);
    }
}
