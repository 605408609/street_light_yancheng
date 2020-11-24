package com.exc.street.light.sl.utils;

import io.netty.buffer.ByteBuf;

/**
 * @Author: XuJiaHao
 * @Description:  ByteBuf工具类
 * @Date: Created in 16:01 2020/6/16
 * @Modified:
 */
public class ByteBufUtil {
    /**
     * bytebuf被该方法读取一次后，之后再读取会为空，所以不要使用该方法调用打印日志
     *
     * @param buf
     * @return
     */
    public static byte[] getByteBuf(ByteBuf buf) {
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        return req;
    }

    public static int getByteBufLength(ByteBuf buf){
        byte[] req = new byte[buf.readableBytes()];
        int length = req.length;
        buf.readBytes(req);
        return length;
    }

}
