package com.exc.street.light.sl.netty.zkzl;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @aythor xujiahaoxixi
 * @data 2017/10/19 14:56
 */
public class MessageRecvChannelInitializer extends ChannelInitializer<SocketChannel> {

    //ObjectDecoder 底层默认继承半包解码器LengthFieldBasedFrameDecoder处理粘包问题的时候，
    //消息头开始即为长度字段，占据4个字节。这里出于保持兼容的考虑
    final public static int MESSAGE_LENGTH = 4;
    private Map<String, Object> channelMap = null;
    private ChannelGroup channelGroup = null;

    public MessageRecvChannelInitializer(Map<String, Object> channelMap, ChannelGroup channelGroup) {
        this.channelMap = channelMap;
        this.channelGroup =channelGroup;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
//        pipeline.addLast("framedecoder", new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024, 0, 4, 0, 4));
        //解码器
        pipeline.addLast(new CustomByteEncoder());
//        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,1,1,0,0));
        pipeline.addLast(new IdleStateHandler(120,0,0, TimeUnit.SECONDS));
        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
        /*pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));*/
        pipeline.addLast(new MessageRecvHandler(channelMap,channelGroup));
    }
}
