package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

/**
 * 作者:Created by sinbara on 2019/2/20.
 * 邮箱:hrb940258169@163.com
 */

public class ShopBean extends Id{
    /**
     * address : string
     * authenticate : 0
     * banner : string
     * corporationCard : string
     * corporationName : string
     * corporationPic : string
     * createDate : 2019-02-20T01:59:11.525Z
     * createMan : string
     * deleted : 0
     * enterprise : string
     * hours : string
     * id : 0
     * licenseNum : string
     * licensePic : string
     * logo : string
     * modifyDate : 2019-02-20T01:59:11.525Z
     * modifyMan : string
     * name : string
     * notice : string
     * returnAddress : string
     * scope : string
     * status : 0
     * summary : string
     * telephone : string
     * validDate : 2019-02-20T01:59:11.525Z
     */

    private String address;
    private int authenticate;
    private String banner;
    private String corporationCard;
    private String corporationName;
    private String corporationPic;
    private String createDate;
    private String createMan;
    private int deleted;
    private String enterprise;
    private String hours;
    private String licenseNum;
    private String licensePic;
    private String logo;
    private String modifyDate;
    private String modifyMan;
    private String name;
    private String notice;
    private String returnAddress;
    private String scope;
    private int status;
    private String summary;
    private String telephone;
    private String validDate;
    private double deposit;//保证金
    private double frozen;//冻结保证金
    private String type;//PERSON  BRAND
    private int focusCount; //关注人数
    private String backAddress;
    private String reciveAddress;

    public String getBackAddress() {
        return backAddress;
    }

    public void setBackAddress(String backAddress) {
        this.backAddress = backAddress;
    }

    public String getReciveAddress() {
        return reciveAddress;
    }

    public void setReciveAddress(String reciveAddress) {
        this.reciveAddress = reciveAddress;
    }

    public int getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(int focusCount) {
        this.focusCount = focusCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String wechat;

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getFrozen() {
        return frozen;
    }

    public void setFrozen(double frozen) {
        this.frozen = frozen;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    private double balance;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(int authenticate) {
        this.authenticate = authenticate;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getCorporationCard() {
        return corporationCard;
    }

    public void setCorporationCard(String corporationCard) {
        this.corporationCard = corporationCard;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public String getCorporationPic() {
        return corporationPic;
    }

    public void setCorporationPic(String corporationPic) {
        this.corporationPic = corporationPic;
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

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }

    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
}
