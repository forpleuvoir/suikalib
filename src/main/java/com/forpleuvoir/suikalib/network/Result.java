package com.forpleuvoir.suikalib.network;

import com.forpleuvoir.suikalib.util.JsonUtil;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author forpleuvoir
 */
public class Result {
    private int code;
    private String msg;

    /**
     * 返回数据默认为空字符串
     **/
    private Object data = "";

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonStr(this);
    }

    public String getDataAsString() {
        if (data instanceof String) {
            return this.data.toString().replace("\"", "");
        }
        return data.toString();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Result msg(String msg) {
        this.msg = msg;
        return this;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public static Result getResult(String jsonStr, HttpURLConnection connection) {
        Result result = new Result();
        try {
            JsonObject jsonObject = JsonUtil.strToJsonObject(jsonStr);
            result.code = jsonObject.get("code").getAsInt();
            result.msg = jsonObject.get("msg").getAsString();
            result.data = jsonObject.get("data").toString();
        } catch (Exception e) {
            try {
                result.code = connection.getResponseCode();
                result.msg = connection.getResponseMessage();
                result.data = jsonStr;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return result;
    }

    public static Result getExceptionResult(Exception e) {
        return new Result(1010, "程序异常", e);
    }
}
