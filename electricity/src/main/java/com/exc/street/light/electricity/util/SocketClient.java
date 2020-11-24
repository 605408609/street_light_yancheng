package com.exc.street.light.electricity.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 * 通过socket发送协议
 *
 * @author LinShiWen
 * @date 2017/10/09
 */
@Component
public class SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClient.class);

    /**
     * 发送数据
     *
     * @param ip
     * @param port
     * @param bytes
     * @return
     */
    public static byte[] send(String ip, int port, byte[] bytes) {
        byte[] receiveData = null;
        OutputStream os = null;
        InputStream is = null;
        Socket socket = null;
        //将Socket和DataOutputStream对象的创建放在try()里,代码执行完后将自动关闭对象
        try {
            logger.warn("发送请求:{},port:{}", ip,port);
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), ConstantUtil.CONNECT_TIMEOUT);
            socket.setSoTimeout(ConstantUtil.SO_TIMEOUT);
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
            //加密
//            byte[] encrypt = AESUtil.encrypt(bytes);
//            if (encrypt != null) {
//                int length = encrypt.length;
//                os.write(encrypt, 0, length);
//            }
            logger.warn("发送未加密数据:{}", HexUtil.bytesTohex(bytes));
            os.write(bytes, 0, bytes.length);
            os.flush();
//            logger.warn("发送加密数据:{}", HexUtil.bytesTohex(encrypt));
            byte[] bytes1 = new byte[1024];
            int read = is.read(bytes1);
            receiveData = Arrays.copyOf(bytes1, read);
//            logger.warn("接收加密数据:{}", HexUtil.bytesTohex(receiveData));
            //解密
//            receiveData = AESUtil.decrypt(receiveData);
            logger.warn("接收未加密数据:{}", HexUtil.bytesTohex(receiveData));
            logger.info("请求结束");
        } catch (Exception e) {
            logger.error("socket连接异常:" + e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
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

    /**
     * 发送数据
     * @param ip ip
     * @param port 端口
     * @param bytes 数据
     * @return 是否成功
     */
    public static boolean sendData(String ip, int port, byte[] bytes) {
        byte[] rtn = send(ip, port, bytes);
        return rtn != null && AnalysisUtil.getRtn(rtn) == ConstantUtil.RET_1;
    }

    /**
     * 发送数据
     *
     * @param ip
     * @param port
     * @param bytes
     * @return
     */
    public static byte[] sendForSearch(String ip, int port, byte[] bytes) {
        byte[] receiveData = null;
        OutputStream os = null;
        InputStream is = null;
        Socket socket = null;
        //将Socket和DataOutputStream对象的创建放在try()里,代码执行完后将自动关闭对象
        try {
            logger.warn("发送请求:{}", ip);
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), ConstantUtil.CONNECT_TIMEOUT);
            socket.setSoTimeout(ConstantUtil.SO_TIMEOUT_SEARCH);
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
            //加密
//            byte[] encrypt = AESUtil.encrypt(bytes);
//            if (encrypt != null) {
//                int length = encrypt.length;
//                os.write(encrypt, 0, length);
//            }
            os.write(bytes, 0, bytes.length);
            os.flush();
            logger.warn("发送未加密数据:{}", HexUtil.bytesTohex(bytes));
//            logger.warn("发送加密数据:{}", HexUtil.bytesTohex(encrypt));
            byte[] bytes1 = new byte[1024];
            int read = 0;
            while ( (read =is.read(bytes1)) != -1){
                logger.warn("接收到数据:{}",read);
                byte[] data = Arrays.copyOf(bytes1, read);
                if(receiveData == null){
                    receiveData = data;
                }else {
                    byte[] tmp = new byte[receiveData.length + read];
                    System.arraycopy(receiveData,0,tmp,0,receiveData.length);
                    System.arraycopy(data,0,tmp,receiveData.length,read);
                    receiveData = tmp;
                }
            }
//            logger.warn("接收加密数据:{}", HexUtil.bytesTohex(receiveData));
            //解密
//            receiveData = AESUtil.decrypt(receiveData);
            logger.warn("接收未加密数据:{}", HexUtil.bytesTohex(receiveData));
            logger.info("请求结束");
        } catch (Exception e) {
            logger.error("socket连接异常:" + e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
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
