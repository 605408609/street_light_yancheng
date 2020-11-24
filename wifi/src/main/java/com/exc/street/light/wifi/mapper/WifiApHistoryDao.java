/**
 * @filename:WifiApHistoryDao 2020-04-30
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.wifi.WifiApHistory;
import com.exc.street.light.resource.qo.WifiChartStatisticQueryObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface WifiApHistoryDao extends BaseMapper<WifiApHistory> {

    /**
     * 通过站点id集合查询wifiAp信息
     * @param queryObject
     * @return
     */
    List<WifiApHistory> selectWifiApBySiteIdList(WifiChartStatisticQueryObject queryObject);

}
