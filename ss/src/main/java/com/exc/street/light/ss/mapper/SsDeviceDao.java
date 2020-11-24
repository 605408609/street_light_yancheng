/**
 * @filename:SsDeviceDao 2020-03-17
 * @project ss  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ss.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.vo.SsDeviceVO;
import org.springframework.stereotype.Repository;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Repository
@Mapper
public interface SsDeviceDao extends BaseMapper<SsDevice> {
    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    SsDeviceVO selectById(Long id);
    /**
     * 根据 ID 查询,APP接口
     *
     * @param id 主键ID
     */
    IPage<SsDeviceVO> selectByIdWithApp(Page<?> page, @Param("id")Long name);
    /**
     * 自定义翻页查询，根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param name         设备名称
     * @param networkState 设备在线状态
     */
    IPage<SsDeviceVO> selectPage(Page<?> page, QueryWrapper<SsDeviceVO> queryWrapper, @Param("queryObject")SsDeviceVO t);

}
