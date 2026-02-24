package com.property.management.controller;

import com.property.management.common.PageResult;
import com.property.management.common.Result;
import com.property.management.entity.FeeRecord;
import com.property.management.service.FeeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fee")
public class FeeRecordController {

    @Autowired
    private FeeRecordService feeRecordService;

    @GetMapping("/page")
    public Result<PageResult<FeeRecord>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String feeType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId) {
        return Result.success(feeRecordService.selectPage(pageNum, pageSize, userName, feeType, status, projectId));
    }

    @GetMapping("/{id}")
    public Result<FeeRecord> getById(@PathVariable Long id) {
        return Result.success(feeRecordService.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody FeeRecord feeRecord) {
        return Result.success(feeRecordService.addFeeRecord(feeRecord));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody FeeRecord feeRecord) {
        return Result.success(feeRecordService.updateFeeRecord(feeRecord));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(feeRecordService.deleteFeeRecord(id));
    }

    @PostMapping("/pay/{id}")
    public Result<Boolean> pay(@PathVariable Long id, @RequestParam String payMethod) {
        return Result.success(feeRecordService.payFee(id, payMethod));
    }

    @GetMapping("/user/{userId}")
    public Result<List<FeeRecord>> getByUserId(@PathVariable Long userId) {
        return Result.success(feeRecordService.selectByUserId(userId));
    }

    @GetMapping("/unpaid/{userId}")
    public Result<List<FeeRecord>> getUnpaidByUserId(@PathVariable Long userId) {
        return Result.success(feeRecordService.selectUnpaidByUserId(userId));
    }

    @GetMapping("/incomeStatistics")
    public Result<Map<String, Object>> incomeStatistics(
            @RequestParam(required = false) Long projectId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return Result.success(feeRecordService.getIncomeStatistics(projectId, startDate, endDate));
    }

    @GetMapping("/expenseStatistics")
    public Result<Map<String, Object>> expenseStatistics(
            @RequestParam(required = false) Long projectId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return Result.success(feeRecordService.getExpenseStatistics(projectId, startDate, endDate));
    }

    @GetMapping("/arrearsStatistics")
    public Result<List<Map<String, Object>>> arrearsStatistics(@RequestParam(required = false) Long projectId) {
        return Result.success(feeRecordService.getArrearsStatistics(projectId));
    }

    @GetMapping("/totalArrears")
    public Result<BigDecimal> totalArrears(@RequestParam(required = false) Long projectId) {
        return Result.success(feeRecordService.getTotalArrears(projectId));
    }

    @GetMapping("/feeTypeStatistics")
    public Result<List<Map<String, Object>>> feeTypeStatistics(
            @RequestParam(required = false) Long projectId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return Result.success(feeRecordService.getFeeTypeStatistics(projectId, startDate, endDate));
    }

    @PostMapping("/autoGenerate")
    public Result<Boolean> autoGenerate(
            @RequestParam Long projectId,
            @RequestParam String feeType,
            @RequestParam String billingPeriod) {
        return Result.success(feeRecordService.autoGenerateFee(projectId, feeType, billingPeriod));
    }

    @GetMapping("/report")
    public Result<Map<String, Object>> report(
            @RequestParam(required = false) Long projectId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "day") String dimension) {
        return Result.success(feeRecordService.getReport(projectId, startDate, endDate, dimension));
    }
}
