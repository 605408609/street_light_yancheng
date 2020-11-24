package com.exc.street.light.sl.netty.zkzl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Author: XuJiaHao
 * @Description:
 * @Date: Created in 18:02 2020/6/19
 * @Modified:
 */
public class CustomByteDecoder extends MessageToMessageDecoder<byte[]> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, byte[] bytes, List<Object> list) throws Exception {
        list.add(bytes.length);
    }
}
