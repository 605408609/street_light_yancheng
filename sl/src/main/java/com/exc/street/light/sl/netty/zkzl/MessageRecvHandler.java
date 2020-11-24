package com.exc.street.light.sl.netty.zkzl;

import com.exc.street.light.sl.utils.ByteBufUtil;
import com.exc.street.light.sl.utils.HexUtil;
import com.exc.street.light.sl.utils.ZkzlProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author LeiJing
 * @date 2020/08/26
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(MessageRecvHandler.class);
    private int lossConnectCount = 0;
    private ConcurrentMap<String, Object> channelMap;
    private ConcurrentMap<Object, String> localMap = new ConcurrentHashMap<>();
    private String BEGIN_MESSAGE = "0x10005";
    private ChannelGroup channelGroup;

    public MessageRecvHandler(Map<String, Object> channelMap, ChannelGroup channelGroup) {
        this.channelMap = (ConcurrentMap<String, Object>) channelMap;
        this.channelGroup = channelGroup;
    }

    //传输过来的数据处理（集中控制器接收到的数据）
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf request = (ByteBuf) msg;
        logger.info("------------------ZKZL接收到数据-----------------------");
        MessageRecvInitializeTask recvTask = new MessageRecvInitializeTask(request, null, ctx);
        //不要阻塞nio线程，复杂的业务逻辑丢给专门的线程池
        MessageRecvExecutor.submit(recvTask);
        logger.debug("------------------ZKZL业务处理完毕---------------------");

//        logger.info("------------------ZKZL接收到数据-----------------------");
//        ByteBuf data = (ByteBuf) msg;
//        byte[] requestData = ByteBufUtil.getByteBuf(data);
//        logger.info("接收到的数据为:{}", HexUtil.bytesTohex(requestData));
//        data.release();
//        //设备地址： 04 19 11 00---（集中器 ID： 19040011）
//        String concentratorId = ZkzlProtocolUtil.getConcentratorId(requestData);
//        this.channelMap.put(concentratorId, ctx);
//        logger.info("集中器 ID：{}", concentratorId);
//        //获取APN
//        byte ApnByte = requestData[12];
//        logger.info("功能码：{}", HexUtil.byteToHex(ApnByte));
//        //获取Fn的数据单元格式，信息类 DT（02 01 = F10）
//        String Fn = ZkzlProtocolUtil.getFn(requestData);
//        logger.info("Fn：{}", Fn);
//        //判断功能码
//        if (ApnByte == (byte) 0x00) {
//            //确认∕否认（ AFN=00H）,命令执行结果返回
//            if (Fn.equals("F1")) {
//                //全部确认
//                logger.info("命令执行成功");
//            }
//            if (Fn.equals("F2")) {
//                //全部否认
//                logger.info("命令执行失败");
//            }
//            if (Fn.equals("F3")) {
//                //执行多个命令时成功或失败
//                logger.info("执行多个命令");
//            }
//            //下发命令复位，收到集中控制器上一条命令执行返回结果后才能执行下一个命令
//        }
//        if (ApnByte == (byte) 0x02) {
//            //链路接口检测（ AFN=02H）
//            if (Fn.equals("F1")) {
//                logger.info("接收到 {} 注册包",concentratorId);
//                //注册包
//                byte[] bytes = ZkzlProtocolUtil.responseRegisteredConfirm(concentratorId);
//                //返回注册包确认帧
//                ZkzlProtocolUtil.sendData(bytes);
//                logger.info("返回注册包确认帧");
//            }
//            if (Fn.equals("F3")) {
//                logger.info("接收到 {} 心跳包",concentratorId);
//                //心跳包
//                byte[] bytes = ZkzlProtocolUtil.responseHeartbeatConfirm(concentratorId);
//                //返回心跳包确认帧
//                ZkzlProtocolUtil.sendData(bytes);
//                logger.info("返回心跳包确认帧");
//            }
//        }
//        if (ApnByte == (byte) 0x0E) {
//            //灯具故障信息上报（ AFN=0EH）
//            if (Fn.equals("F1")) {
//                //灯具故障解析并保存
//                logger.info("灯具故障解析并保存");
//            }
//        }
    }

    //接收到连接后会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("ZKZL接收到新的连接");
        channelGroup.add(ctx.channel());
        localMap.put(ctx, BEGIN_MESSAGE);
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("------------------完成操作,正常返回-----------------------");
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
        logger.error("数据发送完毕，管道异常关闭，{}", cause);
        channelGroup.remove(ctx.channel());
        localMap.remove(ctx);
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
                    channelGroup.remove(ctx.channel());
                    localMap.remove(ctx);
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}