package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

/**
 * Created by LWJ on 2019/3/14.
 * Description : WithdrawBean
 */

public class WithdrawBean extends Id {

        /**
         * amount : 0
         * arrivalAmount : 0
         * createDate : 2019-03-14T01:36:23.092Z
         * createMan : string
         * deleted : 0
         * id : 0
         * modifyDate : 2019-03-14T01:36:23.092Z
         * modifyMan : string
         * payAccount : string
         * payDate : 2019-03-14T01:36:23.092Z
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
        private long shopId;
        private String shopKeeperAccount;
        private long shopKeeperId;
        private String status;
        private String statusName;
        private String trueName;

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    private String bankAddress;
        private String bankName;
        private String bankType;
        private String cardNum;


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

        public long getId() {
            return super.id;
        }

        public void setId(long id) {
           super.id = id;
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

        public long getShopId() {
            return shopId;
        }

        public void setShopId(long shopId) {
            this.shopId = shopId;
        }

        public String getShopKeeperAccount() {
            return shopKeeperAccount;
        }

        public void setShopKeeperAccount(String shopKeeperAccount) {
            this.shopKeeperAccount = shopKeeperAccount;
        }

        public long getShopKeeperId() {
            return shopKeeperId;
        }

        public void setShopKeeperId(long shopKeeperId) {
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
