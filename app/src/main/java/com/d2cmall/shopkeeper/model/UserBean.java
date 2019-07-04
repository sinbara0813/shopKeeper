package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

/**
 * 作者:Created by sinbara on 2019/2/21.
 * 邮箱:hrb940258169@163.com
 */

public class UserBean extends Id{
        /**
         * id : 1101390646150500353
         * createDate : 2019-03-01 15:56:08
         * createMan : null
         * modifyDate : 2019-03-01 15:56:08
         * modifyMan : null
         * deleted : 0
         * account : 15869134982
         * nickname : null
         * avatar : null
         * accessExpired : 2019-03-31 15:56:08
         * registerIp : 192.168.5.13
         * loginDate : 2019-03-01 15:56:08
         * loginIp : 192.168.5.13
         * status : 1
         * shopId : null
         * role : BOSS
         * remark : null
         * roleName : 店主
         */

        private String createDate;
        private String createMan;
        private String modifyDate;
        private String modifyMan;
        private int deleted;
        private String account;
        private String nickname;
        private String avatar;
        private String accessExpired;
        private String registerIp;
        private String loginDate;
        private String loginIp;
        private int status;
        private long shopId;
        private String role;
        private String remark;
        private String roleName;
        private String loginToken;
        private String def;

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

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

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAccessExpired() {
            return accessExpired;
        }

        public void setAccessExpired(String accessExpired) {
            this.accessExpired = accessExpired;
        }

        public String getRegisterIp() {
            return registerIp;
        }

        public void setRegisterIp(String registerIp) {
            this.registerIp = registerIp;
        }

        public String getLoginDate() {
            return loginDate;
        }

        public void setLoginDate(String loginDate) {
            this.loginDate = loginDate;
        }

        public String getLoginIp() {
            return loginIp;
        }

        public void setLoginIp(String loginIp) {
            this.loginIp = loginIp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getShopId() {
            return shopId;
        }

        public void setShopId(long shopId) {
            this.shopId = shopId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getLoginToken() {
            return loginToken;
        }

        public void setLoginToken(String loginToken) {
            this.loginToken = loginToken;
        }
}
