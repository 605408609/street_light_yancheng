
package com.exc.street.light.wifi.webservice.online;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "onlineUserService", targetNamespace = "http://endpoint.webservice.smp.ruijie.com/", wsdlLocation = "${WSDLONLINEUSERURL}")
public class OnlineUserService_Service
    extends Service
{

    /**
     * 静态变量在静态代码块加载后加载，且注解也在之后加载，完成动态注入修改注解里的参数
     */
    public static String WSDLONLINEUSERURL;
    private final static URL ONLINEUSERSERVICE_WSDL_LOCATION;
    private final static WebServiceException ONLINEUSERSERVICE_EXCEPTION;
    private final static QName ONLINEUSERSERVICE_QNAME = new QName("http://endpoint.webservice.smp.ruijie.com/", "onlineUserService");

    static {
        Properties properties = new Properties();
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("bootstrap.yml");
            Set<Object> objects = properties.keySet();
            for (Object object : objects) {
                // 取到参数赋值给静态变量
                if (object.toString().equals("WSDLONLINEUSERURL")) {
                    System.out.println(new String(properties.getProperty((String) object).getBytes("iso-8859-1"), "gbk"));
                    WSDLONLINEUSERURL = new String(properties.getProperty((String) object).getBytes("iso-8859-1"), "gbk");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL(WSDLONLINEUSERURL);
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        ONLINEUSERSERVICE_WSDL_LOCATION = url;
        ONLINEUSERSERVICE_EXCEPTION = e;
    }

    public OnlineUserService_Service() {
        super(__getWsdlLocation(), ONLINEUSERSERVICE_QNAME);
    }

    public OnlineUserService_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), ONLINEUSERSERVICE_QNAME, features);
    }

    public OnlineUserService_Service(URL wsdlLocation) {
        super(wsdlLocation, ONLINEUSERSERVICE_QNAME);
    }

    public OnlineUserService_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, ONLINEUSERSERVICE_QNAME, features);
    }

    public OnlineUserService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public OnlineUserService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns OnlineUserService
     */
    @WebEndpoint(name = "OnlineUserServiceImplPort")
    public OnlineUserService getOnlineUserServiceImplPort() {
        return super.getPort(new QName("http://endpoint.webservice.smp.ruijie.com/", "OnlineUserServiceImplPort"), OnlineUserService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns OnlineUserService
     */
    @WebEndpoint(name = "OnlineUserServiceImplPort")
    public OnlineUserService getOnlineUserServiceImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://endpoint.webservice.smp.ruijie.com/", "OnlineUserServiceImplPort"), OnlineUserService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (ONLINEUSERSERVICE_EXCEPTION!= null) {
            throw ONLINEUSERSERVICE_EXCEPTION;
        }
        return ONLINEUSERSERVICE_WSDL_LOCATION;
    }

}