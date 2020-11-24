/**
 * @filename:DeviceUpgradeLogServiceImpl 2020-08-25
 * @project sl  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.DeviceUpgradeLog;
import com.exc.street.light.resource.entity.sl.DeviceUpgradeLogStatus;
import com.exc.street.light.resource.vo.resp.SlRespDeviceUpgradeLogVO;
import com.exc.street.light.resource.vo.resp.SlRespLampDeviceVO;
import com.exc.street.light.sl.mapper.DeviceUpgradeLogMapper;
import com.exc.street.light.sl.service.DeviceUpgradeLogService;
import com.exc.street.light.sl.service.DeviceUpgradeLogStatusService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**   
 * @Description:TODO(升级记录表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Service
public class DeviceUpgradeLogServiceImpl  extends ServiceImpl<DeviceUpgradeLogMapper, DeviceUpgradeLog> implements DeviceUpgradeLogService  {

    @Autowired
    DeviceUpgradeLogStatusService deviceUpgradeLogStatusService;

    @Override
    public Result pulldown(Integer isSuccess,Integer pageNum,Integer pageSize,HttpServletRequest request) {
        IPage<SlRespDeviceUpgradeLogVO> iPage = new Page<SlRespDeviceUpgradeLogVO>(pageNum, pageSize);
        List<SlRespDeviceUpgradeLogVO> pulldown = baseMapper.pulldown(iPage,isSuccess);
        if(pulldown!=null && pulldown.size()>0){
            iPage.setRecords(pulldown);
        }
        return new Result().success(iPage);
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        QueryWrapper<DeviceUpgradeLogStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("log_id",id);
        deviceUpgradeLogStatusService.remove(queryWrapper);
        this.removeById(id);
        return new Result().success("删除成功");
    }
}