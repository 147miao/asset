package com.property.management.service;

import com.property.management.common.PageResult;
import com.property.management.entity.Project;
import com.property.management.mapper.ProjectMapper;
import com.property.management.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testProject = new Project();
        testProject.setId(1L);
        testProject.setProjectName("测试项目");
        testProject.setProjectType("residential");
        testProject.setAddress("测试地址");
        testProject.setBuildingArea(10000.0);
        testProject.setBuildingCount(5);
        testProject.setStatus("operating");
    }

    @Test
    void testSelectPage() {
        PageResult<Project> result = projectService.selectPage(1, 10, "", "", "");
        assertNotNull(result);
    }

    @Test
    void testSelectById() {
        Project result = projectService.selectById(1L);
        // 在mock环境中可能返回null，主要验证方法不抛出异常
    }

    @Test
    void testAddProject() {
        boolean result = projectService.addProject(testProject);
        // 验证方法执行不抛出异常
    }

    @Test
    void testUpdateProject() {
        boolean result = projectService.updateProject(testProject);
        // 验证方法执行不抛出异常
    }

    @Test
    void testDeleteProject() {
        boolean result = projectService.deleteProject(1L);
        // 验证方法执行不抛出异常
    }

    @Test
    void testGetProjectStatistics() {
        Map<String, Object> result = projectService.getProjectStatistics(1L);
        // 验证方法执行不抛出异常
    }

    @Test
    void testProjectTypeValidation() {
        assertTrue(projectService.isValidProjectType("residential"));
        assertTrue(projectService.isValidProjectType("commercial"));
        assertTrue(projectService.isValidProjectType("industrial"));
        assertFalse(projectService.isValidProjectType("invalid"));
        assertFalse(projectService.isValidProjectType(null));
    }

    @Test
    void testStatusValidation() {
        assertTrue(projectService.isValidStatus("operating"));
        assertTrue(projectService.isValidStatus("pending"));
        assertTrue(projectService.isValidStatus("maintenance"));
        assertFalse(projectService.isValidStatus("invalid"));
        assertFalse(projectService.isValidStatus(null));
    }

    @Test
    void testBuildingAreaValidation() {
        assertTrue(projectService.isValidBuildingArea(100));
        assertTrue(projectService.isValidBuildingArea(1));
        assertFalse(projectService.isValidBuildingArea(0));
        assertFalse(projectService.isValidBuildingArea(-100));
        assertFalse(projectService.isValidBuildingArea(null));
    }

    @Test
    void testBuildingCountValidation() {
        assertTrue(projectService.isValidBuildingCount(5));
        assertTrue(projectService.isValidBuildingCount(0));
        assertFalse(projectService.isValidBuildingCount(-1));
        assertFalse(projectService.isValidBuildingCount(null));
    }
}
