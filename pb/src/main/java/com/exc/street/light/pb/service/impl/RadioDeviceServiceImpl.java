/**
 * @filename:RadioDeviceServiceImpl 2020-03-21
 * @project pb  V1.0
 * Copyright(c) 2018 LeiJing Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.pb.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.pb.config.HttpApi;
import com.exc.street.light.pb.listener.RadioDeviceImportListener;
import com.exc.street.light.pb.mapper.RadioDeviceDao;
import com.exc.street.light.pb.service.*;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.dto.pb.RadioDeviceImportDataDTO;
import com.exc.street.light.resource.entity.dlm.SlLampPost;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.entity.pb.RadioProgram;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.PbRadioDeviceQueryObject;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.req.PbReqBatchUpdateVolumeVO;
import com.exc.street.light.resource.vo.resp.PbRespDeviceAndLampPostVO;
import com.exc.street.light.resource.vo.resp.PbRespLampPostVO;
import com.exc.street.light.resource.vo.resp.ImportDeviceResultVO;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: LeiJing
 */
@Service
public class RadioDeviceServiceImpl extends ServiceImpl<RadioDeviceDao, RadioDevice> implements RadioDeviceService {
    private static final Logger logger = LoggerFactory.getLogger(RadioDeviceServiceImpl.class);

    @Autowired
    private HttpApi httpApi;

    @Autowired
    private RadioDeviceDao radioDeviceDao;

    @Autowired
    private RadioPlayService radioPlayService;

    @Autowired
    private RadioProgramService radioProgramService;

    @Autowired
    private RadioMaterialService radioMaterialService;

    @Autowired
    private RadioProgramMaterialService radioProgramMaterialService;

    @Autowired
    private LogUserService userService;


    @Override
    public Result addDevice(RadioDevice radioDevice, HttpServletRequest request) {
        logger.info("新增公共广播设备，接收参数：radioDevice = {}", radioDevice);
        Result result = new Result();
        radioDevice.setCreateTime(new Date());
        radioDeviceDao.insert(radioDevice);
        //新增与定时任务的关联
        radioPlayService.refreshPlayBind(Collections.singletonList(radioDevice.getLampPostId()), request);
        if (radioDevice.getId() != null && radioDevice.getTermId() != null && radioDevice.getVolume() != null) {
            //修改音量
            PbReqBatchUpdateVolumeVO volVo = new PbReqBatchUpdateVolumeVO();
            volVo.setVolume(radioDevice.getVolume());
            volVo.setIds(String.valueOf(radioDevice.getId()));
            updateDeviceVolume(volVo, request);
        }
        return result.success("新增成功");
    }

    @Override
    public Result updateDevice(RadioDevice radioDevice, HttpServletRequest request) {
        logger.info("编辑公共广播设备，接收参数：radioDevice = {}", radioDevice);
        Result result = new Result();
        RadioDevice device = this.getById(radioDevice.getId());
        radioDeviceDao.updateById(radioDevice);
        //when 修改灯杆 then 修改与定时任务的关联
        if (!device.getLampPostId().equals(radioDevice.getLampPostId())) {
            radioPlayService.refreshPlayBind(Arrays.asList(radioDevice.getLampPostId(), device.getLampPostId()), request);
        } else if (!device.getTermId().equals(radioDevice.getTermId())) {
            radioPlayService.refreshPlayBind(Collections.singletonList(radioDevice.getLampPostId()), request);
        }
        if (radioDevice.getTermId() != null && radioDevice.getVolume() != null) {
            //修改音量
            PbReqBatchUpdateVolumeVO volVo = new PbReqBatchUpdateVolumeVO();
            volVo.setVolume(radioDevice.getVolume());
            volVo.setIds(String.valueOf(radioDevice.getId()));
            updateDeviceVolume(volVo, request);
        }
        return result.success("新增成功");
    }

    @Override
    public Result deleteDevice(Integer deviceId, HttpServletRequest request) {
        logger.info("删除公共广播设备，接收参数：deviceId = {}", deviceId);
        Result result = new Result();
        RadioDevice device = this.getById(deviceId);
        radioDeviceDao.deleteById(deviceId);
        //删除与定时任务的关联
        radioPlayService.refreshPlayBind(Collections.singletonList(device.getLampPostId()), request);
        return result.success("删除成功");
    }

