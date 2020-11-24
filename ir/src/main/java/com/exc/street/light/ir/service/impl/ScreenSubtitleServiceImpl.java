/**
 * @filename:ScreenSubtitleServiceImpl 2020-10-23
 * @project ir  V1.0
 * Copyright(c) 2018 Longshuangyang Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.ir.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exc.street.light.ir.config.parameter.ScreenApi;
import com.exc.street.light.ir.mapper.ScreenDeviceMapper;
import com.exc.street.light.ir.utils.ScreenControlUtil;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.entity.ir.ScreenSubtitle;
import com.exc.street.light.ir.mapper.ScreenSubtitleMapper;
import com.exc.street.light.ir.service.ScreenSubtitleService;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitlePlayVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:TODO(显示屏字幕表服务实现)
 * @version: V1.0
 * @author: Longshuangyang
 */
@Service
public class ScreenSubtitleServiceImpl extends ServiceImpl<ScreenSubtitleMapper, ScreenSubtitle> implements ScreenSubtitleService {
    private static final Logger logger = LoggerFactory.getLogger(ScreenSubtitleServiceImpl.class);

    @Autowired
    private ScreenSubtitleMapper screenSubtitleMapper;
    @Autowired
    private ScreenDeviceMapper screenDeviceMapper;
    @Autowired
    private ScreenApi screenApi;

