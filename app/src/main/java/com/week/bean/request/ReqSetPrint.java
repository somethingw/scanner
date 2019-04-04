package com.week.bean.request;

import com.week.bean.base.GoodsList;
import com.week.bean.base.ReqBase;

import java.util.ArrayList;
import java.util.List;

public class ReqSetPrint extends ReqBase {

    private String token;
    private Params params=new Params();
    public ReqSetPrint(String token){
        super();
        super.setApiName("setPrint");
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

        private PrintOrder printOrder=new PrintOrder();
        public void setPrintOrder(PrintOrder printOrder) {
            this.printOrder = printOrder;
        }
        public PrintOrder getPrintOrder() {
            return printOrder;
        }

    }
    public class PrintOrder {
        private String shopId;
        private String mark;

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        private List<GoodsList> goodsList=new ArrayList<GoodsList>();
        public void setShopId(String shopId) {
            this.shopId = shopId;
        }
        public String getShopId() {
            return shopId;
        }

        public void setGoodsList(List<GoodsList> goodsList) {
            this.goodsList = goodsList;
        }
        public List<GoodsList> getGoodsList() {
            return goodsList;
        }
    }


}
