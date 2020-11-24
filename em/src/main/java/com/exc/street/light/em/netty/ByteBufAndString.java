package com.exc.street.light.em.netty;


import io.netty.buffer.ByteBuf;

/**
 * @aythor xujiahaoxixi
 * @data 2017/10/18 9:27
 */
public class ByteBufAndString {
    public static String ByteBufToString(ByteBuf buf) {
        String result = "";
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        //resultStr=new String(req,"utf-8");
        int length = req.length;
        //byte[] req=new byte[buf.readUnsignedByte()];
        //buf.readBytes(req);
        //buf.readUnsignedByte();
        short by1;
        byte by;
        int i;
        for (i = 0; i < length; i++) {
            by1 = buf.getUnsignedByte(i);
            String hex = Integer.toHexString(by1).toUpperCase();
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            result = result + hex + " ";
            //System.out.print(hex+" ");
        }
        return result;
    }

    public static RequestBean getByteBufLength(ByteBuf buf) {
        RequestBean request = new RequestBean();
        byte[] req = new byte[buf.readableBytes()];
        int length = req.length;
        buf.readBytes(req);
        request.setLenght(length);
        request.setReq(req);
        return request;
    }
}
