package com.exc.street.light.sl.netty.shuncom;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xujiahaoxixi
 * @date 2017/10/19 14:56
 */
@Component
public class MessageRecvChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${shuncom.netty.readerIdleTime}")
    private int readerIdleTIme;

    @Value("${shuncom.netty.writerIdleTime}")
    private int writeIdleTIme;

    @Value("${shuncom.netty.allIdleTime}")
    private int allIdleTIme;

    //ObjectDecoder 底层默认继承半包解码器LengthFieldBasedFrameDecoder处理粘包问题的时候，
    //消息头开始即为长度字段，占据4个字节。这里出于保持兼容的考虑
    final public static int MESSAGE_LENGTH = 4;
    private Map<String, Object> handlerMap = null;

    public MessageRecvChannelInitializer(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //pipeline.addLast("framedecoder", new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024, 0, 4, 0, 4));

        //针对顺舟云盒消息的解码器
        pipeline.addLast("framedecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 2, 6, 0));
        pipeline.addLast(new ObjectEncoder());
        //pipeline.addLast(new IdleStateHandler(readerIdleTIme, writeIdleTIme, allIdleTIme, TimeUnit.SECONDS));
        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
        /*pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));*/
        pipeline.addLast(new MessageRecvHandler(handlerMap));
    }
}
