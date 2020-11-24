/**
 * @filename:OrderDao 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.exc.street.light.resource.entity.woa.OrderInfo;
import com.exc.street.light.resource.qo.WoaOrderQuery;
import com.exc.street.light.resource.vo.resp.WoaRespOrderListVO;
import com.exc.street.light.resource.vo.resp.WoaRespOrderVO;
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
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 工单列表
     *
     * @param iPage
     * @param woaOrderQuery
     * @return
     */
    IPage<WoaRespOrderListVO> listHandle(IPage<WoaRespOrderListVO> iPage,@Param("woaOrderQuery") WoaOrderQuery woaOrderQuery);

    /**
     * 获取工单详情工单对象
     *
     * @param id
     * @return
     */
    WoaRespOrderVO getWoaRespOrderVO(@Param("id") Integer id);

    /**
     * 检测超时，更改超时状态
     */
    void updateOvertime();


    /**
     * 新增工单数量
     * @param woaOrderQuery
     * @return
     */
    List<OrderInfo> selectNum(@Param("woaOrderQuery") WoaOrderQuery woaOrderQuery);
}
