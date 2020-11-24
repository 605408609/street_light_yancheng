package com.exc.street.light.resource.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 *  利用jdk1.5+的特性，测试指定IP能否ping通
 * @author huangmin
 * @date 2020/03/26
 */
public class TestingOnlineUtil {
    private static final Logger logger = LoggerFactory.getLogger(TestingOnlineUtil.class);
    
    /**
     * 测试ip是否能 PING 通
     * @param ip    设备IP
     * @return  boolean true:在线 ,false:离线
     */
    public static boolean getOnline(String ip) {
        int timeout = ConstantUtil.TIMEOUT_PING;
        /*  JDK1.5以上测试IP是否能够ping通
                        1、isReachable方法中给定的时间，为超时时间
                        2、jdk1.5以下（不包括1.5）不支持方法isReachable*/
        boolean reachable = false;
        try {
            InetAddress address = InetAddress.getByName(ip);
            reachable = address.isReachable(timeout);// 门槛值
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ping设备的ip("+ip+")失败,错误原因是：" + e);
        }
        return reachable;
    }
}
