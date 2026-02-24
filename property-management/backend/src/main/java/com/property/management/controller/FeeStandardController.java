package com.property.management.controller;

import com.property.management.common.PageResult;
import com.property.management.common.Result;
import com.property.management.entity.FeeStandard;
import com.property.management.service.FeeStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feeStandard")
public class FeeStandardController {

    @Autowired
    private FeeStandardService feeStandardService;

    @GetMapping("/page")
    public Result<PageResult<FeeStandard>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String feeName,
            @RequestParam(required = false) String feeType,
            @RequestParam(required = false) Long projectId) {
        return Result.success(feeStandardService.selectPage(pageNum, pageSize, feeName, feeType, projectId));
    }

    @GetMapping("/{id}")
    public Result<FeeStandard> getById(@PathVariable Long id) {
        return Result.success(feeStandardService.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody FeeStandard feeStandard) {
        return Result.success(feeStandardService.addFeeStandard(feeStandard));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody FeeStandard feeStandard) {
        return Result.success(feeStandardService.updateFeeStandard(feeStandard));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(feeStandardService.deleteFeeStandard(id));
    }

    @GetMapping("/project/{projectId}")
    public Result<List<FeeStandard>> getByProjectId(@PathVariable Long projectId) {
        return Result.success(feeStandardService.selectByProjectId(projectId));
    }

    @GetMapping("/feeType")
    public Result<List<FeeStandard>> getByFeeType(@RequestParam Long projectId, @RequestParam String feeType) {
        return Result.success(feeStandardService.selectByFeeType(projectId, feeType));
    }
}
