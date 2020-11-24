/**
 * @filename:WifiApDao 2020-03-12
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.wifi.WifiAp;
import com.exc.street.light.resource.vo.resp.WifiRespApVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**   
 * @Description: wifiAp(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface WifiApDao extends BaseMapper<WifiAp> {

    /**
     * AP统计列表
     * @param page
     * @param name
     * @return
     */
    IPage<WifiRespApVO> getPageList(Page<WifiRespApVO> page, String name, Integer areaId);

}
