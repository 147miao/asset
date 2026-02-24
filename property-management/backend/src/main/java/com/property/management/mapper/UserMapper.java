package com.property.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.management.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User selectByPhone(@Param("phone") String phone);

    List<Map<String, Object>> selectUserStatistics();

    List<Map<String, Object>> selectUserFeeStatistics(@Param("userId") Long userId);
}
