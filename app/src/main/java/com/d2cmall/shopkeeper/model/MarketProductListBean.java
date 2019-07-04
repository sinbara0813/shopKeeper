package com.d2cmall.shopkeeper.model;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/5/9.
 * 邮箱:hrb940258169@163.com
 */

public class MarketProductListBean {

    private int total;
    private int size;
    private int current;
    private int p;
    private int ps;
    private boolean searchCount;
    private int pages;
    private List<ProductBean> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<ProductBean> getRecords() {
        return records;
    }

    public void setRecords(List<ProductBean> records) {
        this.records = records;
    }

}
