package com.property.management.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.common.PageResult;
import com.property.management.entity.Project;
import com.property.management.mapper.ProjectMapper;
import com.property.management.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Override
    public PageResult<Project> selectPage(Integer pageNum, Integer pageSize, String projectName, String projectType, String status) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(projectName), Project::getProjectName, projectName)
                .eq(StrUtil.isNotBlank(projectType), Project::getProjectType, projectType)
                .eq(StrUtil.isNotBlank(status), Project::getStatus, status)
                .eq(Project::getDeleted, 0)
                .orderByDesc(Project::getCreateTime);
        Page<Project> page = page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public Project selectById(Long id) {
        return getOne(new LambdaQueryWrapper<Project>()
                .eq(Project::getId, id)
                .eq(Project::getDeleted, 0));
    }

    @Override
    public boolean addProject(Project project) {
        return save(project);
    }

    @Override
    public boolean updateProject(Project project) {
        return updateById(project);
    }

    @Override
    public boolean deleteProject(Long id) {
        Project project = new Project();
        project.setId(id);
        project.setDeleted(1);
        return updateById(project);
    }

    @Override
    public List<Project> selectAll() {
        return list(new LambdaQueryWrapper<Project>()
                .eq(Project::getDeleted, 0)
                .orderByDesc(Project::getCreateTime));
    }

    @Override
    public Map<String, Object> getProjectStatistics(Long projectId) {
        Map<String, Object> result = new HashMap<>();
        if (projectId != null) {
            Project project = selectById(projectId);
            if (project != null) {
                result.put("project", project);
                result.put("equipmentOnlineRate", project.getEquipmentOnlineRate());
                result.put("complaintHandlingRate", project.getComplaintHandlingRate());
            }
        } else {
            List<Project> projects = selectAll();
            result.put("totalProjects", projects.size());
            Map<String, Long> statusCount = projects.stream()
                    .collect(Collectors.groupingBy(Project::getStatus, Collectors.counting()));
            result.put("statusCount", statusCount);
            Map<String, Long> typeCount = projects.stream()
                    .collect(Collectors.groupingBy(Project::getProjectType, Collectors.counting()));
            result.put("typeCount", typeCount);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getProjectList() {
        return selectAll().stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", p.getId());
                    map.put("projectName", p.getProjectName());
                    map.put("projectType", p.getProjectType());
                    map.put("status", p.getStatus());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
