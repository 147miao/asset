package com.property.management.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.common.PageResult;
import com.property.management.entity.ServiceAppointment;
import com.property.management.mapper.ServiceAppointmentMapper;
import com.property.management.service.MessageService;
import com.property.management.service.ServiceAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceAppointmentServiceImpl extends ServiceImpl<ServiceAppointmentMapper, ServiceAppointment> implements ServiceAppointmentService {

    @Autowired
    private MessageService messageService;

    @Override
    public PageResult<ServiceAppointment> selectPage(Integer pageNum, Integer pageSize, String userName, String serviceType, String status, Long projectId) {
        LambdaQueryWrapper<ServiceAppointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(userName), ServiceAppointment::getUserName, userName)
                .eq(StrUtil.isNotBlank(serviceType), ServiceAppointment::getServiceType, serviceType)
                .eq(StrUtil.isNotBlank(status), ServiceAppointment::getStatus, status)
                .eq(projectId != null, ServiceAppointment::getProjectId, projectId)
                .eq(ServiceAppointment::getDeleted, 0)
                .orderByDesc(ServiceAppointment::getCreateTime);
        Page<ServiceAppointment> page = page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public ServiceAppointment selectById(Long id) {
        return getOne(new LambdaQueryWrapper<ServiceAppointment>()
                .eq(ServiceAppointment::getId, id)
                .eq(ServiceAppointment::getDeleted, 0));
    }

    @Override
    public boolean addAppointment(ServiceAppointment appointment) {
        if (StrUtil.isBlank(appointment.getAppointmentNo())) {
            appointment.setAppointmentNo("SVC" + IdUtil.getSnowflakeNextIdStr());
        }
        appointment.setStatus("pending");
        boolean result = save(appointment);
        if (result) {
            messageService.sendMessage(
                    "服务预约成功",
                    "您已成功预约" + appointment.getServiceName() + "服务，预约时间：" + appointment.getAppointmentDate() + " " + appointment.getAppointmentTime(),
                    "service",
                    null,
                    appointment.getUserId(),
                    appointment.getProjectId(),
                    "service",
                    appointment.getId()
            );
        }
        return result;
    }

    @Override
    public boolean updateAppointment(ServiceAppointment appointment) {
        return updateById(appointment);
    }

    @Override
    public boolean cancelAppointment(Long id) {
        ServiceAppointment appointment = new ServiceAppointment();
        appointment.setId(id);
        appointment.setStatus("cancelled");
        return updateById(appointment);
    }

    @Override
    public boolean completeAppointment(Long id, String remark) {
        ServiceAppointment appointment = new ServiceAppointment();
        appointment.setId(id);
        appointment.setStatus("completed");
        appointment.setRemark(remark);
        appointment.setCompleteDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return updateById(appointment);
    }

    @Override
    public boolean rateAppointment(Long id, Integer rating, String feedback) {
        ServiceAppointment appointment = new ServiceAppointment();
        appointment.setId(id);
        appointment.setRating(rating);
        appointment.setFeedback(feedback);
        appointment.setStatus("rated");
        return updateById(appointment);
    }

    @Override
    public List<ServiceAppointment> selectByUserId(Long userId) {
        return list(new LambdaQueryWrapper<ServiceAppointment>()
                .eq(ServiceAppointment::getUserId, userId)
                .eq(ServiceAppointment::getDeleted, 0)
                .orderByDesc(ServiceAppointment::getCreateTime));
    }

    @Override
    public List<Map<String, Object>> getServiceStatistics(Long projectId) {
        LambdaQueryWrapper<ServiceAppointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(projectId != null, ServiceAppointment::getProjectId, projectId)
                .eq(ServiceAppointment::getDeleted, 0);
        List<ServiceAppointment> appointments = list(wrapper);
        return appointments.stream()
                .collect(Collectors.groupingBy(ServiceAppointment::getServiceType))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("serviceType", entry.getKey());
                    map.put("count", entry.getValue().size());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean assignService(Long id, Long assigneeId) {
        ServiceAppointment appointment = new ServiceAppointment();
        appointment.setId(id);
        appointment.setStatus("assigned");
        return updateById(appointment);
    }
}
