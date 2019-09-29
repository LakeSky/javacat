package com.kzh.sys.pojo.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by gang on 2018/2/1.
 */
//接口返回值
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiResParameter {
    private String name;
    private String nick;
    private String type;
    private String desc;
    private Boolean space = false;
}
