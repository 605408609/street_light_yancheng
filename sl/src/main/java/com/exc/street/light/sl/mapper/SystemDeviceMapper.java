/**
 * @filename:SystemDeviceDao 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.qo.SlLampDeviceQuery;
import com.exc.street.light.resource.vo.req.SlDeviceLocationControlVO;
import com.exc.street.light.resource.vo.resp.SlRespLampDeviceVO;
import com.exc.street.light.resource.vo.resp.SlRespLampGroupSingleParamVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceParameterVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**   
 * @Description:TODO(设备表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Mapper
public interface SystemDeviceMapper extends BaseMapper<SystemDevice> {

    /**
     * 灯控设备详情
     * @param deviceId
     * @return
     */
    List<SlRespSystemDeviceParameterVO> detail(@Param("deviceId") Integer deviceId);

    List<Integer> areaLampPostIdList(@Param("areaId") Integer areaId);

    List<SlRespSystemDeviceVO> getSingleLampParamList(@Param("lampPostIdList") List<Integer> lampPostIdList);




    List<SingleLampParamRespVO> getList(@Param("areaId") String deviceName);

    SingleLampParam getSingleLampOne(@Param("deviceNum") String deviceNum, @Param("loopNum") Integer loopNum, @Param("topicNum") Integer topicNum);

    SingleLampParam getSingleLampOneNewLora(@Param("sendId") String sendId,@Param("loopNum") Integer loopNum);

    /**
     * 根据分区id查询灯具列表
     *
     * @param areaId
     */
    List<SingleLampParam> listByAreaId(@Param("areaId") Integer areaId);

    /**
     * 根据灯具分组id和灯具id集合查询灯具列表
     * @param lampGroup
     * @param list
     * @return
     */
    List<SlRespLampGroupSingleParamVO> listByLampGroupIdAndSingleIdList(@Param("lampGroup") Integer lampGroup, @Param("list") List<Integer> list);

    /**
     * 分页查询灯具列表
     * @param page
     * @param slLampDeviceQuery
     * @return
     */
    //List<SlRespSystemDeviceVO> getPage(IPage<SlRespSystemDeviceVO> page, @Param("slLampDeviceQuery") SlLampDeviceQuery slLampDeviceQuery);

    /**
     * 查询灯具id分页对象
     * @param systemDeviceIPage
     * @param slLampDeviceQuery
     * @return
     */
    IPage<Integer> getPageId(IPage<Integer> systemDeviceIPage,@Param("slLampDeviceQuery") SlLampDeviceQuery slLampDeviceQuery);

    /**
     * 查询灯具列表
     * @param slLampDeviceQuery
     * @return
     */
    List<SlRespSystemDeviceVO> getPage(@Param("slLampDeviceQuery") SlLampDeviceQuery slLampDeviceQuery);

    /**
     *
     * @param deviceIdList
     * @return
     */
    List<SlDeviceLocationControlVO> getLocationControlNum(@Param("deviceIdList")List<Integer> deviceIdList);

    /**
     * 根据灯杆id查询灯杆
     * @param deviceId
     * @return
     */
    SlLampPost selectLampPostByDeviceId(@Param("deviceId")Integer deviceId);
}
