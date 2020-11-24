/**
 * @filename:CanStrategyServiceImpl 2020-11-18
 * @project electricity  V1.0
 * Copyright(c) 2018 Xiaok Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.electricity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanStrategyMapper;
import com.exc.street.light.electricity.service.*;
import com.exc.street.light.electricity.util.*;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.dlm.ControlLoopTimerDTO;
import com.exc.street.light.resource.entity.electricity.*;
import com.exc.street.light.resource.qo.electricity.CanStrategyQueryObject;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.electricity.RespCanStrategyActionVO;
import com.exc.street.light.resource.vo.electricity.RespCanStrategyVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanChannelControlVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyIssueVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyUniquenessVO;
import com.exc.street.light.resource.vo.req.electricity.ReqCanStrategyVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: (路灯网关策略表服务实现)
 * @version: V1.0
 * @author: Xiaok
 */
@Slf4j
@Service
public class CanStrategyServiceImpl extends ServiceImpl<CanStrategyMapper, CanStrategy> implements CanStrategyService {

    private static final SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private CanStrategyActionService canStrategyActionService;
    @Autowired
    private CanChannelService canChannelService;
    @Autowired
    private LogUserService logUserService;
    @Autowired
    private ElectricityNodeService electricityNodeService;
    @Autowired
    private CanChannelStrategyHistoryService canChannelStrategyHistoryService;

    @Override
    public Result<String> add(ReqCanStrategyVO reqVo, HttpServletRequest request) {
        log.info("新增路灯网关策略,接收参数:ReqCanStrategyVO={}", reqVo);
        Result<String> result = new Result<>();
        //获取当前用户ID
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        if (userId == null) {
            log.error("新增路灯网关策略失败,未登录,接收参数:ReqCanStrategyVO={}", reqVo);
            return result.error("新增失败", "token失效");
        }
        if (reqVo == null || StringUtils.isBlank(reqVo.getName()) || reqVo.getActionList() == null || reqVo.getActionList().isEmpty()) {
            log.error("新增路灯网关策略失败,参数缺失,接收参数:ReqCanStrategyVO={}", reqVo);
            return result.error("新增失败", "参数缺失");
        }
        if (reqVo.getActionList().size() > 4) {
            log.error("新增路灯网关策略失败,动作个数超出4个限制,接收参数:ReqCanStrategyVO={}", reqVo);
            return result.error("新增失败", "动作个数超出限制");
        }
        //保存策略
        CanStrategy canStrategy = new CanStrategy().setCreateTime(new Date())
                .setCreator(userId)
                .setName(reqVo.getName());
        this.save(canStrategy);
        //更新策略动作
        canStrategyActionService.saveAction(canStrategy.getId(), reqVo.getActionList());
        log.info("新增路灯网关策略成功,接收参数:ReqCanStrategyVO={}", reqVo);
        return result.success("新增成功", "");
    }

    @Override
    public Result<String> update(ReqCanStrategyVO reqVo, HttpServletRequest request) {
        log.info("修改路灯网关策略,接收参数:ReqCanStrategyVO={}", reqVo);
        Result<String> result = new Result<>();
        if (reqVo == null || reqVo.getId() == null || StringUtils.isBlank(reqVo.getName())) {
            log.error("修改路灯网关策略失败,参数缺失,接收参数:ReqCanStrategyVO={}", reqVo);
            return result.error("修改失败", "参数缺失");
        }
        //修改策略
        CanStrategy canStrategy = new CanStrategy()
                .setId(reqVo.getId())
                .setName(reqVo.getName());
        this.updateById(canStrategy);
        //更新策略动作
        canStrategyActionService.saveAction(canStrategy.getId(), reqVo.getActionList());
        log.info("修改路灯网关策略成功,接收参数:ReqCanStrategyVO={}", reqVo);
        return result.success("修改成功", "");
    }

