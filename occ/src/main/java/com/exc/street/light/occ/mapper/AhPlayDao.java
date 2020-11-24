/**
 * @filename:AhPlayDao 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.occ.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.entity.occ.AhPlay;
import com.exc.street.light.resource.vo.OccAhPlayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * @Description:TODO(数据访问层)
 *
 * @version: V1.0
 * @author: Huang Min
 * 
 */
@Repository
@Mapper
public interface AhPlayDao extends BaseMapper<AhPlay> {
    /**
     * 自定义翻页查询，根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param name         设备名称
     * @param networkState 设备在线状态
     */
    IPage<OccAhPlayVO> selectPage(Page<?> page, QueryWrapper<OccAhPlayVO> queryWrapper, @Param("queryObject")OccAhPlayVO t);

    /**
     * 根据紧急报警设备编号查询灯杆ID
     * @param deviceNum
     * @return
     */
    OccAhPlayVO selectByDeviceNum(String deviceNum);
    
    /**
     * 根据紧急报警设备编号查询灯杆ID
     * @param deviceNum
     * @return
     */
    OccAhPlayVO getById(Long id);
    
    /**
     * 查询所有故障告警信息
     * @param id
     * @return
     */
    List<AhPlay> selectPageByWorkbench(@Param("id")Long id);
}
