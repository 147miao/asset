package com.property.management.controller;

import com.property.management.common.PageResult;
import com.property.management.common.Result;
import com.property.management.entity.ServiceAppointment;
import com.property.management.service.ServiceAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/service")
public class ServiceAppointmentController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @GetMapping("/page")
    public Result<PageResult<ServiceAppointment>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String serviceType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId) {
        return Result.success(serviceAppointmentService.selectPage(pageNum, pageSize, userName, serviceType, status, projectId));
    }

    @GetMapping("/{id}")
    public Result<ServiceAppointment> getById(@PathVariable Long id) {
        return Result.success(serviceAppointmentService.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody ServiceAppointment appointment) {
        return Result.success(serviceAppointmentService.addAppointment(appointment));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody ServiceAppointment appointment) {
        return Result.success(serviceAppointmentService.updateAppointment(appointment));
    }

    @PutMapping("/cancel/{id}")
    public Result<Boolean> cancel(@PathVariable Long id) {
        return Result.success(serviceAppointmentService.cancelAppointment(id));
    }

    @PutMapping("/complete/{id}")
    public Result<Boolean> complete(@PathVariable Long id, @RequestParam(required = false) String remark) {
        return Result.success(serviceAppointmentService.completeAppointment(id, remark));
    }

    @PutMapping("/rate/{id}")
    public Result<Boolean> rate(@PathVariable Long id, @RequestParam Integer rating, @RequestParam(required = false) String feedback) {
        return Result.success(serviceAppointmentService.rateAppointment(id, rating, feedback));
    }

    @GetMapping("/user/{userId}")
    public Result<List<ServiceAppointment>> getByUserId(@PathVariable Long userId) {
        return Result.success(serviceAppointmentService.selectByUserId(userId));
    }

    @GetMapping("/statistics")
    public Result<List<Map<String, Object>>> statistics(@RequestParam(required = false) Long projectId) {
        return Result.success(serviceAppointmentService.getServiceStatistics(projectId));
    }

    @PutMapping("/assign/{id}")
    public Result<Boolean> assign(@PathVariable Long id, @RequestParam Long assigneeId) {
        return Result.success(serviceAppointmentService.assignService(id, assigneeId));
    }
}
