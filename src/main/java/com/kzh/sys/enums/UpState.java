package com.kzh.sys.enums;

/**
 * Created by gang on 2018/4/10.
 */
public enum UpState {
    ON("启用"), OFF("停用");

    private String name;

    UpState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
