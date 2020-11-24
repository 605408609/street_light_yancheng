package com.exc.street.light.wifi.webservice;

import com.exc.street.light.resource.entity.wifi.WifiUser;
import com.exc.street.light.wifi.webservice.fail.RadiusLogWebService;
import com.exc.street.light.wifi.webservice.fail.RadiusLogWebService_Service;
import com.exc.street.light.wifi.webservice.online.OnlineUserService;
import com.exc.street.light.wifi.webservice.online.OnlineUserService_Service;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Xiezhipeng
 * @Description 调用第三方接口，获取wifi用户信息
 * @Date 2020/4/29
 */
@Log4j2
public class WebServiceInfo {

    public static void getClient(Object object) {
        //配置验证信息
        Client client = ClientProxy.getClient(object);

        Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        //用户名
        outProps.put(WSHandlerConstants.USER, "admin");
        outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new ClientPwdCallback());
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientPwdCallback.class.getName());
        client.getOutInterceptors().add(new WSS4JOutInterceptor(outProps));
    }

    /**
     * 获取在线(认证成功)用户信息，xml字符串
     */
    public static String getOnlineUserInfo() {
        //调用服务
        OnlineUserService onlineUserService = null;
        long start = System.currentTimeMillis();
        try {
            OnlineUserService_Service service_service = new OnlineUserService_Service();
            onlineUserService = service_service.getOnlineUserServiceImplPort();
            getClient(onlineUserService);
        } catch (Exception e) {
            log.error(e.getMessage());
            long end = System.currentTimeMillis();
            System.out.println("认证成功运行时间：" + (end - start)/1000 + "s");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 拼接在线用户的查询条件
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<conditions></conditions>");
        String onlineUsers = null;
        if (onlineUserService != null) {
            onlineUsers = onlineUserService.queryOnlineUsers(sb.toString());
        }
        return onlineUsers;
    }

    /**
     * 获取认证失败的用户信息，xml字符串
     */
    public static String queryRadiusLogs() {
        //调用服务
        RadiusLogWebService radiusLogWebService = null;
        long start = System.currentTimeMillis();
        try {
            RadiusLogWebService_Service service_service1 = new RadiusLogWebService_Service();
            radiusLogWebService = service_service1.getRadiusLogWebServiceImplPort();
            getClient(radiusLogWebService);
        } catch (Exception e) {
            log.error(e.getMessage());
            long end = System.currentTimeMillis();
            System.out.println("认证失败运行时间：" + (end - start)/1000 + "s");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 查询每天的数据
        LocalDate date = LocalDate.now();
        // 空格不能删除，日期和时间直接要保留空格，否则报错
        String time = date + " 00:00:01";
        // 拼接认证失败用户的查询条件
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<conditions>");
        sb.append("<authenTime type=\"start\">" + time + "</authenTime>");
        sb.append("</conditions>");
        String radiusLogs = null;
        if (radiusLogWebService != null) {
            radiusLogs = radiusLogWebService.queryRadiusLogs(sb.toString());
        }
        return radiusLogs;
    }

    /**
     * 认证失败的用户信息
     * @return
     */
    public static List<WifiUser> getCertFailUserList() {
        SAXBuilder saxReader = new SAXBuilder();
        Document document = null;
        try {
            if (queryRadiusLogs() != null) {
                InputStream inputStream = new ByteArrayInputStream(queryRadiusLogs().getBytes());
                document = saxReader.build(inputStream);
            }
        } catch (JDOMException | IOException e) {
            log.error(e.getMessage());
            return null;
        }
        Element rootElement = null;
        if (document != null) {
            rootElement = document.getRootElement();
        }

        if (rootElement != null) {
            System.out.println("节点个数:"+rootElement.getChildren("radius_log").size());
        }
        List<WifiUser> userList = new ArrayList<>();
        if (rootElement != null) {
            for (int i = 0; i < rootElement.getChildren("radius_log").size(); i++) {
                WifiUser wifiUser = new WifiUser();
                Element radiusLog = (Element) rootElement.getChildren("radius_log").get(i);

                wifiUser.setPhone(radiusLog.getAttributeValue("userId"));
                String userMac = getUserMac(radiusLog.getAttributeValue("userMac"));
                wifiUser.setUserMac(userMac);
                SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                try {
                    Date date =  formatter.parse(radiusLog.getAttributeValue("authenTime"));
                    wifiUser.setCertifTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                wifiUser.setCertifResult(0);
                wifiUser.setInternetTime(0);
                wifiUser.setInternetTraffic(0);
                wifiUser.setCreateTime(new Date());
                userList.add(wifiUser);
            }
        }
        return userList;
    }

    /**
     * 认证成功的在线用户信息
     * @return
     */
    public static List<WifiUser> getCertSuccessUserList(){
        SAXBuilder saxReader = new SAXBuilder();
        Document document = null;
        try {
            if (getOnlineUserInfo() != null) {
                InputStream inputStream = new ByteArrayInputStream(getOnlineUserInfo().getBytes());
                document = saxReader.build(inputStream);
            }
        } catch (JDOMException | IOException e) {
            log.error(e.getMessage());
            return null;
        }
        Element rootElement = null;
        if (document != null) {
            rootElement = document.getRootElement();
        }

        if (rootElement != null) {
            System.out.println("节点个数:"+rootElement.getChildren("online_user").size());
        }
        List<WifiUser> userList = new ArrayList<>();
        if (rootElement != null) {
            for (int i = 0; i < rootElement.getChildren("online_user").size(); i++) {
                WifiUser wifiUser = new WifiUser();
                // 获取子节点
                Element onlineUser = (Element) rootElement.getChildren("online_user").get(i);
                // 获取子节点下的属性值，并将其设置到wifiUser对象中
                String userId = onlineUser.getAttributeValue("userId");
                // 短信认证的话，userId就是手机号，微信认证的话，就是用户名：weixin_用户Mac
                if (userId.length() == 11) {
                    wifiUser.setPhone(userId);
                }
                String userMac = getUserMac(onlineUser.getAttributeValue("userMac"));
                wifiUser.setUserMac(userMac);
                // 终端类型
                wifiUser.setTerminal(onlineUser.getAttributeValue("clientOsType"));
                SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                try {
                    // 认证时间
                    Date certifTime =  formatter.parse(onlineUser.getAttributeValue("loginTime"));
                    wifiUser.setCertifTime(certifTime);
                    wifiUser.setLogoutTime(new Date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // 在线时长
                wifiUser.setInternetTime(Integer.parseInt(onlineUser.getAttributeValue("onlineTime"))/60);
                // 上行流量（字节）流量要计费才有
//                int upLinkFlow = Integer.parseInt(onlineUser.getAttributeValue("uplinkFlow"));
                // 下行流量（字节）
//                int downLinkFlow = Integer.parseInt(onlineUser.getAttributeValue("downlinkFlow"));
                // 单位转换为Mb
//                int flow = (upLinkFlow + downLinkFlow)/(1024*1024);
                // 上网流量
//                wifiUser.setInternetTraffic(flow);
                // 认证结果（0：失败 1：成功）
                wifiUser.setCertifResult(1);
                // 创建时间
                wifiUser.setCreateTime(new Date());
                userList.add(wifiUser);
            }
        }
        return userList;
    }

    /**
     * 用户Mac格式转换,eg:DCF090AE0618 -> dc-f0-90-ae-06-18
     * @param userMac
     * @return
     */
    private static String getUserMac(String userMac){
        if (StringUtils.isNotBlank(userMac)) {
            String mac = userMac.toLowerCase();
            String regex = "(.{2})";
            mac = mac.replaceAll(regex,"$1-");
            mac =  mac.substring(0,mac.length() - 1);
            return mac;
        }
        return null;
    }

}
