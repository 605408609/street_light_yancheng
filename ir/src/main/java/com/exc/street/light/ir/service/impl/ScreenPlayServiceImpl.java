/**
 * @filename:ScreenPlayServiceImpl 2020-04-26
 * @project ir  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.exc.street.light.resource.entity.ir.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.ir.config.parameter.ScreenApi;
import com.exc.street.light.ir.mapper.ScreenPlayMapper;
import com.exc.street.light.ir.service.ScreenDeviceService;
import com.exc.street.light.ir.service.ScreenPlayDeviceService;
import com.exc.street.light.ir.service.ScreenPlayService;
import com.exc.street.light.ir.service.ScreenPlayStrategyService;
import com.exc.street.light.ir.service.ScreenProgramService;
import com.exc.street.light.ir.utils.ScreenControlUtil;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.IrScreenPlayQuery;
import com.exc.street.light.resource.utils.DataUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.IrReqScreenProgramVO;
import com.exc.street.light.resource.vo.req.IrReqSendProgramVO;
import com.exc.street.light.resource.vo.resp.IrRespScreenPlayVO;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ScreenPlayServiceImpl extends ServiceImpl<ScreenPlayMapper, ScreenPlay> implements ScreenPlayService {
    private static final Logger logger = LoggerFactory.getLogger(ScreenPlayServiceImpl.class);

    @Autowired
    private ScreenProgramService screenProgramService;
    @Autowired
    private ScreenPlayDeviceService screenPlayDeviceService;
    @Autowired
    private ScreenDeviceService screenDeviceService;
    @Autowired
    private ScreenPlayStrategyService screenPlayStrategyService;
    @Autowired
    private ScreenApi screenApi;
    
    @Autowired
    private LogUserService userService;

    @Autowired
    private ScreenPlayService screenPlayService;



    @Override
    public Result updateScreenPlay(IrReqScreenProgramVO irReqScreenProgramVO, HttpServletRequest httpServletRequest) {
        logger.info("编辑正在播放节目，接收数据{}", irReqScreenProgramVO);
        Result reqResult = new Result();
        // 修改节目
        Result resultProgram = screenProgramService.updateProgram(irReqScreenProgramVO, httpServletRequest);
        if (resultProgram.getCode() != Const.CODE_SUCCESS) {
            return reqResult.error("节目修改失败");
        }
        // 发布节目
        IrReqSendProgramVO irReqSendProgramVO = irReqScreenProgramVO.getIrReqSendProgramVO();
        irReqSendProgramVO.setProgramId(irReqScreenProgramVO.getId());
        Result resultSend = screenProgramService.sendProgram(irReqScreenProgramVO.getIrReqSendProgramVO(), httpServletRequest);
        String msg = "节目修改成功，" + resultSend.getMessage();
        // 删除当前播放中的节目
        if (resultSend.getCode() == Const.CODE_SUCCESS) {
            this.removeById(irReqScreenProgramVO.getPlayId());
        }
        resultSend.setMessage(msg);
        return resultSend;
    }

    @Override
    public Result getQuery(IrScreenPlayQuery irScreenPlayQuery, HttpServletRequest httpServletRequest) {
        logger.info("分页查询播放中的节目列表，接收数据{}", irScreenPlayQuery);
        this.refresh(httpServletRequest);
        // 获取没有结束播放的数据
        LambdaQueryWrapper<ScreenPlay> wrapperScreenPlay = new LambdaQueryWrapper();
        List<Integer> statusList = new ArrayList<>();
        statusList.add(0);
        statusList.add(1);
        wrapperScreenPlay.in(ScreenPlay::getPlayStatus, statusList);
        List<ScreenPlay> screenPlayList = this.list(wrapperScreenPlay);
        List<Integer> screenPlayIdList = screenPlayList.stream().map(ScreenPlay::getId).collect(Collectors.toList());
        if (screenPlayIdList != null && screenPlayIdList.size() > 0) {
            // 获取对应的播放设备中间表
            LambdaQueryWrapper<ScreenPlayDevice> wrapperScreenPlayDevice = new LambdaQueryWrapper();
            wrapperScreenPlayDevice.in(ScreenPlayDevice::getScreenPlayId, screenPlayIdList);
            List<ScreenPlayDevice> screenPlayDeviceList = screenPlayDeviceService.list(wrapperScreenPlayDevice);
            // 获取没有关联设备的播放数据
            List<Integer> noPlayIdList = new ArrayList<>();
            for (Integer playId : screenPlayIdList) {
                List<ScreenPlayDevice> collect = screenPlayDeviceList.stream().filter(a -> playId.equals(a.getScreenPlayId())).collect(Collectors.toList());
                if (collect == null || collect.size() == 0) {
                    noPlayIdList.add(playId);
                }
            }
            if (noPlayIdList != null && noPlayIdList.size() > 0) {
                // 修改播放数据为结束播放
                LambdaUpdateWrapper<ScreenPlay> updateWrapper = new LambdaUpdateWrapper();
                updateWrapper.set(ScreenPlay::getPlayStatus, 2).in(ScreenPlay::getId, noPlayIdList);
                this.update(updateWrapper);
            }
        }

        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            irScreenPlayQuery.setAreaId(user.getAreaId());
        }

        //分页查询播放中的节目列表
        IPage<IrScreenPlayQuery> page = new Page<IrScreenPlayQuery>(irScreenPlayQuery.getPageNum(), irScreenPlayQuery.getPageSize());
        IPage<IrRespScreenPlayVO> irScreenPlayVOList = this.baseMapper.query(page, irScreenPlayQuery);
        Result result = new Result();
        System.out.println(irScreenPlayVOList);
        return result.success(irScreenPlayVOList);
    }

    @Override
    public Result cancel(Integer id, HttpServletRequest httpServletRequest) {
        logger.info("取消播放，接收数据{}", id);
        // 获取设备id集合
        LambdaQueryWrapper<ScreenPlayDevice> wrapperScreenPlayDevice = new LambdaQueryWrapper();
        wrapperScreenPlayDevice.eq(ScreenPlayDevice::getScreenPlayId, id);
        List<ScreenPlayDevice> list = screenPlayDeviceService.list(wrapperScreenPlayDevice);
        List<Integer> deviceIdList = list.stream().map(ScreenPlayDevice::getDeviceId).collect(Collectors.toList());
        // 获取设备编号集合
        LambdaQueryWrapper<ScreenDevice> wrapperScreenDevice = new LambdaQueryWrapper();
        wrapperScreenDevice.in(ScreenDevice::getId, deviceIdList);
        List<ScreenDevice> screenDeviceList = screenDeviceService.list(wrapperScreenDevice);
        List<String> numList = screenDeviceList.stream().map(ScreenDevice::getNum).collect(Collectors.toList());
        // 获取成功设备编号列表
        Result result1 = ScreenControlUtil.deleteProgramControl(screenApi.getIp(), screenApi.getPort(), numList);
        JSONObject jsonObject = (JSONObject) result1.getData();
        List<JSONObject> successObjectList = (List<JSONObject>) jsonObject.get("successObjectList");
        List<String> snList = new ArrayList<String>();
        for (JSONObject object : successObjectList) {
            String sn = (String) object.get("sn");
            snList.add(sn);
        }
        if (snList != null && snList.size() > 0) {
            // 根据设备编号获取设备列表
            LambdaQueryWrapper<ScreenDevice> wrapper = new LambdaQueryWrapper<ScreenDevice>();
            wrapper.in(ScreenDevice::getNum, snList);
            List<ScreenDevice> successScreenDeviceList = screenDeviceService.list(wrapper);
        }
        // 修改播放状态
        ScreenPlay byId = this.getById(id);
        byId.setPlayStatus(2);
        this.updateById(byId);
        Result result = new Result();
        return result.success("取消播放成功");
    }

    @Override
    public Result getScreenPlay(Integer id, HttpServletRequest httpServletRequest) {
        logger.info("获取播放详情，接收数据{}", id);
        ScreenPlay screenPlay = this.getById(id);
        // 节目详情
        Result result = screenProgramService.get(screenPlay.getProgramId(), httpServletRequest);
        IrReqScreenProgramVO irReqScreenProgramVO = (IrReqScreenProgramVO) result.getData();
        // 策略详情
        LambdaQueryWrapper<ScreenPlayStrategy> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(ScreenPlayStrategy::getScreenPlayId, id);
        ScreenPlayStrategy one = screenPlayStrategyService.getOne(lambdaQueryWrapper);
        IrReqSendProgramVO irReqSendProgramVO = new IrReqSendProgramVO();
        Integer weekValue = one.getWeekValue();
        Integer[] cycleName = DataUtil.getCycleName(weekValue);
        BeanUtils.copyProperties(one, irReqSendProgramVO);
        irReqSendProgramVO.setCycleTypes(cycleName);
        // 设备列表
        LambdaQueryWrapper<ScreenPlayDevice> wrapper = new LambdaQueryWrapper();
        wrapper.eq(ScreenPlayDevice::getScreenPlayId, id);
        List<ScreenPlayDevice> screenPlayDeviceList = screenPlayDeviceService.list(wrapper);
        List<Integer> deviceIdList = screenPlayDeviceList.stream().map(ScreenPlayDevice::getDeviceId).collect(Collectors.toList());
        List<String> screenDeviceIdStringList = new ArrayList<>();
        if(screenPlay.getDeviceSubordinate() == 1){
            for (Integer deviceId : deviceIdList) {
                screenDeviceIdStringList.add("device" + deviceId);
            }
        }else if(screenPlay.getDeviceSubordinate() == 2){
            for (Integer deviceId : deviceIdList) {
                screenDeviceIdStringList.add("group" + deviceId);
            }
        }
        irReqSendProgramVO.setScreenDeviceIdStringList(screenDeviceIdStringList);
        irReqSendProgramVO.setScreenDeviceIdList(deviceIdList);
        irReqSendProgramVO.setDeviceSubordinate(screenPlay.getDeviceSubordinate());
        irReqScreenProgramVO.setPlayId(id);
        irReqScreenProgramVO.setIrReqSendProgramVO(irReqSendProgramVO);

        Result result1 = new Result();
        return result1.success(irReqScreenProgramVO);
    }

    @Override
    public Result refresh(HttpServletRequest request) {
        logger.info("播放节目列表数据刷新");
        Result result = new Result();
        // 播放id集合
        LambdaQueryWrapper<ScreenPlay> screenPlayWrapper = new LambdaQueryWrapper<ScreenPlay>();
        screenPlayWrapper.eq(ScreenPlay::getPlayStatus, 0).eq(ScreenPlay::getPlayStatus, 1);
        List<ScreenPlay> screenPlayListAll = this.list(screenPlayWrapper);
        List<Integer> screenPlayIdList = screenPlayListAll.stream().map(ScreenPlay::getId).collect(Collectors.toList());
        if (screenPlayIdList == null || screenPlayIdList.size() == 0) {
            return result.success("");
        }
        // 播放策略集合
        LambdaQueryWrapper<ScreenPlayStrategy> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ScreenPlayStrategy::getScreenPlayId, screenPlayIdList);
        Collection<ScreenPlayStrategy> screenPlayStrategys = screenPlayStrategyService.list(wrapper);
        // 播放集合
        Collection<ScreenPlay> screenPlays = this.listByIds(screenPlayIdList);
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        List<ScreenPlay> screenPlayList = new ArrayList<>();
        for (ScreenPlayStrategy screenPlayStrategy : screenPlayStrategys) {
            // 获取时间
            Date startDateTime = null;
            Date endDateTime = null;
            Date programStartDateTime = null;
            Date programEndDateTime = null;
            String currentDateString = formatDate.format(currentDate);
            // 对应的播放节目
            List<ScreenPlay> collect = screenPlays.stream().filter(a -> screenPlayStrategy.getScreenPlayId().equals(a.getId())).collect(Collectors.toList());
            if (collect != null && collect.size() > 0 && collect.get(0) != null) {
                ScreenPlay screenPlay = collect.get(0);
                try {
                    startDateTime = formatDateTime.parse(currentDateString + " " + screenPlayStrategy.getExecutionStartTime());
                    endDateTime = formatDateTime.parse(currentDateString + " " + screenPlayStrategy.getExecutionEndTime());
                    programStartDateTime = formatDateTime.parse(screenPlayStrategy.getStartDate() + " " + screenPlayStrategy.getExecutionStartTime());
                    programEndDateTime = formatDateTime.parse(screenPlayStrategy.getEndDate() + " " + screenPlayStrategy.getExecutionEndTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long time = currentDate.getTime();
                if (programStartDateTime.getTime() <= time && time <= programEndDateTime.getTime()) {
                    if (startDateTime.getTime() <= time && time <= endDateTime.getTime()) {
                        screenPlay.setPlayStatus(1);
                    } else {
                        screenPlay.setPlayStatus(0);
                    }
                } else if (time >= programEndDateTime.getTime()) {
                    screenPlay.setPlayStatus(2);
                } else {
                    screenPlay.setPlayStatus(0);
                }
                screenPlayList.add(screenPlay);
            }
        }
        if (screenPlayList != null && screenPlayList.size() > 0) {
            this.updateBatchById(screenPlayList);
        }
        return result.success("");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteProgram(Integer id, HttpServletRequest httpServletRequest) {
        logger.info("节目播放列表中要删除的：id="+id);
        Result result = new Result();
        //查看哪个节目在播放  要把屏幕集合查出来--根据显示屏和播放中间表
        LambdaQueryWrapper<ScreenPlayDevice> screenPlayDeviceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        screenPlayDeviceLambdaQueryWrapper.eq(ScreenPlayDevice::getScreenPlayId,id);
        List<ScreenPlayDevice> screenPlayDevicesList = screenPlayDeviceService.list(screenPlayDeviceLambdaQueryWrapper);
        List<Integer> deviceIdList = screenPlayDevicesList.stream().map(ScreenPlayDevice::getDeviceId).collect(Collectors.toList());

        //查询显示屏集合
        LambdaQueryWrapper<ScreenDevice> screenDeviceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        screenDeviceLambdaQueryWrapper.in(ScreenDevice::getId,deviceIdList);
        List<ScreenDevice> screenDeviceList = screenDeviceService.list(screenDeviceLambdaQueryWrapper);

        //显示屏编号
        List<String> snList = screenDeviceList.stream().map(ScreenDevice::getNum).collect(Collectors.toList());
        if (snList == null || snList.size() == 0) {
            return result.error("目标显示屏编号无效");
        }

        //根据传过来的 显示屏播放表Id 查询此对象  在查寻该节目的播放状态。
        ScreenPlay screenProgramPlay = screenPlayService.getById(id);
        Integer playStatus = screenProgramPlay.getPlayStatus();
        //playStatus==1 节目正在播放中
        if (playStatus==1){
            //显示屏播放列表节目的删除
            screenPlayService.removeById(id);

            //删除显示屏表和显示屏播放的中间表
            LambdaQueryWrapper<ScreenPlayDevice> playDeviceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            playDeviceLambdaQueryWrapper.eq(ScreenPlayDevice::getDeviceId,id);
            screenPlayDeviceService.remove(playDeviceLambdaQueryWrapper);

            //获取显示屏下发策略的id
            LambdaQueryWrapper<ScreenPlayStrategy> screenPlayLambdaQueryWrapper = new LambdaQueryWrapper<>();
            screenPlayLambdaQueryWrapper.eq(ScreenPlayStrategy::getScreenPlayId,id);
            List<ScreenPlayStrategy> list = screenPlayStrategyService.list(screenPlayLambdaQueryWrapper);
            List<Integer> screenPlayStrategyIdList = list.stream().map(ScreenPlayStrategy::getId).collect(Collectors.toList());

            //删除显示屏下发策略
            LambdaQueryWrapper<ScreenPlayStrategy> screenPlayStrategyLambdaQueryWrapper = new LambdaQueryWrapper<ScreenPlayStrategy>();
            screenPlayStrategyLambdaQueryWrapper.in(ScreenPlayStrategy::getScreenPlayId,screenPlayStrategyIdList);
            screenPlayStrategyService.remove(screenPlayLambdaQueryWrapper);

            //删除完之后，让屏幕变灰色  下发“type”：“clearPlayTask” Json。
            Result jsonList = ScreenControlUtil.deleteProgramControl(screenApi.getIp(), screenApi.getPort(), snList);
            JSONObject jsonListData = (JSONObject) jsonList.getData();
            List<JSONObject> successObjectList = (List<JSONObject>) jsonListData.get("successObjectList");
            List<String> failDataList = new ArrayList<>();
            for (JSONObject jsonObject : successObjectList) {
                String  messagestring = (String) jsonObject.get("_type");
                //看data 里面是否包含 “success”字符
                //“type”：“clearPlayTask” json下发成功。
                if (messagestring.equals("success")){
                    failDataList.add(messagestring);
                }
            }
            return result.success("failDataList为空,下发成功.failDataList不为空,下发失败",failDataList);

        }else {
            //playStatus==0 节目待播放
            //playStatus==2 节目已经播放完  直接删除
            //显示屏播放列表节目的删除
            screenPlayService.removeById(id);

            //删除显示屏表和显示屏播放的中间表
            LambdaQueryWrapper<ScreenPlayDevice> playDeviceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            playDeviceLambdaQueryWrapper.eq(ScreenPlayDevice::getScreenPlayId,id);
            screenPlayDeviceService.remove(playDeviceLambdaQueryWrapper);

            //获取显示屏下发策略的id
            LambdaQueryWrapper<ScreenPlayStrategy> screenPlayLambdaQueryWrapper = new LambdaQueryWrapper<>();
            screenPlayLambdaQueryWrapper.eq(ScreenPlayStrategy::getScreenPlayId,id);
            List<ScreenPlayStrategy> list = screenPlayStrategyService.list(screenPlayLambdaQueryWrapper);
            List<Integer> screenPlayStrategyIdList = list.stream().map(ScreenPlayStrategy::getId).collect(Collectors.toList());

            //删除显示屏下发策略
            LambdaQueryWrapper<ScreenPlayStrategy> screenPlayStrategyLambdaQueryWrapper = new LambdaQueryWrapper<ScreenPlayStrategy>();
            screenPlayStrategyLambdaQueryWrapper.in(ScreenPlayStrategy::getScreenPlayId,screenPlayStrategyIdList);
            screenPlayStrategyService.remove(screenPlayLambdaQueryWrapper);
        }
        return result.success("删除成功");
    }
}