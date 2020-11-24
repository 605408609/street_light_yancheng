/**
 * @filename:AlarmOrderSetServiceImpl 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service.impl;

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
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.woa.AlarmOrderSet;
import com.exc.street.light.resource.entity.woa.AlarmOrderSetLampPost;
import com.exc.street.light.resource.qo.WoaAlarmOrderSetQuery;
import com.exc.street.light.resource.quartz.ScheduleParam;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.req.WoaReqAlarmOrderSetVO;
import com.exc.street.light.resource.vo.resp.UaRespSimpleUserVO;
import com.exc.street.light.resource.vo.resp.WoaRespAlarmOrderSetDetailsVO;
import com.exc.street.light.resource.vo.resp.WoaRespAlarmOrderSetVO;
import com.exc.street.light.woa.config.parameter.HttpDlmApi;
import com.exc.street.light.woa.config.parameter.HttpUaApi;
import com.exc.street.light.woa.mapper.AlarmOrderSetMapper;
import com.exc.street.light.woa.quartz.QuartzManager;
import com.exc.street.light.woa.service.AlarmOrderSetLampPostService;
import com.exc.street.light.woa.service.AlarmOrderSetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class AlarmOrderSetServiceImpl extends ServiceImpl<AlarmOrderSetMapper, AlarmOrderSet> implements AlarmOrderSetService {
    private static final Logger logger = LoggerFactory.getLogger(AlarmOrderSetServiceImpl.class);

    @Autowired
    private AlarmOrderSetLampPostService alarmOrderSetLampPostService;
    @Autowired
    private QuartzManager quartzManager;
    @Autowired
    private HttpDlmApi httpDlmApi;
    @Autowired
    private HttpUaApi httpUaApi;

    @Autowired
    private LogUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(WoaReqAlarmOrderSetVO woaReqAlarmOrderSetVO, HttpServletRequest request) {
        logger.info("新增工单生成设置,接收参数：{}", woaReqAlarmOrderSetVO);
        // 持久化工单设置对象，得到id
        AlarmOrderSet alarmOrderSet = new AlarmOrderSet();
        alarmOrderSet.setCreateTime(new Date());
        alarmOrderSet.setHandleDuration(woaReqAlarmOrderSetVO.getHandleDuration());
        alarmOrderSet.setCreator(1);
        alarmOrderSet.setProcessor(woaReqAlarmOrderSetVO.getProcessor());
        this.save(alarmOrderSet);
        // 构建工单生成设置和路灯中间表数据
        List<AlarmOrderSetLampPost> alarmOrderSetLampPostList = new ArrayList<>();
        // 灯杆id集合
        List<Integer> lampPostIdList = woaReqAlarmOrderSetVO.getLampPostIdList();
        // 添加list数据
        for (Integer lampPostId : lampPostIdList) {
            AlarmOrderSetLampPost alarmOrderSetLampPost = new AlarmOrderSetLampPost();
            alarmOrderSetLampPost.setCreateTime(new Date());
            alarmOrderSetLampPost.setLampPostId(lampPostId);
            alarmOrderSetLampPost.setSetId(alarmOrderSet.getId());
            alarmOrderSetLampPostList.add(alarmOrderSetLampPost);
        }
        alarmOrderSetLampPostService.saveBatch(alarmOrderSetLampPostList);
        // 添加定时任务
        AlarmOrderSet byId = this.getById(alarmOrderSet.getId());
        String cron = "0 0/" + byId.getAutoDuration() + " * ? * 1,2,3,4,5,6,7";
        ScheduleParam scheduleParam = new ScheduleParam();
        scheduleParam.setJobKey(String.valueOf(byId.getId()));
        scheduleParam.setHttpServletRequest(request);
        scheduleParam.setCron(cron);
        scheduleParam.setLampPostIdList(lampPostIdList);
        Result result = new Result();
        try {
            quartzManager.addCronCycleJob(scheduleParam);
        } catch (Exception e) {
            e.printStackTrace();
            return result.error("新增自动生成工单设置失败");
        }
        return result.success("新增自动生成工单设置成功");
    }

    @Override
    public Result queryAlarmSet(WoaAlarmOrderSetQuery woaAlarmOrderSetQuery, HttpServletRequest httpServletRequest) {
        logger.info("条件查询告警设置，接收参数：{}", woaAlarmOrderSetQuery);
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            woaAlarmOrderSetQuery.setAreaId(user.getAreaId());
        }
        IPage<WoaAlarmOrderSetQuery> page = new Page<WoaAlarmOrderSetQuery>(woaAlarmOrderSetQuery.getPageNum(), woaAlarmOrderSetQuery.getPageSize());
        IPage<WoaRespAlarmOrderSetVO> woaRespAlarmOrderSetVOIPage = this.baseMapper.query(page, woaAlarmOrderSetQuery);
        Result result = new Result();
        return result.success(woaRespAlarmOrderSetVOIPage);
    }

    @Override
    public Result batchDelete(String ids, HttpServletRequest request) {
        logger.info("批量删除告警设置，接收参数：{}", ids);
        List<Integer> idListFromString = StringConversionUtil.getIdListFromString(ids);
        this.removeByIds(idListFromString);
        for (Integer setId : idListFromString) {
            quartzManager.stop(String.valueOf(setId));
        }
        LambdaQueryWrapper<AlarmOrderSetLampPost> wrapper = new LambdaQueryWrapper();
        wrapper.in(AlarmOrderSetLampPost::getSetId, idListFromString);
        alarmOrderSetLampPostService.remove(wrapper);
        Result result = new Result();
        return result.success("批量删除成功");
    }

    @Override
    public Result updateAlarmOrderSet(WoaReqAlarmOrderSetVO woaReqAlarmOrderSetVO, HttpServletRequest request) {
        logger.info("修改工单生成设置,接收参数：{}", woaReqAlarmOrderSetVO);
        // 获取数据库保存数据对象
        AlarmOrderSet alarmOrderSet = this.getById(woaReqAlarmOrderSetVO.getId());
        // 停止当前定时任务
        quartzManager.stop(String.valueOf(woaReqAlarmOrderSetVO.getId()));
        // 删除当前与灯杆关联关系
        LambdaQueryWrapper<AlarmOrderSetLampPost> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(AlarmOrderSetLampPost::getSetId, woaReqAlarmOrderSetVO.getId());
        alarmOrderSetLampPostService.remove(deleteWrapper);
        // 构建工单生成设置和路灯中间表数据
        List<AlarmOrderSetLampPost> alarmOrderSetLampPostList = new ArrayList<>();
        // 灯杆id集合
        List<Integer> lampPostIdList = woaReqAlarmOrderSetVO.getLampPostIdList();
        // 添加list数据
        for (Integer lampPostId : lampPostIdList) {
            AlarmOrderSetLampPost alarmOrderSetLampPost = new AlarmOrderSetLampPost();
            alarmOrderSetLampPost.setCreateTime(new Date());
            alarmOrderSetLampPost.setLampPostId(lampPostId);
            alarmOrderSetLampPost.setSetId(alarmOrderSet.getId());
            alarmOrderSetLampPostList.add(alarmOrderSetLampPost);
        }
        alarmOrderSetLampPostService.saveBatch(alarmOrderSetLampPostList);
        // 修改告警自动生成工单设置数据
        alarmOrderSet.setHandleDuration(woaReqAlarmOrderSetVO.getHandleDuration());
        alarmOrderSet.setProcessor(woaReqAlarmOrderSetVO.getProcessor());
        this.updateById(alarmOrderSet);
        // 添加定时任务
        AlarmOrderSet byId = this.getById(alarmOrderSet.getId());
        String cron = "0 0/" + byId.getAutoDuration() + " * ? * 1,2,3,4,5,6,7";
        ScheduleParam scheduleParam = new ScheduleParam();
        scheduleParam.setJobKey(String.valueOf(byId.getId()));
        scheduleParam.setHttpServletRequest(request);
        scheduleParam.setCron(cron);
        scheduleParam.setLampPostIdList(lampPostIdList);
        Result result = new Result();
        try {
            quartzManager.addCronCycleJob(scheduleParam);
        } catch (Exception e) {
            e.printStackTrace();
            return result.error("修改自动生成工单设置失败");
        }
        return result.success(alarmOrderSet);
    }

    @Override
    public Result get(Integer id, HttpServletRequest request) {
        logger.info("工单生成设置详情, id=" + id);
        Result result = new Result();
        // 工单生成设置对象
        AlarmOrderSet alarmOrderSet = this.getById(id);
        if (alarmOrderSet == null) {
            return result.error("没有对应设置");
        }
        // 获取灯杆id列表
        LambdaQueryWrapper<AlarmOrderSetLampPost> wrapper = new LambdaQueryWrapper();
        wrapper.eq(AlarmOrderSetLampPost::getSetId, id);
        List<AlarmOrderSetLampPost> alarmOrderSetLampPostList = alarmOrderSetLampPostService.list(wrapper);
        List<Integer> lampPostIdList = alarmOrderSetLampPostList.stream().map(AlarmOrderSetLampPost::getLampPostId).collect(Collectors.toList());
        // 获取路灯集合
        List<SlLampPost> slLampPostList = null;
        // 设置请求头
        Map<String, String> postMap = new HashMap<>();
        postMap.put("token",request.getHeader("token"));
        postMap.put("Content-Type", "application/json");

        // 拼接json
        String json = "[";
        for (int i = 0; i < lampPostIdList.size(); i++) {
            if (i != 0) {
                json += ",";
            }
            json += lampPostIdList.get(i);
        }
        json += "]";
        // 获得灯杆详情
        try {
            JSONObject lampPostResult = JSON.parseObject(HttpUtil.post(httpDlmApi.getUrl() + httpDlmApi.getGetLampPostByIdList(), json, postMap));
            JSONArray lampPostResultArr = lampPostResult.getJSONArray("data");
            slLampPostList = JSON.parseObject(lampPostResultArr.toJSONString(), new TypeReference<List<SlLampPost>>() {
            });
        } catch (Exception e) {
            logger.error("根据灯杆id集合获取灯杆集合接口调用失败，返回为空！");
            e.printStackTrace();
        }
        // 获取用户信息
        String name = null;
        Integer areaId = null;
        if (alarmOrderSet.getProcessor() == 0) {
            name = "系统自动生成";
        } else {
//            JSONObject slDeviceResult = JSON.parseObject(HttpUtil.get(httpUaApi.getUrl() + httpUaApi.getGetById() + "/" + alarmOrderSet.getProcessor(), getMap));
//            JSONObject slDeviceResultObj = slDeviceResult.getJSONObject("data");
//            name = (String) slDeviceResultObj.get("name");
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("token",request.getHeader("token"));
            JSONObject uaDeviceResult = JSON.parseObject(HttpUtil.get(httpUaApi.getUrl() + httpUaApi.getSelectName() + "?ids=" + alarmOrderSet.getProcessor(),headerMap));
            JSONArray uaDeviceResultArr = uaDeviceResult.getJSONArray("data");
            List<User> users = JSON.parseObject(uaDeviceResultArr.toJSONString(), new TypeReference<List<User>>() {
            });
            if (users != null && users.size() > 0) {
                name = users.get(0).getName();
                areaId = users.get(0).getAreaId();
            }
        }
        UaRespSimpleUserVO user = new UaRespSimpleUserVO();
        user.setId(alarmOrderSet.getProcessor());
        user.setName(name);
        user.setAreaId(areaId);
        List<String> areaUserId = new ArrayList<>();
        areaUserId.add("area" + areaId);
        areaUserId.add("user" + alarmOrderSet.getProcessor());
        user.setAreaUserId(areaUserId);
        // 构建返回对象
        WoaRespAlarmOrderSetDetailsVO woaRespAlarmOrderSetDetailsVO = new WoaRespAlarmOrderSetDetailsVO();
        woaRespAlarmOrderSetDetailsVO.setSetId(alarmOrderSet.getId());
        woaRespAlarmOrderSetDetailsVO.setHandleDuration(alarmOrderSet.getHandleDuration());
        woaRespAlarmOrderSetDetailsVO.setLampPostList(slLampPostList);
        woaRespAlarmOrderSetDetailsVO.setUser(user);
        return result.success(woaRespAlarmOrderSetDetailsVO);
    }

    @Override
    public Result control(Integer id, Integer status, HttpServletRequest httpServletRequest) {
        logger.info("控制设置开关，接收参数：id=" + id + "，status=" + status);
        Result result = new Result();
        AlarmOrderSet alarmOrderSet = new AlarmOrderSet();
        alarmOrderSet.setId(id);
        alarmOrderSet.setStatus(status);
        this.updateById(alarmOrderSet);
        // 0关1开
        if (status == 1) {
            LambdaQueryWrapper<AlarmOrderSetLampPost> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AlarmOrderSetLampPost::getSetId, id);
            List<AlarmOrderSetLampPost> list = alarmOrderSetLampPostService.list(wrapper);
            List<Integer> lampPostIdList = list.stream().map(AlarmOrderSetLampPost::getLampPostId).distinct().collect(Collectors.toList());
            // 添加定时任务
            AlarmOrderSet byId = this.getById(alarmOrderSet.getId());
            String cron = "0 0/" + byId.getAutoDuration() + " * ? * 1,2,3,4,5,6,7";
            ScheduleParam scheduleParam = new ScheduleParam();
            scheduleParam.setJobKey(String.valueOf(byId.getId()));
            scheduleParam.setHttpServletRequest(httpServletRequest);
            scheduleParam.setCron(cron);
            scheduleParam.setLampPostIdList(lampPostIdList);
            try {
                quartzManager.addCronCycleJob(scheduleParam);
            } catch (Exception e) {
                e.printStackTrace();
                return result.error("开启设置失败");
            }
        } else {
            quartzManager.stop(String.valueOf(id));
        }
        return result.success("控制成功");
    }

}