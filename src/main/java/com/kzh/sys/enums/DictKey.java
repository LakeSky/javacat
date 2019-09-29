package com.kzh.sys.enums;

/**
 * Created by gang on 2018/4/10.
 */
public enum DictKey {
    AGE("年龄");

    private String name;

    DictKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
