package com.timing.qywx.demo.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * HTTPUTIL
 *
 * @author wb
 */
@Slf4j
public class HttpUtil {

    /**
     * http get请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String get(String url, Map<String, Object> params) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        try {
            if (params != null && !params.isEmpty()) {
                StringBuilder urlBuilder = new StringBuilder(url + "?");
                for (String key : params.keySet()) {
                    String temp = key + "=" + params.get(key) + "&";
                    urlBuilder.append(temp);
                }
                url = urlBuilder.toString();
                url = url.substring(0, url.length() - 1);
            }
            HttpGet httpGet = new HttpGet(url);
            httpClient = HttpClients.createDefault();
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10000).setConnectTimeout(10000)
                    .setSocketTimeout(10000).build();
            httpGet.setConfig(config);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Http请求异常，请求地址：{}，请求参数：{}，异常信息：{}",
                    url, JSONObject.toJSONString(params), e.getMessage());
            throw new RuntimeException("请求异常,请稍后再试");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.warn("", e);
            }
        }
        return "";
    }

    public static byte[] getReturnByte(String url, Map<String, Object> params) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        try {
            if (params != null && !params.isEmpty()) {
                StringBuilder urlBuilder = new StringBuilder(url + "?");
                for (String key : params.keySet()) {
                    String temp = key + "=" + params.get(key) + "&";
                    urlBuilder.append(temp);
                }
                url = urlBuilder.toString();
                url = url.substring(0, url.length() - 1);
            }
            HttpGet httpGet = new HttpGet(url);
            httpClient = HttpClients.createDefault();
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10000).setConnectTimeout(10000)
                    .setSocketTimeout(10000).build();
            httpGet.setConfig(config);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toByteArray(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Http请求异常，请求地址：{}，请求参数：{}，异常信息：{}",
                    url, JSONObject.toJSONString(params), e.getMessage());
            throw new RuntimeException("请求异常,请稍后再试");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.warn("", e);
            }
        }
        return null;
    }

    /**
     * http post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String post(String url, Map<String, Object> params) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> parameters = new ArrayList<>();
            for (String key : params.keySet()) {
                parameters.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(parameters, "utf-8");
            httpPost.setEntity(uefEntity);
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10000).setConnectTimeout(10000)
                    .setSocketTimeout(10000).build();
            httpPost.setConfig(config);
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Http请求异常，请求地址：{}，请求参数：{}，异常信息：{}",
                    url, params.toString(), e.getMessage());
            throw new RuntimeException("请求异常,请稍后再试");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.warn("", e);
            }

        }
        return "";
    }

    /**
     * http post请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public static String post(String url, String params) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity sEntity = new StringEntity(params, "utf-8");
            httpPost.setEntity(sEntity);
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10000).setConnectTimeout(10000)
                    .setSocketTimeout(10000).build();
            httpPost.setConfig(config);
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Http请求异常，请求地址：{}，请求参数：{}，异常信息：{}",
                    url, params, e.getMessage());
            throw new RuntimeException("请求异常,请稍后再试");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                log.warn("", e);
            }

        }
        return "";
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param urlStr    发送请求的 URL
     * @param jsonParam 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendJsonPost(String urlStr, JSONObject jsonParam) {
        StringBuilder sb = new StringBuilder();
        try {

            // 创建url资源
            URL url = new URL(urlStr);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(true);
            // 设置维持长连接
            //conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 转换为字节数组
            //byte[] data = (jsonParam.toString()).getBytes();
            // 设置文件长度
            //conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json");
            // 开始连接请求
            conn.connect();
            //一定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(JSON.toJSONString(jsonParam));
            out.flush();
            out.close();
            // 请求返回的状态
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                // 请求返回的数据
                sb.append(resultData(conn));
            } else {
                log.warn("Http请求异常，请求地址：{}，请求响应状态：{},请求响应信息：{}", urlStr,
                        conn.getResponseCode(), sb.append(resultData(conn)).toString());
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Http请求异常，请求地址：{}，请求参数：{}，异常信息：{}",
                    urlStr, jsonParam.toJSONString(), e.getMessage());
            throw new RuntimeException("请求异常,请稍后再试");
        }
        return sb.toString();

    }

    private static StringBuilder resultData(HttpURLConnection conn) {

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
//            String res = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } catch (Exception e) {
            return sb.append(e.getMessage());
        }
        log.info(sb.toString());
        return sb;
    }

    /**
     * 获取api通道数据
     *
     * @param channelId
     * @param tableId
     * @param config
     * @return
     */
    public static String getApiDate(Long channelId, Integer tableId, JSONObject config) {
        //Todo 通过渠道Id 表id 以及配置信息 请求渠道平台接口获取数据
        String bd = "1999/9/9";
        String name = "maliu";
        String phone = "13900000000";
        Random rand = new Random();
        int id = rand.nextInt(10);
        return "[{name:'" + name + "',phone:'" + phone + "',birthday:'" + bd + "',id:" + id + "}]";
    }
}
