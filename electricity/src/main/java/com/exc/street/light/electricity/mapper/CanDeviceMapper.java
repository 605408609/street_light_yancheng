package com.exc.street.light.electricity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exc.street.light.resource.entity.electricity.CanDevice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 强电设备mapper接口
 *
 * @author Linshiwen
 * @date 2018/5/22
 */
public interface CanDeviceMapper extends BaseMapper<CanDevice> {
    /**
     * 根据节点id查询
     *
     * @param nid 从
     * @return 查询结果集
     */
    List<CanDevice> getByNid(Integer nid);

    /**
     * 根据节点id查询网关mac地址
     *
     * @param nid 根据节点id查询
     * @return
     */
    CanDevice getAddressByNid(Integer nid);

    /**
     * 根据节点id和can索引查询
     *
     * @param nid      节点id
     * @param canIndex can索引
     * @return 数量
     */
    CanDevice getByNidAndIndex(@Param("nid") Integer nid, @Param("canIndex") Integer canIndex);

    /**
     * 根据物理地址统计
     *
     * @param address
     * @return
     */
    int countByAddress(String address);

    /**
     * 根据物理地址和设备id统计
     *
     * @param address
     * @return
     */
    int countByAddressAndId(@Param("address") String address, @Param("id") Integer id);

    /**
     * 根据节点id删除
     *
     * @param nid
     */
    void deleteByNid(Integer nid);

    /**
     * 根据节点nid和设备序列化查找Device
     *
     * @param nid
     * @param batchNumber
     * @return
     */
    CanDevice getByNidAndBatchNumber(@Param("nid") Integer nid, @Param("batchNumber") String batchNumber);
}