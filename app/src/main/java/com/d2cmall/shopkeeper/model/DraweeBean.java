package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

/**
 * 作者:Created by sinbara on 2019/5/16.
 * 邮箱:hrb940258169@163.com
 */

public class DraweeBean extends Id{

    /**
     * address : string
     * city : string
     * code : string
     * createDate : 2019-05-16T02:22:55.705Z
     * createMan : string
     * district : string
     * id : 0
     * mobile : string
     * modifyDate : 2019-05-16T02:22:55.706Z
     * modifyMan : string
     * name : string
     * province : string
     * shopId : 0
     */

    private String address;
    private String city;
    private String createDate;
    private String createMan;
    private String district;
    private String mobile;
    private String modifyDate;
    private String modifyMan;
    private String name;
    private String province;
    private long shopId;

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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
