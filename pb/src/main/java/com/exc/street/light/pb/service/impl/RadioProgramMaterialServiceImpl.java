/**
 * @filename:RadioProgramMaterialServiceImpl 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.pb.mapper.RadioProgramMaterialDao;
import com.exc.street.light.pb.service.RadioProgramMaterialService;
import com.exc.street.light.resource.entity.pb.RadioProgramMaterial;
import com.exc.street.light.resource.vo.PbRespMaterialSizeVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: LeiJing
 *
 */
@Service
public class RadioProgramMaterialServiceImpl extends ServiceImpl<RadioProgramMaterialDao, RadioProgramMaterial> implements RadioProgramMaterialService {

    @Override
    public List<PbRespMaterialSizeVO> selectMaterialSizeByProgramId(Integer programId) {
        if (programId == null) {
            return new ArrayList<>();
        }
        List<PbRespMaterialSizeVO> respMaterialSizeVos = baseMapper.selectMaterialSizeByProgramId(programId);
        return respMaterialSizeVos == null ? new ArrayList<>() : respMaterialSizeVos;
    }
}