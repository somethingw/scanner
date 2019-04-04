package com.week.bean.request;

import com.week.bean.base.ReqBase;
//店铺请求体
public class ReqGetShop extends ReqBase {
    private String token;
    private Object params;
    public ReqGetShop(String token){
        super();
        super.setApiName("getShop");
        this.token=token;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }
}
