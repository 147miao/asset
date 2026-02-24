package com.property.management.controller;

import com.property.management.common.Result;
import com.property.management.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProjectControllerTest {

    @Autowired
    private ProjectController projectController;

    @Test
    void testGetProjectPage() {
        Result<?> result = projectController.page(1, 10, "", "", "");
        assertNotNull(result);
    }

    @Test
    void testGetProjectById() {
        Result<?> result = projectController.getById(1L);
        assertNotNull(result);
    }

    @Test
    void testAddProject() {
        Project project = new Project();
        Result<?> result = projectController.add(project);
        assertNotNull(result);
    }

    @Test
    void testUpdateProject() {
        Project project = new Project();
        Result<?> result = projectController.update(project);
        assertNotNull(result);
    }

    @Test
    void testDeleteProject() {
        Result<?> result = projectController.delete(999L);
        assertNotNull(result);
    }

    @Test
    void testGetStatistics() {
        Result<?> result = projectController.statistics(1L);
        assertNotNull(result);
    }

    @Test
    void testGetAll() {
        Result<?> result = projectController.all();
        assertNotNull(result);
    }

    @Test
    void testGetList() {
        Result<?> result = projectController.list();
        assertNotNull(result);
    }
}
