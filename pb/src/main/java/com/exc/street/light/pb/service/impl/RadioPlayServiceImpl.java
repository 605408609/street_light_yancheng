/**
 * @filename:RadioPlayServiceImpl 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.pb.mapper.RadioPlayDao;
import com.exc.street.light.pb.mapper.RadioPlayDeviceMapper;
import com.exc.street.light.pb.service.*;
import com.exc.street.light.pb.utils.DateUtil;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.pb.*;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.PbRadioPlayQueryObject;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.pb.PbRespRadioPlayPageVO;
import com.exc.street.light.resource.vo.req.PbReqPlayRemoveBindVO;
import com.exc.street.light.resource.vo.req.PbReqRadioPlayControlVO;
import com.exc.street.light.resource.vo.req.PbReqRadioPlayVO;
import com.exc.street.light.resource.vo.resp.PbRespRadioPlayVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Description:(定时广播服务实现)
 * @version: V1.0
 * @author: LeiJing
 */
@Service
public class RadioPlayServiceImpl extends ServiceImpl<RadioPlayDao, RadioPlay> implements RadioPlayService {
    private static final Logger logger = LoggerFactory.getLogger(RadioPlayServiceImpl.class);

    /**
     * 雷拓IP广播平台身份验证JSESSIONID
     */
    public static String jsessionId = null;

    /**
     * 雷拓IP广播平台HTTP接口IP、端口
     */
    @Value("${pb.http.url}")
    private String httpUrl;

    /**
     * 雷拓IP广播平台登录账号
     */
    @Value("${pb.user.name}")
    private String userName;

    /**
     * 雷拓IP广播平台登录密码
     */
    @Value("${pb.password}")
    private String password;

    /**
     * ip网络广播系统的模式  debug:定时任务只能添加三条
     */
    @Value("${pb.ipcast}")
    private String ipcast;

    @Autowired
    private RadioPlayDao radioPlayDao;

    @Autowired
    private RadioDeviceService radioDeviceService;

    @Autowired
    private RadioPlayDeviceService radioPlayDeviceService;

    @Autowired
    private RadioPlayDeviceMapper radioPlayDeviceMapper;

    @Autowired
    private RadioMaterialService radioMaterialService;

    @Autowired
    private RadioProgramService radioProgramService;

    @Autowired
    private RadioProgramMaterialService radioProgramMaterialService;

    @Autowired
    private LogUserService userService;

    /*@Autowired
    private RedisUtil redisUtil;

    private final String CALL_TERM = "pb_call_term_ids";*/

    /**
     * 项目启动时自动登录并获取雷拓IP广播平台身份验证JSESSIONID
     */
    @PostConstruct
    private void init() {
        login();
    }

    @Override
    public Result login() {
        Result result = new Result();
        String URL = httpUrl + "login";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("User", userName);
        jsonObject.put("Passwd", password);
        String dataStr = null;
        try {
            dataStr = HttpUtil.postThrowException(URL, jsonObject.toString(), null);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error("登录失败，请检查雷拓接口服务是否开启.");
            return result.error("登录失败，请检查接口服务是否开启.");
        }
        JSONObject returnJson = JSONObject.parseObject(dataStr);
        jsessionId = returnJson.getString("JSessionID");

        if ("0".equals(returnJson.getString("Ret"))) {
            logger.info("登录成功，已获取JSessionID = {}", jsessionId);
            return result.success(returnJson);
        } else {
            String errorMessage = getErrorMessage(returnJson);
            logger.info("登录失败, {}", errorMessage);
            return result.error(errorMessage);
        }
    }

    @Override
    public Result get(Integer id, HttpServletRequest request) {
        logger.info("获取定时任务信息，接收参数：id = {}", id);
        Result result = new Result();
        if (id == null) {
            return result.error("id为空");
        }
        RadioPlay radioPlay = this.baseMapper.selectById(id);
        if (radioPlay == null) {
            return result.error("未找到定时任务信息");
        }
        PbRespRadioPlayVO respRadioPlay = new PbRespRadioPlayVO();
        BeanUtils.copyProperties(radioPlay, respRadioPlay);
        //拼接选中的星期
        List<Integer> weekValueList = new ArrayList<>();
        if (StringUtils.isNotBlank(radioPlay.getWeekValueStr())) {
            weekValueList = StringConversionUtil.getIdListFromString(radioPlay.getWeekValueStr());
        }
        respRadioPlay.setWeekValue(weekValueList);
        if (radioPlay.getProgramId() != null) {
            //获取节目对象
            respRadioPlay.setPbRespRadioProgramVO(radioProgramService.getResp(radioPlay.getProgramId()));
        }
        //获取关联设备id
        LambdaQueryWrapper<RadioPlayDevice> deviceLambdaQueryWrapper = new LambdaQueryWrapper<RadioPlayDevice>();
        deviceLambdaQueryWrapper.eq(RadioPlayDevice::getPlayId, id)
                .isNotNull(respRadioPlay.getDeviceType().equals(1), RadioPlayDevice::getDeviceId)
                .isNotNull(respRadioPlay.getDeviceType().equals(2), RadioPlayDevice::getGroupId);
        List<RadioPlayDevice> radioPlayDevices = radioPlayDeviceService.list(deviceLambdaQueryWrapper);
        if (radioPlayDevices != null && !radioPlayDevices.isEmpty()) {
            if (respRadioPlay.getDeviceType().equals(1)) {
                respRadioPlay.setDeviceIdList(radioPlayDevices.stream().map(RadioPlayDevice::getDeviceId).collect(Collectors.toList()));
            } else if (respRadioPlay.getDeviceType().equals(2)) {
                respRadioPlay.setGroupIdList(radioPlayDevices.stream().map(RadioPlayDevice::getGroupId).collect(Collectors.toList()));
            }
        }
        logger.info("获取定时任务信息,返回对象：{}", respRadioPlay);
        return result.success(respRadioPlay);
    }

