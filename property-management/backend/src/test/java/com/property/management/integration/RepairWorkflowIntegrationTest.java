package com.property.management.integration;

import com.property.management.entity.User;
import com.property.management.entity.Project;
import com.property.management.entity.House;
import com.property.management.entity.Repair;
import com.property.management.service.UserService;
import com.property.management.service.ProjectService;
import com.property.management.service.HouseService;
import com.property.management.service.RepairService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RepairWorkflowIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private RepairService repairService;

    private User testUser;
    private Project testProject;
    private House testHouse;
    private Repair testRepair;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setRealName("测试用户");
        testUser.setPhone("13800138000");
        testUser.setUserType("owner");
        testUser.setPassword("123456");
        testUser.setStatus("active");

        testProject = new Project();
        testProject.setProjectName("测试项目");
        testProject.setProjectType("residential");
        testProject.setAddress("测试地址");
        testProject.setBuildingArea(10000.0);
        testProject.setBuildingCount(5);
        testProject.setStatus("operating");

        testHouse = new House();
        testHouse.setProjectId(1L);
        testHouse.setBuildingNo("A栋");
        testHouse.setUnitNo("1单元");
        testHouse.setRoomNo("101");
        testHouse.setBuildingArea(100.0);
        testHouse.setStatus("occupied");

        testRepair = new Repair();
        testRepair.setUserId(1L);
        testRepair.setUserName("测试用户");
        testRepair.setUserPhone("13800138000");
        testRepair.setRepairType("water");
        testRepair.setDescription("厨房水管漏水");
    }

    @Test
    void testUserProjectHouseWorkflow() {
        assertNotNull(userService);
        assertNotNull(projectService);
        assertNotNull(houseService);

        assertTrue(userService.isValidPhone("13800138000"));
        assertTrue(userService.isValidPhone("15912345678"));
        assertFalse(userService.isValidPhone("1234567890"));

        assertTrue(projectService.isValidProjectType("residential"));
        assertTrue(projectService.isValidProjectType("commercial"));
        assertFalse(projectService.isValidProjectType("invalid"));

        assertTrue(projectService.isValidBuildingArea(1000));
        assertFalse(projectService.isValidBuildingArea(0));

        assertTrue(houseService.isValidStatus("occupied"));
        assertTrue(houseService.isValidStatus("vacant"));
        assertFalse(houseService.isValidStatus("invalid"));

        assertTrue(houseService.isValidArea(100));
        assertFalse(houseService.isValidArea(0));
    }

    @Test
    void testRepairServiceValidation() {
        assertNotNull(repairService);

        assertTrue(repairService.isValidRepairType("water"));
        assertTrue(repairService.isValidRepairType("electricity"));
        assertFalse(repairService.isValidRepairType("invalid"));

        assertTrue(repairService.isValidStatus("pending"));
        assertTrue(repairService.isValidStatus("processing"));
        assertFalse(repairService.isValidStatus("invalid"));
    }
}
