package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 作者:Created by sinbara on 2019/2/20.
 * 邮箱:hrb940258169@163.com
 */

public class ProductBean extends Id {
    /**
     * id : 1121295912033767425
     * createDate : 2019-04-25 14:12:33
     * createMan : 18595750208
     * modifyDate : 2019-05-03 18:14:18
     * modifyMan : null
     * deleted : 0
     * shopId : 1110426247048908801
     * sourceId : null
     * sn : null
     * name : 黑白上衣
     * pic : /app/f/19/04/25/30973f5570fbc1b34b39e104a7fa6c5a
     * video : null
     * supplyPrice : null
     * price : 86.0
     * profit : null
     * stock : 10
     * freight : null
     * brandId : null
     * categoryId : 1111434939999375362
     * classifyId : 1105747580962017282
     * description :
     * status : 1
     * virtual : 0
     * crowd : 0
     * crowdPrice : 0.0
     * crowdStartDate : null
     * crowdEndDate : null
     * crowdGroupTime : null
     * crowdGroupNum : 0
     * couponId : 0
     * brand : null
     * category : null
     * classify : null
     * standard : null
     * skuList : []
     * info : 86.00
     * firstPic : /app/f/19/04/25/30973f5570fbc1b34b39e104a7fa6c5a
     * targetId : 1121295912033767425
     */

    private String createDate;
    private String createMan;
    private String modifyDate;
    private String modifyMan;
    private int deleted;
    private long shopId;
    private String sourceId;
    private String sn;
    private String name;
    private String pic;
    private String video;
    private double supplyPrice;
    private double price;
    private double profit;
    private int stock;
    private String freight;
    private long brandId;
    private long categoryId;
    private long classifyId;
    private String description;
    private int status;
    private int virtual;
    private int crowd;
    private double crowdPrice;
    private String crowdStartDate;
    private String crowdEndDate;
    private String crowdGroupTime;
    private int crowdGroupNum;
    private long couponId;
    private Brand brand;
    private CategoryBean category;
    private CategoryBean classify;
    private String standard;
    private String info;
    private String firstPic;
    private long targetId;
    private int allot;
    private int buyout;
    private String fromShopName;
    private int sales;
    private String shopName;
    private String shoplogo;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShoplogo() {
        return shoplogo;
    }

    public void setShoplogo(String shoplogo) {
        this.shoplogo = shoplogo;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getFromShopName() {
        return fromShopName;
    }

    public void setFromShopName(String fromShopName) {
        this.fromShopName = fromShopName;
    }

    public int getAllot() {
        return allot;
    }

    public void setAllot(int allot) {
        this.allot = allot;
    }

    public int getBuyout() {
        return buyout;
    }

    public void setBuyout(int buyout) {
        this.buyout = buyout;
    }

    private boolean hasCouponSelect;//绑定优惠券是否选中

    public boolean getHasCouponSelect() {
        return hasCouponSelect;
    }

    public void setHasCouponSelect(boolean hasCouponSelect) {
        this.hasCouponSelect = hasCouponSelect;
    }
    private List<ProductBean.SkuListBean> skuList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public double getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(double supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(long classifyId) {
        this.classifyId = classifyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getCrowd() {
        return crowd;
    }

    public void setCrowd(int crowd) {
        this.crowd = crowd;
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

    public String getCrowdEndDate() {
        return crowdEndDate;
    }

    public void setCrowdEndDate(String crowdEndDate) {
        this.crowdEndDate = crowdEndDate;
    }

    public String getCrowdGroupTime() {
        return crowdGroupTime;
    }

    public void setCrowdGroupTime(String crowdGroupTime) {
        this.crowdGroupTime = crowdGroupTime;
    }

    public int getCrowdGroupNum() {
        return crowdGroupNum;
    }

    public void setCrowdGroupNum(int crowdGroupNum) {
        this.crowdGroupNum = crowdGroupNum;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public CategoryBean getClassify() {
        return classify;
    }

    public void setClassify(CategoryBean classify) {
        this.classify = classify;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public List<SkuListBean> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuListBean> skuList) {
        this.skuList = skuList;
    }

    public static class Brand extends Id {

        private String createDate;
        private String createMan;
        private String description ;
        private String modifyDate ;
        private String modifyMan ;
        private String name;

        private String address;
        private String logo;
        private String qrCode;
        private long shopId ;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public long getShopId() {
            return shopId;
        }

        public void setShopId(long shopId) {
            this.shopId = shopId;
        }


    }

    public static class SkuListBean extends Id {
        /**
         * createDate : 2019-02-20T01:59:11.507Z
         * createMan : string
         * deleted : 0
         * id : 0
         * modifyDate : 2019-02-20T01:59:11.507Z
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
        private String modifyDate;
        private String modifyMan;
        private Long productId;
        private double sellPrice;
        private Long shopId;
        private String standard;
        private int stock;
        private int warnStock;
        private int virtual;
        private double supplyPrice;
        private long sourceId;
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getSourceId() {
            return sourceId;
        }

        public void setSourceId(long sourceId) {
            this.sourceId = sourceId;
        }

        public double getSupplyPrice() {
            return supplyPrice;
        }

        public void setSupplyPrice(double supplyPrice) {
            this.supplyPrice = supplyPrice;
        }

        public int getVirtual() {
            return virtual;
        }

        public void setVirtual(int virtual) {
            this.virtual = virtual;
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

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public double getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(double sellPrice) {
            this.sellPrice = sellPrice;
        }

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
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
