package com.exc.street.light.pb.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.exc.street.light.pb.service.RadioDeviceService;
import com.exc.street.light.resource.dto.pb.RadioDeviceImportDataDTO;
import com.exc.street.light.resource.entity.pb.RadioDevice;
import com.exc.street.light.resource.vo.resp.ImportDeviceFailVO;
import com.exc.street.light.resource.vo.resp.ImportDeviceResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * 广播设备模板的读取类
 *
 * @Author: Xiaok
 * @Date: 2020/11/13 11:28
 */
public class RadioDeviceImportListener extends AnalysisEventListener<RadioDeviceImportDataDTO> {
    private static final Logger log = LoggerFactory.getLogger(RadioDeviceImportListener.class);

    private RadioDeviceService service;
    /**
     * 返回结果
     */
    private ImportDeviceResultVO resultVO;
    /**
     * 当前用户可查看的所有灯杆信息
     */
    private Map<String, Integer> lampPostInfoMap;
    /**
     * 当前用户可查看的已绑定广播设备的灯杆信息
     */
    private Map<String, Integer> bindDeviceLampPostInfoMap;
    /**
     * 已存在的设备编号 用作校验
     */
    private Set<String> numSet;
    /**
     * 已存在的设备名称 用作校验
     */
    private Set<String> nameSet;
    /**
     * 已存在的雷托终端设备编号 用作校验
     */
    private Set<Integer> termIdSet;
    /**
     * 广播设备集合,用于存放校验通过的广播设备信息
     */
    private List<RadioDevice> radioDeviceList;
    /**
     * 创建时间
     */
    private Date now;

    /**
     * excel的序号 从1开始
     */

    private int count = 1;

    private static final int NAME_LEN = 10;
    private static final int NUM_LEN = 36;
    private static final int TERM_ID_MAX_NUM = (int) Math.pow(10, 8);
    private static final int LAMP_POST_NAME_LEN = 100;
    private static final int MODEL_LEN = 25;
    private static final int IP_LEN = 25;
    private static final int MAC_LEN = 25;
    private static final int FACTORY_LEN = 25;


    public RadioDeviceImportListener() {
        now = new Date();
    }

    public RadioDeviceImportListener(RadioDeviceService service, ImportDeviceResultVO resultVO, Map<String, Integer> bindDeviceLampPostInfoMap, Map<String, Integer> lampPostInfo, Set<String> numSet, Set<String> nameSet, Set<Integer> termIdSet, List<RadioDevice> radioDeviceList) {
        this.service = service;
        this.resultVO = resultVO;
        this.bindDeviceLampPostInfoMap = bindDeviceLampPostInfoMap;
        this.lampPostInfoMap = lampPostInfo;
        this.numSet = numSet;
        this.nameSet = nameSet;
        this.termIdSet = termIdSet;
        this.radioDeviceList = radioDeviceList;
        now = new Date();
    }

    @Override
    public void invoke(RadioDeviceImportDataDTO radioDeviceImportDTO, AnalysisContext analysisContext) {
        boolean validRes = validate(radioDeviceImportDTO);
        if (validRes) {
            RadioDevice device = new RadioDevice();
            BeanUtils.copyProperties(radioDeviceImportDTO, device);
            device.setCreateTime(now).setLampPostId(lampPostInfoMap.get(radioDeviceImportDTO.getLampPostName()))
                    .setNetworkState(0);
            radioDeviceList.add(device);
            resultVO.setSuccessNum(resultVO.getSuccessNum() + 1);
        }
        count++;
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("广播设备数据解析完成");
    }

    private boolean validate(RadioDeviceImportDataDTO data) {
        //进行校验
        ImportDeviceFailVO failVO = new ImportDeviceFailVO().setRows(String.valueOf(count));
        List<String> failMsgList = new ArrayList<>();
        if (StringUtils.isBlank(data.getName())) {
            failMsgList.add("设备名称不得为空");
        } else if (data.getName().length() > NAME_LEN) {
            failMsgList.add("设备名称的长度不得超过" + NAME_LEN);
        }
        if (data.getTermId() == null) {
            failMsgList.add("雷拓终端ID不得为空");
        } else if (data.getTermId() > TERM_ID_MAX_NUM) {
            failMsgList.add("雷拓终端ID的长度不得超过" + TERM_ID_MAX_NUM);
        }
        if (StringUtils.isBlank(data.getNum())) {
            failMsgList.add("设备编号不得为空");
        } else if (data.getNum().length() > NUM_LEN) {
            failMsgList.add("设备编号的长度不得超过" + NUM_LEN);
        }
        if (StringUtils.isBlank(data.getLampPostName())) {
            failMsgList.add("所属灯杆名称不得为空");
        } else if (data.getLampPostName().length() > LAMP_POST_NAME_LEN) {
            failMsgList.add("所属灯杆名称的长度不得超过" + LAMP_POST_NAME_LEN);
        }
        if (StringUtils.isBlank(data.getIp())) {
            failMsgList.add("IP不得为空");
        } else if (data.getIp().length() > IP_LEN) {
            failMsgList.add("IP的长度不得超过" + IP_LEN);
        }
        if (StringUtils.isBlank(data.getMac())) {
            failMsgList.add("MAC地址不得为空");
        } else if (data.getMac().length() > MAC_LEN) {
            failMsgList.add("MAC地址的长度不得超过" + MAC_LEN);
        }
        if (StringUtils.isNotBlank(data.getFactory()) && data.getFactory().length() > FACTORY_LEN) {
            failMsgList.add("设备厂商的长度不得超过" + FACTORY_LEN);
        }
        if (StringUtils.isNotBlank(data.getModel()) && data.getModel().length() > MODEL_LEN) {
            failMsgList.add("设备型号的长度不得超过" + MODEL_LEN);
        }
        if (nameSet.contains(data.getName())) {
            failMsgList.add("设备名称已经存在");
        }
        if (numSet.contains(data.getNum())) {
            failMsgList.add("设备编号已经存在");
        }
        if (termIdSet.contains(data.getTermId())) {
            failMsgList.add("雷拓终端ID已经存在");
        }
        if (bindDeviceLampPostInfoMap.containsKey(data.getLampPostName())) {
            failMsgList.add("该灯杆已绑定广播设备");
        } else if (!lampPostInfoMap.containsKey(data.getLampPostName())) {
            failMsgList.add("不存在该灯杆名称:" + data.getLampPostName());
        }
        if (!failMsgList.isEmpty()) {
            failVO.setFailMsgList(failMsgList);
            resultVO.setFailNum(resultVO.getFailNum() + 1);
            resultVO.getFailInfoList().add(failVO);
            return false;
        }
        return true;
    }
}
