/**
 * @filename:ScreenDeviceServiceImpl 2020-03-17
 * @project ir  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.ir.config.parameter.ScreenApi;
import com.exc.street.light.ir.mapper.ScreenDeviceDao;
import com.exc.street.light.ir.service.ScreenDeviceService;
import com.exc.street.light.ir.service.ScreenPlayDeviceService;
import com.exc.street.light.ir.utils.ScreenControlUtil;
import com.exc.street.light.ir.utils.ScreenSendUtil;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.entity.ir.ScreenPlayDevice;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.IrScreenDeviceQuery;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.req.IrReqScreenOperateVO;
import com.exc.street.light.resource.vo.resp.IrRespScreenDeviceVO;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class ScreenDeviceServiceImpl extends ServiceImpl<ScreenDeviceDao, ScreenDevice> implements ScreenDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(ScreenDeviceServiceImpl.class);

    @Autowired
    private ScreenApi screenApi;
    @Autowired
    private ScreenPlayDeviceService screenPlayDeviceService;
    @Autowired
    private ScreenDeviceDao screenDeviceDao;

    @Value("${screen.port}")
    private String serverPort;

    @Value("${screen.ip}")
    private String serverIp;
    
    @Autowired
    private LogUserService userService;

    @Override
    public Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request) {
        logger.info("查询显示屏(根据灯杆id集合)，接收参数：{}", lampPostIdList);
        LambdaQueryWrapper<ScreenDevice> wrapper = new LambdaQueryWrapper();
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            wrapper.in(ScreenDevice::getLampPostId, lampPostIdList);
        }
        List<ScreenDevice> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result add(ScreenDevice screenDevice, HttpServletRequest request) {
        logger.info("添加显示屏，接收参数：{}", screenDevice);
        Result result = new Result();
        Result unique = this.unique(screenDevice, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            screenDevice.setCreateTime(new Date());
            boolean rsg = this.save(screenDevice);
            if (rsg) {
                result.success("添加成功");
            } else {
                result.error("添加失败！");
            }
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result operate(IrReqScreenOperateVO irReqScreenOperateVO, HttpServletRequest httpServletRequest) {
        logger.info("显示屏更多操作，屏幕开关，设置音量，设置亮度，接收参数：{}", irReqScreenOperateVO);
        Result result = new Result();
        // 获取设备集合
        List<ScreenDevice> screenDeviceList = new ArrayList<>();
        if (irReqScreenOperateVO.getIsAll() == 1) {
            screenDeviceList = this.list();
        } else {
            if (irReqScreenOperateVO.getNumList() == null || irReqScreenOperateVO.getNumList().size() == 0) {
                return result.error("请选择显示屏设备");
            }
            LambdaQueryWrapper<ScreenDevice> wrapper = new LambdaQueryWrapper();
            wrapper.in(ScreenDevice::getNum, irReqScreenOperateVO.getNumList());
            screenDeviceList = this.list(wrapper);
        }
        if (screenDeviceList == null || screenDeviceList.size() == 0) {
            return result.error("显示屏设备不存在");
        }
        // 获取设备编号集合
        List<String> deviceNumList = screenDeviceList.stream().map(ScreenDevice::getNum).distinct().collect(Collectors.toList());
        // 返回对象
        JSONObject jsonObject = new JSONObject();
        int i = 0;
        // 控制开关
        int screenOpenCount = 0;
        irReqScreenOperateVO.setState(1);
        Result screenOpenControl = ScreenControlUtil.screenOpenControl(screenApi.getIp(), screenApi.getPort(), irReqScreenOperateVO, deviceNumList);
        jsonObject.put("screenOpenControl", screenOpenControl.getData());
        Object defaultNum1 = ((JSONObject) screenOpenControl.getData()).get("defaultNum");
        if (defaultNum1 != null) {
            screenOpenCount = (int) defaultNum1;
        }
        // 控制声音
        int volumeCount = 0;
        if (irReqScreenOperateVO.getVolume() != null) {
            Result volumeControl = ScreenControlUtil.volumeControl(screenApi.getIp(), screenApi.getPort(), irReqScreenOperateVO, deviceNumList);
            jsonObject.put("volumeControl", volumeControl.getData());
            Object defaultNum = ((JSONObject) volumeControl.getData()).get("defaultNum");
            if (defaultNum != null) {
                volumeCount = (int) defaultNum;
            }
            i++;
        }
        // 控制亮度
        int brightnessCount = 0;
        if (irReqScreenOperateVO.getBrightness() != null) {
            Result brightnessControl = ScreenControlUtil.brightnessControl(screenApi.getIp(), screenApi.getPort(), irReqScreenOperateVO, deviceNumList);
            jsonObject.put("brightnessControl", brightnessControl.getData());
            Object defaultNum = ((JSONObject) brightnessControl.getData()).get("defaultNum");
            if (defaultNum != null) {
                brightnessCount = (int) defaultNum;
            }
            i++;
        }
        if (screenOpenCount + volumeCount + brightnessCount == deviceNumList.size() * i) {
            return result.error("显示屏控制失败");
        } else if (screenOpenCount + volumeCount + brightnessCount > 0) {
            return result.error("显示屏控制部分失败");
        }
        return result.success(jsonObject);
    }

    @Override
    public Result batchDelete(String ids, HttpServletRequest request) {
        logger.info("批量删除显示屏设备，接收参数：{}", ids);
        List<Integer> idListFromString = StringConversionUtil.getIdListFromString(ids);
        this.removeByIds(idListFromString);
        Result result = new Result();
        return result.success("批量删除成功");
    }

    @Override
    public Result updateDevice(ScreenDevice screenDevice, HttpServletRequest request) {
        logger.info("修改显示屏设备，接收参数：{}", screenDevice);
        Result result = new Result();
        Result unique = this.unique(screenDevice, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            boolean rsg = this.updateById(screenDevice);
            if (rsg) {
                result.success("修改成功");
            } else {
                result.error("修改失败！");
            }
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result getQuery(IrScreenDeviceQuery irScreenDeviceQuery, HttpServletRequest httpServletRequest) {
        logger.info("获取显示屏设备列表(分页查询),接收参数：{}", irScreenDeviceQuery);
//        this.refresh(null);
        
     // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
        	irScreenDeviceQuery.setAreaId(user.getAreaId());
        }
        // 获取显示屏设备列表
        IPage<IrScreenDeviceQuery> page = new Page<IrScreenDeviceQuery>(irScreenDeviceQuery.getPageNum(), irScreenDeviceQuery.getPageSize());
        IPage<ScreenDevice> dlmRespGroupVOList = this.baseMapper.query(page, irScreenDeviceQuery);
        // 刷新
//        List<ScreenDevice> records = dlmRespGroupVOList.getRecords();
//        List<Integer> collect = records.stream().map(ScreenDevice::getId).collect(Collectors.toList());
//        this.refresh(collect);
        Result result = new Result();
        return result.success(dlmRespGroupVOList);
    }

    @Override
    public Result unique(ScreenDevice screenDevice, HttpServletRequest request) {
        logger.info("显示屏设备验证唯一性，接收参数：{}", screenDevice);
        Result result = new Result();
        if (null != screenDevice) {
            if (screenDevice.getId() != null) {
                if (screenDevice.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<ScreenDevice> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(ScreenDevice::getName, screenDevice.getName());
                    ScreenDevice screenDeviceByName = this.getOne(wrapperName);
                    if (screenDeviceByName != null && !screenDeviceByName.getId().equals(screenDevice.getId())) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
                if (screenDevice.getNum() != null) {
                    // 验证编号是否重复
                    LambdaQueryWrapper<ScreenDevice> wrapperNum = new LambdaQueryWrapper();
                    wrapperNum.eq(ScreenDevice::getNum, screenDevice.getNum());
                    ScreenDevice screenDeviceByNum = this.getOne(wrapperNum);
                    if (screenDeviceByNum != null && !screenDeviceByNum.getId().equals(screenDevice.getId())) {
                        return result.error("编号已存在");
                    }
                } else {
                    return result.error("编号不能为空");
                }
            } else {
                if (screenDevice.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<ScreenDevice> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(ScreenDevice::getName, screenDevice.getName());
                    ScreenDevice screenDeviceByName = this.getOne(wrapperName);
                    if (screenDeviceByName != null) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
                if (screenDevice.getNum() != null) {
                    // 验证编号是否重复
                    LambdaQueryWrapper<ScreenDevice> wrapperNum = new LambdaQueryWrapper();
                    wrapperNum.eq(ScreenDevice::getNum, screenDevice.getNum());
                    ScreenDevice screenDeviceByNum = this.getOne(wrapperNum);
                    if (screenDeviceByNum != null) {
                        return result.error("编号已存在");
                    }
                } else {
                    return result.error("编号不能为空");
                }
            }
        } else {
            return result.error("接收参数为空");
        }
        return result.success("");
    }

    @Override
    public Result detail(Integer id, HttpServletRequest request) {
        logger.info("获取显示屏详情，接收参数：{}", id);
        IrRespScreenDeviceVO irRespScreenDeviceVO = this.baseMapper.detail(id);
        Result result = new Result();
        return result.success(irRespScreenDeviceVO);
    }

    @Override
    public Result refresh(List<Integer> screenDeviceIdList) {
        Result result = new Result();
        Result refreshDate = getRefreshDate(screenDeviceIdList);
        List<ScreenDevice> screenInfoList = (List<ScreenDevice>) refreshDate.getData();
        if (screenInfoList == null) {
            return result.error("没有显示屏设备");
        }
        this.updateBatchById(screenInfoList);
        return result.success("刷新成功");
    }

    @Override
    public Result getRefreshDate(List<Integer> screenDeviceIdList) {
        logger.info("刷新显示屏接收数据：{}", screenDeviceIdList);
        // 如果没有指定的显示屏编号则刷新全部
        // 获取显示屏编号对应的序列号
        Result result = new Result();
        // 根据序列号列表查询获取显示屏参数集合
        List<ScreenDevice> screenInfoData;
        if (screenDeviceIdList == null || screenDeviceIdList.size() == 0) {
            screenInfoData = this.list();
        } else {
            screenInfoData = (List) this.listByIds(screenDeviceIdList);
        }
        // 显示屏编号集合
        List<String> snList = screenInfoData.stream().map(ScreenDevice::getNum).collect(Collectors.toList());
        if (snList == null || snList.size() == 0) {
            return result.error("目标显示屏编号无效");
        }
        // 获取各个字段数据
        Result getNetworkTypeControl = ScreenControlUtil.getNetworkTypeControl(screenApi.getIp(), screenApi.getPort(), snList);
        // 获取屏幕网络类型,即判断在线离线
        JSONObject getNetworkTypeControlData = (JSONObject) getNetworkTypeControl.getData();
        List<JSONObject> successObjectList = (List<JSONObject>) getNetworkTypeControlData.get("successObjectList");
        if (successObjectList == null) {
            successObjectList = new ArrayList<>();
        }
        List<String> successSn = new ArrayList<>();
        // 修改在线节点状态、时间
        for (JSONObject jsonObject : successObjectList) {
            String sn = (String) jsonObject.get("sn");
            successSn.add(sn);
            List<ScreenDevice> collect = screenInfoData.stream().filter(a -> a.getNum().equals(sn)).collect(Collectors.toList());
            if (collect != null && collect.size() > 0) {
                collect.get(0).setNetworkState(1);
                collect.get(0).setLastOnlineTime(new Date());
            }
        }
        // 修改离线节点的状态
        List<String> defaultSnList = (List<String>) getNetworkTypeControlData.get("defaultSnList");
        if (defaultSnList == null) {
            defaultSnList = new ArrayList<>();
        }
        for (String sn : defaultSnList) {
            List<ScreenDevice> collect = screenInfoData.stream().filter(a -> a.getNum().equals(sn)).collect(Collectors.toList());
            if (collect != null && collect.size() > 0) {
                collect.get(0).setNetworkState(0);
            }
        }
        // 在线节点获取参数更新数据
        Result getScreenWidthControl = ScreenControlUtil.getScreenWidthControl(screenApi.getIp(), screenApi.getPort(), successSn);
        Result getScreenHeightControl = ScreenControlUtil.getScreenHeightControl(screenApi.getIp(), screenApi.getPort(), successSn);
        Result getBrightnessControl = ScreenControlUtil.getBrightnessControl(screenApi.getIp(), screenApi.getPort(), successSn);
        Result getVolumeControl = ScreenControlUtil.getVolumeControl(screenApi.getIp(), screenApi.getPort(), successSn);
        Result isScreenOpenControl = ScreenControlUtil.isScreenOpenControl(screenApi.getIp(), screenApi.getPort(), successSn);
//        Result getPlayingProgramControl = ScreenControlUtil.getPlayingProgramControl(successSn);
        // 之后的值只修改成功获取的值
        // 获取屏幕宽度
        JSONObject getScreenWidthControlData = (JSONObject) getScreenWidthControl.getData();
        if (getScreenWidthControlData != null) {
            List<JSONObject> getScreenWidthList = (List<JSONObject>) getScreenWidthControlData.get("successObjectList");
            if (getScreenWidthList != null) {
                //修改节点的屏幕宽度
                for (JSONObject jsonObject : getScreenWidthList) {
                    String sn = (String) jsonObject.get("sn");
                    Integer width = (Integer) jsonObject.get("result");
                    List<ScreenDevice> collect = screenInfoData.stream().filter(a -> a.getNum().equals(sn)).collect(Collectors.toList());
                    if (collect != null && collect.size() > 0) {
                        collect.get(0).setWidth(width.floatValue());
                    }
                }
            }
        }
        //获取屏幕高度
        JSONObject getScreenHeightControlData = (JSONObject) getScreenHeightControl.getData();
        if (getScreenHeightControlData != null) {
            List<JSONObject> getScreenHeightList = (List<JSONObject>) getScreenHeightControlData.get("successObjectList");
            if (getScreenHeightList != null) {
                //修改节点的屏幕高度
                for (JSONObject jsonObject : getScreenHeightList) {
                    String sn = (String) jsonObject.get("sn");
                    Integer Height = (Integer) jsonObject.get("result");
                    List<ScreenDevice> collect = screenInfoData.stream().filter(a -> a.getNum().equals(sn)).collect(Collectors.toList());
                    if (collect != null && collect.size() > 0) {
                        collect.get(0).setHeight(Height.floatValue());
                    }
                }
            }
        }
        //获取屏幕亮度
        JSONObject getBrightnessControlData = (JSONObject) getBrightnessControl.getData();
        if (getBrightnessControlData != null) {
            List<JSONObject> getBrightnessList = (List<JSONObject>) getBrightnessControlData.get("successObjectList");
            if (getBrightnessList != null) {
                //修改节点的屏幕亮度
                for (JSONObject jsonObject : getBrightnessList) {
                    String sn = (String) jsonObject.get("sn");
                    Integer brightness = (Integer) jsonObject.get("result");
                    List<ScreenDevice> collect = screenInfoData.stream().filter(a -> a.getNum().equals(sn)).collect(Collectors.toList());
                    if (collect != null && collect.size() > 0) {
                        collect.get(0).setBright(brightness);
                    }
                }
            }
        }
        //获取屏幕音量
        JSONObject getVolumeControlData = (JSONObject) getVolumeControl.getData();
        if (getVolumeControlData != null) {
            List<JSONObject> getVolumeList = (List<JSONObject>) getVolumeControlData.get("successObjectList");
            if (getVolumeList != null) {
                //修改节点的屏幕音量
                for (JSONObject jsonObject : getVolumeList) {
                    String sn = (String) jsonObject.get("sn");
                    Integer volume = (Integer) jsonObject.get("result");
                    List<ScreenDevice> collect = screenInfoData.stream().filter(a -> a.getNum().equals(sn)).collect(Collectors.toList());
                    if (collect != null && collect.size() > 0) {
                        collect.get(0).setVolume(volume);
                    }
                }
            }
        }
        //获取屏幕开关状态
        JSONObject isScreenOpenControlData = (JSONObject) isScreenOpenControl.getData();
        if (isScreenOpenControlData != null) {
            List<JSONObject> isScreenOpenList = (List<JSONObject>) isScreenOpenControlData.get("successObjectList");
            if (isScreenOpenList != null) {
                //修改节点的屏幕开关状态
                for (JSONObject jsonObject : isScreenOpenList) {
                    String sn = (String) jsonObject.get("sn");
                    Boolean aBoolean = (Boolean) jsonObject.get("result");
                    Integer state = null;
                    if (aBoolean) {
                        state = 1;
                    } else {
                        state = 0;
                    }
                    List<ScreenDevice> collect = screenInfoData.stream().filter(a -> a.getNum().equals(sn)).collect(Collectors.toList());
                    if (collect != null && collect.size() > 0) {
                        collect.get(0).setSwitchState(state);
                    }
                }
            }
        }
        //获取屏幕正在播放节目
//        JSONObject getPlayingProgramControlData = (JSONObject) getPlayingProgramControl.getData();
//        List<JSONObject> getPlayingProgramList = (List<JSONObject>) getPlayingProgramControlData.get("successObjectList");
//        //修改节点正在播放节目状态
//        for (JSONObject jsonObject : getPlayingProgramList) {
//            String sn = (String) jsonObject.get("sn");
//            String name = (String) jsonObject.get("name");
//            List<ScreenInfo> collect = screenInfoData.stream().filter(a -> a.getNode().getSn().equals(sn)).collect(Collectors.toList());
//            if (collect != null && collect.size() > 0) {
//                collect.get(0).setIsPlayProgram(1);
//            }
//        }
//        List<String> getPlayingProgramdefaultList = (List<String>) getPlayingProgramControlData.get("defaultSnList");
//        //修改节点没有播放节目状态
//        for (String sn : getPlayingProgramdefaultList) {
//            List<ScreenInfo> collect = screenInfoData.stream().filter(a -> a.getNode().getSn().equals(sn)).collect(Collectors.toList());
//            if (collect != null && collect.size() > 0) {
//                //当节点在线的情况下才会修改,因为不在线和没有播放时，查询返回的值都是字符串，
//                if (collect.get(0).getNode() != null && collect.get(0).getNode().getIsOffline() == 1) {
//                    collect.get(0).setIsPlayProgram(0);
//                }
//            }
//        }
        //获取正在播放节目的显示屏
        List<ScreenPlayDevice> playDeviceList = screenPlayDeviceService.list();
        if (playDeviceList != null) {
            List<Integer> playDeviceIdList = playDeviceList.stream().map(ScreenPlayDevice::getDeviceId).distinct().collect(Collectors.toList());
            if (playDeviceIdList != null) {
                for (ScreenDevice screenDevice : screenInfoData) {
                    List<Integer> collect = playDeviceIdList.stream()
                            .filter(a -> a.equals(screenDevice.getId())).collect(Collectors.toList());
                    if (collect != null && collect.size() > 0) {
                        screenDevice.setIsPlayProgram(1);
                    } else {
                        screenDevice.setIsPlayProgram(0);
                    }
                }
            }
        }
//        //获取字幕播放列表，循环判断字幕是否还在播放
//        List<Subtitle> subtitleList = subtitleMapper.selectSubBySnList(snList);
//        if (subtitleList != null && subtitleList.size() > 0 && subtitleList.get(0) != null) {
//            for (Subtitle subtitle : subtitleList) {
//                if (subtitle == null) {
//                    continue;
//                }
//                Integer nid = subtitle.getNid();
//                List<ScreenInfo> collect = screenInfoData.stream().filter(a -> a.getNode().getId().equals(nid)).collect(Collectors.toList());
//                if (collect != null && collect.size() > 0) {
//                    if (subtitle.getEndTime().getTime() > System.currentTimeMillis()) {
//                        System.out.println(collect.get(0).getNid());
//                        collect.get(0).setIsPlaySubtitle(1);
//                    } else {
//                        collect.get(0).setIsPlaySubtitle(0);
//                    }
//                }
//            }
//        } else {
//            for (String sn : snList) {
//                List<ScreenInfo> collect = screenInfoData.stream().filter(a -> a.getNode().getSn().equals(sn)).collect(Collectors.toList());
//                if (collect != null && collect.size() > 0) {
//                    collect.get(0).setIsPlaySubtitle(0);
//                }
//            }
//        }
        logger.info("刷新数据：{}", screenInfoData);
        return result.success(screenInfoData);
    }


    /**
     * 获取截屏
     */
    @Override
    public Result getScreenshots(Integer id) {
        // TODO Auto-generated method stub
        Result lt = new Result<>();
        ScreenDevice device = screenDeviceDao.getScreenshots(id);
        Map<String, Object> map = new HashMap<>();
        if (device != null) {

            StringBuffer buffer = new StringBuffer("{");

            buffer.append("\"type\":").append("\"callCardService\"");    //四个固定参数

            buffer.append(",\"fn\":").append("\"screenshot\"");

            buffer.append(",\"arg1\":").append("100");

            buffer.append(",\"arg2\":").append("100");

            buffer.append("}");
            //正确访问地址：http://192.168.10.160:8081/command/y30-120-20597
            String json = ScreenSendUtil.jsonPost("http://" + serverIp + ":" + serverPort + "/command/" + device.getNum(), buffer.toString());
            JSONObject jsonObject = null;
            if (json != null && !"0".equals(json)) {
                try {
                    jsonObject = JSON.parseObject(json);
                } catch (Exception e) {
                    logger.info("解析json失败，json：{}",json);
                }
            }

            String result = null;

            if (jsonObject != null) {
                result = (String) jsonObject.get("result");
            }

            if (result != null && !"".equals(json)) {
                result = result.replaceAll("\r|\n*", "");//去除base64字符串中的换行符  \n
                jsonObject.put("result", result);
                map.put("base64", jsonObject.get("result"));
            } else {
                lt.setMessage("获取截图失败!");
            }
            map.put("pngName", UUID.randomUUID().toString().replaceAll("-", ""));
            map.put("num", device.getNum());
        }
        lt.setData(map);
        lt.setCode(200);
        return lt;
    }


}