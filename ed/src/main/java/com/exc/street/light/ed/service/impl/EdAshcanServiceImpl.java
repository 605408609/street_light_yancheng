/**
 * @filename:EdAshcanServiceImpl 2020-09-28
 * @project ed  V1.0
 * Copyright(c) 2018 Huang Jin Hao Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ed.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.ed.mapper.AlarmMapper;
import com.exc.street.light.ed.mapper.EdAshcanMapper;
import com.exc.street.light.ed.po.KafkaMessage;
import com.exc.street.light.ed.service.EdAshcanService;
import com.exc.street.light.ed.service.KafkaMessageService;
import com.exc.street.light.ed.utils.RedisUtil;
import com.exc.street.light.lj.utils.CTWingApi;
import com.exc.street.light.lj.utils.MessageParse;
import com.exc.street.light.lj.vo.TrashVo;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.WebsocketQuery;
import com.exc.street.light.resource.entity.ed.EdAshcan;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.EdReqAshcanPageVO;
import com.exc.street.light.resource.vo.resp.EdRespAlarmVO;
import com.exc.street.light.resource.vo.resp.EdRespAshcanVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**   
 * @Description:TODO(垃圾桶信息表服务实现)
 *
 * @version: V1.0
 * @author: Huang Jin Hao
 * 
 */
@Service
public class EdAshcanServiceImpl  extends ServiceImpl<EdAshcanMapper, EdAshcan> implements EdAshcanService  {

    private static final Logger logger = LoggerFactory.getLogger(EdAshcanServiceImpl.class);

    private static long useTime = 0L;
    //时间间隔
    private static final long interval = 24L * 60L * 60L *1000;

    @Autowired
    private LogUserService logUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CTWingApi ctWingApi;

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @Autowired
    private AlarmMapper alarmMapper;

