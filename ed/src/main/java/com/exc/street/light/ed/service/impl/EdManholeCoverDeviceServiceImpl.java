/**
 * @filename:EdManholeCoverDeviceServiceImpl 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ed.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.co.client.service.CoverMqttClientService;
import com.exc.street.light.co.vo.AlarmMsgVo;
import com.exc.street.light.ed.mapper.AlarmMapper;
import com.exc.street.light.ed.po.KafkaMessage;
import com.exc.street.light.ed.service.EdAshcanService;
import com.exc.street.light.ed.service.KafkaMessageService;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.WebsocketQuery;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;
import com.exc.street.light.resource.entity.ed.EdManholeCoverDevice;
import com.exc.street.light.ed.mapper.EdManholeCoverDeviceMapper;
import com.exc.street.light.ed.service.EdManholeCoverDeviceService;
import com.exc.street.light.resource.entity.sl.LampPosition;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.entity.sl.SystemDeviceParameter;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.EdReqManholeCoverDevicePageVO;
import com.exc.street.light.resource.vo.req.SlReqInstallLampZkzlVO;
import com.exc.street.light.resource.vo.resp.EdRespAlarmVO;
import com.exc.street.light.resource.vo.resp.EdRespManholeCoverDeviceVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceParameterVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceVO;
import com.exc.street.light.resource.vo.sl.LoopParamVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(井盖设备表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 *
 */
@Service
public class EdManholeCoverDeviceServiceImpl  extends ServiceImpl<EdManholeCoverDeviceMapper, EdManholeCoverDevice> implements EdManholeCoverDeviceService  {

    private static final Logger logger = LoggerFactory.getLogger(EdManholeCoverDeviceServiceImpl.class);

    @Autowired
    private LogUserService logUserService;

    @Autowired
    private CoverMqttClientService coverMqttClientService;

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @Autowired
    private EdAshcanService edAshcanService;

    @Autowired
    private AlarmMapper alarmMapper;

