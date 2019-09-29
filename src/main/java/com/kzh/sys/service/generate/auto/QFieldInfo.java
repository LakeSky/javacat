package com.kzh.sys.service.generate.auto;


import com.kzh.sys.util.SysUtil;
import lombok.Data;
import net.sf.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class QFieldInfo {
    private String name;
    private String nickname;
    private Integer length;
    private QFieldType type;
    private String javaFiledType;
    private Boolean nullable = true;
    private String validType;
    private String format;
    private Action[] actions;
    private List<String> exportList = new ArrayList<>();
    private Map<String, String> dictMap = new HashMap<>();
    private Map map;

    public void fromQField(Field field, QField qField) {
        this.name = field.getName();
        this.nickname = qField.name();
        this.type = qField.type();
        this.nullable = qField.nullable();
        this.setJavaFiledType(field.getType().getSimpleName());
        switch (qField.type()) {
            case text:
                break;
            case textarea:
                break;
            case date:
                this.format = qField.format();
                break;
            case dict:
                if (QFieldDictType.common.equals(qField.dictType())) {
                    JSONObject jsonObject = JSONObject.fromObject(qField.dictValues());
                    for (Object o : jsonObject.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        dictMap.put((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                if (QFieldDictType.dynamic.equals(qField.dictType())) {
                    //todo:
                }
                break;
        }

    }

    public void fromQField(Field field, QField qField, String enumValue) {
        this.name = field.getName();
        this.nickname = qField.name();
        this.type = qField.type();
        this.nullable = qField.nullable();
        this.setJavaFiledType(field.getType().getSimpleName());
        switch (qField.type()) {
            case text:
                break;
            case textarea:
                break;
            case date:
                this.format = qField.format();
                break;
            case dict:
                if (QFieldDictType.common.equals(qField.dictType())) {
                    JSONObject jsonObject = JSONObject.fromObject(qField.dictValues());
                    for (Object o : jsonObject.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        dictMap.put((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                if (QFieldDictType.dynamic.equals(qField.dictType())) {
                    //todo:
                }
                break;
        }
        if (SysUtil.isNotEmpty(enumValue)) {
            this.setType(QFieldType.dict);
            JSONObject jsonObject = JSONObject.fromObject(enumValue);
            for (Object o : jsonObject.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                dictMap.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    public void fromField(Field field) {
        this.name = field.getName();
        this.nickname = field.getName();
        this.setJavaFiledType(field.getType().getSimpleName());
    }

    //---------------get/set------------------------------------------------
}
