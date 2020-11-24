/**
 * @filename:WifiAcDeviceDao 2020-03-27
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import com.exc.street.light.resource.vo.resp.WifiRespAcDeviceVO;
import com.exc.street.light.wifi.qo.AcDeviceQueryObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: ac设备(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface WifiAcDeviceDao extends BaseMapper<WifiAcDevice> {

    /**
     * ac设备列表
     * @param page
     * @param queryObject
     * @return
     */
    IPage<WifiRespAcDeviceVO> getPageList(Page<WifiRespAcDeviceVO> page, @Param("queryObject") AcDeviceQueryObject queryObject);

    /**
     * 查询所有ac设备信息
     * @return
     */
    List<WifiAcDevice> selectAll();
}
