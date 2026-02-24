package com.property.management.service;

import com.property.management.entity.Repair;
import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.common.PageResult;

import java.util.List;
import java.util.Map;

public interface RepairService extends IService<Repair> {
    PageResult<Repair> selectPage(Integer pageNum, Integer pageSize, String userName, String repairType, String status, Long projectId);

    Repair selectById(Long id);

    boolean addRepair(Repair repair);

    boolean updateRepair(Repair repair);

    boolean cancelRepair(Long id);

    boolean assignRepair(Long id, Long assigneeId, String assigneeName);

    boolean completeRepair(Long id, String result);

    boolean rateRepair(Long id, Integer rating, String feedback);

    List<Repair> selectByUserId(Long userId);

    List<Map<String, Object>> getRepairStatistics(Long projectId);

    default boolean isValidRepairType(String repairType) {
        if (repairType == null) {
            return false;
        }
        return repairType.matches("^(water|electricity|appliance|other)$");
    }

    default boolean isValidStatus(String status) {
        if (status == null) {
            return false;
        }
        return status.matches("^(pending|processing|completed|cancelled)$");
    }
}
