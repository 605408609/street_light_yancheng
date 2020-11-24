package com.exc.street.light.electricity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.exc.street.light.resource.entity.electricity.CanChangeData;

/**
 * 变化数据解析服务
 *
 * @author Linshiwen
 * @date 2018/7/31
 */
public interface CanChangeDataService extends IService<CanChangeData> {

    void analyze(byte[] data);
}
