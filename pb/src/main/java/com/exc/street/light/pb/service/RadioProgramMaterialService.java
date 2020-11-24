/**
 * @filename:RadioProgramMaterialService 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.pb.RadioProgramMaterial;
import com.exc.street.light.resource.vo.PbRespMaterialSizeVO;

import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: LeiJing
 *
 */
public interface RadioProgramMaterialService extends IService<RadioProgramMaterial> {
    /**
     * 根据节目ID获取节目关联素材的大小
     * @param programId
     * @return
     */
    List<PbRespMaterialSizeVO> selectMaterialSizeByProgramId(Integer programId);
}