package com.property.management.controller;

import com.property.management.common.Result;
import com.property.management.entity.Repair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RepairControllerTest {

    @Autowired
    private RepairController repairController;

    @Test
    void testGetRepairPage() {
        Result<?> result = repairController.page(1, 10, "", "", "", null);
        assertNotNull(result);
    }

    @Test
    void testGetRepairById() {
        Result<?> result = repairController.getById(1L);
        assertNotNull(result);
    }

    @Test
    void testAddRepair() {
        Repair repair = new Repair();
        Result<?> result = repairController.add(repair);
        assertNotNull(result);
    }

    @Test
    void testUpdateRepair() {
        Repair repair = new Repair();
        Result<?> result = repairController.update(repair);
        assertNotNull(result);
    }

    @Test
    void testCancelRepair() {
        Result<?> result = repairController.cancel(999L);
        assertNotNull(result);
    }

    @Test
    void testAssignRepair() {
        Result<?> result = repairController.assign(1L, 1L, "维修人员A");
        assertNotNull(result);
    }

    @Test
    void testCompleteRepair() {
        Result<?> result = repairController.complete(1L, "已修复");
        assertNotNull(result);
    }

    @Test
    void testRateRepair() {
        Result<?> result = repairController.rate(1L, 5, "服务很好");
        assertNotNull(result);
    }

    @Test
    void testGetStatistics() {
        Result<?> result = repairController.statistics(null);
        assertNotNull(result);
    }

    @Test
    void testGetByUserId() {
        Result<?> result = repairController.getByUserId(1L);
        assertNotNull(result);
    }
}
