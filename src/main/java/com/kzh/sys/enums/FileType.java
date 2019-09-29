package com.kzh.sys.enums;

/**
 * Created by gang on 2017/4/10.
 */
public enum FileType {
    PIC("图片"), VIDEO("视频"), DOC("文档");

    private String name;

    FileType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
