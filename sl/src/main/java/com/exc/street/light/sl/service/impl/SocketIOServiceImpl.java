package com.exc.street.light.sl.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

import com.exc.street.light.resource.core.Result;
import com.exc.street.light.resource.entity.sl.LampDevice;
import com.exc.street.light.resource.entity.sl.SingleLampParam;
import com.exc.street.light.sl.config.parameter.PathApi;
import com.exc.street.light.sl.po.PushMessage;
import com.exc.street.light.sl.service.LampDeviceService;
import com.exc.street.light.sl.service.SingleLampParamService;
import com.exc.street.light.sl.service.SocketIOService;
import com.exc.street.light.sl.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: XuJiaHao
 * @Description: SocketIOService实现类
 * @Date: Created in 16:26 2020/4/2
 * @Modified:
 */
@Service(value = "socketIOService")
public class SocketIOServiceImpl implements SocketIOService {
    private static final Logger logger = LoggerFactory.getLogger(SocketIOServiceImpl.class);

    @Autowired
    SingleLampParamService singleLampParamService;
    @Autowired
    LampDeviceService lampDeviceService;
    @Autowired
    PathApi pathApi;

    //客户端集合
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() throws Exception {
        startServer();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,解决端口占用问题
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() {
        stopServer();
    }

    @Override
    public void startServer() throws Exception {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String userToken = getParamsByClient(client,"userToken");
            logger.info("新增连接,连接IP{},",client.getRemoteAddress());
            if (userToken != null) {
                clientMap.put(userToken, client);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String userToken = getParamsByClient(client,"userToken");
            if (userToken != null) {
                clientMap.remove(userToken);
                client.disconnect();
            }
        });

        // 处理自定义的事件，与连接监听类似
        socketIOServer.addEventListener(PUSH_EVENT, PushMessage.class, (client, data, ackSender) -> {

        });
        socketIOServer.start();
    }

    @Override
    public void stopServer() {
        if (socketIOServer != null){
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    /**
     * 单个客户端发送信息
     *
     * @param pushMessage
     */
    @Override
    public void pushMessage2Client(PushMessage pushMessage) {
        String userToken = pushMessage.getUserToken();
        if (StringUtils.isNotBlank(userToken)) {
            SocketIOClient client = clientMap.get(userToken);
            if (client != null)
                client.sendEvent(PUSH_EVENT, pushMessage);
        }
    }

    /**
     * 群发消息
     *
     * @param content
     */

    @Override
    public void pushMessage2AllClient(String content) {
        Collection<SocketIOClient> socketIOClients = socketIOServer.getAllClients();
        logger.info("群发消息{}",content);
        if(socketIOClients.size() != 0){
            for (SocketIOClient cli : socketIOClients) {
                if(cli != null){
                    cli.sendEvent(PUSH_EVENT, content);
                }
            }
        }
    }

    /**
     * 将文件写入磁盘
     * @param multipartFile
     * @return
     */
    @Override
    public Result writeFile(MultipartFile multipartFile,String num,String model,String factory,HttpServletRequest request) {

        //上传文件
        if (multipartFile.isEmpty()) {
            return new Result().error("文件为空");
        }
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        logger.info("写入文件，文件名称："+fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (!"bin".equals(suffixName)) {
            logger.info("导入文件" + fileName + "失败,文件类型错误,不是bin文件");
            return new Result().error("导入文件" + fileName + "失败,文件类型错误,不是bin文件");
        }

        String crc16 = Crc16.generateCrc16(String.valueOf(System.currentTimeMillis()));
        String realName = crc16 + "." + suffixName;
        //URL resource = this.getClass().getResource("/");
        //String path = resource.getPath();
        String path = pathApi.getFile();
        String realPath = path + "/" + realName;
        try {
            multipartFile.transferTo(new File(realPath));
            //List<String> messageList = MessageOperationUtil.analysisOtaFile(realPath, num,model,factory);
            List<String> messageList = new ArrayList<>();
            if(messageList==null || messageList.size()==0){
                return new Result().error("该类产品无法进行更新");
            }
            redisUtil.set(crc16,messageList);
            redisUtil.expire(crc16,10800);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(!realPath.isEmpty()){
                // 删除源文件
                File file0 = new File(realPath);
                if (file0.exists()) {
                    file0.delete();
                }
            }
        }
        /*String otaReady = "";
        if("nb".equals(model)){
            otaReady = MessageGeneration.nbOtaReady(num,crc16);
        }else if("cat1".equals(model)){
            otaReady = CatOneMessageGeneration.catOneOtaReady(num,crc16);
        }else if("lora_old".equals(model)){
            otaReady = LoraOldMessageGeneration.loraOldOtaReady(crc16);
        }else if("lora_new".equals(model)){
            otaReady = LoraNewMessageGeneration.loraNewOtaReady(crc16);
        }else if("dxnb".equals(model)){
            otaReady = DxnbMessageGeneration.dxnbOtaReady(crc16);
        }
        Result<LampDevice> lampDeviceResult = lampDeviceService.getByNum(num,model,factory);
        LampDevice lampDevice = lampDeviceResult.getData();
        String sendId = lampDevice.getSendId();
        MessageOperationUtil.sendByMode(otaReady,model,sendId);*/
        return new Result().success(crc16);
    }

    /**
     * 获取参数
     *
     * @param client
     * @param param
     * @return
     */
    private String getParamsByClient(SocketIOClient client,String param) {
        // 从请求的连接中拿出参数（这里的loginUserNum必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get(param);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
