package com.exc.street.light.security.base.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 *
 * @author LinShiWen
 * @date 2018/3/30
 */
@Component
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final String CHARSET = "UTF-8";
    @Autowired
    private PoolingHttpClientConnectionManager httpClientConnectionManager;
    @Autowired
    private RequestConfig requestConfig;
    @Autowired
    private CloseableHttpClient httpClient;


    /**
     * 发送post请求
     *
     * @param url     请求地址
     * @param map     请求参数
     * @param charset 编码类型
     * @return
     */
    public String doPost(String url, Map<String, String> map, String charset) {
        // CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        CloseableHttpResponse response = null;
        try {
            if (httpClientConnectionManager != null && httpClientConnectionManager.getTotalStats() != null) {
                //打印连接池的状态
                logger.info("now client pool {}", httpClientConnectionManager.getTotalStats().toString());
            }
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            if (map.size() > 0) {
                //设置参数
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
                }
                if (list.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                    httpPost.setEntity(entity);
                }
            }
            //请求连接
            response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            logger.error(logger + "--请求失败:" + e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 发送post请求(json)
     *
     * @param url        请求地址
     * @param jsonObject 请求参数
     * @param token      token
     * @return
     */
    public String doPostJson(String url, JSONObject jsonObject, String token) {
        HttpPost httpPost = null;
        String result = null;
        CloseableHttpResponse response = null;
        try {
            if (httpClientConnectionManager != null && httpClientConnectionManager.getTotalStats() != null) {
                //打印连接池的状态
                logger.info("now client pool {}", httpClientConnectionManager.getTotalStats().toString());
            }
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            // 构建消息实体
            StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            if (token != null) {
                httpPost.setHeader("token", token);
            }
            //请求连接
            response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, CHARSET);
                }
            }
        } catch (Exception e) {
            logger.error(logger + "--请求失败:" + e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送post请求(json)
     *
     * @param url        请求地址
     * @param jsonObject 请求参数
     * @return
     */
    public String doFormJson(String url, JSONObject jsonObject) {
        HttpPost httpPost = null;
        String result = null;
        CloseableHttpResponse response = null;
        try {
            if (httpClientConnectionManager != null && httpClientConnectionManager.getTotalStats() != null) {
                //打印连接池的状态
                logger.info("now client pool {}", httpClientConnectionManager.getTotalStats().toString());
            }
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            // 构建消息实体
            StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(entity);
            //请求连接
            response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, CHARSET);
                }
            }
        } catch (Exception e) {
            logger.error(logger + "--请求失败:" + e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送put请求(json)
     *
     * @param url        请求地址
     * @param jsonObject 请求参数
     * @param token    token
     * @return
     */
    public String doPutJson(String url, JSONObject jsonObject, String token) {
        HttpPut httpPut = null;
        String result = null;
        CloseableHttpResponse response = null;
        try {
            if (httpClientConnectionManager != null && httpClientConnectionManager.getTotalStats() != null) {
                //打印连接池的状态
                logger.info("now client pool {}", httpClientConnectionManager.getTotalStats().toString());
            }
            httpPut = new HttpPut(url);
            httpPut.setConfig(requestConfig);
            // 构建消息实体
            StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding(CHARSET);
            // 发送Json格式的数据请求
            entity.setContentType("application/json");
            httpPut.setEntity(entity);
            if (token != null) {
                httpPut.setHeader("token", token);
            }
            //请求连接
            response = httpClient.execute(httpPut);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, CHARSET);
                }
            }
        } catch (Exception e) {
            logger.error(logger + "--请求失败:" + e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpPut != null) {
                    httpPut.releaseConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送get请求
     *
     * @param url   请求地址
     * @param map   请求参数
     * @param token
     * @return
     */
    public String doGet(String url, Map<String, String> map, String token) {
        HttpGet httpGet = null;
        String result = null;
        CloseableHttpResponse response = null;
        try {
            if (httpClientConnectionManager != null && httpClientConnectionManager.getTotalStats() != null) {
                //打印连接池的状态
                logger.info("now client pool {}", httpClientConnectionManager.getTotalStats().toString());
            }
            String str = "";
            if (map != null && map.size() > 0) {
                //设置参数
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
                }
                if (list.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, CHARSET);
                    str = EntityUtils.toString(entity);
                }
            }
            httpGet = new HttpGet(url + "?" + str);
            if (token != null) {
                httpGet.setHeader("token", token);
            }
            httpGet.setConfig(requestConfig);
            //请求连接
            response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, CHARSET);
                }
            }
        } catch (Exception e) {
            logger.error(logger + "--请求失败:" + e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送get请求
     *
     * @param url 请求地址
     * @param map 请求参数
     * @return
     */
    public String doGet(String url, Map<String, String> map) {
        HttpGet httpGet = null;
        String result = null;
        CloseableHttpResponse response = null;
        try {
            if (httpClientConnectionManager != null && httpClientConnectionManager.getTotalStats() != null) {
                //打印连接池的状态
                logger.info("now client pool {}", httpClientConnectionManager.getTotalStats().toString());
            }
            String str = "";
            if (map.size() > 0) {
                //设置参数
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
                }
                if (list.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, CHARSET);
                    str = EntityUtils.toString(entity);
                }
            }
            httpGet = new HttpGet(url + "?" + str);
            httpGet.setConfig(requestConfig);
            //请求连接
            response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, CHARSET);
                }
            }
        } catch (Exception e) {
            logger.error(logger + "--请求失败:" + e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送get请求
     *
     * @param url 请求地址
     * @param map 请求参数
     * @return
     */
    public String doRestFulGet(String url, Map<String, String> map) {
        HttpGet httpGet = null;
        String result = null;
        CloseableHttpResponse response = null;
        try {
            if (httpClientConnectionManager != null && httpClientConnectionManager.getTotalStats() != null) {
                //打印连接池的状态
                logger.info("now client pool {}", httpClientConnectionManager.getTotalStats().toString());
            }
            String str = "";
            if (map.size() > 0) {
                //设置参数
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                    str = elem.getValue();
                    //list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
                }
                /*if (list.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                    str = EntityUtils.toString(entity);
                }*/
            }
            httpGet = new HttpGet(url + "/" + str);
            httpGet.setConfig(requestConfig);
            //请求连接
            response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, CHARSET);
                }
            }
        } catch (Exception e) {
            logger.error(logger + "--请求失败:" + e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
