package com.property.management.service;

import com.property.management.common.PageResult;
import com.property.management.entity.Repair;
import com.property.management.mapper.RepairMapper;
import com.property.management.service.impl.RepairServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RepairServiceTest {

    @Mock
    private RepairMapper repairMapper;

    @InjectMocks
    private RepairServiceImpl repairService;

    private Repair testRepair;

    @BeforeEach
    void setUp() {
        testRepair = new Repair();
        testRepair.setId(1L);
        testRepair.setUserId(1L);
        testRepair.setUserName("测试用户");
        testRepair.setUserPhone("13800138000");
        testRepair.setRepairType("water");
        testRepair.setDescription("厨房水管漏水");
        testRepair.setStatus("pending");
    }

    @Test
    void testSelectPage() {
        PageResult<Repair> result = repairService.selectPage(1, 10, "", "", "", null);
        assertNotNull(result);
    }

    @Test
    void testSelectById() {
        Repair repair = repairService.selectById(1L);
        // 由于使用了MyBatis Plus，这里可能返回null（mock环境）
        // 主要验证方法不抛出异常
    }

    @Test
    void testAddRepair() {
        // 测试添加报修，验证方法不抛出异常
        testRepair.setRepairNo(null);
        boolean result = repairService.addRepair(testRepair);
        // 在mock环境中，结果取决于具体实现
    }

    @Test
    void testUpdateRepair() {
        boolean result = repairService.updateRepair(testRepair);
        // 验证方法执行不抛出异常
    }

    @Test
    void testCancelRepair() {
        boolean result = repairService.cancelRepair(1L);
        // 验证方法执行不抛出异常
    }

    @Test
    void testAssignRepair() {
        boolean result = repairService.assignRepair(1L, 1L, "维修人员A");
        // 验证方法执行不抛出异常
    }

    @Test
    void testCompleteRepair() {
        boolean result = repairService.completeRepair(1L, "已修复完成");
        // 验证方法执行不抛出异常
    }

    @Test
    void testRateRepair() {
        boolean result = repairService.rateRepair(1L, 5, "服务很好");
        // 验证方法执行不抛出异常
    }

    @Test
    void testSelectByUserId() {
        List<Repair> result = repairService.selectByUserId(1L);
        assertNotNull(result);
    }

    @Test
    void testGetRepairStatistics() {
        List<Map<String, Object>> result = repairService.getRepairStatistics(null);
        assertNotNull(result);
    }

    @Test
    void testIsValidRepairType() {
        assertTrue(repairService.isValidRepairType("water"));
        assertTrue(repairService.isValidRepairType("electricity"));
        assertTrue(repairService.isValidRepairType("appliance"));
        assertTrue(repairService.isValidRepairType("other"));
        assertFalse(repairService.isValidRepairType("invalid"));
        assertFalse(repairService.isValidRepairType(null));
    }

    @Test
    void testIsValidStatus() {
        assertTrue(repairService.isValidStatus("pending"));
        assertTrue(repairService.isValidStatus("processing"));
        assertTrue(repairService.isValidStatus("completed"));
        assertTrue(repairService.isValidStatus("cancelled"));
        assertFalse(repairService.isValidStatus("invalid"));
        assertFalse(repairService.isValidStatus(null));
    }
}
