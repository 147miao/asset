package com.property.management.controller;

import com.property.management.common.PageResult;
import com.property.management.common.Result;
import com.property.management.entity.Asset;
import com.property.management.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/page")
    public Result<PageResult<Asset>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String assetName,
            @RequestParam(required = false) String assetType,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long projectId) {
        return Result.success(assetService.selectPage(pageNum, pageSize, assetName, assetType, categoryId, status, projectId));
    }

    @GetMapping("/{id}")
    public Result<Asset> getById(@PathVariable Long id) {
        return Result.success(assetService.selectById(id));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Asset asset) {
        return Result.success(assetService.addAsset(asset));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Asset asset) {
        return Result.success(assetService.updateAsset(asset));
    }

    @DeleteMapping("/batch")
    public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        return Result.success(assetService.deleteAssets(ids));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(assetService.deleteAsset(id));
    }

    @GetMapping("/export")
    public void export(@RequestParam Long projectId, HttpServletResponse response) {
        assetService.exportAssets(projectId, response);
    }

    @PostMapping("/import")
    public Result<Boolean> importAssets(@RequestParam Long projectId, @RequestBody List<Asset> assets) {
        assetService.importAssets(projectId, assets);
        return Result.success();
    }

    @GetMapping("/project/{projectId}")
    public Result<List<Asset>> getByProjectId(@PathVariable Long projectId) {
        return Result.success(assetService.selectByProjectId(projectId));
    }

    @PutMapping("/status/{id}")
    public Result<Boolean> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(assetService.updateStatus(id, status));
    }
}
