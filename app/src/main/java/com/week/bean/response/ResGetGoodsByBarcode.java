package com.week.bean.response;

import com.week.bean.base.ResBase;
//商品返回结果

public class ResGetGoodsByBarcode extends ResBase {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String goodsName;
        private String barCode;
        private int total;
        private String shopName;
        private int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }
        public String getGoodsName() {
            return goodsName;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }
        public String getBarCode() {
            return barCode;
        }

        public void setTotal(int total) {
            this.total = total;
        }
        public int getTotal() {
            return total;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }
        public String getShopName() {
            return shopName;
        }

    }
}
