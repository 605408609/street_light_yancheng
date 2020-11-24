/**
 * @filename:OrderServiceImpl 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.resource.entity.woa.OrderInfo;
import com.exc.street.light.resource.entity.woa.OrderProcess;
import com.exc.street.light.resource.qo.WoaOrderQuery;
import com.exc.street.light.resource.utils.BaseConstantUtil;
import com.exc.street.light.resource.utils.DateUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.WoaReqOrderNewStatusVO;
import com.exc.street.light.resource.vo.req.WoaReqOrderNewVO;
import com.exc.street.light.resource.vo.resp.*;
import com.exc.street.light.woa.config.parameter.HttpUaApi;
import com.exc.street.light.woa.config.parameter.PathApi;
import com.exc.street.light.woa.mapper.OrderInfoMapper;
import com.exc.street.light.woa.service.AlarmService;
import com.exc.street.light.woa.service.OrderInfoService;
import com.exc.street.light.woa.service.OrderPicService;
import com.exc.street.light.woa.service.OrderProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Transactional(rollbackFor = Exception.class)
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    private static final Logger logger = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

    @Autowired
    private AlarmService alarmService;
    @Autowired
    private OrderPicService orderPicService;
    @Autowired
    private OrderProcessService orderProcessService;
    @Autowired
    private PathApi pathApi;
    @Autowired
    private HttpUaApi httpUaApi;
    @Autowired
    private LogUserService logUserService;

    @Override
    public Result summary(HttpServletRequest request, Integer frame) {
        logger.info("首页工单概述数据");
        LambdaQueryWrapper<OrderInfo> beingWrapper = new LambdaQueryWrapper();
        LambdaQueryWrapper<OrderInfo> noWrapper = new LambdaQueryWrapper();
        LambdaQueryWrapper<OrderInfo> overtimeWrapper = new LambdaQueryWrapper();
        LambdaQueryWrapper<OrderInfo> allWrapper = new LambdaQueryWrapper();

        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);

        if (user != null) {
            Integer founderId = user.getFounderId();
            if (founderId == 0) {
                //超管    1，2，3，4，5
                /*beingWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getProcessor, userId).or().eq(OrderInfo::getApproval, userId));
                noWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getProcessor, userId).or().eq(OrderInfo::getApproval, userId));
                overtimeWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getProcessor, userId).or().eq(OrderInfo::getApproval, userId));
                allWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getProcessor, userId).or().eq(OrderInfo::getApproval, userId));*/
            } else if (founderId == 1) {
                //管理员   1，5
                beingWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getApproval, userId));
                noWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getApproval, userId));
                overtimeWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getApproval, userId));
                allWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getApproval, userId));
            } else {
                //普通人员  2，3，4
                beingWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getProcessor, userId));
                noWrapper.and(iteme -> iteme.eq(OrderInfo::getCreator, userId).or().eq(OrderInfo::getProcessor, userId));
                overtimeWrapper.eq(OrderInfo::getCreator, userId).or(iteme -> iteme.eq(OrderInfo::getProcessor, userId));
                allWrapper.eq(OrderInfo::getCreator, userId).or(iteme -> iteme.eq(OrderInfo::getProcessor, userId));
            }
        }
        if (frame != null) {
            if (frame == 1) {
                //今天
                Date dayBeginOrEnd = DateUtil.getDayBeginOrEnd(0, true);
                beingWrapper.gt(OrderInfo::getCreateTime, dayBeginOrEnd);
                noWrapper.gt(OrderInfo::getCreateTime, dayBeginOrEnd);
                overtimeWrapper.gt(OrderInfo::getCreateTime, dayBeginOrEnd);
                allWrapper.gt(OrderInfo::getCreateTime, dayBeginOrEnd);
            } else if (frame == 2) {
                Date weekBeginOrEnd = DateUtil.getWeekBeginOrEnd(0, true);
                beingWrapper.gt(OrderInfo::getCreateTime, weekBeginOrEnd);
                noWrapper.gt(OrderInfo::getCreateTime, weekBeginOrEnd);
                overtimeWrapper.gt(OrderInfo::getCreateTime, weekBeginOrEnd);
                allWrapper.gt(OrderInfo::getCreateTime, weekBeginOrEnd);
            } else if (frame == 3) {
                Date monthTime = DateUtil.getMonthTime(0, true);
                beingWrapper.gt(OrderInfo::getCreateTime, monthTime);
                noWrapper.gt(OrderInfo::getCreateTime, monthTime);
                overtimeWrapper.gt(OrderInfo::getCreateTime, monthTime);
                allWrapper.gt(OrderInfo::getCreateTime, monthTime);
            }
        }
        // 正在处理工单个数
        beingWrapper.eq(OrderInfo::getStatusId, BaseConstantUtil.ORDER_STATUS_UNTREATED).eq(OrderInfo::getOvertime, 0)
                .or().eq(OrderInfo::getStatusId, BaseConstantUtil.ORDER_STATUS_IN_PROCESSING).eq(OrderInfo::getOvertime, 0)
                .or().eq(OrderInfo::getStatusId, BaseConstantUtil.ORDER_STATUS_TO_BE_AUDITED).eq(OrderInfo::getOvertime, 0);
        int beingCount = this.count(beingWrapper);
        // 未处理工单个数
        noWrapper.eq(OrderInfo::getStatusId, BaseConstantUtil.ORDER_STATUS_PENDING_TRIAL).eq(OrderInfo::getOvertime, 0);
        int noCount = this.count(noWrapper);
        // 已超时工单
        overtimeWrapper.eq(OrderInfo::getOvertime, 1);
        int overtimeCount = this.count(overtimeWrapper);
        // 工单总数
        int allCount = this.count(allWrapper);
        // 构建返回对象
        WoaRespOrderSummaryVO woaRespOrderSummaryVO = new WoaRespOrderSummaryVO();
        woaRespOrderSummaryVO.setAllCount(allCount);
        woaRespOrderSummaryVO.setBeingCount(beingCount);
        woaRespOrderSummaryVO.setNoCount(noCount);
        woaRespOrderSummaryVO.setOvertimeCount(overtimeCount);
        Result result = new Result();
        return result.success(woaRespOrderSummaryVO);
    }

    @Override
    public Result addNum(HttpServletRequest request) {
        logger.info("新增工单数量");
        Result result = new Result();
        List<Integer> addNumList = new ArrayList<>();
        Integer addToday = 0;
        Integer addMonth = 0;
        Integer addYear = 0;
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("token", request.getHeader("token"));
        /*String resultJson = HttpUtil.get(httpUaApi.getUrl() + httpUaApi.getSelectById() + "/" + userId, headerMap);
        if (resultJson != null) {
            JSONObject jsonObjectResult = JSONObject.parseObject(resultJson);
            JSONObject jsonObjectData = jsonObjectResult.getJSONObject("data");
            Integer founderId = jsonObjectData.getInteger("founderId");*/
        WoaOrderQuery woaOrderQuery = new WoaOrderQuery();
        User user = logUserService.get(userId);
        Integer founderId = user.getFounderId();
        if (founderId == 0) {
            //超管    1，2，3，4，5
            /*woaOrderQuery.setCreator(userId);
            woaOrderQuery.setProcessor(userId);
            woaOrderQuery.setApproval(userId);*/
        } else if (founderId == 1) {
            //管理员   1，5
            woaOrderQuery.setCreator(userId);
            woaOrderQuery.setApproval(userId);
        } else {
            //普通人员  2，3，4
            woaOrderQuery.setCreator(userId);
            woaOrderQuery.setProcessor(userId);
        }
        List<OrderInfo> orderInfoList = baseMapper.selectNum(woaOrderQuery);
        Date dayBeginOrEnd = DateUtil.getDayBeginOrEnd(0, true);
        Date monthTime = DateUtil.getMonthTime(0, true);
        Date yearBeginOrEnd = DateUtil.getYearBeginOrEnd(0, true);
        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getCreateTime().getTime() > dayBeginOrEnd.getTime()) {
                addToday++;
            }
            if (orderInfo.getCreateTime().getTime() > monthTime.getTime()) {
                addMonth++;
            }
            if (orderInfo.getCreateTime().getTime() > yearBeginOrEnd.getTime()) {
                addYear++;
            }
        }
        addNumList.add(addToday);
        addNumList.add(addMonth);
        addNumList.add(addYear);
        //}
        return result.success(addNumList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO) {
        logger.info("新增工单，接收数据：{}", woaReqOrderNewVO);
        Result result = new Result();
        if (woaReqOrderNewVO.getFoundMode() == null) {
            woaReqOrderNewVO.setFoundMode(0);
        }
        // 当前用户id，过滤超级管理员
        Integer userId = 0;
        Integer approval = 1;
        if (woaReqOrderNewVO.getFoundMode() != 1) {
            userId = JavaWebTokenUtil.parserStaffIdByToken(request);
            if (userId == 0) {
                return result.error("请先登录");
            } else if (userId == 1) {
                //超级管理员
                approval = userId;
            } else {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("token", request.getHeader("token"));
                String resultJson = HttpUtil.get(httpUaApi.getUrl() + httpUaApi.getSelectById() + "/" + userId, headerMap);
                if (resultJson != null) {
                    JSONObject jsonObjectResult = JSONObject.parseObject(resultJson);
                    JSONObject jsonObjectData = jsonObjectResult.getJSONObject("data");
                    Integer founderId = jsonObjectData.getInteger("founderId");
                    if (founderId == 1) {
                        approval = userId;
                    } else {
                        String approvalIdJson = HttpUtil.get(httpUaApi.getUrl() + httpUaApi.getGetApproval() + "?id=" + userId, headerMap);
                        if (approvalIdJson != null) {
                            JSONObject approvalIdObject = JSONObject.parseObject(approvalIdJson);
                            approval = approvalIdObject.getInteger("data");
                        }
                    }
                }
            }
        }
        // 构建工单对象
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setName(woaReqOrderNewVO.getName());
        orderInfo.setDescription(woaReqOrderNewVO.getDescription());
        orderInfo.setCreator(userId);
        orderInfo.setUpdateTime(new Date());
        orderInfo.setCreateTime(orderInfo.getUpdateTime());
        orderInfo.setStatusId(1);
        orderInfo.setLampPostId(woaReqOrderNewVO.getLampPostId());
        // 自动生成
        if (woaReqOrderNewVO.getFoundMode() == 1) {
            orderInfo.setCreator(0);
            orderInfo.setStatusId(3);
            orderInfo.setProcessor(woaReqOrderNewVO.getProcessor());
            orderInfo.setProcessorRole(woaReqOrderNewVO.getProcessorRole());
            orderInfo.setFinishTime(woaReqOrderNewVO.getFinishTime());
            orderInfo.setAlarmTypeId(0);
        }
        // 获取告警id集合
        List<Integer> alarmIdList = woaReqOrderNewVO.getAlarmIdList();
        // 如果工单关联告警列表不为空，处理告警类型和工单地址
        if (alarmIdList != null && alarmIdList.size() > 0) {
            // 告警类型
            List<Alarm> alarms = (ArrayList) alarmService.listByIds(alarmIdList);
            List<Alarm> typeCollect = alarms.stream().filter(a -> a.getTypeId()
                    .equals(alarms.get(0).getTypeId())).collect(Collectors.toList());
            // 如果所有类型，都是同一个，用同一个。不是同一个另议，当前都是以第一个为准，方便以后需求有变
            if (typeCollect.size() == alarms.size()) {
                orderInfo.setAlarmTypeId(typeCollect.get(0).getTypeId());
            } else {
                orderInfo.setAlarmTypeId(0);
            }
            // 工单地址
            List<Alarm> addrCollect = new ArrayList<>();
            if (alarms.get(0) != null && alarms.get(0).getAddr() != null) {
                addrCollect = alarms.stream().filter(a -> alarms.get(0).getAddr().equals(a.getAddr())).collect(Collectors.toList());
            }
            // 如果所有地址，都是同一个，用同一个。不是同一个另议，当前都是以第一个为准，方便以后需求有变
            if (addrCollect.size() == alarms.size()) {
                orderInfo.setAddr(alarms.get(0).getAddr());
            } else {
                orderInfo.setAddr("多个地址");
            }
        } else {
            orderInfo.setAddr(woaReqOrderNewVO.getAddr());
            orderInfo.setAlarmTypeId(woaReqOrderNewVO.getAlarmTypeId());
        }
        orderInfo.setApproval(approval);
        logger.info("新增工单添加数据：{}", orderInfo);
        this.save(orderInfo);
        // 添加工单进程
        OrderProcess orderProcess = new OrderProcess();
        orderProcess.setCreateTime(orderInfo.getUpdateTime());
        orderProcess.setStatusId(orderInfo.getStatusId());
        orderProcess.setOrderId(orderInfo.getId());
        // 判断是否是自动生成的工单
        if (woaReqOrderNewVO.getFoundMode() == 1) {
            orderProcess.setOperator(0);
            orderProcess.setDescription("系统自动生成");
        } else {
            orderProcess.setOperator(userId);
            orderProcess.setDescription(woaReqOrderNewVO.getDescription());
        }
        orderProcessService.save(orderProcess);
        // 关联图片与工单
        if (woaReqOrderNewVO.getImgIdList() != null && woaReqOrderNewVO.getImgIdList().size() > 0) {
            orderPicService.relationOrderImg(woaReqOrderNewVO.getImgIdList(), orderProcess.getId(), orderInfo.getId());
        }
        // 修改告警关联的工单id,并更改告警状态
        if (alarmIdList != null && alarmIdList.size() > 0) {
            alarmService.updateOpenOrder(alarmIdList, orderInfo.getId(), 2);
        }
        return result.success("工单新增成功");
    }

    @Override
    public Result listHandle(HttpServletRequest request, WoaOrderQuery woaOrderQuery) {
        logger.info("工单列表，接收参数：{}", woaOrderQuery);
        Result result = new Result();
        // 检测超时，更改超时状态
        this.baseMapper.updateOvertime();
        // 当前用户id，过滤超级管理员
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);

        if (userId == null) {
            return result.error("请先登录");
        }
        if (userId == 1) {
            userId = null;
        }
        if (woaOrderQuery.getCreator() == null && woaOrderQuery.getProcessor() == null) {
            return result.error("查询有误");
        }
        // 查询创建人
        if (woaOrderQuery.getCreator() != null) {
            if (user.getFounderId() == 1) {
                woaOrderQuery.setApproval(userId);
            }
            woaOrderQuery.setCreator(userId);
        }
        // 查询处理人
        if (woaOrderQuery.getProcessor() != null) {
            woaOrderQuery.setProcessor(userId);
        }
        woaOrderQuery.setChoose(1);

        if (woaOrderQuery.getOvertime() != null && woaOrderQuery.getOvertime() == 1) {
            woaOrderQuery.setOrderStatus(null);
        }
        IPage<WoaRespOrderListVO> iPage = new Page<>(woaOrderQuery.getPageNum(), woaOrderQuery.getPageSize());
        IPage<WoaRespOrderListVO> woaRespOrderListVOList = this.baseMapper.listHandle(iPage, woaOrderQuery);
        // 修改工单为旧的状态
        if (woaOrderQuery.getOrderStatus() != null) {

        }
        return result.success(woaRespOrderListVOList);
    }

    @Override
    public Result listByUserId(HttpServletRequest request, WoaOrderQuery woaOrderQuery) {
        logger.info("工单列表，接收参数：{}");
        Result result = new Result();
        // 检测超时，更改超时状态
        this.baseMapper.updateOvertime();
        // 当前用户id，过滤超级管理员
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        System.out.println(userId);
        if (userId == 1) {
            woaOrderQuery.setChoose(3);
            woaOrderQuery.setApproval(userId);
            //woaOrderQuery.setProcessor(userId);
            woaOrderQuery.setCreator(userId);
        } else {
//            Map<String, String> headerMap = new HashMap<>();
//            headerMap.put("token", request.getHeader("token"));
//            String resultJson = HttpUtil.get(httpUaApi.getUrl() + httpUaApi.getSelectById() + "/" + userId, headerMap);
//            if (resultJson != null) {
//                JSONObject jsonObjectResult = JSONObject.parseObject(resultJson);
//                JSONObject jsonObjectData = jsonObjectResult.getJSONObject("data");
//                Integer founderId = jsonObjectData.getInteger("founderId");
            User user = logUserService.get(userId);
            Integer founderId = user.getFounderId();
            if (founderId == 1) {
                //管理员
                woaOrderQuery.setCreator(userId);
                woaOrderQuery.setApproval(userId);
                woaOrderQuery.setChoose(3);
            } else {
                //非管理员
                woaOrderQuery.setCreator(userId);
                woaOrderQuery.setProcessor(userId);
                woaOrderQuery.setChoose(4);
            }
//            }

            /*// 查询审核人
            if (woaOrderQuery.getApproval() != null) {

            }
            // 查询处理人
            if (woaOrderQuery.getProcessor() != null) {

            }*/
            if (woaOrderQuery.getChoose() == null) {
                return result.error("查询有误");
            }

        }


        if (woaOrderQuery.getOvertime() != null && woaOrderQuery.getOvertime() == 1) {
            woaOrderQuery.setOrderStatus(null);
        }
        IPage<WoaRespOrderListVO> iPage = new Page<>(woaOrderQuery.getPageNum(), woaOrderQuery.getPageSize());
        IPage<WoaRespOrderListVO> woaRespOrderListVOList = this.baseMapper.listHandle(iPage, woaOrderQuery);
        // 修改工单为旧的状态
        if (woaOrderQuery.getOrderStatus() != null) {

        }
        return result.success(woaRespOrderListVOList);
    }

    @Override
    public Result get(HttpServletRequest request, Integer id) {
        logger.info("查询工单详情，工单id：{}", id);
        Result result = new Result();
        // 返回对象
        WoaRespOrderDetailsVO woaRespOrderDetailsVO = new WoaRespOrderDetailsVO();
        if (id == null) {
            return result.error("工单id不能为空");
        }
        // 工单对象
        WoaRespOrderVO woaRespOrderVO = this.baseMapper.getWoaRespOrderVO(id);
        // 工单图片集合
        List<WoaRespOrderPicVO> woaRespOrderPicVOList = orderPicService.getWoaRespOrderPicVO(id);
        // 工单告警对象集合
        List<WoaRespOrderAlarmVO> woaRespOrderAlarmVOList = alarmService.getWoaRespOrderAlarmVO(id);
        // 工单进程对象集合
        List<WoaRespOrderProcessVO> woaRespOrderProcessVOList = orderProcessService.getWoaRespOrderProcessVO(id);
        woaRespOrderDetailsVO.setCurrentTime(new Date());
        woaRespOrderDetailsVO.setWoaRespOrderAlarmVOList(woaRespOrderAlarmVOList);
        woaRespOrderDetailsVO.setWoaRespOrderPicVOList(woaRespOrderPicVOList);
        woaRespOrderDetailsVO.setWoaRespOrderProcessVOList(woaRespOrderProcessVOList);
        woaRespOrderDetailsVO.setWoaRespOrderVO(woaRespOrderVO);
        return result.success(woaRespOrderDetailsVO);
    }

    @Override
    public Result updateOrder(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO) {
        logger.info("编辑工单, 接收参数：{}", woaReqOrderNewVO);
        // 当前用户id，过滤超级管理员
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result result = new Result();
        OrderInfo orderInfo = this.getById(woaReqOrderNewVO.getId());
        if (orderInfo.getStatusId() != 2) {
            result.error("当前状态不能编辑工单");
        }
        // 构建工单对象
        orderInfo.setName(woaReqOrderNewVO.getName());
        orderInfo.setDescription(woaReqOrderNewVO.getDescription());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setStatusId(1);
        orderInfo.setAddr(woaReqOrderNewVO.getAddr());
        orderInfo.setAlarmTypeId(woaReqOrderNewVO.getAlarmTypeId());
        orderInfo.setLampPostId(woaReqOrderNewVO.getLampPostId());
        this.updateById(orderInfo);
        // 添加工单进程
        OrderProcess orderProcess = new OrderProcess();
        orderProcess.setCreateTime(orderInfo.getUpdateTime());
        orderProcess.setStatusId(orderInfo.getStatusId());
        orderProcess.setOrderId(orderInfo.getId());
        orderProcess.setOperator(userId);
        orderProcess.setDescription(woaReqOrderNewVO.getDescription());
        orderProcessService.save(orderProcess);
        // 关联图片与工单
        if (woaReqOrderNewVO.getImgIdList() != null && woaReqOrderNewVO.getImgIdList().size() > 0) {
            orderPicService.relationOrderImg(woaReqOrderNewVO.getImgIdList(), orderProcess.getId(), orderInfo.getId());
        }
        return result.success("工单修改成功");
    }

    @Override
    public Result handle(HttpServletRequest request, Integer id) {
        logger.info("开始处理工单id：{}", id);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        OrderInfo orderInfo = this.getById(id);
        if (userId == null) {
            return result.error("请先登录");
        }
        if (id == null) {
            return result.error("工单id不能为空");
        }
        if (orderInfo.getStatusId() != 3) {
            return result.error("工单当前状态不能处理");
        }
        orderInfo.setUpdateTime(new Date());
        orderInfo.setStatusId(4);
        this.updateById(orderInfo);
        // 更新进度
        OrderProcess orderProcess = new OrderProcess();
        orderProcess.setCreateTime(orderInfo.getUpdateTime());
        orderProcess.setStatusId(orderInfo.getStatusId());
        orderProcess.setOrderId(orderInfo.getId());
        orderProcess.setOperator(userId);
        orderProcessService.save(orderProcess);
        return result.success("开始处理工单");
    }

    @Override
    public Result complete(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO) {
        logger.info("处理完成进入待审核，接收参数：{}", woaReqOrderNewVO);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        if (userId == null) {
            return result.error("请先登录");
        }
        OrderInfo orderInfo = this.getById(woaReqOrderNewVO.getId());
        if (orderInfo == null) {
            return result.error("工单不存在");
        }
        if (orderInfo.getStatusId() != 4) {
            return result.error("工单当前状态不能处理完成");
        }
        orderInfo.setUpdateTime(new Date());
        orderInfo.setStatusId(5);
        orderInfo.setDescription(woaReqOrderNewVO.getDescription());
        this.updateById(orderInfo);
        // 更新进度
        OrderProcess orderProcess = new OrderProcess();
        orderProcess.setCreateTime(orderInfo.getUpdateTime());
        orderProcess.setStatusId(orderInfo.getStatusId());
        orderProcess.setOrderId(orderInfo.getId());
        orderProcess.setOperator(userId);
        orderProcess.setDescription(woaReqOrderNewVO.getDescription());
        orderProcessService.save(orderProcess);
        // 关联图片与工单
        if (woaReqOrderNewVO.getImgIdList() != null && woaReqOrderNewVO.getImgIdList().size() > 0) {
            orderPicService.relationOrderImg(woaReqOrderNewVO.getImgIdList(), orderProcess.getId(), orderInfo.getId());
        }
        return result.success("工单处理完成");
    }

    @Override
    public Result firstTrial(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO) {
        logger.info("工单初审,接收数据：{}", woaReqOrderNewVO);
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        Result result = new Result();
        if (userId == null) {
            return result.error("请先登录");
        }
        OrderInfo orderInfo = this.getById(woaReqOrderNewVO.getId());
        if (orderInfo == null) {
            return result.error("工单不存在");
        }
        if (orderInfo.getStatusId() != 1) {
            return result.error("工单当前状态不能区级初审");
        }
        // 构建工单对象，修改数据
        orderInfo.setUpdateTime(new Date());
        //审核意见（0：不通过  1：通过）
        if (woaReqOrderNewVO.getOpinion() == 1) {
            orderInfo.setStatusId(3);
            orderInfo.setStartHandleTime(orderInfo.getUpdateTime());
            orderInfo.setFinishTime(woaReqOrderNewVO.getFinishTime());
            orderInfo.setProcessor(woaReqOrderNewVO.getProcessor());
            orderInfo.setProcessorRole(woaReqOrderNewVO.getProcessorRole());
        } else {
            orderInfo.setStatusId(2);
        }
        this.updateById(orderInfo);
        // 更新进度
        OrderProcess orderProcess = new OrderProcess();
        orderProcess.setCreateTime(orderInfo.getUpdateTime());
        orderProcess.setStatusId(orderInfo.getStatusId());
        orderProcess.setOrderId(orderInfo.getId());
        orderProcess.setOperator(userId);
        orderProcess.setDescription(woaReqOrderNewVO.getDescription());
        orderProcessService.save(orderProcess);
        return result.success("初审完成");
    }

    @Override
    public Result secondTrial(HttpServletRequest request, WoaReqOrderNewVO woaReqOrderNewVO) {
        logger.info("工单审核,接收数据：{}", woaReqOrderNewVO);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        if (userId == null) {
            return result.error("请先登录");
        }
        OrderInfo orderInfo = this.getById(woaReqOrderNewVO.getId());
        if (orderInfo == null) {
            return result.error("工单不存在");
        }
        if (orderInfo.getStatusId() != 5) {
            return result.error("工单当前状态不能审核");
        }
        //构造工单对象
        orderInfo.setUpdateTime(new Date());
        orderInfo.setDescription(woaReqOrderNewVO.getDescription());
        orderInfo.setFinishTime(woaReqOrderNewVO.getFinishTime());
        orderInfo.setStartHandleTime(orderInfo.getUpdateTime());
        //审核意见（0：不通过  1：通过）
        if (woaReqOrderNewVO.getOpinion() == 0) {
            orderInfo.setStatusId(3);
        } else {
            orderInfo.setStatusId(6);
        }
        this.updateById(orderInfo);
        // 更新进度
        OrderProcess orderProcess = new OrderProcess();
        orderProcess.setCreateTime(orderInfo.getUpdateTime());
        orderProcess.setStatusId(orderInfo.getStatusId());
        orderProcess.setOrderId(orderInfo.getId());
        orderProcess.setOperator(userId);
        orderProcess.setDescription(woaReqOrderNewVO.getDescription());
        orderProcessService.save(orderProcess);
        return result.success("审核成功");
    }

    @Override
    public Result newStatus(WoaReqOrderNewStatusVO woaReqOrderNewStatusVO, HttpServletRequest request) {
        logger.info("获取各状态的新工单");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper();
        wrapper.eq(OrderInfo::getNewStatus, 1);
        List<OrderInfo> orderInfoList = this.list(wrapper);

        return null;
    }

    @Override
    public Result unique(OrderInfo orderInfo, HttpServletRequest request) {
        logger.info("工单验证唯一性，接收参数：{}", orderInfo);
        Result result = new Result();
        if (null != orderInfo) {
            if (orderInfo.getId() != null) {
                if (orderInfo.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<OrderInfo> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(OrderInfo::getName, orderInfo.getName());
                    OrderInfo orderInfoByName = this.getOne(wrapperName);
                    if (orderInfoByName != null && !orderInfoByName.getId().equals(orderInfo.getId())) {
                        return result.error("名称已存在");
                    }
                } else {
                    return result.error("名称不能为空");
                }
            } else {
                if (orderInfo.getName() != null) {
                    // 验证名称是否重复
                    LambdaQueryWrapper<OrderInfo> wrapperName = new LambdaQueryWrapper();
                    wrapperName.eq(OrderInfo::getName, orderInfo.getName());
                    OrderInfo orderInfoByName = this.getOne(wrapperName);
                    if (orderInfoByName != null) {
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