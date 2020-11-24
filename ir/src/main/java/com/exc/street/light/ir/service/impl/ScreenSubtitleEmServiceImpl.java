/**
 * @filename:ScreenSubtitleEmServiceImpl 2020-11-10
 * @project ir  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd. 
 * All right reserved. 
 */
package com.exc.street.light.ir.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exc.street.light.ir.mapper.ScreenDeviceDao;
import com.exc.street.light.ir.mapper.ScreenDeviceMapper;
import com.exc.street.light.ir.mapper.ScreenSubtitleEmScreenMapper;
import com.exc.street.light.log_api.service.LogUserService;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.entity.ir.ScreenPlayDevice;
import com.exc.street.light.resource.entity.ir.ScreenSubtitleEm;
import com.exc.street.light.ir.mapper.ScreenSubtitleEmMapper;
import com.exc.street.light.ir.service.ScreenSubtitleEmService;
import com.exc.street.light.resource.entity.ir.ScreenSubtitleEmScreen;
import com.exc.street.light.resource.entity.ua.User;
import com.exc.street.light.resource.qo.EmMeteorologicalDeviceQueryObject;
import com.exc.street.light.resource.qo.IrScreenDeviceQuery;
import com.exc.street.light.resource.qo.IrScreenSubtitleEmQuery;
import com.exc.street.light.resource.utils.JavaWebTokenUtil;
import com.exc.street.light.resource.utils.StringConversionUtil;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitleEmVO;
import com.exc.street.light.resource.vo.resp.IrRespScreenDeviceVO;
import com.exc.street.light.resource.vo.resp.IrRespScreenSubtitleEmVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**   
 * @Description:TODO(传感器关联显示屏显示数据设置表服务实现)
 *
 * @version: V1.0
 * @author: Longshuangyang
 * 
 */
@Service
public class ScreenSubtitleEmServiceImpl  extends ServiceImpl<ScreenSubtitleEmMapper, ScreenSubtitleEm> implements ScreenSubtitleEmService  {
    private static final Logger logger = LoggerFactory.getLogger(ScreenSubtitleEmServiceImpl.class);

    @Autowired
    private LogUserService userService;
    @Autowired
    private ScreenDeviceDao screenDeviceDao;
    @Autowired
    private ScreenSubtitleEmScreenMapper screenSubtitleEmScreenMapper;

