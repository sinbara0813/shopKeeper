package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.util.List;

/**
 * Created by LWJ on 2019/5/24.
 * Description : PurchaseItemListBean
 */

public class PurchaseItemListBean {

    /**
     * code : 0
     * data : {"current":0,"pages":0,"records":[{"actualQuantity":0,"createDate":"2019-05-24T01:16:40.862Z","createMan":"string","deleted":0,"difference":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-24T01:16:40.862Z","modifyMan":"string","paymentSn":"string","paymentType":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"purchId":0,"purchSn":"string","quantity":0,"standard":"string","status":"string","statusName":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}],"searchCount":true,"size":0,"total":0}
     * msg : string
     */

        /**
         * current : 0
         * pages : 0
         * records : [{"actualQuantity":0,"createDate":"2019-05-24T01:16:40.862Z","createMan":"string","deleted":0,"difference":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-24T01:16:40.862Z","modifyMan":"string","paymentSn":"string","paymentType":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"purchId":0,"purchSn":"string","quantity":0,"standard":"string","status":"string","statusName":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}]
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
             * actualQuantity : 0
             * createDate : 2019-05-24T01:16:40.862Z
             * createMan : string
             * deleted : 0
             * difference : 0
             * fromShopId : 0
             * id : 0
             * logisticsCom : string
             * logisticsNum : string
             * modifyDate : 2019-05-24T01:16:40.862Z
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
            private long productSkuId;
            private long purchId;
            private String purchSn;
            private int quantity;
            private String standard;
            private String status;
            private String statusName;
            private double supplyPrice;
            private long toShopId;
            private double totalAmount;
            private String fromShopName;

            public String getFromShopName() {
                return fromShopName;
            }

            public void setFromShopName(String fromShopName) {
                this.fromShopName = fromShopName;
            }

            private Long targetId; //入库后生成的商品ID

            public Long getTargetId() {
                return targetId;
            }

            public void setTargetId(Long targetId) {
                this.targetId = targetId;
            }

            private double productPrice;

            public double getProductPrice() {
                return productPrice;
            }

            public void setProductPrice(double productPrice) {
                this.productPrice = productPrice;
            }


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

            public long getProductSkuId() {
                return productSkuId;
            }

            public void setProductSkuId(long productSkuId) {
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
