package com.property.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.management.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> selectUnreadByUserId(@Param("userId") Long userId);

    Integer countUnreadByUserId(@Param("userId") Long userId);
}
