/**
 * @filename:ScreenProgramService 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenProgram;
import com.exc.street.light.resource.qo.IrProgramQuery;
import com.exc.street.light.resource.vo.IrReqVerifyProgramVo;
import com.exc.street.light.resource.vo.req.IrReqScreenProgramVO;
import com.exc.street.light.resource.vo.req.IrReqSendProgramVO;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface ScreenProgramService extends IService<ScreenProgram> {

    /**
     * 添加节目
     *
     * @param screenProgram
     * @param httpServletRequest
     * @return
     */
    Result add(IrReqScreenProgramVO screenProgram, HttpServletRequest httpServletRequest);

    /**
     * 获取节目详情
     *
     * @param id
     * @return
     */
    Result get(Integer id, HttpServletRequest httpServletRequest);

    /**
     * 条件分页查询节目列表
     *
     * @param irProgramQuery
     * @return
     */
    Result queryList(IrProgramQuery irProgramQuery, HttpServletRequest httpServletRequest);

    /**
     * 修改节目
     *
     * @param irReqScreenProgramVO
     * @param httpServletRequest
     * @return
     */
    Result updateProgram(IrReqScreenProgramVO irReqScreenProgramVO, HttpServletRequest httpServletRequest);

    /**
     * 删除节目
     *
     * @param id
     * @param httpServletRequest
     * @return
     */
    Result delete(Integer id, HttpServletRequest httpServletRequest);

    /**
     * 发布节目
     *
     * @param irReqSendProgramVO
     * @param httpServletRequest
     * @return
     */
    Result sendProgram(IrReqSendProgramVO irReqSendProgramVO, HttpServletRequest httpServletRequest);

    /**
     * 批量删除节目
     *
     * @param ids
     * @param request
     * @return
     */
    Result batchDelete(String ids, HttpServletRequest request);


    /**
     * 节目状态的审核
     * @param  irReqScreenProgramVO
     * @Param  httpServletRequest
     */
    Result<String> verifyProgram(IrReqVerifyProgramVo irReqScreenProgramVO, HttpServletRequest httpServletRequest);
}