    @Override
    public Result add(PbReqRadioPlayVO pbReqRadioPlayVO, HttpServletRequest request) {
        logger.info("新建定时任务，接收参数：pbReqRadioPlayVO = {}", pbReqRadioPlayVO);
        Result result = new Result();
        if (pbReqRadioPlayVO.getDeviceType() == null) {
            return result.error("新增失败,参数缺失");
        }
        //debug模式 判断定时任务是否超过三条
        if ("debug".equals(ipcast)) {
            Result taskRes = taskList();
            if (taskRes.getCode() == 200 && taskRes.getData() != null) {
                JSONObject resObj = (JSONObject) JSONObject.toJSON(taskRes.getData());
                JSONArray itemArr = resObj.getJSONArray("Items");
                if (itemArr != null && itemArr.size() >= 3) {
                    return result.error("定时任务数量已超限制");
                }
            }
        }
        RadioProgram radioProgram = radioProgramService.getById(pbReqRadioPlayVO.getProgramId());

        LambdaQueryWrapper<RadioProgramMaterial> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RadioProgramMaterial::getProgramId, pbReqRadioPlayVO.getProgramId());
        List<RadioProgramMaterial> list = radioProgramMaterialService.list(wrapper);
        if (list == null || list.isEmpty()) {
            //无素材 雷拓创建定时任务接口会报错
            return result.error("该节目下无素材，发布失败");
        }
        JSONObject jsonObject = getPlayJson(pbReqRadioPlayVO);


