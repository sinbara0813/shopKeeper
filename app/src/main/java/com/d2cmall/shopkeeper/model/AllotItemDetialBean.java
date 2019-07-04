package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LWJ on 2019/5/24.
 * Description : AllotItemDetialBean
 */

public class AllotItemDetialBean extends Id{

    /**
     * code : 0
     * data : {"allotItemList":[{"actualQuantity":0,"allotId":0,"allotSn":"string","createDate":"2019-05-24T01:16:40.306Z","createMan":"string","deleted":0,"difference":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-24T01:16:40.306Z","modifyMan":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"quantity":0,"standard":"string","status":"string","statusName":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}],"createDate":"2019-05-24T01:16:40.306Z","createMan":"string","deleted":0,"fromAddress":"string","fromMobile":"string","fromName":"string","fromShopId":0,"id":0,"modifyDate":"2019-05-24T01:16:40.306Z","modifyMan":"string","productAmount":0,"shopKeeperAccount":"string","shopKeeperId":0,"sn":"string","status":"string","statusName":"string","toAddress":"string","toMobile":"string","toName":"string","toShopId":0}
     * msg : string
     */

        /**
         * allotItemList : [{"actualQuantity":0,"allotId":0,"allotSn":"string","createDate":"2019-05-24T01:16:40.306Z","createMan":"string","deleted":0,"difference":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-24T01:16:40.306Z","modifyMan":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"quantity":0,"standard":"string","status":"string","statusName":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}]
         * createDate : 2019-05-24T01:16:40.306Z
         * createMan : string
         * deleted : 0
         * fromAddress : string
         * fromMobile : string
         * fromName : string
         * fromShopId : 0
         * id : 0
         * modifyDate : 2019-05-24T01:16:40.306Z
         * modifyMan : string
         * productAmount : 0
         * shopKeeperAccount : string
         * shopKeeperId : 0
         * sn : string
         * status : string
         * statusName : string
         * toAddress : string
         * toMobile : string
         * toName : string
         * toShopId : 0
         */

        private String createDate;
        private String createMan;
        private int deleted;
        private String fromAddress;
        private String fromMobile;
        private String fromName;
        private long fromShopId;
        private String modifyDate;
        private String modifyMan;
        private double productAmount;
        private String shopKeeperAccount;
        private long shopKeeperId;
        private String sn;
        private String status;
        private String statusName;
        private String toAddress;
        private String toMobile;
        private String toName;
        private long toShopId;
        private List<AllotDetialBean.AllotItemListBean> allotItemList;

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

        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public String getFromMobile() {
            return fromMobile;
        }

        public void setFromMobile(String fromMobile) {
            this.fromMobile = fromMobile;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public long getFromShopId() {
            return fromShopId;
        }

        public void setFromShopId(long fromShopId) {
            this.fromShopId = fromShopId;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
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

        public double getProductAmount() {
            return productAmount;
        }

        public void setProductAmount(double productAmount) {
            this.productAmount = productAmount;
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

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
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

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public String getToMobile() {
            return toMobile;
        }

        public void setToMobile(String toMobile) {
            this.toMobile = toMobile;
        }

        public String getToName() {
            return toName;
        }

        public void setToName(String toName) {
            this.toName = toName;
        }

        public long getToShopId() {
            return toShopId;
        }

        public void setToShopId(long toShopId) {
            this.toShopId = toShopId;
        }

        public List<AllotDetialBean.AllotItemListBean> getAllotItemList() {
            return allotItemList;
        }

        public void setAllotItemList(List<AllotDetialBean.AllotItemListBean> allotItemList) {
            this.allotItemList = allotItemList;
        }

}
