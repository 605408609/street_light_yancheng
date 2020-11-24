/**
 * @filename:EdManholeCoverDeviceDao 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2020 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ed.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.ed.EdManholeCoverDevice;
import com.exc.street.light.resource.vo.req.EdReqManholeCoverDevicePageVO;
import com.exc.street.light.resource.vo.resp.EdRespAlarmVO;
import com.exc.street.light.resource.vo.resp.EdRespManholeCoverDeviceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(井盖设备表数据访问层)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Repository
@Mapper
public interface EdManholeCoverDeviceMapper extends BaseMapper<EdManholeCoverDevice> {

    /**
     * 井盖设备条件查询
     * @param edReqManholeCoverDevicePageVO
     * @return
     */
    List<EdRespManholeCoverDeviceVO> getPage(@Param("edReqManholeCoverDevicePageVO")EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO);

    /**
     * 井盖设备分页条件查询
     * @param edRespManholeCoverDeviceVOIPage
     * @param edReqManholeCoverDevicePageVO
     * @return
     */
    List<EdRespManholeCoverDeviceVO> getPage(IPage<EdRespManholeCoverDeviceVO> edRespManholeCoverDeviceVOIPage, @Param("edReqManholeCoverDevicePageVO")EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO);

    /**
     * 根据设备编号查询设备所属街道站点
     * @param num
     * @return
     */
    EdRespAlarmVO selectEdManholeCoverDeviceAlarm(@Param("num")String num);
}