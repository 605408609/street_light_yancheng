/**
 * @filename:ScreenProgramMaterialServiceImpl 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ir.service.impl;

import com.exc.street.light.resource.entity.ir.ScreenProgramMaterial;
import com.exc.street.light.ir.mapper.ScreenProgramMaterialMapper;
import com.exc.street.light.ir.service.ScreenProgramMaterialService;
import com.exc.street.light.resource.vo.IrProgramMaterialVO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Service
public class ScreenProgramMaterialServiceImpl  extends ServiceImpl<ScreenProgramMaterialMapper, ScreenProgramMaterial> implements ScreenProgramMaterialService  {

    @Override
    public List<IrProgramMaterialVO> getIrProgramMaterialVO(Integer programId) {
        return this.baseMapper.getIrProgramMaterialVO(programId);
    }
}