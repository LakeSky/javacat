package com.kzh.sys.enums;

/**
 * Created by gang on 2017/4/10.
 */
public enum ImportStatus {
    WAIT("待导入"), DOING("导入中"), SUCCESS("导入完成"), FAIL("导入失败");

    private String name;

    ImportStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
