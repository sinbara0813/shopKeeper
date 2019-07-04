package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.util.List;

/**
 * Created by LWJ on 2019/3/6.
 * Description : OrderListBean
 */

public class OrderListBean {

        /**
         * current : 0
         * pages : 0
         * records : [{"couponWeightingAmount":0,"createDate":"2019-03-06T01:45:26.883Z","createMan":"string","deleted":0,"id":0,"logisticsCom":"string","logisticsNum":"string","memberAccount":"string","memberId":0,"modifyDate":"2019-03-06T01:45:26.883Z","modifyMan":"string","orderSn":"string","payAmount":0,"paymentSn":"string","paymentType":"string","paymentTypeName":"string","product":{"categoryId":0,"classifyId":0,"couponId":0,"createDate":"2019-03-06T01:45:26.883Z","createMan":"string","crowd":0,"crowdEndDate":"2019-03-06T01:45:26.883Z","crowdGroupNum":0,"crowdGroupTime":0,"crowdPrice":0,"crowdStartDate":"2019-03-06T01:45:26.883Z","deleted":0,"description":"string","firstPic":"string","id":0,"modifyDate":"2019-03-06T01:45:26.883Z","modifyMan":"string","name":"string","pic":"string","price":0,"shopId":0,"skuList":[{"createDate":"2019-03-06T01:45:26.883Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-06T01:45:26.883Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}],"sn":"string","status":0,"virtual":0},"productId":0,"productName":"string","productPic":"string","productPrice":0,"productSkuId":0,"quantity":0,"realPrice":0,"shopId":0,"shopName":"string","standard":"string","status":"string","statusName":"string","type":"string","typeName":"string"}]
         * searchCount : true
         * size : 0
         * total : 0
         */

        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private List<OrderDetialBean.OrderItemListBean> records;

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

        public List<OrderDetialBean.OrderItemListBean> getRecords() {
            return records;
        }

        public void setRecords(List<OrderDetialBean.OrderItemListBean> records) {
            this.records = records;
        }

        public static class RecordsBean extends Id{
            /**
             * couponWeightingAmount : 0
             * createDate : 2019-03-06T01:45:26.883Z
             * createMan : string
             * deleted : 0
             * id : 0
             * logisticsCom : string
             * logisticsNum : string
             * memberAccount : string
             * memberId : 0
             * modifyDate : 2019-03-06T01:45:26.883Z
             * modifyMan : string
             * orderSn : string
             * payAmount : 0
             * paymentSn : string
             * paymentType : string
             * paymentTypeName : string
             * product : {"categoryId":0,"classifyId":0,"couponId":0,"createDate":"2019-03-06T01:45:26.883Z","createMan":"string","crowd":0,"crowdEndDate":"2019-03-06T01:45:26.883Z","crowdGroupNum":0,"crowdGroupTime":0,"crowdPrice":0,"crowdStartDate":"2019-03-06T01:45:26.883Z","deleted":0,"description":"string","firstPic":"string","id":0,"modifyDate":"2019-03-06T01:45:26.883Z","modifyMan":"string","name":"string","pic":"string","price":0,"shopId":0,"skuList":[{"createDate":"2019-03-06T01:45:26.883Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-06T01:45:26.883Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}],"sn":"string","status":0,"virtual":0}
             * productId : 0
             * productName : string
             * productPic : string
             * productPrice : 0
             * productSkuId : 0
             * quantity : 0
             * realPrice : 0
             * shopId : 0
             * shopName : string
             * standard : string
             * status : string
             * statusName : string
             * type : string
             * typeName : string
             */

            private double couponWeightingAmount;
            private String createDate;
            private String createMan;
            private int deleted;
            private String logisticsCom;
            private String logisticsNum;
            private String memberAccount;
            private long memberId;
            private String modifyDate;
            private String modifyMan;
            private String orderSn;
            private double payAmount;
            private String paymentSn;
            private String paymentType;
            private String paymentTypeName;
            private ProductBean product;
            private long productId;
            private String productName;
            private String productPic;
            private double productPrice;
            private long productSkuId;
            private int quantity;
            private double realPrice;
            private long shopId;
            private String shopName;
            private String standard;
            private String status;
            private String statusName;
            private String type;
            private String typeName;

            public int getVirtual() {
                return virtual;
            }

            public void setVirtual(int virtual) {
                this.virtual = virtual;
            }

            private int virtual;

            public double getCouponWeightingAmount() {
                return couponWeightingAmount;
            }

