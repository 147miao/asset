package com.property.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.property.management.entity.FeeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface FeeRecordMapper extends BaseMapper<FeeRecord> {
    List<Map<String, Object>> selectIncomeStatistics(@Param("projectId") Long projectId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<Map<String, Object>> selectExpenseStatistics(@Param("projectId") Long projectId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<Map<String, Object>> selectArrearsStatistics(@Param("projectId") Long projectId);

    BigDecimal selectTotalArrears(@Param("projectId") Long projectId);

    List<Map<String, Object>> selectFeeTypeStatistics(@Param("projectId") Long projectId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
