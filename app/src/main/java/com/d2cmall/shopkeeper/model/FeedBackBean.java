package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LWJ on 2019/4/20.
 * Description : FeedBackBean
 */

public class FeedBackBean extends Id{

    /**
     * content : string
     * createDate : 2019-04-20T02:07:14.640Z
     * createMan : string
     * id : 0
     * modifyDate : 2019-04-20T02:07:14.640Z
     * modifyMan : string
     * shopId : 0
     * shopKeeperAccount : string
     * shopKeeperId : 0
     */

    private String content;
    private String createDate;
    private String createMan;
    private String modifyDate;
    private String modifyMan;
    private long shopId;
    private String shopKeeperAccount;
    private long shopKeeperId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
