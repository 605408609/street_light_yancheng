package com.exc.street.light.sl.config.zkzl;//package com.exc.street.light.ic.config;
//
//import com.exc.street.light.ic.dto.Message;
//import com.exc.street.light.ic.service.IcChannelService;
//import com.exc.street.light.ic.util.CacheConstant;
//import com.exc.street.light.ic.util.ConstantUtil;
//import com.exc.street.light.ic.util.HexUtil;
//import com.exc.street.light.ic.util.ProtocolUtil;
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
///**
// * @Author: XuJiaHao
// * @Description:
// * @Date: Created in 20:35 2020/6/22
// * @Modified:
// */
//@Component
//@Slf4j
//public class PubSubMessageReceive {
//
//    @Autowired
//    NettyService nettyService;
//
//    @Autowired
//    IcChannelService icChannelService;
//
//    @Autowired
//    RedisTemplate redisTemplate;
//
//    private ArrayList<Message> arrayList;
//
//    public void getMessage(String object){
//        //序列化对象
//        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(Message.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,
//                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        //过时api
//        //objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        seria.setObjectMapper(objectMapper);
//        Message message = (Message)seria.deserialize(object.getBytes());
//
//        //处理接收的请求
//        String address = message.getAddress();
//        byte function = HexUtil.hexStringToBytes(message.getFunction())[0];
//        int isDevice = message.getIsDevice();
//        int isAll = message.getIsAll();
//        int num = message.getNum();
//        log.info("接收到订阅消息:address:{},function:{},isDevice:{},isAll:{},num:{}",address,function,isDevice,isAll,num);
//        if(isDevice == 1){
//            switch (function){
//                case (ConstantUtil.RESPONSE_STATS_ELECTRICITY): //获取电流状态
//                    break;
//                case ConstantUtil.RESPONSE_STATS_LONGLAT: //获取经纬度信息
//                    break;
//                case ConstantUtil.RESPONSE_STATS_SCENE: //场景状态获取
//                    break;
//                case ConstantUtil.RESPONSE_STATS_TIME: //获取网关时间
//                    break;
//                case ConstantUtil.RESPONSE_STATS_SCENE_ISOPEN: //获取回路场景开关状态
//                    break;
//                case ConstantUtil.RESPONSE_CONTROL_LONGLAT: //设置经纬度
//                    break;
//                case ConstantUtil.RESPONSE_CONTROL_TIME: //设置网关时间
//                    break;
//
//                default:
//                    break;
//            }
//        }else{
//            if(isAll == 1){
//                switch (function){
//                    case ConstantUtil.RESPONSE_STATS_LOOP_ELECTRICITY: //获取回路电流信息
//                        break;
//                }
//            }else{
//                switch (function){
//                    case ConstantUtil.RESPONSE_CONTROL_SCENE_ISOPEN: //设置回路开关状态
//                        break;
//                    case ConstantUtil.RESPONSE_CONTROL_SCENE: //设置回路场景
//                        byte[] data = ProtocolUtil.getSceneStats(address,num);
//                        icChannelService.sendData(address,data,0,null,0,0,0);
//                       break;
//                }
//            }
//        }
//
//    }
//}
