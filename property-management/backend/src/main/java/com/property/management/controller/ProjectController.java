package com.property.management.controller;

import com.property.management.common.PageResult;
import com.property.management.common.Result;
import com.property.management.entity.Project;
import com.property.management.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/page")
    public Result<PageResult<Project>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String projectType,
            @RequestParam(required = false) String status) {
        return Result.success(projectService.selectPage(pageNum, pageSize, projectName, projectType, status));
    }

    @GetMapping("/{id}")
    public Result<Project> getById(@PathVariable Long id) {
        return Result.success(projectService.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Project project) {
        return Result.success(projectService.addProject(project));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Project project) {
        return Result.success(projectService.updateProject(project));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(projectService.deleteProject(id));
    }

    @GetMapping("/all")
    public Result<List<Project>> all() {
        return Result.success(projectService.selectAll());
    }

    @GetMapping("/statistics/{projectId}")
    public Result<Map<String, Object>> statistics(@PathVariable Long projectId) {
        return Result.success(projectService.getProjectStatistics(projectId));
    }

    @GetMapping("/list")
    public Result<List<Map<String, Object>>> list() {
        return Result.success(projectService.getProjectList());
    }
}