    @Override
    public EdManholeCoverDevice getEdManholeCoverDeviceByCondition(Integer id, String name, String num) {
        QueryWrapper<EdManholeCoverDevice> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("id", id);
        }
        if (name != null) {
            queryWrapper.eq("name", name);
        }
        if (num != null) {
            queryWrapper.eq("num", num);
        }
        EdManholeCoverDevice edManholeCoverDevice = baseMapper.selectOne(queryWrapper);
        return edManholeCoverDevice;
    }

    @Override
    public Result getPage(EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request) {

        logger.info("井盖设备分页条件查询,接收参数:{}", edReqManholeCoverDevicePageVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flg = logUserService.isAdmin(userId);
        if (!flg) {
            edReqManholeCoverDevicePageVO.setLocationAreaId(user.getAreaId());
        }
        Result result = new Result();
        if (edReqManholeCoverDevicePageVO.getPageSize() == 0) {
            List<EdRespManholeCoverDeviceVO> edRespManholeCoverDeviceVOList = baseMapper.getPage(edReqManholeCoverDevicePageVO);
            return result.success(edRespManholeCoverDeviceVOList);
        } else {
            IPage<EdRespManholeCoverDeviceVO> edRespManholeCoverDeviceVOIPage = new Page<>(edReqManholeCoverDevicePageVO.getPageNum(), edReqManholeCoverDevicePageVO.getPageSize());
            List<EdRespManholeCoverDeviceVO> edRespManholeCoverDeviceVOList = baseMapper.getPage(edRespManholeCoverDeviceVOIPage, edReqManholeCoverDevicePageVO);
            edRespManholeCoverDeviceVOIPage.setRecords(edRespManholeCoverDeviceVOList);
            return result.success(edRespManholeCoverDeviceVOIPage);
        }
    }

    @Override
    public Result unique(EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request) {
        logger.info("井盖设备验证唯一性，接收参数：{}", edReqManholeCoverDevicePageVO);
        Result result = new Result();
        if (edReqManholeCoverDevicePageVO != null) {
            if (edReqManholeCoverDevicePageVO.getId() != null) {
                String name = edReqManholeCoverDevicePageVO.getName();
                if(name!=null){
                    if (name.length() > 10) {
                        return result.error("名称长度超出最大限制（10个字符）");
                    }
                    // 验证名称是否重复
                    EdManholeCoverDevice edManholeCoverDeviceByCondition = this.getEdManholeCoverDeviceByCondition(null, name, null);
                    if (edManholeCoverDeviceByCondition != null && (edManholeCoverDeviceByCondition.getId() != edReqManholeCoverDevicePageVO.getId())) {
                        return result.error("名称已存在");
                    }
                }else {
                    return result.error("设备名称不能为空");
                }
                String num = edReqManholeCoverDevicePageVO.getNum();
                if (num != null) {
                    if (num.length() > 20) {
                        return result.error("编号超出最大长度（20）");
                    }
                    // 验证编号是否重复
                    EdManholeCoverDevice edManholeCoverDeviceByCondition = this.getEdManholeCoverDeviceByCondition(null, null, num);
                    if (edManholeCoverDeviceByCondition != null && (edManholeCoverDeviceByCondition.getId() != edReqManholeCoverDevicePageVO.getId())) {
                        return result.error("编号已存在");
                    }
                }
            } else {
                String name = edReqManholeCoverDevicePageVO.getName();
                if(name!=null){
                    if (name.length() > 10) {
                        return result.error("名称长度超出最大限制（10个字符）");
                    }
                    // 验证名称是否重复
                    EdManholeCoverDevice edManholeCoverDeviceByCondition = this.getEdManholeCoverDeviceByCondition(null, name, null);
                    if (edManholeCoverDeviceByCondition != null) {
                        return result.error("名称已存在");
                    }
                }else {
                    return result.error("设备名称不能为空");
                }
                String num = edReqManholeCoverDevicePageVO.getNum();
                if (num != null) {
                    if (num.length() > 20) {
                        return result.error("编号超出最大长度（20）");
                    }
                    // 验证编号是否重复
                    EdManholeCoverDevice edManholeCoverDeviceByCondition = this.getEdManholeCoverDeviceByCondition(null, null, num);
                    if (edManholeCoverDeviceByCondition != null) {
                        return result.error("编号已存在");
                    }
                }
            }
        }
        return result.success("");
    }

    @Override
    public Result add(EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request) {
        logger.info("添加井盖设备，接收参数：{}", edReqManholeCoverDevicePageVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result result = new Result().error("");
        if (null != edReqManholeCoverDevicePageVO) {
            String name = edReqManholeCoverDevicePageVO.getName();
            String num = edReqManholeCoverDevicePageVO.getNum();
            Integer locationSiteId = edReqManholeCoverDevicePageVO.getLocationSiteId();
            String longitude = edReqManholeCoverDevicePageVO.getLongitude();
            String latitude = edReqManholeCoverDevicePageVO.getLatitude();

            EdManholeCoverDevice edManholeCoverDevice = new EdManholeCoverDevice();
            edManholeCoverDevice.setName(name);
            edManholeCoverDevice.setNum(num);
            edManholeCoverDevice.setLocationSiteId(locationSiteId);
            edManholeCoverDevice.setLongitude(longitude);
            edManholeCoverDevice.setLatitude(latitude);
            edManholeCoverDevice.setRealData(0.0);
            edManholeCoverDevice.setLimitUpper(30.0);
            edManholeCoverDevice.setStatus(0);
            edManholeCoverDevice.setDeviceVersion("1.0");
            edManholeCoverDevice.setUploadCycle(64800);
            edManholeCoverDevice.setCreator(userId);
            edManholeCoverDevice.setCreateTime(new Date());

            boolean save = this.save(edManholeCoverDevice);
            if(!save){
                result.error("添加失败");
            }else {
                result.success("添加成功");
            }
        } else {
            result.error("请传入正确参数！");
        }
        return result;
    }

    @Override
    public Result updateDevice(EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO, HttpServletRequest request) {
        logger.info("修改井盖设备，接收参数：{}", edReqManholeCoverDevicePageVO);
        Result result = new Result();
        if (null != edReqManholeCoverDevicePageVO) {
            EdManholeCoverDevice edManholeCoverDevice = new EdManholeCoverDevice();
            BeanUtils.copyProperties(edReqManholeCoverDevicePageVO, edManholeCoverDevice);
            boolean rsg = this.updateById(edManholeCoverDevice);
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
    public Result deleteByIds(String ids, HttpServletRequest request) {
        logger.info("批量删除设备,接收参数:{}", ids);
        Result result = new Result();
        String[] deviceIds = ids.split(",");
        List<Integer> deviceIdList = new ArrayList<>();
        try {
            for (String deviceId : deviceIds) {
                deviceIdList.add(Integer.parseInt(deviceId));
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("井盖id字符串解析出错");
            return result.error("传递参数错误");
        }
        try {
            int flag = baseMapper.deleteBatchIds(deviceIdList);
            if (flag>0) {
                return result.success("删除成功");
            } else {
                return result.error("删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info("井盖设备批量删除失败，传递参数错误");
            return result.error("删除失败");
        }

    }

    @Override
    public Result setThreshold(List<EdReqManholeCoverDevicePageVO> edReqManholeCoverDevicePageVOS, HttpServletRequest request) {
        logger.info("设置开盖报警倾角阈值,接收参数:{}", edReqManholeCoverDevicePageVOS);
        Result result = new Result();
        List<Integer> deviceIdList = edReqManholeCoverDevicePageVOS.stream().map(EdReqManholeCoverDevicePageVO::getId).collect(Collectors.toList());
        List<EdManholeCoverDevice> edManholeCoverDevicesTemp = baseMapper.selectBatchIds(deviceIdList);
        //List<Integer> statusList = edManholeCoverDevicesTemp.stream().map(EdManholeCoverDevice::getStatus).collect(Collectors.toList());
        List<String> numList = edManholeCoverDevicesTemp.stream().map(EdManholeCoverDevice::getNum).collect(Collectors.toList());
        /*if(statusList.contains(0)){
            return result.error("存在离线设备，无法成功设置阈值");
        }*/
        List<EdManholeCoverDevice> edManholeCoverDevices = new ArrayList<>();
        for (EdReqManholeCoverDevicePageVO edReqManholeCoverDevicePageVO : edReqManholeCoverDevicePageVOS) {
            EdManholeCoverDevice edManholeCoverDevice = new EdManholeCoverDevice();
            edManholeCoverDevice.setId(edReqManholeCoverDevicePageVO.getId());
            edManholeCoverDevice.setLimitUpper(edReqManholeCoverDevicePageVO.getLimitUpper());
            edManholeCoverDevices.add(edManholeCoverDevice);
        }
        boolean flag = this.updateBatchById(edManholeCoverDevices);
        if(flag){
            Double limitUpper = edReqManholeCoverDevicePageVOS.get(0).getLimitUpper();
            boolean b = coverMqttClientService.changeDipAngelLimitBatch(numList, limitUpper.intValue());
            System.out.println(b);
            return  result.success("设置成功");
        }else {
            return result.error("设置失败");
        }
    }

    @Override
    public List<EdManholeCoverDevice> selectAll() {
        logger.info("查询所有EdManholeCoverDevice");
        return baseMapper.selectList(null);
    }

    @Override
    public Result alarmHandling(AlarmMsgVo alarmMsgVo) {
        logger.info("井盖告警信息处理：{}",alarmMsgVo);

        //处理告警信息（推送）
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setType(1);
        kafkaMessage.setIs2All(2);
        kafkaMessage.setUserIds(null);
        WebsocketQuery<EdRespAlarmVO> websocketQuery = new WebsocketQuery<>();
        websocketQuery.setType(18);
        //获取告警对象
        String num = alarmMsgVo.getSnCode();
        EdRespAlarmVO edRespAlarmVO = baseMapper.selectEdManholeCoverDeviceAlarm(num);
        if(edRespAlarmVO==null){
            return new Result().error("不存在该设备");
        }
        edRespAlarmVO.setAlarmTime(new Date());
        int alarmCommandCode = alarmMsgVo.getAlarmCommandCode();
        if(alarmCommandCode==1){
            //新增告警信息
            edRespAlarmVO.setAlarmStatus(1);
            Result result = edAshcanService.saveAlarm(edRespAlarmVO, 15);
            Alarm alarm = (Alarm)result.getData();

            edRespAlarmVO.setId(alarm.getId());
            websocketQuery.setData(edRespAlarmVO);
            String s = JSON.toJSONString(websocketQuery);
            kafkaMessage.setMessage(s);
            kafkaMessageService.sendMessage("websocket1",kafkaMessage);
            logger.info("井盖告警信息推送成功：{}",kafkaMessage);
        }else {
            //恢复正常
            edRespAlarmVO.setAlarmStatus(0);
            Integer deviceId = edRespAlarmVO.getDeviceId();
            List<Alarm> alarmList = edAshcanService.selectAlarmByDeviceId(deviceId, 15);
            for (Alarm alarm : alarmList) {
                alarm.setStatus(3);
                alarmMapper.updateById(alarm);

                edRespAlarmVO.setId(alarm.getId());
                websocketQuery.setData(edRespAlarmVO);
                String s = JSON.toJSONString(websocketQuery);
                kafkaMessage.setMessage(s);
                kafkaMessageService.sendMessage("websocket1",kafkaMessage);
                logger.info("井盖告警信息推送成功：{}",kafkaMessage);
            }
        }

        return new Result().success("告警处理成功");
    }

}