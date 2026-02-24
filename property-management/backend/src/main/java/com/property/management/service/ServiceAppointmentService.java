package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.common.PageResult;
import com.property.management.entity.ServiceAppointment;

import java.util.List;
import java.util.Map;

public interface ServiceAppointmentService extends IService<ServiceAppointment> {
    PageResult<ServiceAppointment> selectPage(Integer pageNum, Integer pageSize, String userName, String serviceType, String status, Long projectId);

    ServiceAppointment selectById(Long id);

    boolean addAppointment(ServiceAppointment appointment);

    boolean updateAppointment(ServiceAppointment appointment);

    boolean cancelAppointment(Long id);

    boolean completeAppointment(Long id, String remark);

    boolean rateAppointment(Long id, Integer rating, String feedback);

    List<ServiceAppointment> selectByUserId(Long userId);

    List<Map<String, Object>> getServiceStatistics(Long projectId);

    boolean assignService(Long id, Long assigneeId);
}
