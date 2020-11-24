package com.exc.street.light.resource.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.*;

/**
 * Https请求
 *
 * @author longshuangyang
 */
public class HttpsUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final int TIMEOUT = 45000;
    public static final String ENCODING = "UTF-8";


    /**
     * 发送https请求
     *
     * @param address          地址url
     * @param headerParameters 请求头
     * @param body             请求参数
     * @param method           请求方式（GET,POST）
     * @param selfcertpwd      PKCS12格式证书秘钥密码
     * @param trustcapwd       JKS格式证书秘钥密码
     * @param selfcertpath     PKCS12格式证书路径
     * @param trustcapath      KS格式证书路径
     * @return
     */
    public static String httpsSendJson(String address, Map<String, String> headerParameters, String body, String method, String selfcertpwd, String trustcapwd, String selfcertpath, String trustcapath) {
        String urlPar = "";
        urlPar = body;
        headerParameters.put("Content-Type", "application/json;charset=" + ENCODING);


        logger.info("发送参数：{}", urlPar);
        // get方法则地址拼接参数


        // 返回值
        String result = null;
        HttpURLConnection httpConnection = null;
        try {
            // 加载证书
            certificate(selfcertpwd, trustcapwd, selfcertpath, trustcapath);
            // 获取连接
            URL Url = new URL(address);
            httpConnection = (HttpURLConnection) Url.openConnection();
            // 设置请求时间
            httpConnection.setConnectTimeout(TIMEOUT);
            // 设置 header
            if (headerParameters != null) {
                Iterator<String> headerKey = headerParameters.keySet().iterator();
                while (headerKey.hasNext()) {
                    String key = headerKey.next();
                    httpConnection.setRequestProperty(key, headerParameters.get(key));
                }
            }
            // 设置请求方法
            httpConnection.setRequestMethod(method);
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            // 写query数据流
            if (!(urlPar == null || urlPar.trim().equals(""))) {
                OutputStream writer = httpConnection.getOutputStream();
                try {
                    writer.write(urlPar.getBytes(ENCODING));
                } finally {
                    if (writer != null) {
                        writer.flush();
                        writer.close();
                    }
                }
            }
            // 请求结果
            String encoding = "UTF-8";
            if (httpConnection.getContentType() != null && httpConnection.getContentType().indexOf("charset=") >= 0) {
                encoding = httpConnection.getContentType().substring(httpConnection.getContentType().indexOf("charset=") + 8);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), encoding));
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return result;
    }

    /**
     * 发送https请求
     *
     * @param address          地址url
     * @param headerParameters 请求头
     * @param body             请求参数
     * @param method           请求方式（GET,POST）
     * @return
     */
    public static String httpsSendJson(String address, Map<String, String> headerParameters, String body, String method) {
        String urlPar = "";
        urlPar = body;
        headerParameters.put("Content-Type", "application/json;charset=" + ENCODING);


        logger.info("发送参数：{}", urlPar);
        // get方法则地址拼接参数


        // 返回值
        String result = null;
        HttpURLConnection httpConnection = null;
        try {
            // 加载证书
            certificate();
            // 获取连接
            URL Url = new URL(address);
            httpConnection = (HttpURLConnection) Url.openConnection();
            // 设置请求时间
            httpConnection.setConnectTimeout(TIMEOUT);
            // 设置 header
            if (headerParameters != null) {
                Iterator<String> headerKey = headerParameters.keySet().iterator();
                while (headerKey.hasNext()) {
                    String key = headerKey.next();
                    httpConnection.setRequestProperty(key, headerParameters.get(key));
                }
            }
            // 设置请求方法
            httpConnection.setRequestMethod(method);
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            // 写query数据流
            if (!(urlPar == null || urlPar.trim().equals(""))) {
                OutputStream writer = httpConnection.getOutputStream();
                try {
                    writer.write(urlPar.getBytes(ENCODING));
                } finally {
                    if (writer != null) {
                        writer.flush();
                        writer.close();
                    }
                }
            }
            // 请求结果
            String encoding = "UTF-8";
            if (httpConnection.getContentType() != null && httpConnection.getContentType().indexOf("charset=") >= 0) {
                encoding = httpConnection.getContentType().substring(httpConnection.getContentType().indexOf("charset=") + 8);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), encoding));
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return result;
    }


    /**
     * 发送https请求
     *
     * @param address          地址url
     * @param headerParameters 请求头
     * @param bodyMap          请求参数
     * @param method           请求方式（GET,POST）
     * @param selfcertpwd      PKCS12格式证书秘钥密码
     * @param trustcapwd       JKS格式证书秘钥密码
     * @param selfcertpath     PKCS12格式证书路径
     * @param trustcapath      KS格式证书路径
     * @return
     */
    public static String httpsSendForm(String address, Map<String, String> headerParameters, Map<String, String> bodyMap, String method, String selfcertpwd, String trustcapwd, String selfcertpath, String trustcapath) {
        String urlPar = "";



        // 获取发送数据  key=value&key=value&...
        StringBuilder body = new StringBuilder();
        Iterator<String> iteratorHeader = bodyMap.keySet().iterator();
        while (iteratorHeader.hasNext()) {
            String key = iteratorHeader.next();
            String value = bodyMap.get(key);
            try {
                body.append(key + "=" + URLEncoder.encode(value, ENCODING) + "&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (body.length() != 0) {
            urlPar = body.substring(0, body.length() - 1);
        }
        headerParameters.put("Content-Type", "application/x-www-form-urlencoded;charset=" + ENCODING);



        logger.info("发送参数：{}", urlPar);
        // get方法则地址拼接参数
        if ("GET".equals(method)) {
            address += "?" + urlPar;
        }
        // 返回值
        String result = null;
        HttpURLConnection httpConnection = null;
        try {
            // 加载证书
            certificate(selfcertpwd, trustcapwd, selfcertpath, trustcapath);
            // 获取连接
            URL Url = new URL(address);
            httpConnection = (HttpURLConnection) Url.openConnection();
            // 设置请求时间
            httpConnection.setConnectTimeout(TIMEOUT);
            // 设置 header
            if (headerParameters != null) {
                Iterator<String> headerKey = headerParameters.keySet().iterator();
                while (headerKey.hasNext()) {
                    String key = headerKey.next();
                    httpConnection.setRequestProperty(key, headerParameters.get(key));
                }
            }
            // 设置请求方法
            httpConnection.setRequestMethod(method);
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            // 写query数据流
            if(!"GET".equals(method)){
                if (!(urlPar == null || urlPar.trim().equals(""))) {
                    OutputStream writer = httpConnection.getOutputStream();
                    try {
                        writer.write(urlPar.getBytes(ENCODING));
                    } finally {
                        if (writer != null) {
                            writer.flush();
                            writer.close();
                        }
                    }
                }
            }
            // 请求结果
            String encoding = "UTF-8";
            if (httpConnection.getContentType() != null && httpConnection.getContentType().indexOf("charset=") >= 0) {
                encoding = httpConnection.getContentType().substring(httpConnection.getContentType().indexOf("charset=") + 8);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream(), encoding));
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return result;
    }



    /**
     * 可升级优化为可配置
     */
    public static void certificate(String selfcertpwd, String trustcapwd, String selfcertpath, String trustcapath) {
        // 放开验证证书
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String str, SSLSession session) {
                    return true;
                }
            });
            KeyStore selfCert = KeyStore.getInstance("pkcs12");
            selfCert.load(new FileInputStream(selfcertpath), selfcertpwd.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
            kmf.init(selfCert, selfcertpwd.toCharArray());

            KeyStore caCert = KeyStore.getInstance("jks");
            caCert.load(new FileInputStream(trustcapath), trustcapwd.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
            tmf.init(caCert);

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 可升级优化为可配置
     */
    public static void certificate() {
        // 放开验证证书
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String str, SSLSession session) {
                    return true;
                }
            });
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, null, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {

            String selfcertpwd = "IoM@1234";
            String trustcapwd = "Huawei@123";

            String testPath = "F:\\新建文件夹\\中国电信物联网开放平台应用服务器证书（ca）-北向接口调用认证(new)";
            String selfcertpath = testPath + "/outgoing.CertwithKey.pkcs12";
            String trustcapath = testPath + "/ca.jks";

            //请求地址(我这里测试使用淘宝提供的手机号码信息查询的接口)
            String address = "https://device.api.ct10649.com:8743/iocm/app/sec/v1.1.0/refreshToken";
            //请求参数
            Map<String, String> params = new HashMap<String, String>();
            //这是该接口需要的参数
            params.put("appId", "BltfJzVu_J9p5c2xskLgDHb5ys4a");
            params.put("secret", "f_SCpfV_2ArJak2F6T8f86AtWzca");
            params.put("refreshToken", "9a14b351e2e5618dc377153d729ac645");
            Map<String, String> headerParameters = new HashMap<String, String>();
//            headerParameters.put("Content-Type", "application/x-www-form-urlencoded;charset=" + ENCODING);
//            headerParameters.put("Content-Type", "application/json;charset=" + ENCODING);
            // 调用 get 请求
            String res = httpsSendForm(address, headerParameters, params, "POST", selfcertpwd, trustcapwd, selfcertpath, trustcapath);
            //打印返回参数
            res = res.substring(res.indexOf("{"));//截取
            JSONObject result = JSONObject.parseObject(res);//转JSON
            logger.info("返回数据，{}", result.toString());
        } catch (Exception e) {
            // TODO 异常
            e.printStackTrace();
        }

    }

}