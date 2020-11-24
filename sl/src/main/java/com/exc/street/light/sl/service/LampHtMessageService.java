/**
 * @filename:LampHtMessageService 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2020 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.sl.ht.HtCommandDTO;
import com.exc.street.light.resource.entity.sl.LampHtMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:(华体集中控制器消息记录服务层)
 * @version: V1.0
 * @author: Xiaok
 *
 */
public interface LampHtMessageService extends IService<LampHtMessage> {

    /**
     * 命令下发
     * @param locationControlId 华体集中控制器ID 非空
     * @param locationControlAddr 华体集中控制器num 非空
     * @param htCommandDto 工具类返回的消息封装类  cmdTypeEnum非空
     * @return
     */
    Result orderCommand(Integer locationControlId, String locationControlAddr, HtCommandDTO htCommandDto);

    /**
     * 单灯集中器运行数据接收
     * @param dataObj
     * @return
     */
    Result aloneInfo(JSONObject dataObj);
}