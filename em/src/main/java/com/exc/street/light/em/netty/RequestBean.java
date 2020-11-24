package com.exc.street.light.em.netty;

/**
 * @Author:xujiahao
 * @Description
 * @Data:Created in 16:45 2017/12/28
 * @Modified By:
 */
public class RequestBean {
    private byte[] req;
    private int lenght;

    public byte[] getReq() {
        return req;
    }

    public void setReq(byte[] req) {
        this.req = req;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }
}
