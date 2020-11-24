/**
 * @filename:LampStrategyServiceImpl 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.SlLampStrategyQuery;
import com.exc.street.light.resource.utils.DataUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.SlControlSystemDeviceVO;
import com.exc.street.light.resource.vo.req.SlReqLampStrategyExecuteVO;
import com.exc.street.light.resource.vo.req.SlReqStrategyActionVO;
import com.exc.street.light.resource.vo.req.SlReqStrategyVO;
import com.exc.street.light.resource.vo.req.htLamp.HtSetSingleLampOutputPlanRequestVO;
import com.exc.street.light.resource.vo.req.htLamp.HtSingleLampPlanChildRequestVo;
import com.exc.street.light.resource.vo.req.htLamp.HtSingleLampPlanRequestVO;
import com.exc.street.light.resource.vo.resp.DlmRespLocationControlMixVO;
import com.exc.street.light.resource.vo.resp.SlRespStrategyActionVO;
import com.exc.street.light.resource.vo.resp.SlRespStrategyVO;
import com.exc.street.light.sl.VO.TimeTableVO;
import com.exc.street.light.sl.config.parameter.HttpDlmApi;
import com.exc.street.light.sl.config.parameter.HttpSlApi;
import com.exc.street.light.sl.mapper.LampStrategyMapper;
import com.exc.street.light.sl.service.*;
import com.exc.street.light.sl.utils.SendHttpsUtil;
import com.exc.street.light.sl.utils.ZkzlProtocolUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 灯具策略(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LampStrategyServiceImpl extends ServiceImpl<LampStrategyMapper, LampStrategy> implements LampStrategyService {

    private static final Logger logger = LoggerFactory.getLogger(LampStrategyServiceImpl.class);

    @Autowired
    private LampStrategyDeviceTypeService lampStrategyDeviceTypeService;

    @Autowired
    private LampStrategyTimingModeService lampStrategyTimingModeService;

    @Autowired
    private SystemDeviceTypeTimingModeService systemDeviceTypeTimingModeService;

    @Autowired
    private LampStrategyActionService lampStrategyActionService;

    @Autowired
    private LampStrategyActionTimingModeService lampStrategyActionTimingModeService;

    @Autowired
    private LampStrategyActionDeviceTypeService lampStrategyActionDeviceTypeService;

    @Autowired
    private LampLightModeService lampLightModeService;

    @Autowired
    private LogUserService logUserService;

    @Autowired
    private LampGroupSingleService lampGroupSingleService;

    @Autowired
    private HttpDlmApi httpDlmApi;

    @Autowired
    private SystemDeviceService systemDeviceService;

    @Autowired
    private SystemDeviceTypeService systemDeviceTypeService;

    @Autowired
    private DeviceStrategyHistoryService deviceStrategyHistoryService;

    @Autowired
    private LampDeviceParameterService lampDeviceParameterService;

    @Autowired
    private SingleLampParamService singleLampParamService;

    @Autowired
    private ControlLoopDeviceService controlLoopDeviceService;

    @Autowired
    private HttpSlApi httpSlApi;

    @Override
    public Result insertLampStrategy(SlReqStrategyVO slReqStrategyVO, HttpServletRequest request) {
        logger.info("insertLampStrategy - 新增策略 slReqStrategyVO=[{}]", slReqStrategyVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        LampStrategy lampStrategy = new LampStrategy();
        BeanUtils.copyProperties(slReqStrategyVO, lampStrategy);
        lampStrategy.setCreateTime(new Date());
        lampStrategy.setCreator(userId);
        List<LampStrategy> lampStrategyList = baseMapper.selectList(null);
        List<Integer> lampStrategySceneList = lampStrategyList.stream().map(LampStrategy::getScene).collect(Collectors.toList());
        List<Integer> sceneList = new ArrayList<>();
        for (int i = 0; i < lampStrategyList.size() + 1; i++) {
            sceneList.add(i);
        }
        sceneList.removeAll(lampStrategySceneList);
        if (sceneList.size() < 1) {
            return new Result().error("策略数量将超出限制（65535）");
        }
        lampStrategy.setScene(sceneList.get(0));
        int result = baseMapper.insert(lampStrategy);
        if (result < 1) {
            logger.info("insertLampStrategy - 新增策略失败 slReqStrategyVO=[{}]", slReqStrategyVO);
            return new Result().error("新增策略失败");
        }
        // 策略绑定设备类型和定时方式
        lampStrategyBindRelation(slReqStrategyVO, lampStrategy);
        // 新增策略动作
        for (SlReqStrategyActionVO slReqStrategyActionVO : slReqStrategyVO.getSlReqStrategyActionVOList()) {
            slReqStrategyActionVO.setStrategyId(lampStrategy.getId());
        }
        lampStrategyActionService.insertLampStrategyAction(slReqStrategyVO.getSlReqStrategyActionVOList());
        return new Result().success("新增成功");
    }

    /**
     * 策略绑定设备类型和定时方式
     *
     * @param slReqStrategyVO
     * @param lampStrategy
     */
    private void lampStrategyBindRelation(SlReqStrategyVO slReqStrategyVO, LampStrategy lampStrategy) {
        if (slReqStrategyVO.getDeviceTypeIdOfStrategyList() != null && slReqStrategyVO.getDeviceTypeIdOfStrategyList().size() > 0) {
            // 绑定策略关联的设备类型
            for (Integer deviceTypeIdOfStrategy : slReqStrategyVO.getDeviceTypeIdOfStrategyList()) {
                LampStrategyDeviceType lampStrategyDeviceType = new LampStrategyDeviceType();
                lampStrategyDeviceType.setDeviceTypeId(deviceTypeIdOfStrategy);
                lampStrategyDeviceType.setStrategyId(lampStrategy.getId());
                lampStrategyDeviceTypeService.save(lampStrategyDeviceType);
            }
            // 绑定策略关联的定时方式
            // 未勾选一键同步
            if (slReqStrategyVO.getIdSynchro() == 0) {
                // 根据设备类型查询设备类型支持的定时模式对象
                LambdaQueryWrapper<SystemDeviceTypeTimingMode> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(SystemDeviceTypeTimingMode::getDeviceTypeId, slReqStrategyVO.getDeviceTypeIdOfStrategyList());
                List<SystemDeviceTypeTimingMode> timingModeList = systemDeviceTypeTimingModeService.list(wrapper);
                Set<Integer> timingModeIdList = timingModeList.stream().map(SystemDeviceTypeTimingMode::getTimingModeId).collect(Collectors.toSet());
                for (Integer timingModeId : timingModeIdList) {
                    LampStrategyTimingMode lampStrategyTimingMode = new LampStrategyTimingMode();
                    lampStrategyTimingMode.setStrategyId(lampStrategy.getId());
                    lampStrategyTimingMode.setTimingModeId(timingModeId);
                    lampStrategyTimingModeService.save(lampStrategyTimingMode);
                }
            }
            // 勾选一键同步
            if (slReqStrategyVO.getIdSynchro() == 1) {
                // 查询所选设备下的所有定时模式id
                LambdaQueryWrapper<SystemDeviceTypeTimingMode> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(SystemDeviceTypeTimingMode::getDeviceTypeId, slReqStrategyVO.getDeviceTypeIdOfStrategyList());
                List<SystemDeviceTypeTimingMode> timingModeList = systemDeviceTypeTimingModeService.list(wrapper);
                List<Integer> timingModeIdList = timingModeList.stream().map(SystemDeviceTypeTimingMode::getTimingModeId).collect(Collectors.toList());
                if (slReqStrategyVO.getDeviceTypeIdOfStrategyList().size() == 1) {
                    // 如果设备类型id集合的数量为1时也勾选了一键同步，这时就不需要取交集了，否则就会过滤为空值
                    for (Integer timingModeId : timingModeIdList) {
                        LampStrategyTimingMode lampStrategyTimingMode = new LampStrategyTimingMode();
                        lampStrategyTimingMode.setStrategyId(lampStrategy.getId());
                        lampStrategyTimingMode.setTimingModeId(timingModeId);
                        lampStrategyTimingModeService.save(lampStrategyTimingMode);
                    }
                } else {
                    // 过滤出共同支持的定时模式id
                    // 获取定时方式所支持的设备类型
                    Map<Integer, Set<Integer>> timingMap = timingModeList.stream().collect(Collectors.groupingBy(SystemDeviceTypeTimingMode::getTimingModeId, Collectors.mapping(SystemDeviceTypeTimingMode::getDeviceTypeId, Collectors.toSet())));
                    // 获取所有设备类型
                    List<Integer> allDeviceIdList = timingModeList.stream().map(SystemDeviceTypeTimingMode::getDeviceTypeId).distinct().collect(Collectors.toList());
                    // 找出支持所有设备类型的定时模式
                    List<Integer> commonTimeModeIdList = timingMap.entrySet().stream().filter(e -> e.getValue().size() == allDeviceIdList.size())
                            .map(Map.Entry::getKey).collect(Collectors.toList());
                    for (Integer timingModeId : commonTimeModeIdList) {
                        LampStrategyTimingMode lampStrategyTimingMode = new LampStrategyTimingMode();
                        lampStrategyTimingMode.setStrategyId(lampStrategy.getId());
                        lampStrategyTimingMode.setTimingModeId(timingModeId);
                        lampStrategyTimingModeService.save(lampStrategyTimingMode);
                    }
                }
            }
        }
    }

    @Override
    public Result updateLampStrategy(SlReqStrategyVO slReqStrategyVO, HttpServletRequest request) {
        logger.info("updateLampStrategy - 编辑策略 slReqStrategyVO=[{}]", slReqStrategyVO);

        // 删除当前动作
        LambdaUpdateWrapper<LampStrategy> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(LampStrategy::getIsDelete, 1)
                .set(LampStrategy::getScene, null)
                .eq(LampStrategy::getId, slReqStrategyVO.getId());
        baseMapper.update(null, updateWrapper);

        // 新增策略
        Result result = insertLampStrategy(slReqStrategyVO, request);
        if (result.getCode() != 200) {
            return new Result().success("编辑失败");
        }

        // 删除之前的动作
//        LambdaQueryWrapper<LampStrategyAction> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(LampStrategyAction::getStrategyId, slReqStrategyVO.getId());
//        List<LampStrategyAction> strategyActionList = lampStrategyActionService.list(wrapper);
//        List<Integer> strategyActionIdList = strategyActionList.stream().map(LampStrategyAction::getId).collect(Collectors.toList());
//        lampStrategyActionService.remove(wrapper);
//        LampStrategy lampStrategy = new LampStrategy();
//        BeanUtils.copyProperties(slReqStrategyVO, lampStrategy);
//        lampStrategy.setUpdateTime(new Date());
//        int result = baseMapper.updateById(lampStrategy);
//        if (result < 1) {
//            logger.info("updateLampStrategy - 编辑策略失败 slReqStrategyVO=[{}]", slReqStrategyVO);
//            return new Result().error("编辑策略失败");
//        }
//        // 解除策略关联的设备类型
//        LambdaQueryWrapper<LampStrategyDeviceType> deviceTypeWrapper = new LambdaQueryWrapper<>();
//        deviceTypeWrapper.eq(LampStrategyDeviceType::getStrategyId, slReqStrategyVO.getId());
//        lampStrategyDeviceTypeService.remove(deviceTypeWrapper);
//        // 解除策略关联的定时方式
//        LambdaQueryWrapper<LampStrategyTimingMode> timeModeWrapper = new LambdaQueryWrapper<>();
//        timeModeWrapper.eq(LampStrategyTimingMode::getStrategyId, slReqStrategyVO.getId());
//        lampStrategyTimingModeService.remove(timeModeWrapper);
//        // 策略重新绑定设备类型和定时方式
//        lampStrategyBindRelation(slReqStrategyVO, lampStrategy);
//        for (SlReqStrategyActionVO slReqStrategyActionVO : slReqStrategyVO.getSlReqStrategyActionVOList()) {
//            slReqStrategyActionVO.setStrategyId(lampStrategy.getId());
//        }
//        // 解除策略动作和定时方式关系
//        LambdaQueryWrapper<LampStrategyActionTimingMode> actionTimingModeWrapper = new LambdaQueryWrapper<>();
//        actionTimingModeWrapper.in(LampStrategyActionTimingMode::getStrategyActionId, strategyActionIdList);
//        lampStrategyActionTimingModeService.remove(actionTimingModeWrapper);
//        // 解除策略动作和设备类型关系
//        LambdaQueryWrapper<LampStrategyActionDeviceType> actionDeviceTypeWrapper = new LambdaQueryWrapper<>();
//        actionDeviceTypeWrapper.in(LampStrategyActionDeviceType::getLampStrategyActionId, strategyActionIdList);
//        lampStrategyActionDeviceTypeService.remove(actionDeviceTypeWrapper);
//        // 重新绑定动作
//        lampStrategyActionService.insertLampStrategyAction(slReqStrategyVO.getSlReqStrategyActionVOList());
        return new Result().success("编辑成功");
    }

    @Override
    public Result getLampStrategy(Integer strategyId, HttpServletRequest request) {
        logger.info("getLampStrategy - 策略详情 strategyId=[{}]", strategyId);
        LampStrategy lampStrategy = baseMapper.selectById(strategyId);
        SlRespStrategyVO strategyVO = new SlRespStrategyVO();
        BeanUtils.copyProperties(lampStrategy, strategyVO);
        // 查询策略关联的设备类型id集合
        LambdaQueryWrapper<LampStrategyDeviceType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LampStrategyDeviceType::getStrategyId, strategyId);
        List<LampStrategyDeviceType> strategyDeviceTypeList = lampStrategyDeviceTypeService.list(wrapper);
        List<Integer> strategyDeviceTypeIdList = strategyDeviceTypeList.stream().map(LampStrategyDeviceType::getDeviceTypeId).collect(Collectors.toList());
        strategyVO.setDeviceTypeIdList(strategyDeviceTypeIdList);
        // 查询策略下的动作信息
        LambdaQueryWrapper<LampStrategyAction> actionWrapper = new LambdaQueryWrapper<>();
        actionWrapper.eq(LampStrategyAction::getStrategyId, strategyId);
        List<LampStrategyAction> strategyActionList = lampStrategyActionService.list(actionWrapper);
        List<SlRespStrategyActionVO> actionVOList = new ArrayList<>();
        for (LampStrategyAction strategyAction : strategyActionList) {
            SlRespStrategyActionVO actionVO = new SlRespStrategyActionVO();
            BeanUtils.copyProperties(strategyAction, actionVO);
            if (strategyAction.getLightModeId() != null) {
                LampLightMode lightMode = lampLightModeService.getById(strategyAction.getLightModeId());
                actionVO.setLightModeName(lightMode.getName());
            }
            if (strategyAction.getWeekValue() != null) {
                Integer[] cycleName = DataUtil.getCycleName(strategyAction.getWeekValue());
                actionVO.setCycleTypes(cycleName);
            }
            // 查询该动作下的设备类型id集合
            LambdaQueryWrapper<LampStrategyActionDeviceType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LampStrategyActionDeviceType::getLampStrategyActionId, strategyAction.getId());
            List<LampStrategyActionDeviceType> deviceTypeList = lampStrategyActionDeviceTypeService.list(queryWrapper);
            List<Integer> deviceTypeIdList = deviceTypeList.stream().map(LampStrategyActionDeviceType::getDeviceTypeId).collect(Collectors.toList());
            actionVO.setDeviceTypeIdOfActionList(deviceTypeIdList);
            actionVOList.add(actionVO);
        }
        strategyVO.setSlRespStrategyActionVOList(actionVOList);
        Result<SlRespStrategyVO> result = new Result<>();
        return result.success(strategyVO);
    }

    @Override
    public Result deleteLampStrategy(Integer strategyId, HttpServletRequest request) {
        logger.info("deleteLampStrategy - 删除策略 strategyId=[{}]", strategyId);
        LambdaUpdateWrapper<LampStrategy> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(LampStrategy::getIsDelete, 1)
                .set(LampStrategy::getScene, null)
                .eq(LampStrategy::getId, strategyId);
        int result = baseMapper.update(null, updateWrapper);
        if (result < 1) {
            logger.info("deleteLampStrategy - 删除策略失败 strategyId=[{}]", strategyId);
            return new Result().error("删除策略失败");
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result getPage(SlLampStrategyQuery slLampStrategyQuery, HttpServletRequest request) {
        logger.info("SlLampStrategyQuery - 灯控策略分页条件查询 slLampStrategyQuery=[{}]", slLampStrategyQuery);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        Result<Page<SlRespStrategyVO>> result = new Result<>();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean isAdmin = logUserService.isAdmin(userId);
        if (!isAdmin) {
            slLampStrategyQuery.setAreaId(user.getAreaId());
        }
        // 获取灯具设备id集合
        List<Integer> lampDeviceIdList = new ArrayList<>();
        if (slLampStrategyQuery.getLampsGroupIdList() != null && slLampStrategyQuery.getLampsGroupIdList().size() > 0) {
            // 查询灯具分组下的所有关联灯具设备
            LambdaQueryWrapper<LampGroupSingle> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(LampGroupSingle::getLampGroupId, slLampStrategyQuery.getLampsGroupIdList());
            List<LampGroupSingle> lampGroupSingleList = lampGroupSingleService.list(wrapper);
            // 灯具id集合
            lampDeviceIdList = lampGroupSingleList.stream().map(LampGroupSingle::getSingleId).collect(Collectors.toList());
        } else if (slLampStrategyQuery.getLampPostIdList() != null && slLampStrategyQuery.getLampPostIdList().size() > 0) {
            // 查询灯杆分组下的所有关联灯具设备
            // 获取路灯灯杆集合
            List<SlLampPost> slLampPostList = null;
            String json = "groupIdList=";
            for (Integer groupId : slLampStrategyQuery.getLampPostIdList()) {
                json += groupId + "&groupIdList=";
            }
            try {
                JSONObject ssDeviceResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetLampPostByGroupIdList() + "?" + json, headMap));
                JSONArray ssDeviceResultArr = ssDeviceResult.getJSONArray("data");
                slLampPostList = JSON.parseObject(ssDeviceResultArr.toJSONString(), new TypeReference<List<SlLampPost>>() {
                });
            } catch (Exception e) {
                logger.error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
                return new Result().error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
            }
            List<Integer> slLampPostIdList = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
            if (slLampPostIdList.size() > 0) {
                LambdaQueryWrapper<SystemDevice> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(SystemDevice::getLampPostId, slLampPostIdList);
                List<SystemDevice> lampDeviceList = systemDeviceService.list(wrapper);
                // 灯具id集合
                lampDeviceIdList = lampDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
            }
        } else if (slLampStrategyQuery.getControlGroupIdList() != null && slLampStrategyQuery.getControlGroupIdList().size() > 0) {
            // 查询集控分组下的所有关联灯具设备
            List<ControlLoopDevice> controlLoopDeviceList = null;
            String json = "loopIdList=";
            for (Integer loopId : slLampStrategyQuery.getControlGroupIdList()) {
                json += loopId + "&loopIdList=";
            }
            try {
                JSONObject ssDeviceResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetLampPostByGroupIdList() + "?" + json, headMap));
                JSONArray ssDeviceResultArr = ssDeviceResult.getJSONArray("data");
                controlLoopDeviceList = JSON.parseObject(ssDeviceResultArr.toJSONString(), new TypeReference<List<ControlLoopDevice>>() {
                });
            } catch (Exception e) {
                logger.error("根据集控分组id集合获取集控分组设备接口调用失败，返回为空！");
                return new Result().error("根据集控分组id集合获取集控分组设备接口调用失败，返回为空！");
            }
            // 灯具id集合
            lampDeviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
        } else if (slLampStrategyQuery.getLampDeviceIdList() != null && slLampStrategyQuery.getLampDeviceIdList().size() > 0) {
            // 灯具id集合
            lampDeviceIdList = slLampStrategyQuery.getLampDeviceIdList();
        }
        if (lampDeviceIdList != null && lampDeviceIdList.size() > 0) {
            // 找出设备的设备类型
            LambdaQueryWrapper<SystemDevice> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SystemDevice::getId, lampDeviceIdList);
            List<SystemDevice> systemDeviceList = systemDeviceService.list(queryWrapper);
            List<Integer> deviceTypeIdList = systemDeviceList.stream().map(SystemDevice::getDeviceTypeId).distinct().collect(Collectors.toList());
            // 根据设备类型过滤出支持该设备类型的策略
            if (deviceTypeIdList.size() > 0) {
                List<Integer> strategyIdListByDeviceTypeIdList = systemDeviceTypeService.getStrategyIdListByDeviceTypeIdList(deviceTypeIdList, deviceTypeIdList.size());
                if (strategyIdListByDeviceTypeIdList.size() > 0) {
                    slLampStrategyQuery.setStrategyIdList(strategyIdListByDeviceTypeIdList);
                } else {
                    return new Result().error("暂无支持当前设备类型的策略");
                }
            }
        }
        // 构建页面返回对象
        Page<SlRespStrategyVO> resultPage = new Page<>(slLampStrategyQuery.getPageNum(), slLampStrategyQuery.getPageSize());
        // 查询分页数据
        Page<SlRespStrategyVO> page = new Page<>(slLampStrategyQuery.getPageNum(), slLampStrategyQuery.getPageSize());
        IPage<SlRespStrategyVO> slRespStrategyPageDate = baseMapper.selectLampStrategyWithPage(page, slLampStrategyQuery);
        // 分页的数据
        List<SlRespStrategyVO> strategyPageDateRecords = slRespStrategyPageDate.getRecords();
        // 获取策略id集合
        List<Integer> strategyIdList = strategyPageDateRecords.stream().map(SlRespStrategyVO::getId).collect(Collectors.toList());
        // 页面返回数据集合
        List<SlRespStrategyVO> slRespStrategyVOList = new ArrayList<>();
        if (strategyIdList.size() > 0) {
            // 查询策略下的动作集合
            LambdaQueryWrapper<LampStrategyAction> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(LampStrategyAction::getStrategyId, strategyIdList);
            List<LampStrategyAction> strategyActionList = lampStrategyActionService.list(wrapper);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (SlRespStrategyVO strategyPageDateRecord : strategyPageDateRecords) {
                // 获取策略下的动作集合
                List<LampStrategyAction> actionCollect = strategyActionList.stream().filter(a -> a.getStrategyId().equals(strategyPageDateRecord.getId())).collect(Collectors.toList());
                String minStartDateString = null;
                Date minStartDate = null;
                try {
                    minStartDate = dateFormat.parse("2999-01-01");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String maxEndDateString = null;
                Date maxEndDate = new Date(1);
                // 用于表示是否需要继续比较结束时间
                int flag = 1;
                // 循环策略动作集合,对比时间获取策略的开始结束时间
                for (LampStrategyAction lampStrategyAction : actionCollect) {
                    // 比较获取最小开始时间
                    String startDateString = lampStrategyAction.getStartDate();
                    if (startDateString == null || "".equals(startDateString)) {
                        continue;
                    }
                    Date startDate = null;
                    try {
                        startDate = dateFormat.parse(startDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int resStart = minStartDate.compareTo(startDate);
                    if (resStart == 1) {
                        minStartDate = startDate;
                        minStartDateString = startDateString;
                    }
                    // 比较获取最大的结束时间
                    if (flag == 1) {
                        String endDateString = lampStrategyAction.getEndDate();
                        if (endDateString == null || "".equals(endDateString)) {
                            continue;
                        }
                        Date endDate = null;
                        try {
                            endDate = dateFormat.parse(endDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (endDate != null) {
                            int resEnd = maxEndDate.compareTo(endDate);
                            if (resEnd == -1) {
                                maxEndDate = endDate;
                                maxEndDateString = endDateString;
                            }
                        } else {
                            maxEndDateString = null;
                            flag = 0;
                        }
                    }
                }
                strategyPageDateRecord.setStartDate(minStartDateString);
                strategyPageDateRecord.setEndDate(maxEndDateString);
                slRespStrategyVOList.add(strategyPageDateRecord);
            }
        }
        resultPage.setTotal(slRespStrategyPageDate.getTotal());
        resultPage.setCurrent(slRespStrategyPageDate.getCurrent());
        resultPage.setSize(slRespStrategyPageDate.getSize());
        resultPage.setPages(slRespStrategyPageDate.getPages());
        resultPage.setRecords(slRespStrategyVOList);
        return result.success(resultPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result execute(HttpServletRequest request, SlReqLampStrategyExecuteVO slReqLampStrategyExecuteVO) {
        logger.info("execute - 下发策略 slReqLampStrategyExecuteVO=[{}]", slReqLampStrategyExecuteVO);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        headMap.put("Content-Type", "application/json");
        // 灯具分组id集合
        List<Integer> lampsGroupIdList = slReqLampStrategyExecuteVO.getLampsGroupIdList();
        // 灯杆分组id集合
        List<Integer> lampPostGroupIdList = slReqLampStrategyExecuteVO.getLampPostGroupIdList();
        // 集控分组id集合
        List<Integer> controlGroupIdList = slReqLampStrategyExecuteVO.getControlGroupIdList();
        // 灯具设备id集合(前端传过来的)
        List<Integer> deviceIdList = slReqLampStrategyExecuteVO.getLampDeviceIdList();
        // 集控回路设备信息集合
        List<ControlLoopDevice> controlLoopDeviceList = null;
        // 获取灯具设备id集合(自定义)
        List<Integer> lampDeviceIdList = new ArrayList<>();
        if (lampsGroupIdList != null && lampsGroupIdList.size() > 0) {
            // 查询灯具分组下的所有关联灯具设备
            LambdaQueryWrapper<LampGroupSingle> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(LampGroupSingle::getLampGroupId, lampsGroupIdList);
            List<LampGroupSingle> lampGroupSingleList = lampGroupSingleService.list(wrapper);
            // 灯具id集合
            lampDeviceIdList = lampGroupSingleList.stream().map(LampGroupSingle::getSingleId).collect(Collectors.toList());
        } else if (lampPostGroupIdList != null && lampPostGroupIdList.size() > 0) {
            // 查询灯杆分组下的所有关联灯具设备
            // 获取路灯灯杆集合
            List<SlLampPost> slLampPostList = null;
            String json = "groupIdList=";
            for (Integer groupId : lampPostGroupIdList) {
                json += groupId + "&groupIdList=";
            }
            try {
                JSONObject ssDeviceResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetLampPostByGroupIdList() + "?" + json, headMap));
                JSONArray ssDeviceResultArr = ssDeviceResult.getJSONArray("data");
                slLampPostList = JSON.parseObject(ssDeviceResultArr.toJSONString(), new TypeReference<List<SlLampPost>>() {
                });
            } catch (Exception e) {
                logger.error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
                return new Result().error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
            }
            List<Integer> slLampPostIdList = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
            if (slLampPostIdList.size() > 0) {
                LambdaQueryWrapper<SystemDevice> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(SystemDevice::getLampPostId, slLampPostIdList);
                List<SystemDevice> lampDeviceList = systemDeviceService.list(wrapper);
                // 灯具id集合
                lampDeviceIdList = lampDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
            }
        } else if (controlGroupIdList != null && controlGroupIdList.size() > 0) {
            // 查询集控分组下的所有关联灯具设备
            String json = "loopIdList=";
            for (Integer loopId : controlGroupIdList) {
                json += loopId + "&loopIdList=";
            }
            try {
                JSONObject ssDeviceResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetControlLoopDeviceByLoopIdList() + "?" + json, headMap));
                JSONArray ssDeviceResultArr = ssDeviceResult.getJSONArray("data");
                controlLoopDeviceList = JSON.parseObject(ssDeviceResultArr.toJSONString(), new TypeReference<List<ControlLoopDevice>>() {
                });
            } catch (Exception e) {
                logger.error("根据集控分组id集合获取集控分组设备接口调用失败，返回为空！");
                return new Result().error("根据集控分组id集合获取集控分组设备接口调用失败，返回为空！");
            }
            lampDeviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
        } else if (deviceIdList != null && deviceIdList.size() > 0) {
            // 灯具id集合
            lampDeviceIdList = deviceIdList;
        }

        if (lampDeviceIdList.size() == 0) {
            return new Result().success("该目标没有设备");
        }

        // 找出设备和设备类型id信息
        LambdaQueryWrapper<SystemDevice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SystemDevice::getId, lampDeviceIdList);
        List<SystemDevice> systemDeviceList = systemDeviceService.list(queryWrapper);
        List<Integer> deviceTypeIdList = systemDeviceList.stream().map(SystemDevice::getDeviceTypeId).distinct().collect(Collectors.toList());

        // 获取下发策略动作集合
        Integer strategyId = slReqLampStrategyExecuteVO.getStrategyId();
        // 根据id获取策略信息
        LampStrategy lampStrategy = baseMapper.selectById(strategyId);
        LambdaQueryWrapper<LampStrategyAction> actionWrapper = new LambdaQueryWrapper<>();
        actionWrapper.eq(LampStrategyAction::getStrategyId, strategyId);
        List<LampStrategyAction> actionList = lampStrategyActionService.list(actionWrapper);
        if (actionList == null || actionList.size() == 0) {
            return new Result().error("当前策略下没有动作");
        }
        // 查询动作设备类型关联表信息
        List<Integer> actionIdList = actionList.stream().map(LampStrategyAction::getId).collect(Collectors.toList());
        LambdaQueryWrapper<LampStrategyActionDeviceType> deviceTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        deviceTypeLambdaQueryWrapper.in(LampStrategyActionDeviceType::getLampStrategyActionId, actionIdList);
        List<LampStrategyActionDeviceType> actionDeviceTypeList = lampStrategyActionDeviceTypeService.list(deviceTypeLambdaQueryWrapper);

        // 添加新的设备与策略关联关系(之所以在这里建立关联关系，是因为自研单灯要查询历史表的主键id)
        List<DeviceStrategyHistory> deviceStrategyHistoryList = new ArrayList<>();
        Date createTime = new Date();
        for (Integer deviceId : lampDeviceIdList) {
            DeviceStrategyHistory deviceStrategyHistory = new DeviceStrategyHistory();
            deviceStrategyHistory.setDeviceId(deviceId);
            deviceStrategyHistory.setStrategyId(strategyId);
            deviceStrategyHistory.setIsSuccess(1);
            deviceStrategyHistory.setCreateTime(createTime);
            deviceStrategyHistoryList.add(deviceStrategyHistory);
        }
        deviceStrategyHistoryService.saveBatch(deviceStrategyHistoryList);

        // 找出同一类型下的设备和动作下发策略
        for (Integer deviceTypeId : deviceTypeIdList) {
            // 当前设备类型下的设备集合
            List<SystemDevice> deviceList = systemDeviceList.stream().filter(e -> e.getDeviceTypeId().equals(deviceTypeId)).collect(Collectors.toList());
            // 当前设备类型下的策略动作集合
            List<LampStrategyActionDeviceType> lampStrategyActionDeviceTypeList = actionDeviceTypeList.stream().filter(e -> e.getDeviceTypeId().equals(deviceTypeId)).collect(Collectors.toList());
            List<Integer> lampStrategyActionIdList = lampStrategyActionDeviceTypeList.stream().map(LampStrategyActionDeviceType::getLampStrategyActionId).collect(Collectors.toList());
            LambdaQueryWrapper<LampStrategyAction> actionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            actionLambdaQueryWrapper.in(LampStrategyAction::getId, lampStrategyActionIdList);
            List<LampStrategyAction> lampStrategyActionList = lampStrategyActionService.list(actionLambdaQueryWrapper);

            // 自研单灯
            if (deviceTypeId <= 11 || deviceTypeId == 14 || deviceTypeId == 15) {
//                LambdaQueryWrapper<DeviceStrategyHistory> historyLambdaQueryWrapper = new LambdaQueryWrapper<>();
//                historyLambdaQueryWrapper.in(DeviceStrategyHistory::getDeviceId, deviceIdList);
//                List<DeviceStrategyHistory> strategyHistoryList = deviceStrategyHistoryService.list(historyLambdaQueryWrapper);
//                for (DeviceStrategyHistory strategyHistory : strategyHistoryList) {
//                    if (strategyHistory.getIsSuccess() == 0 || strategyHistory.getIsSuccess() == 1) {
//                        return new Result().error("设备正在下发策略中，请稍后下发");
//                    }
//                }
                singleLampExecute(deviceList, lampStrategyActionList, lampStrategy.getScene(),deviceStrategyHistoryList);
            }

            // 华体单灯
            if (deviceTypeId == 12) {
                // 将设备表中的策略id更新,方便整体查询
                List<Integer> systemDeviceIdList = deviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                LambdaUpdateWrapper<SystemDevice> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(SystemDevice::getStrategyId, strategyId)
                        .in(SystemDevice::getId, systemDeviceIdList);
                systemDeviceService.update(updateWrapper);
                // 获取该设备类型下的设备id集合
                List<Integer> deviceIdListOfHt = deviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                LambdaQueryWrapper<ControlLoopDevice> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(ControlLoopDevice::getDeviceId, deviceIdListOfHt);
                List<ControlLoopDevice> controlLoopDeviceListOfHt = controlLoopDeviceService.list(wrapper);
                // 集中控制器id集合
                List<Integer> controlIdListOfHt = controlLoopDeviceListOfHt.stream().map(ControlLoopDevice::getControlId).distinct().collect(Collectors.toList());
                // 构建数据传输对象
                List<HtSetSingleLampOutputPlanRequestVO> planRequestVOList = new ArrayList<>();
                // 遍历集控器id集合
                for (Integer controlId : controlIdListOfHt) {
                    HtSetSingleLampOutputPlanRequestVO planRequestVO = new HtSetSingleLampOutputPlanRequestVO();
                    planRequestVO.setLocationControlId(controlId);
                    List<DlmRespLocationControlMixVO> mixVoListList = null;
                    // 获取集中控制器的编号
                    try {
                        JSONObject controlResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetMixLocationControl() + "/" + controlId, headMap));
                        JSONArray controlResultArr = controlResult.getJSONArray("data");
                        mixVoListList = JSON.parseObject(controlResultArr.toJSONString(), new TypeReference<List<DlmRespLocationControlMixVO>>() {
                        });
                        List<String> numList = mixVoListList.stream().map(DlmRespLocationControlMixVO::getNum).distinct().collect(Collectors.toList());
                        planRequestVO.setLocationControlAddr(numList.get(0));
                    } catch (Exception e) {
                        logger.error("根据集控器id获取集控混合信息接口调用失败，返回为空！");
                        return new Result().error("根据集控器id获取集控混合信息接口调用失败，返回为空！");
                    }
                    // 单灯控制器控制计划集合
                    List<HtSingleLampPlanRequestVO> roList = new ArrayList<>();
                    // 按序号进行分组
                    Map<Integer, List<DlmRespLocationControlMixVO>> orderList = mixVoListList.stream().collect(Collectors.groupingBy(DlmRespLocationControlMixVO::getLoopOrders));
                    for (Map.Entry<Integer, List<DlmRespLocationControlMixVO>> orderEntry : orderList.entrySet()) {
                        HtSingleLampPlanRequestVO singleLampPlanRequestVO = new HtSingleLampPlanRequestVO();
                        // 单灯控制器序号
                        List<Integer> nodeList = new ArrayList<>();
                        nodeList.add(orderEntry.getKey());
                        singleLampPlanRequestVO.setNode(nodeList);
                        List<DlmRespLocationControlMixVO> controlMixVOList = orderEntry.getValue();
                        // 按策略分组
                        Map<Integer, List<DlmRespLocationControlMixVO>> strategyMap = controlMixVOList.stream().collect(Collectors.groupingBy(DlmRespLocationControlMixVO::getStrategyId));
                        for (Map.Entry<Integer, List<DlmRespLocationControlMixVO>> entry : strategyMap.entrySet()) {
                            Integer key = entry.getKey();
                            List<DlmRespLocationControlMixVO> mixVOList = entry.getValue();
                            // 根据策略id查询动作
                            LambdaQueryWrapper<LampStrategyAction> actionWrapper1 = new LambdaQueryWrapper<>();
                            actionWrapper.eq(LampStrategyAction::getStrategyId, key);
                            List<LampStrategyAction> actionList1 = lampStrategyActionService.list(actionWrapper1);
                            // 找出支持该设备类型的动作集合
                            List<Integer> actionIdList1 = actionList1.stream().map(LampStrategyAction::getId).collect(Collectors.toList());
                            LambdaQueryWrapper<LampStrategyActionDeviceType> deviceTypeWrapper = new LambdaQueryWrapper<>();
                            deviceTypeWrapper.in(LampStrategyActionDeviceType::getLampStrategyActionId, actionIdList1);
                            List<LampStrategyActionDeviceType> actionDeviceTypeList1 = lampStrategyActionDeviceTypeService.list(deviceTypeWrapper);
                            // 当前设备类型下的策略动作集合
                            List<LampStrategyActionDeviceType> lampStrategyActionDeviceTypeList1 = actionDeviceTypeList1.stream().filter(e -> e.getDeviceTypeId().equals(deviceTypeId)).collect(Collectors.toList());
                            List<Integer> lampStrategyActionIdList1 = lampStrategyActionDeviceTypeList1.stream().map(LampStrategyActionDeviceType::getLampStrategyActionId).collect(Collectors.toList());
                            LambdaQueryWrapper<LampStrategyAction> actionQueryWrapper = new LambdaQueryWrapper<>();
                            actionQueryWrapper.in(LampStrategyAction::getId, lampStrategyActionIdList1);
                            List<LampStrategyAction> lampStrategyActionList1 = lampStrategyActionService.list(actionQueryWrapper);
                            List<HtSingleLampPlanChildRequestVo> childRequestVoList = new ArrayList<>();
                            for (LampStrategyAction strategyAction : lampStrategyActionList1) {
                                HtSingleLampPlanChildRequestVo childRequestVo = new HtSingleLampPlanChildRequestVo();
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                try {
                                    Date date = sdf.parse(strategyAction.getExecutionTime());
                                    childRequestVo.setTime(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Integer brightness = strategyAction.getBrightness();
                                List<Integer> loopNumList = mixVOList.stream().map(e -> Integer.parseInt(e.getLoopNum())).collect(Collectors.toList());
                                List<Integer> actList = new ArrayList<>();
                                for (int i = 0; i < 3; i++) {
                                    actList.add(loopNumList.contains(i + 1) ? brightness : 255);
                                }
                                childRequestVo.setAct(actList);
                                childRequestVoList.add(childRequestVo);
                            }
                            singleLampPlanRequestVO.setPlan(childRequestVoList);
                            roList.add(singleLampPlanRequestVO);
                        }
                    }
                    planRequestVO.setRoList(roList);
                    planRequestVOList.add(planRequestVO);
                }
                Object jsonObject = JSONArray.toJSON(planRequestVOList);
                String jsonString = jsonObject.toString();
                try {
                    JSONObject loopControlResult = JSON.parseObject(HttpUtil.post(httpSlApi.getUrl() + httpSlApi.getSingleLampOutPlan(), jsonString, headMap));
                    JSONObject loopControlObj = loopControlResult.getJSONObject("data");
                    System.out.println(loopControlObj);
                } catch (Exception e) {
                    logger.error("策略下发失败", e.getMessage());
                    return new Result().error("策略下发失败");
                }
            }

            // 中科智联
            if (deviceTypeId == 13) {
                // 过滤出集控器id集合
                if (controlLoopDeviceList != null && controlLoopDeviceList.size() > 0) {
                    List<Integer> collectIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getControlId).distinct().collect(Collectors.toList());
                    for (Integer controlId : collectIdList) {
                        String num = null;
                        // 获取集中控制器的编号
                        try {
                            JSONObject controlResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetLocationControl() + "/" + controlId, headMap));
                            JSONObject controlResultObj = controlResult.getJSONObject("data");
                            num = controlResultObj.getString("num");
                        } catch (Exception e) {
                            logger.error("根据集控器id获取集控详情信息接口调用失败，返回为空！");
                            return new Result().error("根据集控器id获取集控详情信息接口调用失败，返回为空！");
                        }
                        // 该集控器下的分组集合
                        List<Integer> groupIdList = controlLoopDeviceList.stream().filter(e -> e.getControlId().equals(controlId)).map(ControlLoopDevice::getLoopId).distinct().collect(Collectors.toList());
                        // 获取集控分组的数量和组号
                        List<ControlLoop> controlLoopList = null;
                        String json = "groupIdList=";
                        for (Integer groupId : groupIdList) {
                            json += groupId + "&groupIdList=";
                        }
                        try {
                            JSONObject loopResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetControlLoopList() + "?" + json, headMap));
                            JSONArray loopResultObj = loopResult.getJSONArray("data");
                            controlLoopList = JSON.parseObject(loopResultObj.toJSONString(), new TypeReference<List<ControlLoop>>() {
                            });
                        } catch (Exception e) {
                            logger.error("根据集控分组id集合获取集控分组接口调用失败，返回为空！");
                            return new Result().error("根据集控分组id集合获取集控分组接口调用失败，返回为空！");
                        }
                        List<Integer> loopNumList = controlLoopList.stream().map(e -> Integer.parseInt(e.getNum())).collect(Collectors.toList());
                        TimeTableVO timeTableVO = new TimeTableVO();
                        List<String> executionTimeList = lampStrategyActionList.stream().map(LampStrategyAction::getExecutionTime).collect(Collectors.toList());
                        List<Integer> brightnessList = lampStrategyActionList.stream().map(LampStrategyAction::getBrightness).collect(Collectors.toList());
                        List<String> timeList = new ArrayList<>();
                        for (String executionTime : executionTimeList) {
                            String[] timeString = executionTime.split(":");
                            String time = "";
                            for (int i = 0; i < timeString.length - 1; i++) {
                                time += timeString[i];
                            }
                            timeList.add(time);
                        }
                        timeTableVO.setTimeList(timeList);
                        timeTableVO.setValueList(brightnessList);
                        // 下发控制,一次控制下发九个组，超过九个组就分两次下发
                        if (groupIdList.size() > 9) {
                            ZkzlProtocolUtil.timeTableIssued(num, groupIdList.stream().limit(9).collect(Collectors.toList()).size(), loopNumList.stream().limit(9).collect(Collectors.toList()), timeTableVO);
                            ZkzlProtocolUtil.timeTableIssued(num, groupIdList.stream().skip(9).collect(Collectors.toList()).size(), loopNumList.stream().skip(9).collect(Collectors.toList()), timeTableVO);
                        } else {
                            ZkzlProtocolUtil.timeTableIssued(num, groupIdList.size(), loopNumList, timeTableVO);
                        }
                    }
                }
            }
        }
        updateDeviceAndStrategy(lampDeviceIdList, systemDeviceList, strategyId);
        return new Result().success("策略下发成功");
    }

    /**
     * 更新关联表信息
     *
     * @param lampDeviceIdList
     * @param systemDeviceList
     * @param strategyId
     */
    private void updateDeviceAndStrategy(List<Integer> lampDeviceIdList, List<SystemDevice> systemDeviceList, Integer strategyId) {
        // 将设备表中的策略id更新
        List<Integer> systemDeviceIdList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
        LambdaUpdateWrapper<SystemDevice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SystemDevice::getStrategyId, strategyId).set(SystemDevice::getSetStrategy,0)
                .in(SystemDevice::getId, systemDeviceIdList);
        systemDeviceService.update(updateWrapper);

        // 添加新的设备与策略关联关系
        /*List<DeviceStrategyHistory> deviceStrategyHistoryList = new ArrayList<>();
        Date createTime = new Date();
        for (Integer deviceId : lampDeviceIdList) {
            DeviceStrategyHistory deviceStrategyHistory = new DeviceStrategyHistory();
            deviceStrategyHistory.setDeviceId(deviceId);
            deviceStrategyHistory.setStrategyId(strategyId);
            deviceStrategyHistoryList.add(deviceStrategyHistory);
            deviceStrategyHistory.setCreateTime(createTime);
        }
        deviceStrategyHistoryService.saveBatch(deviceStrategyHistoryList);*/
    }


    @Override
    public void updateDeviceAndStrategy(List<Integer> systemDeviceIdList, Integer strategyId) {
        logger.info("更新设备当前绑定的策略：{}，{}", systemDeviceIdList, strategyId);
        for (Integer deviceId : systemDeviceIdList) {
            // 将设备表中的策略id更新
            SystemDevice systemDevice = new SystemDevice();
            systemDevice.setId(deviceId);
            systemDevice.setStrategyId(strategyId);
            systemDeviceService.updateById(systemDevice);
            //修改最新一条绑定数据
            QueryWrapper<DeviceStrategyHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("device_id", deviceId);
            queryWrapper.eq("strategy_id", strategyId);
            DeviceStrategyHistory deviceStrategyHistory = deviceStrategyHistoryService.selectNewOne(deviceId, strategyId);
            deviceStrategyHistory.setIsSuccess(3);
            deviceStrategyHistoryService.updateById(deviceStrategyHistory);
        }
    }

    @Override
    public void updateDeviceAndStrategy(List<Integer> deviceStrategyHistoryIdList) {
        logger.info("更新设备当前绑定的策略，历史记录id：{}", deviceStrategyHistoryIdList);
        //更改设备下发策略的状态
        if(deviceStrategyHistoryIdList==null||deviceStrategyHistoryIdList.size()==0){
            return;
        }
        QueryWrapper<DeviceStrategyHistory> deviceStrategyHistoryQueryWrapper = new QueryWrapper<>();
        deviceStrategyHistoryQueryWrapper.in("id",deviceStrategyHistoryIdList);
        List<DeviceStrategyHistory> deviceStrategyHistoryList = deviceStrategyHistoryService.list(deviceStrategyHistoryQueryWrapper);
        List<Integer> deviceIdList = deviceStrategyHistoryList.stream().map(DeviceStrategyHistory::getDeviceId).collect(Collectors.toList());
        List<SystemDevice> systemDeviceList = new ArrayList<>();
        for (Integer deviceId : deviceIdList) {
            SystemDevice systemDevice = new SystemDevice();
            systemDevice.setId(deviceId);
            systemDevice.setSetStrategy(1);
            systemDeviceList.add(systemDevice);
        }
        systemDeviceService.updateBatchById(systemDeviceList);
        //修改最新一条绑定数据
        for (DeviceStrategyHistory deviceStrategyHistory : deviceStrategyHistoryList) {
            deviceStrategyHistory.setIsSuccess(100);
        }
        deviceStrategyHistoryService.updateBatchById(deviceStrategyHistoryList);
    }

    @Override
    public Result singleLampExecute(List<SystemDevice> systemDeviceList, List<LampStrategyAction> lampStrategyActionList, Integer scene,List<DeviceStrategyHistory> deviceStrategyHistoryList) {
        logger.info("自研单灯策略下发：{}，{}", systemDeviceList, lampStrategyActionList);
        Map<Integer, Integer> strategyHistoryIdMap = deviceStrategyHistoryList.stream().collect(Collectors.toMap(DeviceStrategyHistory::getDeviceId, DeviceStrategyHistory::getId));

        List<SlControlSystemDeviceVO> slControlSystemDeviceVOList = new ArrayList<>();
        for (SystemDevice systemDevice : systemDeviceList) {
            SlControlSystemDeviceVO slControlSystemDeviceVO = new SlControlSystemDeviceVO();
            BeanUtils.copyProperties(systemDevice, slControlSystemDeviceVO);
            Integer strategyHistoryId = strategyHistoryIdMap.get(systemDevice.getId());
            slControlSystemDeviceVO.setStrategyHistoryId(strategyHistoryId);
            slControlSystemDeviceVOList.add(slControlSystemDeviceVO);
            String loopNum = lampDeviceParameterService.select(systemDevice.getId(), "支路数", systemDevice.getDeviceTypeId());
            if (loopNum == null || loopNum.length() == 0) {
                loopNum = "1";
            }
            slControlSystemDeviceVO.setLoopNum(loopNum);
        }
        //自研灯具策略下发
        Map<Pair<String, Integer>, List<SlControlSystemDeviceVO>> singleLampParamMap =
                slControlSystemDeviceVOList.stream().collect(Collectors.groupingBy(p -> Pair.of(p.getNum(), p.getDeviceTypeId())));
        List<Pair<String, Integer>> deviceGroupingByFlagList = slControlSystemDeviceVOList.stream().map(p -> Pair.of(p.getNum(), p.getDeviceTypeId())).distinct().collect(Collectors.toList());
        List<List<SlControlSystemDeviceVO>> deviceGroupingByFlag = new ArrayList<>();

        for (Pair<String, Integer> stringIntegerPair : deviceGroupingByFlagList) {
            List<SlControlSystemDeviceVO> slControlSystemDeviceVOS = singleLampParamMap.get(stringIntegerPair);
            deviceGroupingByFlag.add(slControlSystemDeviceVOS);
        }

        /*List<Integer> deviceIdList = systemDeviceList.stream().map(SystemDevice::getId).distinct().collect(Collectors.toList());
        Map<Integer, List<SingleLampParam>> groupByDeviceIdMap = singleLampParamList.stream()
                .collect(Collectors.groupingBy(SingleLampParam::getDeviceId));
        Map<Integer,List<Integer>> loopNumsMap = new HashMap<>();
        for (Integer deviceId : deviceIdList) {
            LampDevice lampDevice = lampDeviceService.getById(deviceId);
            Integer loopTypeId = lampDevice.getLoopTypeId();
            List<Integer> loopNums = new ArrayList<>();
            if(loopTypeId == 1){
                loopNums.add(1);
            }else if(loopTypeId == 2){
                List<SingleLampParam> groupByDeviceIdList = groupByDeviceIdMap.get(deviceId);
                for (SingleLampParam singleLampParam : groupByDeviceIdList) {
                    loopNums.add(singleLampParam.getLoopNum());
                }
            }
            loopNumsMap.put(lampDevice.getId(),loopNums);
        }*/
        singleLampParamService.setStrategy(deviceGroupingByFlag, lampStrategyActionList, scene);


        List<String> shuncomNumList = new ArrayList<>();
        for (SystemDevice systemDevice : systemDeviceList) {
            if (systemDevice.getDeviceTypeId() == 11) {
                shuncomNumList.add(systemDevice.getNum());
            }
        }
        if (!shuncomNumList.isEmpty()) {
            Result enable = SendHttpsUtil.strategyEnable(shuncomNumList);
            if (enable.getCode() == 400) {
                return enable;
            }
            Result result = SendHttpsUtil.setStrategy(shuncomNumList, lampStrategyActionList);
            if (result.getCode() == 400) {
                return result;
            }
        }
        return new Result().success("下发成功");
    }
}