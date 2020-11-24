/**
 * @filename:LocationSiteServiceImpl 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LocationSiteMapper;
import com.exc.street.light.dlm.service.LocationSiteService;
import com.exc.street.light.dlm.service.SlLampPostService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationSite;
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
public class LocationSiteServiceImpl extends ServiceImpl<LocationSiteMapper, LocationSite> implements LocationSiteService {
    private static final Logger logger = LoggerFactory.getLogger(LocationSiteServiceImpl.class);

    @Autowired
    private SlLampPostService slLampPostService;

    @Override
    public Result pulldown(Integer streetId, String siteName, HttpServletRequest request) {
        logger.info("查询站点下拉列表，接收参数：streetId=" + streetId + ",siteName=" + siteName);
        LambdaQueryWrapper<LocationSite> wrapper = new LambdaQueryWrapper();
        // 如果streetId等于空，则wrapper不用判断条件
        if (streetId != null) {
            wrapper.eq(LocationSite::getStreetId, streetId);
        }
        if (siteName != null) {
            wrapper.like(LocationSite::getName, siteName);
        }
        List<LocationSite> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result add(LocationSite locationSite, HttpServletRequest request) {
        logger.info("添加站点，接收参数：{}", locationSite);
        Result result = new Result();
        Result unique = this.unique(locationSite, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            locationSite.setCreateTime(new Date());
            boolean save = this.save(locationSite);
            if (save) {
                result.success("添加站点成功");
            } else {
                result.success("添加站点失败");
            }
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result getByStreet(List<Integer> streetIdList, HttpServletRequest request) {
        logger.info("根据街道id集合查询站点集合，接收参数:{}", streetIdList);
        LambdaQueryWrapper<LocationSite> wrapper = new LambdaQueryWrapper();
        // 如果streetId等于空，则wrapper不用判断条件
        if (streetIdList != null && streetIdList.size() > 0) {
            wrapper.in(LocationSite::getStreetId, streetIdList);
        }
        List<LocationSite> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result delete(Long id, HttpServletRequest request) {
        logger.info("删除站点，接收参数:{}", id);
        this.removeById(id);
        // 取消灯杆关联站点
        List<Integer> siteIdList = new ArrayList<>();
        siteIdList.add(id.intValue());
        slLampPostService.cancel(siteIdList, null, request);
        Result result = new Result();
        return result.success("删除成功");
    }

    @Override
    public Result deleteByStreetIdList(List<Integer> streetIdList, HttpServletRequest request) {
        logger.info("根据街道id集合删除站点，接收参数:{}", streetIdList);
        if ( streetIdList != null && streetIdList.size() != 0){
            LambdaQueryWrapper<LocationSite> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(LocationSite::getStreetId, streetIdList);
            // 获取站点id集合
            List<LocationSite> siteList = this.list(wrapper);
            List<Integer> siteIdList = siteList.stream().map(LocationSite::getId).collect(Collectors.toList());
            // 根据街道id集合删除站点
            this.remove(wrapper);
            // 取消灯杆关联站点
            slLampPostService.cancel(siteIdList, null, request);
        }
        Result result = new Result();
        return result.success("批量删除成功");
    }

    @Override
    public Result unique(LocationSite locationSite, HttpServletRequest request) {
        logger.info("站点验证唯一性，接收参数：{}", locationSite);
        Result result = new Result();
        if (null != locationSite) {
            if (locationSite.getId() != null) {
                if (locationSite.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LocationSite> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LocationSite::getName, locationSite.getName());
                    LocationSite slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null && !slLampPostByName.getId().equals(locationSite.getId())) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
            } else {
                if (locationSite.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LocationSite> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LocationSite::getName, locationSite.getName());
                    LocationSite slLampPostByName = this.getOne(wrapperName);
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
    public Result updateSite(LocationSite locationSite, HttpServletRequest request) {
        logger.info("修改站点，接收参数：{}", locationSite);
        Result result = new Result();
        Result unique = this.unique(locationSite, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            boolean rsg = this.updateById(locationSite);
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
        LocationSite locationSite = baseMapper.selectById(id);
        return new Result().success(locationSite);
    }

}