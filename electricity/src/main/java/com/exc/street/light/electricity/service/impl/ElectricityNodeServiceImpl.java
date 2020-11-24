package com.exc.street.light.electricity.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.listener.ElectricityNodeImportListener;
import com.exc.street.light.electricity.mapper.CanChannelMapper;
import com.exc.street.light.electricity.mapper.ElectricityNodeMapper;
import com.exc.street.light.electricity.service.*;
import com.exc.street.light.electricity.util.*;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.*;
import com.exc.street.light.resource.entity.electricity.CanChannel;
import com.exc.street.light.resource.entity.electricity.CanDevice;
import com.exc.street.light.resource.entity.electricity.CanModuleType;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.electricity.ElectricityNodeQueryObject;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.RegexUtil;
import com.exc.street.light.resource.vo.electricity.*;
import com.exc.street.light.resource.vo.req.electricity.ReqElectricityNodeUniquenessVO;
import com.exc.street.light.resource.vo.resp.ImportDeviceResultVO;
import com.exc.street.light.resource.vo.resp.electricity.RespElectricityNodeSceneListVO;
import com.exc.street.light.resource.vo.resp.electricity.RespLampPostVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 强电网关服务接口实现类
 *
 * @author Linshiwen
 * @date 2018/5/21
 */
@Service
public class ElectricityNodeServiceImpl extends ServiceImpl<ElectricityNodeMapper, ElectricityNode> implements ElectricityNodeService {

    private static final Logger logger = LoggerFactory.getLogger(ElectricityNodeServiceImpl.class);
    @Autowired
    private ElectricityNodeMapper electricityNodeMapper;

    @Autowired
    private CanDeviceService canDeviceService;
    @Autowired
    private CanChannelMapper canChannelMapper;
    @Autowired
    private CanSceneService canSceneService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LogUserService logUserService;
    @Autowired
    private CanModuleTypeService canModuleTypeService;

    @Autowired
    private CanChannelSceneStatusService canChannelSceneStatusService;
    /*@Autowired
    private UserService userService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ElectricityHeartbeatLogService electricityHeartbeatLogService;
    @Autowired
    private ElectricityEnergyService electricityEnergyService;
    @Autowired
    private PartitionService partitionService;
    @Autowired
    private SystemLogService systemLogService;*/


    @Override
    public void updateNodeOnlineState(Online online) {
        electricityNodeMapper.updateNodeOnlineState(online);
    }

