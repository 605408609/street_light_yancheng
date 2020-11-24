/**
 * @filename:LampGroupSingleService 2020-07-16
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampGroupSingle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.req.SlReqLampGroupVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(灯具分组中间表服务层)
 * @version: V1.0
 * @author: Longshuangyang
 *
 */
public interface LampGroupSingleService extends IService<LampGroupSingle> {

    /**
     * 修改灯具分组中间表，更新灯具分组关联关系
     *
     * @param slReqLampGroupVOList
     * @param request
     * @return
     */
    Result updateRelation(List<SlReqLampGroupVO> slReqLampGroupVOList, HttpServletRequest request);

    /**
     * 取消设备关联灯具分组
     * @param lampGroupSingle
     */
    void cancelDevice(LampGroupSingle lampGroupSingle);
}