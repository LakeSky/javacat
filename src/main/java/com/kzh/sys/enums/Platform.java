package com.kzh.sys.enums;

/**
 * Created by gang on 2017/4/10.
 */
public enum Platform {
    BACK_ADMIN("总后台"), MOBILE("移动端"), XCX("小程序");

    private String name;

    Platform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
