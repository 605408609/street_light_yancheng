/**
 * @filename:MeteorologicalDeviceDao 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2020 LeiJing Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;
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
     * 根据灯杆id获取灯具返回对象集合
     * @param list
     * @return
     */
    List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(@Param("list") List<Integer> list);
}
