package com.property.management.controller;

import com.property.management.common.PageResult;
import com.property.management.common.Result;
import com.property.management.entity.House;
import com.property.management.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @GetMapping("/page")
    public Result<PageResult<House>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String houseInfo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId) {
        return Result.success(houseService.selectPage(pageNum, pageSize, houseInfo, status, projectId));
    }

    @GetMapping("/{id}")
    public Result<House> getById(@PathVariable Long id) {
        return Result.success(houseService.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody House house) {
        return Result.success(houseService.addHouse(house));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody House house) {
        return Result.success(houseService.updateHouse(house));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(houseService.deleteHouse(id));
    }

    @GetMapping("/project/{projectId}")
    public Result<List<House>> getByProjectId(@PathVariable Long projectId) {
        return Result.success(houseService.selectByProjectId(projectId));
    }

    @GetMapping("/owner/{ownerId}")
    public Result<List<House>> getByOwnerId(@PathVariable Long ownerId) {
        return Result.success(houseService.selectByOwnerId(ownerId));
    }

    @PutMapping("/owner")
    public Result<Boolean> updateOwner(@RequestParam Long id, @RequestParam Long ownerId, @RequestParam String ownerName, @RequestParam String ownerPhone) {
        return Result.success(houseService.updateOwner(id, ownerId, ownerName, ownerPhone));
    }
}
