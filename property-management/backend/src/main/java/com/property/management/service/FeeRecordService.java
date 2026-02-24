package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.common.PageResult;
import com.property.management.entity.FeeRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FeeRecordService extends IService<FeeRecord> {
    PageResult<FeeRecord> selectPage(Integer pageNum, Integer pageSize, String userName, String feeType, String status, Long projectId);

    FeeRecord selectById(Long id);

    boolean addFeeRecord(FeeRecord feeRecord);

    boolean updateFeeRecord(FeeRecord feeRecord);

    boolean deleteFeeRecord(Long id);

    boolean payFee(Long id, String payMethod);

    List<FeeRecord> selectByUserId(Long userId);

    List<FeeRecord> selectUnpaidByUserId(Long userId);

    Map<String, Object> getIncomeStatistics(Long projectId, String startDate, String endDate);

    Map<String, Object> getExpenseStatistics(Long projectId, String startDate, String endDate);

    List<Map<String, Object>> getArrearsStatistics(Long projectId);

    BigDecimal getTotalArrears(Long projectId);

    List<Map<String, Object>> getFeeTypeStatistics(Long projectId, String startDate, String endDate);

    boolean autoGenerateFee(Long projectId, String feeType, String billingPeriod);

    Map<String, Object> getReport(Long projectId, String startDate, String endDate, String dimension);
}
