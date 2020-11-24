package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.electricity.ComDevice;
import org.apache.ibatis.annotations.Param;

/**
 * 串口设备mapper
 *
 * @author Linshiwen
 * @date 2018/11/13
 */
public interface ComDeviceMapper extends BaseMapper<ComDevice> {
    /**
     * 根据节点id查询
     *
     * @param nid           节点id
     * @param deviceAddress 设备地址
     * @param canPort       串口号
     * @return
     */
    ComDevice findByNidAndAddrAndCom(@Param("nid") Integer nid, @Param("deviceAddress") Integer deviceAddress,
                                     @Param("canPort") Integer canPort);

    /**
     * 根据节点id删除
     *
     * @param nid
     */
    void deleteByNid(Integer nid);
}