    @Override
    public Result batchDeleteDevice(List<Integer> idList, HttpServletRequest request) {
        LambdaQueryWrapper<RadioDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(RadioDevice::getLampPostId).in(RadioDevice::getId, idList);
        List<RadioDevice> list = this.list(wrapper);
        List<Integer> lampPostIds = list.stream().filter(e -> e.getLampPostId() != null).map(RadioDevice::getLampPostId).distinct().collect(Collectors.toList());
        boolean bool = this.removeByIds(idList);
        //删除与定时任务的关联
        radioPlayService.refreshPlayBind(lampPostIds, request);
        Result result = new Result();
        if (bool) {
            return result.success("批量删除成功");
        } else {
            return result.error("批量删除失败");
        }
    }

    @Override
    public Result getDeviceInfo(Integer deviceId, HttpServletRequest request) {
        logger.info("获取公共广播设备详细信息，接收参数：deviceId= {}", deviceId);
        Result result = new Result();
        RadioDevice radioDevice = radioDeviceDao.selectById(deviceId);
        PbRespDeviceAndLampPostVO vo = new PbRespDeviceAndLampPostVO();
        BeanUtils.copyProperties(radioDevice, vo);
        try {
            // 获取灯杆信息
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("token", request.getHeader("token"));
            JSONObject slLampPostResult = JSON.parseObject(HttpUtil.get(httpApi.getDlm().get("url") + httpApi.getDlm().get("lampPostById") + radioDevice.getLampPostId(), headerMap));
            if (slLampPostResult != null) {
                JSONObject slLampPostResultObj = slLampPostResult.getJSONObject("data");
                SlLampPost slLampPost = JSONObject.toJavaObject(slLampPostResultObj, SlLampPost.class);
                BeanUtils.copyProperties(radioDevice, vo);
                vo.setLampPostName(slLampPost.getName());
                vo.setLampPostNum(slLampPost.getNum());
                vo.setLongitude(slLampPost.getLongitude());
                vo.setLatitude(slLampPost.getLatitude());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result.success(vo);
    }

    @Override
    public Result getDeviceList(PbRadioDeviceQueryObject qo, HttpServletRequest request) {
        logger.info("获取公共广播备列表，接收参数：qo = {}", qo);
        Result result = new Result();
        //更新设备状态
        //updateDeviceStatus();
        Page<PbRespDeviceAndLampPostVO> page = new Page<>(qo.getPageNum(), qo.getPageSize());
        IPage<PbRespDeviceAndLampPostVO> resultVOPage = radioDeviceDao.getPageList(page, qo);
        return result.success(resultVOPage);
    }

    @Override
    public Result<ImportDeviceResultVO> batchImportDevice(MultipartFile file, HttpServletRequest request) {
        logger.info("批量导入公共广播设备，接收参数：MultipartFile = {}", file);
        Result<ImportDeviceResultVO> result = new Result<>();
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(!".xls".equals(suffix)&&!".xlsx".equals(suffix)){
            return result.error("批量导入失败,文件格式错误",null);
        }
        //根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean isAdmin = userService.isAdmin(userId);
        Integer areaId = null;
        if (!isAdmin) {
            areaId = user.getAreaId();
        }
        //获取当前区域的灯杆及绑定信息
        List<PbRespLampPostVO> lampPostInfoByAreaId = baseMapper.getLampPostInfoByAreaId(areaId);
        Map<String, Integer> lampPostAllInfoMap = lampPostInfoByAreaId.stream().collect(Collectors.toMap(PbRespLampPostVO::getLampPostName, PbRespLampPostVO::getLampPostId));
        //已绑定广播设备的灯杆信息
        Map<String, Integer> bindRadioDeviceLampPostInfoMap = lampPostInfoByAreaId.stream()
                .filter(e -> e.getIsBindRadioDevice() != null && e.getIsBindRadioDevice().equals(1))
                .collect(Collectors.toMap(PbRespLampPostVO::getLampPostName, PbRespLampPostVO::getLampPostId));
        //获取已存在的 雷拓终端编号,设备名称,设备编号
        LambdaQueryWrapper<RadioDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(RadioDevice::getTermId, RadioDevice::getName, RadioDevice::getNum);
        List<RadioDevice> list = this.list(wrapper);
        Set<Integer> termIdSet = list.stream().map(RadioDevice::getTermId).collect(Collectors.toSet());
        Set<String> numSet = list.stream().map(RadioDevice::getNum).collect(Collectors.toSet());
        Set<String> nameSet = list.stream().map(RadioDevice::getName).collect(Collectors.toSet());
        list = new ArrayList<>();
        ImportDeviceResultVO resultVO = new ImportDeviceResultVO();
        try {
            EasyExcel.read(file.getInputStream(), RadioDeviceImportDataDTO.class, new RadioDeviceImportListener(this, resultVO, bindRadioDeviceLampPostInfoMap, lampPostAllInfoMap, numSet, nameSet, termIdSet, list)).sheet().doRead();
        } catch (IOException e) {
            logger.error("广播设备导入失败,e:{}", e.getMessage());
            return result.error("批量导入失败", resultVO);
        }
        if (!list.isEmpty()) {
            this.saveBatch(list);
        }
        logger.info("返回结果:{}", list);
        logger.info("广播设备导入结束,RadioDeviceImportResultVO={}",resultVO);
        if (resultVO.getSuccessNum().equals(0)) {
            return result.error("批量导入失败", resultVO);
        } else if (!resultVO.getSuccessNum().equals(0) && !resultVO.getFailNum().equals(0)) {
            return result.success("批量导入部分失败", resultVO);
        }
        return result.success("批量导入成功", resultVO);
    }

    @Override
    public Result uniqueness(RadioDevice radioDevice, HttpServletRequest request) {
        logger.info("公共广播设备唯一性验证，接收参数：radioDevice = {}", radioDevice);
        Result result = new Result();
        Integer id = radioDevice.getId();
        String name = radioDevice.getName();
        String num = radioDevice.getNum();
        Integer termId = radioDevice.getTermId();

        //如果存在id，则为编辑，判断是否是编辑本身
        if (StringUtils.isNotBlank(name)) {
            LambdaQueryWrapper<RadioDevice> queryWrapperName = new LambdaQueryWrapper<RadioDevice>();
            queryWrapperName.eq(RadioDevice::getName, name);
            queryWrapperName.last("LIMIT 1");
            RadioDevice radioDeviceName = this.getOne(queryWrapperName);
            if ((id != null && radioDeviceName != null && !radioDeviceName.getId().equals(id)) || (id == null && radioDeviceName != null)) {
                logger.info("设备名称 {} 已存在,请重新输入", name);
                return result.success("设备名称已存在,请重新输入", 1);
            }
        }
        if (StringUtils.isNotBlank(num)) {
            LambdaQueryWrapper<RadioDevice> queryWrapperNum = new LambdaQueryWrapper<RadioDevice>();
            queryWrapperNum.eq(RadioDevice::getNum, num);
            queryWrapperNum.last("LIMIT 1");
            RadioDevice radioDeviceNum = this.getOne(queryWrapperNum);
            if ((id != null && radioDeviceNum != null && !radioDeviceNum.getId().equals(id)) || (id == null && radioDeviceNum != null)) {
                logger.info("设备编号 {} 已存在,请重新输入", num);
                return result.success("设备编号已存在,请重新输入", 2);
            }
        }
        if (termId != null) {
            LambdaQueryWrapper<RadioDevice> queryWrapperNum = new LambdaQueryWrapper<RadioDevice>();
            queryWrapperNum.eq(RadioDevice::getTermId, termId);
            queryWrapperNum.last("LIMIT 1");
            RadioDevice radioDeviceTerm = this.getOne(queryWrapperNum);
            if ((id != null && radioDeviceTerm != null && !radioDeviceTerm.getId().equals(id)) || (id == null && radioDeviceTerm != null)) {
                logger.info("雷拓终端编号 {} 已存在,请重新输入", termId);
                return result.success("雷拓终端编号已存在,请重新输入", 3);
            }
        }


        return result.success("唯一性校验通过", 0);
    }

    @Override
    public Result getDevicePulldownList(HttpServletRequest request) {
        logger.info("获取公共广播设备下拉列表");
        Result result = new Result();
        //根据分区过滤数据
        PbRadioDeviceQueryObject qo = getDeviceQueryObject(request);
        List<PbRespDeviceAndLampPostVO> list = baseMapper.getList(qo);
        return result.success(list);
    }

    @Override
    public Result pulldownByLampPost(List<Integer> lampPostIdList, HttpServletRequest request) {
        //根据分区过滤数据
        PbRadioDeviceQueryObject qo = getDeviceQueryObject(request);
        qo.setLampPostIdList(lampPostIdList);
        Result result = new Result();
        List<PbRespDeviceAndLampPostVO> list = baseMapper.getList(qo);
        return result.success(list);
    }

    /**
     * 封装请求
     *
     * @param request
     * @return
     */
    @Override
    public PbRadioDeviceQueryObject getDeviceQueryObject(HttpServletRequest request) {
        PbRadioDeviceQueryObject qo = new PbRadioDeviceQueryObject();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(request);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            qo.setAreaId(user.getAreaId());
        }
        return qo;
    }

    @Override
    public Result updateDeviceVolume(PbReqBatchUpdateVolumeVO pbReqBatchUpdateVolumeVO, HttpServletRequest request) {
        logger.info("调节公共广播设备音量, 接收参数：pbReqBatchUpdateVolumeVO = {}", pbReqBatchUpdateVolumeVO);
        Result result = new Result();
        List<Integer> deviceIds = StringConversionUtil.getIdListFromString(pbReqBatchUpdateVolumeVO.getIds());
        Integer volume = pbReqBatchUpdateVolumeVO.getVolume();

        LambdaQueryWrapper<RadioDevice> deviceWrapper = new LambdaQueryWrapper<>();
        deviceWrapper.in(RadioDevice::getId, deviceIds);
        List<RadioDevice> deviceList = this.list(deviceWrapper);
        deviceList = deviceList == null ? new ArrayList<>() : deviceList;
        List<Integer> termIds = deviceList.stream().filter(item -> item.getTermId() != null).map(RadioDevice::getTermId).collect(Collectors.toList());
        if (termIds.isEmpty()) {
            return result.error("未设置termId");
        }
        //雷拓IP广播平台调节音量接口
        String interfaceName = "TermVolSet";
        JSONObject jsonObject = new JSONObject();
        //雷拓IP广播平台定时任务ID
        jsonObject.put("TermIds", termIds);
        jsonObject.put("Volume", volume);
        //调用雷拓IP广播平台接口
        JSONObject returnJson = radioPlayService.interfaceCall(interfaceName, jsonObject);
        if (returnJson != null && "0".equals(returnJson.getString("Ret"))) {
            for (RadioDevice radioDevice : deviceList) {
                radioDevice.setVolume(volume);
                radioDeviceDao.updateById(radioDevice);
            }
            logger.info("调节设备音量成功");
            return result.success("调节设备音量成功");
        } else {
            String errorMessage = radioPlayService.getErrorMessage(returnJson);
            logger.info("调节设备音量失败, {}", errorMessage);

            return result.error("调节设备音量失败");
        }
    }

    @Override
    public void updateDeviceStatus() {
        logger.info("---开始更新广播设备在线状态---");
        String interfaceName = "getTermState";
        LambdaQueryWrapper<RadioDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(RadioDevice::getId, RadioDevice::getTermId);
        List<RadioDevice> radioDevices = this.list(wrapper);
        if (radioDevices == null || radioDevices.isEmpty()) {
            return;
        }
        List<Integer> termIdList = radioDevices.stream().filter(item -> item.getTermId() != null).map(RadioDevice::getTermId).collect(Collectors.toList());
        List<Integer> idList = radioDevices.stream().filter(item -> item.getId() != null).map(RadioDevice::getId).collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject(true);
        if (termIdList.isEmpty()) {
            return;
        }
        jsonObject.put("TermIDs", termIdList);
        JSONObject returnJson = radioPlayService.interfaceCall(interfaceName, jsonObject);
        if (returnJson == null || !"0".equals(returnJson.getString("Ret"))) {
            String errorMessage = radioPlayService.getErrorMessage(returnJson);
            logger.info("更新广播设备在线状态失败, {}", errorMessage);
            return;
        }
        JSONArray terms = returnJson.getJSONArray("Terms");
        //先将所有广播设备在线状态设置为离线
        radioDeviceDao.updateStatusById(0, idList);
        if (terms == null || terms.isEmpty()) {
            return;
        }
        //在线的雷拓设备编号集合
        List<Integer> onlineTermId = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            JSONObject termObj = terms.getJSONObject(i);
            Integer status = termObj.getInteger("Status");
            status = status != null && status == -1 ? 0 : 1;
            Integer termId = termObj.getInteger("ID");
            if (termId != null && status.equals(1)) {
                onlineTermId.add(termId);
            }
        }
        //更新设备在线状态
        if (!onlineTermId.isEmpty()) {
            LambdaUpdateWrapper<RadioDevice> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(RadioDevice::getNetworkState, 1);
            updateWrapper.in(RadioDevice::getTermId, onlineTermId);
            this.update(updateWrapper);
        }
        logger.info("---结束更新广播设备在线状态---");
    }

