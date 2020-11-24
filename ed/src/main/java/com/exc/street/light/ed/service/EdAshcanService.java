/**
 * @filename:EdAshcanService 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ed.service;

import com.exc.street.light.lj.vo.TrashVo;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ed.EdAshcan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.ed.EdManholeCoverDevice;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.resource.vo.req.EdReqAshcanPageVO;
import com.exc.street.light.resource.vo.req.EdReqAshcanPageVO;
import com.exc.street.light.resource.vo.resp.EdRespAlarmVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**   
 * @Description:TODO(垃圾桶信息表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
public interface EdAshcanService extends IService<EdAshcan> {

    /**
     * 根据指定条件查询垃圾桶设备
     * @param id
     * @param name
     * @param num
     * @return
     */
    EdAshcan getEdAshcanByCondition(Integer id, String name, String num);

    /**
     * 垃圾桶设备分页条件查询
     * @param edReqAshcanPageVO
     * @param request
     * @return
     */
    Result getPage(EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request);

    /**
     * 垃圾桶设备验证唯一性
     * @param edReqAshcanPageVO
     * @param request
     * @return
     */
    Result unique(EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request);

    /**
     * 新增垃圾桶设备
     * @param edReqAshcanPageVO
     * @param request
     * @return
     */
    Result add(EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request);

    /**
     * 修改垃圾桶设备
     * @param edReqAshcanPageVO
     * @param request
     * @return
     */
    Result updateDevice(EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request);

    /**
     * 批量删除设备
     * @param ids
     * @param request
     * @return
     */
    Result deleteByIds(String ids, HttpServletRequest request);

    /**
     * 设置开盖报警倾角阈值
     * @param edReqAshcanPageVOS
     * @param request
     * @return
     */
    Result setThreshold(List<EdReqAshcanPageVO> edReqAshcanPageVOS, HttpServletRequest request);

    /**
     * 修改设备在线离线状态
     * @param list
     */
    void statusUpdate(List<EdAshcan> list);

    /**
     * 告警处理
     * @param trashVos
     * @param imei
     * @return
     */
    Result alarmHandling(TrashVo trashVos, String imei);

    /**
     * 信息发送
     * @param imei
     * @return
     */
    boolean sendMessage(String imei);

    /**
     * 生成告警信息
     * @param edRespAlarmVO
     * @param type_id
     * @return
     */
    Result saveAlarm(EdRespAlarmVO edRespAlarmVO,Integer type_id);

    /**
     * 根据设备id及设备类型获取未处理告警记录
     * @param deviceId
     * @param typeId
     * @return
     */
    List<Alarm> selectAlarmByDeviceId(Integer deviceId, Integer typeId);

}