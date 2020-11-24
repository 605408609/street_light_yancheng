package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanTimingMapper;
import com.exc.street.light.electricity.service.*;
import com.exc.street.light.electricity.task.CanTimerTask;
import com.exc.street.light.electricity.task.TimerClearTask;
import com.exc.street.light.electricity.util.*;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.dto.electricity.Timer;
import com.exc.street.light.resource.dto.electricity.TimingParameter;
import com.exc.street.light.resource.entity.electricity.CanDevice;
import com.exc.street.light.resource.entity.electricity.CanScene;
import com.exc.street.light.resource.entity.electricity.CanTiming;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import com.exc.street.light.resource.vo.electricity.*;
import com.exc.street.light.resource.vo.req.electricity.ReqElectricitySceneVO;
import com.exc.street.light.resource.vo.req.electricity.ReqElectricityScriptVO;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.*;


/**
 * @author Linshiwen
 * @date 2018/05/31
 */
@Slf4j
@Service
public class CanTimingServiceImpl extends ServiceImpl<CanTimingMapper, CanTiming> implements CanTimingService {
    private final Logger logger = LoggerFactory.getLogger(CanTimingServiceImpl.class);


    @Autowired
    private CanTimingMapper canTimingMapper;
    @Autowired
    private CanDeviceService canDeviceService;
    @Autowired
    private ElectricityNodeService nodeService;
    @Autowired
    private CanSceneService canSceneService;
    @Autowired
    private CanControlObjectService canControlObjectService;
    /*@Autowired
    private SystemLogService systemLogService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private PartitionService partitionService;*/

