/**
 * @filename:MeteorologicalDeviceDao 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.em.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.qo.EmMeteorologicalDeviceQueryObject;
import com.exc.street.light.resource.vo.resp.EmRespDeviceAndLampPostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: LeiJing
 * 
 */
@Repository
@Mapper
public interface MeteorologicalDeviceDao extends BaseMapper<MeteorologicalDevice> {

    /**
     * 分页查询设备列表
     *
     * @param page
     * @param qo
     * @return
     */
    IPage<EmRespDeviceAndLampPostVO> getPageList(IPage<EmRespDeviceAndLampPostVO> page, @Param("qo") EmMeteorologicalDeviceQueryObject qo);

    /**
     * 根据查询条件获取列表
     * @param qo
     * @return
     */
    List<MeteorologicalDevice> getList(EmMeteorologicalDeviceQueryObject qo);

    /**
     * 根据查询条件获取列表 并查询灯杆信息
     * @param qo
     * @return
     */
    List<EmRespDeviceAndLampPostVO> getEmAndLampPostList(EmMeteorologicalDeviceQueryObject qo);
	
}
