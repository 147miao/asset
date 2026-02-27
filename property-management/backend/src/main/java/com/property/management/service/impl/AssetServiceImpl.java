package com.property.management.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.common.PageResult;
import com.property.management.entity.Asset;
import com.property.management.mapper.AssetMapper;
import com.property.management.service.AssetService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class AssetServiceImpl extends ServiceImpl<AssetMapper, Asset> implements AssetService {

    @Override
    public PageResult<Asset> selectPage(Integer pageNum, Integer pageSize, String assetName, String assetType, Long categoryId, String status, Long projectId) {
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(assetName), Asset::getAssetName, assetName)
                .eq(StrUtil.isNotBlank(assetType), Asset::getAssetType, assetType)
                .eq(categoryId != null, Asset::getCategoryId, categoryId)
                .eq(StrUtil.isNotBlank(status), Asset::getStatus, status)
                .eq(projectId != null, Asset::getProjectId, projectId)
                .eq(Asset::getDeleted, 0)
                .orderByDesc(Asset::getCreateTime);
        Page<Asset> page = page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public Asset selectById(Long id) {
        return getOne(new LambdaQueryWrapper<Asset>()
                .eq(Asset::getId, id)
                .eq(Asset::getDeleted, 0));
    }

    @Override
    public boolean addAsset(Asset asset) {
        return save(asset);
    }

    @Override
    public boolean updateAsset(Asset asset) {
        return updateById(asset);
    }

    @Override
    public boolean deleteAsset(Long id) {
        try {
            // 使用MyBatis-Plus的UpdateWrapper明确指定SQL语句
            UpdateWrapper<Asset> wrapper = new UpdateWrapper<>();
            wrapper.set("deleted", 1).eq("id", id);
            boolean result = update(wrapper);
            System.out.println("删除资产ID: " + id + "，结果: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("删除资产失败，ID: " + id);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void exportAssets(Long projectId, HttpServletResponse response) {
        List<Asset> assets = selectByProjectId(projectId);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("assetCode", "资产编号");
        writer.addHeaderAlias("assetName", "资产名称");
        writer.addHeaderAlias("assetType", "资产类型");
        writer.addHeaderAlias("categoryName", "资产分类");
        writer.addHeaderAlias("projectName", "所属项目");
        writer.addHeaderAlias("originalValue", "原值");
        writer.addHeaderAlias("currentValue", "现值");
        writer.addHeaderAlias("purchaseDate", "采购日期");
        writer.addHeaderAlias("status", "状态");
        writer.addHeaderAlias("location", "存放位置");
        writer.addHeaderAlias("responsiblePerson", "负责人");
        writer.write(assets, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=assets.xlsx");
        try {
            writer.flush(response.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException("导出失败", e);
        } finally {
            writer.close();
        }
    }

    @Override
    public void importAssets(Long projectId, List<Asset> assets) {
        for (Asset asset : assets) {
            asset.setProjectId(projectId);
            save(asset);
        }
    }

    @Override
    public List<Asset> selectByProjectId(Long projectId) {
        return list(new LambdaQueryWrapper<Asset>()
                .eq(Asset::getProjectId, projectId)
                .eq(Asset::getDeleted, 0)
                .orderByDesc(Asset::getCreateTime));
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Asset asset = new Asset();
        asset.setId(id);
        asset.setStatus(status);
        return updateById(asset);
    }

    @Override
    public boolean deleteAssets(List<Long> ids) {
        try {
            // 使用MyBatis-Plus的update方法批量更新deleted字段
            UpdateWrapper<Asset> wrapper = new UpdateWrapper<>();
            wrapper.set("deleted", 1).in("id", ids);
            boolean result = update(wrapper);
            System.out.println("批量删除资产IDs: " + ids + "，结果: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("批量删除资产失败，IDs: " + ids);
            e.printStackTrace();
            return false;
        }
    }
}
