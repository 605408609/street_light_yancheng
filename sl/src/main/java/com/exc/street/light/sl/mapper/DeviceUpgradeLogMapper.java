/**
 * @filename:DeviceUpgradeLogDao 2020-08-25
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.vo.resp.SlRespDeviceUpgradeLogVO;
import org.apache.ibatis.annotations.Mapper;
import com.exc.street.light.resource.entity.sl.DeviceUpgradeLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(升级记录表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Mapper
public interface DeviceUpgradeLogMapper extends BaseMapper<DeviceUpgradeLog> {

    List<SlRespDeviceUpgradeLogVO> pulldown(IPage<SlRespDeviceUpgradeLogVO> iPage,@Param("isSuccess") Integer isSuccess);
}
