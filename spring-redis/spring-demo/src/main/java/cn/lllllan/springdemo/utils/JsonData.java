package cn.lllllan.springdemo.utils;

public class JsonData {

    private int code;

    private Object data;

    private String msg;

    public JsonData() {
    }

    public JsonData(int code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static JsonData success(Object data, String msg) {
        return new JsonData(0, data, msg);
    }

    public static JsonData success(Object data) {
        return new JsonData(0, data, null);
    }

    public static JsonData success() {
        return new JsonData(0, null, null);
    }

    public static JsonData error(int code, String msg) {
        return new JsonData(code, null, msg);
    }

    public static JsonData error(String msg) {
        return new JsonData(-1, null, msg);
    }

    public static JsonData error() {
        return new JsonData(-1, null, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
