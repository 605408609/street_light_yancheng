package com.exc.street.light.ed.serializer;

import com.alibaba.fastjson.JSON;
import com.exc.street.light.ed.po.KafkaMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: XuJiaHao
 * @Description: 序列化
 * @Date: Created in 10:24 2020/4/3
 * @Modified:
 */
public class PersonSerializer implements Serializer<KafkaMessage> {
    private static final Logger logger = LoggerFactory.getLogger(PersonSerializer.class);

    private static Gson gson;
    static {
        gson = new GsonBuilder().create();
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        logger.info("自定义的序列化组件--configure");
    }

    @Override
    public byte[] serialize(String s, KafkaMessage kafkaTest) {
        logger.info("自定义的序列化组件--serialize");
        return JSON.toJSONBytes(kafkaTest);
    }

    @Override
    public void close() {
        logger.info("自定义的序列化组件--close");
    }
}

