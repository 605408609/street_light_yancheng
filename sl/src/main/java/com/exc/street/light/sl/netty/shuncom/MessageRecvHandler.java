package com.exc.street.light.sl.netty.shuncom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * @author xujiahao
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MessageRecvHandler.class);
    private int lossConnectCount = 0;


    public MessageRecvHandler(Map<String, Object> handlerMap) {
    }

    /**
     * 传输过来的数据处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf request = (ByteBuf) msg;
        logger.debug("------------------接收到顺舟云盒数据-----------------------");
        MessageRecvInitializeTask recvTask = new MessageRecvInitializeTask(request, null, ctx);
        //不要阻塞nio线程，复杂的业务逻辑丢给专门的线程池
        MessageRecvExecutor.submit(recvTask);
        logger.debug("------------------顺舟云盒数据写入完毕---------------------");
    }

    /**
     * 接收到连接后会触发该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("顺舟云盒-接收到新的连接");
        super.channelActive(ctx);
        String uuid = ctx.channel().id().asLongText();
        GatewayService.addGatewayChannel(uuid, (SocketChannel) ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.debug("顺舟云盒-完成操作,正常返回");
        ctx.flush();
    }

    /**
     * 客户端主动断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String id = ctx.channel().id().asLongText();
        //删除map中的连接对象
        GatewayService.removeGatewayChannel(id);
        ctx.fireChannelInactive();
    }

    /**
     * 异常关闭管道
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("顺舟云盒-数据发送完毕，管道异常关闭，{}", cause.getMessage());
        String id = ctx.channel().id().asLongText();
        //删除map中的连接对象
        GatewayService.removeGatewayChannel(id);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                lossConnectCount++;
                if (lossConnectCount > 2) {
                    logger.error("超时连接正常关闭");
                    String id = ctx.channel().id().asLongText();
                    //删除map中的连接对象
                    GatewayService.removeGatewayChannel(id);
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}