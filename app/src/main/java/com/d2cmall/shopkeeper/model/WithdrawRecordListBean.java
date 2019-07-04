package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.util.List;

/**
 * Created by LWJ on 2019/3/6.
 * Description : WithdrawRecordListBean
 */

public class WithdrawRecordListBean {

        /**
         * current : 0
         * pages : 0
         * records : [{"amount":0,"arrivalAmount":0,"createDate":"2019-03-06T08:44:15.407Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-06T08:44:15.407Z","modifyMan":"string","payAccount":"string","payDate":"2019-03-06T08:44:15.407Z","paySn":"string","payType":"string","shopId":0,"shopKeeperAccount":"string","shopKeeperId":0,"status":"string","statusName":"string"}]
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
             * arrivalAmount : 0
             * createDate : 2019-03-06T08:44:15.407Z
             * createMan : string
             * deleted : 0
             * id : 0
             * modifyDate : 2019-03-06T08:44:15.407Z
             * modifyMan : string
             * payAccount : string
             * payDate : 2019-03-06T08:44:15.407Z
             * paySn : string
             * payType : string
             * shopId : 0
             * shopKeeperAccount : string
             * shopKeeperId : 0
             * status : string
             * statusName : string
             */

            private double amount;
            private double arrivalAmount;
            private String createDate;
            private String createMan;
            private int deleted;
            private String modifyDate;
            private String modifyMan;
            private String payAccount;
            private String payDate;
            private String paySn;
            private String payType;
            private int shopId;
            private String shopKeeperAccount;
            private int shopKeeperId;
            private String status;
            private String statusName;

            private String cardNum;//提现卡号

            public String getBankType() {
                return bankType;
            }

            public void setBankType(String bankType) {
                this.bankType = bankType;
            }

            private String bankType;//银行卡类型

            public String getCardNum() {
                return cardNum;
            }

            public void setCardNum(String cardNum) {
                this.cardNum = cardNum;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public double getArrivalAmount() {
                return arrivalAmount;
            }

            public void setArrivalAmount(double arrivalAmount) {
                this.arrivalAmount = arrivalAmount;
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

            public String getPayAccount() {
                return payAccount;
            }

            public void setPayAccount(String payAccount) {
                this.payAccount = payAccount;
            }

            public String getPayDate() {
                return payDate;
            }

            public void setPayDate(String payDate) {
                this.payDate = payDate;
            }

            public String getPaySn() {
                return paySn;
            }

            public void setPaySn(String paySn) {
                this.paySn = paySn;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public int getShopId() {
                return shopId;
            }

            public void setShopId(int shopId) {
                this.shopId = shopId;
            }

            public String getShopKeeperAccount() {
                return shopKeeperAccount;
            }

            public void setShopKeeperAccount(String shopKeeperAccount) {
                this.shopKeeperAccount = shopKeeperAccount;
            }

            public int getShopKeeperId() {
                return shopKeeperId;
            }

            public void setShopKeeperId(int shopKeeperId) {
                this.shopKeeperId = shopKeeperId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatusName() {
                return statusName;
            }

            public void setStatusName(String statusName) {
                this.statusName = statusName;
            }
        }
}
