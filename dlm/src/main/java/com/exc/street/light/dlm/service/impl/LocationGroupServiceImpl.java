/**
 * @filename:GroupServiceImpl 2020-03-18
 * @project dlm  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
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
import com.exc.street.light.dlm.config.parameter.*;
import com.exc.street.light.dlm.mapper.LampGroupSingleMapper;
import com.exc.street.light.dlm.mapper.LocationGroupMapper;
import com.exc.street.light.dlm.mapper.SystemDeviceMapper;
import com.exc.street.light.dlm.service.LocationGroupService;
import com.exc.street.light.dlm.service.SlLampPostService;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.LocationGroup;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.entity.occ.AhDevice;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.entity.sl.*;
import com.exc.street.light.resource.entity.ss.SsDevice;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.wifi.WifiApDevice;
import com.exc.street.light.resource.qo.DlmGroupQuery;
import com.exc.street.light.resource.utils.BaseConstantUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.DlmReqGroupVO;
import com.exc.street.light.resource.vo.req.SlReqLampGroupVO;
import com.exc.street.light.resource.vo.resp.*;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class LocationGroupServiceImpl extends ServiceImpl<LocationGroupMapper, LocationGroup> implements LocationGroupService {
    private static final Logger logger = LoggerFactory.getLogger(LocationGroupServiceImpl.class);

    @Autowired
    private SlLampPostService slLampPostService;
    @Autowired
    private LocationGroupService locationGroupService;
    @Autowired
    private SystemDeviceMapper systemDeviceMapper;
    @Autowired
    private LampGroupSingleMapper lampGroupSingleMapper;
    @Autowired
    private LogUserService logUserService;
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

    @Override
    public Result pulldown(String groupName, HttpServletRequest request) {
        logger.info("分组下拉列表，接收参数：groupName=" + groupName);
        LambdaQueryWrapper<LocationGroup> wrapper = new LambdaQueryWrapper();
        if (groupName != null) {
            wrapper.like(LocationGroup::getName, groupName);
        }
        List<LocationGroup> list = this.list(wrapper);
        Result result = new Result();
        return result.success(list);
    }

    @Override
    public Result add(DlmReqGroupVO dlmReqGroupVO, HttpServletRequest request) {
        logger.info("添加分组，接收参数：{}", dlmReqGroupVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result result = new Result();
        Result unique = this.unique(dlmReqGroupVO, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            // 添加分组
            LocationGroup locationGroup = new LocationGroup();
            BeanUtils.copyProperties(dlmReqGroupVO, locationGroup);
            locationGroup.setCreateTime(new Date());
            locationGroup.setCreator(userId);
            // 修改路灯的分组id关联
            List<Integer> lampPostIdList = dlmReqGroupVO.getLampPostIdList();
            if (dlmReqGroupVO.getTypeId() == 1) {
                // 灯杆分组
                locationGroup.setTypeId(1);
                this.save(locationGroup);
                // 用于确定是否有此灯杆，不存在的灯杆id不添加
                if (lampPostIdList != null && lampPostIdList.size() > 0) {
                    LambdaQueryWrapper<SlLampPost> wrapper = new LambdaQueryWrapper();
                    wrapper.in(SlLampPost::getId, lampPostIdList);
                    List<SlLampPost> SlLampPostList = slLampPostService.list(wrapper);
                    if (SlLampPostList != null && SlLampPostList.size() > 0) {
                        for (SlLampPost slLampPost : SlLampPostList) {
                            slLampPost.setGroupId(locationGroup.getId());
                        }
                        slLampPostService.updateBatchById(SlLampPostList);
                    }
                }
            } else if (dlmReqGroupVO.getTypeId() == 2) {
                // 灯具分组
                locationGroup.setTypeId(2);
                this.save(locationGroup);
                // 用于确定是否有此灯具，不存在的灯具id不添加
                if (lampPostIdList != null && lampPostIdList.size() > 0) {
                    LambdaQueryWrapper<SystemDevice> wrapper = new LambdaQueryWrapper();
                    wrapper.in(SystemDevice::getId, lampPostIdList);
                    List<SystemDevice> systemDeviceList = systemDeviceMapper.selectList(wrapper);
                    if (systemDeviceList != null && systemDeviceList.size() > 0) {
                        for (SystemDevice systemDevice : systemDeviceList) {
                            LampGroupSingle lampGroupSingle = new LampGroupSingle();
                            lampGroupSingle.setSingleId(systemDevice.getId());
                            lampGroupSingle.setLampGroupId(locationGroup.getId());
                            lampGroupSingleMapper.insert(lampGroupSingle);
                        }
                    }
                }
            } else {
                return result.error("不存在的分组类型");
            }
            result.success("添加分组成功");
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result getPage(DlmGroupQuery dlmGroupQuery, HttpServletRequest request) {
        logger.info("分组分页条件查询,接收参数:{}", dlmGroupQuery);
        // 查询灯杆分页信息
        IPage<DlmRespGroupVO> iPage = new Page<DlmRespGroupVO>(dlmGroupQuery.getPageNum(), dlmGroupQuery.getPageSize());
        if (dlmGroupQuery.getStartTime() != null && dlmGroupQuery.getStartTime() != "") {
            dlmGroupQuery.setStartTime(dlmGroupQuery.getStartTime() + " 00:00:00");
        }
        if (dlmGroupQuery.getEndTime() != null && dlmGroupQuery.getEndTime() != "") {
            dlmGroupQuery.setEndTime(dlmGroupQuery.getEndTime() + " 23:59:59");
        }
        List<DlmRespGroupVO> dlmRespGroupVOList = this.baseMapper.query(iPage, dlmGroupQuery);
        for (DlmRespGroupVO dlmRespGroupVO : dlmRespGroupVOList) {
            if (dlmRespGroupVO.getTypeId() == 1) {
                List<Integer> groupIdList = new ArrayList<>();
                groupIdList.add(dlmRespGroupVO.getGroupId());
                Result byGroup = slLampPostService.getByGroup(groupIdList, request);
                List<SlLampPost> slLampPostList = (List<SlLampPost>) byGroup.getData();
                if (slLampPostList != null) {
                    dlmRespGroupVO.setLampPostNumber(slLampPostList.size());
                }
            } else if (dlmRespGroupVO.getTypeId() == 2) {
                LambdaQueryWrapper<LampGroupSingle> wrapper = new LambdaQueryWrapper();
                wrapper.in(LampGroupSingle::getLampGroupId, dlmRespGroupVO.getGroupId());
                List<LampGroupSingle> lampGroupSingleList = lampGroupSingleMapper.selectList(wrapper);
                if (lampGroupSingleList != null) {
                    dlmRespGroupVO.setLampPostNumber(lampGroupSingleList.size());
                }
            }
        }
        iPage.setRecords(dlmRespGroupVOList);
        Result result = new Result();
        return result.success(iPage);
    }

    @Override
    public Result get(Integer groupId, HttpServletRequest request) {
        logger.info("获取分组详情,接收参数:{}", groupId);
        DlmReqGroupVO dlmReqGroupVO = new DlmReqGroupVO();
        LocationGroup byId = this.getById(groupId);
        BeanUtils.copyProperties(byId, dlmReqGroupVO);
        List<Integer> lampPostIdList = new ArrayList<>();
        List<String> lampPostNameList = new ArrayList<>();
        if (byId.getTypeId() == 1) {
            List<Integer> groupIdList = new ArrayList<>();
            groupIdList.add(byId.getId());
            if (groupIdList != null && groupIdList.size() > 0) {
                Result byGroup = slLampPostService.getByGroup(groupIdList, request);
                List<SlLampPost> slLampPostList = (List<SlLampPost>) byGroup.getData();
                if (slLampPostList != null && slLampPostList.size() > 0) {
                    lampPostIdList = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
                    lampPostNameList = slLampPostList.stream().map(SlLampPost::getName).collect(Collectors.toList());
                }
            }
        } else if (byId.getTypeId() == 2) {
            LambdaQueryWrapper<LampGroupSingle> wrapper = new LambdaQueryWrapper();
            wrapper.in(LampGroupSingle::getLampGroupId, groupId);
            List<LampGroupSingle> lampGroupSingleList = lampGroupSingleMapper.selectList(wrapper);
            if (lampGroupSingleList != null && lampGroupSingleList.size() > 0) {
                List<Integer> singleIdList = lampGroupSingleList.stream().map(LampGroupSingle::getSingleId).distinct().collect(Collectors.toList());
                if (singleIdList != null && singleIdList.size() > 0) {
                    List<SystemDevice> systemDeviceList = systemDeviceMapper.selectBatchIds(singleIdList);
                    if (systemDeviceList != null && systemDeviceList.size() > 0) {
                        lampPostIdList = systemDeviceList.stream().map(SystemDevice::getId).collect(Collectors.toList());
                        lampPostNameList = systemDeviceList.stream().map(SystemDevice::getName).collect(Collectors.toList());
                    }
                }
            }

        }
        dlmReqGroupVO.setLampPostIdList(lampPostIdList);
        dlmReqGroupVO.setLampPostNameList(lampPostNameList);
        Result result = new Result();
        return result.success(dlmReqGroupVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateGroup(DlmReqGroupVO dlmReqGroupVO, HttpServletRequest request) {
        logger.info("修改分组,接收参数:{}", dlmReqGroupVO);
        Result result = new Result();
        Result unique = this.unique(dlmReqGroupVO, request);
        if (unique.getCode() == Const.CODE_SUCCESS) {
            // 修改分组信息
            LocationGroup locationGroup = new LocationGroup();
            BeanUtils.copyProperties(dlmReqGroupVO, locationGroup);
            this.updateById(locationGroup);
            List<Integer> lampPostIdList = dlmReqGroupVO.getLampPostIdList();
            if (dlmReqGroupVO.getTypeId() == 1) {
                // 取消之前的分组关联灯杆
                slLampPostService.updateToNoGroup(dlmReqGroupVO.getId());
                // 修改路灯的分组id关联
                LambdaQueryWrapper<SlLampPost> wrapper = new LambdaQueryWrapper();
                wrapper.in(SlLampPost::getId, lampPostIdList);
                List<SlLampPost> SlLampPostList = slLampPostService.list(wrapper);
                for (SlLampPost slLampPost : SlLampPostList) {
                    slLampPost.setGroupId(locationGroup.getId());
                }
                boolean rsg = slLampPostService.updateBatchById(SlLampPostList);
                if (rsg) {
                    result.success("修改分组成功");
                } else {
                    result.error("修改分组失败！");
                }
            } else if (dlmReqGroupVO.getTypeId() == 2) {
                // 删除之前的关联关系
                LambdaQueryWrapper<LampGroupSingle> deleteWrapper = new LambdaQueryWrapper<>();
                deleteWrapper.eq(LampGroupSingle::getLampGroupId, dlmReqGroupVO.getId());
                lampGroupSingleMapper.delete(deleteWrapper);
                // 用于确定是否有此灯具，不存在的灯具id不添加
                LambdaQueryWrapper<SystemDevice> wrapper = new LambdaQueryWrapper();
                wrapper.in(SystemDevice::getId, lampPostIdList);
                List<SystemDevice> systemDeviceList = systemDeviceMapper.selectList(wrapper);
                if (systemDeviceList != null && systemDeviceList.size() > 0) {
                    for (SystemDevice systemDevice : systemDeviceList) {
                        LampGroupSingle lampGroupSingle = new LampGroupSingle();
                        lampGroupSingle.setSingleId(systemDevice.getId());
                        lampGroupSingle.setLampGroupId(locationGroup.getId());
                        lampGroupSingleMapper.insert(lampGroupSingle);
                    }
                }
            } else {
                result.error("不存在的分组类型");
            }
        } else {
            result = unique;
        }
        return result;
    }

    @Override
    public Result getList(Integer hierarchy, Integer deviceTypeNum, Integer typeId, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        logger.info("查询分组详细列表，接收参数：deviceNum=" + deviceTypeNum);
        Result result = new Result();
        String message = "";
        if (deviceTypeNum == null || deviceTypeNum < 1 || deviceTypeNum > 7) {
            deviceTypeNum = null;
        }
        if (hierarchy == null) {
            hierarchy = 1;
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
        // 获取当前分区id
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        Integer areaId = null;
        if (!logUserService.isAdmin(user.getId())) {
            areaId = user.getAreaId();
        }
        // 当前分区下所有用户id
        LambdaQueryWrapper<User> lambdaQueryWrapperUser = new LambdaQueryWrapper<>();
        lambdaQueryWrapperUser.eq(User::getAreaId,areaId);
        List<User> userList = logUserService.list(lambdaQueryWrapperUser);
        List<Integer> userIdList = userList.stream().map(User::getId).distinct().collect(Collectors.toList());
        // 当前接口只能用于灯杆类型的分组查询
        if (typeId == null) {
            typeId = 1;
        }
        if (typeId == 2) {
            logger.info("获取灯具分组列表");
            // 分区下所有灯具
            List<SystemDevice> systemDeviceList = systemDeviceMapper.selectListByAreaId(areaId);
            // 分区下所有灯具id
            List<Integer> systemDeviceIdList = systemDeviceList.stream().map(SystemDevice::getId).distinct().collect(Collectors.toList());
            // 分区下所有分组集合
            LambdaQueryWrapper<LampGroupSingle> lampGroupSingleQueryWrapper = new LambdaQueryWrapper<>();
            lampGroupSingleQueryWrapper.in(LampGroupSingle::getSingleId, systemDeviceIdList);
            List<LampGroupSingle> lampGroupSingleList = lampGroupSingleMapper.selectList(lampGroupSingleQueryWrapper);
            // 得到所有分组id
            List<Integer> lampGroupIdList = lampGroupSingleList.stream().map(LampGroupSingle::getLampGroupId).distinct().collect(Collectors.toList());
            // 得到所有分组
            List<SlRespLampGroupSingleVO> slRespLampGroupSingleVOList = new ArrayList<>();
            if (lampGroupIdList != null && lampGroupIdList.size() > 0) {
                // 分组集合
                Collection<LocationGroup> lampGroupList = this.listByIds(lampGroupIdList);
                for (LocationGroup lampGroup : lampGroupList) {
                    // 当前分组下所有关系集合
                    List<LampGroupSingle> lampGroupSingles = lampGroupSingleList.stream().filter(a -> lampGroup.getId().equals(a.getLampGroupId())).collect(Collectors.toList());
                    // 当前分组下所有灯具id集合
                    List<Integer> lampGroupSingleIdList = lampGroupSingles.stream().map(LampGroupSingle::getSingleId).distinct().collect(Collectors.toList());
                    // 构造分组返回对象
                    SlRespLampGroupSingleVO slRespLampGroupSingleVO = new SlRespLampGroupSingleVO();
                    slRespLampGroupSingleVO.setId(lampGroup.getId());
                    slRespLampGroupSingleVO.setName(lampGroup.getName());
                    slRespLampGroupSingleVO.setPartId("lampGroup" + lampGroup.getId());
                    // 构造分组下灯具返回对象
                    List<SlRespSingleVO> singleList = new ArrayList<>();
                    LambdaQueryWrapper<SystemDevice> systemDeviceLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    systemDeviceLambdaQueryWrapper.in(SystemDevice::getId, lampGroupSingleIdList);
                    List<SystemDevice> systemDeviceList1 = systemDeviceMapper.selectList(systemDeviceLambdaQueryWrapper);
                    for (SystemDevice systemDevice : systemDeviceList1) {
                        SlRespSingleVO slRespSingleVO = new SlRespSingleVO();
                        slRespSingleVO.setId(systemDevice.getId());
                        String lampPosition = systemDeviceMapper.selectPosition(systemDevice.getId());
                        slRespSingleVO.setName(systemDevice.getName() + "<" + lampPosition + ">");
                        slRespSingleVO.setPartId("single" + systemDevice.getId());
                        singleList.add(slRespSingleVO);
                    }
                    slRespLampGroupSingleVO.setSingleList(singleList);
                    slRespLampGroupSingleVOList.add(slRespLampGroupSingleVO);
                }
            }
            Result<List<SlRespLampGroupSingleVO>> result1 = new Result<>();
            return result1.success(slRespLampGroupSingleVOList);
        }
        // 构造返回对象
        List<DlmRespLocationGroupVO> dlmRespLocationGroupVOList = new ArrayList<>();
        Page<DlmRespLocationGroupVO> resultPageData = new Page<DlmRespLocationGroupVO>(pageNum, pageSize);
        List<LocationGroup> locationGroupList = new ArrayList<>();
        if (pageSize == 0) {
            LambdaQueryWrapper<LocationGroup> queryWrapper = new LambdaQueryWrapper<LocationGroup>();
            queryWrapper.orderByDesc(LocationGroup::getId);
            queryWrapper.eq(LocationGroup::getTypeId, typeId);
            if(userIdList != null && userIdList.size() != 0){
                queryWrapper.in(LocationGroup::getCreator, userIdList);
            }
            locationGroupList = this.list(queryWrapper);
        } else {
            // 获取分组集合
            Page<LocationGroup> page = new Page<LocationGroup>(pageNum, pageSize);
            LambdaQueryWrapper<LocationGroup> queryWrapper = new LambdaQueryWrapper<LocationGroup>();
            queryWrapper.orderByDesc(LocationGroup::getId);
            queryWrapper.eq(LocationGroup::getTypeId, typeId);
            if(userIdList != null && userIdList.size() != 0) {
                queryWrapper.in(LocationGroup::getCreator, userIdList);
            }
            //分页数据
            IPage<LocationGroup> pageData = this.page(page, queryWrapper);
            locationGroupList = pageData.getRecords();
            resultPageData.setTotal(pageData.getTotal());
            resultPageData.setSize(pageData.getSize());
            resultPageData.setCurrent(pageData.getCurrent());
            resultPageData.setPages(pageData.getPages());
        }
        // 获取灯杆集合
        LambdaQueryWrapper<SlLampPost> slLampPostQueryWrapper = new LambdaQueryWrapper<SlLampPost>();
        slLampPostQueryWrapper.orderByDesc(SlLampPost::getId);
        List<SlLampPost> slLampPostList = slLampPostService.list(slLampPostQueryWrapper);
        Map<String, String> map = new HashMap<>();
        map.put("token", request.getHeader("token"));
        // 获取各个设备集合
        // 智慧照明
        List<SlRespSystemDeviceVO> lampDeviceList = null;
        if (deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_SL == deviceTypeNum) {
            try {
                JSONObject slDeviceResult = JSON.parseObject(HttpUtil.get(httpSlApi.getUrl() + httpSlApi.getPulldownByLampPostIdList(), map));
                JSONArray slDeviceResultArr = slDeviceResult.getJSONArray("data");
                lampDeviceList = JSON.parseObject(slDeviceResultArr.toJSONString(), new TypeReference<List<SlRespSystemDeviceVO>>() {
                });
            } catch (Exception e) {
                System.out.println("智慧照明接口调用失败，返回为空或连接超时！");
                message += "智慧照明接口调用失败，返回为空或连接超时！\n";
            }
        }
        // 公共WIFI
        List<WifiApDevice> wifiDeviceList = null;
        if (deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_WIFI == deviceTypeNum) {
            try {
                JSONObject wifiDeviceResult = JSON.parseObject(HttpUtil.get(httpWifiApi.getUrl() + httpWifiApi.getPulldownByLampPostIdList(), map));
                JSONArray wifiDeviceResultArr = wifiDeviceResult.getJSONArray("data");
                wifiDeviceList = JSON.parseObject(wifiDeviceResultArr.toJSONString(), new TypeReference<List<WifiApDevice>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("公共WIFI接口调用失败，返回为空或连接超时！");
                message += "公共WIFI接口调用失败，返回为空或连接超时！\n";
            }
        }
        // 公共广播
        List<RadioDevice> pbDeviceList = null;
        if (deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_PB == deviceTypeNum) {
            try {
                JSONObject pbDeviceResult = JSON.parseObject(HttpUtil.get(httpPbApi.getUrl() + httpPbApi.getPulldownByLampPostIdList(), map));
                JSONArray pbDeviceResultArr = pbDeviceResult.getJSONArray("data");
                pbDeviceList = JSON.parseObject(pbDeviceResultArr.toJSONString(), new TypeReference<List<RadioDevice>>() {
                });
            } catch (Exception e) {
                System.out.println("公共广播接口调用失败，返回为空或连接超时！");
                message += "公共广播接口调用失败，返回为空或连接超时！\n";
            }
        }
        // 智能安防
        List<SsDevice> ssDeviceList = null;
        if (deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_SS == deviceTypeNum) {
            try {
                JSONObject ssDeviceResult = JSON.parseObject(HttpUtil.get(httpSsApi.getUrl() + httpSsApi.getPulldownByLampPostIdList(), map));
                JSONArray ssDeviceResultArr = ssDeviceResult.getJSONArray("data");
                ssDeviceList = JSON.parseObject(ssDeviceResultArr.toJSONString(), new TypeReference<List<SsDevice>>() {
                });
            } catch (Exception e) {
                System.out.println("智能安防接口调用失败，返回为空或连接超时！");
                message += "智能安防接口调用失败，返回为空或连接超时！\n";
            }
        }
        // 信息发布
        List<ScreenDevice> screenDeviceList = null;
        if (deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_IR == deviceTypeNum) {
            try {
                JSONObject irDeviceResult = JSON.parseObject(HttpUtil.get(httpIrApi.getUrl() + httpIrApi.getPulldownByLampPostIdList(), map));
                JSONArray irDeviceResultArr = irDeviceResult.getJSONArray("data");
                screenDeviceList = JSON.parseObject(irDeviceResultArr.toJSONString(), new TypeReference<List<ScreenDevice>>() {
                });
            } catch (Exception e) {
                System.out.println("信息发布接口调用失败，返回为空！");
                message += "信息发布接口调用失败，返回为空！\n";
            }
        }
        // 一键呼叫
        List<AhDevice> ahDeviceList = null;
        if (deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_OCC == deviceTypeNum) {
            try {
                JSONObject occDeviceResult = JSON.parseObject(HttpUtil.get(httpOccApi.getUrl() + httpOccApi.getPulldownByLampPostIdList(), map));
                JSONArray ahDeviceResultArr = occDeviceResult.getJSONArray("data");
                ahDeviceList = JSON.parseObject(ahDeviceResultArr.toJSONString(), new TypeReference<List<AhDevice>>() {
                });
            } catch (Exception e) {
                System.out.println("一键呼叫接口调用失败，返回为空！");
                message += "一键呼叫接口调用失败，返回为空！\n";
            }
        }
        // 环境监测
        List<MeteorologicalDevice> emDeviceList = null;
        if (deviceTypeNum != null && BaseConstantUtil.DEVICE_TYPE_EM == deviceTypeNum) {
            try {
                JSONObject emDeviceResult = JSON.parseObject(HttpUtil.get(httpEmApi.getUrl() + httpEmApi.getPulldownByLampPostIdList(), map));
                JSONArray emDeviceResultArr = emDeviceResult.getJSONArray("data");
                emDeviceList = JSON.parseObject(emDeviceResultArr.toJSONString(), new TypeReference<List<MeteorologicalDevice>>() {
                });
            } catch (Exception e) {
                System.out.println("环境监测接口调用失败，返回为空！");
                message += "环境监测接口调用失败，返回为空！\n";
            }
        }
        // 遍历区域集合，添加子集
        for (LocationGroup locationGroup : locationGroupList) {
            DlmRespLocationGroupVO dlmRespLocationGroupVO = new DlmRespLocationGroupVO();
            dlmRespLocationGroupVO.setCreateTime(locationGroup.getCreateTime());
            dlmRespLocationGroupVO.setCreator(locationGroup.getCreator());
            dlmRespLocationGroupVO.setDescription(locationGroup.getDescription());
            dlmRespLocationGroupVO.setId(locationGroup.getId());
            dlmRespLocationGroupVO.setPartId("group" + locationGroup.getId());
            dlmRespLocationGroupVO.setName(locationGroup.getName());
            // 获取当前循环分组下的灯杆集合
            List<SlLampPost> slLampPostCollect = slLampPostList.stream()
                    .filter(a -> locationGroup.getId().equals(a.getGroupId())).collect(Collectors.toList());
            // 分组下设备数量
            Integer groupDeviceNumber = 0;
            // 分组下灯杆集合
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
                    if (BaseConstantUtil.DEVICE_TYPE_SL == deviceTypeNum && lampDeviceList != null && lampDeviceList.size() > 0) {
                        List<SlRespSystemDeviceVO> lampDeviceCollect = lampDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += lampDeviceCollect.size();
                        lampPostDeviceNumber += lampDeviceCollect.size();
                        deviceList = lampDeviceCollect;
                    }
                    // 公共WIFI
                    if (BaseConstantUtil.DEVICE_TYPE_WIFI == deviceTypeNum && wifiDeviceList != null && wifiDeviceList.size() > 0) {
                        List<WifiApDevice> wifiDeviceCollect = wifiDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += wifiDeviceCollect.size();
                        lampPostDeviceNumber += wifiDeviceCollect.size();
                        deviceList = wifiDeviceCollect;
                    }
                    // 公共广播
                    if (BaseConstantUtil.DEVICE_TYPE_PB == deviceTypeNum && pbDeviceList != null && pbDeviceList.size() > 0) {
                        List<RadioDevice> pbDeviceCollect = pbDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += pbDeviceCollect.size();
                        lampPostDeviceNumber += pbDeviceCollect.size();
                        deviceList = pbDeviceCollect;
                    }
                    // 智能安防
                    if (BaseConstantUtil.DEVICE_TYPE_SS == deviceTypeNum && ssDeviceList != null && ssDeviceList.size() > 0) {
                        List<SsDevice> ssDeviceCollect = ssDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += ssDeviceCollect.size();
                        lampPostDeviceNumber += ssDeviceCollect.size();
                        deviceList = ssDeviceCollect;
                    }
                    // 信息发布
                    if (BaseConstantUtil.DEVICE_TYPE_IR == deviceTypeNum && screenDeviceList != null && screenDeviceList.size() > 0) {
                        List<ScreenDevice> screenDeviceCollect = screenDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += screenDeviceCollect.size();
                        lampPostDeviceNumber += screenDeviceCollect.size();
                        deviceList = screenDeviceCollect;
                    }
                    // 一键呼叫
                    if (BaseConstantUtil.DEVICE_TYPE_OCC == deviceTypeNum && ahDeviceList != null && ahDeviceList.size() > 0) {
                        List<AhDevice> ahDeviceCollect = ahDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += ahDeviceCollect.size();
                        lampPostDeviceNumber += ahDeviceCollect.size();
                        deviceList = ahDeviceCollect;
                    }
                    // 环境监测
                    if (BaseConstantUtil.DEVICE_TYPE_EM == deviceTypeNum && emDeviceList != null && emDeviceList.size() > 0) {
                        List<MeteorologicalDevice> emDeviceCollect = emDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += emDeviceCollect.size();
                        lampPostDeviceNumber += emDeviceCollect.size();
                        deviceList = emDeviceCollect;
                    }
                } else {
                    // 智慧照明
                    if (lampDeviceList != null && lampDeviceList.size() > 0) {
                        List<SlRespSystemDeviceVO> lampDeviceCollect = lampDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += lampDeviceCollect.size();
                        lampPostDeviceNumber += lampDeviceCollect.size();
                    }
                    // 公共WIFI
                    if (wifiDeviceList != null && wifiDeviceList.size() > 0) {
                        List<WifiApDevice> wifiDeviceCollect = wifiDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += wifiDeviceCollect.size();
                        lampPostDeviceNumber += wifiDeviceCollect.size();
                    }
                    // 公共广播
                    if (pbDeviceList != null && pbDeviceList.size() > 0) {
                        List<RadioDevice> pbDeviceCollect = pbDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += pbDeviceCollect.size();
                        lampPostDeviceNumber += pbDeviceCollect.size();
                    }
                    // 智能安防
                    if (ssDeviceList != null && ssDeviceList.size() > 0) {
                        List<SsDevice> ssDeviceCollect = ssDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += ssDeviceCollect.size();
                        lampPostDeviceNumber += ssDeviceCollect.size();
                    }
                    // 信息发布
                    if (screenDeviceList != null && screenDeviceList.size() > 0) {
                        List<ScreenDevice> screenDeviceCollect = screenDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += screenDeviceCollect.size();
                        lampPostDeviceNumber += screenDeviceCollect.size();
                    }
                    // 一键呼叫
                    if (ahDeviceList != null && ahDeviceList.size() > 0) {
                        List<AhDevice> ahDeviceCollect = ahDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += ahDeviceCollect.size();
                        lampPostDeviceNumber += ahDeviceCollect.size();
                    }
                    // 环境监测
                    if (emDeviceList != null && emDeviceList.size() > 0) {
                        List<MeteorologicalDevice> emDeviceCollect = emDeviceList.stream()
                                .filter(a -> slLampPost.getId().equals(a.getLampPostId())).collect(Collectors.toList());
                        groupDeviceNumber += emDeviceCollect.size();
                        lampPostDeviceNumber += emDeviceCollect.size();
                    }
                }
                // 是否需要灯杆级数据
                if (hierarchy > 1) {
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
                    dlmRespLocationLampPostVO.setSuperId(dlmRespLocationGroupVO.getId());
                    dlmRespLocationLampPostVO.setSuperName(dlmRespLocationGroupVO.getName());
                    // 是否指定设备
                    if (hierarchy > 2) {
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
            dlmRespLocationGroupVO.setDeviceNumber(groupDeviceNumber);
            if (hierarchy > 1) {
                dlmRespLocationGroupVO.setChildrenList(dlmRespLocationLampPostVOList);
            }
            if (deviceTypeNum == null || dlmRespLocationGroupVO.getDeviceNumber() > 0) {
                dlmRespLocationGroupVOList.add(dlmRespLocationGroupVO);
            }
        }
        // 是否分页，是否返回分页数据数据
        if (pageSize == 0) {
            result.success(dlmRespLocationGroupVOList);
        } else {
            resultPageData.setRecords(dlmRespLocationGroupVOList);
            result.success(resultPageData);
        }
        if (!"".equals(message)) {
            result.setMessage(message);
        }
        return result;
    }

    @Override
    public Result delete(Long id, HttpServletRequest request) {
        logger.info("根据id删除分组，接收参数：{}", id);
        this.removeById(id);
        // 取消灯杆关联站点
        List<Integer> groupIdList = new ArrayList<>();
        groupIdList.add(id.intValue());
        slLampPostService.cancel(null, groupIdList, request);
        Result result = new Result();
        return result.success("删除成功");
    }

    @Override
    public Result unique(DlmReqGroupVO dlmReqGroupVO, HttpServletRequest request) {
        logger.info("站点验证唯一性，接收参数：{}", dlmReqGroupVO);
        LocationGroup locationGroup = new LocationGroup();
        BeanUtils.copyProperties(dlmReqGroupVO, locationGroup);
        Result result = new Result();
        if (null != dlmReqGroupVO) {
            if (dlmReqGroupVO.getId() != null) {
                if (dlmReqGroupVO.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LocationGroup> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LocationGroup::getName, dlmReqGroupVO.getName());
                    LocationGroup slLampPostByName = this.getOne(wrapperName);
                    if (slLampPostByName != null && !slLampPostByName.getId().equals(dlmReqGroupVO.getId())) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
            } else {
                if (dlmReqGroupVO.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<LocationGroup> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(LocationGroup::getName, dlmReqGroupVO.getName());
                    LocationGroup slLampPostByName = this.getOne(wrapperName);
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

}