package com.exc.street.light.electricity.netty;

import com.exc.street.light.electricity.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Channel的自定义
 * @author xujiahaoxixi
 * @date 2017/10/19 14:56
 */
public class MessageRecvChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final static Logger logger = LoggerFactory.getLogger(MessageRecvChannelInitializer.class);
    /**
     * ObjectDecoder 底层默认继承半包解码器LengthFieldBasedFrameDecoder处理粘包问题的时候，
     * 消息头开始即为长度字段，占据4个字节。这里出于保持兼容的考虑
     */
    final public static int MESSAGE_LENGTH = 4;
    private Map<String, Object> handlerMap = null;

    public MessageRecvChannelInitializer(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //ObjectDecoder的基类半包解码器LengthFieldBasedFrameDecoder的报文格式保持兼容。因为底层的父类LengthFieldBasedFrameDecoder
        //的初始化参数即为super(maxObjectSize, 0, 4, 0, 4);
       /* pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageRecvChannelInitializer.MESSAGE_LENGTH, 0, MessageRecvChannelInitializer.MESSAGE_LENGTH));

        //利用LengthFieldPrepender回填补充ObjectDecoder消息报文头
        pipeline.addLast(new LengthFieldPrepender(MessageRecvChannelInitializer.MESSAGE_LENGTH));*/
//        pipeline.addLast("framedecoder", new LengthFieldBasedFrameDecoder(1024 * 1024 * 1024, 12, 2, 0, 0));
        //解码器
        pipeline.addLast(new MessageToByteEncoder() {
            @Override
            protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
                logger.info("返回的数据为:{}", HexUtil.bytesTohex((byte[]) o));
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream oo = new ObjectOutputStream(bo);
//                logger.info("1");
                oo.writeObject(o);
//                logger.info("2");
                byte[] bytes = bo.toByteArray();
//                logger.info("bytes[]为:{}",bytes);
//                logger.info("3");
                byte[] newBytes = new byte[bytes.length - 27];
//                logger.info("4 length:{}",newBytes.length);
                System.arraycopy(bytes, 27, newBytes, 0, newBytes.length);
//                logger.info("5");
//                logger.info("byte[]为:{}",newBytes);
                byteBuf.writeBytes(newBytes);
            }
        });
        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
        /*pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));*/
        pipeline.addLast(new MessageRecvHandler(handlerMap));
    }
}
