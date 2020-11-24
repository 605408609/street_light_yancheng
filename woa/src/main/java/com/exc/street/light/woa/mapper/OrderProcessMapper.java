/**
 * @filename:OrderProcessDao 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.woa.OrderProcess;
import com.exc.street.light.resource.vo.resp.WoaRespOrderProcessVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:TODO(数据访问层)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Repository
@Mapper
public interface OrderProcessMapper extends BaseMapper<OrderProcess> {

    /**
     * 获取工单进程集合
     *
     * @param id
     * @return
     */
    List<WoaRespOrderProcessVO> getWoaRespOrderProcessVO(@Param("id") Integer id);
}
