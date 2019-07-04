package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LWJ on 2019/5/18.
 * Description : AllotListBean
 */

public class AllotListBean {

        /**
         * current : 0
         * pages : 0
         * records : [{"allotItemList":[{"allotId":0,"allotSn":"string","createDate":"2019-05-18T05:13:45.217Z","createMan":"string","deleted":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-18T05:13:45.217Z","modifyMan":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"quantity":0,"standard":"string","status":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}],"createDate":"2019-05-18T05:13:45.217Z","createMan":"string","deleted":0,"fromAddress":"string","fromMobile":"string","fromName":"string","fromShopId":0,"id":0,"modifyDate":"2019-05-18T05:13:45.217Z","modifyMan":"string","productAmount":0,"shopKeeperAccount":"string","shopKeeperId":0,"sn":"string","status":"string","toAddress":"string","toMobile":"string","toName":"string","toShopId":0}]
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

        public static class RecordsBean extends Id {
            /**
             * allotItemList : [{"allotId":0,"allotSn":"string","createDate":"2019-05-18T05:13:45.217Z","createMan":"string","deleted":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-18T05:13:45.217Z","modifyMan":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"quantity":0,"standard":"string","status":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}]
             * createDate : 2019-05-18T05:13:45.217Z
             * createMan : string
             * deleted : 0
             * fromAddress : string
             * fromMobile : string
             * fromName : string
             * fromShopId : 0
             * id : 0
             * modifyDate : 2019-05-18T05:13:45.217Z
             * modifyMan : string
             * productAmount : 0
             * shopKeeperAccount : string
             * shopKeeperId : 0
             * sn : string
             * status : string
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
            private int fromShopId;
            private String modifyDate;
            private String modifyMan;
            private double productAmount;
            private String shopKeeperAccount;
            private long shopKeeperId;
            private String sn;
            private String status;
            private String toAddress;
            private String toMobile;
            private String toName;
            private long toShopId;

            private String statusName;

            public String getStatusName() {
                return statusName;
            }

            public void setStatusName(String statusName) {
                this.statusName = statusName;
            }
            private List<AllotItemListBean> allotItemList;

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

            public int getFromShopId() {
                return fromShopId;
            }

            public void setFromShopId(int fromShopId) {
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

            public List<AllotItemListBean> getAllotItemList() {
                return allotItemList;
            }

            public void setAllotItemList(List<AllotItemListBean> allotItemList) {
                this.allotItemList = allotItemList;
            }

            public static class AllotItemListBean extends BaseCartBean{
                /**
                 * allotId : 0
                 * allotSn : string
                 * createDate : 2019-05-18T05:13:45.217Z
                 * createMan : string
                 * deleted : 0
                 * fromShopId : 0
                 * id : 0
                 * logisticsCom : string
                 * logisticsNum : string
                 * modifyDate : 2019-05-18T05:13:45.217Z
                 * modifyMan : string
                 * productId : 0
                 * productName : string
                 * productPic : string
                 * productSkuId : 0
                 * quantity : 0
                 * standard : string
                 * status : string
                 * supplyPrice : 0
                 * toShopId : 0
                 * totalAmount : 0
                 */

                private long allotId;
                private String allotSn;
                private String createDate;
                private String createMan;
                private int deleted;
                private long fromShopId;
                private String logisticsCom;
                private String logisticsNum;
                private String modifyDate;
                private String modifyMan;
                private long productId;
                private long productSkuId;
                private String status;
                private String statusName;
                private double supplyPrice;
                private long toShopId;
                private double totalAmount;
                private int difference;//异常单标示,实收和申请件数不一致
                private Long targetId; //入库后生成的商品ID

                private String fromShopName;

                public String getFromShopName() {
                    return fromShopName;
                }

                public void setFromShopName(String fromShopName) {
                    this.fromShopName = fromShopName;
                }

                public Long getTargetId() {
                    return targetId;
                }

                public void setTargetId(Long targetId) {
                    this.targetId = targetId;
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

                public long getFromShopId() {
                    return fromShopId;
                }

                public void setFromShopId(long fromShopId) {
                    this.fromShopId = fromShopId;
                }

                public long getId() {
                    return id;
                }

                public void setIdX(long id) {
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

                public double getSupplyPrice() {
                    return supplyPrice;
                }

                public void setSupplyPrice(int supplyPrice) {
                    this.supplyPrice = supplyPrice;
                }

                public double getToShopId() {
                    return toShopId;
                }

                public void setToShopId(int toShopId) {
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
}
