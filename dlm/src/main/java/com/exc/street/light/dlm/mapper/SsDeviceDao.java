/**
 * @filename:SsDeviceDao 2020-03-17
 * @project ss  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.dlm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;
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
public interface SsDeviceDao extends BaseMapper<SsDevice> {

    /**
     * 根据灯杆id获取灯具返回对象集合
     * @param list
     * @return
     */
    List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(@Param("list") List<Integer> list);
}
