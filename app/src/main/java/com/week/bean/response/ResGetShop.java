package com.week.bean.response;

import com.week.bean.base.ResBase;

import java.util.List;

public class ResGetShop extends ResBase {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private List<Shop> shopList;
        public void setShopList(List<Shop> shopList) {
            this.shopList = shopList;
        }
        public List<Shop> getShopList() {
            return shopList;
        }

    }
    public class Shop {

        private String shopId;
        private String shopName;
        public void setShopId(String shopId) {
            this.shopId = shopId;
        }
        public String getShopId() {
            return shopId;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }
        public String getShopName() {
            return shopName;
        }

    }
}
