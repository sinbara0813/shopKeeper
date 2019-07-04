package com.d2cmall.shopkeeper.common;

import java.util.List;

public class City {
    private int code;
    private String name;
    private List<District> children;

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

    public List<District> getChildren() {
        return children;
    }

    public void setChildren(List<District> children) {
        this.children = children;
    }
}
