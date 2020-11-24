package com.exc.street.light.em.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 处理接收消息的线程
 *
 * @Author Longshuangyang
 * @Data 2019/12/05
 */
public class ReceiveMsgThread implements Runnable {
    private Logger logger = LoggerFactory.getLogger(ReceiveMsgThread.class);

    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;
    InputStream inputStream;
    PrintWriter pw;
    OutputStream outputStream;

    public ReceiveMsgThread(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//读取客户端消息  
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//向客户端写消息
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            pw = new PrintWriter(outputStream);
            String lineString = "";
            while (true) {
                byte[] bytes = new byte[1024];
                inputStream.read(bytes);//写入byte数组中。再依次读取出来即可。
                lineString = new String(bytes, 0, bytes.length);
                logger.info("收到来自客户端的发送的消息是：" + lineString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (pw != null) {
                    pw.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
