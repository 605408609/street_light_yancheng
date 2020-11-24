package com.exc.street.light.electricity.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.SocketAddress;
import java.util.Map;


/**
 * @author xujiahaoxixi
 * @date 2017/10/19 14:56
 * Netty每一个连接生成一个Handle，Handle的核心方法
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MessageRecvHandler.class);

    private final Map<String, Object> handlerMap;

    public MessageRecvHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
     * 传输过来的数据处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf request = (ByteBuf) msg;
        //数据的处理
        MessageRecvInitializeTask recvTask = new MessageRecvInitializeTask(request, ctx);

        //不要阻塞nio线程，复杂的业务逻辑丢给专门的线程池
        MessageRecvExecutor.submit(recvTask);
//        StaticUtils.number++;
//        logger.debug("数据写入完毕{}", StaticUtils.number);
    }

    /**
     * 接收到连接后会触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        logger.info("接收到新的连接,socketAddress={}", socketAddress);
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.debug("完成操作,正常返回");
        ctx.flush();
    }

    /**
     * 异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //网络有异常要关闭通道,并删除保存的对象
        GatewayService.removeGatewayChannel((SocketChannel) ctx.channel());
        ctx.close();
    }
}