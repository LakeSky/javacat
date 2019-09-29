package com.kzh.sys.pojo;

import lombok.Data;

/**
 * Created by gang on 2019/7/25.
 */
@Data
public class NameValue {
    private String name;
    private Object value;

    public NameValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}