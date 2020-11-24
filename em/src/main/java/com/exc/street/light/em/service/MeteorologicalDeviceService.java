/**
 * @filename:MeteorologicalDeviceService 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.em.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.em.MeteorologicalHistory;
import com.exc.street.light.resource.qo.EmMeteorologicalDeviceQueryObject;
import com.exc.street.light.resource.vo.req.EmReqStatisParamVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: LeiJing
 * 
 */
public interface MeteorologicalDeviceService extends IService<MeteorologicalDevice> {
    /**
     * 新增气象设备
     *
     * @param meteorologicalDevice
     * @param request
     * @return
     */
	public Result addDevice(MeteorologicalDevice meteorologicalDevice, HttpServletRequest request);

    /**
     * 编辑气象设备
     *
     * @param meteorologicalDevice
     * @param request
     * @return
     */
    public Result updateDevice(MeteorologicalDevice meteorologicalDevice, HttpServletRequest request);

    /**
     * 删除气象设备
     *
     * @param deviceId
     * @param request
     * @return
     */
    public Result deleteDevice(Integer deviceId, HttpServletRequest request);

    /**
     * 批量删除气象设备
     *
     * @param idList
     * @return
     */
    public Result batchDeleteDevice(List<Integer> idList);

    /**
     * 气象设备详情
     *
     * @param deviceId
     * @param request
     * @return
     */
    public Result getDeviceInfo(Integer deviceId, HttpServletRequest request);

    /**
     * 气象设备列表
     *
     * @param qo
     * @param request
     * @return
     */
    public Result getDeviceList(EmMeteorologicalDeviceQueryObject qo, HttpServletRequest request);

    /**
     * 气象设备批量导入
     *
     * @param meteorologicalDevice
     * @param request
     * @return
     */
    public Result batchImportDevice(MeteorologicalDevice meteorologicalDevice, HttpServletRequest request);

    /**
     * 唯一性校验
     *
     * @param meteorologicalDevice
     * @param request
     * @return
     */
    public Result uniqueness(MeteorologicalDevice meteorologicalDevice, HttpServletRequest request);

    /**
     * 气象设备下拉列表
     *
     * @param request
     * @return
     */
    public Result getDevicePulldownList(HttpServletRequest request);

    /**
     * 获取所有设备的实时气象信息并保存至气象历史数据库
     *
     * @return
     */
    public Result getInfoByDevice();

    /**
     * 根据查询日期和查询气象信息类型统计当天气象数据，24小时气象数据
     *
     * @param emReqStatisParamVO
     * @param request
     * @return
     */
    public Result statis(EmReqStatisParamVO emReqStatisParamVO, HttpServletRequest request);

    /**
     * 获取所有设备的实时在线状态并保存至数据库，保存实时气象数据
     *
     * @return
     */
    public Result getStatusByDevice();

    /**
     * 获取气象设备实时数据，传气象设备id则查询指定气象设备，不传气象设备id则查询所有气象设备
     *
     * @return
     */
    public Result getReal(Integer deviceId);

    /**
     * 根据灯杆id集合查询气象设备
     *
     * @param lampPostIdList
     * @param request
     * @return
     */
    public Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request);

    /**
     * 根据灯杆id集合查询气象设备
     *
     * @param lampPostIdList
     * @param request
     * @return
     */
    public Result pulldownByLampPost2(List<Integer> lampPostIdList, HttpServletRequest request);

    public MeteorologicalHistory byteToMeteorologicalHistory(byte[] receiveData);

    /**
     * 发送显示屏字幕
     * @param id
     */
    void sendIrSubtitle(Integer id);
}