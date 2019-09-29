package com.kzh.sys.enums;

/**
 * Created by IntelliJ IDEA.
 */
public enum SysLogAction {
    login("登录");

    private SysLogAction(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
