package com.week.bean.base;

/**
 * 请求题的基类
 */
public class ReqBase {
    private  String version="1.0";
    private  String apiName="login";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
