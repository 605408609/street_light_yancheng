/**
 * @filename:SystemDeviceTypeTimingModeServiceImpl 2020-08-26
 * @project sl  V1.0
 * Copyright(c) 2018 xiezhipeng Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.sl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.SystemDeviceTypeTimingMode;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceTypeTimingModeVO;
import com.exc.street.light.sl.mapper.SystemDeviceTypeTimingModeMapper;
import com.exc.street.light.sl.service.SystemDeviceTypeTimingModeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 * @version: V1.0
 * @author: xiezhipeng
 */
@Service
public class SystemDeviceTypeTimingModeServiceImpl extends ServiceImpl<SystemDeviceTypeTimingModeMapper, SystemDeviceTypeTimingMode> implements SystemDeviceTypeTimingModeService {

    private static final Logger logger = LoggerFactory.getLogger(SystemDeviceTypeTimingModeServiceImpl.class);

    @Override
    public Result deviceTypeTimingModeByIdList(List<Integer> deviceTypeIdList, Integer idSynchro, HttpServletRequest request) {
        logger.info("DeviceTypeTimingModeByIdList - 设备类型支持的定时方式 ltTypeIdList=[{}] idSynchro=[{}]", deviceTypeIdList, idSynchro);
        Result<List<SlRespSystemDeviceTypeTimingModeVO>> result = new Result<>();
        // 返回对象集合
        List<SlRespSystemDeviceTypeTimingModeVO> voList = new ArrayList<>();
        if (deviceTypeIdList != null && deviceTypeIdList.size() > 0) {
            // 未勾选一键同步
            if (idSynchro == 0) {
                for (Integer deviceTypeId : deviceTypeIdList) {
                    SlRespSystemDeviceTypeTimingModeVO timingModeVO = new SlRespSystemDeviceTypeTimingModeVO();
                    LambdaQueryWrapper<SystemDeviceTypeTimingMode> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SystemDeviceTypeTimingMode::getDeviceTypeId, deviceTypeId);
                    List<SystemDeviceTypeTimingMode> timingModeList = baseMapper.selectList(wrapper);
                    List<Integer> timingModeIdList = timingModeList.stream().map(SystemDeviceTypeTimingMode::getTimingModeId).collect(Collectors.toList());
                    timingModeVO.setDeviceTypeId(deviceTypeId);
                    timingModeVO.setTimingModeIdList(timingModeIdList);
                    voList.add(timingModeVO);
                }
            }
            // 勾选一键同步
            if (idSynchro == 1) {
                // 查询所选设备下的所有定时模式id
                LambdaQueryWrapper<SystemDeviceTypeTimingMode> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(SystemDeviceTypeTimingMode::getDeviceTypeId, deviceTypeIdList);
                List<SystemDeviceTypeTimingMode> timingModeList = baseMapper.selectList(wrapper);
                List<Integer> timingModeIdList = timingModeList.stream().map(SystemDeviceTypeTimingMode::getTimingModeId).collect(Collectors.toList());
                if (deviceTypeIdList.size() == 1) {
                    // 如果设备类型id集合的数量为1时也勾选了一键同步，这时就不需要取交集了，否则就会过滤为空值
                    SlRespSystemDeviceTypeTimingModeVO timingModeVO = new SlRespSystemDeviceTypeTimingModeVO();
                    timingModeVO.setTimingModeIdList(timingModeIdList);
                    voList.add(timingModeVO);
                } else {
                    // 过滤出共同支持的定时模式id
                    // 获取定时方式所支持的设备类型
                    Map<Integer, Set<Integer>> timingMap = timingModeList.stream().collect(Collectors.groupingBy(SystemDeviceTypeTimingMode::getTimingModeId, Collectors.mapping(SystemDeviceTypeTimingMode::getDeviceTypeId, Collectors.toSet())));
                    // 获取所有设备类型
                    List<Integer> allDeviceIdList = timingModeList.stream().map(SystemDeviceTypeTimingMode::getDeviceTypeId).distinct().collect(Collectors.toList());
                    // 找出支持所有设备类型的定时模式
                    List<Integer> modeIdList = timingMap.entrySet().stream().filter(e -> e.getValue().size() == allDeviceIdList.size())
                            .map(Map.Entry::getKey).collect(Collectors.toList());
                    SlRespSystemDeviceTypeTimingModeVO timingModeVO = new SlRespSystemDeviceTypeTimingModeVO();
                    timingModeVO.setTimingModeIdList(modeIdList);
                    voList.add(timingModeVO);
                }
            }
            return result.success(voList);
        }
        return null;
    }
}