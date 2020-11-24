/**
 * @filename:OrderProcessServiceImpl 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.woa.service.impl;

import com.exc.street.light.resource.entity.woa.OrderProcess;
import com.exc.street.light.resource.vo.resp.WoaRespOrderProcessVO;
import com.exc.street.light.woa.mapper.OrderProcessMapper;
import com.exc.street.light.woa.service.OrderProcessService;
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
public class OrderProcessServiceImpl  extends ServiceImpl<OrderProcessMapper, OrderProcess> implements OrderProcessService  {

    @Override
    public List<WoaRespOrderProcessVO> getWoaRespOrderProcessVO(Integer id) {
        return this.baseMapper.getWoaRespOrderProcessVO(id);
    }
}