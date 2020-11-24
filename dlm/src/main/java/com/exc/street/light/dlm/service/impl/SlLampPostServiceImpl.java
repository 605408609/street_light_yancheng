/**
 * @filename:SlLampPostServiceImpl 2020-03-17
 * @project dlm  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.config.parameter.*;
import com.exc.street.light.dlm.mapper.SlLampPostMapper;
import com.exc.street.light.dlm.service.*;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationArea;
import com.exc.street.light.resource.entity.dlm.LocationSite;
import com.exc.street.light.resource.entity.dlm.LocationStreet;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.qo.DlmLampPostQuery;
import com.exc.street.light.resource.utils.BaseConstantUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.DeviceTypeGroupVO;
import com.exc.street.light.resource.vo.DlmLampByAreaIdVO;
import com.exc.street.light.resource.vo.req.MapReqDeviceNumberVO;
import com.exc.street.light.resource.vo.resp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class SlLampPostServiceImpl extends ServiceImpl<SlLampPostMapper, SlLampPost> implements SlLampPostService {
    private static final Logger logger = LoggerFactory.getLogger(SlLampPostServiceImpl.class);
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
    private SlLampPostMapper slLampPostMapper;
    @Autowired
    private LocationSiteService locationSiteService;
    @Autowired
    private LocationStreetService locationStreetService;
    @Autowired
    private LocationAreaService locationAreaService;
    @Autowired
    private DeviceOnlineService deviceOnlineService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LogUserService logUserService;

    @Override
    public Result pulldown(Integer siteId, Integer lampPostId, Integer deviceTypeNum, String lampPostName, HttpServletRequest request) {
        logger.info("查询灯杆下拉列表，接收参数：siteId=" + siteId + ",lampPostName=" + lampPostName + ",deviceTypeNum=" + deviceTypeNum + ",lampPostId=" + lampPostId);
        DlmLampPostQuery dlmLampPostQuery = new DlmLampPostQuery();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if(!flag){
            dlmLampPostQuery.setAreaId(user.getAreaId());
        }
        // 如果streetId等于空，则wrapper不用判断站点条件
        if (siteId != null) {
            dlmLampPostQuery.setSiteId(siteId);
        }
        if (lampPostName != null && "".equals(lampPostName)) {
            dlmLampPostQuery.setLampPostNameOrNum(lampPostName);
        }
        List<DlmRespLampPostVO> list = this.baseMapper.queryList(dlmLampPostQuery);
        if (deviceTypeNum != 1 && deviceTypeNum != 4) {
            List<Integer> lampPostIdList = new ArrayList<>();
            // 获取所有绑定了当前设备的灯杆
            List<DlmRespLocationAreaVO> dlmRespLocationAreaVOList = (List<DlmRespLocationAreaVO>) locationAreaService.getList(5, deviceTypeNum, 0, 0, 0, null, request).getData();
            for (DlmRespLocationAreaVO dlmRespLocationAreaVO : dlmRespLocationAreaVOList) {
                List<DlmRespLocationStreetVO> dlmRespLocationStreetVOList = dlmRespLocationAreaVO.getChildrenList();
                for (DlmRespLocationStreetVO dlmRespLocationStreetVO : dlmRespLocationStreetVOList) {
                    List<DlmRespLocationSiteVO> dlmRespLocationSiteVOList = dlmRespLocationStreetVO.getChildrenList();
                    for (DlmRespLocationSiteVO dlmRespLocationSiteVO : dlmRespLocationSiteVOList) {
                        List<DlmRespLocationLampPostVO> dlmRespLocationLampPostVOList = dlmRespLocationSiteVO.getChildrenList();
                        List<Integer> collect = dlmRespLocationLampPostVOList.stream().map(DlmRespLocationLampPostVO::getId).collect(Collectors.toList());
                        lampPostIdList.addAll(collect);
                    }
                }
            }
            list = list.stream().filter(a -> !lampPostIdList.contains(a.getLampPostId())).collect(Collectors.toList());
        }
        if (lampPostId != null) {
            list = list.stream().filter(a -> !lampPostId.equals(a.getLampPostId())).collect(Collectors.toList());
        }
        List<SlLampPost> slLampPostList = new ArrayList<>();
        for (DlmRespLampPostVO dlmRespLampPostVO : list) {
            SlLampPost slLampPost = new SlLampPost();
            slLampPost.setId(dlmRespLampPostVO.getLampPostId());
            slLampPost.setName(dlmRespLampPostVO.getLampPostName());
            slLampPostList.add(slLampPost);
        }
        Result result = new Result();
        return result.success(slLampPostList);
    }

    @Override
    public Result get(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request) {
        logger.info("获取带设备基础信息的灯杆详情，接收参数：{}" + dlmLampPostQuery);

        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if (!flag) {
            dlmLampPostQuery.setAreaId(user.getAreaId());
        }
        Result result = new Result();
        //构造返回对象
        List<DlmRespLocationLampPostVO> dlmRespLocationLampPostVOList = new ArrayList<>();
        // 获取灯杆id集合
        if (dlmLampPostQuery != null) {
            if (dlmLampPostQuery.getId() != null) {
                List<Integer> lampPostIdListTemp = new ArrayList<>();
                lampPostIdListTemp.add(dlmLampPostQuery.getId());
                DlmLampByAreaIdVO dlmLampByAreaIdVO = new DlmLampByAreaIdVO();
                dlmLampByAreaIdVO.setAreaId(dlmLampPostQuery.getAreaId());
                dlmLampByAreaIdVO.setLampPostIdList(lampPostIdListTemp);
                List<DlmRespLocationLampPostVO> dlmRespVOList = this.baseMapper.listByAreaId(dlmLampByAreaIdVO);
                dlmRespLocationLampPostVOList = dlmRespVOList;
            } else if (dlmLampPostQuery.getLampPostIdList() != null && dlmLampPostQuery.getLampPostIdList().size() > 0) {
                DlmLampByAreaIdVO dlmLampByAreaIdVO = new DlmLampByAreaIdVO();
                dlmLampByAreaIdVO.setAreaId(dlmLampPostQuery.getAreaId());
                dlmLampByAreaIdVO.setLampPostIdList(dlmLampPostQuery.getLampPostIdList());
                List<DlmRespLocationLampPostVO> dlmRespVOList = this.baseMapper.listByAreaId(dlmLampByAreaIdVO);
                dlmRespLocationLampPostVOList = dlmRespVOList;
            } else if (dlmLampPostQuery.getSiteId() != null) {
                List<Integer> siteIdListTemp = new ArrayList<>();
                siteIdListTemp.add(dlmLampPostQuery.getSiteId());
                DlmLampByAreaIdVO dlmLampByAreaIdVO = new DlmLampByAreaIdVO();
                dlmLampByAreaIdVO.setAreaId(dlmLampPostQuery.getAreaId());
                dlmLampByAreaIdVO.setSiteIdList(siteIdListTemp);
                List<DlmRespLocationLampPostVO> dlmRespVOList = this.baseMapper.listByAreaId(dlmLampByAreaIdVO);
                dlmRespLocationLampPostVOList = dlmRespVOList;
            } else if (dlmLampPostQuery.getStreetId() != null) {
                List<Integer> streetIdListTemp = new ArrayList<>();
                streetIdListTemp.add(dlmLampPostQuery.getStreetId());
                DlmLampByAreaIdVO dlmLampByAreaIdVO = new DlmLampByAreaIdVO();
                dlmLampByAreaIdVO.setAreaId(dlmLampPostQuery.getAreaId());
                dlmLampByAreaIdVO.setStreetIdList(streetIdListTemp);
                List<DlmRespLocationLampPostVO> dlmRespVOList = this.baseMapper.listByAreaId(dlmLampByAreaIdVO);
                dlmRespLocationLampPostVOList = dlmRespVOList;
            } else if (dlmLampPostQuery.getAreaId() != null) {
                DlmLampByAreaIdVO dlmLampByAreaIdVO = new DlmLampByAreaIdVO();
                dlmLampByAreaIdVO.setAreaId(dlmLampPostQuery.getAreaId());
                List<DlmRespLocationLampPostVO> dlmRespVOList = this.baseMapper.listByAreaId(dlmLampByAreaIdVO);
                dlmRespLocationLampPostVOList = dlmRespVOList;
            } else {
                DlmLampByAreaIdVO dlmLampByAreaIdVO = new DlmLampByAreaIdVO();
                List<DlmRespLocationLampPostVO> dlmRespVOList = this.baseMapper.listByAreaId(dlmLampByAreaIdVO);
                dlmRespLocationLampPostVOList = dlmRespVOList;
            }
            if (dlmLampPostQuery.getLampPostNameOrNum() != null && dlmLampPostQuery.getLampPostNameOrNum() != "") {
                List<DlmRespLocationLampPostVO> slLampPostCollect = dlmRespLocationLampPostVOList.stream().filter(a -> a.getName().contains(dlmLampPostQuery.getLampPostNameOrNum()) || a.getLampPostNum().contains(dlmLampPostQuery.getLampPostNameOrNum())).collect(Collectors.toList());
                dlmRespLocationLampPostVOList = slLampPostCollect;
            }
        }
        // 返回消息
        String message = "";
        if (dlmRespLocationLampPostVOList == null || dlmRespLocationLampPostVOList.size() == 0 || dlmRespLocationLampPostVOList.get(0) == null) {
            return result.success("没有灯杆信息", dlmRespLocationLampPostVOList);
        }
        // 灯杆id集合
        List<Integer> slLampPostIdList = dlmRespLocationLampPostVOList.stream().map(DlmRespLocationLampPostVO::getId).distinct().collect(Collectors.toList());
        // 获取各个设备详情
        // 智慧照明1
        List<DlmRespDevicePublicParVO> dlmRespDevicePublicParVOList = new ArrayList<>();
        if (dlmLampPostQuery == null || dlmLampPostQuery.getDeviceTypeId() == null || dlmLampPostQuery.getDeviceTypeId() == BaseConstantUtil.DEVICE_TYPE_SL) {
            dlmRespDevicePublicParVOList.addAll(singleLampParamService.getDlmRespDeviceVOList(slLampPostIdList));
        }
        // 公共WIFI2
        if (dlmLampPostQuery == null || dlmLampPostQuery.getDeviceTypeId() == null || dlmLampPostQuery.getDeviceTypeId() == BaseConstantUtil.DEVICE_TYPE_WIFI) {
            dlmRespDevicePublicParVOList.addAll(wifiApDeviceService.getDlmRespDeviceVOList(slLampPostIdList));
        }
        // 公共广播3
        if (dlmLampPostQuery == null || dlmLampPostQuery.getDeviceTypeId() == null || dlmLampPostQuery.getDeviceTypeId() == BaseConstantUtil.DEVICE_TYPE_PB) {
            dlmRespDevicePublicParVOList.addAll(radioDeviceService.getDlmRespDeviceVOList(slLampPostIdList));
        }
        // 智能安防4
        if (dlmLampPostQuery == null || dlmLampPostQuery.getDeviceTypeId() == null || dlmLampPostQuery.getDeviceTypeId() == BaseConstantUtil.DEVICE_TYPE_SS) {
            dlmRespDevicePublicParVOList.addAll(ssDeviceService.getDlmRespDeviceVOList(slLampPostIdList));
        }
        // 信息发布5
        if (dlmLampPostQuery == null || dlmLampPostQuery.getDeviceTypeId() == null || dlmLampPostQuery.getDeviceTypeId() == BaseConstantUtil.DEVICE_TYPE_IR) {
            dlmRespDevicePublicParVOList.addAll(screenDeviceService.getDlmRespDeviceVOList(slLampPostIdList));
        }
        // 一键呼叫6
        if (dlmLampPostQuery == null || dlmLampPostQuery.getDeviceTypeId() == null || dlmLampPostQuery.getDeviceTypeId() == BaseConstantUtil.DEVICE_TYPE_OCC) {
            dlmRespDevicePublicParVOList.addAll(ahDeviceService.getDlmRespDeviceVOList(slLampPostIdList));
        }
        // 环境监测7
        if (dlmLampPostQuery == null || dlmLampPostQuery.getDeviceTypeId() == null || dlmLampPostQuery.getDeviceTypeId() == BaseConstantUtil.DEVICE_TYPE_EM) {
            dlmRespDevicePublicParVOList.addAll(meteorologicalDeviceService.getDlmRespDeviceVOList(slLampPostIdList));
        }
        for (DlmRespLocationLampPostVO dlmRespLocationLampPostVO : dlmRespLocationLampPostVOList) {
            List<DlmRespDevicePublicParVO> collect = dlmRespDevicePublicParVOList.stream().filter(a -> a.getSuperId().equals(dlmRespLocationLampPostVO.getId())).collect(Collectors.toList());
            dlmRespLocationLampPostVO.setChildrenList(collect);
            if (collect != null && collect.size() > 0) {
                dlmRespLocationLampPostVO.setDeviceNumber(collect.size());
            } else {
                dlmRespLocationLampPostVO.setDeviceNumber(0);
            }

        }
        return result.success(message, dlmRespLocationLampPostVOList);
    }

    @Override
    public Result getDevice(String name, int type, HttpServletRequest request) {
        logger.info("获取设备名称列表");
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        Integer areaId = null;
        if (!flag) {
            areaId = user.getAreaId();
        }
        DlmLampByAreaIdVO dlmLampByAreaIdVO = new DlmLampByAreaIdVO();
        dlmLampByAreaIdVO.setAreaId(areaId);
        List<DlmRespLocationLampPostVO> dlmRespLocationLampPostVOList = this.baseMapper.listByAreaId(dlmLampByAreaIdVO);
        // 返回消息
        String message = "";
        if (dlmRespLocationLampPostVOList == null || dlmRespLocationLampPostVOList.size() == 0 || dlmRespLocationLampPostVOList.get(0) == null) {
            return result.error("没有相关灯杆");
        }
        // 灯杆id集合
        List<Integer> slLampPostIdList = dlmRespLocationLampPostVOList.stream().map(DlmRespLocationLampPostVO::getId).distinct().collect(Collectors.toList());
        List<DlmRespDevicePublicParVO> dlmRespDeviceVOList = new ArrayList<>();
        switch (type) {
            case BaseConstantUtil.DEVICE_TYPE_DLM:
                List<DlmRespLocationLampPostVO> slLampPostCollect = new ArrayList<>();
                if (name != null && !"".equals(name)) {
                    slLampPostCollect = dlmRespLocationLampPostVOList.stream().filter(a -> a.getName().contains(name)).collect(Collectors.toList());
                } else {
                    slLampPostCollect = dlmRespLocationLampPostVOList;
                }
                if (slLampPostCollect.size() > 0) {
                    // 灯杆信息
                    for (DlmRespLocationLampPostVO dlmRespLocationLampPostVO : slLampPostCollect) {
                        DlmRespDevicePublicParVO dlmRespDevicePublicParVO = new DlmRespDevicePublicParVO();
                        dlmRespDevicePublicParVO.setId(dlmRespLocationLampPostVO.getId());
                        dlmRespDevicePublicParVO.setName(dlmRespLocationLampPostVO.getName());
                        dlmRespDevicePublicParVO.setSuperId(dlmRespLocationLampPostVO.getId());
                        dlmRespDevicePublicParVO.setLampPostId(dlmRespLocationLampPostVO.getId());
                        dlmRespDeviceVOList.add(dlmRespDevicePublicParVO);
                    }
                }
                break;
            case BaseConstantUtil.DEVICE_TYPE_SL:
                // 智慧照明1
                dlmRespDeviceVOList = singleLampParamService.getDlmRespDeviceVOList(slLampPostIdList);
                break;
            case BaseConstantUtil.DEVICE_TYPE_WIFI:
                // 公共WIFI2
                dlmRespDeviceVOList = wifiApDeviceService.getDlmRespDeviceVOList(slLampPostIdList);
                break;
            case BaseConstantUtil.DEVICE_TYPE_PB:
                // 公共广播3
                dlmRespDeviceVOList = radioDeviceService.getDlmRespDeviceVOList(slLampPostIdList);
                break;
            case BaseConstantUtil.DEVICE_TYPE_SS:
                // 智能安防4
                dlmRespDeviceVOList = ssDeviceService.getDlmRespDeviceVOList(slLampPostIdList);
                break;
            case BaseConstantUtil.DEVICE_TYPE_IR:
                // 信息发布5
                dlmRespDeviceVOList = screenDeviceService.getDlmRespDeviceVOList(slLampPostIdList);
                break;
            case BaseConstantUtil.DEVICE_TYPE_OCC:
                // 一键呼叫6
                dlmRespDeviceVOList = ahDeviceService.getDlmRespDeviceVOList(slLampPostIdList);
                break;
            case BaseConstantUtil.DEVICE_TYPE_EM:
                //环境监测7
                dlmRespDeviceVOList = meteorologicalDeviceService.getDlmRespDeviceVOList(slLampPostIdList);
                break;
            default:
                return result.error("输入类型有误");
        }
        List<DlmRespDevicePublicParVO> collect = new ArrayList<>();
        if (name != null && !"".equals(name)) {
            collect = dlmRespDeviceVOList.stream().filter(a -> a.getName().contains(name)).collect(Collectors.toList());
        } else {
            collect = dlmRespDeviceVOList;
        }
        // 前端数据格式需求
        Map map  = new HashMap();
        map.put("records",collect);
        return result.success(map);
    }


    @Override
    public Result getNoDetails(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request) {
        logger.info("获取带设备基础信息的灯杆详情，接收参数：{}" + dlmLampPostQuery);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("token", request.getHeader("token"));
        String resultJson = HttpUtil.get(httpUaApi.getUrl() + httpUaApi.getUser() + userId, headerMap);
        JSONObject jsonObjectResult = JSONObject.parseObject(resultJson);
        JSONObject jsonObjectData = jsonObjectResult.getJSONObject("data");
        dlmLampPostQuery.setAreaId(jsonObjectData.getInteger("areaId"));
        Result result = new Result();
        //构造返回对象
        List<DlmRespLocationLampPostVO> dlmRespLocationLampPostVOList = new ArrayList<>();
        // 获取灯杆id集合
        List<Integer> lampPostIdList = new ArrayList<>();
        if (dlmLampPostQuery != null) {
            if (dlmLampPostQuery.getId() != null) {
                lampPostIdList.add(dlmLampPostQuery.getId());
            } else if (dlmLampPostQuery.getLampPostIdList() != null && dlmLampPostQuery.getLampPostIdList().size() > 0) {
                lampPostIdList = dlmLampPostQuery.getLampPostIdList();
            } else if (dlmLampPostQuery.getSiteId() != null) {
                // 获取灯杆id集合
                List<Integer> siteIdList = new ArrayList<>();
                siteIdList.add(dlmLampPostQuery.getSiteId());
                List<SlLampPost> slLampPostList = (List<SlLampPost>) this.getBySite(siteIdList, request).getData();
                List<Integer> lampPostIdCollect = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
                lampPostIdList.addAll(lampPostIdCollect);
            } else if (dlmLampPostQuery.getStreetId() != null) {
                // 获取站点id集合
                List<Integer> streetIdList = new ArrayList<>();
                streetIdList.add(dlmLampPostQuery.getStreetId());
                List<LocationSite> siteList = (List<LocationSite>) locationSiteService.getByStreet(streetIdList, request).getData();
                List<Integer> siteIdList = siteList.stream().map(LocationSite::getId).collect(Collectors.toList());
                if (siteIdList != null && siteIdList.size() > 0) {
                    // 获取灯杆id集合
                    List<SlLampPost> slLampPostList = (List<SlLampPost>) this.getBySite(siteIdList, request).getData();
                    List<Integer> lampPostIdCollect = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
                    lampPostIdList.addAll(lampPostIdCollect);
                }
            } else if (dlmLampPostQuery.getAreaId() != null) {
                // 获取街道id集合
                List<Integer> areaIdList = new ArrayList<>();
                areaIdList.add(dlmLampPostQuery.getAreaId());
                List<LocationStreet> streetList = (List<LocationStreet>) locationStreetService.getByArea(areaIdList, request).getData();
                List<Integer> streetIdList = streetList.stream().map(LocationStreet::getId).collect(Collectors.toList());
                if (streetIdList != null && streetIdList.size() > 0) {
                    // 获取站点id集合
                    List<LocationSite> siteList = (List<LocationSite>) locationSiteService.getByStreet(streetIdList, request).getData();
                    List<Integer> siteIdList = siteList.stream().map(LocationSite::getId).collect(Collectors.toList());
                    if (siteIdList != null && siteIdList.size() > 0) {
                        // 获取灯杆id集合
                        List<SlLampPost> slLampPostList = (List<SlLampPost>) this.getBySite(siteIdList, request).getData();
                        List<Integer> lampPostIdCollect = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
                        lampPostIdList.addAll(lampPostIdCollect);
                    }
                }
            }
            if (dlmLampPostQuery.getLampPostNameOrNum() != null && dlmLampPostQuery.getLampPostNameOrNum() != "") {
                LambdaQueryWrapper<SlLampPost> wrapper = new LambdaQueryWrapper<SlLampPost>();
                wrapper.like(SlLampPost::getName, dlmLampPostQuery.getLampPostNameOrNum())
                        .or().like(SlLampPost::getNum, dlmLampPostQuery.getLampPostNameOrNum());
                List<SlLampPost> list = this.list(wrapper);
                List<Integer> lampPostIdCollect = list.stream().map(SlLampPost::getId).collect(Collectors.toList());
                lampPostIdList.addAll(lampPostIdCollect);
            }
        }
        LambdaQueryWrapper<SlLampPost> wrapper = new LambdaQueryWrapper();
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            wrapper.in(SlLampPost::getId, lampPostIdList);
        }
        wrapper.orderByDesc(SlLampPost::getCreateTime);
        List<SlLampPost> slLampPostsList = this.list(wrapper);
        // 返回消息
        String message = "";
        if (slLampPostsList == null || slLampPostsList.size() == 0 || slLampPostsList.get(0) == null) {
            return result.error("没有灯杆信息");
        }
        // 获取各个设备详情
        for (SlLampPost slLampPostTemp : slLampPostsList) {
            DlmRespLocationLampPostVO dlmRespLocationLampPostVO = new DlmRespLocationLampPostVO();
            // 构造返回对象
            dlmRespLocationLampPostVO.setId(slLampPostTemp.getId());
            dlmRespLocationLampPostVO.setName(slLampPostTemp.getName());
            dlmRespLocationLampPostVO.setLampPostNum(slLampPostTemp.getNum());
            dlmRespLocationLampPostVO.setLampPostModel(slLampPostTemp.getModel());
            dlmRespLocationLampPostVO.setLampPostManufacturer(slLampPostTemp.getManufacturer());
            dlmRespLocationLampPostVO.setLampPostLongitude(slLampPostTemp.getLongitude());
            dlmRespLocationLampPostVO.setLampPostLocation(slLampPostTemp.getLocation());
            dlmRespLocationLampPostVO.setLampPostLatitude(slLampPostTemp.getLatitude());
            dlmRespLocationLampPostVO.setCreateTime(slLampPostTemp.getCreateTime());

            Integer siteId = slLampPostTemp.getSiteId();
            LocationSite byIdSite = locationSiteService.getById(siteId);
            if (byIdSite != null) {
                Integer streetId = byIdSite.getStreetId();
                LocationStreet byIdStreet = locationStreetService.getById(streetId);
                if (byIdStreet != null) {
                    Integer areaId = byIdStreet.getAreaId();
                    LocationArea byIdArea = locationAreaService.getById(areaId);
                    if (byIdArea != null) {
                        String ids = areaId + "," + streetId + "," + siteId;
                        String names = byIdArea.getName() + "," + byIdStreet.getName() + "," + byIdSite.getName();
                        dlmRespLocationLampPostVO.setIds(ids);
                        dlmRespLocationLampPostVO.setNames(names);
                        dlmRespLocationLampPostVO.setSuperId(siteId);
                        dlmRespLocationLampPostVO.setSuperName(byIdSite.getName());
                        dlmRespLocationLampPostVO.setStreetId(streetId);
                        dlmRespLocationLampPostVO.setStreetName(byIdStreet.getName());
                        dlmRespLocationLampPostVO.setAreaId(areaId);
                        dlmRespLocationLampPostVO.setAreaName(byIdArea.getName());
                    }
                }
            }
            dlmRespLocationLampPostVOList.add(dlmRespLocationLampPostVO);
        }
        return result.success(message, dlmRespLocationLampPostVOList);
    }


    @Override
    public Result batchDelete(String ids, HttpServletRequest request) {
        logger.info("批量删除灯杆，接收参数：{}", ids);
        List<Integer> idListFromString = StringConversionUtil.getIdListFromString(ids);
        this.removeByIds(idListFromString);
        // 取消设备关联灯杆
        this.cancelDevice(idListFromString, request);
        Result result = new Result();
        return result.success("批量删除成功");
    }

//    @Override
//    public Result device(String deviceName, HttpServletRequest request) {
//        logger.info("设备列表，接收参数：deviceName=" + deviceName);
//        String message = "";
//        List<DlmRespDevicePublicParVO> dlmRespDevicePublicParVOList = new ArrayList<>();
//        // 智慧照明1
//        try {
//            JSONObject slDeviceResult = JSON.parseObject(HttpUtil.get(httpSlApi.getUrl() + httpSlApi.getDeviceAll() + "?deviceName=" + deviceName));
//            JSONArray slDeviceResultArr = slDeviceResult.getJSONArray("data");
//            for (int i = 0; i < slDeviceResultArr.size(); i++) {
//                DlmRespDevicePublicParVO dlmRespDevicePublicParVO = deviceJsonObjectToPublicParVO(slDeviceResultArr.getJSONObject(i), 1, null);
//                dlmRespDevicePublicParVOList.add(dlmRespDevicePublicParVO);
//            }
//        } catch (Exception e) {
//            logger.info("智慧照明接口调用失败，返回为空!");
//            message += "智慧照明接口调用失败，返回为空！\n";
//        }
//        // 公共WIFI2
//        // 公共广播3
//        // 智能安防4
//        try {
//            JSONObject ssDeviceResult = JSON.parseObject(HttpUtil.get(httpSsApi.getUrl() + httpSsApi.getDeviceAll() + "?deviceName=" + deviceName));
//            JSONArray ssDeviceResultArr = ssDeviceResult.getJSONArray("data");
//            for (int i = 0; i < ssDeviceResultArr.size(); i++) {
//                DlmRespDevicePublicParVO ssRespDevicePublicParVO = deviceJsonObjectToPublicParVO(ssDeviceResultArr.getJSONObject(i), 4, null);
//                dlmRespDevicePublicParVOList.add(ssRespDevicePublicParVO);
//            }
//        } catch (Exception e) {
//            logger.info("智能安防接口调用失败，返回为空!");
//            message += "智能安防接口调用失败，返回为空！\n";
//        }
//        // 信息发布5
//        try {
//            JSONObject irDeviceResult = JSON.parseObject(HttpUtil.get(httpIrApi.getUrl() + httpIrApi.getDeviceAll() + "?deviceName=" + deviceName));
//            JSONArray irDeviceResultArr = irDeviceResult.getJSONArray("data");
//            for (int i = 0; i < irDeviceResultArr.size(); i++) {
//                DlmRespDevicePublicParVO irRespDevicePublicParVO = deviceJsonObjectToPublicParVO(irDeviceResultArr.getJSONObject(i), 5, null);
//                dlmRespDevicePublicParVOList.add(irRespDevicePublicParVO);
//            }
//        } catch (Exception e) {
//            logger.info("信息发布接口调用失败，返回为空!");
//            message += "信息发布接口调用失败，返回为空！\n";
//        }
//        // 一键呼叫6
//        try {
//            JSONObject occDeviceResult = JSON.parseObject(HttpUtil.get(httpOccApi.getUrl() + httpOccApi.getDeviceAll() + "?deviceName=" + deviceName));
//            JSONArray ahDeviceResultArr = occDeviceResult.getJSONArray("data");
//            for (int i = 0; i < ahDeviceResultArr.size(); i++) {
//                DlmRespDevicePublicParVO ahRespDevicePublicParVO = deviceJsonObjectToPublicParVO(ahDeviceResultArr.getJSONObject(i), 6, null);
//                dlmRespDevicePublicParVOList.add(ahRespDevicePublicParVO);
//            }
//        } catch (Exception e) {
//            logger.info("一键呼叫设备接口调用失败，返回为空!");
//            message += "一键呼叫设备接口调用失败，返回为空！\n";
//        }
//        // 环境监测7
//        Result result = new Result();
//        return result.success(message, dlmRespDevicePublicParVOList);
//    }

    @Override
    public Result add(SlLampPost slLampPost, HttpServletRequest request) {
        logger.info("添加灯杆，接收参数：{}", slLampPost);
        Result result = new Result();
        Result unique = this.unique(slLampPost, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            slLampPost.setCreateTime(new Date());
            boolean save = this.save(slLampPost);
            if (save) {
                result.success("添加灯杆成功");
            } else {
                result.success("添加灯杆失败");
            }
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result getBySite(List<Integer> siteIdList, HttpServletRequest request) {
        logger.info("根据站点id集合查询灯杆集合，接收参数:{}", siteIdList);
        LambdaQueryWrapper<SlLampPost> wrapper = new LambdaQueryWrapper();
        // 如果streetId等于空，则wrapper不用判断条件
        if (siteIdList != null && siteIdList.size() > 0 && siteIdList.get(0) != null) {
            wrapper.in(SlLampPost::getSiteId, siteIdList);
        }
        List<SlLampPost> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result getByGroup(List<Integer> groupIdList, HttpServletRequest request) {
        logger.info("查询灯杆集合（根据分组id集合get），接收参数:{}", groupIdList);
        LambdaQueryWrapper<SlLampPost> wrapper = new LambdaQueryWrapper();
        // 如果streetId等于空，则wrapper不用判断条件
        if (groupIdList != null && groupIdList.size() > 0) {
            wrapper.in(SlLampPost::getGroupId, groupIdList);
        }
        List<SlLampPost> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public DlmRespDevicePublicParVO deviceJsonObjectToPublicParVO(JSONObject slDeviceResultObj, Integer deviceType, SlLampPost slLampPostTemp) {
        DlmRespDevicePublicParVO dlmRespDevicePublicParVO = null;
        if (deviceType == BaseConstantUtil.DEVICE_TYPE_SL) {
            dlmRespDevicePublicParVO = new DlmRespDevicePublicParVO<SlRespSystemDeviceVO>();
            SlRespSystemDeviceVO lampDevice = JSONObject.toJavaObject(slDeviceResultObj, SlRespSystemDeviceVO.class);
            dlmRespDevicePublicParVO.setDlmRespDevice(lampDevice);
        } else if (deviceType == BaseConstantUtil.DEVICE_TYPE_WIFI) {
            dlmRespDevicePublicParVO = new DlmRespDevicePublicParVO<WifiApDevice>();
            WifiApDevice wifiApDevice = JSONObject.toJavaObject(slDeviceResultObj, WifiApDevice.class);
            dlmRespDevicePublicParVO.setDlmRespDevice(wifiApDevice);
        } else if (deviceType == BaseConstantUtil.DEVICE_TYPE_PB) {
            dlmRespDevicePublicParVO = new DlmRespDevicePublicParVO<RadioDevice>();
            RadioDevice radioDevice = JSONObject.toJavaObject(slDeviceResultObj, RadioDevice.class);
            dlmRespDevicePublicParVO.setDlmRespDevice(radioDevice);
        } else if (deviceType == BaseConstantUtil.DEVICE_TYPE_SS) {
            dlmRespDevicePublicParVO = new DlmRespDevicePublicParVO<SsDevice>();
            SsDevice ssDevice = JSONObject.toJavaObject(slDeviceResultObj, SsDevice.class);
            dlmRespDevicePublicParVO.setDlmRespDevice(ssDevice);
        } else if (deviceType == BaseConstantUtil.DEVICE_TYPE_IR) {
            dlmRespDevicePublicParVO = new DlmRespDevicePublicParVO<ScreenDevice>();
            ScreenDevice screenDevice = JSONObject.toJavaObject(slDeviceResultObj, ScreenDevice.class);
            dlmRespDevicePublicParVO.setDlmRespDevice(screenDevice);
        } else if (deviceType == BaseConstantUtil.DEVICE_TYPE_OCC) {
            dlmRespDevicePublicParVO = new DlmRespDevicePublicParVO<AhDevice>();
            AhDevice ahDevice = JSONObject.toJavaObject(slDeviceResultObj, AhDevice.class);
            dlmRespDevicePublicParVO.setDlmRespDevice(ahDevice);
        } else if (deviceType == BaseConstantUtil.DEVICE_TYPE_EM) {
            dlmRespDevicePublicParVO = new DlmRespDevicePublicParVO<MeteorologicalDevice>();
            MeteorologicalDevice meteorologicalDevice = JSONObject.toJavaObject(slDeviceResultObj, MeteorologicalDevice.class);
            dlmRespDevicePublicParVO.setDlmRespDevice(meteorologicalDevice);
        } else if (deviceType == BaseConstantUtil.DEVICE_TYPE_GW) {
            dlmRespDevicePublicParVO = new DlmRespDevicePublicParVO<ElectricityNode>();
            ElectricityNode electricityNode = JSONObject.toJavaObject(slDeviceResultObj, ElectricityNode.class);
            dlmRespDevicePublicParVO.setDlmRespDevice(electricityNode);
        }
        dlmRespDevicePublicParVO.setId(slDeviceResultObj.getInteger("id"));
        dlmRespDevicePublicParVO.setPartId("device" + slDeviceResultObj.getInteger("id"));
        dlmRespDevicePublicParVO.setName(slDeviceResultObj.getString("name"));
        if (deviceType == BaseConstantUtil.DEVICE_TYPE_SL) {
            dlmRespDevicePublicParVO.setName(slDeviceResultObj.getString("name") + "<" + slDeviceResultObj.getString("lampPosition") + ">");
        }
        dlmRespDevicePublicParVO.setNetworkState(slDeviceResultObj.getInteger("networkState"));
        dlmRespDevicePublicParVO.setNum(slDeviceResultObj.getString("num"));
        dlmRespDevicePublicParVO.setLastOnlineTime(slDeviceResultObj.getDate("lastOnlineTime"));
        if(deviceType == BaseConstantUtil.DEVICE_TYPE_SL){
            dlmRespDevicePublicParVO.setNetworkState(slDeviceResultObj.getInteger("isOnline"));
        }
        dlmRespDevicePublicParVO.setDeviceType(deviceType);
        if (slLampPostTemp != null) {
            dlmRespDevicePublicParVO.setLampPostId(slLampPostTemp.getId());
            dlmRespDevicePublicParVO.setSuperId(slLampPostTemp.getId());
            dlmRespDevicePublicParVO.setSuperName(slLampPostTemp.getName());
        }
        return dlmRespDevicePublicParVO;
    }

    @Override
    public Result getPage(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request) {
        logger.info("灯杆分页条件查询,接收参数:{}", dlmLampPostQuery);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        if (!flag) {
            dlmLampPostQuery.setAreaId(user.getAreaId());
        }
        // 查询灯杆分页信息
        IPage<DlmRespLampPostVO> iPage = new Page<DlmRespLampPostVO>(dlmLampPostQuery.getPageNum(), dlmLampPostQuery.getPageSize());
//        IPage<SlLampPost> iPage = new Page<SlLampPost>(dlmLampPostQuery.getPageNum(), dlmLampPostQuery.getPageSize());
        List<DlmRespLampPostVO> dlmRespLampPostVOList = this.baseMapper.query(iPage, dlmLampPostQuery);
//        LambdaQueryWrapper<SlLampPost> lambdaQueryWrapper = new LambdaQueryWrapper<SlLampPost>();
//        lambdaQueryWrapper.orderByDesc(SlLampPost::getId);
//        IPage page = this.page(iPage, lambdaQueryWrapper);
//        List<SlLampPost> records = page.getRecords();
        // 获取当前查询灯杆的id集合
        List<Integer> lampPostIdList = dlmRespLampPostVOList.stream().map(DlmRespLampPostVO::getLampPostId).collect(Collectors.toList());
//        List<Integer> lampPostIdList = records.stream().map(SlLampPost::getId).collect(Collectors.toList());
        // 构造请求对象，请求获取灯杆关联设备详情
        DlmLampPostQuery getQuery = new DlmLampPostQuery();
        // 防止获取所有灯杆
        if(lampPostIdList == null || lampPostIdList.size() == 0 || lampPostIdList.get(0) == null){
            lampPostIdList.add(0);
        }
        getQuery.setLampPostIdList(lampPostIdList);
        Result deviceResult = this.get(getQuery, request);
        List<DlmRespLocationLampPostVO> dlmRespLocationLampPostVOList = (List<DlmRespLocationLampPostVO>) deviceResult.getData();

//        for (DlmRespLampPostVO dlmRespLampPostVO : dlmRespLampPostVOList) {
//            List<DlmRespDevicePublicParVO> collect = null;
//            if (dlmRespLocationLampPostVOList != null && dlmRespLocationLampPostVOList.size() > 0) {
//                List<DlmRespLocationLampPostVO> LampPostCollect = dlmRespLocationLampPostVOList.stream().filter(a -> dlmRespLampPostVO.getLampPostId().equals(a.getId())).collect(Collectors.toList());
//                if (LampPostCollect != null && LampPostCollect.size() > 0) {
//                    collect = LampPostCollect.get(0).getChildrenList();
//                }
//            }
//            int size = 0;
//            if (collect != null && collect.size() > 0) {
//                size = collect.size();
//            }
//            dlmRespLampPostVO.setDeviceNumber(size);
//        }
        IPage<DlmRespLocationLampPostVO> iPageResult = new Page<DlmRespLocationLampPostVO>(dlmLampPostQuery.getPageNum(), dlmLampPostQuery.getPageSize());
        iPageResult.setRecords(dlmRespLocationLampPostVOList);
        iPageResult.setSize(iPage.getSize());
        iPageResult.setTotal(iPage.getTotal());
        iPageResult.setPages(iPage.getPages());
        iPageResult.setCurrent(iPage.getCurrent());
        deviceResult.setData(iPageResult);
        return deviceResult;
    }

    @Override
    public void updateToNoGroup(Integer groupId) {
        slLampPostMapper.updateToNoGroup(groupId);
    }

    @Override
    public Result mapNumber(int version, Integer type, HttpServletRequest request) {
        logger.info("获取各个设备的个数及在线率");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String message = "";
        List<MapReqDeviceNumberVO> mapReqDeviceNumberVOList = new ArrayList<>();
        MapReqDeviceNumberVO mapReqDeviceNumberVO1 = new MapReqDeviceNumberVO();
        MapReqDeviceNumberVO mapReqDeviceNumberVO2 = new MapReqDeviceNumberVO();
        MapReqDeviceNumberVO mapReqDeviceNumberVO3 = new MapReqDeviceNumberVO();
        MapReqDeviceNumberVO mapReqDeviceNumberVO4 = new MapReqDeviceNumberVO();
        MapReqDeviceNumberVO mapReqDeviceNumberVO5 = new MapReqDeviceNumberVO();
        MapReqDeviceNumberVO mapReqDeviceNumberVO6 = new MapReqDeviceNumberVO();
        MapReqDeviceNumberVO mapReqDeviceNumberVO7 = new MapReqDeviceNumberVO();
        mapReqDeviceNumberVO1.setDeviceType(BaseConstantUtil.DEVICE_TYPE_SL);
        mapReqDeviceNumberVO2.setDeviceType(BaseConstantUtil.DEVICE_TYPE_WIFI);
        mapReqDeviceNumberVO3.setDeviceType(BaseConstantUtil.DEVICE_TYPE_PB);
        mapReqDeviceNumberVO4.setDeviceType(BaseConstantUtil.DEVICE_TYPE_SS);
        mapReqDeviceNumberVO5.setDeviceType(BaseConstantUtil.DEVICE_TYPE_IR);
        mapReqDeviceNumberVO6.setDeviceType(BaseConstantUtil.DEVICE_TYPE_OCC);
        mapReqDeviceNumberVO7.setDeviceType(BaseConstantUtil.DEVICE_TYPE_EM);
        mapReqDeviceNumberVO1.setOnlineRate(0);
        mapReqDeviceNumberVO2.setOnlineRate(0);
        mapReqDeviceNumberVO3.setOnlineRate(0);
        mapReqDeviceNumberVO4.setOnlineRate(0);
        mapReqDeviceNumberVO5.setOnlineRate(0);
        mapReqDeviceNumberVO6.setOnlineRate(0);
        mapReqDeviceNumberVO7.setOnlineRate(0);

        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean flag = logUserService.isAdmin(userId);
        List<Integer> lampPostIdList = new ArrayList<>();
        if (!flag) {
            DlmLampByAreaIdVO dlmLampByAreaIdVO = new DlmLampByAreaIdVO();
            dlmLampByAreaIdVO.setAreaId(user.getAreaId());
            List<DlmRespLocationLampPostVO> dlmRespVOList = this.baseMapper.listByAreaId(dlmLampByAreaIdVO);
            lampPostIdList = dlmRespVOList.stream().map(DlmRespLocationLampPostVO::getId).distinct().collect(Collectors.toList());
            // 防止普通用戶，沒有灯杆时查询全部
            if (lampPostIdList == null || lampPostIdList.size() == 0) {
                lampPostIdList.add(-1);
            }
        }

        // 智慧照明1
        List<DlmRespDevicePublicParVO> dlmRespDeviceVOListSl = singleLampParamService.getDlmRespDeviceVOList(lampPostIdList);
        // 公共WIFI2
        List<DlmRespDevicePublicParVO> dlmRespDeviceVOListWifi = wifiApDeviceService.getDlmRespDeviceVOList(lampPostIdList);
        // 公共广播3
        List<DlmRespDevicePublicParVO> dlmRespDeviceVOListPb = radioDeviceService.getDlmRespDeviceVOList(lampPostIdList);
        // 智能安防4
        List<DlmRespDevicePublicParVO> dlmRespDeviceVOListSs = ssDeviceService.getDlmRespDeviceVOList(lampPostIdList);
        // 信息发布5
        List<DlmRespDevicePublicParVO> dlmRespDeviceVOListIr = screenDeviceService.getDlmRespDeviceVOList(lampPostIdList);
        // 一键呼叫6
        List<DlmRespDevicePublicParVO> dlmRespDeviceVOListOcc = ahDeviceService.getDlmRespDeviceVOList(lampPostIdList);
        // 环境监测7
        List<DlmRespDevicePublicParVO> dlmRespDeviceVOListEm = meteorologicalDeviceService.getDlmRespDeviceVOList(lampPostIdList);

        if (dlmRespDeviceVOListSl != null && dlmRespDeviceVOListSl.size() > 0) {
            mapReqDeviceNumberVO1.setNumber(dlmRespDeviceVOListSl.size());
            List<DlmRespDevicePublicParVO> collect = dlmRespDeviceVOListSl.stream().filter(a -> a.getNetworkState().equals(1)).collect(Collectors.toList());
            mapReqDeviceNumberVO1.setOnlineRate(collect.size() * 100 / dlmRespDeviceVOListSl.size());
        } else {
            mapReqDeviceNumberVO1.setNumber(0);
        }
        if (dlmRespDeviceVOListWifi != null && dlmRespDeviceVOListWifi.size() > 0) {
            mapReqDeviceNumberVO2.setNumber(dlmRespDeviceVOListWifi.size());
            List<DlmRespDevicePublicParVO> collect = dlmRespDeviceVOListWifi.stream().filter(a -> a.getNetworkState().equals(1)).collect(Collectors.toList());
            mapReqDeviceNumberVO2.setOnlineRate(collect.size() * 100 / dlmRespDeviceVOListWifi.size());
        } else {
            mapReqDeviceNumberVO2.setNumber(0);
        }

        if (dlmRespDeviceVOListPb != null && dlmRespDeviceVOListPb.size() > 0) {
            mapReqDeviceNumberVO3.setNumber(dlmRespDeviceVOListPb.size());
            List<DlmRespDevicePublicParVO> collect = dlmRespDeviceVOListPb.stream().filter(a -> a.getNetworkState().equals(1)).collect(Collectors.toList());
            mapReqDeviceNumberVO3.setOnlineRate(collect.size() * 100 / dlmRespDeviceVOListPb.size());
        } else {
            mapReqDeviceNumberVO3.setNumber(0);
        }

        if (dlmRespDeviceVOListSs != null && dlmRespDeviceVOListSs.size() > 0) {
            mapReqDeviceNumberVO4.setNumber(dlmRespDeviceVOListSs.size());
            List<DlmRespDevicePublicParVO> collect = dlmRespDeviceVOListSs.stream().filter(a -> a.getNetworkState().equals(1)).collect(Collectors.toList());
            mapReqDeviceNumberVO4.setOnlineRate(collect.size() * 100 / dlmRespDeviceVOListSs.size());
        } else {
            mapReqDeviceNumberVO4.setNumber(0);
        }

        if (dlmRespDeviceVOListIr != null && dlmRespDeviceVOListIr.size() > 0) {
            mapReqDeviceNumberVO5.setNumber(dlmRespDeviceVOListIr.size());
            List<DlmRespDevicePublicParVO> collect = dlmRespDeviceVOListIr.stream().filter(a -> a.getNetworkState().equals(1)).collect(Collectors.toList());
            mapReqDeviceNumberVO5.setOnlineRate(collect.size() * 100 / dlmRespDeviceVOListIr.size());
        } else {
            mapReqDeviceNumberVO5.setNumber(0);
        }

        if (dlmRespDeviceVOListOcc != null && dlmRespDeviceVOListOcc.size() > 0) {
            mapReqDeviceNumberVO6.setNumber(dlmRespDeviceVOListOcc.size());
            List<DlmRespDevicePublicParVO> collect = dlmRespDeviceVOListOcc.stream().filter(a -> a.getNetworkState().equals(1)).collect(Collectors.toList());
            mapReqDeviceNumberVO6.setOnlineRate(collect.size() * 100 / dlmRespDeviceVOListOcc.size());
        } else {
            mapReqDeviceNumberVO6.setNumber(0);
        }

        if (dlmRespDeviceVOListEm != null && dlmRespDeviceVOListEm.size() > 0) {
            mapReqDeviceNumberVO7.setNumber(dlmRespDeviceVOListEm.size());
            List<DlmRespDevicePublicParVO> collect = dlmRespDeviceVOListEm.stream().filter(a -> a.getNetworkState().equals(1)).collect(Collectors.toList());
            mapReqDeviceNumberVO7.setOnlineRate(collect.size() * 100 / dlmRespDeviceVOListEm.size());
        } else {
            mapReqDeviceNumberVO7.setNumber(0);
        }
        Calendar cale = Calendar.getInstance();
        if (type != 0 && version == 1) {
            String dateTime = "";
            int dayNumber = 0;
            if (type == 1) {
                cale.add(Calendar.WEEK_OF_MONTH, 0);
                cale.set(Calendar.DAY_OF_WEEK, 2);
                dateTime = sdf.format(cale.getTime());
            } else if (type == 2) {
                cale = Calendar.getInstance();
                cale.add(Calendar.MONTH, 0);
                cale.set(Calendar.DAY_OF_MONTH, 1);
                dateTime = sdf.format(cale.getTime());
            } else {
                Result result = new Result();
                return result.error("type不合法");
            }
            // 距离今天的天数
            try {
                Date today = new Date();
                Date compareDate = sdf.parse(dateTime);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(compareDate);
                long time1 = cal2.getTimeInMillis();
                cal2.setTime(today);
                long time2 = cal2.getTimeInMillis();
                long between_days = (time2 - time1) / (1000 * 3600 * 24);
                dayNumber = Integer.parseInt(String.valueOf(between_days));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dayNumber > 0) {
                List<DeviceTypeGroupVO> vos = deviceOnlineService.typeGroup(dateTime, lampPostIdList);
                for (DeviceTypeGroupVO vo : vos) {
                    switch (vo.getType()) {
                        case 1:
                            if (mapReqDeviceNumberVO1.getNumber() == 0) {
                                mapReqDeviceNumberVO1.setOnlineRate(0);
                            } else {
                                mapReqDeviceNumberVO1.setOnlineRate(vo.getCount() * 100 / (dlmRespDeviceVOListSl.size() * dayNumber));
                            }
                            break;
                        case 2:
                            if (mapReqDeviceNumberVO2.getNumber() == 0) {
                                mapReqDeviceNumberVO2.setOnlineRate(0);
                            } else {
                                mapReqDeviceNumberVO2.setOnlineRate(vo.getCount() * 100 / (dlmRespDeviceVOListWifi.size() * dayNumber));
                            }
                            break;
                        case 3:
                            if (mapReqDeviceNumberVO3.getNumber() == 0) {
                                mapReqDeviceNumberVO3.setOnlineRate(0);
                            } else {
                                mapReqDeviceNumberVO3.setOnlineRate(vo.getCount() * 100 / (dlmRespDeviceVOListPb.size() * dayNumber));
                            }
                            break;
                        case 4:
                            if (mapReqDeviceNumberVO4.getNumber() == 0) {
                                mapReqDeviceNumberVO4.setOnlineRate(0);
                            } else {
                                mapReqDeviceNumberVO4.setOnlineRate(vo.getCount() * 100 / (dlmRespDeviceVOListSs.size() * dayNumber));
                            }
                            break;
                        case 5:
                            if (mapReqDeviceNumberVO5.getNumber() == 0) {
                                mapReqDeviceNumberVO5.setOnlineRate(0);
                            } else {
                                mapReqDeviceNumberVO5.setOnlineRate(vo.getCount() * 100 / (dlmRespDeviceVOListIr.size() * dayNumber));
                            }
                            break;
                        case 6:
                            if (mapReqDeviceNumberVO6.getNumber() == 0) {
                                mapReqDeviceNumberVO6.setOnlineRate(0);
                            } else {
                                mapReqDeviceNumberVO6.setOnlineRate(vo.getCount() * 100 / (dlmRespDeviceVOListOcc.size() * dayNumber));
                            }
                            break;
                        case 7:
                            if (mapReqDeviceNumberVO7.getNumber() == 0) {
                                mapReqDeviceNumberVO7.setOnlineRate(0);
                            } else {
                                mapReqDeviceNumberVO7.setOnlineRate(vo.getCount() * 100 / (dlmRespDeviceVOListEm.size() * dayNumber));
                            }
                            break;
                    }
                }
            }
        }
        // 添加至返回对象
        mapReqDeviceNumberVOList.add(mapReqDeviceNumberVO1);
        mapReqDeviceNumberVOList.add(mapReqDeviceNumberVO2);
        mapReqDeviceNumberVOList.add(mapReqDeviceNumberVO3);
        mapReqDeviceNumberVOList.add(mapReqDeviceNumberVO4);
        mapReqDeviceNumberVOList.add(mapReqDeviceNumberVO5);
        mapReqDeviceNumberVOList.add(mapReqDeviceNumberVO6);
        mapReqDeviceNumberVOList.add(mapReqDeviceNumberVO7);
        mapReqDeviceNumberVOList.sort(Comparator.comparing(MapReqDeviceNumberVO::getOnlineRate).reversed());
        if (version == 2) {
            int count = 0;
            if (flag) {
                count = this.count();
            } else {
                count = lampPostIdList.size(); //灯杆总数
            }
            Map<String, Object> mapResult = new HashMap<>();
            mapResult.put("count", count);
            mapResult.put("device", mapReqDeviceNumberVOList);
            Result result = new Result();
            return result.success(message, mapResult);
        }
        Result result = new Result();
        return result.success(message, mapReqDeviceNumberVOList);
    }


    /**
     * 查询灯杆名称
     *
     * @return
     */
    @Override
    public List<String> getSlLampPostName() {
        logger.info("查询灯杆名称");
        return slLampPostMapper.getSlLampPostName();
    }

    /**
     * 查询所有灯杆信息
     *
     * @return
     */
    @Override
    public List<SlLampPost> getList() {
        logger.info("查询所有灯杆信息");
        return slLampPostMapper.selectList(null);
    }

    @Override
    public Result delete(Long id, HttpServletRequest request) {
        logger.info("根据id删除灯杆，接收参数：{}", id);
        this.removeById(id);
        // 取消设备关联灯杆
        List<Integer> lampPostIdList = new ArrayList<>();
        lampPostIdList.add(id.intValue());
        this.cancelDevice(lampPostIdList, request);
        Result result = new Result();
        return result.success("删除成功");
    }

    @Override
    public Result cancel(List<Integer> siteIdList, List<Integer> groupIdList, HttpServletRequest request) {
        logger.info("取消灯杆关联站点，接收参数：{}", siteIdList);
        LambdaUpdateWrapper<SlLampPost> updateWrapper = new LambdaUpdateWrapper<>();
        if (siteIdList != null && siteIdList.size() != 0) {
            if (siteIdList != null && siteIdList.size() > 0) {
                updateWrapper.set(SlLampPost::getSiteId, null).in(SlLampPost::getSiteId, siteIdList);
                this.update(updateWrapper);
            }
        }
        if (groupIdList != null && groupIdList.size() != 0) {
            if (groupIdList != null && groupIdList.size() > 0) {
                updateWrapper.set(SlLampPost::getGroupId, null).in(SlLampPost::getGroupId, groupIdList);
                this.update(updateWrapper);
            }
        }
        Result result = new Result();
        return result.success("取消灯杆关联站点");
    }

    @Override
    public Result cancelDevice(List<Integer> lampPostIdList, HttpServletRequest request) {
        logger.info("取消设备关联灯杆，接收参数：{}", lampPostIdList);
        if (lampPostIdList != null && lampPostIdList.size() > 0) {
            this.baseMapper.cancelDeviceSl(lampPostIdList);
            this.baseMapper.cancelDeviceWifi(lampPostIdList);
            this.baseMapper.cancelDevicePb(lampPostIdList);
            this.baseMapper.cancelDeviceSs(lampPostIdList);
            this.baseMapper.cancelDeviceIr(lampPostIdList);
            this.baseMapper.cancelDeviceOcc(lampPostIdList);
            this.baseMapper.cancelDeviceEm(lampPostIdList);
        }
        Result result = new Result();
        return result.success("已取消设备关联灯杆");
    }

    @Override
    public Result unique(SlLampPost slLampPost, HttpServletRequest request) {
        logger.info("灯杆验证唯一性，接收参数：{}", slLampPost);
        Result result = new Result();
        if (null != slLampPost) {
            if (slLampPost.getId() != null) {
                if (slLampPost.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<SlLampPost> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(SlLampPost::getName, slLampPost.getName());
                    SlLampPost slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null && !slLampPostByName.getId().equals(slLampPost.getId())) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
                if (slLampPost.getNum() != null) {
                    // 验证编号是否重复
                    LambdaQueryWrapper<SlLampPost> wrapperNum = new LambdaQueryWrapper();
                    wrapperNum.eq(SlLampPost::getNum, slLampPost.getNum());
                    SlLampPost slLampPostByNum = this.getOne(wrapperNum);
                    if (slLampPostByNum != null && !slLampPostByNum.getId().equals(slLampPost.getId())) {
                        return result.error("编号已存在");
                    }
                } else {
                    return result.error("编号不能为空");
                }
            } else {
                if (slLampPost.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<SlLampPost> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(SlLampPost::getName, slLampPost.getName());
                    SlLampPost slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
                if (slLampPost.getNum() != null) {
                    // 验证编号是否重复
                    LambdaQueryWrapper<SlLampPost> wrapperNum = new LambdaQueryWrapper();
                    wrapperNum.eq(SlLampPost::getNum, slLampPost.getNum());
                    SlLampPost slLampPostByNum = this.getOne(wrapperNum);
                    if (slLampPostByNum != null) {
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
    public Result updateLampPost(SlLampPost slLampPost, HttpServletRequest request) {
        logger.info("修改灯杆，接收参数：{}", slLampPost);
        Result result = new Result();
        Result unique = this.unique(slLampPost, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            boolean rsg = this.updateById(slLampPost);
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
    public Result getLampPostById(Integer id, HttpServletRequest request) {
        Result<SlLampPost> result = new Result<>();
        SlLampPost slLampPost = slLampPostMapper.selectById(id);
        result.success(slLampPost);
        return result;
    }

    @Override
    public Result getCamera(DlmLampPostQuery dlmLampPostQuery, HttpServletRequest request) {
        return null;
    }


}