/**
 * @filename:CanStrategyService 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.electricity.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.CanStrategy;
import com.exc.street.light.resource.qo.electricity.CanStrategyQueryObject;
import com.exc.street.light.resource.vo.electricity.RespCanStrategyVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyIssueVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyUniquenessVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: (路灯网关策略表服务层)
 * @version: V1.0
 * @author: Xiaok
 */
public interface CanStrategyService extends IService<CanStrategy> {


    /**
     * 新增路灯网关策略
     *
     * @param reqVo
     * @param request
     * @return
     */
    Result<String> add(ReqCanStrategyVO reqVo, HttpServletRequest request);

    /**
     * 编辑路灯网关策略
     *
     * @param reqVo
     * @param request
     * @return
     */
    Result<String> update(ReqCanStrategyVO reqVo, HttpServletRequest request);

    /**
     * 分页查询路灯网关策略
     *
     * @param qo      查询对象
     * @param request
     * @return
     */
    Result<IPage<RespCanStrategyVO>> getPageList(CanStrategyQueryObject qo, HttpServletRequest request);

    /**
     * 根据策略ID获取策略详情
     *
     * @param id      策略id
     * @param request
     * @return
     */
    Result<RespCanStrategyVO> getInfoById(Integer id, HttpServletRequest request);

    /**
     * 删除单个策略
     *
     * @param id      策略id
     * @param request
     * @return
     */
    Result<String> deleteById(Integer id, HttpServletRequest request);

    /**
     * 批量删除策略
     *
     * @param ids     策略id集合
     * @param request
     * @return
     */
    Result<String> batchDelete(String ids, HttpServletRequest request);

    /**
     * 对回路进行策略下发
     *
     * @param reqVo
     * @param request
     * @return
     */
    Result<JSONObject> issue(ReqCanStrategyIssueVO reqVo, HttpServletRequest request);


    /**
     * 唯一性校验
     * @param uniquenessVO
     * @param request
     * @return
     */
    Result<Integer> uniqueness(ReqCanStrategyUniquenessVO uniquenessVO, HttpServletRequest request);

}