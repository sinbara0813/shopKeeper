package com.d2cmall.shopkeeper.model;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/6/21.
 * 邮箱:hrb940258169@163.com
 */
public class StatListBean {

    private int current;
    private int pages;
    private boolean searchCount;
    private int size;
    private int total;
    private List<StatBean> records;

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

    public List<StatBean> getRecords() {
        return records;
    }

    public void setRecords(List<StatBean> records) {
        this.records = records;
    }

}
