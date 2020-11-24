package com.exc.street.light.sl.netty.zkzl;

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
//    IotDataService iotDataService;
//    IotProtocolParameterService iotProtocolParameterService;
//
//    public ReceiveMsgThread(Socket socket, IotDataService iotDataService, IotProtocolParameterService iotProtocolParameterService) {
//        super();
//        this.socket = socket;
//        this.iotDataService = iotDataService;
//        this.iotProtocolParameterService = iotProtocolParameterService;
//    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//读取客户端消息  
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//向客户端写消息
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            pw = new PrintWriter(outputStream);
            String lineString = "";
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
