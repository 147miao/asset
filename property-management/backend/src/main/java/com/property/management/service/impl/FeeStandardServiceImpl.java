package com.property.management.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.management.common.PageResult;
import com.property.management.entity.FeeStandard;
import com.property.management.mapper.FeeStandardMapper;
import com.property.management.service.FeeStandardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeStandardServiceImpl extends ServiceImpl<FeeStandardMapper, FeeStandard> implements FeeStandardService {

    @Override
    public PageResult<FeeStandard> selectPage(Integer pageNum, Integer pageSize, String feeName, String feeType, Long projectId) {
        LambdaQueryWrapper<FeeStandard> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(feeName), FeeStandard::getFeeName, feeName)
                .eq(StrUtil.isNotBlank(feeType), FeeStandard::getFeeType, feeType)
                .eq(projectId != null, FeeStandard::getProjectId, projectId)
                .eq(FeeStandard::getDeleted, 0)
                .orderByDesc(FeeStandard::getCreateTime);
        Page<FeeStandard> page = page(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public FeeStandard selectById(Long id) {
        return getOne(new LambdaQueryWrapper<FeeStandard>()
                .eq(FeeStandard::getId, id)
                .eq(FeeStandard::getDeleted, 0));
    }

    @Override
    public boolean addFeeStandard(FeeStandard feeStandard) {
        return save(feeStandard);
    }

    @Override
    public boolean updateFeeStandard(FeeStandard feeStandard) {
        return updateById(feeStandard);
    }

    @Override
    public boolean deleteFeeStandard(Long id) {
        FeeStandard feeStandard = new FeeStandard();
        feeStandard.setId(id);
        feeStandard.setDeleted(1);
        return updateById(feeStandard);
    }

    @Override
    public List<FeeStandard> selectByProjectId(Long projectId) {
        return list(new LambdaQueryWrapper<FeeStandard>()
                .eq(FeeStandard::getProjectId, projectId)
                .eq(FeeStandard::getDeleted, 0)
                .orderByDesc(FeeStandard::getCreateTime));
    }

    @Override
    public List<FeeStandard> selectByFeeType(Long projectId, String feeType) {
        return list(new LambdaQueryWrapper<FeeStandard>()
                .eq(FeeStandard::getProjectId, projectId)
                .eq(FeeStandard::getFeeType, feeType)
                .eq(FeeStandard::getStatus, "active")
                .eq(FeeStandard::getDeleted, 0));
    }
}
