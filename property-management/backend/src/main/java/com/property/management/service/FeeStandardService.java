package com.property.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.management.common.PageResult;
import com.property.management.entity.FeeStandard;

import java.util.List;

public interface FeeStandardService extends IService<FeeStandard> {
    PageResult<FeeStandard> selectPage(Integer pageNum, Integer pageSize, String feeName, String feeType, Long projectId);

    FeeStandard selectById(Long id);

    boolean addFeeStandard(FeeStandard feeStandard);

    boolean updateFeeStandard(FeeStandard feeStandard);

    boolean deleteFeeStandard(Long id);

    List<FeeStandard> selectByProjectId(Long projectId);

    List<FeeStandard> selectByFeeType(Long projectId, String feeType);
}
