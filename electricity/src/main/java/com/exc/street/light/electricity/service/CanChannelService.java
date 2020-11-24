package com.exc.street.light.electricity.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.ControlChannelTagIdInfo;
import com.exc.street.light.resource.dto.electricity.ControlCommand;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.entity.electricity.CanChangeData;
import com.exc.street.light.resource.entity.electricity.CanChannel;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import com.exc.street.light.resource.qo.electricity.CanChannelQueryObject;
import com.exc.street.light.resource.vo.electricity.*;
import com.exc.street.light.resource.vo.req.electricity.ReqCanChannelControlVO;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * 回路服务接口
 *
 * @author Linshiwen
 * @date 2018/05/23
 */
public interface CanChannelService extends IService<CanChannel> {


    /**
     * 查询强电回路状态,并修改状态
     *
     * @param node
     */
    void updateState(ElectricityNode node);


    /**
     * 查询节点的回路
     *
     * @param nid 节点id
     * @return 查询结果集
     */
    List<CanChannelListVO> getByNid(Integer nid);

    /**
     * 批量新增
     *
     * @param dest    导入的文件
     * @param nid     节点编号
     * @param request
     * @return
     */
    Result saveList(File dest, Integer nid, HttpServletRequest request);

    /**
     * 批量控制回路
     *
     * @param controlVO
     * @param request
     * @return
     */
    Result control(ControlVO controlVO, HttpServletRequest request);

    /**
     * 根据节点编号查询场景回路
     *
     * @param nid
     * @return
     */
    List<CanSceneListVO> listByNid(Integer nid);

    /**
     * 根据条件查询继电器回路
     *
     * @param qo 查询对象
     * @return 结果集
     */
    Result query(CanChannelQueryObject qo);

    /**
     * 批量编辑回路
     *
     * @param channelPatchVO
     * @param request
     * @return
     */
    Result patchUpdate(CanChannelPatchVO channelPatchVO, HttpServletRequest request);

    /**
     * 查询回路
     *
     * @param request
     * @param qo
     * @return
     */
    Result listAll(HttpServletRequest request, CanChannelQueryObject qo);

    /**
     * 每天凌晨15分读取一次回路电流
     */
    void readCurrent();

    /**
     * 控制场景
     *
     * @param controlVO
     * @param request
     * @return
     */
    Result controlScene(ControlVO controlVO, HttpServletRequest request);

    /**
     * 获取回路控制结果
     *
     * @param canChannelControl
     * @return
     */
    Result getResult(CanChannelControl canChannelControl);

    /**
     * 获取回路控制结果
     *
     * @param tasks
     * @param executorService
     * @return
     */
    Result getResult(Collection tasks, ExecutorService executorService);

    /**
     * 获取网关场景控制结果
     *
     * @param canChannelControl
     * @return
     */
    Result getSceneControlResult(CanChannelControl canChannelControl);

    /**
     * 根据条件查询回路
     *
     * @param qo
     * @return
     */
    List<CanChannel> selectByCondition(CanChannelQueryObject qo);

    /**
     * 获取群回路控制结果
     *
     * @param controlCommands
     * @param nid
     * @return
     */
    Result getGroupResult(List<ControlCommand> controlCommands, Integer nid);

    /**
     * 通过nid和tagId获取回路的相关信息
     *
     * @param nid
     * @param tagId
     * @return
     */
    List<ControlChannelTagIdInfo> selectInfoByNidAndTagId(Integer tagId, Integer nid);

    List<ControlObject> selectControlByNid(Integer nid);


    /**
     * 处理开关变化
     *
     * @param nid
     * @param canChangeData
     */
    void handleSwitch(Integer nid, CanChangeData canChangeData);

    /**
     * 处理回路电流
     *
     * @param nid
     * @param canChangeData
     */
    void handleCurrent(Integer nid, CanChangeData canChangeData);

    /**
     * 全开全关控制
     *
     * @param reqVoList
     * @param isOpen
     * @param request
     * @return
     */
    Result<JSONObject> allSwitch(List<ReqCanChannelControlVO> reqVoList, boolean isOpen, HttpServletRequest request);


    /**
     * 获取回路当前绑定场景和历史记录
     *
     * @param id 回路ID
     * @param request
     * @return
     */
    Result<JSONObject> currentStrategy(String id, HttpServletRequest request);
}
