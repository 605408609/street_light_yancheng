/**
 * @filename:AhDeviceService 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.occ.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.vo.OccAhDeviceVO;
/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Huang Min
 * 
 */
public interface AhDeviceService extends IService<AhDevice> {
    /**
     * 字段的唯一性校验
     * @param slDeviceVO 
     * @return
     */
    Result uniqueness(AhDevice ahDevice);

    /**
     * 自定义翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param t VO对象
     */
    IPage<OccAhDeviceVO> page(Page<OccAhDeviceVO> page, QueryWrapper<OccAhDeviceVO> queryWrapper, OccAhDeviceVO t, HttpServletRequest request);

    /**
     * @param lampPostIdList
     * @param request
     * @return
     */
    Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request);

	OccAhDeviceVO selectInfoByWorkbench(Long id);
}