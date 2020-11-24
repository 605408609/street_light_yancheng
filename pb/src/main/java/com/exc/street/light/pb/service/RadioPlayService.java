/**
 * @filename:RadioPlayService 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.RadioPlay;
import com.exc.street.light.resource.qo.PbRadioPlayQueryObject;
import com.exc.street.light.resource.vo.req.PbReqPlayRemoveBindVO;
import com.exc.street.light.resource.vo.req.PbReqRadioPlayControlVO;
import com.exc.street.light.resource.vo.req.PbReqRadioPlayVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: LeiJing
 */
public interface RadioPlayService extends IService<RadioPlay> {
    /**
     * 登录
     *
     * @return
     */
    public Result login();

    /**
     * 获取公共广播节目播放信息
     *
     * @param id
     * @param request
     * @return
     */
    public Result get(Integer id, HttpServletRequest request);

    /**
     * 新增公共广播节目播放
     *
     * @param pbReqRadioPlayVO
     * @param request
     * @return
     */
    public Result add(PbReqRadioPlayVO pbReqRadioPlayVO, HttpServletRequest request);

    /**
     * 编辑公共广播节目播放
     *
     * @param pbReqRadioPlayVO
     * @param request
     * @return
     */
    public Result update(PbReqRadioPlayVO pbReqRadioPlayVO, HttpServletRequest request);

    /**
     * 获取公共广播节目播放列表
     *
     * @param qo
     * @param request
     * @return
     */
    public Result getPlayList(PbRadioPlayQueryObject qo, HttpServletRequest request);

    public Result getPageList(PbRadioPlayQueryObject qo, HttpServletRequest request);

    /**
     * 公共广播节目播放控制
     *
     * @param pbReqRadioPlayControlVO
     * @param request
     * @return
     */
    public Result playControl(PbReqRadioPlayControlVO pbReqRadioPlayControlVO, HttpServletRequest request);

    /**
     * 删除公共广播节目播放
     *
     * @param id
     * @param request
     * @return
     */
    public Result delete(Integer id, HttpServletRequest request);

    /**
     * 删除公共广播
     *
     * @param id
     * @return
     */
    public Result deletePlay(Integer id);

    /**
     * 批量删除公共广播节目播放
     *
     * @param idList
     * @return
     */
    public Result batchDelete(List<Integer> idList);

    /**
     * 批量调节音量
     *
     * @param ids      多个id字符串 用,分隔
     * @param volValue 音量值
     * @param request
     * @return
     */
    public Result termVolSet(String ids, Integer volValue, HttpServletRequest request);

    /**
     * 定时任务查询
     *
     * @return
     */
    public Result taskList();

    /**
     * 雷拓IP广播平台接口调用
     *
     * @param interfaceName
     * @param jsonObject
     * @return
     */
    public JSONObject interfaceCall(String interfaceName, JSONObject jsonObject);

    /**
     * 雷拓IP广播平台获取错误信息
     *
     * @param returnJson
     * @return
     */
    public String getErrorMessage(JSONObject returnJson);


    /**
     * 更新定时任务状态
     *
     * @param radioPlayList
     */
    public void updatePlayStatusByList(List<RadioPlay> radioPlayList);

    /**
     * 更新全部定时任务状态
     */
    public void updatePlayStatus();

    /**
     * 通过 控制终端 对其他终端控制
     *
     * @param playStatus      true:开始控制 false:结束控制
     * @param fromDeviceId    发起方设备id
     * @param targetDeviceIds 接听方设备id集合字符串，用逗号分隔
     */
    public Result deviceSpeak(boolean playStatus, Integer fromDeviceId, String targetDeviceIds);

    /**
     * 根据查询的终端id获取终端的状态
     *
     * @param termIds
     * @return
     */
    JSONObject getTermState(List<Integer> termIds);

    /**
     * 解除设备与节目间的关联
     *
     * @param req
     * @param request
     * @return
     */
    Result<String> removeBind(List<PbReqPlayRemoveBindVO> req, HttpServletRequest request);

    /**
     * 根据灯杆id刷新定时广播任务
     * @param lampPostId
     * @return
     */
    Result<String> refreshPlayBind(List<Integer> lampPostId, HttpServletRequest request);

}