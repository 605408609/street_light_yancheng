package com.exc.street.light.electricity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanSceneMapper;
import com.exc.street.light.electricity.service.*;
import com.exc.street.light.electricity.task.DeleteTimingTask;
import com.exc.street.light.electricity.task.SceneTask;
import com.exc.street.light.electricity.util.AnalysisUtil;
import com.exc.street.light.electricity.util.ConstantUtil;
import com.exc.street.light.electricity.util.ProtocolUtil;
import com.exc.street.light.electricity.util.SocketClient;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.electricity.ControlObject;
import com.exc.street.light.resource.dto.electricity.Scene;
import com.exc.street.light.resource.dto.electricity.Timer;
import com.exc.street.light.resource.entity.electricity.*;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.electricity.CanSceneQueryObject;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.vo.electricity.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * 强电场景服务接口实现类
 *
 * @author Linshiwen
 * @date 2018/05/28
 */
@Service
public class CanSceneServiceImpl extends ServiceImpl<CanSceneMapper, CanScene> implements CanSceneService {
    private static final Logger logger = LoggerFactory.getLogger(CanSceneServiceImpl.class);
    @Autowired
    private CanSceneMapper canSceneMapper;
    @Autowired
    private ElectricityNodeService nodeService;
    @Autowired
    private CanDeviceService canDeviceService;
    @Autowired
    private CanControlObjectService canControlObjectService;
    @Autowired
    private CanTimingService canTimingService;
    @Autowired
    private CanSiteSceneService canSiteSceneService;
    @Autowired
    private LogUserService logUserService;


