package com.exc.street.light.lj.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * http请求工具类
 *
 * @author Longshuangyang
 * @date 2020/03/28
 */
public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    @Autowired
    private PoolingHttpClientConnectionManager httpClientConnectionManager;
    @Autowired
    private RequestConfig requestConfig;
    @Autowired
    private CloseableHttpClient httpClient;

    /**
     * 发送一个get请求
     *
     * @param url
     * @return response
     * @
     */
    static public String get(String url) {
        return get(url, null);
    }

    /**
     * 发送一个get请求
     *
     * @param url     远程地址
     * @param headers 带标题的可选映射
     * @return response   Response as string
     */
    static public String get(String url,
                             Map<String, String> headers) {
        return fetch("GET", url, null, headers);
    }

    static public String get(String url, JSONObject paramObj,
                             Map<String, String> headers) {
        if (paramObj != null && !paramObj.isEmpty()) {
            Set<Map.Entry<String, Object>> entries = paramObj.entrySet();
            try {
                StringBuilder urlBuilder = new StringBuilder(url);
                for (Map.Entry<String, Object> entry : entries) {
                    urlBuilder.append(urlBuilder.lastIndexOf("?") == -1 ? "?" : "&");
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    urlBuilder.append("=");
                    urlBuilder.append(URLEncoder.encode(entry.getValue() != null ? entry.getValue().toString() : null, "UTF-8"));
                }
                url = urlBuilder.toString();
            } catch (UnsupportedEncodingException e) {
                logger.info("Get请求发生编码错误,url={}", url);
            }

        }
        return fetch("GET", url, null, headers);
    }

    /**
     * 发送一个post请求
     *
     * @param url     远程地址
     * @param body    请求body
     * @param headers 带标题的可选映射
     * @return response   Response as string
     * @
     */
    static public String post(String url, String body,
                              Map<String, String> headers) {
        return fetch("POST", url, body, headers);
    }

    /**
     * 发送一个post请求 抛出exception
     *
     * @param url     远程地址
     * @param body    请求body
     * @param headers 带标题的可选映射
     * @return response   Response as string
     * @
     */
    static public String postThrowException(String url, String body,
                                            Map<String, String> headers) throws IOException {
        return fetchThrowException("POST", url, body, headers);
    }

    /**
     * 发送一个post请求
     *
     * @param url     远程地址
     * @param body    请求body
     * @param headers 带标题的可选映射
     * @return response   Response as string
     * @
     */
    static public String postGbk(String url, String body,
                                 Map<String, String> headers) {
        return fetchGbk("POST", url, body, headers);
    }

    /**
     * 发送一个post请求
     *
     * @param url  远程地址
     * @param body 请求body
     * @return response   Response as string
     * @
     */
    static public String post(String url, String body) {
        return post(url, body, null);
    }

    /**
     * 使用参数发布表单
     *
     * @param url    远程地址
     * @param params 带参数/值的映射
     * @return response   Response as string
     * @
     */
    static public String postForm(String url, Map<String, String> params) {
        return postForm(url, params, null);
    }

    /**
     * 使用参数发布表单
     *
     * @param url     远程地址
     * @param params  带参数/值的映射
     * @param headers 带标题的可选映射
     * @return response   Response as string
     * @
     */
    static public String postForm(String url, Map<String, String> params,
                                  Map<String, String> headers) {
        // set content type
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        // parse parameters
        String body = "";
        try {
            if (params != null) {
                boolean first = true;
                for (String param : params.keySet()) {
                    if (first) {
                        first = false;
                    } else {
                        body += "&";
                    }
                    String value = params.get(param);
                    body += URLEncoder.encode(param, "UTF-8") + "=";
                    body += URLEncoder.encode(value, "UTF-8");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return post(url, body, headers);
    }

    /**
     * 发送一个put请求
     *
     * @param url     远程地址
     * @param body    请求body
     * @param headers 带标题的可选映射
     * @return response   Response as string
     */
    static public String put(String url, String body,
                             Map<String, String> headers) {
        return fetch("PUT", url, body, headers);
    }

    /**
     * 发送一个put请求
     *
     * @param url 远程地址
     * @return response   Response as string
     */
    static public String put(String url, String body) {
        return put(url, body, null);
    }

    /**
     * 发送一个delete请求
     *
     * @param url     远程地址
     * @param headers 带标题的可选映射
     * @return response   Response as string
     */
    static public String delete(String url,String body,
                                Map<String, String> headers) {
        return fetch("DELETE", url, body, headers);
    }

    /**
     * 发送一个delete请求
     *
     * @param url 远程地址
     * @return response   Response as string
     */
    static public String delete(String url) {
        return delete(url,null, null);
    }

    /**
     * 将查询参数附加到给定的url
     *
     * @param url    远程地址
     * @param params 带查询参数的映射
     * @return url        Url with query parameters appended
     */
    static public String appendQueryParams(String url,
                                           Map<String, String> params) {
        String fullUrl = url;
        try {
            if (params != null) {
                boolean first = (fullUrl.indexOf('?') == -1);
                for (String param : params.keySet()) {
                    if (first) {
                        fullUrl += '?';
                        first = false;
                    } else {
                        fullUrl += '&';
                    }
                    String value = params.get(param);
                    fullUrl += URLEncoder.encode(param, "UTF-8") + '=';
                    fullUrl += URLEncoder.encode(value, "UTF-8");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return fullUrl;
    }

    /**
     * 从给定的url检索查询参数
     *
     * @param url 包含查询参数的Url
     * @return params     Map with query parameters
     */
    static public Map<String, String> getQueryParams(String url) {
        Map<String, String> params = new HashMap<String, String>();

        int start = url.indexOf('?');
        try {
            while (start != -1) {
                // read parameter name
                int equals = url.indexOf('=', start);
                String param = "";
                if (equals != -1) {
                    param = url.substring(start + 1, equals);
                } else {
                    param = url.substring(start + 1);
                }

                // read parameter value
                String value = "";
                if (equals != -1) {
                    start = url.indexOf('&', equals);
                    if (start != -1) {
                        value = url.substring(equals + 1, start);
                    } else {
                        value = url.substring(equals + 1);
                    }
                }

                params.put(URLDecoder.decode(param, "UTF-8"),
                        URLDecoder.decode(value, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return params;
    }

    /**
     * 返回不带查询参数的url
     *
     * @param url 包含查询参数的Url
     * @return url        Url without query parameters
     */
    static public String removeQueryParams(String url) {
        int q = url.indexOf('?');
        if (q != -1) {
            return url.substring(0, q);
        } else {
            return url;
        }
    }

    /**
     * 发送请求
     *
     * @param method  HTTP方法
     * @param url     远程地址
     * @param body    请求body
     * @param headers 带标题的可选映射
     * @return response   Response as string
     */
    static public String fetchThrowException(String method, String url, String body,
                                             Map<String, String> headers) throws IOException {
        String response = null;
        // connection
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        // method
        if (method != null) {
            conn.setRequestMethod(method);
        }

        // headers
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.addRequestProperty(key, headers.get(key));
            }
        }

        // body
        if (body != null) {
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes("GBK"));
            os.flush();
            os.close();
        }

        // response
        InputStream is = conn.getInputStream();
        response = streamToString(is, "GBK");
        is.close();

        // handle redirects
        if (conn.getResponseCode() == 301) {
            String location = conn.getHeaderField("Location");
            return fetchThrowException(method, location, body, headers);
        }

        return response;
    }

    /**
     * 发送请求
     *
     * @param method  HTTP方法
     * @param url     远程地址
     * @param body    请求body
     * @param headers 带标题的可选映射
     * @return response   Response as string
     */
    static public String fetch(String method, String url, String body,
                               Map<String, String> headers) {
        String response = null;
        try {
            // connection
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            // method
            if (method != null) {
                conn.setRequestMethod(method);
            }

            // headers
            if (headers != null) {
                for (String key : headers.keySet()) {
                    conn.addRequestProperty(key, headers.get(key));
                }
            }

            // body
            if (body != null) {
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes());
                os.flush();
                os.close();
            }

            // response
            InputStream is = conn.getInputStream();
            response = streamToString(is);
            is.close();

            // handle redirects
            if (conn.getResponseCode() == 301) {
                String location = conn.getHeaderField("Location");
                return fetch(method, location, body, headers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 发送请求
     *
     * @param method  HTTP方法
     * @param url     远程地址
     * @param body    请求body
     * @param headers 带标题的可选映射
     * @return response   Response as string
     */
    static public String fetchGbk(String method, String url, String body,
                                  Map<String, String> headers) {
        String response = null;
        try {
            // connection
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            // method
            if (method != null) {
                conn.setRequestMethod(method);
            }

            // headers
            if (headers != null) {
                for (String key : headers.keySet()) {
                    conn.addRequestProperty(key, headers.get(key));
                }
            }

            // body
            if (body != null) {
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes("GBK"));
                os.flush();
                os.close();
            }

            // response
            InputStream is = conn.getInputStream();
            response = streamToString(is, "GBK");
            is.close();

            // handle redirects
            if (conn.getResponseCode() == 301) {
                String location = conn.getHeaderField("Location");
                return fetchGbk(method, location, body, headers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 将输入流读入字符串
     *
     * @param in
     * @return
     */
    static public String streamToString(InputStream in) {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        try {
            for (int n; (n = in.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }

    /**
     * 使用字符编码，将输入流读入字符串
     *
     * @param in
     * @param charset
     * @return
     */
    static public String streamToString(InputStream in, String charset) {
        StringBuffer out = new StringBuffer();
        try {
            //将字节流转化成字符流，并指定字符集
            InputStreamReader isr = new InputStreamReader(in, charset);
            int tmp;
            while ((tmp = isr.read()) != -1) {
                out.append((char) tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
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
//            entity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(entity);
            logger.info("消息实体:{}" + entity);
            if (token != null) {
                httpPost.setHeader("token", token);
            }
            //请求连接
            response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
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

}