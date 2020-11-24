/**
 * @filename:SsDeviceService 2020-03-17
 * @project ss  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ss.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.vo.SsDeviceVO;
/**   
 * @Description:TODO(服务层)
 * @version: V1.0
 * @author: Huang Min
 * 
 */
public interface SsDeviceService extends IService<SsDevice> {
    
    /**
     * 字段的唯一性校验
     * @param ssDeviceVO 
     * @return
     */
    Result uniqueness(SsDevice ssDevice);

    /**
     * 自定义翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param t VO对象
     */
    IPage<SsDeviceVO> page(Page<SsDeviceVO> page, QueryWrapper<SsDeviceVO> queryWrapper, SsDeviceVO t, HttpServletRequest request);

    /**
     * @param lampPostIdList
     * @param request
     * @return
     */
    Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request);

    /**
     * 查询设备详情，app 接口
     * @param id
     * @return
     */
    IPage<SsDeviceVO> selectByIdWithApp(Page<SsDeviceVO> page, Long id);
    

//	PreviewUrlsDTO getPreviewUrlsDTOByNum(String num);
//
//	PreviewUrlsDTO getPreviewUrls(PreviewUrlsRequestDTO previewUrlsRequestDTO);

}