/**
 * @filename:LampDeviceThregholdServiceImpl 2020-08-22
 * @project sl  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.resource.vo.req.SlReqThresholdAddListVO;
import com.exc.street.light.resource.vo.req.SlReqThresholdAddVO;
import com.exc.street.light.resource.vo.resp.SlRespThresholdDetailVo;
import com.exc.street.light.sl.mapper.LampDeviceThregholdMapper;
import com.exc.street.light.sl.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**   
 * @Description:TODO(设备阈值数据表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Service
public class LampDeviceThregholdServiceImpl  extends ServiceImpl<LampDeviceThregholdMapper, LampDeviceThreghold> implements LampDeviceThregholdService  {

    @Autowired
    SystemDeviceThresholdService systemDeviceThresholdService;

    @Autowired
    SingleLampParamService singleLampParamService;

    @Autowired
    SystemDeviceService systemDeviceService;

    @Override
    public Result detail(Integer id, HttpServletRequest request) {
        List<SlRespThresholdDetailVo> slRespThresholdDetailVoList = new ArrayList<>();
        slRespThresholdDetailVoList = baseMapper.detail(id);
        List<Integer> thresholdIdList = new ArrayList<>();
        if(slRespThresholdDetailVoList!=null && slRespThresholdDetailVoList.size()>0){
            thresholdIdList = slRespThresholdDetailVoList.stream().map(SlRespThresholdDetailVo::getThresholdId).distinct().collect(Collectors.toList());
        }
        //默认设备类型为1，后期修改
        List<SystemDeviceThreshold> systemDeviceThresholdList = systemDeviceThresholdService.getListByDeviceTypeId(1);
        /*SingleLampParam singleLampParam = singleLampParamService.getById(id);
            if(singleLampParam==null){
                return new Result().error("不存在该设备");
            }*/
            /*Integer deviceId = singleLampParam.getDeviceId();
            LampDevice lampDevice = lampDeviceService.getById(deviceId);
            if("EXC1".equals(lampDevice.getFactory())){
                return new Result().error("暂不支持该类设备设置阈值");
            }*/
        if(systemDeviceThresholdList!=null&&systemDeviceThresholdList.size()>0){
            for (SystemDeviceThreshold systemDeviceThreshold : systemDeviceThresholdList) {
                if(!thresholdIdList.contains(systemDeviceThreshold.getId())){
                    SlRespThresholdDetailVo slRespThresholdDetailVo = new SlRespThresholdDetailVo();
                    BeanUtils.copyProperties(systemDeviceThreshold, slRespThresholdDetailVo);
                    slRespThresholdDetailVo.setThresholdValue("");
                    slRespThresholdDetailVoList.add(slRespThresholdDetailVo);
                }
            }
        }
        return new Result().success(slRespThresholdDetailVoList);
    }

    @Override
    public Result add(SlReqThresholdAddListVO slReqThresholdAddListVO, HttpServletRequest request) {
        List<Integer> deviceIdList = slReqThresholdAddListVO.getDeviceIdList();
        List<SlReqThresholdAddVO> slReqThresholdAddVOList = slReqThresholdAddListVO.getSlReqThresholdAddVOList();
        if(deviceIdList==null || deviceIdList.size()==0){
            return new Result().error("设备为空");
        }
        List<LampDeviceThreghold> lampDeviceThregholdList = new ArrayList<>();
        for (Integer singleLampId : deviceIdList) {
            SystemDevice systemDevice = systemDeviceService.getById(singleLampId);
            for (SlReqThresholdAddVO slReqThresholdAddVO : slReqThresholdAddVOList) {
                String filed = slReqThresholdAddVO.getFiled();
                String thresholdValue = slReqThresholdAddVO.getThresholdValue();
                Integer deviceTypeId = slReqThresholdAddVO.getDeviceTypeId();
                if(thresholdValue!=null&&thresholdValue.length()>0){
                    SystemDeviceThreshold systemDeviceThreshold = systemDeviceThresholdService.getOneByFiled(filed,deviceTypeId);
                    if(systemDeviceThreshold!=null){
                        Integer thresholdId = systemDeviceThreshold.getId();
                        LampDeviceThreghold lampDeviceThreghold = new LampDeviceThreghold();
                        lampDeviceThreghold.setDeviceId(singleLampId);
                        lampDeviceThreghold.setThresholdId(thresholdId);
                        lampDeviceThreghold.setThresholdValue(thresholdValue);
                        lampDeviceThregholdList.add(lampDeviceThreghold);
                        /*SingleLampParam singleLampById = singleLampParamService.getSingleLampById(singleLampId);
                        LampDevice lampDevice = lampDeviceService.getById(singleLampById.getDeviceId());*/
                        Result result = singleLampParamService.setParam(filed, thresholdValue, systemDevice);
                        if(result.getCode()==400){
                            return result;
                        }
                    }
                }
            }
        }
        if(!lampDeviceThregholdList.isEmpty()){
            this.deleteByDeviceIdList(deviceIdList,request);
            this.saveBatch(lampDeviceThregholdList);
        }

        return new Result().success("添加成功");
    }

    @Override
    public Result delete(HttpServletRequest request) {
        List<SystemDevice> list = systemDeviceService.list();
        List<SystemDevice> systemDeviceList = list.stream().filter(p -> (p.getDeviceTypeId() <= 10)).collect(Collectors.toList());
        Boolean aBoolean = singleLampParamService.parameterClear(systemDeviceList);
        if(aBoolean){
            /*QueryWrapper<LampDeviceThreghold> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_id",id);
            baseMapper.delete(queryWrapper);*/
            baseMapper.delete(null);
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result updateThreghold(List<SlReqThresholdAddVO> slReqThresholdAddVOList, HttpServletRequest request) {
        List<LampDeviceThreghold> lampDeviceThregholdList = new ArrayList<>();
        for (SlReqThresholdAddVO slReqThresholdAddVO : slReqThresholdAddVOList) {
            String filed = slReqThresholdAddVO.getFiled();
            Integer deviceTypeId = slReqThresholdAddVO.getDeviceTypeId();
            SystemDeviceThreshold systemDeviceThreshold = systemDeviceThresholdService.getOneByFiled(filed,deviceTypeId);
            Integer thresholdId = systemDeviceThreshold.getId();
            QueryWrapper<LampDeviceThreghold> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_id", slReqThresholdAddVO.getId());
            queryWrapper.eq("threshold_id",thresholdId);
            LampDeviceThreghold lampDeviceThreghold = baseMapper.selectOne(queryWrapper);
            if(lampDeviceThreghold!=null){
                lampDeviceThreghold.setThresholdValue(slReqThresholdAddVO.getThresholdValue());
                lampDeviceThregholdList.add(lampDeviceThreghold);
            }
        }
        if(!lampDeviceThregholdList.isEmpty()){
            this.updateBatchById(lampDeviceThregholdList);
        }
        return new Result().success("修改成功");
    }

    @Override
    public Result deleteByDeviceIdList(List<Integer> deviceIdList, HttpServletRequest request) {
        QueryWrapper<LampDeviceThreghold> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("device_id",deviceIdList);
        this.remove(queryWrapper);
        return new Result().success("删除成功");
    }
}