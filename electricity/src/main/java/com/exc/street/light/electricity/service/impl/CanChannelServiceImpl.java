package com.exc.street.light.electricity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanChannelMapper;
import com.exc.street.light.electricity.service.*;
import com.exc.street.light.electricity.task.CanChannelTask;
import com.exc.street.light.electricity.util.*;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.ChangeDataTO;
import com.exc.street.light.resource.dto.electricity.ControlChannelTagIdInfo;
import com.exc.street.light.resource.dto.electricity.ControlCommand;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.entity.electricity.*;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.electricity.CanChannelQueryObject;
import com.exc.street.light.resource.utils.HttpClientUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.electricity.*;
import com.exc.street.light.resource.vo.req.electricity.ReqCanChannelControlVO;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * 强电回路服务接口实现类
 *
 * @author Linshiwen
 * @date 2018/05/23
 */
@Service
public class CanChannelServiceImpl extends ServiceImpl<CanChannelMapper, CanChannel> implements CanChannelService {
    private static final Logger logger = LoggerFactory.getLogger(CanChannelServiceImpl.class);
    @Autowired
    private CanChannelMapper canChannelMapper;
    @Autowired
    private CanDeviceService canDeviceService;
    @Autowired
    private ElectricityNodeService nodeService;
    @Autowired
    private CanSceneService canSceneService;
    @Autowired
    private CanChannelTypeService canChannelTypeService;
    @Autowired
    private CanTimingService canTimingService;

    private HttpClientUtil httpClientUtil = new HttpClientUtil();
    @Value("${http.webSocket}")
    private String webSocket;
    @Autowired
    private CanControlObjectService canControlObjectService;
    @Autowired
    private ComDeviceService comDeviceService;
    @Autowired
    private LogUserService logUserService;
    @Lazy
    @Autowired
    private CanStrategyService canStrategyService;
    @Lazy
    @Autowired
    private CanChannelStrategyHistoryService canChannelStrategyHistoryService;