    @Override
    public Result add(IrReqScreenSubtitleEmVO irReqScreenSubtitleEmVO, HttpServletRequest httpServletRequest) {
        logger.info("添加传感器关联显示屏数据设置，接收参数：{}", irReqScreenSubtitleEmVO);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        if (userId == null) {
            return result.error("添加失败，token已过期");
        }
        // 对应需要添加对应关系的显示屏id集合，与传感器id
        List<Integer> irDeviceIdList = new ArrayList<>();
        Integer emDeviceId = null;
        if(irReqScreenSubtitleEmVO.getReqEmDeviceId() == null && irReqScreenSubtitleEmVO.getReqEmLampPostId() == null){
            return result.error("请选择传感器");
        }
        if ((irReqScreenSubtitleEmVO.getReqIrDeviceIdList() == null || irReqScreenSubtitleEmVO.getReqIrDeviceIdList().size() == 0) && (irReqScreenSubtitleEmVO.getReqIrLampPostIdList() == null || irReqScreenSubtitleEmVO.getReqIrLampPostIdList().size() == 0)) {
            return result.error("请选择显示屏");
        }
        // 获取传感器设备id
        if(irReqScreenSubtitleEmVO.getReqEmLampPostId() != null){
            MeteorologicalDevice meteorologicalDevice = screenDeviceDao.selectEmDeviceByLampPostId(irReqScreenSubtitleEmVO.getReqEmLampPostId());
            emDeviceId = meteorologicalDevice.getId();
        }
        if(irReqScreenSubtitleEmVO.getReqEmDeviceId() != null){
            emDeviceId = irReqScreenSubtitleEmVO.getReqEmDeviceId();
        }
        // 获取显示屏设备id集合
        if(irReqScreenSubtitleEmVO.getReqIrLampPostIdList() != null && irReqScreenSubtitleEmVO.getReqIrLampPostIdList().size() > 0){
            LambdaQueryWrapper<ScreenDevice> wrapperScreenPlayDevice = new LambdaQueryWrapper();
            wrapperScreenPlayDevice.in(ScreenDevice::getLampPostId, irReqScreenSubtitleEmVO.getReqIrLampPostIdList());
            List<ScreenDevice> screenDevices = screenDeviceDao.selectList(wrapperScreenPlayDevice);
            irDeviceIdList = screenDevices.stream().map(ScreenDevice::getId).distinct().collect(Collectors.toList());
        }
        if(irReqScreenSubtitleEmVO.getReqIrDeviceIdList() != null && irReqScreenSubtitleEmVO.getReqIrDeviceIdList().size() > 0){
            irDeviceIdList = irReqScreenSubtitleEmVO.getReqIrDeviceIdList();
        }
        if(emDeviceId == null){
            return result.error("不存在传感器");
        }
        if (irDeviceIdList == null || irDeviceIdList.size() == 0) {
            return result.error("不存在显示屏");
        }
        // 构建设置对象
        ScreenSubtitleEm screenSubtitleEm = new ScreenSubtitleEm();
        // 设置默认值
        screenSubtitleEm.setEmDataField("temperature,humidity,airPressure,windSpeedAverage,windDirectionAverage,pm25,pm10,noiseAverage");
        screenSubtitleEm.setNum(0);
        screenSubtitleEm.setStepInterval(50);
        screenSubtitleEm.setStep(1);
        screenSubtitleEm.setDirection("left");
        screenSubtitleEm.setAlign("top");
        screenSubtitleEm.setTypefaceSize(30);
        screenSubtitleEm.setTypefaceColor("#FFF");
        screenSubtitleEm.setBackgroundColor("#000");
        screenSubtitleEm.setCreateTime(new Date());
        screenSubtitleEm.setCreator(userId);
        // 设置页面传输值
        if(irReqScreenSubtitleEmVO.getEmDataField() != null && !"".equals(irReqScreenSubtitleEmVO.getEmDataField())){
            screenSubtitleEm.setEmDataField(irReqScreenSubtitleEmVO.getEmDataField());
        }
        if(irReqScreenSubtitleEmVO.getNum() != null){
            screenSubtitleEm.setNum(irReqScreenSubtitleEmVO.getNum());
        }
        if(irReqScreenSubtitleEmVO.getStepInterval() != null){
            screenSubtitleEm.setStepInterval(irReqScreenSubtitleEmVO.getStepInterval());
        }
        if(irReqScreenSubtitleEmVO.getStep() != null){
            screenSubtitleEm.setStep(irReqScreenSubtitleEmVO.getStep());
        }
        if(irReqScreenSubtitleEmVO.getDirection() != null && !"".equals(irReqScreenSubtitleEmVO.getDirection())){
            screenSubtitleEm.setDirection(irReqScreenSubtitleEmVO.getDirection());
        }
        if(irReqScreenSubtitleEmVO.getAlign() != null && !"".equals(irReqScreenSubtitleEmVO.getAlign())){
            screenSubtitleEm.setAlign(irReqScreenSubtitleEmVO.getAlign());
        }
        if(irReqScreenSubtitleEmVO.getTypefaceSize() != null){
            screenSubtitleEm.setTypefaceSize(irReqScreenSubtitleEmVO.getTypefaceSize());
        }
        if(irReqScreenSubtitleEmVO.getTypefaceColor() != null && !"".equals(irReqScreenSubtitleEmVO.getTypefaceColor())){
            screenSubtitleEm.setTypefaceColor(irReqScreenSubtitleEmVO.getTypefaceColor());
        }
        if(irReqScreenSubtitleEmVO.getBackgroundColor() != null && !"".equals(irReqScreenSubtitleEmVO.getBackgroundColor())){
            screenSubtitleEm.setBackgroundColor(irReqScreenSubtitleEmVO.getBackgroundColor());
        }
        if(irReqScreenSubtitleEmVO.getEmDataFieldList() != null && !"".equals(irReqScreenSubtitleEmVO.getEmDataFieldList())){
            String fields = "";
            for(String emDataField : irReqScreenSubtitleEmVO.getEmDataFieldList()){
                fields += emDataField + ",";
            }
            screenSubtitleEm.setEmDataField(fields);
        }
        screenSubtitleEm.setAreaId(irReqScreenSubtitleEmVO.getAreaId());
        screenSubtitleEm.setAreaName(irReqScreenSubtitleEmVO.getAreaName());
        IrRespScreenDeviceVO detail = screenDeviceDao.detail(irDeviceIdList.get(0));
        screenSubtitleEm.setResolvingPower(detail.getWidth() + "*" + detail.getHeight());
        screenSubtitleEm.setEmDeviceId(emDeviceId);
        screenSubtitleEm.setHtml(irReqScreenSubtitleEmVO.getHtml());
        this.baseMapper.insert(screenSubtitleEm);
        // 添加显示屏与传感器数据展示设置关联关系
        for(Integer irDeviceId : irDeviceIdList){
            ScreenSubtitleEmScreen screenSubtitleEmScreen = new ScreenSubtitleEmScreen();
            screenSubtitleEmScreen.setScreenDeviceId(irDeviceId);
            screenSubtitleEmScreen.setScreenSubtitleEmId(screenSubtitleEm.getId());
            screenSubtitleEmScreenMapper.insert(screenSubtitleEmScreen);
        }
        return result.success("添加成功");
    }

