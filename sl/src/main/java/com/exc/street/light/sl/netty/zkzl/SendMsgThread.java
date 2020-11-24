package com.exc.street.light.sl.netty.zkzl;


import java.io.*;
import java.net.Socket;

// 处理发送消息线程
class SendMsgThread extends Thread {

    Socket response;

    public SendMsgThread(Socket response) {
        this.response = response;
    }

    @Override
    public void run() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
            BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

            // InputStreamReader in=new InputStreamReader(System.in, "UTF-8");

            String tempStr = "";
            while ((tempStr = keyboardReader.readLine()) != null) {

                // tempStr=URLEncoder.encode(tempStr, "UTF-8");//加码
                writer.write(tempStr + "\n");
                writer.flush();

            }
            System.out.println("结束会话！");
            //response.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}