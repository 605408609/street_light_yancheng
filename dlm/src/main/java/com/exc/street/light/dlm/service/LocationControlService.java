/**
 * @filename:LocationControlService 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.qo.DlmLocationControlQuery;
import com.exc.street.light.resource.vo.req.DlmReqLocationControlVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 集中控制器(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface LocationControlService extends IService<LocationControl> {

    /**
     * 添加集中控制器
     * @param controlVO
     * @param request
     * @return
     */
    Result insertLocationControl(DlmReqLocationControlVO controlVO, HttpServletRequest request);

    /**
     * 编辑集中控制器
     * @param controlVO
     * @param request
     * @return
     */
    Result updateLocationControl(DlmReqLocationControlVO controlVO, HttpServletRequest request);

    /**
     * 集中控制器详情
     * @param controlId
     * @param request
     * @return
     */
    Result detailOfLocationControl(Integer controlId, HttpServletRequest request);

    /**
     * 集中控制器混合信息
     * @param controlId
     * @param request
     * @return
     */
    Result detailOfMixLocationControl(Integer controlId, HttpServletRequest request);

    /**
     * 删除集中控制器
     * @param controlId
     * @param request
     * @return
     */
    Result deleteLocationControlByControlId(Integer controlId, HttpServletRequest request);

    /**
     * 批量删除集中控制器
     * @param controlIdList
     * @param request
     * @return
     */
    Result deleteOfBatchLocationControl(String[] controlIdList, HttpServletRequest request);

    /**
     * 分页查询集中控制器列表
     * @param controlQuery
     * @param request
     * @return
     */
    Result listLocationControlWithPageByControlQuery(DlmLocationControlQuery controlQuery, HttpServletRequest request);

    /**
     * 集中控制器下拉列表(中科智联)
     * @param request
     * @return
     */
    Result listLocationControlWithOptionQuery(HttpServletRequest request);

    /**
     * 集中控制器下拉列表(华体)
     * @param request
     * @return
     */
    Result listLocationControlOfHtWithOptionQuery(HttpServletRequest request);

    /**
     * 集中控制器下拉列表(爱克)
     * @param request
     * @return
     */
    Result listLocationControlOfExcWithOptionQuery(HttpServletRequest request);

    /**
     * 集中控制器名称和编号唯一性校验
     * @param id
     * @param name
     * @param num
     * @return
     */
    Result nameAndNumUniqueness(Integer id, String name, String num);

    /**
     * 定时检测集控的在离线状态
     */
    void getLocationControlStatus();

}