    @Override
    public Result<Integer> uniqueness(ReqElectricityNodeUniquenessVO uniquenessVO, HttpServletRequest request) {
        logger.info("路灯网关-设备唯一性校验,接收参数:ReqElectricityNodeUniquenessVO = {}", uniquenessVO);
        Result<Integer> result = new Result<>();
        Integer id = uniquenessVO.getId();
        String name = uniquenessVO.getName();
        String num = uniquenessVO.getNum();
        String mac = uniquenessVO.getMac().toUpperCase();

        //如果存在id，则为编辑，判断是否是编辑本身
        if (StringUtils.isNotBlank(name)) {
            LambdaQueryWrapper<ElectricityNode> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ElectricityNode::getName, name).last("LIMIT 1");
            ElectricityNode node = this.getOne(wrapper);
            if ((id != null && node != null && !node.getId().equals(id)) || (id == null && node != null)) {
                logger.info("设备名称 {} 已存在,请重新输入", name);
                return result.error("设备名称已存在,请重新输入", 1);
            }
        }
        if (StringUtils.isNotBlank(num)) {
            LambdaQueryWrapper<ElectricityNode> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ElectricityNode::getNum, num).last("LIMIT 1");
            ElectricityNode node = this.getOne(wrapper);
            if ((id != null && node != null && !node.getId().equals(id)) || (id == null && node != null)) {
                logger.info("设备编号 {} 已存在,请重新输入", num);
                return result.error("设备编号已存在,请重新输入", 2);
            }
        }
        if (StringUtils.isNotBlank(mac)) {
            LambdaQueryWrapper<ElectricityNode> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ElectricityNode::getMac, mac).last("LIMIT 1");
            ElectricityNode node = this.getOne(wrapper);
            if ((id != null && node != null && !node.getId().equals(id)) || (id == null && node != null)) {
                logger.info("mac地址 {} 已存在,请重新输入", mac);
                return result.error("mac地址已存在,请重新输入", 3);
            }
        }
        return result.success("唯一性校验通过", 0);
    }

    @Override
    public Result<ImportDeviceResultVO> batchImport(MultipartFile file, HttpServletRequest request) {
        logger.info("批量导入路灯网关设备");
        Result<ImportDeviceResultVO> result = new Result<>();
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (!".xls".equals(suffix) && !".xlsx".equals(suffix)) {
            return result.error("批量导入失败,文件格式错误", null);
        }
        //根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean isAdmin = logUserService.isAdmin(userId);
        Integer areaId = null;
        if (!isAdmin) {
            areaId = user.getAreaId();
        }
        //获取当前区域的灯杆及绑定信息
        List<RespLampPostVO> lampPostInfoByAreaId = baseMapper.getLampPostInfoByAreaId(areaId);
        //当前区域的所有灯杆信息 key:灯杆名称 value:对应ID
        Map<String, Integer> lampPostAllInfoMap = lampPostInfoByAreaId.stream()
                .collect(Collectors.toMap(RespLampPostVO::getLampPostName, RespLampPostVO::getLampPostId));
        //当前区域已绑定路灯网关的灯杆信息 key:灯杆名称 value:对应ID
        Map<String, Integer> bindElectricityNodeLampPostInfoMap = lampPostInfoByAreaId.stream()
                .filter(e -> e.getIsBindGateway() != null)
                .collect(Collectors.toMap(RespLampPostVO::getLampPostName, RespLampPostVO::getLampPostId));
        //todo  批量导入
        //获取已存在的 mac地址,设备名称,设备编号
        LambdaQueryWrapper<ElectricityNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ElectricityNode::getMac, ElectricityNode::getName, ElectricityNode::getNum);
        List<ElectricityNode> list = this.list(wrapper);
        Set<String> macSet = list.stream().map(ElectricityNode::getMac).collect(Collectors.toSet());
        Set<String> numSet = list.stream().map(ElectricityNode::getNum).collect(Collectors.toSet());
        Set<String> nameSet = list.stream().map(ElectricityNode::getName).collect(Collectors.toSet());
        list = new ArrayList<>();
        ImportDeviceResultVO resultVO = new ImportDeviceResultVO();
        try {
            EasyExcel.read(file.getInputStream(), ElectricityNodeImportDataDTO.class, new ElectricityNodeImportListener(this, resultVO, bindElectricityNodeLampPostInfoMap, lampPostAllInfoMap, numSet, nameSet, macSet, list)).sheet().doRead();
        } catch (IOException e) {
            logger.error("路灯网关导入失败,e:{}", e.getMessage());
            return result.error("批量导入失败", resultVO);
        }
        if (!list.isEmpty()) {
            this.saveBatch(list);
        }
        logger.info("返回结果:{}", list);
        logger.info("路灯网关导入结束,ElectricityNodeImportResultVO={}", resultVO);
        if (resultVO.getSuccessNum().equals(0)) {
            return result.error("批量导入失败", resultVO);
        } else if (!resultVO.getSuccessNum().equals(0) && !resultVO.getFailNum().equals(0)) {
            return result.error("批量导入部分失败", resultVO);
        }
        return result.success("批量导入成功", resultVO);
    }


    @Override
    public Result query(HttpServletRequest request, ElectricityNodeQueryObject qo) {
        logger.info("判断用户所属分区及是否是超级管理员");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        if (user == null) {
            return new Result().error("请先登录");
        }
        boolean admin = logUserService.isAdmin(userId);
        if (!admin) {
            qo.setAreaId(user.getAreaId());
        }
        //获取网关列表信息
        Page<ElectricityNodeListVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<ElectricityNodeListVO> pageList = electricityNodeMapper.getPageList(page, qo);
        return new Result().success(pageList);
    }

    @Override
    public ElectricityNodeVO getById(Integer id) {
        ElectricityNode node = electricityNodeMapper.selectById(id);
        if (node == null) {
            return null;
        }
        //根据网关id查询设备
        List<CanDevice> devices = canDeviceService.getByNid(id);
        //查询模块类型
        List<CanModuleType> moduleTypes = canModuleTypeService.list();
        ElectricityNodeVO view = new ElectricityNodeVO();
        BeanUtils.copyProperties(node, view);
        //设置灯杆名称
        view.setLampPostName(baseMapper.getLampPostNameByNid(node.getId()));
        List<CanDeviceVO> views = new ArrayList<>();
        //区域名称
        AreaDTO areaInfo = this.getAreaById(node.getId());
        view.setAreaId(areaInfo.getAreaId());
        view.setAreaName(areaInfo.getAreaName());
        //网关设备
        for (CanDevice device : devices) {
            CanDeviceVO canDeviceView = new CanDeviceVO();
            BeanUtils.copyProperties(device, canDeviceView);
            Integer mid = device.getMid();
            //根据模块类型id过滤满足条件的集合
            List<CanModuleType> typeList = moduleTypes.stream().filter(m -> m.getId().equals(mid)).collect(Collectors.toList());
            canDeviceView.setModuleTypeName(typeList.get(0).getName());
            views.add(canDeviceView);
        }
        view.setDevices(views);
        return view;
    }

    @Override
    public AreaDTO getAreaById(Integer nid) {
        if (nid == null) {
            return new AreaDTO();
        }
        AreaDTO areaInfoByNid = baseMapper.getAreaInfoByNid(nid);
        return areaInfoByNid == null ? new AreaDTO() : areaInfoByNid;
    }

    @Override
    public Result modify(ElectricityNode electricityNode, HttpServletRequest request) {
        String name = electricityNode.getName();
        String num = electricityNode.getNum();
        logger.info("创建编辑强电网关:{} 日志", name);
        logger.info("判断网关编号是否也被占用");
        ElectricityNode electricityNode1 = electricityNodeMapper.selectByNum(num);
        if (electricityNode1 != null && !electricityNode1.getId().equals(electricityNode.getId())) {
            return new Result().error("网关编号已被其他网关占用");
        }
        electricityNode.setMac(electricityNode.getMac().toUpperCase().trim());
        logger.info("网关编号未被占用则进行更新");
        electricityNodeMapper.updateById(electricityNode);
        return new Result().success(electricityNode.getId());
    }

    @Override
    public Result add(ElectricityNode electricityNode, HttpServletRequest request) {
        logger.info("创建新增强电网关:{} 日志", electricityNode.getName());
        logger.info("判断网关编号是否也被占用");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        if (userId == null) {
            return new Result().error("请先登录");
        }
        ElectricityNode electricityNode1 = electricityNodeMapper.selectByNum(electricityNode.getNum());
        if (electricityNode1 != null) {
            return new Result().error("网关编号已存在");
        }
        logger.info("网关编号未被占用则新增网关");
        electricityNode.setIsOffline(1);
        electricityNode.setIsOpen(1);
        electricityNode.setOpenDirection(0);
        electricityNode.setOfflineTime(new Date());
        electricityNode.setCreator(userId);
        electricityNode.setCreateTime(new Date());
        //对mac进行大写转换
        electricityNode.setMac(electricityNode.getMac().toUpperCase().trim());
        electricityNodeMapper.insert(electricityNode);
        return new Result().success(electricityNode.getId());
    }

    @Override
    public Result addControl(ElectricityNode electricityNode, HttpServletRequest request) {
        Result result = new Result().success("");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        if (userId == null) {
            return result.error("请先登录");
        }
        logger.info("创建新增网关:{} 日志", electricityNode.getName());
        electricityNode.setIsOffline(1);
        electricityNode.setIsOpen(1);
        electricityNode.setOpenDirection(0);
        electricityNode.setOfflineTime(new Date());
        electricityNode.setCreator(userId);
        electricityNode.setCreateTime(new Date());
        //对mac进行大写转换
        electricityNode.setMac(electricityNode.getMac().toUpperCase().trim());
        electricityNodeMapper.insert(electricityNode);
        //添加模块
        //获取网关点表信息
        int nid = electricityNode.getId();
        ArrayList<GatewayParameter> gatewayParameters = new ArrayList<>();
        GatewayParameter gatewayParameter = new GatewayParameter();
        gatewayParameter.setDeviceName("4路交流接触器模块");
        gatewayParameter.setBatchNumber("162008089999");
        gatewayParameter.setHardWareVersion("0x10");
        gatewayParameter.setModuleDeviceType(5);
        gatewayParameter.setUnitType("EXC-TE2-B0820E");
        gatewayParameter.setSoftwareVersion("0x10");
        gatewayParameter.setDeviceAddress(66);
        gatewayParameters.add(gatewayParameter);
        List<CanDevice> canDeviceList = PointTableUtil.getDeviceList(gatewayParameters);
        List<CanChannel> canChannels = PointTableUtil.getCanChannelList(gatewayParameters);
        //插入网关点表信息至数据库，更新或插入
        if (canDeviceList.size() > 0 && canChannels.size() > 0) {
            Result result1 = this.saveDevice(canDeviceList, nid);
            if (result1.getCode() == Const.CODE_FAILED) {
                return result1.error("数据库导入网关设备信息失败");
            }
            for (CanChannel canChannel : canChannels) {
                int tagId = canChannel.getTagId();
                int sid = canChannel.getSid();
                String name = canChannel.getName();
                logger.info("通过回路名称和监控点类型判断其设备类型");
                if (name.contains("场景模块")) {
                    canChannel.setDsn(ConstantUtil.DEVICE_TYPE_1);
                    canChannel.setCanChannelTypeId(ConstantUtil.CHANNEL_TYPE_3);
                }
                if (name.contains("驱动模块")) {
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
                if (name.contains("电流模块")) {
                    canChannel.setDsn(ConstantUtil.DEVICE_TYPE_5);
                    canChannel.setCanChannelTypeId(ConstantUtil.CHANNEL_TYPE_4);
                    canChannel.setBindChannelControlId(canChannel.getBindChannelControlId());
                    canChannel.setBindAddress(canChannel.getBindAddress());
                }
                if (name.contains("交流接触器模块")) {
                    canChannel.setDsn(ConstantUtil.DEVICE_TYPE_6);
                    canChannel.setCanChannelTypeId(ConstantUtil.CHANNEL_TYPE_5);
                    canChannel.setPhasePosition(ConstantUtil.CHANNEL_BIND_PHASE_POSITION_A);
                }
                //查询设备id
                logger.info("查询时nid:{},canIndex:{}", nid, canChannel.getCanIndex());
                CanDevice canDevice = canDeviceService.getByNidAndIndex(nid, canChannel.getCanIndex());
                logger.info("查询时cabDevuce为:{}", canDevice);
                if (canDevice == null) {
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
        }
        //更新全开全关场景
        //添加全开全关场景
        logger.info("更新全开全关场景");
        List<CanChannel> canChannelList = canChannelMapper.selectSceneByNid(nid);
        List<ControlObject> controlObjects = canChannelMapper.selectControlByNid(nid);
        if (canChannelList != null && canChannelList.size() == 2
                && controlObjects != null && controlObjects.size() > 0) {
            //创建全开场景
            Scene scene1 = new Scene();
            scene1.setSn(canChannelList.get(0).getTagId());
            scene1.setNid(nid);
            scene1.setName("全开");
            for (ControlObject controlObject : controlObjects) {
                controlObject.setTagValue(1);
            }
            scene1.setControlObjects(controlObjects);
            canSceneService.add(0, scene1, request);
            //创建全关场景
            Scene scene2 = new Scene();
            scene2.setSn(canChannelList.get(1).getTagId());
            scene2.setNid(nid);
            scene2.setName("全关");
            for (ControlObject controlObject : controlObjects) {
                controlObject.setTagValue(0);
            }
            scene2.setControlObjects(controlObjects);
            //导入全开全关场景
            canSceneService.add(0, scene2, request);
        }

        //暂不需要获取网关经纬度功能
        /*String address = electricityNode.getMac();
        byte[] bytes = ProtocolUtil.getLongitudeAndLatitude(address);
        byte[] receiveData = SocketClient.sendForSearch(electricityNode.getIp(), electricityNode.getPort(), bytes);
        if (receiveData == null) {
            return result.error("网关插入成功，经纬度获取失败,请检查网络是否正常!");
        }
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.RET_1) {
            GeographyParameter geographyParameter = ProtocolUtil.getGeographyParameter(bytes);
            LongitudeAndLatitude longitudeAndLatitude = new LongitudeAndLatitude();
            BeanUtils.copyProperties(geographyParameter, longitudeAndLatitude);
            longitudeAndLatitude.setNid(nid);
            electricityNodeMapper.updateLongitudeAndLatitudeByNum(longitudeAndLatitude);
        } else {
            return result.error("网关插入成功，经纬度获取失败,请检查网络是否正常!");
        }*/
        //生成网关回路场景状态记录
        canChannelSceneStatusService.generateSceneStatusRecordsByNid(nid);
        return result.success("网关添加成功");
    }

    @Override
    public Result importNode(List<ElectricityNodeImportVO> nodes, HttpServletRequest request) {
        List<ElectricityNode> electricityNodes = new ArrayList<>();
        for (ElectricityNodeImportVO node : nodes) {
            ElectricityNode electricityNode = new ElectricityNode();
            //网关编号
            String num = node.getNum();
            if (StringUtils.isBlank(num) || num.length() > 5) {
                return new Result().error("编号:" + num + "(网关编号)数据格式错误");
            } else {
                ElectricityNode electricityNode1 = electricityNodeMapper.selectByNum(num);
                if (electricityNode1 != null) {
                    return new Result().error(num + "网关编号已存在");
                }
            }
            electricityNode.setNum(num);
            //网关名称
            String name = node.getName();
            if (StringUtils.isBlank(name) || name.length() > 20) {
                return new Result().error("编号:" + num + "的" + name + "(网关名称)数据格式错误");
            }
            electricityNode.setName(name);
            //省
            String province = node.getProvince();
            if (StringUtils.isBlank(province) || province.length() > 20) {
                return new Result().error("编号:" + num + "的" + province + "(省)数据格式错误");
            }
            electricityNode.setProvince(province);
            //市
            String city = node.getCity();
            if (StringUtils.isBlank(city) || city.length() > 20) {
                return new Result().error("编号:" + num + "的" + city + "(市)数据格式错误");
            }
            electricityNode.setCity(city);
            //区
            String district = node.getDistrict();
            if (StringUtils.isBlank(district) || district.length() > 20) {
                return new Result().error("编号:" + num + "的" + district + "(区)数据格式错误");
            }
            electricityNode.setDistrict(district);
            //地址
            String addr = node.getAddr();
            if (StringUtils.isBlank(addr) || addr.length() > 50) {
                return new Result().error("编号:" + num + "的" + addr + "(地址)数据格式错误");
            }
            electricityNode.setAddr(addr);
            //IP
            String ip = node.getIp();
            if (StringUtils.isBlank(ip) || !RegexUtil.isboolIp(ip)) {
                return new Result().error("编号:" + num + "的" + ip + "(IP)数据格式错误");
            }
            electricityNode.setIp(ip);
            //端口
            String port = node.getPort();
            if (StringUtils.isNoneBlank(port)) {
                try {
                    int i = Integer.parseInt(port);
                    if (i < 1 || i > 65535) {
                        return new Result().error("编号:" + num + "的" + port + "(端口)不在1~65535");
                    }
                    electricityNode.setPort(i);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    return new Result().error("编号:" + num + "的" + port + "(端口)数据格式错误");
                }
            } else {
                return new Result().error("编号:" + num + "的" + port + "(端口)数据格式错误");
            }
            //安装位置
            String installAddr = node.getInstallAddr();
            if (StringUtils.isBlank(installAddr) || installAddr.length() > 50) {
                return new Result().error("编号:" + num + "的" + installAddr + "(详细安装地址)数据格式错误");
            }
            electricityNode.setInstallAddr(installAddr);
            //施工单位
            String constructionUnits = node.getConstructionUnits();
            if (StringUtils.isBlank(constructionUnits) || constructionUnits.length() > 50) {
                return new Result().error("编号:" + num + "的" + constructionUnits + "(施工单位)数据格式错误");
            }
            electricityNode.setConstructionUnits(constructionUnits);
            electricityNode.setIsOpen(1);
            electricityNode.setIsOffline(1);
            electricityNode.setOpenDirection(0);
            electricityNode.setOfflineTime(new Date());
            electricityNodes.add(electricityNode);
        }
        for (ElectricityNode electricityNode : electricityNodes) {
            electricityNodeMapper.insert(electricityNode);
        }
        return new Result().success("");
    }

    @Override
    public ElectricityNode get(Integer nid) {
        ElectricityNode node;
        String ip = (String) redisUtil.hmGet(ConstantUtil.REDIS_NODE_KEY, nid + ConstantUtil.REDIS_IP_KEY);
        Integer port = (Integer) redisUtil.hmGet(ConstantUtil.REDIS_NODE_KEY, nid + ConstantUtil.REDIS_PORT_KEY);
        if (ip != null && port != null) {
            logger.info("通过redis缓存查询ip和端口");
            node = new ElectricityNode();
            node.setIp(ip);
            node.setPort(port);
        } else {
            logger.info("通过数据库查询ip和端口");
            node = electricityNodeMapper.selectById(nid);
            redisUtil.hmSet(ConstantUtil.REDIS_NODE_KEY, nid + ConstantUtil.REDIS_IP_KEY, node.getIp());
            redisUtil.hmSet(ConstantUtil.REDIS_NODE_KEY, nid + ConstantUtil.REDIS_PORT_KEY, node.getPort());
        }
        return node;
    }

    @Override
    public Result count(HttpServletRequest request) {
        //获取登录用户信息
        Integer partitionId = null;
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        //判断用户是否是超级管理员  todo
        /*User user = userService.findById(userId);
        boolean admin = userService.isAdmin(userId);
        if (!admin) {
            partitionId = user.getPid();
        }*/
        //查询网关总数
        int nodeNum = electricityNodeMapper.getNodeCount(partitionId);
        //查询在线网关数
        int offLineNum = electricityNodeMapper.countOnLineNum(partitionId);
        //在线率
        long round = 0;
        if (nodeNum != 0) {
            double d = ((double) offLineNum / nodeNum) * 100;
            round = Math.round(d);

        }
        OffLineVO offLineVO = new OffLineVO();
        offLineVO.setNodeNum(nodeNum);
        offLineVO.setOffLineNum(offLineNum);
        offLineVO.setOffLineRate((int) round);
        return new Result().success(offLineVO);
    }

    @Override
    public int getNodeCount(Integer partitionId) {
        return electricityNodeMapper.getNodeCount(partitionId);
    }

    @Override
    public int countOnLineNum(Integer partitionId) {
        return electricityNodeMapper.countOnLineNum(partitionId);
    }

    @Override
    public Result listAll(HttpServletRequest request) {
        //获取登录用户信息
        Integer partitionId = null;
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        //判断用户是否是超级管理员 todo
        /*User user = userService.findById(userId);
        boolean admin = userService.isAdmin(userId);
        if (!admin) {
            partitionId = user.getPid();
        }*/
        List<ElectricityNode> electricityNodes = electricityNodeMapper.listAll(partitionId);
        return new Result().success(electricityNodes);
    }

    @Override
    public List<ElectricityNode> findByPid(Integer partitionId, String name) {
        return electricityNodeMapper.selectByPid(partitionId, name);
    }

    @Override
    public List<ElectricityNode> findBySiteId(Integer siteId, String name) {
        return electricityNodeMapper.selectBySiteId(siteId, name);
    }

    @Override
    public List<ElectricityNode> findByBuildingId(Integer buildingId) {
        return electricityNodeMapper.selectByBuildingId(buildingId);
    }

    @Override
    public Result delete(Integer id, HttpServletRequest request) {
        ElectricityNode node = electricityNodeMapper.selectById(id);
        electricityNodeMapper.deleteById(id);
        logger.info("删除强电网关:{} 日志", node.getName());
        return new Result().success("删除成功");
    }

    @Override
    public Result batchDelete(String ids, HttpServletRequest request) {
        if (StringUtils.isBlank(ids)) {
            return new Result().error("批量删除失败", "参数缺失");
        }
        List<Integer> idList = Stream.of(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        boolean isSuccess = this.removeByIds(idList);
        return isSuccess ? new Result().success("批量删除成功") : new Result().error("批量删除失败");
    }

    @Override
    public Result sceneList(HttpServletRequest request, ElectricityNodeQueryObject qo) {
        logger.info("判断用户所属分区及是否是超级管理员");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        //todo 权限
        User user = logUserService.getById(userId);
        if (user == null) {
            return new Result().error("请先登录");
        }
        boolean admin = logUserService.isAdmin(userId);
        qo.setAreaId(admin ? null : user.getAreaId());
        //获取网关列表信息
        Page<ElectricityNode> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        LambdaQueryWrapper<ElectricityNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ElectricityNode::getId, ElectricityNode::getNum, ElectricityNode::getName, ElectricityNode::getIsOffline);
        IPage<ElectricityNode> electricityNodeIPage = electricityNodeMapper.selectPage(page, wrapper);
        //返回对象
        List<RespElectricityNodeSceneListVO> views = new ArrayList<>();
        Map<Integer, List<CanChannel>> channelMap = new HashMap<Integer, List<CanChannel>>(qo.getPageSize());
        List<Integer> nodeIdList = electricityNodeIPage.getRecords().stream().map(ElectricityNode::getId).collect(Collectors.toList());
        if (!nodeIdList.isEmpty()) {
            List<CanChannel> channels = canChannelMapper.querySceneChannelByNidList(nodeIdList);
            if (channels != null) {
                channelMap = channels.stream().collect(Collectors.groupingBy(CanChannel::getNid));
            }
        }
        for (ElectricityNode record : electricityNodeIPage.getRecords()) {
            RespElectricityNodeSceneListVO view = new RespElectricityNodeSceneListVO();
            BeanUtils.copyProperties(record, view);
            //获取网关对应的场景名称列表
            List<String> sceneNameList = channelMap.get(record.getId()).stream()
                    .filter(e -> e.getCanScene() != null)
                    .map(e -> e.getCanScene().getName())
                    .collect(Collectors.toList());
            view.setScenes(sceneNameList);
            views.add(view);
        }
        IPage<RespElectricityNodeSceneListVO> nodePage = new Page<>();
        nodePage.setRecords(views);
        nodePage.setTotal(electricityNodeIPage.getTotal());
        nodePage.setCurrent(electricityNodeIPage.getCurrent());
        nodePage.setPages(electricityNodeIPage.getPages());
        nodePage.setSize(electricityNodeIPage.getSize());
        return new Result().success(nodePage);
    }


    /**
     * 根据网关id搜索设备信息
     *
     * @param nid
     * @param request
     * @return
     */
    @Override
    public Result searchNodeInfoById(Integer nid, HttpServletRequest request) {
        Result result = new Result().success("");
        //获取强电网关信息
        logger.info("获取强电网关点表信息");
        /*SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_2);
        log.setContent("获取强电网关点表信息");*/
        ElectricityNode node;
        logger.info("通过数据库查询ip和端口");
        node = electricityNodeMapper.selectById(nid);
        redisUtil.hmSet(ConstantUtil.REDIS_NODE_KEY, nid + ConstantUtil.REDIS_IP_KEY, node.getIp());
        redisUtil.hmSet(ConstantUtil.REDIS_NODE_KEY, nid + ConstantUtil.REDIS_PORT_KEY, node.getPort());
        //通过网关IP、port搜索
        String address = node.getMac();
        byte[] bytes = ProtocolUtil.getElectricityTable(address, ConstantUtil.GATEWAY_COM_1);
        logger.info("获取到的IP:{},获取到的端口:{}", node.getIp(), node.getPort());
        byte[] receiveData = SocketClient.sendForSearch(node.getIp(), node.getPort(), bytes);
        if (receiveData == null) {
            /*log.setState(0);
            systemLogService.save(log);*/
            return result.error("socket连接到网关失败或者网关应答超时");
        }
        logger.info("判断获取网关点表信息是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.RET_1) {
            logger.info("执行成功，将网关点表信息保存至数据库");
            //获取网关点表信息
            ArrayList<GatewayParameter> gatewayParameters = (ArrayList<GatewayParameter>) ProtocolUtil.getModuleInfo(receiveData);
            List<CanDevice> canDeviceList = PointTableUtil.getDeviceList(gatewayParameters);
            List<CanChannel> canChannels = PointTableUtil.getCanChannelList(gatewayParameters);
            //插入网关点表信息至数据库，更新或插入
            if (canDeviceList.size() > 0 && canChannels.size() > 0) {
                Result result1 = this.saveDevice(canDeviceList, nid);
                if (result1.getCode() == Const.CODE_FAILED) {
//                    log.setState(0);
//                    systemLogService.save(log);
                    return result1.error("数据库导入网关设备信息失败");
                }
                for (CanChannel canChannel : canChannels) {
                    int tagId = canChannel.getTagId();
                    int sid = canChannel.getSid();
                    String name = canChannel.getName();
                    logger.info("通过回路名称和监控点类型判断其设备类型");
                    if (name.contains("场景模块")) {
                        canChannel.setDsn(ConstantUtil.DEVICE_TYPE_1);
                        canChannel.setCanChannelTypeId(ConstantUtil.CHANNEL_TYPE_3);
                    }
                    if (name.contains("驱动模块")) {
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
                    if (name.contains("电流模块")) {
                        canChannel.setDsn(ConstantUtil.DEVICE_TYPE_5);
                        canChannel.setCanChannelTypeId(ConstantUtil.CHANNEL_TYPE_4);
                        canChannel.setBindChannelControlId(canChannel.getBindChannelControlId());
                        canChannel.setBindAddress(canChannel.getBindAddress());
                    }
                    if (name.contains("交流接触器模块")) {
                        canChannel.setDsn(ConstantUtil.DEVICE_TYPE_6);
                        canChannel.setCanChannelTypeId(ConstantUtil.CHANNEL_TYPE_5);
                        canChannel.setPhasePosition(ConstantUtil.CHANNEL_BIND_PHASE_POSITION_A);
                    }
                    //查询设备id
                    logger.info("查询时nid:{},canIndex:{}", nid, canChannel.getCanIndex());
                    CanDevice canDevice = canDeviceService.getByNidAndIndex(nid, canChannel.getCanIndex());
                    logger.info("查询时cabDevuce为:{}", canDevice);
                    if (canDevice == null) {
                       /* log.setContent("强电模块不存在,导入失败");
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
            }
            //更新全开全关场景
            //添加全开全关场景
            logger.info("更新全开全关场景");
            List<CanChannel> canChannelList = canChannelMapper.selectSceneByNid(nid);
            List<ControlObject> controlObjects = canChannelMapper.selectControlByNid(nid);
            if (canChannelList != null && canChannelList.size() == 2
                    && controlObjects != null && controlObjects.size() > 0) {
                //创建全开场景
                Scene scene1 = new Scene();
                scene1.setSn(canChannelList.get(0).getTagId());
                scene1.setNid(nid);
                scene1.setName("全开");
                for (ControlObject controlObject : controlObjects) {
                    controlObject.setTagValue(1);
                }
                scene1.setControlObjects(controlObjects);
                canSceneService.add(0, scene1, request);
                //创建全关场景
                Scene scene2 = new Scene();
                scene2.setSn(canChannelList.get(1).getTagId());
                scene2.setNid(nid);
                scene2.setName("全关");
                for (ControlObject controlObject : controlObjects) {
                    controlObject.setTagValue(0);
                }
                scene2.setControlObjects(controlObjects);
                //导入全开全关场景
                canSceneService.add(0, scene2, request);
            }
            byte[] bytes1 = ProtocolUtil.getLongitudeAndLatitude(address);
            receiveData = SocketClient.sendForSearch(node.getIp(), node.getPort(), bytes1);
            if (receiveData == null) {
                /*log.setState(0);
                log.setContent("插入网关成功，获取经纬度失败!");
                systemLogService.save(log);*/
                return result.error("网关插入成功，经纬度获取失败,请检查网络是否正常!");
            }
            rtn = AnalysisUtil.getRtn(receiveData);
            if (rtn == ConstantUtil.RET_1) {
                GeographyParameter geographyParameter = ProtocolUtil.getGeographyParameter(bytes);
                LongitudeAndLatitude longitudeAndLatitude = new LongitudeAndLatitude();
                BeanUtils.copyProperties(geographyParameter, longitudeAndLatitude);
                longitudeAndLatitude.setNid(nid);
                electricityNodeMapper.updateLongitudeAndLatitudeByNum(longitudeAndLatitude);
            } else {
                /*log.setState(0);
                log.setContent("插入网关成功，获取经纬度失败!");
                systemLogService.save(log);*/
                return result.error("网关插入成功，经纬度获取失败,请检查网络是否正常!");
            }
        } else {
            logger.error("协议执行失败:" + ProtocolUtil.getRet(rtn));
            /*log.setState(0);
            systemLogService.save(log);*/
            return result.error("协议未成功执行:" + ProtocolUtil.getRet(rtn));
        }
        //systemLogService.save(log);
        return result;
    }

    @Override
    public Result updateLongitudeAndLatitude(HttpServletRequest request, LongitudeAndLatitude longitudeAndLatitude) {
        Result result = new Result();
        int nid = longitudeAndLatitude.getNid();
        Double longitude = longitudeAndLatitude.getLongitude();
        Double latitude = longitudeAndLatitude.getLatitude();
        if (longitude == null || latitude == null) {
            return result.error("经纬度为null");
        }
        //更改状态
        ElectricityNode electricityNode = electricityNodeMapper.selectById(nid);
        String address = electricityNode.getMac();
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        byte[] bytes = ProtocolUtil.setLongitudeAndLatitude(address, longitude, latitude);
        logger.info("经纬度数据:{}", HexUtil.bytesTohex(bytes));
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        if (receiveData == null) {
            return result.error("socket连接到网关失败或者网关应答超时");
        }
        logger.info("判断定时命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.RET_1) {
            electricityNodeMapper.updateLongitudeAndLatitudeByNum(longitudeAndLatitude);
        } else {
            logger.error("协议执行失败:" + ProtocolUtil.getRet(rtn));
            return result.error("协议未成功执行");
        }
        return result.success("执行成功");
    }

    @Override
    public Result getLongitudeAndLatitude(Integer nid, HttpServletRequest request) {
        Result result = new Result().success("");
        //更改状态
        ElectricityNode electricityNode = electricityNodeMapper.selectById(nid);
        String address = electricityNode.getMac();
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        byte[] bytes1 = ProtocolUtil.getLongitudeAndLatitude(address);
        byte[] receiveData = SocketClient.sendForSearch(ip, port, bytes1);
        if (receiveData == null) {
            return result.error("搜索经纬度失败,请检查网络是否正常!");
        }
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.RET_1) {
            GeographyParameter geographyParameter = ProtocolUtil.getGeographyParameter(receiveData);
            LongitudeAndLatitude longitudeAndLatitude = new LongitudeAndLatitude();
            BeanUtils.copyProperties(geographyParameter, longitudeAndLatitude);
            longitudeAndLatitude.setNid(nid);
            electricityNodeMapper.updateLongitudeAndLatitudeByNum(longitudeAndLatitude);
        } else {
            return result.error("搜索经纬度失败,请检查网络是否正常!");
        }
        return result;
    }

    @Override
    public Result<JSONArray> tree(HttpServletRequest request) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result<JSONArray> result = new Result<>();
        logger.info("获取路灯网关下拉列表,接收参数:用户ID={}", userId);
        if (userId == null) {
            return result.error("获取失败,token失效", null);
        }
        User user = logUserService.get(userId);
        Integer areaId = null;
        if (!logUserService.isAdmin(userId)) {
            areaId = user.getAreaId();
        }
        List<RespElectricityNodeVO> respPullDownList = baseMapper.getRespPullDownList(areaId);
        respPullDownList = respPullDownList == null ? new ArrayList<>() : respPullDownList;
        JSONArray rootArr = new JSONArray();
        int loopNum = 4;
        int index = 0;
        String indexSuffix = "id_";
        for (int i = 1; i <= loopNum; i++) {
            JSONObject loopObj = new JSONObject(true);
            loopObj.put("name", "回路" + i);
            loopObj.put("partId", index++);
            JSONArray childrenList = new JSONArray();
            for (RespElectricityNodeVO node : respPullDownList) {
                JSONObject gatewayObj = new JSONObject(true);
                gatewayObj.put("name", node.getName());
                gatewayObj.put("partId", index++);
                gatewayObj.put("controlId_nodeId", i + "_" + node.getId());
                childrenList.add(gatewayObj);
            }
            loopObj.put("childrenList", childrenList);
            rootArr.add(loopObj);
        }
        return result.success("获取列表成功", rootArr);
    }

    /**
     * 设备地址保存类
     *
     * @param canDevices
     * @param nid
     * @return
     */
    private Result saveDevice(List<CanDevice> canDevices, Integer nid) {
        List<Integer> deviceIds = new ArrayList<>();
        List<Integer> oldDeviceIds = new ArrayList<>();
        List<CanDevice> oldDevices = canDeviceService.getByNid(nid);
        for (CanDevice canDevice : oldDevices) {
            oldDeviceIds.add(canDevice.getId());
        }
        for (CanDevice canDevice : canDevices) {
            if (StringUtils.isNotBlank(canDevice.getBatchNumber()) && StringUtils.isNotBlank(canDevice.getModuleType())) {
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
                if (name.contains("电流模块")) {
                    canDevice.setMid(ConstantUtil.MODULE_TYPE_4);
                }
                if (name.contains("交流接触器模块")) {
                    canDevice.setMid(ConstantUtil.MODULE_TYPE_5);
                }
                canDevice.setNid(nid);
                CanDevice canDevice1 = canDeviceService.getByNidAndBatchNumber(nid, canDevice.getBatchNumber());
                logger.info("查询数据库是否已保存该记录:");
                if (canDevice1 == null) {
//                if (canDevice.getAddress() != null) {
//                    int num = canDeviceService.countByAddress(canDevice.getAddress());
//                    if (num > 0) {
//                        logger.info("查询mac地址是否存在");
//                        return new Result().error("设备物理地址已存在");
//                    }
//                }
                    logger.info("不存在则新增记录");
                    canDeviceService.save(canDevice);
                } else {
                    canDeviceService.updateById(canDevice);
                    deviceIds.add(canDevice1.getId());
                }
            } else {
                logger.error("搜索网关出错，错误原因:获取设备参数为null");
            }
        }
        //删除可能存在的冗余数据
        oldDeviceIds.removeAll(deviceIds);
        for (int i = 0; i < oldDeviceIds.size(); ++i) {
            canDeviceService.removeById(oldDeviceIds.get(i));
        }
        return new Result().success("");
    }
}