    @Override
    public Result getTermState(Integer termId, HttpServletRequest request) {
        logger.info("获取设备状态,接收参数termId：{}", termId);
        Result result = new Result();
        if (termId == null) {
            result.error("雷拓终端id为空");
        }
        String interfaceName = "getTermState";
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("TermIDs", Collections.singletonList(termId));
        JSONObject returnJson = radioPlayService.interfaceCall(interfaceName, jsonObject);
        if (returnJson != null && "0".equals(returnJson.getString("Ret"))) {
            logger.info("获取设备状态成功,{}", returnJson);
            JSONArray terms = returnJson.getJSONArray("Terms");
            String programName = null;
            if (terms != null && !terms.isEmpty()) {
                JSONObject jsonObject1 = terms.getJSONObject(0);
                JSONObject sessionInfo = jsonObject1.getJSONObject("SessionInfo");
                if (sessionInfo == null) {
                    return result.error("当前无播放信息");
                }
                programName = sessionInfo.getString("Name");
                if (StringUtils.isEmpty(programName)) {
                    return result.error("任务无节目名称");
                }
            }
            return result.success(programName);
        } else {
            String errorMessage = radioPlayService.getErrorMessage(returnJson);
            logger.info("获取设备状态失败, {}", errorMessage);
        }
        return result.error("");
    }

