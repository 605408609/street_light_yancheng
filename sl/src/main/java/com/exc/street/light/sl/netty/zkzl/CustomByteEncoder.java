package com.exc.street.light.sl.netty.zkzl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: XuJiaHao
 * @Description:
 * @Date: Created in 11:34 2020/6/17
 * @Modified:
 */
public class CustomByteEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, byte[] o, ByteBuf byteBuf) throws Exception {
        //write msg
        byteBuf.writerIndex(0);
        byteBuf.writeBytes(o);
    }
}
