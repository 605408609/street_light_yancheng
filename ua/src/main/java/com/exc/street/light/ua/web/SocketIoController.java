package com.exc.street.light.ua.web;

import com.exc.street.light.ua.service.SocketIoService;
import com.exc.street.light.ua.to.WebsocketTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * websocket控制器
 *
 * @author Linshiwen
 * @date 2018/7/12
 */
@RestController
@RequestMapping("/api/socketio")
public class SocketIoController {

    private static final Logger logger = LoggerFactory.getLogger(SocketIoController.class);

    @Autowired
    private SocketIoService socketIoService;

    @PostMapping
    public void send(@RequestBody WebsocketTO websocketTO) {
        logger.info("websocket接收参数:{}", websocketTO);
        System.out.println(websocketTO);
        socketIoService.sendMessage(websocketTO.getEventType(), websocketTO.getMessage());
    }
}
