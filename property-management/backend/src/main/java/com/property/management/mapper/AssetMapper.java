package com.property.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.management.entity.Asset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AssetMapper extends BaseMapper<Asset> {
    @Update("UPDATE pm_asset SET deleted = 1 WHERE id = #{id}")
    int updateDeletedById(@Param("id") Long id);
}
