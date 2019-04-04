package com.week.bean.request;

import com.week.bean.base.ReqBase;

import java.util.Date;
//获取历史打印单
public class ReqGetPrintOrderList extends ReqBase {
    private String token;
    private Params params;
    public ReqGetPrintOrderList(String token){
        super();
        super.setApiName("getPrintOrderList");
        this.params=new Params();
        this.token=token;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public class Params {
        private Date startTime;
        private Date endTime;
        private String shopId;
        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }
        public Date getStartTime() {
            return startTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }
        public Date getEndTime() {
            return endTime;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }
        public String getShopId() {
            return shopId;
        }

    }
}
