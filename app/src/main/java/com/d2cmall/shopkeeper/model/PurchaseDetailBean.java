package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.util.List;

/**
 * Created by LWJ on 2019/5/24.
 * Description : PurchaseDetailBean
 */

public class PurchaseDetailBean extends Id{

    /**
     * code : 0
     * data : {"createDate":"2019-05-24T01:16:40.843Z","createMan":"string","deleted":0,"expireDate":"2019-05-24T01:16:40.843Z","expireMinute":0,"fromAddress":"string","fromMobile":"string","fromName":"string","fromShopId":0,"id":0,"modifyDate":"2019-05-24T01:16:40.843Z","modifyMan":"string","payAmount":0,"paymentSn":"string","paymentType":"string","paymentTypeName":"string","productAmount":0,"purchItemList":[{"actualQuantity":0,"createDate":"2019-05-24T01:16:40.843Z","createMan":"string","deleted":0,"difference":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-24T01:16:40.843Z","modifyMan":"string","paymentSn":"string","paymentType":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"purchId":0,"purchSn":"string","quantity":0,"standard":"string","status":"string","statusName":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}],"shopKeeperAccount":"string","shopKeeperId":0,"sn":"string","status":"string","statusName":"string","toAddress":"string","toMobile":"string","toName":"string","toShopId":0}
     * msg : string
     */

        /**
         * createDate : 2019-05-24T01:16:40.843Z
         * createMan : string
         * deleted : 0
         * expireDate : 2019-05-24T01:16:40.843Z
         * expireMinute : 0
         * fromAddress : string
         * fromMobile : string
         * fromName : string
         * fromShopId : 0
         * id : 0
         * modifyDate : 2019-05-24T01:16:40.843Z
         * modifyMan : string
         * payAmount : 0
         * paymentSn : string
         * paymentType : string
         * paymentTypeName : string
         * productAmount : 0
         * purchItemList : [{"actualQuantity":0,"createDate":"2019-05-24T01:16:40.843Z","createMan":"string","deleted":0,"difference":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-24T01:16:40.843Z","modifyMan":"string","paymentSn":"string","paymentType":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"purchId":0,"purchSn":"string","quantity":0,"standard":"string","status":"string","statusName":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}]
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
        private String expireDate;
        private int expireMinute;
        private String fromAddress;
        private String fromMobile;
        private String fromName;
        private long fromShopId;
        private String modifyDate;
        private String modifyMan;
        private double payAmount;
        private String paymentSn;
        private String paymentType;
        private String paymentTypeName;
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
        private List<PurchItemListBean> purchItemList;

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

        public String getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

        public int getExpireMinute() {
            return expireMinute;
        }

        public void setExpireMinute(int expireMinute) {
            this.expireMinute = expireMinute;
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

        public double getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(double payAmount) {
            this.payAmount = payAmount;
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

        public List<PurchItemListBean> getPurchItemList() {
            return purchItemList;
        }

        public void setPurchItemList(List<PurchItemListBean> purchItemList) {
            this.purchItemList = purchItemList;
        }

        public static class PurchItemListBean extends Id{
            /**
             * actualQuantity : 0
             * createDate : 2019-05-24T01:16:40.843Z
             * createMan : string
             * deleted : 0
             * difference : 0
             * fromShopId : 0
             * id : 0
             * logisticsCom : string
             * logisticsNum : string
             * modifyDate : 2019-05-24T01:16:40.843Z
             * modifyMan : string
             * paymentSn : string
             * paymentType : string
             * productId : 0
             * productName : string
             * productPic : string
             * productSkuId : 0
             * purchId : 0
             * purchSn : string
             * quantity : 0
             * standard : string
             * status : string
             * statusName : string
             * supplyPrice : 0
             * toShopId : 0
             * totalAmount : 0
             */

            private int actualQuantity;
            private String createDate;
            private String createMan;
            private int deleted;
            private int difference;
            private long fromShopId;
            private String logisticsCom;
            private String logisticsNum;
            private String modifyDate;
            private String modifyMan;
            private String paymentSn;
            private String paymentType;
            private long productId;
            private String productName;
            private String productPic;
            private int productSkuId;
            private long purchId;
            private String purchSn;
            private int quantity;
            private String standard;
            private String status;
            private String statusName;
            private double supplyPrice;
            private long toShopId;
            private double totalAmount;

            public int getActualQuantity() {
                return actualQuantity;
            }

            public void setActualQuantity(int actualQuantity) {
                this.actualQuantity = actualQuantity;
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

            public int getDifference() {
                return difference;
            }

            public void setDifference(int difference) {
                this.difference = difference;
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

            public String getLogisticsCom() {
                return logisticsCom;
            }

            public void setLogisticsCom(String logisticsCom) {
                this.logisticsCom = logisticsCom;
            }

            public String getLogisticsNum() {
                return logisticsNum;
            }

            public void setLogisticsNum(String logisticsNum) {
                this.logisticsNum = logisticsNum;
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

            public long getProductId() {
                return productId;
            }

            public void setProductId(long productId) {
                this.productId = productId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getProductPic() {
                return productPic;
            }

            public void setProductPic(String productPic) {
                this.productPic = productPic;
            }

            public int getProductSkuId() {
                return productSkuId;
            }

            public void setProductSkuId(int productSkuId) {
                this.productSkuId = productSkuId;
            }

            public long getPurchId() {
                return purchId;
            }

            public void setPurchId(long purchId) {
                this.purchId = purchId;
            }

            public String getPurchSn() {
                return purchSn;
            }

            public void setPurchSn(String purchSn) {
                this.purchSn = purchSn;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getStandard() {
                return standard;
            }

            public void setStandard(String standard) {
                this.standard = standard;
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

            public double getSupplyPrice() {
                return supplyPrice;
            }

            public void setSupplyPrice(double supplyPrice) {
                this.supplyPrice = supplyPrice;
            }

            public long getToShopId() {
                return toShopId;
            }

            public void setToShopId(long toShopId) {
                this.toShopId = toShopId;
            }

            public double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }
        }
}
