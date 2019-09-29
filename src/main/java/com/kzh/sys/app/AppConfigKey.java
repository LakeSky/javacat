package com.kzh.sys.app;

/**
 * Created by gang on 2019/5/15.
 */
public enum AppConfigKey {
    domain_server_ip("域服务器IP"),
    domain_postfix("域后缀"),

    ;

    private String name;

    AppConfigKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
