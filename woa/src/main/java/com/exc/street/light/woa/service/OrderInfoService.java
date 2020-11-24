/**
 * @filename:OrderService 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.woa.OrderInfo;
import com.exc.street.light.resource.qo.WoaOrderQuery;
import com.exc.street.light.resource.vo.req.WoaReqOrderNewStatusVO;
import com.exc.street.light.resource.vo.req.WoaReqOrderNewVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 首页工单概述数据
     *
     * @param request
     * @return
     */
    Result summary(HttpServletRequest request, Integer frame);

    /**
     * 新增工单数量
     *
     * @param request
     * @return
     */
    Result addNum(HttpServletRequest request);

    /**
     * 新增工单
     *
     * @param request
     * @param woaReqOrderNewVO
     * @return
     */
    Result add(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO);

    /**
     * 工单列表（我处理的工单）
     *
     * @param request
     * @param woaOrderQuery
     * @return
     */
    Result listHandle(HttpServletRequest request, WoaOrderQuery woaOrderQuery);

    /**
     * 根据用户id查询工单列表
     *
     * @param request
     * @return
     */
    Result listByUserId(HttpServletRequest request, WoaOrderQuery woaOrderQuery);

    /**
     * 查看详情
     *
     * @param request
     * @param id
     * @return
     */
    Result get(HttpServletRequest request, Integer id);

    /**
     * 编辑工单
     *
     * @param request
     * @param woaReqOrderNewVO
     * @return
     */
    Result updateOrder(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO);

    /**
     * 处理
     *
     * @param request
     * @param id
     * @return
     */
    Result handle(HttpServletRequest request, Integer id);

    /**
     * 处理完成进入待审核
     *
     * @param request
     * @param woaReqOrderNewVO
     * @return
     */
    Result complete(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO);

    /**
     * 初审
     *
     * @param request
     * @param woaReqOrderNewVO
     * @return
     */
    Result firstTrial(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO);

    /**
     * 审核
     *
     * @param request
     * @param woaReqOrderNewVO
     * @return
     */
    Result secondTrial(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO);

    /**
     * 获取各状态的新工单
     *
     * @param woaReqOrderNewStatusVO
     * @param request
     * @return
     */
    Result newStatus(WoaReqOrderNewStatusVO woaReqOrderNewStatusVO, HttpServletRequest request);

    /**
     * 工单验证唯一性
     *
     * @param orderInfo
     * @param request
     * @return
     */
    Result unique(OrderInfo orderInfo, HttpServletRequest request);
}