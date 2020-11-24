/**
 * @filename:EdManholeCoverDeviceService 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ed.service;

import com.exc.street.light.co.vo.AlarmMsgVo;
import com.exc.street.light.lj.vo.TrashVo;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ed.EdManholeCoverDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.req.EdReqManholeCoverDevicePageVO;
import com.exc.street.light.resource.vo.resp.EdRespAlarmVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @Description:TODO(井盖设备表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 *
 */
public interface EdManholeCoverDeviceService extends IService<EdManholeCoverDevice> {

    /**
     * 根据指定条件查询井盖设备
     * @param id
     * @param name
     * @param num
     * @return
     */
    EdManholeCoverDevice getEdManholeCoverDeviceByCondition(Integer id, String name, String num);

    /**
     * 井盖设备分页条件查询
     * @param edReqManholeCoverDevicePageVO
     * @param request
     * @return
     */
    Result getPage(EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request);

    /**
     * 井盖设备验证唯一性
     * @param edReqManholeCoverDevicePageVO
     * @param request
     * @return
     */
    Result unique(EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request);

    /**
     * 新增井盖设备
     * @param edReqManholeCoverDevicePageVO
     * @param request
     * @return
     */
    Result add(EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request);

    /**
     * 修改井盖设备
     * @param edReqManholeCoverDevicePageVO
     * @param request
     * @return
     */
    Result updateDevice(EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request);

    /**
     * 批量删除设备
     * @param ids
     * @param request
     * @return
     */
    Result deleteByIds(String ids, HttpServletRequest request);

    /**
     * 设置开盖报警倾角阈值
     * @param edReqManholeCoverDevicePageVOS
     * @param request
     * @return
     */
    Result setThreshold(List<EdReqManholeCoverDevicePageVO> edReqManholeCoverDevicePageVOS, HttpServletRequest request);

    /**
     * 查询所有设备信息
     * @return
     */
    List<EdManholeCoverDevice> selectAll();

    /**
     * 告警处理
     * @param alarmMsgVo
     * @return
     */
    Result alarmHandling(AlarmMsgVo alarmMsgVo);

}