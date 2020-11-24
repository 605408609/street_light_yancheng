/**
 * @filename:MeteorologicalDeviceServiceImpl 2020-03-21
 * @project em  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.em.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.em.config.AlarmConfig;
import com.exc.street.light.em.config.HttpApi;
import com.exc.street.light.em.mapper.MeteorologicalDeviceDao;
import com.exc.street.light.em.mapper.MeteorologicalHistoryDao;
import com.exc.street.light.em.mapper.MeteorologicalRealDao;
import com.exc.street.light.em.po.KafkaMessage;
import com.exc.street.light.em.service.KafkaMessageService;
import com.exc.street.light.em.service.MeteorologicalDeviceService;
import com.exc.street.light.em.service.ScreenSubtitleEmScreenService;
import com.exc.street.light.em.service.ScreenSubtitleEmService;
import com.exc.street.light.em.util.HttpConnectionPools;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.em.*;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.EmMeteorologicalDeviceQueryObject;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.SocketClient;
import com.exc.street.light.resource.vo.req.EmReqStatisParamVO;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitlePlayVO;
import com.exc.street.light.resource.vo.resp.EmRespDeviceAndLampPostVO;
import com.exc.street.light.resource.vo.resp.EmRespMeteorologicalRealVO;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: LeiJing
 */
