package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.common.PageResult;
import com.property.management.entity.Asset;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AssetService extends IService<Asset> {
    PageResult<Asset> selectPage(Integer pageNum, Integer pageSize, String assetName, String assetType, Long categoryId, String status, Long projectId);

    Asset selectById(Long id);

    boolean addAsset(Asset asset);

    boolean updateAsset(Asset asset);

    boolean deleteAsset(Long id);

    void exportAssets(Long projectId, HttpServletResponse response);

    void importAssets(Long projectId, List<Asset> assets);

    List<Asset> selectByProjectId(Long projectId);

    boolean updateStatus(Long id, String status);

    boolean deleteAssets(List<Long> ids);
}
