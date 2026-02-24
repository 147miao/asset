package com.property.management.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.common.PageResult;
import com.property.management.entity.House;
import com.property.management.mapper.HouseMapper;
import com.property.management.service.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

    @Override
    public PageResult<House> selectPage(Integer pageNum, Integer pageSize, String houseInfo, String status, Long projectId) {
        LambdaQueryWrapper<House> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StrUtil.isNotBlank(houseInfo), w -> w
                .like(House::getBuildingNo, houseInfo)
                .or().like(House::getUnitNo, houseInfo)
                .or().like(House::getRoomNo, houseInfo))
                .eq(StrUtil.isNotBlank(status), House::getStatus, status)
                .eq(projectId != null, House::getProjectId, projectId)
                .eq(House::getDeleted, 0)
                .orderByDesc(House::getCreateTime);
        Page<House> page = page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public House selectById(Long id) {
        return getOne(new LambdaQueryWrapper<House>()
                .eq(House::getId, id)
                .eq(House::getDeleted, 0));
    }

    @Override
    public boolean addHouse(House house) {
        return save(house);
    }

    @Override
    public boolean updateHouse(House house) {
        return updateById(house);
    }

    @Override
    public boolean deleteHouse(Long id) {
        House house = new House();
        house.setId(id);
        house.setDeleted(1);
        return updateById(house);
    }

    @Override
    public List<House> selectByProjectId(Long projectId) {
        return list(new LambdaQueryWrapper<House>()
                .eq(House::getProjectId, projectId)
                .eq(House::getDeleted, 0)
                .orderByAsc(House::getBuildingNo)
                .orderByAsc(House::getUnitNo)
                .orderByAsc(House::getRoomNo));
    }

    @Override
    public List<House> selectByOwnerId(Long ownerId) {
        return list(new LambdaQueryWrapper<House>()
                .eq(House::getOwnerId, ownerId)
                .eq(House::getDeleted, 0));
    }

    @Override
    public boolean updateOwner(Long id, Long ownerId, String ownerName, String ownerPhone) {
        House house = new House();
        house.setId(id);
        house.setOwnerId(ownerId);
        house.setOwnerName(ownerName);
        house.setOwnerPhone(ownerPhone);
        house.setStatus("occupied");
        return updateById(house);
    }
}
