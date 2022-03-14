package cn.lllllan.springbootlearn.utils;

public class JsonData {

    /**
     * 状态码
     * - 0 表示成功
     * - 1 表示处理中
     * - -1 表示失败
     */
    private Integer code;

    private Object data;

    private String msg;

    public JsonData() {
    }

    public JsonData(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    /**
     * 成功，不返回数据
     *
     * @return
     */
    public static JsonData buildSuccess() {
        return new JsonData(0, null, null);
    }

    /**
     * 成功，返回数据
     *
     * @param data 返回的数据
     * @return
     */
    public static JsonData buildSuccess(Object data) {
        return new JsonData(0, data, null);
    }

    /**
     * 成功，返回数据和描述
     *
     * @param data 返回的数据
     * @param msg  返回的描述
     * @return
     */
    public static JsonData buildSuccess(Object data, String msg) {
        return new JsonData(0, data, msg);
    }

    /**
     * 失败，不返回数据
     *
     * @return
     */
    public static JsonData buildError() {
        return new JsonData(-1, null, null);
    }

    /**
     * 失败，返回描述
     *
     * @param msg 返回的描述
     * @return
     */
    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    /**
     * 失败，返回自定义状态码、描述
     *
     * @param code 自定义状态码
     * @param msg  返回的描述
     * @return
     */
    public static JsonData buildError(Integer code, String msg) {
        return new JsonData(code, null, msg);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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