    @Override
    public Result add(TimerVO timerVO, HttpServletRequest request) {
        logger.info("创建新增场景定时日志");
        logger.info("获取定时对象集合并进行遍历");
        Integer nid = timerVO.getNid();
        ElectricityNodeVO electricityNode = nodeService.getById(nid);
        logger.info("通过节点id查询IP和端口");
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        logger.info("根据节点id查询网关mac地址");
        CanDevice canDevice = canDeviceService.getAddressByNid(nid);
        Result result = new Result();
        String name = electricityNode.getName();
        result.setData(name);
        if (canDevice != null) {
//            String address = canDevice.getAddress();
            String address = electricityNode.getMac();
            List<TimingParameter> timingParameters = timerVO.getTimingParameters();
            Timer timer = new Timer();
            Integer sn;
            int type = 1;
            List<CanTiming> canTimings = new ArrayList<>();
            LambdaQueryWrapper<CanTiming> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CanTiming::getNid, nid);
            List<CanTiming> canTimingList = canTimingMapper.selectList(wrapper);
            for (TimingParameter timingParameter : timingParameters) {
                logger.info("用UUID作为定时器的名称");
                String taskId = DataUtil.getTaskId();
                CanTiming canTiming = new CanTiming();
                BeanUtils.copyProperties(timingParameter, canTiming);
                Date beginDate = timingParameter.getBeginDate();
                Date endDate = timingParameter.getEndDate();
                sn = timingParameter.getTagId();
                canTiming.setName(taskId);
                canTiming.setTagId(sn);
                timingParameter.setTagId(sn);
                logger.info("判断是按时执行还是周期执行");
                type = timingParameter.getType();
                canTiming.setType(type);
                logger.info("查询之前已有的按时执行或者周期执行的定时器");
                if (type == ConstantUtil.TIMING_TYPE_1) {
                    logger.info("设置按时间段执行定时");
                    canTiming.setStartDate(beginDate);
                    canTiming.setEndDate(endDate);
                    String[] times = timingParameter.getTime().split(":");
                    byte[] time = new byte[3];
                    time[0] = HexUtil.getByte(Integer.parseInt(times[0]));
                    time[1] = HexUtil.getByte(Integer.parseInt(times[1]));
                    time[2] = HexUtil.getByte(Integer.parseInt(times[2]));
                    timingParameter.setTime(HexUtil.bytesTohex(time));
                    canTiming.setTime(HexUtil.bytesTohex(time));
                    int[] cycleTypes = timingParameter.getCycleTypes();
                    int cycleType = ProtocolUtil.getCycleType(cycleTypes, 1, 0);
                    canTiming.setCycleType(cycleType);
                } else if (type == ConstantUtil.TIMING_TYPE_2) {
                    logger.info("设置周期执行定时");
                    canTiming.setStartDate(beginDate);
                    canTiming.setEndDate(endDate);
                    String[] times = timingParameter.getTime().split(":");
                    byte[] time = new byte[3];
                    time[0] = HexUtil.getByte(Integer.parseInt(times[0]));
                    time[1] = HexUtil.getByte(Integer.parseInt(times[1]));
                    time[2] = HexUtil.getByte(Integer.parseInt(times[2]));
                    timingParameter.setTime(HexUtil.bytesTohex(time));
                    canTiming.setTime(HexUtil.bytesTohex(time));
                    int[] cycleTypes = timingParameter.getCycleTypes();
                    int cycleType = ProtocolUtil.getCycleType(cycleTypes, 0, 1);
                    canTiming.setCycleType(cycleType);
                } else if (type == ConstantUtil.TIMING_TYPE_3) {
                    //当类型为3时，立即执行场景，自研新增
                    int tagId = timingParameter.getTagId();
                    List<ControlObject> controlObjects = canControlObjectService.selectBySceneIdAndNid(nid, tagId);
                    //获取设置场景协议
                    byte[] bytes = ProtocolUtil.setControlNow(address, controlObjects);
                    //发送设置场景协议到网关
                    byte[] receiveData = SocketClient.send(ip, port, bytes);
                    if (receiveData == null) {
                        return result.error("新增失败,socket连接到网关失败或者网关应答超时");
                    }
                    logger.info("判断定时命令是否执行成功");
                    byte rtn = AnalysisUtil.getRtn(receiveData);
                    if (rtn == ConstantUtil.RET_1) {
                        return result.success("新增成功");
                    } else {
                        logger.error("协议执行失败:" + ProtocolUtil.getRet(rtn));
                        return result.error("新增失败,协议未成功执行");
                    }
                } else if (type == ConstantUtil.TIMING_TYPE_4) {
                    logger.info("设置每天定时定时");
                    String[] times = timingParameter.getTime().split(":");
                    byte[] time = new byte[3];
                    time[0] = HexUtil.getByte(Integer.parseInt(times[0]));
                    time[1] = HexUtil.getByte(Integer.parseInt(times[1]));
                    time[2] = HexUtil.getByte(Integer.parseInt(times[2]));
                    timingParameter.setTime(HexUtil.bytesTohex(time));
                    canTiming.setTime(HexUtil.bytesTohex(time));
                    int[] cycleTypes = timingParameter.getCycleTypes();
                    int cycleType = ProtocolUtil.getCycleType(cycleTypes, 1, 0);
                    canTiming.setCycleType(cycleType);
                } else if (type == ConstantUtil.TIMING_TYPE_5) {
                    //日出之前
                    logger.info("设置日出之前定时");
                    canTiming.setCycleType(0);
                    canTiming.setMinuteValue(timingParameter.getMinute());
                } else if (type == ConstantUtil.TIMING_TYPE_6) {
                    //日出之后
                    canTiming.setCycleType(0);
                    canTiming.setMinuteValue(timingParameter.getMinute());
                } else if (type == ConstantUtil.TIMING_TYPE_7) {
                    //日落之前
                    canTiming.setCycleType(0);
                    canTiming.setMinuteValue(timingParameter.getMinute());
                } else if (type == ConstantUtil.TIMING_TYPE_8) {
                    //日落之后
                    canTiming.setCycleType(0);
                    canTiming.setMinuteValue(timingParameter.getMinute());
                }
                canTimings.add(canTiming);
            }
//            ReentrantLock
//            StampedLock
            logger.info("如果之前已存在按时执行或者周期执行的定时器,则添加到定时数量中");
            canTimings.addAll(canTimingList);
            timer.setCanTimings(canTimings);
            if (canTimings.size() > 16) {
                return result.error("定时数量最大为16个,当前为:" + canTimings.size() + "已超过最大数量");
            }
            byte[] bytes = ProtocolUtil.setTimer(address, timer);
            logger.info("定时数据:{}", HexUtil.bytesTohex(bytes));
            byte[] receiveData = SocketClient.send(ip, port, bytes);
            if (receiveData == null) {
                return result.error("新增失败,socket连接到网关失败或者网关应答超时");
            }
            logger.info("判断定时命令是否执行成功");
            byte rtn = AnalysisUtil.getRtn(receiveData);
            canTimings.removeAll(canTimingList);
            for (CanTiming canTiming : canTimings) {
                if (rtn == ConstantUtil.RET_1) {
                    logger.info("执行成功,将定时器保存到数据库");
                    canTiming.setNid(nid);
                    canTiming.setInputTime(new Date());
                    logger.info("新增定时器");
                    canTimingMapper.insert(canTiming);
                } else {
                    logger.error("协议执行失败:" + ProtocolUtil.getRet(rtn));
                    return result.error("新增失败,协议未成功执行");
                }
            }
            return result.success("新增成功");
        } else {
            return result.error("新增失败,节点:" + name + ",没有设备");
        }
    }

    @Override
    public List<CanTiming> selectBySidAndNid(Integer sid, Integer nid) {
        return canTimingMapper.selectBySidAndNid(sid, nid);
    }

    @Override
    public void deleteBySidAndNid(Integer sid, Integer nid) {
        canTimingMapper.deleteBySidAndNid(sid, nid);
    }

    @Override
    public int countTimingNumByNid(Integer nid) {
        return canTimingMapper.countTimingNumByNid(nid);
    }

    @Override
    public int countCycleNumByNid(Integer nid) {
        return canTimingMapper.countCycleNumByNid(nid);
    }

    @Override
    public Result patchAdd(TimerPatchVO timerPatchVO, HttpServletRequest request) {
        logger.info("创建新增场景定时日志");
        logger.info("获取任务集合");
        List<TimerVO> timerVOS = timerPatchVO.getTimerVOS();
        Collection<CanTimerTask> tasks = new ArrayList<>();
        for (TimerVO timerVO : timerVOS) {
            CanTimerTask task = new CanTimerTask(timerVO, this, request);
            tasks.add(task);
        }
        //开启多线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("channel-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(ConstantUtil.THREADPOOLSIZE_2,
                ConstantUtil.THREADPOOLSIZE_2, 0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(), namedThreadFactory);
        logger.info("运行任务");
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
                return new Result().error("节点执行失败", vo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            executorService.shutdown();
        }
        return new Result().success(vo);
    }

    @Override
    public List<CanTimingVO> findByNid(Integer nid) {
        logger.info("根据节点id查询定时器");
        List<CanTiming> canTimings = canTimingMapper.findByNid(nid);
        logger.info("设置定时列表对象");
        List<CanTimingVO> views = new ArrayList<>();
        if (canTimings != null && canTimings.size() > 0) {
            for (CanTiming canTiming : canTimings) {
                CanTimingVO view = new CanTimingVO();
                view.setId(canTiming.getId());
                Integer tagId = canTiming.getTagId();
                view.setSid(tagId);
                view.setSceneName(canTiming.getCanScene().getName());
                int type = canTiming.getType();
                view.setType(type);
                view.setIsExecute(canTiming.getIsExecute());
                Date bedginDate = canTiming.getStartDate();
                Date endDate = canTiming.getEndDate();
                logger.info("查询结束时间");
                if (type == ConstantUtil.TIMING_TYPE_1) {
                    logger.info("按时间执行,则设置日期格式");
                    view.setBeginDate(TimeUtil.format2Date(bedginDate));
                    view.setEndDate(TimeUtil.format2Date(endDate));
                    String[] times = canTiming.getTime().split(" ");
                    String time = TimeUtil.getHexTime(times);
                    view.setTime(time);
                } else if (type == ConstantUtil.TIMING_TYPE_2) {
                    logger.info("周期执行");
                    String[] times = canTiming.getTime().split(" ");
                    String time = TimeUtil.getHexTime(times);
                    view.setTime(time);
                    logger.info("获取周期名称");
                    int cycleType = canTiming.getCycleType();
                    Integer[] cycleNames = DataUtil.getCycleName(cycleType);
                    view.setCycleNames(cycleNames);
                } else if (type == ConstantUtil.TIMING_TYPE_4) {
                    logger.info("一直执行");
                    String[] times = canTiming.getTime().split(" ");
                    String time = TimeUtil.getHexTime(times);
                    view.setTime(time);
                } else if (type == ConstantUtil.TIMING_TYPE_5 || type == ConstantUtil.TIMING_TYPE_6 || type == ConstantUtil.TIMING_TYPE_7 || type == ConstantUtil.TIMING_TYPE_8) {
                    logger.info("日出日落执行");
                    view.setMinute(canTiming.getMinuteValue());
                }
                views.add(view);
            }
        }
        return views;
    }

    @Override
    public Result delete2ById(Integer id, HttpServletRequest request) {
        logger.info("创建删除场景定时日志");
        CanTiming canTiming = canTimingMapper.selectById(id);
        if (canTiming == null) {
            return new Result().error("未找到该定时记录");
        }
        Integer nid = canTiming.getNid();
        CanScene canScene = canSceneService.selectBySidAndNid(canTiming.getTagId(), nid);
        ElectricityNodeVO electricityNode = nodeService.getById(nid);
        logger.info("删除强电场景定时:{},节点名称:{}", canScene.getName(), electricityNode.getName());
        logger.info("查询该节点所有的定时");
        LambdaQueryWrapper<CanTiming> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CanTiming::getNid, canTiming.getNid());
        List<CanTiming> canTimings = canTimingMapper.selectList(wrapper);
        logger.info("将要删除的定时从集合中删除");
        canTimings.remove(canTiming);
        logger.info("设置定时对象");
        Timer timer = new Timer();
        timer.setCanTimings(canTimings);
        CanDevice canDevice = canDeviceService.getAddressByNid(nid);
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        String address = electricityNode.getMac();
        byte[] bytes = ProtocolUtil.deleteTimer(address);
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        if (receiveData == null) {
            return new Result().error("socket连接到网关失败或者网关应答超时");
        }
        logger.info("判断删除定时命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.RET_1) {
            logger.info("删除成功,再将定时集合重新下发");
            if (timer.getCanTimings().size() > 0) {
                byte[] data = ProtocolUtil.setTimer(address, timer);
                byte[] receiveData1 = SocketClient.send(ip, port, data);
                logger.info("判断定时集合重新下发是否执行成功");
                byte rtn1 = AnalysisUtil.getRtn(receiveData1);
                if (rtn1 != ConstantUtil.RET_1) {
                    logger.error("协议执行失败");
                    return new Result().error("删除失败,协议未成功执行");
                }
            }
        } else {
            logger.error("协议执行失败:" + ProtocolUtil.getRet(rtn));
            return new Result().error("删除失败,协议未成功执行");
        }
        canTimingMapper.deleteById(id);
        return new Result().success("删除成功");
    }

    @Override
    public Result addSite(ReqElectricityScriptVO vo, HttpServletRequest request) {
        List<ReqElectricitySceneVO> sceneVOS = vo.getSceneVOS();
        Integer scheduleModeId = vo.getScheduleModeId();
        String startDate = vo.getStartDate();
        String stopDate = vo.getStopDate();
        TimerPatchVO patchVO = new TimerPatchVO();
        List<TimerVO> timerVOS;
        for (ReqElectricitySceneVO sceneVO : sceneVOS) {
            //根据站点场景id查询节点场景
            LambdaQueryWrapper<CanScene> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CanScene::getSiteSceneId, sceneVO.getSiteSceneId());
            List<CanScene> canScenes = canSceneService.list(wrapper);
            timerVOS = new ArrayList<>(canScenes.size());
            String time1 = sceneVO.getTime();
            Date start = TimeUtil.getDate(startDate, time1);
            Date stop = TimeUtil.getDate(stopDate, time1);
            List<Date> datesBetweenTwoDate = TimeUtil.getDatesBetweenTwoDate(start, stop);
            for (CanScene canScene : canScenes) {
                TimerVO timerVO = new TimerVO();
                Integer nid = canScene.getNid();
                timerVO.setNid(nid);
                List<TimingParameter> timingParameters = new ArrayList<>(10);

                if (scheduleModeId == ConstantUtil.SCHEDULE_MODE_2) {
                    for (Date date : datesBetweenTwoDate) {
                        TimingParameter timingParameter = new TimingParameter();
                        timingParameter.setTagId(canScene.getSceneId());
                        timingParameter.setNid(nid);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        byte hours = HexUtil.getByte(calendar.get(Calendar.HOUR_OF_DAY));
                        byte minute = HexUtil.getByte(calendar.get(Calendar.MINUTE));
                        byte second = HexUtil.getByte(calendar.get(Calendar.SECOND));
                        byte[] time = new byte[3];
                        time[0] = hours;
                        time[1] = minute;
                        time[2] = second;
                        timingParameter.setTime(HexUtil.bytesTohex(time));
                        timingParameter.setType(1);
                        timingParameter.setBeginDate(date);
                        timingParameters.add(timingParameter);
                    }
                } else {
                    logger.info("周期执行,获取当前日期在当前的秒数");
                    TimingParameter timingParameter = new TimingParameter();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start);
                    byte hours = HexUtil.getByte(calendar.get(Calendar.HOUR_OF_DAY));
                    byte minute = HexUtil.getByte(calendar.get(Calendar.MINUTE));
                    byte second = HexUtil.getByte(calendar.get(Calendar.SECOND));
                    byte[] time = new byte[3];
                    time[0] = hours;
                    time[1] = minute;
                    time[2] = second;
                    timingParameter.setTime(HexUtil.bytesTohex(time));
                    timingParameter.setTagId(canScene.getSceneId());
                    timingParameter.setCycleTypes(vo.getCycleTypes());
                    timingParameter.setType(2);
                    timingParameter.setBeginDate(start);
                    timingParameters.add(timingParameter);
                }
                timerVO.setTimingParameters(timingParameters);
                timerVOS.add(timerVO);
            }
            patchVO.setTimerVOS(timerVOS);
        }

        Result result = this.patchAdd(patchVO, request);
        return result;
    }

    @Override
    public Result clear(Integer nid, HttpServletRequest request) {
        // SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_2);
        ElectricityNode electricityNode = nodeService.get(nid);
        //log.setContent("清空节点定时" + ",节点名称:" + electricityNode.getName());
        CanDevice canDevice = canDeviceService.getAddressByNid(nid);
        Result result = new Result();
        result.setData(electricityNode.getName());
        if (canDevice == null) {
            return result.error("该节点没有强电网关");
        }
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        String address = electricityNode.getMac();
        if (address == null) {
            return result.error("节点address为null");
        }
        byte[] bytes = ProtocolUtil.deleteTimer(address);
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        if (receiveData == null) {
            return result.error("socket连接到网关失败或者网关应答超时");
        }
        logger.info("判断清空定时命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.RET_1) {
            canTimingMapper.deleteByNid(nid);
            //systemLogService.save(log);
        } else {
            //log.setState(0);
            //systemLogService.save(log);
            logger.error("协议执行失败:" + ProtocolUtil.getRet(rtn));
            return result.error("协议未成功执行");
        }
        return result.success("");
    }

    @Override
    public Result clearSite(Integer siteId, HttpServletRequest request) {
        /*Site site = siteService.findById(siteId);
        SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_2);
        log.setContent("清空站点定时" + ",站点名称:" + site.getName());
        List<ElectricityNode> nodes = nodeService.findBySiteId(siteId, null);
        Collection<TimerClearTask> tasks = new ArrayList<>();
        for (ElectricityNode node : nodes) {
            TimerClearTask task = new TimerClearTask(node.getId(), this, request);
            tasks.add(task);
        }
        //开启多线程
        Result result = getResult(tasks);
        if (result.getCode() == ResultCode.FAIL.code) {
            log.setState(0);
        }
        systemLogService.save(log);
        return result;*/
        //todo
        return null;
    }

    @Override
    public Result clearPartition(Integer partitionId, HttpServletRequest request) {
        /*Partition partition = partitionService.findById(partitionId);
        SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_2);
        log.setContent("清空分区定时" + ",分区名称:" + partition.getName());
        List<ElectricityNode> nodes = nodeService.findByPid(partitionId, null);
        Collection<TimerClearTask> tasks = new ArrayList<>();
        for (ElectricityNode node : nodes) {
            TimerClearTask task = new TimerClearTask(node.getId(), this, request);
            tasks.add(task);
        }
        //开启多线程
        Result result = getResult(tasks);
        if (result.getCode() == ResultCode.FAIL.code) {
            log.setState(0);
        }
        systemLogService.save(log);
        return result;*/
        //todo
        return null;
    }

    @Override
    public Result clearAll(HttpServletRequest request) {
        //SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_2);
        //log.setContent("清空所有定时");
        List<ElectricityNode> nodes = nodeService.list();
        Collection<TimerClearTask> tasks = new ArrayList<>();
        for (ElectricityNode node : nodes) {
            TimerClearTask task = new TimerClearTask(node.getId(), this, request);
            tasks.add(task);
        }
        //开启多线程
        Result result = getResult(tasks);
        if (result.getCode() == Const.CODE_FAILED) {
            //log.setState(0);
        }
        //systemLogService.save(log);
        return result;
    }

    @Override
    public Result deleteTiming(Integer nid, Integer cycleType, Integer type, String sceneName) {
        Result result = new Result().success("");
        List<CanTiming> canTimingList = canTimingMapper.selectByTypeAndName(nid, cycleType, type, sceneName);
        ElectricityNode electricityNode = nodeService.get(nid);
        logger.info("查询该节点所有的定时");
        LambdaQueryWrapper<CanTiming> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CanTiming::getNid, nid);
        List<CanTiming> canTimings = canTimingMapper.selectList(wrapper);
        logger.info("原定时数量{}", canTimings.size());
        logger.info("需要删除的定时数量{}", canTimingList.size());
        logger.info("将要删除的定时从集合中删除");
        if (canTimings != null && canTimings.size() > 0) {
            for (CanTiming canTiming : canTimingList) {
                canTimings.remove(canTiming);
            }
        } else {
            result.setData(electricityNode.getName());
            return result;
        }
        logger.info("删除后定时数量{}", canTimings.size());
        logger.info("设置定时对象");
        Timer timer = new Timer();
        timer.setCanTimings(canTimings);
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        String address = electricityNode.getMac();
        byte[] bytes = ProtocolUtil.deleteTimer(address);
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        if (receiveData == null) {
            return result.error("协议执行失败", electricityNode.getName());
        }
        logger.info("判断删除定时命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.CONTROL_IDENTIFIER2_10) {
            logger.info("删除成功,再将定时集合重新下发");
            if (timer.getCanTimings().size() > 0) {
                byte[] data = ProtocolUtil.setTimer(address, timer);
                byte[] receiveData1 = SocketClient.send(ip, port, data);
                logger.info("判断定时集合重新下发是否执行成功");
                byte rtn1 = AnalysisUtil.getRtn(receiveData1);
                if (rtn1 != ConstantUtil.CONTROL_IDENTIFIER2_10) {
                    logger.error("协议执行失败");
                    return result.error("协议执行失败", electricityNode.getName());
                }
            }
        } else {
            logger.error("协议执行失败");
            return result.error("协议执行失败", electricityNode.getName());
        }
        for (CanTiming canTiming : canTimingList) {
            canTimingMapper.deleteById(canTiming.getId());
        }
        result.setData(electricityNode.getName());
        return result;
    }

    @Override
    public Result deleteTimingByPid(Integer pid, Integer nid) {
        Result result = new Result().success("");
        List<CanTiming> canTimingList = canTimingMapper.selectByPidAndNid(pid, nid);
        ElectricityNode electricityNode = nodeService.get(nid);
        logger.info("查询该节点所有的定时");
        LambdaQueryWrapper<CanTiming> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CanTiming::getNid, nid);
        List<CanTiming> canTimings = canTimingMapper.selectList(wrapper);
        logger.info("原定时数量{}", canTimings.size());
        logger.info("需要删除的定时数量{}", canTimingList.size());
        logger.info("将要删除的定时从集合中删除");
        if (canTimings != null && canTimings.size() > 0) {
            for (CanTiming canTiming : canTimingList) {
                canTimings.remove(canTiming);
            }
        } else {
            result.setData(electricityNode.getName());
            return result;
        }
        logger.info("删除后定时数量{}", canTimings.size());
        logger.info("设置定时对象");
        Timer timer = new Timer();
        timer.setCanTimings(canTimings);
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        String address = electricityNode.getMac();
        byte[] bytes = ProtocolUtil.deleteTimer(address);
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        if (receiveData == null) {
            return result.error("协议执行失败", electricityNode.getName());
        }
        logger.info("判断删除定时命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.CONTROL_IDENTIFIER2_10) {
            logger.info("删除成功,再将定时集合重新下发");
            if (timer.getCanTimings().size() > 0) {
                byte[] data = ProtocolUtil.setTimer(address, timer);
                byte[] receiveData1 = SocketClient.send(ip, port, data);
                logger.info("判断定时集合重新下发是否执行成功");
                byte rtn1 = AnalysisUtil.getRtn(receiveData1);
                if (rtn1 != ConstantUtil.CONTROL_IDENTIFIER2_10) {
                    logger.error("协议执行失败");
                    return result.error("协议执行失败", electricityNode.getName());
                }
            }
        } else {
            logger.error("协议执行失败");
            return result.error("协议执行失败", electricityNode.getName());
        }
        for (CanTiming canTiming : canTimingList) {
            canTimingMapper.deleteById(canTiming.getId());
        }
        result.setData(electricityNode.getName());
        return result;
    }

    @Override
    public Result changeExecuteState(Integer nid, Integer id, Integer isExecute) {
        Result result = new Result().success("");
        CanTiming canTiming = canTimingMapper.selectById(id);
        ElectricityNode electricityNode = nodeService.get(nid);
        logger.info("查询该节点所有的定时");
        LambdaQueryWrapper<CanTiming> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CanTiming::getNid, nid);
        List<CanTiming> canTimings = canTimingMapper.selectList(wrapper);
        logger.info("原定时数量{}", canTimings.size());
        logger.info("需要修改的定时{}", canTiming);
        if (canTimings != null && canTimings.size() > 0) {
            for (int i = 0; i < canTimings.size(); ++i) {
                if (canTiming.getId().equals(canTimings.get(i).getId())) {
                    canTimings.get(i).setIsExecute(isExecute);
                }
            }
        } else {
            return result.error("", "未设置定时");
        }
        logger.info("设置定时对象");
        Timer timer = new Timer();
        timer.setCanTimings(canTimings);
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        String address = electricityNode.getMac();
        byte[] bytes = ProtocolUtil.setTimer(address, timer);
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        if (receiveData == null) {
            return result.error("", electricityNode.getName());
        }
        logger.info("判断定时修改命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.RET_1) {
            logger.info("执行成功");
            canTiming.setIsExecute(isExecute);
            canTimingMapper.updateById(canTiming);
        } else {
            logger.error("协议执行失败");
            return result.error("", electricityNode.getName());
        }
        return result.success("", electricityNode.getName());
    }

    @Override
    public Result changeExecuteStates(Integer nid, List<Integer> pids, Integer isExecute) {
        Result result = new Result().success("");
        List<CanTiming> changeCanTimings = new ArrayList<>();
        for (Integer pid : pids) {
            List<CanTiming> canTimings = canTimingMapper.selectByPidAndNid(pid, nid);
            for (int i = 0; i < canTimings.size(); ++i) {
                CanTiming canTiming = canTimings.get(i);
                canTiming.setIsExecute(isExecute);
                changeCanTimings.add(canTiming);
            }
        }
        ElectricityNode electricityNode = nodeService.get(nid);
        logger.info("查询该节点所有的定时");
        LambdaQueryWrapper<CanTiming> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CanTiming::getNid, nid);
        List<CanTiming> canTimings = canTimingMapper.selectList(wrapper);
        logger.info("原定时数量{}", canTimings.size());
        logger.info("需要修改的定时{}", changeCanTimings.size());
        if (canTimings.size() > 0) {
            for (int i = 0; i < canTimings.size(); ++i) {
                for (int j = 0; j < changeCanTimings.size(); ++j) {
                    if (canTimings.get(i).getId().equals(changeCanTimings.get(j).getId())) {
                        canTimings.get(i).setIsExecute(isExecute);
                    }
                }
            }
        } else {
            result.setData("未设置定时");
            return result;
        }
        logger.info("设置定时对象");
        Timer timer = new Timer();
        timer.setCanTimings(canTimings);
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        String address = electricityNode.getMac();
        byte[] bytes = ProtocolUtil.setTimer(address, timer);
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        if (receiveData == null) {
            return result.error("", electricityNode.getName());
        }
        logger.info("判断定时修改命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        if (rtn == ConstantUtil.RET_1) {
            logger.info("执行成功");
            for (int i = 0; i < changeCanTimings.size(); ++i) {
                CanTiming canTiming1 = changeCanTimings.get(i);
                canTiming1.setIsExecute(isExecute);
                canTimingMapper.updateById(canTiming1);
            }
        } else {
            logger.error("协议执行失败");
            return result.error("协议执行失败", electricityNode.getName());
        }
        return result.success("", electricityNode.getName());
    }

    @Override
    public List<CanTiming> selectByPid(Integer pid) {
        return canTimingMapper.selectByPid(pid);
    }

    @Override
    public List<CanTiming> selectByPidAndNid(Integer pid, Integer nid) {
        return canTimingMapper.selectByPidAndNid(pid, nid);
    }

    @Override
    public List<CanTiming> deleteByTimingId(Integer id) {
        return null;
    }

    private Result getResult(Collection<TimerClearTask> tasks) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("clearTimer-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(ConstantUtil.THREADPOOLSIZE_2,
                ConstantUtil.THREADPOOLSIZE_2, 0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), namedThreadFactory);
        logger.info("运行任务");
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
                return new Result().error("节点执行失败", vo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            executorService.shutdown();
        }
        return new Result().success(vo);
    }
}
