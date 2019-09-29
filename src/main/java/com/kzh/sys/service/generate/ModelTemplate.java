package com.kzh.sys.service.generate;

import com.kzh.sys.service.generate.auto.QFieldInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by gang.wang on 2016/4/6.
 */
@Data
public class ModelTemplate {
    private String namespace;
    private String fullClassName;
    private String className;
    private String classNameLowerCase;
    private String classZhName;//中文名字
    private Map<String, List<QFieldInfo>> fieldMap;
}
