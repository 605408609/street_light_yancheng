/**
 * @filename:AhDeviceServiceImpl 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2018 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.RadioDeviceDao;
import com.exc.street.light.dlm.service.RadioDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Service
public class RadioDeviceServiceImpl extends ServiceImpl<RadioDeviceDao, RadioDevice> implements RadioDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(RadioDeviceServiceImpl.class);

    @Autowired
    RadioDeviceDao radioDeviceDao;

    @Override
    public Result addList(List<RadioDevice> radioDeviceList) {
        logger.info("批量导入RadioDevice，接收参数:{}", radioDeviceList);
        for (RadioDevice radioDevice : radioDeviceList) {
            radioDeviceDao.insert(radioDevice);
        }
        return new Result().success("批量导入RadioDevice成功");

    }
    @Override
    public Result selectAll(){
        logger.info("查询所有AhDevice");
        List<RadioDevice> radioDeviceList = radioDeviceDao.selectList(null);
        return new Result().success("查询成功",radioDeviceList);
    }

    @Override
    public List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList) {
        return this.baseMapper.getDlmRespDeviceVOList(slLampPostIdList);
    }
}