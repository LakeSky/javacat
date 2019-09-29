package com.kzh.sys.service.generate;

import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.core.dao.SqlBuildUtil;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.service.generate.auto.Action;
import com.kzh.sys.service.generate.auto.QField;
import com.kzh.sys.service.generate.auto.QFieldInfo;
import com.kzh.sys.service.generate.auto.QFieldQueryType;
import com.kzh.sys.service.sys.AppService;
import com.kzh.sys.util.Excel;
import com.kzh.sys.util.SysUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FieldService {
    private static final Logger logger = Logger.getLogger(FieldService.class);

    @javax.annotation.Resource
    private UserDao userDao;

    public static List<QFieldInfo> getAllFieldInfo(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<QFieldInfo> qFieldInfoAll = new ArrayList<>();
        for (Field field : fields) {
            QField qField = field.getAnnotation(QField.class);
            if (qField != null) {
                Map<String, String> enumMap = FieldService.getEnumValueMap();
                String enumValue = enumMap.get(field.getType().getSimpleName());
                QFieldInfo fieldInfo = new QFieldInfo();
                fieldInfo.fromQField(field, qField, enumValue);
                qFieldInfoAll.add(fieldInfo);
            }
        }
        return qFieldInfoAll;
    }

    public static Map<String, List<QFieldInfo>> getFieldMap(Class clazz) {
        Map<String, List<QFieldInfo>> qFieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        List<QFieldInfo> qFieldInfoAll = new ArrayList<>();
        for (Field field : fields) {
            QField qField = field.getAnnotation(QField.class);
            if (qField != null) {
                Action[] actions = qField.actions();
                QFieldInfo fieldInfo = new QFieldInfo();
                Map<String, String> enumMap = FieldService.getEnumValueMap();
                String enumValue = enumMap.get(field.getType().getSimpleName());
                fieldInfo.fromQField(field, qField, enumValue);
                for (Action action : actions) {
                    List<QFieldInfo> qFields = qFieldMap.get(action.name());
                    if (qFields == null) {
                        qFields = new ArrayList<>();
                        qFieldMap.put(action.name(), qFields);
                    }
                    qFields.add(fieldInfo);
                }
                qFieldInfoAll.add(fieldInfo);
            }
        }
        qFieldMap.put("all", qFieldInfoAll);
        List<QFieldInfo> editFields = qFieldMap.get(Action.edit.name());
        if (editFields == null) {
            editFields = new ArrayList<>();
        }
        return qFieldMap;
    }

    public static Map<String, String> getEnumValueMap() {
        Map<String, String> map = new HashMap<>();
        List<Class> classes = AppService.enums;
        for (Class model : classes) {
            //这里是获得枚举的遍历方法  即:枚举对象.values();一样      
            Method method = null;
            try {
                method = model.getMethod("values");
                //这里获取所有的枚举对象 method.invoke(null)也可写成method.invoke(null,null)
                Object inter[] = (Object[]) method.invoke(null);
                String desc = "";
                for (Object enumMessage : inter) {
                    Method name = model.getMethod("name");
                    Object nameVal = name.invoke(enumMessage, null);
                    Method getName = model.getMethod("getName");
                    Object getNameVal = getName.invoke(enumMessage, null);
                    desc += "'" + nameVal + "' : '" + getNameVal + "',";
                }
                if (SysUtil.isNotEmpty(desc)) {
                    map.put(model.getSimpleName(), "{" + desc.substring(0, desc.length() - 1) + "}");
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        return map;
    }

    public List queryByHql(Class clazz, String params) {
        String hql = "from " + clazz.getSimpleName() + " o where 1=1";
        List<SimpleExpression> expressions = new ArrayList<SimpleExpression>();
        if (StringUtils.isNotEmpty(params)) {
            JSONObject jsonObject = JSONObject.fromObject(params);
            for (Object o : jsonObject.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                if (entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())) {
                    String queryField = (String) entry.getKey();
                    if (queryField.endsWith("_start")) {
                        String field = queryField.replace("_start", "");
                        expressions.add(Restrictions.gte(field, entry.getValue(), true));
                    } else if (queryField.endsWith("_end")) {
                        String field = queryField.replace("_end", "");
                        expressions.add(Restrictions.lte(field, entry.getValue(), true));
                    } else {
                        if (StringUtils.isNotEmpty(queryField)) {
                            try {
                                Field field = clazz.getDeclaredField(queryField);
                                QField qField = field.getAnnotation(QField.class);
                                if (QFieldQueryType.eq.equals(qField.queryType())) {
                                    expressions.add(Restrictions.eq(queryField, entry.getValue(), true));
                                } else if (QFieldQueryType.like.equals(qField.queryType())) {
                                    expressions.add(Restrictions.like(queryField, (String) entry.getValue(), true));
                                }
                            } catch (Exception e) {
                                logger.error(e);
                            }
                        }
                    }
                }
            }
        }
        String whereHql = SqlBuildUtil.buildWhereQuery(expressions);
        return userDao.findByHql(hql + whereHql);
    }

    public Workbook export(String params, Class clazz) throws Exception {
        List<String[]> excel = new ArrayList<>();
        List<String> excelHeadList = new ArrayList<>();
        List<String> fieldList = new ArrayList<>();
        Map<String, List<QFieldInfo>> qFieldMap = getFieldMap(clazz);
        List<QFieldInfo> qFieldInfos = qFieldMap.get(Action.export.name());
        Map<String, Method> methodMap = new HashMap<>();
        for (QFieldInfo fieldInfo : qFieldInfos) {
            excelHeadList.add(fieldInfo.getNickname());
            fieldList.add(fieldInfo.getName());
            Method method = clazz.getDeclaredMethod("get" + StringUtils.capitalize(fieldInfo.getName()), null);
            methodMap.put(fieldInfo.getName(), method);
        }
        String[] head = new String[excelHeadList.size()];
        excelHeadList.toArray(head);
        excel.add(head);
        List exchangedList = queryByHql(clazz, params);
        for (int i = 0; i < exchangedList.size(); i++) {
            String[] content = new String[head.length];
            Object obj = exchangedList.get(i);
            for (int j = 0; j < fieldList.size(); j++) {
                String field = fieldList.get(j);
                Method method = methodMap.get(field);
                content[j] = String.valueOf(method.invoke(obj, null));
            }
            excel.add(content);
        }
        Workbook workbook = Excel.simpleExportExcel(excel);
        return workbook;
    }
}
