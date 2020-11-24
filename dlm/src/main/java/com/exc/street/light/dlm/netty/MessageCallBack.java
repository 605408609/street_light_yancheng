package com.exc.street.light.dlm.netty;

import io.netty.buffer.ByteBuf;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xujiahaoxixi
 * @date 2017/10/19 14:57
 */
public class MessageCallBack {

    private ByteBuf request;
    private ByteBuf response;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();

    public MessageCallBack(ByteBuf request) {
        this.request = request;
    }

    public Object start() throws InterruptedException {
        try {
            lock.lock();
            //设定一下超时时间，rpc服务器太久没有相应的话，就默认返回空吧。
            finish.await(10 * 1000, TimeUnit.MILLISECONDS);
            if (this.response != null) {
                return this.response;
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void over(ByteBuf reponse) {
        try {
            lock.lock();
            finish.signal();
            this.response = reponse;
        } finally {
            lock.unlock();
        }
    }
}