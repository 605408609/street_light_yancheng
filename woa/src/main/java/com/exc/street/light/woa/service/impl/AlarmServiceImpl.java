/**
 * @filename:AlarmServiceImpl 2020-03-28
 * @project woa  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.woa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.occ.AhPlay;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.entity.woa.Alarm;
import com.exc.street.light.resource.entity.woa.AlarmType;
import com.exc.street.light.resource.entity.woa.OrderInfo;
import com.exc.street.light.resource.qo.QueryObject;
import com.exc.street.light.resource.qo.WoaAlarmQuery;
import com.exc.street.light.resource.utils.*;
import com.exc.street.light.resource.vo.OccAhPlayVO;
import com.exc.street.light.resource.vo.TimeVO;
import com.exc.street.light.resource.vo.WoaAlarmAreaAnalyseVO;
import com.exc.street.light.resource.vo.req.WoaReqAlarmTypeAnalyseVO;
import com.exc.street.light.resource.vo.resp.*;
import com.exc.street.light.woa.config.parameter.HttpOccApi;
import com.exc.street.light.woa.mapper.AlarmMapper;
import com.exc.street.light.woa.service.AlarmService;
import com.exc.street.light.woa.service.AlarmTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {
    private static final Logger logger = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    private AlarmTypeService alarmTypeService;
    @Autowired
    private HttpOccApi httpOccApi;

    @Autowired
    private LogUserService userService;

    @Autowired
    private LogUserService logUserService;

    @Override
    public Result analysis(HttpServletRequest request) {
        logger.info("首页运维分析数据");
        // 获取所有未处理完成的告警
        LambdaQueryWrapper<Alarm> wrapper = new LambdaQueryWrapper();
        //wrapper.eq(Alarm::getStatus, BaseConstantUtil.ALARM_STATUS_UNTREATED).or().eq(Alarm::getStatus, BaseConstantUtil.ALARM_STATUS_IN_PROCESSING);
        wrapper.and(iteme -> iteme.eq(Alarm::getStatus, BaseConstantUtil.ALARM_STATUS_UNTREATED).or().eq(Alarm::getStatus, BaseConstantUtil.ALARM_STATUS_IN_PROCESSING));
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        if(userId!=1){
            User user = logUserService.get(userId);
            Integer areaId = user.getAreaId();
            List<Integer> lampPostIdList = areaLampPostIdList(areaId);
            if(lampPostIdList!=null&&lampPostIdList.size()>0){
                wrapper.in(Alarm::getLampPostId,lampPostIdList);
            }else {
                wrapper.eq(Alarm::getLampPostId,-1);
            }
        }
        List<Alarm> alarmList = this.list(wrapper);
        // 得到告警类型id集合
        Set<Integer> alarmTypeIdCollect = alarmList.stream().map(Alarm::getTypeId).collect(Collectors.toSet());
        // 获取告警类型集合
        List<AlarmType> alarmTypeList = new ArrayList<>();
        if (alarmTypeIdCollect != null && alarmTypeIdCollect.size() > 0) {
            LambdaQueryWrapper<AlarmType> typeWrapper = new LambdaQueryWrapper();
            //typeWrapper.in(AlarmType::getId, alarmTypeIdCollect);
            alarmTypeList = alarmTypeService.list(typeWrapper);
        }
        // 构建返回对象数组
        List<WoaRespAlarmTypeVO> woaRespAlarmTypeVOList = new ArrayList<>();
        for (AlarmType alarmType : alarmTypeList) {
            // 构建返回对象
            WoaRespAlarmTypeVO woaRespAlarmTypeVO = new WoaRespAlarmTypeVO();
            woaRespAlarmTypeVO.setAlarmTypeId(alarmType.getId());
            woaRespAlarmTypeVO.setAlarmTypeName(alarmType.getName());
            // 获取对应告警类型的告警集合
            List<Alarm> collect = alarmList.stream().filter(a -> a.getTypeId().equals(alarmType.getId())).collect(Collectors.toList());
            if(collect!=null&&collect.size()>0){
                woaRespAlarmTypeVO.setAlarmNumber(collect.size());
            }else {
                woaRespAlarmTypeVO.setAlarmNumber(0);
            }
            woaRespAlarmTypeVOList.add(woaRespAlarmTypeVO);
        }
        Result result = new Result();
        return result.success(woaRespAlarmTypeVOList);
    }

    @Override
    public Result queryAlarm(WoaAlarmQuery woaAlarmQuery, HttpServletRequest httpServletRequest) {
        logger.info("分页条件查询，接收参数：{}", woaAlarmQuery);
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            woaAlarmQuery.setAreaId(user.getAreaId());
        }
        IPage<WoaRespAlarmVO> iPage = new Page<WoaRespAlarmVO>(woaAlarmQuery.getPageNum(), woaAlarmQuery.getPageSize());
        IPage<WoaRespAlarmVO> woaRespAlarmVOList = this.baseMapper.query(iPage, woaAlarmQuery);
        Result result = new Result();
        return result.success(woaRespAlarmVOList);
    }
    @Override
    public Result queryNews(WoaAlarmQuery woaAlarmQuery, HttpServletRequest httpServletRequest) {
        logger.info("分页条件查询，接收参数：{}", woaAlarmQuery);
        IPage<WoaRespAlarmVO> iPage = new Page<WoaRespAlarmVO>(woaAlarmQuery.getPageNum(), woaAlarmQuery.getPageSize());
        IPage<WoaRespAlarmVO> woaRespAlarmVOList = this.baseMapper.queryNews(iPage, woaAlarmQuery);
        Result result = new Result();
        return result.success(woaRespAlarmVOList);
    }

    @Override
    public Result batchDelete(String ids, HttpServletRequest request) {
        logger.info("批量删除告警，接收参数：{}", ids);
        List<Integer> idListFromString = StringConversionUtil.getIdListFromString(ids);
        this.removeByIds(idListFromString);
        Result result = new Result();
        return result.success("批量删除成功");
    }

    @Override
    public void updateOpenOrder(List<Integer> alarmIdList, Integer id, Integer status) {
        logger.info("修改告警关联工单id,接收参数：alarmIdList={}，id={}，status={}", alarmIdList, id, status);
        this.baseMapper.updateOpenOrder(alarmIdList, id, status);
    }

    @Override
    public Result areaAnalyseApp(QueryObject queryObject, HttpServletRequest httpServletRequest) {
        Result result = areaAnalyse(1, queryObject, httpServletRequest);
        List<WoaRespAlarmAreaAnalyseVO> resultList = new ArrayList<>();
        if(result!=null){
            Integer stage = queryObject.getStage();
            Date date = new Date();
            if(stage==1){
                //当日
                date = DateUtil.getDayBeginOrEnd(0, true);
            }else if(stage==2){
                //本周
                date = DateUtil.getWeekBeginOrEnd(0,true);
            }else if(stage==3){
                //本月
                date = DateUtil.getMonthTime(-1,true);
            }
            IPage<WoaRespAlarmAreaAnalyseVO> iPage = (IPage<WoaRespAlarmAreaAnalyseVO>)result.getData();
            List<WoaRespAlarmAreaAnalyseVO> woaRespAlarmAreaAnalyseVOList = iPage.getRecords();
            for (WoaRespAlarmAreaAnalyseVO woaRespAlarmAreaAnalyseVO : woaRespAlarmAreaAnalyseVOList) {
                if(woaRespAlarmAreaAnalyseVO.getLatelyAlarmTime()!=null){
                    if(woaRespAlarmAreaAnalyseVO.getLatelyAlarmTime().getTime()>date.getTime()){
                        resultList.add(woaRespAlarmAreaAnalyseVO);
                    }else {
                        woaRespAlarmAreaAnalyseVO.setAlarmTotal(0);
                        woaRespAlarmAreaAnalyseVO.setUntreatedAlarmNumber(0);
                        resultList.add(woaRespAlarmAreaAnalyseVO);
                    }
                }else {
                    woaRespAlarmAreaAnalyseVO.setAlarmTotal(0);
                    woaRespAlarmAreaAnalyseVO.setUntreatedAlarmNumber(0);
                    resultList.add(woaRespAlarmAreaAnalyseVO);
                }

            }
        }
        return new Result().success(resultList);
    }

    @Override
    public Result areaAnalyse(Integer type, QueryObject queryObject, HttpServletRequest httpServletRequest) {
        logger.info("告警区域分析");
        // 返回对象
        List<WoaRespAlarmAreaAnalyseVO> returnVOList = new ArrayList<>();
        // 空时间参数对象，防止排序报null
        List<WoaRespAlarmAreaAnalyseVO> returnVONullList = new ArrayList<>();
        // 所有数据
        IPage<WoaAlarmAreaAnalyseVO> iPage = new Page<>(queryObject.getPageNum(), queryObject.getPageSize());
        List<WoaAlarmAreaAnalyseVO> list = this.baseMapper.areaAnalyse();
        // 获取区域去重集合
        List<Integer> areaIdList = new ArrayList<>();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        if(userId!=1){
            User user = logUserService.get(userId);
            areaIdList.add(user.getAreaId());
        }else {
            // 1为分页，2为不分页
            if (type == 1) {
                areaIdList = this.baseMapper.areaAnalysePage(iPage, queryObject);
            } else {
                areaIdList = list.stream().map(WoaAlarmAreaAnalyseVO::getAreaId).distinct().collect(Collectors.toList());
            }
        }
        logger.info("areaIdList={}", areaIdList);
        // 循环区域id集合
        for (Integer areaId : areaIdList) {
            // 得到每个区域数据
            List<WoaAlarmAreaAnalyseVO> areaCollect = list.stream().filter(a -> areaId.equals(a.getAreaId())).collect(Collectors.toList());
            // 根据街道id去重
            List<WoaAlarmAreaAnalyseVO> streetCollectNotNull = areaCollect.stream().filter(a -> a.getStreetId() != null).collect(Collectors.toList());
            List<WoaAlarmAreaAnalyseVO> streetCollect = streetCollectNotNull.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WoaAlarmAreaAnalyseVO::getStreetId))), ArrayList::new));
            // 得到街道名称拼接
            String streetNames = "";
            for (int i = 0; i < streetCollect.size(); i++) {
                if (i == 0) {
                    streetNames += streetCollect.get(i).getStreetName();
                } else {
                    streetNames += "，" + streetCollect.get(i).getStreetName();
                }
            }
            // 根据站点id去重
            List<WoaAlarmAreaAnalyseVO> siteCollectNotNull = areaCollect.stream().filter(a -> a.getSiteId() != null).collect(Collectors.toList());
            List<WoaAlarmAreaAnalyseVO> siteCollect = siteCollectNotNull.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WoaAlarmAreaAnalyseVO::getSiteId))), ArrayList::new));
            // 得到街道名称拼接
            String siteNames = "";
            for (int i = 0; i < siteCollect.size(); i++) {
                if (i == 0) {
                    siteNames += siteCollect.get(i).getSiteName();
                } else {
                    siteNames += "，" + siteCollect.get(i).getSiteName();
                }
            }
            // 根据路灯id去重
            List<WoaAlarmAreaAnalyseVO> lampPostCollectNotNull = areaCollect.stream().filter(a -> a.getLampPostId() != null).collect(Collectors.toList());
            List<WoaAlarmAreaAnalyseVO> lampPostCollect = lampPostCollectNotNull.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WoaAlarmAreaAnalyseVO::getLampPostId))), ArrayList::new));
            Integer lampPostNumber = lampPostCollect.size();
            // 根据告警id去重
            List<WoaAlarmAreaAnalyseVO> alarmCollectNotNull = areaCollect.stream().filter(a -> a.getAlarmId() != null).collect(Collectors.toList());
            List<WoaAlarmAreaAnalyseVO> alarmCollect = alarmCollectNotNull.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WoaAlarmAreaAnalyseVO::getAlarmId))), ArrayList::new));
            Integer alarmTotal = alarmCollect.size();
            // 根据告警id去重数据，得到状态等于1的未处理告警数据
            List<WoaAlarmAreaAnalyseVO> untreatedAlarmCollectNotNull = alarmCollect.stream().filter(a -> a.getAlarmStatus() != null).collect(Collectors.toList());
            List<WoaAlarmAreaAnalyseVO> untreatedAlarmCollect = untreatedAlarmCollectNotNull.stream().filter(a -> 1 == a.getAlarmStatus()).collect(Collectors.toList());
            Integer untreatedAlarmNumber = untreatedAlarmCollect.size();
            if (type != 1) {
                if (untreatedAlarmNumber == 0) {
                    continue;
                }
            }
            // 根据用户id去重
            List<WoaAlarmAreaAnalyseVO> userCollectNotNull = areaCollect.stream().filter(a -> a.getUserId() != null).collect(Collectors.toList());
            List<WoaAlarmAreaAnalyseVO> userCollect = userCollectNotNull.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WoaAlarmAreaAnalyseVO::getUserId))), ArrayList::new));
            // 得到维护人名称拼接
            String maintainers = "";
            for (int i = 0; i < userCollect.size(); i++) {
                if (i == 0) {
                    maintainers += userCollect.get(i).getUserName();
                } else {
                    maintainers += "，" + userCollect.get(i).getUserName();
                }
            }
            Date latelyAlarmTime = null;
            for (int i = 0; i < areaCollect.size(); i++) {
                if (areaCollect.get(i).getAlarmCreateTime() != null) {
                    latelyAlarmTime = areaCollect.get(i).getAlarmCreateTime();
                    break;
                }
            }
            WoaRespAlarmAreaAnalyseVO returnVO = new WoaRespAlarmAreaAnalyseVO();
            returnVO.setAreaName(areaCollect.get(0).getAreaName());
            returnVO.setStreetNames(streetNames);
            returnVO.setSiteNames(siteNames);
            returnVO.setLampPostNumber(lampPostNumber);
            returnVO.setAlarmTotal(alarmTotal);
            returnVO.setUntreatedAlarmNumber(untreatedAlarmNumber);
            returnVO.setMaintainers(maintainers);
            returnVO.setLatelyAlarmTime(latelyAlarmTime);
            if(latelyAlarmTime == null){
                returnVONullList.add(returnVO);
            }else {
                returnVOList.add(returnVO);
            }
        }
        // 倒叙排序
        if (returnVOList != null && returnVOList.size() > 0) {
            returnVOList.sort(Comparator.comparing(WoaRespAlarmAreaAnalyseVO::getLatelyAlarmTime).reversed());
        }
        returnVOList.addAll(returnVONullList);
        Result result = new Result();
        if (type == 1) {
            IPage<WoaRespAlarmAreaAnalyseVO> resultPage = new Page<>(queryObject.getPageNum(), queryObject.getPageSize());
            resultPage.setRecords(returnVOList);
            resultPage.setTotal(iPage.getTotal());
            resultPage.setSize(iPage.getSize());
            resultPage.setCurrent(iPage.getCurrent());
            resultPage.setPages(iPage.getPages());
            return result.success(resultPage);
        } else {
            return result.success(returnVOList);
        }

    }

    @Override
    public Result typeAnalyse(WoaReqAlarmTypeAnalyseVO alarmTypeAnalyseVO, HttpServletRequest httpServletRequest) {
        logger.info("工单总览数据，接收参数：{}", alarmTypeAnalyseVO);
        // 匹配DateUtil.nearlyWeek()中的对应值
        if (alarmTypeAnalyseVO.getStage() == 1) {
            alarmTypeAnalyseVO.setStage(4);
        } else if (alarmTypeAnalyseVO.getStage() == 2) {
            alarmTypeAnalyseVO.setStage(5);
        } else if (alarmTypeAnalyseVO.getStage() == 3) {
            alarmTypeAnalyseVO.setStage(6);
        }
        // 格式化日期，获取开始结束日期
        if ((alarmTypeAnalyseVO.getStartTime() != null && !"".equals(alarmTypeAnalyseVO.getStartTime()))) {
            alarmTypeAnalyseVO.setStartTime(alarmTypeAnalyseVO.getStartTime().substring(0, 10) + " 00:00:00");
        }
        if ((alarmTypeAnalyseVO.getEndTime() != null && !"".equals(alarmTypeAnalyseVO.getEndTime()))) {
            alarmTypeAnalyseVO.setEndTime(alarmTypeAnalyseVO.getEndTime().substring(0, 10) + " 23:59:59");
        }
        if ((alarmTypeAnalyseVO.getStartTime() == null || "".equals(alarmTypeAnalyseVO.getStartTime()))
                && (alarmTypeAnalyseVO.getEndTime() == null || "".equals(alarmTypeAnalyseVO.getEndTime()))) {
            if (alarmTypeAnalyseVO.getStage() != null) {
                TimeVO timeVO = DateUtil.nearlyWeek(alarmTypeAnalyseVO.getStage());
                alarmTypeAnalyseVO.setStartTime(timeVO.getStartTimeString());
                alarmTypeAnalyseVO.setEndTime(timeVO.getEndTimeString());
            }
        }
        // 所有数据
        WoaReqAlarmTypeAnalyseVO nullTimeVO = new WoaReqAlarmTypeAnalyseVO();
        List<WoaRespAlarmTypeAnalyseVO> allTotal = this.baseMapper.typeAnalyse(nullTimeVO);
        // 当前查询时间下数据
        List<WoaRespAlarmTypeAnalyseVO> alarmTypeAlarmList = this.baseMapper.typeAnalyse(alarmTypeAnalyseVO);
        // 有过数据的故障类型集合
        // 根据街道id去重
        List<WoaRespAlarmTypeAnalyseVO> streetCollectNotNull = allTotal.stream().filter(a -> a.getAlarmTypeId() != null).collect(Collectors.toList());
        List<WoaRespAlarmTypeAnalyseVO> alarmTypeList = streetCollectNotNull.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(WoaRespAlarmTypeAnalyseVO::getAlarmTypeId))), ArrayList::new));
        // 返回对象集合
        List<WoaRespAlarmTypeAnalyseVO> woaRespAlarmTypeAnalyseVOList = new ArrayList<>();
        for (WoaRespAlarmTypeAnalyseVO alarmType : alarmTypeList) {
            WoaRespAlarmTypeAnalyseVO woaRespAlarmTypeAnalyseVO = new WoaRespAlarmTypeAnalyseVO();
            // 当前查询时间下当前告警类型数据
            List<WoaRespAlarmTypeAnalyseVO> collect = alarmTypeAlarmList.stream().filter(a -> alarmType.getAlarmTypeId().equals(a.getAlarmTypeId())).collect(Collectors.toList());
            // 所有数据当前告警类型数据
            List<WoaRespAlarmTypeAnalyseVO> collectAll = allTotal.stream().filter(a -> alarmType.getAlarmTypeId().equals(a.getAlarmTypeId())).collect(Collectors.toList());
            woaRespAlarmTypeAnalyseVO.setAlarmTypeName(alarmType.getAlarmTypeName());
            woaRespAlarmTypeAnalyseVO.setAlarmNumber(collect.size());
            woaRespAlarmTypeAnalyseVO.setAlarmTotal(collectAll.size());
            woaRespAlarmTypeAnalyseVO.setLatelyAlarmTime(collectAll.get(0).getLatelyAlarmTime());
            // 计算占比
            if (alarmTypeAlarmList.size() == 0) {
                woaRespAlarmTypeAnalyseVO.setProportion(0D);
            } else {
                BigDecimal bg = new BigDecimal((collect.size() * 100) / alarmTypeAlarmList.size());
                double doubleValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                woaRespAlarmTypeAnalyseVO.setProportion(doubleValue);
            }
            woaRespAlarmTypeAnalyseVOList.add(woaRespAlarmTypeAnalyseVO);
        }
        // 倒叙排序
        woaRespAlarmTypeAnalyseVOList.sort(Comparator.comparing(WoaRespAlarmTypeAnalyseVO::getLatelyAlarmTime).reversed());
        Result result = new Result();
        return result.success(woaRespAlarmTypeAnalyseVOList);
    }

    @Override
    public List<WoaRespOrderAlarmVO> getWoaRespOrderAlarmVO(Integer id) {
        return this.baseMapper.getWoaRespOrderAlarmVO(id);
    }

	@Override
	public Result newsStatus(WoaAlarmQuery woaAlarmQuery, HttpServletRequest httpServletRequest) {
		Result result = new Result();
		
		List<WoaRespAlarmVO>  listWoaRespAlarmVO= woaAlarmQuery.getListNews();
		
		List<WoaRespAlarmVO>  listAlarm = listWoaRespAlarmVO.stream().filter(WoaRespAlarmVO -> WoaRespAlarmVO.getServiceId() == ConstantUtil.SERVER_ID_ALARM).collect(Collectors.toList());
		List<Alarm> listAlarmObj = new ArrayList<>(); 
		for (WoaRespAlarmVO woaRespAlarmVO : listAlarm) {
			Alarm alarm = new Alarm();
			BeanUtils.copyProperties(woaRespAlarmVO, alarm);
			listAlarmObj.add(alarm);
		}
//		listAlarmObj = null;
		if (listAlarmObj.size() > 0 ) {
			
			boolean bool= this.updateBatchById(listAlarmObj);
			if (!bool) {
				return result.error("修改故障告警数据失败");
			}
		}
		
		
		List<WoaRespAlarmVO>  listOCC = listWoaRespAlarmVO.stream().filter(WoaRespAlarmVO -> WoaRespAlarmVO.getServiceId() == ConstantUtil.SERVER_ID_OCC).collect(Collectors.toList());
		List<AhPlay> listAhPlay = new ArrayList<>(); 
		for (WoaRespAlarmVO woaRespAlarmVO : listOCC) {
			AhPlay ahPlay = new AhPlay();
			BeanUtils.copyProperties(woaRespAlarmVO, ahPlay);
			listAhPlay.add(ahPlay);
		}
		
		if (listAhPlay.size() > 0 ) {
			OccAhPlayVO occAhPlayVO= new OccAhPlayVO();
			occAhPlayVO.setListAhPlay(listAhPlay);
			String json = JacksonUtil.toJsonString(occAhPlayVO);
			String url = httpOccApi.getUrl()+httpOccApi.getNewsStatus();
			String bool= HttpClientUtil.sendHttpPostJson(url, json);
		}
		
		// TODO Auto-generated method stub
		return result.success("成功");
	}
	@Override
	public Result newsAll(HttpServletRequest httpServletRequest) {
		Result result = new Result();
		
		List<Alarm> listAlarm = baseMapper.selectList(null);
		for (Alarm alarm : listAlarm) {
			alarm.setHaveRead(1);//设置状态为已读，1
		}
		if (listAlarm.size() > 0 ) {
			this.updateBatchById(listAlarm);
		}
		
		OccAhPlayVO occAhPlayVO= new OccAhPlayVO();
		String json = JacksonUtil.toJsonString(occAhPlayVO);
		String url = httpOccApi.getUrl()+httpOccApi.getNewsAll();
		String bool= HttpClientUtil.sendHttpPostJson(url, json);
		
		// TODO Auto-generated method stub
		return result.success("成功");
	}

    @Override
    public List<Integer> areaLampPostIdList(Integer areaId) {
        return baseMapper.areaLampPostIdList(areaId);
    }

    @Override
    public Result updateHaveRead(Integer alarmId, HttpServletRequest request) {
        Alarm alarm = new Alarm();
        alarm.setId(alarmId);
        alarm.setHaveRead(1);
        baseMapper.updateById(alarm);
        return new Result().success("标记成功");
    }


}