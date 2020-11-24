package com.exc.street.light.sl.service;


import com.exc.street.light.resource.core.Result;
import com.exc.street.light.sl.po.PushMessage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: XuJiaHao
 * @Description: 提供SocketIOService服务
 * @Date: Created in 14:00 2020/4/2
 * @Modified:
 */
public interface SocketIOService {

    //推送的事件
    String PUSH_EVENT = "push_event";

    // 启动
    void startServer() throws Exception;

    // 停止
    void stopServer() ;

    // 推送服务
    void pushMessage2Client(PushMessage pushMessage);

    // 群推
    void pushMessage2AllClient(String content);


    Result writeFile(MultipartFile multipartFile,String num,String model,String factory,HttpServletRequest request);

}
