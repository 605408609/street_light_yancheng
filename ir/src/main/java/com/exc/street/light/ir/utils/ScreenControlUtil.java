package com.exc.street.light.ir.utils;

import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.ir.task.ScreenPlayingProgramTask;
import com.exc.street.light.resource.core.Const;
import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.ir.ScreenDevice;
import com.exc.street.light.resource.utils.BaseConstantUtil;
import com.exc.street.light.resource.vo.IrMaterialVO;
import com.exc.street.light.resource.vo.IrProgramMaterialVO;
import com.exc.street.light.resource.vo.IrProgramVO;
import com.exc.street.light.resource.vo.req.IrReqScreenOperateVO;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitlePlayVO;
import com.exc.street.light.resource.vo.req.IrReqSendProgramVO;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * 显示屏信息发布群控制工具类
 *
 * @author Longshuangyang
 * @date 2019/08/15
 */
public class ScreenControlUtil {
    private static final Logger logger = LoggerFactory.getLogger(ScreenControlUtil.class);

    /**
     * 发送请求设置显示屏开关控制器
     *
     * @param reqScreenOperateVO
     * @param snList
     * @return
     */
    public static Result screenOpenControl(String ip, Integer port, IrReqScreenOperateVO reqScreenOperateVO, List<String> snList) {
        //获取参数json
        String bool = "true";
        if (reqScreenOperateVO.getState() == 0) {
            bool = "false";
        }
        String playingProgram = ScreenSendUtil.setScreenOpen(bool);
        logger.info("设置显示屏开关json参数：{}", playingProgram.replaceAll("\n", ""));
        return groupSend(ip, port, snList, playingProgram);
    }

    /**
     * 发送请求设置显示屏音量控制器
     *
     * @param reqScreenOperateVO
     * @param snList
     * @return
     */
    public static Result volumeControl(String ip, Integer port, IrReqScreenOperateVO reqScreenOperateVO, List<String> snList) {
        //获取参数json
        String setVolume = ScreenSendUtil.setVolume(reqScreenOperateVO.getVolume());
        logger.info("设置显示屏音量json参数：{}", setVolume.replaceAll("\n", ""));
        return groupSend(ip, port, snList, setVolume);
    }

    /**
     * 发送请求设置显示屏亮度控制器
     *
     * @param reqScreenOperateVO
     * @param snList
     * @return
     */
    public static Result brightnessControl(String ip, Integer port, IrReqScreenOperateVO reqScreenOperateVO, List<String> snList) {
        //获取参数json
        String setBrightness = ScreenSendUtil.setBrightness(reqScreenOperateVO.getBrightness());
        logger.info("设置显示屏亮度json参数：{}", setBrightness.replaceAll("\n", ""));
        return groupSend(ip, port, snList, setBrightness);
    }

    /**
     * 发送请求获取显示屏网络类型控制器
     *
     * @param snList
     * @return
     */
    public static Result getNetworkTypeControl(String ip, Integer port, List<String> snList) {
        //获取参数json
        String getNetworkType = ScreenSendUtil.getNetworkType();
        logger.info("获取显示屏网络类型json参数：{}", getNetworkType.replaceAll("\n", ""));
        return groupSend(ip, port, snList, getNetworkType);
    }

    /**
     * 发送请求获取显示屏宽控制器
     *
     * @param snList
     * @return
     */
    public static Result getScreenWidthControl(String ip, Integer port, List<String> snList) {
        //获取参数json
        String getScreenWidth = ScreenSendUtil.getScreenWidth();
        logger.info("获取显示屏宽json参数：{}", getScreenWidth.replaceAll("\n", ""));
        return groupSend(ip, port, snList, getScreenWidth);
    }

    /**
     * 发送请求获取显示屏高控制器
     *
     * @param snList
     * @return
     */
    public static Result getScreenHeightControl(String ip, Integer port, List<String> snList) {
        //获取参数json
        String getScreenHeight = ScreenSendUtil.getScreenHeight();
        logger.info("获取显示屏高json参数：{}", getScreenHeight.replaceAll("\n", ""));
        return groupSend(ip, port, snList, getScreenHeight);
    }

    /**
     * 发送请求获取显示屏亮度控制器
     *
     * @param snList
     * @return
     */
    public static Result getBrightnessControl(String ip, Integer port, List<String> snList) {
        //获取参数json
        String getBrightness = ScreenSendUtil.getBrightness();
        logger.info("获取显示屏亮度json参数：{}", getBrightness.replaceAll("\n", ""));
        return groupSend(ip, port, snList, getBrightness);
    }

    /**
     * 发送请求获取显示屏音量控制器
     *
     * @param snList
     * @return
     */
    public static Result getVolumeControl(String ip, Integer port, List<String> snList) {
        //获取参数json
        String getVolume = ScreenSendUtil.getVolume();
        logger.info("获取显示屏音量json参数：{}", getVolume.replaceAll("\n", ""));
        return groupSend(ip, port, snList, getVolume);
    }

