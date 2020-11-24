/**
 * @filename:OrderProcessService 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service;

import com.exc.street.light.resource.entity.woa.OrderProcess;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.resp.WoaRespOrderProcessVO;

import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface OrderProcessService extends IService<OrderProcess> {

    /**
     * 获取工单进程集合
     *
     * @param id
     * @return
     */
    List<WoaRespOrderProcessVO> getWoaRespOrderProcessVO(Integer id);
}