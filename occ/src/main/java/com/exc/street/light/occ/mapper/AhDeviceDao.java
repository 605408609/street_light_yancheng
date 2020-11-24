/**
 * @filename:AhDeviceDao 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.occ.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.vo.OccAhDeviceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface AhDeviceDao extends BaseMapper<AhDevice> {
//    /**
//     * 根据 ID 查询
//     *
//     * @param id 主键ID
//     */
//    OccAhDeviceVO selectById(Long id);
    /**
     * 根据 ID 查询 设备信息，用于APP的工作台查询
     *
     * @param id 主键ID
     */
    OccAhDeviceVO selectInfoByWorkbench(Long id);

    /**
     *
     * 自定义翻页查询，根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param t 查询参数对象
     * @return
     */
    IPage<OccAhDeviceVO> selectPage(Page<?> page, QueryWrapper<OccAhDeviceVO> queryWrapper, @Param("queryObject")OccAhDeviceVO t);
}
