package com.property.management.service;

import com.property.management.entity.House;
import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.common.PageResult;

import java.util.List;

public interface HouseService extends IService<House> {
    PageResult<House> selectPage(Integer pageNum, Integer pageSize, String houseInfo, String status, Long projectId);

    House selectById(Long id);

    boolean addHouse(House house);

    boolean updateHouse(House house);

    boolean deleteHouse(Long id);

    List<House> selectByProjectId(Long projectId);

    List<House> selectByOwnerId(Long ownerId);

    boolean updateOwner(Long id, Long ownerId, String ownerName, String ownerPhone);

    default boolean isValidStatus(String status) {
        if (status == null) {
            return false;
        }
        return status.matches("^(occupied|vacant|rented)$");
    }

    default boolean isValidArea(Integer area) {
        return area != null && area > 0;
    }

    default boolean isValidRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.isEmpty()) {
            return false;
        }
        return roomNumber.matches("^\\d{3,4}$");
    }
}
