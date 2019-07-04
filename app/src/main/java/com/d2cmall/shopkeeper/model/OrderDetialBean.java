package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LWJ on 2019/3/6.
 * Description : OrderDetialBean
 */

public class OrderDetialBean extends Id {

    /**
     * address : string
     * city : string
     * couponAmount : 0
     * couponId : 0
     * couponList : [{"amount":0,"couponId":0,"createDate":"2019-03-18T01:26:52.950Z","createMan":"string","id":0,"memberId":0,"modifyDate":"2019-03-18T01:26:52.950Z","modifyMan":"string","name":"string","needAmount":0,"remark":"string","serviceEndDate":"2019-03-18T01:26:52.950Z","serviceStartDate":"2019-03-18T01:26:52.950Z","shopId":0,"shopName":"string","status":0}]
     * createDate : 2019-03-18T01:26:52.950Z
     * createMan : string
     * crowdGroup : {"attendNum":0,"avatars":"string","createDate":"2019-03-18T01:26:52.950Z","createMan":"string","crowdNum":0,"crowdPrice":0,"deadline":"2019-03-18T01:26:52.950Z","id":0,"modifyDate":"2019-03-18T01:26:52.950Z","modifyMan":"string","paidNum":0,"product":{"category":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0},"categoryId":0,"classify":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0},"classifyId":0,"couponId":0,"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowd":0,"crowdEndDate":"2019-03-18T01:26:52.951Z","crowdGroupNum":0,"crowdGroupTime":0,"crowdPrice":0,"crowdStartDate":"2019-03-18T01:26:52.951Z","deleted":0,"description":"string","firstPic":"string","id":0,"info":"string","modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","pic":"string","price":0,"shopId":0,"skuList":[{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}],"sn":"string","status":0,"stock":0,"targetId":0,"virtual":0},"productId":0,"productName":"string","productPic":"string","productPrice":0,"shopId":0,"standard":"string","status":0}
     * crowdId : 0
     * deleted : 0
     * district : string
     * expireMinute : 0
     * id : 0
     * memberAccount : string
     * memberId : 0
     * mobile : string
     * modifyDate : 2019-03-18T01:26:52.951Z
     * modifyMan : string
     * name : string
     * orderItemList : [{"couponWeightingAmount":0,"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowdGroup":{"attendNum":0,"avatars":"string","createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowdNum":0,"crowdPrice":0,"deadline":"2019-03-18T01:26:52.951Z","id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","paidNum":0,"product":{"category":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0},"categoryId":0,"classify":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0},"classifyId":0,"couponId":0,"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowd":0,"crowdEndDate":"2019-03-18T01:26:52.951Z","crowdGroupNum":0,"crowdGroupTime":0,"crowdPrice":0,"crowdStartDate":"2019-03-18T01:26:52.951Z","deleted":0,"description":"string","firstPic":"string","id":0,"info":"string","modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","pic":"string","price":0,"shopId":0,"skuList":[{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}],"sn":"string","status":0,"stock":0,"targetId":0,"virtual":0},"productId":0,"productName":"string","productPic":"string","productPrice":0,"shopId":0,"standard":"string","status":0},"crowdId":0,"deleted":0,"id":0,"logisticsCom":"string","logisticsNum":"string","memberAccount":"string","memberId":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","orderSn":"string","payAmount":0,"paymentSn":"string","paymentType":"string","paymentTypeName":"string","product":{"category":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0},"categoryId":0,"classify":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0},"classifyId":0,"couponId":0,"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowd":0,"crowdEndDate":"2019-03-18T01:26:52.951Z","crowdGroupNum":0,"crowdGroupTime":0,"crowdPrice":0,"crowdStartDate":"2019-03-18T01:26:52.951Z","deleted":0,"description":"string","firstPic":"string","id":0,"info":"string","modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","pic":"string","price":0,"shopId":0,"skuList":[{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}],"sn":"string","status":0,"stock":0,"targetId":0,"virtual":0},"productId":0,"productName":"string","productPic":"string","productPrice":0,"productSkuId":0,"quantity":0,"realPrice":0,"shopId":0,"shopName":"string","standard":"string","status":"string","statusName":"string","type":"string","typeName":"string"}]
     * payAmount : 0
     * paymentSn : string
     * paymentType : string
     * paymentTypeName : string
     * productAmount : 0
     * province : string
     * shopId : 0
     * shopName : string
     * sn : string
     * status : string
     * statusName : string
     * type : string
     * typeName : string
     */

