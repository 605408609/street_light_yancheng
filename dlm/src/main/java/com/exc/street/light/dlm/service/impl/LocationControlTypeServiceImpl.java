/**
 * @filename:LocationControlTypeServiceImpl 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LocationControlTypeMapper;
import com.exc.street.light.dlm.service.LocationControlTypeService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationControlType;
import com.exc.street.light.resource.vo.resp.DlmRespLocationControlTypeWithOptionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
public class LocationControlTypeServiceImpl extends ServiceImpl<LocationControlTypeMapper, LocationControlType> implements LocationControlTypeService {

    private static final Logger logger = LoggerFactory.getLogger(LocationControlTypeServiceImpl.class);

    @Override
    public Result listLocationControlTypeWithOptionQuery(HttpServletRequest request) {
        logger.info("listLocationControlTypeWithOptionQuery - 集中控制器类型下拉列表");
        List<DlmRespLocationControlTypeWithOptionVO> voList = new ArrayList<>();
        List<LocationControlType> locationControlTypeList = this.list();
        if (locationControlTypeList != null && locationControlTypeList.size() > 0) {
            for (LocationControlType controlType : locationControlTypeList) {
                DlmRespLocationControlTypeWithOptionVO optionVO = new DlmRespLocationControlTypeWithOptionVO();
                optionVO.setId(controlType.getId());
                optionVO.setType(controlType.getType());
                voList.add(optionVO);
            }
        }
        Result<List<DlmRespLocationControlTypeWithOptionVO>> result = new Result<>();
        return result.success(voList);
    }
}