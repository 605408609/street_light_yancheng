/**
 * @filename:ScreenProgramMaterialService 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service;

import com.exc.street.light.resource.entity.ir.ScreenProgramMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.IrProgramMaterialVO;

import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
public interface ScreenProgramMaterialService extends IService<ScreenProgramMaterial> {

    /**
     * 获取节目素材中间对象
     * @param programId
     * @return
     */
    List<IrProgramMaterialVO> getIrProgramMaterialVO(Integer programId);
}