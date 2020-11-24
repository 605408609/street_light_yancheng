package com.exc.street.light.ed.serializer;

import com.alibaba.fastjson.JSON;
import com.exc.street.light.ed.po.KafkaMessage;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: XuJiaHao
 * @Description:
 * @Date: Created in 11:08 2020/4/3
 * @Modified:
 */
public class PersonDeserializer  implements Deserializer<KafkaMessage> {
    private static final Logger logger = LoggerFactory.getLogger(PersonDeserializer.class);

    @Override
    public void configure(Map<String, ?> map, boolean b) {

        }

    @Override
    public KafkaMessage deserialize(String s, byte[] bytes) {
        logger.info("自定义的反序列化-deserialize");
        return JSON.parseObject(bytes, KafkaMessage.class);
        }

    @Override
    public void close() {

        }
}

