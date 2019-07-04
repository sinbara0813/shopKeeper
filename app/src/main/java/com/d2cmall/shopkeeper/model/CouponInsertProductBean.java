package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

/**
 * Created by LWJ on 2019/3/11.
 * Description : CouponInsertProductBean
 */

public class CouponInsertProductBean extends Id{

    /**
     * couponId : 0
     * createDate : 2019-03-11T06:10:52.252Z
     * createMan : string
     * id : 0
     * modifyDate : 2019-03-11T06:10:52.252Z
     * modifyMan : string
     * productId : 0
     * type : 0
     */

    private Long couponId;
    private String createDate;
    private String createMan;
    private String modifyDate;
    private String modifyMan;
    private Long productId;
    private int type;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