    private String address;
    private String city;
    private double couponAmount;
    private long couponId;
    private String createDate;
    private String createMan;
    private CrowdGroupBean crowdGroup;
    private long crowdId;
    private int deleted;
    private String district;
    private int expireMinute;
    private String expireDate;
    private String memberAccount;
    private long memberId;
    private String mobile;
    private String modifyDate;
    private String modifyMan;
    private String name;
    private int offline;
    private double payAmount;
    private String paymentSn;
    private String paymentType;
    private String paymentTypeName;
    private double productAmount;
    private String province;
    private long shopId;
    private String shopName;
    private String sn;
    private String status;
    private String statusName;
    private String type;
    private String typeName;
    private List<CouponListBean> couponList;
    private List<OrderItemListBean> orderItemList;

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public int getOffline() {
        return offline;
    }

    public void setOffline(int offline) {
        this.offline = offline;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
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

    public CrowdGroupBean getCrowdGroup() {
        return crowdGroup;
    }

    public void setCrowdGroup(CrowdGroupBean crowdGroup) {
        this.crowdGroup = crowdGroup;
    }

    public long getCrowdId() {
        return crowdId;
    }

    public void setCrowdId(long crowdId) {
        this.crowdId = crowdId;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getExpireMinute() {
        return expireMinute;
    }

    public void setExpireMinute(int expireMinute) {
        this.expireMinute = expireMinute;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public List<CouponListBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponListBean> couponList) {
        this.couponList = couponList;
    }

    public List<OrderItemListBean> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemListBean> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public static class CrowdGroupBean {
        /**
         * attendNum : 0
         * avatars : string
         * createDate : 2019-03-18T01:26:52.950Z
         * createMan : string
         * crowdNum : 0
         * crowdPrice : 0
         * deadline : 2019-03-18T01:26:52.950Z
         * id : 0
         * modifyDate : 2019-03-18T01:26:52.950Z
         * modifyMan : string
         * paidNum : 0
         * product : {"category":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0},"categoryId":0,"classify":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0},"classifyId":0,"couponId":0,"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowd":0,"crowdEndDate":"2019-03-18T01:26:52.951Z","crowdGroupNum":0,"crowdGroupTime":0,"crowdPrice":0,"crowdStartDate":"2019-03-18T01:26:52.951Z","deleted":0,"description":"string","firstPic":"string","id":0,"info":"string","modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","pic":"string","price":0,"shopId":0,"skuList":[{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}],"sn":"string","status":0,"stock":0,"targetId":0,"virtual":0}
         * productId : 0
         * productName : string
         * productPic : string
         * productPrice : 0
         * shopId : 0
         * standard : string
         * status : 0
         */

        private int attendNum;
        private String avatars;
        private String createDate;
        private String createMan;
        private int crowdNum;
        private double crowdPrice;
        private String deadline;
        @SerializedName("id")
        private long idX;
        private String modifyDate;
        private String modifyMan;
        private int paidNum;
        private ProductBean product;
        private long productId;
        private String productName;
        private String productPic;
        private double productPrice;
        private long shopId;
        private String standard;
        private int status;

        public int getAttendNum() {
            return attendNum;
        }

        public void setAttendNum(int attendNum) {
            this.attendNum = attendNum;
        }

        public String getAvatars() {
            return avatars;
        }

        public void setAvatars(String avatars) {
            this.avatars = avatars;
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

        public int getCrowdNum() {
            return crowdNum;
        }

        public void setCrowdNum(int crowdNum) {
            this.crowdNum = crowdNum;
        }

        public double getCrowdPrice() {
            return crowdPrice;
        }

        public void setCrowdPrice(double crowdPrice) {
            this.crowdPrice = crowdPrice;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public long getIdX() {
            return idX;
        }

        public void setIdX(long idX) {
            this.idX = idX;
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

        public int getPaidNum() {
            return paidNum;
        }

        public void setPaidNum(int paidNum) {
            this.paidNum = paidNum;
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

        public long getShopId() {
            return shopId;
        }

        public void setShopId(long shopId) {
            this.shopId = shopId;
        }

        public String getStandard() {
            return standard;
        }

        public void setStandard(String standard) {
            this.standard = standard;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class ProductBean {
            /**
             * category : {"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0}
             * categoryId : 0
             * classify : {"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0}
             * classifyId : 0
             * couponId : 0
             * createDate : 2019-03-18T01:26:52.951Z
             * createMan : string
             * crowd : 0
             * crowdEndDate : 2019-03-18T01:26:52.951Z
             * crowdGroupNum : 0
             * crowdGroupTime : 0
             * crowdPrice : 0
             * crowdStartDate : 2019-03-18T01:26:52.951Z
             * deleted : 0
             * description : string
             * firstPic : string
             * id : 0
             * info : string
             * modifyDate : 2019-03-18T01:26:52.951Z
             * modifyMan : string
             * name : string
             * pic : string
             * price : 0
             * shopId : 0
             * skuList : [{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}]
             * sn : string
             * status : 0
             * stock : 0
             * targetId : 0
             * virtual : 0
             */

            private CategoryBean category;
            private long categoryId;
            private ClassifyBean classify;
            private long classifyId;
            private long couponId;
            private String createDate;
            private String createMan;
            private int crowd;
            private String crowdEndDate;
            private int crowdGroupNum;
            private int crowdGroupTime;
            private double crowdPrice;
            private String crowdStartDate;
            private int deleted;
            private String description;
            private String firstPic;
            private long id;
            private String info;
            private String modifyDate;
            private String modifyMan;
            private String name;
            private String pic;
            private double price;
            private long shopId;
            private String sn;
            private int status;
            private int stock;
            private long targetId;
            private int virtual;
            private List<SkuListBean> skuList;

            public CategoryBean getCategory() {
                return category;
            }

            public void setCategory(CategoryBean category) {
                this.category = category;
            }

            public long getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(long categoryId) {
                this.categoryId = categoryId;
            }

            public ClassifyBean getClassify() {
                return classify;
            }

            public void setClassify(ClassifyBean classify) {
                this.classify = classify;
            }

            public long getClassifyId() {
                return classifyId;
            }

            public void setClassifyId(long classifyId) {
                this.classifyId = classifyId;
            }

            public long getCouponId() {
                return couponId;
            }

            public void setCouponId(long couponId) {
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

            public double getCrowdPrice() {
                return crowdPrice;
            }

            public void setCrowdPrice(double crowdPrice) {
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

            public long getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
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

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public long getShopId() {
                return shopId;
            }

            public void setShopId(long shopId) {
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

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public long getTargetId() {
                return targetId;
            }

            public void setTargetId(long targetId) {
                this.targetId = targetId;
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

            public static class CategoryBean {
                /**
                 * children : [{}]
                 * createDate : 2019-03-18T01:26:52.951Z
                 * createMan : string
                 * id : 0
                 * level : 0
                 * modifyDate : 2019-03-18T01:26:52.951Z
                 * modifyMan : string
                 * name : string
                 * parentId : 0
                 * shopId : 0
                 * sort : 0
                 */

                private String createDate;
                private String createMan;
                private long id;
                private int level;
                private String modifyDate;
                private String modifyMan;
                private String name;
                private long parentId;
                private long shopId;
                private int sort;
                private List<ChildrenBean> children;

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

                public long getId() {
                    return id;
                }

                public void setIdX(long id) {
                    this.id = id;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
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

                public long getParentId() {
                    return parentId;
                }

                public void setParentId(long parentId) {
                    this.parentId = parentId;
                }

                public long getShopId() {
                    return shopId;
                }

                public void setShopId(long shopId) {
                    this.shopId = shopId;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public List<ChildrenBean> getChildren() {
                    return children;
                }

                public void setChildren(List<ChildrenBean> children) {
                    this.children = children;
                }

                public static class ChildrenBean {
                }
            }

            public static class ClassifyBean {
                /**
                 * children : [{}]
                 * createDate : 2019-03-18T01:26:52.951Z
                 * createMan : string
                 * id : 0
                 * level : 0
                 * modifyDate : 2019-03-18T01:26:52.951Z
                 * modifyMan : string
                 * name : string
                 * parentId : 0
                 * sort : 0
                 */

                private String createDate;
                private String createMan;
                private long id;
                private int level;
                private String modifyDate;
                private String modifyMan;
                private String name;
                private long parentId;
                private int sort;
                private List<ChildrenBeanX> children;

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

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
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

                public long getParentId() {
                    return parentId;
                }

                public void setParentId(long parentId) {
                    this.parentId = parentId;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public List<ChildrenBeanX> getChildren() {
                    return children;
                }

                public void setChildren(List<ChildrenBeanX> children) {
                    this.children = children;
                }

                public static class ChildrenBeanX {
                }
            }

            public static class SkuListBean {
                /**
                 * createDate : 2019-03-18T01:26:52.951Z
                 * createMan : string
                 * deleted : 0
                 * id : 0
                 * modifyDate : 2019-03-18T01:26:52.951Z
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
                private long id;
                private String modifyDate;
                private String modifyMan;
                private long productId;
                private double sellPrice;
                private long shopId;
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

                public long getProductId() {
                    return productId;
                }

                public void setProductId(long productId) {
                    this.productId = productId;
                }

                public double getSellPrice() {
                    return sellPrice;
                }

                public void setSellPrice(double sellPrice) {
                    this.sellPrice = sellPrice;
                }

                public long getShopId() {
                    return shopId;
                }

                public void setShopId(long shopId) {
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

    public static class CouponListBean {
        /**
         * amount : 0
         * couponId : 0
         * createDate : 2019-03-18T01:26:52.950Z
         * createMan : string
         * id : 0
         * memberId : 0
         * modifyDate : 2019-03-18T01:26:52.950Z
         * modifyMan : string
         * name : string
         * needAmount : 0
         * remark : string
         * serviceEndDate : 2019-03-18T01:26:52.950Z
         * serviceStartDate : 2019-03-18T01:26:52.950Z
         * shopId : 0
         * shopName : string
         * status : 0
         */

        private double amount;
        private long couponId;
        private String createDate;
        private String createMan;
        private long id;
        private long memberId;
        private String modifyDate;
        private String modifyMan;
        private String name;
        private double needAmount;
        private String remark;
        private String serviceEndDate;
        private String serviceStartDate;
        private long shopId;
        private String shopName;
        private int status;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getCouponId() {
            return couponId;
        }

        public void setCouponId(long couponId) {
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

        public long getId() {
            return id;
        }

        public void setIdX(long id) {
            this.id = id;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getNeedAmount() {
            return needAmount;
        }

        public void setNeedAmount(double needAmount) {
            this.needAmount = needAmount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getServiceEndDate() {
            return serviceEndDate;
        }

        public void setServiceEndDate(String serviceEndDate) {
            this.serviceEndDate = serviceEndDate;
        }

        public String getServiceStartDate() {
            return serviceStartDate;
        }

        public void setServiceStartDate(String serviceStartDate) {
            this.serviceStartDate = serviceStartDate;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class OrderItemListBean extends BaseCartBean{
        /**
         * couponWeightingAmount : 0
         * createDate : 2019-03-18T01:26:52.951Z
         * createMan : string
         * crowdGroup : {"attendNum":0,"avatars":"string","createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowdNum":0,"crowdPrice":0,"deadline":"2019-03-18T01:26:52.951Z","id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","paidNum":0,"product":{"category":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0},"categoryId":0,"classify":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0},"classifyId":0,"couponId":0,"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowd":0,"crowdEndDate":"2019-03-18T01:26:52.951Z","crowdGroupNum":0,"crowdGroupTime":0,"crowdPrice":0,"crowdStartDate":"2019-03-18T01:26:52.951Z","deleted":0,"description":"string","firstPic":"string","id":0,"info":"string","modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","pic":"string","price":0,"shopId":0,"skuList":[{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}],"sn":"string","status":0,"stock":0,"targetId":0,"virtual":0},"productId":0,"productName":"string","productPic":"string","productPrice":0,"shopId":0,"standard":"string","status":0}
         * crowdId : 0
         * deleted : 0
         * id : 0
         * logisticsCom : string
         * logisticsNum : string
         * memberAccount : string
         * memberId : 0
         * modifyDate : 2019-03-18T01:26:52.951Z
         * modifyMan : string
         * orderSn : string
         * payAmount : 0
         * paymentSn : string
         * paymentType : string
         * paymentTypeName : string
         * product : {"category":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0},"categoryId":0,"classify":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0},"classifyId":0,"couponId":0,"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowd":0,"crowdEndDate":"2019-03-18T01:26:52.951Z","crowdGroupNum":0,"crowdGroupTime":0,"crowdPrice":0,"crowdStartDate":"2019-03-18T01:26:52.951Z","deleted":0,"description":"string","firstPic":"string","id":0,"info":"string","modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","pic":"string","price":0,"shopId":0,"skuList":[{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}],"sn":"string","status":0,"stock":0,"targetId":0,"virtual":0}
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
        private CrowdGroupBeanX crowdGroup;
        private long crowdId;
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
        private ProductBeanXX product;
        private long productId;
        private long productSkuId;
        private double realPrice;
        private long shopId;

        private int virtual;
        private String shopName;
        private String status;
        private String statusName;
        private String type;
        private String typeName;

        private double afterAmount;//售后金额 ,
        private String afterMemo;//售后理由
        private String afterPic;//售后凭证
        private int afterQuantity;//售后数量

        private String refuseMemo;//售后拒绝理由
        private String reshipAddress;//退货收货地址
        private String reshipLogisticsCom;//退货物流公司
        private String reshipLogisticsNum;//退货物流单号
        private String reshipMobile;//退货联系电话
        private String reshipName;//退货收货人

        private int afterType; //1退款  2退货退款

        private String afterDate;//售后发起时间

        private int offline;//线下付款

        public int getOffline() {
            return offline;
        }

        public void setOffline(int offline) {
            this.offline = offline;
        }

        public String getAfterDate() {
            return afterDate;
        }

        public void setAfterDate(String afterDate) {
            this.afterDate = afterDate;
        }

        public int getAfterType() {
            return afterType;
        }

        public void setAfterType(int afterType) {
            this.afterType = afterType;
        }

        public double getAfterAmount() {
            return afterAmount;
        }

        public void setAfterAmount(double afterAmount) {
            this.afterAmount = afterAmount;
        }

        public String getAfterMemo() {
            return afterMemo;
        }

        public void setAfterMemo(String afterMemo) {
            this.afterMemo = afterMemo;
        }

        public String getAfterPic() {
            return afterPic;
        }

        public void setAfterPic(String afterPic) {
            this.afterPic = afterPic;
        }

        public int getAfterQuantity() {
            return afterQuantity;
        }

        public void setAfterQuantity(int afterQuantity) {
            this.afterQuantity = afterQuantity;
        }

        public String getRefuseMemo() {
            return refuseMemo;
        }

        public void setRefuseMemo(String refuseMemo) {
            this.refuseMemo = refuseMemo;
        }

        public String getReshipAddress() {
            return reshipAddress;
        }

        public void setReshipAddress(String reshipAddress) {
            this.reshipAddress = reshipAddress;
        }

        public String getReshipLogisticsCom() {
            return reshipLogisticsCom;
        }

        public void setReshipLogisticsCom(String reshipLogisticsCom) {
            this.reshipLogisticsCom = reshipLogisticsCom;
        }

        public String getReshipLogisticsNum() {
            return reshipLogisticsNum;
        }

        public void setReshipLogisticsNum(String reshipLogisticsNum) {
            this.reshipLogisticsNum = reshipLogisticsNum;
        }

        public String getReshipMobile() {
            return reshipMobile;
        }

        public void setReshipMobile(String reshipMobile) {
            this.reshipMobile = reshipMobile;
        }

        public String getReshipName() {
            return reshipName;
        }

        public void setReshipName(String reshipName) {
            this.reshipName = reshipName;
        }


        public int getVirtual() {
            return virtual;
        }

        public void setVirtual(int virtual) {
            this.virtual = virtual;
        }


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

        public CrowdGroupBeanX getCrowdGroup() {
            return crowdGroup;
        }

        public void setCrowdGroup(CrowdGroupBeanX crowdGroup) {
            this.crowdGroup = crowdGroup;
        }

        public long getCrowdId() {
            return crowdId;
        }

        public void setCrowdId(long crowdId) {
            this.crowdId = crowdId;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
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

        public ProductBeanXX getProduct() {
            return product;
        }

        public void setProduct(ProductBeanXX product) {
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

        public static class CrowdGroupBeanX {
            /**
             * attendNum : 0
             * avatars : string
             * createDate : 2019-03-18T01:26:52.951Z
             * createMan : string
             * crowdNum : 0
             * crowdPrice : 0
             * deadline : 2019-03-18T01:26:52.951Z
             * id : 0
             * modifyDate : 2019-03-18T01:26:52.951Z
             * modifyMan : string
             * paidNum : 0
             * product : {"category":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0},"categoryId":0,"classify":{"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0},"classifyId":0,"couponId":0,"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","crowd":0,"crowdEndDate":"2019-03-18T01:26:52.951Z","crowdGroupNum":0,"crowdGroupTime":0,"crowdPrice":0,"crowdStartDate":"2019-03-18T01:26:52.951Z","deleted":0,"description":"string","firstPic":"string","id":0,"info":"string","modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","pic":"string","price":0,"shopId":0,"skuList":[{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}],"sn":"string","status":0,"stock":0,"targetId":0,"virtual":0}
             * productId : 0
             * productName : string
             * productPic : string
             * productPrice : 0
             * shopId : 0
             * standard : string
             * status : 0
             */

            private int attendNum;
            private String avatars;
            private String createDate;
            private String createMan;
            private int crowdNum;
            private double crowdPrice;
            private String deadline;
            private long id;
            private String modifyDate;
            private String modifyMan;
            private int paidNum;
            private ProductBeanX product;
            private long productId;
            private String productName;
            private String productPic;
            private double productPrice;
            private long shopId;
            private String standard;
            private int status;

            public int getAttendNum() {
                return attendNum;
            }

            public void setAttendNum(int attendNum) {
                this.attendNum = attendNum;
            }

            public String getAvatars() {
                return avatars;
            }

            public void setAvatars(String avatars) {
                this.avatars = avatars;
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

            public int getCrowdNum() {
                return crowdNum;
            }

            public void setCrowdNum(int crowdNum) {
                this.crowdNum = crowdNum;
            }

            public double getCrowdPrice() {
                return crowdPrice;
            }

            public void setCrowdPrice(double crowdPrice) {
                this.crowdPrice = crowdPrice;
            }

            public String getDeadline() {
                return deadline;
            }

            public void setDeadline(String deadline) {
                this.deadline = deadline;
            }

            public long getId() {
                return id;
            }

            public void setId(long idX) {
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

            public int getPaidNum() {
                return paidNum;
            }

            public void setPaidNum(int paidNum) {
                this.paidNum = paidNum;
            }

            public ProductBeanX getProduct() {
                return product;
            }

            public void setProduct(ProductBeanX product) {
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

            public long getShopId() {
                return shopId;
            }

            public void setShopId(long shopId) {
                this.shopId = shopId;
            }

            public String getStandard() {
                return standard;
            }

            public void setStandard(String standard) {
                this.standard = standard;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public static class ProductBeanX {
                /**
                 * category : {"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0}
                 * categoryId : 0
                 * classify : {"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0}
                 * classifyId : 0
                 * couponId : 0
                 * createDate : 2019-03-18T01:26:52.951Z
                 * createMan : string
                 * crowd : 0
                 * crowdEndDate : 2019-03-18T01:26:52.951Z
                 * crowdGroupNum : 0
                 * crowdGroupTime : 0
                 * crowdPrice : 0
                 * crowdStartDate : 2019-03-18T01:26:52.951Z
                 * deleted : 0
                 * description : string
                 * firstPic : string
                 * id : 0
                 * info : string
                 * modifyDate : 2019-03-18T01:26:52.951Z
                 * modifyMan : string
                 * name : string
                 * pic : string
                 * price : 0
                 * shopId : 0
                 * skuList : [{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}]
                 * sn : string
                 * status : 0
                 * stock : 0
                 * targetId : 0
                 * virtual : 0
                 */

                private CategoryBeanX category;
                private long categoryId;
                private ClassifyBeanX classify;
                private long classifyId;
                private long couponId;
                private String createDate;
                private String createMan;
                private int crowd;
                private String crowdEndDate;
                private int crowdGroupNum;
                private int crowdGroupTime;
                private double crowdPrice;
                private String crowdStartDate;
                private int deleted;
                private String description;
                private String firstPic;
                private long id;
                private String info;
                private String modifyDate;
                private String modifyMan;
                private String name;
                private String pic;
                private double price;
                private long shopId;
                private String sn;
                private int status;
                private int stock;
                private long targetId;
                private int virtual;
                private List<SkuListBeanX> skuList;

                public CategoryBeanX getCategory() {
                    return category;
                }

                public void setCategory(CategoryBeanX category) {
                    this.category = category;
                }

                public long getCategoryId() {
                    return categoryId;
                }

                public void setCategoryId(long categoryId) {
                    this.categoryId = categoryId;
                }

                public ClassifyBeanX getClassify() {
                    return classify;
                }

                public void setClassify(ClassifyBeanX classify) {
                    this.classify = classify;
                }

                public long getClassifyId() {
                    return classifyId;
                }

                public void setClassifyId(long classifyId) {
                    this.classifyId = classifyId;
                }

                public long getCouponId() {
                    return couponId;
                }

                public void setCouponId(long couponId) {
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

                public double getCrowdPrice() {
                    return crowdPrice;
                }

                public void setCrowdPrice(double crowdPrice) {
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

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
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

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public long getShopId() {
                    return shopId;
                }

                public void setShopId(long shopId) {
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

                public int getStock() {
                    return stock;
                }

                public void setStock(int stock) {
                    this.stock = stock;
                }

                public long getTargetId() {
                    return targetId;
                }

                public void setTargetId(long targetId) {
                    this.targetId = targetId;
                }

                public int getVirtual() {
                    return virtual;
                }

                public void setVirtual(int virtual) {
                    this.virtual = virtual;
                }

                public List<SkuListBeanX> getSkuList() {
                    return skuList;
                }

                public void setSkuList(List<SkuListBeanX> skuList) {
                    this.skuList = skuList;
                }

                public static class CategoryBeanX {
                    /**
                     * children : [{}]
                     * createDate : 2019-03-18T01:26:52.951Z
                     * createMan : string
                     * id : 0
                     * level : 0
                     * modifyDate : 2019-03-18T01:26:52.951Z
                     * modifyMan : string
                     * name : string
                     * parentId : 0
                     * shopId : 0
                     * sort : 0
                     */

                    private String createDate;
                    private String createMan;
                    private long id;
                    private int level;
                    private String modifyDate;
                    private String modifyMan;
                    private String name;
                    private long parentId;
                    private long shopId;
                    private int sort;
                    private List<ChildrenBeanXX> children;

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

                    public long getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getLevel() {
                        return level;
                    }

                    public void setLevel(int level) {
                        this.level = level;
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

                    public long getParentId() {
                        return parentId;
                    }

                    public void setParentId(long parentId) {
                        this.parentId = parentId;
                    }

                    public long getShopId() {
                        return shopId;
                    }

                    public void setShopId(long shopId) {
                        this.shopId = shopId;
                    }

                    public int getSort() {
                        return sort;
                    }

                    public void setSort(int sort) {
                        this.sort = sort;
                    }

                    public List<ChildrenBeanXX> getChildren() {
                        return children;
                    }

                    public void setChildren(List<ChildrenBeanXX> children) {
                        this.children = children;
                    }

                    public static class ChildrenBeanXX {
                    }
                }

                public static class ClassifyBeanX {
                    /**
                     * children : [{}]
                     * createDate : 2019-03-18T01:26:52.951Z
                     * createMan : string
                     * id : 0
                     * level : 0
                     * modifyDate : 2019-03-18T01:26:52.951Z
                     * modifyMan : string
                     * name : string
                     * parentId : 0
                     * sort : 0
                     */

                    private String createDate;
                    private String createMan;
                    private long id;
                    private int level;
                    private String modifyDate;
                    private String modifyMan;
                    private String name;
                    private long parentId;
                    private int sort;
                    private List<ChildrenBeanXXX> children;

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

                    public long getId() {
                        return id;
                    }

                    public void setId(long id) {
                        this.id = id;
                    }

                    public int getLevel() {
                        return level;
                    }

                    public void setLevel(int level) {
                        this.level = level;
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

                    public long getParentId() {
                        return parentId;
                    }

                    public void setParentId(long parentId) {
                        this.parentId = parentId;
                    }

                    public int getSort() {
                        return sort;
                    }

                    public void setSort(int sort) {
                        this.sort = sort;
                    }

                    public List<ChildrenBeanXXX> getChildren() {
                        return children;
                    }

                    public void setChildren(List<ChildrenBeanXXX> children) {
                        this.children = children;
                    }

                    public static class ChildrenBeanXXX {
                    }
                }

                public static class SkuListBeanX {
                    /**
                     * createDate : 2019-03-18T01:26:52.951Z
                     * createMan : string
                     * deleted : 0
                     * id : 0
                     * modifyDate : 2019-03-18T01:26:52.951Z
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
                    private long id;
                    private String modifyDate;
                    private String modifyMan;
                    private long productId;
                    private double sellPrice;
                    private long shopId;
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

                    public long getProductId() {
                        return productId;
                    }

                    public void setProductId(long productId) {
                        this.productId = productId;
                    }

                    public double getSellPrice() {
                        return sellPrice;
                    }

                    public void setSellPrice(double sellPrice) {
                        this.sellPrice = sellPrice;
                    }

                    public long getShopId() {
                        return shopId;
                    }

                    public void setShopId(long shopId) {
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

        public static class ProductBeanXX {
            /**
             * category : {"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"shopId":0,"sort":0}
             * categoryId : 0
             * classify : {"children":[{}],"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","id":0,"level":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","name":"string","parentId":0,"sort":0}
             * classifyId : 0
             * couponId : 0
             * createDate : 2019-03-18T01:26:52.951Z
             * createMan : string
             * crowd : 0
             * crowdEndDate : 2019-03-18T01:26:52.951Z
             * crowdGroupNum : 0
             * crowdGroupTime : 0
             * crowdPrice : 0
             * crowdStartDate : 2019-03-18T01:26:52.951Z
             * deleted : 0
             * description : string
             * firstPic : string
             * id : 0
             * info : string
             * modifyDate : 2019-03-18T01:26:52.951Z
             * modifyMan : string
             * name : string
             * pic : string
             * price : 0
             * shopId : 0
             * skuList : [{"createDate":"2019-03-18T01:26:52.951Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-03-18T01:26:52.951Z","modifyMan":"string","productId":0,"sellPrice":0,"shopId":0,"standard":"string","stock":0,"warnStock":0}]
             * sn : string
             * status : 0
             * stock : 0
             * targetId : 0
             * virtual : 0
             */

            private CategoryBeanXX category;
            private long categoryId;
            private ClassifyBeanXX classify;
            private long classifyId;
            private long couponId;
            private String createDate;
            private String createMan;
            private int crowd;
            private String crowdEndDate;
            private int crowdGroupNum;
            private int crowdGroupTime;
            private double crowdPrice;
            private String crowdStartDate;
            private int deleted;
            private String description;
            private String firstPic;
            private long id;
            private String info;
            private String modifyDate;
            private String modifyMan;
            private String name;
            private String pic;
            private double price;
            private long shopId;
            private String sn;
            private int status;
            private int stock;
            private long targetId;
            private int virtual;
            private List<SkuListBeanXX> skuList;

            public CategoryBeanXX getCategory() {
                return category;
            }

            public void setCategory(CategoryBeanXX category) {
                this.category = category;
            }

            public long getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(long categoryId) {
                this.categoryId = categoryId;
            }

            public ClassifyBeanXX getClassify() {
                return classify;
            }

            public void setClassify(ClassifyBeanXX classify) {
                this.classify = classify;
            }

            public long getClassifyId() {
                return classifyId;
            }

            public void setClassifyId(long classifyId) {
                this.classifyId = classifyId;
            }

            public long getCouponId() {
                return couponId;
            }

            public void setCouponId(long couponId) {
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

            public double getCrowdPrice() {
                return crowdPrice;
            }

            public void setCrowdPrice(double crowdPrice) {
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

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
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

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public long getShopId() {
                return shopId;
            }

            public void setShopId(long shopId) {
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

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public long getTargetId() {
                return targetId;
            }

            public void setTargetId(long targetId) {
                this.targetId = targetId;
            }

            public int getVirtual() {
                return virtual;
            }

            public void setVirtual(int virtual) {
                this.virtual = virtual;
            }

            public List<SkuListBeanXX> getSkuList() {
                return skuList;
            }

            public void setSkuList(List<SkuListBeanXX> skuList) {
                this.skuList = skuList;
            }

            public static class CategoryBeanXX {
                /**
                 * children : [{}]
                 * createDate : 2019-03-18T01:26:52.951Z
                 * createMan : string
                 * id : 0
                 * level : 0
                 * modifyDate : 2019-03-18T01:26:52.951Z
                 * modifyMan : string
                 * name : string
                 * parentId : 0
                 * shopId : 0
                 * sort : 0
                 */

                private String createDate;
                private String createMan;
                private long id;
                private int level;
                private String modifyDate;
                private String modifyMan;
                private String name;
                private long parentId;
                private long shopId;
                private int sort;
                private List<ChildrenBeanXXXX> children;

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

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
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

                public long getParentId() {
                    return parentId;
                }

                public void setParentId(long parentId) {
                    this.parentId = parentId;
                }

                public long getShopId() {
                    return shopId;
                }

                public void setShopId(long shopId) {
                    this.shopId = shopId;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public List<ChildrenBeanXXXX> getChildren() {
                    return children;
                }

                public void setChildren(List<ChildrenBeanXXXX> children) {
                    this.children = children;
                }

                public static class ChildrenBeanXXXX {
                }
            }

            public static class ClassifyBeanXX {
                /**
                 * children : [{}]
                 * createDate : 2019-03-18T01:26:52.951Z
                 * createMan : string
                 * id : 0
                 * level : 0
                 * modifyDate : 2019-03-18T01:26:52.951Z
                 * modifyMan : string
                 * name : string
                 * parentId : 0
                 * sort : 0
                 */

                private String createDate;
                private String createMan;
                private long id;
                private int level;
                private String modifyDate;
                private String modifyMan;
                private String name;
                private long parentId;
                private int sort;
                private List<ChildrenBeanXXXXX> children;

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

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
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

                public long getParentId() {
                    return parentId;
                }

                public void setParentId(long parentId) {
                    this.parentId = parentId;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public List<ChildrenBeanXXXXX> getChildren() {
                    return children;
                }

                public void setChildren(List<ChildrenBeanXXXXX> children) {
                    this.children = children;
                }

                public static class ChildrenBeanXXXXX {
                }
            }

            public static class SkuListBeanXX {
                /**
                 * createDate : 2019-03-18T01:26:52.951Z
                 * createMan : string
                 * deleted : 0
                 * id : 0
                 * modifyDate : 2019-03-18T01:26:52.951Z
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
                private long id;
                private String modifyDate;
                private String modifyMan;
                private long productId;
                private double sellPrice;
                private long shopId;
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

                public long getProductId() {
                    return productId;
                }

                public void setProductId(long productId) {
                    this.productId = productId;
                }

                public double getSellPrice() {
                    return sellPrice;
                }

                public void setSellPrice(double sellPrice) {
                    this.sellPrice = sellPrice;
                }

                public long getShopId() {
                    return shopId;
                }

                public void setShopId(long shopId) {
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