    @Override
    public Result add(int isSend, Scene scene, HttpServletRequest request) {
        if (isSend == 1) {
            String name = scene.getName();
            logger.info("创建新增场景:{} 日志", name);
            //通过节点id查询IP和端口
            Integer nid = scene.getNid();
            ElectricityNodeVO electricityNode = nodeService.getById(nid);
            String ip = electricityNode.getIp();
            int port = electricityNode.getPort();
            //通过can设备id查询mac地址;合广使用此，自研直接获取即可
            //CanDevice canDevice = canDeviceService.getAddressByNid(nid);
            //String address = canDevice.getAddress();
            String address = electricityNode.getMac();
            //获取设置场景协议
            byte[] bytes = ProtocolUtil.setScene(address, scene);
            //发送设置场景协议到网关
            byte[] receiveData = SocketClient.send(ip, port, bytes);
            if (receiveData == null) {
                return new Result().error("socket连接到网关失败或者网关应答超时", electricityNode.getNum());
            }
            logger.info("判断设置场景命令是否执行成功");
            Result result = new Result().error("协议未成功执行");
            byte rtn = AnalysisUtil.getRtn(receiveData);
            if (rtn == ConstantUtil.RET_1) {
                logger.info("判断场景之前是否有定时,有则删除");
                LambdaQueryWrapper<CanTiming> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(CanTiming::getNid, nid);
                List<CanTiming> canTimings = canTimingService.list(wrapper);
                int sn = scene.getSn();
                CanScene canScene1 = canSceneMapper.selectByNameAndNid(name, nid);
                if (canScene1 != null) {
                    Integer sceneId = canScene1.getSceneId();
                    List<CanTiming> canTimings1 = canTimingService.selectBySidAndNid(sceneId, nid);
                    if (canTimings1.size() > 0) {
                        canTimings.removeAll(canTimings1);
                        Timer timer = new Timer();
                        timer.setCanTimings(canTimings);
                        byte[] deleteBytes = ProtocolUtil.deleteTimer(address);
                        byte[] receiveData1 = SocketClient.send(ip, port, deleteBytes);
                        logger.info("判断删除定时命令是否执行成功");
                        byte rtn1 = AnalysisUtil.getRtn(receiveData1);
                        if (rtn1 == ConstantUtil.RET_1) {
                            logger.info("删除成功,再将定时集合重新下发");
                            if (timer.getCanTimings().size() > 0) {
                                byte[] data = ProtocolUtil.setTimer(address, timer);
                                byte[] receiveData2 = SocketClient.send(ip, port, data);
                                logger.info("判断定时集合重新下发是否执行成功");
                                byte rtn2 = AnalysisUtil.getRtn(receiveData2);
                                if (rtn2 != ConstantUtil.RET_1) {
                                    logger.error("协议执行失败");
                                    return result.error("新增失败", electricityNode.getNum());
                                }
                            }
                        } else {
                            logger.error("协议执行失败");
                            return result.error("新增失败", electricityNode.getNum());
                        }
                        canTimingService.deleteBySidAndNid(sceneId, nid);
                    }
                }
                logger.info("协议执行成功,将场景保存到数据库");
                //设置场景表对象属性
                CanScene canScene = new CanScene();
                canScene.setSceneId(sn);
                canScene.setNid(nid);
                canScene.setSiteSceneId(scene.getSiteSceneId());
                canScene.setName(name);
                canScene.setInputTime(new Date());
                logger.info("判断场景是否已编辑过");
                List<ControlObject> controlObjects = scene.getControlObjects();
                if (canScene1 == null) {
                    logger.info("场景未编辑过则新增场景,并新增控制对象");
                    canSceneMapper.insert(canScene);
                    for (ControlObject controlObject : controlObjects) {
                        CanControlObject canControlObject = new CanControlObject();
                        BeanUtils.copyProperties(controlObject, canControlObject);
                        canControlObject.setSid(canScene.getId());
                        canControlObjectService.save(canControlObject);
                    }
                } else {
                    logger.info("场景编辑过则更新场景,并更新控制对象");
                    Integer canScene1Id = canScene1.getId();
                    canScene.setSiteSceneId(canScene1.getSiteSceneId());
                    canScene.setId(canScene1Id);
                    canSceneMapper.updateById(canScene);
                    logger.info("先删除原有的控制对象");
                    canControlObjectService.deleteBySid(canScene1Id);
                    for (ControlObject controlObject : controlObjects) {
                        CanControlObject canControlObject = new CanControlObject();
                        BeanUtils.copyProperties(controlObject, canControlObject);
                        canControlObject.setSid(canScene1Id);
                        canControlObjectService.save(canControlObject);
                    }
                }
            } else {
                logger.error("协议执行失败");
                return result.error("新增失败", electricityNode.getNum());
            }
        } else {
            String name = scene.getName();
            logger.info("创建初始化新增场景:{} 日志", name);
            Integer nid = scene.getNid();
            int sn = scene.getSn();
            CanScene canScene1 = canSceneMapper.selectByNameAndNid(name, nid);
            //设置场景表对象属性
            CanScene canScene = new CanScene();
            canScene.setSceneId(sn);
            canScene.setNid(nid);
            canScene.setSiteSceneId(scene.getSiteSceneId());
            canScene.setName(name);
            canScene.setInputTime(new Date());
            logger.info("判断场景是否已编辑过");
            List<ControlObject> controlObjects = scene.getControlObjects();
            if (canScene1 == null) {
                logger.info("场景未编辑过则新增场景,并新增控制对象");
                canSceneMapper.insert(canScene);
                for (ControlObject controlObject : controlObjects) {
                    CanControlObject canControlObject = new CanControlObject();
                    BeanUtils.copyProperties(controlObject, canControlObject);
                    canControlObject.setSid(canScene.getId());
                    canControlObjectService.save(canControlObject);
                }
            } else {
                logger.info("场景编辑过则更新场景,并更新控制对象");
                Integer canScene1Id = canScene1.getId();
                canScene.setSiteSceneId(canScene1.getSiteSceneId());
                canScene.setId(canScene1Id);
                canSceneMapper.updateById(canScene);
                logger.info("先删除原有的控制对象");
                canControlObjectService.deleteBySid(canScene1Id);
                for (ControlObject controlObject : controlObjects) {
                    CanControlObject canControlObject = new CanControlObject();
                    BeanUtils.copyProperties(controlObject, canControlObject);
                    canControlObject.setSid(canScene1Id);
                    canControlObjectService.save(canControlObject);
                }
            }
        }
        return new Result().success("新增成功");
    }

    @Override
    public CanScene selectBySidAndNid(Integer sid, Integer nid) {
        CanScene canScene = canSceneMapper.selectBySidAndNid(sid, nid);
        return canScene;
    }

