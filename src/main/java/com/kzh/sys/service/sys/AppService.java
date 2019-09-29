package com.kzh.sys.service.sys;

import com.kzh.sys.app.utils.AppUtils;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.model.BaseEntity;
import com.kzh.sys.util.CollectionUtil;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gang on 2017/6/8.
 */
@Service
@Transactional
public class AppService {
    //实体类的搜索路径
    public final static String class_pattern = "classpath*:com/kzh/**/*.class";
    public final static String api_controller_pattern = "classpath*:/com/kzh/api/controller/**/*.class";

    public static List<Class> enums = new ArrayList<>();
    public static List<Class> controllers = new ArrayList<>();
    public static List<Class> entities = new ArrayList<>();

    @Resource
    private UserDao userDao;

    public Map<String, List<String>> statisticTable() {
        String sql = "select table_name,table_rows from information_schema.tables\n" +
                "where TABLE_SCHEMA = 'javacat'\n" +
                "order by table_rows asc;";
        Query sqlQuery = userDao.createSQLQuery(sql);
        List results = sqlQuery.getResultList();

        List<String> xAxis = new ArrayList<>();
        List<String> yAxis = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(results)) {
            for (Object oArr : results) {
                Object[] arr = (Object[]) oArr;
                xAxis.add(arr[0].toString());
                yAxis.add(arr[1].toString());
            }
        }
        Map<String, List<String>> dataMap = new HashMap<>();
        dataMap.put("x", xAxis);
        dataMap.put("y", yAxis);
        return dataMap;
    }

    public static void initCache() {
        List<Class> classes = getAllClasses(class_pattern);
        for (Class aClass : classes) {
            RequestMapping annotation = (RequestMapping) aClass.getAnnotation(RequestMapping.class);
            if (annotation != null) {
                controllers.add(aClass);
            } else if (aClass.isInstance(BaseEntity.class)) {
                entities.add(aClass);
            } else if (aClass.isEnum()) {
                enums.add(aClass);
            }
        }
    }

    public static List<Class> getAllClasses(String searchPath) {
        List<Class> classes = new ArrayList<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String classpath = AppUtils.class.getResource("/").toString();
        try {
            org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(searchPath);
            for (int i = 0; i < resources.length; i++) {
                String source = resources[i].getURL().toString();
                int index=source.indexOf("com");
                String str = source.substring(index, source.length() - 6).replace("/", ".");
                try {
                    classes.add(Class.forName(str));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public static List<Class> getEnums() {
        if (CollectionUtil.isNotEmpty(enums)) {
            initCache();
        }
        return enums;
    }

    public static List<Class> getControllers() {
        if (CollectionUtil.isEmpty(controllers)) {
            initCache();
        }
        return controllers;
    }

    public static List<Class> getEntities() {
        if (CollectionUtil.isEmpty(entities)) {
            initCache();
        }
        return entities;
    }

}
