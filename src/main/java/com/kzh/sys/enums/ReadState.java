package com.kzh.sys.enums;

/**
 * Created by gang on 2017/4/10.
 */
public enum ReadState {
    YD("已读"), WD("未读");

    private String name;

    ReadState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
