package com.kzh.sys.enums;

/**
 * Created by IntelliJ IDEA.
 */
public enum YesNo {
    YES("是"), NO("否");

    private YesNo(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
