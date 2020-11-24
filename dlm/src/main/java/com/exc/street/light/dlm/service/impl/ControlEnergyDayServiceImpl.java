/**
 * @filename:ControlEnergyDayServiceImpl 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.ControlEnergyDayMapper;
import com.exc.street.light.dlm.service.ControlEnergyDayService;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ControlEnergyDay;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.DlmReqControlEnergyStatisticVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class ControlEnergyDayServiceImpl extends ServiceImpl<ControlEnergyDayMapper, ControlEnergyDay> implements ControlEnergyDayService {

    private static final Logger logger = LoggerFactory.getLogger(ControlEnergyDayServiceImpl.class);

    @Autowired
    private LogUserService logUserService;

    @Override
    public ControlEnergyDay selectOneByDayTime(Integer controlId, String formatDay) {
        logger.info("查询日能耗: controlId:{} formatDay{}", controlId, formatDay);
        return baseMapper.selectOneByDayTime(controlId, formatDay);
    }

    @Override
    public Result energyStatistic(DlmReqControlEnergyStatisticVO energyStatisticVO, HttpServletRequest request) {
        logger.info("获取集控能耗统计报表数据: energyStatisticVO:{}", energyStatisticVO);
        String startDate = energyStatisticVO.getStartDate();
        String endDate = energyStatisticVO.getEndDate();
        // 根据token获取用户id
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean isAdmin = logUserService.isAdmin(userId);
        if (!isAdmin) {
            energyStatisticVO.setAreaId(user.getAreaId());
        }
        // 根据条件查询集控能耗数据(没有勾选的情况下，查该分区的所有数据，目前只有自己的能耗集控数据，所以就没有区分集控类型)
        List<ControlEnergyDay> controlEnergyDayList = baseMapper.selectControlEnergyDayList(energyStatisticVO);
        // 格式化
        SimpleDateFormat dateFormatJustDay = new SimpleDateFormat("yyyy-MM-dd");
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        // 构建时间返回对象集合
        List<String> timeList = new ArrayList<>();
        // 构建能耗返回对象集合
        List<Float> energyList = new ArrayList<>();
        try {
            if (controlEnergyDayList != null && controlEnergyDayList.size() > 0) {
                timeList.add(startDate);
                Float energyFirst = 0.0f;
                for (ControlEnergyDay controlEnergyDay : controlEnergyDayList) {
                    Date createTime = controlEnergyDay.getCreateTime();
                    String energyTime = dateFormatJustDay.format(createTime);
                    if (energyTime.equals(startDate)) {
                        energyFirst += controlEnergyDay.getEnergy();
                    }
                }
                String formatFirst = numberFormat.format(energyFirst);
                energyList.add(Float.parseFloat(formatFirst));
                while (!startDate.equals(endDate)) {
                    // 天数递增
                    Date start = dateFormatJustDay.parse(startDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    Date selectTime = calendar.getTime();
                    startDate = dateFormatJustDay.format(selectTime);
                    timeList.add(startDate);
                    Float energy = 0.0f;
                    for (ControlEnergyDay controlEnergyDay : controlEnergyDayList) {
                        Date createTime = controlEnergyDay.getCreateTime();
                        String energyTime = dateFormatJustDay.format(createTime);
                        if (energyTime.equals(startDate)) {
                            energy += controlEnergyDay.getEnergy();
                        }
                    }
                    String format = numberFormat.format(energy);
                    energyList.add(Float.parseFloat(format));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timeList", timeList);
        jsonObject.put("energyList", energyList);
        Result<Object> result = new Result<>();
        return result.success(jsonObject);
    }
}