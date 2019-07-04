package com.d2cmall.shopkeeper.model;

/**
 * 作者:Created by sinbara on 2019/5/27.
 * 邮箱:hrb940258169@163.com
 */

public class AllotSkuBean extends BaseCartBean {

    private String createDate;
    private String createMan;
    private String modifyDate;
    private String modifyMan;
    private int deleted;
    private long shopId;
    private long sourceId;
    private long productId;
    private double supplyPrice;
    private double sellPrice;
    private int status;
    private int virtual;
    private int stock;
    private int warnStock;
    private int settleStock;
    private long fromShopId;
    private ProductBean product;

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

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(double supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
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

    public int getSettleStock() {
        return settleStock;
    }

    public void setSettleStock(int settleStock) {
        this.settleStock = settleStock;
    }

    public long getFromShopId() {
        return fromShopId;
    }

    public void setFromShopId(long fromShopId) {
        this.fromShopId = fromShopId;
    }

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public static class ProductBean{
        private String name;
        private String firstPic;
        private double supplyPrice;
        private double price;
        private double profit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirstPic() {
            return firstPic;
        }

        public void setFirstPic(String firstPic) {
            this.firstPic = firstPic;
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
    }
}
