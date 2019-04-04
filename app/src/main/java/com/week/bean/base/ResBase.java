package com.week.bean.base;

/**
 * 返回数据的基类
 */
public class ResBase {
    private int code=200;
    private String message="success";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
