/**
 * @filename:LampDeviceParameterService 2020-09-03
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.sl.LampDeviceParameter;

import java.util.List;

/**
 * @Description:TODO(设备参数数据表服务层)
 * @version: V1.0
 * @author: Huang Jin Hao
 */
public interface LampDeviceParameterService extends IService<LampDeviceParameter> {


    /**
     * 根据单灯序号和支路数查询单灯设备id
     *
     * @param locationControlNum 集中控制器通讯地址
     * @param indexField   对应HtParameterEnum.INDEX.code()
     * @param index        序号值
     * @param loopNumField 对应HtParameterEnum.LOOP_NUM.code()
     * @param loopNum      第几回路
     * @return 灯具id
     */
    Integer selectSystemDeviceIdByIndexAndLoopNum(String locationControlNum,String indexField, Integer index,
                                                  String loopNumField, Integer loopNum);

    /**
     * 更新（新增或修改）设备数据
     * @param deviceId 设备id
     * @param paramId 参数id
     * @param paramValue 参数值
     * @return 是否成功
     */
    boolean saveParamValue(Integer deviceId, Integer paramId, String paramValue);

    /**
     * 设置设备私有属性的默认值
     * @param deviceId
     * @param deviceTypeId
     * @return
     */
    boolean addDefaultParamValue(Integer deviceId, Integer deviceTypeId);

    /**
     * 根据设备id集合删除它的私有属性
     * @param deviceIdList
     * @return
     */
    boolean deleteByDeviceIds(List<Integer> deviceIdList);

    /**
     * 根据信息查询设备参数值
     * @param deviceId
     * @param name
     * @return
     */
    String select(Integer deviceId,String name,Integer deviceTypeId);

    /**
     * 区分同一设备类型及编号的不同回路
     * @param num
     * @param loopNum
     * @param deviceTypeId
     * @return
     */
    Integer selectDeviceIdByLoopNum(String num,String loopNum,Integer deviceTypeId);
}