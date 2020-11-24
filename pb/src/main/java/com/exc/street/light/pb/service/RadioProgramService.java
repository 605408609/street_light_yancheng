/**
 * @filename:RadioProgramService 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.RadioProgram;
import com.exc.street.light.resource.qo.PbRadioProgramQueryObject;
import com.exc.street.light.resource.vo.req.PbReqRadioProgramVO;
import com.exc.street.light.resource.vo.req.PbReqVerifyProgramVO;
import com.exc.street.light.resource.vo.resp.PbRespRadioProgramVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: LeiJing
 *
 */
public interface RadioProgramService extends IService<RadioProgram> {
    /**
     * 新增公共广播节目
     *
     * @param pbReqRadioProgramVO
     * @param request
     * @return
     */
    public Result add(PbReqRadioProgramVO pbReqRadioProgramVO, HttpServletRequest request);

    /**
     * 编辑公共广播节目
     *
     * @param pbReqRadioProgramVO
     * @param request
     * @return
     */
    public Result update(PbReqRadioProgramVO pbReqRadioProgramVO, HttpServletRequest request);

    /**
     * 删除公共广播节目
     *
     * @param id
     * @param request
     * @return
     */
    public Result delete(Integer id, HttpServletRequest request);

    /**
     * 批量删除公共广播节目
     *
     * @param idList
     * @return
     */
    public Result batchDelete(List<Integer> idList, HttpServletRequest request);

    /**
     * 获取公共广播节目详情
     *
     * @param id
     * @param request
     * @return
     */
    public Result getInfo(Integer id, HttpServletRequest request);

    /**
     * 获取公共广播节目列表
     *
     * @param qo
     * @param request
     * @return
     */
    public Result getList(PbRadioProgramQueryObject qo, HttpServletRequest request);

    /**
     * 获取公共广播节目详情
     * @param programId
     * @return
     */
    public PbRespRadioProgramVO getResp(Integer programId);

    /**
     * 唯一性校验
     * @param radioProgram
     * @param request
     * @return
     */
    public Result uniqueness(RadioProgram radioProgram, HttpServletRequest request);

    /**
     * 刷新节目的容量的大小
     * @param programId 节目ID
     */
    public void refreshProgramDurationAndSize(Integer programId);

    /**
     * 审核节目
     * @param reqVO
     * @param request
     * @return
     */
    public Result<String> verifyProgram(PbReqVerifyProgramVO reqVO, HttpServletRequest request);
}