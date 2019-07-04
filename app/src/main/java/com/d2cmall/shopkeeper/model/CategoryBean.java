package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 作者:Created by sinbara on 2019/2/20.
 * 邮箱:hrb940258169@163.com
 */

public class CategoryBean extends Id {
    /**
     * children : [{}]
     * createDate : 2019-02-20T01:59:11.428Z
     * createMan : string
     * id : 0
     * level : 0
     * modifyDate : 2019-02-20T01:59:11.428Z
     * modifyMan : string
     * name : string
     * parentId : 0
     * shopId : 0
     * sort : 0
     */

    private String createDate;
    private String createMan;
    private int level;
    private String modifyDate;
    private String modifyMan;
    private String name;
    private Long parentId;
    private long shopId;
    private int sort;
    private String pic;
    private List<CategoryBean> children;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<CategoryBean> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryBean> children) {
        this.children = children;
    }

    public static class ChildrenBean {
    }
}
