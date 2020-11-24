/**
 * @filename:ControlLoopServiceImpl 2020-08-24
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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.config.parameter.HttpSlApi;
import com.exc.street.light.dlm.mapper.ControlLoopMapper;
import com.exc.street.light.dlm.service.ControlLoopDeviceService;
import com.exc.street.light.dlm.service.ControlLoopService;
import com.exc.street.light.dlm.service.LocationControlService;
import com.exc.street.light.dlm.service.LocationControlTypeService;
import com.exc.street.light.dlm.utils.ProtocolUtil;
import com.exc.street.light.dlm.utils.SocketClient;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.dlm.ControlLoopDTO;
import com.exc.street.light.resource.dto.electricity.ControlCommand;
import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.exc.street.light.resource.entity.dlm.ControlLoopDevice;
import com.exc.street.light.resource.entity.dlm.LocationControl;
import com.exc.street.light.resource.entity.dlm.LocationControlType;
import com.exc.street.light.resource.entity.sl.SystemDevice;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.enums.dlm.LocationControlTypeEnum;
import com.exc.street.light.resource.qo.DlmControlLoopQuery;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.DlmReqControlLoopVO;
import com.exc.street.light.resource.vo.req.htLamp.HtSetLampPostOutputRequestVO;
import com.exc.street.light.resource.vo.resp.DlmRespControlLoopDetailVO;
import com.exc.street.light.resource.vo.resp.DlmRespControlLoopVO;
import com.exc.street.light.resource.vo.resp.DlmRespControlLoopWithOptionVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 集控分组或回路(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ControlLoopServiceImpl extends ServiceImpl<ControlLoopMapper, ControlLoop> implements ControlLoopService {

    private static final Logger logger = LoggerFactory.getLogger(ControlLoopServiceImpl.class);

    @Autowired
    private LocationControlService locationControlService;

    @Autowired
    private LocationControlTypeService locationControlTypeService;

    @Autowired
    private ControlLoopDeviceService controlLoopDeviceService;

    @Autowired
    private LogUserService logUserService;

    @Autowired
    private HttpSlApi httpSlApi;

    @Override
    public Result insertControlLoop(DlmReqControlLoopVO loopVO, HttpServletRequest request) {
        logger.info("insertControlLoop - 新增集控分组 loopVO=[{}]", loopVO);
        Integer creator = JavaWebTokenUtil.parserStaffIdByToken(request);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        headMap.put("Content-Type", "application/json");
        // 判断集中控制器的类型
        if (loopVO != null) {
            LocationControl locationControl = locationControlService.getById(loopVO.getControlId());
            if (locationControl != null) {
                LocationControlType locationControlType = locationControlTypeService.getById(locationControl.getTypeId());
                if (locationControlType.getId() == LocationControlTypeEnum.ZKZL.code()) {
                    // 判断是否小于18个分组
                    LambdaQueryWrapper<ControlLoop> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(ControlLoop::getControlId, loopVO.getControlId());
                    List<ControlLoop> controlLoopList = baseMapper.selectList(wrapper);
                    if (controlLoopList.size() > 18) {
                        return new Result().error("该集控器下的分组数量不能超过18个");
                    } else {
                        ControlLoop controlLoop = new ControlLoop();
                        BeanUtils.copyProperties(loopVO, controlLoop);
                        controlLoop.setCreateTime(new Date());
                        controlLoop.setCreator(creator);
                        controlLoop.setTypeId(2);
                        // 给分组设置编号
                        // 查询该集控下的分组编号
                        List<String> insertNumList = new ArrayList<>();
                        List<String> numList = controlLoopList.stream().map(ControlLoop::getNum).collect(Collectors.toList());
                        for (int i = 1; i <= 18; i++) {
                            insertNumList.add(i + "");
                        }
                        // 去除已经存在的编号
                        insertNumList.removeAll(numList);
                        // 取未使用过的编号中的第一个，递增效果
                        controlLoop.setNum(insertNumList.get(0));
                        int result = baseMapper.insert(controlLoop);
                        if (result < 1) {
                            logger.info("insertControlLoop - 新增集控分组失败 loopVO=[{}]", loopVO);
                            return new Result().error("新增集控分组失败");
                        }
                        // 绑定集控分组设备之间的关系
                        if (loopVO.getDeviceIdList() != null && loopVO.getDeviceIdList().size() > 0) {
                            for (Integer deviceId : loopVO.getDeviceIdList()) {
                                ControlLoopDevice loopDevice = new ControlLoopDevice();
                                loopDevice.setControlId(loopVO.getControlId());
                                loopDevice.setLoopId(controlLoop.getId());
                                loopDevice.setDeviceId(deviceId);
                                controlLoopDeviceService.save(loopDevice);
                            }
                            List<Integer> deviceIdList = loopVO.getDeviceIdList();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("deviceIdList", deviceIdList);
                            jsonObject.put("concentratorId", locationControl.getNum());
                            jsonObject.put("installNum", deviceIdList.size());
                            jsonObject.put("addOrDelete", 1);
                            jsonObject.put("groupNo", insertNumList.get(0));
                            try {
                                //注册设备到集中控制器上
                                JSONObject registerResult = JSON.parseObject(HttpUtil.post(httpSlApi.getUrl() + httpSlApi.getRegister(), jsonObject.toJSONString(), headMap));
                                Integer code = (Integer) registerResult.get("code");
                                if (code == 400) {
                                    logger.error("注册失败" + deviceIdList);
                                    return new Result().error("灯具注册到集中控制器失败" + deviceIdList);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.error("注册失败" + deviceIdList);
                                return new Result().error("灯具注册到集中控制器失败" + deviceIdList);
                            }

                        }
                    }
                } else if (locationControlType.getId() == LocationControlTypeEnum.HT.code()) {
                    // 设置支路编号
                    for (int i = 1; i <= 3; i++) {
                        ControlLoop controlLoop = new ControlLoop();
                        BeanUtils.copyProperties(loopVO, controlLoop);
                        controlLoop.setCreateTime(new Date());
                        controlLoop.setCreator(creator);
                        controlLoop.setTypeId(3);
                        controlLoop.setNum(i + "");
                        int result = baseMapper.insert(controlLoop);
                        if (result < 1) {
                            logger.info("insertControlLoop - 新增单灯控制器支路失败 loopVO=[{}]", loopVO);
                            return new Result().error("新增单灯控制器支路失败");
                        }
                        // 在这不用绑定集控和支路之间的关系，在编辑的时候绑定关系
                    }
                }
            }
        }
        return new Result().success("新增成功");
    }

    @Override
    public Result updateControlLoop(DlmReqControlLoopVO loopVO, HttpServletRequest request) {
        logger.info("updateControlLoop - 编辑集控分组 loopVO=[{}]", loopVO);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        headMap.put("Content-Type", "application/json");
        ControlLoop controlLoop = new ControlLoop();
        BeanUtils.copyProperties(loopVO, controlLoop);
        controlLoop.setUpdateTime(new Date());
        int result = baseMapper.updateById(controlLoop);
        if (result < 1) {
            logger.info("updateControlLoop - 编辑集控分组失败 loopVO=[{}]", loopVO);
            return new Result().error("编辑集控分组失败");
        }
        // 解除之前分组设备之间的关系
        LambdaQueryWrapper<ControlLoopDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ControlLoopDevice::getLoopId, loopVO.getId());
        LocationControl locationControl = locationControlService.getById(loopVO.getControlId());
        if (locationControl != null) {
            List<ControlLoopDevice> controlLoopDeviceList = controlLoopDeviceService.list(wrapper);
            if (controlLoopDeviceList != null && controlLoopDeviceList.size() > 0) {
                List<Integer> deviceIdDeleteList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
                LocationControlType locationControlType = locationControlTypeService.getById(locationControl.getTypeId());
                if (locationControlType.getId() == LocationControlTypeEnum.ZKZL.code()) {
                    ControlLoop controlLoopTemp = baseMapper.selectById(loopVO.getId());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("deviceIdList", deviceIdDeleteList);
                    jsonObject.put("concentratorId", locationControl.getNum());
                    jsonObject.put("installNum", deviceIdDeleteList.size());
                    jsonObject.put("addOrDelete", 0);
                    jsonObject.put("groupNo", controlLoopTemp.getNum());
                    try {
                        //解除设备绑定集中控制器
                        JSONObject registerResult = JSON.parseObject(HttpUtil.post(httpSlApi.getUrl() + httpSlApi.getRegister(), jsonObject.toJSONString(), headMap));
                        Integer code = (Integer) registerResult.get("code");
                        if (code == 400) {
                            logger.error("解除绑定失败" + deviceIdDeleteList);
                            return new Result().error("解除绑定失败" + deviceIdDeleteList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("解除绑定失败" + deviceIdDeleteList);
                        return new Result().error("解除绑定失败" + deviceIdDeleteList);
                    }
                }
            }
            controlLoopDeviceService.remove(wrapper);
        }
        // 重新注册设备到集中控制器上
        if (locationControl != null) {
            LocationControlType locationControlType = locationControlTypeService.getById(locationControl.getTypeId());
            if (locationControlType.getId() == LocationControlTypeEnum.ZKZL.code()) {
                ControlLoop controlLoopTemp = baseMapper.selectById(loopVO.getId());
                List<Integer> deviceIdList = loopVO.getDeviceIdList();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceIdList", deviceIdList);
                jsonObject.put("concentratorId", locationControl.getNum());
                jsonObject.put("installNum", deviceIdList.size());
                jsonObject.put("addOrDelete", 1);
                jsonObject.put("groupNo", controlLoopTemp.getNum());
                try {
                    //注册设备到集中控制器上
                    JSONObject registerResult = JSON.parseObject(HttpUtil.post(httpSlApi.getUrl() + httpSlApi.getRegister(), jsonObject.toJSONString(), headMap));
                    Integer code = (Integer) registerResult.get("code");
                    if (code == 400) {
                        logger.error("注册失败" + deviceIdList);
                        return new Result().error("注册失败" + deviceIdList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("注册失败" + deviceIdList);
                    return new Result().error("注册失败" + deviceIdList);
                }
            }
        }
        //重新绑定分组设备之间的关系
        if (loopVO.getDeviceIdList() != null && loopVO.getDeviceIdList().size() > 0) {
            for (Integer deviceId : loopVO.getDeviceIdList()) {
                ControlLoopDevice device = new ControlLoopDevice();
                device.setDeviceId(deviceId);
                device.setLoopId(loopVO.getId());
                device.setControlId(loopVO.getControlId());
                controlLoopDeviceService.save(device);
            }
        }
        return new Result().success("编辑成功");
    }

    @Override
    public Result detailOfControlLoop(Integer loopId, HttpServletRequest request) {
        logger.info("detailOfControlLoop - 集控分组详情 loopId=[{}]", loopId);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        headMap.put("Content-Type", "application/json");
        ControlLoop controlLoop = baseMapper.selectById(loopId);
        // 查询该分组下的灯具设备id集合
        LambdaQueryWrapper<ControlLoopDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ControlLoopDevice::getLoopId, loopId);
        List<ControlLoopDevice> loopDeviceList = controlLoopDeviceService.list(wrapper);
        List<Integer> deviceIdList = loopDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
        DlmRespControlLoopDetailVO detailVO = new DlmRespControlLoopDetailVO();
        BeanUtils.copyProperties(controlLoop, detailVO);
        LambdaQueryWrapper<LocationControl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LocationControl::getId, controlLoop.getControlId());
        LocationControl locationControl = locationControlService.getOne(queryWrapper);
        detailVO.setControlName(locationControl.getName());
        detailVO.setDeviceIdList(deviceIdList);
        List<SystemDevice> deviceList = null;
        String json = "deviceIdList=";
        for (Integer deviceId : deviceIdList) {
            json += deviceId + "&deviceIdList=";
        }
        try {
            JSONObject deviceListResult = JSON.parseObject(HttpUtil.get(httpSlApi.getUrl() + httpSlApi.getGetDeviceListByIdList() + "?" + json, headMap));
            JSONArray deviceListResultArr = deviceListResult.getJSONArray("data");
            deviceList = JSON.parseObject(deviceListResultArr.toJSONString(), new TypeReference<List<SystemDevice>>() {
            });
            List<String> deviceNameList = deviceList.stream().map(SystemDevice::getName).collect(Collectors.toList());
            detailVO.setDeviceNameList(deviceNameList);
        } catch (Exception e) {
            logger.error("根据设备id集合获取设备列表信息接口调用失败！");
        }
        Result<DlmRespControlLoopDetailVO> result = new Result<>();
        return result.success(detailVO);
    }

    @Override
    public Result deleteControlLoopByLoopId(Integer loopId, HttpServletRequest request) {
        logger.info("deleteControlLoopByLoopId - 删除集控分组 loopId=[{}]", loopId);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        ControlLoop controlLoop = baseMapper.selectById(loopId);
        Integer controlId = controlLoop.getControlId();
        int result = baseMapper.deleteById(loopId);
        if (result < 1) {
            logger.info("deleteControlLoopByLoopId - 删除集控分组失败 loopId=[{}]", loopId);
            return new Result().error("删除集控分组失败");
        }
        // 解除之前分组设备之间的关系
        LambdaQueryWrapper<ControlLoopDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ControlLoopDevice::getLoopId, loopId);
        List<ControlLoopDevice> controlLoopDeviceList = controlLoopDeviceService.list(wrapper);
        List<Integer> deviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
        controlLoopDeviceService.remove(wrapper);

        LocationControl locationControl = locationControlService.getById(controlId);
        if (locationControl != null && controlLoopDeviceList != null && controlLoopDeviceList.size() > 0) {
            LocationControlType locationControlType = locationControlTypeService.getById(locationControl.getTypeId());
            if (locationControlType.getId() == LocationControlTypeEnum.ZKZL.code()) {
                ControlLoop controlLoopTemp = baseMapper.selectById(loopId);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceIdList", deviceIdList);
                jsonObject.put("concentratorId", locationControl.getNum());
                jsonObject.put("installNum", deviceIdList.size());
                jsonObject.put("addOrDelete", 0);
                jsonObject.put("groupNo", controlLoopTemp.getNum());
                try {
                    //解除设备绑定集中控制器
                    JSONObject registerResult = JSON.parseObject(HttpUtil.post(httpSlApi.getUrl() + httpSlApi.getRegister(), jsonObject.toJSONString(), headMap));
                    Integer code = (Integer) registerResult.get("code");
                    if (code == 400) {
                        logger.error("解除绑定失败" + loopId);
                        return new Result().error("解除绑定失败" + loopId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("解除绑定失败" + loopId);
                    return new Result().error("解除绑定失败" + loopId);
                }
            }
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result listControlLoopWithPageByLoopQuery(DlmControlLoopQuery loopQuery, HttpServletRequest request) {
        logger.info("listControlLoopWithPageByLoopQuery - 分页查询集控分组列表 loopQuery=[{}]", loopQuery);
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if (!flag) {
            loopQuery.setAreaId(user.getAreaId());
        }
        Result<Map<String, IPage<DlmRespControlLoopVO>>> result = new Result<>();
        Page<DlmRespControlLoopVO> page = new Page<>(loopQuery.getPageNum(), loopQuery.getPageSize());
        Page<DlmRespControlLoopVO> page2 = new Page<>(loopQuery.getPageNum(), loopQuery.getPageSize());
        Page<DlmRespControlLoopVO> page3 = new Page<>(loopQuery.getPageNum(), loopQuery.getPageSize());
        Page<DlmRespControlLoopVO> page4 = new Page<>(loopQuery.getPageNum(), loopQuery.getPageSize());
        // 回路分页数据
        Integer loopTypeId = 1;
        IPage<DlmRespControlLoopVO> loopVOIPage = baseMapper.selectControlLoopWithPageByLoopQuery(page, loopQuery, loopTypeId);
        // 分组分页数据
        Integer groupTypeId = 2;
        IPage<DlmRespControlLoopVO> groupVOIPage = baseMapper.selectControlLoopWithPageByLoopQuery(page2, loopQuery, groupTypeId);
        // 支路分页数据
        Integer branchTypeId = 3;
        IPage<DlmRespControlLoopVO> branchVOIPage = baseMapper.selectControlLoopWithPageByLoopQuery(page3, loopQuery, branchTypeId);
        // 自研回路分页数据
        Integer researchLoopTypeId = 4;
        IPage<DlmRespControlLoopVO> researchLoopVOIPage = baseMapper.selectControlLoopWithPageByLoopQuery(page4, loopQuery, researchLoopTypeId);
        Map<String, IPage<DlmRespControlLoopVO>> pageMap = new HashMap<>();
        pageMap.put("loopVOIPage", loopVOIPage);
        pageMap.put("groupVOIPage", groupVOIPage);
        pageMap.put("branchVOIPage", branchVOIPage);
        pageMap.put("researchLoopVOIPage", researchLoopVOIPage);
        return result.success(pageMap);
    }

    @Override
    public Result listControlLoopWithOptionQuery(Integer controlId, HttpServletRequest request) {
        logger.info("listControlLoopWithOptionQuery - 集控器下的分组下拉列表 controlId=[{}]", controlId);
        LambdaQueryWrapper<ControlLoop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ControlLoop::getControlId, controlId);
        List<ControlLoop> controlLoopList = baseMapper.selectList(wrapper);
        // 构建返回对象集合
        List<DlmRespControlLoopWithOptionVO> optionVOList = new ArrayList<>();
        for (ControlLoop controlLoop : controlLoopList) {
            DlmRespControlLoopWithOptionVO optionVO = new DlmRespControlLoopWithOptionVO();
            optionVO.setId(controlLoop.getId());
            optionVO.setName(controlLoop.getName());
            optionVOList.add(optionVO);
        }
        Result<List<DlmRespControlLoopWithOptionVO>> result = new Result<>();
        return result.success(optionVOList);
    }

    @Override
    public Result updateControlLoopSwitch(Integer loopId, Integer isOpen, HttpServletRequest request) {
        logger.info("updateControlLoopSwitch - 回路开关控制 loopId={} isOpen={}", loopId, isOpen);
        // 根据回路id查询所属的集控回路
        ControlLoop controlLoop = baseMapper.selectById(loopId);
        // 根据集控id查询集控信息
        LocationControl locationControl = locationControlService.getById(controlLoop.getControlId());
        // 根据类型控制不同类型的集控回路 -- EXC
        if (locationControl.getTypeId() == LocationControlTypeEnum.EXC.code()) {
            if (StringUtils.isAnyBlank(locationControl.getIp(), locationControl.getPort(), locationControl.getMac())) {
                logger.info("ip={} port={} mac={}", locationControl.getIp(), locationControl.getPort(), locationControl.getMac());
                return new Result().error("集中控制器ip或端口或Mac地址为空");
            }
            ControlCommand controlCommand = new ControlCommand();
            controlCommand.setControlId(Integer.parseInt(controlLoop.getNum()));
            controlCommand.setDeviceAddress(66);
            controlCommand.setValue(isOpen == 0 ? 1 : 0);
            controlCommand.setTagId(0);
            byte[] bytes = ProtocolUtil.setControlCommand(locationControl.getMac(), controlCommand);
            boolean flag = SocketClient.sendData(locationControl.getIp(), Integer.parseInt(locationControl.getPort()), bytes);
            if (!flag){
                return new Result().error("指令下发失败");
            }
        }
        // 华体 -- HT
        if (locationControl.getTypeId() == LocationControlTypeEnum.HT.code()) {
            // 调用下发控制接口
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("token", request.getHeader("token"));
            headerMap.put("Content-Type", "application/json");
            HtSetLampPostOutputRequestVO postOutputRequestVO = new HtSetLampPostOutputRequestVO();
            postOutputRequestVO.setLocationControlId(controlLoop.getControlId());
            postOutputRequestVO.setLocationControlAddr(locationControl.getNum());
            List<Integer> actionList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                actionList.add(Integer.parseInt(controlLoop.getNum()) - 1 == i ? (isOpen == 1 ? 1 : 2) : 0);
            }
            postOutputRequestVO.setActList(actionList);
            String jsonString = JSONObject.toJSONString(postOutputRequestVO);
            try {
                JSONObject loopControlResult = JSON.parseObject(HttpUtil.post(httpSlApi.getUrl() + httpSlApi.getLoopControl(), jsonString, headerMap));
                JSONObject loopControlObj = loopControlResult.getJSONObject("data");
                System.out.println(loopControlObj);
            } catch (Exception e) {
                logger.error("控制下发失败", e.getMessage());
                return new Result().error("控制失败");
            }
        }
        LambdaUpdateWrapper<ControlLoop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ControlLoop::getIsOpen, isOpen)
                .eq(ControlLoop::getId, loopId);
        int result = baseMapper.update(null, updateWrapper);
        if (result < 1) {
            return new Result().error("控制失败");
        }
        return new Result().success("控制成功");
    }

    @Override
    public Result deleteOfBatchControlLoop(String[] loopIdList, HttpServletRequest request) {
        logger.info("deleteOfBatchControlLoop - 批量删除集控分组 loopIdList=[{}]", loopIdList);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("token", request.getHeader("token"));
        headMap.put("Content-Type", "application/json");
        if (loopIdList == null || loopIdList.length <= 0) {
            return new Result<>().error("批量删除失败，非法参数");
        }
        List<Integer> idList = Stream.of(loopIdList).map(Integer::parseInt).collect(Collectors.toList());
        if (!idList.isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            List<ControlLoop> controlLoopList = baseMapper.selectBatchIds(idList);
            if (controlLoopList == null || controlLoopList.size() == 0) {
                return new Result().error("不存在需要删除的分组");
            }
            //获取所有需要解绑的设备id集合
            Result controlLoopDeviceByLoopIdList = controlLoopDeviceService.getControlLoopDeviceByLoopIdList(idList, request);
            List<ControlLoopDevice> controlLoopDeviceList = (List<ControlLoopDevice>) controlLoopDeviceByLoopIdList.getData();
            List<Integer> deviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).distinct().collect(Collectors.toList());
            if (deviceIdList.size() > 0) {
                //获取集中控制器id集合
                List<Integer> controlIdList = controlLoopList.stream().map(ControlLoop::getControlId).distinct().collect(Collectors.toList());
                //根据集中控制器id对集中控制器分组集合进行分组
                Map<Integer, List<ControlLoop>> groupingByControlId = controlLoopList.stream()
                        .collect(Collectors.groupingBy(ControlLoop::getControlId));
                //获取集中控制器id与集中控制器的Map
                Collection<LocationControl> locationControlList = locationControlService.listByIds(controlIdList);
                Map<Integer, LocationControl> locationControlMap = locationControlList.stream().collect(Collectors.toMap(LocationControl::getId, obj -> obj));
                for (Integer controlId : controlIdList) {
                    LocationControl locationControl = locationControlMap.get(controlId);
                    Integer typeId = locationControl.getTypeId();
                    if (typeId == LocationControlTypeEnum.ZKZL.code()) {
                        List<ControlLoop> controlLoopListTemp = groupingByControlId.get(controlId);
                        List<Integer> groupNoList = controlLoopListTemp.stream().map(p -> Integer.parseInt(p.getNum())).collect(Collectors.toList());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("deviceIdList", deviceIdList);
                        jsonObject.put("concentratorId", locationControl.getNum());
                        jsonObject.put("groupNoList", groupNoList);
                        jsonArray.add(jsonObject);
                    }
                }
                if (!jsonArray.isEmpty()) {
                    try {
                        //批量解除绑定
                        JSONObject relieveRegisterResult = JSON.parseObject(HttpUtil.post(httpSlApi.getUrl() + httpSlApi.getRelieveRegister(), jsonArray.toJSONString(), headMap));
                        Integer code = (Integer) relieveRegisterResult.get("code");
                        if (code == 400) {
                            logger.error("批量解除绑定失败" + idList);
                            return new Result().error("批量解除绑定失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("批量解除绑定失败" + idList);
                        return new Result().error("批量解除绑定失败");
                    }
                }
            }
            int result = baseMapper.deleteBatchIds(idList);
            if (result < 1) {
                logger.info("deleteOfBatchControlLoop - 批量删除集控分组 loopIdList=[{}]", loopIdList);
                return new Result().error("批量删除集控分组");
            }
            // 解除集控分组和设备之间的关联
            LambdaQueryWrapper<ControlLoopDevice> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(ControlLoopDevice::getLoopId, idList);
            controlLoopDeviceService.remove(wrapper);
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result getControlLoopByGroupIdList(List<Integer> groupIdList, HttpServletRequest request) {
        logger.info("getControlLoopByGroupIdList - 根据分组id集合查询回路信息 groupIdList=[{}]", groupIdList);
        List<ControlLoop> controlLoopList = baseMapper.selectBatchIds(groupIdList);
        Result<List<ControlLoop>> result = new Result<>();
        return result.success(controlLoopList);
    }

    @Override
    public Result nameUniqueness(Integer id, String name) {
        logger.info("nameUniqueness - 分组名称唯一性校验 id={} name={}", id, name);
        Result<Integer> result = new Result<>();
        int isUniqueness = 0;
        LambdaQueryWrapper<ControlLoop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ControlLoop::getName, name);
        ControlLoop controlLoop = baseMapper.selectOne(wrapper);
        // 编辑时判重
        if (id != null) {
            if (controlLoop != null && !controlLoop.getId().equals(id)) {
                isUniqueness = 1;
                return result.error("分组名称已存在", isUniqueness);
            }
        } else if (controlLoop != null) {
            isUniqueness = 1;
            return result.error("分组名称已存在", isUniqueness);
        }
        return result.success(isUniqueness);
    }

    @Override
    public Result uniqueness(DlmReqControlLoopVO loopVO) {
        logger.info("uniqueness - 设备名称，通讯地址，序号唯一性校验 loopVO={}", loopVO);
        Result<Integer> result = new Result<>();
        int isUniqueness = 0;
        LambdaQueryWrapper<ControlLoop> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(ControlLoop::getName, loopVO.getName());
        wrapper1.last("limit 1");
        ControlLoop controlLoopByName = baseMapper.selectOne(wrapper1);
        LambdaQueryWrapper<ControlLoop> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(ControlLoop::getSn, loopVO.getSn());
        wrapper2.last("limit 1");
        ControlLoop controlLoopBySn = baseMapper.selectOne(wrapper2);
        LambdaQueryWrapper<ControlLoop> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(ControlLoop::getOrders, loopVO.getOrders());
        wrapper3.last("limit 1");
        ControlLoop controlLoopByOrders = baseMapper.selectOne(wrapper3);
        // 编辑，由于不能编辑这些字段，所以没有必要进行校验，直接返回不存在即可
        if (loopVO.getId() != null) {
            return result.success(isUniqueness);
        } else if (controlLoopByName != null) {
            isUniqueness = 1;
            return result.error("设备名称已存在", isUniqueness);
        } else if (controlLoopBySn != null) {
            isUniqueness = 2;
            return result.error("通讯地址已存在", isUniqueness);
        } else if (controlLoopByOrders != null) {
            isUniqueness = 3;
            return result.error("序号已存在", isUniqueness);
        }
        return result.success(isUniqueness);
    }

    @Override
    public List<ControlLoopDTO> getControlLoopByIdList(List<Integer> loopIdList) {
        logger.info("getControlLoopByIdList - 根据回路id集合查询回路和集控相关信息 loopIdList={}", loopIdList);
        return baseMapper.selectControlLoopByIdList(loopIdList);
    }
}