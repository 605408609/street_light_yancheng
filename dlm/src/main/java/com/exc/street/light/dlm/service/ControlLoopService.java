/**
 * @filename:ControlLoopService 2020-08-24
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.dlm.ControlLoopDTO;
import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.exc.street.light.resource.qo.DlmControlLoopQuery;
import com.exc.street.light.resource.vo.req.DlmReqControlLoopVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface ControlLoopService extends IService<ControlLoop> {

    /**
     * 新增集控分组
     * @param loopVO
     * @param request
     * @return
     */
    Result insertControlLoop(DlmReqControlLoopVO loopVO, HttpServletRequest request);

    /**
     * 编辑集控分组
     * @param loopVO
     * @param request
     * @return
     */
    Result updateControlLoop(DlmReqControlLoopVO loopVO, HttpServletRequest request);

    /**
     * 集控分组详情
     * @param loopId
     * @param request
     * @return
     */
    Result detailOfControlLoop(Integer loopId, HttpServletRequest request);

    /**
     * 删除集控分组
     * @param loopId
     * @param request
     * @return
     */
    Result deleteControlLoopByLoopId(Integer loopId, HttpServletRequest request);

    /**
     * 分页查询集控分组列表
     * @param loopQuery
     * @param request
     * @return
     */
    Result listControlLoopWithPageByLoopQuery(DlmControlLoopQuery loopQuery, HttpServletRequest request);

    /**
     * 集控器下的分组下拉列表
     * @param request
     * @param controlId
     * @return
     */
    Result listControlLoopWithOptionQuery(Integer controlId, HttpServletRequest request);

    /**
     * 回路开关控制
     * @param loopId
     * @param isOpen
     * @param request
     * @return
     */
    Result updateControlLoopSwitch(Integer loopId, Integer isOpen, HttpServletRequest request);

    /**
     * 批量删除集控分组
     * @param loopIdList
     * @param request
     * @return
     */
    Result deleteOfBatchControlLoop(String[] loopIdList, HttpServletRequest request);

    /**
     * 根据分组id集合查询回路信息
     * @param groupIdList
     * @param request
     * @return
     */
    Result getControlLoopByGroupIdList(List<Integer> groupIdList, HttpServletRequest request);

    /**
     * 分组名称唯一性校验
     * @param id
     * @param name
     * @return
     */
    Result nameUniqueness(Integer id, String name);

    /**
     * 设备名称，通讯地址，序号唯一性校验
     * @param loopVO
     * @return
     */
    Result uniqueness(DlmReqControlLoopVO loopVO);

    /**
     * 根据回路id集合查询回路和集控相关信息
     * @param loopIdList
     * @return
     */
    List<ControlLoopDTO> getControlLoopByIdList(List<Integer> loopIdList);
}