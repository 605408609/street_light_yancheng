/**
 * @filename:WifiApDeviceDao 2020-03-16
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.vo.resp.WifiRespApDeviceVO;
import com.exc.street.light.resource.vo.resp.WifiRespSimpleApVO;
import com.exc.street.light.wifi.qo.ApDeviceQueryObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**   
 * @Description: AP设备(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface WifiApDeviceDao extends BaseMapper<WifiApDevice> {

    /**
     * AP设备列表
     * @param page
     * @param queryObject
     * @return
     */
    IPage<WifiRespApDeviceVO> getPageList(Page<WifiRespApDeviceVO> page, @Param("queryObject") ApDeviceQueryObject queryObject);

    /**
     * ap设备简单信息
     * @param id
     * @return
     */
    WifiRespSimpleApVO getByApp(Integer id);
}
