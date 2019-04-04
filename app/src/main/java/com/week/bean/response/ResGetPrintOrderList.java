package com.week.bean.response;

import com.week.bean.base.ResBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//历史打印订单的返回结果
public class ResGetPrintOrderList extends ResBase {

    private Data data=new Data();

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<PrintOrderList> printOrderList=new ArrayList<PrintOrderList>();

        public void setPrintOrderList(List<PrintOrderList> printOrderList) {
            this.printOrderList = printOrderList;
        }
        public List<PrintOrderList> getPrintOrderList() {
            return printOrderList;
        }

    }
    public class PrintOrderList {
        private String shopId;
        private String shopName;
        private int printOrderId;
        private int status;
        private String createTime;
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

    }
}
