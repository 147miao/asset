package com.property.management.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.common.PageResult;
import com.property.management.entity.FeeRecord;
import com.property.management.entity.FeeStandard;
import com.property.management.entity.House;
import com.property.management.mapper.FeeRecordMapper;
import com.property.management.service.FeeRecordService;
import com.property.management.service.FeeStandardService;
import com.property.management.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeeRecordServiceImpl extends ServiceImpl<FeeRecordMapper, FeeRecord> implements FeeRecordService {

    @Autowired
    private FeeStandardService feeStandardService;

    @Autowired
    private HouseService houseService;

    @Override
    public PageResult<FeeRecord> selectPage(Integer pageNum, Integer pageSize, String userName, String feeType, String status, Long projectId) {
        LambdaQueryWrapper<FeeRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(userName), FeeRecord::getUserName, userName)
                .eq(StrUtil.isNotBlank(feeType), FeeRecord::getFeeType, feeType)
                .eq(StrUtil.isNotBlank(status), FeeRecord::getStatus, status)
                .eq(projectId != null, FeeRecord::getProjectId, projectId)
                .eq(FeeRecord::getDeleted, 0)
                .orderByDesc(FeeRecord::getCreateTime);
        Page<FeeRecord> page = page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public FeeRecord selectById(Long id) {
        return getOne(new LambdaQueryWrapper<FeeRecord>()
                .eq(FeeRecord::getId, id)
                .eq(FeeRecord::getDeleted, 0));
    }

    @Override
    public boolean addFeeRecord(FeeRecord feeRecord) {
        if (StrUtil.isBlank(feeRecord.getFeeNo())) {
            feeRecord.setFeeNo("FEE" + IdUtil.getSnowflakeNextIdStr());
        }
        feeRecord.setUnpaidAmount(feeRecord.getAmount().subtract(feeRecord.getPaidAmount() != null ? feeRecord.getPaidAmount() : BigDecimal.ZERO));
        if (feeRecord.getUnpaidAmount().compareTo(BigDecimal.ZERO) == 0) {
            feeRecord.setStatus("paid");
        } else {
            feeRecord.setStatus("unpaid");
        }
        return save(feeRecord);
    }

    @Override
    public boolean updateFeeRecord(FeeRecord feeRecord) {
        return updateById(feeRecord);
    }

    @Override
    public boolean deleteFeeRecord(Long id) {
        FeeRecord feeRecord = new FeeRecord();
        feeRecord.setId(id);
        feeRecord.setDeleted(1);
        return updateById(feeRecord);
    }

    @Override
    public boolean payFee(Long id, String payMethod) {
        FeeRecord feeRecord = selectById(id);
        if (feeRecord == null) {
            throw new RuntimeException("费用记录不存在");
        }
        feeRecord.setPaidAmount(feeRecord.getAmount());
        feeRecord.setUnpaidAmount(BigDecimal.ZERO);
        feeRecord.setStatus("paid");
        feeRecord.setPayMethod(payMethod);
        feeRecord.setPayDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return updateById(feeRecord);
    }

    @Override
    public List<FeeRecord> selectByUserId(Long userId) {
        return list(new LambdaQueryWrapper<FeeRecord>()
                .eq(FeeRecord::getUserId, userId)
                .eq(FeeRecord::getDeleted, 0)
                .orderByDesc(FeeRecord::getCreateTime));
    }

    @Override
    public List<FeeRecord> selectUnpaidByUserId(Long userId) {
        return list(new LambdaQueryWrapper<FeeRecord>()
                .eq(FeeRecord::getUserId, userId)
                .eq(FeeRecord::getStatus, "unpaid")
                .eq(FeeRecord::getDeleted, 0)
                .orderByAsc(FeeRecord::getDueDate));
    }

    @Override
    public Map<String, Object> getIncomeStatistics(Long projectId, String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = baseMapper.selectIncomeStatistics(projectId, startDate, endDate);
        result.put("data", data);
        BigDecimal total = data.stream()
                .map(m -> (BigDecimal) m.get("amount"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("total", total);
        return result;
    }

    @Override
    public Map<String, Object> getExpenseStatistics(Long projectId, String startDate, String endDate) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = baseMapper.selectExpenseStatistics(projectId, startDate, endDate);
        result.put("data", data);
        BigDecimal total = data.stream()
                .map(m -> (BigDecimal) m.get("amount"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("total", total);
        return result;
    }

    @Override
    public List<Map<String, Object>> getArrearsStatistics(Long projectId) {
        return baseMapper.selectArrearsStatistics(projectId);
    }

    @Override
    public BigDecimal getTotalArrears(Long projectId) {
        return baseMapper.selectTotalArrears(projectId);
    }

    @Override
    public List<Map<String, Object>> getFeeTypeStatistics(Long projectId, String startDate, String endDate) {
        return baseMapper.selectFeeTypeStatistics(projectId, startDate, endDate);
    }

    @Override
    public boolean autoGenerateFee(Long projectId, String feeType, String billingPeriod) {
        List<FeeStandard> standards = feeStandardService.selectByFeeType(projectId, feeType);
        List<House> houses = houseService.selectByProjectId(projectId);
        for (House house : houses) {
            if (house.getOwnerId() != null) {
                for (FeeStandard standard : standards) {
                    FeeRecord record = new FeeRecord();
                    record.setFeeNo("FEE" + IdUtil.getSnowflakeNextIdStr());
                    record.setUserId(house.getOwnerId());
                    record.setUserName(house.getOwnerName());
                    record.setProjectId(projectId);
                    record.setProjectName(house.getProjectName());
                    record.setHouseId(house.getId());
                    record.setHouseInfo(house.getBuildingNo() + "-" + house.getUnitNo() + "-" + house.getRoomNo());
                    record.setFeeType(standard.getFeeType());
                    record.setFeeName(standard.getFeeName());
                    BigDecimal amount = standard.getUnitPrice().multiply(BigDecimal.valueOf(house.getBuildingArea()));
                    record.setAmount(amount);
                    record.setPaidAmount(BigDecimal.ZERO);
                    record.setUnpaidAmount(amount);
                    record.setBillingPeriod(billingPeriod);
                    record.setStatus("unpaid");
                    LocalDate dueDate = LocalDate.now().plusMonths(1);
                    record.setDueDate(dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    save(record);
                }
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> getReport(Long projectId, String startDate, String endDate, String dimension) {
        Map<String, Object> result = new HashMap<>();
        result.put("income", getIncomeStatistics(projectId, startDate, endDate));
        result.put("expense", getExpenseStatistics(projectId, startDate, endDate));
        result.put("arrears", getArrearsStatistics(projectId));
        result.put("totalArrears", getTotalArrears(projectId));
        result.put("feeTypeStatistics", getFeeTypeStatistics(projectId, startDate, endDate));
        return result;
    }
}
