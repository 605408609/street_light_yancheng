package com.exc.street.light.occ.alarm;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 并行模式，视频文件裁剪任务执行
 * 
 * @author huangmin
 * @date 2019/06/22
 */
public class ReceiveAlarmCallable implements Callable<Boolean>{
	private static final Logger logger = LoggerFactory.getLogger(ReceiveAlarmCallable.class);
    private ReceiveAlarm receiveAlarm;

    public ReceiveAlarmCallable(ReceiveAlarm receiveAlarm) {
        this.receiveAlarm = receiveAlarm;
    }
    /* (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public synchronized Boolean call() {
        // TODO Auto-generated method stub
        receiveAlarm.jButtonLoginActionPerformedNew();
        boolean bool = false;
        try {
        	bool = receiveAlarm.SetupAlarmChan();
        	if (bool) {
        		logger.info("ReceiveAlarmCallable bool: "+bool);
        		this.wait();
			}
        } catch (InterruptedException e) {
             e.printStackTrace();
        }
         return bool;
    }
}
