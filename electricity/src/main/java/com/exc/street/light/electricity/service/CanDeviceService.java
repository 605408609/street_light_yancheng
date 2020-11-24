package com.exc.street.light.electricity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.electricity.CanDevice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 强电设备服务接口
 *
 * @author Linshiwen
 * @date 2018/05/22
 */
public interface CanDeviceService extends IService<CanDevice> {
    /**
     * 根据节点id查询
     *
     * @param id 节点id
     * @return 查询结果集
     */
    List<CanDevice> getByNid(Integer id);

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
    CanDevice getByNidAndIndex(Integer nid, Integer canIndex);

    /**
     * 新增设备
     *
     * @param canDevice
     * @param request
     * @return
     */
    Result add(CanDevice canDevice, HttpServletRequest request);

    /**
     * 批量更新设备
     *
     * @param canDevices 设备集合
     * @param request
     * @return 更新结果
     */
    Result patchUpdate(List<CanDevice> canDevices, HttpServletRequest request);

    /**
     * 根据物理地址统计
     *
     * @param address
     * @return
     */
    int countByAddress(String address);

    /**
     * 批量删除设备
     *
     * @param ids
     * @param request
     * @return
     */
    Result deletePatch(Integer[] ids, HttpServletRequest request);

    /**
     * 删除设备
     *
     * @param id
     * @param request
     * @return
     */
    Result delete(Integer id, HttpServletRequest request);

    /**
     * 根据节点id删除
     *
     * @param nid
     */
    void deleteByNid(Integer nid);

    /**
     * 根据nid和序列号查找CanDevice
     *
     * @param nid
     * @param batchNumber
     * @return
     */
    CanDevice getByNidAndBatchNumber(Integer nid, String batchNumber);
}