            public void setCouponWeightingAmount(double couponWeightingAmount) {
                this.couponWeightingAmount = couponWeightingAmount;
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

            public String getMemberAccount() {
                return memberAccount;
            }

            public void setMemberAccount(String memberAccount) {
                this.memberAccount = memberAccount;
            }

            public long getMemberId() {
                return memberId;
            }

            public void setMemberId(long memberId) {
                this.memberId = memberId;
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

            public ProductBean getProduct() {
                return product;
            }

            public void setProduct(ProductBean product) {
                this.product = product;
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

            public double getProductPrice() {
                return productPrice;
            }

            public void setProductPrice(double productPrice) {
                this.productPrice = productPrice;
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

            public double getRealPrice() {
                return realPrice;
            }

            public void setRealPrice(double realPrice) {
                this.realPrice = realPrice;
            }

            public long getShopId() {
                return shopId;
            }

            public void setShopId(long shopId) {
                this.shopId = shopId;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
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

            public static class ProductBean {
                /**
                 * categoryId : 0
                 * classifyId : 0
                 * couponId : 0
                 * createDate : 2019-03-06T01:45:26.883Z
                 * createMan : string
                 * crowd : 0
                 * crowdEndDate : 2019-03-06T01:45:26.883Z
                 * crowdGroupNum : 0
                 * crowdGroupTime : 0
                 * crowdPrice : 0
                 * crowdStartDate : 2019-03-06T01:45:26.883Z
                 * deleted : 0
                 * description : string
                 * firstPic : string
                 * id : 0
                 * modifyDate : 2019-03-06T01:45:26.883Z
                 * modifyMan : string
                 * name : string
                 * pic : string
                 * price : 0
                 * shopId : 0
                 * skuList : [{"createDate":"2019-03-06T01:45:26.883Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-06T01:45:26.883Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}]
                 * sn : string
                 * status : 0
                 * virtual : 0
                 */

                private int categoryId;
                private int classifyId;
                private int couponId;
                private String createDate;
                private String createMan;
                private int crowd;
                private String crowdEndDate;
                private int crowdGroupNum;
                private int crowdGroupTime;
                private int crowdPrice;
                private String crowdStartDate;
                private int deleted;
                private String description;
                private String firstPic;
                private int id;
                private String modifyDate;
                private String modifyMan;
                private String name;
                private String pic;
                private int price;
                private int shopId;
                private String sn;
                private int status;
                private int virtual;
                private List<SkuListBean> skuList;

                public int getCategoryId() {
                    return categoryId;
                }

                public void setCategoryId(int categoryId) {
                    this.categoryId = categoryId;
                }

                public int getClassifyId() {
                    return classifyId;
                }

                public void setClassifyId(int classifyId) {
                    this.classifyId = classifyId;
                }

                public int getCouponId() {
                    return couponId;
                }

                public void setCouponId(int couponId) {
                    this.couponId = couponId;
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

                public int getCrowd() {
                    return crowd;
                }

                public void setCrowd(int crowd) {
                    this.crowd = crowd;
                }

                public String getCrowdEndDate() {
                    return crowdEndDate;
                }

                public void setCrowdEndDate(String crowdEndDate) {
                    this.crowdEndDate = crowdEndDate;
                }

                public int getCrowdGroupNum() {
                    return crowdGroupNum;
                }

                public void setCrowdGroupNum(int crowdGroupNum) {
                    this.crowdGroupNum = crowdGroupNum;
                }

                public int getCrowdGroupTime() {
                    return crowdGroupTime;
                }

                public void setCrowdGroupTime(int crowdGroupTime) {
                    this.crowdGroupTime = crowdGroupTime;
                }

                public int getCrowdPrice() {
                    return crowdPrice;
                }

                public void setCrowdPrice(int crowdPrice) {
                    this.crowdPrice = crowdPrice;
                }

                public String getCrowdStartDate() {
                    return crowdStartDate;
                }

                public void setCrowdStartDate(String crowdStartDate) {
                    this.crowdStartDate = crowdStartDate;
                }

                public int getDeleted() {
                    return deleted;
                }

                public void setDeleted(int deleted) {
                    this.deleted = deleted;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getFirstPic() {
                    return firstPic;
                }

                public void setFirstPic(String firstPic) {
                    this.firstPic = firstPic;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }

                public int getShopId() {
                    return shopId;
                }

                public void setShopId(int shopId) {
                    this.shopId = shopId;
                }

                public String getSn() {
                    return sn;
                }

                public void setSn(String sn) {
                    this.sn = sn;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getVirtual() {
                    return virtual;
                }

                public void setVirtual(int virtual) {
                    this.virtual = virtual;
                }

                public List<SkuListBean> getSkuList() {
                    return skuList;
                }

                public void setSkuList(List<SkuListBean> skuList) {
                    this.skuList = skuList;
                }

                public static class SkuListBean {
                    /**
                     * createDate : 2019-03-06T01:45:26.883Z
                     * createMan : string
                     * deleted : 0
                     * id : 0
                     * modifyDate : 2019-03-06T01:45:26.883Z
                     * modifyMan : string
                     * productId : 0
                     * sellPrice : 0
                     * shopId : 0
                     * standard : string
                     * stock : 0
                     * warnStock : 0
                     */

                    private String createDate;
                    private String createMan;
                    private int deleted;
                    private int id;
                    private String modifyDate;
                    private String modifyMan;
                    private int productId;
                    private int sellPrice;
                    private int shopId;
                    private String standard;
                    private int stock;
                    private int warnStock;

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

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
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

                    public int getProductId() {
                        return productId;
                    }

                    public void setProductId(int productId) {
                        this.productId = productId;
                    }

                    public int getSellPrice() {
                        return sellPrice;
                    }

                    public void setSellPrice(int sellPrice) {
                        this.sellPrice = sellPrice;
                    }

                    public int getShopId() {
                        return shopId;
                    }

                    public void setShopId(int shopId) {
                        this.shopId = shopId;
                    }

                    public String getStandard() {
                        return standard;
                    }

                    public void setStandard(String standard) {
                        this.standard = standard;
                    }

                    public int getStock() {
                        return stock;
                    }

                    public void setStock(int stock) {
                        this.stock = stock;
                    }

                    public int getWarnStock() {
                        return warnStock;
                    }

                    public void setWarnStock(int warnStock) {
                        this.warnStock = warnStock;
                    }
                }
            }
        }
}
