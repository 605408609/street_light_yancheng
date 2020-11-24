/**
 * @filename:LocationStreetServiceImpl 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LocationStreetMapper;
import com.exc.street.light.dlm.service.LocationSiteService;
import com.exc.street.light.dlm.service.LocationStreetService;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationStreet;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class LocationStreetServiceImpl extends ServiceImpl<LocationStreetMapper, LocationStreet> implements LocationStreetService {
    private static final Logger logger = LoggerFactory.getLogger(LocationStreetServiceImpl.class);

    @Autowired
    private LocationSiteService locationSiteService;
    @Autowired
    private LogUserService logUserService;

    @Override
    public Result pulldown(Integer areaId, String streetName, HttpServletRequest request) {
        logger.info("查询街道下拉列表，接收参数areaId=" + areaId + ",streetName=" + streetName);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if(!flag){
            areaId = user.getAreaId();
        }
        LambdaQueryWrapper<LocationStreet> wrapper = new LambdaQueryWrapper();
        // 如果streetId等于空，则wrapper不用判断条件
        if (areaId != null) {
            wrapper.eq(LocationStreet::getAreaId, areaId);
        }
        if (streetName != null) {
            wrapper.like(LocationStreet::getName, streetName);
        }
        List<LocationStreet> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result add(LocationStreet locationStreet, HttpServletRequest request) {
        logger.info("添加街道，接收参数：{}", locationStreet);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if(!flag){
            if(!user.getAreaId().equals(locationStreet.getAreaId())){
                return result.error("只能创建自己分区所属街道");
            }
        }
        Result unique = this.unique(locationStreet, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            locationStreet.setCreateTime(new Date());
            boolean save = this.save(locationStreet);
            if (save) {
                result.success("添加街道成功");
            } else {
                result.success("添加街道失败");
            }
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result getByArea(List<Integer> areaIdList, HttpServletRequest request) {
        logger.info("根据区域id集合查询街道集合，接收参数:{}", areaIdList);
        LambdaQueryWrapper<LocationStreet> wrapper = new LambdaQueryWrapper();
        // 如果streetId等于空，则wrapper不用判断条件
        if (areaIdList != null && areaIdList.size() > 0) {
            wrapper.in(LocationStreet::getAreaId, areaIdList);
        }
        List<LocationStreet> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result delete(Long id, HttpServletRequest request) {
        logger.info("根据id删除街道，接收参数:{}", id);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if(!flag){
            LocationStreet byId = this.getById(id);
            if(!user.getAreaId().equals(byId.getAreaId())){
                return result.error("只能删除自己分区所属街道");
            }
        }
        this.removeById(id);
        // 删除站点
        List<Integer> idList = new ArrayList<>();
        idList.add(id.intValue());
        locationSiteService.deleteByStreetIdList(idList, request);
        return result.success("删除成功");
    }

    @Override
    public Result deleteByAreaIdList(List<Integer> areaIdList, HttpServletRequest request) {
        logger.info("根据区域id集合删除街道，接收参数:{}", areaIdList);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if (areaIdList != null && areaIdList.size() != 0) {
            // 获取区域下所有街道id集合
            LambdaQueryWrapper<LocationStreet> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(LocationStreet::getAreaId, areaIdList);
            List<LocationStreet> locationStreetList = this.list(wrapper);
            List<Integer> streetIdList = locationStreetList.stream().map(LocationStreet::getId).collect(Collectors.toList());
            if(!flag){
                for(LocationStreet street : locationStreetList){
                    if(!user.getAreaId().equals(street.getAreaId())){
                        return result.error("只能删除自己分区所属街道");
                    }
                }
            }
            // 删除街道
            this.remove(wrapper);
            // 删除站点
            locationSiteService.deleteByStreetIdList(streetIdList, request);
        }
        return result.success("批量删除成功");
    }

    @Override
    public Result unique(LocationStreet locationStreet, HttpServletRequest request) {
        logger.info("街道验证唯一性，接收参数：{}", locationStreet);
        Result result = new Result();
        if (null != locationStreet) {
            if(locationStreet.getAreaId() == null){
                return result.error("区域id不能为空");
            }
            if (locationStreet.getId() != null) {
                if (locationStreet.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LocationStreet> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LocationStreet::getAreaId, locationStreet.getAreaId());
                    wrapperName.eq(LocationStreet::getName, locationStreet.getName());
                    LocationStreet slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null && !slLampPostByName.getId().equals(locationStreet.getId())) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
            } else {
                if (locationStreet.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LocationStreet> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LocationStreet::getAreaId, locationStreet.getAreaId());
                    wrapperName.eq(LocationStreet::getName, locationStreet.getName());
                    LocationStreet slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
            }
        } else {
            return result.error("接收参数为空");
        }
        return result.success("");
    }

    @Override
    public Result updateStreet(LocationStreet locationStreet, HttpServletRequest request) {
        logger.info("修改街道，接收参数：{}", locationStreet);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if(!flag){
            if(!user.getAreaId().equals(locationStreet.getAreaId())){
                return result.error("只能修改自己分区所属街道");
            }
        }
        Result unique = this.unique(locationStreet, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            boolean rsg = this.updateById(locationStreet);
            if (rsg) {
                result.success("修改成功");
            } else {
                result.error("修改失败！");
            }
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result get(Long id, HttpServletRequest request) {
        Result result = new Result();
        LocationStreet locationStreet = baseMapper.selectById(id);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if(!flag){
            if(!user.getAreaId().equals(locationStreet.getAreaId())){
                return result.error("只能查看自己分区所属街道");
            }
        }
        return result.success(locationStreet);
    }
}