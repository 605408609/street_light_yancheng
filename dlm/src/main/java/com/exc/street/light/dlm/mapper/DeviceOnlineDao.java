/**
 * @filename:AhDeviceDao 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.dlm.DeviceOnline;
import com.exc.street.light.resource.vo.DeviceAllVo;
import com.exc.street.light.resource.vo.DeviceTypeGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Repository
@Mapper
public interface DeviceOnlineDao extends BaseMapper<DeviceOnline> {
    List<DeviceTypeGroupVO> typeGroup(@Param("dateTime") String dateTime,@Param("list") List<Integer> list);

    List<DeviceAllVo> deviceAll();

    /**
     * 查询当天是否已经存在记录
     * @param id
     * @param type
     * @param format
     * @return
     */
    List<DeviceOnline> deviceExist(@Param("id") Integer id,@Param("type") Integer type,@Param("format") String format);
}
