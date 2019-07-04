package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LWJ on 2019/5/27.
 * Description : AllotDetialBean
 */

public class AllotDetialBean extends Id {
        /**
         * id : 1129679456390959105
         * createDate : 2019-05-18 17:25:45
         * createMan : 18595750208
         * modifyDate : 2019-05-23 13:52:05
         * modifyMan : test20
         * deleted : 0
         * sn : A1129679456349327360
         * status : AGREE
         * shopKeeperId : 1106014363581165570
         * shopKeeperAccount : 18595750208
         * fromShopId : 1105731580711473153
         * fromName : dong
         * fromMobile : 15695887428
         * fromAddress : 浙江省杭州市上城区
         * toShopId : 1110426247048908801
         * toName : Depart
         * toMobile : 18595750208
         * toAddress : 浙江省杭州市上城区
         * productAmount : 0.0
         * statusName : 已审核
         * allotItemList : [{"id":1129679456785223681,"createDate":"2019-05-18 17:25:46","createMan":"18595750208","modifyDate":"2019-05-23 14:30:20","modifyMan":"test20","deleted":0,"allotId":1129679456390959105,"allotSn":"A1129679456349327360","status":"DELIVER","fromShopId":1105731580711473153,"toShopId":1110426247048908801,"productId":1128542015171006465,"productSkuId":1128542016198610946,"quantity":1,"standard":"粉色","productName":"夏季外套","productPic":"/app/f/19/05/15/a57e8f156046df6c2712070a19b6ee0c","supplyPrice":0,"totalAmount":0,"logisticsCode":null,"logisticsCom":"顺丰速递","logisticsNum":"123123123","actualQuantity":null,"difference":null,"statusName":"已发货"}]
         */

        private String createDate;
        private String createMan;
        private String modifyDate;
        private String modifyMan;
        private int deleted;
        private String sn;
        private String status;
        private long shopKeeperId;
        private String shopKeeperAccount;
        private long fromShopId;
        private String fromName;
        private String fromMobile;
        private String fromAddress;
        private long toShopId;
        private String toName;
        private String toMobile;
        private String toAddress;
        private double productAmount;
        private String statusName;
        private List<AllotItemListBean> allotItemList;

        public long getId() {
            return id;
        }

        public void setIdX(long id) {
            this.id = id;
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

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
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

        public long getShopKeeperId() {
            return shopKeeperId;
        }

        public void setShopKeeperId(long shopKeeperId) {
            this.shopKeeperId = shopKeeperId;
        }

        public String getShopKeeperAccount() {
            return shopKeeperAccount;
        }

        public void setShopKeeperAccount(String shopKeeperAccount) {
            this.shopKeeperAccount = shopKeeperAccount;
        }

        public long getFromShopId() {
            return fromShopId;
        }

        public void setFromShopId(long fromShopId) {
            this.fromShopId = fromShopId;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public String getFromMobile() {
            return fromMobile;
        }

        public void setFromMobile(String fromMobile) {
            this.fromMobile = fromMobile;
        }

        public String getFromAddress() {
            return fromAddress;
        }

        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        public long getToShopId() {
            return toShopId;
        }

        public void setToShopId(long toShopId) {
            this.toShopId = toShopId;
        }

        public String getToName() {
            return toName;
        }

        public void setToName(String toName) {
            this.toName = toName;
        }

        public String getToMobile() {
            return toMobile;
        }

        public void setToMobile(String toMobile) {
            this.toMobile = toMobile;
        }

        public String getToAddress() {
            return toAddress;
        }

        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        public double getProductAmount() {
            return productAmount;
        }

        public void setProductAmount(double productAmount) {
            this.productAmount = productAmount;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public List<AllotItemListBean> getAllotItemList() {
            return allotItemList;
        }

        public void setAllotItemList(List<AllotItemListBean> allotItemList) {
            this.allotItemList = allotItemList;
        }

        public static class AllotItemListBean extends Id{
            /**
             * id : 1129679456785223681
             * createDate : 2019-05-18 17:25:46
             * createMan : 18595750208
             * modifyDate : 2019-05-23 14:30:20
             * modifyMan : test20
             * deleted : 0
             * allotId : 1129679456390959105
             * allotSn : A1129679456349327360
             * status : DELIVER
             * fromShopId : 1105731580711473153
             * toShopId : 1110426247048908801
             * productId : 1128542015171006465
             * productSkuId : 1128542016198610946
             * quantity : 1
             * standard : 粉色
             * productName : 夏季外套
             * productPic : /app/f/19/05/15/a57e8f156046df6c2712070a19b6ee0c
             * supplyPrice : 0.0
             * totalAmount : 0.0
             * logisticsCode : null
             * logisticsCom : 顺丰速递
             * logisticsNum : 123123123
             * actualQuantity : null
             * difference : null
             * statusName : 已发货
             */

            private String createDate;
            private String createMan;
            private String modifyDate;
            private String modifyMan;
            private int deleted;
            private long allotId;
            private String allotSn;
            private String status;
            private long fromShopId;
            private long toShopId;
            private long productId;
            private long productSkuId;
            private int quantity;
            private String standard;
            private String productName;
            private String productPic;
            private double supplyPrice;
            private double totalAmount;
            private String logisticsCode;
            private String logisticsCom;
            private String logisticsNum;
            private int actualQuantity;
            private int difference;
            private String statusName;

            public long getId() {
                return id;
            }

            public void setId(long idX) {
                this.id = idX;
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

            public int getDeleted() {
                return deleted;
            }

            public void setDeleted(int deleted) {
                this.deleted = deleted;
            }

            public long getAllotId() {
                return allotId;
            }

            public void setAllotId(long allotId) {
                this.allotId = allotId;
            }

            public String getAllotSn() {
                return allotSn;
            }

            public void setAllotSn(String allotSn) {
                this.allotSn = allotSn;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public long getFromShopId() {
                return fromShopId;
            }

            public void setFromShopId(long fromShopId) {
                this.fromShopId = fromShopId;
            }

            public long getToShopId() {
                return toShopId;
            }

            public void setToShopId(long toShopId) {
                this.toShopId = toShopId;
            }

            public long getProductId() {
                return productId;
            }

            public void setProductId(long productId) {
                this.productId = productId;
            }

            public long getProductSkuId() {
                return productSkuId;
            }

            public void setProductSkuId(long productSkuId) {
                this.productSkuId = productSkuId;
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

            public double getSupplyPrice() {
                return supplyPrice;
            }

            public void setSupplyPrice(double supplyPrice) {
                this.supplyPrice = supplyPrice;
            }

            public double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public String getLogisticsCode() {
                return logisticsCode;
            }

            public void setLogisticsCode(String logisticsCode) {
                this.logisticsCode = logisticsCode;
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

            public int getActualQuantity() {
                return actualQuantity;
            }

            public void setActualQuantity(int actualQuantity) {
                this.actualQuantity = actualQuantity;
            }

            public int getDifference() {
                return difference;
            }

            public void setDifference(int difference) {
                this.difference = difference;
            }

            public String getStatusName() {
                return statusName;
            }

            public void setStatusName(String statusName) {
                this.statusName = statusName;
            }
        }
}
