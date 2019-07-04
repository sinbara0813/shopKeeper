package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.util.List;

/**
 * Created by LWJ on 2019/3/5.
 * Description : CustomerListBean
 */

public class CustomerListBean {


        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private List<RecordsBean> records;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public boolean isSearchCount() {
            return searchCount;
        }

        public void setSearchCount(boolean searchCount) {
            this.searchCount = searchCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean extends Id{
            /**
             * accessExpired : 2019-03-05T05:13:50.891Z
             * account : string
             * avatar : string
             * consumeAmount : 0
             * consumeTimes : 0
             * createDate : 2019-03-05T05:13:50.892Z
             * createMan : string
             * deleted : 0
             * id : 0
             * loginDate : 2019-03-05T05:13:50.892Z
             * loginIp : string
             * loginToken : string
             * modifyDate : 2019-03-05T05:13:50.892Z
             * modifyMan : string
             * nickname : string
             * registerIp : string
             * sex : string
             * status : 0
             */

            private String accessExpired;
            private String account;
            private String avatar;
            private double consumeAmount;
            private int consumeTimes;
            private String createDate;
            private String createMan;
            private int deleted;
            private String loginDate;
            private String loginIp;
            private String loginToken;
            private String modifyDate;
            private String modifyMan;
            private String nickname;
            private String registerIp;
            private String sex;
            private int status;

            public String getAccessExpired() {
                return accessExpired;
            }

            public void setAccessExpired(String accessExpired) {
                this.accessExpired = accessExpired;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public double getConsumeAmount() {
                return consumeAmount;
            }

            public void setConsumeAmount(double consumeAmount) {
                this.consumeAmount = consumeAmount;
            }

            public int getConsumeTimes() {
                return consumeTimes;
            }

            public void setConsumeTimes(int consumeTimes) {
                this.consumeTimes = consumeTimes;
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

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
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

            public String getLoginToken() {
                return loginToken;
            }

            public void setLoginToken(String loginToken) {
                this.loginToken = loginToken;
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

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getRegisterIp() {
                return registerIp;
            }

            public void setRegisterIp(String registerIp) {
                this.registerIp = registerIp;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
}