    @Override
    public Result query(CanSceneQueryObject qo, HttpServletRequest request) {
        logger.info("判断用户所属分区及是否是超级管理员");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        //权限
        User user = logUserService.get(userId);
        if (user == null) {
            return new Result().error("请先登录");
        }
        boolean admin = logUserService.isAdmin(userId);
        qo.setAreaId(admin ? null : user.getAreaId());
        logger.info("获取列表数据");
        IPage<CanSceneNodeVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<CanSceneNodeVO> pageList = canSceneMapper.getPageList(page, qo);
        return new Result().success(pageList);
    }

    @Override
    public Result listByName(CanSceneQueryObject qo, HttpServletRequest request) {
        logger.info("判断用户所属分区及是否是超级管理员");
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        //权限
        User user = logUserService.get(userId);
        if (user == null) {
            return new Result().error("请先登录");
        }
        boolean admin = logUserService.isAdmin(userId);
        qo.setAreaId(admin ? null : user.getAreaId());
        logger.info("根据场景名称查询");
        List<CanSceneNameVO> recordList = canSceneMapper.getNodeListBySceneName(qo);
        for (CanSceneNameVO vo : recordList) {
            logger.info("设置返回对象属性");
            vo.setTimingNum(vo.getCanTimings().stream().filter(e -> e.getType() == 1)
                    .collect(Collectors.toList()).size());
            vo.setCycleNum(vo.getCanTimings().stream().filter(e -> e.getType() == 2)
                    .collect(Collectors.toList()).size());
        }
        return new Result().success(recordList);
    }

    @Override
    public Result findBySiteId(Integer siteId) {
        Integer[] sceneIds = ConstantUtil.SCENE_IDS;
        List<SceneVO> sceneVOS = new ArrayList<>();
        List<ElectricityNode> electricityNodes = nodeService.findBySiteId(siteId, null);
        for (ElectricityNode electricityNode : electricityNodes) {
            List<Integer> list1 = Arrays.asList(sceneIds);
            list1 = new ArrayList<>(list1);
            SceneVO vo = new SceneVO();
            Integer nid = electricityNode.getId();
            List<Integer> list = canSceneMapper.findByNid(nid);
            list1.removeAll(list);
            vo.setNid(nid);
            vo.setNum(list1.size());
            vo.setTagIds(list1);
            sceneVOS.add(vo);
        }
        return new Result().success(sceneVOS);
    }

