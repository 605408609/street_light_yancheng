package com.exc.street.light.em.netty; /**
 * @aythor Longshuangyang
 * @data 2017/10/19 14:56
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.exc.street.light.em.config.HttpApi;
import com.exc.street.light.em.mapper.MeteorologicalHistoryDao;
import com.exc.street.light.em.mapper.MeteorologicalRealDao;
import com.exc.street.light.em.service.MeteorologicalDeviceService;
import com.exc.street.light.em.util.AnalyzeUtil;
import com.exc.street.light.resource.entity.em.MeteorologicalDevice;
import com.exc.street.light.resource.entity.em.MeteorologicalHistory;
import com.exc.street.light.resource.entity.em.MeteorologicalReal;
import com.exc.street.light.resource.utils.HexUtil;
import com.exc.street.light.resource.utils.HttpUtil;
import com.exc.street.light.resource.vo.req.IrReqScreenSubtitlePlayVO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.net.InetSocketAddress;
import java.util.*;


/**
 * 处理任务
 * @author EXC
 */
public class MessageRecvInitializeTask implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(MessageRecvInitializeTask.class);

    private ByteBuf request = null;
    private String response = null;
    private byte[] requestbytes = null;
    private ChannelHandlerContext ctx = null;
    private MeteorologicalDeviceService meteorologicalDeviceService = null;
    private MeteorologicalHistoryDao meteorologicalHistoryDao = null;
    private MeteorologicalRealDao meteorologicalRealDao = null;
    private HttpApi httpApi = null;

    public String getResponse() {
        return response;
    }

    public ByteBuf getRequest() {
        return request;
    }

    public void setRequest(ByteBuf request) {
        this.request = request;
    }

    MessageRecvInitializeTask(ByteBuf request, String response, ChannelHandlerContext ctx, MeteorologicalDeviceService meteorologicalDeviceService,
                              MeteorologicalRealDao meteorologicalRealDao, MeteorologicalHistoryDao meteorologicalHistoryDao, HttpApi httpApi) {
        this.request = request;
        this.response = response;
        this.ctx = ctx;
        this.meteorologicalDeviceService = meteorologicalDeviceService;
        this.meteorologicalRealDao = meteorologicalRealDao;
        this.meteorologicalHistoryDao = meteorologicalHistoryDao;
        this.httpApi = httpApi;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        logger.debug("正在运行的线程ID为{},名称为{}", thread.getId(), thread.getName());
        RequestBean bean;
        //try {
        bean = ByteBufAndString.getByteBufLength(request);
        requestbytes = bean.getReq();
        //释放资源
        request.release();
        logger.info("接收数据：{}", requestbytes);
        String requestString = HexUtil.bytesTohex(requestbytes).replaceAll(" ", "");
        logger.info("接收编号：{}", requestString);
        logger.info("数据：{}", new String(requestbytes));
        Map<String, SocketChannel> map = GatewayService.getChannels();
        MeteorologicalDevice device = null;
        LambdaQueryWrapper<MeteorologicalDevice> wrapper = null;
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
        int port = insocket.getPort();

        //去除无用字符
        String content = new String(requestbytes).replaceAll(" ", "").replaceAll("\r", "").replaceAll("\n", "");
        //获取设备MAC地址
        String mac = AnalyzeUtil.getMac(content);
        //根据mac地址查询
        if (StringUtils.isNotBlank(mac)) {
            SocketChannel channel = map.get(mac);
            if (channel == null || ctx.channel() != channel) {
                for (Map.Entry<String, SocketChannel> channelEntry : map.entrySet()) {
                    SocketChannel socketChannel = channelEntry.getValue();
                    if (socketChannel == ctx.channel()) {
                        GatewayService.addGatewayChannel(mac, socketChannel);
                    }
                }
            }
            // 获取气象设备信息并更新设备状态为在线
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MeteorologicalDevice::getNum, mac.toUpperCase()).or().eq(MeteorologicalDevice::getNum, mac.toLowerCase());
            wrapper.last("limit 1");
            device = meteorologicalDeviceService.getOne(wrapper);
        }
        if (device == null && clientIP != null) {
            //根据IP获取气象设备
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MeteorologicalDevice::getIp, clientIP);
            wrapper.last("limit 1");
            device = meteorologicalDeviceService.getOne(wrapper);
        }
        String num = null;
        //根据心跳编号获取设备
        if (device == null && requestString != null) {
            // 判断是否为心跳编号
            if (requestString.length() == 10 && "305230".equals(requestString.substring(0, 6))) {
                num = requestString;
                wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(MeteorologicalDevice::getNum, num);
                wrapper.last("limit 1");
                device = meteorologicalDeviceService.getOne(wrapper);
                // 保存id关联通道
                for (Map.Entry<String, SocketChannel> channelEntry : map.entrySet()) {
                    SocketChannel socketChannel = channelEntry.getValue();
                    if (socketChannel == ctx.channel()) {
                        logger.info("当前连接为：{}", channelEntry.getKey());
                        GatewayService.addGatewayChannel(requestString, socketChannel);
                        break;
                    }
                }
            }
        }
        //获取了对应设备
        if (device != null) {
            // 更新ip端口和在线状态
            InetSocketAddress iNetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            device.setIp(iNetSocketAddress.getAddress().getHostAddress());
            device.setPort(iNetSocketAddress.getPort());
            device.setNetworkState(1);
            device.setLastOnlineTime(new Date());
            meteorologicalDeviceService.updateById(device);
            // 保存气象数据
            MeteorologicalHistory deviceHistory = meteorologicalDeviceService.byteToMeteorologicalHistory(requestbytes);
            MeteorologicalReal deviceReal = new MeteorologicalReal();
            BeanUtils.copyProperties(deviceHistory, deviceReal);
            deviceReal.setCreateTime(new Date());
            LambdaQueryWrapper<MeteorologicalReal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MeteorologicalReal::getDeviceId, device.getId());
            queryWrapper.last("limit 1");
            MeteorologicalReal real = meteorologicalRealDao.selectOne(queryWrapper);
            //实时数据库不存在此气象设备的实时数据则添加，存在则修改
            if (real != null) {
                LambdaUpdateWrapper<MeteorologicalReal> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(MeteorologicalReal::getDeviceId, device.getId());
                meteorologicalRealDao.update(deviceReal, updateWrapper);
            } else {
                deviceReal.setDeviceId(device.getId());
                meteorologicalRealDao.insert(deviceReal);
            }
            // 保存历史数据
            deviceHistory.setCreateTime(new Date());
            deviceHistory.setDeviceId(device.getId());
            meteorologicalHistoryDao.insert(deviceHistory);
            meteorologicalDeviceService.sendIrSubtitle(device.getId());

        } else {
            logger.info("平台未配置mac地址为:{}|clientIP:{}|port:{}|num:{}的气象设备", mac, clientIP, port, num);
        }
        //应答数据到客户端 response必须有数据才能应答，不然不能调用该方法
        response = IotDataUtil.getDevice1Response(requestbytes);
        ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    logger.debug("EXC Server Send message-id respone:{}", response);
                    logger.debug("应答完毕!");
                } else {
                    logger.error("应答失败！，失败原因:{}", channelFuture.cause().toString());
                }
            }
        });
        logger.debug("业务处理完毕");
    }

}
