package com.forpleuvoir.suikalib.network;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个类会新建一条线程 来防止线程阻塞
 * 可能会导致线程不安全，修改对象时请注意
 *
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.network
 * @class_name SuikaHttp
 * @create_time 2020/11/13 16:48
 */
public class SuikaHttp {

    private final String url;
    private String params;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> GETParameters = new HashMap<>();

    /**
     * @param url http连接地址
     */
    public SuikaHttp(String url) {
        this.url = url;
    }

    public void post(Callback callback) {
        new Thread(() -> {
            Result result = HttpUtil.post(url, params, headers);
            if (callback != null)
                callback.callback(result);
        }).start();
    }

    public void get(Callback callback) {
        new Thread(() -> {
            Result result = HttpUtil.get(url, GETParameters, headers);
            if (callback != null)
                callback.callback(result);
        }).start();
    }

    public void put(Callback callback) {
        new Thread(() -> {
            Result result = HttpUtil.put(url, params, headers);
            if (callback != null)
                callback.callback(result);
        }).start();
    }

    public void delete(Callback callback) {
        new Thread(() -> {
            Result result = HttpUtil.delete(url, params, headers);
            if (callback != null)
                callback.callback(result);
        }).start();
    }

    public void setToken(String token) {
        headers.put("token", token);
    }

    public void setGETParameters(Map<String, String> GETParameters) {
        this.GETParameters.putAll(GETParameters);
    }

    /**
     * get请求之外的请求使用的参数 推荐使用json格式
     *
     * @param params
     */
    public void setParams(String params) {
        this.params = params;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public interface Callback {
        void callback(Result result);
    }
}
