/**
 * @filename:WifiAcDeviceServiceImpl 2020-03-27
 * @project wifi  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.wifi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.wifi.WifiAcDevice;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.resp.WifiRespAcDeviceVO;
import com.exc.street.light.wifi.config.HttpApi;
import com.exc.street.light.wifi.mapper.WifiAcDeviceDao;
import com.exc.street.light.wifi.mapper.WifiApDeviceDao;
import com.exc.street.light.wifi.qo.AcDeviceQueryObject;
import com.exc.street.light.wifi.service.WifiAcDeviceService;
import com.exc.street.light.wifi.utils.Constants;
import com.exc.street.light.wifi.utils.SNMPUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: AC设备(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WifiAcDeviceServiceImpl extends ServiceImpl<WifiAcDeviceDao, WifiAcDevice> implements WifiAcDeviceService {

    @Autowired
    private HttpApi httpApi;

    @Autowired
    private WifiAcDeviceDao wifiAcDeviceDao;

    @Autowired
    private WifiApDeviceDao wifiApDeviceDao;

    @Autowired
    private LogUserService logUserService;

    @Override
    public Result add(WifiAcDevice wifiAcDevice, HttpServletRequest request) {
        wifiAcDevice.setCreateTime(new Date());
        wifiAcDeviceDao.insert(wifiAcDevice);
        Result result = new Result();
        return result.success("添加成功");
    }

    @Override
    public Result modify(WifiAcDevice wifiAcDevice, HttpServletRequest request) {
        wifiAcDeviceDao.updateById(wifiAcDevice);
        Result result = new Result();
        return result.success("编辑成功");
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        wifiAcDeviceDao.deleteById(id);
        Result result = new Result();
        return result.success("删除成功");
    }

    @Override
    public Result get(Integer id, HttpServletRequest request) {
        WifiAcDevice wifiAcDevice = wifiAcDeviceDao.selectById(id);
        WifiRespAcDeviceVO acDeviceVO = new WifiRespAcDeviceVO();
        BeanUtils.copyProperties(wifiAcDevice, acDeviceVO);
        try {
            // 获取区域名称
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("token",request.getHeader("token"));
            JSONObject lampPostResult = JSON.parseObject(HttpUtil.get(httpApi.getUrl() + httpApi.getAreaById()+"/" + wifiAcDevice.getAreaId(), headerMap));
            JSONObject areaResultObj = lampPostResult.getJSONObject("data");
            acDeviceVO.setAreaName(areaResultObj.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryWrapper<WifiApDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ac_device_ip", wifiAcDevice.getIp());
        List<WifiApDevice> wifiApDeviceList = wifiApDeviceDao.selectList(queryWrapper);
        if (wifiApDeviceList != null && wifiApDeviceList.size() > 0) {
            acDeviceVO.setApDeviceList(wifiApDeviceList);
        }
        Result<WifiRespAcDeviceVO> result = new Result<>();
        return result.success(acDeviceVO);
    }

    @Override
    public Result getList(HttpServletRequest request, AcDeviceQueryObject qo) {
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        Page<WifiRespAcDeviceVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<WifiRespAcDeviceVO> list = wifiAcDeviceDao.getPageList(page, qo);
        Result<IPage<WifiRespAcDeviceVO>> result = new Result<>();
        return result.success(list);
    }

    @Override
    public Result batchDelete(HttpServletRequest request, String ids) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);
        wifiAcDeviceDao.deleteBatchIds(idList);
        Result result = new Result();
        return result.success("删除成功");
    }

    @Override
    public Result getAcIpList(HttpServletRequest request) {
        // 根据当前用户的分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if (!flag) {
            LambdaQueryWrapper<WifiAcDevice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WifiAcDevice::getAreaId, user.getAreaId());
            List<WifiAcDevice> acDeviceList = wifiAcDeviceDao.selectList(wrapper);
            Result<List<WifiAcDevice>> result = new Result<>();
            return result.success(acDeviceList);
        }
        List<WifiAcDevice> acDeviceList = wifiAcDeviceDao.selectAll();
        Result<List<WifiAcDevice>> result = new Result<>();
        return result.success(acDeviceList);
    }

    @Override
    public Result nameUniqueness(Integer id, String name, String num) {
        Result result = new Result();
        Integer isUniqueness = 0;
        QueryWrapper<WifiAcDevice> queryWrapper = new QueryWrapper();
        QueryWrapper<WifiAcDevice> queryWrapper1 = new QueryWrapper();
        queryWrapper.eq("name", name);
        queryWrapper1.eq("num", num);
        WifiAcDevice wifiAcDeviceByName = wifiAcDeviceDao.selectOne(queryWrapper);
        WifiAcDevice wifiAcDeviceByNum = wifiAcDeviceDao.selectOne(queryWrapper1);
        // 编辑时判重
        if (id != null) {
            if (wifiAcDeviceByName != null && !wifiAcDeviceByName.getId().equals(id)) {
                isUniqueness = 1;
                return result.success("设备名称已存在", isUniqueness);
            }
            if (wifiAcDeviceByNum != null && !wifiAcDeviceByNum.getId().equals(id)) {
                isUniqueness = 2;
                return result.success("设备编号已存在", isUniqueness);
            }
        } else if (wifiAcDeviceByName != null){
            isUniqueness = 1;
            return result.success("设备名称已存在", isUniqueness);
        } else if (wifiAcDeviceByNum != null){
            isUniqueness = 2;
            return result.success("设备编号已存在", isUniqueness);
        }
        return result.success(isUniqueness);
    }

    @Override
    public Result getStatusAcDevice() {
        List<WifiAcDevice> acDeviceList = wifiAcDeviceDao.selectAll();
        if (acDeviceList != null && acDeviceList.size() > 0) {
            for (WifiAcDevice acDevice : acDeviceList) {
                Object object = SNMPUtils.snmpGet(acDevice.getIp(), Constants.community, Constants.cpu);
                // cpu使用率不为空，说明AC设备在线
                if (object != null) {
                    acDevice.setNetworkState(1);
                    acDevice.setLastOnlineTime(new Date());
                } else {
                    acDevice.setNetworkState(0);
                }
                // AC设备下的ap数量
                LambdaQueryWrapper<WifiApDevice> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(WifiApDevice::getAcDeviceIp, acDevice.getIp());
                List<WifiApDevice> apDeviceList = wifiApDeviceDao.selectList(wrapper);
                acDevice.setConnApCount(apDeviceList.size());
                wifiAcDeviceDao.updateById(acDevice);
            }
        }
        Result result = new Result();
        return result.success("获取数据成功");
    }

//    @Override
//    public Result numUniqueness(Integer id, String num) {
//        Integer isUniqueness = 0;
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("num", num);
//        WifiAcDevice wifiAcDevice = wifiAcDeviceDao.selectOne(queryWrapper);
//        // 编辑时判重
//        if (id != null) {
//            if (wifiAcDevice != null && !wifiAcDevice.getId().equals(id)) {
//                isUniqueness = 1;
//            }
//        } else if (wifiAcDevice != null){
//            isUniqueness = 1;
//        }
//        Result result = new Result();
//        return result.success(isUniqueness);
//    }
}