    @Override
    public Result batchDelete(String ids, HttpServletRequest request) {
        logger.info("批量删除传感器关联显示屏显示数据设置，接收参数：{}", ids);
        List<Integer> idListFromString = StringConversionUtil.getIdListFromString(ids);
        this.removeByIds(idListFromString);
        LambdaQueryWrapper<ScreenSubtitleEmScreen> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(ScreenSubtitleEmScreen::getScreenSubtitleEmId,idListFromString);
        screenSubtitleEmScreenMapper.delete(lambdaQueryWrapper);
        Result result = new Result();
        return result.success("删除成功");
    }

    @Override
    public Result updateDevice(IrReqScreenSubtitleEmVO irReqScreenSubtitleEmVO, HttpServletRequest httpServletRequest) {
        logger.info("修改传感器关联显示屏显示数据设置，接收参数：{}", irReqScreenSubtitleEmVO);
        Result result = new Result();
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        if (userId == null) {
            return result.error("添加失败，token已过期");
        }
        // 对应需要添加对应关系的显示屏id集合，与传感器id
        List<Integer> irDeviceIdList = new ArrayList<>();
        Integer emDeviceId = null;
        if(irReqScreenSubtitleEmVO.getReqEmDeviceId() == null && irReqScreenSubtitleEmVO.getReqEmLampPostId() == null){
            return result.error("请选择传感器");
        }
        if ((irReqScreenSubtitleEmVO.getReqIrDeviceIdList() == null || irReqScreenSubtitleEmVO.getReqIrDeviceIdList().size() == 0) && (irReqScreenSubtitleEmVO.getReqIrLampPostIdList() == null || irReqScreenSubtitleEmVO.getReqIrLampPostIdList().size() == 0)) {
            return result.error("请选择显示屏");
        }
        // 获取传感器设备id
        if(irReqScreenSubtitleEmVO.getReqEmLampPostId() != null){
            MeteorologicalDevice meteorologicalDevice = screenDeviceDao.selectEmDeviceByLampPostId(irReqScreenSubtitleEmVO.getReqEmLampPostId());
            emDeviceId = meteorologicalDevice.getId();
        }
        if(irReqScreenSubtitleEmVO.getReqEmDeviceId() != null){
            emDeviceId = irReqScreenSubtitleEmVO.getReqEmDeviceId();
        }
        // 获取显示屏设备id集合
        if(irReqScreenSubtitleEmVO.getReqIrLampPostIdList() != null && irReqScreenSubtitleEmVO.getReqIrLampPostIdList().size() > 0){
            LambdaQueryWrapper<ScreenDevice> wrapperScreenPlayDevice = new LambdaQueryWrapper();
            wrapperScreenPlayDevice.in(ScreenDevice::getLampPostId, irReqScreenSubtitleEmVO.getReqIrLampPostIdList());
            List<ScreenDevice> screenDevices = screenDeviceDao.selectList(wrapperScreenPlayDevice);
            irDeviceIdList = screenDevices.stream().map(ScreenDevice::getId).distinct().collect(Collectors.toList());
        }
        if(irReqScreenSubtitleEmVO.getReqIrDeviceIdList() != null && irReqScreenSubtitleEmVO.getReqIrDeviceIdList().size() > 0){
            irDeviceIdList = irReqScreenSubtitleEmVO.getReqIrDeviceIdList();
        }
        if(emDeviceId == null){
            return result.error("不存在传感器");
        }
        if (irDeviceIdList == null || irDeviceIdList.size() == 0) {
            return result.error("不存在显示屏");
        }
        // 构建设置对象
        ScreenSubtitleEm screenSubtitleEm = new ScreenSubtitleEm();
        // 设置默认值
        screenSubtitleEm.setEmDataField("temperature,humidity,airPressure,windSpeedAverage,windDirectionAverage,pm25,pm10,noiseAverage");
        screenSubtitleEm.setNum(0);
        screenSubtitleEm.setStepInterval(50);
        screenSubtitleEm.setStep(1);
        screenSubtitleEm.setDirection("left");
        screenSubtitleEm.setAlign("top");
        screenSubtitleEm.setTypefaceSize(30);
        screenSubtitleEm.setTypefaceColor("#FFF");
        screenSubtitleEm.setBackgroundColor("#000");
        screenSubtitleEm.setCreateTime(new Date());
        screenSubtitleEm.setCreator(userId);
        // 设置页面传输值
        if(irReqScreenSubtitleEmVO.getEmDataField() != null && !"".equals(irReqScreenSubtitleEmVO.getEmDataField())){
            screenSubtitleEm.setEmDataField(irReqScreenSubtitleEmVO.getEmDataField());
        }
        if(irReqScreenSubtitleEmVO.getNum() != null){
            screenSubtitleEm.setNum(irReqScreenSubtitleEmVO.getNum());
        }
        if(irReqScreenSubtitleEmVO.getStepInterval() != null){
            screenSubtitleEm.setStepInterval(irReqScreenSubtitleEmVO.getStepInterval());
        }
        if(irReqScreenSubtitleEmVO.getStep() != null){
            screenSubtitleEm.setStep(irReqScreenSubtitleEmVO.getStep());
        }
        if(irReqScreenSubtitleEmVO.getDirection() != null && !"".equals(irReqScreenSubtitleEmVO.getDirection())){
            screenSubtitleEm.setDirection(irReqScreenSubtitleEmVO.getDirection());
        }
        if(irReqScreenSubtitleEmVO.getAlign() != null && !"".equals(irReqScreenSubtitleEmVO.getAlign())){
            screenSubtitleEm.setAlign(irReqScreenSubtitleEmVO.getAlign());
        }
        if(irReqScreenSubtitleEmVO.getTypefaceSize() != null){
            screenSubtitleEm.setTypefaceSize(irReqScreenSubtitleEmVO.getTypefaceSize());
        }
        if(irReqScreenSubtitleEmVO.getTypefaceColor() != null && !"".equals(irReqScreenSubtitleEmVO.getTypefaceColor())){
            screenSubtitleEm.setTypefaceColor(irReqScreenSubtitleEmVO.getTypefaceColor());
        }
        if(irReqScreenSubtitleEmVO.getBackgroundColor() != null && !"".equals(irReqScreenSubtitleEmVO.getBackgroundColor())){
            screenSubtitleEm.setBackgroundColor(irReqScreenSubtitleEmVO.getBackgroundColor());
        }
        if(irReqScreenSubtitleEmVO.getEmDataFieldList() != null && !"".equals(irReqScreenSubtitleEmVO.getEmDataFieldList())){
            String fields = "";
            for(String emDataField : irReqScreenSubtitleEmVO.getEmDataFieldList()){
                fields += emDataField + ",";
            }
            screenSubtitleEm.setEmDataField(fields);
        }
        screenSubtitleEm.setAreaId(irReqScreenSubtitleEmVO.getAreaId());
        screenSubtitleEm.setAreaName(irReqScreenSubtitleEmVO.getAreaName());
        IrRespScreenDeviceVO detail = screenDeviceDao.detail(irDeviceIdList.get(0));
        screenSubtitleEm.setResolvingPower(detail.getWidth() + "*" + detail.getHeight());
        screenSubtitleEm.setEmDeviceId(emDeviceId);
        screenSubtitleEm.setHtml(irReqScreenSubtitleEmVO.getHtml());
        screenSubtitleEm.setId(irReqScreenSubtitleEmVO.getId());
        LambdaQueryWrapper<ScreenSubtitleEm> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ScreenSubtitleEm::getId,screenSubtitleEm.getId());
        this.baseMapper.update(screenSubtitleEm,lambdaQueryWrapper);
        // 删除旧的显示屏与传感器数据展示设置关联关系
        LambdaQueryWrapper<ScreenSubtitleEmScreen> deleteLambdaQueryWrapper = new LambdaQueryWrapper<>();
        deleteLambdaQueryWrapper.eq(ScreenSubtitleEmScreen::getScreenSubtitleEmId,screenSubtitleEm.getId());
        screenSubtitleEmScreenMapper.delete(deleteLambdaQueryWrapper);
        // 添加显示屏与传感器数据展示设置关联关系
        for(Integer irDeviceId : irDeviceIdList){
            ScreenSubtitleEmScreen screenSubtitleEmScreen = new ScreenSubtitleEmScreen();
            screenSubtitleEmScreen.setScreenDeviceId(irDeviceId);
            screenSubtitleEmScreen.setScreenSubtitleEmId(screenSubtitleEm.getId());
            screenSubtitleEmScreenMapper.insert(screenSubtitleEmScreen);
        }
        return result.success("修改成功");
    }

    @Override
    public Result getQuery(IrScreenSubtitleEmQuery irScreenSubtitleEmQuery, HttpServletRequest httpServletRequest) {
        logger.info("获取修改传感器关联显示屏显示数据设置列表(分页查询)，接收参数：{}", irScreenSubtitleEmQuery);
        // 根据分区过滤数据
        Integer userId = JavaWebTokenUtil.parserStaffIdByToken(httpServletRequest);
        User user = userService.get(userId);
        boolean flag = userService.isAdmin(userId);
        if (!flag) {
            irScreenSubtitleEmQuery.setAreaId(user.getAreaId());
        }
        // 获取修改传感器关联显示屏显示数据设置列表
        IPage<IrScreenDeviceQuery> page = new Page<IrScreenDeviceQuery>(irScreenSubtitleEmQuery.getPageNum(), irScreenSubtitleEmQuery.getPageSize());
        IPage<IrRespScreenSubtitleEmVO> dlmRespGroupVOList = this.baseMapper.query(page, irScreenSubtitleEmQuery);
        Result result = new Result();
        return result.success(dlmRespGroupVOList);
    }

    @Override
    public Result detail(Integer id, HttpServletRequest request) {
        logger.info("查询传感器关联显示屏显示数据设置详情:id:" + id);
        // 设置的数据库对象
        ScreenSubtitleEm screenSubtitleEm = this.baseMapper.selectById(id);
        // 返回对象
        IrReqScreenSubtitleEmVO irReqScreenSubtitleEmVO = new IrReqScreenSubtitleEmVO();
        BeanUtils.copyProperties(screenSubtitleEm, irReqScreenSubtitleEmVO);
        irReqScreenSubtitleEmVO.setReqEmDeviceId(screenSubtitleEm.getEmDeviceId());
        // 关联的显示屏id
        LambdaQueryWrapper<ScreenSubtitleEmScreen> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(ScreenSubtitleEmScreen::getScreenSubtitleEmId, screenSubtitleEm.getId());
        List<ScreenSubtitleEmScreen> screenSubtitleEmScreens = screenSubtitleEmScreenMapper.selectList(lambdaQueryWrapper);
        List<Integer> irDeviceIdList = screenSubtitleEmScreens.stream().map(ScreenSubtitleEmScreen::getScreenDeviceId).distinct().collect(Collectors.toList());
        irReqScreenSubtitleEmVO.setReqIrDeviceIdList(irDeviceIdList);
        // 支持的传感器数据字段转换为集合
        String emDataField = screenSubtitleEm.getEmDataField();
        if(emDataField == null){
            emDataField = "";
        }
        String[] split = emDataField.split(",");
        irReqScreenSubtitleEmVO.setEmDataFieldList(Arrays.asList(split));
        Result result = new Result();
        return result.success(irReqScreenSubtitleEmVO);
    }
}