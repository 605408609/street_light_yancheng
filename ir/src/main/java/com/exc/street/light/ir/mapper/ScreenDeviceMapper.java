/**
 * @filename:ScreenDeviceDao 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ir.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Repository
@Mapper
public interface ScreenDeviceMapper extends BaseMapper<ScreenDevice> {


}
