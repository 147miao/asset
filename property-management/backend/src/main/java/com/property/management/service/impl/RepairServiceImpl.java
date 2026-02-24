package com.property.management.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.common.PageResult;
import com.property.management.entity.Repair;
import com.property.management.mapper.RepairMapper;
import com.property.management.service.MessageService;
import com.property.management.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {

    @Autowired
    private MessageService messageService;

    @Override
    public PageResult<Repair> selectPage(Integer pageNum, Integer pageSize, String userName, String repairType, String status, Long projectId) {
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(userName), Repair::getUserName, userName)
                .eq(StrUtil.isNotBlank(repairType), Repair::getRepairType, repairType)
                .eq(StrUtil.isNotBlank(status), Repair::getStatus, status)
                .eq(projectId != null, Repair::getProjectId, projectId)
                .eq(Repair::getDeleted, 0)
                .orderByDesc(Repair::getCreateTime);
        Page<Repair> page = page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public Repair selectById(Long id) {
        return getOne(new LambdaQueryWrapper<Repair>()
                .eq(Repair::getId, id)
                .eq(Repair::getDeleted, 0));
    }

    @Override
    public boolean addRepair(Repair repair) {
        if (StrUtil.isBlank(repair.getRepairNo())) {
            repair.setRepairNo("REP" + IdUtil.getSnowflakeNextIdStr());
        }
        repair.setStatus("pending");
        boolean result = save(repair);
        if (result) {
            messageService.sendMessage(
                    "报修申请已提交",
                    "您的报修申请已提交，我们将尽快处理。报修内容：" + repair.getDescription(),
                    "repair",
                    null,
                    repair.getUserId(),
                    repair.getProjectId(),
                    "repair",
                    repair.getId()
            );
        }
        return result;
    }

    @Override
    public boolean updateRepair(Repair repair) {
        return updateById(repair);
    }

    @Override
    public boolean cancelRepair(Long id) {
        Repair repair = new Repair();
        repair.setId(id);
        repair.setStatus("cancelled");
        return updateById(repair);
    }

    @Override
    public boolean assignRepair(Long id, Long assigneeId, String assigneeName) {
        Repair repair = new Repair();
        repair.setId(id);
        repair.setAssigneeId(String.valueOf(assigneeId));
        repair.setAssigneeName(assigneeName);
        repair.setStatus("processing");
        repair.setAssignDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Repair originalRepair = selectById(id);
        boolean result = updateById(repair);
        if (result && originalRepair != null) {
            messageService.sendMessage(
                    "报修进度更新",
                    "您的报修已分配给维修人员：" + assigneeName + "，请保持联系方式畅通。",
                    "repair",
                    null,
                    originalRepair.getUserId(),
                    originalRepair.getProjectId(),
                    "repair",
                    id
            );
        }
        return result;
    }

    @Override
    public boolean completeRepair(Long id, String result) {
        Repair repair = new Repair();
        repair.setId(id);
        repair.setStatus("completed");
        repair.setResult(result);
        repair.setCompleteDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Repair originalRepair = selectById(id);
        boolean updateResult = updateById(repair);
        if (updateResult && originalRepair != null) {
            messageService.sendMessage(
                    "报修完成通知",
                    "您的报修已完成，维修结果：" + result,
                    "repair",
                    null,
                    originalRepair.getUserId(),
                    originalRepair.getProjectId(),
                    "repair",
                    id
            );
        }
        return updateResult;
    }

    @Override
    public boolean rateRepair(Long id, Integer rating, String feedback) {
        Repair repair = new Repair();
        repair.setId(id);
        repair.setRating(rating);
        repair.setFeedback(feedback);
        return updateById(repair);
    }

    @Override
    public List<Repair> selectByUserId(Long userId) {
        return list(new LambdaQueryWrapper<Repair>()
                .eq(Repair::getUserId, userId)
                .eq(Repair::getDeleted, 0)
                .orderByDesc(Repair::getCreateTime));
    }

    @Override
    public List<Map<String, Object>> getRepairStatistics(Long projectId) {
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(projectId != null, Repair::getProjectId, projectId)
                .eq(Repair::getDeleted, 0);
        List<Repair> repairs = list(wrapper);
        return repairs.stream()
                .collect(Collectors.groupingBy(Repair::getStatus))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("status", entry.getKey());
                    map.put("count", entry.getValue().size());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