    @Override
    @Async
    public void updateState(ElectricityNode node) {
        Integer nid = node.getId();
        LambdaQueryWrapper<CanChannel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CanChannel::getNid, nid).eq(CanChannel::getSid, 4);
        List<CanChannel> canChannels = canChannelMapper.selectList(wrapper);
        for (CanChannel channel : canChannels) {
            LambdaQueryWrapper<CanDevice> deviceWrapper = new LambdaQueryWrapper<>();
            deviceWrapper.eq(CanDevice::getNid, nid).select(CanDevice::getId, CanDevice::getAddress);
            deviceWrapper.last("limit 1");
            CanDevice canDevice = canDeviceService.getOne(deviceWrapper);
            if (canDevice == null) {
                continue;
            }
            String address = canDevice.getAddress();
            CanChannel canChannel = canChannelMapper.selectByTagIdAndNid(channel.getTagId() + 1, channel.getNid());
            //查询电流值
            byte[] byDevice = ProtocolUtil.getDataByDevice(address, channel.getTagId());
            byte[] bytes = SocketClient.send(node.getIp(), node.getPort(), byDevice);
            if (bytes != null) {
                byte rtn = AnalysisUtil.getRtn(bytes);
                if (rtn == ConstantUtil.CONTROL_IDENTIFIER2_12) {
                    List<Double> list = AnalysisUtil.getDeviceData(bytes);
                    Double value = list.get(0);
                    channel.setValue(value);
                    if (value.equals(0D)) {
                        canChannel.setValue(value);
                    } else {
                        Double aDouble = list.get(1);
                        BigDecimal bg1 = new BigDecimal(aDouble);
                        aDouble = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        canChannel.setValue(aDouble);
                    }
                    canChannelMapper.updateById(channel);
                    canChannelMapper.updateById(canChannel);
                }
            }
        }
    }

    @Override
    public List<CanChannelListVO> getByNid(Integer nid) {
        //根据节点id获取该节点的回路
        List<CanChannel> channels = canChannelMapper.getByNid(nid);
        List<CanChannelListVO> views = new ArrayList<>();
        //回路类型
        List<CanChannelType> channelTypes = canChannelTypeService.list();
        if (channels != null) {
            for (CanChannel channel : channels) {
                //设置回路列表对象属性
                CanChannelListVO view = new CanChannelListVO();
                BeanUtils.copyProperties(channel, view);
                Integer canChannelTypeId = channel.getCanChannelTypeId();
                //回路类型
                if (canChannelTypeId != null) {
                    List<CanChannelType> typeList = channelTypes.stream().filter(a -> a.getId().equals(canChannelTypeId)).collect(Collectors.toList());
                    view.setChannelTypeName(typeList.get(0).getName());
                }
                view.setCanDeviceName(channel.getCanDevice().getName());
                view.setNodeName(channel.getElectricityNode().getName());
                views.add(view);
            }
        }
        return views;
    }

    @Override
    public Result saveList(File dest, Integer nid, HttpServletRequest request) {
        logger.info("删除原有设备");
        comDeviceService.deleteByNid(nid);
        canDeviceService.deleteByNid(nid);
        List<CanDevice> canDevices = ReadTxtUtil.readCanDevice(dest);
        String address = ReadTxtUtil.readAddress(dest);
        logger.info("新增强电模块设备");
        Result result1 = this.saveDevice(canDevices, address, nid);
        if (result1.getCode() == Const.CODE_FAILED) {
            return result1;
        }
        //SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_1);
        List<CanChannel> canChannels = ReadTxtUtil.readCanChannel(dest);
        for (CanChannel canChannel : canChannels) {
            int tagId = canChannel.getTagId();
            int sid = canChannel.getSid();
            String name = canChannel.getName();
            logger.info("通过回路名称和监控点类型判断其设备类型");
            if (sid == ConstantUtil.SUPERVISION_TYPE_11) {
                canChannel.setDsn(ConstantUtil.DEVICE_TYPE_1);
                canChannel.setCanChannelTypeId(ConstantUtil.CHANNEL_TYPE_3);
            }
            if (name.contains("驱动模块") || sid == ConstantUtil.SUPERVISION_TYPE_4) {
                canChannel.setDsn(ConstantUtil.DEVICE_TYPE_2);
                canChannel.setCanChannelTypeId(ConstantUtil.CHANNEL_TYPE_2);
            }
            if (name.contains("输入模块")) {
                canChannel.setCanChannelTypeId(ConstantUtil.CHANNEL_TYPE_1);
                switch (sid) {
                    case ConstantUtil.SUPERVISION_TYPE_1:
                        canChannel.setDsn(ConstantUtil.DEVICE_TYPE_3);
                        break;
                    case ConstantUtil.SUPERVISION_TYPE_2:
                        canChannel.setDsn(ConstantUtil.DEVICE_TYPE_4);
                        break;
                    default:
                }
            }
            //查询设备id
            CanDevice canDevice = canDeviceService.getByNidAndIndex(nid, canChannel.getCanIndex());
            if (canDevice == null) {
                /*log.setContent("强电模块不存在,导入失败");
                log.setState(0);
                systemLogService.save(log);*/
                return new Result().error("强电模块设备不存在,请先新增强电设备");
            }
            canChannel.setDid(canDevice.getId());
            CanChannel channel = canChannelMapper.getByTagIdAndNid(nid, tagId);
            canChannel.setNid(nid);
            logger.info("查询数据库是否已保存该记录:");
            if (channel == null) {
                logger.info("不存在则新增记录");
                canChannelMapper.insert(canChannel);
            } else {
                //存在则更新
                CanChannel canChannel1 = new CanChannel();
                BeanUtils.copyProperties(canChannel, canChannel1);
                canChannel1.setId(channel.getId());
                canChannelMapper.updateById(canChannel1);
            }
        }
        //导入电表设备和电表回路
        List<ComDevice> comDevices = ReadTxtUtil.readComDevice(dest);
        List<ComChannel> comChannels = ReadTxtUtil.readComChannel(dest);
        comDeviceService.importDevice(comDevices, comChannels, nid);

        //log.setContent("导入强电回路成功");
        //systemLogService.save(log);
        return new Result().success("导入强电回路成功");
    }

    /**
     * 设备地址保存类
     *
     * @param canDevices
     * @param address
     * @param nid
     * @return
     */
    private Result saveDevice(List<CanDevice> canDevices, String address, Integer nid) {
        CanDevice canDevice2 = canDevices.get(0);
        canDevice2.setAddress(address);
        for (CanDevice canDevice : canDevices) {
            String name = canDevice.getName();
            if (name.contains("场景模块")) {
                canDevice.setMid(ConstantUtil.MODULE_TYPE_1);
            }
            if (name.contains("驱动模块")) {
                canDevice.setMid(ConstantUtil.MODULE_TYPE_2);
            }
            if (name.contains("输入模块")) {
                canDevice.setMid(ConstantUtil.MODULE_TYPE_3);
            }
            canDevice.setNid(nid);
            CanDevice canDevice1 = canDeviceService.getByNidAndIndex(nid, canDevice.getCanIndex());
            logger.info("查询数据库是否已保存该记录:");
            if (canDevice1 == null) {
                if (canDevice.getAddress() != null) {
                    int num = canDeviceService.countByAddress(canDevice.getAddress());
                    if (num > 0) {
                        logger.info("查询mac地址是否存在");
                        return new Result().error("设备物理地址已存在");
                    }
                }
                logger.info("不存在则新增记录");
                canDeviceService.save(canDevice);
            }
        }
        return new Result().success("");
    }

    @Override
    public Result control(ControlVO controlVO, HttpServletRequest request) {
        logger.info("路灯网关-控制回路开关，接收参数：ControlVO={}", controlVO);
        Result result = new Result();
        if (controlVO == null || controlVO.getCanChannelControls() == null || controlVO.getCanChannelControls().isEmpty()) {
            logger.error("路灯网关-控制回路开关失败，接收参数：ControlVO={}", controlVO);
            return result.error("控制失败，参数缺失", null);
        }
        //下发失败的网关名称集合
        List<String> issueFailNameList = new ArrayList<>();
        //离线的网关名称集合
        List<String> offlineNameList = new ArrayList<>();
        //下发无响应的路灯网关名称集合
        List<String> noRespNameList = new ArrayList<>();
        List<CanChannelControl> canChannelControls = controlVO.getCanChannelControls();
        //根据网关ID分组
        Map<Integer, List<CanChannelControl>> nodeMap = canChannelControls.stream().collect(Collectors.groupingBy(CanChannelControl::getNid));
        if (nodeMap.isEmpty()) {
            logger.error("路灯网关-控制回路开关失败，网关参数缺失，接收参数：ControlVO={}", controlVO);
            return result.error("控制失败，网关参数缺失", "");
        }
        List<Integer> nodeIdList = new ArrayList<>(nodeMap.keySet());
        //需要更新状态的回路信息
        List<CanChannel> channelUpdateList = new ArrayList<>();
        //获取网关信息
        LambdaQueryWrapper<ElectricityNode> nodeWrapper = new LambdaQueryWrapper<>();
        nodeWrapper.select(ElectricityNode::getIsOffline, ElectricityNode::getName, ElectricityNode::getId, ElectricityNode::getIp, ElectricityNode::getPort, ElectricityNode::getMac)
                .in(ElectricityNode::getId, nodeIdList);
        List<ElectricityNode> nodeList = nodeService.list(nodeWrapper);
        if (nodeList == null || nodeList.isEmpty()) {
            logger.error("路灯网关-控制回路开关失败，未找到网关记录，接收参数：ControlVO={}", controlVO);
            return result.error("控制失败，未找到网关记录", "");
        }
        for (ElectricityNode node : nodeList) {
            List<CanChannelControl> canChannelControls1 = nodeMap.get(node.getId());
            if (canChannelControls1 == null || canChannelControls1.isEmpty()) {
                issueFailNameList.add(node.getName());
                continue;
            }
            //设备离线，跳过
            if (node.getIsOffline() == null || node.getIsOffline().equals(1)) {
                offlineNameList.add(node.getName());
                continue;
            }
            List<ControlCommand> cmdList = new ArrayList<>();
            for (CanChannelControl controlVo : canChannelControls1) {
                ControlCommand cmd = new ControlCommand();
                cmd.setValue(controlVo.getValue());
                cmd.setControlId(controlVo.getControlId());
                cmd.setTagId(controlVo.getTagId());
                cmd.setDeviceAddress(controlVo.getDeviceAddress());
                cmdList.add(cmd);
            }
            //生成下发命令
            byte[] bytes = ProtocolUtil.setGroupControlCommand(node.getMac(), cmdList);
            //下发命令得到返回结果
            byte[] rtn = SocketClient.send(node.getIp(), node.getPort(), bytes);
            if (rtn == null) {
                //网关无响应
                noRespNameList.add(node.getName());
            } else if (AnalysisUtil.getRtn(rtn) != ConstantUtil.RET_1) {
                //命令下发失败
                issueFailNameList.add(node.getName());
            } else {
                //添加入需要更新的列表
                for (CanChannelControl canChannelControl : canChannelControls1) {
                    CanChannel channel = new CanChannel();
                    channel.setId(canChannelControl.getId());
                    channel.setValue(Double.valueOf(canChannelControl.getValue()));
                    channelUpdateList.add(channel);
                }
            }
        }
        //更新回路开关状态
        if (!channelUpdateList.isEmpty()) {
            this.updateBatchById(channelUpdateList);
        }
        logger.info("路灯网关-控制回路开关成功，接收参数：ControlVO={},失败网关名称={},离线网关名称={}", controlVO, issueFailNameList, offlineNameList);
        JSONObject returnData = new JSONObject(true);
        boolean isAllSuccess = issueFailNameList.isEmpty() && offlineNameList.isEmpty() && noRespNameList.isEmpty();
        returnData.put("isAllSuccess", isAllSuccess);
        returnData.put("offlineList", offlineNameList);
        returnData.put("issueFailList", issueFailNameList);
        returnData.put("noRespNameList", noRespNameList);
        return isAllSuccess ? result.success("下发成功", returnData) : result.error("下发失败", returnData);
    }


    @Override
    public Result getResult(CanChannelControl canChannelControl) {
        Integer nid = canChannelControl.getNid();
        ElectricityNodeVO electricityNode = nodeService.getById(nid);
        if (electricityNode == null) {
            return new Result().error("网关设备不存在");
        }
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        //通过nid获取网关地址
        String address = electricityNode.getMac();
        Result result = new Result().success("");
        String name = electricityNode.getName();
        result.setData(name);
        if (address != null && !"".equals(address)) {
            //设置命令控制对象属性
            ControlCommand controlCommand = new ControlCommand();
            controlCommand.setControlId(canChannelControl.getControlId());
            int value = canChannelControl.getValue();
            int tagId = canChannelControl.getTagId();
            controlCommand.setValue(value == 0 ? 1 : 0);
            Integer deviceAddress = canChannelControl.getDeviceAddress();
            controlCommand.setDeviceAddress(deviceAddress);
            //获取控制命令协议
            byte[] bytes = ProtocolUtil.setControlCommand(address, controlCommand);
            //发送控制命令协议到网关,最多发3次
            byte[] receiveData = null;
            int j = 0;
            CanChannel canChannel = canChannelMapper.selectByTagIdAndNid(tagId, nid);
            for (int i = 0; i < ConstantUtil.MAX_CONNCET; i++) {
                logger.info("连接次数:" + (i + 1));
                receiveData = SocketClient.send(ip, port, bytes);
                j++;
                if (receiveData != null) {
                    break;
                }
            }
            if (j == ConstantUtil.MAX_CONNCET) {
                return result.error("控制失败,socket连接到网关失败或者网关应答超时", canChannel.getName() + ",节点名称:" + name);
            }
            logger.info("判断控制命令是否执行成功");
            //获取设备实时数据协议
            byte rtn = AnalysisUtil.getRtn(receiveData);
            //更新回路值
            if (rtn == ConstantUtil.RET_1) {
                logger.info("控制命令执行成功");
                if (canChannel != null) {
                    canChannel.setValue(Double.valueOf(Integer.toString(value)));
                    canChannelMapper.updateById(canChannel);
                }
            } else {
                logger.info("控制命令执行失败:" + ProtocolUtil.getRet(rtn));
                return result.error("tagId:" + tagId + " 控制失败");
            }
            return result.success(canChannel.getName() + ",节点名称:" + name, name);
        } else {
            return result.error("节点:" + name + ",没有设备");
        }
    }

    @Override
    public List<CanChannel> selectByCondition(CanChannelQueryObject qo) {
        return canChannelMapper.listAll(qo);
    }

    @Override
    public Result getGroupResult(List<ControlCommand> controlCommands, Integer nid) {
        ElectricityNode electricityNode = nodeService.get(nid);
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        //通过can设备id查询mac地址
        CanDevice canDevice = canDeviceService.getAddressByNid(nid);
        if (canDevice == null) {
            Result result = new Result().error("设备不存在");
            result.setData(electricityNode.getName());
            return result;
        }
        String address = electricityNode.getMac();
        //获取群控制命令协议
        if (controlCommands.size() > 0) {
            byte[] bytes = ProtocolUtil.setGroupControlCommand(address, controlCommands);
            //发送控制命令协议到网关,最多发3次
            byte[] receiveData = null;
            int j = 0;
            for (int i = 0; i < ConstantUtil.MAX_CONNCET; i++) {
                logger.info("连接次数:" + (i + 1));
                receiveData = SocketClient.send(ip, port, bytes);
                j++;
                if (receiveData != null) {
                    break;
                }
            }
            if (j == ConstantUtil.MAX_CONNCET) {
                return new Result().error("socket连接到网关失败或者网关应答超时", electricityNode.getName());
            }
            logger.info("判断控制命令是否执行成功");
            //todo
            /*BuildingVO vo = null;
            Integer buildingId = electricityNode.getBuildingId();
            if (buildingId != null) {
                Building building = buildingService.findById(buildingId);
                vo = new BuildingVO();
                BeanUtils.copyProperties(building, vo);
            }*/
            //获取设备实时数据协议
            byte rtn = AnalysisUtil.getRtn(receiveData);
            //更新回路值
            if (rtn == ConstantUtil.RET_1) {
                logger.info("控制命令执行成功");
                for (ControlCommand controlCommand : controlCommands) {
                    CanChannel canChannel = canChannelMapper.selectByTagIdAndNid(controlCommand.getTagId(), nid);
                    if (canChannel != null) {
                        canChannel.setValue(Double.valueOf(Integer.toString(controlCommand.getValue())));
                        canChannelMapper.updateById(canChannel);
                    }
                }
                //todo
                /*if (vo != null) {
                    vo.setState(0);
                    JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
                    logger.info("推送节点控制成功信息:{}", jsonObject);
                    httpClientUtil.doPostJson(webSocket, jsonObject, null);
                }*/
            } else {
                logger.error("控制命令执行失败:" + ProtocolUtil.getRet(rtn));
                //todo
                /*if (vo != null) {
                    vo.setState(1);
                    JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
                    logger.info("推送节点控制失败信息:{}", jsonObject);
                    httpClientUtil.doPostJson(webSocket, jsonObject, null);
                }*/
                return new Result().error("节点:" + electricityNode.getName() + " 群控制失败", electricityNode.getName());
            }
        }
        return new Result().success("");
    }


    @Override
    public List<ControlChannelTagIdInfo> selectInfoByNidAndTagId(Integer tagId, Integer nid) {
        return canChannelMapper.selectInfoByNidAndTagId(tagId, nid);
    }

    @Override
    public List<ControlObject> selectControlByNid(Integer nid) {
        return canChannelMapper.selectControlByNid(nid);
    }

    @Override
    public void handleSwitch(Integer nid, CanChangeData canChangeData) {
        Float value = canChangeData.getValue();
        //状态输出量,更新对应的通道值
        ChangeDataTO to = new ChangeDataTO();
        to.setData(value);
        to.setType(1);
        CanChannel canChannel = canChannelMapper.selectByTagIdAndNidAndAddress(canChangeData.getControlId(), nid, canChangeData.getAddress());
        if (canChannel != null) {
            to.setId(canChannel.getId());
            canChannel.setValue(Double.valueOf(value));
            canChannelMapper.updateById(canChannel);
            logger.info("已发送信息:{}", JSON.toJSONString(to));
            //todo 发送消息
            //socketIoService.sendMessage("changeData", JSON.toJSONString(to));
        }
    }

    @Override
    public void handleCurrent(Integer nid, CanChangeData canChangeData) {
        Float value = canChangeData.getValue();
        ChangeDataTO to = new ChangeDataTO();
        Date nowTime = new Date();
        to.setData(value);
        to.setType(2);
        CanChannel canChannel1 = canChannelMapper.selectByTagIdAndNidAndAddress(canChangeData.getControlId(), nid, canChangeData.getAddress());
        if (canChannel1 != null) {
            canChannel1.setElectricityValue(Double.valueOf(value));
            int tmpAddress = canChannel1.getBindAddress();
            int tmpControlId = canChannel1.getBindChannelControlId();
            if (tmpAddress != 0 && tmpControlId != 0) {
                CanChannel channel = canChannelMapper.selectByTagIdAndNidAndAddress(tmpControlId, nid, tmpAddress);
                if (channel != null) {
                    int phasePosition = canChannelMapper.selectPhasePositionById(channel.getId());
                    Double value1 = Double.valueOf(value);
                    to.setId(canChannel1.getId());
                    if (phasePosition == (canChannel1.getControlId() % 3)) {
                        to.setId(channel.getId());
                        Double value2 = channel.getElectricityValue();
                        int re = value1.compareTo(value2);
                        if (re != 0) {
                            channel.setElectricityUpdateTime(nowTime);
                            channel.setElectricityValue(value1);
                            canChannelMapper.updateById(channel);
                        }
                    }
                }
            } else {
                to.setId(canChannel1.getId());
            }
            canChannel1.setElectricityUpdateTime(nowTime);
            canChannelMapper.updateById(canChannel1);
            logger.info("已发送信息:{}", JSON.toJSONString(to));
            //发送消息 todo
            //socketIoService.sendMessage("changeData", JSON.toJSONString(to));
        }

    }

    @Override
    public Result<JSONObject> allSwitch(List<ReqCanChannelControlVO> reqVoList, boolean isOpen, HttpServletRequest request) {
        logger.info("回路全开全关控制,接收参数:ReqCanChannelControlVO.list={},isOpen={}", reqVoList, isOpen);
        Result<JSONObject> result = new Result<>();
        if (reqVoList == null || reqVoList.isEmpty()) {
            logger.info("回路全开全关控制失败,参数缺失,接收参数:ReqCanChannelControlVO.list={},isOpen={}", reqVoList, isOpen);
            return result.error("控制失败,参数缺失", new JSONObject());
        }
        //下发失败的路灯网关名称集合
        List<String> issueFailNameList = new ArrayList<>();
        //离线的路灯网关名称集合
        List<String> offlineNameList = new ArrayList<>();
        //下发无响应的路灯网关名称集合
        List<String> noRespNameList = new ArrayList<>();
        Map<Integer, List<ReqCanChannelControlVO>> nodeDataMap = reqVoList.stream()
                .filter(e -> e.getControlId() != null && e.getNodeId() != null)
                .collect(Collectors.groupingBy(ReqCanChannelControlVO::getNodeId));
        for (Map.Entry<Integer, List<ReqCanChannelControlVO>> entry : nodeDataMap.entrySet()) {
            List<ReqCanChannelControlVO> controlList = entry.getValue();
            if (controlList == null || controlList.isEmpty()) {
                continue;
            }
            LambdaQueryWrapper<ElectricityNode> nodeWrapper = new LambdaQueryWrapper<>();
            nodeWrapper.select(ElectricityNode::getId, ElectricityNode::getName, ElectricityNode::getIsOffline, ElectricityNode::getMac, ElectricityNode::getIp, ElectricityNode::getPort)
                    .eq(ElectricityNode::getId, entry.getKey());
            ElectricityNode node = nodeService.getOne(nodeWrapper);
            //数据不存在 跳过
            if (node == null) {
                continue;
            }
            String mac = node.getMac();
            String ip = node.getIp();
            Integer port = node.getPort();
            //必要参数缺失或离线 跳过
            if (node.getIsOffline().equals(1) || StringUtils.isAnyBlank(mac, ip) || port == null) {
                offlineNameList.add(node.getName());
                continue;
            }
            List<ControlCommand> cmdList = new ArrayList<>();
            for (ReqCanChannelControlVO controlVO : controlList) {
                ControlCommand cmd = new ControlCommand();
                cmd.setValue(isOpen ? 1 : 0);
                cmd.setDeviceAddress(66);
                cmd.setControlId(controlVO.getControlId());
                cmdList.add(cmd);
            }
            byte[] bytes = ProtocolUtil.setGroupControlCommand(node.getMac(), cmdList);
            byte[] rtn = SocketClient.send(ip, port, bytes);
            if (rtn == null) {
                //网关无响应
                noRespNameList.add(node.getName());
            } else if (AnalysisUtil.getRtn(rtn) != ConstantUtil.RET_1) {
                //命令下发失败
                issueFailNameList.add(node.getName());
            } else {
                //更新回路状态
                List<Integer> controlIdList = controlList.stream().map(ReqCanChannelControlVO::getControlId).collect(Collectors.toList());
                LambdaUpdateWrapper<CanChannel> updateStatusWrapper = new LambdaUpdateWrapper<>();
                updateStatusWrapper.set(CanChannel::getValue, isOpen ? 1 : 0).in(CanChannel::getControlId, controlIdList)
                        .eq(CanChannel::getNid, node.getId());
                this.update(updateStatusWrapper);
            }
        }
        logger.info("回路全开全关控制下发成功,接收参数:ReqCanChannelControlVO.list={},isOpen={}", reqVoList, isOpen);
        JSONObject returnData = new JSONObject(true);
        returnData.put("isAllSuccess", issueFailNameList.isEmpty() && offlineNameList.isEmpty() && noRespNameList.isEmpty());
        returnData.put("offlineList", offlineNameList);
        returnData.put("issueFailList", issueFailNameList);
        returnData.put("noRespNameList", noRespNameList);
        return result.success("下发成功", returnData);
    }

    @Override
    public Result<JSONObject> currentStrategy(String id, HttpServletRequest request) {
        logger.info("获取回路当前绑定场景和历史记录,接收参数：id={}", id);
        Result<JSONObject> result = new Result<>();
        if (id == null) {
            logger.error("获取回路当前绑定场景和历史记录失败,回路ID为null");
            return result.error("获取失败,参数缺失", null);
        }
        LambdaQueryWrapper<CanChannel> channelWrapper = new LambdaQueryWrapper<>();
        channelWrapper.select(CanChannel::getId, CanChannel::getStrategyId).eq(CanChannel::getId, id);
        CanChannel channel = this.getOne(channelWrapper);
        if (channel == null) {
            logger.error("获取回路当前绑定场景和历史记录失败,回路记录不存在,接收参数：id={}", id);
            return result.error("获取失败,记录不存在", null);
        }
        JSONObject dataObj = new JSONObject(true);
        //当前绑定的策略信息
        RespCanStrategyVO currentStrategy = null;
        if (channel.getStrategyId() != null) {
            currentStrategy = canStrategyService.getInfoById(channel.getStrategyId(), request).getData();
        }
        dataObj.put("current", currentStrategy == null ? new RespCanStrategyVO() : currentStrategy);
        //最近的历史记录
        LambdaQueryWrapper<CanChannelStrategyHistory> historyWrapper = new LambdaQueryWrapper<>();
        historyWrapper.eq(CanChannelStrategyHistory::getChannelId, channel.getId())
                .orderByDesc(CanChannelStrategyHistory::getCreateTime)
                .last("limit 1");
        CanChannelStrategyHistory history = canChannelStrategyHistoryService.getOne(historyWrapper);
        RespCanStrategyVO lastStrategy = null;
        if (history != null && history.getStrategyId() != null) {
            lastStrategy = canStrategyService.getInfoById(channel.getStrategyId(), request).getData();
        }
        dataObj.put("last", lastStrategy == null ? new RespCanStrategyVO() : lastStrategy);
        return result.success("获取成功", dataObj);
    }


    @Override
    public List<CanSceneListVO> listByNid(Integer nid) {
        //根据节点id查询场景回路
        List<CanChannel> channels = canChannelMapper.querySceneChannelByNid(nid);
        List<CanSceneListVO> views = new ArrayList<>();
        //根据节点id查询节点定时信息
        int timingNum = canTimingService.countTimingNumByNid(nid);
        //根据节点id查询周期定时信息
        int cycleNum = canTimingService.countCycleNumByNid(nid);
        for (CanChannel channel : channels) {
            //设置场景回路列表对象属性
            CanSceneListVO view = new CanSceneListVO();
            view.setId(channel.getId());
            view.setTagId(channel.getTagId());
            CanScene canScene = channel.getCanScene();
            if (canScene != null) {
                view.setName(canScene.getName());
                //场景canDevice不为空表示已编辑
                view.setIsEdit(1);
            }
            view.setTimingNum(timingNum);
            view.setCycleNum(cycleNum);
            views.add(view);
        }
        return views;
    }

    @Override
    public Result query(CanChannelQueryObject qo) {
        //继电器类型回路列表
        IPage<CanChannelListVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<CanChannelListVO> pageList = canChannelMapper.getPageList(page, qo);
        //根据场景id查询回路
        CanScene canScene = canSceneService.selectBySidAndNid(qo.getSid(), qo.getNid());
        Map<Integer, List<CanControlObject>> controlObjectMap = new HashMap<>();
        if (canScene != null && canScene.getControlObjectList() != null) {
            controlObjectMap = canScene.getControlObjectList().stream()
                    .filter(e -> e.getTagId() != null)
                    .collect(Collectors.groupingBy(CanControlObject::getTagId));
        }
        for (CanChannelListVO record : pageList.getRecords()) {
            List<CanControlObject> canControlObjectList = controlObjectMap.get(record.getControlId());
            if (canControlObjectList == null || canControlObjectList.isEmpty()) {
                continue;
            }
            record.setIsEdit(1);
            record.setValue(canControlObjectList.get(0).getTagValue());
        }
        return new Result().success(pageList);
    }

    @Override
    public Result patchUpdate(CanChannelPatchVO channelPatchVO, HttpServletRequest request) {
        List<CanChannel> canChannels = channelPatchVO.getCanChannels();
        this.updateBatchById(canChannels);
        return new Result().success("修改成功");
    }

    @Override
    public Result listAll(HttpServletRequest request, CanChannelQueryObject qo) {
        logger.info("判断用户所属分区及是否是超级管理员");
        //判断用户是否是超级管理员
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        //权限
        User user = logUserService.getById(userId);
        if (user == null) {
            return new Result().error("请先登录");
        }
        boolean admin = logUserService.isAdmin(userId);
        qo.setAreaId(admin ? null : user.getAreaId());
        //继电器类型回路列表
        IPage<CanChannelListVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<CanChannelListVO> pageList = canChannelMapper.getPageList(page, qo);
        //回路类型
        List<CanChannelType> channelTypes = canChannelTypeService.list();
        for (CanChannelListVO view : pageList.getRecords()) {
            if (view.getStrategyId() == null) {
                view.setStrategyId("");
            }
            //设置回路列表对象属性
            //上面的方法为合广在使用，不知为何+1
            //CanChannel canChannel = canChannelMapper.selectByTagIdAndNid(channel.getTagId() + 1, nid);
            CanChannel canChannel = canChannelMapper.selectByTagIdAndNid(view.getTagId(), view.getNid());
            view.setCurrentValue(canChannel.getElectricityValue() == null ? 0.0 : canChannel.getElectricityValue());
            //回路类型
            if (view.getCanChannelTypeId() != null) {
                List<CanChannelType> typeList = channelTypes.stream().filter(a -> a.getId()
                        .equals(view.getCanChannelTypeId())).collect(Collectors.toList());
                if (typeList.isEmpty()) {
                    continue;
                }
                view.setChannelTypeName(typeList.get(0).getName());
            }
        }
        return new Result().success(pageList);
    }

    @Override
    //@Scheduled(cron = "0 0 15 * * ?")
    public void readCurrent() {
        //继电器类型回路列表
        CanChannelQueryObject qo = new CanChannelQueryObject();
        qo.setPageSize(0);
        List<CanChannel> channels = canChannelMapper.listAll(qo);
        //回路类型
        for (CanChannel channel : channels) {
            Integer nid = channel.getNid();
            CanDevice canDevice = canDeviceService.getAddressByNid(channel.getNid());
            String address = canDevice.getAddress();
            ElectricityNode electricityNode = nodeService.get(nid);
            CanChannel canChannel = canChannelMapper.selectByTagIdAndNid(channel.getTagId() + 1, channel.getNid());
            //查询电流值
            byte[] byDevice = ProtocolUtil.getDataByDevice(address, channel.getTagId());
            byte[] bytes = SocketClient.send(electricityNode.getIp(), electricityNode.getPort(), byDevice);
            if (bytes != null) {
                byte rtn = AnalysisUtil.getRtn(bytes);
                if (rtn == ConstantUtil.CONTROL_IDENTIFIER2_12) {
                    List<Double> list = AnalysisUtil.getData(bytes);
                    Double value = list.get(0);
                    channel.setValue(value);
                    if (value.equals(0D)) {
                        canChannel.setValue(value);
                    } else {
                        Double aDouble = list.get(1);
                        BigDecimal bg1 = new BigDecimal(aDouble);
                        aDouble = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        canChannel.setValue(aDouble);
                    }
                    canChannelMapper.updateById(channel);
                    canChannelMapper.updateById(canChannel);
                }
            }
        }
    }

    @Override
    public Result controlScene(ControlVO controlVO, HttpServletRequest request) {
        logger.info("获取任务集合");
        Collection<CanChannelTask> tasks = new ArrayList<>();
        List<CanChannelControl> canChannelControls = controlVO.getCanChannelControls();
        for (CanChannelControl canChannelControl : canChannelControls) {
            CanChannelTask task = new CanChannelTask(canChannelControl, this);
            tasks.add(task);
        }
        //开启多线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("channel-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(ConstantUtil.THREADPOOLSIZE_2,
                ConstantUtil.THREADPOOLSIZE_2, 0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), namedThreadFactory);
        logger.info("运行任务");
        Result result = this.getResult(tasks, executorService);
        /*SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_2);
        log.setContent("控制场景执行");
        if (result.getCode() == ResultCode.FAIL.code) {
            log.setState(0);
        }
        systemLogService.save(log);*/
        return result;
    }

    @Override
    public Result getResult(Collection tasks, ExecutorService executorService) {
        DownLoadInformationVO vo = null;
        try {
            List<Future<Result>> futures = executorService.invokeAll(tasks);
            vo = new DownLoadInformationVO();
            int successNum = 0;
            int defaultNum = 0;
            List<String> nodeNames = new ArrayList<>();
            for (Future<Result> future : futures) {
                Result result = future.get();
                logger.info(result.getData() + ":" + result.getMessage());
                if (result.getCode() == Const.CODE_SUCCESS) {
                    successNum += 1;
                } else {
                    nodeNames.add((String) result.getData());
                    defaultNum += 1;
                }
            }
            vo.setSuccessNum(successNum);
            vo.setDefaultNum(defaultNum);
            vo.setNodeNames(nodeNames);
            if (vo.getDefaultNum() > 0) {
                return new Result().error("节点控制失败", vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            executorService.shutdown();
        }
        return new Result().success("", vo);
    }

    @Override
    public Result getSceneControlResult(CanChannelControl canChannelControl) {
        Result result = new Result().success("");
        int nid = canChannelControl.getNid();
        ElectricityNode electricityNode = nodeService.get(nid);
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        //通过nid获取网关地址
        String address = electricityNode.getMac();
        //当类型为3时，立即执行场景，自研新增
        int tagId = canChannelControl.getTagId();
        List<ControlObject> controlObjects = canControlObjectService.selectBySceneIdAndNid(nid, tagId);
        //获取设置场景协议
        byte[] bytes = ProtocolUtil.setControlNow(address, controlObjects);
        //发送设置场景协议到网关
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        result.setData(electricityNode.getName());
        if (receiveData == null) {
            return result.error("socket连接到网关失败或者网关应答超时");
        }
        logger.info("判断定时命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.RET_1) {
            return result.success("执行成功");
        } else {
            logger.error("协议执行失败:" + ProtocolUtil.getRet(rtn));
            return result.error("协议未成功执行");
        }
    }
}
