/**
 * @filename:LocationAreaDao 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.dlm.LocationArea;
import com.exc.street.light.resource.qo.DlmControlLoopOfDeviceQuery;
import com.exc.street.light.resource.vo.resp.DlmRespLocationAreaByLoopVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Repository
@Mapper
public interface LocationAreaMapper extends BaseMapper<LocationArea> {

    /**
     * 根据用户id关联的得到分区id
     * @param userId
     * @return
     */
    Integer selectUserArea(@Param("userId") Integer userId);

    /**
     * 根据集控id和分组id查询灯具
     * @param loopQuery
     * @return
     */
    List<DlmRespLocationAreaByLoopVO> selectControlLoopDeviceList(@Param("loopQuery") DlmControlLoopOfDeviceQuery loopQuery);

    /**
     * 根据升级记录获取对应设备id集合
     * @param logId
     * @param isSuccess
     * @return
     */
    List<Integer> getDeviceIdByUpgradeLog(@Param("logId")Integer logId,@Param("isSuccess")Integer isSuccess);

}
