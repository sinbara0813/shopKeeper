package com.d2cmall.shopkeeper.common;

import java.util.List;

public class Province {
    private int code;
    private String name;
    private List<City> children;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getChildren() {
        return children;
    }

    public void setChildren(List<City> children) {
        this.children = children;
    }
}
