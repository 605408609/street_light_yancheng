/**
 * @filename:OrderPicDao 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.woa.OrderPic;
import com.exc.street.light.resource.vo.resp.WoaRespOrderPicVO;
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
public interface OrderPicMapper extends BaseMapper<OrderPic> {

    /**
     * 关联图片与工单
     *
     * @param imgIdList
     * @param processId
     * @param id
     */
    void relationOrderImg(@Param("list") List<Integer> imgIdList,@Param("processId") Integer processId,@Param("id") Integer id);

    /**
     * 获取工单详情图片对象
     *
     * @param id
     * @return
     */
    List<WoaRespOrderPicVO> getWoaRespOrderPicVO(@Param("id") Integer id);
}