        //调用雷拓IP广播平台接口
        String interfaceName = "TaskCreate";
        JSONObject returnJson = interfaceCall(interfaceName, jsonObject);
        if (returnJson != null && "0".equals(returnJson.getString("Ret"))) {
            RadioPlay radioPlay = new RadioPlay();
            BeanUtils.copyProperties(pbReqRadioPlayVO, radioPlay);
            if (pbReqRadioPlayVO.getExecutionMode() == 4) {
                //获得秒数
                Integer second = jsonObject.getJSONObject("Item").getInteger("Length");
                Date endTime = new Date(radioPlay.getExecutionTime().getTime() + (second * 1000L));
                radioPlay.setExecutionEndTime(endTime);
            } else if (pbReqRadioPlayVO.getExecutionMode() == 2) {
                //周期模式  转换字符串存在库中
                if (pbReqRadioPlayVO.getWeekValue() != null && !pbReqRadioPlayVO.getWeekValue().isEmpty()) {
                    String weekValueStr = StringUtils.join(pbReqRadioPlayVO.getWeekValue(), ",");
                    radioPlay.setWeekValueStr(weekValueStr);
                }
            }
            radioPlay.setPlayStatus(0);
            radioPlay.setSessionId(Integer.valueOf(returnJson.getString("TaskID")));
            radioPlay.setCreateTime(new Date());
            radioPlayDao.insert(radioPlay);
            pbReqRadioPlayVO.setId(radioPlay.getId());
            //更新绑定设备或分组记录
            radioPlayDeviceService.saveBind(pbReqRadioPlayVO);
            return result.success("新建定时任务成功");
        } else {
            String errorMessage = getErrorMessage(returnJson);
            logger.info("新建定时任务失败, {}", errorMessage);

            return result.error("新建定时任务失败");
        }
    }

    /**
     * 获取公共广播节目播放json数据
     *
     * @param pbReqRadioPlayVO
     * @return
     */
    public JSONObject getPlayJson(PbReqRadioPlayVO pbReqRadioPlayVO) {
        RadioProgram radioProgram = radioProgramService.getById(pbReqRadioPlayVO.getProgramId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        JSONObject jsonObject = new JSONObject(true);
        JSONObject itemObject = new JSONObject(true);
        Integer executionMode = pbReqRadioPlayVO.getExecutionMode();
        //定时任务id为-1时为新增
        if (pbReqRadioPlayVO.getId() != null) {
            itemObject.put("ID", pbReqRadioPlayVO.getSessionId());
        } else {
            itemObject.put("ID", -1);
        }
        //节目名称
        itemObject.put("Name", radioProgram.getName());
        //节目播放模式
        itemObject.put("Type", executionMode);
        //获取开始和结束时间
        long start = pbReqRadioPlayVO.getExecutionTime() != null ? pbReqRadioPlayVO.getExecutionTime().getTime() : 0L;
        long end = pbReqRadioPlayVO.getExecutionEndTime() != null ? pbReqRadioPlayVO.getExecutionEndTime().getTime() : 0L;
        if (executionMode == 1) {
            //每日任务
            JSONObject dayJson = new JSONObject();
            //每一天执行
            dayJson.put("Every", 1);
            itemObject.put("DayItem", dayJson);
            //获取节目时长
            Integer length = Math.abs((int) ((end - start) / 1000));
            //获取开始时间和结束日期
            Date startDate = pbReqRadioPlayVO.getStartDate();
            Date startTime = pbReqRadioPlayVO.getExecutionTime();
            Date endDate = pbReqRadioPlayVO.getEndDate();
            try {
                itemObject.put("StartTime", sdfDate.format(startDate) + " " + sdfTime.format(startTime));
                itemObject.put("EndDate", sdfDate.format(endDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //播放时长 设置为节目的时长
            itemObject.put("Length", length);
        } else if (executionMode == 2) {
            //每周任务
            JSONObject weekJson = new JSONObject(true);
            List<Integer> weekValue = pbReqRadioPlayVO.getWeekValue();
            weekValue = weekValue == null ? new ArrayList<>() : weekValue;
            //删除非1到7的数字
            weekValue.removeIf(item -> item < 1 || item > 7);
            //每一周执行
            weekJson.put("Every", 1);
            weekJson.put("DaysInWeek", weekValue);
            itemObject.put("WeekItem", weekJson);
            //获取节目时长
            Integer length = Math.abs((int) ((end - start) / 1000));
            //获取开始时间和结束日期
            Date startDate = pbReqRadioPlayVO.getStartDate();
            Date startTime = pbReqRadioPlayVO.getExecutionTime();
            try {
                itemObject.put("StartTime", sdfDate.format(startDate) + " " + sdfTime.format(startTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //播放时长 设置为节目的时长
            itemObject.put("Length", length);
            //} else if (executionMode == 3) {
            //每月任务

        } else if (executionMode == 4) {
            //一次性任务
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, 5);
            Date startDate = pbReqRadioPlayVO.getStartDate();
            Date endDate = pbReqRadioPlayVO.getExecutionTime();
            try {
                itemObject.put("StartTime", sdfDate.format(startDate) + " " + sdfTime.format(endDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //播放时长 设置为节目的时长
            itemObject.put("Length", radioProgram.getDuration());
        }
        //设置音量 0~56
        if (pbReqRadioPlayVO.getPlayVol() != null) {
            itemObject.put("PlayVol", pbReqRadioPlayVO.getPlayVol());
        }
        //播放方式，0-顺序 1-随机
        itemObject.put("PlayMode", 0);
        //冻结状态，0-冻结 1-启用
        itemObject.put("Enable", 1);
        //节目播放设备集合
        List<Integer> tids = new ArrayList<Integer>();
        List<Integer> radioDeviceIds = new ArrayList<>();
        if (pbReqRadioPlayVO.getDeviceType().equals(1)) {
            radioDeviceIds = pbReqRadioPlayVO.getDeviceIds();
        } else if (pbReqRadioPlayVO.getDeviceType().equals(2)) {
            radioDeviceIds = radioDeviceService.getIdListByGroupIdList(pbReqRadioPlayVO.getGroupIds());
        }
        if (!radioDeviceIds.isEmpty()) {
            LambdaQueryWrapper<RadioDevice> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(RadioDevice::getId, radioDeviceIds).select(RadioDevice::getTermId).isNotNull(RadioDevice::getTermId);
            List<RadioDevice> deviceList = radioDeviceService.list(wrapper);
            tids = deviceList.stream().map(RadioDevice::getTermId).collect(Collectors.toList());
        }
        itemObject.put("Tids", tids);
        //节目播放素材集合
        List<Integer> progIds = new ArrayList<>();
        LambdaQueryWrapper<RadioProgramMaterial> queryWrapperProgramMaterial = new LambdaQueryWrapper<RadioProgramMaterial>();
        queryWrapperProgramMaterial.select(RadioProgramMaterial::getMaterialId);
        queryWrapperProgramMaterial.eq(RadioProgramMaterial::getProgramId, pbReqRadioPlayVO.getProgramId());
        List<RadioProgramMaterial> radioProgramMaterialList = radioProgramMaterialService.list(queryWrapperProgramMaterial);
        List<Integer> materialIds = radioProgramMaterialList.stream().filter(e -> e.getMaterialId() != null).map(RadioProgramMaterial::getMaterialId).distinct().collect(Collectors.toList());
        //获取所有fileId
        LambdaQueryWrapper<RadioMaterial> radioMaterialWrapper = new LambdaQueryWrapper<>();
        radioMaterialWrapper.select(RadioMaterial::getFileId).in(RadioMaterial::getId, materialIds);
        List<RadioMaterial> materialList = radioMaterialService.list(radioMaterialWrapper);
        progIds = materialList.stream().filter(e -> e.getFileId() != null).map(RadioMaterial::getFileId).distinct().collect(Collectors.toList());
        itemObject.put("ProgIds", progIds);
        jsonObject.put("Item", itemObject);

        logger.info("公共广播节目播放json = {}", jsonObject);
        return jsonObject;
    }

    @Override
    public Result update(PbReqRadioPlayVO pbReqRadioPlayVO, HttpServletRequest request) {
        logger.info("修改定时任务，接收参数：pbReqRadioPlayVO = {}", pbReqRadioPlayVO);
        Result result = new Result();
        if (pbReqRadioPlayVO.getDeviceType() == null) {
            return result.error("修改失败,参数缺失");
        }
        RadioPlay oldRadioPlay = radioPlayDao.selectById(pbReqRadioPlayVO.getId());
        JSONObject jsonObject = getPlayJson(pbReqRadioPlayVO);
        //指定taskid
        jsonObject.put("TaskID", oldRadioPlay.getSessionId());
        //调用雷拓IP广播平台接口
        String interfaceName = "TaskUpdate";
        JSONObject returnJson = interfaceCall(interfaceName, jsonObject);
        if (returnJson != null && "0".equals(returnJson.getString("Ret"))) {
            RadioPlay radioPlay = new RadioPlay();
            BeanUtils.copyProperties(pbReqRadioPlayVO, radioPlay);
            radioPlay.setSessionId(oldRadioPlay.getSessionId());
            if (pbReqRadioPlayVO.getExecutionMode() == 4) {
                //获得秒数
                Integer second = jsonObject.getJSONObject("Item").getInteger("Length");
                Date endTime = new Date(radioPlay.getExecutionTime().getTime() + (second * 1000L));
                radioPlay.setExecutionEndTime(endTime);
            } else if (pbReqRadioPlayVO.getExecutionMode() == 2) {
                List<Integer> weekValue = pbReqRadioPlayVO.getWeekValue();
                weekValue = weekValue == null ? new ArrayList<>() : weekValue;
                radioPlay.setWeekValueStr(StringUtils.join(weekValue, ","));
            }
            //设置为待播放状态
            radioPlay.setPlayStatus(0);
            radioPlayDao.updateById(radioPlay);

            pbReqRadioPlayVO.setId(radioPlay.getId());
            //更新绑定设备或分组记录
            radioPlayDeviceService.saveBind(pbReqRadioPlayVO);
            return result.success("修改定时任务成功");
        } else {
            String errorMessage = getErrorMessage(returnJson);
            logger.info("修改定时任务失败, {}", errorMessage);
            return result.error("修改定时任务失败");
        }
    }

    @Override
    public Result getPlayList(PbRadioPlayQueryObject qo, HttpServletRequest request) {
        logger.info("获取公共广播节目播放列表，接收参数：qo = {}", qo);
        Result result = new Result();
        //更新任务状态
        //updatePlayStatus();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        IPage<RadioPlay> iPage = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<RadioPlay> radioPlayList = this.baseMapper.query(iPage, qo);
        List<PbRespRadioPlayVO> pbRespRadioPlayVOList = new ArrayList<>();
        List<RadioPlay> radioPlayRecords = iPage.getRecords();
        for (RadioPlay radioPlay : radioPlayRecords) {
            PbRespRadioPlayVO pbRespRadioPlayVO = new PbRespRadioPlayVO();
            BeanUtils.copyProperties(radioPlay, pbRespRadioPlayVO);

            pbRespRadioPlayVO.setRadioProgram(radioProgramService.getById(radioPlay.getProgramId()));
            if (pbRespRadioPlayVO.getExecutionMode() == 2 && StringUtils.isNotBlank(radioPlay.getWeekValueStr())) {
                //周模式
                pbRespRadioPlayVO.setWeekValue(StringConversionUtil.getIdListFromString(radioPlay.getWeekValueStr()));
            }
            pbRespRadioPlayVOList.add(pbRespRadioPlayVO);
        }
        // 构建返回对象
        IPage<PbRespRadioPlayVO> pbRespRadioPlayVOIPage = new Page<PbRespRadioPlayVO>();
        pbRespRadioPlayVOIPage.setRecords(pbRespRadioPlayVOList);
        pbRespRadioPlayVOIPage.setSize(radioPlayList.getSize());
        pbRespRadioPlayVOIPage.setCurrent(radioPlayList.getCurrent());
        pbRespRadioPlayVOIPage.setPages(radioPlayList.getPages());
        pbRespRadioPlayVOIPage.setTotal(radioPlayList.getTotal());
        return result.success(pbRespRadioPlayVOIPage);
    }

    @Override
    public Result getPageList(PbRadioPlayQueryObject qo, HttpServletRequest request) {
        logger.info("获取公共广播节目播放分页列表，接收参数：qo = {}", qo);
        Result result = new Result();
        //更新任务状态
        //updatePlayStatus();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        IPage<PbRespRadioPlayPageVO> iPage = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<PbRespRadioPlayPageVO> radioPlayList = baseMapper.getPageList(iPage, qo);
        return result.success(radioPlayList);
    }

    /**
     * 获取播放状态
     *
     * @param radioPlay
     * @return 播放状态（0：待播放，1：正在播放 2:结束播放）
     */
    public Integer getPlayStatus(PbRespRadioPlayVO radioPlay) {
        if (radioPlay == null) {
            return null;
        }
        SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        Integer playStatus = radioPlay.getPlayStatus();
        //不是正在播放状态，判断该定时任务 待播放 结束播放
        if (playStatus != 1) {
            Date now = new Date();
            Date startDate = DateUtil.getDayBeginOrEnd(0, true, radioPlay.getStartDate());
            Date endDate = DateUtil.getDayBeginOrEnd(0, true, radioPlay.getEndDate());
            Date lastTime = null;
            try {
                lastTime = sdfLong.parse(sdfDate.format(endDate) + " " + sdfTime.format(radioPlay.getExecutionEndTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (radioPlay.getExecutionMode() == 4) {
                //单次任务
                try {
                    lastTime = sdfLong.parse(sdfDate.format(startDate) + " " + sdfTime.format(radioPlay.getExecutionEndTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (now.getTime() > lastTime.getTime()) {
                //超过最后时间
                return 2;
            } else {
                //待播放状态
                return 0;
            }
        }
        return radioPlay.getPlayStatus();
    }

    @Override
    public Result playControl(PbReqRadioPlayControlVO pbReqRadioPlayControlVO, HttpServletRequest request) {
        logger.info("公共广播节目播放控制，接收参数：pbReqRadioPlayControlVO = {}", pbReqRadioPlayControlVO);
        Result result = new Result();
        RadioPlay radioPlay = this.getById(pbReqRadioPlayControlVO.getPlayId());
        RadioProgram radioProgram = radioProgramService.getById(radioPlay.getProgramId());
        String playName = radioProgram.getName();
        Integer playStatus = pbReqRadioPlayControlVO.getPlayStatus();
        String interfaceName = null;
        if (playStatus != null) {
            if (playStatus == 0 || playStatus == 2) {
                //停止定时任务
                interfaceName = "TaskManualStop";
            } else if (playStatus == 1) {
                //启动定时任务
                interfaceName = "TaskManualStart";
            }
        } else {
            logger.info("控制定时任务：{} 失败", playName);
            return result.error("控制定时任务失败");
        }
        JSONObject jsonObject = new JSONObject();
        //雷拓IP广播平台定时任务ID
        jsonObject.put("TaskID", radioPlay.getSessionId());
        //调用雷拓IP广播平台接口
        JSONObject returnJson = interfaceCall(interfaceName, jsonObject);
        if (returnJson != null && "0".equals(returnJson.getString("Ret"))) {
            radioPlay.setPlayStatus(playStatus);
            PbRespRadioPlayVO radioPlayVO = new PbRespRadioPlayVO();
            BeanUtils.copyProperties(radioPlay, radioPlayVO);
            radioPlay.setPlayStatus(getPlayStatus(radioPlayVO));
            this.updateById(radioPlay);

            logger.info("控制定时任务：{} 成功", playName);
            return result.success("控制定时任务成功");
        } else {
            String errorMessage = getErrorMessage(returnJson);
            logger.info("控制定时任务：{} 失败, {}", playName, errorMessage);
            return result.error("控制定时任务失败");
        }
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        logger.info("删除公共广播节目播放，接收参数：id = {}", id);
        Result result = new Result();

        //调用雷拓IP广播平台接口
        String interfaceName = "TaskDelete";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TaskID", id);
        JSONObject returnJson = interfaceCall(interfaceName, jsonObject);
        if (returnJson != null && "0".equals(returnJson.getString("Ret"))) {
            //删除关联的设备绑定信息
            LambdaQueryWrapper<RadioPlayDevice> playDeviceWrapper = new LambdaQueryWrapper<>();
            playDeviceWrapper.eq(RadioPlayDevice::getPlayId, id);
            radioPlayDeviceService.remove(playDeviceWrapper);
            //删除定时任务
            this.baseMapper.deleteById(id);
            return result.success("删除定时任务成功");
        } else {
            String errorMessage = getErrorMessage(returnJson);
            logger.info("删除定时任务: {} 失败, {}", id, errorMessage);

            return result.error("删除定时任务失败");
        }
    }

    @Override
    public Result deletePlay(Integer id) {
        logger.info("删除公共广播节目播放，接收参数：id = {}", id);
        Result result = new Result();
        RadioPlay radioPlay = this.getById(id);
        //雷拓节目播放会话ID
        Integer taskId = radioPlay.getSessionId();
        //调用雷拓IP广播平台接口
        String interfaceName = "TaskDelete";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TaskID", taskId);
        JSONObject returnJson = interfaceCall(interfaceName, jsonObject);
        if (returnJson != null && "0".equals(returnJson.getString("Ret"))) {
            this.baseMapper.deleteById(id);
            return result.success("删除定时任务成功");
        } else {
            String errorMessage = getErrorMessage(returnJson);
            logger.info("删除定时任务: {} 失败, {}", id, errorMessage);

            return result.error("删除定时任务失败");
        }
    }

    @Override
    public Result batchDelete(List<Integer> idList) {
        logger.info("删除公共广播节目播放，接收参数：idList = {}", idList);
        Result result = new Result();
        //删除关联的设备绑定信息
        LambdaQueryWrapper<RadioPlayDevice> playDeviceWrapper = new LambdaQueryWrapper<>();
        playDeviceWrapper.in(RadioPlayDevice::getPlayId, idList);
        radioPlayDeviceService.remove(playDeviceWrapper);
        for (Integer id : idList) {
            deletePlay(id);
        }
        return this.removeByIds(idList) ? result.success("删除成功") : result.error("删除失败");
    }

    @Override
    public Result termVolSet(String ids, Integer volValue, HttpServletRequest request) {
        Result result = new Result();
        if (StringUtils.isBlank(ids)) {
            return result.error("ids为空");
        }
        //雷拓终端音量范围 0~56
        if (volValue == null || volValue < 0 || volValue > 56) {
            return result.error("音量值有误");
        }
        logger.info("公共广播节目音量控制，接收参数：ids = {}", ids);
        //调用雷拓IP广播平台接口
        String interfaceName = "TaskUpdate";
        //获取节目列表
        List<Integer> idList = StringConversionUtil.getIdListFromString(ids);
        LambdaQueryWrapper<RadioPlay> playWrapper = new LambdaQueryWrapper<>();
        playWrapper.in(RadioPlay::getId, idList);
        List<RadioPlay> playList = this.list(playWrapper);
        if (playList != null && !playList.isEmpty()) {
            for (RadioPlay radioPlay : playList) {
                //拼接json
                PbReqRadioPlayVO pbReqRadioPlayVO = new PbReqRadioPlayVO();
                BeanUtils.copyProperties(radioPlay, pbReqRadioPlayVO);
                if (StringUtils.isNotBlank(radioPlay.getWeekValueStr())) {
                    pbReqRadioPlayVO.setWeekValue(StringConversionUtil.getIdListFromString(radioPlay.getWeekValueStr()));
                }
                LambdaQueryWrapper<RadioPlayDevice> playDeviceWrapper = new LambdaQueryWrapper<>();
                playDeviceWrapper.eq(RadioPlayDevice::getPlayId, radioPlay.getId());
                List<RadioPlayDevice> deviceList = radioPlayDeviceService.list(playDeviceWrapper);
                if (deviceList != null && !deviceList.isEmpty()) {
                    List<Integer> deviceIds = deviceList.stream().map(RadioPlayDevice::getDeviceId).collect(Collectors.toList());
                    pbReqRadioPlayVO.setDeviceIds(deviceIds);
                }
                pbReqRadioPlayVO.setPlayVol(volValue);
                JSONObject playJson = getPlayJson(pbReqRadioPlayVO);
                JSONObject returnJson = interfaceCall(interfaceName, playJson);
                if (returnJson != null && "0".equals(returnJson.getString("Ret"))) {
                    //保存音量值
                    logger.info("更新定时任务音量成功,id:{}", radioPlay.getId());
                    radioPlay.setPlayVol(volValue);
                    this.updateById(radioPlay);
                } else {
                    logger.info("更新定时任务音量失败,id:{}", radioPlay.getId());
                    return result.error("修改音量失败");
                }
            }
        }
        return result.success("");
    }

    @Override
    public Result taskList() {
        Result result = new Result();
        //获取列表
        String interfaceName = "TaskList";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TaskIDs", new int[]{});
        JSONObject resultObj = interfaceCall(interfaceName, jsonObject);
        return result.success(resultObj);
    }

    /**
     * 雷拓IP广播平台获取错误信息
     *
     * @param returnJson
     * @return
     */
    @Override
    public String getErrorMessage(JSONObject returnJson) {
        String errorMessage = null;
        if (returnJson == null) {
            return "接口服务访问失败，请查看是否开启";
        }
        switch (returnJson.getString("Ret")) {
            case "8000":
                errorMessage = "接口未实现或者无效的接口名";
                break;
            case "8001":
                errorMessage = "JSESSIONID 无效";
                break;
            case "10000":
                errorMessage = "参数错误，缺少必须的参数";
                break;
            case "10001":
                errorMessage = "用户名、密码不正确";
                break;
            case "10002":
                errorMessage = "发起呼叫终端不在线";
                break;
            case "10003":
                errorMessage = "无效的终端 ID";
                break;
            case "10004":
                errorMessage = "发起呼叫终端正忙";
                break;
            case "10005":
                errorMessage = "无效的终端通话编码";
                break;
            case "10006":
                errorMessage = "串口无效";
                break;
            case "10007":
                errorMessage = "串口操作超时";
                break;
            case "10008":
                errorMessage = "串口数据错误";
                break;
            case "-1":
                errorMessage = "请求错误或雷拓服务访问失败";
                break;
            default:
                errorMessage = "未知错误";
                break;
        }

        return errorMessage;
    }

    @Override
    public void updatePlayStatusByList(List<RadioPlay> radioPlayList) {
        //调用雷拓ip广播 查询定时任务方法
        String interfaceName = "TaskList";
        JSONObject paramObj = new JSONObject(true);
        paramObj.put("TaskIDs", null);
        JSONObject returnObj = interfaceCall(interfaceName, paramObj);
        if (returnObj != null && "0".equals(returnObj.getString("Ret"))) {
            List<Integer> sessionIdList = new ArrayList<>();
            Map<Integer, Integer> sessionStatusMap = new HashMap<>();
            JSONArray items = returnObj.getJSONArray("Items");
            if (items == null || items.isEmpty()) {
                return;
            }
            for (int i = 0; i < items.size(); i++) {
                JSONObject itemObj = items.getJSONObject(i);
                Integer status = itemObj.getInteger("Status");
                Integer sessionId = itemObj.getInteger("ID");
                if (status == null || sessionId == null) {
                    continue;
                }
                sessionIdList.add(sessionId);
                sessionStatusMap.put(sessionId, status);
            }
            if (!sessionIdList.isEmpty()) {
                //查询对应定时任务
                LambdaQueryWrapper<RadioPlay> radioPlayWrapper = new LambdaQueryWrapper<>();
                radioPlayWrapper.in(RadioPlay::getSessionId, sessionIdList);
                List<RadioPlay> playList = this.list(radioPlayWrapper);
                if (playList == null || playList.isEmpty()) {
                    return;
                }
                for (RadioPlay radioPlay : playList) {
                    Integer oldStatus = radioPlay.getPlayStatus();
                    //保存定时任务的播放状态
                    Integer status = sessionStatusMap.get(radioPlay.getSessionId());
                    radioPlay.setPlayStatus(status);
                    PbRespRadioPlayVO respRadioPlayVO = new PbRespRadioPlayVO();
                    BeanUtils.copyProperties(radioPlay, respRadioPlayVO);
                    status = getPlayStatus(respRadioPlayVO);
                    if (status.equals(oldStatus)) {
                        //和数据库状态一致，不保存
                        continue;
                    }
                    radioPlay.setPlayStatus(status);
                    this.updateById(radioPlay);
                }
            }
        }
    }

    @Override
    public void updatePlayStatus() {
        logger.info("开始更新定时任务状态");
        updatePlayStatusByList(null);
        logger.info("结束更新定时任务状态");
    }

    @Override
    public Result deviceSpeak(boolean playStatus, Integer fromDeviceId, String targetDeviceIds) {
        Result result = new Result();
        if (fromDeviceId == null) {
            return result.error("发起方设备id为空");
        }
        RadioDevice fromDevice = radioDeviceService.getById(fromDeviceId);
        if (fromDevice == null || fromDevice.getTermId() == null) {
            return result.error("发起方设备不存在或雷拓终端编号为空");
        }
        //方法名-终端呼叫控制
        String interfaceName = "TermSpeak";
        JSONObject paramObj = new JSONObject(true);
        paramObj.put("Action", playStatus ? "start" : "stop");
        boolean isAddRedis = true;
        if (playStatus) {
            //开始控制
            logger.info("开始雷拓终端呼叫控制，接受参数：fromDeviceId={}，targetDeviceIds={}", fromDeviceId, targetDeviceIds);
            List<Integer> termIds = new ArrayList<>();
            //获取被控制方的雷拓终端id
            if (StringUtils.isNotBlank(targetDeviceIds)) {
                LambdaQueryWrapper<RadioDevice> deviceWrapper = new LambdaQueryWrapper<>();
                deviceWrapper.in(RadioDevice::getId, targetDeviceIds.split(",")).isNotNull(RadioDevice::getTermId);
                List<RadioDevice> deviceList = radioDeviceService.list(deviceWrapper);
                if (deviceList != null && !deviceList.isEmpty()) {
                    termIds = deviceList.stream().map(RadioDevice::getTermId).collect(Collectors.toList());
                }
            }
            paramObj.put("FromTermId", fromDevice.getTermId());
            paramObj.put("TargetTermIds", termIds);
        } else {
            //结束控制
            logger.info("结束雷拓终端呼叫控制，接受参数：fromDeviceId={}", fromDeviceId);
            paramObj.put("FromTermId", fromDevice.getTermId());
            isAddRedis = false;
        }
        JSONObject resObj = interfaceCall(interfaceName, paramObj);
        if (resObj != null && "0".equals(resObj.getString("Ret"))) {
            //写入redis
            modifyCallTerm(fromDevice.getTermId(), isAddRedis);
            logger.info("{}控制成功，广播设备id：{}，被控制方id集合：{}", (playStatus ? "开始" : "结束"), fromDeviceId, targetDeviceIds);
            return result.error("控制成功");
        } else {
            logger.info("{}控制失败，广播设备id：{}，被控制方id集合：{}", (playStatus ? "开始" : "结束"), fromDeviceId, targetDeviceIds);
            return result.error("控制失败");
        }
    }

    /**
     * 向redis中添加正在播放的终端id
     *
     * @param termId 终端id
     * @param isAdd  true:添加  false:删除
     */
    private void modifyCallTerm(int termId, boolean isAdd) {
        /*Object termIdObj = redisUtil.get(CALL_TERM);
        List<Integer> termIdList = new ArrayList<>();
        if (termIdObj != null && !"".equals(termIdObj)) {
            String[] split = termIdObj.toString().split(",");
            termIdList = Stream.of(split).map(Integer::parseInt).collect(Collectors.toList());
        }
        if (isAdd && !termIdList.contains(termId)) {
            termIdList.add(termId);
        } else {
            termIdList.removeIf(e -> e == termId);
        }
        redisUtil.set(CALL_TERM, StringUtils.join(termIdList, ","));

         */
    }

    @Override
    public JSONObject getTermState(List<Integer> termIds) {
        if (termIds == null || termIds.isEmpty()) {
            return null;
        }
        String interfaceName = "getTermState";
        JSONObject obj = new JSONObject();
        obj.put("TermIDs", termIds);
        return interfaceCall(interfaceName, obj);
    }

    @Override
    public Result<String> removeBind(List<PbReqPlayRemoveBindVO> req, HttpServletRequest request) {
        Result<String> result = new Result<>();
        if (req == null || req.isEmpty()) {
            return result.error("删除失败,参数缺失");
        }
        Map<Integer, List<PbReqPlayRemoveBindVO>> groupList = req.stream().filter(e -> e.getPlayId() != null && e.getDeviceId() != null)
                .collect(Collectors.groupingBy(PbReqPlayRemoveBindVO::getPlayId));
        for (Map.Entry<Integer, List<PbReqPlayRemoveBindVO>> entry : groupList.entrySet()) {
            List<Integer> deviceIdList = entry.getValue().stream().map(PbReqPlayRemoveBindVO::getDeviceId).collect(Collectors.toList());
            LambdaQueryWrapper<RadioPlayDevice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RadioPlayDevice::getPlayId, entry.getKey()).in(RadioPlayDevice::getDeviceId, deviceIdList);
            //删除设备与节目的关联
            boolean remove = radioPlayDeviceService.remove(wrapper);
        }
        //获取需要更新的节目id
        List<Integer> playIdList = new ArrayList<>(groupList.keySet());
        for (Integer playId : playIdList) {
            RadioPlay radioPlay = this.getById(playId);
            if (radioPlay == null) {
                continue;
            }
            LambdaQueryWrapper<RadioPlayDevice> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.select(RadioPlayDevice::getDeviceId);
            wrapper2.eq(RadioPlayDevice::getPlayId, radioPlay.getId());
            List<RadioPlayDevice> list = radioPlayDeviceService.list(wrapper2);
            if (list == null || list.isEmpty()) {
                //该策略下无绑定广播设备，则删除策略
                deletePlay(radioPlay.getId());
                continue;
            }
            //获取设备id集合
            List<Integer> deviceIdList = list.stream().map(RadioPlayDevice::getDeviceId).distinct().collect(Collectors.toList());
            PbReqRadioPlayVO playVo = new PbReqRadioPlayVO();
            BeanUtils.copyProperties(radioPlay, playVo);
            playVo.setDeviceIds(deviceIdList);

            //拼接选中的星期
            List<Integer> weekValueList = new ArrayList<>();
            if (StringUtils.isNotBlank(radioPlay.getWeekValueStr())) {
                weekValueList = StringConversionUtil.getIdListFromString(radioPlay.getWeekValueStr());
            }
            playVo.setWeekValue(weekValueList);
            JSONObject jsonObject = getPlayJson(playVo);
            //指定taskid
            jsonObject.put("TaskID", radioPlay.getSessionId());
            //调用雷拓IP广播平台接口
            String interfaceName = "TaskUpdate";
            JSONObject returnJson = interfaceCall(interfaceName, jsonObject);
            if (returnJson == null || !"0".equals(returnJson.getString("Ret"))) {
                return result.success("删除失败", radioPlay.getId().toString());
            }
        }
        return result.success("删除成功", "");
    }

    @Override
    public Result<String> refreshPlayBind(List<Integer> lampPostIds, HttpServletRequest request) {
        Result<String> result = new Result<>();
        if (lampPostIds == null || lampPostIds.isEmpty()) {
            return result.error("");
        }
        List<Integer> playIdList = baseMapper.getPlayIdListByLampPostId(lampPostIds);
        if (playIdList == null || playIdList.isEmpty()) {
            return result.success("", "");
        }
        Collection<RadioPlay> radioPlays = this.listByIds(playIdList);
        for (RadioPlay radioPlay : radioPlays) {
            PbReqRadioPlayVO reqVo = new PbReqRadioPlayVO();
            BeanUtils.copyProperties(radioPlay, reqVo);
            //拼接选中的星期
            if (StringUtils.isNotBlank(radioPlay.getWeekValueStr())) {
                reqVo.setWeekValue(StringConversionUtil.getIdListFromString(radioPlay.getWeekValueStr()));
            }
            //获取关联设备id
            LambdaQueryWrapper<RadioPlayDevice> deviceLambdaQueryWrapper = new LambdaQueryWrapper<RadioPlayDevice>();
            deviceLambdaQueryWrapper.eq(RadioPlayDevice::getPlayId, radioPlay.getId())
                    .isNotNull(reqVo.getDeviceType().equals(1), RadioPlayDevice::getDeviceId)
                    .isNotNull(reqVo.getDeviceType().equals(2), RadioPlayDevice::getGroupId);
            List<RadioPlayDevice> radioPlayDevices = radioPlayDeviceService.list(deviceLambdaQueryWrapper);
            if (radioPlayDevices != null && !radioPlayDevices.isEmpty()) {
                if (reqVo.getDeviceType().equals(1)) {
                    reqVo.setDeviceIds(radioPlayDevices.stream().map(RadioPlayDevice::getDeviceId).collect(Collectors.toList()));
                } else if (reqVo.getDeviceType().equals(2)) {
                    reqVo.setGroupIds(radioPlayDevices.stream().map(RadioPlayDevice::getGroupId).collect(Collectors.toList()));
                }
            }
            this.update(reqVo, request);
        }
        return result.success("", "");
    }


    /**
     * 雷拓IP广播平台接口调用
     *
     * @param interfaceName 雷拓IP广播平台接口名称
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject interfaceCall(String interfaceName, JSONObject jsonObject) {
        long beginTime = System.currentTimeMillis();
        String url = httpUrl + interfaceName + ";JSESSIONID=" + jsessionId;
        logger.info("雷拓IP广播平台接口调用：URL = {}, json = {}", url, jsonObject.toString());
        String dataStr = null;
        try {
            dataStr = HttpUtil.postThrowException(url, jsonObject.toString(), null);
        } catch (IOException e) {
            logger.error("雷拓接口服务访问失败，请查看是否开启.");
            return null;
        }
        long endTime = System.currentTimeMillis();
        logger.info("雷拓IP广播平台接口调用，耗时:{} ms,返回结果：dataStr = {}", (endTime - beginTime), dataStr);
        return JSONObject.parseObject(dataStr);
    }
}