package com.d2cmall.shopkeeper.model;

/**
 * 作者:Created by sinbara on 2019/5/29.
 * 邮箱:hrb940258169@163.com
 */

public class AllotSettleBean {

    /**
     * createDate : 2019-05-29T07:25:44.153Z
     * createMan : string
     * deleted : 0
     * expireDate : 2019-05-29T07:25:44.153Z
     * fromShopId : 0
     * goods : string
     * id : 0
     * modifyDate : 2019-05-29T07:25:44.153Z
     * modifyMan : string
     * payAmount : 0
     * paymentSn : string
     * paymentType : string
     * paymentTypeName : string
     * shopId : 0
     * shopKeeperAccount : string
     * shopKeeperId : 0
     * sn : string
     * status : string
     * statusName : string
     */

    private String createDate;
    private String createMan;
    private int deleted;
    private String expireDate;
    private int expireMinute;
    private long fromShopId;
    private long id;
    private String modifyDate;
    private String modifyMan;
    private double payAmount;
    private String paymentSn;
    private String paymentType;
    private String paymentTypeName;
    private long shopId;
    private String shopKeeperAccount;
    private long shopKeeperId;
    private String sn;
    private String status;
    private String statusName;

    public int getExpireMinute() {
        return expireMinute;
    }

    public void setExpireMinute(int expireMinute) {
        this.expireMinute = expireMinute;
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

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
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

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