    @Override
    public Result<IPage<RespCanStrategyVO>> getPageList(CanStrategyQueryObject qo, HttpServletRequest request) {
        log.info("分页查询路灯网关策略,接收参数:CanStrategyQueryObject={}", qo);
        Result<IPage<RespCanStrategyVO>> result = new Result<>();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        if (userId == null) {
            log.error("分页查询路灯网关策略失败，token失效,接收参数:CanStrategyQueryObject={}", qo);
            return result.error("查询失败,身份已过期", null);
        }
        IPage<RespCanStrategyVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<RespCanStrategyVO> pageList = baseMapper.getPageList(page, qo);
        List<Integer> strategyIdList = pageList.getRecords().stream().map(RespCanStrategyVO::getId).collect(Collectors.toList());
        if (!strategyIdList.isEmpty()) {
            LambdaQueryWrapper<CanStrategyAction> actionWrapper = new LambdaQueryWrapper<>();
            actionWrapper.in(CanStrategyAction::getStrategyId, strategyIdList)
                    .select(CanStrategyAction::getStrategyId, CanStrategyAction::getStartDate, CanStrategyAction::getEndDate);
            List<CanStrategyAction> allActionList = canStrategyActionService.list(actionWrapper);
            Map<Integer, List<CanStrategyAction>> actionMap = allActionList.stream().collect(Collectors.groupingBy(CanStrategyAction::getStrategyId));
            for (RespCanStrategyVO record : pageList.getRecords()) {
                List<CanStrategyAction> actions = actionMap.get(record.getId());
                if (actions == null || actions.isEmpty()) {
                    continue;
                }
                //设置策略的开始时间
                String startDateStr = "";
                List<Long> startDateLongList = actions.stream().filter(e -> StringUtils.isNotBlank(e.getStartDate())).map(e -> {
                    try {
                        return dateSdf.parse(e.getStartDate()).getTime();
                    } catch (ParseException ex) {
                        return System.currentTimeMillis();
                    }
                }).sorted().collect(Collectors.toList());
                if (!startDateLongList.isEmpty()) {
                    startDateStr = dateSdf.format(new Date(startDateLongList.get(0)));
                }
                record.setStartDate(startDateStr);
                //设置策略的结束时间
                String endDateStr = "";
                List<CanStrategyAction> isForever = actions.stream().filter(e -> StringUtils.isNotBlank(e.getEndDate())).collect(Collectors.toList());
                if (actions.size() == isForever.size()) {
                    List<Long> endDateLongList = actions.stream().filter(e -> StringUtils.isNotBlank(e.getEndDate())).map(e -> {
                        try {
                            return dateSdf.parse(e.getEndDate()).getTime();
                        } catch (ParseException ex) {
                            return 0L;
                        }
                    }).sorted().collect(Collectors.toList());
                    //反转
                    Collections.reverse(endDateLongList);
                    if (!endDateLongList.isEmpty() && !endDateLongList.get(0).equals(0L)) {
                        endDateStr = dateSdf.format(new Date(endDateLongList.get(0)));
                    }
                }
                record.setEndDate(endDateStr);
            }
        }
        log.info("分页查询路灯网关策略成功,接收参数:CanStrategyQueryObject={}", qo);
        return result.success("获取成功", pageList);

    }

    @Override
    public Result<RespCanStrategyVO> getInfoById(Integer id, HttpServletRequest request) {
        log.info("获取路灯网关策略详情,接收参数：网关策略ID={}", id);
        Result<RespCanStrategyVO> result = new Result<>();
        if (id == null) {
            log.error("获取路灯网关策略详情失败,网关策略ID为空");
            return result.error("获取失败,参数缺失", null);
        }
        LambdaQueryWrapper<CanStrategy> strategyWrapper = new LambdaQueryWrapper<>();
        strategyWrapper.eq(CanStrategy::getId, id).eq(CanStrategy::getDelFlag, 0);
        CanStrategy canStrategy = this.getOne(strategyWrapper);
        if (canStrategy == null) {
            log.error("获取路灯网关策略详情失败,记录不存在,接收参数：网关策略ID={}", id);
            return result.error("获取失败,记录不存在或已被删除", null);
        }
        RespCanStrategyVO respVo = new RespCanStrategyVO();
        BeanUtils.copyProperties(canStrategy, respVo);
        LambdaQueryWrapper<CanStrategyAction> actionWrapper = new LambdaQueryWrapper<>();
        actionWrapper.eq(CanStrategyAction::getStrategyId, respVo.getId());
        List<CanStrategyAction> actionList = canStrategyActionService.list(actionWrapper);
        if (!actionList.isEmpty()) {
            List<RespCanStrategyActionVO> respActionVoList = new ArrayList<>();
            for (CanStrategyAction canStrategyAction : actionList) {
                Integer[] cycleList = DataUtil.getCycleName(canStrategyAction.getWeekValue());
                RespCanStrategyActionVO respActionVo = new RespCanStrategyActionVO();
                BeanUtils.copyProperties(canStrategyAction, respActionVo);
                respActionVo.setCycleTypes(cycleList);
                respActionVoList.add(respActionVo);
            }
            respVo.setActionVOList(respActionVoList);
        }
        log.info("获取路灯网关策略详情成功,接收参数：网关策略ID={}", id);
        return result.success("获取成功", respVo);
    }

