package com.exc.street.light.dlm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exc.street.light.dlm.mapper.SingleLampParamDao;
import com.exc.street.light.dlm.service.LampPositionService;
import com.exc.street.light.dlm.service.SingleLampParamService;
import com.exc.street.light.resource.entity.sl.LampLoopType;
import com.exc.street.light.resource.entity.sl.LampPosition;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.resource.vo.resp.DlmRespDevicePublicParVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceParameterVO;
import com.exc.street.light.resource.vo.resp.SlRespSystemDeviceVO;
import com.exc.street.light.resource.vo.sl.SingleLampParamRespVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:TODO(服务实现)
 *
 * @version: V1.0
 * @author: HuangJinHao
 *
 */
@Service
public class SingleLampParamServiceImpl extends ServiceImpl<SingleLampParamDao, SingleLampParam> implements SingleLampParamService {

    private static final Logger logger = LoggerFactory.getLogger(SingleLampParamServiceImpl.class);

    @Autowired
    LampPositionService lampPositionService;

    @Override
    public List<DlmRespDevicePublicParVO> getDlmRespDeviceVOList(List<Integer> slLampPostIdList) {
        List<DlmRespDevicePublicParVO> dlmRespDeviceVOList = this.baseMapper.getDlmRespDeviceVOList(slLampPostIdList);
        List<SlRespSystemDeviceVO> slRespSystemDeviceVOList = (List<SlRespSystemDeviceVO>) (List) dlmRespDeviceVOList.stream().map(DlmRespDevicePublicParVO::getDlmRespDevice).collect(Collectors.toList());
        if (slRespSystemDeviceVOList != null) {
            List<LampPosition> lampPositionList = lampPositionService.list();
            Map<Integer, String> lampPositionMap = lampPositionList.stream().collect(Collectors.toMap(LampPosition::getId, LampPosition::getPosition));
            List<Integer> specialDeviceTypeList = Arrays.asList(1, 2, 5, 6);
            for (SlRespSystemDeviceVO slRespLampDeviceVO : slRespSystemDeviceVOList) {
                List<SlRespSystemDeviceParameterVO> slRespSystemDeviceParameterVOS = slRespLampDeviceVO.getSlRespSystemDeviceParameterVOS();
                if (slRespSystemDeviceParameterVOS != null) {
                    for (SlRespSystemDeviceParameterVO slRespSystemDeviceParameterVO : slRespSystemDeviceParameterVOS) {
                        String name = slRespSystemDeviceParameterVO.getName();
                        if ("亮度".equals(name)) {
                            String parameterValue = slRespSystemDeviceParameterVO.getParameterValue();
                            if (parameterValue != null && parameterValue.length() > 0) {
                                int brightness = Integer.parseInt(parameterValue);
                                slRespLampDeviceVO.setBrightness(brightness);
                            }
                        }
                        if ("灯具位置".equals(name)) {
                            String parameterValue = slRespSystemDeviceParameterVO.getParameterValue();
                            if (parameterValue != null && parameterValue.length() > 0) {
                                Integer lampPositionId = Integer.parseInt(parameterValue);
                                slRespLampDeviceVO.setLampPositionId(lampPositionId);
                                String position = lampPositionMap.get(lampPositionId);
                                if (position != null) {
                                    slRespLampDeviceVO.setLampPosition(position);
                                }
                            }
                        }

                    }
                }
                if (specialDeviceTypeList.contains(slRespLampDeviceVO.getDeviceTypeId())) {
                    String numTemp = slRespLampDeviceVO.getNum();
                    String deviceNum = String.valueOf(Integer.parseInt(numTemp, 16));
                    String prefix = "";
                    for (int i = 0; i < 8 - deviceNum.length(); i++) {
                        prefix += "0";
                    }
                    deviceNum = prefix + deviceNum;
                    slRespLampDeviceVO.setNum(deviceNum);
                }
            }
        }
        return dlmRespDeviceVOList;
    }

    @Override
    public List<SlRespSystemDeviceVO> getSystemDeviceList(List<Integer> slLampPostIdList) {
        return this.baseMapper.getSystemDeviceList(slLampPostIdList);
    }

    @Override
    public List<LampLoopType> loopTypeList() {
        return this.baseMapper.loopTypeList();
    }
}
