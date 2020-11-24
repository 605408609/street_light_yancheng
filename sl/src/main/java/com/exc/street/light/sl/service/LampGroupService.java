/**
 * @filename:LampGroupService 2020-07-16
 * @project sl  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.req.SlReqLampGroupVO;
import com.exc.street.light.resource.vo.resp.SlRespLampGroupSingleVO;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(灯具分组表服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface LampGroupService extends IService<LampGroup> {

    /**
     * 添加灯具分组
     *
     * @param slReqLampGroupVO
     * @param request
     * @return
     */
    Result addLampGroup(SlReqLampGroupVO slReqLampGroupVO, HttpServletRequest request);

    /**
     * 灯具分组验证唯一性
     *
     * @param slReqLampGroupVO
     * @param request
     * @return
     */
    Result unique(SlReqLampGroupVO slReqLampGroupVO, HttpServletRequest request);

    /**
     * 获取灯具分组列表
     *
     * @param request
     * @return
     */
    Result<List<SlRespLampGroupSingleVO>> getList(HttpServletRequest request);

    /**
     * 批量删除灯具分组
     *
     * @param slReqLampGroupVO
     * @param request
     * @return
     */
    Result deleteList(SlReqLampGroupVO slReqLampGroupVO, HttpServletRequest request);
}