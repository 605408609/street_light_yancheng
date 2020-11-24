/**
 * @filename:WifiUserDao 2020-03-12
 * @project wifi  V1.0
 * Copyright(c) 2020 xiezhipeng Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.wifi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.wifi.WifiUser;
import com.exc.street.light.resource.vo.resp.WifiRespUserVO;
import com.exc.street.light.wifi.qo.WifiUserQueryObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**   
 * @Description: wifi用户(数据访问层)
 *
 * @version: V1.0
 * @author: xiezhipeng
 * 
 */
@Repository
@Mapper
public interface WifiUserDao extends BaseMapper<WifiUser> {

    /**
     * wifi用户列表
     * @param page
     * @param queryObject
     * @return
     */
    IPage<WifiRespUserVO> getList(Page<WifiRespUserVO> page, @Param("queryObject") WifiUserQueryObject queryObject);

    /**
     * 查询当日入网人数
     * @param currentDate
     * @param deviceId
     * @return
     */
    int selectDayConnCount(String currentDate, Integer deviceId);

    /**
     * 查询当日ap上关联的用户的流量之和
     * @param currentDate
     * @param deviceId
     * @return
     */
    int selectSumUserFlow(String currentDate, Integer deviceId);
}
