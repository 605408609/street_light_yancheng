package com.exc.street.light.sl.netty.shuncom;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exc.street.light.resource.enums.sl.shuncom.CommandTypeEnums;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * 处理任务线程类
 *
 * @author xiaok
 */
@Slf4j
public class MessageRecvInitializeTask implements Runnable {

    private ByteBuf request = null;
    private String response = null;
    private ChannelHandlerContext ctx = null;

    public String getResponse() {
        return response;
    }

    public ByteBuf getRequest() {
        return request;
    }

    public void setRequest(ByteBuf request) {
        this.request = request;
    }

    MessageRecvInitializeTask(ByteBuf request, String response, ChannelHandlerContext ctx) {
        this.request = request;
        this.response = response;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        log.debug("正在运行的线程ID为{},名称为{}", thread.getId(), thread.getName());
        RequestBean bean = ByteBufAndString.getByteBufLength(request);
        byte[] requestBytes = bean.getReq();
        //释放资源
        request.release();
        //进行数据校验
        if (!ShuncomMsgUtil.validation(requestBytes)) {
            log.error("数据校验失败,reqStr = {},reqBytes = {}", new String(requestBytes), requestBytes);
            return;
        }
        byte[] payload = new byte[requestBytes.length - 10];
        System.arraycopy(requestBytes, 10, payload, 0, payload.length);
        String reqStr = new String(payload);
        JSONObject dataJson;
        try {
            dataJson = JSON.parseObject(reqStr);
        } catch (Exception e) {
            log.error("json转换失败,data={}", reqStr);
            return;
        }
        Integer code = dataJson.getInteger("code");
        if (code == null) {
            //判断是否为认证数据包
            String mac = dataJson.getString("mac");
            if (StringUtils.isBlank(mac)) {
                log.error("顺舟认证数据包结构有误,data={}", dataJson.toString());
                return;
            }
            Map<String, SocketChannel> gwMap = GatewayService.getChannels();
            for (Map.Entry<String, SocketChannel> next : gwMap.entrySet()) {
                if (next.getValue() == ctx.channel()) {
                    //写入map 用作后续推送消息
                    GatewayService.addGatewayChannel(mac, next.getValue());
                    break;
                }
            }
            return;
        }
        //返回json
        JSONObject returnObj = null;
        CommandTypeEnums cmdTypeEnum = CommandTypeEnums.getByCode(code);
        if (cmdTypeEnum != null) {
            log.info("接收到顺舟云盒数据,类型:{},数据：{}", cmdTypeEnum.getName(), reqStr);
            switch (cmdTypeEnum) {
                case REPORT_HEART_BEAT:
                    returnObj = ShuncomMsgUtil.reportHeartBeatHandle(dataJson);
                    break;
                case REPORT_DEVICE_CONTROL:
                    ShuncomMsgUtil.reportControlDevice(dataJson);
                    break;
                case ORDER_DEVICE_DELETE:
                    ShuncomMsgUtil.reportDeviceDelete(dataJson);
                    break;
                case REPORT_DEVICE_INFO:
                    returnObj = ShuncomMsgUtil.reportDeviceInfoHandle(dataJson);
                    break;
                case REPORT_DEVICE_REGISTER:
                    returnObj = ShuncomMsgUtil.reportNewDeviceRegister(dataJson);
                    break;
                case ORDER_SETTING_SEARCH_OR_MODIFY:
                    ShuncomMsgUtil.reportSettingSearchOrModify(dataJson);
                    break;
                case ORDER_DELETE_ALL_DEVICE:
                    ShuncomMsgUtil.reportDeleteAllChildrenDevice(dataJson);
                    break;
                case ORDER_GROUP_CREATE:
                    ShuncomMsgUtil.reportCreateGroup(dataJson);
                    break;
                case ORDER_GROUP_MODIFY:
                    ShuncomMsgUtil.reportModifyGroup(dataJson);
                    break;
                case ORDER_GROUP_REQUEST_SETTING:
                    ShuncomMsgUtil.reportSetGroupStatus(dataJson);
                    break;
                case ORDER_GROUP_DELETE:
                    ShuncomMsgUtil.reportGroupDelete(dataJson);
                    break;
                case REPORT_GROUP_DELETE:
                    returnObj = ShuncomMsgUtil.reportGroupDeleteUpload(dataJson);
                    break;
                case ORDER_GET_GROUP_INFO:
                    ShuncomMsgUtil.reportGroupGetInfo(dataJson);
                    break;
                case ORDER_GROUP_DELETE_ALL:
                    ShuncomMsgUtil.reportDeleteAllGroup(dataJson);
                    break;
                case ORDER_STRATEGY_ADD_OR_MODIFY:
                    ShuncomMsgUtil.reportStrategyCreateOrModify(dataJson);
                    break;
                case ORDER_STRATEGY_SET_STATE:
                    ShuncomMsgUtil.reportStrategySetState(dataJson);
                    break;
                case ORDER_STRATEGY_DELETE:
                    ShuncomMsgUtil.reportStrategyDelete(dataJson);
                    break;
                case REPORT_STRATEGY_DELETE:
                    ShuncomMsgUtil.reportStrategyMsgUpload(dataJson);
                    break;
                case ORDER_STRATEGY_GET_INFO:
                    ShuncomMsgUtil.reportStrategyGetInfo(dataJson);
                    break;
                default:
                    break;
            }
        } else {
            log.debug("接收到顺舟云盒数据-未定义类型数据：{}", reqStr);
        }
        if (returnObj == null) {
            return;
        }
        //返回byte数组
        byte[] returnBytes;
        try {
            returnBytes = ShuncomMsgUtil.addHeader(returnObj);
        } catch (UnsupportedEncodingException e) {
            log.error("封装返回消息失败，json={}", returnObj);
            return;
        }
        //应答数据到客户端 response必须有数据才能应答，不然不能调用该方法
        ctx.writeAndFlush(returnBytes).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    log.debug("EXC Server Send message-id response:{}", response);
                    log.debug("应答完毕!");
                } else {
                    log.error("应答失败！，失败原因:{}", channelFuture.cause().toString());
                }
            }
        });
        log.debug("业务处理完毕");
    }

}
