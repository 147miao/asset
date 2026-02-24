package com.property.management.service;

import com.property.management.common.PageResult;
import com.property.management.entity.House;
import com.property.management.mapper.HouseMapper;
import com.property.management.service.impl.HouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HouseServiceTest {

    @Mock
    private HouseMapper houseMapper;

    @InjectMocks
    private HouseServiceImpl houseService;

    private House testHouse;

    @BeforeEach
    void setUp() {
        testHouse = new House();
        testHouse.setId(1L);
        testHouse.setProjectId(1L);
        testHouse.setProjectName("测试项目");
        testHouse.setBuildingNo("A栋");
        testHouse.setUnitNo("1单元");
        testHouse.setRoomNo("101");
        testHouse.setBuildingArea(100.0);
        testHouse.setStatus("occupied");
    }

    @Test
    void testSelectPage() {
        PageResult<House> result = houseService.selectPage(1, 10, "", "", null);
        assertNotNull(result);
    }

    @Test
    void testSelectById() {
        House result = houseService.selectById(1L);
        // 在mock环境中可能返回null，主要验证方法不抛出异常
    }

    @Test
    void testAddHouse() {
        boolean result = houseService.addHouse(testHouse);
        // 验证方法执行不抛出异常
    }

    @Test
    void testUpdateHouse() {
        boolean result = houseService.updateHouse(testHouse);
        // 验证方法执行不抛出异常
    }

    @Test
    void testDeleteHouse() {
        boolean result = houseService.deleteHouse(1L);
        // 验证方法执行不抛出异常
    }

    @Test
    void testStatusValidation() {
        assertTrue(houseService.isValidStatus("occupied"));
        assertTrue(houseService.isValidStatus("vacant"));
        assertTrue(houseService.isValidStatus("rented"));
        assertFalse(houseService.isValidStatus("invalid"));
        assertFalse(houseService.isValidStatus(null));
    }

    @Test
    void testAreaValidation() {
        assertTrue(houseService.isValidArea(100));
        assertTrue(houseService.isValidArea(1));
        assertFalse(houseService.isValidArea(0));
        assertFalse(houseService.isValidArea(-100));
        assertFalse(houseService.isValidArea(null));
    }

    @Test
    void testRoomNumberValidation() {
        assertTrue(houseService.isValidRoomNumber("101"));
        assertTrue(houseService.isValidRoomNumber("1001"));
        assertFalse(houseService.isValidRoomNumber("10"));
        assertFalse(houseService.isValidRoomNumber(""));
        assertFalse(houseService.isValidRoomNumber(null));
    }
}
