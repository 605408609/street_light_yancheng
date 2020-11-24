package com.exc.street.light.electricity.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.exc.street.light.electricity.service.ElectricityNodeService;
import com.exc.street.light.resource.dto.electricity.ElectricityNodeImportDataDTO;
import com.exc.street.light.resource.entity.electricity.ElectricityNode;
import com.exc.street.light.resource.vo.resp.ImportDeviceFailVO;
import com.exc.street.light.resource.vo.resp.ImportDeviceResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 路灯网关模板的读取类
 *
 * @Author: Xiaok
 * @Date: 2020/11/13 11:28
 */
public class ElectricityNodeImportListener extends AnalysisEventListener<ElectricityNodeImportDataDTO> {
    private static final Logger log = LoggerFactory.getLogger(ElectricityNodeImportListener.class);

    private ElectricityNodeService service;
    /**
     * 返回结果
     */
    private ImportDeviceResultVO resultVO;
    /**
     * 当前用户可查看的已绑定路灯网关的灯杆信息
     */
    private Map<String, Integer> bindDeviceLampPostInfoMap;
    /**
     * 当前用户可查看的所有灯杆信息
     */
    private Map<String, Integer> lampPostInfoMap;
    /**
     * 已存在的设备编号 用作校验
     */
    private Set<String> numSet;
    /**
     * 已存在的设备名称 用作校验
     */
    private Set<String> nameSet;
    /**
     * 已存在的mac地址 用作校验
     */
    private Set<String> macSet;
    /**
     * 路灯网关集合,用于存放校验通过的路灯网关信息
     */
    private List<ElectricityNode> deviceList;
    /**
     * 创建时间
     */
    private Date now;

    /**
     * excel的序号 从2开始
     */

    private int count = 2;

    private static final int NAME_LEN = 10;
    private static final int NUM_LEN = 36;
    private static final int LAMP_POST_NAME_LEN = 100;
    private static final int IP_LEN = 25;
    private static final int PORT_MAX_NUM = 0XFFFF;
    private static final int MAC_LEN = 17;

    private static final Pattern PATTERN_MAC = Pattern.compile("^[A-F0-9]{2}(-[A-F0-9]{2}){5}$");
    private static final Pattern PATTERN_IP = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");


    public ElectricityNodeImportListener() {
        this.now = new Date();
    }

    public ElectricityNodeImportListener(ElectricityNodeService service, ImportDeviceResultVO resultVO, Map<String, Integer> bindDeviceLampPostInfoMap, Map<String, Integer> lampPostInfoMap, Set<String> numSet, Set<String> nameSet, Set<String> macSet, List<ElectricityNode> deviceList) {
        this.service = service;
        this.resultVO = resultVO;
        this.bindDeviceLampPostInfoMap = bindDeviceLampPostInfoMap;
        this.lampPostInfoMap = lampPostInfoMap;
        this.numSet = numSet;
        this.nameSet = nameSet;
        this.macSet = macSet;
        this.deviceList = deviceList;
        this.now = new Date();
    }

    @Override
    public void invoke(ElectricityNodeImportDataDTO importDataDTO, AnalysisContext analysisContext) {
        boolean validRes = validate(importDataDTO);
        if (validRes) {
            ElectricityNode device = new ElectricityNode();
            BeanUtils.copyProperties(importDataDTO, device);
            device.setCreateTime(now).setLampPostId(lampPostInfoMap.get(importDataDTO.getLampPostName()))
                    .setIsOffline(1);
            deviceList.add(device);
            resultVO.setSuccessNum(resultVO.getSuccessNum() + 1);
        }
        count++;
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param analysisContext 1
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        log.info("路灯网关导入数据解析完成");
    }

    private boolean validate(ElectricityNodeImportDataDTO data) {
        //进行校验
        ImportDeviceFailVO failVO = new ImportDeviceFailVO().setRows(String.valueOf(count));
        List<String> failMsgList = new ArrayList<>();
        if (StringUtils.isBlank(data.getName())) {
            failMsgList.add("设备名称不得为空");
        } else if (data.getName().length() > NAME_LEN) {
            failMsgList.add("设备名称的长度不得超过" + NAME_LEN);
        }
        if (StringUtils.isBlank(data.getMac())) {
            failMsgList.add("MAC地址不得为空");
        } else if (data.getMac().length() > MAC_LEN) {
            failMsgList.add("MAC地址的长度不得超过" + MAC_LEN);
        } else {
            data.setMac(data.getMac().toUpperCase());
            if (!PATTERN_MAC.matcher(data.getMac()).find()) {
                failMsgList.add("MAC地址格式或者大小写错误");
            }
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
        } else {
            if (!PATTERN_IP.matcher(data.getIp()).find()) {
                failMsgList.add("IP格式错误");
            }
        }
        if (data.getPort() == null) {
            failMsgList.add("端口号不可为空");
        } else if (data.getPort() <= 0 || data.getPort() > PORT_MAX_NUM) {
            failMsgList.add("端口号须在0~" + PORT_MAX_NUM + "之间");
        }
        if (nameSet.contains(data.getName())) {
            failMsgList.add("设备名称已经存在");
        }
        if (numSet.contains(data.getNum())) {
            failMsgList.add("设备编号已经存在");
        }
        if (macSet.contains(data.getMac())) {
            failMsgList.add("mac地址已经存在");
        }
        if (bindDeviceLampPostInfoMap.containsKey(data.getLampPostName())) {
            failMsgList.add("该灯杆已绑定路灯网关");
        } else if (!lampPostInfoMap.containsKey(data.getLampPostName())) {
            failMsgList.add("不存在该灯杆:" + data.getLampPostName());
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
