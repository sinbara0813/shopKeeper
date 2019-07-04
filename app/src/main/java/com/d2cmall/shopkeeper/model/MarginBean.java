package com.d2cmall.shopkeeper.model;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/6/20.
 * 邮箱:hrb940258169@163.com
 */
public class MarginBean {
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

    public static class RecordsBean {
        /**
         * amount : 0
         * createDate : 2019-06-20T05:15:12.276Z
         * createMan : string
         * deleted : 0
         * id : 0
         * modifyDate : 2019-06-20T05:15:12.276Z
         * modifyMan : string
         * shopId : 0
         * status : 0
         * type : string
         * typeName : string
         */

        private double amount;
        private String createDate;
        private String createMan;
        private int deleted;
        private int id;
        private String modifyDate;
        private String modifyMan;
        private int shopId;
        private int status;
        private String type;
        private String typeName;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

}
