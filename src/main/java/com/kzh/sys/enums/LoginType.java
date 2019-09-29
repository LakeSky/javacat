package com.kzh.sys.enums;

/**
 * Created by gang on 2017/11/28.
 */
public enum LoginType {
    USERNAME("用户名"), PHONE("手机号"), EMAIL("邮箱"), QQ("QQ"), WEIXIN("微信"),;

    private String name;

    LoginType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
