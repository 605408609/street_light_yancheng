/**
 * @filename:ControlEnergyServiceImpl 2020-10-27
 * @project dlm  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.ControlEnergyMapper;
import com.exc.street.light.dlm.service.*;
import com.exc.street.light.dlm.utils.AnalysisUtil;
import com.exc.street.light.dlm.utils.ConstantUtil;
import com.exc.street.light.dlm.utils.ProtocolUtil;
import com.exc.street.light.dlm.utils.SocketClient;
import com.exc.street.light.resource.dto.electricity.ElectricityMeterModuleDataDTO;
import com.exc.street.light.resource.entity.dlm.*;
import com.exc.street.light.resource.enums.dlm.LocationControlTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: xiezhipeng
 *
 */
@Service
public class ControlEnergyServiceImpl extends ServiceImpl<ControlEnergyMapper, ControlEnergy> implements ControlEnergyService {

    private static final Logger logger = LoggerFactory.getLogger(ControlEnergyServiceImpl.class);

    @Autowired
    private LocationControlService locationControlService;
    
    @Autowired
    private ControlEnergyDayService controlEnergyDayService;

    @Autowired
    private ControlEnergyMonthService controlEnergyMonthService;
    
    @Autowired
    private ControlEnergyYearService controlEnergyYearService;
    
    @Override
    public void insertOrUpdateControlEnergy() {
        logger.info("整点插入或更新集控能耗数据");
        // 查询所有的集中控制器信息
        List<LocationControl> locationControlList = locationControlService.list();
        if (locationControlList != null && locationControlList.size() > 0) {
            List<Integer> controlTypeIdList = locationControlList.stream().map(LocationControl::getTypeId).distinct().collect(Collectors.toList());
            for (Integer controlTypeId : controlTypeIdList) {
                List<LocationControl> controlList = locationControlList.stream().filter(e -> e.getTypeId().equals(controlTypeId)).collect(Collectors.toList());
                // 爱克集控
                if (controlTypeId == LocationControlTypeEnum.EXC.code()) {
                    for (LocationControl locationControl : controlList) {
                        if (locationControl.getMac() != null && !locationControl.getMac().isEmpty()) {
                            byte[] commandBytes = ProtocolUtil.getMeterModuleDataCommand(locationControl.getMac());
                            byte[] receiveData = SocketClient.send(locationControl.getIp(), Integer.parseInt(locationControl.getPort()), commandBytes);
                            logger.info("判断获取网关电表模块数据命令是否执行成功");
                            byte rtn = AnalysisUtil.getRtn(receiveData);
                            if (rtn == ConstantUtil.RET_1) {
                                logger.info("获取网关电表模块数据成功");
                                // 解析电表数据
                                this.analyze(receiveData);
                            } else {
                                logger.info("获取网关电表模块数据失败");
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void analyze(byte[] data) {
         List<ElectricityMeterModuleDataDTO> dataCommandResp = AnalysisUtil.getMeterModuleDataCommandResp(data);
        if (dataCommandResp != null && dataCommandResp.size() > 0) {
            // 按单个电表处理
            ElectricityMeterModuleDataDTO dataDTO = dataCommandResp.get(0);
            // 设备Mac地址
            String mac = dataDTO.getGatewayMac();
            // 总能耗
            Float totalEnergy = dataDTO.getCurrentCombinedActiveTotalEnergy();
            // 根据Mac查询集控信息
            LambdaQueryWrapper<LocationControl> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LocationControl::getMac, mac);
            LocationControl locationControl = locationControlService.getOne(wrapper);
            // 查询该集控设备最近的一条能耗数据
            ControlEnergy controlEnergyOld = baseMapper.getLastTimeEnergyByControlId(locationControl.getId());
            if (controlEnergyOld == null) {
                // 初始化
                controlEnergyOld = new ControlEnergy();
                controlEnergyOld.setControlId(locationControl.getId());
                controlEnergyOld.setAccumulationEnergy(0.0f);
            }
            // 将数据保存到数据库
            ControlEnergy controlEnergyNew = new ControlEnergy();
            controlEnergyNew.setControlId(controlEnergyOld.getControlId());
            controlEnergyNew.setCreateTime(new Date());
            controlEnergyNew.setControlTypeId(locationControl.getTypeId());
            controlEnergyNew.setAccumulationEnergy(totalEnergy);
            // 能耗增加量
            Float energy = (new BigDecimal(totalEnergy).subtract(new BigDecimal(controlEnergyOld.getAccumulationEnergy()))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            controlEnergyNew.setEnergy(energy);
            baseMapper.insert(controlEnergyNew);
            // 新增或更新年月日的能耗数据
            insertOrUpdateControlEnergy(locationControl.getId(), locationControl.getTypeId(), energy, new Date());
        }
    }

    private void insertOrUpdateControlEnergy(Integer controlId, Integer typeId, Float energy, Date date) {

        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
        String formatDay = simpleDateFormatDay.format(date);
        String formatMonth = simpleDateFormatMonth.format(date);
        String formatYear = simpleDateFormatYear.format(date);
        // 查询日能耗
        ControlEnergyDay controlEnergyDay = controlEnergyDayService.selectOneByDayTime(controlId, formatDay);
        // 查询月度能耗
        ControlEnergyMonth controlEnergyMonth = controlEnergyMonthService.selectOneByMonthTime(controlId, formatMonth);
        // 查询年度能耗
        ControlEnergyYear controlEnergyYear = controlEnergyYearService.selectOneByYearTime(controlId, formatYear);

        // 不存在新增，存在则更新
        if (controlEnergyDay != null) {
            controlEnergyDay.setCreateTime(date);
            controlEnergyDay.setEnergy((new BigDecimal(controlEnergyDay.getEnergy()).add(new BigDecimal(energy))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            controlEnergyDayService.updateById(controlEnergyDay);
        } else {
            controlEnergyDay = new ControlEnergyDay();
            controlEnergyDay.setCreateTime(date);
            controlEnergyDay.setEnergy(energy);
            controlEnergyDay.setControlId(controlId);
            controlEnergyDay.setControlTypeId(typeId);
            controlEnergyDayService.save(controlEnergyDay);
        }

        if (controlEnergyMonth != null) {
            controlEnergyMonth.setCreateTime(date);
            controlEnergyMonth.setEnergy((new BigDecimal(controlEnergyMonth.getEnergy()).add(new BigDecimal(energy))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            controlEnergyMonthService.updateById(controlEnergyMonth);
        } else {
            controlEnergyMonth = new ControlEnergyMonth();
            controlEnergyMonth.setEnergy(energy);
            controlEnergyMonth.setCreateTime(date);
            controlEnergyMonth.setControlId(controlId);
            controlEnergyMonth.setControlTypeId(typeId);
            controlEnergyMonthService.save(controlEnergyMonth);
        }

        if (controlEnergyYear != null) {
            controlEnergyYear.setEnergy((new BigDecimal(controlEnergyYear.getEnergy()).add(new BigDecimal(energy))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            controlEnergyYear.setCreateTime(date);
            controlEnergyYearService.updateById(controlEnergyYear);
        } else {
            controlEnergyYear = new ControlEnergyYear();
            controlEnergyYear.setCreateTime(date);
            controlEnergyYear.setEnergy(energy);
            controlEnergyYear.setControlId(controlId);
            controlEnergyYear.setControlTypeId(typeId);
            controlEnergyYearService.save(controlEnergyYear);
        }
    }
}