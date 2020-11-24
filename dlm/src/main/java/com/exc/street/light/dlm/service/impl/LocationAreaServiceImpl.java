/**
 * @filename:LocationAreaServiceImpl 2020-03-16
 * @project dlm  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.config.parameter.*;
import com.exc.street.light.dlm.mapper.LampCityDao;
import com.exc.street.light.dlm.mapper.LocationAreaMapper;
import com.exc.street.light.dlm.service.*;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.*;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.qo.DlmControlLoopOfDeviceQuery;
import com.exc.street.light.resource.qo.DlmLocationControlQuery;
import com.exc.street.light.resource.utils.BaseConstantUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.DlmReqLocationAreaVO;
import com.exc.street.light.resource.vo.req.SlReqLightControlVO;
import com.exc.street.light.resource.vo.resp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class LocationAreaServiceImpl extends ServiceImpl<LocationAreaMapper, LocationArea> implements LocationAreaService {
    private static final Logger logger = LoggerFactory.getLogger(LocationAreaServiceImpl.class);

    @Resource
    private LocationAreaMapper locationAreaMapper;
    @Autowired
    private LocationStreetService locationStreetService;
    @Autowired
    private LocationSiteService locationSiteService;
    @Autowired
    private SlLampPostService slLampPostService;


    @Autowired
    private AhDeviceService ahDeviceService;
    @Autowired
    private MeteorologicalDeviceService meteorologicalDeviceService;
    @Autowired
    private RadioDeviceService radioDeviceService;
    @Autowired
    private ScreenDeviceService screenDeviceService;
    @Autowired
    private LampDeviceService lampDeviceService;
    @Autowired
    private SingleLampParamService singleLampParamService;
    @Autowired
    private SsDeviceService ssDeviceService;
    @Autowired
    private WifiApDeviceService wifiApDeviceService;

    @Autowired
    private HttpSlApi httpSlApi;
    @Autowired
    private HttpSsApi httpSsApi;
    @Autowired
    private HttpIrApi httpIrApi;
    @Autowired
    private HttpOccApi httpOccApi;
    @Autowired
    private HttpWifiApi httpWifiApi;
    @Autowired
    private HttpPbApi httpPbApi;
    @Autowired
    private HttpEmApi httpEmApi;
    @Autowired
    private HttpUaApi httpUaApi;
    @Autowired
    private LampCityDao lampCityDao;
    @Autowired
    private LogUserService logUserService;

    @Autowired
    private LocationControlService locationControlService;

    @Autowired
    private LocationControlTypeService locationControlTypeService;

    @Autowired
    private ControlLoopDeviceService controlLoopDeviceService;

//    @Autowired
//    private ProjectPicService projectPicService;

    @Autowired
    private ElectricityNodeService electricityNodeService;

    @Override
    public Result getLampCity(HttpServletRequest request) {
        LampCity lampCity = lampCityDao.selectById(1);
        return new Result().success(lampCity);
    }

    @Override
    public Result cityControl(HttpServletRequest request, SlReqLightControlVO vo) {
        List<LocationArea> locationAreaList = this.list();
        List<Integer> areaIdList = locationAreaList.stream().map(LocationArea::getId).collect(Collectors.toList());
        vo.setAreaIdList(areaIdList);
        this.wholeControl(request, vo);
        //更改市级灯具信息
        LampCity lampCity = new LampCity();
        lampCity.setId(1);
        lampCity.setLampState(vo.getType());
        lampCity.setLampBrightness(vo.getLightness());
        lampCityDao.updateById(lampCity);
        return new Result().success("操作成功");
    }

    @Override
    public Result wholeControl(HttpServletRequest request, SlReqLightControlVO vo) {
        logger.info("整体控制，接收参数：vo=" + vo);
        Result<SlRespControlVO> result = new Result<>();
        //获取参数
        List<Integer> areaIdList = vo.getAreaIdList();
        List<Integer> streetIdList = vo.getStreetIdList();
        List<Integer> siteIdList = vo.getSiteIdList();
        Integer type = vo.getType();
        Integer lightness = vo.getLightness();

        List<LocationArea> locationAreaList = new ArrayList<>();
        List<LocationStreet> locationStreetList = new ArrayList<>();
        List<LocationSite> locationSiteList = new ArrayList<>();
        if (siteIdList != null && siteIdList.size() > 0) {
            //修改站点灯具状态及亮度
            updateSite(siteIdList, type, lightness);
        } else if (streetIdList != null && streetIdList.size() > 0) {
            //通过街道id获取站点id集合
            Result byStreet = locationSiteService.getByStreet(streetIdList, request);
            locationSiteList = (List<LocationSite>) byStreet.getData();
            siteIdList = locationSiteList.stream().map(LocationSite::getId).collect(Collectors.toList());
            //修改站点灯具状态及亮度
            updateSite(siteIdList, type, lightness);

            //修改街道灯具状态及亮度
            updateStreet(streetIdList, type, lightness);
        } else if (areaIdList != null && areaIdList.size() > 0) {
            //通过区域id获取街道id集合
            Result byArea = locationStreetService.getByArea(areaIdList, request);
            locationStreetList = (List<LocationStreet>) byArea.getData();
            streetIdList = locationStreetList.stream().map(LocationStreet::getId).collect(Collectors.toList());
            //修改街道灯具状态及亮度
            updateStreet(streetIdList, type, lightness);

            //通过街道id获取站点id集合
            Result byStreet = locationSiteService.getByStreet(streetIdList, request);
            locationSiteList = (List<LocationSite>) byStreet.getData();
            siteIdList = locationSiteList.stream().map(LocationSite::getId).collect(Collectors.toList());
            //修改站点灯具状态及亮度
            updateSite(siteIdList, type, lightness);

            //修改区域灯具状态及亮度
            for (Integer integer : areaIdList) {
                LocationArea locationArea = new LocationArea();
                locationArea.setId(integer);
                locationArea.setLampState(type);
                locationArea.setLampBrightness(lightness);
                locationAreaList.add(locationArea);
            }
            this.updateBatchById(locationAreaList);
        }

        if (siteIdList.isEmpty()) {
            return new Result().error("当前选中区域无信息");
        }
        vo.setSiteIdList(siteIdList);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("token", request.getHeader("token"));
        headerMap.put("Content-Type", "application/json");
        String body = JSON.toJSONString(vo);
        //调用sl控制接口
        String post = HttpUtil.post(httpSlApi.getUrl() + httpSlApi.getControl(), body, headerMap);
        if (!post.isEmpty()) {
            result = JSON.parseObject(post, Result.class);
        }
        return result;
    }

    /**
     * 修改站点灯具状态及亮度
     *
     * @param siteIdList
     * @param type
     * @param lightness
     */
    public void updateSite(List<Integer> siteIdList, Integer type, Integer lightness) {
        List<LocationSite> locationSiteList = new ArrayList<>();
        for (Integer integer : siteIdList) {
            LocationSite locationSite = new LocationSite();
            locationSite.setId(integer);
            locationSite.setLampState(type);
            locationSite.setLampBrightness(lightness);
            locationSiteList.add(locationSite);
        }
        locationSiteService.updateBatchById(locationSiteList);
    }

    /**
     * 修改街道灯具状态及亮度
     *
     * @param streetIdList
     * @param type
     * @param lightness
     */
    public void updateStreet(List<Integer> streetIdList, Integer type, Integer lightness) {
        List<LocationStreet> locationStreetList = new ArrayList<>();
        for (Integer integer : streetIdList) {
            LocationStreet locationStreet = new LocationStreet();
            locationStreet.setId(integer);
            locationStreet.setLampState(type);
            locationStreet.setLampBrightness(lightness);
            locationStreetList.add(locationStreet);
        }
        locationStreetService.updateBatchById(locationStreetList);
    }

    @Override
    public Result getList(Integer hierarchy, Integer deviceTypeNum, Integer eliminate, Integer pageNum, Integer pageSize, Integer lampPositionId, HttpServletRequest request) {
        logger.info("查询区域详细列表，接收参数：deviceNum=" + deviceTypeNum);
        Result result = new Result();
        String message = "";
        // 控制查询层级及其他参数
        if (deviceTypeNum == null || deviceTypeNum < 1 || (deviceTypeNum > 7 && deviceTypeNum != 12)) {
            deviceTypeNum = null;
        }
        if (deviceTypeNum != null) {
            hierarchy = 5;
            if (deviceTypeNum != 1) {
                lampPositionId = null;
            }
        }
        if (hierarchy == null) {
            hierarchy = 3;
        }
        if (hierarchy < 1) {
            hierarchy = 1;
        }
        if (pageNum == null) {
            pageNum = 0;
        }
        if (pageSize == null) {
            pageSize = 0;
        }
        if (eliminate == null) {
            eliminate = 1;
        }

        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        // 构造返回对象
        List<DlmRespLocationAreaVO> dlmRespLocationAreaVOList = new ArrayList<>();
        Page<DlmRespLocationAreaVO> resultPageData = new Page<DlmRespLocationAreaVO>(pageNum, pageSize);
        List<LocationArea> locationAreaList = new ArrayList<>();
        if (pageSize == 0) {
            LambdaQueryWrapper<LocationArea> queryWrapper = new LambdaQueryWrapper<LocationArea>();
            queryWrapper.orderByDesc(LocationArea::getId);
            if (!flag) {
                queryWrapper.eq(LocationArea::getId, user.getAreaId());
            }
            locationAreaList = this.list(queryWrapper);
        } else {
            // 获取区域集合
            Result<IPage<LocationArea>> returnPage = new Result<IPage<LocationArea>>();
            Page<LocationArea> page = new Page<LocationArea>(pageNum, pageSize);
            LambdaQueryWrapper<LocationArea> queryWrapper = new LambdaQueryWrapper<LocationArea>();
            queryWrapper.orderByDesc(LocationArea::getId);
            if (!flag) {
                queryWrapper.eq(LocationArea::getId, user.getAreaId());
            }
            //分页数据
            IPage<LocationArea> pageData = this.page(page, queryWrapper);
            locationAreaList = pageData.getRecords();
            resultPageData.setTotal(pageData.getTotal());
            resultPageData.setSize(pageData.getSize());
            resultPageData.setCurrent(pageData.getCurrent());
            resultPageData.setPages(pageData.getPages());
        }
        List<LocationStreet> locationStreetList = new ArrayList<>();
        List<LocationSite> locationSiteList = new ArrayList<>();
        List<SlLampPost> slLampPostList = new ArrayList<>();
        List<Integer> slLampPostIdList = new ArrayList<>();
        if (locationAreaList != null && locationAreaList.size() > 0) {
            // 获取分区id集合
            List<Integer> areaIdCollect = locationAreaList.stream().map(LocationArea::getId).distinct().collect(Collectors.toList());
            // 获取街道集合
            LambdaQueryWrapper<LocationStreet> streetQueryWrapper = new LambdaQueryWrapper<LocationStreet>();
            streetQueryWrapper.orderByDesc(LocationStreet::getId);
            streetQueryWrapper.in(LocationStreet::getAreaId, areaIdCollect);
            locationStreetList = locationStreetService.list(streetQueryWrapper);
            if (locationStreetList != null && locationStreetList.size() > 0) {
                // 获取街道id集合
                List<Integer> streetIdCollect = locationStreetList.stream().map(LocationStreet::getId).distinct().collect(Collectors.toList());
                // 获取站点集合
                LambdaQueryWrapper<LocationSite> siteQueryWrapper = new LambdaQueryWrapper<LocationSite>();
                siteQueryWrapper.orderByDesc(LocationSite::getId);
                siteQueryWrapper.in(LocationSite::getStreetId, streetIdCollect);
                locationSiteList = locationSiteService.list(siteQueryWrapper);
                if (locationSiteList != null && locationSiteList.size() > 0) {
                    // 获取站点id集合
                    List<Integer> siteIdCollect = locationSiteList.stream().map(LocationSite::getId).distinct().collect(Collectors.toList());
                    // 获取灯杆集合
                    LambdaQueryWrapper<SlLampPost> slLampPostQueryWrapper = new LambdaQueryWrapper<SlLampPost>();
                    slLampPostQueryWrapper.orderByDesc(SlLampPost::getId);
                    slLampPostQueryWrapper.in(SlLampPost::getSiteId, siteIdCollect);
                    slLampPostList = slLampPostService.list(slLampPostQueryWrapper);
                    if (slLampPostList != null && slLampPostList.size() > 0) {
                        // 获取灯杆id集合
                        slLampPostIdList = slLampPostList.stream().map(SlLampPost::getId).distinct().collect(Collectors.toList());
                    }
                }
            }
        }
        List<SlRespSystemDeviceVO> singleLampParamRespVOList = null;
        List<WifiApDevice> wifiDeviceList = null;
        List<RadioDevice> pbDeviceList = null;
        List<SsDevice> ssDeviceList = null;
        List<ScreenDevice> screenDeviceList = null;
        List<AhDevice> ahDeviceList = null;
        List<MeteorologicalDevice> emDeviceList = null;
        List<ElectricityNode> electricityNodeList = null;
        if (slLampPostIdList != null && slLampPostIdList.size() > 0) {
            // 获取各个设备集合
            // 智慧照明
            if ((deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_SL == deviceTypeNum) || deviceTypeNum == null) {
                singleLampParamRespVOList = new ArrayList<>();
                List<DlmRespDevicePublicParVO> dlmRespDeviceVOList = singleLampParamService.getDlmRespDeviceVOList(slLampPostIdList);
                singleLampParamRespVOList = (List<SlRespSystemDeviceVO>) (List) dlmRespDeviceVOList.stream().map(DlmRespDevicePublicParVO::getDlmRespDevice).collect(Collectors.toList());
                if (lampPositionId != null) {
                    Integer lampPositionId1 = lampPositionId;
                    singleLampParamRespVOList = singleLampParamRespVOList.stream().filter(a -> lampPositionId1.equals(a.getLampPositionId())).collect(Collectors.toList());
                }
//                // 获取灯控器集合
//                LambdaQueryWrapper<LampDevice> lampDeviceQueryWrapper = new LambdaQueryWrapper<LampDevice>();
//                lampDeviceQueryWrapper.in(LampDevice::getLampPostId, slLampPostIdList);
//                List<LampDevice> lampDeviceList = lampDeviceService.list(lampDeviceQueryWrapper);
//                List<LampLoopType> lampLoopTypeList = singleLampParamService.loopTypeList();
//                for (LampDevice lampDevicep : lampDeviceList) {
//                    // 获取灯具集合
//                    LambdaQueryWrapper<SingleLampParam> singleLampParamQueryWrapper = new LambdaQueryWrapper<SingleLampParam>();
//                    singleLampParamQueryWrapper.eq(SingleLampParam::getDeviceId, lampDevicep.getId());
//                    if (lampPositionId != null) {
//                        singleLampParamQueryWrapper.eq(SingleLampParam::getLampPositionId, lampPositionId);
//                    }
//                    List<SingleLampParam> singleLampParamList = singleLampParamService.list(singleLampParamQueryWrapper);
//                    for (SingleLampParam singleLampParam : singleLampParamList) {
//                        SingleLampParamRespVO singleLampParamRespVO = new SingleLampParamRespVO();
//                        BeanUtils.copyProperties(lampDevicep, singleLampParamRespVO);
//                        BeanUtils.copyProperties(singleLampParam, singleLampParamRespVO);
//                        singleLampParamRespVOList.add(singleLampParamRespVO);
//                    }
//                }
            }
            // 公共WIFI
            if ((deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_WIFI == deviceTypeNum) || deviceTypeNum == null) {
                LambdaQueryWrapper<WifiApDevice> wifiApDeviceQueryWrapper = new LambdaQueryWrapper<WifiApDevice>();
                wifiApDeviceQueryWrapper.in(WifiApDevice::getLampPostId, slLampPostIdList);
                wifiDeviceList = wifiApDeviceService.list(wifiApDeviceQueryWrapper);
            }
            // 公共广播
            if ((deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_PB == deviceTypeNum) || deviceTypeNum == null) {
                LambdaQueryWrapper<RadioDevice> radioDeviceQueryWrapper = new LambdaQueryWrapper<RadioDevice>();
                radioDeviceQueryWrapper.in(RadioDevice::getLampPostId, slLampPostIdList);
                pbDeviceList = radioDeviceService.list(radioDeviceQueryWrapper);
            }
            // 智能安防
            if ((deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_SS == deviceTypeNum) || deviceTypeNum == null) {
                LambdaQueryWrapper<SsDevice> ssDeviceQueryWrapper = new LambdaQueryWrapper<SsDevice>();
                ssDeviceQueryWrapper.in(SsDevice::getLampPostId, slLampPostIdList);
                ssDeviceList = ssDeviceService.list(ssDeviceQueryWrapper);
            }
            // 信息发布
            if ((deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_IR == deviceTypeNum) || deviceTypeNum == null) {
                LambdaQueryWrapper<ScreenDevice> screenDeviceQueryWrapper = new LambdaQueryWrapper<ScreenDevice>();
                screenDeviceQueryWrapper.in(ScreenDevice::getLampPostId, slLampPostIdList);
                screenDeviceList = screenDeviceService.list(screenDeviceQueryWrapper);
            }
            // 一键呼叫
            if ((deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_OCC == deviceTypeNum) || deviceTypeNum == null) {
                LambdaQueryWrapper<AhDevice> ahDeviceQueryWrapper = new LambdaQueryWrapper<AhDevice>();
                ahDeviceQueryWrapper.in(AhDevice::getLampPostId, slLampPostIdList);
                ahDeviceList = ahDeviceService.list(ahDeviceQueryWrapper);
            }
            // 环境监测
            if ((deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_EM == deviceTypeNum) || deviceTypeNum == null) {
                LambdaQueryWrapper<MeteorologicalDevice> meteorologicalDeviceQueryWrapper = new LambdaQueryWrapper<MeteorologicalDevice>();
                meteorologicalDeviceQueryWrapper.in(MeteorologicalDevice::getLampPostId, slLampPostIdList);
                emDeviceList = meteorologicalDeviceService.list(meteorologicalDeviceQueryWrapper);
            }
            // 路灯网关
            if ((deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_GW == deviceTypeNum) || deviceTypeNum == null) {
                LambdaQueryWrapper<ElectricityNode> electricityNodeLambdaQueryWrapper = new LambdaQueryWrapper<ElectricityNode>();
                electricityNodeLambdaQueryWrapper.in(ElectricityNode::getLampPostId, slLampPostIdList);
                electricityNodeList = electricityNodeService.list(electricityNodeLambdaQueryWrapper);
            }
        }
        // 遍历区域集合，添加子集
        for (LocationArea locationArea : locationAreaList) {
            DlmRespLocationAreaVO dlmRespLocationAreaVO = new DlmRespLocationAreaVO();
            dlmRespLocationAreaVO.setId(locationArea.getId());
            dlmRespLocationAreaVO.setPartId("area" + locationArea.getId());
            dlmRespLocationAreaVO.setName(locationArea.getName());
            dlmRespLocationAreaVO.setDescription(locationArea.getDescription());
            dlmRespLocationAreaVO.setCreateTime(locationArea.getCreateTime());

            // 获取项目图片
//            DlmRespProjectPicVO projectPicRespVO = projectPicService.getProjectPicRespVO(locationArea.getId());
//            dlmRespLocationAreaVO.setProjectPicVO(projectPicRespVO);

            // 获取当前循环区域下街道集合
            List<LocationStreet> streetCollect = locationStreetList.stream()
                    .filter(a -> a.getAreaId().equals(locationArea.getId())).collect(Collectors.toList());
            // 区域下设备数量
            Integer areaDeviceNumber = 0;
            // 区域下街道集合
            List<DlmRespLocationStreetVO> dlmRespLocationStreetVOList = new ArrayList<>();
            // 遍历街道集合，添加子集
            for (LocationStreet locationStreet : streetCollect) {
                DlmRespLocationStreetVO dlmRespLocationStreetVO = new DlmRespLocationStreetVO();
                if (hierarchy > 1) {
                    dlmRespLocationStreetVO.setId(locationStreet.getId());
                    dlmRespLocationStreetVO.setPartId("street" + locationStreet.getId());
                    dlmRespLocationStreetVO.setName(locationStreet.getName());
                    dlmRespLocationStreetVO.setDescription(locationStreet.getDescription());
                    dlmRespLocationStreetVO.setCreateTime(locationStreet.getCreateTime());
                    dlmRespLocationStreetVO.setSuperId(locationArea.getId());
                    dlmRespLocationStreetVO.setSuperName(locationArea.getName());
                }
                // 获取当前循环街道下站点集合
                List<LocationSite> siteCollect = locationSiteList.stream()
                        .filter(a -> a.getStreetId().equals(locationStreet.getId())).collect(Collectors.toList());
                // 街道下设备数量
                Integer streetDeviceNumber = 0;
                // 街道下站点集合
                List<DlmRespLocationSiteVO> dlmRespLocationSiteVOList = new ArrayList<>();
                // 遍历站点集合，构造对象
                for (LocationSite locationSite : siteCollect) {
                    DlmRespLocationSiteVO dlmRespLocationSiteVO = new DlmRespLocationSiteVO();
                    if (hierarchy > 2) {
                        dlmRespLocationSiteVO.setId(locationSite.getId());
                        dlmRespLocationSiteVO.setPartId("site" + locationSite.getId());
                        dlmRespLocationSiteVO.setName(locationSite.getName());
                        dlmRespLocationSiteVO.setDescription(locationSite.getDescription());
                        dlmRespLocationSiteVO.setCreateTime(locationSite.getCreateTime());
                        dlmRespLocationSiteVO.setSuperId(locationStreet.getId());
                        dlmRespLocationSiteVO.setSuperName(locationStreet.getName());
                    }
                    // 获取当前循环站点下的灯杆集合
                    List<SlLampPost> slLampPostCollect = slLampPostList.stream()
                            .filter(a -> locationSite.getId().equals(a.getSiteId())).collect(Collectors.toList());
                    // 站点下设备数量
                    Integer siteDeviceNumber = 0;
                    // 站点下灯杆集合
                    List<DlmRespLocationLampPostVO> dlmRespLocationLampPostVOList = new ArrayList<>();
                    // 遍历灯杆集合
                    for (SlLampPost slLampPost : slLampPostCollect) {
                        DlmRespLocationLampPostVO dlmRespLocationLampPostVO = new DlmRespLocationLampPostVO();
                        Integer lampPostDeviceNumber = 0;
                        // 获取灯杆下各个设备的集合
                        List deviceList = new ArrayList<>();
                        // 是否指定设备类型编号
                        if (deviceTypeNum != null) {
                            // 智慧照明
                            if (BaseConstantUtil.DEVICE_TYPE_SL == deviceTypeNum && singleLampParamRespVOList != null && singleLampParamRespVOList.size() > 0) {
                                List<SlRespSystemDeviceVO> lampDeviceCollect = singleLampParamRespVOList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += lampDeviceCollect.size();
                                lampPostDeviceNumber += lampDeviceCollect.size();
                                deviceList = lampDeviceCollect;
                            }
                            // 公共WIFI
                            if (BaseConstantUtil.DEVICE_TYPE_WIFI == deviceTypeNum && wifiDeviceList != null && wifiDeviceList.size() > 0) {
                                List<WifiApDevice> wifiDeviceCollect = wifiDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += wifiDeviceCollect.size();
                                lampPostDeviceNumber += wifiDeviceCollect.size();
                                deviceList = wifiDeviceCollect;
                            }
                            // 公共广播
                            if (BaseConstantUtil.DEVICE_TYPE_PB == deviceTypeNum && pbDeviceList != null && pbDeviceList.size() > 0) {
                                List<RadioDevice> pbDeviceCollect = pbDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += pbDeviceCollect.size();
                                lampPostDeviceNumber += pbDeviceCollect.size();
                                deviceList = pbDeviceCollect;
                            }
                            // 智能安防
                            if (BaseConstantUtil.DEVICE_TYPE_SS == deviceTypeNum && ssDeviceList != null && ssDeviceList.size() > 0) {
                                List<SsDevice> ssDeviceCollect = ssDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += ssDeviceCollect.size();
                                lampPostDeviceNumber += ssDeviceCollect.size();
                                deviceList = ssDeviceCollect;
                            }
                            // 信息发布
                            if (BaseConstantUtil.DEVICE_TYPE_IR == deviceTypeNum && screenDeviceList != null && screenDeviceList.size() > 0) {
                                List<ScreenDevice> screenDeviceCollect = screenDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += screenDeviceCollect.size();
                                lampPostDeviceNumber += screenDeviceCollect.size();
                                deviceList = screenDeviceCollect;
                            }
                            // 一键呼叫
                            if (BaseConstantUtil.DEVICE_TYPE_OCC == deviceTypeNum && ahDeviceList != null && ahDeviceList.size() > 0) {
                                List<AhDevice> ahDeviceCollect = ahDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += ahDeviceCollect.size();
                                lampPostDeviceNumber += ahDeviceCollect.size();
                                deviceList = ahDeviceCollect;
                            }
                            // 环境监测
                            if (BaseConstantUtil.DEVICE_TYPE_EM == deviceTypeNum && emDeviceList != null && emDeviceList.size() > 0) {
                                List<MeteorologicalDevice> emDeviceCollect = emDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += emDeviceCollect.size();
                                lampPostDeviceNumber += emDeviceCollect.size();
                                deviceList = emDeviceCollect;
                            }
                            // 环境监测
                            if (BaseConstantUtil.DEVICE_TYPE_GW == deviceTypeNum && electricityNodeList != null && electricityNodeList.size() > 0) {
                                List<ElectricityNode> electricityNodeCollect = electricityNodeList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += electricityNodeCollect.size();
                                lampPostDeviceNumber += electricityNodeCollect.size();
                                deviceList = electricityNodeCollect;
                            }
                        } else {
                            // 智慧照明
                            if (singleLampParamRespVOList != null && singleLampParamRespVOList.size() > 0) {
                                List<SlRespSystemDeviceVO> lampDeviceCollect = singleLampParamRespVOList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += lampDeviceCollect.size();
                                lampPostDeviceNumber += lampDeviceCollect.size();
                            }
                            // 公共WIFI
                            if (wifiDeviceList != null && wifiDeviceList.size() > 0) {
                                List<WifiApDevice> wifiDeviceCollect = wifiDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += wifiDeviceCollect.size();
                                lampPostDeviceNumber += wifiDeviceCollect.size();
                            }
                            // 公共广播
                            if (pbDeviceList != null && pbDeviceList.size() > 0) {
                                List<RadioDevice> pbDeviceCollect = pbDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += pbDeviceCollect.size();
                                lampPostDeviceNumber += pbDeviceCollect.size();
                            }
                            // 智能安防
                            if (ssDeviceList != null && ssDeviceList.size() > 0) {
                                List<SsDevice> ssDeviceCollect = ssDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += ssDeviceCollect.size();
                                lampPostDeviceNumber += ssDeviceCollect.size();
                            }
                            // 信息发布
                            if (screenDeviceList != null && screenDeviceList.size() > 0) {
                                List<ScreenDevice> screenDeviceCollect = screenDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += screenDeviceCollect.size();
                                lampPostDeviceNumber += screenDeviceCollect.size();
                            }
                            // 一键呼叫
                            if (ahDeviceList != null && ahDeviceList.size() > 0) {
                                List<AhDevice> ahDeviceCollect = ahDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += ahDeviceCollect.size();
                                lampPostDeviceNumber += ahDeviceCollect.size();
                            }
                            // 环境监测
                            if (emDeviceList != null && emDeviceList.size() > 0) {
                                List<MeteorologicalDevice> emDeviceCollect = emDeviceList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += emDeviceCollect.size();
                                lampPostDeviceNumber += emDeviceCollect.size();
                            }
                            // 环境监测
                            if (electricityNodeList != null && electricityNodeList.size() > 0) {
                                List<ElectricityNode> electricityNodeCollect = electricityNodeList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += electricityNodeCollect.size();
                                lampPostDeviceNumber += electricityNodeCollect.size();
                            }
                        }
                        // 是否需要灯杆级数据
                        if (hierarchy > 3) {
                            dlmRespLocationLampPostVO.setId(slLampPost.getId());
                            dlmRespLocationLampPostVO.setPartId("lampPost" + slLampPost.getId());
                            dlmRespLocationLampPostVO.setName(slLampPost.getName());
                            dlmRespLocationLampPostVO.setLampPostNum(slLampPost.getNum());
                            dlmRespLocationLampPostVO.setLampPostModel(slLampPost.getModel());
                            dlmRespLocationLampPostVO.setLampPostManufacturer(slLampPost.getManufacturer());
                            dlmRespLocationLampPostVO.setLampPostLongitude(slLampPost.getLongitude());
                            dlmRespLocationLampPostVO.setLampPostLocation(slLampPost.getLocation());
                            dlmRespLocationLampPostVO.setLampPostLatitude(slLampPost.getLatitude());
                            dlmRespLocationLampPostVO.setCreateTime(slLampPost.getCreateTime());
                            dlmRespLocationLampPostVO.setDeviceNumber(lampPostDeviceNumber);
                            dlmRespLocationLampPostVO.setSuperId(dlmRespLocationSiteVO.getId());
                            dlmRespLocationLampPostVO.setSuperName(dlmRespLocationSiteVO.getName());
                            if (hierarchy > 4) {
                                // 是否指定设备
                                if (deviceTypeNum != null) {
                                    // 构建设备参数
                                    List<DlmRespDevicePublicParVO> dlmRespDevicePublicParVOList = new ArrayList<>();
                                    for (Object lampDeviceObject : deviceList) {
                                        JSONObject lampDevice = (JSONObject) JSON.toJSON(lampDeviceObject);
                                        DlmRespDevicePublicParVO dlmRespDevicePublicParVO = slLampPostService.deviceJsonObjectToPublicParVO(lampDevice, deviceTypeNum, slLampPost);
                                        dlmRespDevicePublicParVOList.add(dlmRespDevicePublicParVO);
                                    }
                                    dlmRespLocationLampPostVO.setChildrenList(dlmRespDevicePublicParVOList);
                                }
                            }
                            if (deviceTypeNum == null || dlmRespLocationLampPostVO.getDeviceNumber() > 0) {
                                dlmRespLocationLampPostVOList.add(dlmRespLocationLampPostVO);
                            }
                        }
                    }
                    dlmRespLocationSiteVO.setDeviceNumber(siteDeviceNumber);
                    if (hierarchy > 3 && pageSize == 0) {
                        dlmRespLocationSiteVO.setChildrenList(dlmRespLocationLampPostVOList);
                    }
                    if (deviceTypeNum == null || dlmRespLocationSiteVO.getDeviceNumber() > 0) {
                        dlmRespLocationSiteVOList.add(dlmRespLocationSiteVO);
                    }
                    streetDeviceNumber += siteDeviceNumber;
                }
                dlmRespLocationStreetVO.setDeviceNumber(streetDeviceNumber);
                if (hierarchy > 2) {
                    dlmRespLocationStreetVO.setChildrenList(dlmRespLocationSiteVOList);
                }
                if (deviceTypeNum == null || dlmRespLocationStreetVO.getDeviceNumber() > 0) {
                    dlmRespLocationStreetVOList.add(dlmRespLocationStreetVO);
                }
                areaDeviceNumber += streetDeviceNumber;
            }
            dlmRespLocationAreaVO.setDeviceNumber(areaDeviceNumber);
            if (hierarchy > 1) {
                dlmRespLocationAreaVO.setChildrenList(dlmRespLocationStreetVOList);
            }
            if (deviceTypeNum == null || dlmRespLocationAreaVO.getDeviceNumber() > 0) {
                dlmRespLocationAreaVOList.add(dlmRespLocationAreaVO);
            }
        }
        // 剔除指定级别没有数据的分支
        if (eliminate == 1) {
            // 区域迭代器
            Iterator<DlmRespLocationAreaVO> dlmRespLocationAreaVOIterator = dlmRespLocationAreaVOList.iterator();
            while (dlmRespLocationAreaVOIterator.hasNext()) {
                // 获取区域下街道集合
                DlmRespLocationAreaVO dlmRespLocationAreaVO = dlmRespLocationAreaVOIterator.next();
                List<DlmRespLocationStreetVO> dlmRespLocationStreetVOList = dlmRespLocationAreaVO.getChildrenList();
                if (hierarchy > 2) {
                    // 街道迭代器
                    Iterator<DlmRespLocationStreetVO> dlmRespLocationStreetVOIterator = dlmRespLocationStreetVOList.iterator();
                    while (dlmRespLocationStreetVOIterator.hasNext()) {
                        // 获取街道下站点集合
                        DlmRespLocationStreetVO dlmRespLocationStreetVO = dlmRespLocationStreetVOIterator.next();
                        List<DlmRespLocationSiteVO> dlmRespLocationSiteVOList = dlmRespLocationStreetVO.getChildrenList();
                        if (hierarchy > 3) {
                            // 站点迭代器
                            Iterator<DlmRespLocationSiteVO> dlmRespLocationSiteVOIterator = dlmRespLocationSiteVOList.iterator();
                            while (dlmRespLocationSiteVOIterator.hasNext()) {
                                // 获取站点下灯杆集合
                                DlmRespLocationSiteVO dlmRespLocationSiteVO = dlmRespLocationSiteVOIterator.next();
                                List<DlmRespLocationLampPostVO> dlmRespLocationLampPostVOList = dlmRespLocationSiteVO.getChildrenList();
                                // 移除没有灯杆的站点
                                if (hierarchy > 3) {
                                    if (dlmRespLocationLampPostVOList == null || dlmRespLocationLampPostVOList.size() == 0) {
                                        dlmRespLocationSiteVOIterator.remove();
                                    }
                                }
                            }
                        }
                        // 移除没有站点的街道
                        if (hierarchy > 2) {
                            if (dlmRespLocationSiteVOList == null || dlmRespLocationSiteVOList.size() == 0) {
                                dlmRespLocationStreetVOIterator.remove();
                            }
                        }
                    }
                }
                // 移除没有街道的分区
                if (hierarchy > 1) {
                    if (dlmRespLocationStreetVOList == null || dlmRespLocationStreetVOList.size() == 0) {
                        dlmRespLocationAreaVOIterator.remove();
                    }
                }
            }
        }
        // 是否分页，是否返回分页数据数据
        if (pageSize == 0) {
            result.success(dlmRespLocationAreaVOList);
        } else {
            resultPageData.setRecords(dlmRespLocationAreaVOList);
            result.success(resultPageData);
        }
        if (!"".equals(message)) {
            result.setMessage(message);
        }
        return result;
    }


    @Override
    public Result getListByDeviceType(Integer deviceType, Integer logId, Integer isSuccess, Integer eliminate, Integer pageNum, Integer pageSize, Integer lampPositionId, HttpServletRequest request) {
        logger.info("根据设备型号查询灯具下拉列表，接收参数：deviceNum=");
        Result result = new Result();
        String message = "";
        // 控制查询层级及其他参数
        Integer hierarchy = 5;
        Integer deviceTypeNum = 1;
        if (hierarchy == null) {
            hierarchy = 3;
        }
        if (hierarchy < 1) {
            hierarchy = 1;
        }
        if (pageNum == null) {
            pageNum = 0;
        }
        if (pageSize == null) {
            pageSize = 0;
        }
        if (eliminate == null) {
            eliminate = 1;
        }

        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        // 构造返回对象
        List<DlmRespLocationAreaVO> dlmRespLocationAreaVOList = new ArrayList<>();
        Page<DlmRespLocationAreaVO> resultPageData = new Page<DlmRespLocationAreaVO>(pageNum, pageSize);
        List<LocationArea> locationAreaList = new ArrayList<>();
        if (pageSize == 0) {
            LambdaQueryWrapper<LocationArea> queryWrapper = new LambdaQueryWrapper<LocationArea>();
            queryWrapper.orderByDesc(LocationArea::getId);
            if (!flag) {
                queryWrapper.eq(LocationArea::getId, user.getAreaId());
            }
            locationAreaList = this.list(queryWrapper);
        } else {
            // 获取区域集合
            Result<IPage<LocationArea>> returnPage = new Result<IPage<LocationArea>>();
            Page<LocationArea> page = new Page<LocationArea>(pageNum, pageSize);
            LambdaQueryWrapper<LocationArea> queryWrapper = new LambdaQueryWrapper<LocationArea>();
            queryWrapper.orderByDesc(LocationArea::getId);
            if (!flag) {
                queryWrapper.eq(LocationArea::getId, user.getAreaId());
            }
            //分页数据
            IPage<LocationArea> pageData = this.page(page, queryWrapper);
            locationAreaList = pageData.getRecords();
            resultPageData.setTotal(pageData.getTotal());
            resultPageData.setSize(pageData.getSize());
            resultPageData.setCurrent(pageData.getCurrent());
            resultPageData.setPages(pageData.getPages());
        }
        List<LocationStreet> locationStreetList = new ArrayList<>();
        List<LocationSite> locationSiteList = new ArrayList<>();
        List<SlLampPost> slLampPostList = new ArrayList<>();
        List<Integer> slLampPostIdList = new ArrayList<>();
        if (locationAreaList != null && locationAreaList.size() > 0) {
            // 获取分区id集合
            List<Integer> areaIdCollect = locationAreaList.stream().map(LocationArea::getId).distinct().collect(Collectors.toList());
            // 获取街道集合
            LambdaQueryWrapper<LocationStreet> streetQueryWrapper = new LambdaQueryWrapper<LocationStreet>();
            streetQueryWrapper.orderByDesc(LocationStreet::getId);
            streetQueryWrapper.in(LocationStreet::getAreaId, areaIdCollect);
            locationStreetList = locationStreetService.list(streetQueryWrapper);
            if (locationStreetList != null && locationStreetList.size() > 0) {
                // 获取街道id集合
                List<Integer> streetIdCollect = locationStreetList.stream().map(LocationStreet::getId).distinct().collect(Collectors.toList());
                // 获取站点集合
                LambdaQueryWrapper<LocationSite> siteQueryWrapper = new LambdaQueryWrapper<LocationSite>();
                siteQueryWrapper.orderByDesc(LocationSite::getId);
                siteQueryWrapper.in(LocationSite::getStreetId, streetIdCollect);
                locationSiteList = locationSiteService.list(siteQueryWrapper);
                if (locationSiteList != null && locationSiteList.size() > 0) {
                    // 获取站点id集合
                    List<Integer> siteIdCollect = locationSiteList.stream().map(LocationSite::getId).distinct().collect(Collectors.toList());
                    // 获取灯杆集合
                    LambdaQueryWrapper<SlLampPost> slLampPostQueryWrapper = new LambdaQueryWrapper<SlLampPost>();
                    slLampPostQueryWrapper.orderByDesc(SlLampPost::getId);
                    slLampPostQueryWrapper.in(SlLampPost::getSiteId, siteIdCollect);
                    slLampPostList = slLampPostService.list(slLampPostQueryWrapper);
                    if (slLampPostList != null && slLampPostList.size() > 0) {
                        // 获取灯杆id集合
                        slLampPostIdList = slLampPostList.stream().map(SlLampPost::getId).distinct().collect(Collectors.toList());
                    }
                }
            }
        }
        List<SlRespSystemDeviceVO> singleLampParamRespVOList = null;
        List<WifiApDevice> wifiDeviceList = null;
        List<RadioDevice> pbDeviceList = null;
        List<SsDevice> ssDeviceList = null;
        List<ScreenDevice> screenDeviceList = null;
        List<AhDevice> ahDeviceList = null;
        List<MeteorologicalDevice> emDeviceList = null;
        if (slLampPostIdList != null && slLampPostIdList.size() > 0) {
            // 获取各个设备集合
            // 智慧照明
            if ((deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_SL == deviceTypeNum) || deviceTypeNum == null) {
                singleLampParamRespVOList = new ArrayList<>();
                List<DlmRespDevicePublicParVO> dlmRespDeviceVOList = singleLampParamService.getDlmRespDeviceVOList(slLampPostIdList);
                singleLampParamRespVOList = (List<SlRespSystemDeviceVO>) (List) dlmRespDeviceVOList.stream().map(DlmRespDevicePublicParVO::getDlmRespDevice).collect(Collectors.toList());
                if (lampPositionId != null) {
                    Integer lampPositionId1 = lampPositionId;
                    singleLampParamRespVOList = singleLampParamRespVOList.stream().filter(a -> lampPositionId1.equals(a.getLampPositionId())).collect(Collectors.toList());
                }
                if (logId != null && isSuccess != null) {
                    List<Integer> deviceIdByUpgradeLog = baseMapper.getDeviceIdByUpgradeLog(logId, isSuccess);
                    if (deviceIdByUpgradeLog != null) {
                        singleLampParamRespVOList = singleLampParamRespVOList.stream().filter(SingleLampParamRespVO -> deviceIdByUpgradeLog.contains(SingleLampParamRespVO.getId())).collect(Collectors.toList());
                    }

                }
                if (deviceType != null) {
                    if (deviceType == 1) {
                        List<Integer> specialDeviceTypeList = Arrays.asList(1, 2);
                        singleLampParamRespVOList = singleLampParamRespVOList.stream().filter(p -> specialDeviceTypeList.contains(p.getDeviceTypeId())).collect(Collectors.toList());
                    } else if (deviceType == 2) {
                        List<Integer> specialDeviceTypeList = Arrays.asList(9, 10);
                        singleLampParamRespVOList = singleLampParamRespVOList.stream().filter(p -> specialDeviceTypeList.contains(p.getDeviceTypeId())).collect(Collectors.toList());
                    } else if (deviceType == 3) {
                        List<Integer> specialDeviceTypeList = Arrays.asList(14, 15);
                        singleLampParamRespVOList = singleLampParamRespVOList.stream().filter(p -> specialDeviceTypeList.contains(p.getDeviceTypeId())).collect(Collectors.toList());
                    } else if (deviceType == 4) {
                        List<Integer> specialDeviceTypeList = Arrays.asList(14, 15);
                        singleLampParamRespVOList = singleLampParamRespVOList.stream().filter(p -> specialDeviceTypeList.contains(p.getDeviceTypeId())).collect(Collectors.toList());
                    }
                }

//                // 获取灯控器集合
//                LambdaQueryWrapper<LampDevice> lampDeviceQueryWrapper = new LambdaQueryWrapper<LampDevice>();
//                lampDeviceQueryWrapper.in(LampDevice::getLampPostId, slLampPostIdList);
//                List<LampDevice> lampDeviceList = lampDeviceService.list(lampDeviceQueryWrapper);
//                List<LampLoopType> lampLoopTypeList = singleLampParamService.loopTypeList();
//                for (LampDevice lampDevicep : lampDeviceList) {
//                    // 获取灯具集合
//                    LambdaQueryWrapper<SingleLampParam> singleLampParamQueryWrapper = new LambdaQueryWrapper<SingleLampParam>();
//                    singleLampParamQueryWrapper.eq(SingleLampParam::getDeviceId, lampDevicep.getId());
//                    if (lampPositionId != null) {
//                        singleLampParamQueryWrapper.eq(SingleLampParam::getLampPositionId, lampPositionId);
//                    }
//                    List<SingleLampParam> singleLampParamList = singleLampParamService.list(singleLampParamQueryWrapper);
//                    for (SingleLampParam singleLampParam : singleLampParamList) {
//                        SingleLampParamRespVO singleLampParamRespVO = new SingleLampParamRespVO();
//                        BeanUtils.copyProperties(lampDevicep, singleLampParamRespVO);
//                        BeanUtils.copyProperties(singleLampParam, singleLampParamRespVO);
//                        singleLampParamRespVOList.add(singleLampParamRespVO);
//                    }
//                }
            }
        }
        // 遍历区域集合，添加子集
        for (LocationArea locationArea : locationAreaList) {
            DlmRespLocationAreaVO dlmRespLocationAreaVO = new DlmRespLocationAreaVO();
            dlmRespLocationAreaVO.setId(locationArea.getId());
            dlmRespLocationAreaVO.setPartId("area" + locationArea.getId());
            dlmRespLocationAreaVO.setName(locationArea.getName());
            dlmRespLocationAreaVO.setDescription(locationArea.getDescription());
            dlmRespLocationAreaVO.setCreateTime(locationArea.getCreateTime());
            // 获取当前循环区域下街道集合
            List<LocationStreet> streetCollect = locationStreetList.stream()
                    .filter(a -> a.getAreaId().equals(locationArea.getId())).collect(Collectors.toList());
            // 区域下设备数量
            Integer areaDeviceNumber = 0;
            // 区域下街道集合
            List<DlmRespLocationStreetVO> dlmRespLocationStreetVOList = new ArrayList<>();
            // 遍历街道集合，添加子集
            for (LocationStreet locationStreet : streetCollect) {
                DlmRespLocationStreetVO dlmRespLocationStreetVO = new DlmRespLocationStreetVO();
                if (hierarchy > 1) {
                    dlmRespLocationStreetVO.setId(locationStreet.getId());
                    dlmRespLocationStreetVO.setPartId("street" + locationStreet.getId());
                    dlmRespLocationStreetVO.setName(locationStreet.getName());
                    dlmRespLocationStreetVO.setDescription(locationStreet.getDescription());
                    dlmRespLocationStreetVO.setCreateTime(locationStreet.getCreateTime());
                    dlmRespLocationStreetVO.setSuperId(locationArea.getId());
                    dlmRespLocationStreetVO.setSuperName(locationArea.getName());
                }
                // 获取当前循环街道下站点集合
                List<LocationSite> siteCollect = locationSiteList.stream()
                        .filter(a -> a.getStreetId().equals(locationStreet.getId())).collect(Collectors.toList());
                // 街道下设备数量
                Integer streetDeviceNumber = 0;
                // 街道下站点集合
                List<DlmRespLocationSiteVO> dlmRespLocationSiteVOList = new ArrayList<>();
                // 遍历站点集合，构造对象
                for (LocationSite locationSite : siteCollect) {
                    DlmRespLocationSiteVO dlmRespLocationSiteVO = new DlmRespLocationSiteVO();
                    if (hierarchy > 2) {
                        dlmRespLocationSiteVO.setId(locationSite.getId());
                        dlmRespLocationSiteVO.setPartId("site" + locationSite.getId());
                        dlmRespLocationSiteVO.setName(locationSite.getName());
                        dlmRespLocationSiteVO.setDescription(locationSite.getDescription());
                        dlmRespLocationSiteVO.setCreateTime(locationSite.getCreateTime());
                        dlmRespLocationSiteVO.setSuperId(locationStreet.getId());
                        dlmRespLocationSiteVO.setSuperName(locationStreet.getName());
                    }
                    // 获取当前循环站点下的灯杆集合
                    List<SlLampPost> slLampPostCollect = slLampPostList.stream()
                            .filter(a -> locationSite.getId().equals(a.getSiteId())).collect(Collectors.toList());
                    // 站点下设备数量
                    Integer siteDeviceNumber = 0;
                    // 站点下灯杆集合
                    List<DlmRespLocationLampPostVO> dlmRespLocationLampPostVOList = new ArrayList<>();
                    // 遍历灯杆集合
                    for (SlLampPost slLampPost : slLampPostCollect) {
                        DlmRespLocationLampPostVO dlmRespLocationLampPostVO = new DlmRespLocationLampPostVO();
                        Integer lampPostDeviceNumber = 0;
                        // 获取灯杆下各个设备的集合
                        List deviceList = new ArrayList<>();
                        // 是否指定设备类型编号
                        if (deviceTypeNum != null) {
                            // 智慧照明
                            if (BaseConstantUtil.DEVICE_TYPE_SL == deviceTypeNum && singleLampParamRespVOList != null && singleLampParamRespVOList.size() > 0) {
                                List<SlRespSystemDeviceVO> lampDeviceCollect = singleLampParamRespVOList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += lampDeviceCollect.size();
                                lampPostDeviceNumber += lampDeviceCollect.size();
                                deviceList = lampDeviceCollect;
                            }
                        } else {
                            // 智慧照明
                            if (singleLampParamRespVOList != null && singleLampParamRespVOList.size() > 0) {
                                List<SlRespSystemDeviceVO> lampDeviceCollect = singleLampParamRespVOList.stream()
                                        .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                                siteDeviceNumber += lampDeviceCollect.size();
                                lampPostDeviceNumber += lampDeviceCollect.size();
                            }
                        }
                        // 是否需要灯杆级数据
                        if (hierarchy > 3) {
                            dlmRespLocationLampPostVO.setId(slLampPost.getId());
                            dlmRespLocationLampPostVO.setPartId("lampPost" + slLampPost.getId());
                            dlmRespLocationLampPostVO.setName(slLampPost.getName());
                            dlmRespLocationLampPostVO.setLampPostNum(slLampPost.getNum());
                            dlmRespLocationLampPostVO.setLampPostModel(slLampPost.getModel());
                            dlmRespLocationLampPostVO.setLampPostManufacturer(slLampPost.getManufacturer());
                            dlmRespLocationLampPostVO.setLampPostLongitude(slLampPost.getLongitude());
                            dlmRespLocationLampPostVO.setLampPostLocation(slLampPost.getLocation());
                            dlmRespLocationLampPostVO.setLampPostLatitude(slLampPost.getLatitude());
                            dlmRespLocationLampPostVO.setCreateTime(slLampPost.getCreateTime());
                            dlmRespLocationLampPostVO.setDeviceNumber(lampPostDeviceNumber);
                            dlmRespLocationLampPostVO.setSuperId(dlmRespLocationSiteVO.getId());
                            dlmRespLocationLampPostVO.setSuperName(dlmRespLocationSiteVO.getName());
                            if (hierarchy > 4) {
                                // 是否指定设备
                                if (deviceTypeNum != null) {
                                    // 构建设备参数
                                    List<DlmRespDevicePublicParVO> dlmRespDevicePublicParVOList = new ArrayList<>();
                                    for (Object lampDeviceObject : deviceList) {
                                        JSONObject lampDevice = (JSONObject) JSON.toJSON(lampDeviceObject);
                                        DlmRespDevicePublicParVO dlmRespDevicePublicParVO = slLampPostService.deviceJsonObjectToPublicParVO(lampDevice, deviceTypeNum, slLampPost);
                                        dlmRespDevicePublicParVOList.add(dlmRespDevicePublicParVO);
                                    }
                                    dlmRespLocationLampPostVO.setChildrenList(dlmRespDevicePublicParVOList);
                                }
                            }
                            if (deviceTypeNum == null || dlmRespLocationLampPostVO.getDeviceNumber() > 0) {
                                dlmRespLocationLampPostVOList.add(dlmRespLocationLampPostVO);
                            }
                        }
                    }
                    dlmRespLocationSiteVO.setDeviceNumber(siteDeviceNumber);
                    if (hierarchy > 3 && pageSize == 0) {
                        dlmRespLocationSiteVO.setChildrenList(dlmRespLocationLampPostVOList);
                    }
                    if (deviceTypeNum == null || dlmRespLocationSiteVO.getDeviceNumber() > 0) {
                        dlmRespLocationSiteVOList.add(dlmRespLocationSiteVO);
                    }
                    streetDeviceNumber += siteDeviceNumber;
                }
                dlmRespLocationStreetVO.setDeviceNumber(streetDeviceNumber);
                if (hierarchy > 2) {
                    dlmRespLocationStreetVO.setChildrenList(dlmRespLocationSiteVOList);
                }
                if (deviceTypeNum == null || dlmRespLocationStreetVO.getDeviceNumber() > 0) {
                    dlmRespLocationStreetVOList.add(dlmRespLocationStreetVO);
                }
                areaDeviceNumber += streetDeviceNumber;
            }
            dlmRespLocationAreaVO.setDeviceNumber(areaDeviceNumber);
            if (hierarchy > 1) {
                dlmRespLocationAreaVO.setChildrenList(dlmRespLocationStreetVOList);
            }
            if (deviceTypeNum == null || dlmRespLocationAreaVO.getDeviceNumber() > 0) {
                dlmRespLocationAreaVOList.add(dlmRespLocationAreaVO);
            }
        }
        // 剔除指定级别没有数据的分支
        if (eliminate == 1) {
            // 区域迭代器
            Iterator<DlmRespLocationAreaVO> dlmRespLocationAreaVOIterator = dlmRespLocationAreaVOList.iterator();
            while (dlmRespLocationAreaVOIterator.hasNext()) {
                // 获取区域下街道集合
                DlmRespLocationAreaVO dlmRespLocationAreaVO = dlmRespLocationAreaVOIterator.next();
                List<DlmRespLocationStreetVO> dlmRespLocationStreetVOList = dlmRespLocationAreaVO.getChildrenList();
                if (hierarchy > 2) {
                    // 街道迭代器
                    Iterator<DlmRespLocationStreetVO> dlmRespLocationStreetVOIterator = dlmRespLocationStreetVOList.iterator();
                    while (dlmRespLocationStreetVOIterator.hasNext()) {
                        // 获取街道下站点集合
                        DlmRespLocationStreetVO dlmRespLocationStreetVO = dlmRespLocationStreetVOIterator.next();
                        List<DlmRespLocationSiteVO> dlmRespLocationSiteVOList = dlmRespLocationStreetVO.getChildrenList();
                        if (hierarchy > 3) {
                            // 站点迭代器
                            Iterator<DlmRespLocationSiteVO> dlmRespLocationSiteVOIterator = dlmRespLocationSiteVOList.iterator();
                            while (dlmRespLocationSiteVOIterator.hasNext()) {
                                // 获取站点下灯杆集合
                                DlmRespLocationSiteVO dlmRespLocationSiteVO = dlmRespLocationSiteVOIterator.next();
                                List<DlmRespLocationLampPostVO> dlmRespLocationLampPostVOList = dlmRespLocationSiteVO.getChildrenList();
                                // 移除没有灯杆的站点
                                if (hierarchy > 3) {
                                    if (dlmRespLocationLampPostVOList == null || dlmRespLocationLampPostVOList.size() == 0) {
                                        dlmRespLocationSiteVOIterator.remove();
                                    }
                                }
                            }
                        }
                        // 移除没有站点的街道
                        if (hierarchy > 2) {
                            if (dlmRespLocationSiteVOList == null || dlmRespLocationSiteVOList.size() == 0) {
                                dlmRespLocationStreetVOIterator.remove();
                            }
                        }
                    }
                }
                // 移除没有街道的分区
                if (hierarchy > 1) {
                    if (dlmRespLocationStreetVOList == null || dlmRespLocationStreetVOList.size() == 0) {
                        dlmRespLocationAreaVOIterator.remove();
                    }
                }
            }
        }
        // 是否分页，是否返回分页数据数据
        if (pageSize == 0) {
            result.success(dlmRespLocationAreaVOList);
        } else {
            resultPageData.setRecords(dlmRespLocationAreaVOList);
            result.success(resultPageData);
        }
        if (!"".equals(message)) {
            result.setMessage(message);
        }
        return result;
    }

    @Override
    public Result pulldown(String areaName, HttpServletRequest request) {
        logger.info("查询区域下拉列表，接收参数：areaName=" + areaName);
        LambdaQueryWrapper<LocationArea> wrapper = new LambdaQueryWrapper();
        // 过滤非超级管理员用户，只查询对应分区
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if (!flag) {
            wrapper.eq(LocationArea::getId, user.getAreaId());
        }
        if (areaName != null) {
            wrapper.like(LocationArea::getName, areaName);
        }
        List<LocationArea> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result add(DlmReqLocationAreaVO locationAreaVO, HttpServletRequest request) {
        logger.info("添加区域，接收参数：{}", locationAreaVO);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        boolean flag = logUserService.isAdmin(userId);
        if (!flag) {
            result.error("添加分区请联系爱克路灯系统管理员");
        }
        LocationArea locationArea = new LocationArea();
        BeanUtils.copyProperties(locationAreaVO, locationArea);
        Result unique = this.unique(locationArea, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            locationArea.setCreateTime(new Date());
            boolean save = this.save(locationArea);
            if (save) {
                result.success("添加区域成功");
            } else {
                result.success("添加区域失败");
            }
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result delete(Long id, HttpServletRequest request) {
        logger.info("根据id删除分区，接收参数：{}", id);
        // 删除分区
        this.removeById(id);
        // 删除街道
        List<Integer> idList = new ArrayList<>();
        idList.add(id.intValue());
        locationStreetService.deleteByAreaIdList(idList, request);
        Map<String, String> map = new HashMap<>();
        map.put("token", request.getHeader("token"));
        JSON.parseObject(HttpUtil.put(httpUaApi.getUrl() + httpUaApi.getModifyByArea(), null, map));
        Result result = new Result();
        return result.success("删除成功");
    }

    @Override
    public Result unique(LocationArea locationArea, HttpServletRequest request) {
        logger.info("区域验证唯一性，接收参数：{}", locationArea);
        Result result = new Result();
        if (null != locationArea) {
            if (locationArea.getId() != null) {
                if (locationArea.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LocationArea> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LocationArea::getName, locationArea.getName());
                    LocationArea slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null && !slLampPostByName.getId().equals(locationArea.getId())) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
            } else {
                if (locationArea.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LocationArea> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LocationArea::getName, locationArea.getName());
                    LocationArea slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
            }
        } else {
            return result.error("接收参数为空");
        }
        return result.success("");
    }

    @Override
    public Result updateArea(DlmReqLocationAreaVO locationAreaVO, HttpServletRequest request) {
        logger.info("修改区域，接收参数：{}", locationAreaVO);
        Result result = new Result();
        LocationArea locationArea = new LocationArea();
        BeanUtils.copyProperties(locationAreaVO, locationArea);
        Result unique = this.unique(locationArea, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            boolean rsg = this.updateById(locationArea);
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
    public Result get(Integer id, HttpServletRequest request) {
        LocationArea area = locationAreaMapper.selectById(id);
        DlmRespLocationAreaVO areaVO = new DlmRespLocationAreaVO();
        BeanUtils.copyProperties(area, areaVO);
        Result<DlmRespLocationAreaVO> result = new Result<>();
        return result.success(areaVO);
    }

    @Override
    public Result getDeviceByControlIdAndLoopId(DlmControlLoopOfDeviceQuery loopQuery, HttpServletRequest request) {
        logger.info("getDeviceByControlIdAndLoopId 根据集控id和分组id查询灯具，接收参数：{}", loopQuery);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        List<DlmRespLocationAreaByLoopVO> loopVO = null;
        Result<List<DlmRespLocationAreaByLoopVO>> result = new Result<>();
        // 新增分组时的灯具信息
        if (loopQuery.getControlId() != null && loopQuery.getLoopId() == null) {
            //判断是否是超级管理员
            if (!flag) {
                loopQuery.setAreaId(user.getAreaId());
            }
            // 获取登录用户下的所有集控器id集合
            DlmLocationControlQuery locationControlQuery = new DlmLocationControlQuery();
            locationControlQuery.setPageSize(0);
            Result resultData = locationControlService.listLocationControlWithPageByControlQuery(locationControlQuery, request);
            List<DlmRespLocationControlVO> data = (List<DlmRespLocationControlVO>) resultData.getData();
            List<Integer> controlIdList = data.stream().map(DlmRespLocationControlVO::getId).collect(Collectors.toList());
            // 查询该用户的集控器下的所有分组下的灯具信息
            LambdaQueryWrapper<ControlLoopDevice> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(ControlLoopDevice::getControlId, controlIdList);
            List<ControlLoopDevice> controlLoopDeviceList = controlLoopDeviceService.list(wrapper);
            List<Integer> deviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
            loopQuery.setDeviceIdList(deviceIdList);
            // 过滤出未绑定集控器的灯具信息
            loopVO = baseMapper.selectControlLoopDeviceList(loopQuery);
            return result.success(loopVO);
        }
        // 编辑分组时的灯具信息
        if (loopQuery.getControlId() != null && loopQuery.getLoopId() != null) {
            if (!flag) {
                loopQuery.setAreaId(user.getAreaId());
            }
            // 获取登录用户下的所有集控器id集合
            DlmLocationControlQuery locationControlQuery = new DlmLocationControlQuery();
            locationControlQuery.setPageSize(0);
            Result resultData = locationControlService.listLocationControlWithPageByControlQuery(locationControlQuery, request);
            List<DlmRespLocationControlVO> data = (List<DlmRespLocationControlVO>) resultData.getData();
            List<Integer> controlIdList = data.stream().map(DlmRespLocationControlVO::getId).collect(Collectors.toList());
            // 查询该用户的集控器下的所有分组下的灯具信息
            LambdaQueryWrapper<ControlLoopDevice> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(ControlLoopDevice::getControlId, controlIdList);
            List<ControlLoopDevice> controlLoopDeviceList = controlLoopDeviceService.list(wrapper);
            List<Integer> deviceIdList = controlLoopDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
            // 查询该分组下的设备
            LambdaQueryWrapper<ControlLoopDevice> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(ControlLoopDevice::getLoopId, loopQuery.getLoopId());
            List<ControlLoopDevice> loopDeviceList = controlLoopDeviceService.list(wrapper2);
            List<Integer> loopDeviceIdList = loopDeviceList.stream().map(ControlLoopDevice::getDeviceId).collect(Collectors.toList());
            // 两个集合的差集，得到绑定集控器的id集合，同时这个差集不包含该分组已经绑定的灯具信息
            deviceIdList.removeAll(loopDeviceIdList);
            loopQuery.setDeviceIdList(deviceIdList);
            // 过滤出未绑定的灯具信息同时这个集合中是包含绑定该分组的灯具
            loopVO = baseMapper.selectControlLoopDeviceList(loopQuery);
            return result.success(loopVO);
        }
        return new Result().success("暂无数据");
    }

    /*@Override
    public Result pulldownByDeviceType(HttpServletRequest request) {
        Result list = getList(null, 1, null, null, null, null, request);


        return list;
    }*/

}