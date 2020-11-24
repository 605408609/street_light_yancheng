package com.exc.street.light.em.netty;

import com.exc.street.light.em.config.HttpApi;
import com.exc.street.light.em.mapper.MeteorologicalHistoryDao;
import com.exc.street.light.em.mapper.MeteorologicalRealDao;
import com.exc.street.light.em.service.MeteorologicalDeviceService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * @author xujiahao
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MessageRecvHandler.class);
    private MeteorologicalDeviceService meteorologicalDeviceService;
    private MeteorologicalRealDao meteorologicalRealDao;
    private MeteorologicalHistoryDao meteorologicalHistoryDao;
    private HttpApi httpApi;
    private int lossConnectCount = 0;


    public MessageRecvHandler(Map<String, Object> handlerMap, MeteorologicalDeviceService meteorologicalDeviceService,
                              MeteorologicalRealDao meteorologicalRealDao, MeteorologicalHistoryDao meteorologicalHistoryDao, HttpApi httpApi) {
        this.meteorologicalDeviceService = meteorologicalDeviceService;
        this.meteorologicalRealDao = meteorologicalRealDao;
        this.meteorologicalHistoryDao = meteorologicalHistoryDao;
        this.httpApi = httpApi;
    }

    //传输过来的数据处理
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf request = (ByteBuf) msg;
        logger.info("------------------接收到数据-----------------------");
        String arr = null;
        MessageRecvInitializeTask recvTask = new MessageRecvInitializeTask(request, arr, ctx, meteorologicalDeviceService, meteorologicalRealDao, meteorologicalHistoryDao, httpApi);
        //不要阻塞nio线程，复杂的业务逻辑丢给专门的线程池
        MessageRecvExecutor.submit(recvTask);
        logger.info("数据写入完毕");


//        Runnable sendTask = new Runnable() {
//            @Override
//            public void run() {
//                sendTaskLoop:
//                for (; ; ) {
//                    logger.debug("task is beginning...");
//                    try {
//                        Map<String, SocketChannel> map = GatewayService.getChannels();
//                        Iterator<String> it = map.keySet().iterator();
//                        while (it.hasNext()) {
//                            String key = it.next();
//                            SocketChannel obj = map.get(key);
//                            logger.info("channel id is: " + key);
//                            logger.info("channel: " + obj.isActive());
//                            byte[] bytes = {(byte) 0x30, (byte) 0x52, (byte) 0x30, (byte) 0x0D, (byte) 0x0A};
////                            byte[] bytes = {(byte) 0x79, (byte) 0x82, (byte) 0x79};
////                            String sData = "ORO";
////                            byte[] bytes = sData.getBytes();
//                            logger.info("发送数据：{}", bytes);
////                            obj.writeAndFlush(bytes);
//                            ctx.writeAndFlush(bytes);
//                        }
//                    } catch (Exception e) {
//                        break sendTaskLoop;
//                    }
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        new Thread(sendTask).start();
//        logger.info("持续发送数据中......");
    }

    //接收到连接后会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("接收到新的连接");
        super.channelActive(ctx);
        String uuid = ctx.channel().id().asLongText();
        GatewayService.addGatewayChannel(uuid, (SocketChannel) ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("完成操作,正常返回");
        ctx.flush();
    }

    /**
     * 异常关闭管道
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("数据发送完毕，管道异常关闭，{}", cause.getMessage());
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
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}