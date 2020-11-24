/**
 * @filename:AlarmDao 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.resource.qo.QueryObject;
import com.exc.street.light.resource.qo.WoaAlarmQuery;
import com.exc.street.light.resource.vo.WoaAlarmAreaAnalyseVO;
import com.exc.street.light.resource.vo.req.WoaReqAlarmTypeAnalyseVO;
import com.exc.street.light.resource.vo.resp.WoaRespAlarmTypeAnalyseVO;
import com.exc.street.light.resource.vo.resp.WoaRespAlarmVO;
import com.exc.street.light.resource.vo.resp.WoaRespOrderAlarmVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Repository
@Mapper
public interface AlarmMapper extends BaseMapper<Alarm> {

    /**
     * 条件分页查询告警
     *
     * @param iPage
     * @param woaAlarmQuery
     * @return
     */
    IPage<WoaRespAlarmVO> query(@Param(value = "iPage") IPage<WoaRespAlarmVO> iPage, @Param(value = "woaAlarmQuery") WoaAlarmQuery woaAlarmQuery);
    /**
     * 条件分页查询我的消息
     *
     * @param iPage
     * @param woaAlarmQuery
     * @return
     */
    IPage<WoaRespAlarmVO> queryNews(IPage<WoaRespAlarmVO> iPage, WoaAlarmQuery woaAlarmQuery);

    /**
     * 修改告警关联工单id
     *
     * @param alarmIdList
     * @param id
     * @param status
     */
    void updateOpenOrder(@Param("list") List<Integer> alarmIdList, @Param("id") Integer id, @Param("status") Integer status);

    /**
     * 告警区域分析(分页)
     *
     * @param iPage
     * @param queryObject
     * @return
     */
    List<Integer> areaAnalysePage(IPage<WoaAlarmAreaAnalyseVO> iPage, QueryObject queryObject);

    /**
     * 告警区域分析(不分页)
     *
     * @return
     */
    List<WoaAlarmAreaAnalyseVO> areaAnalyse();

    /**
     * 告警类型分析
     *
     * @param alarmTypeAnalyseVO
     * @return
     */
    List<WoaRespAlarmTypeAnalyseVO> typeAnalyse(@Param("alarmTypeAnalyseVO") WoaReqAlarmTypeAnalyseVO alarmTypeAnalyseVO);

    /**
     * 获取告警
     * @param id
     * @return
     */
    List<WoaRespOrderAlarmVO> getWoaRespOrderAlarmVO(@Param("id") Integer id);

    List<Integer> areaLampPostIdList(@Param("areaId") Integer areaId);

}
