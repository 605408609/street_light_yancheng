/**
 * @filename:ScreenDeviceDao 2020-03-17
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.IrScreenDeviceQuery;
import com.exc.street.light.resource.vo.resp.IrRespScreenDeviceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Repository
@Mapper
public interface ScreenDeviceDao extends BaseMapper<ScreenDevice> {

    /**
     * 获取显示屏设备列表(分页查询)
     *
     * @param page
     * @param irScreenDeviceQuery
     * @return
     */
    IPage<ScreenDevice> query(IPage<IrScreenDeviceQuery> page, @Param("irScreenDeviceQuery") IrScreenDeviceQuery irScreenDeviceQuery);

    /**
     * 获取显示屏详情
     *
     * @param id
     * @return
     */
    IrRespScreenDeviceVO detail(@Param("id") Integer id);

    /**
     * 根据设备ID得到设备信息
     * @return
     */
	ScreenDevice getScreenshots(@Param("id")Integer id);

    /**
     * 根据灯杆id获取传感器对象
     * @param reqEmLampPostId
     * @return
     */
    MeteorologicalDevice selectEmDeviceByLampPostId(@Param("reqEmLampPostId") Integer reqEmLampPostId);


}
