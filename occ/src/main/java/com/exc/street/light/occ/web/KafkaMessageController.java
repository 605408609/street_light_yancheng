/**
 * @filename:AhPlayController 2020-03-16
 * @project occ  V1.0
 * Copyright(c) 2020 Huang Min Co. Ltd.
 * All right reserved.
 */
package com.exc.street.light.occ.web;

import com.exc.street.light.occ.config.KafkaTopicConfig;
import com.exc.street.light.occ.po.KafkaMessage;
import com.exc.street.light.occ.service.KafkaMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>自动生成工具：mybatis-dsc-generator</p>
 *
 * <p>说明： Controller</P>
 * @version: V1.0
 * @author: Huang Min
 * @time 2020-03-16
 *
 */
@Api(description = "", value = "")
@RestController
@CrossOrigin
@RequestMapping("/kafka")
public class KafkaMessageController {
    @Autowired
    private KafkaMessageService kafkaMessageService;
    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;


    /**
     * @explain 向kafka发送数据包
     * @author Huang Min
     * @time 2020-03-16
     */
    @PostMapping(value = "/send")
    @ApiOperation(value = "发送信息", notes = "作者：Huang Min")
    public void sendMsg(@RequestBody KafkaMessage kafkaMessage) {
        kafkaMessageService.sendMessage(kafkaTopicConfig.getKafkaTopicName()[0],kafkaMessage);
    }
}