package com.exc.street.light.electricity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.electricity.mapper.CanAlarmDataMapper;
import com.exc.street.light.electricity.service.CanAlarmDataService;
import com.exc.street.light.electricity.util.AnalysisUtil;
import com.exc.street.light.resource.entity.electricity.CanAlarmData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/07/31.
 */
@Service
@Transactional
public class CanAlarmDataServiceImpl extends ServiceImpl<CanAlarmDataMapper, CanAlarmData> implements CanAlarmDataService {

    @Override
    public void analyze(byte[] data) {
        List<CanAlarmData> alarmData = AnalysisUtil.getAlarmData(data);
    }
}