    @Override
    public Result addSite(SiteSceneVO siteSceneVO, HttpServletRequest request) {
        String name = siteSceneVO.getName();
        logger.info("创建新增站点场景:{} 日志", siteSceneVO.getName());
        //SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_2);
        //Site site = siteService.findById(siteSceneVO.getSiteId());
        //log.setContent("新增站点场景:" + siteSceneVO.getName() + ",站点名称" + site.getName());
        List<Scene> scenes = siteSceneVO.getScenes();
        logger.info("保存站点场景");
        //todo
        CanSiteScene siteScene = new CanSiteScene();
        siteScene.setName(name);
        siteScene.setCreateTime(new Date());
        siteScene.setSiteId(siteSceneVO.getSiteId());
        siteScene.setState(1);
        canSiteSceneService.save(siteScene);
        logger.info("获取任务集合");
        Collection<SceneTask> tasks = new ArrayList<SceneTask>();
        for (Scene scene : scenes) {
            scene.setSiteSceneId(siteScene.getId());
            SceneTask task = new SceneTask(scene, this, request);
            tasks.add(task);
        }
        //开启多线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("script-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(ConstantUtil.THREADPOOLSIZE_2,
                ConstantUtil.THREADPOOLSIZE_2, 0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), namedThreadFactory);
        logger.info("运行任务");
        SceneDownLoadVO vo = null;
        try {
            List<Future<Result>> futures = executorService.invokeAll(tasks);
            vo = new SceneDownLoadVO();
            int successNum = 0;
            int defaultNum = 0;
            List<String> nums = new ArrayList<>();
            for (Future<Result> future : futures) {
                Result result = future.get();
                logger.info(result.getData() + ":" + result.getMessage());
                if (result.getCode() == Const.CODE_SUCCESS) {
                    successNum += 1;
                } else {
                    nums.add((String) result.getData());
                    defaultNum += 1;
                }
            }
            vo.setSuccessNum(successNum);
            vo.setDefaultNum(defaultNum);
            vo.setNums(nums);
            logger.info("保存站点场景");
            if (vo.getDefaultNum() > 0) {
                siteScene.setState(0);
                canSiteSceneService.updateById(siteScene);
                return new Result().error("节点下载失败", vo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            executorService.shutdown();
        }
        return new Result().success(vo);
    }

    @Override
    public Result check(Integer nid, String name) {
        LambdaQueryWrapper<CanScene> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CanScene::getNid, nid).eq(CanScene::getName, name);
        List<CanScene> canScenes = canSceneMapper.selectList(wrapper);
        if (canScenes.size() > 0) {
            return new Result().error("场景名称已存在:" + name);
        }
        return new Result().success("");
    }

    @Override
    public Result modify(Scene scene, HttpServletRequest request) {
        String name = scene.getName();
        Integer nid = scene.getNid();
        LambdaQueryWrapper<CanScene> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CanScene::getNid, nid).eq(CanScene::getName, name).ne(CanScene::getSceneId, scene.getSn());
        List<CanScene> canScenes = canSceneMapper.selectList(wrapper);
        if (canScenes.size() > 0) {
            return new Result().error("场景名称已存在:" + name);
        }
        logger.info("创建编辑场景:{} 日志", name);
        //SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_2);
        //通过节点id查询IP和端口
        ElectricityNode electricityNode = nodeService.get(nid);
        //log.setContent("编辑场景:" + name + ",节点名称:" + electricityNode.getName());
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        //通过节点id查询mac地址
        String address = electricityNode.getMac();
        //获取设置场景协议
        byte[] bytes = ProtocolUtil.setScene(address, scene);
        //发送设置场景协议到网关
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        if (receiveData == null) {
           /* log.setState(0);
            systemLogService.save(log);*/
            return new Result().error("socket连接到网关失败或者网关应答超时");
        }
        logger.info("判断设置场景命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        Result result = new Result().error("协议未成功执行");
        if (rtn == ConstantUtil.RET_1) {
            logger.info("判断场景之前是否有定时过,有则删除");
            LambdaQueryWrapper<CanTiming> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(CanTiming::getNid, nid);
            List<CanTiming> canTimings = canTimingService.list(wrapper2);
            int sn = scene.getSn();
            CanScene canScene = canSceneMapper.selectBySidAndNid(sn, nid);
            Integer sceneId = canScene.getSceneId();
            List<CanTiming> canTimings1 = canTimingService.selectBySidAndNid(sceneId, nid);
            if (canTimings1.size() > 0) {
                canTimings.removeAll(canTimings1);
                Timer timer = new Timer();
                timer.setCanTimings(canTimings);
                byte[] deleteBytes = ProtocolUtil.deleteTimer(address);
                byte[] receiveData1 = SocketClient.send(ip, port, deleteBytes);
                logger.info("判断删除定时命令是否执行成功");
                byte rtn1 = AnalysisUtil.getRtn(receiveData1);
                if (rtn1 == ConstantUtil.RET_1) {
                    logger.info("删除成功,再将定时集合重新下发");
                    if (timer.getCanTimings().size() > 0) {
                        byte[] data = ProtocolUtil.setTimer(address, timer);
                        byte[] receiveData2 = SocketClient.send(ip, port, data);
                        logger.info("判断定时集合重新下发是否执行成功");
                        byte rtn2 = AnalysisUtil.getRtn(receiveData2);
                        if (rtn2 != ConstantUtil.RET_1) {
                            logger.error("协议执行失败");
                            return result.error("协议执行失败", electricityNode.getNum());
                        }
                    }
                } else {
                    return new Result().error("协议执行失败", electricityNode.getNum());
                }
                canTimingService.deleteBySidAndNid(sceneId, nid);
            }
            logger.info("协议执行成功,将场景保存到数据库");
            //设置场景表对象属性
            List<ControlObject> controlObjects = scene.getControlObjects();
            logger.info("场景编辑过则更新场景,并更新控制对象");
            Integer canSceneId = canScene.getId();
            canScene.setName(name);
            canScene.setInputTime(new Date());
            canSceneMapper.updateById(canScene);
            logger.info("先删除原有的控制对象");
            canControlObjectService.deleteBySid(canSceneId);
            for (ControlObject controlObject : controlObjects) {
                CanControlObject canControlObject = new CanControlObject();
                BeanUtils.copyProperties(controlObject, canControlObject);
                canControlObject.setSid(canSceneId);
                canControlObjectService.save(canControlObject);
            }
        } else {
            logger.error("协议执行失败");
            result.setData(electricityNode.getNum());
            return result.error("协议执行失败", electricityNode.getNum());
        }
        return new Result().success("");
    }

    @Override
    public Result delete(Integer sn, Integer nid, HttpServletRequest request) {
        Scene scene = new Scene();
        scene.setSn(sn);
        scene.setStatus(0);
        scene.setNid(nid);
        ElectricityNodeVO electricityNode = nodeService.getById(nid);
        String ip = electricityNode.getIp();
        int port = electricityNode.getPort();
        //通过nid查询mac地址
        String address = electricityNode.getMac();
        //获取设置场景协议
        byte[] bytes = ProtocolUtil.setScene(address, scene);
        //发送设置场景协议到网关
        byte[] receiveData = SocketClient.send(ip, port, bytes);
        CanScene canScene = canSceneMapper.selectBySidAndNid(sn, nid);
        if (canScene == null) {
            return new Result().error("删除失败,未找到该场景记录");
        }
        if (receiveData == null) {
            logger.error("socket连接到网关失败或者网关应答超时");
            return new Result().error("删除失败,连接到网关失败或者网关应答超时");
        }
        logger.info("判断设置场景命令是否执行成功");
        byte rtn = AnalysisUtil.getRtn(receiveData);
        Result result = new Result().error("删除失败");
        if (rtn == ConstantUtil.RET_1) {
            logger.info("判断场景之前是否有定时过,有则删除");
            LambdaQueryWrapper<CanTiming> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CanTiming::getNid, nid);
            List<CanTiming> canTimings = canTimingService.list(wrapper);
            Integer sceneId = canScene.getSceneId();
            List<CanTiming> canTimings1 = canTimingService.selectBySidAndNid(sceneId, nid);
            if (canTimings1.size() > 0) {
                canTimings.removeAll(canTimings1);
                Timer timer = new Timer();
                timer.setCanTimings(canTimings);
                byte[] deleteBytes = ProtocolUtil.deleteTimer(address);
                byte[] receiveData1 = SocketClient.send(ip, port, deleteBytes);
                logger.info("判断删除定时命令是否执行成功");
                byte rtn1 = AnalysisUtil.getRtn(receiveData1);
                if (rtn1 == ConstantUtil.RET_1) {
                    logger.info("删除成功,再将定时集合重新下发");
                    if (timer.getCanTimings().size() > 0) {
                        byte[] data = ProtocolUtil.setTimer(address, timer);
                        byte[] receiveData2 = SocketClient.send(ip, port, data);
                        logger.info("判断定时集合重新下发是否执行成功");
                        byte rtn2 = AnalysisUtil.getRtn(receiveData2);
                        if (rtn2 != ConstantUtil.RET_1) {
                            logger.error("协议执行失败");
                            return result.error("删除失败", electricityNode.getNum());
                        }
                    }
                } else {
                    logger.error("协议执行失败");
                    return result.error("删除失败", electricityNode.getNum());
                }
                canTimingService.deleteBySidAndNid(sceneId, nid);
            }
            logger.info("协议执行成功,将场景场景从数据库删除");
            canSceneMapper.deleteById(canScene.getId());
        } else {
            logger.error("协议执行失败");
            return result.error("删除失败", electricityNode.getNum());
        }
        return new Result().success("删除成功");
    }

    @Override
    public Result deleteByDateName(Integer cycleType, Integer type, String sceneName, String date, HttpServletRequest request) {
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        //获取用户
        User user = logUserService.get(userId);
        if (user == null) {
            return new Result().error("请先登录");
        }
        boolean admin = logUserService.isAdmin(userId);
        Integer areaId = admin ? null : user.getAreaId();
        List<Object> failList = new ArrayList<Object>();
        List<Object> successList = new ArrayList<Object>();
        List<Integer> elNodeIdList = canSceneMapper.selectByAreaId(areaId);
        logger.info("操作人:{}" + user.getName());
        logger.info("获取任务集合:{}", sceneName);
        Collection<DeleteTimingTask> deleteTimingTasks = new ArrayList<DeleteTimingTask>();
        for (int i = 0; i < elNodeIdList.size(); i++) {
            DeleteTimingTask deleteTimingTask = new DeleteTimingTask(elNodeIdList.get(i), cycleType, type, canTimingService, sceneName);
            deleteTimingTasks.add(deleteTimingTask);
        }
        //开启多线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("script-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(ConstantUtil.THREADPOOLSIZE_2,
                ConstantUtil.THREADPOOLSIZE_2, 0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), namedThreadFactory);
        logger.info("运行任务");
        try {
            List<Future<Result>> futures = executorService.invokeAll(deleteTimingTasks);
            for (Future<Result> future : futures) {
                Result result = future.get();
                logger.info(result.getData() + ":" + result.getMessage());
                if (result.getCode() == Const.CODE_FAILED) {
                    failList.add(result.getData());
                } else {
                    successList.add(result.getData());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            executorService.shutdown();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("failList", failList);
        jsonObject.put("successList", successList);
        return new Result().success(jsonObject);
    }

    @Override
    public Result deleteByNid(Integer nid, HttpServletRequest request) {
        List<CanScene> list = canSceneMapper.selectByNid(nid);
        for (int i = 0; i < list.size(); i++) {
            Scene scene = new Scene();
            int sn = list.get(i).getSceneId();
            scene.setSn(sn);
            scene.setNid(nid);
            ElectricityNode electricityNode = nodeService.get(nid);
            String ip = electricityNode.getIp();
            int port = electricityNode.getPort();
            //通过nid查询mac地址
            String address = electricityNode.getMac();
            //获取设置场景协议
            byte[] bytes = ProtocolUtil.setScene(address, scene);
            //发送设置场景协议到网关
            byte[] receiveData = SocketClient.send(ip, port, bytes);
            CanScene canScene = list.get(i);
            //SystemLog log = systemLogService.getSystemLog(request, BaseConstantUtil.LOG_TYPE_2);
            String name = canScene.getName();
            //log.setContent("删除场景:" + name + ",节点名称:" + electricityNode.getName());
            if (receiveData == null) {
                return new Result().error("socket连接到网关失败或者网关应答超时");
            }
            logger.info("判断设置场景命令是否执行成功");
            byte rtn = AnalysisUtil.getRtn(receiveData);
            Result result = new Result().error("协议未成功执行");
            if (rtn == ConstantUtil.RET_1) {
                logger.info("判断场景之前是否有定时过,有则删除");
                LambdaQueryWrapper<CanTiming> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(CanTiming::getNid, nid);
                List<CanTiming> canTimings = canTimingService.list(wrapper);
                Integer sceneId = canScene.getSceneId();
                List<CanTiming> canTimings1 = canTimingService.selectBySidAndNid(sceneId, nid);
                if (canTimings1.size() > 0) {
                    canTimings.removeAll(canTimings1);
                    Timer timer = new Timer();
                    timer.setCanTimings(canTimings);
                    byte[] deleteBytes = ProtocolUtil.deleteTimer(address);
                    byte[] receiveData1 = SocketClient.send(ip, port, deleteBytes);
                    logger.info("判断删除定时命令是否执行成功");
                    byte rtn1 = AnalysisUtil.getRtn(receiveData1);
                    if (rtn1 == ConstantUtil.RET_1) {
                        logger.info("删除成功,再将定时集合重新下发");
                        if (timer.getCanTimings().size() > 0) {
                            byte[] data = ProtocolUtil.setTimer(address, timer);
                            byte[] receiveData2 = SocketClient.send(ip, port, data);
                            logger.info("判断定时集合重新下发是否执行成功");
                            byte rtn2 = AnalysisUtil.getRtn(receiveData2);
                            if (rtn2 != ConstantUtil.RET_1) {
                                logger.error("协议执行失败");
                                return result.error("协议执行失败", electricityNode.getNum());
                            }
                        }
                    } else {
                        logger.error("协议执行失败");
                        return result.error("协议执行失败", electricityNode.getNum());
                    }
                    canTimingService.deleteBySidAndNid(sceneId, nid);
                }
                logger.info("协议执行成功,将场景场景从数据库删除");
                canSceneMapper.deleteById(canScene.getId());
            } else {
                logger.error("协议执行失败:" + ProtocolUtil.getRet(rtn));
                return result.error("协议执行失败", electricityNode.getNum());
            }
        }
        return new Result().success("协议执行成功");
    }

}
