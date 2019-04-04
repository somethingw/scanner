package com.week.bean.request;

import com.week.bean.base.ReqBase;
//通过店铺id和条码获取商品信息
public class ReqGetGoodsByBarcode extends ReqBase {
    private String token;
    private Params params;
    public ReqGetGoodsByBarcode(String token){
        super();
        super.setApiName("getGoodsByBarcode");
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

        private String barCode;
        private String shopId;
        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }
        public String getBarCode() {
            return barCode;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }
        public String getShopId() {
            return shopId;
        }

    }

}
