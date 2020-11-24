/**
 * @filename:LampEnergyMonthServiceImpl 2020-03-28
 * @project sl  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.DateUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.SlReqEnergyStatisticslVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import com.exc.street.light.resource.vo.resp.SlRespEnergyMonthlyListVO;
import com.exc.street.light.resource.vo.resp.SlRespEnergyMonthlyVO;
import com.exc.street.light.sl.mapper.LampEnergyMonthMapper;
import com.exc.street.light.sl.service.*;
import org.apache.shiro.authc.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class LampEnergyMonthServiceImpl extends ServiceImpl<LampEnergyMonthMapper, LampEnergyMonth> implements LampEnergyMonthService {
    private static final Logger logger = LoggerFactory.getLogger(LampEnergyMonthServiceImpl.class);

    @Autowired
    LampEnergyDayService lampEnergyDayService;
    @Autowired
    LampEnergyService lampEnergyService;
    @Autowired
    private LogUserService logUserService;
    @Autowired
    private LampDeviceService lampDeviceService;
    @Autowired
    private SystemDeviceService systemDeviceService;

    @Override
    public Result monthly(HttpServletRequest request) {
        logger.info("首页月度能耗数据");
        Result result = new Result();
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
        //获取灯具id集合
        List<Integer> singleLampParamIdList = new ArrayList<>();
        List<Integer> lampPostIdList = lampDeviceService.areaLampPostIdList(areaId);
        if(lampPostIdList!=null&&lampPostIdList.size()>0){
            List<SystemDevice> listByLampPost = systemDeviceService.getListByLampPost(lampPostIdList, request);
            /*List<Integer> lampDeviceIdList = listByLampPost.stream().map(SystemDevice::getId).collect(Collectors.toList());
            List<SingleLampParam> singleLampByDeviceIds = singleLampParamService.getSingleLampByDeviceIds(lampDeviceIdList);*/
            singleLampParamIdList = listByLampPost.stream().map(SystemDevice::getId).collect(Collectors.toList());
        }else {
            singleLampParamIdList.add(-1);
        }

        // 获取最近6个月的数据
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String currentDateString = simpleDateFormat.format(new Date());
        List<SlRespEnergyMonthlyVO> lampEnergyMonthList = baseMapper.monthly(currentDateString,singleLampParamIdList);
        // 构建返回数据(不足6月补足6月)
        SlRespEnergyMonthlyListVO slRespEnergyMonthlyListVO = new SlRespEnergyMonthlyListVO();
        List<String> yearMonthList = new ArrayList<>();
        List<Float> energyList = new ArrayList<>();
        if(lampEnergyMonthList.size()<6){
            Map<String,Float> lampEnergyMonthMap = lampEnergyMonthList.stream().collect(Collectors.toMap(SlRespEnergyMonthlyVO::getYearMonth, SlRespEnergyMonthlyVO::getEnergy));
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            for (int i =0; i < 6; i++){
                calendar.add(Calendar.MONTH, 1);
                Date date = calendar.getTime();
                String monthString = simpleDateFormat.format(date);
                if(lampEnergyMonthMap.get(monthString)!=null){
                    yearMonthList.add(monthString);
                    energyList.add(lampEnergyMonthMap.get(monthString));
                }else {
                    yearMonthList.add(monthString);
                    energyList.add(0f);
                }
            }
        /*Result monthResult = lampEnergyDayService.selectByEnergy(nowString);
        Float energyMonth = 0f;
        if(monthResult.getCode()==200){
            List<LampEnergyDay> lampEnergyDayList = (List<LampEnergyDay>)monthResult.getData();

            for (LampEnergyDay lampEnergyDay : lampEnergyDayList) {
                energyMonth += lampEnergyDay.getEnergy();
            }
        }
        energyList.add(energyMonth);*/
        }else {
            yearMonthList = lampEnergyMonthList.stream().map(SlRespEnergyMonthlyVO::getYearMonth).collect(Collectors.toList());
            energyList = lampEnergyMonthList.stream().map(SlRespEnergyMonthlyVO::getEnergy).collect(Collectors.toList());
        }
        slRespEnergyMonthlyListVO.setEnergyList(energyList);
        slRespEnergyMonthlyListVO.setYearMonthList(yearMonthList);
        //获取年度累计能耗数据
        Float yearEnergy = 0f;
        SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
        String format = simpleDateFormatYear.format(new Date());
        Result resultYear = this.selectByEnergy(singleLampParamIdList,format);
        List<LampEnergyMonth> lampEnergyMonths = (List<LampEnergyMonth>)resultYear.getData();
        if(lampEnergyMonths!=null){
            for (LampEnergyMonth lampEnergyMonth : lampEnergyMonths) {
                yearEnergy += lampEnergyMonth.getEnergy();
            }
        }
        /*Result resultMonth = lampEnergyDayService.selectByEnergy(currentDateString);
        List<LampEnergyDay> lampEnergyDayList = (List<LampEnergyDay>)resultMonth.getData();
        if(lampEnergyDayList!=null){
            for (LampEnergyDay lampEnergyDay : lampEnergyDayList) {
                yearEnergy += lampEnergyDay.getEnergy();
            }
        }*/
        slRespEnergyMonthlyListVO.setYearEnery(yearEnergy);
        //获取首页周月增长率
        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //本周的第一天
            Date weekBeginTemp = DateUtil.getWeekBeginOrEnd(0, true);
            String weekBeginString = dateFormatJustDay.format(weekBeginTemp);
            Date weekBegin = dateFormatJustDay.parse(weekBeginString);
            //本月的第一天
            Date monthBeginTemp = DateUtil.getMonthTime(0, true);
            String monthBeginString = dateFormatJustDay.format(monthBeginTemp);
            Date monthBegin = dateFormatJustDay.parse(monthBeginString);
            //上周的第一天
            Date lastWeekBeginTemp = DateUtil.getWeekBeginOrEnd(-1, true);
            String lastWeekBeginString = dateFormatJustDay.format(lastWeekBeginTemp);
            Date lastWeekBegin = dateFormatJustDay.parse(lastWeekBeginString);
            //上个月的第一天
            Date lastMonthBeginTemp = DateUtil.getMonthTime(-1, true);
            String lastMonthBeginString = dateFormatJustDay.format(lastMonthBeginTemp);
            Date lastMonthBegin = dateFormatJustDay.parse(lastMonthBeginString);


            //获取前一天的日期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date yesterday = calendar.getTime();
            String yesterdayString = dateFormatJustDay.format(yesterday);
            //获取上周的上述日期
            Calendar calendarWeek = Calendar.getInstance();
            calendarWeek.setTime(yesterday);
            calendarWeek.add(Calendar.WEEK_OF_MONTH, -1);
            Date lastWeekYesterday = calendarWeek.getTime();
            String lastWeekYesterdayString = dateFormatJustDay.format(lastWeekYesterday);
            //获取上个月的上述日期
            Calendar calendarMonth = Calendar.getInstance();
            calendarMonth.setTime(yesterday);
            calendarMonth.add(Calendar.MONTH, -1);
            Date lastMonthYesterday = calendarMonth.getTime();
            String lastMonthYesterdayString = dateFormatJustDay.format(lastMonthYesterday);


            SlReqEnergyStatisticslVO slReqEnergyStatisticslVO = new SlReqEnergyStatisticslVO();
            slReqEnergyStatisticslVO.setStartDate(lastMonthBeginString);
            slReqEnergyStatisticslVO.setEndDate(yesterdayString);
            Result energyResult = lampEnergyDayService.energy(slReqEnergyStatisticslVO, request);
            JSONObject jsonObject = (JSONObject)energyResult.getData();
            List<String> timeList = new ArrayList<>();
            List<Float> energyListParam = new ArrayList<>();
            if(energyResult.getCode()==200){
                timeList = (List<String>)jsonObject.get("timeList");
                energyListParam = (List<Float>)jsonObject.get("energyList");
            }

            Float weekEnergy = 0f;
            Float monthEnergy = 0f;
            Float lastWeekEnergy = 0f;
            Float lastMonthEnergy = 0f;

            Float weekEnergySum = 0f;
            Float monthEnergySum = 0f;
            Float lastWeekEnergySum = 0f;
            Float lastMonthEnergySum = 0f;
            if(!(timeList.isEmpty()||energyListParam.isEmpty())){
                for (int i = 0; i<timeList.size(); i++){
                    String timeString = timeList.get(i);
                    Date time = dateFormatJustDay.parse(timeString);

                    Float energy = energyListParam.get(i);
                    if(time.getTime()>=lastMonthBegin.getTime()){
                        lastMonthEnergySum += energy;
                    }
                    if(time.getTime()>=monthBegin.getTime()){
                        monthEnergySum += energy;
                    }
                    if(time.getTime()>=lastWeekBegin.getTime()){
                        lastWeekEnergySum += energy;
                    }
                    if(time.getTime()>=weekBegin.getTime()){
                        weekEnergySum += energy;
                    }

                    if(timeString.equals(lastMonthYesterdayString)){
                        lastMonthEnergy = lastMonthEnergySum;
                    }
                    if(timeString.equals(lastWeekYesterdayString)){
                        lastWeekEnergy = lastWeekEnergySum;
                    }
                    if(timeString.equals(yesterdayString)){
                        weekEnergy = weekEnergySum;
                        monthEnergy = monthEnergySum;
                    }

                }
            }
            slRespEnergyMonthlyListVO.setWeekEnergy(weekEnergy);
            slRespEnergyMonthlyListVO.setMonthEnergy(monthEnergy);
            slRespEnergyMonthlyListVO.setLastWeekEnergy(lastWeekEnergy);
            slRespEnergyMonthlyListVO.setLastMonthEnergy(lastMonthEnergy);
        }catch (Exception e){
            logger.info("周月能耗数据统计错误");
            e.printStackTrace();
            return result.error("周月能耗数据统计错误");
        }
        return result.success(slRespEnergyMonthlyListVO);
    }

    @Override
    public Result addEnergy(String monthTime) {
        logger.info("新增月能耗数据");
        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM");
        Result result = lampEnergyDayService.selectByEnergy(monthTime);
        if(result.getCode()==400){
            return new Result().error("未查询到能耗数据");
        }
        List<LampEnergyDay> lampEnergyDayList = (List<LampEnergyDay>)result.getData();
        Float energyMonth = 0f;
        for (LampEnergyDay lampEnergyDay : lampEnergyDayList) {
            energyMonth += lampEnergyDay.getEnergy();
        }
        LampEnergyMonth lampEnergyMonth = new LampEnergyMonth();
        try {
            Date parse = dateFormatJustDay.parse(monthTime);
            lampEnergyMonth.setCreateTime(parse);
            lampEnergyMonth.setEnergy(energyMonth);
            lampEnergyMonth.setDeviceId(1);
            baseMapper.insert(lampEnergyMonth);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result().error("新增月能耗数据失败");
        }
        return new Result().success("新增月能耗数据成功");
    }

    @Override
    public Result selectByEnergy(List<Integer> deviceIdList,String monthTime) {
        List<LampEnergyMonth> lampEnergyMonthList = baseMapper.selectByEnergy(deviceIdList,monthTime);
        return new Result().success(lampEnergyMonthList);
    }

    @Override
    public LampEnergyMonth selectOneByTime(Integer deviceId, String monthTime) {
        LampEnergyMonth lampEnergyMonth = baseMapper.selectOneByTime(deviceId,monthTime);
        return lampEnergyMonth;
    }
}