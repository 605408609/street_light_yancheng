/**
 * @filename:AhPlayService 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.occ.service;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhPlay;
import com.exc.street.light.resource.vo.OccAhPlayVO;
/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Huang Min
 * 
 */
public interface AhPlayService extends IService<AhPlay> {

    /**
     * 自定义翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param t VO对象
     */
    IPage<OccAhPlayVO> page(Page<OccAhPlayVO> page, QueryWrapper<OccAhPlayVO> queryWrapper, OccAhPlayVO t, HttpServletRequest request);

    /**
     * 查询报警信息对象
     * @param id	报警信息表ID
     * @return	报警信息对象
     */
	OccAhPlayVO getByAhPlayId(Long id);

	/**
	 * 批量设置我的消息为已读状态
	 * @param occAhPlayVO
	 * @param httpServletRequest
	 * @return
	 */
	Result newsStatus(OccAhPlayVO occAhPlayVO, HttpServletRequest httpServletRequest);
	/**
	 * 设置我的消息全部为已读状态
	 * @param httpServletRequest
	 * @return
	 */
	Result newsAll(HttpServletRequest httpServletRequest);
	
}