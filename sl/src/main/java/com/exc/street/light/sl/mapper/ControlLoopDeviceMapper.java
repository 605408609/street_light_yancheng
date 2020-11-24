/**
 * @filename:ControlLoopDeviceDao 2020-09-14
 * @project sl  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;
import com.exc.street.light.resource.vo.req.SlReqInstallLampZkzlVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @Description:TODO(灯具、回路（分组）、集中控制器关联表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Mapper
public interface ControlLoopDeviceMapper extends BaseMapper<ControlLoopDevice> {

    /**
     * 根据设备id查询分组相关信息
     * @param deviceIdList
     * @return
     */
    List<SlReqInstallLampZkzlVO> getByDeviceId(@Param("deviceIdList") List<Integer> deviceIdList);

    /**
     * 根据集中控制器id及分组num查询集控分组
     * @param concentratorId
     * @param num
     * @return
     */
    ControlLoop selectControlLoopByNum(@Param("concentratorId")Integer concentratorId, @Param("num")String num);

    /**
     * 查找当前集中控制器下的所有序号
     * @param concentratorId
     * @return
     */
    List<Integer> getAllLampNoList(@Param("concentratorId")String concentratorId);

}
