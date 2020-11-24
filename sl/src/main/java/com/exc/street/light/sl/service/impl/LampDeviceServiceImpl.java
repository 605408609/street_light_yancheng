/**
 * @filename:LampDeviceServiceImpl 2020-03-17
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.LampLoopType;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.SlLampDeviceQuery;
import com.exc.street.light.resource.utils.BaseConstantUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import com.exc.street.light.resource.vo.resp.SlRespControlVO;
import com.exc.street.light.resource.vo.resp.SlRespLampDeviceVO;
import com.exc.street.light.resource.vo.sl.LoopParamVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamReqVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;
import com.exc.street.light.sl.config.parameter.HttpDlmApi;
import com.exc.street.light.sl.config.parameter.LoraApi;
import com.exc.street.light.sl.config.parameter.SlLampParameter;
import com.exc.street.light.sl.mapper.LampDeviceDao;
import com.exc.street.light.sl.mapper.SingleLampParamDao;
import com.exc.street.light.sl.service.*;
import com.exc.street.light.sl.task.SingleLampControlTask;
import com.exc.street.light.sl.utils.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class LampDeviceServiceImpl extends ServiceImpl<LampDeviceDao, LampDevice> implements LampDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(LampDeviceServiceImpl.class);

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
    private LampDeviceDao lampDeviceDao;
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

    @Override
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
    public Result<LampDevice> getByNum(String num,String model,String factory) {
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

/*    @Override
    public Result singleControl(SlReqDeviceControlVO vo,LampDevice lampDevice) {
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
    public Result singleControl(List<SingleLampParam> singleLampParamList, LampDevice lampDevice) {
        logger.info("单灯控制接收参数:{},{}", singleLampParamList,lampDevice);
        Result realResult = new Result();
        String model = lampDevice.getModel();
        String sendId = lampDevice.getSendId();
        String factory = lampDevice.getFactory();
        if("nb".equals(model)&&"EXC2".equals(factory)){
            for (SingleLampParam singleLampParam : singleLampParamList) {
                Result result = new Result();
                //Result result = singleLampParamService.controlByShuncom(singleLampParam);
                if(result.getCode()!=200){
                    return realResult.error("消息发送失败");
                }
            }
        }else {
            Result result = new Result();
            //realResult = singleLampParamService.singleLampControl(singleLampParamList,model,sendId);
        }

        return realResult.success(singleLampParamList);
    }

    @Override
    public Result singleControl(LampDevice lampDevice) {
        logger.info("路灯单灯控制接收参数:{}", lampDevice);
        Result result = new Result();
        /*String num = lampDevice.getNum();
        Integer brightState = lampDevice.getBrightState();
        Integer brightness = lampDevice.getBrightness();
        Integer presetBrightState = lampDevice.getPresetBrightState();
        Integer presetBrightness = lampDevice.getPresetBrightness();
        List<String> messageList = new ArrayList<>();
        if(brightState!=presetBrightState){
            String state = "00";
            if(brightState==0){
                state = "01";
            }
            String message = MessageGeneration.control("00000001","05",state,brightness);
            messageList.add(message);
        }
        if(brightness!=presetBrightness){
            String message = MessageGeneration.control("00000001","04","00",brightness);
            messageList.add(message);
        }

        if(!messageList.isEmpty()){
            for (String message : messageList) {
                IssueResponse issue = SendHttpsUtil.issue(message, num);
                String status = issue.getStatus();
                if("FAILED".equals(status)){
                    result.error("命令下发失败");
                }
                if("TIMEOUT".equals(status)){
                    result.error("命令下发已超时");
                }
            }
        }*/
        return result.success("命令已下发",lampDevice);
    }

    @Override
    public Result control(HttpServletRequest request, SlReqLightControlVO vo) {
        logger.info("路灯实时控制接收参数:{}", vo);
        Map<String,String> headMap = new HashMap<>();
        headMap.put("token",request.getHeader("token"));
        /*String sendMode = vo.getSendMode();
        if(sendMode.isEmpty()){
            return new Result().error("未选择发送模式");
        }*/
        Result respResult = new Result();
        // 站点id集合
        List<Integer> siteIdList = vo.getSiteIdList();
        // 灯杆id集合
        List<Integer> lampPostList = vo.getLampPostIdList();
        // 分组id集合
        List<Integer> groupIdList = vo.getGroupIdList();
        // 灯具id集合
        List<Integer> singleLampParamIdList = vo.getLampDeviceIdList();
        // 获取灯控器集合
        List<LampDevice> lampDeviceVOList = new ArrayList<>();
        // 获取灯具设备集合
        List<SingleLampParam> singleLampParamVOList = new ArrayList<>();
        //优先获取灯具集合
        if (singleLampParamIdList != null && singleLampParamIdList.size() > 0){
            List<SingleLampParam> singleLampParamList = singleLampParamService.getListByIdList(singleLampParamIdList);
            singleLampParamVOList.addAll(singleLampParamList);
            /*List<Integer> lampDeviceIdList = vo.getLampDeviceIdList();
            Result<List<LampDevice>> listByIdList = lampDeviceService.getListByIdList(lampDeviceIdList);
            List<LampDevice> LampDeviceList = listByIdList.getData();
            for (LampDevice lampDevice : LampDeviceList) {
                lampDeviceVOList.add(lampDevice);
            }*/
        }
        // 优先按路灯灯杆id集合,获得灯具设备集合
        else if (lampPostList != null && lampPostList.size() > 0) {
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
                return respResult.error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
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
        if(!lampDeviceVOList.isEmpty()){
            List<Integer> collect = lampDeviceVOList.stream().map(LampDevice::getId).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<SingleLampParam> wrapper = new LambdaQueryWrapper<SingleLampParam>();
            wrapper.in(SingleLampParam::getDeviceId,collect);
            singleLampParamVOList =  singleLampParamService.list(wrapper);
        }
        if (lampDeviceVOList.isEmpty()&&singleLampParamVOList.isEmpty()) {
            return respResult.error("当前目标没有设备");
        }

        /*List<Integer> networkStateList = lampDeviceVOList.stream().map(LampDevice::getNetworkState).collect(Collectors.toList());
        if(networkStateList.contains(0)){
            return respResult.error("部分设备离线，指令下发失败");
        }*/
        // 更新设备当前设置的状态和亮度预设值
        /*for (LampDevice lampDevice : lampDeviceVOList) {
            lampDevice.setPresetBrightness(lampDevice.getBrightness());
            lampDevice.setPresetBrightState(lampDevice.getBrightState());
        }*/

        // 保留下亮度以便持久化
        //Integer lightness = vo.getLightness();
        // 多线程
        Collection<SingleLampControlTask> tasks = new ArrayList<>();
        if(!singleLampParamVOList.isEmpty()){
            Map<Integer, List<SingleLampParam>> singleLampParamMapByDeviceId = singleLampParamVOList.stream()
                    .collect(Collectors.groupingBy(SingleLampParam::getDeviceId));
            List<Integer> deviceIdList = singleLampParamVOList.stream().map(SingleLampParam::getDeviceId).distinct().collect(Collectors.toList());
            for (Integer deviceId : deviceIdList) {
                LampDevice lampDevice = lampDeviceService.getById(deviceId);
                List<SingleLampParam> singleLampParamList = singleLampParamMapByDeviceId.get(deviceId);
                for (int i = 0; i < singleLampParamList.size(); i++){
                    SingleLampParam singleLampParam = singleLampParamList.get(i);
                    singleLampParam.setBrightState(vo.getType());
                    singleLampParam.setBrightness(vo.getLightness());
                }
                SingleLampControlTask task = new SingleLampControlTask(singleLampParamList, this, lampDevice);
                tasks.add(task);
            }

            /*for (SingleLampParam singleLampParam : singleLampParamVOList) {
                LampDevice lampDevice = lampDeviceService.getById(singleLampParam.getDeviceId());
                singleLampParam.setBrightState(vo.getType());
                singleLampParam.setBrightness(vo.getLightness());
                SingleLampControlTask task = new SingleLampControlTask(singleLampParam, this, lampDevice);
                tasks.add(task);
            }*/
        }/*else {
            for (LampDevice lampDevice : lampDeviceVOList) {
                List<SingleLampParam> singleLampByDeviceId = singleLampParamService.getSingleLampByDeviceId(lampDevice.getId(),null);
                for (SingleLampParam singleLampParam : singleLampByDeviceId) {
                    singleLampParam.setBrightState(vo.getType());
                    singleLampParam.setBrightness(vo.getLightness());
                    SingleLampControlTask task = new SingleLampControlTask(singleLampParam, this, lampDevice);
                    tasks.add(task);
                }
            }
        }*/

        /*for (LampDevice lampDevice : lampDeviceVOList) {

            lampDevice.setBrightness(lightness);
            lampDevice.setBrightState(vo.getType());

            SingleLampParam singleLampParam = new SingleLampParam();
            singleLampParam.setDeviceId(lampDevice.getId());
            switch (vo.getType()){
                case 0:
                    singleLampParam.setStateOne("00");
                    break;
                case 1:
                    singleLampParam.setStateOne("01");
                    break;

            }
            singleLampParam.setBrightnessOne(vo.getLightness());
            SingleLampControlTask task = new SingleLampControlTask(singleLampParam, this, lampDevice);
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
        /*SlRespControlVO slRespControlVO = (SlRespControlVO) result.getData();
        if (slRespControlVO.getSuccessLampDeviceList() != null && slRespControlVO.getSuccessLampDeviceList().size() > 0) {
            singleLampParamService.updateBatchById(slRespControlVO.getSuccessLampDeviceList());
        }*/
        return result;

    }

    @Override
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

    }

    @Override
    public Result getResult(Collection tasks, ExecutorService executorService) {
        SlRespControlVO vo = null;
        try {
            List<Future<Result>> futures = executorService.invokeAll(tasks);
            vo = new SlRespControlVO();
            int successNum = 0;
            int defaultNum = 0;
            List<SingleLampParam> successLampDeviceList = new ArrayList<>();
            List<SingleLampParam> defaultLampDeviceList = new ArrayList<>();
            for (Future<Result> future : futures) {
                Result result = future.get();
                List<SingleLampParam> singleLampParamList = (List<SingleLampParam>) result.getData();
                for (SingleLampParam singleLampParam : singleLampParamList) {
                    if (result.getCode() == Const.CODE_SUCCESS) {
                        successNum += 1;
                        // 设置下发成功的灯具的正确状态和亮度
                    /*lampDevice.setBrightState(lampDevice.getPresetBrightState());
                    lampDevice.setBrightness(lampDevice.getPresetBrightness());*/
                        successLampDeviceList.add(singleLampParam);
                    } else {
                        defaultNum += 1;
                        defaultLampDeviceList.add(singleLampParam);
                    }
                }

            }
            vo.setSuccessNum(successNum);
            //vo.setSuccessLampDeviceList(successLampDeviceList);
            vo.setDefaultNum(defaultNum);
            //vo.setDefaultLampDeviceList(defaultLampDeviceList);
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
        if(lampPostIdList==null||lampPostIdList.size()==0){
            lampPostIdList = new ArrayList<>();
            Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
            if(userId!=1){
                Map<String,String> headerMap = new HashMap<>();
                headerMap.put("token",request.getHeader("token"));
                User user = logUserService.get(userId);
                Integer areaId = user.getAreaId();
                List<Integer> areaLampPostIdList = areaLampPostIdList(areaId);
                if(areaLampPostIdList.isEmpty()){
                    lampPostIdList.add(-1);
                }else {
                    lampPostIdList.addAll(areaLampPostIdList);
                }
            }

        }
        List<SingleLampParamRespVO> singleLampParamList = baseMapper.getSingleLampParamList(lampPostIdList);
        for (SingleLampParamRespVO singleLampParamRespVO : singleLampParamList) {
            String model = singleLampParamRespVO.getModel();
            String factory = singleLampParamRespVO.getFactory();
            if(("nb".equals(model)||"cat1".equals(model))&&"EXC1".equals(factory)){
                String numTemp = singleLampParamRespVO.getNum();
                String deviceNum = String.valueOf(Integer.parseInt(numTemp, 16));
                String prefix = "";
                for (int i = 0; i < 8 - deviceNum.length(); i++){
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
    public Result getListByLampPost(List<Integer> lampPostIdList, HttpServletRequest request) {
        LambdaQueryWrapper<LampDevice> wrapper = new LambdaQueryWrapper();
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            wrapper.in(LampDevice::getLampPostId, lampPostIdList);
        }
        List<LampDevice> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result getPage(SlLampDeviceQuery slLampDeviceQuery, HttpServletRequest request) {
        logger.info("灯控设备分页条件查询,接收参数:{}", slLampDeviceQuery);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flg = logUserService.isAdmin(userId);
        if(!flg){
            slLampDeviceQuery.setAreaId(user.getAreaId());
        }
        Result result = new Result();
        if(slLampDeviceQuery.getPageSize() == 0){
            List<SlRespLampDeviceVO> slRespLampDeviceVOList = singleLampParamDao.query(slLampDeviceQuery);
            for (SlRespLampDeviceVO slRespLampDeviceVO : slRespLampDeviceVOList) {
                String model = slRespLampDeviceVO.getModel();
                String factory = slRespLampDeviceVO.getFactory();
                if(("nb".equals(model)||"cat1".equals(model))&&"EXC1".equals(factory)){
                    String numTemp = slRespLampDeviceVO.getNum();
                    String deviceNum = String.valueOf(Integer.parseInt(numTemp, 16));
                    String prefix = "";
                    for (int i = 0; i < 8 - deviceNum.length(); i++){
                        prefix += "0";
                    }
                    deviceNum = prefix + deviceNum;
                    slRespLampDeviceVO.setNum(deviceNum);
                }
            }
            return result.success(slRespLampDeviceVOList);
        }else{
            IPage<SlRespLampDeviceVO> iPage = new Page<SlRespLampDeviceVO>(slLampDeviceQuery.getPageNum(), slLampDeviceQuery.getPageSize());
            List<SlRespLampDeviceVO> slRespLampDeviceVOList = singleLampParamDao.query(iPage, slLampDeviceQuery);
            for (SlRespLampDeviceVO slRespLampDeviceVO : slRespLampDeviceVOList) {
                String model = slRespLampDeviceVO.getModel();
                String factory = slRespLampDeviceVO.getFactory();
                if(("nb".equals(model)||"cat1".equals(model))&&"EXC1".equals(factory)){
                    String numTemp = slRespLampDeviceVO.getNum();
                    String deviceNum = String.valueOf(Integer.parseInt(numTemp, 16));
                    String prefix = "";
                    for (int i = 0; i < 8 - deviceNum.length(); i++){
                        prefix += "0";
                    }
                    deviceNum = prefix + deviceNum;
                    slRespLampDeviceVO.setNum(deviceNum);
                }
            }
            iPage.setRecords(slRespLampDeviceVOList);
            return result.success(iPage);
        }
    }

    @Override
    public Result add(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        logger.info("添加灯控设备，接收参数：{}", singleLampParamReqVO);
        Result result = new Result().error("");
        LampDevice lampDevice = new LampDevice();
        String num = singleLampParamReqVO.getNum();
        String model = singleLampParamReqVO.getModel();

        String deviceNum = "";
        if(("nb".equals(model)||"cat1".equals(model))&&"EXC1".equals(singleLampParamReqVO.getFactory())){
            try {
                deviceNum = Long.toHexString(Long.parseLong(num));
            }catch (Exception e){
                return result.error("对应设备类型编号只能为数字");
            }
            deviceNum = deviceNum.toUpperCase();
            String prefix = "";
            for (int i = 0; i < 8 - deviceNum.length(); i++){
                prefix += "0";
            }
            deviceNum = prefix + deviceNum;
        }else {
            deviceNum = num;
        }

        if (null != singleLampParamReqVO) {
            lampDevice.setNum(deviceNum);
            lampDevice.setLoopTypeId(1);
            lampDevice.setModel(model);
            lampDevice.setFactory(singleLampParamReqVO.getFactory());
            lampDevice.setLampPostId(singleLampParamReqVO.getLampPostId());
            lampDevice.setCreateTime(new Date());
            lampDevice.setLastOnlineTime(new Date(System.currentTimeMillis()-86400000));
            lampDevice.setNetworkState(0);
            lampDevice.setLoopTypeId(singleLampParamReqVO.getLoopTypeId());

            // 验证名称是否重复
            List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
            LampLoopType lampLoopTypeServiceById = lampLoopTypeService.getById(singleLampParamReqVO.getLoopTypeId());
            if(loopParamVOList.size()>lampLoopTypeServiceById.getLoopTotal()){
                return result.error("超出该回路类型允许添加的灯具个数");
            }
            for (LoopParamVO loopParamVO : loopParamVOList) {
                String name = loopParamVO.getName();
                if(name.length()>20){
                    return result.error("名称长度超出最大限制（20个字符）");
                }
                List<SingleLampParam> screenDeviceByName = singleLampParamService.getSingleLampByDeviceId(null,name);
                if (screenDeviceByName != null && screenDeviceByName.size()>0) {
                    return result.error("名称已存在");
                }
            }



            // 验证编号是否重复
            LambdaQueryWrapper<LampDevice> wrapperNum = new LambdaQueryWrapper();
            wrapperNum.eq(LampDevice::getNum, lampDevice.getNum());
            wrapperNum.eq(LampDevice::getModel,lampDevice.getModel());
            LampDevice screenDeviceByNum = this.getOne(wrapperNum);
            if (screenDeviceByNum != null) {
                return result.error("编号已存在");
            }

            if("nb".equals(singleLampParamReqVO.getModel())&&"EXC2".equals(singleLampParamReqVO.getFactory())){

            }else if("nb".equals(model)){
                num = "00" + num;
                lampDevice.setSendId(num);
            }else if("lora_old".equals(model)){
                String devEui = deviceNum;
                lampDevice.setSendId(devEui);
                boolean node = MessageOperationUtil.createNode(loopParamVOList.get(0).getName(), devEui);
                boolean nodeMc = MessageOperationUtil.createNodeMc(devEui, loraApi.getLoraMcId());
                if(!(node&&nodeMc)){
                    return result.error("请正确填写lora编号！");
                }
            }else if("lora_new".equals(model)){
                String devEui = deviceNum;
                lampDevice.setSendId(devEui);
                String newLoraId = MessageOperationUtil.createNewLoraNode(devEui);
                if(newLoraId==null||newLoraId.length()==0){
                    return result.error("请正确填写lora编号！");
                }else {
                    deviceNum = newLoraId;
                    lampDevice.setNum(deviceNum);
                }
            }else if("cat1".equals(model)){
                num = "cat1/00" + num;
                lampDevice.setSendId(num);
            }else if("dxnb".equals(model)){
                String imei = deviceNum;
                String name = singleLampParamReqVO.getLoopParamVOList().get(0).getName();
                try {
                    Result device = ctWingApi.createDevice(name, imei);
                    if(device.getCode()==200){
                        String sendId = (String) device.getData();
                        lampDevice.setSendId(sendId);
                    }else {
                        return result.error("请正确填写IMEI号");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return result.error("请正确填写IMEI号");
                }
            }

            boolean rsg = this.save(lampDevice);
            if (rsg) {
                result.success("添加成功！");
            }else {
                result.error("添加失败！");
            }
        } else {
            result.error("请传入正确参数！");
        }

        if(result.getCode()==200){
            List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
            if(loopParamVOList!=null && loopParamVOList.size()>0){
                for (int i = 0; i < loopParamVOList.size(); i++){
                    LoopParamVO loopParamVO = loopParamVOList.get(i);
                    String name = loopParamVO.getName();
                    Integer lamoPositionId = loopParamVO.getLampPositionId();
                    SingleLampParam singleLampParam = new SingleLampParam();
                    singleLampParam.setDeviceId(lampDevice.getId());
                    /*singleLampParam.setName(name);
                    singleLampParam.setLampPositionId(lamoPositionId);
                    singleLampParam.setLoopNum(i+1);*/
                    singleLampParam.setBrightState(0);
                    singleLampParam.setBrightness(0);
                    singleLampParam.setModuleTemperature(0.0);
                    singleLampParam.setVoltage(0.0);
                    singleLampParam.setElectricCurrent(0.0);
                    singleLampParam.setPower(0.0);
                    singleLampParam.setElectricEnergy(0.0);
                    singleLampParam.setPowerTime(0.0);
                    singleLampParam.setModuleATime(0.0);
                    singleLampParam.setLampATime(0.0);
                    singleLampParam.setLampTime(0.0);
                    singleLampParam.setLampTime(0.0);
                    singleLampParam.setAlarm("0000");
                    singleLampParam.setVoltageAlarm(0);
                    singleLampParam.setElectricCurrentAlarm(0);
                    singleLampParam.setTemperatureAlarm(0);
                    singleLampParam.setFactorySerialNum("0000000000000000");
                    singleLampParam.setCreateTime(new Date());
                    /*singleLampParam.setDeviceNum(deviceNum);
                    singleLampParam.setLoopTypeId(singleLampParamReqVO.getLoopTypeId());*/
                    singleLampParamService.add(singleLampParam);
                }
            }
        }
        return result;
    }

    @Override
    public Result updateDevice(SingleLampParamReqVO singleLampParamReqVO, HttpServletRequest request) {
        logger.info("修改灯控设备，接收参数：{}", singleLampParamReqVO);
        Result result = new Result();
        SingleLampParam singleLampById = singleLampParamService.getSingleLampById(singleLampParamReqVO.getId());
        Integer deviceId = singleLampById.getDeviceId();
        if (null != singleLampParamReqVO) {
            // 验证名称是否重复
            List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
            LampLoopType lampLoopTypeServiceById = lampLoopTypeService.getById(singleLampParamReqVO.getLoopTypeId());
            if(loopParamVOList.size()>lampLoopTypeServiceById.getLoopTotal()){
                return result.error("超出该回路类型允许添加的灯具个数");
            }
            if(loopParamVOList!=null && loopParamVOList.size()>0){
                for (LoopParamVO loopParamVO : loopParamVOList) {
                    List<SingleLampParam> screenDeviceByName = singleLampParamService.getSingleLampByDeviceId(null,loopParamVO.getName());
                    if (screenDeviceByName != null && screenDeviceByName.size()>0) {
                        for (SingleLampParam singleLampParam : screenDeviceByName) {
                            if(!singleLampParam.getId().equals(singleLampParamReqVO.getId())){
                                return result.error("名称已存在");
                            }
                        }
                    }
                }
            }
            String num = singleLampParamReqVO.getNum();
            String deviceNum = "";
            String model = singleLampParamReqVO.getModel();
            String factory = singleLampParamReqVO.getFactory();
            if(("nb".equals(model)||"cat1".equals(model))&&"EXC1".equals(factory)){
                try {
                    deviceNum = Long.toHexString(Long.parseLong(num));
                }catch (Exception e){
                    return result.error("编号只能为数字");
                }
                deviceNum = deviceNum.toUpperCase();
                String prefix = "";
                for (int i = 0; i < 8 - deviceNum.length(); i++){
                    prefix += "0";
                }
                deviceNum = prefix + deviceNum;
            }else {
                deviceNum = num;
            }
            // 验证编号是否重复
            LambdaQueryWrapper<LampDevice> wrapperNum = new LambdaQueryWrapper();
            wrapperNum.eq(LampDevice::getNum, deviceNum);
            wrapperNum.eq(LampDevice::getModel,singleLampParamReqVO.getModel());
            LampDevice screenDeviceByNum = this.getOne(wrapperNum);
            if (screenDeviceByNum != null && !screenDeviceByNum.getId().equals(deviceId)) {
                return result.error("编号已存在");
            }
            LampDevice lampDeviceTemp = new LampDevice();

            BeanUtils.copyProperties(singleLampParamReqVO,lampDeviceTemp);
            lampDeviceTemp.setId(deviceId);
            lampDeviceTemp.setNum(deviceNum);
            boolean rsg = this.updateById(lampDeviceTemp);
            for (int i = 0; i < loopParamVOList.size(); i++){
                LoopParamVO loopParamVO = loopParamVOList.get(i);
                String name = loopParamVO.getName();
                Integer lampPositionId = loopParamVO.getLampPositionId();
                SingleLampParam singleLampParamTemp = new SingleLampParam();
                BeanUtils.copyProperties(singleLampParamReqVO,singleLampParamTemp);
                /*singleLampParamTemp.setDeviceNum(deviceNum);
                singleLampParamTemp.setName(name);
                singleLampParamTemp.setLampPositionId(lampPositionId);*/
                singleLampParamService.updateOne(singleLampParamTemp);
            }
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
        if (null != singleLampParamReqVO) {
            if (singleLampParamReqVO.getId() != null) {
                List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
                if (loopParamVOList!=null && loopParamVOList.size()>0) {
                    for (LoopParamVO loopParamVO : loopParamVOList) {
                        // 验证名称是否重复
                        List<SingleLampParam> screenDeviceByName = singleLampParamService.getSingleLampByDeviceId(null,loopParamVO.getName());
                        for (SingleLampParam singleLampParam : screenDeviceByName) {
                            if(!singleLampParam.getId().equals(singleLampParamReqVO.getId())){
                                return result.error("名称已存在");
                            }
                        }
                    }
                }
                SingleLampParam singleLampById = singleLampParamService.getSingleLampById(singleLampParamReqVO.getId());
                if (singleLampParamReqVO.getNum() != null) {
                    String num = singleLampParamReqVO.getNum();
                    String deviceNum = "";
                    String model = singleLampParamReqVO.getModel();
                    String factory = singleLampParamReqVO.getFactory();
                    if(("nb".equals(model)||"cat1".equals(model))&&"EXC1".equals(factory)){
                        if(num.length()!=8){
                            return result.error("该类编号长度为8位");
                        }
                        try {
                            deviceNum = Long.toHexString(Long.parseLong(num));
                        }catch (Exception e){
                            return result.error("编号只能为数字");
                        }
                        deviceNum = deviceNum.toUpperCase();
                        String prefix = "";
                        for (int i = 0; i < 8 - deviceNum.length(); i++){
                            prefix += "0";
                        }
                        deviceNum = prefix + deviceNum;
                    }else {
                        if(("lora_old".equals(model)||"lora_new".equals(model))&&"EXC1".equals(factory)){
                            if(num.length()!=16){
                                return result.error("该类编号长度为16位");
                            }
                        }else if("nb".equals(model)&&"EXC2".equals(factory)){
                            if(num.length()!=36){
                                return result.error("该类编号长度为36位");
                            }
                        }else if("dxnb".equals(model)&&"EXC1".equals(factory)){
                            if(num.length()!=15){
                                return result.error("该类编号长度为15位");
                            }
                        }
                        deviceNum = num;
                    }
                    // 验证编号是否重复
                    LambdaQueryWrapper<LampDevice> wrapperNum = new LambdaQueryWrapper();
                    wrapperNum.eq(LampDevice::getNum, deviceNum);
                    wrapperNum.eq(LampDevice::getModel,singleLampParamReqVO.getModel());
                    LampDevice screenDeviceByNum = this.getOne(wrapperNum);
                    if (screenDeviceByNum != null && !screenDeviceByNum.getId().equals(singleLampById.getDeviceId())) {
                        return result.error("编号已存在");
                    }
                }
            } else {
                List<LoopParamVO> loopParamVOList = singleLampParamReqVO.getLoopParamVOList();
                if (loopParamVOList!=null && loopParamVOList.size()>0) {
                    // 验证名称是否重复
                    for (LoopParamVO loopParamVO : loopParamVOList) {
                        List<SingleLampParam> screenDeviceByName = singleLampParamService.getSingleLampByDeviceId(null,loopParamVO.getName());
                        if (screenDeviceByName != null && screenDeviceByName.size()>0) {
                            return result.error("名称已存在");
                        }
                    }
                }
                if (singleLampParamReqVO.getNum() != null) {
                    String num = singleLampParamReqVO.getNum();
                    String deviceNum = "";
                    String model = singleLampParamReqVO.getModel();
                    String factory = singleLampParamReqVO.getFactory();
                    if(("nb".equals(model)||"cat1".equals(model))&&"EXC1".equals(factory)){
                        try {
                            deviceNum = Long.toHexString(Long.parseLong(num));
                        }catch (Exception e){
                            return result.error("编号只能为数字");
                        }
                        deviceNum = deviceNum.toUpperCase();
                        String prefix = "";
                        for (int i = 0; i < 8 - deviceNum.length(); i++){
                            prefix += "0";
                        }
                        deviceNum = prefix + deviceNum;
                    }else {
                        if(("lora_old".equals(model)||"lora_new".equals(model))&&"EXC1".equals(factory)){
                            if(num.length()!=16){
                                return result.error("该类编号长度为16位");
                            }
                        }else if("nb".equals(model)&&"EXC2".equals(factory)){
                            if(num.length()!=36){
                                return result.error("该类编号长度为36位");
                            }
                        }else if("dxnb".equals(model)&&"EXC1".equals(factory)){
                            if(num.length()!=15){
                                return result.error("该类编号长度为15位");
                            }
                        }
                        deviceNum = num;
                    }
                    // 验证编号是否重复
                    LambdaQueryWrapper<LampDevice> wrapperNum = new LambdaQueryWrapper();
                    wrapperNum.eq(LampDevice::getNum, deviceNum);
                    wrapperNum.eq(LampDevice::getModel,singleLampParamReqVO.getModel());
                    LampDevice screenDeviceByNum = this.getOne(wrapperNum);
                    if (screenDeviceByNum != null) {
                        return result.error("编号已存在");
                    }
                }
            }
        }
        return result.success("");
    }

    @Override
    public Result detail(Integer id, HttpServletRequest request) {
        logger.info("灯控设备详情，接收参数：{}", id);
        SlRespLampDeviceVO slRespLampDeviceVO = this.baseMapper.detail(id);
//        String model = slRespLampDeviceVO.getModel();
//        String factory = slRespLampDeviceVO.getFactory();
//        if(("nb".equals(model)||"cat1".equals(model))&&"EXC1".equals(factory)){
//            String numTemp = slRespLampDeviceVO.getNum();
//            String deviceNum = String.valueOf(Integer.parseInt(numTemp, 16));
//            String prefix = "";
//            for (int i = 0; i < 8 - deviceNum.length(); i++){
//                prefix += "0";
//            }
//            deviceNum = prefix + deviceNum;
//            slRespLampDeviceVO.setNum(deviceNum);
//        }
//        Result deviceStrategyResult = lampDeviceStrategyService.usedStrategyById(id);
//        if(deviceStrategyResult.getCode()==200&&deviceStrategyResult.getData()!=null){
//            LampDeviceStrategy lampDeviceStrategy = (LampDeviceStrategy)deviceStrategyResult.getData();
//            Result strategyResult = lampStrategyService.get(lampDeviceStrategy.getStrategyId(), request);
//            if(strategyResult.getCode()==200&&strategyResult.getData()!=null){
//                SlRespStrategyVO slRespStrategyVO = (SlRespStrategyVO)strategyResult.getData();
//                slRespLampDeviceVO.setSlRespStrategyVO(slRespStrategyVO);
//            }
//        }
        Result result = new Result();
        return result.success(slRespLampDeviceVO);
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        logger.info("灯控设备删除,接收参数:{}", id);
        SingleLampParam singleLampById = singleLampParamService.getSingleLampById(id);
        Integer deviceId = singleLampById.getDeviceId();
        LampDevice lampDevice = baseMapper.selectById(deviceId);
        String model = lampDevice.getModel();
        String factory = lampDevice.getFactory();
        boolean result = false;
        //删除灯具参数信息
        boolean b = singleLampParamService.removeById(id);
        if(b){
            List<SingleLampParam> singleLampByDeviceId = singleLampParamService.getSingleLampByDeviceId(deviceId, null);
            if(singleLampByDeviceId==null||singleLampByDeviceId.size()==0){
                baseMapper.deleteById(deviceId);
                if("lora_old".equals(model)&&"EXC1".equals(factory)){
                    List<String> devEuiList = new ArrayList<>();
                    String devEui = lampDevice.getSendId();
                    devEuiList.add(devEui);
                    result = MessageOperationUtil.deleteNode(devEuiList);
                    /*if(result){
                        MessageOperationUtil.deleteNodeMc(devEui, loraApi.getLoraMcId());
                    }*/
                }else if("lora_new".equals(model)&&"EXC1".equals(factory)){
                    List<String> nodeIdList = new ArrayList<>();
                    String nodeId = lampDevice.getNum();
                    nodeIdList.add(nodeId);
                    result = MessageOperationUtil.deleteNewLoraNode(nodeIdList);
                }else if("dxnb".equals(model)&&"EXC1".equals(factory)){
                    String nodeId = lampDevice.getSendId();
                    try {
                        result = CTWingApi.deleteDevice(nodeId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    result = true;
                }
            }else {
                result = true;
            }
        }
        if(result){
            return new Result().success("删除成功");
        }else {
            return new Result().error("删除失败");
        }

    }

    @Override
    public Result delete(List<Integer> ids, HttpServletRequest request) {
        logger.info("灯控设备批量删除,接收参数:{}", ids);
        for (Integer id : ids) {
            SingleLampParam singleLampById = singleLampParamService.getSingleLampById(id);
            Integer deviceId = singleLampById.getDeviceId();
            LampDevice lampDevice = baseMapper.selectById(deviceId);
            String model = lampDevice.getModel();
            String factory = lampDevice.getFactory();
            //删除灯具参数信息
            boolean b = singleLampParamService.removeById(id);
            if(b){
                List<SingleLampParam> singleLampByDeviceId = singleLampParamService.getSingleLampByDeviceId(deviceId, null);
                if(singleLampByDeviceId==null||singleLampByDeviceId.size()==0){
                    baseMapper.deleteById(deviceId);
                    if("lora_old".equals(model)&&"EXC1".equals(factory)){
                        List<String> devEuiList = new ArrayList<>();
                        String devEui = lampDevice.getSendId();
                        devEuiList.add(devEui);
                        MessageOperationUtil.deleteNode(devEuiList);
                        //MessageOperationUtil.deleteNodeMc(devEui, loraApi.getLoraMcId());
                    }else if("lora_new".equals(model)&&"EXC1".equals(factory)){
                        List<String> nodeIdList = new ArrayList<>();
                        String nodeId = lampDevice.getSendId();
                        nodeIdList.add(nodeId);
                        MessageOperationUtil.deleteNewLoraNode(nodeIdList);
                    }else if("dxnb".equals(model)&&"EXC1".equals(factory)){
                        String nodeId = lampDevice.getSendId();
                        try {
                            CTWingApi.deleteDevice(nodeId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return new Result().success("删除成功");
    }

    @Override
    public List<Integer> areaLampPostIdList(Integer areaId) {
        return baseMapper.areaLampPostIdList(areaId);
    }
}