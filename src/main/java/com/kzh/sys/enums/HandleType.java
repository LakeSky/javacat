package com.kzh.sys.enums;

/**
 * Created by gang on 2018/4/10.
 */
public enum HandleType {
    auto("自动"), manual("手动");

    private String name;

    HandleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
