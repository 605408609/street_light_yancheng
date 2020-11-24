package com.exc.street.light.em.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Huang Min
 * @date 2020/10/21 20:48
 * @description
 */
@Configuration
public class HttpConnectionPools {
    private static final Logger logger = LoggerFactory.getLogger(HttpConnectionPools.class);
    /**
     * 同时最多连接数
     */
    static final Integer MAX_TOTAL = 20;
    /**
     * 设置最大路由
     */
    static final Integer MAX_PER_ROUTE = 10;
    static final Integer CONNECT_TIMEOUT = 5000;
    static final Integer CONNECTION_REQUEST_TIMEOUT = 5000;
    static final Integer SOCKET_TIMEOUT = 5000;
    /**
     * 重试次数
     */
    static final Integer RETRY_COUNT = 2;



    /**池化管理*/
    private  PoolingHttpClientConnectionManager poolConnManager;
    /**它是线程安全的，所有的线程都可以使用它一起发送http请求*/
    private  CloseableHttpClient httpClient;

    @Bean(name = "createHttpConnectionPool")
    public PoolingHttpClientConnectionManager createHttpConnectionPool() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            // 配置同时支持 HTTP 和 HTPPS
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
            // 初始化连接管理器
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 同时最多连接数
            poolConnManager.setMaxTotal(MAX_TOTAL);
            // 设置最大路由
            poolConnManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
            // 此处解释下MaxtTotal和DefaultMaxPerRoute的区别：
            // 1、MaxtTotal是整个池子的大小；
            // 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
            // MaxtTotal=400 DefaultMaxPerRoute=200
            // 而我只连接到http://www.abc.com时，到这个主机的并发最多只有200；而不是400；
            // 而我连接到http://www.bac.com 和
            // http://www.ccd.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute
            // 初始化httpClient
            httpClient = getConnection();
            logger.info("初始化HttpConnectionPool完毕");
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException错误:{}",e);
            e.printStackTrace();
        } catch (KeyStoreException e) {
            logger.error("KeyStoreException错误:{}",e);
            e.printStackTrace();
        } catch (KeyManagementException e) {
            logger.error("KeyManagementException错误:{}",e);
            e.printStackTrace();
        }
        return poolConnManager;
    }


    private CloseableHttpClient getConnection() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        CloseableHttpClient httpClient = HttpClients.custom()
                // 设置连接池管理
                .setConnectionManager(poolConnManager)
                .setDefaultRequestConfig(config)
                // 设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, false)).build();
        return httpClient;
    }

    public String httpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity());
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                return result;
            } else {
                logger.error("请求URL{},返回错误码:{},{}", url, code,result);
                return null;
            }
        } catch (IOException e) {
            logger.error("http请求异常,请求URL:{},错误:{}",url,e);
        } finally {
            try {
                if (response != null){
                    response.close();
                }
            } catch (IOException e) {
                logger.error("http请求连接放回连接池异常,错误:{}",e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 发送post请求,BasicHeader basicHeader = new BasicHeader("token",videoVO.getTokenField());
     * @param uri   请求地址
     * @param params    请求参数
     * @param heads 请求头 BasicHeader basicHeader = new BasicHeader("token","value");
     * @return
     */
    public String post(String uri, Object params, Header... heads) {
        HttpPost httpPost = new HttpPost(uri);
        CloseableHttpResponse response = null;
        try {
            StringEntity paramEntity = new StringEntity(JSON.toJSONString(params),"UTF-8");
            paramEntity.setContentEncoding("UTF-8");
            paramEntity.setContentType("application/json");
            httpPost.setEntity(paramEntity);
            if (heads != null) {
                httpPost.setHeaders(heads);
            }
            response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            String result = EntityUtils.toString(response.getEntity());
            if (code == HttpStatus.SC_OK) {
                return result;
            } else {
                logger.error("请求URI:{}返回错误码:{},params:{},result:{}", uri, code, params,result);
                return null;
            }
        } catch (IOException e) {
            logger.error("http请求异常", e);
        } finally {
            try {
                if(response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("http请求连接放回连接池异常,错误:{}",e);
                e.printStackTrace();
            }
        }
        return null;
    }
}
