package com.d2cmall.shopkeeper.common;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/5/16.
 * 邮箱:hrb940258169@163.com
 */

public class OrderRequest {

    private long draweeId; //付款人id
    private List<String> packageIds; //购物车id
    private int quantity; //数量
    private long skuId;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }

    public long getDraweeId() {
        return draweeId;
    }

    public void setDraweeId(long draweeId) {
        this.draweeId = draweeId;
    }

    public List<String> getPackageIds() {
        return packageIds;
    }

    public void setPackageIds(List<String> packageIds) {
        this.packageIds = packageIds;
    }
}
