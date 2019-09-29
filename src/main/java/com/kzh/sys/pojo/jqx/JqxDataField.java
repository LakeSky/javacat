package com.kzh.sys.pojo.jqx;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gang on 2019/7/10.
 */
@Data
public class JqxDataField {
    public static Map<String, String> map = new HashMap<>();

    static {
        map.put("String", "string");
        map.put("Integer", "int");
        map.put("Float", "float");
        map.put("Double", "float");
        map.put("File", "string");
        map.put(Boolean.class.toString(), "bool");
    }

    private String name;
    private String type;
}
