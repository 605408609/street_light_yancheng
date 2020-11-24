/**
 * @filename:AlarmService 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.resource.qo.QueryObject;
import com.exc.street.light.resource.qo.WoaAlarmQuery;
import com.exc.street.light.resource.vo.req.WoaReqAlarmTypeAnalyseVO;
import com.exc.street.light.resource.vo.resp.WoaRespAlarmVO;
import com.exc.street.light.resource.vo.resp.WoaRespOrderAlarmVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Longshuangyang
 */
public interface AlarmService extends IService<Alarm> {

    /**
     * 首页运维分析数据
     *
     * @param request
     * @return
     */
    Result analysis(HttpServletRequest request);

    /**
     * 分页条件查询
     *
     * @param woaAlarmQuery
     * @param httpServletRequest
     * @return
     */
    Result queryAlarm(WoaAlarmQuery woaAlarmQuery, HttpServletRequest httpServletRequest);

    /**
     * 批量删除告警
     *
     * @param ids
     * @param request
     * @return
     */
    Result batchDelete(String ids, HttpServletRequest request);

    /**
     * 修改告警关联工单id
     *
     * @param alarmIdList
     * @param id
     * @param status
     */
    void updateOpenOrder(List<Integer> alarmIdList, Integer id, Integer status);

    /**
     * 告警区域分析（增加时间筛选）
     *
     * @param queryObject
     * @param httpServletRequest
     * @return
     */
    Result areaAnalyseApp(QueryObject queryObject, HttpServletRequest httpServletRequest);


    /**
     * 告警区域分析
     *
     * @param type               1为分页数据
     * @param queryObject
     * @param httpServletRequest
     * @return
     */
    Result areaAnalyse(Integer type, QueryObject queryObject, HttpServletRequest httpServletRequest);

    /**
     * 告警类型分析
     *
     * @param woaReqAlarmTypeAnalyseVO
     * @param httpServletRequest
     * @return
     */
    Result typeAnalyse(WoaReqAlarmTypeAnalyseVO woaReqAlarmTypeAnalyseVO, HttpServletRequest httpServletRequest);

    /**
     * 获取工单告警对象列表
     *
     * @param id
     * @return
     */
    List<WoaRespOrderAlarmVO> getWoaRespOrderAlarmVO(Integer id);
    /**
     * 分页条件查询我的消息
     *
     * @param woaAlarmQuery
     * @param httpServletRequest
     * @return
     */
	Result queryNews(WoaAlarmQuery woaAlarmQuery, HttpServletRequest httpServletRequest);

	/**
	 * 批量设置我的消息为已读状态
	 * @param woaAlarmQuery
	 * @param httpServletRequest
	 * @return
	 */
	Result newsStatus(WoaAlarmQuery woaAlarmQuery, HttpServletRequest httpServletRequest);

	/**
	 * 设置我的消息全部为已读状态
	 * @param httpServletRequest
	 * @return
	 */
	Result newsAll(HttpServletRequest httpServletRequest);

    /**
     * 查询区域灯杆id集合
     * @param areaId
     * @return
     */
    List<Integer> areaLampPostIdList(Integer areaId);

    /**
     * 修改告警信息为已读
     * @param alarmId
     * @param request
     * @return
     */
    Result updateHaveRead(Integer alarmId, HttpServletRequest request);

}