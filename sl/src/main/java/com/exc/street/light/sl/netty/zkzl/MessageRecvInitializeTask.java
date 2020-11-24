package com.exc.street.light.sl.netty.zkzl;

import com.exc.street.light.sl.config.zkzl.NettyService;
import com.exc.street.light.sl.netty.shuncom.ByteBufAndString;
import com.exc.street.light.sl.netty.shuncom.RequestBean;
import com.exc.street.light.sl.utils.HexUtil;
import com.exc.street.light.sl.utils.ZkzlProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 处理任务线程类
 *
 * @author LeiJing
 */
@Slf4j
public class MessageRecvInitializeTask implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(MessageRecvInitializeTask.class);

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
        //处理接收到的中科智联数据
        ZkzlProtocolUtil.receiveData(request, ctx);
    }
}
