/**
 * @filename:ScreenSubtitleService 2020-10-23
 * @project ir  V1.0
 * Copyright(c) 2020 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ir.service;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenSubtitle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitlePlayVO;

import javax.servlet.http.HttpServletRequest;

/**   
 * @Description:TODO(显示屏字幕表服务层)
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
public interface ScreenSubtitleService extends IService<ScreenSubtitle> {

    /**
     * 发送字幕
     *
     * @param irReqScreenSubtitlePlayVO
     * @param httpServletRequest
     * @return
     */
    Result sendSubtitle(IrReqScreenSubtitlePlayVO irReqScreenSubtitlePlayVO, HttpServletRequest httpServletRequest);
}