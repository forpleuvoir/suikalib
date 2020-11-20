package com.forpleuvoir.suikalib.network;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.network
 * @class_name HttpUtil
 * @create_time 2020/11/13 15:38
 */

public class HttpUtil {

    /**
     * 发送post请求
     *
     * @param httpUrl 链接地址
     * @param params  参数 为json格式的字符串
     * @param headers HttpRequestHeader属性map集合 一般用来添加token
     * @return {@link Result}
     */
    public static Result post(String httpUrl, String params, Map<String, String> headers) {
        return base(httpUrl, params, headers, Type.POST);
    }

    /**
     * 发送get请求
     *
     * @param httpUrl 链接地址
     * @param headers HttpRequestHeader属性map集合 一般用来添加token
     * @return {@link Result}
     */
    public static Result get(String httpUrl, Map<String, String> params, Map<String, String> headers) {
        try {
            if (params != null && !params.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder(httpUrl).append("?");
                params.forEach((k, v) -> {
                    stringBuilder.append(k).append("=").append(v);
                    stringBuilder.append("&");
                });
                stringBuilder.deleteCharAt(stringBuilder.length());
                httpUrl = stringBuilder.toString();
            }
            URL url = new URL(httpUrl);
            HttpURLConnection conn = getConnection(url, "GET", headers);
            conn.connect();
            return getReturn(conn);
        } catch (Exception e) {
            return Result.getExceptionResult(e).msg("服务器连接失败");
        }
    }


    /**
     * 发送put请求
     *
     * @param httpUrl 链接地址
     * @param params  参数 为json格式的字符串
     * @param headers HttpRequestHeader属性map集合 一般用来添加token
     * @return {@link Result}
     */
    public static Result put(String httpUrl, String params, Map<String, String> headers) {
        return base(httpUrl, params, headers, Type.PUT);
    }

    /**
     * 发送delete请求
     *
     * @param httpUrl 链接地址
     * @param params  参数 为json格式的字符串
     * @param headers HttpRequestHeader属性map集合 一般用来添加token
     * @return {@link Result}
     */
    public static Result delete(String httpUrl, String params, Map<String, String> headers) {
        return base(httpUrl, params, headers, Type.DELETE);
    }

    /**
     * 基础请求
     *
     * @param httpUrl 链接地址
     * @param params  参数 为json格式的字符串
     * @param headers HttpRequestHeader属性map集合 一般用来添加token
     * @return {@link Result}
     */
    private static Result base(String httpUrl, String params, Map<String, String> headers, Type type) {
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = getConnection(url, type.getValue(), headers);
            if (params != null && !params.isEmpty()) {
                OutputStream os = conn.getOutputStream();
                os.write(params.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
            conn.connect();
            return getReturn(conn);
        } catch (Exception e) {
            return Result.getExceptionResult(e).msg("服务器连接失败");
        }
    }

    /**
     * @param connection 连接对象
     * @return {@link Result}
     */
    private static Result getReturn(HttpURLConnection connection) {
        StringBuilder buffer = new StringBuilder();
        try {
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            String result = buffer.toString();
            return Result.getResult(result, connection);
        } catch (Exception e) {
            return Result.getExceptionResult(e);
        }
    }


    private static HttpURLConnection getConnection(URL url, String method, Map<String, String> header) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");
        if (header != null && !header.isEmpty()) {
            header.forEach(conn::addRequestProperty);
        }
        conn.setInstanceFollowRedirects(false);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        return conn;
    }

    private enum Type {
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
