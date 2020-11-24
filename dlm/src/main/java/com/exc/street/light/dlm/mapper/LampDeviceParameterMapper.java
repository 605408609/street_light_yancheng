/**
 * @filename:LampDeviceParameterDao 2020-09-03
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.sl.LampDeviceParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: (设备参数数据表数据访问层)
 * @version: V1.0
 * @author: Huang Jin Hao
 */
@Repository
@Mapper
public interface LampDeviceParameterMapper extends BaseMapper<LampDeviceParameter> {

    /**
     * 根据单灯序号和支路数查询单灯设备id
     *
     * @param locationControlNum 集中控制器通讯地址
     * @param indexField         对应HtParameterEnum.INDEX.code()
     * @param index              序号值
     * @param loopNumField       对应HtParameterEnum.LOOP_NUM.code()
     * @param loopNum            第几回路
     * @return
     */
    Integer selectSystemDeviceIdByIndexAndLoopNum(@Param("locationControlNum") String locationControlNum, @Param("indexField") String indexField, @Param("index") Integer index,
                                                  @Param("loopNumField") String loopNumField, @Param("loopNum") Integer loopNum);

    /**
     * 插入设备对应的参数数据
     *
     * @param deviceId 设备id
     * @param paramVal 参数值
     * @param paramId  参数id
     * @return
     */
    Integer insertParamValue(@Param("deviceId") Integer deviceId, @Param("paramVal") String paramVal,
                             @Param("paramId") Integer paramId);

    /**
     * 修改设备对应的参数数据
     *
     * @param deviceId 设备id
     * @param paramVal 参数值
     * @param paramId  参数id
     * @return
     */
    Integer updateParamValue(@Param("deviceId") Integer deviceId, @Param("paramVal") String paramVal,
                             @Param("paramId") Integer paramId);

    /**
     * 根据设备id及字段名称查询设备参数数据
     * @param deviceId
     * @param name
     * @param deviceTypeId
     * @return
     */
    String select(@Param("deviceId") Integer deviceId, @Param("name") String name, @Param("deviceTypeId") Integer deviceTypeId);

    /**
     * 区分同一设备类型及编号的不同回路
     * @param num
     * @param loopNum
     * @param deviceTypeId
     * @return
     */
    Integer selectDeviceIdByLoopNum(@Param("num") String num, @Param("loopNum") String loopNum, @Param("deviceTypeId") Integer deviceTypeId);

}
