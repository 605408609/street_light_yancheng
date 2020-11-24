/**
 * @filename:LampEnergyDayServiceImpl 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.sl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.SlReqEnergyStatisticslVO;
import com.exc.street.light.sl.config.parameter.HttpDlmApi;
import com.exc.street.light.sl.mapper.LampEnergyDayMapper;
import com.exc.street.light.sl.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**   
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Service
public class LampEnergyDayServiceImpl  extends ServiceImpl<LampEnergyDayMapper, LampEnergyDay> implements LampEnergyDayService  {
    private static final Logger logger = LoggerFactory.getLogger(LampEnergyDayServiceImpl.class);

    @Autowired
    LampEnergyService lampEnergyService;
    @Autowired
    private LogUserService logUserService;
    @Autowired
    private LampDeviceService lampDeviceService;
    @Autowired
    private HttpDlmApi httpDlmApi;
    @Autowired
    private SystemDeviceService systemDeviceService;

    @Override
    public Result addEnergy(String dayTime) {
        logger.info("新增日能耗数据");
        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");
        Result result = lampEnergyService.select(null, dayTime);
        if(result.getCode()==400){
            return new Result().error("未查询到能耗数据");
        }
        List<LampEnergy> lampEnergyList = (List<LampEnergy>)result.getData();
        Float energyDay = 0f;
        for (LampEnergy lampEnergy : lampEnergyList) {
            energyDay += lampEnergy.getEnergy();
        }
        LampEnergyDay lampEnergyDay = new LampEnergyDay();
        try {
            Date parse = dateFormatJustDay.parse(dayTime);
            lampEnergyDay.setCreateTime(parse);
            lampEnergyDay.setEnergy(energyDay);
            lampEnergyDay.setDeviceId(1);
            baseMapper.insert(lampEnergyDay);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Result().error("新增日能耗数据失败");
        }
        return new Result().success("新增日能耗数据成功");
    }

    @Override
    public Result selectByEnergy(String monthTime) {
        List<LampEnergyDay> lampEnergyDayList = baseMapper.selectByEnergy(monthTime);
        return new Result().success(lampEnergyDayList);
    }

    @Override
    public LampEnergyDay selectOneByTime(Integer deviceId, String dayTime) {
        LampEnergyDay lampEnergyDay = baseMapper.selectOneByTime(deviceId,dayTime);
        return lampEnergyDay;
    }

    @Override
    public Result energy(SlReqEnergyStatisticslVO slReqEnergyStatisticslVO, HttpServletRequest request) {
        logger.info("获取能耗信息，接收参数：{}",slReqEnergyStatisticslVO);
        String startDate = slReqEnergyStatisticslVO.getStartDate();
        String endDate = slReqEnergyStatisticslVO.getEndDate();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Map<String,String> headMap = new HashMap<>();
        headMap.put("token",request.getHeader("token"));
        Integer areaId = null;
        if(userId!=1){
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("token",request.getHeader("token"));
            User user = logUserService.get(userId);
            areaId = user.getAreaId();
        }
        // 灯杆id集合
        //List<Integer> lampPostList = new ArrayList<>();
        // 站点id集合
        //List<Integer> siteIdList = slReqEnergyStatisticslVO.getSiteIdList();
        // 设备id集合
        List<Integer> deviceIdList = slReqEnergyStatisticslVO.getDeviceIdList();
        // 分组id集合
        List<Integer> groupIdList = slReqEnergyStatisticslVO.getGroupIdList();
        // 获取灯控器集合
        List<SystemDevice> lampDeviceVOList = new ArrayList<>();

        /*if((siteIdList == null || siteIdList.size()==0) && (groupIdList == null || groupIdList.size() == 0)){
            if(areaId!=null){
                lampPostList = lampDeviceService.areaLampPostIdList(areaId);
            }
        }*/
        if((deviceIdList == null || deviceIdList.size()==0) && (groupIdList == null || groupIdList.size() == 0)){
            if(areaId!=null){
                List<Integer> lampPostList = lampDeviceService.areaLampPostIdList(areaId);
                lampDeviceVOList = systemDeviceService.getListByLampPost(lampPostList, request);
                if(lampDeviceVOList==null||lampDeviceVOList.size()==0){
                    return new Result().error("当前目标没有设备");
                }
            }
        }
        // 优先按路灯灯杆id集合,获得灯具设备集合
        /*if (lampPostList != null && lampPostList.size() > 0) {
            lampDeviceVOList = systemDeviceService.getListByLampPost(lampPostList, request);
        }
        // 根据站点id集合获取灯具设备集合
        else if (siteIdList != null && siteIdList.size() > 0) {
            // 获取路灯集合
            List<SlLampPost> slLampPostList = null;
            String json = "siteIdList=";
            for (Integer siteId : siteIdList) {
                json += siteId + "&siteIdList=";
            }
            try {
                JSONObject lampPostResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetLampPostBySiteIdList() + "?" + json,headMap));
                JSONArray lampPostResultArr = lampPostResult.getJSONArray("data");
                slLampPostList = JSON.parseObject(lampPostResultArr.toJSONString(), new TypeReference<List<SlLampPost>>() {
                });
            } catch (Exception e) {
                logger.error("根据站点id集合获取灯杆集合接口调用失败，返回为空！");
                return new Result().error("根据站点id集合获取灯杆集合接口调用失败，返回为空！");
            }
            // 获取路灯灯具设备集合
            List<Integer> lampPostIdList = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
            LambdaQueryWrapper<SystemDevice> wrapper = new LambdaQueryWrapper();
            if (lampPostIdList != null && lampPostIdList.size() > 0) {
                wrapper.in(SystemDevice::getLampPostId, lampPostIdList);
            } else {
                wrapper.eq(SystemDevice::getId, 0);
            }
            lampDeviceVOList = systemDeviceService.list(wrapper);
        }*/
        if(deviceIdList !=null && deviceIdList.size()>0){
            Result deviceListByIdList = systemDeviceService.getDeviceListByIdList(deviceIdList, request);
            if(deviceListByIdList.getCode()==200&&deviceListByIdList.getData()!=null){
                lampDeviceVOList = (List<SystemDevice>) deviceListByIdList.getData();
            }
            if(lampDeviceVOList==null||lampDeviceVOList.size()==0){
                return new Result().error("当前目标没有设备");
            }
        }
        // 根据分组id集合获取灯具设备集合
        else if (groupIdList != null && groupIdList.size() > 0) {
            // 获取路灯灯杆集合
            List<SlLampPost> slLampPostList = null;
            String json = "groupIdList=";
            for (Integer groupId : groupIdList) {
                json += groupId + "&groupIdList=";
            }
            try {
                JSONObject ssDeviceResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetLampPostByGroupIdList() + "?" + json,headMap));
                JSONArray ssDeviceResultArr = ssDeviceResult.getJSONArray("data");
                slLampPostList = JSON.parseObject(ssDeviceResultArr.toJSONString(), new TypeReference<List<SlLampPost>>() {
                });
            } catch (Exception e) {
                logger.error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
                return new Result().error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
            }
            // 获取路灯灯具设备集合
            List<Integer> lampPostIdList = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
            LambdaQueryWrapper<SystemDevice> wrapper = new LambdaQueryWrapper();
            if (lampPostIdList != null && lampPostIdList.size() > 0) {
                wrapper.in(SystemDevice::getLampPostId, lampPostIdList);
            } else {
                wrapper.eq(SystemDevice::getId, 0);
            }
            lampDeviceVOList = systemDeviceService.list(wrapper);
            if(lampDeviceVOList==null||lampDeviceVOList.size()==0){
                return new Result().error("当前目标没有设备");
            }
        }
        List<LampEnergyDay> lampEnergyDayList = new ArrayList<>();
        /*if (lampDeviceVOList.isEmpty()&&areaId!=null) {
            return new Result().error("当前目标没有设备");
        }else {*/
            if(lampDeviceVOList.isEmpty()){
                List<SystemDevice> list = systemDeviceService.list();
                List<Integer> singleLampParamIdList = list.stream().map(SystemDevice::getId).collect(Collectors.toList());
                QueryWrapper<LampEnergyDay> queryWrapper = new QueryWrapper<>();
                if(singleLampParamIdList==null||singleLampParamIdList.size()==0){
                    singleLampParamIdList.add(-1);
                }
                queryWrapper.in("device_id",singleLampParamIdList);
                lampEnergyDayList = baseMapper.selectList(queryWrapper);
            }else {
                List<Integer> singleLampParamIdList = lampDeviceVOList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                QueryWrapper<LampEnergyDay> queryWrapper = new QueryWrapper<>();
                if(singleLampParamIdList==null||singleLampParamIdList.size()==0){
                    singleLampParamIdList.add(-1);
                }
                queryWrapper.in("device_id",singleLampParamIdList);
                lampEnergyDayList = baseMapper.selectList(queryWrapper);

            }
        //}

        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");

        List<String> timeList = new ArrayList<>();
        List<Float> energyList = new ArrayList<>();
        List<Integer> lightUpNumList = new ArrayList<>();
        List<Integer> lightNumList = new ArrayList<>();
        List<String> lightingRateList = new ArrayList<>();
        try {
            timeList.add(startDate);
            Float energtFirst = 0f;
            Integer lightUpNumFirst = 0;
            Integer lightNumFirst = 0;
            if(lampEnergyDayList!=null&&lampEnergyDayList.size()>0){
                for (LampEnergyDay lampEnergyDay : lampEnergyDayList) {
                    Date createTime = lampEnergyDay.getCreateTime();

                    String energyTime = dateFormatJustDay.format(createTime);
                    if(energyTime.equals(startDate)){
                        energtFirst += lampEnergyDay.getEnergy();
                        if(lampEnergyDay.getEnergy()!=0){
                            lightUpNumFirst++;
                        }
                        lightNumFirst++;
                    }
                }
            }
            energyList.add(new BigDecimal(energtFirst).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
            lightUpNumList.add(lightUpNumFirst);
            lightNumList.add(lightNumFirst);
            do {
                //天数加一
                Date start = dateFormatJustDay.parse(startDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date selectTime = calendar.getTime();
                String selectTimeString = dateFormatJustDay.format(selectTime);
                startDate = selectTimeString;
                timeList.add(startDate);

                Float energt = 0f;
                Integer lightUpNum = 0;
                Integer lightNum = 0;
                if(lampEnergyDayList!=null&&lampEnergyDayList.size()>0){
                    for (LampEnergyDay lampEnergyDay : lampEnergyDayList) {
                        Date createTime = lampEnergyDay.getCreateTime();
                        String energyTime = dateFormatJustDay.format(createTime);
                        if(energyTime.equals(startDate)){
                            energt += lampEnergyDay.getEnergy();
                            if(lampEnergyDay.getEnergy()!=0){
                                lightUpNum++;
                            }
                            lightNum++;
                        }
                    }
                }

                energyList.add(new BigDecimal(energt).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
                lightUpNumList.add(lightUpNum);
                lightNumList.add(lightNum);

            }while (!startDate.equals(endDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0;i < lightUpNumList.size();i++){
            Integer lightUpNum = lightUpNumList.get(i);
            String lightUpNumString = String.valueOf(lightUpNum);
            Integer lightNum = lightNumList.get(i);
            String lightNumString = String.valueOf(lightNum);

            String lightingRate = "0";
            if(lightUpNum!=0){
                lightingRate = new BigDecimal(Float.parseFloat(lightUpNumString)/Float.parseFloat(lightNumString)*100).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            }
            lightingRateList.add(lightingRate);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timeList",timeList);
        jsonObject.put("energyList",energyList);
        jsonObject.put("lightingRateList",lightingRateList);
        System.out.println(jsonObject);
        return new Result().success(jsonObject);
    }


}