    /**
     * 发送请求获取显示屏开关状态控制器
     *
     * @param snList
     * @return
     */
    public static Result isScreenOpenControl(String ip, Integer port, List<String> snList) {
        //获取参数json
        String isScreenOpen = ScreenSendUtil.isScreenOpen();
        logger.info("获取显示屏开关状态json参数：{}", isScreenOpen.replaceAll("\n", ""));
        return groupSend(ip, port, snList, isScreenOpen);
    }

    /**
     * 发送请求获取显示屏播放节目控制器
     *
     * @param snList
     * @return
     */
    public static Result getPlayingProgramControl(String ip, Integer port, List<String> snList) {
        //获取参数json
        String getPlayingProgram = ScreenSendUtil.getPlayingProgram();
        logger.info("获取显示屏播放节目json参数：{}", getPlayingProgram.replaceAll("\n", ""));
        return groupSend(ip, port, snList, getPlayingProgram);
    }

    /**
     * 发送请求下发节目控制器
     *
     * @param irReqSendProgramVO
     * @param screenDevice
     * @param irProgramVO
     * @param irProgramMaterialVOList
     * @param irMaterialVOList
     * @param screenDeviceNumList
     * @return
     */
    public static Result sendProgramControl(String ip, Integer port, String servicePath, String progressInterface, IrReqSendProgramVO irReqSendProgramVO, ScreenDevice screenDevice, IrProgramVO irProgramVO, List<IrProgramMaterialVO> irProgramMaterialVOList, List<IrMaterialVO> irMaterialVOList, List<String> screenDeviceNumList) {
        // 获取参数json
        String sendProgram = ScreenSendUtil.sendProgram(servicePath, progressInterface, irReqSendProgramVO, screenDevice, irProgramVO, irProgramMaterialVOList, irMaterialVOList);
        logger.info("下发节目json参数：{}", sendProgram.replaceAll("\n", ""));
        return groupSend(ip, port, screenDeviceNumList, sendProgram);
    }

    /**
     * 发送请求取消节目控制器
     *
     * @param snList
     * @return
     */
    public static Result deleteProgramControl(String ip, Integer port, List<String> snList) {
        //获取参数json
        String deleteProgram = ScreenSendUtil.deleteProgram();
        logger.info("取消节目json参数：{}", deleteProgram.replaceAll("\n", ""));
        return groupSend(ip, port, snList, deleteProgram);
    }

    /**
     * 发送请求发送字幕控制器
     *
     * @param ip
     * @param port
     * @param irReqScreenSubtitlePlayVO
     * @param snList
     * @return
     */
    public static Result sendSubtitleControl(String ip, Integer port, IrReqScreenSubtitlePlayVO irReqScreenSubtitlePlayVO, List<String> snList) {
        //获取参数json
        String sendSubtitle = ScreenSendUtil.sendSubtitle(irReqScreenSubtitlePlayVO);
        logger.info("发送字幕json参数：{}", sendSubtitle.replaceAll("\n", ""));
        return groupSend(ip, port, snList, sendSubtitle);
    }

    /**
     * 显示屏群控发送
     *
     * @param snList
     * @param jsonString
     * @return
     */
    public static Result groupSend(String ip, Integer port, List<String> snList, String jsonString) {
        Result resultre = new Result();
        JSONObject jsonObject = new JSONObject();
        if (snList == null || snList.size() == 0) {
            return resultre.error("没有设备");
        }
        //构建多线程
        Collection<ScreenPlayingProgramTask> tasks = new ArrayList<>();
        for (String sn : snList) {
            ScreenPlayingProgramTask screenPlayingProgramTask = new ScreenPlayingProgramTask(ip, port, sn, jsonString);
            tasks.add(screenPlayingProgramTask);
        }
        //创建线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("channel-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(BaseConstantUtil.THREADPOOLSIZE_2,
                BaseConstantUtil.THREADPOOLSIZE_2, 0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(), namedThreadFactory);
        //启动多线程
        try {
            List<Future<Result>> futures = executorService.invokeAll(tasks);
            int successNum = 0;
            int defaultNum = 0;
            List<String> defaultSnList = new ArrayList<>();
            List<JSONObject> JSONObjectList = new ArrayList<>();
            for (Future<Result> future : futures) {
                if (future != null) {
                    Result result = future.get();
                    if (result.getCode() == Const.CODE_SUCCESS) {
                        JSONObjectList.add((JSONObject) result.getData());
                        successNum += 1;
                    } else {
                        defaultSnList.add((String) result.getData());
                        defaultNum += 1;
                    }
                }
            }
            jsonObject.put("successNum", successNum);
            jsonObject.put("defaultNum", defaultNum);
            jsonObject.put("defaultSnList", defaultSnList);
            jsonObject.put("successObjectList", JSONObjectList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        return resultre.success(jsonObject);
    }


}
