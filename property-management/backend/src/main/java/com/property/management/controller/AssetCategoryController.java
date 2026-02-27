package com.property.management.controller;

import com.property.management.common.Result;
import com.property.management.entity.AssetCategory;
import com.property.management.service.AssetCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/asset-category")
public class AssetCategoryController {

    @Resource
    private AssetCategoryService assetCategoryService;

    @GetMapping("/tree")
    public Result getCategoryTree() {
        List<AssetCategory> tree = assetCategoryService.getCategoryTree();
        return Result.success(tree);
    }

    @GetMapping("/parent/{parentId}")
    public Result getCategoriesByParentId(@PathVariable Long parentId) {
        List<AssetCategory> categories = assetCategoryService.getCategoriesByParentId(parentId);
        return Result.success(categories);
    }

    @GetMapping("/type/{assetType}")
    public Result getCategoriesByAssetType(@PathVariable String assetType) {
        List<AssetCategory> categories = assetCategoryService.getCategoriesByAssetType(assetType);
        return Result.success(categories);
    }

    @PostMapping
    public Result saveCategory(@RequestBody AssetCategory category) {
        boolean success = assetCategoryService.saveCategory(category);
        return success ? Result.success() : Result.error("保存失败");
    }

    @PutMapping
    public Result updateCategory(@RequestBody AssetCategory category) {
        boolean success = assetCategoryService.updateCategory(category);
        return success ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable Long id) {
        boolean success = assetCategoryService.deleteCategory(id);
        return success ? Result.success() : Result.error("删除失败，可能存在子分类");
    }

    @PutMapping("/sort")
    public Result updateSortOrder(@RequestBody Map<Long, Integer> sortMap) {
        boolean success = assetCategoryService.updateSortOrder(sortMap);
        return success ? Result.success() : Result.error("排序更新失败");
    }

    @PostMapping("/fix-encoding")
    public Result fixEncoding() {
        boolean success = assetCategoryService.fixEncoding();
        return success ? Result.success("编码修复成功") : Result.error("编码修复失败");
    }
}