package com.exc.street.light.ir.timer;

import com.exc.street.light.ir.service.ScreenDeviceService;
import com.exc.street.light.ir.service.ScreenSubtitleService;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitlePlayVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableAsync
public class EmSubtitleTimer {
    private static final Logger logger = LoggerFactory.getLogger(DeviceOnlineTimer.class);
    @Autowired
    private ScreenSubtitleService screenSubtitleService;
    private Integer x = 111111;

    /**
     * 显示屏播放气象字幕
     */
    @Async
//    @Scheduled(cron = "0 0/3 * * * ?")
    public void getInfoByDevice() {
        logger.info("显示屏播放气象字幕");
        // 发送数据至显示屏
        IrReqScreenSubtitlePlayVO irReqScreenSubtitlePlayVO = new IrReqScreenSubtitlePlayVO();
        irReqScreenSubtitlePlayVO.setIsAll(0);
        irReqScreenSubtitlePlayVO.setNum(-1);
        irReqScreenSubtitlePlayVO.setAlign("top");
        irReqScreenSubtitlePlayVO.setDirection("left");
        irReqScreenSubtitlePlayVO.setHtml("<i style='background:#000;color:#FFF'>" + x + "阿US好嗲US和</i>");
        irReqScreenSubtitlePlayVO.setPrototype("阿US好嗲US和");
        irReqScreenSubtitlePlayVO.setInterval(50);
        irReqScreenSubtitlePlayVO.setStep(1);
        List<String> numList = new ArrayList<>();
        numList.add("y60-720-30497");
        irReqScreenSubtitlePlayVO.setNumList(numList);
        x = x + 111111;
        screenSubtitleService.sendSubtitle(irReqScreenSubtitlePlayVO, null);
    }

}
