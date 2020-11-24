/**
 * @filename:LocationDistributeCabinetService 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationDistributeCabinet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.qo.DlmDistributeCabinetQuery;
import com.exc.street.light.resource.vo.req.DlmReqLocationDistributeCabinetVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 配电柜(服务层)
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
public interface LocationDistributeCabinetService extends IService<LocationDistributeCabinet> {

    /**
     * 新增配电柜
     * @param cabinetVO
     * @param request
     * @return
     */
    Result insertDistributeCabinet(DlmReqLocationDistributeCabinetVO cabinetVO, HttpServletRequest request);

    /**
     * 编辑配电柜
     * @param cabinetVO
     * @param request
     * @return
     */
    Result updateDistributeCabinet(DlmReqLocationDistributeCabinetVO cabinetVO, HttpServletRequest request);

    /**
     * 配电柜详情
     * @param cabinetId
     * @param request
     * @return
     */
    Result detailOfDistributeCabinet(Integer cabinetId, HttpServletRequest request);

    /**
     * 删除配电柜
     * @param cabinetId
     * @param request
     * @return
     */
    Result deleteDistributeCabinetByCabinetId(Integer cabinetId, HttpServletRequest request);

    /**
     * 批量删除配电柜
     * @param cabinetIdList
     * @param request
     * @return
     */
    Result deleteOfBatchDistributeCabinet(String[] cabinetIdList, HttpServletRequest request);

    /**
     * 分页查询配电柜列表
     * @param cabinetQuery
     * @param request
     * @return
     */
    Result listDistributeCabinetWithPageByCabinetQuery(DlmDistributeCabinetQuery cabinetQuery, HttpServletRequest request);

    /**
     * 配电柜下拉列表
     * @param request
     * @return
     */
    Result listDistributeCabinetWithOptionQuery(HttpServletRequest request);

    /**
     * 配电柜名称和编号唯一性校验
     * @param id
     * @param name
     * @param num
     * @return
     */
    Result nameAndNumUniqueness(Integer id, String name, String num);
}