    @Override
    public Result sendSubtitle(IrReqScreenSubtitlePlayVO irReqScreenSubtitlePlayVO, HttpServletRequest httpServletRequest) {
        logger.info("下发字幕，接收参数：{}", irReqScreenSubtitlePlayVO);
        Result result = new Result();
        // 验证编号列表的正确性
        List<String> deviceNumList;
        if (irReqScreenSubtitlePlayVO.getIsAll() == 1) {
            List<ScreenDevice> screenDeviceList = screenDeviceMapper.selectList(null);
            deviceNumList = screenDeviceList.stream().map(ScreenDevice::getNum).distinct().collect(Collectors.toList());
        } else {
            if (irReqScreenSubtitlePlayVO.getNumList() == null || irReqScreenSubtitlePlayVO.getNumList().size() == 0) {
                logger.info("发送字幕失败，目标显示屏编号不能为空");
                return result.error("发送字幕失败，目标显示屏编号不能为空");
            }
            LambdaQueryWrapper<ScreenDevice> lambdaQueryWrapper = new LambdaQueryWrapper<ScreenDevice>();
            lambdaQueryWrapper.in(ScreenDevice::getNum, irReqScreenSubtitlePlayVO.getNumList());
            List<ScreenDevice> screenDeviceList = screenDeviceMapper.selectList(lambdaQueryWrapper);
            deviceNumList = screenDeviceList.stream().map(ScreenDevice::getNum).distinct().collect(Collectors.toList());
        }
        if (deviceNumList == null || deviceNumList.size() == 0) {
            logger.info("发送字幕失败，目标显示屏编号无效");
            return result.error("目标显示屏编号无效");
        }

        Result sendSubtitleControl = ScreenControlUtil.sendSubtitleControl(screenApi.getIp(), screenApi.getPort(), irReqScreenSubtitlePlayVO, deviceNumList);
        logger.info("发送字幕返回信息：{}", sendSubtitleControl);
        // 获取发送成功的序列号集合
        long currentTime = System.currentTimeMillis();
        JSONObject data = (JSONObject) sendSubtitleControl.getData();
        List<JSONObject> successList = (List<JSONObject>) data.get("successObjectList");
        List<String> successSnList = new ArrayList<>();
        if (successList != null && successList.size() > 0) {
            // 序列号集合获得对应的显示屏参数
            for (JSONObject jsonObject : successList) {
                successSnList.add((String) jsonObject.get("sn"));
            }
            LambdaQueryWrapper<ScreenDevice> lambdaQueryWrapper = new LambdaQueryWrapper<ScreenDevice>();
            lambdaQueryWrapper.in(ScreenDevice::getNum, successSnList);
            List<ScreenDevice> screenDeviceList = screenDeviceMapper.selectList(lambdaQueryWrapper);
            // 构建需要添加的字幕集合
            List<ScreenSubtitle> screenSubtitleList = new ArrayList<>();
            for (ScreenDevice screenDevice : screenDeviceList) {
                ScreenSubtitle subtitleNew = new ScreenSubtitle();
                subtitleNew.setStep(irReqScreenSubtitlePlayVO.getStep());
                subtitleNew.setNum(irReqScreenSubtitlePlayVO.getNum());
                subtitleNew.setDeviceId(screenDevice.getId());
                subtitleNew.setInterval(irReqScreenSubtitlePlayVO.getInterval());
                subtitleNew.setHtml(irReqScreenSubtitlePlayVO.getHtml());
                subtitleNew.setDirection(irReqScreenSubtitlePlayVO.getDirection());
                subtitleNew.setAlign(irReqScreenSubtitlePlayVO.getAlign());
                //获取原始数据去除换行
                String prototype = irReqScreenSubtitlePlayVO.getPrototype();
                prototype = prototype.replace("\r\n", "");
                //获取中文之外的文字
                String noCnString = prototype.replaceAll("[\\u4e00-\\u9fa5]", "");
                //获取总字段长度（汉字为1，其余为0.5）
                Double stringLength = noCnString.length() * 0.5 + prototype.length() - noCnString.length();
                //获取字幕持续时长
                long subtitleTimeLong;
                if (irReqScreenSubtitlePlayVO.getNum() == 0) {
                    subtitleTimeLong = 0;
                } else if (irReqScreenSubtitlePlayVO.getNum() < 0) {
                    subtitleTimeLong = currentTime + 31536000000L;
                } else {
                    if (stringLength * irReqScreenSubtitlePlayVO.getFontSize() + screenApi.getSubtitleSpacing() >= screenDevice.getWidth()) {
                        //当一段字幕加上间隔大于屏幕宽度时，每一次字幕都是全部字加上间隔
                        //字幕第一个字到达最左边是固定屏幕长度时间，是全字符串的长度消耗时间，间隔最后一次没有则减一
                        subtitleTimeLong = (long) (stringLength * irReqScreenSubtitlePlayVO.getFontSize() * irReqScreenSubtitlePlayVO.getStep()
                                * irReqScreenSubtitlePlayVO.getInterval() * irReqScreenSubtitlePlayVO.getNum()
                                + screenDevice.getWidth() * irReqScreenSubtitlePlayVO.getStep() * irReqScreenSubtitlePlayVO.getInterval()
                                + (irReqScreenSubtitlePlayVO.getNum() - 1) * screenApi.getSubtitleSpacing() * irReqScreenSubtitlePlayVO.getInterval() * irReqScreenSubtitlePlayVO.getStep()
                                + (prototype.length() - irReqScreenSubtitlePlayVO.getNum() - 1) * irReqScreenSubtitlePlayVO.getNum() * irReqScreenSubtitlePlayVO.getStep()
                                * irReqScreenSubtitlePlayVO.getInterval());
                    } else {
                        //当小于屏幕宽度时，没有间隔，每一次滚动的长度都是固定的屏幕宽度
                        //字幕最后一次消失时，是全字符串的长度消耗时间
                        subtitleTimeLong = (long) (screenDevice.getWidth() * irReqScreenSubtitlePlayVO.getStep() * irReqScreenSubtitlePlayVO.getInterval()
                                * irReqScreenSubtitlePlayVO.getNum()
                                + stringLength * irReqScreenSubtitlePlayVO.getStep() * irReqScreenSubtitlePlayVO.getInterval() * irReqScreenSubtitlePlayVO.getNum())
                                + (prototype.length() - irReqScreenSubtitlePlayVO.getNum() - 1) * irReqScreenSubtitlePlayVO.getNum() * irReqScreenSubtitlePlayVO.getStep()
                                * irReqScreenSubtitlePlayVO.getInterval();
                    }
                }
                subtitleTimeLong = subtitleTimeLong + currentTime;
                subtitleNew.setEndTime(new Date(subtitleTimeLong));
                screenSubtitleList.add(subtitleNew);
            }
            // 删除下发成功的设备记录，添加新记录
            List<Integer> screenDeviceIdList = screenDeviceList.stream().map(ScreenDevice::getId).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<ScreenSubtitle> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.in(ScreenSubtitle::getDeviceId, screenDeviceIdList);
            screenSubtitleMapper.delete(deleteWrapper);
            for (ScreenSubtitle screenSubtitle : screenSubtitleList) {
                screenSubtitleMapper.insert(screenSubtitle);
            }
        }
        // 返回值
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sendSubtitleControl", sendSubtitleControl.getData());
        // 显示屏的设备集合
        LambdaQueryWrapper<ScreenDevice> lambdaQueryWrapper = new LambdaQueryWrapper<ScreenDevice>();
        lambdaQueryWrapper.in(ScreenDevice::getNum, irReqScreenSubtitlePlayVO.getNumList());
        List<ScreenDevice> screenDeviceList = screenDeviceMapper.selectList(lambdaQueryWrapper);
        // 返回成功与失败的，各自设备名称字符串
        String nodeNameSuccessString = "";
        String nodeNameFailString = "";
        for (int i = 0; i < screenDeviceList.size(); i++) {
            boolean contains = successSnList.contains(screenDeviceList.get(i).getNum());
            if (contains) {
                if ("".equals(nodeNameSuccessString)) {
                    nodeNameSuccessString = screenDeviceList.get(i).getName();
                } else {
                    nodeNameSuccessString = nodeNameSuccessString + "," + screenDeviceList.get(i).getName();
                }
            } else {
                if ("".equals(nodeNameFailString)) {
                    nodeNameFailString = screenDeviceList.get(i).getName();
                } else {
                    nodeNameFailString = nodeNameFailString + "," + screenDeviceList.get(i).getName();
                }
            }
        }
        if ("".equals(nodeNameFailString)) {
            logger.info("发送字幕至：" + nodeNameSuccessString + ",全部成功");
            return result.success("发送字幕全部成功");
        } else if ("".equals(nodeNameSuccessString)) {
            logger.info("发送字幕至：" + nodeNameFailString + ",全部失败");
            result = result.error("发送字幕全部失败");
            result.setData(jsonObject);
            return result;
        } else {
            logger.info("成功发送字幕至：" + nodeNameSuccessString
                    + "，下发失败显示屏：" + nodeNameFailString);
            result = result.error("发送字幕部分失败");
            result.setData(jsonObject);
            return result;
        }
    }
}