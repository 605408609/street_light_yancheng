/**
 * @filename:SlLampPostDao 2020-03-17
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.qo.DlmLampPostQuery;
import com.exc.street.light.resource.vo.DlmLampByAreaIdVO;
import com.exc.street.light.resource.vo.resp.DlmRespLampPostVO;
import com.exc.street.light.resource.vo.resp.DlmRespLocationLampPostVO;
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
public interface SlLampPostMapper extends BaseMapper<SlLampPost> {


    List<DlmRespLampPostVO> query(IPage<DlmRespLampPostVO> page, @Param("dlmLampPostQuery") DlmLampPostQuery dlmLampPostQuery);

    /**
     * 取消灯杆和分组的关联关系
     *
     * @param groupId
     */
    void updateToNoGroup(@Param("groupId") Integer groupId);

    /**
     * 获取灯杆名称
     *
     * @return
     */
    List<String> getSlLampPostName();

    /**
     * 取消设备关联灯杆(灯具)
     *
     * @param lampPostIdList
     */
    int cancelDeviceSl(@Param("list") List<Integer> lampPostIdList);

    /**
     * 取消设备关联灯杆(WIFI)
     *
     * @param lampPostIdList
     */
    int cancelDeviceWifi(@Param("list") List<Integer> lampPostIdList);

    /**
     * 取消设备关联灯杆(广播)
     *
     * @param lampPostIdList
     */
    int cancelDevicePb(@Param("list") List<Integer> lampPostIdList);

    /**
     * 取消设备关联灯杆(摄像头)
     *
     * @param lampPostIdList
     */
    int cancelDeviceSs(@Param("list") List<Integer> lampPostIdList);

    /**
     * 取消设备关联灯杆(显示屏)
     *
     * @param lampPostIdList
     */
    int cancelDeviceIr(@Param("list") List<Integer> lampPostIdList);
    /**
     * 取消设备关联灯杆(一键呼叫)
     *
     * @param lampPostIdList
     */
    int cancelDeviceOcc(@Param("list") List<Integer> lampPostIdList);

    /**
     * 取消设备关联灯杆(气象)
     *
     * @param lampPostIdList
     */
    int cancelDeviceEm(@Param("list") List<Integer> lampPostIdList);


    List<DlmRespLampPostVO>  queryList(@Param("dlmLampPostQuery") DlmLampPostQuery dlmLampPostQuery);

    /**
     * 根据灯杆id集合和分区id查询灯杆
     *
     * @param dlmLampByAreaIdVO
     * @return
     */
    List<DlmRespLocationLampPostVO> listByAreaId(@Param("dlmLampByAreaIdVO") DlmLampByAreaIdVO dlmLampByAreaIdVO);
}