@Service
public class MeteorologicalDeviceServiceImpl extends ServiceImpl<MeteorologicalDeviceDao, MeteorologicalDevice> implements MeteorologicalDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(MeteorologicalDeviceServiceImpl.class);

    @Autowired
    private HttpApi httpApi;

    @Autowired
    private AlarmConfig alarmConfig;

    @Resource
    private MeteorologicalDeviceDao meteorologicalDeviceDao;

    @Resource
    private MeteorologicalHistoryDao meteorologicalHistoryDao;

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @Resource
    private MeteorologicalRealDao meteorologicalRealDao;

    @Autowired
    private HttpConnectionPools httpConnectionPools;

    @Autowired
    private ScreenSubtitleEmService screenSubtitleEmService;

    @Autowired
    private ScreenSubtitleEmScreenService screenSubtitleEmScreenService;


    @Value("${alarm.topic}")
    private String alarmTopic;
    @Value("${isPlayAppli}")
    private Integer isPlayAppli;
    @Value("${emDeviceIdAppli}")
    private String emDeviceIdAppli;
    @Value("${screenDeviceNumAppli}")
    private String screenDeviceNumAppli;
    @Value("${fontSize}")
    private Integer fontSize;
    @Value("${interval}")
    private Integer interval;
    @Value("${step}")
    private Integer step;

    @Autowired
    private LogUserService userService;



    @Override
    public Result addDevice(MeteorologicalDevice meteorologicalDevice, HttpServletRequest request) {
        logger.info("新增气象设备，接收参数：meteorologicalDevice = {}", meteorologicalDevice);
        Result result = new Result();
        meteorologicalDevice.setCreateTime(new Date());
        meteorologicalDeviceDao.insert(meteorologicalDevice);
        return result.success("新增成功");
    }

    @Override
    public Result updateDevice(MeteorologicalDevice meteorologicalDevice, HttpServletRequest request) {
        logger.info("编辑气象设备，接收参数：meteorologicalDevice = {}", meteorologicalDevice);
        Result result = new Result();
        meteorologicalDeviceDao.updateById(meteorologicalDevice);
        return result.success("新增成功");
    }

    @Override
    public Result deleteDevice(Integer deviceId, HttpServletRequest request) {
        logger.info("删除气象设备，接收参数：deviceId = {}", deviceId);
        Result result = new Result();
        meteorologicalDeviceDao.deleteById(deviceId);
        return result.success("删除成功");
    }

    @Override
    public Result batchDeleteDevice(List<Integer> idList) {
        boolean bool = this.removeByIds(idList);
        Result result = new Result();
        if (bool) {
            return result.success("批量删除成功");
        } else {
            return result.success("批量删除失败");
        }
    }

    @Override
    public Result getDeviceInfo(Integer deviceId, HttpServletRequest request) {
        logger.info("获取气象设备详细信息，接收参数：deviceId= {}", deviceId);
        Result result = new Result();
        MeteorologicalDevice meteorologicalDevice = meteorologicalDeviceDao.selectById(deviceId);
        EmRespDeviceAndLampPostVO vo = new EmRespDeviceAndLampPostVO();
        BeanUtils.copyProperties(meteorologicalDevice, vo);
        try {
            // 获取灯杆信息
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("token", request.getHeader("token"));
            JSONObject slLampPostResult = JSON.parseObject(HttpUtil.get(httpApi.getDlm().get("url") + httpApi.getDlm().get("lampPostById") + meteorologicalDevice.getLampPostId(), headerMap));
            if (slLampPostResult != null && !slLampPostResult.isEmpty()) {
                JSONObject slLampPostResultObj = slLampPostResult.getJSONObject("data");
                SlLampPost slLampPost = JSONObject.toJavaObject(slLampPostResultObj, SlLampPost.class);
                BeanUtils.copyProperties(meteorologicalDevice, vo);
                vo.setLampPostName(slLampPost.getName());
                vo.setLampPostNum(slLampPost.getNum());
                vo.setLongitude(slLampPost.getLongitude());
                vo.setLatitude(slLampPost.getLatitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.success(vo);
    }

    @Override
    public Result getDeviceList(EmMeteorologicalDeviceQueryObject qo, HttpServletRequest request) {
        logger.info("获取气象设备列表，接收参数：qo = {}", qo);

        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        Result result = new Result();
        Page<EmRespDeviceAndLampPostVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<EmRespDeviceAndLampPostVO> resultVOPage = meteorologicalDeviceDao.getPageList(page, qo);
        return result.success(resultVOPage);
    }

    @Override
    public Result batchImportDevice(MeteorologicalDevice meteorologicalDevice, HttpServletRequest request) {
        logger.info("批量导入气象设备，接收参数：meteorologicalDevice = {}", meteorologicalDevice);
        Result result = new Result();
        return result.success("批量导入成功");
    }

    @Override
    public Result uniqueness(MeteorologicalDevice meteorologicalDevice, HttpServletRequest request) {
        logger.info("气象设备唯一性验证，接收参数：meteorologicalDevice = {}", meteorologicalDevice);
        Integer id = meteorologicalDevice.getId();
        String name = meteorologicalDevice.getName();
        String num = meteorologicalDevice.getNum();
        Result result = new Result();

        if (id != null) {
            //如果存在id，则为编辑，判断是否是编辑本身
            if (StringUtils.isNotBlank(name)) {
                LambdaQueryWrapper<MeteorologicalDevice> queryWrapperName = new LambdaQueryWrapper<MeteorologicalDevice>();
                queryWrapperName.eq(MeteorologicalDevice::getName, name);
                queryWrapperName.last("LIMIT 1");
                MeteorologicalDevice meteorologicalDeviceName = this.getOne(queryWrapperName);
                if (meteorologicalDeviceName != null && !meteorologicalDeviceName.getId().equals(id)) {
                    logger.info("设备名称 {} 已存在,请重新输入", name);
                    return result.success("设备名称已存在,请重新输入", 1);
                }
            }
            if (StringUtils.isNotBlank(num)) {
                LambdaQueryWrapper<MeteorologicalDevice> queryWrapperNum = new LambdaQueryWrapper<MeteorologicalDevice>();
                queryWrapperNum.eq(MeteorologicalDevice::getNum, num);
                queryWrapperNum.last("LIMIT 1");
                MeteorologicalDevice meteorologicalDeviceNum = this.getOne(queryWrapperNum);
                if (meteorologicalDeviceNum != null && !meteorologicalDeviceNum.getId().equals(id)) {
                    logger.info("设备编号 {} 已存在,请重新输入", name);
                    return result.success("设备编号已存在,请重新输入", 2);
                }
            }
        } else {
            //如果不存在id，则为新增
            if (StringUtils.isNotBlank(name)) {
                LambdaQueryWrapper<MeteorologicalDevice> queryWrapperName = new LambdaQueryWrapper<MeteorologicalDevice>();
                queryWrapperName.eq(MeteorologicalDevice::getName, name);
                queryWrapperName.last("LIMIT 1");
                MeteorologicalDevice meteorologicalDeviceName = this.getOne(queryWrapperName);
                if (meteorologicalDeviceName != null) {
                    logger.info("设备名称 {} 已存在,请重新输入", name);
                    return result.success("设备名称已存在,请重新输入", 1);
                }
            }
            if (StringUtils.isNotBlank(num)) {
                LambdaQueryWrapper<MeteorologicalDevice> queryWrapperNum = new LambdaQueryWrapper<MeteorologicalDevice>();
                queryWrapperNum.eq(MeteorologicalDevice::getNum, num);
                queryWrapperNum.last("LIMIT 1");
                MeteorologicalDevice meteorologicalDeviceNum = this.getOne(queryWrapperNum);
                if (meteorologicalDeviceNum != null) {
                    logger.info("设备编号 {} 已存在,请重新输入", name);
                    return result.success("设备编号已存在,请重新输入", 2);
                }
            }
        }

        return result.success("唯一性校验通过", 0);
    }

    @Override
    public Result getDevicePulldownList(HttpServletRequest request) {
        logger.info("获取气象设备下拉列表");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        Result result = new Result();
        EmMeteorologicalDeviceQueryObject qo = new EmMeteorologicalDeviceQueryObject();
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        Page<EmRespDeviceAndLampPostVO> page = new Page<>(0, 9999);
        IPage<EmRespDeviceAndLampPostVO> resultVOPage = meteorologicalDeviceDao.getPageList(page, qo);
        return result.success(resultVOPage.getRecords());
    }

    @Override
    public Result statis(EmReqStatisParamVO vo, HttpServletRequest request) {
        logger.info("根据查询日期和查询气象信息类型统计当天气象数据,接收参数： {}", vo);
        Result result = new Result();
        String queryDate = vo.getQueryDate();
        String queryType = vo.getQueryType();
        List<Float> datalist = new ArrayList<Float>();

        if (StringUtils.isNotBlank(queryDate) && StringUtils.isNotBlank(queryType)) {
            Map<String, Float> valueMap = new HashMap<>();
            Calendar c = Calendar.getInstance();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                c.setTime(sdf.parse(queryDate));
            } catch (ParseException e) {
                logger.error("转换时间错误,errMsg={}", e.getMessage());
                return result.error("时间格式错误");
            }
            //设置开始时间
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            vo.setBeginTime(c.getTime());
            //设置结束时间
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            vo.setEndTime(c.getTime());
            List<Map<String, Object>> statisMaps = meteorologicalHistoryDao.statis2(vo);
            if (statisMaps != null && !statisMaps.isEmpty()) {
                for (Map<String, Object> statisMap : statisMaps) {
                    String hour = statisMap.get("hour").toString();
                    Float value = Float.valueOf(statisMap.get("value").toString());
                    valueMap.put(hour, value);
                }
            }
            for (int i = 0; i < 24; i++) {
                String hourStr = i < 10 ? "0" + i : "" + i;
                datalist.add(valueMap.getOrDefault(hourStr, 0F));
            }
        }
        return result.success(datalist);
    }

    @Override
    public Result getInfoByDevice() {
        logger.info("获取所有设备的实时气象信息并保存至数据库");
        List<MeteorologicalDevice> deviceList = meteorologicalDeviceDao.selectList(null);
        Result result = new Result();
        if (deviceList != null) {
            //循环获取每个气象设备的实时数据并保存至数据库
            for (MeteorologicalDevice device : deviceList) {
                String ip = device.getIp();
                Integer port = device.getPort();
                //命令：0R0，获取气象设备实时气象数据
                byte[] bytes = {(byte) 0x30, (byte) 0x52, (byte) 0x30, (byte) 0x0D, (byte) 0x0A};
                byte[] receiveData = null;
                int j = 0;
                for (int i = 0; i < 3; i++) {
                    logger.info("连接次数:" + (i + 1));
                    receiveData = SocketClient.sendReturn(ip, port, bytes);
                    j++;
                    if (receiveData != null) {
                        device.setNetworkState(1);
                        device.setLastOnlineTime(new Date());
                        meteorologicalDeviceDao.updateById(device);
                        logger.info("获取气象设备 {} 实时数据成功 receiveData = {}", device.getNum(), receiveData);

                        //保存气象数据至历史数据表
                        MeteorologicalHistory history = byteToMeteorologicalHistory(receiveData);
                        history.setCreateTime(new Date());
                        history.setDeviceId(device.getId());
                        meteorologicalHistoryDao.insert(history);
                        break;
                    }
                    if (j == 3) {
                        device.setNetworkState(0);
                        //device.setLastOnlineTime(new Date());
                        meteorologicalDeviceDao.updateById(device);
                        logger.info("获取气象设备 {} 实时数据失败,socket连接到网关失败或者网关应答超时", device.getNum());
                    }
                }

            }
        } else {
            return result.error("新增失败，没有气象设备");
        }

        return result.success("新增气象设备历史数据成功");
    }

    @Override
    public Result getStatusByDevice() {
        List<MeteorologicalDevice> deviceList = meteorologicalDeviceDao.selectList(null);
        Result result = new Result();
        if (deviceList != null) {
            //循环获取每个气象设备的实时数据并保存至数据库
            for (MeteorologicalDevice device : deviceList) {
                String ip = device.getIp();
                Integer port = device.getPort();
                //命令：0R0，获取气象设备实时气象数据
                byte[] bytes = {(byte) 0x30, (byte) 0x52, (byte) 0x30, (byte) 0x0D, (byte) 0x0A};
                byte[] receiveData = null;
                int j = 0;
                for (int i = 0; i < 3; i++) {
                    logger.info("连接次数:" + (i + 1));
                    receiveData = SocketClient.sendReturn(ip, port, bytes);
                    j++;
                    if (receiveData != null) {
                        device.setNetworkState(1);
                        device.setLastOnlineTime(new Date());
                        meteorologicalDeviceDao.updateById(device);
                        logger.info("获取气象设备 {} 在线数据成功 receiveData = {}", device.getNum(), receiveData);

                        MeteorologicalReal deviceReal = new MeteorologicalReal();
                        MeteorologicalHistory deviceHistory = byteToMeteorologicalHistory(receiveData);
                        BeanUtils.copyProperties(deviceHistory, deviceReal);

                        /*deviceReal.setPm25(100f);
                        deviceReal.setNoiseAverage(80f);
                        deviceReal.setWindSpeedAverage(21f);

                        deviceReal.setTemperature(32F);*/
                        JSONObject realAlarmInfo = getRealAlarmInfo(deviceReal);
                        if (realAlarmInfo != null) {
                            JSONObject resData = new JSONObject(true);
                            resData.put("type", alarmTopic);
                            realAlarmInfo.put("deviceId", device.getId());
                            realAlarmInfo.put("lampPostId", device.getLampPostId());
                            resData.put("data", realAlarmInfo);
                            KafkaMessage kafkaMessage = new KafkaMessage();
                            kafkaMessage.setType(1);
                            kafkaMessage.setMessage(resData.toJSONString());
                            kafkaMessage.setIs2All(2);
                            kafkaMessage.setUserIds(null);
                            kafkaMessageService.sendMessage("websocket1", kafkaMessage);
                            //发送告警信息到前台
                            //GatewayService.sendMsg(alarmTopic,realAlarmInfo.toJSONString());
                        }
                        LambdaQueryWrapper<MeteorologicalReal> queryWrapper = new LambdaQueryWrapper<MeteorologicalReal>();
                        queryWrapper.eq(MeteorologicalReal::getDeviceId, device.getId());
                        MeteorologicalReal real = meteorologicalRealDao.selectOne(queryWrapper);
                        //实时数据库不存在此气象设备的实时数据则添加，存在则修改
                        if (real != null) {
                            LambdaUpdateWrapper<MeteorologicalReal> updateWrapper = new LambdaUpdateWrapper<MeteorologicalReal>();
                            updateWrapper.eq(MeteorologicalReal::getDeviceId, device.getId());
                            updateWrapper.set(MeteorologicalReal::getCreateTime, new Date());
                            meteorologicalRealDao.update(deviceReal, updateWrapper);
                        } else {
                            deviceReal.setCreateTime(new Date());
                            deviceReal.setDeviceId(device.getId());
                            meteorologicalRealDao.insert(deviceReal);
                        }
                        break;
                    }
                    if (j == 3) {
                        device.setNetworkState(0);
                        //device.setLastOnlineTime(new Date());
                        meteorologicalDeviceDao.updateById(device);
                        logger.info("获取气象设备 {} 在线数据失败,socket连接到网关失败或者网关应答超时", device.getNum());
                    }
                }
            }
        } else {
            return result.error("获取气象设备在线数据失败，没有气象设备");
        }
        return result.success("获取气象设备在线数据成功");
    }

    /**
     * 根据实时数据获取告警信息
     *
     * @param real
     * @return
     */
    private JSONObject getRealAlarmInfo(MeteorologicalReal real) {
        JSONObject alarmInfo = new JSONObject(true);
        if (real == null) {
            return null;
        }
        if (!alarmConfig.isEnable()) {
            return null;
        }
        //超过30度
        alarmInfo.put("temperature", real.getTemperature() != null && real.getTemperature() >= alarmConfig.getTemperature() ? real.getTemperature() : null);
        //超过75，提醒轻度污染，轻度：75~115，中度：115~150，重度：150~250，严重：大于50及以上
        alarmInfo.put("pm25", real.getPm25() != null && real.getPm25() >= alarmConfig.getPm25() ? real.getPm25() : null);
        //超过60
        alarmInfo.put("noise", real.getNoiseAverage() != null && real.getNoiseAverage() >= alarmConfig.getNoise() ? real.getNoiseAverage() : null);
        //风速超过17.2
        alarmInfo.put("windSpeed", real.getWindSpeedAverage() != null && real.getWindSpeedAverage() >= alarmConfig.getWindSpeed() ? real.getWindSpeedAverage() : null);
        if (alarmInfo.get("temperature") == null && alarmInfo.get("pm25") == null && alarmInfo.get("noise") == null && alarmInfo.get("windSpeed") == null) {
            return null;
        }
        return alarmInfo;
    }

    @Override
    public Result getReal(Integer deviceId) {
        Result result = new Result();
        QueryWrapper<MeteorologicalReal> queryWrapper = new QueryWrapper<MeteorologicalReal>();
        if (deviceId != null) {
            queryWrapper.eq("device_id", deviceId);
        }
        List<MeteorologicalReal> list = meteorologicalRealDao.selectList(queryWrapper);
        List<EmRespMeteorologicalRealVO> vos = new ArrayList<EmRespMeteorologicalRealVO>();
        for (MeteorologicalReal real : list) {
            EmRespMeteorologicalRealVO vo = new EmRespMeteorologicalRealVO();
            BeanUtils.copyProperties(real, vo);
            vo.setWindDirectionMaxString(getDirection(real.getWindDirectionMax(), real.getWindSpeedMax()));
            vo.setWindDirectionAverageString(getDirection(real.getWindDirectionAverage(), real.getWindSpeedMax()));
            vo.setWindDirectionMinString(getDirection(real.getWindDirectionMin(), real.getWindSpeedMax()));
            vo.setAirQuality(getAirQualityInt(real.getPm25()));
            vos.add(vo);
        }

        return result.success(vos);
    }

    @Override
    public Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request) {
        LambdaQueryWrapper<MeteorologicalDevice> wrapper = new LambdaQueryWrapper();
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            wrapper.in(MeteorologicalDevice::getLampPostId, lampPostIdList);
        }
        List<MeteorologicalDevice> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result pulldownByLampPost2(List<Integer> lampPostIdList, HttpServletRequest request) {
        List<MeteorologicalDevice> list = new ArrayList<MeteorologicalDevice>();
        Result result = new Result();
        for (Integer lampPostId : lampPostIdList) {
            LambdaQueryWrapper<MeteorologicalDevice> queryWrapper = new LambdaQueryWrapper<MeteorologicalDevice>();
            queryWrapper.eq(MeteorologicalDevice::getLampPostId, lampPostId);
            List<MeteorologicalDevice> MeteorologicalDeviceList = meteorologicalDeviceDao.selectList(queryWrapper);
            if (MeteorologicalDeviceList.size() > 0) {
                list.addAll(MeteorologicalDeviceList);
            }
        }
        List<EmRespDeviceAndLampPostVO> vos = new ArrayList<EmRespDeviceAndLampPostVO>();
        for (MeteorologicalDevice device : list) {
            EmRespDeviceAndLampPostVO vo = new EmRespDeviceAndLampPostVO();
            try {
                // 获取灯杆信息
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("token", request.getHeader("token"));
                JSONObject slLampPostResult = JSON.parseObject(HttpUtil.get(httpApi.getDlm().get("url") + httpApi.getDlm().get("lampPostById") + device.getLampPostId(), headerMap));
                JSONObject slLampPostResultObj = slLampPostResult.getJSONObject("data");
                SlLampPost slLampPost = JSONObject.toJavaObject(slLampPostResultObj, SlLampPost.class);
                BeanUtils.copyProperties(device, vo);
                vo.setLampPostName(slLampPost.getName());
                vo.setLampPostNum(slLampPost.getNum());
                vo.setLongitude(slLampPost.getLongitude());
                vo.setLatitude(slLampPost.getLatitude());
                vos.add(vo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.success(vos);
    }

    /**
     * 气象设备协议数据转MeteorologicalHistory（气象历史数据）对象
     *
     * @param receiveData
     * @return
     */
    @Override
    public MeteorologicalHistory byteToMeteorologicalHistory(byte[] receiveData) {
        String receiveStr = new String(receiveData);
        logger.info("接收设备返回的数据：receiveStr={}", receiveStr.toString());
        //返回数据处理
        MeteorologicalHistory meteorologicalHistory = new MeteorologicalHistory();
        Map<String, String> receiveMap = new HashMap<String, String>();
        String[] strs = receiveStr.toString().split(",");
        for (int i = 1; i < strs.length; i++) {
            String[] ss = strs[i].split("=");
            receiveMap.put(ss[0], ss[1]);
        }
        //最小风向
        meteorologicalHistory.setWindDirectionMin(Integer.parseInt(getNumber(receiveMap.get("Dn"))));
        //平均风向
        meteorologicalHistory.setWindDirectionAverage(Integer.parseInt(getNumber(receiveMap.get("Dm"))));
        //最大风向
        meteorologicalHistory.setWindDirectionMax(Integer.parseInt(getNumber(receiveMap.get("Dx"))));
        //最小风速
        meteorologicalHistory.setWindSpeedMin(Float.parseFloat(getNumber(receiveMap.get("Sn"))));
        //平均风速
        meteorologicalHistory.setWindSpeedAverage(Float.parseFloat(getNumber(receiveMap.get("Sm"))));
        //最大风速
        meteorologicalHistory.setWindSpeedMax(Float.parseFloat(getNumber(receiveMap.get("Sx"))));
        //温度
        meteorologicalHistory.setTemperature(Float.parseFloat(getNumber(receiveMap.get("Ta"))));
        //湿度
        meteorologicalHistory.setHumidity(Float.parseFloat(getNumber(receiveMap.get("Ua"))));
        //气压
        meteorologicalHistory.setAirPressure(Float.parseFloat(getNumber(receiveMap.get("Pa"))));
        //最大噪声
        meteorologicalHistory.setNoiseMax(Float.parseFloat(getNumber(receiveMap.get("NX"))));
        //最小噪声
        meteorologicalHistory.setNoiseMin(Float.parseFloat(getNumber(receiveMap.get("NI"))));
        //平均噪声
        meteorologicalHistory.setNoiseAverage(Float.parseFloat(getNumber(receiveMap.get("NS"))));
        //PM2.5颗粒物浓度
        meteorologicalHistory.setPm25(Float.parseFloat(getNumber(receiveMap.get("PM2.5"))));
        //PM10颗粒物浓度
        meteorologicalHistory.setPm10(Float.parseFloat(getNumber(receiveMap.get("PM10"))));

        return meteorologicalHistory;
    }

    @Override
    public void sendIrSubtitle(Integer id) {
        logger.info("发送显示屏字幕,传感器设备id：{}", id);
        if(isPlayAppli==0){
            return;
        }
        // 获取传感器关联显示屏显示数据设置
        LambdaQueryWrapper<ScreenSubtitleEm> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ScreenSubtitleEm::getEmDeviceId, id);
        ScreenSubtitleEm screenSubtitleEm = screenSubtitleEmService.getOne(queryWrapper);
        // 传感器关联显示屏集合
        LambdaQueryWrapper<ScreenSubtitleEmScreen> listLambdaWrapper = new LambdaQueryWrapper<>();
        listLambdaWrapper.eq(ScreenSubtitleEmScreen::getScreenSubtitleEmId, screenSubtitleEm.getId());
        List<ScreenSubtitleEmScreen> screenSubtitleEmScreenList = screenSubtitleEmScreenService.list(listLambdaWrapper);
        // 显示屏
        List<Integer> screenDeviceIdList = screenSubtitleEmScreenList.stream().map(ScreenSubtitleEmScreen::getScreenDeviceId).distinct().collect(Collectors.toList());
        List<String> screenDeviceNumList = new ArrayList<>();
        if(screenDeviceIdList.size() > 0){
            screenDeviceNumList = screenSubtitleEmScreenService.selectScreenDeviceNumList(screenDeviceIdList);
        }
        // 传感器设备对象
        MeteorologicalDevice meteorologicalDevice = meteorologicalDeviceDao.selectById(id);
        if(screenSubtitleEm.getEmDataField() != null && !"".equals(screenSubtitleEm.getEmDataField()) && screenDeviceIdList.size() >0){
            Result real = this.getReal(id);
            if(real != null){
                // 传感器数据
                List<EmRespMeteorologicalRealVO> data = (List<EmRespMeteorologicalRealVO>)real.getData();
                if(data != null && data.size() > 0 && data.get(0) != null){
                    EmRespMeteorologicalRealVO emRespMeteorologicalRealVO = data.get(0);
                    String emDataField = screenSubtitleEm.getEmDataField();
                    String[] split = emDataField.split(",");
                    // 获取字幕显示字符串
                    String prototype = "";
                    for(int i = 0;i<split.length;i++){
                        if(split[i].equals("temperature")){
                            prototype += "温度：" + emRespMeteorologicalRealVO.getTemperature() + "℃";
                        }
                        if(split[i].equals("humidity")){
                            prototype += "湿度：" + emRespMeteorologicalRealVO.getHumidity() + "%";
                        }
                        if(split[i].equals("airPressure")){
                            prototype += "气压：" + emRespMeteorologicalRealVO.getAirPressure() + "pha";
                        }
                        if(split[i].equals("windSpeedAverage")){
                            prototype += "风速：" + emRespMeteorologicalRealVO.getWindSpeedAverage() + "m/s";
                        }
                        if(split[i].equals("windDirectionAverage")){
                            String direction = getDirection(emRespMeteorologicalRealVO.getWindDirectionAverage(), emRespMeteorologicalRealVO.getWindSpeedMax());
                            prototype += "风向：" + direction;
                        }
                        if(split[i].equals("pm25")){
                            prototype += "PM2.5：" + emRespMeteorologicalRealVO.getPm25() + "ug/m3";
                        }
                        if(split[i].equals("pm10")){
                            prototype += "PM10：" + emRespMeteorologicalRealVO.getPm10() + "ug/m3";
                        }
                        if(split[i].equals("noiseAverage")){
                            prototype += "噪音：" + emRespMeteorologicalRealVO.getNoiseAverage() + "db";
                        }
                        if((i + 1) != split.length){
                            prototype += "，";
                        }
                    }
                    // 数据
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("isAll", 0);
                    jsonObject.put("num", -1);
                    jsonObject.put("align", screenSubtitleEm.getAlign());
                    jsonObject.put("direction", screenSubtitleEm.getDirection());
                    jsonObject.put("html", "<span style='background:" + screenSubtitleEm.getBackgroundColor() + ";color:" + screenSubtitleEm.getTypefaceColor() + ";font-size: "+screenSubtitleEm.getTypefaceSize()+"px;'>" + prototype + "</span>");
                    jsonObject.put("prototype", prototype);
                    jsonObject.put("interval", screenSubtitleEm.getStepInterval());
                    jsonObject.put("step", screenSubtitleEm.getStep());
                    jsonObject.put("numList", screenDeviceNumList);
                    jsonObject.put("fontSize", screenSubtitleEm.getTypefaceSize());
                    logger.info("发送到显示屏数据：{}",  jsonObject);
                    String url = httpApi.getIr().get("url") + httpApi.getIr().get("sendSubtitle");
                    BasicHeader basicHeader = new BasicHeader("Content-Type","application/json;charset=UTF-8");
                    String jsonResult = httpConnectionPools.post(url, jsonObject, basicHeader);
                    logger.info("显示屏返回：{}"+jsonResult);
                }
            }
        }

//        if(emDeviceIdAppli != null && !"".equals(emDeviceIdAppli)){
//            String[] split = emDeviceIdAppli.split(",");
//            int i = 0;
//            for(;i<split.length;i++){
//                if(String.valueOf(id).equals(split[i])){
//                    break;
//                }
//            }
//            logger.info("12121212121:{}", i);
//            if(i<split.length){
//                String[] split1 = screenDeviceNumAppli.split("/");
//                String screenDeviceNums = split1[i];
//                String[] screenDeviceNumArr = screenDeviceNums.split(",");
//                Result real = this.getReal(id);
//                if(real != null){
//                    List<EmRespMeteorologicalRealVO> data = (List<EmRespMeteorologicalRealVO>)real.getData();
//                    if(data != null && data.size() > 0 && data.get(0) != null){
//                        List<String> numList = new ArrayList<>();
//                        for(int x = 0;x<screenDeviceNumArr.length;x++){
//                            numList.add(screenDeviceNumArr[x]);
//                        }
//                        EmRespMeteorologicalRealVO emRespMeteorologicalRealVO = data.get(0);
//                        String direction = getDirection(emRespMeteorologicalRealVO.getWindDirectionAverage(), emRespMeteorologicalRealVO.getWindSpeedMax());
//                        String prototype = "温度："+emRespMeteorologicalRealVO.getTemperature()+"℃，湿度："
//                                +emRespMeteorologicalRealVO.getHumidity()+"%，气压："+emRespMeteorologicalRealVO.getAirPressure()
//                                +"pha，PM2.5："+emRespMeteorologicalRealVO.getPm25()+"ug/m3，PM10："
//                                +emRespMeteorologicalRealVO.getPm10()+"ug/m3，噪音："+emRespMeteorologicalRealVO.getNoiseAverage()+"db，风向："
//                                +direction+"，风速："+emRespMeteorologicalRealVO.getWindSpeedAverage()+"m/s";
//                        // 数据
//                        // 发送数据至显示屏
//                        IrReqScreenSubtitlePlayVO irReqScreenSubtitlePlayVO = new IrReqScreenSubtitlePlayVO();
//                        irReqScreenSubtitlePlayVO.setIsAll(0);
//                        irReqScreenSubtitlePlayVO.setNum(-1);
//                        irReqScreenSubtitlePlayVO.setAlign("top");
//                        irReqScreenSubtitlePlayVO.setDirection("left");
//                        irReqScreenSubtitlePlayVO.setHtml("<i style='background:#000;color:#FFF'>" + prototype + "</i>");
//                        irReqScreenSubtitlePlayVO.setPrototype("阿US好嗲US和");
//                        irReqScreenSubtitlePlayVO.setInterval(50);
//                        irReqScreenSubtitlePlayVO.setStep(1);
//                        irReqScreenSubtitlePlayVO.setNumList(numList);
//
//                        Map<String,String> headMap = new HashMap<>();
//                        headMap.put("Content-Type","application/json;charset=UTF-8");
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("isAll",0);
//                        jsonObject.put("num",-1);
//                        jsonObject.put("align","top");
//                        jsonObject.put("direction","left");
//                        jsonObject.put("html","<span style='background:#000;color:#FF0000;font-size: "+fontSize+"px;'>" + prototype + "</span>");
//                        jsonObject.put("prototype", prototype);
//                        jsonObject.put("interval",interval);
//                        jsonObject.put("step",step);
//                        jsonObject.put("numList",numList);
//                        jsonObject.put("fontSize",fontSize);
//                        logger.info("11111111发送到显示屏数据：{}",jsonObject);
//                        String url = httpApi.getIr().get("url") + httpApi.getIr().get("sendSubtitle");
//
//                        BasicHeader basicHeader = new BasicHeader("Content-Type","application/json;charset=UTF-8");
//
//                        String jsonResult = httpConnectionPools.post(url, jsonObject, basicHeader);
//                        System.out.println("2222222222显示屏返回：{}"+jsonResult);
//
////                        logger.info("11111111发送到显示屏数据：{}",jsonObject);
////                        JSONObject registerResult = JSON.parseObject(HttpUtil.post(httpApi.getIr().get("url") + httpApi.getIr().get("sendSubtitle"), jsonObject.toJSONString(), headMap));
////                        logger.info("2222222222显示屏返回：{}",registerResult);
//                    }
//                }
//            }
//
//        }
    }

    /**
     * 获取字符串中的数字字符
     *
     * @param str
     * @return
     */
    public String getNumber(String str) {
        // 需要取整数和小数的字符串
        // 控制正则表达式的匹配行为的参数(小数)
        Pattern p = Pattern.compile("(\\d+\\.\\d+)");
        //Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence input)方法得到该类的实例.
        Matcher m = p.matcher(str);
        //m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
        if (m.find()) {
            //如果有相匹配的,则判断是否为null操作
            //group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
            str = m.group(1) == null ? "" : m.group(1);
        } else {
            //如果匹配不到小数，就进行整数匹配
            p = Pattern.compile("(\\d+)");
            m = p.matcher(str);
            if (m.find()) {
                //如果有整数相匹配
                str = m.group(1) == null ? "" : m.group(1);
            } else {
                //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
                str = "";
            }
        }
        return str;
    }

    /**
     * 获取风向
     *
     * @param directionNum 风向数值
     * @param windSpeedMax 最大风速
     * @return
     */
    public String getDirection(float directionNum, float windSpeedMax) {
        String direction = null;
        int num = (int) Math.ceil(directionNum / 22.5);
        if (num == 16 || num == 1) {
            direction = "北";
        } else if (num == 2 || num == 3) {
            direction = "东北";
        } else if (num == 4 || num == 5) {
            direction = "东";
        } else if (num == 6 || num == 7) {
            direction = "东南";
        } else if (num == 8 || num == 9) {
            direction = "南";
        } else if (num == 10 || num == 11) {
            direction = "西南";
        } else if (num == 12 || num == 13) {
            direction = "西";
        } else if (num == 14 || num == 15) {
            direction = "西北";
        } else if (num == 0) {
            if (windSpeedMax > 0) {
                direction = "北";
            } else {
                direction = "无";
            }
        }
        return direction;
    }

    /**
     * 获取空气质量
     *
     * @param pm25
     * @return
     */
    public String getAirQuality(Float pm25) {
        String airQuality = "优";
        if (pm25 < 35) {
            airQuality = "优";
        } else if (pm25 >= 35 && pm25 < 75) {
            airQuality = "良";
        } else if (pm25 >= 75 && pm25 < 115) {
            airQuality = "轻度污染";
        } else if (pm25 >= 115 && pm25 < 150) {
            airQuality = "中度污染";
        } else if (pm25 >= 150 && pm25 < 250) {
            airQuality = "重度污染";
        } else if (pm25 >= 250) {
            airQuality = "严重污染";
        }

        return airQuality;
    }

    /**
     * 获取空气质量
     *
     * @param pm25
     * @return
     */
    public String getAirQualityInt(Float pm25) {
        String airQuality = "1";
        if (pm25 < 35) {
            airQuality = "1";
        } else if (pm25 >= 35 && pm25 < 75) {
            airQuality = "2";
        } else if (pm25 >= 75 && pm25 < 115) {
            airQuality = "3";
        } else if (pm25 >= 115 && pm25 < 150) {
            airQuality = "4";
        } else if (pm25 >= 150 && pm25 < 250) {
            airQuality = "5";
        } else if (pm25 >= 250) {
            airQuality = "6";
        }

        return airQuality;
    }
}