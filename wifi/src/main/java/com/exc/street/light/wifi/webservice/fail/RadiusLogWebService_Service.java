
package com.exc.street.light.wifi.webservice.fail;

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
@WebServiceClient(name = "radiusLogWebService", targetNamespace = "http://audit.webservice.smp.ruijie.com/", wsdlLocation = "${WSDLRADIUSURL}")
public class RadiusLogWebService_Service
    extends Service
{
    /**
     * 静态变量在静态代码块加载后加载，且注解也在之后加载，完成动态注入修改注解里的参数
     */
    public static String WSDLRADIUSURL;
    private final static URL RADIUSLOGWEBSERVICE_WSDL_LOCATION;
    private final static WebServiceException RADIUSLOGWEBSERVICE_EXCEPTION;
    private final static QName RADIUSLOGWEBSERVICE_QNAME = new QName("http://audit.webservice.smp.ruijie.com/", "radiusLogWebService");

    static {
        Properties properties = new Properties();
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("bootstrap.yml");
            Set<Object> objects = properties.keySet();
            for (Object object : objects) {
                //取到参数赋值给静态变量
                if (object.toString().equals("WSDLRADIUSURL")) {
                    System.out.println(new String(properties.getProperty((String) object).getBytes("iso-8859-1"), "gbk"));
                    WSDLRADIUSURL = new String(properties.getProperty((String) object).getBytes("iso-8859-1"), "gbk");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL(WSDLRADIUSURL);
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        RADIUSLOGWEBSERVICE_WSDL_LOCATION = url;
        RADIUSLOGWEBSERVICE_EXCEPTION = e;
    }

    public RadiusLogWebService_Service() {
        super(__getWsdlLocation(), RADIUSLOGWEBSERVICE_QNAME);
    }

    public RadiusLogWebService_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), RADIUSLOGWEBSERVICE_QNAME, features);
    }

    public RadiusLogWebService_Service(URL wsdlLocation) {
        super(wsdlLocation, RADIUSLOGWEBSERVICE_QNAME);
    }

    public RadiusLogWebService_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, RADIUSLOGWEBSERVICE_QNAME, features);
    }

    public RadiusLogWebService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RadiusLogWebService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns RadiusLogWebService
     */
    @WebEndpoint(name = "RadiusLogWebServiceImplPort")
    public RadiusLogWebService getRadiusLogWebServiceImplPort() {
        return super.getPort(new QName("http://audit.webservice.smp.ruijie.com/", "RadiusLogWebServiceImplPort"), RadiusLogWebService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RadiusLogWebService
     */
    @WebEndpoint(name = "RadiusLogWebServiceImplPort")
    public RadiusLogWebService getRadiusLogWebServiceImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://audit.webservice.smp.ruijie.com/", "RadiusLogWebServiceImplPort"), RadiusLogWebService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (RADIUSLOGWEBSERVICE_EXCEPTION!= null) {
            throw RADIUSLOGWEBSERVICE_EXCEPTION;
        }
        return RADIUSLOGWEBSERVICE_WSDL_LOCATION;
    }

}
