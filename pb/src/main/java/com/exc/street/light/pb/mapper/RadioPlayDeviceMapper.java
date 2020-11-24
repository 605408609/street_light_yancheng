/**
 * @filename:RadioPlayDeviceDao 2020-05-11
 * @project pb  V1.0
 * Copyright(c) 2020 XiaoKun Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.pb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.pb.RadioPlayDevice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(广播播放设备中间表数据访问层)
 *
 * @version: V1.0
 * @author: XiaoKun
 * 
 */
@Repository
@Mapper
public interface RadioPlayDeviceMapper extends BaseMapper<RadioPlayDevice> {

    List<Integer> findIds(RadioPlayDevice radioPlayDevice);
	
}