    @Override
    public Result<String> deleteById(Integer id, HttpServletRequest request) {
        log.info("删除路灯网关策略,接收参数：网关策略ID={}", id);
        Result<String> result = new Result<>();
        if (id == null) {
            log.error("删除路灯网关策略失败,网关策略ID为空");
            return result.error("删除失败", "参数缺失");
        }
        //先讲策略的删除标记置为删除状态
        LambdaUpdateWrapper<CanStrategy> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CanStrategy::getDelFlag, 1).eq(CanStrategy::getId, id);
        this.update(updateWrapper);
        //再将回路里绑定的策略ID置空
        LambdaUpdateWrapper<CanChannel> channelWrapper = new LambdaUpdateWrapper<>();
        channelWrapper.set(CanChannel::getStrategyId, null).eq(CanChannel::getStrategyId, id);
        canChannelService.update(channelWrapper);
        log.info("删除路灯网关策略成功,接收参数：网关策略ID={}", id);
        return result.success("删除成功", "");
    }

    @Override
    public Result<String> batchDelete(String ids, HttpServletRequest request) {
        log.info("批量删除路灯网关策略,接收参数：网关策略ID集合={}", ids);
        Result<String> result = new Result<>();
        if (StringUtils.isBlank(ids)) {
            log.error("批量删除路灯网关策略失败,网关策略ID集合为空");
            return result.error("删除失败", "参数缺失");
        }
        List<Integer> idList = Stream.of(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        if (idList.isEmpty()) {
            log.error("批量删除路灯网关策略失败,网关策略ID集合参数不合法,接收参数：网关策略ID集合={}", ids);
            return result.error("删除失败", "参数不合法");
        }
        //先讲策略的删除标记置为删除状态
        LambdaUpdateWrapper<CanStrategy> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CanStrategy::getDelFlag, 1).in(CanStrategy::getId, idList);
        this.update(updateWrapper);
        //再将回路里绑定的策略ID置空
        LambdaUpdateWrapper<CanChannel> channelWrapper = new LambdaUpdateWrapper<>();
        channelWrapper.set(CanChannel::getStrategyId, null).in(CanChannel::getStrategyId, idList);
        canChannelService.update(channelWrapper);
        log.info("批量删除路灯网关策略成功,接收参数：网关策略ID集合={}", ids);
        return result.success("批量删除成功", "");
    }

    @Override
    public Result<JSONObject> issue(ReqCanStrategyIssueVO reqVo, HttpServletRequest request) {
        log.info("网关策略下发，接收参数：ReqCanStrategyIssueVO = {}", reqVo);
        Result<JSONObject> result = new Result<>();
        if (reqVo == null || reqVo.getStrategyId() == null || reqVo.getControlVOList() == null || reqVo.getControlVOList().isEmpty()) {
            log.error("网关策略下发失败，参数缺失，接收参数：ReqCanStrategyIssueVO = {}", reqVo);
            return result.error("下发失败，参数缺失", new JSONObject(true));
        }
        Date now = new Date();
        //离线的路灯网关名称集合
        List<String> offlineNameList = new ArrayList<>();
        //下发失败的路灯网关名称集合
        List<String> issueFailNameList = new ArrayList<>();
        //下发无响应的路灯网关名称集合
        List<String> noRespNameList = new ArrayList<>();
        Integer strategyId = reqVo.getStrategyId();
        List<ReqCanChannelControlVO> controlVOList = reqVo.getControlVOList();
        //获取场景下的动作集合
        LambdaQueryWrapper<CanStrategyAction> actionWrapper = new LambdaQueryWrapper<>();
        actionWrapper.eq(CanStrategyAction::getStrategyId, strategyId);
        List<CanStrategyAction> actionList = canStrategyActionService.list(actionWrapper);
        if (actionList.isEmpty()) {
            log.error("网关策略下发失败，场景下无动作，接收参数：ReqCanStrategyIssueVO = {}", reqVo);
            return result.error("下发失败，场景下无动作", new JSONObject());
        }
        //下发成功的回路ID
        List<Integer> successLoopIdList = new ArrayList<>();
        //根据网关ID分组
        Map<Integer, List<ReqCanChannelControlVO>> nodeMap = controlVOList.stream().filter(e -> e.getNodeId() != null && e.getControlId() != null)
                .collect(Collectors.groupingBy(ReqCanChannelControlVO::getNodeId));
        if (nodeMap.isEmpty()) {
            log.error("网关策略下发失败，参数错误，接收参数：ReqCanStrategyIssueVO = {}", reqVo);
            return result.error("下发失败，参数错误", new JSONObject());
        }
        for (Map.Entry<Integer, List<ReqCanChannelControlVO>> entry : nodeMap.entrySet()) {
            Integer nodeId = entry.getKey();
            List<ReqCanChannelControlVO> controlList = entry.getValue();
            //控制的回路 集合
            List<Integer> controlIdList = controlList.stream().map(ReqCanChannelControlVO::getControlId).collect(Collectors.toList());
            LambdaQueryWrapper<ElectricityNode> nodeWrapper = new LambdaQueryWrapper<>();
            nodeWrapper.select(ElectricityNode::getId, ElectricityNode::getName, ElectricityNode::getIsOffline, ElectricityNode::getMac, ElectricityNode::getIp, ElectricityNode::getPort)
                    .eq(ElectricityNode::getId, nodeId);
            ElectricityNode node = electricityNodeService.getOne(nodeWrapper);
            //网关数据不存在 跳过
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
            //获取下发timer对象集合
            List<ControlLoopTimerDTO> dtoList = canStrategyActionService.getTimerList(node.getId(), actionList, controlIdList);
            byte[] bytes = ProtocolUtil.setTimer(mac, dtoList);
            //返回结果
            byte[] rtn = SocketClient.send(ip, port, bytes);
            if (rtn == null) {
                //网关无响应
                noRespNameList.add(node.getName());
            } else if (AnalysisUtil.getRtn(rtn) != ConstantUtil.RET_1) {
                //下发失败 将网关名称添加入失败集合
                issueFailNameList.add(node.getName());
            } else {
                //下发成功
                //获取对应回路信息集合
                LambdaQueryWrapper<CanChannel> channelQueryWrapper = new LambdaQueryWrapper<>();
                channelQueryWrapper.select(CanChannel::getId).eq(CanChannel::getNid, node.getId())
                        .in(CanChannel::getControlId, controlIdList)
                        .eq(CanChannel::getSid, 4);
                List<CanChannel> channelList = canChannelService.list(channelQueryWrapper);
                if (channelList != null && !channelList.isEmpty()) {
                    List<Integer> channelIdList = channelList.stream().map(CanChannel::getId).collect(Collectors.toList());
                    if (channelIdList.isEmpty()) {
                        continue;
                    }
                    successLoopIdList.addAll(channelIdList);
                }
            }
        }
        if (!successLoopIdList.isEmpty()) {
            //将成功下发的回路绑定当前下发的策略ID
            LambdaUpdateWrapper<CanChannel> channelWrapper = new LambdaUpdateWrapper<>();
            channelWrapper.set(CanChannel::getStrategyId, strategyId)
                    .in(CanChannel::getId, successLoopIdList)
                    .eq(CanChannel::getSid, 4);
            canChannelService.update(channelWrapper);
            //写入策略下发记录
            List<CanChannelStrategyHistory> historyList = new ArrayList<>();
            successLoopIdList.forEach(e -> {
                historyList.add(new CanChannelStrategyHistory()
                        .setCreateTime(now)
                        .setChannelId(e)
                        .setIsSuccess(1)
                        .setStrategyId(strategyId)
                );
            });
            if (!historyList.isEmpty()) {
                canChannelStrategyHistoryService.saveBatch(historyList);
            }
        }
        JSONObject returnData = new JSONObject(true);
        returnData.put("isAllSuccess", issueFailNameList.isEmpty() && offlineNameList.isEmpty() && noRespNameList.isEmpty());
        returnData.put("offlineList", offlineNameList);
        returnData.put("issueFailList", issueFailNameList);
        returnData.put("noRespNameList", noRespNameList);
        log.info("网关策略下发成功，接收参数：ReqCanStrategyIssueVO = {},returnJSON = {}", reqVo, returnData);
        return result.success("下发成功", returnData);
    }

    @Override
    public Result<Integer> uniqueness(ReqCanStrategyUniquenessVO uniquenessVO, HttpServletRequest request) {
        log.info("网关策略唯一性校验，接收参数：ReqCanStrategyUniquenessVO = {}", uniquenessVO);
        Result<Integer> result = new Result<>();
        if (uniquenessVO == null) {
            log.error("网关策略唯一性校验失败，参数缺失，接收参数：ReqCanStrategyUniquenessVO = {}", uniquenessVO);
            return result.error("校验失败，参数缺失",-1);
        }
        Integer id = uniquenessVO.getId();
        String name = uniquenessVO.getName();
        //如果存在id，则为编辑，判断是否是编辑本身
        if (StringUtils.isNotBlank(name)) {
            LambdaQueryWrapper<CanStrategy> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CanStrategy::getName, name).last("LIMIT 1");
            CanStrategy strategy = this.getOne(wrapper);
            if ((id != null && strategy != null && !strategy.getId().equals(id)) || (id == null && strategy != null)) {
                log.error("网关策略唯一性校验失败,设备名称 {} 已存在,请重新输入", name);
                return result.error("设备名称已存在,请重新输入", 1);
            }
        }
        log.info("网关策略唯一性校验通过，接收参数：ReqCanStrategyUniquenessVO = {}", uniquenessVO);
        return result.success("校验通过", 0);
    }
}