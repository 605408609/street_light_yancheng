/**
 * @filename:SsDeviceServiceImpl 2020-03-17
 * @project ss  V1.0
 * Copyright(c) 2018 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.SsDeviceDao;
import com.exc.street.light.dlm.service.SsDeviceService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ss.SsDevice;
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
public class SsDeviceServiceImpl extends ServiceImpl<SsDeviceDao, SsDevice> implements SsDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(SsDeviceServiceImpl.class);

    @Autowired
    SsDeviceDao ssDeviceDao;

    @Override
    public Result addList(List<SsDevice> ssDeviceList) {
        logger.info("批量导入SsDevice，接收参数:{}", ssDeviceList);
        for (SsDevice ssDevice : ssDeviceList) {
            ssDeviceDao.insert(ssDevice);
        }
        return new Result().success("批量导入SsDevice成功");
    }

    @Override
    public Result selectAll() {
        logger.info("查询所有SsDevice");
        List<SsDevice> ssDeviceList = ssDeviceDao.selectList(null);
        return new Result().success("查询成功",ssDeviceList);
    }

    @Override
    public List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList) {
        return this.baseMapper.getDlmRespDeviceVOList(slLampPostIdList);
    }
}