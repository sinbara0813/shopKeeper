package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LWJ on 2019/5/9.
 * Description : BankInfoBean
 */

public class BankInfoBean extends Id {

        /**
         * bankAddress : string
         * bankName : string
         * bankType : string
         * cardNum : string
         * createDate : 2019-05-09T01:58:01.800Z
         * createMan : string
         * deleted : 0
         * id : 0
         * modifyDate : 2019-05-09T01:58:01.800Z
         * modifyMan : string
         * shopId : 0
         */

        private String bankAddress;//开户行地区
        private String bankName;//开户行名称
        private String bankType;//银行类型
        private String cardNum;//银行卡号
        private String createDate;
        private String createMan;
        private int deleted;
        private String modifyDate;
        private String modifyMan;
        private long shopId;
        private String trueName;

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public long getId() {
             return id;
         }
         public void setId(long id) {
             this.id = id;
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

        public long getShopId() {
            return shopId;
        }

        public void setShopId(long shopId) {
            this.shopId = shopId;
        }
}