    @Override
    public EdAshcan getEdAshcanByCondition(Integer id, String name, String num) {
        QueryWrapper<EdAshcan> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("id", id);
        }
        if (name != null) {
            queryWrapper.eq("name", name);
        }
        if (num != null) {
            queryWrapper.eq("num", num);
        }
        EdAshcan edAshcan = baseMapper.selectOne(queryWrapper);
        return edAshcan;
    }

    @Override
    public Result getPage(EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request) {

        logger.info("垃圾桶设备分页条件查询,接收参数:{}", edReqAshcanPageVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flg = logUserService.isAdmin(userId);
        if (!flg) {
            edReqAshcanPageVO.setLocationAreaId(user.getAreaId());
        }
        Result result = new Result();
        if (edReqAshcanPageVO.getPageSize() == 0) {
            List<EdRespAshcanVO> edRespAshcanVOList = baseMapper.getPage(edReqAshcanPageVO);
            return result.success(edRespAshcanVOList);
        } else {
            IPage<EdRespAshcanVO> edRespAshcanVOIPage = new Page<>(edReqAshcanPageVO.getPageNum(), edReqAshcanPageVO.getPageSize());
            List<EdRespAshcanVO> edRespAshcanVOList = baseMapper.getPage(edRespAshcanVOIPage, edReqAshcanPageVO);
            edRespAshcanVOIPage.setRecords(edRespAshcanVOList);
            return result.success(edRespAshcanVOIPage);
        }
    }

    @Override
    public Result unique(EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request) {
        logger.info("垃圾桶设备验证唯一性，接收参数：{}", edReqAshcanPageVO);
        Result result = new Result();
        if (edReqAshcanPageVO != null) {
            if (edReqAshcanPageVO.getId() != null) {
                String name = edReqAshcanPageVO.getName();
                if (name != null) {
                    if (name.length() > 10) {
                        return result.error("名称长度超出最大限制（10个字符）");
                    }
                    // 验证名称是否重复
                    EdAshcan edAshcanByCondition = this.getEdAshcanByCondition(null, name, null);
                    if (edAshcanByCondition != null && (edAshcanByCondition.getId() != edReqAshcanPageVO.getId())) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("设备名称不能为空");
                }
                String num = edReqAshcanPageVO.getNum();
                if (num != null) {
                    if (num.length() > 20) {
                        return result.error("编号超出最大长度（20）");
                    }
                    // 验证编号是否重复
                    EdAshcan edAshcanByCondition = this.getEdAshcanByCondition(null, null, num);
                    if (edAshcanByCondition != null && (edAshcanByCondition.getId() != edReqAshcanPageVO.getId())) {
                        return result.error("编号已存在");
                    }
                }
            } else {
                String name = edReqAshcanPageVO.getName();
                if (name != null) {
                    if (name.length() > 10) {
                        return result.error("名称长度超出最大限制（10个字符）");
                    }
                    // 验证名称是否重复
                    EdAshcan edAshcanByCondition = this.getEdAshcanByCondition(null, name, null);
                    if (edAshcanByCondition != null) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("设备名称不能为空");
                }
                String num = edReqAshcanPageVO.getNum();
                if (num != null) {
                    if (num.length() > 20) {
                        return result.error("编号超出最大长度（20）");
                    }
                    // 验证编号是否重复
                    EdAshcan edAshcanByCondition = this.getEdAshcanByCondition(null, null, num);
                    if (edAshcanByCondition != null) {
                        return result.error("编号已存在");
                    }
                }
            }
        }
        return result.success("");
    }

    @Override
    public Result add(EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request) {
        logger.info("添加垃圾桶设备，接收参数：{}", edReqAshcanPageVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result result = new Result().error("");
        if (null != edReqAshcanPageVO) {
            String name = edReqAshcanPageVO.getName();
            String num = edReqAshcanPageVO.getNum();
            Integer locationSiteId = edReqAshcanPageVO.getLocationSiteId();
            String longitude = edReqAshcanPageVO.getLongitude();
            String latitude = edReqAshcanPageVO.getLatitude();

            EdAshcan edAshcan = new EdAshcan();
            edAshcan.setName(name);
            edAshcan.setNum(num);
            edAshcan.setLocationSiteId(locationSiteId);
            edAshcan.setLongitude(longitude);
            edAshcan.setLatitude(latitude);
            edAshcan.setArmedStatus(1);
            edAshcan.setRealData(0.0);
            edAshcan.setStatus(0);
            edAshcan.setDeviceVersion("1.0");
            edAshcan.setUploadCycle(1440);
            edAshcan.setCreator(userId);
            edAshcan.setCreateTime(new Date());
            try {
                Result device = ctWingApi.createDevice(name, num);
                if (device.getCode() == 200) {
                    String sendId = (String) device.getData();
                    if (sendId != null) {
                        edAshcan.setSendId(sendId);
                    }
                }
            } catch (Exception e) {
                logger.error("垃圾桶设备注册至电信平台失败");
                e.printStackTrace();
            }
            boolean save = this.save(edAshcan);
            if (!save) {

                result.error("添加失败");
            } else {
                result.success("添加成功");
            }
        } else {
            result.error("请传入正确参数！");
        }
        return result;
    }

    @Override
    public Result updateDevice(EdReqAshcanPageVO edReqAshcanPageVO, HttpServletRequest request) {
        logger.info("修改垃圾桶设备，接收参数：{}", edReqAshcanPageVO);
        Result result = new Result();
        if (null != edReqAshcanPageVO) {
            EdAshcan edAshcan = new EdAshcan();
            BeanUtils.copyProperties(edReqAshcanPageVO, edAshcan);
            boolean rsg = this.updateById(edAshcan);
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
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("垃圾桶id字符串解析出错");
            return result.error("传递参数错误");
        }
        try {
            List<String> sendIdList = new ArrayList<>();
            List<EdAshcan> edAshcanList = baseMapper.selectBatchIds(deviceIdList);
            if(edAshcanList!=null&&edAshcanList.size()>0){
                sendIdList = edAshcanList.stream().map(EdAshcan::getSendId).collect(Collectors.toList());
            }
            int flag = baseMapper.deleteBatchIds(deviceIdList);
            if (flag>0) {
                if (!sendIdList.isEmpty()) {
                    for (String nodeId : sendIdList) {
                        ctWingApi.deleteDevice(nodeId);
                    }
                }
                return result.success("删除成功");
            }
            else {
                return result.error("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("垃圾桶设备批量删除失败，传递参数错误");
            return result.error("删除失败");
        }

    }

    @Override
    public Result setThreshold(List<EdReqAshcanPageVO> edReqAshcanPageVOS, HttpServletRequest request) {
        logger.info("设置垃圾桶深度阈值/布防撤防,接收参数:{}", edReqAshcanPageVOS);
        Result result = new Result();
        List<Integer> deviceIdList = edReqAshcanPageVOS.stream().map(EdReqAshcanPageVO::getId).collect(Collectors.toList());
        List<EdAshcan> edAshcansTemp = baseMapper.selectBatchIds(deviceIdList);
        List<Integer> statusList = edAshcansTemp.stream().map(EdAshcan::getStatus).collect(Collectors.toList());
        if (statusList.contains(0)) {
            return result.error("存在离线设备，无法成功设置");
        }
        List<EdAshcan> edAshcans = new ArrayList<>();
        List<String> keyList = new ArrayList<>();
        Map<String, String> messageMap = new HashMap<>();
        for (EdReqAshcanPageVO edReqAshcanPageVO : edReqAshcanPageVOS) {
            EdAshcan edAshcan = new EdAshcan();
            edAshcan.setId(edReqAshcanPageVO.getId());
            if (edReqAshcanPageVO.getLimitUpper() != null) {
                Integer limitUpper = edReqAshcanPageVO.getLimitUpper().intValue();
                edAshcan.setLimitUpper(edReqAshcanPageVO.getLimitUpper());
                String key = "limitUpper" + edReqAshcanPageVO.getId();
                String message = MessageParse.order(0, 0, 0, 0, limitUpper, (int) ((Math.random() * 9 + 1) * 1000));
                keyList.add(key);
                messageMap.put(key, message);
            }
            if (edReqAshcanPageVO.getArmedStatus() != null) {
                Integer armedStatus = edReqAshcanPageVO.getArmedStatus();
                edAshcan.setArmedStatus(armedStatus);
                String key = "armedStatus" + edReqAshcanPageVO.getId();
                String message = MessageParse.defence(0, armedStatus, (int) ((Math.random() * 9 + 1) * 1000));
                keyList.add(key);
                messageMap.put(key, message);
            }
            edAshcans.add(edAshcan);
        }
        boolean flag = this.updateBatchById(edAshcans);
        if (flag) {
            //在Redis中添加设置垃圾桶深度阈值/布防撤防的报文
            if (!messageMap.isEmpty() && !keyList.isEmpty()) {
                for (String key : keyList) {
                    String message = messageMap.get(key);
                    redisUtil.set(key, message);
                    redisUtil.expire(key, 90000);
                }
            }
            return result.success("设置成功");
        } else {
            return result.error("设置失败");
        }
    }

    @Override
    public Result alarmHandling(TrashVo trashVos, String num) {
        logger.info("垃圾桶告警信息处理：{}，{}",trashVos,num);
        if(trashVos==null){
            return new Result().success("不存在告警");
        }
        Integer trashType = trashVos.getTrashType();
        if(trashType==null){
            return new Result().success("不存在告警");
        }
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setType(1);
        kafkaMessage.setIs2All(2);
        kafkaMessage.setUserIds(null);
        WebsocketQuery<EdRespAlarmVO> websocketQuery = new WebsocketQuery<>();
        websocketQuery.setType(17);
        //获取告警对象
        EdRespAlarmVO edRespAlarmVO = baseMapper.selectEdAshcanAlarm(num);
        if(edRespAlarmVO==null){
            return new Result().error("不存在该设备");
        }
        edRespAlarmVO.setAlarmTime(new Date());
        switch (trashType){
            case 0:
                //恢复正常
                edRespAlarmVO.setAlarmStatus(0);
                Integer deviceId = edRespAlarmVO.getDeviceId();
                List<Alarm> alarmList = this.selectAlarmByDeviceId(deviceId, 14);
                for (Alarm alarm : alarmList) {
                    alarm.setStatus(3);
                    alarmMapper.updateById(alarm);

                    edRespAlarmVO.setId(alarm.getId());
                    websocketQuery.setData(edRespAlarmVO);
                    String message = JSON.toJSONString(websocketQuery);
                    kafkaMessage.setMessage(message);
                    kafkaMessageService.sendMessage("websocket1",kafkaMessage);
                    logger.info("垃圾桶告警信息推送成功：{}",kafkaMessage);
                }
                break;
            case 5:
                //A桶告警
                edRespAlarmVO.setAlarmStatus(1);
                Result result = this.saveAlarm(edRespAlarmVO, 14);
                Alarm alarm = (Alarm)result.getData();

                edRespAlarmVO.setId(alarm.getId());
                websocketQuery.setData(edRespAlarmVO);
                String message = JSON.toJSONString(websocketQuery);
                kafkaMessage.setMessage(message);
                kafkaMessageService.sendMessage("websocket1",kafkaMessage);
                logger.info("垃圾桶告警信息推送成功：{}",kafkaMessage);
                break;
            case 6:
                //B桶告警
                break;
            default:
                break;
        }

        return new Result().success("告警处理成功");
    }

    @Override
    public boolean sendMessage(String imei) {
        EdAshcan edAshcanByCondition = getEdAshcanByCondition(null, null, imei);
        String sendId = edAshcanByCondition.getSendId();
        Integer id = edAshcanByCondition.getId();
        Object limitUpperObj = redisUtil.get("limitUpper" + id);
        try {
            if(limitUpperObj!=null){
                String limitUpper = (String)limitUpperObj;
                ctWingApi.sendMessage(sendId,limitUpper);
                logger.info("下发修改垃圾桶配置报文"+limitUpper);
            }
            Object armedStatusObj = redisUtil.get("armedStatus" + id);
            if(armedStatusObj!=null){
                String armedStatus = (String)armedStatusObj;
                ctWingApi.sendMessage(sendId,armedStatus);
                logger.info("下发修改垃圾桶撤防报文"+armedStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Result saveAlarm(EdRespAlarmVO edRespAlarmVO, Integer type_id) {
        logger.info("生成告警信息：{},{}",edRespAlarmVO,type_id);
        Alarm alarm = new Alarm();
        alarm.setTypeId(type_id);
        alarm.setCreateTime(new Date());
        alarm.setHaveRead(0);
        alarm.setStatus(1);
        alarm.setDeviceId(edRespAlarmVO.getDeviceId());
        alarm.setDeviceName(edRespAlarmVO.getName());
        alarm.setDeviceTypeId(type_id);
        String content = "";
        if(type_id==14){
            content = "垃圾桶";
        }else {
            content = "井盖";
        }
        alarm.setContent(content+edRespAlarmVO.getNum());
        alarmMapper.insert(alarm);
        return new Result().success("告警信息生成成功",alarm);
    }

    @Override
    public List<Alarm> selectAlarmByDeviceId(Integer deviceId, Integer typeId) {
        QueryWrapper<Alarm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_id",deviceId);
        queryWrapper.eq("type_id",typeId);
        queryWrapper.eq("status",1);
        return alarmMapper.selectList(queryWrapper);
    }

    @Override
    public void statusUpdate(List<EdAshcan> list) {
        try {
            for (EdAshcan edAshcan : list) {
                //取出设备信息标识
                String sendId = edAshcan.getSendId();
                //根据标识取出redis中此设备上次登录的时间
                Object data = redisUtil.get(sendId);
                if (data == null) {
                    logger.info("Redis中没有数据");
                    continue;
                }
                Long time = (long) data;
                //生成当前时间
                long currentTimeMillis = System.currentTimeMillis();
                //判断
                if (currentTimeMillis - time > interval) {
                    //改为离线状态
                    edAshcan.setStatus(0);
                    baseMapper.updateStatus(edAshcan);
                } else {
                    //修改为在线状态
                    edAshcan.setStatus(1);
                    baseMapper.updateStatus(edAshcan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}