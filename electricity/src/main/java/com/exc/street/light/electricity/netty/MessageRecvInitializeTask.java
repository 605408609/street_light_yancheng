package com.exc.street.light.electricity.netty; /**
 * @aythor xujiahaoxixi
 * @data 2017/10/19 14:56
 */

import com.exc.street.light.electricity.util.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * @author EXC
 */
public class MessageRecvInitializeTask implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(MessageRecvInitializeTask.class);

    private ByteBuf request;
    private ChannelHandlerContext ctx = null;

    public MessageRecvInitializeTask(ByteBuf request, ChannelHandlerContext ctx) {
        this.request = request;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        //业务处理部分
        byte[] response = null;
        byte[] req = new byte[request.readableBytes()];
        request.readBytes(req);
        request.release();
        //自研去掉加密
//        req = AESUtil.decrypt(req);
        logger.info("接收到数据:{}", HexUtil.bytesTohex(req));
        //获取连接的IP地址
        String clientIp = null;
        //获取功能码
        byte controlId = AnalysisUtil.getControlId(req);
        logger.info("功能码为：{}", controlId);
        //获取应答地址
        String address = ArrayUtil.getAddress(req);
        logger.info("Mac地址为：{}", address);

        if (StringUtils.isBlank(address)) {
            logger.error("Mac地址为空,req = {}", req);
            return;
        }
        if (GatewayService.getGatewayChannel(address) == null) {
            GatewayService.addGatewayChannel(address, (SocketChannel) ctx.channel());
        }

        switch (controlId) {

            //心跳包解析
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_5):
                try {
                    InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
                    clientIp = inSocket.getAddress().getHostAddress();
                    logger.debug("心跳包：ip:{}", clientIp);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                response = ProtocolUtil.responseHeartbeat(address);
                break;
            //变化数据上传解析
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_21):
                //告警数据上传解析
            case (ConstantUtil.CONTROL_IDENTIFIER_EXC_22):
                response = ProtocolUtil.getProtocol(address, controlId);
                break;
            default:
                break;
        }
        logger.info("准备应答");
        //应答数据到客户端 response必须有数据才能应答，不然不能调用该方法
        if (response != null) {
//            ctx.writeAndFlush(AESUtil.encrypt(response)).addListener(new ChannelFutureListener() {
            ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.info("应答完毕:{},功能码:{}", address, controlId);
//                StaticUtils.endTime = TimeUtils.getMillisecond();
//                logger.debug("应答时间为{}毫秒", (StaticUtils.endTime - StaticUtils.beginTime));
                }
            });
        }
        //关闭Handle连接
        ctx.close();
        logger.info("该连接已经断开！");
        //解析数据并处理
        Analysis.receiveData(req, clientIp);
    }

}
