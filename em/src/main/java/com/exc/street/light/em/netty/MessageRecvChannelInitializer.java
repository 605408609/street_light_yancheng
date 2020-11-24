package com.exc.street.light.em.netty;

import com.exc.street.light.em.config.HttpApi;
import com.exc.street.light.em.mapper.MeteorologicalHistoryDao;
import com.exc.street.light.em.mapper.MeteorologicalRealDao;
import com.exc.street.light.em.service.MeteorologicalDeviceService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @aythor xujiahaoxixi
 * @data 2017/10/19 14:56
 */
@Component
public class MessageRecvChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${netty.readerIdleTime}")
    private int readerIdleTIme;

    @Value("${netty.writerIdleTime}")
    private int writeIdleTIme;

    @Value("${netty.allIdleTime}")
    private int allIdleTIme;

    //ObjectDecoder 底层默认继承半包解码器LengthFieldBasedFrameDecoder处理粘包问题的时候，
    //消息头开始即为长度字段，占据4个字节。这里出于保持兼容的考虑
    final public static int MESSAGE_LENGTH = 4;
    private Map<String, Object> handlerMap = null;
    private MeteorologicalDeviceService meteorologicalDeviceService;
    private MeteorologicalRealDao meteorologicalRealDao;
    private MeteorologicalHistoryDao meteorologicalHistoryDao;
    private HttpApi httpApi;

    public MessageRecvChannelInitializer(Map<String, Object> handlerMap, MeteorologicalDeviceService meteorologicalDeviceService,
                                         MeteorologicalRealDao meteorologicalRealDao, MeteorologicalHistoryDao meteorologicalHistoryDao, HttpApi httpApi) {
        this.handlerMap = handlerMap;
        this.meteorologicalDeviceService = meteorologicalDeviceService;
        this.meteorologicalRealDao = meteorologicalRealDao;
        this.meteorologicalHistoryDao = meteorologicalHistoryDao;
        this.httpApi = httpApi;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
//        pipeline.addLast("framedecoder", new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024, 0, 4, 0, 4));
        //解码器

        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new IdleStateHandler(readerIdleTIme, writeIdleTIme, allIdleTIme, TimeUnit.SECONDS));
        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
        /*pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));*/
        pipeline.addLast(new MessageRecvHandler(handlerMap, meteorologicalDeviceService, meteorologicalRealDao, meteorologicalHistoryDao, httpApi));
    }
}