    @Override
    public Result getPlayingProgram(Integer id, HttpServletRequest request) {
        Result result = new Result();
        if (id == null) {
            return result.error("id不可为空");
        }
        logger.info("获取设备正在播放的节目,接收参数 id:{}", id);
        RadioDevice radioDevice = this.getById(id);
        if (radioDevice != null && radioDevice.getTermId() != null) {
            Result termStateRes = getTermState(radioDevice.getTermId(), request);
            if (termStateRes.getCode() == 200) {
                String programName = termStateRes.getMessage();
                LambdaQueryWrapper<RadioProgram> programWrapper = new LambdaQueryWrapper<>();
                programWrapper.eq(RadioProgram::getName, programName).last("LIMIT 1");
                RadioProgram radioProgram = radioProgramService.getOne(programWrapper);
                if (radioProgram == null) {
                    return result.error("未找到节目");
                }
                return radioProgramService.getInfo(radioProgram.getId(), request);
            }
        }
        return result.success("当前没有正在播放的节目");
    }

    @Override
    public List<Integer> getIdListByGroupIdList(List<Integer> groupIdList) {
        if (groupIdList == null || groupIdList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> ids = baseMapper.getIdsByGroupIds(groupIdList);
        return ids == null ? new ArrayList<>() : ids;
    }


}