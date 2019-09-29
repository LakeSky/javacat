package com.kzh.sys.pojo.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by gang on 2018/2/1.
 */
//接口请求参数
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiReqParameter {
    private String name;
    private String nick;
    private String type;
    private String must;
    private String desc;
}
