/**
 * @filename:WifiApDeviceServiceImpl 2020-03-16
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
import com.exc.street.light.resource.entity.wifi.WifiAp;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.resp.WifiRespApDeviceVO;
import com.exc.street.light.resource.vo.resp.WifiRespSimpleApVO;
import com.exc.street.light.wifi.config.HttpApi;
import com.exc.street.light.wifi.mapper.WifiApDao;
import com.exc.street.light.wifi.mapper.WifiApDeviceDao;
import com.exc.street.light.wifi.qo.ApDeviceQueryObject;
import com.exc.street.light.wifi.service.WifiApDeviceService;
import com.exc.street.light.wifi.utils.Constants;
import com.exc.street.light.wifi.utils.SNMPUtils;
import com.exc.street.light.wifi.utils.StringDealUtils;
import org.snmp4j.smi.VariableBinding;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: ap设备(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WifiApDeviceServiceImpl extends ServiceImpl<WifiApDeviceDao, WifiApDevice> implements WifiApDeviceService {

    @Autowired
    private HttpApi httpApi;

    @Autowired
    private WifiApDeviceDao wifiApDeviceDao;

    @Autowired
    private WifiApDao wifiApDao;

    @Autowired
    private LogUserService userService;

    @Override
    public Result add(WifiApDevice wifiApDevice, HttpServletRequest request) {
        wifiApDevice.setCreateTime(new Date());
        wifiApDeviceDao.insert(wifiApDevice);
        Result result = new Result();
        return result.success("添加成功");
    }

    @Override
    public Result modify(WifiApDevice wifiApDevice, HttpServletRequest request) {
        wifiApDeviceDao.updateById(wifiApDevice);
        Result result = new Result();
        return result.success("编辑成功");
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        wifiApDeviceDao.deleteById(id);
        // 删除设备时，也要删除wifi_ap表中的对应的数据
        LambdaQueryWrapper<WifiAp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WifiAp::getDeviceId, id);
        wifiApDao.delete(wrapper);
        Result result = new Result();
        return result.success("删除成功");
    }

    @Override
    public Result get(Integer id, HttpServletRequest request) {
        WifiApDevice wifiApDevice = wifiApDeviceDao.selectById(id);
        WifiRespApDeviceVO apDeviceVO = new WifiRespApDeviceVO();
        BeanUtils.copyProperties(wifiApDevice, apDeviceVO);
        try {
            // 获取灯杆名称
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("token",request.getHeader("token"));
            JSONObject lampPostResult = JSON.parseObject(HttpUtil.get(httpApi.getUrl() + httpApi.getLampPostById()+ "/" + wifiApDevice.getLampPostId(), headerMap));
            JSONObject lampPostResultObj = lampPostResult.getJSONObject("data");
            apDeviceVO.setLampPostName(lampPostResultObj.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("device_id", wifiApDevice.getId());
        WifiAp wifiAp = wifiApDao.selectOne(queryWrapper);
        if (wifiAp != null) {
            apDeviceVO.setPopulation(wifiAp.getCurrentPopulation());
            apDeviceVO.setAvgFlow(wifiAp.getAvgFlow());
            apDeviceVO.setDayConn(wifiAp.getDayConn());
            apDeviceVO.setDayConn(wifiAp.getDayConn());
            apDeviceVO.setCount(wifiAp.getCount());
            apDeviceVO.setProbability(wifiAp.getProbability());
            apDeviceVO.setOnlineTime(wifiAp.getOnlineTime());
            apDeviceVO.setNetflow(wifiAp.getNetflow());
        }
        Result result = new Result();
        return result.success(apDeviceVO);
    }

    @Override
    public Result getList(HttpServletRequest request, ApDeviceQueryObject qo) {
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        Page<WifiRespApDeviceVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<WifiRespApDeviceVO> list = wifiApDeviceDao.getPageList(page, qo);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result batchDelete(HttpServletRequest request, String ids) {
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);
        wifiApDeviceDao.deleteBatchIds(idList);
        Result result = new Result();
        return result.success("删除成功");
    }

    @Override
    public Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request) {
        LambdaQueryWrapper<WifiApDevice> wrapper = new LambdaQueryWrapper<>();
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            wrapper.in(WifiApDevice::getLampPostId, lampPostIdList);
        }
        List<WifiApDevice> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result nameUniqueness(Integer id, String name, String num) {
        Result result = new Result();
        Integer isUniqueness = 0;
        QueryWrapper<WifiApDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        QueryWrapper<WifiApDevice> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("num", num);
        WifiApDevice wifiApDeviceByName = wifiApDeviceDao.selectOne(queryWrapper);
        WifiApDevice wifiApDeviceByNum = wifiApDeviceDao.selectOne(queryWrapper1);
        // 编辑时判重
        if (id != null) {
            if (wifiApDeviceByName != null && !wifiApDeviceByName.getId().equals(id)) {
                isUniqueness = 1;
                return result.success("设备名称已存在", isUniqueness);
            }
            if (wifiApDeviceByNum != null && !wifiApDeviceByNum.getId().equals(id)) {
                isUniqueness = 2;
                return result.success("设备编号已存在", isUniqueness);
            }
        } else if (wifiApDeviceByName != null){
            isUniqueness = 1;
            return result.success("设备名称已存在", isUniqueness);
        } else if (wifiApDeviceByNum != null){
            isUniqueness = 2;
            return result.success("设备编号已存在", isUniqueness);
        }
        return result.success(isUniqueness);
    }

    @Override
    public Result getStatusApDevice() {
        // 查询所有的ap设备信息
        List<WifiApDevice> wifiApDevices = wifiApDeviceDao.selectList(null);
        // 过滤出ap所关联的所有的ac设备的ip
        List<String> acIpList = wifiApDevices.stream().map(WifiApDevice::getAcDeviceIp).distinct().collect(Collectors.toList());
        for (String acIp : acIpList) {
            // 获取该ac的ip下的所有关联的ap设备的ip
            List<VariableBinding> apIpList = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apIp);
            // 获取ac设备下所有ap的设备状态信息,oid获取的在离线状态 1：在线 2：离线
            List<VariableBinding> apStatusList = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apStatus);
            if (apIpList != null && apIpList.size() > 0) {
                for (VariableBinding binding : apIpList) {
                    String mac = StringDealUtils.stringDeal(binding.getOid().toString());
                    for (WifiApDevice apDevice : wifiApDevices) {
                        if (mac.equals(apDevice.getMac())) {
                            apDevice.setIp(binding.getVariable().toString());
                        }
                        wifiApDeviceDao.updateById(apDevice);
                    }
                }
            }
            if (apStatusList != null && apStatusList.size() > 0) {
                for (VariableBinding binding : apStatusList) {
                    String mac = StringDealUtils.stringDeal(binding.getOid().toString());
                    for (WifiApDevice apDevice : wifiApDevices) {
                        if (mac.equals(apDevice.getMac())) {
                            if (Integer.parseInt(binding.getVariable().toString()) == 1) {
                                apDevice.setNetworkState(1);
                                apDevice.setLastOnlineTime(new Date());
                            } else {
                                apDevice.setNetworkState(0);
                            }
                        }
                        wifiApDeviceDao.updateById(apDevice);
                    }
                }
            } else {
                for (WifiApDevice apDevice : wifiApDevices) {
                    apDevice.setNetworkState(0);
                    wifiApDeviceDao.updateById(apDevice);
                }
            }
        }
        Result result = new Result();
        return result.success("获取数据成功");
    }

    @Override
    public Result getByApp(Integer id) {
        WifiRespSimpleApVO simpleApVO = wifiApDeviceDao.getByApp(id);
        Result result = new Result();
        return result.success(simpleApVO);
    }

//    @Override
//    public Result numUniqueness(Integer id, String num) {
//        Integer isUniqueness = 0;
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("num", num);
//        WifiApDevice wifiApDevice = wifiApDeviceDao.selectOne(queryWrapper);
//        // 编辑时判重
//        if (id != null) {
//            if (wifiApDevice != null && !wifiApDevice.getId().equals(id)) {
//                isUniqueness = 1;
//            }
//        } else if (wifiApDevice != null){
//            isUniqueness = 1;
//        }
//        Result result = new Result();
//        return result.success(isUniqueness);
//    }
}