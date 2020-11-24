package com.exc.street.light.resource.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过socket发送协议
 *
 * @author LinShiWen
 * @date 2017/10/09
 */
@Component
public class SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClient.class);

    //长连接socket
    public static Socket longSocket;

    //长连接重连计数器
    public static Integer count = 1;

    public static String longIp = null;
    public static Integer longPort = null;


    /**
     * @param bytes 要发送的协议数据
     */
    public static boolean send(String ip, int port, byte[] bytes) {
        int length = 0;
        byte[] sendByte = null;
        DataOutputStream dout = null;
        Socket socket = null;
        boolean connected = false;
        //将Socket和DataOutputStream对象的创建放在try()里,代码执行完后将自动关闭对象
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 1000);
            connected = socket.isConnected();
            dout = new DataOutputStream(socket.getOutputStream());
            sendByte = bytes;
            length = bytes.length;
            dout.write(sendByte, 0, length);
            dout.flush();
        } catch (Exception e) {
            logger.error("socket连接异常:" + e.getMessage());
        } finally {
            if (dout != null) {
                try {
                    dout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return connected;
    }

    /**
     * 发送数据(有返回)
     *
     * @param ip
     * @param port
     * @param bytes
     * @return
     */
    public static byte[] sendReturn(String ip, int port, byte[] bytes) {
        int length = 0;
        byte[] sendByte = null;
        DataOutputStream dout = null;
        Socket socket = null;
        boolean connected = false;
        
        byte[] receiveData = null;
        //将Socket和DataOutputStream对象的创建放在try()里,代码执行完后将自动关闭对象
        try {
            socket = new Socket();
            //连接超时时间
            socket.connect(new InetSocketAddress(ip, port), 1000);
            //读取超时时间
            socket.setSoTimeout(1000);
            connected = socket.isConnected();
            dout = new DataOutputStream(socket.getOutputStream());
            
            InputStream is = new DataInputStream(socket.getInputStream());
            
            sendByte = bytes;
            length = bytes.length;
            dout.write(sendByte, 0, length);
            dout.flush();
            
            //读取返回数据
            byte[] bytes1 = new byte[1024];
            int read = is.read(bytes1);
            receiveData = Arrays.copyOf(bytes1, read);
            logger.info("请求读取信息结束");
        } catch (Exception e) {
            logger.error("socket连接异常:" + e.getMessage());
        } finally {
            if (dout != null) {
                try {
                    dout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return receiveData;
    }

}
