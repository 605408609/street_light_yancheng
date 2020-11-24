/**
 * @filename:LampEnergyServiceImpl 2020-03-20
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
import com.exc.street.light.resource.utils.DateUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.SlReqEnergyStatisticslVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import com.exc.street.light.sl.config.parameter.HttpDlmApi;
import com.exc.street.light.sl.mapper.LampEnergyDao;
import com.exc.street.light.sl.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
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
public class LampEnergyServiceImpl extends ServiceImpl<LampEnergyDao, LampEnergy> implements LampEnergyService {
    private static final Logger logger = LoggerFactory.getLogger(LampEnergyServiceImpl.class);

    @Autowired
    private LogUserService logUserService;
    @Autowired
    private LampDeviceService lampDeviceService;
    @Autowired
    private HttpDlmApi httpDlmApi;
    @Autowired
    private SingleLampParamService singleLampParamService;
    @Autowired
    private LampEnergyDayService lampEnergyDayService;
    @Autowired
    private LampEnergyMonthService lampEnergyMonthService;
    @Autowired
    private LampEnergyYearService lampEnergyYearService;

    @Override
    public Result cumulativeEnergyByTime(Integer deviceId,Float energy) {
        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
        String formatDay = simpleDateFormatDay.format(new Date());
        String formatMonth = simpleDateFormatMonth.format(new Date());
        String formatYear = simpleDateFormatYear.format(new Date());
        LampEnergyDay lampEnergyDay = lampEnergyDayService.selectOneByTime(deviceId, formatDay);
        LampEnergyMonth lampEnergyMonth = lampEnergyMonthService.selectOneByTime(deviceId, formatMonth);
        LampEnergyYear lampEnergyYear = lampEnergyYearService.selectOneByTime(deviceId, formatYear);
        if(lampEnergyDay!=null){
            lampEnergyDay.setEnergy(lampEnergyDay.getEnergy()+energy);
            lampEnergyDay.setCreateTime(new Date());
            lampEnergyDayService.updateById(lampEnergyDay);
        }else {
            lampEnergyDay = new LampEnergyDay();
            lampEnergyDay.setEnergy(energy);
            lampEnergyDay.setDeviceId(deviceId);
            lampEnergyDay.setCreateTime(new Date());
            lampEnergyDayService.save(lampEnergyDay);
        }

        if(lampEnergyMonth!=null){
            lampEnergyMonth.setEnergy(lampEnergyMonth.getEnergy()+energy);
            lampEnergyMonth.setCreateTime(new Date());
            lampEnergyMonthService.updateById(lampEnergyMonth);
        }else {
            lampEnergyMonth = new LampEnergyMonth();
            lampEnergyMonth.setEnergy(energy);
            lampEnergyMonth.setDeviceId(deviceId);
            lampEnergyMonth.setCreateTime(new Date());
            lampEnergyMonthService.save(lampEnergyMonth);
        }

        if(lampEnergyYear!=null){
            lampEnergyYear.setEnergy(lampEnergyYear.getEnergy()+energy);
            lampEnergyYear.setCreateTime(new Date());
            lampEnergyYearService.updateById(lampEnergyYear);
        }else {
            lampEnergyYear = new LampEnergyYear();
            lampEnergyYear.setEnergy(energy);
            lampEnergyYear.setDeviceId(deviceId);
            lampEnergyYear.setCreateTime(new Date());
            lampEnergyYearService.save(lampEnergyYear);
        }
        return new Result().success("");
    }

    /*@Override
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
        List<Integer> lampPostList = new ArrayList<>();
        // 站点id集合
        List<Integer> siteIdList = slReqEnergyStatisticslVO.getSiteIdList();
        // 分组id集合
        List<Integer> groupIdList = slReqEnergyStatisticslVO.getGroupIdList();
        // 获取灯控器集合
        List<LampDevice> lampDeviceVOList = new ArrayList<>();

        if((siteIdList == null || siteIdList.size()==0) && (groupIdList == null || groupIdList.size() == 0)){
            if(areaId!=null){
                lampPostList = lampDeviceService.areaLampPostIdList(areaId);
            }
        }
        // 优先按路灯灯杆id集合,获得灯具设备集合
        if (lampPostList != null && lampPostList.size() > 0) {
            Result result = lampDeviceService.getListByLampPost(lampPostList, request);
            lampDeviceVOList = (List<LampDevice>) result.getData();
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
            LambdaQueryWrapper<LampDevice> wrapper = new LambdaQueryWrapper();
            if (lampPostIdList != null && lampPostIdList.size() > 0) {
                wrapper.in(LampDevice::getLampPostId, lampPostIdList);
            } else {
                wrapper.eq(LampDevice::getId, 0);
            }
            lampDeviceVOList = lampDeviceService.list(wrapper);
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
            LambdaQueryWrapper<LampDevice> wrapper = new LambdaQueryWrapper();
            if (lampPostIdList != null && lampPostIdList.size() > 0) {
                wrapper.in(LampDevice::getLampPostId, lampPostIdList);
            } else {
                wrapper.eq(LampDevice::getId, 0);
            }
            lampDeviceVOList = lampDeviceService.list(wrapper);
        }
        List<LampEnergy> lampEnergyList = new ArrayList<>();
        if (lampDeviceVOList.isEmpty()&&areaId!=null) {
            return new Result().error("当前目标没有设备");
        }else {
            if(lampDeviceVOList.isEmpty()){
                lampEnergyList = this.list();
            }else {
                List<Integer> lampDeviceIdList = lampDeviceVOList.stream().map(LampDevice::getId).collect(Collectors.toList());
                List<SingleLampParam> singleLampByDeviceIds = singleLampParamService.getSingleLampByDeviceIds(lampDeviceIdList);
                List<Integer> singleLampParamIdList = singleLampByDeviceIds.stream().map(SingleLampParam::getId).collect(Collectors.toList());
                QueryWrapper<LampEnergy> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("device_id",singleLampParamIdList);
                lampEnergyList = baseMapper.selectList(queryWrapper);

            }
        }

        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);

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
            if(lampEnergyList!=null&&lampEnergyList.size()>0){
                for (LampEnergy lampEnergy : lampEnergyList) {
                    Date createTime = lampEnergy.getCreateTime();

                    String energyTime = dateFormatJustDay.format(createTime);
                    if(energyTime.equals(startDate)){
                        energtFirst += lampEnergy.getEnergy();
                        if(lampEnergy.getEnergy()!=0){
                            lightUpNumFirst++;
                        }
                        lightNumFirst++;
                    }
                }
            }
            String formatFirst = numberFormat.format(energtFirst);
            energyList.add(Float.parseFloat(formatFirst));
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
                if(lampEnergyList!=null&&lampEnergyList.size()>0){
                    for (LampEnergy lampEnergy : lampEnergyList) {
                        Date createTime = lampEnergy.getCreateTime();
                        String energyTime = dateFormatJustDay.format(createTime);
                        if(energyTime.equals(startDate)){
                            energt += lampEnergy.getEnergy();
                            if(lampEnergy.getEnergy()!=0){
                                lightUpNum++;
                            }
                            lightNum++;
                        }
                    }
                }
                String format = numberFormat.format(energt);
                energyList.add(Float.parseFloat(format));
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
                lightingRate = numberFormat.format(Float.parseFloat(lightUpNumString)/Float.parseFloat(lightNumString)*100);
            }
            lightingRateList.add(lightingRate);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timeList",timeList);
        jsonObject.put("energyList",energyList);
        jsonObject.put("lightingRateList",lightingRateList);
        System.out.println(jsonObject);
        return new Result().success(jsonObject);
    }*/

    @Override
    public Result select(Integer deviceId, String energyTime) {
        QueryWrapper<LampEnergy> queryWrapper = new QueryWrapper<>();
        if(deviceId!=null){
            queryWrapper.eq("device_id",deviceId);
        }
        queryWrapper.eq("energy_time",energyTime);
        List<LampEnergy> lampEnergyList = baseMapper.selectList(queryWrapper);
        if(lampEnergyList!=null){
            return new Result().success(lampEnergyList);
        }
        return new Result().error("未查询到结果");
    }

    @Override
    public Result energyInformation(HttpServletRequest request) {

        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject jsonObjectResult = new JSONObject();

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
            List<String> timeList = (List<String>)jsonObject.get("timeList");
            List<Float> energyList = (List<Float>)jsonObject.get("energyList");

            Float weekEnergy = 0f;
            Float monthEnergy = 0f;
            Float lastWeekEnergy = 0f;
            Float lastMonthEnergy = 0f;

            Float weekEnergySum = 0f;
            Float monthEnergySum = 0f;
            Float lastWeekEnergySum = 0f;
            Float lastMonthEnergySum = 0f;
            if(!(timeList.isEmpty()||energyList.isEmpty())){
                for (int i = 0; i<timeList.size(); i++){
                    String timeString = timeList.get(i);
                    Date time = dateFormatJustDay.parse(timeString);

                    Float energy = energyList.get(i);
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
            jsonObjectResult.put("lastMonthEnergy",lastMonthEnergy);
            jsonObjectResult.put("lastWeekEnergy",lastWeekEnergy);
            jsonObjectResult.put("monthEnergy",monthEnergy);
            jsonObjectResult.put("weekEnergy",weekEnergy);
        }catch (Exception e){

        }

        return new Result().success(jsonObjectResult);
    }

    @Override
    public Result deleteByEnergyTime(String energyTime) {
        QueryWrapper<LampEnergy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("energy_time",energyTime);
        baseMapper.delete(queryWrapper);
        return new Result().success("删除成功");
    }

    @Override
    public Result weekEnergy(HttpServletRequest request) {
        return null;
    }

    @Override
    public Result weekLightingRate(HttpServletRequest request) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        Date endTime = calendar.getTime();
        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = dateFormatJustDay.format(endTime);
        calendar.add(Calendar.DATE, -6);
        Date startTime = calendar.getTime();
        String startDate = dateFormatJustDay.format(startTime);
        SlReqEnergyStatisticslVO slReqEnergyStatisticslVO = new SlReqEnergyStatisticslVO();
        slReqEnergyStatisticslVO.setStartDate(startDate);
        slReqEnergyStatisticslVO.setEndDate(endDate);
        return lampEnergyDayService.energy(slReqEnergyStatisticslVO,request);
    }
}