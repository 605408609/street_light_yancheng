package com.exc.street.light.dlm;

import com.exc.street.light.dlm.service.ControlLoopSceneStatusService;
import com.exc.street.light.dlm.utils.AnalysisUtil;
import com.exc.street.light.dlm.utils.HexUtil;
import com.exc.street.light.dlm.utils.ProtocolUtil;
import com.exc.street.light.dlm.utils.SocketClient;
import com.exc.street.light.resource.dto.dlm.ControlLoopTimerDTO;
import com.exc.street.light.resource.dto.electricity.ControlCommand;
import com.exc.street.light.resource.dto.electricity.Timer;
import com.exc.street.light.resource.entity.electricity.CanTiming;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Xiaok
 * @Date: 2020/11/10 9:11
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationControlTest {

    @Autowired
    private ControlLoopSceneStatusService sceneStatusService;

    private static Integer controlId = 58;
    private static List<Integer> loopIdList = Arrays.asList(169, 170, 171, 172, 173, 174, 175, 176);
    private static String mac = "00-14-97-32-70-73".toUpperCase();
    private static String ip = "192.168.112.25";
    private static Integer port = 9999;

    private static Integer loopNum = 1;
    //0-关 1-开
    private static Integer tagValue = 1;

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");


    /**
     * 下发16个 回路场景
     */
    @Test
    @Ignore
    public void orderLoopScene() {
        long start = System.currentTimeMillis();
        log.info("下发回路场景开始时间:{}", start);
        sceneStatusService.deleteSceneStatusByControlId(controlId);
        sceneStatusService.saveSceneStatusRecord(controlId, loopIdList);
        sceneStatusService.saveLoopScene(mac);
        long end = System.currentTimeMillis();
        log.info("下发回路场景结束时间:{} ", end);
        log.info("下发回路场景耗时:{} ms", end - start);
    }

    /**
     * 回路开关控制
     */
    @Test
    @Ignore
    public void loopControl() {
        long start = System.currentTimeMillis();
        log.info("下发回路控制开始时间:{} ", start);
        ControlCommand cc = new ControlCommand();
        cc.setControlId(loopNum);
        cc.setDeviceAddress(66);
        cc.setValue(tagValue == 0 ? 1 : 0);
        cc.setTagId(0);
        byte[] bytes = ProtocolUtil.setControlCommand(mac, cc);
        boolean b = SocketClient.sendData(ip, port, bytes);
        long end = System.currentTimeMillis();
        log.info("下发回路控制结束时间:{} ", end);
        log.info("下发回路控制耗时:{} ms，结果：{}", end - start, b);
    }

    /**
     * 设置周期定时 星期定时
     */
    @Test
    @Ignore
    public void setTimer() {
        long start = System.currentTimeMillis();
        log.info("下发定时开始时间:{} ", start);
        //30秒后
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 61);
        //1分钟后
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.SECOND, 61 * 2);
        //时间处理 30s后
        String[] times = sdf.format(c.getTime()).split(":");
        byte[] time = new byte[3];
        time[0] = HexUtil.getByte(Integer.parseInt(times[0]));
        time[1] = HexUtil.getByte(Integer.parseInt(times[1]));
        time[2] = HexUtil.getByte(Integer.parseInt(times[2]));

        int[] cycleTypes = new int[]{1, 2, 3, 5, 7};//星期几
        int cycleType = ProtocolUtil.getCycleType(cycleTypes, 0, 1);
        //定时执行-周期定时对象
        CanTiming can = new CanTiming()
                .setCycleType(cycleType)//周期
                .setIsExecute(1) //固定为1
                .setTagId(AnalysisUtil.getSceneIdByLoopNum(loopNum, true)) //回路1 开
                .setTime(HexUtil.bytesTohex(time))//时间点
                .setType(1) //定时执行()
                .setStartDate(new Date())//不设置就持续到结束时间
                .setEndDate(null);//不设置就永久持续


        //时间处理 60s后
        String[] times2 = sdf.format(c1.getTime()).split(":");
        byte[] time2 = new byte[3];
        time2[0] = HexUtil.getByte(Integer.parseInt(times2[0]));
        time2[1] = HexUtil.getByte(Integer.parseInt(times2[1]));
        time2[2] = HexUtil.getByte(Integer.parseInt(times2[2]));
        //定时执行-年月日定时
        CanTiming can1 = new CanTiming()
                .setCycleType(1)//年月日 该项固定为1
                .setIsExecute(1) //固定为1
                .setTagId(AnalysisUtil.getSceneIdByLoopNum(loopNum, false)) //回路1 关
                .setTime(HexUtil.bytesTohex(time2))//时间点
                .setType(1) //定时执行()
                .setStartDate(new Date())//不设置就持续到结束时间
                .setEndDate(null);//不设置就永久持续

        //今日日落时间 17:40:05
        long l = 1605001205000L;
        long currentL = System.currentTimeMillis();
        long l1 = (currentL - l) / 1000L / 60L;
        //2:日出之前 3:日出之后 4:日落之前 5:日落之后
        int lonLatType = 4;
        //偏移当前时间两分钟
        int offset = 2;
        if (l1 > 0) {
            lonLatType = 5;
        }
        offset = Math.abs((int) l1 + offset);
        //经纬度执行-2:日出之前 3:日出之后 4:日落之前 5:日落之后
        CanTiming can2 = new CanTiming()
                .setIsExecute(1) //固定为1
                .setTagId(AnalysisUtil.getSceneIdByLoopNum(loopNum, false)) //回路1 开
                .setType(lonLatType) //2:日出之前 3:日出之后 4:日落之前 5:日落之后
                .setMinuteValue(offset)//日出日落时间偏移量(单位分钟)
                .setStartDate(new Date())//不设置就持续到结束时间
                .setEndDate(null);//不设置就永久持续

        List<CanTiming> cList = new ArrayList<>();
        //cList.add(can);
        //cList.add(can1);
        cList.add(can2);
        byte[] bytes = ProtocolUtil.setTimer(mac, new Timer().setCanTimings(cList));
        boolean b = SocketClient.sendData(ip, port, bytes);
        long end = System.currentTimeMillis();
        log.info("下发定时结束时间:{} ", end);
        log.info("下发定时控制耗时:{} ms，结果：{}", end - start, b);
    }

    @Test
    public void setTimer2() {
        List<ControlLoopTimerDTO> dtoList = new ArrayList<>();
        // 年月日定时/周期定时
        for (int i = 1; i <=
                8; i++) {

            ControlLoopTimerDTO dto = new ControlLoopTimerDTO()
                    .setLoopNum(i) //回路编号
                    .setIsOpen(true)     //动作： true-开 false-关
                    .setCycleTypes(new int[]{1, 2, 3, 5, 6}) //选择周期 没有填null
                    .setStartDate(null)  //开始时间 没有传null
                    .setEndDate(null)    //结束时间 没有传null
                    .setTime("20:20:00") //HH:mm:ss 时间点
                    .setType(1)          //固定为1
                    ;
            ControlLoopTimerDTO dto1 = new ControlLoopTimerDTO()
                    .setLoopNum(i) //回路编号
                    .setIsOpen(false)     //动作： true-开 false-关
                    .setCycleTypes(new int[]{1, 2, 3, 5, 6}) //选择周期 没有填null
                    .setStartDate(null)  //开始时间 没有传null
                    .setEndDate(null)    //结束时间 没有传null
                    .setTime("20:20:00") //HH:mm:ss 时间点
                    .setType(1)          //固定为1
                    ;
            dtoList.add(dto);
            dtoList.add(dto1);
        }
        /*long l = 1605001205000L;
        long l1 = (System.currentTimeMillis() - l) / 1000L / 60L;
        int offset = ((int) l1) + 2;*/

        /*// 经纬度定时
        ControlLoopTimerDTO dto1 = new ControlLoopTimerDTO()
                .setLoopNum(loopNum)    //回路编号
                .setIsOpen(false)       //回路动作： true-开 false-关
                .setStartDate(null)     //开始时间 没有传null
                .setEndDate(null)       //结束时间 没有传null
                .setType(5)             //2:日出之前 3:日出之后 4:日落之前 5:日落之后
                .setMinuteValue(offset) //偏移分钟数 没有传null
                ;

        dtoList.add(dto1);*/
        byte[] bytes = ProtocolUtil.setTimer(mac, dtoList);
        //返回结果
        boolean b = SocketClient.sendData(ip, port, bytes);
        System.out.println(b);
    }

}
