package com.property.management.controller;

import com.property.management.common.PageResult;
import com.property.management.common.Result;
import com.property.management.entity.Repair;
import com.property.management.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repair")
public class RepairController {

    @Autowired
    private RepairService repairService;

    @GetMapping("/page")
    public Result<PageResult<Repair>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String repairType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId) {
        return Result.success(repairService.selectPage(pageNum, pageSize, userName, repairType, status, projectId));
    }

    @GetMapping("/{id}")
    public Result<Repair> getById(@PathVariable Long id) {
        return Result.success(repairService.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Repair repair) {
        return Result.success(repairService.addRepair(repair));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Repair repair) {
        return Result.success(repairService.updateRepair(repair));
    }

    @PutMapping("/cancel/{id}")
    public Result<Boolean> cancel(@PathVariable Long id) {
        return Result.success(repairService.cancelRepair(id));
    }

    @PutMapping("/assign/{id}")
    public Result<Boolean> assign(@PathVariable Long id, @RequestParam Long assigneeId, @RequestParam String assigneeName) {
        return Result.success(repairService.assignRepair(id, assigneeId, assigneeName));
    }

    @PutMapping("/complete/{id}")
    public Result<Boolean> complete(@PathVariable Long id, @RequestParam String result) {
        return Result.success(repairService.completeRepair(id, result));
    }

    @PutMapping("/rate/{id}")
    public Result<Boolean> rate(@PathVariable Long id, @RequestParam Integer rating, @RequestParam(required = false) String feedback) {
        return Result.success(repairService.rateRepair(id, rating, feedback));
    }

    @GetMapping("/user/{userId}")
    public Result<List<Repair>> getByUserId(@PathVariable Long userId) {
        return Result.success(repairService.selectByUserId(userId));
    }

    @GetMapping("/statistics")
    public Result<List<Map<String, Object>>> statistics(@RequestParam(required = false) Long projectId) {
        return Result.success(repairService.getRepairStatistics(projectId));
    }
}
