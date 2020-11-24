/**
 * @filename:LocationControlServiceImpl 2020-08-22
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.config.parameter.HttpSlApi;
import com.exc.street.light.dlm.mapper.LocationControlMapper;
import com.exc.street.light.dlm.service.*;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.*;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.enums.dlm.LocationControlTypeEnum;
import com.exc.street.light.resource.qo.DlmLocationControlQuery;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.DlmReqLocationControlVO;
import com.exc.street.light.resource.vo.resp.DlmRespControlLoopWithOptionVO;
import com.exc.street.light.resource.vo.resp.DlmRespLocationControlMixVO;
import com.exc.street.light.resource.vo.resp.DlmRespLocationControlVO;
import com.exc.street.light.resource.vo.resp.DlmRespLocationControlWithOptionVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 集中控制器(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class LocationControlServiceImpl extends ServiceImpl<LocationControlMapper, LocationControl> implements LocationControlService {

    private static final Logger logger = LoggerFactory.getLogger(LocationControlServiceImpl.class);

    @Autowired
    private ControlLoopService controlLoopService;

    @Autowired
    private ControlLoopDeviceService controlLoopDeviceService;

    @Autowired
    private LogUserService logUserService;

    @Autowired
    private LocationControlTypeLoopService locationControlTypeLoopService;

    @Autowired
    private ControlLoopSceneStatusService controlLoopSceneStatusService;

    @Autowired
    private HttpSlApi httpSlApi;

    @Autowired
    private ControlEnergyService controlEnergyService;

    @Autowired
    private ControlEnergyDayService controlEnergyDayService;

    @Autowired
    private ControlEnergyMonthService controlEnergyMonthService;

    @Autowired
    private ControlEnergyYearService controlEnergyYearService;

    @Override
    public Result insertLocationControl(DlmReqLocationControlVO controlVO, HttpServletRequest request) {
        logger.info("insertLocationControl - 新增集中控制器 controlVO=[{}]", controlVO);
        // 创建人
        Integer creator = JavaWebTokenUtil.parserStaffIdByToken(request);
        LocationControl locationControl = new LocationControl();
        BeanUtils.copyProperties(controlVO, locationControl);
        locationControl.setCreateTime(new Date());
        locationControl.setCreator(creator);
        int result = baseMapper.insert(locationControl);
        if (result < 1) {
            logger.error("新增集中控制器失败 controlVO=[{}]", controlVO);
            return new Result().error("新增集中控制器失败");
        }
        if (controlVO.getTypeId() != null && locationControl.getId() != null) {
            // 如果是华体的集中控制器，那么每个集中控制器下有四个回路
            if (controlVO.getTypeId() == LocationControlTypeEnum.HT.code()) {
                for (int i = 1; i <= 4; i++) {
                    ControlLoop controlLoop = new ControlLoop();
                    controlLoop.setTypeId(1);
                    controlLoop.setName("回路0" + i);
                    controlLoop.setNum("0" + i);
                    controlLoop.setCreator(creator);
                    controlLoop.setCreateTime(new Date());
                    controlLoop.setControlId(locationControl.getId());
                    controlLoopService.save(controlLoop);
                    // 在集控回路设备中间表之间建立关系
                    if (controlLoop.getId() != null) {
                        ControlLoopDevice controlLoopDevice = new ControlLoopDevice();
                        controlLoopDevice.setControlId(locationControl.getId());
                        controlLoopDevice.setLoopId(controlLoop.getId());
                        controlLoopDeviceService.save(controlLoopDevice);
                    }
                }
            }
            // 如果是EXC的集中控制器,每个集控下面有8个回路
            if (controlVO.getTypeId() == LocationControlTypeEnum.EXC.code()) {
                // 回路id集合
                List<Integer> loopIdList = new ArrayList<>();
                for (int i = 1; i <= 8 ; i++) {
                    ControlLoop controlLoop = new ControlLoop();
                    controlLoop.setTypeId(4);
                    controlLoop.setName("回路0" + i);
                    controlLoop.setNum("0" + i);
                    controlLoop.setCreator(creator);
                    controlLoop.setCreateTime(new Date());
                    controlLoop.setControlId(locationControl.getId());
                    controlLoopService.save(controlLoop);
                    loopIdList.add(controlLoop.getId());
                    // 在集控回路设备中间表之间建立关系
                    if (controlLoop.getId() != null) {
                        ControlLoopDevice controlLoopDevice = new ControlLoopDevice();
                        controlLoopDevice.setControlId(locationControl.getId());
                        controlLoopDevice.setLoopId(controlLoop.getId());
                        controlLoopDeviceService.save(controlLoopDevice);
                    }
                }
                controlLoopSceneStatusService.saveSceneStatusRecord(locationControl.getId(), loopIdList);
            }
        }
        return new Result().success("新增成功");
    }

    @Override
    public Result updateLocationControl(DlmReqLocationControlVO controlVO, HttpServletRequest request) {
        logger.info("updateLocationControl - 编辑集中控制器 controlVO=[{}]", controlVO);
        LocationControl locationControl = new LocationControl();
        BeanUtils.copyProperties(controlVO, locationControl);
        int result = baseMapper.updateById(locationControl);
        if (result < 1) {
            logger.info("updateLocationControl - 编辑集中控制器失败 controlVO=[{}]", controlVO);
            return new Result().error("编辑集中控制器失败");
        }
        return new Result().success("编辑成功");
    }

    @Override
    public Result detailOfLocationControl(Integer controlId, HttpServletRequest request) {
        logger.info("detailOfLocationControl - 集中控制器详情 controlId=[{}]", controlId);
        DlmRespLocationControlVO locationControlVO = baseMapper.selectLocationControlByControlId(controlId);
        Result<DlmRespLocationControlVO> result = new Result<>();
        return result.success(locationControlVO);
    }

    @Override
    public Result detailOfMixLocationControl(Integer controlId, HttpServletRequest request) {
        logger.info("detailOfMixLocationControl - 集中控制器混合信息 controlId=[{}]", controlId);
        List<DlmRespLocationControlMixVO> mixVOList = baseMapper.selectMixLocationControlByControlId(controlId);
        Result<List<DlmRespLocationControlMixVO>> result = new Result<>();
        return result.success(mixVOList);
    }

    @Override
    public Result deleteLocationControlByControlId(Integer controlId, HttpServletRequest request) {
        logger.info("deleteLocationControlByControlId - 删除集中控制器 controlId=[{}]", controlId);
        LocationControl locationControl = baseMapper.selectById(controlId);
        if (locationControl.getTypeId() == LocationControlTypeEnum.EXC.code()){
            controlLoopSceneStatusService.deleteSceneStatusByControlId(controlId);
            // 删除该集控的相关能耗数据
            // 实时能耗
            LambdaQueryWrapper<ControlEnergy> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(ControlEnergy::getControlId, locationControl.getId());
            controlEnergyService.remove(wrapper1);
            // 日能耗
            LambdaQueryWrapper<ControlEnergyDay> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(ControlEnergyDay::getControlId, locationControl.getId());
            controlEnergyDayService.remove(wrapper2);
            // 月能耗
            LambdaQueryWrapper<ControlEnergyMonth> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.eq(ControlEnergyMonth::getControlId, locationControl.getId());
            controlEnergyMonthService.remove(wrapper3);
            // 年能耗
            LambdaQueryWrapper<ControlEnergyYear> wrapper4 = new LambdaQueryWrapper<>();
            wrapper4.eq(ControlEnergyYear::getControlId, locationControl.getId());
            controlEnergyYearService.remove(wrapper4);
        }
        int result = baseMapper.deleteById(controlId);
        if (result < 1) {
            logger.info("deleteLocationControlByControlId - 删除集中控制器失败 controlId=[{}]", controlId);
            return new Result().error("删除集中控制器失败");
        }
        // 删除该集中控制器下的分组或回路
        LambdaQueryWrapper<ControlLoop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ControlLoop::getControlId, controlId);
        List<ControlLoop> controlLoopList = controlLoopService.list(queryWrapper);
        List<Integer> controlLoopIdList = controlLoopList.stream().map(ControlLoop::getId).collect(Collectors.toList());
        if (controlLoopIdList.size() > 0) {
            controlLoopService.removeByIds(controlLoopIdList);
        }
        // 删除集控回路设备之间的关系
        LambdaQueryWrapper<ControlLoopDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ControlLoopDevice::getControlId, controlId);
        List<ControlLoopDevice> controlLoopDeviceList = controlLoopDeviceService.list(wrapper);
        List<Integer> controlLoopDeviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getId).collect(Collectors.toList());
        if (controlLoopDeviceIdList.size() > 0) {
            controlLoopDeviceService.removeByIds(controlLoopDeviceIdList);
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result deleteOfBatchLocationControl(String[] controlIdList, HttpServletRequest request) {
        logger.info("deleteOfBatchLocationControl - 批量删除集中控制器 controlId=[{}]", controlIdList);
        if (controlIdList == null || controlIdList.length <= 0) {
            return new Result<>().error("批量删除失败，非法参数");
        }
        List<Integer> idList = Stream.of(controlIdList).map(Integer::parseInt).collect(Collectors.toList());
        if (!idList.isEmpty()) {
            // 删除该集中控制器下的分组或回路
            LambdaQueryWrapper<ControlLoop> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ControlLoop::getControlId, idList);
            List<ControlLoop> controlLoopList = controlLoopService.list(queryWrapper);
            List<Integer> controlLoopIdList = controlLoopList.stream().map(ControlLoop::getId).collect(Collectors.toList());
            if (controlLoopIdList.size() > 0) {
                String[] loopIdList = controlLoopIdList.stream().map(String::valueOf).collect(Collectors.toList()).toArray(new String[controlLoopIdList.size()]);
                Result result = controlLoopService.deleteOfBatchControlLoop(loopIdList, request);
                if (result.getCode() != 200) {
                    return new Result().error("集控下的集控分组删除失败");
                }
            }
            // EXC集控
            List<LocationControl> controlList = baseMapper.selectBatchIds(idList);
            for (LocationControl locationControl : controlList) {
                if (locationControl.getTypeId() == LocationControlTypeEnum.EXC.code()){
                    controlLoopSceneStatusService.deleteSceneStatusByControlId(locationControl.getId());
                    // 删除该集控的相关能耗数据
                    // 实时能耗
                    LambdaQueryWrapper<ControlEnergy> wrapper1 = new LambdaQueryWrapper<>();
                    wrapper1.eq(ControlEnergy::getControlId, locationControl.getId());
                    controlEnergyService.remove(wrapper1);
                    // 日能耗
                    LambdaQueryWrapper<ControlEnergyDay> wrapper2 = new LambdaQueryWrapper<>();
                    wrapper2.eq(ControlEnergyDay::getControlId, locationControl.getId());
                    controlEnergyDayService.remove(wrapper2);
                    // 月能耗
                    LambdaQueryWrapper<ControlEnergyMonth> wrapper3 = new LambdaQueryWrapper<>();
                    wrapper3.eq(ControlEnergyMonth::getControlId, locationControl.getId());
                    controlEnergyMonthService.remove(wrapper3);
                    // 年能耗
                    LambdaQueryWrapper<ControlEnergyYear> wrapper4 = new LambdaQueryWrapper<>();
                    wrapper4.eq(ControlEnergyYear::getControlId, locationControl.getId());
                    controlEnergyYearService.remove(wrapper4);
                }
            }
            // 批量删除
            int result = baseMapper.deleteBatchIds(idList);
            if (result < 1) {
                logger.info("deleteOfBatchLocationControl - 批量删除集中控制器失败 controlIdList=[{}]", controlIdList);
                return new Result().error("删除集中控制器失败");
            }
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result listLocationControlWithPageByControlQuery(DlmLocationControlQuery controlQuery, HttpServletRequest request) {
        logger.info("listLocationControlWithPageByControlQuery - 分页查询集中控制器列表 controlId=[{}]", controlQuery);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        boolean flag = logUserService.isAdmin(userId);
        User user = logUserService.get(userId);
        // 不是超级管理员则根据区域查询
        if (!flag) {
            controlQuery.setAreaId(user.getAreaId());
        }
        // 不分页
        if (controlQuery.getPageSize() == 0) {
            List<DlmRespLocationControlVO> voList = baseMapper.selectLocationControlListByControlQuery(controlQuery);
            Result<List<DlmRespLocationControlVO>> result = new Result<>();
            return result.success(voList);
        } else {
            Result<IPage<DlmRespLocationControlVO>> result = new Result<>();
            Page<DlmRespLocationControlVO> page = new Page<>(controlQuery.getPageNum(), controlQuery.getPageSize());
            return result.success(baseMapper.selectLocationControlWithPageByControlQuery(page, controlQuery));
        }
    }

    @Override
    public Result listLocationControlWithOptionQuery(HttpServletRequest request) {
        logger.info("listLocationControlWithOptionQuery - 集中控制器下拉列表(中科智联)");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        // 找出支持分组的集控类型
        LambdaQueryWrapper<LocationControlTypeLoop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LocationControlTypeLoop::getLoopId, 2);
        List<LocationControlTypeLoop> typeLoopList = locationControlTypeLoopService.list(wrapper);
        List<Integer> locationControlTypeIdList = typeLoopList.stream().map(LocationControlTypeLoop::getLocationControlTypeId).collect(Collectors.toList());
        // 根据集控类型找到所有的集控器,并且根据分区过滤
        List<LocationControl> controlList = null;
        if (!flag) {
            // 不是超级管理员过滤出该分区的数据
            controlList = baseMapper.selectControlListByArea(locationControlTypeIdList, user.getAreaId());
        } else {
            LambdaQueryWrapper<LocationControl> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(LocationControl::getTypeId, locationControlTypeIdList)
                    .isNotNull(LocationControl::getCabinetId);
            controlList = baseMapper.selectList(queryWrapper);
        }
        List<DlmRespLocationControlWithOptionVO> voList = new ArrayList<>();
        if (controlList != null && controlList.size() > 0) {
            for (LocationControl locationControl : controlList) {
                DlmRespLocationControlWithOptionVO optionVO = new DlmRespLocationControlWithOptionVO();
                optionVO.setId(locationControl.getId());
                optionVO.setPartId("control" + locationControl.getId());
                optionVO.setName(locationControl.getName());
                // 查询该集控下的分组信息
                LambdaQueryWrapper<ControlLoop> loopQueryWrapper = new LambdaQueryWrapper<>();
                loopQueryWrapper.eq(ControlLoop::getControlId, locationControl.getId());
                List<ControlLoop> controlLoopList = controlLoopService.list(loopQueryWrapper);
                List<DlmRespControlLoopWithOptionVO> loopWithOptionVOList = new ArrayList<>();
                for (ControlLoop controlLoop : controlLoopList) {
                    DlmRespControlLoopWithOptionVO loopWithOptionVO = new DlmRespControlLoopWithOptionVO();
                    loopWithOptionVO.setId(controlLoop.getId());
                    loopWithOptionVO.setPartId("loop" + controlLoop.getId());
                    loopWithOptionVO.setName(controlLoop.getName());
                    loopWithOptionVOList.add(loopWithOptionVO);
                }
                optionVO.setChildrenList(loopWithOptionVOList.size() > 0 ? loopWithOptionVOList : null);
                voList.add(optionVO);
            }
        }
        Result<List<DlmRespLocationControlWithOptionVO>> result = new Result<>();
        return result.success(voList);
    }

    @Override
    public Result listLocationControlOfHtWithOptionQuery(HttpServletRequest request) {
        logger.info("listLocationControlOfHtWithOptionQuery - 集中控制器下拉列表(华体)");
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        // 找出支持华体支路的集控类型(无论是回路还是支路查出来的类型都是一样，所以这里可以直接通过类型查询)
        LambdaQueryWrapper<LocationControlTypeLoop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LocationControlTypeLoop::getLoopId, 3);
        List<LocationControlTypeLoop> typeLoopList = locationControlTypeLoopService.list(wrapper);
        List<Integer> locationControlTypeIdList = typeLoopList.stream().map(LocationControlTypeLoop::getLocationControlTypeId).collect(Collectors.toList());
        // 根据集控类型找到所有的集控器,并且根据分区过滤
        List<LocationControl> controlList = null;
        if (!flag) {
            // 不是超级管理员过滤出该分区的数据
            controlList = baseMapper.selectControlListByArea(locationControlTypeIdList, user.getAreaId());
        } else {
            LambdaQueryWrapper<LocationControl> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(LocationControl::getTypeId, locationControlTypeIdList)
                    .isNotNull(LocationControl::getCabinetId);
            controlList = baseMapper.selectList(queryWrapper);
        }
        List<DlmRespLocationControlWithOptionVO> voList = new ArrayList<>();
        if (controlList != null && controlList.size() > 0) {
            for (LocationControl locationControl : controlList) {
                DlmRespLocationControlWithOptionVO optionVO = new DlmRespLocationControlWithOptionVO();
                optionVO.setId(locationControl.getId());
                optionVO.setPartId("control" + locationControl.getId());
                optionVO.setName(locationControl.getName());
                // 查询该集控下的设备信息
                LambdaQueryWrapper<ControlLoopDevice> controlDeviceWrapper = new LambdaQueryWrapper<>();
                controlDeviceWrapper.eq(ControlLoopDevice::getControlId, locationControl.getId());
                List<ControlLoopDevice> controlDeviceList = controlLoopDeviceService.list(controlDeviceWrapper);
                List<Integer> deviceIdList = controlDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
                List<SystemDevice> deviceList = null;
                List<DlmRespControlLoopWithOptionVO> loopWithOptionVOList = new ArrayList<>();
                String json = "deviceIdList=";
                for (Integer deviceId : deviceIdList) {
                    json += deviceId + "&deviceIdList=";
                }
                try {
                    JSONObject deviceListResult = JSON.parseObject(HttpUtil.get(httpSlApi.getUrl() + httpSlApi.getGetDeviceListByIdList() + "?" + json, headMap));
                    JSONArray deviceListResultArr = deviceListResult.getJSONArray("data");
                    deviceList = JSON.parseObject(deviceListResultArr.toJSONString(), new TypeReference<List<SystemDevice>>() {
                    });
                    for (SystemDevice device : deviceList) {
                        DlmRespControlLoopWithOptionVO loopWithOptionVO = new DlmRespControlLoopWithOptionVO();
                        loopWithOptionVO.setId(device.getId());
                        loopWithOptionVO.setPartId("device" + device.getId());
                        loopWithOptionVO.setName(device.getName());
                        loopWithOptionVOList.add(loopWithOptionVO);
                    }
                    optionVO.setChildrenList(loopWithOptionVOList.size() > 0 ? loopWithOptionVOList : null);

                } catch (Exception e) {
                    logger.error("根据设备id集合获取设备列表信息接口调用失败！");
                }
                voList.add(optionVO);
            }
        }
        Result<List<DlmRespLocationControlWithOptionVO>> result = new Result<>();
        return result.success(voList);
    }

    @Override
    public Result listLocationControlOfExcWithOptionQuery(HttpServletRequest request) {
        logger.info("listLocationControlOfExcWithOptionQuery - 集中控制器下拉列表(EXC)");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean isAdmin = logUserService.isAdmin(userId);
        Integer areaId = null;
        if (!isAdmin) {
            areaId = user.getAreaId();
        }
        int typeId = LocationControlTypeEnum.EXC.code();
        List<LocationControl> locationControlList = baseMapper.selectListLocationControlOfExcWithOptionQuery(areaId, typeId);
        Result<List<LocationControl>> result = new Result<>();
        return result.success(locationControlList);
    }

    @Override
    public Result nameAndNumUniqueness(Integer id, String name, String num) {
        logger.info("nameAndNumUniqueness - 集中控制器名称和编号唯一性校验 id={} name={} num={}", id, name, num);
        Result<Integer> result = new Result<>();
        int isUniqueness = 0;
        LambdaQueryWrapper<LocationControl> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(LocationControl::getName, name);
        LambdaQueryWrapper<LocationControl> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(LocationControl::getNum, num);
        LocationControl controlByName = baseMapper.selectOne(wrapper1);
        LocationControl controlByNum = baseMapper.selectOne(wrapper2);
        // 编辑时判重
        if (id != null) {
            if (controlByName != null && !controlByName.getId().equals(id)) {
                isUniqueness = 1;
                return result.error("集中控制器名称已存在", isUniqueness);
            }
            if (controlByNum != null && !controlByNum.getId().equals(id)) {
                isUniqueness = 2;
                return result.error("集中控制器编号已存在", isUniqueness);
            }
        } else if (controlByName != null){
            isUniqueness = 1;
            return result.error("集中控制器名称已存在", isUniqueness);
        } else if (controlByNum != null){
            isUniqueness = 2;
            return result.error("集中控制器编号已存在", isUniqueness);
        }
        return result.success(isUniqueness);
    }

    @Override
    public void getLocationControlStatus() {
        logger.info("getLocationControlStatus - 定时检测集控的在离线状态");
        // 查询所有的EXC类型的集控
        LambdaQueryWrapper<LocationControl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LocationControl::getTypeId, LocationControlTypeEnum.EXC.code());
        List<LocationControl> locationControlList = baseMapper.selectList(queryWrapper);
        for (LocationControl locationControl : locationControlList) {
            if (locationControl.getLastOnlineTime() != null) {
                long lastOnlineTimeMillis = locationControl.getLastOnlineTime().getTime();
                long currentTimeMillis = System.currentTimeMillis();
                // 超过10分钟判定为离线
                if (currentTimeMillis - lastOnlineTimeMillis > 10 * 1000) {
                    locationControl.setIsOnline(0);
                    baseMapper.updateById(locationControl);
                }
            }
        }
    }
}