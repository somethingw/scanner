package com.week.bean.request;

import com.week.bean.base.ReqBase;

public class ReqGetPrintOrderDetails extends ReqBase {

    private String token;
    private Params params=new Params();
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


    public ReqGetPrintOrderDetails(String token){
        super();
        super.setApiName("getPrintOrderDetails");
        this.token=token;
    }
    public class Params {

        private int printOrderId;
        public void setPrintOrderId(int printOrderId) {
            this.printOrderId = printOrderId;
        }
        public int getPrintOrderId() {
            return printOrderId;
        }

    }
}
