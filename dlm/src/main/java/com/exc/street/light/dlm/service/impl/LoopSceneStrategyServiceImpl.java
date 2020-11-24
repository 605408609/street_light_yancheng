/**
 * @filename:LoopSceneStrategyServiceImpl 2020-11-07
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.LoopSceneStrategyMapper;
import com.exc.street.light.dlm.service.ControlLoopService;
import com.exc.street.light.dlm.service.LoopSceneActionService;
import com.exc.street.light.dlm.service.LoopSceneStrategyHistoryService;
import com.exc.street.light.dlm.service.LoopSceneStrategyService;
import com.exc.street.light.dlm.utils.ProtocolUtil;
import com.exc.street.light.dlm.utils.SocketClient;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.dlm.ControlLoopDTO;
import com.exc.street.light.resource.dto.dlm.ControlLoopTimerDTO;
import com.exc.street.light.resource.dto.electricity.Timer;
import com.exc.street.light.resource.entity.dlm.ControlLoop;
import com.exc.street.light.resource.entity.dlm.LoopSceneAction;
import com.exc.street.light.resource.entity.dlm.LoopSceneStrategy;
import com.exc.street.light.resource.entity.dlm.LoopSceneStrategyHistory;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.DlmSceneStrategyQuery;
import com.exc.street.light.resource.utils.DataUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.req.DlmReqSceneActionVO;
import com.exc.street.light.resource.vo.req.DlmReqSceneStrategyExecuteVO;
import com.exc.street.light.resource.vo.req.DlmReqSceneStrategyVO;
import com.exc.street.light.resource.vo.resp.DlmRespSceneActionVO;
import com.exc.street.light.resource.vo.resp.DlmRespSceneStrategyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 场景策略(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class LoopSceneStrategyServiceImpl extends ServiceImpl<LoopSceneStrategyMapper, LoopSceneStrategy> implements LoopSceneStrategyService {

    private static final Logger logger = LoggerFactory.getLogger(LoopSceneStrategyServiceImpl.class);

    @Autowired
    private LoopSceneActionService loopSceneActionService;

    @Autowired
    private LogUserService logUserService;

    @Autowired
    private ControlLoopService controlLoopService;

    @Autowired
    private LoopSceneStrategyHistoryService loopSceneStrategyHistoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertSceneStrategy(DlmReqSceneStrategyVO dlmReqSceneStrategyVO, HttpServletRequest request) {
        logger.info("insertSceneStrategy - 新增场景策略 dlmReqSceneStrategyVO=[{}]", dlmReqSceneStrategyVO);
        if (dlmReqSceneStrategyVO.getDlmReqSceneActionVOList() != null && dlmReqSceneStrategyVO.getDlmReqSceneActionVOList().size() > 2) {
            return new Result().error("场景动作不能超过2个");
        }
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        LoopSceneStrategy loopSceneStrategy = new LoopSceneStrategy();
        BeanUtils.copyProperties(dlmReqSceneStrategyVO, loopSceneStrategy);
        loopSceneStrategy.setCreator(userId);
        loopSceneStrategy.setCreateTime(new Date());
        int result = baseMapper.insert(loopSceneStrategy);
        if (result < 1) {
            logger.info("insertSceneStrategy - 新增场景策略失败 dlmReqSceneStrategyVO=[{}]", dlmReqSceneStrategyVO);
            return new Result().error("新增场景策略失败");
        }
        // 新增策略动作
        for (DlmReqSceneActionVO dlmReqSceneActionVO : dlmReqSceneStrategyVO.getDlmReqSceneActionVOList()) {
            dlmReqSceneActionVO.setStrategyId(loopSceneStrategy.getId());
        }
        loopSceneActionService.insertSceneStrategyAction(dlmReqSceneStrategyVO.getDlmReqSceneActionVOList());
        return new Result().success("新增成功");
    }

    @Override
    public Result updateSceneStrategy(DlmReqSceneStrategyVO dlmReqSceneStrategyVO, HttpServletRequest request) {
        logger.info("updateSceneStrategy - 编辑场景策略 dlmReqSceneStrategyVO=[{}]", dlmReqSceneStrategyVO);
        // 删除当前策略（逻辑删除）,跟删除策略的区别就是调用了一次新增策略
        LambdaUpdateWrapper<LoopSceneStrategy> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(LoopSceneStrategy::getIsDelete, 1)
                .eq(LoopSceneStrategy::getId, dlmReqSceneStrategyVO.getId());
        baseMapper.update(null, updateWrapper);

        // 新增策略
        Result result = insertSceneStrategy(dlmReqSceneStrategyVO, request);
        if (result.getCode() != 200) {
            return new Result().success("编辑失败");
        }
        return new Result().success("编辑成功");
    }

    @Override
    public Result getSceneStrategy(Integer strategyId, HttpServletRequest request) {
        logger.info("getSceneStrategy - 场景策略详情 strategyId=[{}]", strategyId);
        LoopSceneStrategy loopSceneStrategy = baseMapper.selectById(strategyId);
        DlmRespSceneStrategyVO sceneStrategyVO = new DlmRespSceneStrategyVO();
        BeanUtils.copyProperties(loopSceneStrategy, sceneStrategyVO);
        User user = logUserService.get(loopSceneStrategy.getCreator());
        sceneStrategyVO.setCreatorName(user.getName());
        // 获取该场景策略下的动作集合
        LambdaQueryWrapper<LoopSceneAction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoopSceneAction::getStrategyId, strategyId);
        List<LoopSceneAction> sceneActionList = loopSceneActionService.list(wrapper);
        List<DlmRespSceneActionVO> sceneActionVOList = new ArrayList<>();
        for (LoopSceneAction loopSceneAction : sceneActionList) {
            DlmRespSceneActionVO respSceneActionVO = new DlmRespSceneActionVO();
            BeanUtils.copyProperties(loopSceneAction, respSceneActionVO);
            if (loopSceneAction.getWeekValue() != null) {
                Integer[] cycleName = DataUtil.getCycleName(loopSceneAction.getWeekValue());
                respSceneActionVO.setCycleTypes(cycleName);
            }
            sceneActionVOList.add(respSceneActionVO);
        }
        sceneStrategyVO.setDlmRespSceneActionVOList(sceneActionVOList);
        Result<DlmRespSceneStrategyVO> result = new Result<>();
        return result.success(sceneStrategyVO);
    }

    @Override
    public Result deleteSceneStrategy(Integer strategyId, HttpServletRequest request) {
        logger.info("deleteSceneStrategy - 删除场景策略 strategyId=[{}]", strategyId);
        LambdaUpdateWrapper<LoopSceneStrategy> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(LoopSceneStrategy::getIsDelete, 1)
                .eq(LoopSceneStrategy::getId, strategyId);
        int result = baseMapper.update(null, updateWrapper);
        if (result < 1) {
            logger.info("deleteSceneStrategy - 删除场景策略失败 strategyId=[{}]", strategyId);
            return new Result().error("删除失败");
        }
        // 将回路中的场景策略id设置为null
        LambdaQueryWrapper<ControlLoop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ControlLoop::getSceneStrategyId, strategyId);
        List<ControlLoop> controlLoopList = controlLoopService.list(wrapper);
        if (controlLoopList != null && controlLoopList.size() > 0) {
            List<Integer> loopIdList = controlLoopList.stream().map(ControlLoop::getId).collect(Collectors.toList());
            LambdaUpdateWrapper<ControlLoop> updateWrapper1 = new LambdaUpdateWrapper<>();
            updateWrapper1.set(ControlLoop::getSceneStrategyId, null)
                    .in(ControlLoop::getId, loopIdList);
            controlLoopService.update(null, updateWrapper1);
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result getPage(DlmSceneStrategyQuery dlmSceneStrategyQuery, HttpServletRequest request) {
        logger.info("getPage - 场景策略分页条件查询 dlmSceneStrategyQuery=[{}]", dlmSceneStrategyQuery);
        Result<Page<DlmRespSceneStrategyVO>> result = new Result<>();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = logUserService.get(userId);
        boolean isAdmin = logUserService.isAdmin(userId);
        if (!isAdmin) {
            dlmSceneStrategyQuery.setAreaId(user.getAreaId());
        }
        // 构建页面返回对象
        Page<DlmRespSceneStrategyVO> resultPage = new Page<>(dlmSceneStrategyQuery.getPageNum(), dlmSceneStrategyQuery.getPageSize());
        // 查询分页数据
        Page<DlmRespSceneStrategyVO> page = new Page<>(dlmSceneStrategyQuery.getPageNum(), dlmSceneStrategyQuery.getPageSize());
        IPage<DlmRespSceneStrategyVO> slRespStrategyPageDate = baseMapper.selectSceneStrategyWithPage(page, dlmSceneStrategyQuery);
        // 分页的数据
        List<DlmRespSceneStrategyVO> strategyPageDateRecords = slRespStrategyPageDate.getRecords();
        // 获取策略id集合
        List<Integer> strategyIdList = strategyPageDateRecords.stream().map(DlmRespSceneStrategyVO::getId).collect(Collectors.toList());
        // 页面返回数据集合
        List<DlmRespSceneStrategyVO> DlmRespSceneStrategyVOList = new ArrayList<>();
        if (strategyIdList.size() > 0) {
            // 查询策略下的动作集合
            LambdaQueryWrapper<LoopSceneAction> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(LoopSceneAction::getStrategyId, strategyIdList);
            List<LoopSceneAction> strategyActionList = loopSceneActionService.list(wrapper);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (DlmRespSceneStrategyVO strategyPageDateRecord : strategyPageDateRecords) {
                // 获取策略下的动作集合
                List<LoopSceneAction> actionCollect = strategyActionList.stream().filter(a -> a.getStrategyId().equals(strategyPageDateRecord.getId())).collect(Collectors.toList());
                String minStartDateString = null;
                Date minStartDate = null;
                try {
                    minStartDate = dateFormat.parse("2999-01-01");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String maxEndDateString = null;
                Date maxEndDate = new Date(1);
                // 用于表示是否需要继续比较结束时间
                int flag = 1;
                // 循环策略动作集合,对比时间获取策略的开始结束时间
                for (LoopSceneAction LoopSceneAction : actionCollect) {
                    // 比较获取最小开始时间
                    String startDateString = LoopSceneAction.getStartDate();
                    if (startDateString == null || "".equals(startDateString)) {
                        continue;
                    }
                    Date startDate = null;
                    try {
                        startDate = dateFormat.parse(startDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int resStart = minStartDate.compareTo(startDate);
                    if (resStart == 1) {
                        minStartDate = startDate;
                        minStartDateString = startDateString;
                    }
                    // 比较获取最大的结束时间
                    if (flag == 1) {
                        String endDateString = LoopSceneAction.getEndDate();
                        if (endDateString == null || "".equals(endDateString)) {
                            continue;
                        }
                        Date endDate = null;
                        try {
                            endDate = dateFormat.parse(endDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (endDate != null) {
                            int resEnd = maxEndDate.compareTo(endDate);
                            if (resEnd == -1) {
                                maxEndDate = endDate;
                                maxEndDateString = endDateString;
                            }
                        } else {
                            maxEndDateString = null;
                            flag = 0;
                        }
                    }
                }
                strategyPageDateRecord.setStartDate(minStartDateString);
                strategyPageDateRecord.setEndDate(maxEndDateString);
                DlmRespSceneStrategyVOList.add(strategyPageDateRecord);
            }
        }
        resultPage.setTotal(slRespStrategyPageDate.getTotal());
        resultPage.setCurrent(slRespStrategyPageDate.getCurrent());
        resultPage.setSize(slRespStrategyPageDate.getSize());
        resultPage.setPages(slRespStrategyPageDate.getPages());
        resultPage.setRecords(DlmRespSceneStrategyVOList);
        return result.success(resultPage);
    }

    @Override
    public Result execute(HttpServletRequest request, DlmReqSceneStrategyExecuteVO dlmReqSceneStrategyExecuteVO) {
        logger.info("execute - 场景策略下发 dlmReqSceneStrategyExecuteVO=[{}]", dlmReqSceneStrategyExecuteVO);
        if (dlmReqSceneStrategyExecuteVO.getStrategyId() == null || dlmReqSceneStrategyExecuteVO.getLoopIdList() == null || dlmReqSceneStrategyExecuteVO.getLoopIdList().isEmpty()) {
            return new Result().error("下发失败，参数缺失");
        }
        // 根据回路id集合查询回路和集控信息集合
        List<Integer> loopIdList = dlmReqSceneStrategyExecuteVO.getLoopIdList();
        List<ControlLoopDTO> controlLoopList = controlLoopService.getControlLoopByIdList(loopIdList);

        // 根据策略id查询动作信息集合
        LambdaQueryWrapper<LoopSceneAction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoopSceneAction::getStrategyId, dlmReqSceneStrategyExecuteVO.getStrategyId());
        List<LoopSceneAction> sceneActionList = loopSceneActionService.list(wrapper);
        // 根据集控id分组
        Map<Integer, List<ControlLoopDTO>> loopMap = controlLoopList.stream().collect(Collectors.groupingBy(ControlLoopDTO::getControlId));
        for (Map.Entry<Integer, List<ControlLoopDTO>> entry : loopMap.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                continue;
            }
            String ip = entry.getValue().get(0).getIp();
            String port = entry.getValue().get(0).getPort();
            String mac = entry.getValue().get(0).getMac();
            // 构建下发对象集合
            List<ControlLoopTimerDTO> dtoList = new ArrayList<>();
            for (LoopSceneAction sceneAction : sceneActionList) {
                for (ControlLoopDTO loopDTO : entry.getValue()) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date startDate = dateFormat.parse(sceneAction.getStartDate());
                        Date endDate = dateFormat.parse(sceneAction.getEndDate());
                        int[] intArr = null;
                        if (sceneAction.getWeekValue() != null) {
                            Integer[] cycleTypes = DataUtil.getCycleName(sceneAction.getWeekValue());
                            // 将日期向前移一天到正常日期
                            List<Integer> cycleTypeList = Arrays.stream(cycleTypes).filter(Objects::nonNull).map(e -> e - 1 <= 0 ? 7 : e - 1).collect(Collectors.toList());
                            intArr = cycleTypeList.stream().mapToInt(Integer::intValue).toArray();
                        }
                        // 构建下发对象
                        ControlLoopTimerDTO dto = new ControlLoopTimerDTO()
                                //回路编号
                                .setLoopNum(Integer.parseInt(loopDTO.getNum()))
                                //动作： true-开 false-关
                                .setIsOpen(sceneAction.getIsOpen() == 1)
                                //选择周期 没有填null
                                .setCycleTypes(intArr)
                                //开始时间 没有传null
                                .setStartDate(startDate)
                                //结束时间 没有传null
                                .setEndDate(endDate)
                                //HH:mm:ss 时间点
                                .setTime(sceneAction.getExecutionTime());
                        // 定时类型
                        if (sceneAction.getOpenModeId() != null) {
                            if (sceneAction.getOpenModeId() == 1) {
                                dto.setType(4);
                            } else if (sceneAction.getOpenModeId() == 2) {
                                dto.setType(5);
                            } else if (sceneAction.getOpenModeId() == 3) {
                                dto.setType(2);
                            } else if (sceneAction.getOpenModeId() == 4) {
                                dto.setType(3);
                            }
                            dto.setMinuteValue(sceneAction.getDeviation());
                        } else {
                            dto.setType(1);
                        }
                        dtoList.add(dto);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            byte[] bytes = ProtocolUtil.setTimer(mac, dtoList);
            //返回结果
            boolean flag = SocketClient.sendData(ip, Integer.parseInt(port), bytes);
            if (!flag) {
                return new Result().error("下发失败");
            }
        }

        // 修改回路表中的场景策略id字段，场景是否启用字段
        LambdaUpdateWrapper<ControlLoop> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ControlLoop::getIsUse, 1)
                .set(ControlLoop::getSceneStrategyId, dlmReqSceneStrategyExecuteVO.getStrategyId())
                .in(ControlLoop::getId, loopIdList);
        controlLoopService.update(null, updateWrapper);

        // 新增回路场景策略历史记录
        List<LoopSceneStrategyHistory> sceneStrategyHistoryList = new ArrayList<>();
        Date date = new Date();
        for (Integer loopId : loopIdList) {
            LoopSceneStrategyHistory sceneStrategyHistory = new LoopSceneStrategyHistory();
            sceneStrategyHistory.setLoopId(loopId);
            sceneStrategyHistory.setStrategyId(dlmReqSceneStrategyExecuteVO.getStrategyId());
            sceneStrategyHistory.setCreateTime(date);
            sceneStrategyHistoryList.add(sceneStrategyHistory);
        }
        loopSceneStrategyHistoryService.saveBatch(sceneStrategyHistoryList);
        return new Result().success("下发成功");
    }
}