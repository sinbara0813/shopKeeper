package com.d2cmall.shopkeeper.model;

/**
 * 作者:Created by sinbara on 2019/5/9.
 * 邮箱:hrb940258169@163.com
 */

public class CartBean extends BaseCartBean{
    /**
     * createDate : 2019-05-09T03:52:55.524Z
     * createMan : string
     * id : 0
     * modifyDate : 2019-05-09T03:52:55.524Z
     * modifyMan : string
     * productId : 0
     * productName : string
     * productPic : string
     * productPrice : 0
     * productSkuId : 0
     * quantity : 0
     * realPrice : 0
     * shop : {"address":"string","authenticate":0,"balance":0,"banner":"string","corporationCard":"string","corporationName":"string","corporationPic":"string","createDate":"2019-05-09T03:52:55.524Z","createMan":"string","deleted":0,"deposit":0,"enterprise":"string","hours":"string","id":0,"licenseNum":"string","licensePic":"string","logo":"string","modifyDate":"2019-05-09T03:52:55.524Z","modifyMan":"string","name":"string","notice":"string","recharge":0,"returnAddress":"string","scope":"string","status":0,"summary":"string","telephone":"string","validDate":"2019-05-09T03:52:55.524Z","wechat":"string"}
     * shopId : 0
     * shopKeeperAccount : string
     * shopKeeperId : 0
     * standard : string
     * status : 0
     * stock : 0
     */

    private String createDate;
    private String createMan;
    private String modifyDate;
    private String modifyMan;
    private long productId;
    private long productSkuId;
    private double realPrice;
    private ShopBean shop;
    private long shopId;
    private String shopKeeperAccount;
    private long shopKeeperId;
    private int status;
    private int stock;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(long productSkuId) {
        this.productSkuId = productSkuId;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
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

    public static class ShopBean {
        /**
         * address : string
         * authenticate : 0
         * balance : 0
         * banner : string
         * corporationCard : string
         * corporationName : string
         * corporationPic : string
         * createDate : 2019-05-09T03:52:55.524Z
         * createMan : string
         * deleted : 0
         * deposit : 0
         * enterprise : string
         * hours : string
         * id : 0
         * licenseNum : string
         * licensePic : string
         * logo : string
         * modifyDate : 2019-05-09T03:52:55.524Z
         * modifyMan : string
         * name : string
         * notice : string
         * recharge : 0
         * returnAddress : string
         * scope : string
         * status : 0
         * summary : string
         * telephone : string
         * validDate : 2019-05-09T03:52:55.524Z
         * wechat : string
         */

        private String address;
        private int authenticate;
        private int balance;
        private String banner;
        private String corporationCard;
        private String corporationName;
        private String corporationPic;
        private String createDate;
        private String createMan;
        private int deleted;
        private int deposit;
        private String enterprise;
        private String hours;
        private long id;
        private String licenseNum;
        private String licensePic;
        private String logo;
        private String modifyDate;
        private String modifyMan;
        private String name;
        private String notice;
        private int recharge;
        private String returnAddress;
        private String scope;
        private int status;
        private String summary;
        private String telephone;
        private String validDate;
        private String wechat;

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

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
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

        public int getDeposit() {
            return deposit;
        }

        public void setDeposit(int deposit) {
            this.deposit = deposit;
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

        public long getId() {
            return id;
        }

        public void setId(long id) {
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

        public int getRecharge() {
            return recharge;
        }

        public void setRecharge(int recharge) {
            this.recharge = recharge;
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

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }
    }
}
