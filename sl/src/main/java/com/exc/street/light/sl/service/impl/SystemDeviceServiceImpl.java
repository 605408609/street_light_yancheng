/**
 * @filename:SystemDeviceServiceImpl 2020-09-02
 * @project sl  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd.
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
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.SlLampDeviceQuery;
import com.exc.street.light.resource.utils.BaseConstantUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.SlControlSystemDeviceVO;
import com.exc.street.light.resource.vo.req.SlDeviceLocationControlVO;
import com.exc.street.light.resource.vo.req.SlReqInstallLampZkzlVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import com.exc.street.light.resource.vo.resp.SlRespControlVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceParameterVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceVO;
import com.exc.street.light.resource.vo.sl.LoopParamVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamReqVO;
import com.exc.street.light.sl.config.parameter.HttpDlmApi;
import com.exc.street.light.sl.config.parameter.LoraApi;
import com.exc.street.light.sl.config.parameter.SlLampParameter;
import com.exc.street.light.sl.mapper.SingleLampParamDao;
import com.exc.street.light.sl.mapper.SystemDeviceMapper;
import com.exc.street.light.sl.service.*;
import com.exc.street.light.sl.task.SystemDeviceControlTask;
import com.exc.street.light.sl.utils.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(设备表服务实现)
 * @version: V1.0
 * @author: Huang Jin Hao
 */
@Service
public class SystemDeviceServiceImpl extends ServiceImpl<SystemDeviceMapper, SystemDevice> implements SystemDeviceService {

    private static final Logger logger = LoggerFactory.getLogger(SystemDeviceServiceImpl.class);

    @Autowired
    private LampApiUtil lampApiUtil;
    @Autowired
    private SlLampParameter slLampParameter;
    @Autowired
    private HttpDlmApi httpDlmApi;
    @Autowired
    private LoraApi loraApi;
    @Autowired
    private LampDeviceService lampDeviceService;
    @Autowired
    private SingleLampParamService singleLampParamService;
    @Autowired
    private SingleLampParamDao singleLampParamDao;
    @Autowired
    private LogUserService logUserService;
    @Autowired
    private LampLoopTypeService lampLoopTypeService;
    @Autowired
    private LampPositionService lampPositionService;
    @Autowired
    private LampGroupSingleService lampGroupSingleService;
    @Autowired
    private CTWingApi ctWingApi;
    @Autowired
    private LampStrategyService lampStrategyService;
    @Autowired
    private LampDeviceStrategyService lampDeviceStrategyService;
    @Autowired
    private LampDeviceParameterService lampDeviceParameterService;
    @Autowired
    private SystemDeviceParameterService systemDeviceParameterService;
    @Autowired
    private ControlLoopDeviceService controlLoopDeviceService;
    @Autowired
    private LocationControlService locationControlService;
    @Autowired
    private DeviceStrategyHistoryService deviceStrategyHistoryService;

    /*@Override
    public Result subscription(Integer type, String url,HttpServletRequest request) {
        logger.info("设置设备订阅地址，接收参数：{}，{}",type,url);
        String notifyType = "";
        if(type==1){
            notifyType = "deviceDataChanged";
        }else {
            notifyType = "deviceInfoChanged";
        }
        SendHttpsUtil.deviceSubscription(notifyType,url);
        return new Result().success("设置成功");
    }

    @Override
    public Result<LampDevice> getByNum(String num, String model, String factory) {
        Result<LampDevice> result = new Result<>();
        QueryWrapper<LampDevice> queryWrapper = new QueryWrapper<>();
        if(model!=null && model.length()>0){
            queryWrapper.eq("model",model);
            if("lora_new".equals(model)){
                queryWrapper.eq("send_id",num);
            }else {
                queryWrapper.eq("num",num);
            }
        }
        if(factory!=null && factory.length()>0){
            queryWrapper.eq("factory",factory);
        }
        LampDevice lampDevice = baseMapper.selectOne(queryWrapper);
        return result.success(lampDevice);
    }

    @Override
    public Result<List<LampDevice>> getList() {
        List<LampDevice> list = this.list(null);
        Result<List<LampDevice>> result = new Result();
        return result.success(list);
    }

    @Override
    public Result<List<LampDevice>> getListByIdList(List<Integer> idList) {
        QueryWrapper<LampDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",idList);
        List<LampDevice> lampDeviceList = lampDeviceDao.selectList(queryWrapper);
        Result<List<LampDevice>> result = new Result();
        return result.success(lampDeviceList);
    }

    @Override
    public Result singleControl(SlReqDeviceControlVO vo, LampDevice lampDevice) {
        logger.info("路灯单灯控制接收参数:{}", vo);
        Result result = new Result();
        HttpUtil httpUtil = new HttpUtil();
        Map<String, String> contentMap = new HashMap<>(1);
//        contentMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        contentMap.put("Content-Type", "application/json;charset=UTF-8");
        contentMap.put("token", vo.getToken());
        logger.info("contentMap :{}", contentMap);
        String json = HttpUtil.post(slLampParameter.getAddress() + slLampParameter.getControlUrl(), JSON.toJSONString(vo), contentMap);
        logger.info("单灯控制返回信息:{}", json);
        if (json == null) {
            return result.error("第三方接口调用超时", lampDevice);
        }
        JSONObject jsonObject = JSON.parseObject(json);
        int singleResult = jsonObject.getIntValue("result");
        if (singleResult == 1) {
            return result.error("设备不存在", lampDevice);
        }
        return result.success(lampDevice);
    }*/


