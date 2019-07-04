package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.util.List;

/**
 * Created by LWJ on 2019/3/6.
 * Description : TransactionRecordListBean
 */

public class TransactionRecordListBean {

        /**
         * current : 0
         * pages : 0
         * records : [{"amount":0,"createDate":"2019-03-06T08:44:15.346Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-06T08:44:15.346Z","modifyMan":"string","orderSn":"string","paymentSn":"string","paymentType":"string","paymentTypeName":"string","productName":"string","shopId":0,"status":0,"type":"string","typeName":"string"}]
         * searchCount : true
         * size : 0
         * total : 0
         */

        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private List<RecordsBean> records;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public boolean isSearchCount() {
            return searchCount;
        }

        public void setSearchCount(boolean searchCount) {
            this.searchCount = searchCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean extends Id{
            /**
             * amount : 0
             * createDate : 2019-03-06T08:44:15.346Z
             * createMan : string
             * deleted : 0
             * id : 0
             * modifyDate : 2019-03-06T08:44:15.346Z
             * modifyMan : string
             * orderSn : string
             * paymentSn : string
             * paymentType : string
             * paymentTypeName : string
             * productName : string
             * shopId : 0
             * status : 0
             * type : string
             * typeName : string
             */

            private double amount;
            private String createDate;
            private String createMan;
            private int deleted;
            private String modifyDate;
            private String modifyMan;
            private String orderSn;
            private String paymentSn;
            private String paymentType;
            private String paymentTypeName;
            private String productName;
            private int shopId;
            private int status;
            private String type;
            private String typeName;

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getCreateMan() {
                return createMan;
            }

            public void setCreateMan(String createMan) {
                this.createMan = createMan;
            }

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getModifyDate() {
                return modifyDate;
            }

            public void setModifyDate(String modifyDate) {
                this.modifyDate = modifyDate;
            }

            public String getModifyMan() {
                return modifyMan;
            }

            public void setModifyMan(String modifyMan) {
                this.modifyMan = modifyMan;
            }

            public String getOrderSn() {
                return orderSn;
            }

            public void setOrderSn(String orderSn) {
                this.orderSn = orderSn;
            }

            public String getPaymentSn() {
                return paymentSn;
            }

            public void setPaymentSn(String paymentSn) {
                this.paymentSn = paymentSn;
            }

            public String getPaymentType() {
                return paymentType;
            }

            public void setPaymentType(String paymentType) {
                this.paymentType = paymentType;
            }

            public String getPaymentTypeName() {
                return paymentTypeName;
            }

            public void setPaymentTypeName(String paymentTypeName) {
                this.paymentTypeName = paymentTypeName;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public int getShopId() {
                return shopId;
            }

            public void setShopId(int shopId) {
                this.shopId = shopId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }
        }
}
