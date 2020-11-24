/**
 * @filename:ScreenProgramServiceImpl 2020-04-02
 * @project ir  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.ir.config.parameter.HttpDlmApi;
import com.exc.street.light.ir.config.parameter.HttpUaApi;
import com.exc.street.light.ir.config.parameter.MaterialApi;
import com.exc.street.light.ir.config.parameter.ScreenApi;
import com.exc.street.light.ir.mapper.ScreenProgramMapper;
import com.exc.street.light.ir.service.*;
import com.exc.street.light.ir.utils.ScreenControlUtil;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.ir.*;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.IrProgramQuery;
import com.exc.street.light.resource.utils.DataUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.IrMaterialVO;
import com.exc.street.light.resource.vo.IrProgramMaterialVO;
import com.exc.street.light.resource.vo.IrProgramVO;
import com.exc.street.light.resource.vo.IrReqVerifyProgramVo;
import com.exc.street.light.resource.vo.req.IrReqScreenMaterialVO;
import com.exc.street.light.resource.vo.req.IrReqScreenProgramMaterialVO;
import com.exc.street.light.resource.vo.req.IrReqScreenProgramVO;
import com.exc.street.light.resource.vo.req.IrReqSendProgramVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(rollbackFor = Exception.class)
public class ScreenProgramServiceImpl extends ServiceImpl<ScreenProgramMapper, ScreenProgram> implements ScreenProgramService {
    private static final Logger logger = LoggerFactory.getLogger(ScreenProgramServiceImpl.class);

    @Autowired
    private ScreenMaterialService screenMaterialService;
    @Autowired
    private ScreenProgramMaterialService screenProgramMaterialService;
    @Autowired
    private ScreenDeviceService screenDeviceService;
    @Autowired
    private ScreenPlayService screenPlayService;
    @Autowired
    private ScreenPlayDeviceService screenPlayDeviceService;
    @Autowired
    private ScreenPlayStrategyService screenPlayStrategyService;
    @Autowired
    private HttpUaApi httpUaApi;
    @Autowired
    private HttpDlmApi httpDlmApi;
    @Autowired
    private ScreenApi screenApi;
    @Autowired
    private MaterialApi materialApi;
    @Autowired
    private LogUserService userService;

    @Autowired
    private ScreenProgramService screenProgramService;

    @Override
    public Result add(IrReqScreenProgramVO screenProgram, HttpServletRequest httpServletRequest) {
        //获取登录用户信息
        logger.info("添加节目接受数据：{}", screenProgram);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        if (userId == null) {
            return result.error("添加失败，token已过期");
        }
        if (screenProgram.getName() == null || "".equals(screenProgram.getName().trim())) {
            return result.error("节目名称不能为空");
        }
        LambdaQueryWrapper<ScreenProgram> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScreenProgram::getName, screenProgram.getName());
        ScreenProgram selectByName = this.getOne(wrapper);
        if (selectByName != null) {
            return result.error("节目名称已存在");
        }
        //获取传入的素材参数集合
        List<IrReqScreenProgramMaterialVO> programMaterialArrayList = screenProgram.getProgramMaterialArrayList();
        if (programMaterialArrayList == null || programMaterialArrayList.size() == 0) {
            return result.error("节目的素材列表不能为空");
        }
        //根据素材集合获取素材id集合
        List<Integer> materialsIdList = programMaterialArrayList.stream().map(IrReqScreenProgramMaterialVO::getMaterialId).collect(Collectors.toList());
        //查询素材集合
        Collection<ScreenMaterial> materials = screenMaterialService.listByIds(materialsIdList);
        logger.info("添加节目对应素材实体集合：{}", materials);
        //计算所有素材集合的文件总大小，文本没有大小则为0
        Integer totalSize = 0;
        for (ScreenMaterial screenMaterial : materials) {
            totalSize += screenMaterial.getSize();
        }
        screenProgram.setTotalSize(totalSize);
        //设置参数
        screenProgram.setCreator(userId);
        screenProgram.setCreateTime(new Date());
        //添加节目对象至数据库并返回主键id
        ScreenProgram screenProgramSave = new ScreenProgram();
        BeanUtils.copyProperties(screenProgram, screenProgramSave);
        this.save(screenProgramSave);
        //构建节目素材关联对象
        Integer playTime = 0;
        List<ScreenProgramMaterial> programMaterialList = new ArrayList<ScreenProgramMaterial>();
        for (int i = 0; i < programMaterialArrayList.size(); i++) {
            ScreenProgramMaterial programMaterial = new ScreenProgramMaterial();
            programMaterial.setMaterialId(programMaterialArrayList.get(i).getMaterialId());
            programMaterial.setProgramId(screenProgramSave.getId());
            //根据不同的类型给默认值
            if (programMaterialArrayList.get(i).getTimeSpan() != null) {
                programMaterial.setTimeSpan(programMaterialArrayList.get(i).getTimeSpan());
            }
            programMaterial.setPlayTime(playTime);
            programMaterial.setCreateTime(new Date());
            playTime += programMaterialArrayList.get(i).getTimeSpan();
            programMaterialList.add(programMaterial);
        }
        logger.info("节目素材关系集合：{}", programMaterialList);
        screenProgramMaterialService.saveBatch(programMaterialList);
        return result.success("节目" + screenProgram.getName().trim() + "添加成功");
    }

    @Override
    public Result get(Integer id, HttpServletRequest httpServletRequest) {
        logger.info("获取节目详情");
        Result result = new Result();
        // 构造返回对象
        IrReqScreenProgramVO irReqScreenProgramVO = new IrReqScreenProgramVO();
        List<IrReqScreenProgramMaterialVO> programMaterialArrayList = new ArrayList<>();
        // 获取节目详情
        ScreenProgram byId = this.getById(id);
        if (byId == null) {
            result.error("节目不存在");
        }
        BeanUtils.copyProperties(byId, irReqScreenProgramVO);
        Map<String, String> map = new HashMap<>();
        map.put("token", httpServletRequest.getHeader("token"));
        // 获取节目创建人名称
        User user = userService.get(irReqScreenProgramVO.getCreator());
        if (user != null) {
            irReqScreenProgramVO.setCreatorName(user.getName());
        }
        // 获取节目素材关系列表
        LambdaQueryWrapper<ScreenProgramMaterial> wrapperScreenProgramMaterial = new LambdaQueryWrapper();
        wrapperScreenProgramMaterial.eq(ScreenProgramMaterial::getProgramId, id);
        List<ScreenProgramMaterial> screenProgramMaterialList = screenProgramMaterialService.list(wrapperScreenProgramMaterial);
        if (screenProgramMaterialList != null && screenProgramMaterialList.size() > 0) {
            // 获取素材id列表
            List<Integer> materialIdList = screenProgramMaterialList.stream().map(ScreenProgramMaterial::getMaterialId).collect(Collectors.toList());
            // 获取素材集合
            LambdaQueryWrapper<ScreenMaterial> wrapperScreenMaterial = new LambdaQueryWrapper<>();
            wrapperScreenMaterial.in(ScreenMaterial::getId, materialIdList);
            List<ScreenMaterial> screenMaterialList = screenMaterialService.list(wrapperScreenMaterial);
            if (screenMaterialList == null || screenMaterialList.size() == 0) {
                return result.success(irReqScreenProgramVO);
            }
            // 素材创建人id集合
            List<Integer> materialCreatorIdList = screenMaterialList.stream().map(ScreenMaterial::getCreator).collect(Collectors.toList());
            // 获取素材创建人名称集合
            List<User> materialUsers = new ArrayList<>();
            for (Integer creatorId : materialCreatorIdList) {
                User materialUser = userService.get(creatorId);
                materialUsers.add(materialUser);
            }
            // 遍历节目素材中间列表
            for (ScreenProgramMaterial screenProgramMaterial : screenProgramMaterialList) {
                IrReqScreenProgramMaterialVO irReqScreenProgramMaterialVO = new IrReqScreenProgramMaterialVO();
                BeanUtils.copyProperties(screenProgramMaterial, irReqScreenProgramMaterialVO);
                // 获取对应的素材对象
                List<ScreenMaterial> collect = screenMaterialList.stream().filter(a -> screenProgramMaterial.getMaterialId().equals(a.getId())).collect(Collectors.toList());
                if (collect == null || collect.size() == 0) {
                    continue;
                }
                ScreenMaterial screenMaterial = collect.get(0);
                IrReqScreenMaterialVO irReqScreenMaterialVO = new IrReqScreenMaterialVO();
                BeanUtils.copyProperties(screenMaterial, irReqScreenMaterialVO);
                List<User> materialUser = materialUsers.stream().filter(a -> irReqScreenMaterialVO.getCreator().equals(a.getId())).collect(Collectors.toList());
                if (materialUser != null && materialUser.size() > 0) {
                    irReqScreenMaterialVO.setCreatorName(materialUser.get(0).getName());
                }
                // 构造返回对象
                irReqScreenProgramMaterialVO.setMaterial(irReqScreenMaterialVO);
                programMaterialArrayList.add(irReqScreenProgramMaterialVO);
            }
        }
        irReqScreenProgramVO.setProgramMaterialArrayList(programMaterialArrayList);
        return result.success(irReqScreenProgramVO);
    }

    @Override
    public Result queryList(IrProgramQuery irProgramQuery, HttpServletRequest httpServletRequest) {
        logger.info("条件分页查询节目列表：{}", irProgramQuery);
        Result result = new Result();
        List<IrReqScreenProgramVO> irReqScreenProgramVOList = new ArrayList<>();

        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            irProgramQuery.setAreaId(user.getAreaId());
        }

        //管理员节目审核
        //当前区域下 管理员ID集合
          List<Integer> managerIdList = userService.getManagerIdList(httpServletRequest);
        IPage<ScreenProgram> page = new Page<ScreenProgram>(irProgramQuery.getPageNum(), irProgramQuery.getPageSize());
        IPage<ScreenProgram> programIPage = this.baseMapper.query(page, irProgramQuery);
        List<ScreenProgram> records = programIPage.getRecords();
                if (programIPage != null && records != null && !records.isEmpty()) {
                    for (ScreenProgram record : records) {
                        //循环节目列表 获取其
                        Result screenProgramResult = this.get(record.getId(), httpServletRequest);
                        IrReqScreenProgramVO irReqScreenProgramVO = (IrReqScreenProgramVO) screenProgramResult.getData();

                        //是这个区域下的用户,节目的创建人 不是该用户创建的  这样才能审核节目,不能审自己的.
                        boolean isCanVerify = managerIdList != null && managerIdList.contains(userId) && !irReqScreenProgramVO.getCreator().equals(userId);
                        //在IrReqScreenProgramVO类里面加了个IsCanVerify字段(是否可以审核).
                        irReqScreenProgramVO.setIsCanVerify(isCanVerify ? 1 : 0);
                        irReqScreenProgramVOList.add(irReqScreenProgramVO);
                    }
                }

        //令时间区间为空串时为空 判断某字符串是否为空，为空的标准是 str==null 或 str.length()==0
        if (StringUtils.isEmpty(irProgramQuery.getStartTime())) {
            irProgramQuery.setStartTime(null);
        }
        if (StringUtils.isEmpty(irProgramQuery.getEndTime())) {
            irProgramQuery.setEndTime(null);
        }
        // 构建返回对象
        IPage<IrReqScreenProgramVO> irReqScreenProgramVOIPage = new Page<IrReqScreenProgramVO>();
        irReqScreenProgramVOIPage.setRecords(irReqScreenProgramVOList);
        irReqScreenProgramVOIPage.setSize(programIPage.getSize());
        irReqScreenProgramVOIPage.setCurrent(programIPage.getCurrent());
        irReqScreenProgramVOIPage.setPages(programIPage.getPages());
        irReqScreenProgramVOIPage.setTotal(programIPage.getTotal());
        return result.success(irReqScreenProgramVOIPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateProgram(IrReqScreenProgramVO irReqScreenProgramVO, HttpServletRequest httpServletRequest) {
        logger.info("修改节目接受数据：{}", irReqScreenProgramVO);
        Result result = new Result();
        if (irReqScreenProgramVO.getName() == null || "".equals(irReqScreenProgramVO.getName().trim())) {
            return result.error("节目名称不能为空");
        }
        LambdaQueryWrapper<ScreenProgram> byNameWrapper = new LambdaQueryWrapper();
        byNameWrapper.eq(ScreenProgram::getName, irReqScreenProgramVO.getName());
        ScreenProgram selectByName = this.getOne(byNameWrapper);
        if (selectByName != null && !selectByName.getId().equals(irReqScreenProgramVO.getId())) {
            return result.error("节目名称已存在");
        }
        //获取传入的素材参数集合
        List<IrReqScreenProgramMaterialVO> programMaterialArrayList = irReqScreenProgramVO.getProgramMaterialArrayList();
        //根据素材集合获取素材id集合
        List<Integer> materialsIdList = programMaterialArrayList.stream().map(IrReqScreenProgramMaterialVO::getMaterialId).collect(Collectors.toList());
        //查询素材集合
        Collection<ScreenMaterial> materials = screenMaterialService.listByIds(materialsIdList);
        logger.info("修改节目对应素材实体集合：{}", materials);
        //计算所有素材集合的文件总大小，文本没有大小则为0
        Integer totalSize = 0;
        for (ScreenMaterial screenMaterial : materials) {
            totalSize += screenMaterial.getSize();
        }
        irReqScreenProgramVO.setTotalSize(totalSize);
        irReqScreenProgramVO.setUpdateTime(new Date());
        ScreenProgram screenProgram = new ScreenProgram();
        BeanUtils.copyProperties(irReqScreenProgramVO, screenProgram);
        //修改节目对象至数据库并返回主键id
        this.updateById(screenProgram);
        //根据节目id删除节目与素材的关联表
        LambdaQueryWrapper<ScreenProgramMaterial> deleteProgramMaterialWrapper = new LambdaQueryWrapper<>();
        deleteProgramMaterialWrapper.eq(ScreenProgramMaterial::getProgramId, screenProgram.getId());
        screenProgramMaterialService.remove(deleteProgramMaterialWrapper);
        //构建节目素材关联对象
        Integer playTime = 0;
        List<ScreenProgramMaterial> programMaterialList = new ArrayList<ScreenProgramMaterial>();
        for (int i = 0; i < programMaterialArrayList.size(); i++) {
            ScreenProgramMaterial programMaterial = new ScreenProgramMaterial();
            programMaterial.setMaterialId(programMaterialArrayList.get(i).getMaterialId());
            programMaterial.setProgramId(screenProgram.getId());
            programMaterial.setTimeSpan(programMaterialArrayList.get(i).getTimeSpan());
            programMaterial.setPlayTime(playTime);
            programMaterial.setCreateTime(new Date());
            playTime += programMaterialArrayList.get(i).getTimeSpan();
            programMaterialList.add(programMaterial);
        }
        logger.info("节目素材关系集合：{}", programMaterialList);
        screenProgramMaterialService.saveBatch(programMaterialList);
        return result.success("节目" + screenProgram.getName().trim() + "修改成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Integer id, HttpServletRequest httpServletRequest) {
        logger.info("删除节目：id = " + id);
        Result result = new Result();
        try {
            this.removeById(id);
            //根据节目id删除节目与素材的关联表
            LambdaQueryWrapper<ScreenProgramMaterial> deleteProgramMaterialWrapper = new LambdaQueryWrapper<>();
            deleteProgramMaterialWrapper.eq(ScreenProgramMaterial::getProgramId, id);
            screenProgramMaterialService.remove(deleteProgramMaterialWrapper);
            // 获取节目对应的播放列表id
            LambdaQueryWrapper<ScreenPlay> getProgramPlayWrapper = new LambdaQueryWrapper();
            getProgramPlayWrapper.eq(ScreenPlay::getProgramId, id);
            List<ScreenPlay> list = screenPlayService.list(getProgramPlayWrapper);
            List<Integer> playIdList = list.stream().map(ScreenPlay::getId).collect(Collectors.toList());
            // 删除节目关联的播放列表
            if (playIdList != null && playIdList.size() > 0) {
                screenPlayService.removeByIds(playIdList);
                // 删除显示屏和播放关联
                LambdaQueryWrapper<ScreenPlayDevice> deleteScreenPlayDeviceWrapper = new LambdaQueryWrapper<>();
                deleteScreenPlayDeviceWrapper.in(ScreenPlayDevice::getScreenPlayId, playIdList);
                screenPlayDeviceService.remove(deleteScreenPlayDeviceWrapper);
                // 删除显示屏下发策略
                LambdaQueryWrapper<ScreenPlayStrategy> deleteScreenPlayStrategyWrapper = new LambdaQueryWrapper<>();
                deleteScreenPlayStrategyWrapper.in(ScreenPlayStrategy::getScreenPlayId, playIdList);
                screenPlayStrategyService.remove(deleteScreenPlayStrategyWrapper);
            }
        } catch (Exception e) {
            return result.error("删除失败");
        }
        return result.success("删除成功");
    }




    @Override
    public Result sendProgram(IrReqSendProgramVO irReqSendProgramVO, HttpServletRequest httpServletRequest) {
        logger.info("下发节目接收参数：{}", irReqSendProgramVO);
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
        Date currentDate = new Date();
        Result respResult = new Result();
        // 设备id集合
        List<Integer> reqScreenDeviceIdList = irReqSendProgramVO.getScreenDeviceIdList();
        // 灯杆id集合
        List<Integer> lampPostList = irReqSendProgramVO.getLampPostIdList();
        // 分组id集合
        List<Integer> groupIdList = irReqSendProgramVO.getGroupIdList();
        // 获取灯具设备集合
        List<ScreenDevice> screenDeviceList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("token", httpServletRequest.getHeader("token"));
        // 优先按设备id集合,获得灯具设备集合
        if (reqScreenDeviceIdList != null && reqScreenDeviceIdList.size() > 0) {
            screenDeviceList = (List<ScreenDevice>) screenDeviceService.listByIds(reqScreenDeviceIdList);
        }
        // 优先按路灯id集合,获得灯具设备集合,区域
        else if (lampPostList != null && lampPostList.size() > 0) {
            irReqSendProgramVO.setDeviceSubordinate(1);
            Result result = screenDeviceService.pulldownByLampPost(lampPostList, httpServletRequest);
            screenDeviceList = (List<ScreenDevice>) result.getData();
        }
        // 根据分组id集合获取灯具设备集合
        else if (groupIdList != null && groupIdList.size() > 0) {
            irReqSendProgramVO.setDeviceSubordinate(2);
            // 获取路灯灯杆集合
            List<SlLampPost> slLampPostList = null;
            String json = "groupIdList=";
            for (Integer groupId : groupIdList) {
                json += groupId + "&groupIdList=";
            }
            try {
                JSONObject slLampPostResult = JSON.parseObject(HttpUtil.get(httpDlmApi.getUrl() + httpDlmApi.getGetLampPostByGroupIdList() + "?" + json, map));
                JSONArray slLampPostResultArr = slLampPostResult.getJSONArray("data");
                slLampPostList = JSON.parseObject(slLampPostResultArr.toJSONString(), new TypeReference<List<SlLampPost>>() {
                });
            } catch (Exception e) {
                logger.error("根据分组id集合获取灯杆集合接口调用失败，返回为空！");
                return respResult.error("根据分组获取灯杆集合接口调用失败！");
            }
            // 获取路灯灯具设备集合
            List<Integer> lampPostIdList = slLampPostList.stream().map(SlLampPost::getId).collect(Collectors.toList());
            LambdaQueryWrapper<ScreenDevice> wrapper = new LambdaQueryWrapper();
            if (lampPostIdList != null && lampPostIdList.size() > 0) {
                wrapper.in(ScreenDevice::getLampPostId, lampPostIdList);
            } else {
                wrapper.eq(ScreenDevice::getId, 0);
            }
            screenDeviceList = screenDeviceService.list(wrapper);
        }
        if (screenDeviceList == null || screenDeviceList.size() == 0) {
            return respResult.error("该目标没有设备");
        }
        // 获取设备num集合
        List<String> screenDeviceNumList = screenDeviceList.stream().map(ScreenDevice::getNum).collect(Collectors.toList());
        // 节目对象
        IrProgramVO irProgramVO = this.baseMapper.getIrProgramVO(irReqSendProgramVO.getProgramId());
        // 节目素材中间对象集合
        List<IrProgramMaterialVO> irProgramMaterialVOList = screenProgramMaterialService.getIrProgramMaterialVO(irReqSendProgramVO.getProgramId());
        // 素材id集合
        List<Integer> materialIdList = irProgramMaterialVOList.stream().map(IrProgramMaterialVO::getMaterialId).collect(Collectors.toList());
        // 素材对象集合
        if (materialIdList != null && materialIdList.size() == 0) {
            return respResult.error("没有素材");
        }
        materialIdList = materialIdList.stream().distinct().collect(Collectors.toList());
        List<IrMaterialVO> irMaterialVOList = screenMaterialService.getIrMaterialVO(materialIdList);
        if (materialIdList.size() != irMaterialVOList.size()) {
            return respResult.error("素材缺失");
        }
        // 下发
        Result sendProgramControl = new Result();
        try {
            // 假下发
//            JSONObject jsonObject = new JSONObject();
//            List<JSONObject> JSONObjectList = new ArrayList<>();
//            JSONObject jsonObject1 = new JSONObject();
//            for(String s : screenDeviceNumList){
//                jsonObject1.put("sn",s);
//                JSONObjectList.add(jsonObject1);
//            }
//            jsonObject.put("successObjectList", JSONObjectList);
//            sendProgramControl.setData(jsonObject);
            // 真下发
            sendProgramControl = ScreenControlUtil.sendProgramControl(screenApi.getIp(), screenApi.getPort(),
                    materialApi.getServicePath(), materialApi.getProgressInterface(), irReqSendProgramVO, screenDeviceList.get(0), irProgramVO, irProgramMaterialVOList, irMaterialVOList, screenDeviceNumList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("下发节目返回信息：{}", sendProgramControl);
        if (sendProgramControl == null) {
            return respResult.error("下发失败");
        }
        //获取发送成功的序列号集合
        JSONObject data = (JSONObject) sendProgramControl.getData();
        if (data == null) {
            return respResult.error("下发失败");
        }
        List<JSONObject> successList = (List<JSONObject>) data.get("successObjectList");
        List<String> successSnList = new ArrayList<>();
        if (successList != null && successList.size() > 0) {
            //序列号集合获得对应的显示屏参数
            for (JSONObject jsonObject : successList) {
                successSnList.add((String) jsonObject.get("sn"));
            }
//            LambdaQueryWrapper<ScreenDevice> successScreenDeviceWrapper = new LambdaQueryWrapper<>();
//            successScreenDeviceWrapper.in(ScreenDevice::getNum, successSnList);
//            List<ScreenDevice> screenInfoList = screenDeviceService.list(successScreenDeviceWrapper);

            List<ScreenDevice> screenInfoList = screenDeviceList.stream().filter(a -> successSnList.contains(a.getNum())).collect(Collectors.toList());
            // 获取设备id集合
            List<Integer> screenDeviceIdList = screenInfoList.stream().map(ScreenDevice::getId).collect(Collectors.toList());
            // 获取时间
            Date startDateTime = null;
            Date endDateTime = null;
            Date programStartDateTime = null;
            Date programEndDateTime = null;
            String currentDateString = formatDate.format(currentDate);
            try {
                startDateTime = formatDateTime.parse(currentDateString + " " + irReqSendProgramVO.getExecutionStartTime());
                endDateTime = formatDateTime.parse(currentDateString + " " + irReqSendProgramVO.getExecutionEndTime());
                programStartDateTime = formatDateTime.parse(irReqSendProgramVO.getStartDate() + " " + irReqSendProgramVO.getExecutionStartTime());
                programEndDateTime = formatDateTime.parse(irReqSendProgramVO.getEndDate() + " " + irReqSendProgramVO.getExecutionEndTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 本地持久化正在播放节目
            ScreenPlay screenPlay = new ScreenPlay();
            screenPlay.setCreateTime(currentDate);
            screenPlay.setUpdateTime(screenPlay.getCreateTime());
            screenPlay.setDeviceSubordinate(irReqSendProgramVO.getDeviceSubordinate());
            screenPlay.setProgramId(irReqSendProgramVO.getProgramId());
            long time = currentDate.getTime();
            if (programStartDateTime.getTime() <= time && time <= programEndDateTime.getTime()) {
                if (startDateTime.getTime() <= time && time <= endDateTime.getTime()) {
                    screenPlay.setPlayStatus(1);
                } else {
                    screenPlay.setPlayStatus(0);
                }
            } else if (time >= programEndDateTime.getTime()) {
                screenPlay.setPlayStatus(2);
            } else {
                screenPlay.setPlayStatus(0);
            }
            screenPlayService.save(screenPlay);
            // 先根据设备id，移除之前设备与播放的节目关联
            LambdaQueryWrapper<ScreenPlayDevice> deleteScreenPlayDevice = new LambdaQueryWrapper<>();
            deleteScreenPlayDevice.in(ScreenPlayDevice::getDeviceId, screenDeviceIdList);
            screenPlayDeviceService.remove(deleteScreenPlayDevice);
            // 保存新的关联
            List<ScreenPlayDevice> screenPlayDeviceList = new ArrayList<>();
            if(irReqSendProgramVO.getDeviceSubordinate() == 1){
                for (Integer deviceId : screenDeviceIdList) {
                    ScreenPlayDevice screenPlayDevice = new ScreenPlayDevice();
                    screenPlayDevice.setDeviceId(deviceId);
                    screenPlayDevice.setScreenPlayId(screenPlay.getId());
                    screenPlayDeviceList.add(screenPlayDevice);
                }
            }else if(irReqSendProgramVO.getDeviceSubordinate() == 2){
                for (Integer groupId : groupIdList) {
                    ScreenPlayDevice screenPlayDevice = new ScreenPlayDevice();
                    screenPlayDevice.setDeviceId(groupId);
                    screenPlayDevice.setScreenPlayId(screenPlay.getId());
                    screenPlayDeviceList.add(screenPlayDevice);
                }
            }
            screenPlayDeviceService.saveBatch(screenPlayDeviceList);
            // 保存播放记录关联的策略
            ScreenPlayStrategy screenPlayStrategy = new ScreenPlayStrategy();
            screenPlayStrategy.setCreateTime(currentDate);
            screenPlayStrategy.setUpdateTime(currentDate);
            Integer[] cycleTypes = irReqSendProgramVO.getCycleTypes();
            int cycleType = DataUtil.getCycleType(cycleTypes, 1);
            screenPlayStrategy.setWeekValue(cycleType);
            screenPlayStrategy.setScreenPlayId(screenPlay.getId());
            screenPlayStrategy.setStartDate(irReqSendProgramVO.getStartDate());
            screenPlayStrategy.setEndDate(irReqSendProgramVO.getEndDate());
            screenPlayStrategy.setExecutionStartTime(irReqSendProgramVO.getExecutionStartTime());
            screenPlayStrategy.setExecutionEndTime(irReqSendProgramVO.getExecutionEndTime());
//            screenPlayStrategy.setStartDate("2020-04-20");
//            screenPlayStrategy.setEndDate("2020-04-25");
//            screenPlayStrategy.setExecutionStartTime("18:14:48");
//            screenPlayStrategy.setExecutionEndTime("21:14:48");
            screenPlayStrategy.setExecutionMode(irReqSendProgramVO.getExecutionMode());
            screenPlayStrategyService.save(screenPlayStrategy);
        }
        // 获取下发成功或者失败的设备名称
        String nodeNameScreenString = "";
        String nodeNameFailString = "";
        for (ScreenDevice screenDevice : screenDeviceList) {
            // 获取当前循环设备是否在成功设备的列表中
            List<String> collect = successSnList.stream().filter(a -> a.equals(screenDevice.getNum())).collect(Collectors.toList());
            if (collect != null && collect.size() > 0) {
                if (!"".equals(nodeNameScreenString)) {
                    nodeNameScreenString = nodeNameScreenString + ",";
                }
                nodeNameScreenString = nodeNameScreenString + screenDevice.getName();
            } else {
                if (!"".equals(nodeNameFailString)) {
                    nodeNameFailString = nodeNameFailString + ",";
                }
                nodeNameFailString = nodeNameFailString + screenDevice.getName();
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sendProgramControl", data);
        if ("".equals(nodeNameFailString)) {
            return respResult.success("发送节目全部成功", jsonObject);
        } else if ("".equals(nodeNameScreenString)) {
            return respResult.error("发送节目全部失败", jsonObject);
        } else {
            return respResult.error("发送节目部分失败，下发失败显示屏：" + nodeNameFailString, jsonObject);
        }
    }

    @Override
    public Result batchDelete(String ids, HttpServletRequest request) {
        logger.info("批量删除节目，接收参数：{}", ids);
        List<Integer> idListFromString = StringConversionUtil.getIdListFromString(ids);
        this.removeByIds(idListFromString);
        //根据节目id集合删除节目与素材的关联表
        LambdaQueryWrapper<ScreenProgramMaterial> deleteProgramMaterialWrapper = new LambdaQueryWrapper<>();
        deleteProgramMaterialWrapper.in(ScreenProgramMaterial::getProgramId, idListFromString);
        screenProgramMaterialService.remove(deleteProgramMaterialWrapper);
        Result result = new Result();
        return result.success("批量删除成功");
    }

    /**
     * 获取后一天日期
     *
     * @param time
     * @return
     */
    public static String getAfterDay(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day - 1);

        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    /**
     * 获取前一天日期
     *
     * @param time
     * @return
     */
    public static String getBeforeDay(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day + 1);

        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    @Override
    public Result<String> verifyProgram(IrReqVerifyProgramVo reqVO, HttpServletRequest request) {
        logger.info("信息发布节目审核，接收参数：IrReqVerifyProgramVo = {}", reqVO);
        Result<String> result = new Result<>();
        if (reqVO == null || reqVO.getId() == null || reqVO.getVerifyStatus() == null) {
            return result.error("审核失败,参数缺失");
        }
        if (!Arrays.asList(1, 2).contains(reqVO.getVerifyStatus())) {
            return result.error("审核失败,参数错误");
        }
        ScreenProgram screenProgram = screenProgramService.getById(reqVO.getId());
        if (screenProgram == null) {
            return result.error("审核失败,未找到节目信息");
        }
        if (!screenProgram.getVerifyStatus().equals(0)) {
            return result.error("审核失败,该节目不可审核");
        }
        screenProgram.setVerifyStatus(reqVO.getVerifyStatus());
        this.updateById(screenProgram);
        return result.success("审核成功", "");
    }

}