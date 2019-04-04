package com.week.bean.response;

import com.week.bean.base.ResBase;

import java.util.ArrayList;
import java.util.List;

public class ResGetPrintOrderDetails extends ResBase {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private PrintOrderDetails printOrderDetails=new PrintOrderDetails();
        public void setPrintOrderDetails(PrintOrderDetails printOrderDetails) {
            this.printOrderDetails = printOrderDetails;
        }
        public PrintOrderDetails getPrintOrderDetails() {
            return printOrderDetails;
        }

    }

    public class PrintOrderDetails {

        private String shopId;
        private String shopName;
        private int printOrderId;
        private int status;
        private String createTime;
        private String returnCode;
        private String printTime;
        private List<GoodsList> goodsList=new ArrayList<GoodsList>();
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

        public void setPrintOrderId(int printOrderId) {
            this.printOrderId = printOrderId;
        }
        public int getPrintOrderId() {
            return printOrderId;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        public int getStatus() {
            return status;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getCreateTime() {
            return createTime;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }
        public String getReturnCode() {
            return returnCode;
        }

        public void setPrintTime(String printTime) {
            this.printTime = printTime;
        }
        public String getPrintTime() {
            return printTime;
        }

        public void setGoodsList(List<GoodsList> goodsList) {
            this.goodsList = goodsList;
        }
        public List<GoodsList> getGoodsList() {
            return goodsList;
        }

    }
    public class GoodsList {

        private String goodsName;
        private String barcode;
        private int total;
        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }
        public String getGoodsName() {
            return goodsName;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }
        public String getBarcode() {
            return barcode;
        }

        public void setTotal(int total) {
            this.total = total;
        }
        public int getTotal() {
            return total;
        }

    }
}
