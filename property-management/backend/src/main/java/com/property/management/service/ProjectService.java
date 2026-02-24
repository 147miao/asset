package com.property.management.service;

import com.property.management.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.common.PageResult;

import java.util.List;
import java.util.Map;

public interface ProjectService extends IService<Project> {
    PageResult<Project> selectPage(Integer pageNum, Integer pageSize, String projectName, String projectType, String status);

    Project selectById(Long id);

    boolean addProject(Project project);

    boolean updateProject(Project project);

    boolean deleteProject(Long id);

    List<Project> selectAll();

    Map<String, Object> getProjectStatistics(Long projectId);

    List<Map<String, Object>> getProjectList();

    default boolean isValidProjectType(String projectType) {
        if (projectType == null) {
            return false;
        }
        return projectType.matches("^(residential|commercial|industrial)$");
    }

    default boolean isValidStatus(String status) {
        if (status == null) {
            return false;
        }
        return status.matches("^(operating|pending|maintenance)$");
    }

    default boolean isValidBuildingArea(Integer area) {
        return area != null && area > 0;
    }

    default boolean isValidBuildingCount(Integer count) {
        return count != null && count >= 0;
    }
}
