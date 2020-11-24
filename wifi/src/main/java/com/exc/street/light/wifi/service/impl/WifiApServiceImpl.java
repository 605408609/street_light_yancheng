/**
 * @filename:WifiApServiceImpl 2020-03-12
 * @project wifi  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.wifi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationSite;
import com.exc.street.light.resource.entity.dlm.LocationStreet;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.wifi.WifiAp;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.entity.wifi.WifiApHistory;
import com.exc.street.light.resource.qo.WifiChartStatisticQueryObject;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.resp.WifiRespApVO;
import com.exc.street.light.resource.vo.resp.WifiRespStatisticApVO;
import com.exc.street.light.wifi.config.HttpApi;
import com.exc.street.light.wifi.mapper.WifiApDao;
import com.exc.street.light.wifi.mapper.WifiApDeviceDao;
import com.exc.street.light.wifi.mapper.WifiApHistoryDao;
import com.exc.street.light.wifi.mapper.WifiUserDao;
import com.exc.street.light.wifi.qo.ApQueryObject;
import com.exc.street.light.wifi.service.WifiApService;
import com.exc.street.light.wifi.utils.Constants;
import com.exc.street.light.wifi.utils.SNMPUtils;
import com.exc.street.light.wifi.utils.StringDealUtils;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.VariableBinding;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: wifiAp(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WifiApServiceImpl extends ServiceImpl<WifiApDao, WifiAp> implements WifiApService {

    private static final Logger logger = LoggerFactory.getLogger(WifiApServiceImpl.class);

    @Autowired
    private WifiApDao wifiApDao;

    @Autowired
    private WifiApHistoryDao wifiApHistoryDao;

    @Autowired
    private WifiApDeviceDao wifiApDeviceDao;

    @Autowired
    private WifiUserDao wifiUserDao;

    @Autowired
    private HttpApi httpApi;

    @Autowired
    private LogUserService userService;

    @Override
    public Result getList(HttpServletRequest request, ApQueryObject qo) {
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        Page<WifiRespApVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<WifiRespApVO> list = wifiApDao.getPageList(page, qo.getName(), qo.getAreaId());
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result chartStatistic(HttpServletRequest request, WifiChartStatisticQueryObject queryObject) {
        Result result = new Result();
        String queryDate = queryObject.getQueryDate();
        List<WifiApHistory> wifiApList = new ArrayList<>();
        List<Integer> upFlowList = new ArrayList<>();
        List<Integer> downFlowList = new ArrayList<>();
        List<Integer> currentUserList = new ArrayList<>();
        // 查询昨天23时的ap历史信息
        List<WifiApHistory> wifiApListLastDay = new ArrayList<>();
        // 昨天23时的上行流量
        int sumUpFlowLastDay = 0;
        // 昨天23时的下行流量
        int sumDownFlowLastDay = 0;
        // 累计用户数
        Integer sumUserCount = 0;
        WifiRespStatisticApVO apVO = new WifiRespStatisticApVO();
        // 请求头携带token
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("token",request.getHeader("token"));
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            queryObject.setAreaId(user.getAreaId());
        }
        if (queryObject.getSiteId() != null) {
            List<Integer> siteIdList = new ArrayList<>();
            siteIdList.add(queryObject.getSiteId());
            queryObject.setSiteIdList(siteIdList);
        } else if (queryObject.getStreetId() != null) {
            // 获取站点id集合
            List<Integer> streetIdList = new ArrayList<>();
            List<LocationSite> siteList = new ArrayList<>();
            streetIdList.add(queryObject.getStreetId());
            String json = "streetIdList=";
            for (Integer streetId : streetIdList) {
                json += streetId + "&streetIdList=";
            }
            try {
                JSONObject streetResult = JSON.parseObject(HttpUtil.get(httpApi.getUrl() + httpApi.getGetSiteByStreetIdList() + "?" + json, headerMap));
                JSONArray streetResultArr = streetResult.getJSONArray("data");
                siteList = JSON.parseObject(streetResultArr.toJSONString(), new TypeReference<List<LocationSite>>() {
                });
            } catch (Exception e) {
                logger.error("根据街道id集合获取站点集合接口调用失败，返回为空！");
                return result.error("根据街道id集合获取站点集合接口调用失败！");
            }
            if (siteList != null && siteList.size() > 0 ) {
                List<Integer> siteIdList = siteList.stream().map(LocationSite::getId).collect(Collectors.toList());
                queryObject.setSiteIdList(siteIdList);
            }
        } else if (queryObject.getAreaId() != null) {
            // 获取街道id集合
            List<Integer> areaIdList = new ArrayList<>();
            List<LocationStreet> streetList = new ArrayList<>();
            areaIdList.add(queryObject.getAreaId());
            String json = "areaIdList=";
            for (Integer areaId : areaIdList) {
                json += areaId + "&areaIdList=";
            }
            try {
                JSONObject areaResult = JSON.parseObject(HttpUtil.get(httpApi.getUrl() + httpApi.getGetStreetByAreaIdList() + "?" + json, headerMap));
                JSONArray areaResultArr = areaResult.getJSONArray("data");
                streetList = JSON.parseObject(areaResultArr.toJSONString(), new TypeReference<List<LocationStreet>>() {
                });
            } catch (Exception e) {
                logger.error("根据区域id集合获取街道集合接口调用失败，返回为空！");
                return result.error("根据区域id集合获取街道集合接口调用失败！");
            }
            if (streetList != null && streetList.size() > 0) {
                List<Integer> streetIdList = streetList.stream().map(LocationStreet::getId).collect(Collectors.toList());
                // 获取站点id集合
                List<LocationSite> siteList = new ArrayList<>();
                String json2 = "streetIdList=";
                for (Integer streetId : streetIdList) {
                    json2 += streetId + "&streetIdList=";
                }
                try {
                    JSONObject streetResult = JSON.parseObject(HttpUtil.get(httpApi.getUrl() + httpApi.getGetSiteByStreetIdList() + "?" + json2, headerMap));
                    JSONArray streetResultArr = streetResult.getJSONArray("data");
                    siteList = JSON.parseObject(streetResultArr.toJSONString(), new TypeReference<List<LocationSite>>() {
                    });
                } catch (Exception e) {
                    logger.error("根据街道id集合获取站点集合接口调用失败，返回为空！");
                    return result.error("根据街道id集合获取站点集合接口调用失败！");
                }
                if (siteList != null && siteList.size() > 0) {
                    List<Integer> siteIdList = siteList.stream().map(LocationSite::getId).collect(Collectors.toList());
                    queryObject.setSiteIdList(siteIdList);
                }
            }
        }
        if (StringUtils.isNotBlank(queryDate)) {
            for (int i = 0; i < 24; i++) {
                if (i < 10) {
                    queryObject.setQueryDate(queryDate + " 0" + i);
                } else {
                    queryObject.setQueryDate(queryDate + " " + i);
                }
                // 根据站点id集合查找wifiAP信息
                wifiApList = wifiApHistoryDao.selectWifiApBySiteIdList(queryObject);

                if (i == 23) {
                    queryObject.setQueryDate(getLastDay(queryDate) + " " + i);
                    wifiApListLastDay = wifiApHistoryDao.selectWifiApBySiteIdList(queryObject);
                    for (WifiApHistory apHistory : wifiApListLastDay) {
                        sumUpFlowLastDay += apHistory.getUpFlow();
                        sumDownFlowLastDay += apHistory.getDownFlow();
                    }
                }
                // 上行流量
                int sumUpFlow = 0;
                // 下行流量
                int sumDownFlow = 0;
                // 当前用户数
                Integer currentUserCount = 0;
                // 用于统计每个时间点的各个设备当前用户数之和
                Integer currentUserSum = 0;
                // 峰值用户数
                Integer maxUserCount;
                // 获取当前时间的整点时间
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                // 获取当前ap的在、离线数
                if (i == hour && queryObject.getApDeviceId() == null) {
                    List<Integer> wifiDeviceIds = wifiApList.stream().map(WifiApHistory::getDeviceId).collect(Collectors.toList());
                    if (wifiDeviceIds.size() > 0) {
                        List<WifiApDevice> apDeviceList = wifiApDeviceDao.selectBatchIds(wifiDeviceIds);
                        List<WifiApDevice> apOfflineCount = apDeviceList.stream().filter(a -> a.getNetworkState().equals(0)).collect(Collectors.toList());
                        List<WifiApDevice> apOnlineCount = apDeviceList.stream().filter(a -> a.getNetworkState().equals(1)).collect(Collectors.toList());
                        apVO.setApOfflineCount(apOfflineCount.size());
                        apVO.setApOnlineCount(apOnlineCount.size());
                    }
                }
                for (WifiApHistory wifiAp : wifiApList) {
                    sumUpFlow += wifiAp.getUpFlow();
                    sumDownFlow += wifiAp.getDownFlow();
                    if (i == hour) {
                        currentUserCount += wifiAp.getCurrentPopulation();
                        // 当前用户数
                        apVO.setCurrentUserCount(currentUserCount);
                        // 通过查询wifi用户表，来确定当日认证成功的入网人数
                        sumUserCount += wifiAp.getDayConn();
                        // 累计用户数
                        apVO.setSumUserCount(sumUserCount);
                    }
                    currentUserSum += wifiAp.getCurrentPopulation();
                }
                // 将某一时刻所有设备的当前用户数之和加入到集合中，为了app端的人流量统计表
                currentUserList.add(currentUserSum);
                // 峰值用户数，用i==hour是为了查询之前日期同时刻的数据
                if (currentUserList.size() > 0 && i == hour) {
                    maxUserCount = currentUserList.stream().mapToInt(a -> a).max().getAsInt();
                    apVO.setMaxUserCount(maxUserCount);
                }
                upFlowList.add(sumUpFlow);
                downFlowList.add(sumDownFlow);
            }
            List<Integer> upList = getFlow(upFlowList, sumUpFlowLastDay);
            List<Integer> downList = getFlow(downFlowList, sumDownFlowLastDay);
            apVO.setUpFlowList(upList);
            apVO.setDownFlowList(downList);
            apVO.setCurrentUserList(currentUserList);
        }
        return result.success(apVO);
    }

    private List<Integer> getFlow(List<Integer> flowList, Integer upOrDownFlow) {
        List<Integer> upOrDownFlowList = new ArrayList<>();
        int df;
        for (int i = 0; i < flowList.size(); i++) {
            if (i == 0) {
                df = flowList.get(0) - upOrDownFlow;
                if (df < 0) {
                    df = 0;
                }
                upOrDownFlowList.add(df);
            } else {
                // 如果前一个时间点的流量为零，说明设备可能离线了
                if (flowList.get(i - 1) == 0) {
                    df = 0;
                } else {
                    df = flowList.get(i) - flowList.get(i - 1);
                }
                if (df < 0) {
                    df = 0;
                }
                upOrDownFlowList.add(df);
            }
        }
        return upOrDownFlowList;
    }

    // 获取前一天
    private static String getLastDay(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        // 此处修改为+1则是获取后一天
        calendar.set(Calendar.DATE, day - 1);

        return sdf.format(calendar.getTime());
    }

    @Override
    public Result getInfoApDevice() {
        // 查询所有的ap设备信息
        List<WifiApDevice> wifiApDevices = wifiApDeviceDao.selectList(null);
        // 过滤出ap所关联的所有的ac设备的ip
        if (wifiApDevices != null && wifiApDevices.size() > 0) {
            List<String> acIpList = wifiApDevices.stream().map(WifiApDevice::getAcDeviceIp).distinct().collect(Collectors.toList());
            for (String acIp : acIpList) {
                // 通过AC的ip查询关联的ap设备
                LambdaQueryWrapper<WifiApDevice> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(WifiApDevice::getAcDeviceIp, acIp);
                List<WifiApDevice> apDeviceList = wifiApDeviceDao.selectList(wrapper);
                if (apDeviceList != null && apDeviceList.size() > 0) {
                    // 当前连接ap且认证成功的人数
                    List<VariableBinding> apConnPeopleCount = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apConnPeopleCount);
                    // ap在线时长,单位秒
                    List<VariableBinding> apOnlineTime = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apOnlineTime);
                    // ap上行流量
                    List<VariableBinding> apUpFlow = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apUpFlow);
                    // ap下行流量
                    List<VariableBinding> apDownFlow = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apDownFlow);
                    // 用户认证成功的次数
                    List<VariableBinding> certSuccessTimes = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.certSuccessTimes);
                    // 用户请求认证的次数
                    List<VariableBinding> certTimes = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.certTimes);
                    for (WifiApDevice apDevice : apDeviceList) {
                        WifiAp wifiAp = getWifiAp(apConnPeopleCount, apOnlineTime, apUpFlow, apDownFlow, certSuccessTimes, certTimes, apDevice);
                        if (wifiAp != null) {
                            LambdaQueryWrapper<WifiAp> wrapper1 = new LambdaQueryWrapper<>();
                            wrapper1.eq(WifiAp::getDeviceId, apDevice.getId());
                            WifiAp ap = wifiApDao.selectOne(wrapper1);
                            // 存在则修改，不存在则新增
                            if (ap != null) {
                                // 获得当前日期
                                String currentDate = LocalDate.now().toString();
                                // 通过查询wifi用户表，来确定当日认证成功的入网人数
                                int count = wifiUserDao.selectDayConnCount(currentDate, apDevice.getId());
                                wifiAp.setDayConn(count);
                                // 查询该ap设备上用户使用的流量之和
                                int sumFlow = wifiUserDao.selectSumUserFlow(currentDate, apDevice.getId());
                                if (count > 0) {
                                    // 人均流量为当天的数据
                                    wifiAp.setAvgFlow(sumFlow / count);
                                } else {
                                    wifiAp.setAvgFlow(0);
                                }
                                wifiApDao.update(wifiAp, wrapper1);
                            } else {
                                wifiAp.setDeviceId(apDevice.getId());
                                wifiAp.setCreateTime(new Date());
                                wifiApDao.insert(wifiAp);
                            }
                        }
                    }
                }
            }
        }
        Result result = new Result();
        return result.success("数据同步成功");
    }

    @Override
    public Result getHistoryInfoApDevice() {
        // 查询所有的ap设备信息
        List<WifiApDevice> wifiApDevices = wifiApDeviceDao.selectList(null);
        // 过滤出ap所关联的所有的ac设备的ip
        if (wifiApDevices != null && wifiApDevices.size() > 0) {
            List<String> acIpList = wifiApDevices.stream().map(WifiApDevice::getAcDeviceIp).distinct().collect(Collectors.toList());
            for (String acIp : acIpList) {
                // 通过AC的ip查询关联的ap设备
                LambdaQueryWrapper<WifiApDevice> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(WifiApDevice::getAcDeviceIp, acIp);
                List<WifiApDevice> apDeviceList = wifiApDeviceDao.selectList(wrapper);
                if (apDeviceList != null && apDeviceList.size() > 0) {
                    // 当前ap连接人数
                    List<VariableBinding> apConnPeopleCount = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apConnPeopleCount);
                    // ap在线时长,单位秒
                    List<VariableBinding> apOnlineTime = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apOnlineTime);
                    // ap上行流量速率
                    List<VariableBinding> apUpFlowRate = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apUpFlow);
                    // ap下行流量速率
                    List<VariableBinding> apDownFlowRate = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.apDownFlow);
                    // 用户认证成功的次数
                    List<VariableBinding> certSuccessTimes = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.certSuccessTimes);
                    // 用户请求认证的次数
                    List<VariableBinding> certTimes = SNMPUtils.snmpAsynWalk(acIp, Constants.community, Constants.certTimes);
                    for (WifiApDevice apDevice : apDeviceList) {
                        WifiAp wifiAp = getWifiAp(apConnPeopleCount, apOnlineTime, apUpFlowRate, apDownFlowRate, certSuccessTimes, certTimes, apDevice);
                        // 将ap信息同步到历史数据库
                        WifiApHistory history = new WifiApHistory();
                        BeanUtils.copyProperties(wifiAp, history);
                        if (history.getCount() == null) {
                            history.setCount(0);
                        }
                        if (history.getPopulation() == null) {
                            history.setPopulation(0);
                        }
                        if (history.getDayConn() == null) {
                            history.setDayConn(0);
                        }
                        if (history.getOnlineTime() == null) {
                            history.setOnlineTime(0);
                        }
                        if (history.getCurrentPopulation() == null) {
                            history.setCurrentPopulation(0);
                        }
                        if (history.getUpFlow() == null || history.getDownFlow() == null) {
                            history.setUpFlow(0);
                            history.setDownFlow(0);
                            history.setNetflow(0);
                        } else {
                            history.setUpFlow(wifiAp.getUpFlow());
                            history.setDownFlow(wifiAp.getDownFlow());
                            history.setNetflow(history.getUpFlow() + history.getDownFlow());
                        }
                        if (history.getProbability() == null) {
                            history.setProbability(0);
                        }
                        history.setDeviceId(apDevice.getId());
                        history.setCreateTime(new Date());
                        // 获得当前日期
                        String currentDate = LocalDate.now().toString();
                        // 通过查询wifi用户表，来确定当日认证成功的入网人数
                        int count = wifiUserDao.selectDayConnCount(currentDate, apDevice.getId());
                        history.setDayConn(count);
                        // 查询该ap设备上用户使用的流量之和
                        int sumFlow = wifiUserDao.selectSumUserFlow(currentDate, apDevice.getId());
                        if (count > 0) {
                            history.setAvgFlow(sumFlow / count);
                        } else {
                            history.setAvgFlow(0);
                        }
                        wifiApHistoryDao.insert(history);
                    }
                }
            }
        }
        Result result = new Result();
        return result.success("数据同步成功");
    }

    public WifiAp getWifiAp(List<VariableBinding> apConnPeopleCount, List<VariableBinding> apOnlineTime, List<VariableBinding> apUpFlowRate, List<VariableBinding> apDownFlowRate, List<VariableBinding> certSuccessTimes, List<VariableBinding> certTimes, WifiApDevice apDevice) {
        WifiAp wifiAp = new WifiAp();
        // 当前连接ap且认证成功的人数
        int peopleCount;
        if (apConnPeopleCount != null && apConnPeopleCount.size() > 0) {
            for (VariableBinding binding : apConnPeopleCount) {
                String mac = StringDealUtils.stringDeal(binding.getOid().toString());
                if (apDevice.getMac().equals(mac)) {
                    peopleCount = binding.getVariable().toInt();
                    wifiAp.setCurrentPopulation(peopleCount);
                }
            }
        }
        // ap在线时长
        if (apOnlineTime != null && apOnlineTime.size() > 0) {
            for (VariableBinding binding : apOnlineTime) {
                String mac = StringDealUtils.stringDeal(binding.getOid().toString());
                if (apDevice.getMac().equals(mac)) {
                    // 在线时长
                    int time = StringDealUtils.StringDateToMinute(binding.getVariable().toString());
                    wifiAp.setOnlineTime(time);
                }
            }
        }
        // ap上行流量
        if (apUpFlowRate != null && apUpFlowRate.size() > 0) {
            for (VariableBinding binding : apUpFlowRate) {
                String mac = StringDealUtils.deleteLastChar(binding.getOid().toString());
                if (apDevice.getMac().equals(mac)) {
                    // 上行流量，单位字节(byte)，每分钟一次
                    long upFlowRate = binding.getVariable().toLong();
                    wifiAp.setUpFlow((int) (upFlowRate / (1024 * 1024)));
                }
            }
        }
        // ap下行流量
        if (apDownFlowRate != null && apDownFlowRate.size() > 0) {
            for (VariableBinding binding : apDownFlowRate) {
                String mac = StringDealUtils.deleteLastChar(binding.getOid().toString());
                if (apDevice.getMac().equals(mac)) {
                    // 下行流量(byte),每分钟一次
                    long downFlowRate = binding.getVariable().toLong();
                    wifiAp.setDownFlow((int) (downFlowRate / (1024 * 1024)));
                }
            }
        }
        if (wifiAp.getUpFlow() != null && wifiAp.getDownFlow() != null) {
            wifiAp.setNetflow(wifiAp.getUpFlow() + wifiAp.getDownFlow());
        }
        // 认证成功的次数,连接人数
        int successTimes = 0;
        if (certSuccessTimes != null && certSuccessTimes.size() > 0) {
            for (VariableBinding binding : certSuccessTimes) {
                String mac = StringDealUtils.stringDeal(binding.getOid().toString());
                if (apDevice.getMac().equals(mac)) {
                    successTimes = binding.getVariable().toInt();
                    wifiAp.setPopulation(successTimes);
                }
            }
        }
        // 认证请求的总次数，连接次数
        int times = 0;
        if (certTimes != null && certTimes.size() > 0) {
            for (VariableBinding binding : certTimes) {
                String mac = StringDealUtils.stringDeal(binding.getOid().toString());
                if (apDevice.getMac().equals(mac)) {
                    times = binding.getVariable().toInt();
                    wifiAp.setCount(times);
                }
            }
        }
        if (times > 0) {
            double successProbability = new BigDecimal((float) successTimes / times).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            // 认证成功率
            wifiAp.setProbability((int) (successProbability * 100));
        }
        return wifiAp;
    }
}