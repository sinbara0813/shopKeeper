package com.d2cmall.shopkeeper.model;

/**
 * 作者:Created by sinbara on 2019/5/10.
 * 邮箱:hrb940258169@163.com
 */

public class BaseCartBean {
    public Long id;
    public int quantity;
    public String productName;
    public String productPic;
    public double productPrice;
    public double profit;
    public String standard;
    private int allot;
    private int buyout;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }
}