    @Override
    public Result control(HttpServletRequest request, SlReqLightControlVO vo) {
        logger.info("路灯实时控制接收参数:{}", vo);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        /*String sendMode = vo.getSendMode();
        if(sendMode.isEmpty()){
            return new Result().error("未选择发送模式");
        }*/
        Result respResult = new Result();
        // 站点id集合
        //List<Integer> siteIdList = vo.getSiteIdList();
        // 灯杆id集合
        List<Integer> lampPostList = vo.getLampPostIdList();
        // 分组id集合
        List<Integer> groupIdList = vo.getGroupIdList();
        // 灯具id集合
        List<Integer> systemDeviceIdList = vo.getLampDeviceIdList();
        // 获取集中控制器分组id集合
        List<Integer> controlLoopIdList = vo.getControlLoopIdList();
        // 获取灯具设备集合
        List<SlRespSystemDeviceVO> systemDeviceParamList = new ArrayList<>();

        //优先获取灯具集合
        if (systemDeviceIdList != null && systemDeviceIdList.size() > 0) {
            SlLampDeviceQuery slLampDeviceQuery = new SlLampDeviceQuery();
            slLampDeviceQuery.setSingleIdList(systemDeviceIdList);
            slLampDeviceQuery.setPageSize(0);
            Result page = this.getPage(slLampDeviceQuery, request);
            systemDeviceParamList = (List<SlRespSystemDeviceVO>)page.getData();
        }
        // 优先按路灯灯杆id集合,获得灯具设备集合
        else if (lampPostList != null && lampPostList.size() > 0) {
            List<SystemDevice> listByLampPost = getListByLampPost(lampPostList, request);
            systemDeviceIdList = listByLampPost.stream().map(SystemDevice::getId).collect(Collectors.toList());
            SlLampDeviceQuery slLampDeviceQuery = new SlLampDeviceQuery();
            slLampDeviceQuery.setSingleIdList(systemDeviceIdList);
            slLampDeviceQuery.setPageSize(0);
            Result page = this.getPage(slLampDeviceQuery, request);
            systemDeviceParamList = (List<SlRespSystemDeviceVO>)page.getData();
        }
        // 根据站点id集合获取灯具设备集合
        /*else if (siteIdList != null && siteIdList.size() > 0) {
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
                return respResult.error("根据站点id集合获取灯杆集合接口调用失败，返回为空！");
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
        }*/
        // 根据分组id集合获取灯具设备集合
        else if (groupIdList != null && groupIdList.size() > 0) {
            // 获取路灯灯杆集合
            List<SlLampPost> slLampPostList = null;
            String json = "groupIdList=";
            for (Integer groupId : groupIdList) {
                json += groupId + "&groupIdList=";
            }
            try {
                JSONObject ssDeviceResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetLampPostByGroupIdList() + "?" + json, headMap));
                JSONArray ssDeviceResultArr = ssDeviceResult.getJSONArray("data");
                slLampPostList = JSON.parseObject(ssDeviceResultArr.toJSONString(), new TypeReference<List<SlLampPost>>() {
                });
            } catch (Exception e) {
                logger.error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
                return respResult.error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
            }
            // 获取路灯灯具设备集合
            List<Integer> lampPostIdList = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
            if (lampPostIdList != null && lampPostIdList.size() > 0) {
                List<SystemDevice> listByLampPost = getListByLampPost(lampPostIdList, request);
                systemDeviceIdList = listByLampPost.stream().map(SystemDevice::getId).collect(Collectors.toList());
                SlLampDeviceQuery slLampDeviceQuery = new SlLampDeviceQuery();
                slLampDeviceQuery.setSingleIdList(systemDeviceIdList);
                slLampDeviceQuery.setPageSize(0);
                Result page = this.getPage(slLampDeviceQuery, request);
                systemDeviceParamList = (List<SlRespSystemDeviceVO>)page.getData();
            }
        } else if (controlLoopIdList != null && controlLoopIdList.size() > 0) {
            String json = "loopIdList=";
            for (Integer controlLoopId : controlLoopIdList) {
                json += controlLoopId + "&loopIdList=";
            }
            JSONObject systemDeviceResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetControlLoopDeviceByLoopIdList() + "?" + json, headMap));
            JSONArray systemDeviceResultArr = systemDeviceResult.getJSONArray("data");
            List<ControlLoopDevice> controlLoopDeviceList = JSON.parseObject(systemDeviceResultArr.toJSONString(), new TypeReference<List<ControlLoopDevice>>() {
            });
            List<Integer> deviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).distinct().collect(Collectors.toList());
            SlLampDeviceQuery slLampDeviceQuery = new SlLampDeviceQuery();
            slLampDeviceQuery.setSingleIdList(deviceIdList);
            slLampDeviceQuery.setPageSize(0);
            Result page = this.getPage(slLampDeviceQuery, request);
            systemDeviceParamList = (List<SlRespSystemDeviceVO>)page.getData();
        }

        if (systemDeviceParamList.isEmpty()) {
            return respResult.error("当前目标没有设备");
        }

        /*List<Integer> networkStateList = systemDeviceParamList.stream().map(SystemDevice::getIsOnline).collect(Collectors.toList());
        if(networkStateList.contains(0)){
            return respResult.error("部分设备离线，指令下发失败");
        }*/
        //获取集中控制器num
        List<Integer> systemDeviceTempIdList = systemDeviceParamList.stream().map(SlRespSystemDeviceVO::getId).collect(Collectors.toList());
        List<SlDeviceLocationControlVO> locationControlNums = getLocationControlNums(systemDeviceTempIdList);
        Map<Integer, String> locationControlNumMap = locationControlNums.stream().collect(Collectors.toMap(SlDeviceLocationControlVO::getDeviceId, SlDeviceLocationControlVO::getNum));
        List<SlControlSystemDeviceVO> slControlSystemDeviceVOS = new ArrayList<>();
        // 更新设备当前设置的状态和亮度预设值
        for (SlRespSystemDeviceVO systemDevice : systemDeviceParamList) {
            SlControlSystemDeviceVO slControlSystemDeviceVO = new SlControlSystemDeviceVO();
            if (systemDevice.getDeviceTypeId() == 13) {
                String locationControlNum = locationControlNumMap.get(systemDevice.getId());
                if (locationControlNum == null || locationControlNum.length() == 0) {
                    return new Result().error("存在设备未绑定集中控制器");
                } else {
                    slControlSystemDeviceVO.setLocationControlNum(locationControlNum);
                }
            }
            BeanUtils.copyProperties(systemDevice, slControlSystemDeviceVO);
            if (vo.getType() != null) {
                slControlSystemDeviceVO.setDeviceState(vo.getType());
            }
            if(vo.getLightness() != null){
                slControlSystemDeviceVO.setBrightness(vo.getLightness());
            }
            String loopNum = lampDeviceParameterService.select(systemDevice.getId(), "支路数", systemDevice.getDeviceTypeId());
            slControlSystemDeviceVO.setLoopNum(loopNum);
            slControlSystemDeviceVOS.add(slControlSystemDeviceVO);
        }
        Map<Pair<String, Integer>, List<SlControlSystemDeviceVO>> singleLampParamMap =
                slControlSystemDeviceVOS.stream().collect(Collectors.groupingBy(p -> Pair.of(p.getNum(), p.getDeviceTypeId())));
        List<Pair<String, Integer>> deviceGroupingByFlagList = slControlSystemDeviceVOS.stream().map(p -> Pair.of(p.getNum(), p.getDeviceTypeId())).distinct().collect(Collectors.toList());
        // 多线程
        Collection<SystemDeviceControlTask> tasks = new ArrayList<>();
        for (Pair<String, Integer> deviceGroupingByFlag : deviceGroupingByFlagList) {
            List<SlControlSystemDeviceVO> slControlSystemDeviceVOSList = singleLampParamMap.get(deviceGroupingByFlag);
            SystemDeviceControlTask task = new SystemDeviceControlTask(slControlSystemDeviceVOSList, this);
            tasks.add(task);
        }


        /*for (SlControlSystemDeviceVO slControlSystemDeviceVO : slControlSystemDeviceVOS) {
            SystemDeviceControlTask task = new SystemDeviceControlTask(slControlSystemDeviceVO, this);
            tasks.add(task);
        }*/
        //开启多线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("channel-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(BaseConstantUtil.THREADPOOLSIZE_2,
                BaseConstantUtil.THREADPOOLSIZE_2, 0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), namedThreadFactory);


        logger.info("运行任务");
        Result result = this.getResult(tasks, executorService);
        // 修改当前状态，亮度，预设亮度
        SlRespControlVO slRespControlVO = (SlRespControlVO) result.getData();
        if (slRespControlVO.getSuccessLampDeviceList() != null && slRespControlVO.getSuccessLampDeviceList().size() > 0) {
            List<SlControlSystemDeviceVO> successLampDeviceList = slRespControlVO.getSuccessLampDeviceList();
            updateByControlParam(successLampDeviceList);
        }
        return result;
    }

    /*@Override
    public Result controlTemp(HttpServletRequest request, SlReqLightControlVO vo) {
        System.out.println("临时灯具控制");
        //设置亮度
        Integer lightness = vo.getLightness();
        Double colorValue1 = (vo.getColorValue() * (lightness.doubleValue()/100))/2.55;
        Double colorValue2 = ((255-vo.getColorValue()) * (lightness.doubleValue()/100))/2.55;
        Integer lightness1 = 1;
        Integer lightness2 = 1;
        if(colorValue1>1){
            lightness1 = colorValue1.intValue();
        }
        if(colorValue2>1){
            lightness2 = colorValue2.intValue();
        }
        String stateOne = "01";
        String stateTwo = "01";
        if(vo.getType()==0){
            stateOne = "00";
            stateTwo = "00";
        }
        System.out.println(loraApi.getNum());
        System.out.println(loraApi.getSendId());

        String message = MessageGeneration.nbSingleLampControl(loraApi.getNum(), lightness1, stateOne,lightness2,stateTwo);
        System.out.println(message);
        boolean nb = false;
        if(message!=null&&message.length()>0){
            nb = MessageOperationUtil.sendByMode(message, "nb", loraApi.getSendId());
        }
        if(nb){
            return new Result().success("控制成功");
        }else {
            return new Result().error("控制失败");
        }

    }*/

    @Override
    public Result getResult(Collection tasks, ExecutorService executorService) {
        SlRespControlVO vo = null;
        try {
            List<Future<Result>> futures = executorService.invokeAll(tasks);
            vo = new SlRespControlVO();
            int successNum = 0;
            int defaultNum = 0;
            List<SlControlSystemDeviceVO> successLampDeviceList = new ArrayList<>();
            List<SlControlSystemDeviceVO> defaultLampDeviceList = new ArrayList<>();
            for (Future<Result> future : futures) {
                Result result = future.get();
                List<SlControlSystemDeviceVO> slControlSystemDeviceVOS = (List<SlControlSystemDeviceVO>) result.getData();
                for (SlControlSystemDeviceVO slControlSystemDeviceVO : slControlSystemDeviceVOS) {
                    logger.info(slControlSystemDeviceVO.getName() + ":" + result.getMessage());
                    if (result.getCode() == Const.CODE_SUCCESS) {
                        successNum += 1;
                        // 设置下发成功的灯具的正确状态和亮度
                    /*lampDevice.setBrightState(lampDevice.getPresetBrightState());
                    lampDevice.setBrightness(lampDevice.getPresetBrightness());*/
                        successLampDeviceList.add(slControlSystemDeviceVO);
                    } else {
                        defaultNum += 1;
                        defaultLampDeviceList.add(slControlSystemDeviceVO);
                    }
                }

            }
            vo.setSuccessNum(successNum);
            vo.setSuccessLampDeviceList(successLampDeviceList);
            vo.setDefaultNum(defaultNum);
            vo.setDefaultLampDeviceList(defaultLampDeviceList);
            if (vo.getDefaultNum() > 0) {
                Result result = new Result();
                result.setCode(Const.CODE_FAILED);
                result.setMessage("控制失败");
                result.setData(vo);
                return result;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            executorService.shutdown();
        }
        Result result = new Result();
        return result.success(vo);
    }

    @Override
    public Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request) {
        if (lampPostIdList == null || lampPostIdList.size() == 0) {
            lampPostIdList = new ArrayList<>();
            Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
            if (userId != 1) {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("token", request.getHeader("token"));
                User user = logUserService.get(userId);
                Integer areaId = user.getAreaId();
                List<Integer> areaLampPostIdList = areaLampPostIdList(areaId);
                if (areaLampPostIdList.isEmpty()) {
                    lampPostIdList.add(-1);
                } else {
                    lampPostIdList.addAll(areaLampPostIdList);
                }
            }

        }
        List<SlRespSystemDeviceVO> singleLampParamList = baseMapper.getSingleLampParamList(lampPostIdList);
        List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
        for (SlRespSystemDeviceVO singleLampParamRespVO : singleLampParamList) {
            if (specialDeviceTypeList.contains(singleLampParamRespVO.getDeviceTypeId())) {
                String numTemp = singleLampParamRespVO.getNum();
                String deviceNum = String.valueOf(Integer.parseInt(numTemp, 16));
                String prefix = "";
                for (int i = 0; i < 8 - deviceNum.length(); i++) {
                    prefix += "0";
                }
                deviceNum = prefix + deviceNum;
                singleLampParamRespVO.setNum(deviceNum);
            }
        }
        /*LambdaQueryWrapper<LampDevice> wrapper = new LambdaQueryWrapper();
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            wrapper.in(LampDevice::getLampPostId, lampPostIdList);
        }
        List<LampDevice> list = this.list(wrapper);
        List<SingleLampParamRespVO> singleLampParamRespVOList = new ArrayList<>();
        for (LampDevice lampDevice : list) {
            List<SingleLampParam> singleLampByDeviceId = singleLampParamService.getSingleLampByDeviceId(lampDevice.getId(), null);
            for (SingleLampParam singleLampParam : singleLampByDeviceId) {
                SingleLampParamRespVO singleLampParamRespVO = new SingleLampParamRespVO();
                BeanUtils.copyProperties(lampDevice,singleLampParamRespVO);
                BeanUtils.copyProperties(singleLampParam,singleLampParamRespVO);
                LampLoopType lampLoopType = lampLoopTypeService.getById(singleLampParam.getLoopTypeId());
                LampPosition lampPosition = lampPositionService.getById(singleLampParam.getLampPositionId());
                singleLampParamRespVO.setLoopType(lampLoopType.getType());
                singleLampParamRespVO.setLampPosition(lampPosition.getPosition());
                String model = singleLampParamRespVO.getModel();
                String factory = singleLampParamRespVO.getFactory();
                if(!("nb".equals(model)&&"EXC2".equals(factory))){
                    String numTemp = singleLampParamRespVO.getNum();
                    String deviceNum = String.valueOf(Integer.parseInt(numTemp, 16));
                    String prefix = "";
                    for (int i = 0; i < 8 - deviceNum.length(); i++){
                        prefix += "0";
                    }
                    deviceNum = prefix + deviceNum;
                    singleLampParamRespVO.setNum(deviceNum);
                }
                singleLampParamRespVOList.add(singleLampParamRespVO);
            }
        }*/
        Result result = new Result();
        return result.success(singleLampParamList);
    }

    @Override
    public List<SystemDevice> getListByLampPost(List<Integer> lampPostIdList, HttpServletRequest request) {
        LambdaQueryWrapper<SystemDevice> wrapper = new LambdaQueryWrapper();
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            wrapper.in(SystemDevice::getLampPostId, lampPostIdList);
        }
        return this.list(wrapper);
    }


    @Override
    public Result getPage(SlLampDeviceQuery slLampDeviceQuery, HttpServletRequest request) {
        logger.info("灯控设备分页条件查询,接收参数:{}", slLampDeviceQuery);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flg = logUserService.isAdmin(userId);
        if (!flg) {
            slLampDeviceQuery.setAreaId(user.getAreaId());
        }
        List<LampPosition> lampPositionList = lampPositionService.list();
        Map<Integer, String> lampPositionMap = lampPositionList.stream().collect(Collectors.toMap(LampPosition::getId, LampPosition::getPosition));
        List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
        Result result = new Result();
        if (slLampDeviceQuery.getPageSize() == 0) {
            List<SlRespSystemDeviceVO> slRespLampDeviceVOList = baseMapper.getPage(slLampDeviceQuery);
            if (slRespLampDeviceVOList != null) {
                for (SlRespSystemDeviceVO slRespLampDeviceVO : slRespLampDeviceVOList) {
                    List<SlRespSystemDeviceParameterVO> slRespSystemDeviceParameterVOS = slRespLampDeviceVO.getSlRespSystemDeviceParameterVOS();
                    if (slRespSystemDeviceParameterVOS != null) {
                        for (SlRespSystemDeviceParameterVO slRespSystemDeviceParameterVO : slRespSystemDeviceParameterVOS) {
                            String name = slRespSystemDeviceParameterVO.getName();
                            if ("亮度".equals(name)) {
                                String parameterValue = slRespSystemDeviceParameterVO.getParameterValue();
                                if (parameterValue != null && parameterValue.length() > 0) {
                                    int brightness = Integer.parseInt(parameterValue);
                                    slRespLampDeviceVO.setBrightness(brightness);
                                }
                            }
                            if ("灯具位置".equals(name)) {
                                String parameterValue = slRespSystemDeviceParameterVO.getParameterValue();
                                if (parameterValue != null && parameterValue.length() > 0) {
                                    Integer lampPositionId = Integer.parseInt(parameterValue);
                                    slRespLampDeviceVO.setLampPositionId(lampPositionId);
                                    String position = lampPositionMap.get(lampPositionId);
                                    if (position != null) {
                                        slRespLampDeviceVO.setLampPosition(position);
                                    }
                                }
                            }

                        }
                    }
                    if (specialDeviceTypeList.contains(slRespLampDeviceVO.getDeviceTypeId())) {
                        String numTemp = slRespLampDeviceVO.getNum();
                        String deviceNum = String.valueOf(Integer.parseInt(numTemp, 16));
                        String prefix = "";
                        for (int i = 0; i < 8 - deviceNum.length(); i++) {
                            prefix += "0";
                        }
                        deviceNum = prefix + deviceNum;
                        slRespLampDeviceVO.setNum(deviceNum);
                    }
                }
            }
            return result.success(slRespLampDeviceVOList);
        } else {
            IPage<Integer> systemDeviceIPage = new Page<>(slLampDeviceQuery.getPageNum(), slLampDeviceQuery.getPageSize());
            /*LambdaQueryWrapper<SystemDevice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.select(SystemDevice::getId);
            lambdaQueryWrapper.orderByDesc(SystemDevice::getCreateTime);
            IPage<SystemDevice> systemDeviceIPageResult = baseMapper.selectPage(systemDeviceIPage, lambdaQueryWrapper);*/
            IPage<Integer> pageId = baseMapper.getPageId(systemDeviceIPage, slLampDeviceQuery);
            List<Integer> records = pageId.getRecords();
            if (records == null || records.size() == 0) {
                IPage<SlRespSystemDeviceVO> iPage = new Page<SlRespSystemDeviceVO>(slLampDeviceQuery.getPageNum(), slLampDeviceQuery.getPageSize());
                List<SlRespSystemDeviceVO> slRespLampDeviceVOList = new ArrayList<>();
                iPage.setRecords(slRespLampDeviceVOList);
                return result.success("数据为空",iPage);
            }
            /*List<SystemDevice> systemDeviceList = systemDeviceIPageResult.getRecords();
            List<Integer> deviceIdList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());*/
            slLampDeviceQuery.setDeviceIdList(records);
            IPage<SlRespSystemDeviceVO> iPage = new Page<SlRespSystemDeviceVO>(slLampDeviceQuery.getPageNum(), slLampDeviceQuery.getPageSize());
            List<SlRespSystemDeviceVO> slRespLampDeviceVOList = baseMapper.getPage(slLampDeviceQuery);
            iPage.setTotal(pageId.getTotal());
            if (slRespLampDeviceVOList != null) {
                for (SlRespSystemDeviceVO slRespLampDeviceVO : slRespLampDeviceVOList) {
                    List<SlRespSystemDeviceParameterVO> slRespSystemDeviceParameterVOS = slRespLampDeviceVO.getSlRespSystemDeviceParameterVOS();
                    if (slRespSystemDeviceParameterVOS != null) {
                        for (SlRespSystemDeviceParameterVO slRespSystemDeviceParameterVO : slRespSystemDeviceParameterVOS) {
                            String name = slRespSystemDeviceParameterVO.getName();
                            if ("亮度".equals(name)) {
                                String parameterValue = slRespSystemDeviceParameterVO.getParameterValue();
                                if (parameterValue != null && parameterValue.length() > 0) {
                                    int brightness = Integer.parseInt(parameterValue);
                                    slRespLampDeviceVO.setBrightness(brightness);
                                }
                            }
                            if ("灯具位置".equals(name)) {
                                String parameterValue = slRespSystemDeviceParameterVO.getParameterValue();
                                if (parameterValue != null && parameterValue.length() > 0) {
                                    Integer lampPositionId = Integer.parseInt(parameterValue);
                                    slRespLampDeviceVO.setLampPositionId(lampPositionId);
                                    String position = lampPositionMap.get(lampPositionId);
                                    if (position != null) {
                                        slRespLampDeviceVO.setLampPosition(position);
                                    }
                                }
                            }
                        }
                    }
                    if (specialDeviceTypeList.contains(slRespLampDeviceVO.getDeviceTypeId())) {
                        String numTemp = slRespLampDeviceVO.getNum();
                        String deviceNum = String.valueOf(Integer.parseInt(numTemp, 16));
                        String prefix = "";
                        for (int i = 0; i < 8 - deviceNum.length(); i++) {
                            prefix += "0";
                        }
                        deviceNum = prefix + deviceNum;
                        slRespLampDeviceVO.setNum(deviceNum);
                    }
                }
            }
            iPage.setRecords(slRespLampDeviceVOList);
            return result.success(iPage);
        }
    }

    @Override
    public Result add(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        logger.info("添加灯控设备，接收参数：{}", singleLampParamReqVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result result = new Result().error("");

        //LampDevice lampDevice = new LampDevice();
        String num = singleLampParamReqVO.getNum();
        //String model = singleLampParamReqVO.getModel();
        Integer deviceTypeId = singleLampParamReqVO.getDeviceTypeId();
        String deviceNum = "";
        List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
        if (specialDeviceTypeList.contains(deviceTypeId)) {
            try {
                deviceNum = Long.toHexString(Long.parseLong(num));
            } catch (Exception e) {
                return result.error("对应设备类型编号只能为数字");
            }
            deviceNum = deviceNum.toUpperCase();
            String prefix = "";
            for (int i = 0; i < 8 - deviceNum.length(); i++) {
                prefix += "0";
            }
            deviceNum = prefix + deviceNum;
        } else {
            deviceNum = num;
        }
        Map<String, String> errorMap = new HashMap<>();
        SystemDeviceParameter sdpLampPosition = systemDeviceParameterService.selectByName(deviceTypeId, "灯具位置");
        SystemDeviceParameter sdpLoopNum = systemDeviceParameterService.selectByName(deviceTypeId, "支路数");
        SystemDeviceParameter sdpBrightness = systemDeviceParameterService.selectByName(deviceTypeId, "亮度");
        Integer sdpLampPositionId = null;
        Integer sdpLoopNumId = null;
        Integer sdpBrightnessId = null;
        if (sdpLampPosition != null) {
            sdpLampPositionId = sdpLampPosition.getId();
        }
        if (sdpLoopNum != null) {
            sdpLoopNumId = sdpLoopNum.getId();
        }
        if (sdpBrightness != null) {
            sdpBrightnessId = sdpBrightness.getId();
        }
        if (null != singleLampParamReqVO) {
            List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
            String sendId = "";
            if (deviceTypeId == 11) {

            } else if (deviceTypeId == 1 || deviceTypeId == 2) {
                sendId = "00" + num;
            } else if (deviceTypeId == 3 || deviceTypeId == 4) {
                String devEui = deviceNum;
                sendId = devEui;
                boolean node = MessageOperationUtil.createNode(loopParamVOList.get(0).getName(), devEui);
                boolean nodeMc = MessageOperationUtil.createNodeMc(devEui, loraApi.getLoraMcId());
                if (!(node && nodeMc)) {
                    return result.error("请正确填写lora编号！");
                }
            } else if (deviceTypeId == 9 || deviceTypeId == 10) {
                String devEui = deviceNum;
                sendId = devEui;
                String newLoraId = MessageOperationUtil.createNewLoraNode(devEui);
                if (newLoraId == null || newLoraId.length() == 0) {
                    return result.error("请正确填写lora编号！");
                } else {
                    deviceNum = newLoraId;
                }
            } else if (deviceTypeId == 5 || deviceTypeId == 6) {
                sendId = "cat1/00" + num;
            } else if (deviceTypeId == 7 || deviceTypeId == 8) {
                String imei = deviceNum;
                String name = singleLampParamReqVO.getLoopParamVOList().get(0).getName();
                try {
                    Result device = ctWingApi.createDevice(name, imei);
                    if (device.getCode() == 200) {
                        sendId = (String) device.getData();
                    } else {
                        String msg = (String)device.getData();
                        if("IMEI���已存在".equals(msg)){
                            msg = "IMEI编号已存在";
                        }
                        return result.error(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return result.error("请正确填写IMEI号");
                }
            } else if (deviceTypeId == 14 || deviceTypeId == 15) {
                String name = singleLampParamReqVO.getLoopParamVOList().get(0).getName();
                try {
                    Result<String> device = CtWingMqttUtil.createDevice(deviceNum, name);
                    if (device.getCode() != Const.CODE_SUCCESS) {
                        return result.error("添加设备失败");
                    }
                    sendId = device.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return result.error("添加设备失败");
                }
            }


            for (int i = 0; i < loopParamVOList.size(); i++) {
                LoopParamVO loopParamVO = loopParamVOList.get(i);
                SystemDevice systemDevice = new SystemDevice();
                systemDevice.setNum(deviceNum);
                systemDevice.setDeviceTypeId(deviceTypeId);
                systemDevice.setLampPostId(singleLampParamReqVO.getLampPostId());
                systemDevice.setCreateTime(new Date());
                systemDevice.setLastOnlineTime(new Date(System.currentTimeMillis() - 86400000));
                systemDevice.setIsOnline(0);
                systemDevice.setCreator(userId);
                systemDevice.setName(loopParamVO.getName());
                systemDevice.setDeviceState(0);
                systemDevice.setReserveOne(sendId);

                boolean rsg = this.save(systemDevice);
                if (rsg) {
                    lampDeviceParameterService.addDefaultParamValue(systemDevice.getId(), singleLampParamReqVO.getDeviceTypeId());
                    if (sdpLampPositionId != null) {
                        lampDeviceParameterService.saveParamValue(systemDevice.getId(), sdpLampPositionId, String.valueOf(loopParamVO.getLampPositionId()));
                    }
                    if (sdpLoopNumId != null) {
                        lampDeviceParameterService.saveParamValue(systemDevice.getId(), sdpLoopNumId, String.valueOf(i + 1));
                    }
                    if (sdpBrightnessId != null) {
                        lampDeviceParameterService.saveParamValue(systemDevice.getId(), sdpBrightnessId, "50");
                    }
                } else {
                    errorMap.put(systemDevice.getName(), "添加失败");
                }
            }
        } else {
            result.error("请传入正确参数！");
        }

        if (errorMap.isEmpty()) {
            return result.success("添加成功");
        } else {
            return result.error("添加失败", errorMap);
        }

    }

    @Override
    public Result addZkzl(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        logger.info("添加灯控设备，接收参数：{}", singleLampParamReqVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        headMap.put("Content-Type", "application/json");
        Result result = new Result().error("");

        String num = singleLampParamReqVO.getNum();
        Integer deviceTypeId = 13;
        String deviceNum = num;

        Map<String, String> errorMap = new HashMap<>();
        SystemDeviceParameter sdpLampPosition = systemDeviceParameterService.selectByName(deviceTypeId, "灯具位置");
        SystemDeviceParameter sdpLoopNum = systemDeviceParameterService.selectByName(deviceTypeId, "支路数");
        SystemDeviceParameter sdpBrightness = systemDeviceParameterService.selectByName(deviceTypeId, "亮度");
        Integer sdpLampPositionId = null;
        Integer sdpLoopNumId = null;
        Integer sdpBrightnessId = null;
        if (sdpLampPosition != null) {
            sdpLampPositionId = sdpLampPosition.getId();
        }
        if (sdpLoopNum != null) {
            sdpLoopNumId = sdpLoopNum.getId();
        }
        if (sdpBrightness != null) {
            sdpBrightnessId = sdpBrightness.getId();
        }
        if (null != singleLampParamReqVO) {
            List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();

            for (int i = 0; i < loopParamVOList.size(); i++) {
                LoopParamVO loopParamVO = loopParamVOList.get(i);
                SystemDevice systemDevice = new SystemDevice();
                systemDevice.setNum(deviceNum);
                systemDevice.setDeviceTypeId(deviceTypeId);
                systemDevice.setLampPostId(singleLampParamReqVO.getLampPostId());
                systemDevice.setCreateTime(new Date());
                systemDevice.setLastOnlineTime(new Date(System.currentTimeMillis() - 86400000));
                systemDevice.setIsOnline(0);
                systemDevice.setCreator(userId);
                systemDevice.setName(loopParamVO.getName());
                systemDevice.setDeviceState(0);

                boolean rsg = this.save(systemDevice);
                if (rsg) {
                    lampDeviceParameterService.addDefaultParamValue(systemDevice.getId(), singleLampParamReqVO.getDeviceTypeId());
                    if (sdpLampPositionId != null) {
                        lampDeviceParameterService.saveParamValue(systemDevice.getId(), sdpLampPositionId, String.valueOf(loopParamVO.getLampPositionId()));
                    }
                    if (sdpLoopNumId != null) {
                        lampDeviceParameterService.saveParamValue(systemDevice.getId(), sdpLoopNumId, String.valueOf(i + 1));
                    }
                    if (sdpBrightnessId != null) {
                        lampDeviceParameterService.saveParamValue(systemDevice.getId(), sdpBrightnessId, "50");
                    }
                } else {
                    errorMap.put(systemDevice.getName(), "添加失败");
                }

                Integer concentratorId = singleLampParamReqVO.getConcentratorId();

                ControlLoop controlLoop = controlLoopDeviceService.selectControlLoopByNum(concentratorId, "1");
                List<Integer> deviceIdList = new ArrayList<>();
                deviceIdList.add(systemDevice.getId());
                if (controlLoop == null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", String.valueOf(System.currentTimeMillis()));
                    jsonObject.put("controlId", concentratorId);
                    jsonObject.put("deviceIdList", deviceIdList);
                    System.out.println(httpDlmApi.getUrl() + httpDlmApi.getAddControlLoop());
                    JSONObject addControlLoopResult = JSON.parseObject(HttpUtil.post(httpDlmApi.getUrl() + httpDlmApi.getAddControlLoop(), jsonObject.toJSONString(), headMap));
                    Integer code = (Integer) addControlLoopResult.get("code");
                    if (code == 400) {
                        return result.error("创建分组错误");
                    }
                } else {
                    ControlLoopDevice controlLoopDevice = new ControlLoopDevice();
                    controlLoopDevice.setDeviceId(systemDevice.getId());
                    controlLoopDevice.setControlId(concentratorId);
                    controlLoopDevice.setLoopId(controlLoop.getId());
                    boolean flag = controlLoopDeviceService.save(controlLoopDevice);
                    if (flag) {
                        LocationControl byId = locationControlService.getById(concentratorId);
                        SlReqInstallLampZkzlVO slReqInstallLampZkzlVO = new SlReqInstallLampZkzlVO();
                        slReqInstallLampZkzlVO.setConcentratorId(byId.getNum());
                        slReqInstallLampZkzlVO.setInstallNum(1);
                        slReqInstallLampZkzlVO.setAddOrDelete(1);
                        slReqInstallLampZkzlVO.setGroupNo(1);
                        slReqInstallLampZkzlVO.setDeviceIdList(deviceIdList);
                        registerDevice(request, slReqInstallLampZkzlVO);
                    }

                }
            }
        } else {
            result.error("请传入正确参数！");
        }

        if (errorMap.isEmpty()) {
            return result.success("添加成功");
        } else {
            return result.error("添加失败", errorMap);
        }

    }

    @Override
    public Result addNh(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        logger.info("添加灯控设备，接收参数：{}", singleLampParamReqVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result result = new Result().error("");

        String num = singleLampParamReqVO.getNum();
        Integer deviceTypeId = singleLampParamReqVO.getDeviceTypeId();
        String deviceNum = num;

        Map<String, String> errorMap = new HashMap<>();
        SystemDeviceParameter sdpLampPosition = systemDeviceParameterService.selectByName(deviceTypeId, "灯具位置");
        SystemDeviceParameter sdpLoopNum = systemDeviceParameterService.selectByName(deviceTypeId, "支路数");
        SystemDeviceParameter sdpBrightness = systemDeviceParameterService.selectByName(deviceTypeId, "亮度");
        Integer sdpLampPositionId = null;
        Integer sdpLoopNumId = null;
        Integer sdpBrightnessId = null;
        if (sdpLampPosition != null) {
            sdpLampPositionId = sdpLampPosition.getId();
        }
        if (sdpLoopNum != null) {
            sdpLoopNumId = sdpLoopNum.getId();
        }
        if (sdpBrightness != null) {
            sdpBrightnessId = sdpBrightness.getId();
        }
        if (null != singleLampParamReqVO) {
            List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();

            for (int i = 0; i < loopParamVOList.size(); i++) {
                LoopParamVO loopParamVO = loopParamVOList.get(i);
                SystemDevice systemDevice = new SystemDevice();
                systemDevice.setNum(deviceNum);
                systemDevice.setDeviceTypeId(deviceTypeId);
                systemDevice.setCreateTime(new Date());
                systemDevice.setLastOnlineTime(new Date(System.currentTimeMillis() - 86400000));
                systemDevice.setIsOnline(0);
                systemDevice.setCreator(userId);
                systemDevice.setName(loopParamVO.getName());
                systemDevice.setDeviceState(0);
                if(singleLampParamReqVO.getLampPostId()!=null){
                    systemDevice.setLampPostId(singleLampParamReqVO.getLampPostId());
                }

                String sendId = "";
                String imei = deviceNum;
                String name = singleLampParamReqVO.getLoopParamVOList().get(0).getName();
                try {
                    Result device = ctWingApi.createDevice(name, imei);
                    if (device.getCode() == 200) {
                        sendId = (String) device.getData();
                        systemDevice.setReserveOne(sendId);
                    } else {
                        return result.error("请正确填写IMEI号");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return result.error("请正确填写IMEI号");
                }
                boolean rsg = this.save(systemDevice);
                if (rsg) {
                    lampDeviceParameterService.addDefaultParamValue(systemDevice.getId(), singleLampParamReqVO.getDeviceTypeId());
                    if (sdpLampPositionId != null) {
                        lampDeviceParameterService.saveParamValue(systemDevice.getId(), sdpLampPositionId, String.valueOf(loopParamVO.getLampPositionId()));
                    }
                    if (sdpLoopNumId != null) {
                        lampDeviceParameterService.saveParamValue(systemDevice.getId(), sdpLoopNumId, String.valueOf(i + 1));
                    }
                    if (sdpBrightnessId != null) {
                        lampDeviceParameterService.saveParamValue(systemDevice.getId(), sdpBrightnessId, "50");
                    }
                } else {
                    errorMap.put(systemDevice.getName(), "添加失败");
                }
            }
        } else {
            result.error("请传入正确参数！");
        }
        if (errorMap.isEmpty()) {
            return result.success("添加成功");
        } else {
            return result.error("添加失败", errorMap);
        }
    }

    @Override
    public Result updateDevice(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        logger.info("修改灯控设备，接收参数：{}", singleLampParamReqVO);
        Result result = new Result();
        if (null != singleLampParamReqVO) {
            String num = singleLampParamReqVO.getNum();
            String deviceNum = "";
            Integer deviceTypeId = singleLampParamReqVO.getDeviceTypeId();
            List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
            if (specialDeviceTypeList.contains(deviceTypeId)) {
                try {
                    deviceNum = Long.toHexString(Long.parseLong(num));
                } catch (Exception e) {
                    return result.error("编号只能为数字");
                }
                deviceNum = deviceNum.toUpperCase();
                String prefix = "";
                for (int i = 0; i < 8 - deviceNum.length(); i++) {
                    prefix += "0";
                }
                deviceNum = prefix + deviceNum;
            } else {
                deviceNum = num;
            }
            SystemDevice systemDeviceTemp = new SystemDevice();

            BeanUtils.copyProperties(singleLampParamReqVO, systemDeviceTemp);
            if(singleLampParamReqVO.getLampPostId()!=null){
                systemDeviceTemp.setSetLonLat(0);
            }
            systemDeviceTemp.setId(singleLampParamReqVO.getId());
            systemDeviceTemp.setNum(deviceNum);
            List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
            if (loopParamVOList != null && loopParamVOList.size() > 0) {
                systemDeviceTemp.setName(loopParamVOList.get(0).getName());
            }
            boolean rsg = this.updateById(systemDeviceTemp);
            if (rsg) {
                result.success("修改成功");
            } else {
                result.error("修改失败！");
            }
        } else {
            result.error("请传入正确参数！");
        }
        return result;
    }

    @Override
    public Result unique(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        logger.info("灯控设备验证唯一性，接收参数：{}", singleLampParamReqVO);
        Result result = new Result();
        if (singleLampParamReqVO != null) {
            if (singleLampParamReqVO.getId() != null) {
                List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
                if (loopParamVOList != null && loopParamVOList.size() > 0) {
                    List<String> collect = loopParamVOList.stream().map(LoopParamVO::getName).distinct().collect(Collectors.toList());
                    if (collect.size() != loopParamVOList.size()) {
                        return result.error("参数中的名称重复");
                    }
                    for (LoopParamVO loopParamVO : loopParamVOList) {
                        String name = loopParamVO.getName();
                        if (name.length() > 20) {
                            return result.error("名称长度超出最大限制（20个字符）");
                        }
                        // 验证名称是否重复
                        Result systemDeviceById = this.getSystemDeviceById(null, name, null, null);
                        SystemDevice systemDevice = (SystemDevice) systemDeviceById.getData();
                        if (systemDevice != null && (!systemDevice.getId().equals(singleLampParamReqVO.getId()))) {
                            return result.error("名称已存在");
                        }
                    }
                }
                //SingleLampParam singleLampById = singleLampParamService.getSingleLampById(singleLampParamReqVO.getId());
                if (singleLampParamReqVO.getNum() != null) {
                    String num = singleLampParamReqVO.getNum();
                    String deviceNum = "";
                    Integer deviceTypeId = singleLampParamReqVO.getDeviceTypeId();
                    List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
                    List<Integer> loraDeviceTypeList = Arrays.asList(3, 4, 9, 10);
                    if (specialDeviceTypeList.contains(deviceTypeId)) {
                        if (num.length() != 8) {
                            return result.error("该类编号长度为8位");
                        }
                        try {
                            deviceNum = Long.toHexString(Long.parseLong(num));
                        } catch (Exception e) {
                            return result.error("编号只能为数字");
                        }
                        deviceNum = deviceNum.toUpperCase();
                        String prefix = "";
                        for (int i = 0; i < 8 - deviceNum.length(); i++) {
                            prefix += "0";
                        }
                        deviceNum = prefix + deviceNum;
                    } else {
                        if (loraDeviceTypeList.contains(deviceTypeId)) {
                            if (num.length() != 16) {
                                return result.error("该类编号长度为16位");
                            }
                        } else if (deviceTypeId == 11) {
                            if (num.length() != 36) {
                                return result.error("该类编号长度为36位");
                            }
                        } else if (deviceTypeId == 7 || deviceTypeId == 8 || deviceTypeId == 14 || deviceTypeId == 15) {
                            if (num.length() != 15) {
                                return result.error("该类编号长度为15位");
                            }
                        }
                        deviceNum = num;
                    }
                    // 验证编号是否重复
                    Result systemDeviceById = getSystemDeviceById(null, null, deviceNum, deviceTypeId);
                    List<SystemDevice> systemDeviceList = (List<SystemDevice>) systemDeviceById.getData();
                    if (systemDeviceList != null && systemDeviceList.size() > 0) {
                        List<Integer> collect = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                        if(!collect.contains(singleLampParamReqVO.getId())){
                            return result.error("编号已存在");
                        }
                        /*for (SystemDevice systemDevice : systemDeviceList) {
                            if (!systemDevice.getId().equals(singleLampParamReqVO.getId())) {

                            }
                        }*/
                    }
                }
            } else {
                List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
                if (loopParamVOList != null && loopParamVOList.size() > 0) {
                    List<String> collect = loopParamVOList.stream().map(LoopParamVO::getName).distinct().collect(Collectors.toList());
                    if (collect.size() != loopParamVOList.size()) {
                        return result.error("参数中的名称重复");
                    }
                    for (LoopParamVO loopParamVO : loopParamVOList) {
                        // 验证名称是否重复
                        Result systemDeviceById = this.getSystemDeviceById(null, loopParamVO.getName(), null, null);
                        SystemDevice systemDevice = (SystemDevice) systemDeviceById.getData();
                        if (systemDevice != null) {
                            return result.error("名称已存在");
                        }
                    }
                }
                if (singleLampParamReqVO.getNum() != null) {
                    String num = singleLampParamReqVO.getNum();
                    String deviceNum = "";
                    Integer deviceTypeId = singleLampParamReqVO.getDeviceTypeId();
                    List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
                    List<Integer> loraDeviceTypeList = Arrays.asList(3, 4, 9, 10);
                    if (specialDeviceTypeList.contains(deviceTypeId)) {
                        if (num.length() != 8) {
                            return result.error("该类编号长度为8位");
                        }
                        try {
                            deviceNum = Long.toHexString(Long.parseLong(num));
                        } catch (Exception e) {
                            return result.error("编号只能为数字");
                        }
                        deviceNum = deviceNum.toUpperCase();
                        String prefix = "";
                        for (int i = 0; i < 8 - deviceNum.length(); i++) {
                            prefix += "0";
                        }
                        deviceNum = prefix + deviceNum;
                    } else {
                        if (loraDeviceTypeList.contains(deviceTypeId)) {
                            if (num.length() != 16) {
                                return result.error("该类编号长度为16位");
                            }
                        } else if (deviceTypeId == 11) {
                            if (num.length() != 36) {
                                return result.error("该类编号长度为36位");
                            }
                        } else if (deviceTypeId == 7 || deviceTypeId == 8) {
                            if (num.length() != 15) {
                                return result.error("该类编号长度为15位");
                            }
                        }
                        deviceNum = num;
                    }
                    // 验证编号是否重复
                    Result systemDeviceById = getSystemDeviceById(null, null, deviceNum, deviceTypeId);
                    List<SystemDevice> systemDeviceList = (List<SystemDevice>) systemDeviceById.getData();
                    if (systemDeviceList != null && systemDeviceList.size() > 0) {
                        return result.error("编号已存在");
                    }
                }
            }
        }
        return result.success("");
    }

    @Override
    public Result uniqueNh(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        logger.info("灯控设备验证唯一性，接收参数：{}", singleLampParamReqVO);
        String num = singleLampParamReqVO.getNum();
        List<LoopParamVO> loopParamVOList = new ArrayList<>();
        LoopParamVO loopParamVO = new LoopParamVO();
        loopParamVO.setName("扫码" + num);
        loopParamVO.setLampPositionId(1);
        loopParamVOList.add(loopParamVO);
        singleLampParamReqVO.setLoopParamVOList(loopParamVOList);
        singleLampParamReqVO.setDeviceTypeId(7);
        Result unique = unique(singleLampParamReqVO, request);
        if (unique.getCode() == 400) {
            return unique;
        } else {
            return addNh(singleLampParamReqVO, request);
        }
    }

    @Override
    public Result detail(Integer id, HttpServletRequest request) {
        logger.info("灯控设备详情，接收参数：{}", id);
        List<SlRespSystemDeviceParameterVO> detail = this.baseMapper.detail(id);
        /*List<LampPosition> lampPositionList = lampPositionService.list();
        Map<Integer, String> lampPositionMap = lampPositionList.stream().collect(Collectors.toMap(LampPosition::getId, LampPosition::getPosition));
        for (SlRespSystemDeviceParameterVO slRespSystemDeviceParameterVO : detail) {
            String name = slRespSystemDeviceParameterVO.getName();
            if ("灯具位置".equals(name)) {
                String parameterValue = slRespSystemDeviceParameterVO.getParameterValue();
                if (parameterValue != null && parameterValue.length() > 0) {
                    Integer lampPositionId = Integer.parseInt(parameterValue);
                    String position = lampPositionMap.get(lampPositionId);
                    if (position != null) {
                        slRespSystemDeviceParameterVO.setParameterValue(position);
                    }
                }
            }
        }*/
        return new Result().success(detail);
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        logger.info("灯控设备删除,接收参数:{}", id);
        SystemDevice systemDevice = baseMapper.selectById(id);
        if (systemDevice == null) {
            return new Result().error("不存在该设备");
        }
        List<Integer> multiLoopTypeList = Arrays.asList(2, 4, 6, 8, 10, 12, 15);
        List<Integer> deviceIdList = new ArrayList<>();
        if (multiLoopTypeList.contains(systemDevice.getDeviceTypeId())) {
            Result systemDeviceById = getSystemDeviceById(null, null, systemDevice.getNum(), systemDevice.getDeviceTypeId());
            List<SystemDevice> systemDeviceList = (List<SystemDevice>) systemDeviceById.getData();
            deviceIdList = systemDeviceList.stream().map(SystemDevice::getId).distinct().collect(Collectors.toList());
        } else {
            deviceIdList.add(id);
        }
        boolean result = true;
        List<SystemDevice> systemDeviceList = baseMapper.selectBatchIds(deviceIdList);
        if (systemDeviceList != null || systemDeviceList.size() > 0) {
            SystemDevice device = systemDeviceList.get(0);
            if (!result) {
            }
            Integer deviceTypeId = device.getDeviceTypeId();
            if (deviceTypeId == 3 || deviceTypeId == 4) {
                List<String> devEuiList = new ArrayList<>();
                String devEui = device.getReserveOne();
                devEuiList.add(devEui);
                result = MessageOperationUtil.deleteNode(devEuiList);
                if (result) {
                    MessageOperationUtil.deleteNodeMc(devEui, loraApi.getLoraMcId());
                }
            } else if (deviceTypeId == 9 || deviceTypeId == 10) {
                List<String> nodeIdList = new ArrayList<>();
                String nodeId = device.getNum();
                nodeIdList.add(nodeId);
                result = MessageOperationUtil.deleteNewLoraNode(nodeIdList);
            } else if (deviceTypeId == 7 || deviceTypeId == 8) {
                String nodeId = device.getReserveOne();
                try {
                    result = ctWingApi.deleteDevice(nodeId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (deviceTypeId == 13) {
                List<Integer> deviceIds = new ArrayList<>();
                deviceIds.add(device.getId());
                List<SlReqInstallLampZkzlVO> slReqInstallLampZkzlVOList = controlLoopDeviceService.getByDeviceIdList(deviceIds);
                if (slReqInstallLampZkzlVOList != null && slReqInstallLampZkzlVOList.size() > 0) {
                    SlReqInstallLampZkzlVO slReqInstallLampZkzlVO = slReqInstallLampZkzlVOList.get(0);
                    String concentratorId = slReqInstallLampZkzlVO.getConcentratorId();
                    Integer groupNo = slReqInstallLampZkzlVO.getGroupNo();
                    List<Integer> lampNoList = new ArrayList<>();
                    String reserveOne = device.getReserveOne();
                    if (reserveOne == null || reserveOne == "0" || reserveOne.length() == 0) {
                        result = true;
                    } else {
                        lampNoList.add(Integer.parseInt(reserveOne));
                        List<String> lampAdressList = new ArrayList<>();
                        lampAdressList.add(device.getNum());
                        result = ZkzlProtocolUtil.installLamp(concentratorId, 1, lampNoList, 0, lampAdressList, groupNo);
                    }
                }
            } else if (deviceTypeId == 14 || deviceTypeId == 15) {
                String nodeId = device.getReserveOne();
                try {
                    result = CtWingMqttUtil.deleteDevice(nodeId);
                } catch (Exception e) {
                    result = false;
                    logger.error("ctwing-mqtt删除设备失败,errMsg={}", e.getMessage());
                }
            } else {
                result = true;
            }
            if (result) {
                this.removeByIds(deviceIdList);
                QueryWrapper<DeviceStrategyHistory> dshQueryWrapper = new QueryWrapper<>();
                dshQueryWrapper.in("device_id",deviceIdList);
                deviceStrategyHistoryService.remove(dshQueryWrapper);
                QueryWrapper<ControlLoopDevice> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("device_id", deviceIdList);
                controlLoopDeviceService.remove(queryWrapper);
                //删除灯具私有属性信息
                lampDeviceParameterService.deleteByDeviceIds(deviceIdList);
            }
        } else {
            result = true;
        }
        if (result) {
            return new Result().success("删除成功");
        } else {
            return new Result().error("删除失败");
        }
    }

    @Override
    public Result delete(List<Integer> ids, HttpServletRequest request) {
        logger.info("灯控设备批量删除,接收参数:{}", ids);
        for (Integer id : ids) {
            Result result = delete(id, request);
            if (result.getCode() == 400) {
                return new Result().success("删除失败");
            }
        }
        return new Result().success("删除成功");
    }

    @Override
    public List<Integer> areaLampPostIdList(Integer areaId) {
        return baseMapper.areaLampPostIdList(areaId);
    }

    @Override
    public boolean updateDeviceOnlineStatus(Integer deviceId, Integer onlineStatus) {
        if (deviceId == null || onlineStatus == null) {
            return false;
        }
        LambdaUpdateWrapper<SystemDevice> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SystemDevice::getIsOnline, onlineStatus);
        wrapper.eq(SystemDevice::getId, deviceId);
        int update = baseMapper.update(null, wrapper);
        return update > 0;
    }

    @Override
    public Result getSystemDeviceById(Integer deviceId, String name, String num, Integer deviceTypeId) {
        QueryWrapper<SystemDevice> queryWrapper = new QueryWrapper<>();
        Result result = new Result();
        if (deviceId != null) {
            queryWrapper.eq("id", deviceId);
        }
        if (name != null) {
            queryWrapper.eq("name", name);
        }
        if (num != null && deviceTypeId != null) {
            queryWrapper.eq("num", num);
            queryWrapper.eq("device_type_id", deviceTypeId);
            List<SystemDevice> systemDeviceList = baseMapper.selectList(queryWrapper);
            if (systemDeviceList == null || systemDeviceList.size() == 0) {
                return result.success(null);
            }
            return result.success(systemDeviceList);
        } else {
            SystemDevice systemDevice = baseMapper.selectOne(queryWrapper);
            return result.success(systemDevice);
        }
    }

    @Override
    public List<SystemDevice> getByFlag(String flag,Integer deviceTypeFlag) {
        QueryWrapper<SystemDevice> queryWrapper = new QueryWrapper<>();
        //nb--1  lora_old--2  cat1--3  dxnb--4  lora_new--5  dxCat1--6
        if(deviceTypeFlag!=null){
            if(deviceTypeFlag==5||deviceTypeFlag==6){
                queryWrapper.eq("reserve_one",flag);
            }else {
                queryWrapper.eq("num",flag);
            }
        }
        List<SystemDevice> systemDeviceList = baseMapper.selectList(queryWrapper);
        return systemDeviceList;
    }

    @Override
    public Integer selectCountByNum(String num, List<Integer> deviceTypeIdList) {
        QueryWrapper<SystemDevice> queryWrapper = new QueryWrapper<>();
        if (num != null) {
            queryWrapper.eq("num", num);
        }
        if (deviceTypeIdList != null && deviceTypeIdList.size() > 0) {
            queryWrapper.in("device_type_id", deviceTypeIdList);
        }
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public Result getDeviceListByIdList(List<Integer> deviceIdList, HttpServletRequest request) {
        logger.info("getDeviceListByIdList - 根据设备id集合查询对象列表 deviceIdList=[{}]", deviceIdList);
        if (deviceIdList == null || deviceIdList.size() == 0) {
            return new Result().error("请传递正确参数");
        }
        List<SystemDevice> systemDeviceList = baseMapper.selectBatchIds(deviceIdList);
        Result<List<SystemDevice>> result = new Result<>();
        return result.success(systemDeviceList);
    }

    @Override
    public Result singleControl(List<SlControlSystemDeviceVO> slControlSystemDeviceVOList) {
        logger.info("单灯控制接收参数:{},{}", slControlSystemDeviceVOList);
        Result realResult = new Result();
        Integer deviceTypeId = slControlSystemDeviceVOList.get(0).getDeviceTypeId();
        if (deviceTypeId == 11) {
            for (SlControlSystemDeviceVO slControlSystemDeviceVO : slControlSystemDeviceVOList) {
                Result result = singleLampParamService.controlByShuncom(slControlSystemDeviceVO);
                if (result.getCode() != 200) {
                    return realResult.error("消息发送失败");
                }
            }
        } else if (deviceTypeId == 13) {
            for (SlControlSystemDeviceVO slControlSystemDeviceVO : slControlSystemDeviceVOList) {
                Integer brightness = slControlSystemDeviceVO.getBrightness();
                if (slControlSystemDeviceVO.getDeviceState() == 0) {
                    brightness = 0;
                }
                String reserveOne = slControlSystemDeviceVO.getReserveOne();
                if (reserveOne == null || reserveOne == "0" || reserveOne.length() == 0) {
                    return realResult.error("下发失败", slControlSystemDeviceVOList);
                }
                int lampNo = Integer.parseInt(reserveOne);
                ZkzlProtocolUtil.oneLampControl(slControlSystemDeviceVO.getLocationControlNum(), lampNo, brightness);
            }
        } else {
            realResult = singleLampParamService.singleLampControl(slControlSystemDeviceVOList);
        }

        return realResult.success(slControlSystemDeviceVOList);
    }

    @Override
    public Result controlByGroup(HttpServletRequest request, SlReqLightControlVO vo) {
        logger.info("集中控制器按组控制:{}", vo);
        List<Integer> controlLoopIdList = vo.getControlLoopIdList();
        if (controlLoopIdList == null || controlLoopIdList.size() == 0) {
            return new Result().error("参数传递错误");
        }
        QueryWrapper<ControlLoopDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("loop_id", controlLoopIdList);
        List<ControlLoopDevice> controlLoopDeviceList = controlLoopDeviceService.list(queryWrapper);
        if (controlLoopDeviceList == null || controlLoopDeviceList.size() == 0) {
            return new Result().error("当前分组不存在设备");
        }
        List<Integer> deviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
        List<SlReqInstallLampZkzlVO> slReqInstallLampZkzlVOS = controlLoopDeviceService.getByDeviceIdList(deviceIdList);
        List<String> concentratorIdList = slReqInstallLampZkzlVOS.stream().map(SlReqInstallLampZkzlVO::getConcentratorId).distinct().collect(Collectors.toList());
        Map<String, List<SlReqInstallLampZkzlVO>> concentratorIdMap = slReqInstallLampZkzlVOS.stream().collect(Collectors.groupingBy(SlReqInstallLampZkzlVO::getConcentratorId));
        for (String concentratorId : concentratorIdList) {
            List<SlReqInstallLampZkzlVO> slReqInstallLampZkzlVOList = concentratorIdMap.get(concentratorId);
            Integer groupNum = slReqInstallLampZkzlVOList.size();
            List<Integer> groupNoList = slReqInstallLampZkzlVOList.stream().map(SlReqInstallLampZkzlVO::getGroupNo).collect(Collectors.toList());
            Integer lightness = vo.getLightness();
            if (vo.getType() != null && vo.getType() == 0) {
                lightness = 0;
            }
            List<Integer> lightnessList = new ArrayList<>();
            for (int i = 0; i < slReqInstallLampZkzlVOList.size(); i++) {
                lightnessList.add(lightness);
            }
            boolean result = ZkzlProtocolUtil.lampGroupControl(concentratorId, groupNum, groupNoList, lightnessList);
            if (!result) {
                return new Result().error("下发失败");
            }
        }
        List<SlControlSystemDeviceVO> successLampDeviceList = new ArrayList<>();
        for (Integer deviceId : deviceIdList) {
            SlControlSystemDeviceVO slControlSystemDeviceVO = new SlControlSystemDeviceVO();
            slControlSystemDeviceVO.setId(deviceId);
            slControlSystemDeviceVO.setDeviceTypeId(13);
            slControlSystemDeviceVO.setDeviceState(vo.getType());
            slControlSystemDeviceVO.setBrightness(vo.getLightness());
            successLampDeviceList.add(slControlSystemDeviceVO);
        }
        boolean result = updateByControlParam(successLampDeviceList);
        if (result) {
            return new Result().success("下发成功");
        } else {
            return new Result().error("下发失败");
        }

    }

    @Override
    public List<SlDeviceLocationControlVO> getLocationControlNums(List<Integer> deviceIdList) {
        List<SlDeviceLocationControlVO> slDeviceLocationControlVOList = baseMapper.getLocationControlNum(deviceIdList);
        return slDeviceLocationControlVOList;
    }

    @Override
    public boolean updateByControlParam(List<SlControlSystemDeviceVO> successLampDeviceList) {
        List<SystemDevice> systemDeviceList = new ArrayList<>();
        for (SlControlSystemDeviceVO slControlSystemDeviceVO : successLampDeviceList) {
            SystemDevice systemDevice = new SystemDevice();
            systemDevice.setId(slControlSystemDeviceVO.getId());
            systemDevice.setDeviceState(slControlSystemDeviceVO.getDeviceState());
            systemDeviceList.add(systemDevice);
            SystemDeviceParameter systemDeviceParameter = systemDeviceParameterService.selectByName(slControlSystemDeviceVO.getDeviceTypeId(), "亮度");
            Integer brightness = slControlSystemDeviceVO.getBrightness();
            if(brightness!=null){
                lampDeviceParameterService.saveParamValue(systemDevice.getId(), systemDeviceParameter.getId(), String.valueOf(brightness));
            }
        }
        return this.updateBatchById(systemDeviceList);
    }

    @Override
    public Result registerDevice(HttpServletRequest request, SlReqInstallLampZkzlVO slReqInstallLampZkzlVO) {
        String concentratorId = slReqInstallLampZkzlVO.getConcentratorId();
        Integer installNum = slReqInstallLampZkzlVO.getInstallNum();
        List<Integer> lampNoList = new ArrayList<>();
        Integer addOrDelete = slReqInstallLampZkzlVO.getAddOrDelete();
        Integer groupNo = slReqInstallLampZkzlVO.getGroupNo();

        List<Integer> deviceIdList = slReqInstallLampZkzlVO.getDeviceIdList();
        if (deviceIdList == null || deviceIdList.size() == 0) {
            return new Result().error("不存在该设备");
        }
        List<SystemDevice> systemDeviceList = baseMapper.selectBatchIds(deviceIdList);
        if (installNum != systemDeviceList.size()) {
            return new Result().error("注册数量不匹配");
        }
        List<String> lampAdressList = systemDeviceList.stream().map(SystemDevice::getNum).collect(Collectors.toList());
        if (addOrDelete == 1) {
            List<Integer> allLampNoList = controlLoopDeviceService.getAllLampNoList(concentratorId);
            List<Integer> electedLampNoList = new ArrayList<>();
            for (int i = 55; i < allLampNoList.size() + 56; i++) {
                electedLampNoList.add(i);
            }
            electedLampNoList.removeAll(lampAdressList);
            if (electedLampNoList.size() < 1) {
                return new Result().error("策略数量将超出限制（65535）");
            }
            for (int i = 0; i < deviceIdList.size(); i++) {
                SystemDevice systemDevice = systemDeviceList.get(i);
                systemDevice.setReserveOne(String.valueOf(electedLampNoList.get(i)));
                lampNoList.add(electedLampNoList.get(i));
            }
        } else {
            for (SystemDevice systemDevice : systemDeviceList) {
                String reserveOne = systemDevice.getReserveOne();
                lampNoList.add(Integer.parseInt(reserveOne));
                systemDevice.setReserveOne("0");
            }
        }
        boolean flag = ZkzlProtocolUtil.installLamp(concentratorId, installNum, lampNoList, addOrDelete, lampAdressList, groupNo);
        if (flag) {
            this.updateBatchById(systemDeviceList);
            return new Result().success("注册成功");
        } else {
            return new Result().error("注册失败");
        }
    }

    @Override
    public Result relieveRegisterDevice(HttpServletRequest request, List<SlReqInstallLampZkzlVO> slReqInstallLampZkzlVOList) {
        Map<String, List<Integer>> errorMap = new HashMap<>();
        SlReqInstallLampZkzlVO slReqInstallLampZkzlVOOne = slReqInstallLampZkzlVOList.get(0);
        if (slReqInstallLampZkzlVOOne != null) {
            List<Integer> deviceIdList = slReqInstallLampZkzlVOOne.getDeviceIdList();
            if (deviceIdList != null && deviceIdList.size() > 0) {
                List<SystemDevice> systemDeviceList = baseMapper.selectBatchIds(deviceIdList);
                for (SystemDevice systemDevice : systemDeviceList) {
                    systemDevice.setReserveOne("0");
                }
                this.updateBatchById(systemDeviceList);
            }
        }
        for (SlReqInstallLampZkzlVO slReqInstallLampZkzlVO : slReqInstallLampZkzlVOList) {
            String concentratorId = slReqInstallLampZkzlVO.getConcentratorId();
            List<Integer> groupNoList = slReqInstallLampZkzlVO.getGroupNoList();
            boolean flag = ZkzlProtocolUtil.removeLampByGroup(concentratorId, groupNoList);
            if (!flag) {
                errorMap.put(concentratorId, groupNoList);
            }
        }
        if (errorMap.isEmpty()) {
            return new Result().success("删除成功");
        } else {
            return new Result().error("删除失败", errorMap);
        }

    }

    @Override
    public SlLampPost selectLampPostByDeviceId(Integer deviceId) {
        return baseMapper.selectLampPostByDeviceId(deviceId);
    }

    @Override
    public Result updateDeviceBatch(List<SingleLampParamReqVO> singleLampParamReqVOS, HttpServletRequest request) {
        Result result = new Result().success("");
        for (SingleLampParamReqVO singleLampParamReqVO : singleLampParamReqVOS) {
            if(result.getCode()==400){
                return result;
            }
            result = updateDevice(singleLampParamReqVO, request);
        }
        return result;
    }

    @Override
    public Result uniqueBatch(List<SingleLampParamReqVO> singleLampParamReqVOS, HttpServletRequest request) {
        Result result = new Result().success("");
        for (SingleLampParamReqVO singleLampParamReqVO : singleLampParamReqVOS) {
            if(result.getCode()==400){
                return result;
            }
            result = unique(singleLampParamReqVO, request);
        }
        return result;
    }
}